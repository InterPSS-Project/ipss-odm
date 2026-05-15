/*
 * CIMTransformer3WMapper.java
 *
 * Maps CIM PowerTransformer with 3 PowerTransformerEnd → ODM Xfr3WBranchXmlType.
 *
 * CIM 3-winding transformer model:
 *   End1 (typically HV), End2 (MV), End3 (LV)
 *   Each end has ratedU, r, x (Ohms on winding side)
 *   Connected to 3 different TopologicalNodes (buses)
 *
 * ODM Xfr3WBranchXmlType model:
 *   Extends XfrBranchXmlType (from_bus, to_bus, z, fromTurnRatio, toTurnRatio)
 *   Adds: tertBus, z23, z31, tertTurnRatio
 *   Uses star-bus equivalent: z12 on base branch, z23 and z31 as additional legs
 *
 * Conversion approach (matches PowSyBl / standard star-bus model):
 *   - Convert winding impedances to per-unit on a common base (ratedU0 = ratedU of end1)
 *   - z12 = r1+jx1 + r2+jx2 (series combination of end1 and end2)
 *   - z23 = r2+jx2 + r3+jx3
 *   - z31 = r3+jx3 + r1+jx1
 *   - Turn ratios relative to nominal voltages
 */

package org.ieee.odm.adapter.cim.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.Xfr3WBranchXmlType;
import org.ieee.odm.schema.ZUnitType;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM 3-winding PowerTransformer → ODM Xfr3WBranchXmlType.
 * Uses the standard star-bus equivalent impedance model.
 */
