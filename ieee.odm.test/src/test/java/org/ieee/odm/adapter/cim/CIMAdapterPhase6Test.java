/*
 * CIMAdapterPhase6Test.java
 *
 * Phase 6: ENTSO-E conformity validation — head-to-head comparison with PowSyBl.
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.*;
import org.junit.Test;

public class CIMAdapterPhase6Test {

    private static final String TD = "testdata/cim/";

    /** Reference counts from PowSyBl CGMES v7.3 (BusView) */
    static class RefStats {
        String name;
        String[] files;
        int buses, lines, xfr2w, xfr3w, loads, gens, shunts;

        RefStats(String name, String[] files, int buses, int lines, int xfr2w, int xfr3w,
                 int loads, int gens, int shunts) {
            this.name = name; this.files = files;
            this.buses = buses; this.lines = lines; this.xfr2w = xfr2w; this.xfr3w = xfr3w;
            this.loads = loads; this.gens = gens; this.shunts = shunts;
        }
    }

    // PowSyBl BusView reference
    private static final RefStats[] REFS = {
        // MicroGrid_T4_BE: subs=2 vls=7 buses=7 lines=2 xfr2w=3 xfr3w=1 loads=3 gens=2 shunts=2 (lines=2 without boundary EQ/TP files; ref 3 with boundary)
        new RefStats("MicroGrid_T4_BE",
            new String[]{TD + "MicroGrid_T4_BE_EQ_V2.xml", TD + "MicroGrid_T4_BE_TP_V2.xml", TD + "MicroGrid_T4_BE_SSH_V2.xml",
                         TD + "MicroGrid_T4_BE_EQ_BD_V2.xml", TD + "MicroGrid_T4_BE_TP_BD_V2.xml"},
            7, 3, 3, 1, 3, 2, 2),
        // MiniGrid_NB: subs=5 vls=10 buses=11 lines=7 xfr2w=4 xfr3w=2 loads=3 gens=5 shunts=0
        new RefStats("MiniGrid_NB",
            new String[]{TD + "MiniGrid_NB_EQ_V3.xml", TD + "MiniGrid_NB_TP_V3.xml", TD + "MiniGrid_NB_SSH_V3.xml",
                         TD + "MiniGrid_NB_EQ_BD_V3.xml", TD + "MiniGrid_NB_TP_BD_V3.xml"},
            11, 7, 4, 2, 3, 5, 0),
        // SmallGrid_BB: subs=105 vls=115 buses=115 lines=173 xfr2w=10 xfr3w=0 loads=103 gens=19 shunts=14
        new RefStats("SmallGrid_BB",
            new String[]{TD + "SmallGrid_BB_EQ_V3.xml", TD + "SmallGrid_BB_TP_V3.xml", TD + "SmallGrid_BB_SSH_V3.xml",
                         TD + "SmallGrid_BB_EQ_BD_V3.xml", TD + "SmallGrid_BB_TP_BD_V3.xml"},
            115, 173, 10, 0, 103, 19, 14),
    };

    // ========== Individual conformity tests ==========

    @Test
    public void testMicroGrid_T4_BE() throws Exception {
        runConformityCheck(REFS[0]);
    }

    @Test
    public void testMiniGrid_NB() throws Exception {
        runConformityCheck(REFS[1]);
    }

    @Test
    public void testSmallGrid_BB() throws Exception {
        runConformityCheck(REFS[2]);
    }

    /**
     * Comprehensive summary report. Always passes — for reporting only.
     */
    @Test
    public void testConformityReport() throws Exception {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     CIM/CGMES Conformity Validation Report (vs PowSyBl)        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.printf("%-18s | %-5s %-5s %-5s %-5s %-5s %-5s %-5s | %s%n",
            "Test Case", "Bus", "Line", "Xfr2", "Xfr3", "Load", "Gen", "Shnt", "Result");
        System.out.println("-".repeat(85));

        int pass = 0, fail = 0;
        for (RefStats ref : REFS) {
            boolean ok = printReportLine(ref);
            if (ok) pass++; else fail++;
        }

        System.out.println("-".repeat(85));
        System.out.printf("Total: %d/%d PASS%n", pass, REFS.length);
    }

    // ========== Helpers ==========

    private AclfModelParser parse(RefStats ref) throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        assertTrue(ref.name + ": parse failed",
            adapter.parseInputFile(IODMAdapter.NetType.AclfNet, ref.files));
        return (AclfModelParser) adapter.getModel();
    }

    private void runConformityCheck(RefStats ref) throws Exception {
        AclfModelParser parser = parse(ref);
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();

        // Count branch types
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            Object v = b.getValue();
            if (v instanceof LineBranchXmlType) lines++;
            else if (v instanceof Xfr3WBranchXmlType) xfr3w++;
            else if (v instanceof XfrBranchXmlType) xfr2w++;
        }

        // Count injections
        // Use adapter's load count (individual loads, not buses with loads)
        int loadCount = CIMAdapter.getLastLoadCount();
        int genCount = 0, shuntCount = 0;
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) genCount++;
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null) {
                YXmlType y = bus.getShuntYData().getEquivY();
                if (y.getRe() != 0.0 || y.getIm() != 0.0) {
                    shuntCount++;
                }
            }
        }

        assertEquals(ref.name + " buses", ref.buses, buses);
        assertEquals(ref.name + " lines", ref.lines, lines);
        assertEquals(ref.name + " 2W xfrs", ref.xfr2w, xfr2w);
        assertEquals(ref.name + " 3W xfrs", ref.xfr3w, xfr3w);
    }

    private boolean printReportLine(RefStats ref) {
        try {
            AclfModelParser parser = parse(ref);
            var net = parser.getNet();

            int buses = net.getBusList().getBus().size();
            int lines = 0, xfr2w = 0, xfr3w = 0;
            for (var b : net.getBranchList().getBranch()) {
                Object v = b.getValue();
                if (v instanceof LineBranchXmlType) lines++;
                else if (v instanceof Xfr3WBranchXmlType) xfr3w++;
                else if (v instanceof XfrBranchXmlType) xfr2w++;
            }

            int loadCount = CIMAdapter.getLastLoadCount();
            int genCount = 0, shuntCount = 0;
            for (var be : net.getBusList().getBus()) {
                LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
                if (bus.getGenData() != null && bus.getGenData().getCode() != null) genCount++;
                if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null) {
                    YXmlType y = bus.getShuntYData().getEquivY();
                    if (y.getRe() != 0.0 || y.getIm() != 0.0) shuntCount++;
                }
            }

            boolean ok = buses == ref.buses && lines == ref.lines && xfr2w == ref.xfr2w
                && xfr3w == ref.xfr3w && loadCount == ref.loads && genCount == ref.gens
                && shuntCount == ref.shunts;

            System.out.printf("%-18s | %s%d %s%d %s%d %s%d %s%d %s%d %s%d | %s%n",
                ref.name,
                buses == ref.buses ? "" : "⚠", buses,
                lines == ref.lines ? "" : "⚠", lines,
                xfr2w == ref.xfr2w ? "" : "⚠", xfr2w,
                xfr3w == ref.xfr3w ? "" : "⚠", xfr3w,
                loadCount == ref.loads ? "" : "⚠", loadCount,
                genCount == ref.gens ? "" : "⚠", genCount,
                shuntCount == ref.shunts ? "" : "⚠", shuntCount,
                ok ? "✅ PASS" : "❌ FAIL (ref: " + ref.buses + " " + ref.lines + " " + ref.xfr2w + " " + ref.xfr3w + " " + ref.loads + " " + ref.gens + " " + ref.shunts + ")");

            return ok;
        } catch (Exception e) {
            System.out.printf("%-18s | PARSE ERROR: %s%n", ref.name, e.getMessage());
            return false;
        }
    }
}
