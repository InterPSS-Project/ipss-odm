/*
 * @(#)GEXfrZAdjustDataParser.java   
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
public class GEXfrZAdjustDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<no> <ta> <t0> <f0> <t1> <f1> ... <t10> <f10>
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "no",      "ta",      "t0",      "f0",      "t1",    
		   //  5          6          7          8          9
		     "f1",      "t2",      "f2",      "t3",      "f3",
		   //  10         11         12         13         14  
		     "t4",      "f4",      "t5",      "f5",      "t6",    
		   //  15         16         17         18         19
		     "f6",      "t7",      "f7",      "t8",      "f8",
		   //  20         21         22         23         
		     "t9",      "f9",      "t10",     "f10" 
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
	}
}