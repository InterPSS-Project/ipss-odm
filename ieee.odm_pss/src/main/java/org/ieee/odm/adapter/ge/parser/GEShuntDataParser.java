/*
 * @(#)GEShuntDataParser.java   
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
 * Class for processing GE PSLF shunt data line string
 * 
 * @author mzhou
 *
 */
public class GEShuntDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<fbus> <"fname"> <fbkv> <"id"> <tbus> <"tname"> <tbkv> <"ck"> <sec> 
                <"long id"> <st> <ar> <z> <g> <b> <d_in> <d_out> <proj id> <nst> 
                <o1> <p1> <o2> <p2> <o3> <p3> <o4> <p4> <reg bus> <"rname"> <rkv>
		 */
		return new String[] {
		   //  0----------1----------2----------3------------4
		     "fbus",    "fname",   "fbkv",     "id",       "tbus",
		   //  5          6          7          8          9  
		     "tname",   "tbkv",    "ck",       "sec",      "long_id",       
		   //  10         11         12         13         14
		     "st",       "ar",      "z",       "g",        "b",       
		   //  15         16         17         18         19
		     "d_in",     "d_out",   "proj_id", "nst",      "o1",       
		   //  20         21         22         23         24
		     "p1",       "o2",      "p2",      "o3",       "p3",       
		   //  25         26         27         28         29
		     "o4",       "p4",     "reg_bus",  "rname",    "rkv"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
	}
}