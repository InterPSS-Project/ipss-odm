package org.ieee.odm.bpa;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class AreaTransPowerRecordTest {
	@Test
	public void bpaTestCase() throws Exception {
		IODMAdapter adapter = new BPAAdapter();
		assertTrue(adapter.parseInputFile("testdata/bpa/AreaTransTestData.DAT"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		parser.stdout();
		assertTrue(parser.getNet().getAreaList().getArea().get(0).getName().endsWith("GD"));
		assertTrue(parser.getNet().getInterchangeList().getInterchange().get(0)
				.getAreaTransfer().getAmountMW()==2250.0);
	}

}
