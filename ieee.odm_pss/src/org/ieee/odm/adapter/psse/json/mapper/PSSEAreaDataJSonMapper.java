 /*
  * @(#)PSSEAreaDataMapper.java   
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
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.psse.json.mapper;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.util.List;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.json.parser.PSSEDataJSonParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ExchangeAreaXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEAreaDataJSonMapper extends BasePSSEDataJSonMapper{
	
	public PSSEAreaDataJSonMapper(PsseVersion ver, List<String> fieldDef) {
		super(ver);
		this.dataParser = new PSSEDataJSonParser(ver, fieldDef);
	}
	

	public void proc(List<Object> areaData, BaseAclfModelParser<? extends NetworkXmlType> parser) {
		dataParser.parseFields(areaData.toArray());
		
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		if (baseCaseNet.getAreaList() == null)
			baseCaseNet.setAreaList(OdmObjFactory.createNetworkXmlTypeAreaList());
		ExchangeAreaXmlType area = OdmObjFactory.createExchangeAreaXmlType();
		baseCaseNet.getAreaList().getArea().add(area);
		
		/*
            "fields":["iarea", "isw", "pdes",    "ptol",   "arname"], 
            "data":  [1,       101,   -2800.000, 10.00000, "CENTRAL"],  '
		 */		
		try {
			int i = this.dataParser.getInt("I");
			int isw = this.dataParser.getInt("ISW");
			double pdes = this.dataParser.getDouble("PDES");
			double ptol = this.dataParser.getDouble("PTOL");
			String arnam = this.dataParser.getString("ARNAM");
			
			area.setId(new Integer(i).toString());
			area.setNumber(i);
			area.setName(arnam);

			if (isw > 0) {
				area.setSwingBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+isw));
				area.setDesiredExchangePower(BaseDataSetter.createActivePowerValue(pdes, ActivePowerUnitType.MW));
				area.setExchangeErrTolerance(BaseDataSetter.createActivePowerValue(ptol, ActivePowerUnitType.MW));			
			}	
		} catch (ODMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
