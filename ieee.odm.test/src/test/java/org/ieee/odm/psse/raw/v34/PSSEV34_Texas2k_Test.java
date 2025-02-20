package org.ieee.odm.psse.raw.v34;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class PSSEV34_Texas2k_Test {
	
	@Test
	public void testSummerPeak_noSub() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_34);
		assertTrue(adapter.parseInputFile("testdata/psse/v34/Texas2k_series24_case3_2024summerpeak_noSub.RAW"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();		
	}
	
	@Test
	public void testSummerPeak() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_34);
		assertTrue(adapter.parseInputFile("testdata/psse/v34/Texas2k_series24_case3_2024summerpeak.RAW"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();		
	}

}
