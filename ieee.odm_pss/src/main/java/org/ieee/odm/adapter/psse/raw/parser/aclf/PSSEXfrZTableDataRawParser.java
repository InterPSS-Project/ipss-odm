/*
 * @(#)PSSEXfrZTableDataParser.java   
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
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E Transformer impedance table data
 * 
 * @author mzhou
 *
 */
public class PSSEXfrZTableDataRawParser extends BasePSSEDataRawParser {
	public PSSEXfrZTableDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* 
		 * format V29, V30, V32, V33
		 * 
		 *       I, T1, F1, T2, F2, T3, F3, ... T11, F11
		 * 
		*/

		
		String[] v29_33 = new  String[] {
		   //  0----------1----------2----------3----------4
			  "I",      "T1",      "F1",      "T2",       "F2", 
		   //  0----------1----------2----------3----------4
			  "T3",     "F3",      "T4",      "F4",       "T5",     
		   //  0----------1----------2----------3----------4
			  "F5",     "T6",      "F6",      "T7",       "F7",  
		   //  0----------1----------2----------3----------4	  
			  "T8",      "F8",     "T9",     "F9",      "T10",         
		   //  0----------1----------2----------3----------4	  
			  "F10",      "T11",  "F11"           
		};
		
		
		/* 
		 * format V34-36
		 * 
		 *       I, T1, Re(F1), Im(F1), T2, Re(F2), Im(F2), ..., T6, Re(F6), Im(F6)
				T7, Re(F7), Im(F7), T8, Re(F8), Im(F8), ..., T12, Re(F12), Im(F12)
				...
				...
				Tn, Re(Fn), Im(Fn), 0.0, 0.0, 0.0
		 * 
		*/
		
		String[] v34_36 = new  String[] {
				   //  0----------1----------2----------3----------4
					  "I",      "T1",      "RE(F1)",    "IM(F1)", "T2",      
				   //  0----------1----------2----------3----------4
					  "RE(F2)",   "IM(F2)", "T3",     "RE(F3)",   "IM(F3)",     
				   //  0----------1----------2----------3----------4
					  "T4",      "RE(F4)",  "IM(F4)",   "T5",    "RE(F5)",    
				   //  0----------1----------2----------3----------4	  
					  "IM(F5)", "T6",      "RE(F6)",  "IM(F6)",  "T7",       
				   //  0----------1----------2----------3----------4	  
					  "RE(F7)",  "IM(F7)",   "T8",       "RE(F8)",  "IM(F8)",
				    //
					   "T9",    "RE(F9)",  "IM(F9)",   "T10",    "RE(F10)", 
					//
					   "IM(F10)", "T11",    "RE(F11)",  "IM(F11)",  "T12",  
					//
					   "RE(F12)",  "IM(F12)"
				};
		switch(this.version){
			case PSSE_29:
			case PSSE_30:
			case PSSE_31:
			case PSSE_32:
			case PSSE_33:
				return v29_33;
			case PSSE_34:
			case PSSE_35:
			case PSSE_36:
				return v34_36;
			default:
				return v34_36;

   		}
	}
	
	@Override public void parseFields(final String[] strAry) throws ODMException {
		this.clearNVPairTableData();

		 // if strAry array has more than 2 lines, throw exception as it is not supported yet
		 if(strAry.length > 2) {
			throw new ODMException("Error: Transformer impedance table data has more than 2 lines, which is not supported yet.");
		}
		 
		 
		 String lineStr1 = strAry[0];
		
		 
		 // set the number of expectedFieldCount based on v34-36 as default
		 int expectedLine1Num = 19;

		 if(PSSERawAdapter.getVersionNo(this.version) < 34) {
			 expectedLine1Num = 23;
		 }

		 

		  //line 1
		  int startingIdx = 0;
		  // for version 30-33, the first field is the number of the table
		  parseLineStr(lineStr1, startingIdx, expectedLine1Num);
		 
		 
		 if(PSSERawAdapter.getVersionNo(this.version) >= 34 && strAry.length > 1) {

			int expectedLine2Num = 18; // for version 30-33, the second line is not used
			
			String lineStr2 = strAry[1];
			 //line 2
			 parseLineStr(lineStr2, expectedLine1Num, expectedLine2Num);

		 }

					
  	}
}