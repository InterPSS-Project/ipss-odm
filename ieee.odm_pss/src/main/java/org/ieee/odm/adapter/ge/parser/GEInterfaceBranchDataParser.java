/*
 * @(#)GEInterfaceBranchDataParser.java   
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
 * Class for processing GE PSLF interface branch data line string
 * 
 * @author mzhou
 *
 */
public class GEInterfaceBranchDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<f bus> <"f name"> <f bkv> <t bus> <"t name"> <t bkv> <"ck"> <ifn> <pf>
		 * 
		 Sample data
		 79 "E-55    " 380.00       1 "P-1     " 380.00 "1 "   :      1     1.000
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "f_bus",   "f_name",  "f_bkv",   "t_bus",   "t_name",
		   //  5          6          7          8           
		     "t_bkv",   "ck",      "ifn",     "pf" 
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		String str1 = lineStr.substring(0, lineStr.indexOf(':')),
	           str2 = lineStr.substring(lineStr.indexOf(':')+1);
		
		int cnt = 0;
		
		StringTokenizer st = new StringTokenizer(str1, "\"");
		String f_bus = st.nextToken().trim();
		setValue(cnt++, f_bus);
		String f_name  = st.nextToken().trim();
		setValue(cnt++, f_name);
		
		String s = st.nextToken();
		StringTokenizer st1 = new StringTokenizer(s);
		String f_bkv  = st1.nextToken().trim();
		setValue(cnt++, f_bkv);
		String t_bus = st1.nextToken().trim();
		setValue(cnt++, t_bus);
		
		String t_name = st.nextToken().trim();
		setValue(cnt++, t_name);
		String t_bkv = st.nextToken().trim();
		setValue(cnt++, t_bkv);
		String ck = st.nextToken().trim();
		setValue(cnt++, ck);

		int m = 7;
			
		st = new StringTokenizer(str2);
		cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());	
	}
}