/*
 * @(#)PSSEXfrDataParser.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @Author Mike Zhou
 * @Version 1.0
 * @Date 04/11/2013
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.psse.raw.parser.aclf;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E Transformer data
 * 
 * @author mzhou
 *
 */
public class PSSEXfrDataRawParser extends BasePSSEDataRawParser {
	public PSSEXfrDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	 /* 
	 * format V29
	 * ==========
	 * 
	 * 3W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD,  CONT,RMA, RMI,  VMA, VMI,  NTP,TAB,CR,CX
Line-4      WINDV2,  NOMV2,  ANG2, RATA2,RATB2,RATC2, 
Line-5 		WINDV3,  NOMV3,  ANG3, RATA3,RATB3,RATC3,  

	 * 2W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD,  CONT,RMA, RMI,  VMA, VMI,  NTP,TAB,CR,CX
Line-4      WINDV2,  NOMV2

		
	 * format V30
	 * ==========
	 * 
	 * 3W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD,  CONT,RMA, RMI,  VMA, VMI,  NTP,TAB,CR,CX
Line-4      WINDV2,  NOMV2,  ANG2, RATA2,RATB2,RATC2,  COD2, CONT2,RMA2,RMI2,VMA2,VMI2, NTP2,TAB2,CR2,CX2
Line-5 		WINDV3,  NOMV3,  ANG3, RATA3,RATB3,RATC3,  COD3, CONT3,RMA3,RMI3,VMA3,VMI3, NTP3,TAB3,CR3,CX3

	 * 2W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2s
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD,  CONT,RMA, RMI,  VMA, VMI,  NTP,TAB,CR,CX
Line-4      WINDV2,  NOMV2


	 * format V32
	 * ==========
	 * 3W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1,VMI1, NTP1,TAB1,CR1,CX1
Line-4      WINDV2,  NOMV2,  ANG2, RATA2,RATB2,RATC2,  COD2, CONT2,RMA2,RMI2,VMA2,VMI2, NTP2,TAB2,CR2,CX2
Line-5 		WINDV3,  NOMV3,  ANG3, RATA3,RATB3,RATC3,  COD3, CONT3,RMA3,RMI3,VMA3,VMI3, NTP3,TAB3,CR3,CX3

	 * 2W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4
Line-2 		R1-2,X1-2,SBASE1-2
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1, VMI1,  NTP1,TAB1,CR1,CX1
Line-4      WINDV2,  NOMV2

      Format V30 and V32, data structure is the same, only renamed some of the fields, for example COD -> COD1
      
	 * format V33
	 * ==========
	 * 3W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4,VECGRP
Line-2 		R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1,VMI1, NTP1,TAB1,CR1,CX1
Line-4      WINDV2,  NOMV2,  ANG2, RATA2,RATB2,RATC2,  COD2, CONT2,RMA2,RMI2,VMA2,VMI2, NTP2,TAB2,CR2,CX2
Line-5 		WINDV3,  NOMV3,  ANG3, RATA3,RATB3,RATC3,  COD3, CONT3,RMA3,RMI3,VMA3,VMI3, NTP3,TAB3,CR3,CX3

	 * 2W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4,VECGRP
Line-2 		R1-2,X1-2,SBASE1-2
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1, VMI1,  NTP1,TAB1,CR1,CX1
Line-4      WINDV2,  NOMV2
      
      VECGRP	Alphanumeric identifier specifying vector group based on transformer winding connections and phase angles. 
      			VECGRP value is used for information purpose only.  VECGRP is 12 blanks by default.
      			
      			
     * format V34 V35 V36
	 * =================
	 * 3W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4,VECGRP, ZCOD
Line-2 		R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1,VMI1, NTP1,TAB1,CR1,CX1,CNXA1
Line-4      WINDV2,  NOMV2,  ANG2, RATA2,RATB2,RATC2,  COD2, CONT2,RMA2,RMI2,VMA2,VMI2, NTP2,TAB2,CR2,CX2,CNXA2
Line-5 		WINDV3,  NOMV3,  ANG3, RATA3,RATB3,RATC3,  COD3, CONT3,RMA3,RMI3,VMA3,VMI3, NTP3,TAB3,CR3,CX3,CNXA3

	 * 2W
Line-1  	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR, NAME, STAT,O1,F1,...,O4,F4,VECGRP, ZCOD
Line-2 		R1-2,X1-2,SBASE1-2
Line-3 	 	WINDV1,  NOMV1,  ANG1, RATA1,RATB1,RATC1,  COD1, CONT1,RMA1,RMI1,VMA1, VMI1,  NTP1,TAB1,CR1,CX1, CNXA1
Line-4      WINDV2,  NOMV2
      
      VECGRP	Alphanumeric identifier specifying vector group based on transformer winding connections and phase angles. 
      			VECGRP value is used for information purpose only.  VECGRP is 12 blanks by default.
    */

