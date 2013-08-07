/*
 * @(#)PSSEZoneDataParser.java   
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
import org.ieee.odm.model.base.ModelStringUtil;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class PSSEGenDataParser extends BasePSSEDataParser {
	public PSSEGenDataParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override public String[] getMetadata() {
		/* Format V26
		 * 
		 * 	I, ID, PG, QG, QT, QB, VS, IREG,MBASE, ZR,ZX,RT,XT, GTAP, STAT,RMPCT, PT,PB, O1,F1,...,O4,F4
		 * 
		 * Format V30
		 * 	I, ID, PG, QG, QT, QB, VS, IREG,MBASE, ZR,ZX,RT,XT, GTAP, STAT,RMPCT, PT,PB, O1,F1,...,O4,F4

		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",       "ID",      "PG",      "QG",      "QT",      
		   //  5          6          7          8          9
			  "QB",      "VS",      "IREG",    "MBASE",   "ZR",
		   //  10         11         12         13         14
			  "ZX",      "RT",      "XT",      "GTAP",    "STAT",
		   //  15         16         17         18         19
			  "RMPCT",   "PT",      "PB",      "O1",      "F1",
		   //  20         21         22         23         24
			  "O2",      "F2",      "O3",      "F3",      "O4", 
		   //  25 
			  "F1"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		this.clearNVPairTableData();
		
		//I,ID,PG,QG,QT,QB,VS,IREG,MBASE,ZR,ZX,RT,XT,GTAP,STAT,RMPCT,PT,PB,
  		StringTokenizer st = new StringTokenizer(str, ",");
		for (int i = 0; i < 18; i++){
			if(i==1){//genId, need to trim the quote
				setValue(i,ModelStringUtil.trimQuote(st.nextToken()).trim());
			}
			else setValue(i, st.nextToken().trim());
			
		}
        //O1,F1,...,O4,F4
		for (int i = 18; i < 26; i++)
			setValue(i, "0");
		
		if (st.hasMoreTokens()) {
			setValue(18, st.nextToken().trim());
			setValue(19, st.nextToken().trim());
		}
		if (st.hasMoreTokens()) {
			setValue(20, st.nextToken().trim());
			setValue(21, st.nextToken().trim());
		}
		if (st.hasMoreTokens()) {
			setValue(22, st.nextToken().trim());
			setValue(23, st.nextToken().trim());
		}
		if (st.hasMoreTokens()) {
			setValue(24, st.nextToken().trim());
			setValue(25, st.nextToken().trim());
		}
	}
}