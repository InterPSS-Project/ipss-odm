/*
 * CIMTransformerMapper.java
 *
 * Maps CIM PowerTransformer + PowerTransformerEnd → ODM XfrBranch.
 */

package org.ieee.odm.adapter.cim.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM PowerTransformer (2-winding) to ODM XfrBranch.
 * CIM represents transformer data via PowerTransformerEnd objects.
 * Each end has ratedU, r, x (in Ohms on the winding side).
 */
public class CIMTransformerMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMTransformerMapper.class);

    private double baseMVA;
    private Map<String, List<CIMPropertyBag>> endsByTransformer = new HashMap<>();

    public CIMTransformerMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    /**
     * Pre-process transformer ends to group by transformer.
     * Must be called before map().
     */
    public void indexEnds(List<CIMPropertyBag> ends) {
        for (CIMPropertyBag end : ends) {
            String xfrId = end.getResourceId("PowerTransformerEnd.PowerTransformer");
            if (xfrId != null) {
                endsByTransformer.computeIfAbsent(xfrId, k -> new ArrayList<>()).add(end);
            }
        }
        log.debug("Indexed transformer ends: {} transformers", endsByTransformer.size());
    }

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String xfrId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = xfrId;

        log.info("CIMTransformerMapper.map() called for: {} (id={})", name, bag.getId());

        List<CIMPropertyBag> ends = endsByTransformer.get(bag.getId());
        if (ends == null || ends.size() < 2) {
            log.warn("Skipping transformer {} - insufficient ends ({})", name, 
                ends == null ? 0 : ends.size());
            return;
        }

        // For 2-winding: sort by end number
        ends.sort((a, b) -> {
            int ea = a.getInt("TransformerEnd.endNumber", 
                a.getInt("PowerTransformerEnd.endNumber", 1));
            int eb = b.getInt("TransformerEnd.endNumber",
                b.getInt("PowerTransformerEnd.endNumber", 1));
            return Integer.compare(ea, eb);
        });

        CIMPropertyBag end1 = ends.get(0); // primary (high voltage side typically)
        CIMPropertyBag end2 = ends.get(1); // secondary

        // Get rated voltages
        double ratedU1 = end1.getDouble("PowerTransformerEnd.ratedU",
                            end1.getDouble("TransformerEnd.ratedU", 0.0));
        double ratedU2 = end2.getDouble("PowerTransformerEnd.ratedU",
                            end2.getDouble("TransformerEnd.ratedU", 0.0));

        // Get resistance and reactance from ends (in Ohms)
        // In CIM, r and x on each end are the winding values
        double r1 = end1.getDouble("PowerTransformerEnd.r", end1.getDouble("TransformerEnd.r", 0.0));
        double x1 = end1.getDouble("PowerTransformerEnd.x", end1.getDouble("TransformerEnd.x", 0.0));
        double r2 = end2.getDouble("PowerTransformerEnd.r", end2.getDouble("TransformerEnd.r", 0.0));
        double x2 = end2.getDouble("PowerTransformerEnd.x", end2.getDouble("TransformerEnd.x", 0.0));
        double r = r1 + r2;
        double x = x1 + x2;

        // Rated power
        double ratedS = end1.getDouble("PowerTransformerEnd.ratedS", 
                        end1.getDouble("TransformerEnd.ratedS", baseMVA));

        // Resolve bus connectivity
        String[] busIds = resolveBranchBusIds(bag.getId());
        String fromBusId = busIds[0];
        String toBusId = busIds[1];

        if (fromBusId == null || toBusId == null) {
            log.warn("Skipping transformer {} - cannot resolve buses (from={}, to={})", 
                name, fromBusId, toBusId);
            return;
        }

        // Determine base voltage from buses
        Double baseKV = null;
        if (cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
            }
        }
        if (baseKV == null) baseKV = ratedU1;
        if (baseKV == 0.0) baseKV = 100.0;

        // Convert to per-unit on system base
        double baseZ = baseKV * baseKV / baseMVA;
        double rPU = r / baseZ;
        double xPU = x / baseZ;

        // Compute turn ratios: from side and to side
        // Turn ratio = ratedU / baseKV (on each side)
        // For ODM: fromTurnRatio = ratedU1/baseKV_from, toTurnRatio = ratedU2/baseKV_to
        // Simple model: ratio represents off-nominal tap ratio
        // If baseKV matches rated voltage, ratio = 1.0
        Double baseKV_from = baseKV;
        Double baseKV_to = null;
        if (cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (topoNodes.size() >= 2) {
                baseKV_to = cimModel.getNominalVoltageForTopoNode(topoNodes.get(1));
            }
        }
        if (baseKV_to == null) baseKV_to = ratedU2;

        double fromTurnRatio = ratedU1 / baseKV_from;
        double toTurnRatio = (ratedU2 / baseKV_to) * (baseKV_from / ratedU1);
        // Simplify: if both sides match their nominal, ratio is 1.0
        if (Math.abs(ratedU1 - baseKV_from) < 0.01 && Math.abs(ratedU2 - baseKV_to) < 0.01) {
            fromTurnRatio = 1.0;
            toTurnRatio = 1.0;
        }

        log.debug("Transformer {}: from={}, to={}, ratedU1={}, ratedU2={}, r={:.4f}, x={:.4f}",
            name, fromBusId, toBusId, ratedU1, ratedU2, rPU, xPU);

        // Create ODM transformer branch
        String cirId = "1";
        XfrBranchXmlType branch = null;
        for (int ci = 1; ci <= 10; ci++) {
            cirId = String.valueOf(ci);
            try {
                branch = parser.createXfrBranch(fromBusId, toBusId, cirId);
                break;
            } catch (org.ieee.odm.common.ODMBranchDuplicationException e) {
                // parallel xfr, try next circuit ID
            }
        }
        if (branch == null) {
            log.warn("Skipping transformer {} - too many parallel circuits", name);
            return;
        }
        branch.setId(xfrId);
        branch.setName(name);

        // Set transformer data
        AclfDataSetter.createXformerData(branch, rPU, xPU, ZUnitType.PU, fromTurnRatio, toTurnRatio);

        log.info("Created xfr branch: {} ({}→{}) ratedU1={:.1f} ratedU2={:.1f} r={:.6f} x={:.6f} PU",
            name, fromBusId, toBusId, ratedU1, ratedU2, rPU, xPU);
    }
}
