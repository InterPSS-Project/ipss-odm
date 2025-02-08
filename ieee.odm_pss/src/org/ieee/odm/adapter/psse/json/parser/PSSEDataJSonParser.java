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

import org.ieee.odm.adapter.common.obj.BaseInputRowObjParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;

/**
 * 
 * @author mzhou
 *
 */
public class PSSEDataJSonParser extends BaseInputRowObjParser  {		  
	protected PsseVersion version = PsseVersion.PSSE_JSON;
	
	List<String> fieldDef = null;
	
	public PSSEDataJSonParser() {
		super();
	}

	public PSSEDataJSonParser(PsseVersion ver, List<String> fieldDef) {
		super();
		this.version = ver;
		this.fieldDef = fieldDef;
	}
}