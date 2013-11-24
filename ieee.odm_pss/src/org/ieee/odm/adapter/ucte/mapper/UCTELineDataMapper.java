/*
 * @(#)UCTELineDataMapper.java   
 *
 * Copyright (C) 2006 www.interpss.org
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.ucte.mapper;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.ucte.parser.UCTELineDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class UCTELineDataMapper extends BaseUCTEDataMapper {

	public UCTELineDataMapper() {
		this.dataParser = new UCTELineDataParser();
	}

	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		this.dataParser.parseFields(str);
		
		// parse the input line for line information
		String fromNodeId = this.dataParser.getString("fromNodeId"), 
		       toNodeId = this.dataParser.getString("toNodeId"), 
		       orderCode = this.dataParser.getString("orderCode"), 
		       elemName = this.dataParser.getString("elemName", "");
		int    status = this.dataParser.getInt("status", 0), 
		       currentLimit = this.dataParser.getInt("currentLimit", 0);
		double rOhm = this.dataParser.getDouble("rOhm", 0.0), 
		       xOhm = this.dataParser.getDouble("xOhm", 0.0), 
		       bMuS = this.dataParser.getDouble("bMuS", 0.0);  

    	// create a branch record
		LineBranchXmlType aclfLine = parser.createLineBranch(fromNodeId, toNodeId, orderCode);
      	if (elemName != null)
      		aclfLine.setName(elemName);

		// LineData object created in the following call
		AclfDataSetter.setLineData(aclfLine, rOhm, xOhm,
				ZUnitType.OHM, 0.0, bMuS, YUnitType.MICROMHO);
      	
    	// by default the branch is active
    	if (status == 8 || status == 9) 
    		aclfLine.setOffLine(true);

    	aclfLine.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
    	AclfDataSetter.setBranchRatingLimitData(aclfLine.getRatingLimit(),
				currentLimit, CurrentUnitType.AMP);		
	}
}