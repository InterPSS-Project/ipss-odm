package org.ieee.odm.ieeecdf;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class IEEE_UCTE2000_Test {
	
	@Test
	public void UCTE2000TestCase() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new IeeeCDFAdapter();
		assertTrue(adapter.parseInputFile("testdata/ieee_format/UCTE_2002_Winter_Offpeak.CF"));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		//System.out.println(parser.toXmlDoc());
		
		//assertTrue(parser.getBranch(branchId))
	}

}
