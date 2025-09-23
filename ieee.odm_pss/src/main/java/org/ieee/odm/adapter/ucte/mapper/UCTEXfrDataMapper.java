/*
 * @(#)UCTEXfrDataMapper.java   
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

import org.ieee.odm.adapter.ucte.parser.UCTEXfrDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.MagnitizingZSideEnumType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UCTEXfrDataMapper extends BaseUCTEDataMapper {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(UCTEXfrDataMapper.class.getName());
	
	public UCTEXfrDataMapper() {
		this.dataParser = new UCTEXfrDataParser();
	}

	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		this.dataParser.parseFields(str);

		String fromNodeId= this.dataParser.getValue("fromNodeId"), 
		       toNodeId= this.dataParser.getValue("toNodeId"), 
		       orderCode= this.dataParser.getValue("orderCode"), 
		       elemName= this.dataParser.getValue("elemName", "");
		int    status= this.dataParser.getInt("status", 0), 
		       currentLimit= this.dataParser.getInt("currentLimit", 0);
		double fromRatedKV= this.dataParser.getDouble("fromRatedKV", 0.0), 
		       toRatedKV= this.dataParser.getDouble("toRatedKV", 0.0), 
		       normialMva= this.dataParser.getDouble("normialMva", 0.0),
		       rOhm= this.dataParser.getDouble("rOhm", 0.0), 
		       xOhm= this.dataParser.getDouble("xOhm", 0.0), 
		       bMuS= this.dataParser.getDouble("bMuS", 0.0), 
		       gMuS= this.dataParser.getDouble("gMuS", 0.0);  

    	if (status == 0 || status == 8) {
    		if (rOhm < 0.0 || xOhm < 0.0) {
    			throw new ODMException("Error: transform r < 0 or x < 0, data line: " + str);
    		}
    	}

    	// create a branch record
		XfrBranchXmlType branchRec = parser.createXfrBranch(fromNodeId, toNodeId, orderCode);
      	if (elemName != null)
      		branchRec.setName(elemName);

      	// r, x, g, b are measured at from side in Ohms
		// they are converted to PU using from bus base voltage
		if (fromRatedKV < toRatedKV) {
			// TODO: need to transfer R,X to high voltage side
			log.error("Need more implementation");
		}
		// XformerData object created in the following call
		AclfDataSetter.createXformerData(branchRec,
				rOhm, xOhm, ZUnitType.OHM, 1.0, 1.0, gMuS, bMuS,
				YUnitType.MICROMHO, MagnitizingZSideEnumType.FROM_SIDE);

		AclfDataSetter.setXfrRatingData(branchRec,
				fromRatedKV, toRatedKV, VoltageUnitType.KV,
				normialMva, ApparentPowerUnitType.MVA);

    	// by default the branch is active
    	if (status == 8 || status == 9) 
    		branchRec.setOffLine(true);
    	
    	branchRec.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
    	AclfDataSetter.setBranchRatingLimitData(branchRec.getRatingLimit(),
				currentLimit, CurrentUnitType.AMP);
	}
}