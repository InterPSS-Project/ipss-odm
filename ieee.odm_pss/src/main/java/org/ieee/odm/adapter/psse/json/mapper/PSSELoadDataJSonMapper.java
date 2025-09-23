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

import java.util.List;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;

public class PSSELoadDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSELoadDataJSonMapper(List<String> fieldDef) {
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
		
		/*
           "fields":["ibus", "loadid", "stat", "area", "zone", "pl", "ql", "ip", "iq", "yp", "yq", 
           			"owner", "scale", "intrpt", "dgenp", "dgenq", "dgenm", "loadtype", "name"], 
            "data": [5, "1", 1, 1, 1, 125.0000, 50.00000, 0.000000, 0.000000, 0.000000, 0.000000, 
            		1, 1, 0, 0.000000, 0.000000, 0, "", ""], 
               
		 */		
		try {
			int ibus = dataParser.getInt("ibus");
		    final String busId = IODMModelParser.BusIdPreFix+ibus;
		    BusXmlType busRecXml = odmParser.getBus(busId);
		    if (busRecXml == null){
		    	throw new RuntimeException("Bus "+ busId+ " not found in the network");
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

		    String id = dataParser.getString("loadid");
		    contribLoad.setId(id);
		    contribLoad.setName("Load:" + id + "(" + ibus + ")");
		    contribLoad.setDesc("PSSE Load " + id + " at Bus " + ibus);
		    
		    int status = dataParser.getInt("stat");
		    contribLoad.setOffLine(status!=1);

		    contribLoad.setAreaNumber(dataParser.getInt("area", 1));
		    contribLoad.setZoneNumber(dataParser.getInt("zone", 1));
		    int owner = dataParser.getInt("owner");
		    BaseJaxbHelper.addOwner(contribLoad, new Integer(owner).toString());
			
		    double pl = dataParser.getDouble("pl", 0.0);
		    double ql = dataParser.getDouble("ql", 0.0);
			if (pl != 0.0 || ql != 0.0)
				contribLoad.setConstPLoad(BaseDataSetter.createPowerValue(pl, ql, ApparentPowerUnitType.MVA));

			double ip = dataParser.getDouble("ip", 0.0);
		    double iq = dataParser.getDouble("iq", 0.0);
			if (ip != 0.0 || iq != 0.0)
				contribLoad.setConstILoad(BaseDataSetter.createPowerValue(ip, iq, ApparentPowerUnitType.MVA));

			double yp = dataParser.getDouble("yp", 0.0);
		    double yq = dataParser.getDouble("yq", 0.0);
		    
		    //TODO  Note YQ is negative for an inductive load in PSS/E. However, as a general convention, inductive load is positive
			if (yp != 0.0 || yq != 0.0)
				contribLoad.setConstZLoad(BaseDataSetter.createPowerValue(yp, -yq, ApparentPowerUnitType.MVA));
		} catch (ODMException e) {
			throw new RuntimeException(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}