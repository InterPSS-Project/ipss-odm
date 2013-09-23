package org.ieee.odm.adapter;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.ODMObjectFactory;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.junit.Test;

public class adapterTest {
	@Test
	public void ver_test() throws FileNotFoundException, ODMException {
		assertTrue(PSSEAdapter.getVersionNo(PSSEAdapter.PsseVersion.PSSE_26) == 26);
	}
	
	@Test
	public void test1() throws FileNotFoundException, ODMException {
		String fileName = "testdata/ieee_format/Ieee14Bus.ieee";
		//IODMModelParser parser = new ODMModelParser();
		
		ODMFileFormatEnum f= ODMFileFormatEnum.PWD;
		//ODMFileFormatEnum f= ODMFileFormatEnum.GePSLF;
		//ODMFileFormatEnum f= ODMFileFormatEnum.UCTE;
		InputStream stream = new FileInputStream(fileName);
		IODMAdapter adapter = ODMObjectFactory.createODMAdapter(f); 
		boolean odmParsingStatus = adapter.parseInputStream(stream);
		IODMModelParser parser = adapter.getModel();
		assertTrue(odmParsingStatus == true);
		assertTrue(parser != null);
	}
}
