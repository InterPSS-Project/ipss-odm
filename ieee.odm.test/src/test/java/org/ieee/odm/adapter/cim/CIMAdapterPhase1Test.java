/*
 * CIMAdapterPhase1Test.java
 *
 * Phase 1 Test: Foundation
 * Goal: Parse CGMES EQ RDF/XML file and verify CIMModel queries return expected elements.
 */
package org.ieee.odm.adapter.cim;

import static org.junit.Assert.*;

import java.util.List;

import org.ieee.odm.adapter.cim.CIMAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.junit.Test;

/**
 * Phase 1: Foundation
 * - Parse RDF/XML into Jena Model
 * - Verify CIMModel element accessors
 * - Verify index building
 * - Verify CIM version detection
 */
public class CIMAdapterPhase1Test {

    /** T4 BE bus-branch EQ file */
    private static final String T4_BE_EQ = "testdata/cim/MicroGrid_T4_BE_EQ_V2.xml";

    @Test
    public void test1_1_ParseEQFile() throws Exception {
        System.out.println("\n=== Phase 1.1: Parse EQ RDF/XML ===");

        CIMAdapter adapter = new CIMAdapter();
        boolean result = adapter.parseInputFile(T4_BE_EQ);

        assertTrue("CIM parsing should succeed", result);
        assertNotNull("Model should not be null", adapter.getModel());
        assertTrue("Model should be AclfModelParser", adapter.getModel() instanceof AclfModelParser);
        assertEquals("Original format should be CIM",
            OriginalDataFormatEnumType.CIM,
            adapter.getModel().getStudyCase().getContentInfo().getOriginalDataFormat());

        System.out.println("Parse result: OK");
    }

    @Test
    public void test1_2_CIMModelElementQueries() throws Exception {
        System.out.println("\n=== Phase 1.2: CIMModel Element Queries ===");

        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(T4_BE_EQ);

        CIMModel model = adapter.getCimModel();
        assertNotNull("CIMModel should be available", model);

        // Verify model size
        assertTrue("Model should have triples", model.size() > 0);
        System.out.println("Model size: " + model.size() + " triples");

        // Verify version detection
        String version = model.detectVersion();
        System.out.println("CIM version: " + version);
        assertEquals("Should detect CIM16", "CIM16", version);

        // Verify element counts — these should match what's in the T4 BE EQ file
        List<CIMPropertyBag> substations = model.substations();
        List<CIMPropertyBag> voltageLevels = model.voltageLevels();
        List<CIMPropertyBag> acLines = model.acLineSegments();
        List<CIMPropertyBag> transformers = model.powerTransformers();
        List<CIMPropertyBag> loads = model.energyConsumers();
        List<CIMPropertyBag> generators = model.synchronousMachines();
        List<CIMPropertyBag> shunts = model.shuntCompensators();
        List<CIMPropertyBag> terminals = model.terminals();
        List<CIMPropertyBag> baseVoltages = model.baseVoltages();
        List<CIMPropertyBag> busbars = model.busbarSections();
        List<CIMPropertyBag> xfrEnds = model.transformerEnds();

        System.out.println("\n--- Element Counts ---");
        System.out.println("Substations:         " + substations.size());
        System.out.println("VoltageLevels:       " + voltageLevels.size());
        System.out.println("ACLineSegments:      " + acLines.size());
        System.out.println("PowerTransformers:   " + transformers.size());
        System.out.println("PowerTransformerEnds:" + xfrEnds.size());
        System.out.println("EnergyConsumers:     " + loads.size());
        System.out.println("SynchronousMachines: " + generators.size());
        System.out.println("ShuntCompensators:   " + shunts.size());
        System.out.println("Terminals:           " + terminals.size());
        System.out.println("BaseVoltages:        " + baseVoltages.size());
        System.out.println("BusbarSections:      " + busbars.size());

        // T4 BE EQ expected element counts
        assertTrue("Should have substations", substations.size() > 0);
        assertTrue("Should have voltage levels", voltageLevels.size() > 0);
        assertEquals("Should have 7 ACLineSegments", 7, acLines.size());
        assertTrue("Should have PowerTransformers", transformers.size() > 0);
        assertTrue("Should have EnergyConsumers", loads.size() > 0);
        assertTrue("Should have SynchronousMachines", generators.size() > 0);
        assertTrue("Should have ShuntCompensators", shunts.size() > 0);
        assertTrue("Should have Terminals", terminals.size() > 0);
        assertTrue("Should have BaseVoltages", baseVoltages.size() > 0);
        assertTrue("Should have BusbarSections", busbars.size() > 0);
        assertTrue("Should have TransformerEnds", xfrEnds.size() > 0);
    }

