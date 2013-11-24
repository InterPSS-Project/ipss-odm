/*
 * @(#)UCTEExPowerDataMapper.java   
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

import org.ieee.odm.adapter.ucte.parser.UCTEExPowerDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.UCTEExchangeXmlType;

public class UCTEExPowerDataMapper extends BaseUCTEDataMapper {

	public UCTEExPowerDataMapper() {
		this.dataParser = new UCTEExPowerDataParser();
	}

	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		this.dataParser.parseFields(str);
		
		String fromIsoId = this.dataParser.getString("fromIsoId"), 
		       toIsoId = this.dataParser.getString("toIsoId"), 
		       comment = this.dataParser.getString("", "comment");
		double exPower = this.dataParser.getDouble("exPower", 0.0);  

		LoadflowNetXmlType baseCaseNet = parser.getNet();
		InterchangeXmlType interChange = OdmObjFactory.createInterchangeXmlType();
		baseCaseNet.getInterchangeList().getInterchange().add(interChange);
		
		UCTEExchangeXmlType ucteExRec = OdmObjFactory.createUCTEExchangeXmlType(); 
		interChange.setUcteExchange(ucteExRec);
		ucteExRec.setFromIsoId(fromIsoId);
		ucteExRec.setToIsoId(toIsoId);
		ucteExRec.setExchangePower(BaseDataSetter.createPowerValue(exPower, 0.0, ApparentPowerUnitType.MVA)); 
		if (comment != null)
			ucteExRec.setComment(comment);	
	}
}