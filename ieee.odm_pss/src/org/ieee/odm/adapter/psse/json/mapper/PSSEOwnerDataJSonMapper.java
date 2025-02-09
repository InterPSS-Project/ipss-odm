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

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OwnerXmlType;

public class PSSEOwnerDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEOwnerDataJSonMapper(List<String> fieldDef) {
		super(fieldDef);
	}
	
	public void proc(List<Object> data, BaseAclfModelParser<? extends NetworkXmlType> parser) {
		dataParser.loadFields(data.toArray());
		
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		OwnerXmlType owner = OdmObjFactory.createOwnerXmlType();
		baseCaseNet.getOwnerList().add(owner);
		
		/*
            "fields":["iowner", "owname"], 
            "data": [1, "OWNER 1"], 
		 */		
		try {
			int i = this.dataParser.getInt("iowner");
			String name = this.dataParser.getString("owname");
			owner.setId(new Integer(i).toString());
			owner.setNumber(i);
			owner.setName(name);		
		} catch (ODMException e) {
			ODMLogger.getLogger().severe(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}
