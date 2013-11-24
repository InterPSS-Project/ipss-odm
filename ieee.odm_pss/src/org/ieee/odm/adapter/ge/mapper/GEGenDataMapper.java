 /*
  * @(#)GEGenDataMapper.java   
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
import org.ieee.odm.adapter.ge.parser.GEGenDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.ZUnitType;

public class GEGenDataMapper extends BaseGEDataMapper {
	public GEGenDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEGenDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
	    final String busId = IODMModelParser.BusIdPreFix+dataParser.getString("bus");
		// get the responding-bus data with busId
		LoadflowBusXmlType busRec = parser.getBus(busId);
		if (busRec==null)
			throw new ODMException("Error: Bus not found in the network, bus number: " + busId);
		
	    // ODM allows one equiv gen has many contribute generators, but here, we assume there is only one contribute gen.

	    LoadflowGenDataXmlType contriGen = AclfParserHelper.createContriGen(busRec);
		
	    contriGen.setId(dataParser.getString("id"));
	    String longId = dataParser.getString("long_id");
		if (longId != null && !longId.equals(""))
			contriGen.setDesc(longId);
	    contriGen.setAreaNumber(dataParser.getInt("ar"));
	    contriGen.setZoneNumber(dataParser.getInt("z"));
	    
		// <st> gen status 1 =	in service; 0 =	out of service
	    contriGen.setOffLine(dataParser.getInt("st") != 1);
		
	    // <stn> Normal gen status 1=in service; 0=out of service
	    contriGen.setNormalOffLineStatus(dataParser.getInt("stn") != 1);		
		
	    contriGen.setRemoteVoltageControlBus(parser.createBusRef(IODMModelParser.BusIdPreFix+dataParser.getString("igreg_bus")));
		
	    contriGen.setMwControlParticipateFactor(dataParser.getDouble("prf"));
	    contriGen.setMvarVControlParticipateFactor(dataParser.getDouble("qrf"));

	    contriGen.setMvaBase(BaseDataSetter.createPowerMvaValue(dataParser.getDouble("mbase")));
	    contriGen.setPower(BaseDataSetter.createPowerValue(dataParser.getDouble("pgen"), dataParser.getDouble("qgen"), ApparentPowerUnitType.MVA));
	    contriGen.setPLimit(BaseDataSetter.createActivePowerLimit(dataParser.getDouble("pmax"), dataParser.getDouble("pmin"), ActivePowerUnitType.MW));
	    contriGen.setQLimit(BaseDataSetter.createReactivePowerLimit(dataParser.getDouble("qmax"), dataParser.getDouble("qmin"), ReactivePowerUnitType.MVAR));
		
	    double rcomp = dataParser.getDouble("rcomp");
	    double xcomp = dataParser.getDouble("xcomp");
		if (rcomp != 0.0 || xcomp != 0.0)
			contriGen.setSourceZ(BaseDataSetter.createZValue(rcomp, xcomp, ZUnitType.PU));

		double zgenr = dataParser.getDouble("zgenr");
	    double zgenx = dataParser.getDouble("zgenx");
		if (zgenr != 0.0 || zgenx != 0.0)
			contriGen.setXfrZ(BaseDataSetter.createZValue(zgenr, zgenx, ZUnitType.PU));
	}	
}