	// The data structure applies to both 2W and 3W Xfr
	
	private static String[][] META_DATA_v30_33 =  new String[][] {{
			   //	Line-1
			   //  0----------1----------2----------3----------4
				  "I",       "J",       "K",       "CKT",     "CW",             
			   //  5          6          7          8          9
				  "CZ",      "CM",      "MAG1",    "MAG2",    "NMETR",
			   //  10         11         12         13         14
				  "NAME",    "STAT",    "O1",      "F1",      "O2", 
			   //  15         16         17         18         19
				  "F2",       "O3",     "F3",      "O4",      "F4",
			   //  20	  
				  "VECGRP"},                                            // added V33
					  
	           //
			   //	Line-2 (start position 21)
			   //  0----------1----------2----------3----------4
		       { "R1-2",    "X1-2",   "SBASE1-2", "R2-3",   "X2-3",
			   //  5          6          7          8          9
				  "SBASE2-3", "R3-1",   "X3-1",   "SBASE3-1", "VMSTAR",
			   //  10
				  "ANSTAR"},            

			   //
			   //	Line-3 (start position 32)
			   //  0----------1----------2----------3----------4
		       { "WINDV1",  "NOMV1",   "ANG1",    "RATA1",  "RATB1",
			   //  5          6          7          8          9
				  "RATC1",   "COD1",     "CONT1",    "RMA1",    "RMI1",
			   //  10         11         12         13         14
				  "VMA1",     "VMI1",     "NTP1",     "TAB1",     "CR1",
			   //  15
				  "CX1"},             

			   //
			   //	Line-4 (start position 48)
			   //  0----------1----------2----------3----------4
		       { "WINDV2",  "NOMV2",  "ANG2",    "RATA2",   "RATB2",
			   //  5          6          7          8          9
				  "RATC2",   
				             "COD2",   "CONT2",   "RMA2",    "RMI2",   // V30 and late version
			   //  10         11         12         13         14
				  "VMA2",    "VMI2",   "NTP2",    "TAB2",    "CR2",    // V30 and late version
			   //  15
				  "CX2"},                                               // V30 and late version
			
			   //
			   //	Line-5 (start position 64)
			   //  0----------1----------2----------3----------4
		       {"WINDV3",  "NOMV3",   "ANG3",   "RATA3",    "RATB3",
			   //  5          6          7          8          9
				  "RATC3",   
				             "COD3",    "CONT3",  "RMA3",     "RMI3",   // V30 and late version
			   //  10         11         12         13         14
				  "VMA3",    "VMI3",    "NTP3",   "TAB3",     "CR3",    // V30 and late version
				// 15
				  "CX3"}                                                 // V30 and late version
	};
	
	private static String[][] META_DATA_v34 = new String[][] {{
		   //	Line-1
		   //  0----------1----------2----------3----------4
			  "I",       "J",       "K",       "CKT",     "CW",             
		   //  5          6          7          8          9
			  "CZ",      "CM",      "MAG1",    "MAG2",    "NMETR",
		   //  10         11         12         13         14
			  "NAME",    "STAT",    "O1",      "F1",      "O2", 
		   //  15         16         17         18         19
			  "F2",       "O3",     "F3",      "O4",      "F4",
		   //  20	  
			  "VECGRP",  "ZCOD"},                                        
				  
        //
		   //	Line-2 (start position 21)
		   //  0----------1----------2----------3----------4
		   {"R1-2",    "X1-2",   "SBASE1-2", "R2-3",   "X2-3",
		   //  5          6          7          8          9
			  "SBASE2-3", "R3-1",   "X3-1",   "SBASE3-1", "VMSTAR",
		   //  10
			  "ANSTAR"},            

		   //
		   //	Line-3 (start position 32)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV1",  "NOMV1",   "ANG1",    "RATE11",  "RATE21","RATE31",  "RATE41","RATE51",  "RATE61",  "RATE71",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE81",    "RATE91",  "RATE101", "RATE111",   "RATE121", "COD1",     "CONT1",     "RMA1",    "RMI1",  "VMA1",
		    //  20         21          22         23      24             25          26
			  "VMI1",     "NTP1",     "TAB1",     "CR1",   "CX1",       "CNXA1",  "NODE1"},  //NOTE in V34 NODE is at the end of the line, but in V35-36, it is moved to after CONT
		
			       

