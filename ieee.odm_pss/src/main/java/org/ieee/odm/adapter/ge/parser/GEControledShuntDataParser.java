/*
 * @(#)GEControledShuntDataParser.java   
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

import org.ieee.odm.common.ODMException;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class GEControledShuntDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<bus> <"name"> <bkv> <"id"> <"long id"> : <st> <ty> <kreg bus> <"kreg name"> 
                <kreg bkv> <ar> <z> <g> <b> <bmin> <bmax> <vband> <befmin> <befmax> 
                <d_in> <d_out> <proj id> <nst> <o1> <p1> <o2> <p2> <o3> <p3> <o4> <p4> 
                <n1 b1> <n2 b2> ... <n10 b10>
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "bus",     "name",    "bkv",     "id",       "long_id",      
		   //  5          6          7          8          9
		     "st",      "ty",    "kreg_bus", "kreg_name", "kreg_bkv",      
		   //  10         11         12         13         14
		     "ar",      "z",        "g",       "b",       "bmin",      
		   //  15         16         17         18         19
		     "bmax",   "vband",   "befmin",   "befmax",   "d_in",      
		   //  20         21         22         23         24
		     "d_out",  "proj_id",  "nst",      "o1",      "p1",      
		   //  25         26         27         28         29
		     "o2",      "p2",       "o3",      "p3",      "o4",      
		   //  30         31         32         33         34
		     "p4",      "n1",       "b1",      "n2",      "b2",      
		   //  35         36         37         38         39
		     "n3",      "b3",       "n4",      "b4",      "n5",      
           //  40         41         42         43         44
		     "b5",      "n6",       "b6",      "n7",      "b7",      
           //  45         46         47         48         49
		     "n8",      "b8",       "n9",      "b9",      "n10",      
           // 50
		     "b10"  
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
	}
}