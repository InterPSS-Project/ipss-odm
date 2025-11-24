/*
 * @(#)GEInterfaceDataParser.java   
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

package org.ieee.odm.adapter.ge.parser;

import java.util.StringTokenizer;

import org.ieee.odm.common.ODMException;

/**
 * Class for processing GE PSLF interface data line string
 * 
 * @author mzhou
 *
 */
public class GEInterfaceDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<ifno> <"name"> <pnet> <qnet> <r1> <r2> <r3> <r4> <r5> <r6> <r7> <r8>
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "ifno",    "name",   "pnet",    "qnet",     "r1",  
		   //  5          6          7          8          9  
		     "r2",      "r3",     "r4",      "r5",       "r6",
		   //  10         11 
		     "r7",      "r8" 
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		StringTokenizer st = new StringTokenizer(lineStr, "\"");
		
		int cnt = 0;
		
		String ifno = st.nextToken();
		setValue(cnt++, ifno);
		String name = st.nextToken();
		setValue(cnt++, name);
		
		int m = 2;

		String str = st.nextToken();
		st = new StringTokenizer(str);
		cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());
	}
}