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

import org.ieee.odm.adapter.psse.PSSEAdapter;
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
		/* Format V30, V29
		 * 	I, ID, PG, QG, QT, QB, VS, IREG,MBASE, ZR,ZX,RT,XT, GTAP, STAT,RMPCT, PT,PB, O1,F1,...,O4,F4
 
 			Format V32, V33
		 * 	I, ID, PG, QG, QT, QB, VS, IREG,MBASE, ZR,ZX,RT,XT, GTAP, STAT,RMPCT, PT,PB, O1,F1,...,O4,F4, WMOD, WPF

WMOD Wind machine control mode; WMOD is used to indicate whether a machine is a wind machine, and, if it is, the type of reactive power limits to be imposed.
	0 for a machine that is not a wind machine.
	1 for a wind machine for which reactive power limits are specified  by QT and QB.
	2 for a wind machine for which reactive power limits are determined from  the machines active power output and WPF; 
		limits are of equal  magnitude and opposite sign
	3 for a wind machine with a fixed reactive power setting determined from  the machines active power output and WPF; when WPF is positive,  
		the machines reactive power has the same sign as its active power;  when WPF is negative, the machines reactive power has the opposite  
		sign of its active power.
	WMOD = 0 by default.
   
WPF Power factor used in calculating reactive power limits or output when WMOD is 2 or 3. WPF = 1.0 by default.   

Sample data:

    I, ID,      PG,         QG,        QT,       QB,    VS,          IREG, MBASE,  ZR,         ZX,         RT,         XT,        GTAP,   STAT,RMPCT,   PT,         PB,      O1,F1,...,O4,F4, WMOD, WPF
    14,'1 ',    14.752,     0.748,     8.200,    -5.600,1.02500,     0,    21.000, 0.00000E+0, 1.30000E-1, 0.00000E+0, 0.00000E+0,1.00000,1,  100.0,    17.000,     6.000,   4,1.0000

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
		   //  25         26         27
			  "F1",      
			             "WMOD",    "WPF"                // for V32 only
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
		
		if (PSSEAdapter.getVersionNo(this.version) >= 32) {
			setValue(26, "");
			setValue(27, "");
			if (st.hasMoreTokens()) {
				setValue(26, st.nextToken().trim());
				setValue(27, st.nextToken().trim());
			}			
		}
	}
}