/*
 * @(#)BasePSSEDataParser.java   
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

package org.ieee.odm.adapter.psse.parser.aclf;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public abstract class BasePSSEDataParser extends AbstractDataFieldParser {
	   //  0----------1----------2----------3----------4
	   //  5          6          7          8          9
	   //  10         11         12         13         14
	   //  15         16         17         18         19
	   //  20         21         22         23         24
	   //  25         26         27         28         29
	   //  30         31         32         33         34
	   //  35         36         37         38         39
	   //  40         41         42         43         44
	   //  45         46         47         48         49
		  
	protected PsseVersion version = PsseVersion.PSSE_30;
	
	public BasePSSEDataParser() {
		super();
	}

	public BasePSSEDataParser(PsseVersion ver) {
		super();
		this.version = ver;
	}
}