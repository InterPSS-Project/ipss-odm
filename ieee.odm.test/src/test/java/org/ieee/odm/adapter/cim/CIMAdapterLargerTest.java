/*
 * CIMAdapterLargerTest.java
 *
 * Tests CIM import against larger ENTSO-E conformity test cases:
 * - SmallGrid BusBranch (176 lines, 103 loads, 19 generators, 629 topo nodes)
 * - MiniGrid NodeBreaker (9 lines, 6 transformers, 11 busbars)
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class CIMAdapterLargerTest {

    private static final String TD = "testdata/cim/";

    // SmallGrid BusBranch — much larger test case
    private static final String SG_EQ = TD + "SmallGrid_BB_EQ_V3.xml";
    private static final String SG_TP = TD + "SmallGrid_BB_TP_V3.xml";
    private static final String SG_SSH = TD + "SmallGrid_BB_SSH_V3.xml";
    private static final String SG_SV = TD + "SmallGrid_BB_SV_V3.xml";

    // MiniGrid NodeBreaker — mid-size with busbar sections
    private static final String MG_EQ = TD + "MiniGrid_NB_EQ_V3.xml";
    private static final String MG_TP = TD + "MiniGrid_NB_TP_V3.xml";
    private static final String MG_SSH = TD + "MiniGrid_NB_SSH_V3.xml";

    // ========== SmallGrid BusBranch ==========

    @Test
    public void testSmallGrid_Phase1_ParseAndCount() throws Exception {
        System.out.println("\n=== SmallGrid Phase 1: Parse & Element Count ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(SG_EQ));

        CIMModel model = adapter.getCimModel();
        assertNotNull(model);

        System.out.println("Model: " + model.size() + " triples, version=" + model.detectVersion());
        System.out.println("Substations:    " + model.substations().size());
        System.out.println("VoltageLevels:  " + model.voltageLevels().size());
        System.out.println("Lines:          " + model.acLineSegments().size());
        System.out.println("Transformers:   " + model.powerTransformers().size());
        System.out.println("Loads:          " + model.energyConsumers().size());
        System.out.println("Generators:     " + model.synchronousMachines().size());
        System.out.println("Shunts:         " + model.shuntCompensators().size());
        System.out.println("Terminals:      " + model.terminals().size());
        System.out.println("TransformerEnds:" + model.transformerEnds().size());

        // Verify expected counts from SmallGrid
        assertEquals("CIM16", model.detectVersion());
        assertEquals(176, model.acLineSegments().size());
        assertEquals(10, model.powerTransformers().size());
        assertEquals(103, model.energyConsumers().size());
        assertEquals(19, model.synchronousMachines().size());
        assertEquals(14, model.shuntCompensators().size());
        assertTrue(model.terminals().size() > 400);

        System.out.println("SmallGrid Phase 1: PASSED");
    }

    @Test
    public void testSmallGrid_Phase2_BusBranch() throws Exception {
        System.out.println("\n=== SmallGrid Phase 2: Bus-Branch ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_EQ, SG_TP};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();

        System.out.println("Buses: " + buses);
        System.out.println("Branches: " + branches);

        // SmallGrid has 629 TopologicalNodes
        assertTrue("Should have buses", buses > 100);

        // Count lines vs transformers
        int lines = 0, xfrs = 0;
        for (var b : net.getBranchList().getBranch()) {
            if (b.getValue() instanceof LineBranchXmlType) lines++;
            else if (b.getValue() instanceof XfrBranchXmlType) xfrs++;
        }
        System.out.println("Lines: " + lines + ", Transformers: " + xfrs);

        assertTrue("Should have many lines", lines >= 100);
        assertTrue("Should have transformers", xfrs > 0);

        // Verify bus voltage coverage
        int withV = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getBaseVoltage() != null && bus.getBaseVoltage().getValue() > 0) withV++;
        }
        System.out.println("Buses with voltage: " + withV + "/" + buses);
        assertTrue("Most buses should have voltage", withV > buses / 2);

        System.out.println("SmallGrid Phase 2: PASSED");
    }

    // ========== MiniGrid NodeBreaker ==========

    @Test
    public void testMiniGrid_Phase1_Parse() throws Exception {
        System.out.println("\n=== MiniGrid Phase 1: Parse ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(MG_EQ));

        CIMModel model = adapter.getCimModel();
        System.out.println("Model: " + model.size() + " triples");
        System.out.println("Lines: " + model.acLineSegments().size());
        System.out.println("Transformers: " + model.powerTransformers().size());
        System.out.println("BusbarSections: " + model.busbarSections().size());
        System.out.println("Terminals: " + model.terminals().size());

        assertEquals(9, model.acLineSegments().size());
        assertEquals(6, model.powerTransformers().size());
        assertEquals(11, model.busbarSections().size());

        System.out.println("MiniGrid Phase 1: PASSED");
    }

    @Test
    public void testMiniGrid_Phase2_BusBranchWithTP() throws Exception {
        System.out.println("\n=== MiniGrid Phase 2: EQ+TP Bus-Branch ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {MG_EQ, MG_TP};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        System.out.println("Buses: " + net.getBusList().getBus().size());
        System.out.println("Branches: " + net.getBranchList().getBranch().size());

        assertTrue("Should have buses", net.getBusList().getBus().size() > 0);
        assertTrue("Should have branches", net.getBranchList().getBranch().size() > 0);

        // Print bus summary
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            System.out.println(String.format("  %-40s %-20s baseKV=%s",
                bus.getId(),
                bus.getName() != null ? bus.getName() : "",
                bus.getBaseVoltage() != null ? String.format("%.1f", bus.getBaseVoltage().getValue()) : "?"));
        }

        System.out.println("MiniGrid Phase 2: PASSED");
    }

    @Test
    public void testMiniGrid_EQOnly_Busbars() throws Exception {
        System.out.println("\n=== MiniGrid EQ-only (BusbarSections) ===");

        CIMAdapter adapter = new CIMAdapter();
        assertTrue(adapter.parseInputFile(MG_EQ));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        System.out.println("EQ-only buses (from BusbarSections): " + buses);
        assertEquals("Should have 11 buses from BusbarSections", 11, buses);

        System.out.println("MiniGrid EQ-only: PASSED");
    }

    // ========== Multi-region assembly ==========

    @Test
    public void testSmallGrid_FullProfile_EQ_TP_SSH() throws Exception {
        System.out.println("\n=== SmallGrid Full (EQ+TP+SSH) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_EQ, SG_TP, SG_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        System.out.println("Buses: " + net.getBusList().getBus().size());
        System.out.println("Branches: " + net.getBranchList().getBranch().size());

        // Count injections
        int loadBuses = 0, genBuses = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loadBuses++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) genBuses++;
        }
        System.out.println("Load buses: " + loadBuses);
        System.out.println("Generator buses: " + genBuses);

        assertTrue("Should have load buses", loadBuses > 0);
        assertTrue("Should have generator buses", genBuses > 0);

        System.out.println("SmallGrid Full: PASSED");
    }
}
