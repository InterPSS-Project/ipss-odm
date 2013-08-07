/*
 * @(#)UCTEXfrAdjustDataParser.java   
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
 * Class for processing UCTE xfr adjustment data line string
 * 
 * @author mzhou
 *
 */
public class UCTEXfrAdjustDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0---------------1---------------2---------------3---------------4
			 "fromNodeId", "toNodeId",     "orderCode",     "dUPhase",     "nPhase",
		   //  5               6               7               8               9
			 "n1Phase",    "uKvPhase",     "dUAngle",      "thetaDegAngle", "nAngle",  
		   //  10              11              12              13              14
			 "n1Angle",    "pMwAngle",     "type"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		int cnt = 0;
		String fromNodeId = ModelStringUtil.getString(str, 1, 8).trim().replace(' ', '_');
		this.setValue(cnt++, fromNodeId);
		String toNodeId = ModelStringUtil.getString(str, 10, 17).trim().replace(' ', '_');
		this.setValue(cnt++, toNodeId);
		String orderCode = ModelStringUtil.getString(str, 19, 19);
		this.setValue(cnt++, orderCode);

		String dUPhase = ModelStringUtil.getString(str, 21, 25);  
		this.setValue(cnt++, dUPhase);
		String nPhase = ModelStringUtil.getString(str, 27, 28);  
		this.setValue(cnt++, nPhase);
		String n1Phase = ModelStringUtil.getString(str, 30, 32);  
		this.setValue(cnt++, n1Phase);
		String uKvPhase = ModelStringUtil.getString(str, 34, 38);  
		this.setValue(cnt++, uKvPhase);

		String dUAngle = ModelStringUtil.getString(str, 40, 44);  
		this.setValue(cnt++, dUAngle);
		String thetaDegAngle = ModelStringUtil.getString(str, 46, 50);  
		this.setValue(cnt++, thetaDegAngle);
		String nAngle = ModelStringUtil.getString(str, 52, 53);  
		this.setValue(cnt++, nAngle);
		String n1Angle = ModelStringUtil.getString(str, 55, 57);  
		this.setValue(cnt++, n1Angle);
		String pMwAngle = ModelStringUtil.getString(str, 59, 63);  
		this.setValue(cnt++, pMwAngle);

		String type = ModelStringUtil.getString(str, 65, 68);		
		this.setValue(cnt++, type);
	}
}