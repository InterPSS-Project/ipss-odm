 /*
  * @(#)GEAreaDataMapper.java   
  *
  * Copyright (C) 2006-2008 www.interpss.org
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
  * @Date 06/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.ge.mapper;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.adapter.ge.parser.GEAreaDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.ExchangeAreaXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;

public class GEAreaDataMapper extends BaseGEDataMapper {
	public GEAreaDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEAreaDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		if (baseCaseNet.getAreaList() == null)
			baseCaseNet.setAreaList(odmObjFactory.createNetworkXmlTypeAreaList());
		ExchangeAreaXmlType area = odmObjFactory.createExchangeAreaXmlType();
		baseCaseNet.getAreaList().getArea().add(area);
		area.setId(dataParser.getString("arnum"));
		area.setNumber(dataParser.getInt("arnum"));
		area.setName(dataParser.getString("arnam"));	
		area.setSwingBusId(parser.createBusRef(dataParser.getString("swing")));
		
		double pnet = dataParser.getDouble("pnet");
		double qnet = dataParser.getDouble("qnet");
		area.setTotalExchangePower(BaseDataSetter.createPowerValue(pnet, qnet, ApparentPowerUnitType.MVA));

		double pnetdes = dataParser.getDouble("pnetdes");
		double pnettol = dataParser.getDouble("pnettol");
		area.setDesiredExchangePower(BaseDataSetter.createActivePowerValue(pnetdes, ActivePowerUnitType.MW));
		area.setExchangeErrTolerance(BaseDataSetter.createActivePowerValue(pnettol, ActivePowerUnitType.MW));		
	}
}
