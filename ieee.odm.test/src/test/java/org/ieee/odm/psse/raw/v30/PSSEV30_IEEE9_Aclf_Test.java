package org.ieee.odm.psse.raw.v30;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class PSSEV30_IEEE9_Aclf_Test {
	@Test
	public void testCase1() throws Exception {
		ODMLogger.getLogger().setLevel(Level.WARNING);
		
		PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile("testdata/psse/v30/IEEE9Bus/ieee9.raw"));
		
		AclfModelParser aclfParser =(AclfModelParser) adapter.getModel();
		//aclfParser.stdout();
	}
}

