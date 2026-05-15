/*
 * CIMVoltageLevelMapper.java
 *
 * Maps CIM VoltageLevel → ODM bus base voltage information.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM VoltageLevel elements.
 * Voltage levels define the nominal voltage for a group of buses.
 */
public class CIMVoltageLevelMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMVoltageLevelMapper.class);

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String vlId = bag.getLocalId();
        String name = bag.getName();
        double nominalV = bag.getDouble("VoltageLevel.nominalVoltage");
        log.debug("Mapping VoltageLevel: {} ({}), nominalV={} kV", name, vlId, nominalV);
        // Voltage level info is used during bus creation for base voltage lookup
    }
}
