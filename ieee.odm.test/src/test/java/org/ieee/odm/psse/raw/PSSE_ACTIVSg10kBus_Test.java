package org.ieee.odm.psse.raw;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import static org.junit.Assert.assertTrue;

public class PSSE_ACTIVSg10kBus_Test  {
	
	//@Test
	public void test_ACTIVSg10k_v33() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_33);
		assertTrue(adapter.parseInputFile("testdata/psse/v33/ACTIVSg10k_v33.RAW"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();


		// Line Str = 50203,50204,50206,'1 ',1,1,1,0.00000E0,0.00000E0,2,'            ',1,   1,1.0000,   0,1.0000,   0,1.0000,   0,1.0000,' 
		
		//parser.stdout();		
	}

		
		

}
