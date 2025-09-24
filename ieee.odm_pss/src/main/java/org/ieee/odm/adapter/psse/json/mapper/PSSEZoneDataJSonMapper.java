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
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSEZoneDataJSonMapper extends BasePSSEDataJSonMapper{
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSEZoneDataJSonMapper.class);
	
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEZoneDataJSonMapper(List<String> fieldDef) {
		super(fieldDef);
	}
	
	/**
	 * map the data list into the DOM model parser 
	 * 
	 * @param data
	 * @param odmParser
	 */
	public void map(List<Object> data, BaseAclfModelParser<? extends NetworkXmlType> odmParser) {
		dataParser.loadFields(data.toArray());
		
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) odmParser.getNet();
		if (baseCaseNet.getLossZoneList() == null)
			baseCaseNet.setLossZoneList(OdmObjFactory.createNetworkXmlTypeLossZoneList());
		NetZoneXmlType zone = OdmObjFactory.createNetZoneXmlType(); 
		baseCaseNet.getLossZoneList().getLossZone().add(zone);
		
		/*
           "fields":["izone", "zoname"], 
            "data": [1, "NORTH_A1"] 
		 */		
		try {
			int	i = this.dataParser.getInt("izone");
			String name = this.dataParser.getString("zoname");

			zone.setId(new Integer(i).toString());
			zone.setNumber(i);
			zone.setName(name);			
		} catch (ODMException e) {
			log.error(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}
