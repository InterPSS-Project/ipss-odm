/*
 * @(#)UCTEExPowerDataParser.java   
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
import org.ieee.odm.model.base.ODMModelStringUtil;

/**
 * Class for processing UCTE exchange power data line string
 * 
 * @author mzhou
 *
 */
public class UCTEExPowerDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0---------------1---------------2---------------3---------------4
			 "fromNodeId",   "toNodeId",     "exPower",     "comment"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		String fromIsoId = ODMModelStringUtil.getString(str, 1, 2);
		this.setValue(0, fromIsoId);
		
		String toIsoId = ODMModelStringUtil.getString(str, 4, 5);
		this.setValue(1, toIsoId);
		
		String exPower = ODMModelStringUtil.getString(str, 7, 13);  
		this.setValue(2, exPower);
		
		String comment = ODMModelStringUtil.getString(str, 15, 26);
		this.setValue(3, comment);
	}
}