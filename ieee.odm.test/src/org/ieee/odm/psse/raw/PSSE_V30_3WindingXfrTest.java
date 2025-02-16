package org.ieee.odm.psse.raw;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.junit.Test;

public class PSSE_V30_3WindingXfrTest {

	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile("testData/psse/v30/IEEE9Bus/ieee9_3WXfr.raw"));
		System.out.println(adapter.getModel().toXmlDoc());
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		LoadflowNetXmlType net = parser.getNet();
		assertTrue(net.getBasePower().getValue() == 100.0);
	}
}
