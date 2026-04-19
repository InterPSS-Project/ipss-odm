package org.ieee.odm.aclf.matpower;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.matpower.MatpowerAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.ZUnitType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class Matpower_ODMTest {
    private AclfModelParser parseCase(String path) throws Exception {
        IODMAdapter adapter = new MatpowerAdapter();
        assertTrue(adapter.parseInputFile(path));
        return (AclfModelParser) adapter.getModel();
    }

    @Test
    public void testCase9() throws Exception {
        AclfModelParser parser = parseCase("testdata/matpower/case9.m");
        LoadflowNetXmlType net = parser.getAclfNet();

        assertEquals("Aclf_from_Matpower_case9", net.getId());
        assertEquals(9, net.getBusList().getBus().size());
        assertEquals(9, net.getBranchList().getBranch().size());
        assertEquals(100.0, net.getBasePower().getValue(), 0.0);
        assertEquals(ApparentPowerUnitType.MVA, net.getBasePower().getUnit());

        LoadflowBusXmlType bus1 = parser.getAclfBus("Bus1");
        assertNotNull(bus1);
        assertEquals(345.0, bus1.getBaseVoltage().getValue(), 0.0);
        assertEquals(1.0, bus1.getVoltage().getValue(), 0.0);
        assertEquals(0.0, bus1.getAngle().getValue(), 0.0);
        assertEquals(LFGenCodeEnumType.SWING, bus1.getGenData().getCode());

        LoadflowBusXmlType bus5 = parser.getAclfBus("Bus5");
        assertNotNull(bus5);
        LoadflowLoadDataXmlType load5 = bus5.getLoadData().getContributeLoad().get(0).getValue();
        assertEquals(LFLoadCodeEnumType.CONST_P, load5.getCode());
        assertEquals(90.0, load5.getConstPLoad().getRe(), 0.0);
        assertEquals(30.0, load5.getConstPLoad().getIm(), 0.0);
        assertEquals(ApparentPowerUnitType.MVA, load5.getConstPLoad().getUnit());

        LoadflowBusXmlType bus2 = parser.getAclfBus("Bus2");
        assertNotNull(bus2);
        assertEquals(LFGenCodeEnumType.PV, bus2.getGenData().getCode());
        JAXBElement<? extends LoadflowGenDataXmlType> genElem = bus2.getGenData().getContributeGen().get(0);
        LoadflowGenDataXmlType gen2 = genElem.getValue();
        assertEquals(163.0, gen2.getPower().getRe(), 0.0);
        assertEquals(0.0, gen2.getPower().getIm(), 0.0);
        assertEquals(300.0, gen2.getPLimit().getMax(), 0.0);
        assertEquals(10.0, gen2.getPLimit().getMin(), 0.0);
        assertEquals(300.0, gen2.getQLimit().getMax(), 0.0);
        assertEquals(-300.0, gen2.getQLimit().getMin(), 0.0);

        LineBranchXmlType branch14 = parser.getLineBranch("Bus1", "Bus4", "1");
        assertNotNull(branch14);
        assertEquals(0.0, branch14.getZ().getRe(), 0.0);
        assertEquals(0.0576, branch14.getZ().getIm(), 0.0);
        assertEquals(ZUnitType.PU, branch14.getZ().getUnit());
        assertNotNull(branch14.getRatingLimit());
        assertNotNull(branch14.getRatingLimit().getMva());
        assertEquals(250.0, branch14.getRatingLimit().getMva().getRating1(), 0.0);
    }

    @Test
    public void testCase30() throws Exception {
        AclfModelParser parser = parseCase("testdata/matpower/case30.m");
        LoadflowNetXmlType net = parser.getAclfNet();

        assertEquals("Aclf_from_Matpower_case30", net.getId());
        assertEquals(30, net.getBusList().getBus().size());
        assertEquals(41, net.getBranchList().getBranch().size());
        assertEquals(100.0, net.getBasePower().getValue(), 0.0);
        assertEquals(ApparentPowerUnitType.MVA, net.getBasePower().getUnit());

        LoadflowBusXmlType bus1 = parser.getAclfBus("Bus1");
        assertNotNull(bus1);
        assertEquals(LFGenCodeEnumType.SWING, bus1.getGenData().getCode());
        JAXBElement<? extends LoadflowGenDataXmlType> genElem1 = bus1.getGenData().getContributeGen().get(0);
        LoadflowGenDataXmlType gen1 = genElem1.getValue();
        assertEquals(23.54, gen1.getPower().getRe(), 0.0);
        assertEquals(150.0, gen1.getQLimit().getMax(), 0.0);
        assertEquals(-20.0, gen1.getQLimit().getMin(), 0.0);

        LoadflowBusXmlType bus13 = parser.getAclfBus("Bus13");
        assertNotNull(bus13);
        assertEquals(LFGenCodeEnumType.PV, bus13.getGenData().getCode());
        JAXBElement<? extends LoadflowGenDataXmlType> genElem13 = bus13.getGenData().getContributeGen().get(0);
        LoadflowGenDataXmlType gen13 = genElem13.getValue();
        assertEquals(37.0, gen13.getPower().getRe(), 0.0);
        assertEquals(40.0, gen13.getPLimit().getMax(), 0.0);

        LoadflowBusXmlType bus5 = parser.getAclfBus("Bus5");
        assertNotNull(bus5);
        assertNotNull(bus5.getShuntYData());
        assertNotNull(bus5.getShuntYData().getEquivY());
        assertEquals(0.0019, bus5.getShuntYData().getEquivY().getIm(), 0.0);

        LineBranchXmlType branch12 = parser.getLineBranch("Bus1", "Bus2", "1");
        assertNotNull(branch12);
        assertEquals(0.02, branch12.getZ().getRe(), 0.0);
        assertEquals(0.06, branch12.getZ().getIm(), 0.0);
        assertEquals(130.0, branch12.getRatingLimit().getMva().getRating1(), 0.0);
    }

    @Test
    public void testCase118() throws Exception {
        AclfModelParser parser = parseCase("testdata/matpower/case118.m");
        LoadflowNetXmlType net = parser.getAclfNet();

        assertEquals("Aclf_from_Matpower_case118", net.getId());
        assertEquals(118, net.getBusList().getBus().size());
        assertEquals(186, net.getBranchList().getBranch().size());
        assertEquals(100.0, net.getBasePower().getValue(), 0.0);
        assertEquals(ApparentPowerUnitType.MVA, net.getBasePower().getUnit());

        LoadflowBusXmlType bus69 = parser.getAclfBus("Bus69");
        assertNotNull(bus69);
        assertEquals(LFGenCodeEnumType.SWING, bus69.getGenData().getCode());
        assertEquals(1.035, bus69.getVoltage().getValue(), 0.0);
        assertEquals(30.0, bus69.getAngle().getValue(), 0.0);

        LoadflowBusXmlType bus1 = parser.getAclfBus("Bus1");
        assertNotNull(bus1);
        assertEquals(138.0, bus1.getBaseVoltage().getValue(), 0.0);
        LoadflowLoadDataXmlType load1 = bus1.getLoadData().getContributeLoad().get(0).getValue();
        assertEquals(51.0, load1.getConstPLoad().getRe(), 0.0);
        assertEquals(27.0, load1.getConstPLoad().getIm(), 0.0);

        LoadflowBusXmlType bus5 = parser.getAclfBus("Bus5");
        assertNotNull(bus5);
        assertNotNull(bus5.getShuntYData());
        assertNotNull(bus5.getShuntYData().getEquivY());
        assertEquals(-0.4, bus5.getShuntYData().getEquivY().getIm(), 0.0);

        LineBranchXmlType branch12 = parser.getLineBranch("Bus1", "Bus2", "1");
        assertNotNull(branch12);
        assertEquals(0.0303, branch12.getZ().getRe(), 0.0);
        assertEquals(0.0999, branch12.getZ().getIm(), 0.0);
        assertNotNull(branch12.getRatingLimit());
    }

    @Test
    public void testCase8387Pegase() throws Exception {
        AclfModelParser parser = parseCase("testdata/matpower/case8387pegase.m");
        LoadflowNetXmlType net = parser.getAclfNet();

        assertEquals("Aclf_from_Matpower_case8387pegase", net.getId());
        assertEquals(8387, net.getBusList().getBus().size());
        assertEquals(14561, net.getBranchList().getBranch().size());
        assertEquals(100.0, net.getBasePower().getValue(), 0.0);
        assertEquals(ApparentPowerUnitType.MVA, net.getBasePower().getUnit());

        LoadflowBusXmlType bus1 = parser.getAclfBus("Bus1");
        assertNotNull(bus1);
        assertEquals(220.0, bus1.getBaseVoltage().getValue(), 0.0);
        assertEquals(1.038963, bus1.getVoltage().getValue(), 0.0);
        assertEquals(-34.89481, bus1.getAngle().getValue(), 0.0);
        assertEquals(LFGenCodeEnumType.PQ, bus1.getGenData().getCode());
        LoadflowLoadDataXmlType load1 = bus1.getLoadData().getContributeLoad().get(0).getValue();
        assertEquals(378.4, load1.getConstPLoad().getRe(), 0.0);
        assertEquals(98.5, load1.getConstPLoad().getIm(), 0.0);

        LoadflowBusXmlType bus5 = parser.getAclfBus("Bus5");
        assertNotNull(bus5);
        assertEquals(LFGenCodeEnumType.PV, bus5.getGenData().getCode());
        assertNotNull(bus5.getShuntYData());
        assertNotNull(bus5.getShuntYData().getEquivY());
        assertEquals(0.2, bus5.getShuntYData().getEquivY().getIm(), 0.0);

        LoadflowBusXmlType bus122 = parser.getAclfBus("Bus122");
        assertNotNull(bus122);
        LoadflowLoadDataXmlType load122 = bus122.getLoadData().getContributeLoad().get(0).getValue();
        assertEquals(-75.3836, load122.getConstPLoad().getRe(), 0.0);
        assertEquals(7.8, load122.getConstPLoad().getIm(), 0.0);

        LoadflowBusXmlType bus3853 = parser.getAclfBus("Bus3853");
        assertNotNull(bus3853);
        assertEquals(LFGenCodeEnumType.SWING, bus3853.getGenData().getCode());

        LoadflowGenDataXmlType gen5 = bus5.getGenData().getContributeGen().get(0).getValue();
        assertEquals(1044.4, gen5.getPower().getRe(), 0.0);
        assertEquals(140.4, gen5.getPower().getIm(), 0.0);
        assertEquals(1200.0, gen5.getPLimit().getMax(), 0.0);
        assertEquals(399.999996, gen5.getPLimit().getMin(), 0.0);
        assertEquals(524.5259, gen5.getQLimit().getMax(), 0.0);
        assertEquals(-211.567, gen5.getQLimit().getMin(), 0.0);

        LoadflowBusXmlType bus10 = parser.getAclfBus("Bus10");
        assertNotNull(bus10);
        LoadflowGenDataXmlType gen10 = bus10.getGenData().getContributeGen().get(0).getValue();
        assertEquals(31.25, gen10.getPower().getRe(), 0.0);
        assertEquals(13.32, gen10.getPower().getIm(), 0.0);
        assertTrue(Double.isInfinite(gen10.getPLimit().getMax()));
        assertTrue(Double.isInfinite(gen10.getPLimit().getMin()));
        assertTrue(Double.isInfinite(gen10.getQLimit().getMax()));
        assertTrue(Double.isInfinite(gen10.getQLimit().getMin()));

        LineBranchXmlType line = parser.getLineBranch("Bus4679", "Bus2816", "1");
        assertNotNull(line);
        assertEquals(0.0006, line.getZ().getRe(), 0.0);
        assertEquals(0.00616, line.getZ().getIm(), 0.0);
        assertEquals(ZUnitType.PU, line.getZ().getUnit());
        assertEquals(837.866207, line.getRatingLimit().getMva().getRating1(), 0.0);

        XfrBranchXmlType transformer = parser.getXfrBranch("Bus1216", "Bus6469", "1");
        assertNotNull(transformer);
        assertEquals(0.006356, transformer.getZ().getRe(), 0.0);
        assertEquals(0.142878, transformer.getZ().getIm(), 0.0);
        assertEquals(1.026667, transformer.getFromTurnRatio().getValue(), 0.0);
        assertEquals(1.0, transformer.getToTurnRatio().getValue(), 0.0);
        assertEquals(119.999944, transformer.getRatingLimit().getMva().getRating1(), 0.0);

        PSXfrBranchXmlType phaseShifter = parser.getPSXfrBranch("Bus4709", "Bus466", "1");
        assertNotNull(phaseShifter);
        assertEquals(0.000454, phaseShifter.getZ().getRe(), 0.0);
        assertEquals(0.028943, phaseShifter.getZ().getIm(), 0.0);
        assertEquals(1.013237, phaseShifter.getFromTurnRatio().getValue(), 0.0);
        assertEquals(1.0, phaseShifter.getToTurnRatio().getValue(), 0.0);
        assertEquals(0.61829, phaseShifter.getFromAngle().getValue(), 0.0);
        assertTrue(phaseShifter.getToAngle() == null);
        assertEquals(710.594074, phaseShifter.getRatingLimit().getMva().getRating1(), 0.0);
    }
}