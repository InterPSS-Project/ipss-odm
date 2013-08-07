package org.ieee.odm.bpa;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class BPA_500kv_NoDC_Test {
	@Test
	public void bpaTestCase() throws Exception {
		IODMAdapter adapter = new BPAAdapter();
//		IODMAdapter adapter =new PSSEV30Adapter();
		assertTrue(adapter.parseInputFile("EQ_0907_500KV-NoDC.dat"));//EQ_0907_500KV-N0DC.dat
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		parser.stdout();
//		String xml=parser.toXmlDoc(false);
//		FileOutputStream out=new FileOutputStream(new File("500kvNoDC_BPA_ODM_0408.xml"));
//		out.write(xml.getBytes());
//		out.flush();
//		out.close();
		
	}
}
