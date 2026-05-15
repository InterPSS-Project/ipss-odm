/*
 * CIMAdapterPhase5Test.java
 *
 * Phase 5 tests: 3-winding transformers, extended equipment.
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.*;
import org.junit.Test;

public class CIMAdapterPhase5Test {

    private static final String TD = "testdata/cim/";

    // MicroGrid T4 BE — has 1 three-winding transformer (BE-TR3_1)
    private static final String MG_BE_EQ = TD + "MicroGrid_T4_BE_EQ_V2.xml";
    private static final String MG_BE_TP = TD + "MicroGrid_T4_BE_TP_V2.xml";
    private static final String MG_BE_SSH = TD + "MicroGrid_T4_BE_SSH_V2.xml";

    // MiniGrid NodeBreaker — has 2 three-winding transformers (T3, T4)
    private static final String MN_EQ = TD + "MiniGrid_NB_EQ_V3.xml";
    private static final String MN_TP = TD + "MiniGrid_NB_TP_V3.xml";
    private static final String MN_SSH = TD + "MiniGrid_NB_SSH_V3.xml";

    // ========== MicroGrid T4 BE — 1 three-winding transformer ==========

    @Test
    public void testMicroGrid_3WXfr_Detection() throws Exception {
        System.out.println("\n=== MicroGrid BE: 3W Transformer Detection ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(MG_BE_EQ));

        CIMModel model = adapter.getCimModel();
        System.out.println("PowerTransformers: " + model.powerTransformers().size());
        System.out.println("TransformerEnds:   " + model.transformerEnds().size());

        // 4 transformers total: 3 two-winding + 1 three-winding
        assertEquals(4, model.powerTransformers().size());
        assertEquals(9, model.transformerEnds().size()); // 3*2 + 1*3 = 9

        System.out.println("MicroGrid 3W detection: PASSED");
    }

    @Test
    public void testMicroGrid_3WXfr_Conversion() throws Exception {
        System.out.println("\n=== MicroGrid BE: 3W Transformer Conversion ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {MG_BE_EQ, MG_BE_TP, MG_BE_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();

        System.out.println("Buses: " + buses);
        System.out.println("Branches: " + branches);

        // Count branch types
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            if (b.getValue() instanceof LineBranchXmlType) lines++;
            else if (b.getValue() instanceof Xfr3WBranchXmlType) xfr3w++;
            else if (b.getValue() instanceof XfrBranchXmlType) xfr2w++;
        }
        System.out.println("Lines: " + lines + ", 2W Xfrs: " + xfr2w + ", 3W Xfrs: " + xfr3w);

        // MicroGrid BE should have:
        // - 3 lines (BE-Line_1,3,5,7 → but BE-Line_1 and 7 are boundary → expect fewer)
        // - 3 two-winding transformers (BE-TR2_1, BE-TR2_2, BE-TR2_3)
        // - 1 three-winding transformer (BE-TR3_1)
        assertEquals("Should have 1 three-winding transformer", 1, xfr3w);
        assertEquals("Should have 3 two-winding transformers", 3, xfr2w);

        // Verify the 3W transformer data
        for (var b : net.getBranchList().getBranch()) {
            if (b.getValue() instanceof Xfr3WBranchXmlType) {
                Xfr3WBranchXmlType xfr3wBranch = (Xfr3WBranchXmlType) b.getValue();
                System.out.println("\n3W Transformer: " + xfr3wBranch.getName());
                System.out.println("  z12: r=" + xfr3wBranch.getZ().getRe() + " x=" + xfr3wBranch.getZ().getIm());
                System.out.println("  z23: r=" + xfr3wBranch.getZ23().getRe() + " x=" + xfr3wBranch.getZ23().getIm());
                System.out.println("  z31: r=" + xfr3wBranch.getZ31().getRe() + " x=" + xfr3wBranch.getZ31().getIm());
                System.out.println("  fromTurnRatio: " + (xfr3wBranch.getFromTurnRatio() != null ? xfr3wBranch.getFromTurnRatio().getValue() : "null"));
                System.out.println("  toTurnRatio: " + (xfr3wBranch.getToTurnRatio() != null ? xfr3wBranch.getToTurnRatio().getValue() : "null"));
                System.out.println("  tertTurnRatio: " + (xfr3wBranch.getTertTurnRatio() != null ? xfr3wBranch.getTertTurnRatio().getValue() : "null"));

                // Verify impedances are non-zero and reasonable
                assertTrue("z12.re should be > 0", xfr3wBranch.getZ().getRe() > 0);
                assertTrue("z12.im should be > 0", xfr3wBranch.getZ().getIm() > 0);
                assertTrue("z23.re should be > 0", xfr3wBranch.getZ23().getRe() > 0);
                assertTrue("z23.im should be > 0", xfr3wBranch.getZ23().getIm() > 0);
                assertTrue("z31.re should be > 0", xfr3wBranch.getZ31().getRe() > 0);
                assertTrue("z31.im should be > 0", xfr3wBranch.getZ31().getIm() > 0);
            }
        }

        System.out.println("MicroGrid 3W conversion: PASSED");
    }

    // ========== MiniGrid — 2 three-winding transformers ==========

    @Test
    public void testMiniGrid_3WXfr_Conversion() throws Exception {
        System.out.println("\n=== MiniGrid NB: 3W Transformer Conversion ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {MN_EQ, MN_TP, MN_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();

        System.out.println("Buses: " + buses);
        System.out.println("Branches: " + branches);

        // Count branch types
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            if (b.getValue() instanceof LineBranchXmlType) lines++;
            else if (b.getValue() instanceof Xfr3WBranchXmlType) xfr3w++;
            else if (b.getValue() instanceof XfrBranchXmlType) xfr2w++;
        }
        System.out.println("Lines: " + lines + ", 2W Xfrs: " + xfr2w + ", 3W Xfrs: " + xfr3w);

        // MiniGrid should have:
        // - 9 ACLineSegments (but some may be boundary → fewer lines)
        // - 4 two-winding transformers
        // - 2 three-winding transformers (T3, T4)
        assertEquals("Should have 2 three-winding transformers", 2, xfr3w);
        assertEquals("Should have 4 two-winding transformers", 4, xfr2w);

        // Print 3W transformer details
        for (var b : net.getBranchList().getBranch()) {
            if (b.getValue() instanceof Xfr3WBranchXmlType) {
                Xfr3WBranchXmlType xfr3wBranch = (Xfr3WBranchXmlType) b.getValue();
                System.out.println("\n3W Transformer: " + xfr3wBranch.getName());
                System.out.println("  z12: r=" + xfr3wBranch.getZ().getRe() + " x=" + xfr3wBranch.getZ().getIm());
                System.out.println("  z23: r=" + xfr3wBranch.getZ23().getRe() + " x=" + xfr3wBranch.getZ23().getIm());
                System.out.println("  z31: r=" + xfr3wBranch.getZ31().getRe() + " x=" + xfr3wBranch.getZ31().getIm());
            }
        }

        System.out.println("MiniGrid 3W conversion: PASSED");
    }
}
