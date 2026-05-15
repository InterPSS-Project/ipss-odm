/*
 * CIMAdapterTest.java
 *
 * Tests for the CIM/CGMES adapter implementation.
 * Uses ENTSO-E CGMES conformity test cases from PowSyBl.
 */

package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.junit.Test;

/**
 * Test suite for CIM adapter phases.
 * Uses ENTSO-E CGMES conformity test cases from PowSyBl.
 */
public class CIMAdapterTest {

    private static final String TEST_DATA_DIR = "testdata/cim/";

    /** T4 BE bus-branch EQ file (has Terminals, BusbarSections, equipment) */
    private static final String T4_BE_EQ = TEST_DATA_DIR + "MicroGrid_T4_BE_EQ_V2.xml";

    /** T4 BE TP file (has TopologicalNodes and Terminal.TopologicalNode refs) */
    private static final String T4_BE_TP = TEST_DATA_DIR + "MicroGrid_T4_BE_TP_V2.xml";

    /** T4 BE SSH file */
    private static final String T4_BE_SSH = TEST_DATA_DIR + "MicroGrid_T4_BE_SSH_V2.xml";

    /** T4 BE SV file */
    private static final String T4_BE_SV = TEST_DATA_DIR + "MicroGrid_T4_BE_SV_V2.xml";

    /** BE EQ-only file (assembled, has no ConnectivityNodes) */
    private static final String BE_EQ = TEST_DATA_DIR + "MicroGridTestConfiguration_BC_BE_EQ_V2.xml";

    /** NL EQ file */
    private static final String NL_EQ = TEST_DATA_DIR + "MicroGridTestConfiguration_BC_NL_EQ_V2.xml";

    // ==========================================
    // Phase 1: Foundation (parse RDF/XML)
    // ==========================================

    @Test
    public void testPhase1_ParseEQFile() throws Exception {
        System.out.println("\n=== Phase 1: Parse EQ File ===");

        CIMAdapter adapter = new CIMAdapter();
        boolean result = adapter.parseInputFile(T4_BE_EQ);
        assertTrue("CIM parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        assertNotNull("Model should not be null", parser);

        // Verify format type
        assertEquals("Original format should be CIM",
            OriginalDataFormatEnumType.CIM,
            parser.getStudyCase().getContentInfo().getOriginalDataFormat());

        System.out.println("Phase 1: PASSED - RDF/XML parsed successfully");
    }

    // ==========================================
    // Phase 2: Bus-Branch Conversion (EQ+TP)
    // ==========================================

    @Test
    public void testPhase2_BusBranchConversion() throws Exception {
        System.out.println("\n=== Phase 2: Bus-Branch Conversion (EQ+TP) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_BE_EQ, T4_BE_TP};
        boolean result = adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        assertTrue("EQ+TP parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int busCount = net.getBusList().getBus().size();
        int branchCount = net.getBranchList().getBranch().size();

        System.out.println("Bus count: " + busCount);
        System.out.println("Branch count: " + branchCount);

        assertTrue("Should have buses from TopologicalNodes", busCount > 0);
        assertTrue("Should have branches", branchCount > 0);

        // Verify bus data integrity
        for (var busElem : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) busElem.getValue();
            assertNotNull("Bus should have an ID", bus.getId());
            assertNotNull("Bus should have base voltage", bus.getBaseVoltage());
            assertTrue("Bus base voltage should be positive", bus.getBaseVoltage().getValue() > 0);
        }

        // Count line vs transformer branches
        int lineCount = 0, xfrCount = 0;
        for (var branchElem : net.getBranchList().getBranch()) {
            Object branch = branchElem.getValue();
            if (branch instanceof LineBranchXmlType) lineCount++;
            else if (branch instanceof XfrBranchXmlType) xfrCount++;
        }
        System.out.println("Lines: " + lineCount + ", Transformers: " + xfrCount);

        assertTrue("Should have at least one line", lineCount > 0);
        assertTrue("Should have at least one transformer", xfrCount > 0);

        System.out.println("Phase 2: PASSED");
    }

    // ==========================================
    // Phase 3: Injections (loads, generators, shunts)
    // ==========================================

    @Test
    public void testPhase3_Injections() throws Exception {
        System.out.println("\n=== Phase 3: Injections (EQ+TP+SSH) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_BE_EQ, T4_BE_TP, T4_BE_SSH};
        boolean result = adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        assertTrue("EQ+TP+SSH parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int loadBusCount = 0;
        int genBusCount = 0;
        int shuntBusCount = 0;

        for (var busElem : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) busElem.getValue();

            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) {
                loadBusCount++;
            }
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) {
                genBusCount++;
            }
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null) {
                double b = bus.getShuntYData().getEquivY().getIm();
                if (b != 0.0) shuntBusCount++;
            }
        }

        System.out.println("Load buses: " + loadBusCount);
        System.out.println("Generator buses: " + genBusCount);
        System.out.println("Shunt buses: " + shuntBusCount);

        assertTrue("Should have at least 1 load bus", loadBusCount > 0);
        assertTrue("Should have at least 1 generator bus", genBusCount > 0);

        // Check SWING bus
        boolean hasSwing = false;
        for (var busElem : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) busElem.getValue();
            if (bus.getGenData() != null &&
                bus.getGenData().getCode() == org.ieee.odm.schema.LFGenCodeEnumType.SWING) {
                hasSwing = true;
                System.out.println("SWING bus: " + bus.getId());
                break;
            }
        }
        assertTrue("Should have at least one SWING bus", hasSwing);

