/*
 * @(#)GELineDataParser.java   
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
 * Class for processing GE PSLF line data line string
 * 
 * @author mzhou
 *
 */
public class GELineDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<f bus> <"f name"> <f bkv> <t bus> <"t name"> <t bkv> <"ck"> <sec> <"long id">
                <st> <"p name"> <len> <ws1> <cond1> <ws2> <cond2> <ws3> <cond3> <ws4> 
                <cond4> <ws5> <cond5> <ar> <z> <ncb> <rating> <d_in> <d_out> <projid> <nst> 
                <o1> <p1> <o2> <p2> <o3> <p3> <o4> <p4> <o5> <p5> <o6> <p6> <o7> <p7> 
                <o8> <p8> <r> <x> <b> <r0> <x0> <b0> <a0> <a1> <a2> <a3> <a4> <al>
		 *      
		 */
		 return new String[] {
		   //  0----------1----------2----------3----------4
		     "f_bus",  "f_name",   "f_bkv",   "t_bus",   "t_name",   
		   //  5          6          7          8          9
		     "t_bkv",   "ck",      "sec",     "long_id",   "st",   
		   //  10         11         12         13         14
		     "p_name",  "len",     "ws1",     "cond1",     "ws2",   
		   //  15         16         17         18         19
		     "cond2",   "ws3",     "cond3",   "ws4",      "cond4",   
		   //  20         21         22         23         24
		     "ws5",     "cond5",   "ar",       "z",       "ncb",   
		   //  25         26         27         28         29
		     "rating",   "d_in",   "d_out",   "proj_id",  "nst",   
		   //  30         31         32         33         34
		     "o1",       "p1",     "o2",       "p2",       "o3",   
		   //  35         36         37         38         39
		     "p3",       "o4",     "p4",       "o5",       "p5",   
		   //  40         41         42         43         44
		     "o6",       "p6",     "o7",       "p7",       "o8",   
		   //  45         46         47         48         49
		     "p8",       "r",      "x",        "b",        "r0",   
		   //  50         51         52         53         54
		     "x",        "b0",     "a0",       "a1",       "a2",   
		   //  55         56         57        
		     "a3",       "a4",     "al"
	     };
	}
	
	@Override public void parseFields(final String str) throws ODMException {
	}
}