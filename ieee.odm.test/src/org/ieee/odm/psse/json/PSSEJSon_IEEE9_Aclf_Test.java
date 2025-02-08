package org.ieee.odm.psse.json;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.json.PSSEJSonAdapter;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class PSSEJSon_IEEE9_Aclf_Test {
	@Test
	public void testCase1() throws Exception {
		ODMLogger.getLogger().setLevel(Level.WARNING);
		
		PSSEAdapter adapter = new PSSEJSonAdapter();
		assertTrue(adapter.parseInputFile("testdata/psse/json/ieee9.rawx"));
		
		AclfModelParser aclfParser =(AclfModelParser) adapter.getModel();
		aclfParser.stdout();
	}
}

