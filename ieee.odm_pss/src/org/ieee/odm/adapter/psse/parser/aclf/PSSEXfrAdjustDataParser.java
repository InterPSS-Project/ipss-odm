/*
 * @(#)PSSEXfrAdjustDataParser.java   
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
public class PSSEXfrAdjustDataParser extends BasePSSEDataParser {
	public PSSEXfrAdjustDataParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V26
		 * 
		 * 	 I,    J,     CKT,ICONT,     RMA,       RMI,       VMA,       VMI,   STEP,   TABLE 
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",       "J",       "CKT",     "ICONT",   "RMA",             
		   //  5          6          7          8          9
			  "RMI",     "VMA",     "VMI",    "STEP",     "TABLE"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		this.clearNVPairTableData();
		
  		StringTokenizer st = new StringTokenizer(str, ",");
		/*
	    I,    J,     CKT,  ICONT,     RMA,       RMI,       VMA,       VMI,   STEP,   TABLE 
        */
  		for (int i = 0; i < 8; i++)
  			setValue(i, st.nextToken().trim());
  		
  		if (st.hasMoreTokens())
  			setValue(8, st.nextToken().trim());
  	}
}