        System.out.println("Phase 3: PASSED");
    }

    // ==========================================
    // Phase 4: Multi-file Input (EQ+TP+SSH)
    // ==========================================

    @Test
    public void testPhase4_MultiFileInput() throws Exception {
        System.out.println("\n=== Phase 4: Multi-file Input (EQ+TP+SSH) ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_BE_EQ, T4_BE_TP, T4_BE_SSH};
        boolean result = adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        assertTrue("Multi-file CIM parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int busCount = net.getBusList().getBus().size();
        int branchCount = net.getBranchList().getBranch().size();

        System.out.println("Buses: " + busCount + ", Branches: " + branchCount);
        assertTrue("Should have buses", busCount > 0);
        assertTrue("Should have branches", branchCount > 0);

        System.out.println("Phase 4: PASSED");
    }

    // ==========================================
    // Phase 5: Full summary print (EQ+TP+SSH+SV)
    // ==========================================

    @Test
    public void testPhase5_FullSummary() throws Exception {
        System.out.println("\n=== Phase 5: Full Network Summary ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_BE_EQ, T4_BE_TP, T4_BE_SSH, T4_BE_SV};
        boolean result = adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files);
        assertTrue("Full EQ+TP+SSH+SV parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        System.out.println("\n--- Buses (" + net.getBusList().getBus().size() + ") ---");
        for (var busElem : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) busElem.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("  %-35s %-20s %8.1f kV",
                bus.getId(),
                bus.getName() != null ? bus.getName() : "",
                bus.getBaseVoltage() != null ? bus.getBaseVoltage().getValue() : 0));

            if (bus.getGenData() != null && bus.getGenData().getCode() != null) {
                sb.append(" Gen=").append(bus.getGenData().getCode());
            }
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) {
                sb.append(" Load=yes");
            }
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null) {
                double b = bus.getShuntYData().getEquivY().getIm();
                if (b != 0.0) sb.append(String.format(" B=%.6f", b));
            }
            System.out.println(sb);
        }

        System.out.println("\n--- Branches (" + net.getBranchList().getBranch().size() + ") ---");
        for (var branchElem : net.getBranchList().getBranch()) {
            Object branch = branchElem.getValue();
            if (branch instanceof LineBranchXmlType) {
                LineBranchXmlType line = (LineBranchXmlType) branch;
                System.out.println(String.format("  LINE %-35s r=%.6f x=%.6f bch=%.6f",
                    line.getName() != null ? line.getName() : line.getId(),
                    line.getZ().getRe(), line.getZ().getIm(),
                    line.getTotalShuntY() != null ? line.getTotalShuntY().getIm() : 0));
            } else if (branch instanceof XfrBranchXmlType) {
                XfrBranchXmlType xfr = (XfrBranchXmlType) branch;
                System.out.println(String.format("  XFR  %-35s r=%.6f x=%.6f",
                    xfr.getName() != null ? xfr.getName() : xfr.getId(),
                    xfr.getZ().getRe(), xfr.getZ().getIm()));
            }
        }

        System.out.println("Phase 5: PASSED");
    }

    // ==========================================
    // EQ-only tests (node-breaker, limited)
    // ==========================================

    @Test
    public void testEQ_Only_Parse() throws Exception {
        System.out.println("\n=== EQ-only Parse Test (node-breaker) ===");

        CIMAdapter adapter = new CIMAdapter();
        boolean result = adapter.parseInputFile(T4_BE_EQ);
        assertTrue("EQ-only parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();
        int busCount = net.getBusList().getBus().size();
        System.out.println("EQ-only buses (from BusbarSections): " + busCount);

        // EQ-only should create buses from BusbarSections
        assertTrue("Should have buses from BusbarSections", busCount > 0);

        System.out.println("EQ-only Parse: PASSED");
    }

    @Test
    public void testNL_EQ_Parse() throws Exception {
        System.out.println("\n=== NL EQ Parse Test ===");

        CIMAdapter adapter = new CIMAdapter();
        boolean result = adapter.parseInputFile(NL_EQ);
        assertTrue("NL EQ parsing should succeed", result);

        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        System.out.println("NL buses: " + parser.getNet().getBusList().getBus().size());
        System.out.println("NL branches: " + parser.getNet().getBranchList().getBranch().size());

        System.out.println("NL EQ Parse: PASSED");
    }
}
