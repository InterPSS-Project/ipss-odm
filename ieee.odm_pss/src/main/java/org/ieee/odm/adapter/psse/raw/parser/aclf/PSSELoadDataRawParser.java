/*
 * @(#)PSSELoadDataParser.java   
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
  * Class for processing PSS/E Load Data
  * 
  * @author mzhou
  *
  */
 public class PSSELoadDataRawParser extends BasePSSEDataRawParser {
	 public PSSELoadDataRawParser(PsseVersion ver) {
		 super(ver);
	 }
	 
	 @Override public String[] getMetadata() {
		 /* Format V30, V29
		  * 
		  *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER
		  *  
		  *  Format V32
		  * 
		  *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER, SCALE
		  *  
		  *     SCALE Load scaling flag of one for a scalaliable load and zero for a fixed load (refer to SCAL). SCALE = 1 by default.
		  *     
		  *  Format V33
		  *  
		  *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER, SCALE, INTRPT
		  *  
		  *  	INTRPT	Interruptible load flag of one for an interruptible load for zero for a non interruptible load.  INTRPT=0 by default.   
		  */
		 
		 String[] v29_30 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12     
				   "YQ",      "OWNER"
				   };
		 
		 String[] v31_32 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12     
				   "YQ",      "OWNER",  "SCALE"
				   };
		 String[] v33 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12     
				   "YQ",      "OWNER",  "SCALE",   "INTRPT" //new in V33 
				   };
		 String[] v34 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12         13          
				   "YQ",      "OWNER",  "SCALE",   "INTRPT", 
				   //14       15       16          --NEW IN V34
				   "DGENP", "DGENQ", "DGENM"  
				   };
		 String[] v35 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12         13          
				   "YQ",      "OWNER",  "SCALE",   "INTRPT", 
				   //14       15       16         17
				   "DGENP", "DGENQ", "DGENM",    "LOADTYPE"
				   };
		 String[] v36 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
				//  5          6          7          8          9
				   "PL",      "QL",      "IP",      "IQ",      "YP",    
				//  10         11        12         13          
				   "YQ",      "OWNER",  "SCALE",   "INTRPT", 
				   //14       15       16         17          18 
				   "DGENP", "DGENQ", "DGENM",    "LOADTYPE",  "NAME"
				   };
		 
		 switch(this.version){
			 case PSSE_29:
			 case PSSE_30:
				 return v29_30;
			 case PSSE_31:
			 case PSSE_32:
				 return v31_32;
			 case PSSE_33:
				 return v33;
			 case PSSE_34:
				 return v34;
			 case PSSE_35:
				 return v35;
			 default:
				 return v36;
		 
		 }
 
	 
	 }
	 
	 @Override public void parseFields(final String str) throws ODMException {
		 super.parseFields(str);	
	 }
 }