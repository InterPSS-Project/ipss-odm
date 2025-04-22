package org.ieee.odm.adapter.psse.raw.parser.aclf;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E  VSC HVDC Data
 * 
 * @author mzhou
 *
 */
public class PSSEVSCHVDC2TDataRawParser extends BasePSSEDataRawParser {
	public PSSEVSCHVDC2TDataRawParser(PsseVersion ver) {
		super(ver);
	}	

	private static String[][] META_DATA_V30_33 = {
		//LINE-1
		{ "NAME",     "MDC",      "RDC",       "O1",       "F1",    "O2",       "F2",        "O3",       "F3",      "O4",     "F4" }, 
		   //LINE-2
		{"IBUS",     "TYPE1",    "MODE1",    "DCSET1",     "ACSET1",       "ALOSS1",   "BLOSS1",  "MINLOSS1",  "SMAX1", 
		
		"IMAX1",        "PWF1",    "MAXQ1",    "MINQ1",     "REMOT1", "RMPCT1"},
		//LINE-3           
		{ "JBUS",     "TYPE2",    "MODE2",    "DCSET2",     "ACSET2",       "ALOSS2",   "BLOSS2",  "MINLOSS2",  "SMAX2", 
		
		"IMAX2",        "PWF2",    "MAXQ2",    "MINQ2",     "REMOT2",      "RMPCT2" }
	};

	
	private static String[][] META_DATA_V34 = {
		//LINE-1
		{ "NAME",     "MDC",      "RDC",       "O1",       "F1",    "O2",       "F2",        "O3",       "F3",      "O4",     "F4" }, 
		   //LINE-2
		{"IBUS",     "TYPE1",    "MODE1",    "DCSET1",     "ACSET1",       "ALOSS1",   "BLOSS1",  "MINLOSS1",  "SMAX1", 
		
		"IMAX1",        "PWF1",    "MAXQ1",    "MINQ1",     "VSREG1", "RMPCT1", "NREG1"},
		//LINE-3           
		{ "JBUS",     "TYPE2",    "MODE2",    "DCSET2",     "ACSET2",       "ALOSS2",   "BLOSS2",  "MINLOSS2",  "SMAX2", 
		
		"IMAX2",        "PWF2",    "MAXQ2",    "MINQ2",     "VSREG2", "RMPCT2", "NREG2" }
	};

	private static String[][] META_DATA_V35_36 = {
		//LINE-1
		{ "NAME",     "MDC",      "RDC",       "O1",       "F1",    "O2",       "F2",        "O3",       "F3",      "O4",     "F4" }, 
		   //LINE-2
		{"IBUS",     "TYPE1",    "MODE1",    "DCSET1",     "ACSET1",       "ALOSS1",   "BLOSS1",  "MINLOSS1",  "SMAX1", 
		
		"IMAX1",        "PWF1",    "MAXQ1",    "MINQ1",     "VSREG1",  "NREG1", "RMPCT1"},
		//LINE-3           
		{ "JBUS",     "TYPE2",    "MODE2",    "DCSET2",     "ACSET2",       "ALOSS2",   "BLOSS2",  "MINLOSS2",  "SMAX2", 
		
		"IMAX2",        "PWF2",    "MAXQ2",    "MINQ2",     "VSREG2",  "NREG2", "RMPCT2"}
	};
	
	@Override public String[] getMetadata() {
		switch(this.version){
			case PSSE_30:
			case PSSE_31:
			case PSSE_32:
			case PSSE_33:
				return convertStringAry2DTo1D(META_DATA_V30_33);
			case PSSE_34:
				return convertStringAry2DTo1D(META_DATA_V34);
			case PSSE_35:
			case PSSE_36:
				return convertStringAry2DTo1D(META_DATA_V35_36);
			default:
				throw new IllegalArgumentException("Unsupported PSS/E version: " + this.version);
	
		}

	}
	
	@Override public void parseFields(final String[] lineStrAry) throws ODMException {
		 this.clearNVPairTableData();
		 
		 String lineStr1 = lineStrAry[0];
		 String lineStr2 = lineStrAry[1];
		 String lineStr3 = lineStrAry[2];

		 
		 // set the number of expectedFieldCount based on v34-36 as default
		 int expectedLine1Num = META_DATA_V35_36[0].length;
		 int expectedLine2Num = META_DATA_V35_36[1].length;
		 int expectedLine3Num = META_DATA_V35_36[2].length;

 
		 
		 // for version 30-33
		 if(PSSERawAdapter.getVersionNo(this.version) < 34) {
			 expectedLine1Num = META_DATA_V30_33[0].length;
			 expectedLine2Num = META_DATA_V30_33[1].length;
			 expectedLine3Num = META_DATA_V30_33[2].length;
		 }
		 
		 //line 1
		 int startingIdx = 0;
		 parseLineStr(lineStr1, startingIdx, expectedLine1Num);
		 
		 //line 2
		 startingIdx += expectedLine1Num;
		 parseLineStr(lineStr2, expectedLine1Num, expectedLine2Num);
		 
		 //line 3
		 startingIdx += expectedLine2Num;
		 parseLineStr(lineStr3, startingIdx, expectedLine3Num);
  	}
		

}