    @Test
    public void test1_3_PropertyBagAccessors() throws Exception {
        System.out.println("\n=== Phase 1.3: CIMPropertyBag Typed Accessors ===");

        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(T4_BE_EQ);
        CIMModel model = adapter.getCimModel();

        // Test property access on an ACLineSegment
        List<CIMPropertyBag> lines = model.acLineSegments();
        assertFalse("Should have at least one ACLineSegment", lines.isEmpty());

        CIMPropertyBag firstLine = lines.get(0);
        System.out.println("\nFirst ACLineSegment:");
        System.out.println("  ID:     " + firstLine.getId());
        System.out.println("  Local:  " + firstLine.getLocalId());
        System.out.println("  Name:   " + firstLine.getName());
        System.out.println("  R:      " + firstLine.getDouble("ACLineSegment.r") + " Ohm");
        System.out.println("  X:      " + firstLine.getDouble("ACLineSegment.x") + " Ohm");
        System.out.println("  Bch:    " + firstLine.getDouble("ACLineSegment.bch", 0.0) + " S");
        System.out.println("  BaseV:  " + firstLine.getResourceId("ConductingEquipment.BaseVoltage"));

        assertNotNull("Line should have ID", firstLine.getId());
        assertNotNull("Line should have local ID", firstLine.getLocalId());
        assertNotNull("Line should have name", firstLine.getName());
        assertTrue("Line should have R > 0", firstLine.getDouble("ACLineSegment.r") > 0);
        assertTrue("Line should have X > 0", firstLine.getDouble("ACLineSegment.x") > 0);

        // Test property access on a VoltageLevel
        List<CIMPropertyBag> vls = model.voltageLevels();
        CIMPropertyBag firstVL = vls.get(0);
        System.out.println("\nFirst VoltageLevel:");
        System.out.println("  ID:     " + firstVL.getLocalId());
        System.out.println("  Name:   " + firstVL.getName());
        System.out.println("  NomV:   " + firstVL.getDouble("VoltageLevel.nominalVoltage") + " kV");

        assertTrue("VL should have name", firstVL.getName() != null);
        // Not all VLs have nominalVoltage directly — some use BaseVoltage reference
        // We verify at least some VLs have voltage in test1_4
    }

    @Test
    public void test1_4_IndexBuilding() throws Exception {
        System.out.println("\n=== Phase 1.4: Topology Index Verification ===");

        CIMAdapter adapter = new CIMAdapter();
        adapter.parseInputFile(T4_BE_EQ);
        CIMModel model = adapter.getCimModel();

        // Verify terminal → equipment mapping
        List<CIMPropertyBag> lines = model.acLineSegments();
        CIMPropertyBag firstLine = lines.get(0);
        List<String> terminals = model.getTerminalsForEquipment(firstLine.getId());

        System.out.println("Line '" + firstLine.getName() + "' has " + terminals.size() + " terminals");
        assertTrue("ACLineSegment should have at least 2 terminals", terminals.size() >= 2);

        // Verify equipment → terminal reverse mapping
        for (String tid : terminals) {
            String equip = model.getEquipmentByTerminal(tid);
            assertNotNull("Terminal should map to equipment", equip);
            System.out.println("  Terminal " + CIMPropertyBag.extractLocal(tid)
                + " → equipment " + CIMPropertyBag.extractLocal(equip));
        }

        // Some VLs store nominalVoltage directly, others via BaseVoltage reference
        // Not all VLs in EQ-only mode have this resolvable
        for (CIMPropertyBag vl : model.voltageLevels()) {
            Double nomV = model.getVLRatedVoltage(vl.getId());
            System.out.println("VL " + vl.getName() + ": nominalV=" + nomV + " kV");
        }
        // At least one VL should have a resolvable voltage
        boolean anyVLHasVoltage = false;
        for (CIMPropertyBag vl : model.voltageLevels()) {
            if (model.getVLRatedVoltage(vl.getId()) != null) {
                anyVLHasVoltage = true;
                break;
            }
        }
        assertTrue("At least one VL should have resolvable nominal voltage", anyVLHasVoltage);

        // Check BaseVoltage index
        for (CIMPropertyBag bv : model.baseVoltages()) {
            Double val = model.getBaseVoltageValue(bv.getId());
            System.out.println("BV " + bv.getLocalId() + ": " + val + " kV");
            assertNotNull("BaseVoltage should have value", val);
        }
    }

    @Test
    public void test1_5_MultiFileParse() throws Exception {
        System.out.println("\n=== Phase 1.5: Multi-file (EQ+TP) Parse ===");

        CIMAdapter adapter = new CIMAdapter();
        String[] files = {
            "testdata/cim/MicroGrid_T4_BE_EQ_V2.xml",
            "testdata/cim/MicroGrid_T4_BE_TP_V2.xml"
        };
        boolean result = adapter.parseInputFile(
            org.ieee.odm.adapter.IODMAdapter.NetType.AclfNet, files);
        assertTrue("Multi-file parse should succeed", result);

        CIMModel model = adapter.getCimModel();
        assertNotNull(model);

        // After merging EQ+TP, TopologicalNodes should exist
        List<CIMPropertyBag> topoNodes = model.topologicalNodes();
        System.out.println("TopologicalNodes after EQ+TP merge: " + topoNodes.size());
        assertTrue("Should have TopologicalNodes from TP profile", topoNodes.size() > 0);

        // Verify some topo nodes have names and voltage levels
        for (int i = 0; i < Math.min(3, topoNodes.size()); i++) {
            CIMPropertyBag tn = topoNodes.get(i);
            System.out.println("  TN: " + tn.getName() + " (" + tn.getLocalId() + ")");
        }
    }
}
