/*
 * CIMLoadMapper.java
 *
 * Maps CIM EnergyConsumer → ODM LoadData on bus.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM EnergyConsumer to ODM load data on the connected bus.
 * CIM stores P and Q in MW and MVAr.
 */
public class CIMLoadMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMLoadMapper.class);

    private double baseMVA;
    private int mappedCount = 0;

    public CIMLoadMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    public int getMappedCount() { return mappedCount; }

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String loadId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = loadId;

        // Get P and Q (MW, MVAr) — works for EnergyConsumer and AsynchronousMachine
        double p = bag.getDouble("EnergyConsumer.p",
                    bag.getDouble("RotatingMachine.p", 0.0));
        double q = bag.getDouble("EnergyConsumer.q",
                    bag.getDouble("RotatingMachine.q", 0.0));

        // For AsynchronousMachine, PowSyBl always creates a Load even with P=Q=0
        // Check if this is an AsynchronousMachine (no EnergyConsumer properties)
        boolean isAsyncMachine = bag.getString("EnergyConsumer.p") == null;
        
        if (p == 0.0 && q == 0.0 && !isAsyncMachine) {
            log.debug("Skipping zero load: {}", name);
            return;
        }

        // Resolve bus
        String busId = resolveBusId(bag.getId());
        if (busId == null) {
            log.warn("Skipping load {} - cannot resolve bus", name);
            return;
        }

        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus == null) {
            log.warn("Skipping load {} - bus {} not found", name, busId);
            return;
        }

        // Accumulate load if bus already has load data (multiple loads on same bus)
        if (bus.getLoadData() != null) {
            var existingLoads = bus.getLoadData().getContributeLoad();
            if (existingLoads != null && !existingLoads.isEmpty()) {
                for (var el : existingLoads) {
                    var ld = el.getValue();
                    if (ld.getConstPLoad() != null) {
                        double oldP = ld.getConstPLoad().getRe();
                        double oldQ = ld.getConstPLoad().getIm();
                        p += oldP;
                        q += oldQ;
                    }
                }
            }
        }

        // Set load data on bus (constant power load)
        AclfDataSetter.setLoadData(bus, LFLoadCodeEnumType.CONST_P,
            p, q, ApparentPowerUnitType.MVA);

        log.info(String.format("Created load: %s on bus %s, P=%.2f MW, Q=%.2f MVAr", name, busId, p, q));
        mappedCount++;
    }
}
