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
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.FrequencyUnitType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSECaseDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSECaseDataJSonMapper(List<String> fieldDef) {
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
		
		/*
            "fields":["ic", "sbase", "rev", "xfrrat", "nxfrat", "basfrq", "title1", "title2"], 
            "data":[0, 100.0000, 36, 0, 1, 50.00000, "00/00/01                      100.0  0    0", "0"]
		 */		
		try {
			double baseMva = dataParser.getDouble("sbase");
			baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
			
			BaseJaxbHelper.addNVPair(baseCaseNet, "CaseIndicator", new Integer(dataParser.getInt("ic")).toString());
			double rev = dataParser.getDouble("rev");
			
			baseCaseNet.setDesc(dataParser.getString("title1") + ", " + dataParser.getString("title2"));
			
			baseCaseNet.setName("AclfNet-PSSE-JSon-rev" + rev);
			
			double baseFreq = dataParser.getDouble("basefrq",50.0);
			baseCaseNet.setFrequency(BaseDataSetter.createFrequency(baseFreq, FrequencyUnitType.HZ));		
		} catch (ODMException e) {
			throw new RuntimeException(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	}
}