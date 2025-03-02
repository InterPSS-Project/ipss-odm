/*
 * @(#)PSSEDcLine2TDataParser.java   
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

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSSE Two-terminal DC lines
 * 
 * @author mzhou
 *
 */
public class PSSEDcLine2TDataRawParser extends BasePSSEDataRawParser {
	public PSSEDcLine2TDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	
	private static String[][] META_DATA_v30_33 = new String[][] {
		       {
			   //  0-----------1-----------2-----------3-----------4
				  "I",       "MDC",      "RDC",      "SETVL",    "VSCHD",      // Line-1   
			   //  5           6           7           8           9
				  "VCMOD",   "RCOMP",    "DELTI",    "METER",    "DCVMIN",
			   //  10          11          12          13          14
				  "CCCITMX", "CCCACC"},
		
		       {"IPR",      "NBR",      "ALFMX",      // Line-2
			   //  15          16          17          18          19
				  "ALFMN",   "RCR",      "XCR",      "EBASR",    "TRR",
			   //  20          21          22          23          24
				  "TAPR",    "TMXR",     "TMNR",     "STPR",     "ICR",
			   //  25          26          27          28          29
				  "IFR",     "ITR",      "IDR",      "XCAPR"},
		        { "IPI",        // Line-3
	       	   //  30          31          32          33          34
				  "NBI",     "GAMMX",    "GAMMN",    "RCI",      "XCI",
			   //  35          36          37          38          39
				  "EBASI",   "TRI",      "TAPI",     "TMXI",     "TMNI",
			   //  40          41          42          43          44
				  "STPI",    "ICI",      "IFI",      "ITI",      "IDI",
			   //  45
				  "XCAPI"}};
	private static String[][] META_DATA_v34 = new String[][] {
		      // Line-1 
			   //  0-----------1-----------2-----------3-----------4
		       { "I",       "MDC",      "RDC",      "SETVL",    "VSCHD",        
			   //  5           6           7           8           9
				  "VCMOD",   "RCOMP",    "DELTI",    "METER",    "DCVMIN",
			   //  10          11          12          13          14
				  "CCCITMX", "CCCACC"},
		       
		        // Line-2
		       { "IPR",      "NBR",      "ALFMX",      
			   //  15          16          17          18          19
				  "ALFMN",   "RCR",      "XCR",      "EBASR",    "TRR",
			   //  20          21          22          23          24
				  "TAPR",    "TMXR",     "TMNR",     "STPR",     "ICR",
			   //  25          26          27          28          29
				  "IFR",     "ITR",      "IDR",      "XCAPR",    "NDR"},
		       
				 // Line-3
	       	   //  30          31          32          33          34
		       { "IPI",     "NBI",     "GAMMX",    "GAMMN",    "RCI",    
			   //  35          36          37          38          39
				  "XCI",    "EBASI",   "TRI",      "TAPI",     "TMXI",    
			   //  40          41          42          43          44
				  "TMNI",   "STPI",    "ICI",      "IFI",      "ITI",     
			   //  45        46      47 
				  "IDI",   "XCAPI", "NDI"}};
	
	private static String[][] META_DATA_v35_36 = new String[][] {
		
		   // Line-1  
			   //  0-----------1-----------2-----------3-----------4
		       {   "I",       "MDC",      "RDC",      "SETVL",    "VSCHD",      
			   //  5           6           7           8           9
				  "VCMOD",   "RCOMP",    "DELTI",    "METER",    "DCVMIN",
			   //  10          11          12          13          14
				  "CCCITMX", "CCCACC"},
		       
		    // Line-2
		       { "IPR",      "NBR",      "ALFMX",      
			   //  15          16          17          18          19
				  "ALFMN",   "RCR",      "XCR",      "EBASR",    "TRR",
			   //  20          21          22          23          24
				  "TAPR",    "TMXR",     "TMNR",     "STPR",     "ICR",
			   //  25          26          27          28          29
				  "NDR",      "IFR",     "ITR",      "IDR",      "XCAPR"}, 
		       
	         // Line-3
	       	   //  30          31          32          33          34
		       { "IPI",     "NBI",     "GAMMX",    "GAMMN",    "RCI",      
			   //  35          36          37          38          39
				  "XCI",    "EBASI",   "TRI",      "TAPI",     "TMXI",     
			   //  40          41          42          43          44
				  "TMNI",   "STPI",    "ICI",      "NDI",        "IFI",     
			   //  45         46        47      
				  "ITI",      "IDI",    "XCAPI"}};
	
