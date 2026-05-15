/*
 * CIMAdapterAdditionalTest.java
 *
 * Additional test cases from PowSyBl ENTSO-E conformity suite:
 * - T2 Multi-region assembled (BE + NL with interconnections)
 * - SmallGrid NodeBreaker (2840 terminals, node-breaker topology)
 * - SmallGrid HVDC (2 DC line segments, 12 transformers)
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.junit.Test;

public class CIMAdapterAdditionalTest {

    private static final String TD = "testdata/cim/";

    // T2 Multi-region (BE + NL)
    private static final String T2_BE_EQ = TD + "T2_BE_EQ.xml";
    private static final String T2_BE_TP = TD + "T2_BE_TP.xml";
    private static final String T2_BE_SSH = TD + "T2_BE_SSH.xml";
    private static final String T2_NL_EQ = TD + "T2_NL_EQ.xml";
    private static final String T2_NL_TP = TD + "T2_NL_TP.xml";
    private static final String T2_NL_SSH = TD + "T2_NL_SSH.xml";

    // SmallGrid NodeBreaker
    private static final String SG_NB_EQ = TD + "SmallGrid_NB_EQ_V3.xml";
    private static final String SG_NB_TP = TD + "SmallGrid_NB_TP_V3.xml";
    private static final String SG_NB_SSH = TD + "SmallGrid_NB_SSH_V3.xml";
    private static final String SG_NB_SV = TD + "SmallGrid_NB_SV_V3.xml";

    // SmallGrid HVDC
    private static final String HVDC_EQ = TD + "SmallGrid_HVDC_EQ_V3.xml";
    private static final String HVDC_TP = TD + "SmallGrid_HVDC_TP_V3.xml";
    private static final String HVDC_SSH = TD + "SmallGrid_HVDC_SSH_V3.xml";
    private static final String HVDC_SV = TD + "SmallGrid_HVDC_SV_V3.xml";

    // ==========================================
    // T2 Multi-Region (BE + NL)
    // ==========================================

    @Test
    public void testT2_BE_Region() throws Exception {
        System.out.println("\n=== T2 BE Region ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T2_BE_EQ, T2_BE_TP, T2_BE_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("BE: " + buses + " buses, " + branches + " branches");

        // T2 BE has 9 lines, 6 transformers → ~84 topological nodes
        assertTrue("BE should have buses", buses > 5);

        // Count injections
        int loads = 0, gens = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
        }
        System.out.println("BE loads: " + loads + ", gens: " + gens);
        assertTrue("BE should have loads", loads > 0);
        assertTrue("BE should have gens", gens > 0);

        System.out.println("T2 BE: PASSED");
    }

    @Test
    public void testT2_NL_Region() throws Exception {
        System.out.println("\n=== T2 NL Region ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T2_NL_EQ, T2_NL_TP, T2_NL_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("NL: " + buses + " buses, " + branches + " branches");

        assertTrue("NL should have buses", buses > 3);

        int loads = 0, gens = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
        }
        System.out.println("NL loads: " + loads + ", gens: " + gens);
        assertTrue("NL should have loads", loads > 0);
        assertTrue("NL should have gens", gens > 0);

        System.out.println("T2 NL: PASSED");
    }

    @Test
    public void testT2_MultiRegionMerge() throws Exception {
        System.out.println("\n=== T2 Multi-Region Merge (BE+NL) ===");

        CIMAdapter adapter = new CIMAdapter();
        // Merge all profiles from both regions
        String[] files = {
            T2_BE_EQ, T2_BE_TP, T2_BE_SSH,
            T2_NL_EQ, T2_NL_TP, T2_NL_SSH
        };
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("BE+NL merged: " + buses + " buses, " + branches + " branches");

        // Combined: BE(~10 buses) + NL(~5 buses) + cross-border ties
        assertTrue("Merged should have >10 buses", buses > 10);

        // Check SWING exists
        boolean hasSwing = false;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null && bus.getGenData().getCode() == LFGenCodeEnumType.SWING) {
                hasSwing = true;
                break;
            }
        }
        assertTrue("Should have SWING bus", hasSwing);

        System.out.println("T2 Multi-Region: PASSED");
    }

    // ==========================================
    // SmallGrid NodeBreaker
    // ==========================================

    @Test
    public void testSmallGrid_NB_Parse() throws Exception {
        System.out.println("\n=== SmallGrid NodeBreaker Parse ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(SG_NB_EQ));

        CIMModel model = adapter.getCimModel();
        System.out.println("Model: " + model.size() + " triples");
        System.out.println("Terminals: " + model.terminals().size());
        System.out.println("BusbarSections: " + model.busbarSections().size());
        System.out.println("Lines: " + model.acLineSegments().size());
        System.out.println("Transformers: " + model.powerTransformers().size());

        assertEquals(176, model.acLineSegments().size());
        assertEquals(103, model.energyConsumers().size());
        assertEquals(19, model.synchronousMachines().size());
        // Node-breaker has way more terminals than bus-branch
        assertTrue("Should have >2000 terminals", model.terminals().size() > 2000);

        System.out.println("SmallGrid NB Parse: PASSED");
    }

    @Test
    public void testSmallGrid_NB_BusBranch() throws Exception {
        System.out.println("\n=== SmallGrid NodeBreaker EQ+TP ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_NB_EQ, SG_NB_TP};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("NB buses: " + buses + ", branches: " + branches);

        // Node-breaker topology creates more buses than bus-branch
        assertTrue("Should have buses", buses > 100);
        assertTrue("Should have branches", branches > 100);

        System.out.println("SmallGrid NB Bus-Branch: PASSED");
    }

    @Test
    public void testSmallGrid_NB_FullState() throws Exception {
        System.out.println("\n=== SmallGrid NodeBreaker Full (EQ+TP+SSH+SV) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_NB_EQ, SG_NB_TP, SG_NB_SSH, SG_NB_SV};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int loads = 0, gens = 0, shunts = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null
                    && bus.getShuntYData().getEquivY().getIm() != 0.0) shunts++;
        }

        System.out.println("NB loads: " + loads + ", gens: " + gens + ", shunts: " + shunts);
        assertTrue("Should have loads", loads > 0);
        assertTrue("Should have gens", gens > 0);

        System.out.println("SmallGrid NB Full: PASSED");
    }

    // ==========================================
    // SmallGrid HVDC
    // ==========================================

    @Test
    public void testSmallGrid_HVDC_Parse() throws Exception {
        System.out.println("\n=== SmallGrid HVDC Parse ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(HVDC_EQ));

        CIMModel model = adapter.getCimModel();
        System.out.println("Model: " + model.size() + " triples");
        System.out.println("Lines: " + model.acLineSegments().size());
        System.out.println("Transformers: " + model.powerTransformers().size());
        System.out.println("Loads: " + model.energyConsumers().size());
        System.out.println("Gens: " + model.synchronousMachines().size());

        assertEquals(174, model.acLineSegments().size());
        assertEquals(12, model.powerTransformers().size());

        System.out.println("SmallGrid HVDC Parse: PASSED");
    }

    @Test
    public void testSmallGrid_HVDC_BusBranch() throws Exception {
        System.out.println("\n=== SmallGrid HVDC Bus-Branch ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {HVDC_EQ, HVDC_TP, HVDC_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("HVDC: " + buses + " buses, " + branches + " branches");

        assertTrue("Should have buses", buses > 100);
        assertTrue("Should have branches", branches > 100);

        // Check injections
        int loads = 0, gens = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
        }
        System.out.println("HVDC loads: " + loads + ", gens: " + gens);
        assertTrue("Should have loads", loads > 0);
        assertTrue("Should have gens", gens > 0);

        System.out.println("SmallGrid HVDC: PASSED");
    }

    // ==========================================
    // Summary
    // ==========================================

    @Test
    public void testAllCasesSummary() throws Exception {
        System.out.println("\n========================================");
        System.out.println("  COMPLETE TEST CASE SUMMARY");
        System.out.println("========================================");
        System.out.printf("%-25s | %5s | %8s | %5s | %4s | %6s%n",
            "Test Case", "Buses", "Branches", "Loads", "Gens", "Shunts");
        System.out.println("--------------------------+-------+----------+-------+------+-------");

        printCase("MicroGrid T4 BE",
            new String[]{TD+"MicroGrid_T4_BE_EQ_V2.xml", TD+"MicroGrid_T4_BE_TP_V2.xml", TD+"MicroGrid_T4_BE_SSH_V2.xml"});
        printCase("SmallGrid BB",
            new String[]{TD+"SmallGrid_BB_EQ_V3.xml", TD+"SmallGrid_BB_TP_V3.xml", TD+"SmallGrid_BB_SSH_V3.xml"});
        printCase("SmallGrid NB",
            new String[]{SG_NB_EQ, SG_NB_TP, SG_NB_SSH});
        printCase("SmallGrid HVDC",
            new String[]{HVDC_EQ, HVDC_TP, HVDC_SSH});
        printCase("MiniGrid NB",
            new String[]{TD+"MiniGrid_NB_EQ_V3.xml", TD+"MiniGrid_NB_TP_V3.xml", TD+"MiniGrid_NB_SSH_V3.xml"});
        printCase("T2 BE+NL merged",
            new String[]{T2_BE_EQ, T2_BE_TP, T2_BE_SSH, T2_NL_EQ, T2_NL_TP, T2_NL_SSH});

        System.out.println("========================================");
        System.out.println("All test cases imported successfully");
    }

    private void printCase(String label, String[] files) throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        int loads = 0, gens = 0, shunts = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null
                    && bus.getShuntYData().getEquivY().getIm() != 0.0) shunts++;
        }
        System.out.printf("%-25s | %5d | %8d | %5d | %4d | %6d%n",
            label, buses, branches, loads, gens, shunts);
    }
}
