package org.ieee.odm.psse;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class PSSEV33_Sample_savnw_Test {
	
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSEAdapter(PsseVersion.PSSE_33);
		assertTrue(adapter.parseInputFile("testdata/psse/v33/PSSE_sample_savnw.raw"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		parser.stdout();		
	}

}
