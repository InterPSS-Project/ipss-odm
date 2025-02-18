package org.ieee.odm.psse.raw;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.FactorUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.OwnerXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV31_v36_IEEE9_Test {


	
	
@Test
public void testV31() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_31);
	assertTrue(adapter.parseInputFile("testdata/psse/v31/ieee9_v31.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	//parser.stdout();

	checkData(parser);

}

	
@Test
public void testV32() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_32);
	assertTrue(adapter.parseInputFile("testdata/psse/v32/ieee9_v32.raw"));

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
	assertTrue(adapter.parseInputFile("testdata/psse/v33/ieee9_v33.raw"));

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
	assertTrue(adapter.parseInputFile("testdata/psse/v34/ieee9_v34.raw"));

	AclfModelParser parser = (AclfModelParser) adapter.getModel();
	// parser.stdout();

	checkData(parser);
}

@Test
public void testV35() throws Exception {
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_35);
	assertTrue(adapter.parseInputFile("testdata/psse/v35/ieee9_v35.raw"));

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
	assertTrue(adapter.parseInputFile("testdata/psse/v36/ieee9_v36.raw"));

	AclfModelParser parser = (AclfModelParser)adapter.getModel();
	checkData(parser);

	
}

private void checkData(AclfModelParser parser) {
	//BUS
	
	/*
	*  <aclfBus id="Bus9" areaNumber="1" zoneNumber="1" number="9" offLine="false" name="BUS-9">
	<ownerList id="1">
	<ownership unit="PU" value="1.0"/>
	</ownerList>
	<baseVoltage unit="KV" value="230.0"/>
	<voltage unit="PU" value="1.032"/>
	<angle unit="DEG" value="1.97"/>
	<genData code="NoneGen"/>
	<loadData/>
	<shuntYData/>
	</aclfBus>
	*/
	// Test Bus attributes
	LoadflowBusXmlType bus9 = parser.getBus("Bus9");
	assertEquals("Bus9", bus9.getId());
	assertEquals((Integer)1, bus9.getAreaNumber());
	assertEquals((Integer)1, bus9.getZoneNumber());
	assertFalse(bus9.isOffLine());
	assertEquals("BUS-9", bus9.getName());
	assertEquals(230.0, bus9.getBaseVoltage().getValue(), 0.0001);
	assertEquals(1.032, bus9.getVoltage().getValue(), 0.0001);
	assertEquals(1.97, bus9.getAngle().getValue(), 0.0001);
	
	// Test Generator attributes
	assertEquals(LFGenCodeEnumType.NONE_GEN, bus9.getGenData().getCode());
	
	// Test Load attributes
	assertNotNull(bus9.getLoadData());
	
	//GENERATOR
	
	/*
	*   <aclfBus id="Bus3" areaNumber="1" zoneNumber="1" number="3" offLine="false" name="BUS-3">
	<ownerList id="1">
	<ownership unit="PU" value="1.0"/>
	</ownerList>
	<baseVoltage unit="KV" value="13.8"/>
	<voltage unit="PU" value="1.025"/>
	<angle unit="DEG" value="4.66"/>
	<genData code="PV">
	<contributeGen id="1" offLine="false" name="Gen:1(3)">
	<desc>PSSE Generator 1 at Bus 3</desc>
	<ownerList id="1">
	<ownership unit="PU" value="1.0"/>
	</ownerList>
	<power unit="MVA" re="85.0" im="-10.86"/>
	<desiredVoltage unit="PU" value="1.025"/>
	<qLimit unit="MVAR" max="99990.0" min="-99990.0"/>
	<pLimit unit="MW" max="9999.0" min="-9999.0"/>
	<mvaBase unit="MVA" value="100.0"/>
	<sourceZ unit="PU" re="0.0" im="0.107"/>
	<mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
	</contributeGen>
	</genData>
	<loadData/>
	<shuntYData/>
	</aclfBus>
	*/
	
	LoadflowBusXmlType bus3 = parser.getBus("Bus3");
	assertEquals("Bus3", bus3.getId());
	assertEquals((Integer)1, bus3.getAreaNumber());
	assertEquals((Integer)1, bus3.getZoneNumber());
	assertFalse(bus3.isOffLine());
	assertEquals("BUS-3", bus3.getName());
	assertEquals(13.8, bus3.getBaseVoltage().getValue(), 0.0001);
	assertEquals(1.025, bus3.getVoltage().getValue(), 0.0001);
	assertEquals(4.66, bus3.getAngle().getValue(), 0.0001);
	
	// Create an instance of contributeGen and test its attributes
	LoadflowGenDataXmlType contributeGen = bus3.getGenData().getContributeGen().get(0).getValue();
	assertEquals("Gen:1(3)", contributeGen.getName());
	assertEquals("PSSE Generator 1 at Bus 3", contributeGen.getDesc());
	assertEquals(85.0, contributeGen.getPower().getRe(), 0.0001);
	assertEquals(-10.86, contributeGen.getPower().getIm(), 0.0001);
	assertEquals(1.025, contributeGen.getDesiredVoltage().getValue(), 0.0001);
	assertEquals(99990.0, contributeGen.getQLimit().getMax(), 0.0001);
	assertEquals(-99990.0, contributeGen.getQLimit().getMin(), 0.0001);
	assertEquals(9999.0, contributeGen.getPLimit().getMax(), 0.0001);
	assertEquals(-9999.0, contributeGen.getPLimit().getMin(), 0.0001);
	assertEquals(100.0, contributeGen.getMvaBase().getValue(), 0.0001);
	assertEquals(0.0, contributeGen.getSourceZ().getRe(), 0.0001);
	assertEquals(0.107, contributeGen.getSourceZ().getIm(), 0.0001);
	assertEquals(1.0, contributeGen.getMvarVControlParticipateFactor(), 0.0001);
	
	
	//ACLINE
	
	/*
	*   <aclfLine circuitId="0" id="Bus4_to_Bus6_cirId_0" areaNumber="1" zoneNumber="1" offLine="false">
	<ownerList id="1">
	<ownership unit="PU" value="1.0"/>
	</ownerList>
	<fromBus idRef="Bus4"/>
	<toBus idRef="Bus6"/>
	<z unit="PU" re="0.017" im="0.092"/>
	<ratingLimit/>
	<meterLocation>ToSide</meterLocation>
	<totalShuntY unit="PU" re="0.0" im="0.158"/>
	<lineInfo/>
	</aclfLine>
	*/
	
	LineBranchXmlType line = parser.getLineBranch("Bus4", "Bus6", "0");
	
	// Verify line attributes
	assertEquals("0", line.getCircuitId());
	assertEquals("Bus4_to_Bus6_cirId_0", line.getId());
	assertEquals((Integer)1, line.getAreaNumber());
	assertEquals((Integer)1, line.getZoneNumber());
	assertFalse(line.isOffLine());
	
	// Verify owner list
	assertEquals(1, line.getOwnerList().size());
	OwnerXmlType owner = line.getOwnerList().get(0);
	assertEquals(FactorUnitType.PU, owner.getOwnership().getUnit());
	assertEquals(1.0, owner.getOwnership().getValue(), 0.0001);
	
	
	// Verify impedance
	assertEquals(ZUnitType.PU, line.getZ().getUnit());
	assertEquals(0.017, line.getZ().getRe(), 0.0001);
	assertEquals(0.092, line.getZ().getIm(), 0.0001);
	
	// Verify shunt admittance
	assertEquals(YUnitType.PU, line.getTotalShuntY().getUnit());
	assertEquals(0.0, line.getTotalShuntY().getRe(), 0.0001);
	assertEquals(0.158, line.getTotalShuntY().getIm(), 0.0001);
	
	// Verify meter location
	assertEquals(BranchBusSideEnumType.TO_SIDE, line.getMeterLocation());
	
	
	// Test Shunt attributes
	assertNotNull(bus9.getShuntYData());
	
	// Test Transformer attributes
	/*
		* <aclfXfr circuitId="1" id="Bus3_to_Bus9_cirId_1" areaNumber="1" zoneNumber="1" offLine="false" name="GEN3_TO_BUS3">
			<ownerList id="1">
				<ownership unit="PU" value="1.0"/>
			</ownerList>
			<ownerList id="0">
				<ownership unit="PU" value="1.0"/>
			</ownerList>
			<ownerList id="0">
				<ownership unit="PU" value="1.0"/>
			</ownerList>
			<ownerList id="0">
				<ownership unit="PU" value="1.0"/>
			</ownerList>
			<fromBus idRef="Bus3"/>
			<toBus idRef="Bus9"/>
			<z unit="PU" re="0.0" im="0.0586"/>
			<ratingLimit/>
			<meterLocation>ToSide</meterLocation>
			<fromTurnRatio unit="PU" value="1.0"/>
			<toTurnRatio unit="PU" value="1.0"/>
			<xfrInfo>
				<dataOnSystemBase>true</dataOnSystemBase>
				<ratedPower unit="MVA" value="100.0"/>
			</xfrInfo>
		</aclfXfr>
		*/
	XfrBranchXmlType transformer = parser.getXfrBranch("Bus3", "Bus9", "1");
	assertEquals("1", transformer.getCircuitId());
	assertEquals("Bus3_to_Bus9_cirId_1", transformer.getId());
	assertEquals((Integer)1, transformer.getAreaNumber());
	assertEquals((Integer)1, transformer.getZoneNumber());
	assertFalse(transformer.isOffLine());
	assertEquals("GEN3_TO_BUS3_CIRID_1", transformer.getName());

	// Verify owner list
//	assertEquals(4, transformer.getOwnerList().size());
//	assertTrue(transformer.getOwnerList().size()==4);
	OwnerXmlType owner2 = transformer.getOwnerList().get(0);
	assertEquals(FactorUnitType.PU, owner2.getOwnership().getUnit());
	assertEquals(1.0, owner.getOwnership().getValue(), 0.0001);
	

	// Verify impedance
	assertEquals(ZUnitType.PU, transformer.getZ().getUnit());
	assertEquals(0.0, transformer.getZ().getRe(), 0.0001);
	assertEquals(0.0586, transformer.getZ().getIm(), 0.0001);

	// Verify meter location
	assertEquals(BranchBusSideEnumType.TO_SIDE, transformer.getMeterLocation());

	// Verify turn ratios
	assertEquals(1.0, transformer.getFromTurnRatio().getValue(), 0.0001);
	assertEquals(1.0, transformer.getToTurnRatio().getValue(), 0.0001);

	// Verify transformer info
	assertNotNull(transformer.getXfrInfo());
	assertTrue(transformer.getXfrInfo().isDataOnSystemBase());
	assertEquals(100.0, transformer.getXfrInfo().getRatedPower().getValue(), 0.0001);

}

}



