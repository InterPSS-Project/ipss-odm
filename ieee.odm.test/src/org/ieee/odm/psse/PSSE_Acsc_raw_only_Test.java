package org.ieee.odm.psse;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.junit.Test;

public class PSSE_Acsc_raw_only_Test {
	
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSEAdapter(PsseVersion.PSSE_33);
		assertTrue(adapter.parseInputFile(NetType.AcscNet, new String[]{
				"testData/psse/v33/PSSE_sample_savnw.raw",
				"testData/psse/v33/PSSE_sample_savnw.seq"
				//"testData/psse/IEEE9Bus/ieee9.seq"
		}));
		AcscModelParser acscParser =(AcscModelParser) adapter.getModel();
		acscParser.stdout();
	}

}
