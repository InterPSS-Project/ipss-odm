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

	
	//@Test
	public void testCaseInputLines() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new IeeeCDFAdapter();
		assertTrue(adapter.parseInputFile("testdata/ieee_format/009ieee.cf"));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		//System.out.println(parser.toXmlDoc());
		
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
//		String filename = "testdata/bpa/014bpa.DAT";
//		File file = new File(filename);
//		final InputStream stream = new FileInputStream(file);
//		final BufferedReader din = new BufferedReader(new InputStreamReader(stream));
//		String content = "";
//		String str = din.readLine();
//		do{
//			content = content+str+"\n";
//			str = din.readLine();
//		}
//		while(str != null);
		
		assertTrue(!adapter.parseFileContent(this.BPA_14_BUS));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		//System.out.println(parser.toXmlDoc());
		
		//assertTrue(parser.getBranch(branchId))
	}
	
	public static String BPA_14_BUS="loadflow\r\n(POWERFLOW,CASEID=EMS,PROJECT=TEST)\r\n/P_INPUT_LIST,FULL\\\r\n/P_OUTPUT_LIST,FULL\\\r\n/P_ANALYSIS_RPT,LEVEL=4\\\r\n/NETWORK_DATA\\\r\n.234567890*234567890*234567890*234567890*234567890*234567890*234567890*234567890\r\nBS    BUS-1    100 1                      232.399990-99991060                                                                                                                                                                                                                                              \r\nBQ    BUS-2    100 121.7 12.7             40.  50.  -40. 1045                                                                                                                                                                                                                                              \r\nBQ    BUS-3    100 194.2 19.                   40.       1010                                                                                                                                                                                                                                              \r\nB     BUS-4    100 147.8 -3.9                                                                                                                                                                                                                                                                              \r\nB     BUS-5    100 17.6  1.6                                                                                                                                                                                                                                                                               \r\nBQ    BUS-6    100 111.2 7.5                   24.  -6.  1070                                                                                                                                                                                                                                              \r\nB     BUS-7    100 1                                                                                                                                                                                                                                                                                       \r\nBQ    BUS-8    100 1                           24.  -6.  1090                                                                                                                                                                                                                                              \r\nB     BUS-9    100 129.5 16.6     19.                                                                                                                                                                                                                                                                      \r\nB     BUS-10   100 19.   5.8                                                                                                                                                                                                                                                                               \r\nB     BUS-11   100 13.5  1.8                                                                                                                                                                                                                                                                               \r\nB     BUS-12   100 16.1  1.6                                                                                                                                                                                                                                                                               \r\nB     BUS-13   100 113.5 5.8                                                                                                                                                                                                                                                                               \r\nB     BUS-14   100 114.9 5.                                                                                                                                                                                                                                                                                \r\n-999\r\n.234567890*234567890*234567890*234567890*234567890*234567890*234567890*234567890\r\n.234567890*234567890*234567890*234567890*234567890*234567890*234567890*234567890\r\nL     BUS-2    100 BUS-1    100       .01938.05917      .0264                                                                                                                                                                                                                                              \r\nL     BUS-3    100 BUS-2    100       .04699.19797      .0219                                                                                                                                                                                                                                              \r\nL     BUS-4    100 BUS-2    100       .05811.17632      .0187                                                                                                                                                                                                                                              \r\nL     BUS-5    100 BUS-1    100       .05403.22304      .0246                                                                                                                                                                                                                                              \r\nL     BUS-5    100 BUS-2    100       .05695.17388      .017                                                                                                                                                                                                                                               \r\nL     BUS-4    100 BUS-3    100       .06701.17103      .0173                                                                                                                                                                                                                                              \r\nL     BUS-5    100 BUS-4    100       .01335.04211      .0064                                                                                                                                                                                                                                              \r\nL     BUS-8    100 BUS-7    100             .17615                                                                                                                                                                                                                                                         \r\nL     BUS-9    100 BUS-7    100             .11001                                                                                                                                                                                                                                                         \r\nL     BUS-10   100 BUS-9    100       .03181.0845                                                                                                                                                                                                                                                          \r\nL     BUS-11   100 BUS-6    100       .09498.1989                                                                                                                                                                                                                                                          \r\nL     BUS-12   100 BUS-6   100       .12291.25581                                                                                                                                                                                                                                                         \r\nL     BUS-13   100 BUS-6    100       .06615.13027                                                                                                                                                                                                                                                         \r\nL     BUS-14   100 BUS-9    100       .12711.27038                                                                                                                                                                                                                                                         \r\nL     BUS-11   100 BUS-10   100       .08205.19207                                                                                                                                                                                                                                                         \r\nL     BUS-13   100 BUS-12   100       .22092.19988                                                                                                                                                                                                                                                         \r\nL     BUS-14   100 BUS-13   100       .17093.34802                                                                                                                                                                                                                                                         \r\nT     BUS-6    100 BUS-5    100             .25202            100. 93.2                                                                                                                                                                                                                                    \r\nT     BUS-7    100 BUS-4    100             .20912            100. 97.8                                                                                                                                                                                                                                    \r\nT     BUS-9    100 BUS-4    100             .55618            100. 96.9                                                                                                                                                                                                                                    \r\n.234567890*234567890*234567890*234567890*234567890*234567890*234567890*234567890\r\n(END)";
	
}
