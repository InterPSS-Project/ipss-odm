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
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;

public class PSSEFixedShuntDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEFixedShuntDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEFixedShuntDataParser(ver);
	}
	
	/*
			  "I",      "ID",     "STATUS",    "GL",     "BL"
	 */	
	public void procLineString(String lineStr, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		//procLineString(lineStr, version);
		this.dataParser.parseFields(lineStr);

		int i = dataParser.getInt("I");
	    final String busId = AbstractModelParser.BusIdPreFix+i;
	    BusXmlType busRecXml = parser.getBus(busId);
	    if (busRecXml == null){
	    	ODMLogger.getLogger().severe("Bus "+ busId+ " not found in the network");
	    	return;
	    }
		
	    LoadflowLoadDataXmlType contribLoad; 
	    if (busRecXml instanceof DStabBusXmlType) {
	    	contribLoad = DStabParserHelper.createDStabContriLoad((DStabBusXmlType)busRecXml);
	    }
	    else if (busRecXml instanceof ShortCircuitBusXmlType) {
	    	contribLoad = AcscParserHelper.createAcscContributeLoad((ShortCircuitBusXmlType)busRecXml);
	    } 
	    else {
	    	contribLoad = AclfParserHelper.createContriLoad((LoadflowBusXmlType)busRecXml); 
	    }	    

	    String id = dataParser.getString("ID");
	    contribLoad.setId(id);
	    contribLoad.setName("Load:" + id + "(" + i + ")");
	    contribLoad.setDesc("PSSE Load " + id + " at Bus " + i);
	    
	    int status = dataParser.getInt("STATUS");
	    contribLoad.setOffLine(status!=1);

	    contribLoad.setAreaNumber(dataParser.getInt("AREA", 1));
	    contribLoad.setZoneNumber(dataParser.getInt("ZONE", 1));
	    BaseJaxbHelper.addOwner(contribLoad, dataParser.getString("OWNER"));
		
	    double pl = dataParser.getDouble("PL", 0.0);
	    double ql = dataParser.getDouble("QL", 0.0);
		if (pl != 0.0 || ql != 0.0)
			contribLoad.setConstPLoad(BaseDataSetter.createPowerValue(pl, ql, ApparentPowerUnitType.MVA));

		double ip = dataParser.getDouble("IP", 0.0);
	    double iq = dataParser.getDouble("IQ", 0.0);
		if (ip != 0.0 || iq != 0.0)
			contribLoad.setConstILoad(BaseDataSetter.createPowerValue(ip, iq, ApparentPowerUnitType.MVA));

		double yp = dataParser.getDouble("YP", 0.0);
	    double yq = dataParser.getDouble("YQ", 0.0);
		if (yp != 0.0 || yq != 0.0)
			contribLoad.setConstZLoad(BaseDataSetter.createPowerValue(yp, yq, ApparentPowerUnitType.MVA));
	}
}
