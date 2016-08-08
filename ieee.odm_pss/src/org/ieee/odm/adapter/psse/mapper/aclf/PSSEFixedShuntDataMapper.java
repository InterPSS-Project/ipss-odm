 /*
  * @(#)PSSELoadDataMapper.java   
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

package org.ieee.odm.adapter.psse.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEFixedShuntDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowShuntYDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.YUnitType;

public class PSSEFixedShuntDataMapper extends BasePSSEDataMapper{
	
	public PSSEFixedShuntDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEFixedShuntDataParser(ver);
	}
	
	/*
			  "I",      "ID",     "STATUS",    "GL",     "BL"
	 */	
	public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		//procLineString(lineStr, version);
		this.dataParser.parseFields(lineStr);

		int i = dataParser.getInt("I");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    BusXmlType busRecXml = parser.getBus(busId);
	    if (busRecXml == null){
	    	ODMLogger.getLogger().severe("Bus "+ busId+ " not found in the network");
	    	return;
	    }
		
	    LoadflowShuntYDataXmlType contribShutY; 
    	contribShutY = AclfParserHelper.createContriShuntY((LoadflowBusXmlType)busRecXml); 

	    String id = dataParser.getString("ID");
	    contribShutY.setId(id);
	    contribShutY.setName("ShuntY:" + id + "(" + i + ")");
	    contribShutY.setDesc("PSSE ShuntY " + id + " at Bus " + i);
	    
	    int status = dataParser.getInt("STATUS");
	    contribShutY.setOffLine(status!=1);

	    double g = dataParser.getDouble("GL", 0.0);
	    double b = dataParser.getDouble("BL", 0.0);
		if (g != 0.0 || b != 0.0)
			contribShutY.setY(BaseDataSetter.createYValue(g, b, YUnitType.MVAR));
	}
}
