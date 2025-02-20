/*
 * @(#)PSSEMultiSecDataParser.java   
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
  * Class for processing PSS/E Multi-section data
  * 
  * @author mzhou
  *
  */
 public class PSSEMultiSecDataRawParser extends BasePSSEDataRawParser {
	 public PSSEMultiSecDataRawParser(PsseVersion ver) {
		 super(ver);
	 }	
	 
	 @Override public String[] getMetadata() {
		 /*
		  * Format V29, V30
		  * 
		 I, J, ID, DUM1, DUM2, ... DUM9
 
		 I - "From bus" number, or extended bus name enclosed in single quotes.
		 J - "To bus" number, or extended bus name enclosed in single quotes). 
			 J is entered as a negative number or with a minus sign before the
			 first character of the extended bus name to designate it as the metered end; otherwise,
			 bus I is assumed to be the metered end.
		 ID - Two-character upper case alphanumeric multisection line grouping identifier. The
			 first character must be an ampersand ("&"). ID = �1�7&1�1�7 by default.
		 DUMi Bus numbers, or extended bus names enclosed in single quotes,
			 of the "dummy buses" connected by the branches that comprise this multisection
			 line grouping.	 
		 /*
		  * Format V32, V33, V34 , V35, V36
		  * 
		 I, J, ID, MET, DUM1, DUM2, ... DUM9
		 
		 MET	Metered end flag:
			 <=1 to designate bus I as the metered end 
			 >=2 to designate bus J as the metered end.
			 MET = 1 by default.
		 
		  */
		 String [] v29_30 = new String[] {
					//  0----------1----------2----------3----------4
				   "I",       "J",       "ID",               "DUM1",    
				   "DUM2",    "DUM3",    "DUM4",   "DUM5",   "DUM6",   
				   "DUM7",    "DUM8",    "DUM9"
		 };
		 
		 String [] v31_36 =new String[] {
					//  0----------1----------2----------3----------4
					   "I",       "J",       "ID",               "DUM1",    
													   "MET",				// V32 or newer only
					   "DUM2",    "DUM3",    "DUM4",   "DUM5",   "DUM6",   
					   "DUM7",    "DUM8",    "DUM9"
				 };
		
		 switch(this.version){
		 case PSSE_29:
		 case PSSE_30:
			 return v29_30;
		 default:
			 return v31_36;
	 
		 }
 
	 }
	 
	 @Override public void parseFields(final String lineStr) throws ODMException {
		super.parseFields(lineStr);
	 }
		
 }