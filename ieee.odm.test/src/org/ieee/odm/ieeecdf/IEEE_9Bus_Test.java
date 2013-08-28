package org.ieee.odm.ieeecdf;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.junit.Test;

public class IEEE_9Bus_Test {

	
	@Test
	public void testCaseInputLines() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new IeeeCDFAdapter();
		assertTrue(adapter.parseInputFile("testdata/ieee_format/009ieee.cf"));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		System.out.println(parser.toXmlDoc());
		
		//assertTrue(parser.getBranch(branchId))
	}
	
	@Test
	public void testCaseParseInputContent() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		// test using a IEEE CDF adapter to parse data in bpa format
		IODMAdapter adapter = new IeeeCDFAdapter();
		String filename = "testdata/bpa/014bpa.DAT";
		File file = new File(filename);
		final InputStream stream = new FileInputStream(file);
		final BufferedReader din = new BufferedReader(new InputStreamReader(stream));
		String content = "";
		String str = din.readLine();
		do{
			content = content+str;
			str = din.readLine();
		}
		while(str != null);
		
		assertTrue(!adapter.parseFileContent(content));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		System.out.println(parser.toXmlDoc());
		
		//assertTrue(parser.getBranch(branchId))
	}
	
	
}
