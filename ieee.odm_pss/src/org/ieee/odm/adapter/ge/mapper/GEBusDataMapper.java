 /*
  * @(#)GEBusDataMapper.java   
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

import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.adapter.ge.parser.GEBusDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.VoltageUnitType;

public class GEBusDataMapper extends BaseGEDataMapper {
	public GEBusDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEBusDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		long num = dataParser.getLong("number");
		final String busId = IODMModelParser.BusIdPreFix+num;
		// XML requires id start with a char
		LoadflowBusXmlType busRec;
		busRec = parser.createBus(busId, num);
		busRec.setNumber(num);

		busRec.setAreaNumber(dataParser.getInt("ar"));
		busRec.setZoneNumber(dataParser.getInt("z"));	
		busRec.setName(dataParser.getString("name"));
		busRec.setBaseVoltage(BaseDataSetter.createVoltageValue(dataParser.getDouble("kV"), VoltageUnitType.KV));
		
		int owner = dataParser.getInt("owner");
		if (owner > 0)
			BaseJaxbHelper.addOwner(busRec, new Integer(owner).toString());

		int ty = dataParser.getInt("ty");
		LFGenCodeEnumType genType = ty == 0? LFGenCodeEnumType.SWING : 
				( ty == 1? LFGenCodeEnumType.PQ : LFGenCodeEnumType.PV);
		AclfDataSetter.setGenData(busRec, genType, dataParser.getDouble("vs"), 
				VoltageUnitType.PU, dataParser.getDouble("an"), AngleUnitType.DEG, 
				0.0, 0.0,	ApparentPowerUnitType.MVA);
		busRec.getGenData().getEquivGen().getValue().setVoltageLimit(
				BaseDataSetter.createVoltageLimit(dataParser.getDouble("vma"), dataParser.getDouble("vmi"), VoltageUnitType.PU));
	}
}
