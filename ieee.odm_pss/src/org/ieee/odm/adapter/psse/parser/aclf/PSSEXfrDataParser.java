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

package org.ieee.odm.adapter.psse.parser.aclf;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class PSSEXfrDataParser extends BasePSSEDataParser {
	public PSSEXfrDataParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
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
	
			/* 
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
	Line-2 		R1-2,X1-2,SBASE1-2
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
        */

		return new String[] {
	           //
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
				  "VECGRP",
					  
	           //
			   //	Line-2
			   //  0----------1----------2----------3----------4
				  "R1-2",    "X1-2",   "SBASE1-2", "R2-3",   "X2-3",
			   //  5          6          7          8          9
				  "SBASE2-3", "R3-1",   "X3-1",   "SBASE3-1", "VMSTAR",
			   //  10
				  "ANSTAR",            

			   //
			   //	Line-3
			   //  0----------1----------2----------3----------4
				  "WINDV1",  "NOMV1",   "ANG1",    "RATA1",  "RATB1",
			   //  5          6          7          8          9
				  "RATC1",   "COD",     "CONT",    "RMA",    "RMI",
			   //  10         11         12         13         14
				  "VMA",     "VMI",     "NTP",     "TAB",     "CR",
			   //  15
				  "CX",             

			   //
			   //	Line-4
			   //  0----------1----------2----------3----------4
				  "WINDV2",  "NOMV2",  "ANG2",    "RATA2",   "RATB2",
			   //  5          6          7          8          9
				  "RATC2",   
				             "COD2",   "CONT2",   "RMA2",    "RMI2",   // V30 and late version
			   //  10         11         12         13         14
				  "VMA2",    "VMI2",   "NTP2",    "TAB2",    "CR2",    // V30 and late version
			   //  15
				  "CX2",                                               // V30 and late version
			
			   //
			   //	Line-5
			   //  0----------1----------2----------3----------4
				  "WINDV3",  "NOMV3",   "ANG3",   "RATA3",    "RATB3",
			   //  5          6          7          8          9
				  "RATC3",   
				             "COD3",    "CONT3",  "RMA3",     "RMI3",   // V30 and late version
			   //  10         11         12         13         14
				  "VMA3",    "VMI3",    "NTP3",   "TAB3",     "CR3",    // V30 and late version
				// 15
				  "CX3"                                                 // V30 and late version
		};
	}
	
	@Override public void parseFields(final String[] strAry) throws ODMException {
		this.clearNVPairTableData();
		
		String lineStr1 = strAry[0];
		String lineStr2 = strAry[1];
		String lineStr3 = strAry[2];
		String lineStr4 = strAry[3];
		String lineStr5 = strAry[4];
		
		// 324558,324023,     0,'1 ',2,2,1,   0.00036,  -0.00197,1,'HFL- 1,2    ',1,   1,1.0000
		// The name field might have ','
		StringTokenizer st = new StringTokenizer(lineStr1, "'");
		String s1 = st.nextToken();  // 324558,324023,     0, 
		String s2 = st.nextToken();  // 1 
		String s3 = st.nextToken();  // ,2,2,1,   0.00036,  -0.00197,1, 
		String s4 = st.nextToken();  // HFL- 1,2    
		String s5 = st.nextToken();  // ,1,   1,1.0000 

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

		int N2 = 21;
		st = new StringTokenizer(lineStr2, ",");
		for (int i = N2; i < N2+3; i++)
			setValue(i, st.nextToken().trim());
		if (k != 0) {
			for (int i = N2+3; i < N2+11; i++)
				setValue(i, st.nextToken().trim());
		}

		int N3 = N2 + 11;
		st = new StringTokenizer(lineStr3, ",");
		for (int i = N3; i < N3+16; i++)
			setValue(i, st.nextToken().trim());

		int N4 = N3 + 16;
		st = new StringTokenizer(lineStr4, ",");
		for (int i = N4; i < N4+2; i++)
			setValue(i, st.nextToken().trim());
		if (k != 0) {
			for (int i = N4+2; i < N4+16; i++)
				setValue(i, st.nextToken().trim());
		}
		
		int N5 = N4 + 16;
		if (k != 0) {
			st = new StringTokenizer(lineStr5, ",");
			for (int i = N5; i < N5+16; i++)
				if (st.hasMoreTokens())
					setValue(i, st.nextToken().trim());
		}
	}
}