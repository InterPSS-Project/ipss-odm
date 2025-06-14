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

package org.ieee.odm.adapter.psse.json.parser;

import java.util.List;

import org.ieee.odm.adapter.common.json.BaseInputRowJSonParser;
import org.ieee.odm.util.Counter;

/**
 * PSSE JSon format data parser. Its implementation is based on the field definition
 * in the JSon file.
 * 
 *          "fields":["iarea", "isw", "pdes",    "ptol",   "arname"], 
            "data":  [1,       101,   -2800.000, 10.00000, "CENTRAL"],  '
 * 
 * @author mzhou
 *
 */
public class PSSEDataJSonParser extends BaseInputRowJSonParser  {	
	/**
	 * Constructor
	 */
	public PSSEDataJSonParser() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEDataJSonParser(List<String> fieldDef) {
		super();
		// Load the field definitions into the position table.
		Counter cnt = new Counter();
		fieldDef.forEach(key -> this.positionTable.put(cnt.getCountThenIncrement(), key));
	}
}