public class CIMTransformer3WMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMTransformer3WMapper.class);

    private double baseMVA;

    public CIMTransformer3WMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    /**
     * Map a 3-winding transformer.
     * @param bag The PowerTransformer property bag
     * @param sortedEnds The 3 PowerTransformerEnd bags, sorted by endNumber (1, 2, 3)
     * @param parser The ODM parser
     */
    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        // Should not be called directly — use map3W instead
        throw new UnsupportedOperationException("Use map3W for 3-winding transformers");
    }

    /**
     * Map a 3-winding transformer.
     * @param bag The PowerTransformer property bag
     * @param sortedEnds The 3 PowerTransformerEnd bags, sorted by endNumber (1, 2, 3)
     * @param parser The ODM parser
     */
    public void map3W(CIMPropertyBag bag, List<CIMPropertyBag> sortedEnds, AclfModelParser parser) throws Exception {
        String xfrId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = xfrId;

        if (sortedEnds.size() != 3) {
            log.warn("3W transformer {} has {} ends, expected 3 — skipping", name, sortedEnds.size());
            return;
        }

        CIMPropertyBag end1 = sortedEnds.get(0);
        CIMPropertyBag end2 = sortedEnds.get(1);
        CIMPropertyBag end3 = sortedEnds.get(2);

        // Get rated voltages for each winding
        double ratedU1 = getRatedU(end1);
        double ratedU2 = getRatedU(end2);
        double ratedU3 = getRatedU(end3);

        // Get winding impedances (Ohms, referenced to the winding side)
        double r1 = getR(end1);
        double x1 = getX(end1);
        double r2 = getR(end2);
        double x2 = getX(end2);
        double r3 = getR(end3);
        double x3 = getX(end3);

        // Reference base voltage: use ratedU of end 1 (highest voltage / primary)
        double ratedU0 = ratedU1;

        // Convert winding impedances to per-unit on system base
        // Z_pu = Z_ohm * baseMVA / (ratedU^2)  — referenced to the end's rated voltage
        // Then refer to the common base ratedU0:
        // Z_pu_on_U0 = Z_pu * (ratedU / ratedU0)^2  for each winding
        double z1_pu_r = r1 * baseMVA / (ratedU1 * ratedU1);  // already on end1 base = ratedU0
        double z1_pu_x = x1 * baseMVA / (ratedU1 * ratedU1);
        double z2_pu_r = r2 * baseMVA / (ratedU2 * ratedU2) * (ratedU2 * ratedU2) / (ratedU0 * ratedU0);
        double z2_pu_x = x2 * baseMVA / (ratedU2 * ratedU2) * (ratedU2 * ratedU2) / (ratedU0 * ratedU0);
        double z3_pu_r = r3 * baseMVA / (ratedU3 * ratedU3) * (ratedU3 * ratedU3) / (ratedU0 * ratedU0);
        double z3_pu_x = x3 * baseMVA / (ratedU3 * ratedU3) * (ratedU3 * ratedU3) / (ratedU0 * ratedU0);

        // Simplify: z_pu_on_U0 = r * baseMVA / (ratedU0^2) for each winding
        // Because: r * baseMVA / (ratedU^2) * (ratedU/ratedU0)^2 = r * baseMVA / ratedU0^2
        z1_pu_r = r1 * baseMVA / (ratedU0 * ratedU0);
        z1_pu_x = x1 * baseMVA / (ratedU0 * ratedU0);
        z2_pu_r = r2 * baseMVA / (ratedU0 * ratedU0);
        z2_pu_x = x2 * baseMVA / (ratedU0 * ratedU0);
        z3_pu_r = r3 * baseMVA / (ratedU0 * ratedU0);
        z3_pu_x = x3 * baseMVA / (ratedU0 * ratedU0);

        // Star-bus equivalent impedances:
        // z12 = z1 + z2, z23 = z2 + z3, z31 = z3 + z1
        double z12_r = z1_pu_r + z2_pu_r;
        double z12_x = z1_pu_x + z2_pu_x;
        double z23_r = z2_pu_r + z3_pu_r;
        double z23_x = z2_pu_x + z3_pu_x;
        double z31_r = z3_pu_r + z1_pu_r;
        double z31_x = z3_pu_x + z1_pu_x;

        // Resolve bus connectivity for all 3 terminals
        String[] busIds = resolveBranchBusIds(bag.getId());
        String bus1Id = busIds[0]; // end1 bus (from)
        String bus2Id = busIds[1]; // end2 bus (to)

        // Resolve bus3 from the third terminal
        String bus3Id = resolveBusIdForEnd(bag.getId(), 3, sortedEnds);

        if (bus1Id == null || bus2Id == null || bus3Id == null) {
            log.warn("Skipping 3W transformer {} - cannot resolve all buses (bus1={}, bus2={}, bus3={})",
                name, bus1Id, bus2Id, bus3Id);
            return;
        }

        // Turn ratios
        // fromTurnRatio: ratio at bus1 side = ratedU1 / nominalV_bus1
        // toTurnRatio: ratio at bus2 side = ratedU2 / nominalV_bus2 * (nominalV_bus1 / ratedU1)
        // tertTurnRatio: ratio at bus3 side = ratedU3 / nominalV_bus3 * (nominalV_bus1 / ratedU1)
        Double nominalV1 = getNominalVForBus(bus1Id);
        Double nominalV2 = getNominalVForBus(bus2Id);
        Double nominalV3 = getNominalVForBus(bus3Id);

        if (nominalV1 == null) nominalV1 = ratedU1;
        if (nominalV2 == null) nominalV2 = ratedU2;
        if (nominalV3 == null) nominalV3 = ratedU3;

        double fromTurnRatio = ratedU1 / nominalV1;
        double toTurnRatio = (ratedU2 / nominalV2) * (nominalV1 / ratedU1);
        double tertTurnRatio = (ratedU3 / nominalV3) * (nominalV1 / ratedU1);

        // Simplify if nominal voltages match rated voltages
        if (Math.abs(ratedU1 - nominalV1) < 0.01 &&
            Math.abs(ratedU2 - nominalV2) < 0.01 &&
            Math.abs(ratedU3 - nominalV3) < 0.01) {
            fromTurnRatio = 1.0;
            toTurnRatio = 1.0;
            tertTurnRatio = 1.0;
        }

        log.debug("3W Transformer {}: bus1={}, bus2={}, bus3={}, ratedU={}/{}/{}, z12={:.6f}+j{:.6f}, z23={:.6f}+j{:.6f}, z31={:.6f}+j{:.6f}",
            name, bus1Id, bus2Id, bus3Id, ratedU1, ratedU2, ratedU3,
            z12_r, z12_x, z23_r, z23_x, z31_r, z31_x);

        // Create ODM 3-winding transformer branch
        String cirId = "1";
        Xfr3WBranchXmlType branch = null;
        for (int ci = 1; ci <= 10; ci++) {
            cirId = String.valueOf(ci);
            try {
                branch = parser.createXfr3WBranch(bus1Id, bus2Id, bus3Id, cirId);
                break;
            } catch (org.ieee.odm.common.ODMBranchDuplicationException e) {
                // parallel, try next circuit ID
            }
        }
        if (branch == null) {
            log.warn("Skipping 3W transformer {} - too many parallel circuits", name);
            return;
        }
        branch.setId(xfrId);
        branch.setName(name);

        // Set z12 on the base branch (from XfrBranchXmlType)
        branch.setZ(BaseDataSetter.createZValue(z12_r, z12_x, ZUnitType.PU));
        branch.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
        branch.getXfrInfo().setDataOnSystemBase(true);
        branch.setFromTurnRatio(
            BaseDataSetter.createTurnRatioPU(fromTurnRatio));
        branch.setToTurnRatio(
            BaseDataSetter.createTurnRatioPU(toTurnRatio));

        // Set z23 and z31 for the 3rd winding
        branch.setZ23(BaseDataSetter.createZValue(z23_r, z23_x, ZUnitType.PU));
        branch.setZ31(BaseDataSetter.createZValue(z31_r, z31_x, ZUnitType.PU));
        branch.setTertTurnRatio(
            BaseDataSetter.createTurnRatioPU(tertTurnRatio));

        log.info("Created 3W xfr branch: {} ({}→{}→{}) ratedU={:.1f}/{:.1f}/{:.1f} z12={:.6f}+j{:.6f} PU",
            name, bus1Id, bus2Id, bus3Id, ratedU1, ratedU2, ratedU3, z12_r, z12_x);
    }

    private double getRatedU(CIMPropertyBag end) {
        return end.getDouble("PowerTransformerEnd.ratedU",
                end.getDouble("TransformerEnd.ratedU", 0.0));
    }

    private double getR(CIMPropertyBag end) {
        return end.getDouble("PowerTransformerEnd.r",
                end.getDouble("TransformerEnd.r", 0.0));
    }

    private double getX(CIMPropertyBag end) {
        return end.getDouble("PowerTransformerEnd.x",
                end.getDouble("TransformerEnd.x", 0.0));
    }

    /**
     * Resolve the bus ID for a specific end of a 3-winding transformer.
     * Uses the CIMModel terminal topology resolution.
     */
    private String resolveBusIdForEnd(String xfrId, int endNumber, List<CIMPropertyBag> ends) {
        if (cimModel == null) return null;

        // Try to get the bus for this specific end via its terminal
        // The ends are sorted 1,2,3 so index = endNumber-1
        int idx = endNumber - 1;
        if (idx >= ends.size()) return null;

        // Get all topological nodes for this transformer
        java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(xfrId);
        if (topoNodes.size() >= endNumber) {
            String busId = cimModel.getBusId(topoNodes.get(idx));
            if (busId != null) return busId;
            return CIMPropertyBag.extractLocal(topoNodes.get(idx));
        }
        return null;
    }

    private Double getNominalVForBus(String busId) {
        if (cimModel == null) return null;
        return cimModel.getNominalVoltageForTopoNode(busId);
    }
}
