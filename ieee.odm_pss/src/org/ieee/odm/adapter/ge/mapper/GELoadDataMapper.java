 /*
  * @(#)GELoadDataMapper.java   
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
import org.ieee.odm.adapter.ge.parser.GELoadDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;

public class GELoadDataMapper extends BaseGEDataMapper {
	public GELoadDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GELoadDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
	    final String busId = AbstractModelParser.BusIdPreFix+dataParser.getString("bus");
		// get the responding-bus data with busId
		LoadflowBusXmlType busRec = parser.getBus(busId);
		if (busRec==null)
			throw new ODMException("Error: Bus not found in the network, bus number: " + busId);
			
		// ODM allows one equiv load has many contribute loads, but here, we assume there is only one contribute load.

		LoadflowLoadDataXmlType contribLoad = AclfParserHelper.createContriLoad(busRec);
			
		contribLoad.setId(dataParser.getString("id"));
		String longId = dataParser.getString("long_id");
		if (longId != null && !longId.equals(""))
			contribLoad.setDesc(longId);
		contribLoad.setAreaNumber(dataParser.getInt("ar"));
		contribLoad.setZoneNumber(dataParser.getInt("z"));
		    
		// <st> gen status 1 =	in service; 0 =	out of service
		contribLoad.setOffLine(dataParser.getInt("st") != 1);
			
		// <stn> Normal gen status 1=in service; 0=out of service
		contribLoad.setNormalOffLineStatus(dataParser.getInt("stn") != 1);		

	    double p = dataParser.getDouble("p");
	    double q = dataParser.getDouble("q");
		if (p != 0.0 || q != 0.0)
			contribLoad.setConstPLoad(BaseDataSetter.createPowerValue(p, q, ApparentPowerUnitType.MVA));

	    double ip = dataParser.getDouble("ip");
	    double iq = dataParser.getDouble("iq");
		if (ip != 0.0 || iq != 0.0)
			contribLoad.setConstILoad(BaseDataSetter.createPowerValue(ip, iq, ApparentPowerUnitType.MVA));

	    double g = dataParser.getDouble("g");
	    double b = dataParser.getDouble("b");
		if (g != 0.0 || b != 0.0)
			contribLoad.setConstZLoad(BaseDataSetter.createPowerValue(g, b, ApparentPowerUnitType.MVA));
	}
}