	@Override public String[] getMetadata() {
		/*
		 * Format V30
		 * 
		Line-1: 
			I,MDC,RDC,      SETVL,    VSCHD,   VCMOD,  RCOMP,    DELTI,  METER,  DCVMIN,CCCITMX,CCCACC
            1,1,  13.7500,  552.00,   410.00,  -1.00,  13.7500,  0.10000,'I',    0.00,  20,   1.00000
			
			MDC Control mode: 0 for blocked, 1 for power, 2 for current. MDC = 0 by default.
			SETVL Current (amps) or power (MW) demand. When MDC is one, a positive value of
				SETVL specifies desired power at the rectifier and a negative value specifies
				desired inverter power. No default allowed.					
			RDC The dc line resistance; entered in ohms. No default allowed.
			VSCHD Scheduled compounded dc voltage; entered in kV. No default allowed.
			METER Metered end code of either (for rectifier) or (for inverter). METER = by default.
			VCMOD Mode switch dc voltage; entered in kV. When the inverter dc voltage falls below
				this value and the line is in power control mode (i.e., MDC = 1), the line switches
				to current control mode with a desired current corresponding to the desired power
				at scheduled dc voltage. VCMOD = 0.0 by default.
			RCOMP Compounding resistance; entered in ohms. Gamma and/or TAPI is used to attempt
				to hold the compounded voltage (VDCI + DCCUR*RCOMP) at VSCHD. To control
				the inverter end dc voltage VDCI, set RCOMP to zero; to control the rectifier
				end dc voltage VDCR, set RCOMP to the dc line resistance, RDC; otherwise, set
				RCOMP to the appropriate fraction of RDC. RCOMP = 0.0 by default.
			DELTI Margin entered in per unit of desired dc power or current. This is the fraction by
				which the order is reduced when ALPHA is at its minimum and the inverter is controlling
				the line current. DELTI = 0.0 by default.
			DCVMIN Minimum compounded dc voltage; entered in kV. Only used in constant gamma
				operation (i.e., when GAMMX = GAMMN) when TAPI is held constant and an ac
				transformer tap is adjusted to control dc voltage (i.e., when IFI, ITI, and IDI specify
				a two-winding transformer). DCVMIN = 0.0 by default.

		Line-2: IPR,NBR,ALFMX,ALFMN,RCR,XCR,EBASR,TRR,TAPR,TMXR,TMNR,STPR,ICR,IFR,ITR,IDR,XCAPR
		Line-3: IPI,NBI,GAMMX,GAMMN,RCI,XCI,EBASI,TRI,TAPI,TMXI,TMNI,STPI,ICI,IFI,ITI,IDI,XCAPI		
			IPR Rectifier converter bus number, or extended bus name enclosed in single quotes
				(see Section 4.1.2). No default allowed.
			NBR Number of bridges in series (rectifier). No default allowed.
			ALFMX Nominal maximum rectifier firing angle; entered in degrees. No default allowed.
			ALFMN Minimum steady-state rectifier firing angle; entered in degrees. No default allowed.
			EBASR Rectifier primary base ac voltage; entered in kV. No default allowed.
			ICR Rectifier firing angle measuring bus number, or extended bus name enclosed in
				single quotes (see Section 4.1.2). The firing angle and angle limits used inside the
				dc model are adjusted by the difference between the phase angles at this bus and
				the ac/dc interface (i.e., the converter bus, IPR). ICR = 0 by default.
			*/
		/*
		 *  "fields":["name", "mdc", "rdc", "setvl", "vschd", "vcmod", "rcomp", "delti", "met", 
		 *           "dcvmin", "cccitmx", "cccacc", 
		 *           "ipr", "nbr", "anmxr", "anmnr", "rcr", "xcr",  "ebasr", "trr", "tapr", "tmxr", "tmnr", "stpr", "icr", "ndr", "ifr", "itr","idr", "xcapr", 
		 *           "ipi", "nbi", "anmxi", "anmni", "rci", "xci", "ebasi", "tri", "tapi", "tmxi", "tmni", "stpi", "ici", "ndi", "ifi", "iti", "idi", "xcapi"],  
  
		 */
		
		
		
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
		
		String lineStr1 = strAry[0];
		String lineStr2 = strAry[1];
		String lineStr3 = strAry[2];


        
        // set the number of expectedFieldCount based on v35-36 as default
        int expectedLine1Num = META_DATA_v35_36[0].length;
        int expectedLine2Num = META_DATA_v35_36[1].length;
        int expectedLine3Num = META_DATA_v35_36[2].length;

        
        // for version 30-33
        if(PSSERawAdapter.getVersionNo(this.version) < 34) {
        	expectedLine1Num = META_DATA_v30_33[0].length;
        	expectedLine2Num = META_DATA_v30_33[1].length;
        	expectedLine3Num = META_DATA_v30_33[2].length;
        	
        }
        else if(PSSERawAdapter.getVersionNo(this.version) == 34) {
        	expectedLine1Num = META_DATA_v34[0].length;
        	expectedLine2Num = META_DATA_v34[1].length;
        	expectedLine3Num = META_DATA_v34[2].length;
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
        
		
		
//		StringTokenizer st = new StringTokenizer(lineStrAry[0], ",");
//		int cnt = 0;
//		while (st.hasMoreTokens())
//			this.setValue(cnt++, st.nextToken().trim());
//
//		st = new StringTokenizer(lineStrAry[1], ",");
//		cnt = 12;
//		while (st.hasMoreTokens())
//			this.setValue(cnt++, st.nextToken().trim());
//		
//		st = new StringTokenizer(lineStrAry[2], ",");
//		cnt = 29;
//		while (st.hasMoreTokens())
//			this.setValue(cnt++, st.nextToken().trim());
  	}
}