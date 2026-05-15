/*
 * CIMGeneratorMapper.java
 *
 * Maps CIM SynchronousMachine + GeneratingUnit → ODM GenData on bus.
 */

package org.ieee.odm.adapter.cim.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM SynchronousMachine to ODM generator data.
 * Generator type (SWING/PV/PQ) is inferred from:
 * - SynchronousMachine.type (Generator or Motor or null)
 * - GeneratingUnit data
 * - Regulating control (target voltage)
 */
public class CIMGeneratorMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMGeneratorMapper.class);

    private double baseMVA;
    private Map<String, CIMPropertyBag> genUnitById = new HashMap<>();

    public CIMGeneratorMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    /**
     * Index generating units by their ID for lookup.
     */
    public void indexGeneratingUnits(List<CIMPropertyBag> genUnits) {
        for (CIMPropertyBag gu : genUnits) {
            genUnitById.put(gu.getId(), gu);
        }
    }

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String genId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = genId;

        // Resolve bus
        String busId = resolveBusId(bag.getId());
        if (busId == null) {
            log.warn("Skipping generator {} - cannot resolve bus", name);
            return;
        }

        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus == null) {
            log.warn("Skipping generator {} - bus {} not found", name, busId);
            return;
        }

        // Determine generator type
        LFGenCodeEnumType genCode = LFGenCodeEnumType.PQ;

        // Check SynchronousMachine.type (RDF resource reference)
        String machineTypeUri = bag.getResourceId("SynchronousMachine.type");
        boolean isGenerator = machineTypeUri != null && machineTypeUri.contains("generator");
        // Also check operatingMode from SSH
        String operatingModeUri = bag.getResourceId("SynchronousMachine.operatingMode");
        if (operatingModeUri != null && operatingModeUri.contains("generator")) {
            isGenerator = true;
        }

        // Get P from generating unit reference
        double p = 0.0;
        double q = 0.0;
        double targetV = 0.0;
        String genUnitRef = bag.getResourceId("RotatingMachine.GeneratingUnit");
        if (genUnitRef != null) {
            CIMPropertyBag gu = genUnitById.get(genUnitRef);
            if (gu != null) {
                p = gu.getDouble("GeneratingUnit.initialP", 0.0);
                double minP = gu.getDouble("GeneratingUnit.minOperatingP", 0.0);
                double maxP = gu.getDouble("GeneratingUnit.maxOperatingP", 0.0);
                log.debug("Generator {} genUnit: initialP={}, minP={}, maxP={}", name, p, minP, maxP);
            }
        }

        // Check for regulating control (PV generator with voltage target)
        String regControl = bag.getResourceId("RegulatingCondEq.RegulatingControl");
        boolean hasRegulating = regControl != null;

        // Heuristic: if the machine is a generator with regulating control, it's PV or SWING
        if (hasRegulating && isGenerator) {
            genCode = LFGenCodeEnumType.PV;
            // Try to get target voltage from regulating control
            if (cimModel != null && regControl != null) {
                CIMPropertyBag rc = cimModel.getResource(regControl);
                if (rc != null) {
                    targetV = rc.getDouble("RegulatingControl.targetValue", 0.0);
                }
            }
        }

        // If p is negative in CIM convention, make it positive for generation
        if (p < 0) p = -p;

        // Get rated S for voltage target conversion
        double ratedS = bag.getDouble("SynchronousMachine.ratedS", baseMVA);

        // Set generator data
        double targetVPU = targetV;
        if (targetV > 0 && cimModel != null) {
            // targetV is in kV, convert to PU
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                Double baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
                if (baseKV != null && baseKV > 0) {
                    targetVPU = targetV / baseKV;
                }
            }
        } else if (targetV == 0) {
            targetVPU = 1.0; // default
        }

        if (genCode == LFGenCodeEnumType.PV) {
            AclfDataSetter.setGenData(bus, genCode, targetVPU, VoltageUnitType.PU,
                p, q, ApparentPowerUnitType.MVA);
        } else {
            AclfDataSetter.setGenData(bus, genCode, targetVPU, VoltageUnitType.PU,
                p, q, ApparentPowerUnitType.MVA);
        }

        log.info(String.format("Created generator: %s on bus %s, type=%s, P=%.2f MW, targetV=%.4f",
            name, busId, genCode, p, targetVPU));
    }

    /**
     * Designate the first generator as SWING bus.
     * Call this after all generators are mapped if no SWING bus was found.
     */
    public void designateSwingBus(AclfModelParser parser, String busId) {
        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus != null && bus.getGenData() != null) {
            bus.getGenData().setCode(LFGenCodeEnumType.SWING);
            log.info("Designated bus {} as SWING", busId);
        }
    }

    /**
     * Map ExternalNetworkInjection to a generator (SWING bus).
     * Per PowSyBl, ExternalNetworkInjections become generators.
     */
    public void mapExternalNetworkInjection(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String eniId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = eniId;

        // Resolve bus
        String busId = resolveBusId(bag.getId());
        if (busId == null) {
            log.warn("Skipping ExternalNetworkInjection {} - cannot resolve bus", name);
            return;
        }

        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus == null) {
            log.warn("Skipping ExternalNetworkInjection {} - bus {} not found", name, busId);
            return;
        }

        // ExternalNetworkInjections are typically SWING buses (equivalent of infinite bus)
        double targetV = 1.0; // default
        double p = 0.0;
        double q = 0.0;

        // Try to get regulating control target voltage
        String regControl = bag.getResourceId("RegulatingCondEq.RegulatingControl");
        if (regControl != null && cimModel != null) {
            CIMPropertyBag rc = cimModel.getResource(regControl);
            if (rc != null) {
                targetV = rc.getDouble("RegulatingControl.targetValue", 0.0);
            }
        }

        // Convert target voltage to PU if possible
        double targetVPU = targetV;
        if (targetV > 0 && cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                Double baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
                if (baseKV != null && baseKV > 0) {
                    targetVPU = targetV / baseKV;
                }
            }
        } else if (targetV == 0) {
            targetVPU = 1.0;
        }

        // Set as SWING generator
        AclfDataSetter.setGenData(bus, LFGenCodeEnumType.SWING, targetVPU, VoltageUnitType.PU,
            p, q, ApparentPowerUnitType.MVA);

        log.info(String.format("Created ExternalNetworkInjection generator: %s on bus %s, SWING, targetV=%.4f",
            name, busId, targetVPU));
    }
}
