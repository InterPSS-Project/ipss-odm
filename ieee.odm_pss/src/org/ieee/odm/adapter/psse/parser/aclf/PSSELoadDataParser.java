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
public class PSSELoadDataParser extends BasePSSEDataParser {
	public PSSELoadDataParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override public String[] getMetadata() {
		/* Format V30
		 * 
		 *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER
		 *  
		 *  Format V32
		 * 
		 *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER, SCALE
		 *  
		 *     SCALE Load scaling flag ofone for a scalalie load and zero for a ?xed load (refer to SCAL). SCALE = 1 by defarlt.
		 *     
		 *  Format V33
		 *  
		 *  I, ID, STATUS, AREA, ZONE, PL, QL, IP, IQ, YP, YQ, OWNER, SCALE, INTRPT
		 *  
		 *  	INTRPT	Interruptible load flag of one for an interruptible load for zero for a non interruptible load.  INTRPT=0 by default.   
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",       "ID",     "STATUS",   "AREA",    "ZONE",       
		   //  5          6          7          8          9
			  "PL",      "QL",      "IP",      "IQ",      "YP",    
		   //  10         11        12     
			  "YQ",      "OWNER",  
			                       "SCALE",                     	// V32, V33 only
			                                   "INTRPT"				// V33 only
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		this.clearNVPairTableData();
		
  		StringTokenizer st = new StringTokenizer(str, ",");
		for (int i = 0; i < 12; i++)
			setValue(i, st.nextToken().trim());
		
		setValue(12, "1");
		if (this.verion == PsseVersion.PSSE_32)
			setValue(12, st.nextToken().trim());		
	}
}