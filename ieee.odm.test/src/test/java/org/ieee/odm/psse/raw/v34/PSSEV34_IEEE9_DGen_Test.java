package org.ieee.odm.psse.raw.v34;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.junit.Test;

public class PSSEV34_IEEE9_DGen_Test {
	
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_34);
		assertTrue(adapter.parseInputFile("testdata/psse/v34/ieee9_dgen_v34.raw"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();
		
		/*
		 * 0 / END OF BUS DATA, BEGIN LOAD DATA
			@!   I,'ID',STAT,AREA,ZONE,      PL,        QL,        IP,        IQ,        YP,        YQ, OWNER,SCALE,INTRPT,  DGENP,     DGENQ, DGENF
				5,'1 ',   1,   1,   1,   150.000,    60.000,     0.000,     0.000,     0.000,     0.000,   1,    1,  0,     25.000,     10.000,   1
				6,'1 ',   1,   1,   1,    90.000,    30.000,     0.000,     0.000,     0.000,     0.000,   1,    1,  0,     10.000,     5.000,   0
				8,'1 ',   1,   1,   1,   100.000,    35.000,     0.000,     0.000,     0.000,     0.000,   1,    1,  0,     0.000,     0.000,   0
		 */
			
		// Test distributed generation in v34 format
		LoadflowBusXmlType bus5 = parser.getBus("Bus5");
		assertNotNull("Bus5 should exist", bus5);
		assertFalse(bus5.getLoadData().getContributeLoad().isEmpty());
		LoadflowLoadDataXmlType load5 = bus5.getLoadData().getContributeLoad().get(0).getValue();
		// Check load data P and Q
		assertEquals(150.0, load5.getConstPLoad().getRe(), 0.0001);
		assertEquals(60.0, load5.getConstPLoad().getIm(), 0.0001);
		// Check load data I and Z components
		assertNull(load5.getConstILoad());
		assertNull(load5.getConstZLoad());


		// Check distributed generation data
		assertEquals(25.0, load5.getDGenPower().getRe(), 0.0001);
		assertEquals(10.0, load5.getDGenPower().getIm(), 0.0001);
		assertTrue(load5.isDGenStatus());
		
		// Test bus6 with distributed gen that is off
		LoadflowBusXmlType bus6 = parser.getBus("Bus6");
		LoadflowLoadDataXmlType load6 = bus6.getLoadData().getContributeLoad().get(0).getValue();
		assertEquals(10.0, load6.getDGenPower().getRe(), 0.0001);
		assertEquals(5.0, load6.getDGenPower().getIm(), 0.0001);
		assertFalse(load6.isDGenStatus());

		// Test bus8 with no distributed gen
		LoadflowBusXmlType bus8 = parser.getBus("Bus8");
		assertFalse(bus8.getLoadData().getContributeLoad().isEmpty());
		LoadflowLoadDataXmlType load8 = bus8.getLoadData().getContributeLoad().get(0).getValue();
		//assertnull (load8.getDGenPower()); // should be null since no DGen
		assertNull(load8.getDGenPower());
		assertNull(load8.isDGenStatus());
	}

}