		   //
		   //	Line-4 (start position 48)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV2",  "NOMV2",   "ANG2",    "RATE12",  "RATE22","RATE32",  "RATE42","RATE52",  "RATE62",  "RATE72",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE82",    "RATE92",  "RATE102", "RATE112",   "RATE122", "COD2",     "CONT2",    "RMA2",    "RMI2",   "VMA2", 
		    //  20         21          22         23      24             25          26
			  "VMI2",     "NTP2",     "TAB2",     "CR2",   "CX2",      "CNXA2",   "NODE2"},  
		
		   //
		   //	Line-5 (start position 64)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV3",  "NOMV3",   "ANG3",    "RATE13",  "RATE23","RATE33",  "RATE43","RATE53",  "RATE63",  "RATE73",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE83",    "RATE93",  "RATE103", "RATE113",   "RATE123", "COD3",     "CONT3",    "RMA3",    "RMI3",    "VMA3",  
		    //  20         21          22         23      24             25          26
			"VMI3",     "NTP3",     "TAB3",     "CR3",   "CX3",     "CNXA3",      "NODE3" },  
		
	};
	
	private static String[][] META_DATA_v35_36 = new String[][] {{
		   //	Line-1
		   //  0----------1----------2----------3----------4
			  "I",       "J",       "K",       "CKT",     "CW",             
		   //  5          6          7          8          9
			  "CZ",      "CM",      "MAG1",    "MAG2",    "NMETR",
		   //  10         11         12         13         14
			  "NAME",    "STAT",    "O1",      "F1",      "O2", 
		   //  15         16         17         18         19
			  "F2",       "O3",     "F3",      "O4",      "F4",
		   //  20	  
			  "VECGRP",  "ZCOD"},                                        
				  
           //
		   //	Line-2 (start position 21)
		   //  0----------1----------2----------3----------4
		   {"R1-2",    "X1-2",   "SBASE1-2", "R2-3",   "X2-3",
		   //  5          6          7          8          9
			  "SBASE2-3", "R3-1",   "X3-1",   "SBASE3-1", "VMSTAR",
		   //  10
			  "ANSTAR"},            

		   //
		   //	Line-3 (start position 32)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV1",  "NOMV1",   "ANG1",    "RATE11",  "RATE21","RATE31",  "RATE41","RATE51",  "RATE61",  "RATE71",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE81",    "RATE91",  "RATE101", "RATE111",   "RATE121", "COD1",     "CONT1",    "NODE1",   "RMA1",    "RMI1",
		    //  20         21          22         23      24             25          26
			  "VMA1",     "VMI1",     "NTP1",     "TAB1",     "CR1",   "CX1",       "CNXA1"},  
		
			       

		   //
		   //	Line-4 (start position 48)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV2",  "NOMV2",   "ANG2",    "RATE12",  "RATE22","RATE32",  "RATE42","RATE52",  "RATE62",  "RATE72",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE82",    "RATE92",  "RATE102", "RATE112",   "RATE122", "COD2",     "CONT2",    "NODE2",   "RMA2",    "RMI2",
		    //  20         21          22         23      24             25          26
			  "VMA2",     "VMI2",     "NTP2",     "TAB2",     "CR2",   "CX2",       "CNXA2"},  
		
		   //
		   //	Line-5 (start position 64)
		   //  0----------1----------2----------3----------4----------5----------6----------7----------8-------9
		   { "WINDV3",  "NOMV3",   "ANG3",    "RATE13",  "RATE23","RATE33",  "RATE43","RATE53",  "RATE63",  "RATE73",
				  
			//  10         11         12         13         14         15          16          17         18          19
		   "RATE83",    "RATE93",  "RATE103", "RATE113",   "RATE123", "COD3",     "CONT3",    "NODE3",   "RMA3",    "RMI3",
		    //  20         21          22         23      24             25          26
			  "VMA3",     "VMI3",     "NTP3",     "TAB3",     "CR3",   "CX3",       "CNXA3"},  
		
	};
	
	@Override public String[] getMetadata() {
		
		
		switch(this.version){
			case PSSE_29:
			case PSSE_30:
			case PSSE_31:
			case PSSE_32:
			case PSSE_33:
				return convertStringAry2DTo1D(META_DATA_v30_33);
			case PSSE_34:
				return convertStringAry2DTo1D(META_DATA_v34);
			case PSSE_35:
			case PSSE_36:
				return convertStringAry2DTo1D(META_DATA_v35_36);
			default:
				return convertStringAry2DTo1D(META_DATA_v35_36);
	
       }
	}
	

	
	@Override public void parseFields(final String[] strAry) throws ODMException {
		this.clearNVPairTableData();
		
		
		String lineStr1 = strAry[0];
		String lineStr2 = strAry[1];
		String lineStr3 = strAry[2];
		String lineStr4 = strAry[3];
		String lineStr5 = strAry[4];   // for 2W xfr, line-5 = ""
		
		String[] firstLineData = lineStr1.trim().split("\\s*,\\s*");
        boolean isThreeWinding = !firstLineData[2].trim().equals("0");
        
        // set the number of expectedFieldCount based on v34-36 as default
        int expectedLine1Num = META_DATA_v35_36[0].length;
        int expectedLine2Num = META_DATA_v35_36[1].length;
        int expectedLine3Num = META_DATA_v35_36[2].length;
        int expectedLine4Num = META_DATA_v35_36[3].length;
        int expectedLine5Num = META_DATA_v35_36[4].length;

        
        // for version 30-33
        if(PSSERawAdapter.getVersionNo(this.version) < 34) {
        	expectedLine1Num = META_DATA_v30_33[0].length;
        	expectedLine2Num = META_DATA_v30_33[1].length;
        	expectedLine3Num = META_DATA_v30_33[2].length;
        	expectedLine4Num = META_DATA_v30_33[3].length;
        	expectedLine5Num = META_DATA_v30_33[4].length;
        	
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
        
        //line 4
        startingIdx += expectedLine3Num;
        parseLineStr(lineStr4, startingIdx, expectedLine4Num);
        
        //line 5
        if(isThreeWinding) {
        	startingIdx += expectedLine4Num;
        	parseLineStr(lineStr5, startingIdx, expectedLine5Num);
        }
        
		/*
		// 324558,324023,     0,'1 ',2,2,1,   0.00036,  -0.00197,1,'HFL- 1,2    ',1,   1,1.0000
		// The name field might have ','
		StringTokenizer st = new StringTokenizer(lineStr1, "'");
		String s1 = st.nextToken();  				// 324558,324023,     0, 
		String s2 = st.nextToken();  				// 1 
		String s3 = st.nextToken();  				// ,2,2,1,   0.00036,  -0.00197,1, 
		String s4 = st.nextToken();  				// HFL- 1,2    
		String s5 = st.nextToken();  				// ,1,   1,1.0000 

		st = new StringTokenizer(s1, ",");
		for (int i = 0; i < 3; i++)
			setValue(i, st.nextToken().trim());

		setValue(3, s2.trim());

		st = new StringTokenizer(s3, ",");
		for (int i = 4; i < 10; i++)
			setValue(i, st.nextToken().trim());

		setValue(10, s4);

		st = new StringTokenizer(s5, ",");
		setValue(11, st.nextToken().trim());

		for (int i = 12; i < 21; i++)
			if (st.hasMoreTokens())
				setValue(i, st.nextToken().trim());
		
		int k = this.getInt("K", 0);

		int N2 = 21;   // Line-1 has 21 fields
		st = new StringTokenizer(lineStr2, ",");
		for (int i = N2; i < N2+3; i++){
			setValue(i, st.nextToken().trim());
			if(i==N2+1 && !st.hasMoreTokens()){
				// if MVA base is not provided, use 100 MW as default
				setValue(N2+2, "100.0");
				break;
			}
		}
		if (k != 0) {
			for (int i = N2+3; i < N2+11; i++)
				if(st.hasMoreElements())
				  setValue(i, st.nextToken().trim());
		}

		int N3 = N2 + 11;  // Line-2 has 11 fields
		st = new StringTokenizer(lineStr3, ",");
		for (int i = N3; i < N3+16; i++)
			setValue(i, st.nextToken().trim());

		int N4 = N3 + 16; // Line-3 has 16 fields
		st = new StringTokenizer(lineStr4, ",");
		for (int i = N4; i < N4+2; i++)
			setValue(i, st.nextToken().trim());
		if (k != 0) {
			for (int i = N4+2; i < N4+16; i++)
				setValue(i, st.nextToken().trim());
		}
		
		int N5 = N4 + 16;   // Line-4 has 16 fields
		if (k != 0) {
			st = new StringTokenizer(lineStr5, ",");
			for (int i = N5; i < N5+16; i++)
				if (st.hasMoreTokens())
					setValue(i, st.nextToken().trim());
		}
		
		*/
	}
        
}