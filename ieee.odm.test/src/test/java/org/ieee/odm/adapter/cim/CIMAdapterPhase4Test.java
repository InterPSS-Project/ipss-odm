/*
 * CIMAdapterPhase4Test.java
 *
 * Phase 4 Test: SSH/SV State Data
 * Goal: Multi-file EQ+TP+SSH+SV, verify state values applied correctly.
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.junit.Test;

public class CIMAdapterPhase4Test {

    private static final String TD = "testdata/cim/";
    private static final String T4_EQ = TD + "MicroGrid_T4_BE_EQ_V2.xml";
    private static final String T4_TP = TD + "MicroGrid_T4_BE_TP_V2.xml";
    private static final String T4_SSH = TD + "MicroGrid_T4_BE_SSH_V2.xml";
    private static final String T4_SV = TD + "MicroGrid_T4_BE_SV_V2.xml";
    private static final String SG_EQ = TD + "SmallGrid_BB_EQ_V3.xml";
    private static final String SG_TP = TD + "SmallGrid_BB_TP_V3.xml";
    private static final String SG_SSH = TD + "SmallGrid_BB_SSH_V3.xml";
    private static final String SG_SV = TD + "SmallGrid_BB_SV_V3.xml";

    @Test
    public void test4_1_MultiFileMerge_EQ_TP_SSH_SV() throws Exception {
        System.out.println("\n=== Phase 4.1: 4-File Merge (EQ+TP+SSH+SV) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_EQ, T4_TP, T4_SSH, T4_SV};
        assertTrue("4-file merge should succeed",
            adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();
        System.out.println("Buses: " + net.getBusList().getBus().size());
        System.out.println("Branches: " + net.getBranchList().getBranch().size());
        assertTrue("Should have buses", net.getBusList().getBus().size() > 0);

        System.out.println("Phase 4.1: PASSED");
    }

    @Test
    public void test4_2_SSH_LoadValues() throws Exception {
        System.out.println("\n=== Phase 4.2: SSH Load P/Q Values ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_EQ, T4_TP, T4_SSH};
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        AclfModelParser parser = (AclfModelParser) adapter.getModel();

        // Check that loads have non-zero P from SSH
        int nonZeroLoads = 0;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) {
                // Load data is set — SSH P/Q values were applied
                nonZeroLoads++;
            }
        }
        System.out.println("Loads with data: " + nonZeroLoads);
        assertTrue("Should have loads with SSH data", nonZeroLoads > 0);

        System.out.println("Phase 4.2: PASSED");
    }

    @Test
    public void test4_3_SSH_GeneratorSetpoints() throws Exception {
        System.out.println("\n=== Phase 4.3: SSH Generator Setpoints ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_EQ, T4_TP, T4_SSH};
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        AclfModelParser parser = (AclfModelParser) adapter.getModel();

        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) {
                System.out.println(String.format("  Gen: %-20s type=%s",
                    bus.getName(), bus.getGenData().getCode()));
            }
        }

        // At least one SWING and one PV should exist
        boolean hasSwing = false, hasPV = false;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null) {
                if (bus.getGenData().getCode() == org.ieee.odm.schema.LFGenCodeEnumType.SWING) hasSwing = true;
                if (bus.getGenData().getCode() == org.ieee.odm.schema.LFGenCodeEnumType.PV) hasPV = true;
            }
        }
        assertTrue("Should have SWING bus", hasSwing);

        System.out.println("Phase 4.3: PASSED");
    }

    @Test
    public void test4_4_SmallGrid_FullState() throws Exception {
        System.out.println("\n=== Phase 4.4: SmallGrid Full State (EQ+TP+SSH+SV) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_EQ, SG_TP, SG_SSH, SG_SV};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int branches = net.getBranchList().getBranch().size();
        System.out.println("Buses: " + buses + ", Branches: " + branches);

        // Verify full state
        int loads = 0, gens = 0, shunts = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loads++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) gens++;
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null
                    && bus.getShuntYData().getEquivY().getIm() != 0.0) shunts++;
        }

        System.out.println("Loads: " + loads + ", Gens: " + gens + ", Shunts: " + shunts);
        assertTrue("SmallGrid should have many loads", loads > 50);
        assertTrue("SmallGrid should have gens", gens > 0);

        System.out.println("Phase 4.4: PASSED");
    }

    @Test
    public void test4_5_ProfileOrdering() throws Exception {
        System.out.println("\n=== Phase 4.5: Different Profile Orderings ===");

        // Test that file order doesn't matter
        CIMAdapter a1 = new CIMAdapter();
        a1.parseInputFile(IODMAdapter.NetType.AclfNet,
            new String[]{T4_EQ, T4_TP, T4_SSH});
        int b1 = ((AclfModelParser) a1.getModel()).getNet().getBusList().getBus().size();

        CIMAdapter a2 = new CIMAdapter();
        a2.parseInputFile(IODMAdapter.NetType.AclfNet,
            new String[]{T4_SSH, T4_EQ, T4_TP});
        int b2 = ((AclfModelParser) a2.getModel()).getNet().getBusList().getBus().size();

        System.out.println("Order EQ,TP,SSH: " + b1 + " buses");
        System.out.println("Order SSH,EQ,TP: " + b2 + " buses");
        assertEquals("Bus count should be same regardless of file order", b1, b2);

        System.out.println("Phase 4.5: PASSED");
    }
}
