 /*
  * @(#)PSSEZoneDataMapper.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEZoneDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEZoneDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEZoneDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEZoneDataParser(ver);
	}

	public void procLineString(String lineStr, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		/*
		 * Format: I, ’ZONAME’
		 */
		int	i = this.dataParser.getInt("I");
		String name = this.dataParser.getString("ZONAME");

		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		if (baseCaseNet.getLossZoneList() == null)
			baseCaseNet.setLossZoneList(OdmObjFactory.createNetworkXmlTypeLossZoneList());
		NetZoneXmlType zone = OdmObjFactory.createNetZoneXmlType(); 
		baseCaseNet.getLossZoneList().getLossZone().add(zone);
		zone.setId(new Integer(i).toString());
		zone.setNumber(i);
		zone.setName(name);		
	}
}
