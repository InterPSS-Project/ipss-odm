 /*
  * @(#)GEZoneDataMapper.java   
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
import org.ieee.odm.adapter.ge.parser.GEZoneDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.ExchangeZoneXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;

public class GEZoneDataMapper extends BaseGEDataMapper {
	public GEZoneDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEZoneDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		if (baseCaseNet.getLossZoneList() == null)
			baseCaseNet.setLossZoneList(odmObjFactory.createNetworkXmlTypeLossZoneList());
		ExchangeZoneXmlType zone = odmObjFactory.createExchangeZoneXmlType(); 
		baseCaseNet.getLossZoneList().getLossZone().add(zone);
		zone.setId(dataParser.getString("zonum"));
		zone.setNumber(dataParser.getInt("zonum"));
		zone.setName(dataParser.getString("zonam"));
		
		double pznet = dataParser.getDouble("pznet");
		double qznet = dataParser.getDouble("qznet");
		zone.setExchangePower(BaseDataSetter.createPowerValue(pznet, qznet, ApparentPowerUnitType.MVA)); 		
	}
}
