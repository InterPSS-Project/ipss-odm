/*
 * CIMLineMapper.java
 *
 * Maps CIM ACLineSegment → ODM LineBranch.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.ZUnitType;
import org.ieee.odm.schema.YUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM ACLineSegment to ODM LineBranch.
 * CIM stores R, X in Ohms and Gch, Bch in Siemens (total line charging).
 * ODM uses per-unit on system base.
 */
public class CIMLineMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMLineMapper.class);

    private double baseMVA;

    public CIMLineMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String lineId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = lineId;

        // Get electrical parameters (in physical units)
        double r = bag.getDouble("ACLineSegment.r");
        double x = bag.getDouble("ACLineSegment.x");
        double gch = bag.getDouble("ACLineSegment.gch", 0.0);
        double bch = bag.getDouble("ACLineSegment.bch", 0.0);

        // Resolve bus connectivity
        String[] busIds = resolveBranchBusIds(bag.getId());
        String fromBusId = busIds[0];
        String toBusId = busIds[1];

        if (fromBusId == null || toBusId == null) {
            log.warn("Skipping line {} - cannot resolve bus connectivity (from={}, to={})", name, fromBusId, toBusId);
            return;
        }

        // Determine base voltage for PU conversion
        Double baseKV = null;
        String bvRef = bag.getResourceId("ConductingEquipment.BaseVoltage");
        if (bvRef != null && cimModel != null) {
            baseKV = cimModel.getBaseVoltageValue(bvRef);
        }
        // Fallback: look up from connected bus
        if (baseKV == null && cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
            }
        }
        if (baseKV == null) {
            log.warn("Cannot determine base voltage for line {}, using 100 kV", name);
            baseKV = 100.0;
        }

        // Convert to per-unit
        double baseZ = baseKV * baseKV / baseMVA;
        double baseY = baseMVA / (baseKV * baseKV);
        double rPU = r / baseZ;
        double xPU = x / baseZ;
        double gPU = gch / baseY;
        double bPU = bch / baseY;

        log.debug("Line {}: from={}, to={}, r={:.4f}, x={:.4f}, bch={:.6f}, baseKV={}", 
            name, fromBusId, toBusId, rPU, xPU, bPU, baseKV);

        // Create ODM line branch — increment circuit ID if duplicate exists
        String cirId = "1";
        LineBranchXmlType branch = null;
        for (int ci = 1; ci <= 10; ci++) {
            cirId = String.valueOf(ci);
            try {
                branch = parser.createLineBranch(fromBusId, toBusId, cirId);
                break;
            } catch (org.ieee.odm.common.ODMBranchDuplicationException e) {
                // parallel line, try next circuit ID
            }
        }
        if (branch == null) {
            log.warn("Skipping line {} - too many parallel circuits", name);
            return;
        }
        branch.setId(lineId);
        branch.setName(name);

        // Set impedance and admittance (total shunt Y on system base in PU)
        AclfDataSetter.setLineData(branch, rPU, xPU, ZUnitType.PU, gPU, bPU, YUnitType.PU);

        log.info("Created line branch: {} ({}→{}) r={:.6f} x={:.6f} bch={:.6f} PU",
            name, fromBusId, toBusId, rPU, xPU, bPU);
    }

    /**
     * Map SeriesCompensator as a line (PowSyBl behavior).
     * SeriesCompensator has r, x (impedance) but typically no shunt admittance.
     */
    public void mapSeriesCompensator(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String lineId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = lineId;

        double r = bag.getDouble("SeriesCompensator.r", 0.0);
        double x = bag.getDouble("SeriesCompensator.x", 0.0);

        String[] busIds = resolveBranchBusIds(bag.getId());
        String fromBusId = busIds[0];
        String toBusId = busIds[1];

        if (fromBusId == null || toBusId == null) {
            log.warn("Skipping SeriesCompensator {} - cannot resolve bus (from={}, to={})", name, fromBusId, toBusId);
            return;
        }

        Double baseKV = null;
        String bvRef = bag.getResourceId("ConductingEquipment.BaseVoltage");
        if (bvRef != null && cimModel != null) {
            baseKV = cimModel.getBaseVoltageValue(bvRef);
        }
        if (baseKV == null && cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
            }
        }
        if (baseKV == null) {
            log.warn("Cannot determine base voltage for SeriesCompensator {}, using 100 kV", name);
            baseKV = 100.0;
        }

        double baseZ = baseKV * baseKV / baseMVA;
        double rPU = r / baseZ;
        double xPU = x / baseZ;

        String cirId = "1";
        LineBranchXmlType branch = null;
        for (int ci = 1; ci <= 10; ci++) {
            cirId = String.valueOf(ci);
            try {
                branch = parser.createLineBranch(fromBusId, toBusId, cirId);
                break;
            } catch (org.ieee.odm.common.ODMBranchDuplicationException e) {
                // try next circuit ID
            }
        }
        if (branch == null) {
            log.warn("Skipping SeriesCompensator {} - too many parallel circuits", name);
            return;
        }
        branch.setId(lineId);
        branch.setName(name);
        AclfDataSetter.setLineData(branch, rPU, xPU, ZUnitType.PU, 0.0, 0.0, YUnitType.PU);
        log.info("Created SeriesCompensator as line: {} ({}→{}) r={:.6f} x={:.6f} PU",
            name, fromBusId, toBusId, rPU, xPU);
    }
}
