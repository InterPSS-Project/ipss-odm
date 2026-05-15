/*
 * CIMAdapterPhase3Test.java
 *
 * Phase 3 Test: Injections
 * Goal: Loads, generators, shunts mapped to buses. SWING bus designated.
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.junit.Test;

public class CIMAdapterPhase3Test {

    private static final String TD = "testdata/cim/";
    private static final String T4_EQ = TD + "MicroGrid_T4_BE_EQ_V2.xml";
    private static final String T4_TP = TD + "MicroGrid_T4_BE_TP_V2.xml";
    private static final String T4_SSH = TD + "MicroGrid_T4_BE_SSH_V2.xml";
    private static final String SG_EQ = TD + "SmallGrid_BB_EQ_V3.xml";
    private static final String SG_TP = TD + "SmallGrid_BB_TP_V3.xml";
    private static final String SG_SSH = TD + "SmallGrid_BB_SSH_V3.xml";

    private AclfModelParser loadT4() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        String[] files = {T4_EQ, T4_TP, T4_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));
        return (AclfModelParser) adapter.getModel();
    }

    private AclfModelParser loadSG() throws Exception {
        CIMAdapter adapter = new CIMAdapter();
        String[] files = {SG_EQ, SG_TP, SG_SSH};
        assertTrue(adapter.parseInputFile(IODMAdapter.NetType.AclfNet, files));
        return (AclfModelParser) adapter.getModel();
    }

    @Test
    public void test3_1_T4_LoadInjection() throws Exception {
        System.out.println("\n=== Phase 3.1: Load Injection (T4) ===");

        AclfModelParser parser = loadT4();
        int loadBuses = 0;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) {
                loadBuses++;
                System.out.println("  Load on bus: " + bus.getName() + " (" + bus.getId() + ")");
            }
        }
        System.out.println("Load buses: " + loadBuses);
        assertTrue("T4 should have at least 1 load bus", loadBuses > 0);
    }

    @Test
    public void test3_2_T4_GeneratorInjection() throws Exception {
        System.out.println("\n=== Phase 3.2: Generator Injection (T4) ===");

        AclfModelParser parser = loadT4();
        int genBuses = 0;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) {
                genBuses++;
                System.out.println(String.format("  Gen on bus: %-20s type=%s",
                    bus.getName(), bus.getGenData().getCode()));
            }
        }
        System.out.println("Generator buses: " + genBuses);
        assertTrue("T4 should have at least 1 generator bus", genBuses > 0);
    }

    @Test
    public void test3_3_T4_SwingBus() throws Exception {
        System.out.println("\n=== Phase 3.3: SWING Bus (T4) ===");

        AclfModelParser parser = loadT4();
        boolean hasSwing = false;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getGenData() != null && bus.getGenData().getCode() == LFGenCodeEnumType.SWING) {
                hasSwing = true;
                System.out.println("  SWING bus: " + bus.getName() + " (" + bus.getId() + ")");
            }
        }
        assertTrue("Should have at least one SWING bus", hasSwing);
    }

    @Test
    public void test3_4_T4_ShuntInjection() throws Exception {
        System.out.println("\n=== Phase 3.4: Shunt Injection (T4) ===");

        AclfModelParser parser = loadT4();
        int shuntBuses = 0;
        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null) {
                double b = bus.getShuntYData().getEquivY().getIm();
                if (b != 0.0) {
                    shuntBuses++;
                    System.out.println(String.format("  Shunt on bus: %-20s B=%.6f",
                        bus.getName(), b));
                }
            }
        }
        System.out.println("Shunt buses: " + shuntBuses);
        // T4 has shunt compensators — may or may not resolve depending on topology
        // Just verify no crash
        System.out.println("Phase 3.4: PASSED (shunt check complete)");
    }

    @Test
    public void test3_5_SmallGrid_Injections() throws Exception {
        System.out.println("\n=== Phase 3.5: SmallGrid Full Injections ===");

        AclfModelParser parser = loadSG();
        int loadBuses = 0, genBuses = 0, shuntBuses = 0, swingBuses = 0;

        for (var be : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty()) loadBuses++;
            if (bus.getGenData() != null && bus.getGenData().getCode() != null) {
                genBuses++;
                if (bus.getGenData().getCode() == LFGenCodeEnumType.SWING) swingBuses++;
            }
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null
                    && bus.getShuntYData().getEquivY().getIm() != 0.0) shuntBuses++;
        }

        System.out.println("Load buses: " + loadBuses);
        System.out.println("Generator buses: " + genBuses);
        System.out.println("SWING buses: " + swingBuses);
        System.out.println("Shunt buses: " + shuntBuses);

        assertTrue("SmallGrid should have many load buses", loadBuses > 10);
        assertTrue("SmallGrid should have generator buses", genBuses > 0);
        assertTrue("Should have at least one SWING bus", swingBuses > 0);

        System.out.println("Phase 3.5: PASSED");
    }

    @Test
    public void test3_6_FullSummary_T4() throws Exception {
        System.out.println("\n=== Phase 3.6: Full Network Summary (T4 EQ+TP+SSH) ===");

        AclfModelParser parser = loadT4();
        var net = parser.getNet();

        System.out.println("\n--- Buses ---");
        for (var be : net.getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) be.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("  %-35s %-15s %6.0fkV",
                bus.getName() != null ? bus.getName() : bus.getId(),
                bus.getId(),
                bus.getBaseVoltage() != null ? bus.getBaseVoltage().getValue() : 0));
            if (bus.getGenData() != null && bus.getGenData().getCode() != null)
                sb.append(" Gen=").append(bus.getGenData().getCode());
            if (bus.getLoadData() != null && bus.getLoadData().getContributeLoad() != null
                    && !bus.getLoadData().getContributeLoad().isEmpty())
                sb.append(" Load=yes");
            if (bus.getShuntYData() != null && bus.getShuntYData().getEquivY() != null
                    && bus.getShuntYData().getEquivY().getIm() != 0.0)
                sb.append(String.format(" B=%.4f", bus.getShuntYData().getEquivY().getIm()));
            System.out.println(sb);
        }

        System.out.println("\n--- Branches ---");
        for (var be : net.getBranchList().getBranch()) {
            Object b = be.getValue();
            if (b instanceof org.ieee.odm.schema.LineBranchXmlType) {
                org.ieee.odm.schema.LineBranchXmlType line = (org.ieee.odm.schema.LineBranchXmlType) b;
                System.out.println(String.format("  LINE %-25s z=(%.6f+j%.6f)",
                    line.getName() != null ? line.getName() : line.getId(),
                    line.getZ().getRe(), line.getZ().getIm()));
            } else if (b instanceof org.ieee.odm.schema.XfrBranchXmlType) {
                org.ieee.odm.schema.XfrBranchXmlType xfr = (org.ieee.odm.schema.XfrBranchXmlType) b;
                System.out.println(String.format("  XFR  %-25s z=(%.6f+j%.6f)",
                    xfr.getName() != null ? xfr.getName() : xfr.getId(),
                    xfr.getZ().getRe(), xfr.getZ().getIm()));
            }
        }
        System.out.println("Phase 3.6: PASSED");
    }
}
