/*
 * @(#)PSSEAreaInterchangeDataParser.java   
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
import org.ieee.odm.common.ODMException;

/**
 * 
 * @author mzhou
 *
 */
public class PSSEAreaInterchangeDataRawParser extends BasePSSEDataRawParser {
	public PSSEAreaInterchangeDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V30, V32
		 * 
		//    Area number , no zeros! *
		//    swing bus name [A]
		//    Area interchange export, MW [F] (+ = out) *
		//    Area interchange tolerance, MW [F] *
		 * 
		*/
		/*
		 * No equiv Json schema
		 */
		return new String[] {
		   //  0----------1---------     -2------------3---------   -4
			 "AreaNum", "SwingBusName", "ExpoertMw", "ExTolerance"             
		};
	}
	
//	@Override public void parseFields(final String str) throws ODMException {
//  		StringTokenizer st = new StringTokenizer(str, ",");
//  		int cnt = 0;
//  		while (st.hasMoreTokens())
//  			setValue(cnt++, st.nextToken().trim());
//  	}
}