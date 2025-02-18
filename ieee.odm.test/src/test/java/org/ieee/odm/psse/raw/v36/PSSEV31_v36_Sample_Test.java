package org.ieee.odm.psse.raw.v36;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.FactorUnitType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.OwnerXmlType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV31_v36_Sample_Test {


	
	
@Test
public void testV31() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_31);
	assertTrue(adapter.parseInputFile("testdata/psse/v31/sample_v31.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	parser.stdout();
	
	parser.toXmlDoc("out/sample_V31.xml");

	checkData(parser);

}

	
@Test
public void testV32() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_32);
	assertTrue(adapter.parseInputFile("testdata/psse/v32/sample_v32.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	//parser.stdout();

	checkData(parser);

}

@Test
public void testV33() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_33);
	assertTrue(adapter.parseInputFile("testdata/psse/v33/sample_v33.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	//parser.stdout();

	checkData(parser);

}

@Test
public void testV34() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);

	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_34);
	assertTrue(adapter.parseInputFile("testdata/psse/v34/sample_v34.raw"));

	AclfModelParser parser = (AclfModelParser) adapter.getModel();
	

	checkData(parser);
}

@Test
public void testV35() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_35);
	assertTrue(adapter.parseInputFile("testdata/psse/v35/sample_v35.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	checkData(parser);

	
}

@Test
public void testV36() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_36);
	assertTrue(adapter.parseInputFile("testdata/psse/v36/sample_v36.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	checkData(parser);

	
}

private void checkData(AclfModelParser parser) {
	// Check Bus 102
    LoadflowBusXmlType bus102 = parser.getBus("Bus102");
    assertEquals("Bus102", bus102.getId());
    assertEquals((Integer)1, bus102.getAreaNumber());
    assertEquals((Integer)1, bus102.getZoneNumber());
    assertFalse(bus102.isOffLine());
    assertEquals("NUC-B", bus102.getName());
    assertEquals(21.6, bus102.getBaseVoltage().getValue(), 0.0001);
    assertEquals(1.01, bus102.getVoltage().getValue(), 0.0001);
    assertEquals(-16.3595, bus102.getAngle().getValue(), 0.0001);

    LoadflowGenDataXmlType gen102 = bus102.getGenData().getContributeGen().get(0).getValue();
    assertEquals("Gen:1(102)", gen102.getName());
    assertEquals("PSSE Generator 1 at Bus 102", gen102.getDesc());
    assertEquals(650.0, gen102.getPower().getRe(), 0.0001);
    assertEquals(150.015, gen102.getPower().getIm(), 0.0001);
    assertEquals(1.01, gen102.getDesiredVoltage().getValue(), 0.0001);
    assertEquals(406.25, gen102.getQLimit().getMax(), 0.0001);
    assertEquals(-62.5, gen102.getQLimit().getMin(), 0.0001);
    assertEquals(700.0, gen102.getPLimit().getMax(), 0.0001);
    assertEquals(33.0, gen102.getPLimit().getMin(), 0.0001);
    assertEquals(950.0, gen102.getMvaBase().getValue(), 0.0001);
    assertEquals(0.0105, gen102.getSourceZ().getRe(), 0.0001);
    assertEquals(0.32, gen102.getSourceZ().getIm(), 0.0001);
    assertEquals(1.0, gen102.getMvarVControlParticipateFactor(), 0.0001);

    // Check Bus 402
    LoadflowBusXmlType bus402 = parser.getBus("Bus402");
    assertEquals("Bus402", bus402.getId());
    assertEquals((Integer)6, bus402.getAreaNumber());
    assertEquals((Integer)9, bus402.getZoneNumber());
    assertFalse(bus402.isOffLine());
    assertEquals("COGEN-2", bus402.getName());
    assertEquals(500.0, bus402.getBaseVoltage().getValue(), 0.0001);
    assertEquals(1.0, bus402.getVoltage().getValue(), 0.0001);
    assertEquals(0.0, bus402.getAngle().getValue(), 0.0001);

    LoadflowGenDataXmlType gen402 = bus402.getGenData().getContributeGen().get(0).getValue();
    assertEquals("Gen:1(402)", gen402.getName());
    assertEquals("PSSE Generator 1 at Bus 402", gen402.getDesc());
    assertEquals(321.0, gen402.getPower().getRe(), 0.0001);
    assertEquals(142.325, gen402.getPower().getIm(), 0.0001);
    assertEquals(1.0, gen402.getDesiredVoltage().getValue(), 0.0001);
    assertEquals(610.0, gen402.getQLimit().getMax(), 0.0001);
    assertEquals(-110.0, gen402.getQLimit().getMin(), 0.0001);
    assertEquals(351.0, gen402.getPLimit().getMax(), 0.0001);
    assertEquals(26.0, gen402.getPLimit().getMin(), 0.0001);
    assertEquals(610.0, gen402.getMvaBase().getValue(), 0.0001);
    assertEquals(0.0045, gen402.getSourceZ().getRe(), 0.0001);
    assertEquals(0.2432, gen402.getSourceZ().getIm(), 0.0001);
    assertEquals(0.91, gen402.getMvarVControlParticipateFactor(), 0.0001);

    // Check Zone
    assertEquals(9, parser.getAclfNet().getLossZoneList().getLossZone().size());
    NetZoneXmlType zone = parser.getAclfNet().getLossZoneList().getLossZone().get(0);
    assertEquals("1", zone.getId());
    assertEquals((Integer)1, zone.getNumber());
    assertEquals("NORTH_A1", zone.getName().trim());

    // Check Owner
    assertEquals(5, parser.getAclfNet().getOwnerList().size());
    OwnerXmlType owner = parser.getAclfNet().getOwnerList().get(0);
    assertEquals("1", owner.getId());
    assertEquals((Integer)1, owner.getNumber());
    assertEquals("OWNER 1", owner.getName().trim());

    // Check Interchange
    assertEquals(5, parser.getAclfNet().getInterchangeList().getInterchange().size());
    InterchangeXmlType interchange = parser.getAclfNet().getInterchangeList().getInterchange().get(0);
    assertEquals("A", interchange.getAreaTransfer().getId().trim());
    assertEquals(1, interchange.getAreaTransfer().getFromArea());
    assertEquals(2, interchange.getAreaTransfer().getToArea());
    assertEquals(1000.0, interchange.getAreaTransfer().getAmountMW(), 0.0001);

    // Check AC Line
    LineBranchXmlType line = parser.getLineBranch("Bus151", "Bus152", "1");
    assertEquals("1", line.getCircuitId());
    assertEquals("Bus151_to_Bus152_cirId_1", line.getId());
    assertEquals((Integer) 1, line.getAreaNumber());
    assertEquals((Integer) 1, line.getZoneNumber());
    assertFalse(line.isOffLine());
    assertEquals(4, line.getOwnerList().size());
    assertEquals(0.0026, line.getZ().getRe(), 0.0001);
    assertEquals(0.046, line.getZ().getIm(), 0.0001);
    assertEquals(1200.0, line.getRatingLimit().getMva().getRating1(), 0.0001);
    assertEquals(0.0, line.getTotalShuntY().getRe(), 0.0001);
    assertEquals(3.5, line.getTotalShuntY().getIm(), 0.0001);

    // Check Transformer
    XfrBranchXmlType transformer = parser.getXfrBranch("Bus101", "Bus151", "T1");
    assertEquals("T1", transformer.getCircuitId());
    assertEquals("Bus101_to_Bus151_cirId_T1", transformer.getId());
    assertEquals((Integer) 1, transformer.getAreaNumber());
    assertEquals((Integer) 1, transformer.getZoneNumber());
    assertFalse(transformer.isOffLine());
    assertTrue( transformer.getName().contains("EL DORADO NU"));
    assertEquals(4, transformer.getOwnerList().size());
    assertEquals(9.166666666666667E-5, transformer.getZ().getRe(), 0.0001);
    assertEquals(0.007583333333333333, transformer.getZ().getIm(), 0.0001);
    assertEquals(1200.0, transformer.getRatingLimit().getMva().getRating1(), 0.0001);
    assertEquals(1.0, transformer.getFromTurnRatio().getValue(), 0.0001);
    assertEquals(1.0, transformer.getToTurnRatio().getValue(), 0.0001);
    assertEquals(0.17147, transformer.getMagnitizingY().getRe(), 0.0001);
    assertEquals(-0.10288, transformer.getMagnitizingY().getIm(), 0.0001);
    assertNotNull(transformer.getTapAdjustment());
    assertEquals(TapAdjustmentEnumType.VOLTAGE, transformer.getTapAdjustment().getAdjustmentType());
    assertEquals(1.0499999999999998, transformer.getTapAdjustment().getTapLimit().getMax(), 0.0001);
    assertEquals(0.95, transformer.getTapAdjustment().getTapLimit().getMin(), 0.0001);
    assertEquals((Integer)25, transformer.getTapAdjustment().getTapAdjSteps());
    assertTrue(transformer.getTapAdjustment().isTapAdjOnFromSide());
    assertEquals(AdjustmentModeEnumType.RANGE_ADJUSTMENT, transformer.getTapAdjustment().getVoltageAdjData().getMode());
    assertEquals(1.05, transformer.getTapAdjustment().getVoltageAdjData().getRange().getMax(), 0.0001);
    assertEquals(0.95, transformer.getTapAdjustment().getVoltageAdjData().getRange().getMin(), 0.0001);
    //assertEquals("Bus101", transformer.getTapAdjustment().getVoltageAdjData().getAdjVoltageBus().getIdRef());
    assertNotNull(transformer.getXfrInfo());
    assertTrue(transformer.getXfrInfo().isDataOnSystemBase());
    assertEquals(1200.0, transformer.getXfrInfo().getRatedPower().getValue(), 0.0001);
}

}



