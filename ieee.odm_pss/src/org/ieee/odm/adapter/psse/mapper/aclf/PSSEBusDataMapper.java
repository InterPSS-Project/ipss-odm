 /*
  * @(#)PSSEBusDataMapper.java   
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
import org.ieee.odm.adapter.psse.parser.aclf.PSSEBusDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;

public class PSSEBusDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{

	public PSSEBusDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEBusDataParser(ver);
	}
	
	public void procLineString(String lineStr, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		//System.out.println(lineStr + "\n" + dataParser.toString());
		
		int i = dataParser.getInt("I");
		String iStr = AbstractModelParser.BusIdPreFix+i;
		LoadflowBusXmlType aclfBusXml;
		try {
			aclfBusXml = (LoadflowBusXmlType) parser.createBus(iStr, i);
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			return;
		}
		aclfBusXml.setNumber((long)i);
		
		aclfBusXml.setAreaNumber(dataParser.getInt("AREA", 0));
		aclfBusXml.setZoneNumber(dataParser.getInt("ZONE", 0));
		if (dataParser.exist("OWNER")) {
			BaseJaxbHelper.addOwner(aclfBusXml, dataParser.getString("OWNER"));
		}
		
		aclfBusXml.setName(dataParser.getString("NAME"));
		aclfBusXml.setBaseVoltage(BaseDataSetter.createVoltageValue(dataParser.getDouble("BASKV"), VoltageUnitType.KV));
		
		double vm = dataParser.getDouble("VM", 1.0);
		double va = dataParser.getDouble("VA", 0.0);
		aclfBusXml.setVoltage(BaseDataSetter.createVoltageValue(vm, VoltageUnitType.PU));
		aclfBusXml.setAngle(BaseDataSetter.createAngleValue(va, AngleUnitType.DEG));

		double gl = dataParser.getDouble("GL", 0.0);
		double bl = dataParser.getDouble("BL", 0.0);
    	if (gl != 0.0 || bl != 0.0) {
    		double factor = parser.getNet().getBasePower().getValue();  
    		// for transfer G+jB to PU on system base, gl, bl are entered in MW at one per unit voltage
    		// bl is reactive power consumed, - for capactor
    		aclfBusXml.getShuntYData().setEquivY(BaseDataSetter.createYValue(gl/factor, bl/factor, YUnitType.PU));
    	}
    	
    	/*
		Bus type code:
			1 - load bus (no generator boundary condition)
			2 - generator or plant bus (either voltage regulating or fixed Mvar)
			3 - swing bus
			4 - disconnected (isolated) bus
			IDE = 1 by default.
    	 */
    	int ide = dataParser.getInt("IDE", 1);
    	// set input data to the bus object
		LFGenCodeEnumType genType = ide == 3? LFGenCodeEnumType.SWING : 
								( ide == 1? LFGenCodeEnumType.NONE_GEN :     
									( ide == 2 ? LFGenCodeEnumType.PV : LFGenCodeEnumType.NONE_GEN ));
		AclfDataSetter.setGenData(aclfBusXml, genType, vm, VoltageUnitType.PU, va, AngleUnitType.DEG, 
						0.0, 0.0,	ApparentPowerUnitType.MVA);

		if (ide == 1 || ide == 2 || ide == 3) 
			aclfBusXml.setOffLine(false);
		else
			aclfBusXml.setOffLine(true);
	}
}
