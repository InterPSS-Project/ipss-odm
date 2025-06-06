/*
 * @(#)PSSEBusDataParser.java   
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

 import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
 import org.ieee.odm.common.ODMException;
 
 /**
  * Class for processing PSS/E Bus data
  * 
  * @author mzhou
  *
  */
 public class PSSEBusDataRawParser extends BasePSSEDataRawParser {
	 public PSSEBusDataRawParser(PsseVersion ver) {
		 super(ver);
	 }	
	 
	 @Override public String[] getMetadata() {
		 /* Format V30, V29
		  * 
		  *  I,  NAME,  BASKV, IDE,  GL, BL, AREA, ZONE,        VM, VA,  OWNER
		  * 
		  *  Format V32, V31
		  * 
		  *  I,  NAME,  BASKV, IDE,          AREA, ZONE, OWNER,  VM, VA
		  * 
		  * Format V33, V34, V35, V36
		  *  
		  *  I,  'NAME',  BASKV, IDE,        AREA, ZONE, OWNER,  VM, VA, NVHI, NVLO, EVHI, EVLO
		  *  
		  *  	NVHI	Normal voltage magnitude high limit; entered in pu.  NVHI=1.1 by default 
		  *  	NVLO	Normal voltage magnitude low limit, entered in pu.  NVLO=0.9 by default 
		  *  	EVHI	Emergency voltage magnitude high limit; entered in pu.  EVHI=1.1 by default 
		  *  	EVLO	Emergency voltage magnitude low limit; entered in pu.  EVLO=0.9 by default
		  *  
		  */
		 /*
		  *  "fields":["ibus", "name", "baskv", "ide", "area", "zone", "owner", 
		  *            "vm", "va", "nvhi", "nvlo", "evhi", "evlo"], 
		  */
		 
		 String[] v29_30 =  new String[] {
					//  0----------1----------2----------3----------4
					   "I",      "NAME",    "BASKV",    "IDE",     "GL",      
					//  5          6          7          8          9
					   "BL",     "AREA",    "ZONE",     "VM",      "VA",
					//  10         11         12         13         14
					   "OWNER", 
				 };
 
		 String[] v31_32 =  new String[] {
					//  0----------1----------2----------3----------4
					   "I",      "NAME",    "BASKV",    "IDE",         
					//  5          6          7          8          9
					"AREA",    "ZONE",     "OWNER",    "VM",      "VA",
					//  10         11         12         13         14
					 
				 };
		 
		 String[] v33_36 = new String[] {
			//  0----------1----------2----------3----------4
			   "I",      "NAME",    "BASKV",    "IDE",     
			//  5          6          7          8          9
			   "AREA",    "ZONE",    "OWNER",  "VM",      "VA",
			//  10         11         12         13         14
			   "NVHI",    "NVLO",     "EVHI",    "EVLO"
		 };
		 
		 
		 switch(this.version){
		 case PSSE_29:
		 case PSSE_30:
			 return v29_30;
		 case PSSE_31:
		 case PSSE_32:
			 return v31_32;
		 case PSSE_33:
		 case PSSE_34:
		 case PSSE_35:
			 return v33_36;
		 default:
			 return v33_36;
			 
	 }
		 
		 
	 }
	 
	 @Override public void parseFields(final String lineStr) throws ODMException {
		 super.parseFields(lineStr);
	 }
	 
	 /*
	 @Override public void parseFields(final String lineStr) throws ODMException {
		 this.clearNVPairTableData();
		 
	 
		 if (PSSERawAdapter.getVersionNo(this.version) >= 29 && PSSERawAdapter.getVersionNo(this.version) <= 36) {
			 StringTokenizer st;
 
			 // V30
			 //    10001,'ALB_T4*     ',   1.0000,1,     0.000,     0.000,   1,   1,     1.03259, -13.5044,   1
			 // V31
			 //        1,'BUS-1       ',  16.5000,3,                         1,   1,  1, 1.04000,   0.0000
			 // V32
			 //    10001,'ALB_T4*     ',   1.0000,1,                         1,   1,  1, 1.03259, -13.5044
			 // V33
			 //        1,'BUS-1       ',  16.5000,3,                         1,   1,  1, 1.04000,   0.0000, 1.10000, 0.90000, 1.10000, 0.90000
			 
			 // -- str1-- ----str2----- -----------str3---------------	
			 String str1 = lineStr.substring(0, lineStr.indexOf('\'')),
						strbuf = lineStr.substring(lineStr.indexOf('\'')+1),
						str2 = strbuf.substring(0, strbuf.indexOf('\'')),
						str3 = strbuf.substring(strbuf.indexOf('\'')+1);
				 
			 st = new StringTokenizer(str1, ",");
			 setValue(0, st.nextToken().trim());
			 setValue(1, str2);
 
			 st = new StringTokenizer(str3, ",");
			 String s = st.nextToken().trim();
			 if (s.equals(""))   // in case there are spaces between '  ,
				 s = st.nextToken().trim();		
			 setValue(2,s);
			 
			 int cnt = 3;
			 if (PSSERawAdapter.getVersionNo(this.version) >= 31) {
				 setValue(3, st.nextToken().trim());
				 setValue(6, st.nextToken().trim());
				 setValue(7, st.nextToken().trim());
				 setValue(10, st.nextToken().trim());
				 setValue(8, st.nextToken().trim());
				 setValue(9, st.nextToken().trim());
 
				 if (PSSERawAdapter.getVersionNo(this.version) >= 33) {
					 cnt = 11;
					 while(st.hasMoreTokens()) {
						 setValue(cnt++, st.nextToken().trim());
					 }
				 }
			 }
			 else {
				 while(st.hasMoreTokens()) {
					 setValue(cnt++, st.nextToken().trim());
				 }
			 }
		 }
		 else
			 throw new ODMException("PSSEBusDataParser, wrong PSSE Version " + this.version);
	 }
	 */
 }