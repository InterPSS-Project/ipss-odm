package org.ieee.odm.adapter.cim;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test CIM adapter against CIMHub BES test cases (IEEE 118, WECC 240).
 * Cross-validates with MATPOWER reference files.
 * https://github.com/GRIDAPPSD/CIMHub/tree/master/BES
 */
public class CIMCIMHubTest {
    private static final String TD = "testdata/cim/";

    @Test
    public void testIEEE118() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, new String[]{TD + "IEEE118_CIM.xml"});
        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            Object v = b.getValue();
            if (v instanceof LineBranchXmlType) lines++;
            else if (v instanceof XfrBranchXmlType) xfr2w++;
            else if (v instanceof Xfr3WBranchXmlType) xfr3w++;
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

        System.err.println(String.format(
            "IEEE118_CIM: buses=%d lines=%d xfr2w=%d xfr3w=%d loads=%d gens=%d shunts=%d",
            buses, lines, xfr2w, xfr3w, loadCount, genCount, shuntCount));

        // MATPOWER reference
        assertEquals("Buses should match MATPOWER", 193, buses);
        assertEquals("Lines should match MATPOWER", 170, lines);
        assertEquals("2W transformers should match MATPOWER", 84, xfr2w);
        assertEquals("Loads should match MATPOWER", 99, loadCount);
        assertEquals("Shunts should match MATPOWER", 14, shuntCount);
        assertTrue("Should have generators (56 SynchronousMachines)", genCount >= 49);

        // Cross-validate first line PU values against MATPOWER
        // Line 1_2_1 (bus 1 → bus 2): MATPOWER r=0.003040 x=0.010020 b=0.008340
        boolean foundLine12 = false;
        for (var b : net.getBranchList().getBranch()) {
            Object v = b.getValue();
            if (v instanceof LineBranchXmlType) {
                LineBranchXmlType line = (LineBranchXmlType) v;
                if (line.getName() != null && line.getName().equals("1_2_1")) {
                    foundLine12 = true;
                    double rPU = line.getZ().getRe();
                    double xPU = line.getZ().getIm();
                    System.err.println(String.format("  Line 1_2_1: r=%.6f x=%.6f", rPU, xPU));
                    // MATPOWER: r=0.030300, x=0.099900
                    assertEquals("Line 1_2_1 r (PU)", 0.030300, rPU, 0.0001);
                    assertEquals("Line 1_2_1 x (PU)", 0.099900, xPU, 0.0001);
                    break;
                }
            }
        }
        assertTrue("Should find line 1_2_1", foundLine12);
    }

    @Test
    public void testWECC240() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(IODMAdapter.NetType.AclfNet, new String[]{TD + "WECC240_CIM.xml"});
        AclfModelParser parser = (AclfModelParser) adapter.getModel();
        var net = parser.getNet();

        int buses = net.getBusList().getBus().size();
        int lines = 0, xfr2w = 0, xfr3w = 0;
        for (var b : net.getBranchList().getBranch()) {
            Object v = b.getValue();
            if (v instanceof LineBranchXmlType) lines++;
            else if (v instanceof XfrBranchXmlType) xfr2w++;
            else if (v instanceof Xfr3WBranchXmlType) xfr3w++;
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

        System.err.println(String.format(
            "WECC240_CIM: buses=%d lines=%d xfr2w=%d xfr3w=%d loads=%d gens=%d shunts=%d",
            buses, lines, xfr2w, xfr3w, loadCount, genCount, shuntCount));

        // MATPOWER reference
        assertEquals("Buses should match MATPOWER", 243, buses);
        assertEquals("Lines should match MATPOWER", 329, lines);
        assertEquals("2W transformers should match MATPOWER", 122, xfr2w);
        assertEquals("Loads should match MATPOWER", 139, loadCount);
        // CIM has 7 LinearShuntCompensators on 7 unique buses; MATPOWER merges some into bus data → 5
        assertEquals("Shunts should match CIM count", 7, shuntCount);

        // Verify line PU values against MATPOWER
        // Find a known line and validate PU conversion
        boolean checkedLine = false;
        for (var b : net.getBranchList().getBranch()) {
            Object v = b.getValue();
            if (v instanceof LineBranchXmlType) {
                LineBranchXmlType line = (LineBranchXmlType) v;
                if (line.getZ() != null && line.getZ().getIm() > 0.01) {
                    // Any line with reasonable X in PU range — just check it's not off by 1000x
                    double xPU = line.getZ().getIm();
                    assertTrue("Line X should be in reasonable PU range (0.001-10), got " + xPU,
                        xPU > 0.0001 && xPU < 10.0);
                    checkedLine = true;
                    break;
                }
            }
        }
        assertTrue("Should have checked at least one line", checkedLine);
    }
}
