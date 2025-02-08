/*
 * @(#)PSSELineDataParser.java   
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

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class PSSELineDataRawParser extends BasePSSEDataRawParser {
	public PSSELineDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V26
		 * 
		 * 	I, J, CKT, R,X,B, RATEA,RATEB,RATEC, RATIO,ANGLE,GI,BI,GJ,BJ,ST,      LEN,O1,F1,...,O4,F4
		 * 
		 * V30, V29
		 *  I, J, CKT, R,X,B, RATEA,RATEB,RATEC,             GI,BI,GJ,BJ,ST,      LEN,O1,F1,...,O4,F4
		 *  
		 * V31, V32, V33
		 *  I, J, CKT, R,X,B, RATEA,RATEB,RATEC,             GI,BI,GJ,BJ,ST, MET, LEN,O1,F1,...,O4,F4 
		 *  
			MET Metered end flag;
    			<=1 to designate bus I as the metered end
    			>=2 to designate bus J as the metered end.
				MET = 1 by default.
		 */
		/*
		 *  "fields":["ibus", "jbus", "ckt", "rpu", "xpu", "bpu", "name", "rate1", "rate2", 
		 *  		  "rate3", "rate4", "rate5", "rate6", "rate7", "rate8", "rate9", "rate10", 
		 *  		  "rate11", "rate12", "gi", "bi", "gj", "bj", "stat", "bp", "met", "len", 
		 *  		  "o1", "f1", "o2", "f2", "o3", "f3", "o4", "f4"], 
		 */
		return new String[] {
				/*
				 * V26
				 */
		   //  0----------1----------2----------3----------4
			  "I",       "J",       "CKT",     "R",       "X",             
		   //  5          6          7          8          9
			  "B",      "RATEA",    "RATEB",   "RATEC",   
			                                              "RATIO",   // ver26 only
		   //  10         11         12         13         14
			  "ANGLE",                                               // ver26 only
			             "GI",      "BI",      "GJ",      "BJ",
		   //  15         16         17         18         19
			  "ST",     
			             "MET",                                      // >= V32 only
			                       "LEN",     "O1",      "F1",    
		   //  20         21         22         23         24	  
			  "O2",      "F2",      "O3",      "F3",      "O4",     
		   //  26  
			  "F4"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		this.clearNVPairTableData();
		
  		StringTokenizer st = new StringTokenizer(str, ",");

  		for (int i = 0; i < 9; i++) {
  			if (i == 2 && PSSERawAdapter.getVersionNo(this.version) >= 29)
  				setValue(i, ODMModelStringUtil.trimQuote(st.nextToken()).trim());
  			else
  				setValue(i, st.nextToken().trim());
  		}	

  		if (this.version == PsseVersion.PSSE_26) {
			setValue(9, st.nextToken().trim());
			setValue(10, st.nextToken().trim());
  		}

  		for (int i = 11; i < 16; i++)
  			if (st.hasMoreTokens()) 
  				setValue(i, st.nextToken().trim());

		setValue(16, "1");
		if (PSSERawAdapter.getVersionNo(this.version) >= 31)
			setValue(16, st.nextToken().trim());
 		
  		for (int i = 17; i < 25; i++)
  			if (st.hasMoreTokens()) 
  				setValue(i, st.nextToken().trim());
  		
  		
	}
}