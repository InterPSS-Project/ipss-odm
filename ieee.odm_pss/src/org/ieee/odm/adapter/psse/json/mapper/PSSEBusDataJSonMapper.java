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
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.VoltageUnitType;

public class PSSEBusDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEBusDataJSonMapper(List<String> fieldDef) {
		super(fieldDef);
	}
	
	public void map(List<Object> data, BaseAclfModelParser<? extends NetworkXmlType> parser) {
		dataParser.loadFields(data.toArray());
		
		/*
 		"fields":["ibus", "name", "baskv", "ide", "area", "zone", "owner", "vm", "va", "nvhi", 	
 					"nvlo", "evhi", "evlo"], 
        "data":  [1, "BUS-1", 16.50000, 3, 1, 1, 1, 1.040000, 0.000000, 1.100000, 0.9000000, 
        			1.100000, 0.9000000], 
		 */		
		try {
			int ibus = (int)this.dataParser.getDouble("ibus");
	
			String iStr = IODMModelParser.BusIdPreFix+ibus;
			LoadflowBusXmlType aclfBusXml;
			try {
				aclfBusXml = (LoadflowBusXmlType) parser.createBus(iStr, ibus);
			} catch (Exception e) {
				ODMLogger.getLogger().severe(e.toString());
				return;
			}
			aclfBusXml.setNumber((long)ibus);
			
			aclfBusXml.setAreaNumber(dataParser.getInt("AREA", 0));
			aclfBusXml.setZoneNumber(dataParser.getInt("ZONE", 0));
			if (dataParser.exist("OWNER")) {
				BaseJaxbHelper.addOwner(aclfBusXml, dataParser.getString("owner"));
			}
			
			aclfBusXml.setName(dataParser.getString("name"));
			aclfBusXml.setBaseVoltage(BaseDataSetter.createVoltageValue(dataParser.getDouble("baskv"), VoltageUnitType.KV));
			
			double vm = dataParser.getDouble("vm", 1.0);
			double va = dataParser.getDouble("va", 0.0);
			aclfBusXml.setVoltage(BaseDataSetter.createVoltageValue(vm, VoltageUnitType.PU));
			aclfBusXml.setAngle(BaseDataSetter.createAngleValue(va, AngleUnitType.DEG));

			/*
			double gl = dataParser.getDouble("GL", 0.0);
			double bl = dataParser.getDouble("BL", 0.0);
	    	if (gl != 0.0 || bl != 0.0) {
	    		double factor = parser.getNet().getBasePower().getValue();  
	    		// for transfer G+jB to PU on system base, gl, bl are entered in MW at one per unit voltage
	    		// bl is reactive power consumed, - for capactor
	    		aclfBusXml.getShuntYData().setEquivY(BaseDataSetter.createYValue(gl/factor, bl/factor, YUnitType.PU));
	    	}
	    	*/
	    	
	    	/*
			Bus type code:
				1 - load bus (no generator boundary condition)
				2 - generator or plant bus (either voltage regulating or fixed Mvar)
				3 - swing bus
				4 - disconnected (isolated) bus
				IDE = 1 by default.
	    	 */
	    	int ide = dataParser.getInt("ide", 1);
	    	// set input data to the bus object
			LFGenCodeEnumType genType = ide == 3? LFGenCodeEnumType.SWING : 
									( ide == 1? LFGenCodeEnumType.NONE_GEN :     
										( ide == 2 ? LFGenCodeEnumType.PV : 
											ide==4?LFGenCodeEnumType.OFF: LFGenCodeEnumType.NONE_GEN ));
			AclfDataSetter.setGenData(aclfBusXml, genType);

			if (ide == 1 || ide == 2 || ide == 3) 
				aclfBusXml.setOffLine(false);
			else
				aclfBusXml.setOffLine(true);
			
		} catch (ODMException e) {
			ODMLogger.getLogger().severe(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}
