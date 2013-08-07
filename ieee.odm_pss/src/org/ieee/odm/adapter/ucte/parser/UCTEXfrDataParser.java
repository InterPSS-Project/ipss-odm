/*
 * @(#)UCTEXfrDataParser.java   
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

package org.ieee.odm.adapter.ucte.parser;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ModelStringUtil;

/**
 * Class for processing UCTE xfr data line string
 * 
 * @author mzhou
 *
 */
public class UCTEXfrDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
			//  0---------------1---------------2---------------3---------------4
			 "fromNodeId",   "toNodeId",     "orderCode",    "status",       "fromRatedKV",  
			//  5               6               7               8               9
			 "toRatedKV",    "normialMva",    "rOhm",         "xOhm",        "bMuS",       
			//  10              11              12
			 "gMuS",         "currentLimit",  "elemName"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		String fromNodeId = ModelStringUtil.getString(str, 1, 8).trim().replace(' ', '_');
		this.setValue(0, fromNodeId);
		
		String toNodeId = ModelStringUtil.getString(str, 10, 17).trim().replace(' ', '_');
		this.setValue(1, toNodeId);
		
		String orderCode = ModelStringUtil.getString(str, 19, 19);
		this.setValue(2, orderCode);
		
		String status = ModelStringUtil.getString(str, 21, 21);  // 0 real, i equivalent
		this.setValue(3, status);
		
		String fromRatedKV = ModelStringUtil.getString(str, 23, 27);  
		this.setValue(4, fromRatedKV);
		
		String toRatedKV = ModelStringUtil.getString(str, 29, 33);  
		this.setValue(5, toRatedKV);
		
		String normialMva = ModelStringUtil.getString(str, 35, 39);  
		this.setValue(6, normialMva);
		
		String rOhm = ModelStringUtil.getString(str, 41, 46);  
		this.setValue(7, rOhm);
		
		String xOhm = ModelStringUtil.getString(str, 48, 53);  
		this.setValue(8, xOhm);
		
		String bMuS = ModelStringUtil.getString(str, 55, 62);  
		this.setValue(9, bMuS);
		
		String gMuS = ModelStringUtil.getString(str, 64, 69);  
		this.setValue(10, gMuS);
		
		String currentLimit = ModelStringUtil.getString(str, 71, 76);  
		this.setValue(11, currentLimit);
		
		String elemName = ModelStringUtil.getString(str, 78, 89);
		this.setValue(12, elemName);
	}
}