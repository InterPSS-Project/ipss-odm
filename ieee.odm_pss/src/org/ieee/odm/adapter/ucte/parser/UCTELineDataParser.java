/*
 * @(#)UCTELineDataParser.java   
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
 * Class for processing UCTE line data line string
 * 
 * @author mzhou
 *
 */
public class UCTELineDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0---------------1---------------2---------------3---------------4
			 "fromNodeId",   "toNodeId",     "orderCode",    "status",      "rOhm", 
		   //  5               6               7               8               9
			"xOhm",          "bMuS",         "currentLimit", "elemName"
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
		
		String rOhm = ModelStringUtil.getString(str, 23, 28);  
		this.setValue(4, rOhm);
		
		String xOhm = ModelStringUtil.getString(str, 30, 35);  
		this.setValue(5, xOhm);
		
		String bMuS = ModelStringUtil.getString(str, 37, 44);  
		this.setValue(6, bMuS);
		
		String currentLimit = ModelStringUtil.getString(str, 46, 51);  
		this.setValue(7, currentLimit);

		String elemName = ModelStringUtil.getString(str, 53, 64);		
		this.setValue(8, elemName);
	}
}