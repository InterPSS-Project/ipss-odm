/*
 * @(#)GEBranchDataParser.java   
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
 * Class for processing GE PSLF branch data line string
 * 
 * @author mzhou
 *
 */
public class GEBranchDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<f bus> <"f name"> <f bkv> <t bus> <"t name"> <t bkv> <"ck"> <sec> <"long id">
                <st> <r> <x> <b> <r1> <r2> <r3> <r4> <al> <l> <ar> <z> <gi> <tf> <tt> 
                <d_in> <d_out> <proj id> <nst> <type> <r5> <r6> <r7> <r8>
                <o1> <p1> <o2> <p2> <o3> <p3> <o4> <p4> <o5> <p5> <o6> <p6> <o7> <p7> <o8> <p8> <ohms> 
                
		Sample data:
		
      	1 "P-1     " 380.00       2 "P-2     " 380.00 "1 "  1 "        " :  1 0.00000 0.02348 0.00000    
      	0.0    0.0    0.0    0.0 1.000    1.0 /
  		1 201 0.0000 0.000 1.000   400101   391231   0 1  0    0.0    0.0    0.0    
  		0.0   1 1.000   0 1.000   0 1.000   0 1.000   0 0.000   0 0.000   0 0.000   0 0.000  0
	                
		 *      
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "f_bus",   "f_name",   "f_bkv",   "t_bus",  "t_name",   
		   //  5          6          7          8          9   
		     "t_bkv",   "ck",       "sec",    "long_id",  "st",
		   //  10         11         12         13         14
		     "r",       "x",        "b",       "r1",      "r2",
		   //  15         16         17         18         19  
		     "r3",      "r4",       "al",      "l",       "ar",
		   //  20         21         22         23         24
		     "z",       "gi",       "tf",      "tt",      "d_in",
		   //  25         26         27         28         29
		     "d_out",   "proj_id",  "nst",     "type",    "r5",
		   //  30         31         32         33         34  
		     "r6",      "r7",       "r8",      "o1",      "p1",      
		   //  35         36         37         38         39 
		     "o2",      "p2",       "o3",      "p3",      "o4",      
		   //  40         41         42         43         44   
		     "p4",      "o5",       "p5",      "o6",      "p6",      
		   //  45         46         47         48         49  
		     "o7",      "p7",       "o8",      "p8",      "ohms"
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		int n = lineStr.indexOf(':');
		String str1 = lineStr.substring(0, n),
			   str2 = lineStr.substring(n+1);
			
		int cnt = 0;
		/*
		 * Sample data : 1 "P-1     " 380.00       2 "P-2     " 380.00 "1 "  1 "        "
		 */
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
		String sec = st.nextToken().trim();
		setValue(cnt++, sec);
		String long_id = st.nextToken().trim();
		setValue(cnt++, long_id);

		int m = 9;
			
		st = new StringTokenizer(str2);
		cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());		
	}
}