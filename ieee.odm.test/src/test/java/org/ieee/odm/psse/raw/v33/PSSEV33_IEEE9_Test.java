package org.ieee.odm.psse.raw.v33;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.StaticVarCompensatorXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV33_IEEE9_Test {
	
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_33);
		assertTrue(adapter.parseInputFile("testdata/psse/v33/ieee9_v33.raw"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		NetworkXmlType net = parser.getNet();
		
		// Test the network attributes
		assertEquals("Base frequency", 60.0, net.getFrequency().getValue(), 0.0001);

		
		// Test Bus1 data
		LoadflowBusXmlType bus1 = parser.getBus("Bus1");
		assertNotNull("Bus1 should exist", bus1);
		assertEquals("Bus1", bus1.getId());
		assertEquals(Integer.valueOf(1), bus1.getAreaNumber());
		assertEquals(Integer.valueOf(1), bus1.getZoneNumber());
		assertFalse(bus1.isOffLine());
		assertEquals("BUS-1", bus1.getName());
		assertEquals(16.5, bus1.getBaseVoltage().getValue(), 0.0001);
		assertEquals(1.04, bus1.getVoltage().getValue(), 0.0001);
		assertEquals(0.0, bus1.getAngle().getValue(), 0.0001);
		
		// Test generator data for Bus1
		assertTrue(bus1.getGenData().getContributeGen().size() > 0);
		LoadflowGenDataXmlType gen1 = bus1.getGenData().getContributeGen().get(0).getValue();
		assertEquals("Gen:1(1)", gen1.getName());
		assertEquals("PSSE Generator 1 at Bus 1", gen1.getDesc());
		assertEquals(71.64, gen1.getPower().getRe(), 0.0001);
		assertEquals(27.05, gen1.getPower().getIm(), 0.0001);
		assertEquals(1.04, gen1.getDesiredVoltage().getValue(), 0.0001);
		assertEquals(9999.0, gen1.getQLimit().getMax(), 0.0001);
		assertEquals(-9999.0, gen1.getQLimit().getMin(), 0.0001);
		assertEquals(100.0, gen1.getMvaBase().getValue(), 0.0001);
		assertEquals(0.0, gen1.getSourceZ().getRe(), 0.0001);
		assertEquals(0.04, gen1.getSourceZ().getIm(), 0.0001);
		
		// Test Bus2 data
		LoadflowBusXmlType bus2 = parser.getBus("Bus2");
		assertNotNull("Bus2 should exist", bus2);
		assertEquals("Bus2", bus2.getId());
		assertEquals(Integer.valueOf(1), bus2.getAreaNumber());
		assertEquals(Integer.valueOf(1), bus2.getZoneNumber());
		assertEquals("BUS-2", bus2.getName());
		assertEquals(18.0, bus2.getBaseVoltage().getValue(), 0.0001);
		assertEquals(1.025, bus2.getVoltage().getValue(), 0.0001);
		assertEquals(9.28, bus2.getAngle().getValue(), 0.0001);
		
		// Test generator data for Bus2
		assertTrue(bus2.getGenData().getContributeGen().size() > 0);
		LoadflowGenDataXmlType gen2 = bus2.getGenData().getContributeGen().get(0).getValue();
		assertEquals(163.0, gen2.getPower().getRe(), 0.0001);
		assertEquals(6.65, gen2.getPower().getIm(), 0.0001);
		assertEquals(0.089, gen2.getSourceZ().getIm(), 0.0001);
		
		// Test Bus 5 load data
		LoadflowBusXmlType bus5 = parser.getBus("Bus5");
		assertNotNull("Bus5 should exist", bus5);
		assertEquals("Bus5", bus5.getId());
		assertFalse(bus5.getLoadData().getContributeLoad().isEmpty());
		LoadflowLoadDataXmlType load5 = bus5.getLoadData().getContributeLoad().get(0).getValue();
		assertEquals("1", load5.getId());
		assertEquals(125.0, load5.getConstPLoad().getRe(), 0.0001);
		assertEquals(50.0, load5.getConstPLoad().getIm(), 0.0001);
		
		// Test transformer data
		BaseBranchXmlType xfr1 = parser.getBranch("Bus2", "Bus7", "1");
		assertNotNull("Transformer Bus2-Bus7 should exist", xfr1);
		assertEquals("Bus2_to_Bus7_cirId_1", xfr1.getId());
		assertEquals("GEN2_TO_BUS2_CIRID_1", xfr1.getName());
		XfrBranchXmlType xfr1Rec = (XfrBranchXmlType)xfr1;
		assertEquals(0.0, xfr1Rec.getZ().getRe(), 0.0001);
		assertEquals(0.0625, xfr1Rec.getZ().getIm(), 0.0001);
		assertEquals(1.0, xfr1Rec.getFromTurnRatio().getValue(), 0.0001);
		assertEquals(1.0, xfr1Rec.getToTurnRatio().getValue(), 0.0001);
	}

	@Test
	public void testSVC() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_33);
		assertTrue(adapter.parseInputFile("testdata/psse/v33/ieee9_svc_v33.raw"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		NetworkXmlType net = parser.getNet();

		//svc attached to bus 5
		LoadflowBusXmlType bus5 = parser.getBus("Bus5");

		
		/*
		 * @!  'NAME',         I,     J,MODE,PDES,   QDES,  VSET,   SHMX,   TRMX,   VTMN,   VTMX,   VSMX,    IMX,   LINX,   RMPCT,OWNER,  SET1,    SET2,VSREF, FCREG,NREG,   'MNAME'
			"SVC1",5,     0, 1,  0.000,  0.000,1.00,50.000,  0.000,0.90000,1.10000,1.00000,  0.000,0.05000,  100.0, 0, 0.00000, 0.00000,   0, 0,   0,"   
		 */
		
		StaticVarCompensatorXmlType svc1 = bus5.getSvc();
		assertNotNull("SVC should exist at Bus5", svc1);
		assertEquals("SVC1", svc1.getName());
		//vset
		assertEquals(1.00, svc1.getVoltageSetPoint().getValue(), 0.0001);
		//shmx: check the CapacitiveRating
		assertEquals(50.0, svc1.getCapacitiveRating().getValue(), 0.0001);
		
		//rmpct
		assertEquals(100.0, svc1.getRemoteControlledPercentage(), 0.0001);


	}
		
	

}
