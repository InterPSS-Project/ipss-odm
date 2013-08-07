/*
 * @(#)GEBusDataParser.java   
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
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class GEBusDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<number> <"name"> <kV> : <ty> <vs> <vt> <an> <ar> <z> <vma> <vmi> <d_in> <d_out> <projid> <level> <owner> 
		 *      <stisol> <latitude> <longitude> <islnum>
		 */
		return new String[] {
		   //  0----------1----------2----------3------------4
		     "number",  "name",    "kV",       "ty",        "vs",  
		   //  5          6          7          8            9   
		     "vt",      "an",      "ar",       "z",         "vma", 
		   //  10         11         12         13           14
		     "vmi",     "d_in",    "d_out",    "proj_id",   "level",
		   //  15         16         17         18           19  
		     "owner",   "stisol",  "latitude", "longitude", "islnum"
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		int n = lineStr.indexOf(':');
		String str1 = lineStr.substring(0, n),
			   str2 = lineStr.substring(n+1);
			
		int m = 3;
		StringTokenizer st = new StringTokenizer(str1, "\"");
		for (int cnt = 0; cnt < m; cnt++)
			setValue(cnt, st.nextToken().trim());
			
		st = new StringTokenizer(str2);
		int cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());
	}
}