/*
 * @(#)GEGenDataParser.java   
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
public class GEGenDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<bus> <"name"> <bkv> <"id"> <"long id"> : <st> <igreg bus> <"igreg name"> 
                <igreg bkv> <prf> <qrf> <ar> <z> <pgen> <pmax> <pmin> <qgen> <qmax> <qmin> 
                <mbase> <rcomp> <xcomp> <zgenr> <zgenx> <h bus> <"h name"> <h bkv> <t bus> 
                <"t name"> <t bkv> <d_in> <d_out> <projid> <stn> <rtr> <xtr> <gtap> 
                <o1> <p1> <o2> <p2> <o3> <p3> <o4> <p4> <o5> <p5> <o6> <p6> <o7> <p7> <o8> <p8> 
                <gov_flag> <agc_flag> <dispatch_flag> <baseload_flag> <air_temp> 
                <turbine_type> <qtab> <pmax2>
		 */
		return new String[] {
		   //  0----------1----------2----------3------------4
		     "bus",     "name",    "bkv",     "id",      "long_id",    
		   //  10         11         12         13          14
		     "st",  "igreg_bus", "igreg_name", "igreg_bkv",  "prf",    
		   //  15         16         17         18          19
		     "qrf",      "ar",      "z",      "pgen",      "pmax",    
		   //  20         21         22         23          24
		     "pmin",    "qgen",    "qmax",    "qmin",      "mbase",    
		   //  25         26         27         28          29
		     "rcomp",   "xcomp",   "zgenr",    "zgenx",    "h_bus",    
		   //  30         31         32         33          34
		     "h_name",  "h_bkv",   "t_bus",    "t_name",   "t_bkv", 
		   //  35         36         37         38          39
		     "d_in",    "d_out",   "proj_id",  "stn",      "rtr",    
		   //  40         41         42         43          44
		     "xtr",     "gtap",    "o1",       "p1",       "o2",   
		   //  45         46         47         48          49
		     "p2",      "o3",      "p3",       "o4",       "p4",    
		   //  50         51         52         53          54
		     "o5",      "p5",      "o6",       "p6",       "o7",    
		   //  55         56         57         58          59
		     "p7",      "o8",      "p8",      "gov_flag",  "agc_flag",    
		   //  60               61                62          63              64
		     "dispatch_flag", "baseload_flag",  "air_temp", "turbine_type",  "qtab",    
		   // 65
		     "pmax2"
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		int n = lineStr.indexOf(':');
		String str1 = lineStr.substring(0, n),
			   str2 = lineStr.substring(n+1);
			
		int m = 5;
		StringTokenizer st = new StringTokenizer(str1, "\"");
		for (int cnt = 0; cnt < 5; cnt++)
			setValue(cnt, st.nextToken().trim());
			
		st = new StringTokenizer(str2);
		int cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());
	}
}