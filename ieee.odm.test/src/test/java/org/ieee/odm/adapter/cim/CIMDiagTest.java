package org.ieee.odm.adapter.cim;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.*;
import org.junit.Test;

public class CIMDiagTest {
    private static final String TD = "testdata/cim/";

    @Test
    public void diagMiniGrid() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, new String[]{
            TD + "MiniGrid_NB_EQ_V3.xml", TD + "MiniGrid_NB_TP_V3.xml", TD + "MiniGrid_NB_SSH_V3.xml"
        });
        CIMModel model = adapter.getCimModel();
        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        System.err.println("PowerTransformers: " + model.powerTransformers().size());
        System.err.println("TransformerEnds: " + model.transformerEnds().size());
        
        // Check grouping
        java.util.Map<String, java.util.List<CIMPropertyBag>> endsByXfr = new java.util.HashMap<>();
        for (CIMPropertyBag end : model.transformerEnds()) {
            String xfrId = end.getResourceId("PowerTransformerEnd.PowerTransformer");
            endsByXfr.computeIfAbsent(xfrId != null ? xfrId : "null", k -> new java.util.ArrayList<>()).add(end);
        }
        for (CIMPropertyBag pt : model.powerTransformers()) {
            String ptId = pt.getId();
            java.util.List<CIMPropertyBag> ends = endsByXfr.get(ptId);
            System.err.println("PT " + pt.getName() + " id=" + ptId + " ends=" + (ends != null ? ends.size() : 0));
        }

        // Count branch types and names
        int idx = 0;
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            var v = b.getValue();
            idx++;
            if (v instanceof Xfr3WBranchXmlType) { xfr3w++; System.err.println("  [" + idx + "] xfr3w: name=" + ((Xfr3WBranchXmlType)v).getName()); }
            else if (v instanceof XfrBranchXmlType) { xfr2w++; System.err.println("  [" + idx + "] xfr2w: name=" + ((XfrBranchXmlType)v).getName()); }
            else if (v instanceof LineBranchXmlType) { lines++; System.err.println("  [" + idx + "] line: name=" + ((LineBranchXmlType)v).getName()); }
            else System.err.println("  [" + idx + "] other: " + v.getClass().getSimpleName());
        }
        System.err.println("Lines: " + lines + ", xfr2w: " + xfr2w + ", xfr3w: " + xfr3w);
        
        // Count injections
        int loads = 0, gens = 0, shunts = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
        }
        System.err.println("Loads: " + loads + ", Gens: " + gens);
    }

    @Test
    public void diagMicroGrid() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, new String[]{
            TD + "MicroGrid_T4_BE_EQ_V2.xml", TD + "MicroGrid_T4_BE_TP_V2.xml", TD + "MicroGrid_T4_BE_SSH_V2.xml"
        });
        CIMModel model = adapter.getCimModel();
        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        // Check all ACLineSegments
        System.err.println("=== ACLineSegments ===");
        for (CIMPropertyBag seg : model.acLineSegments()) {
            String name = seg.getName();
            String[] busIds = new String[2];
            java.util.List<String> tns = model.getTopologicalNodesForEquipment(seg.getId());
            int idx = 0;
            for (String tn : tns) {
                String busId = model.getBusId(tn);
                busIds[idx++] = busId;
            }
            System.err.println("  " + name + ": TNs=" + tns + " busIds=[" + busIds[0] + ", " + busIds[1] + "]");
        }

        // Lines created
        int lines = 0;
        for (var b : net.getBranchList().getBranch()) {
            var v = b.getValue();
            if (v instanceof LineBranchXmlType) {
                lines++;
                LineBranchXmlType lb = (LineBranchXmlType) v;
                System.err.println("  Created line: " + lb.getName());
            }
        }
        System.err.println("Total lines: " + lines);
    }
}
