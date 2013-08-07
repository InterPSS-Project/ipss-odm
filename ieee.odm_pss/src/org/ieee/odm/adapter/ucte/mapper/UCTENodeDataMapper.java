/*
 * @(#)UCTENodeDataMapper.java   
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.ucte.mapper;

import org.ieee.odm.adapter.ucte.parser.UCTENodeDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;

public class UCTENodeDataMapper extends BaseUCTEDataMapper {
	private String isoId;
	public void setIsoId(String id) { this.isoId = id; }
	
	private int busCnt;
	public void setBusCnt(int cnt) { this.busCnt = cnt; }
	
	public UCTENodeDataMapper() {
		this.dataParser = new UCTENodeDataParser();
	}

	public UCTENodeDataParser getDataParser() { return (UCTENodeDataParser)this.dataParser; }
	
	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		this.dataParser.parseFields(str);
		
		// parse the input line for node information
		String id = this.dataParser.getString("id"), 
		       name = this.dataParser.getString("name");
		double baseKv = this.dataParser.getDouble("baseKv", 0.0);
		int    status = this.dataParser.getInt("status", 0), 
		       nodeType = this.dataParser.getInt("nodeType");
		double voltage = this.dataParser.getDouble("voltage"), 
		       pLoadMW = this.dataParser.getDouble("pLoadMW"), 
		       qLoadMvar = this.dataParser.getDouble("qLoadMvar"), 
		       pGenMW = -this.dataParser.getDouble("pGenMW"), 
		       qGenMvar = -this.dataParser.getDouble("qGenMvar");
		double minGenMW = -this.dataParser.getDouble("minGenMW", 0.0), 
		       maxGenMW = -this.dataParser.getDouble("maxGenMW", 0.0), 
		       minGenMVar = -this.dataParser.getDouble("minGenMvar", 0.0), 
		       maxGenMVar = -this.dataParser.getDouble("maxGenMvar", 0.0), 
			   staticPrimaryControl = this.dataParser.getDouble("staticPrimaryControl", 0.0), 
			   normalPowerPrimaryControl = this.dataParser.getDouble("normalPowerPrimaryControl", 0.0),
			   scMVA3P = this.dataParser.getDouble("scMVA3P", 0.0), 
			   x_rRatio = this.dataParser.getDouble("x_rRatio", 0.0);
		String powerPlanType = this.dataParser.getString("powerPlanType", "");
		
		// create a bus record
		LoadflowBusXmlType aclfBus = parser.createBus(id); 
      	aclfBus.setId(id);
      	aclfBus.setNumber((long)busCnt);
      	if (name != null && !name.trim().equals(""))
      		aclfBus.setName(name);
      	if (isoId != null && !isoId.trim().equals(""))
      		aclfBus.setIsoCode(isoId);
      	aclfBus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv, VoltageUnitType.KV));
		
      	aclfBus.setVoltage(BaseDataSetter.createVoltageValue(voltage, VoltageUnitType.KV));

      	aclfBus.setAngle(BaseDataSetter.createAngleValue(0.0, AngleUnitType.DEG));    	
    	
		if (pLoadMW != 0.0 || qLoadMvar != 0.0) {
			AclfDataSetter.setLoadData(aclfBus,
					LFLoadCodeEnumType.CONST_P, pLoadMW,
					qLoadMvar, ApparentPowerUnitType.MVA);
		}

		switch (nodeType) {
		case 0: // PQ bus
			if (pGenMW != 0.0 || qGenMvar != 0.0) {
				AclfDataSetter.setGenData(aclfBus,
						LFGenCodeEnumType.PQ,
						1.0, VoltageUnitType.PU, 0.0, AngleUnitType.DEG,
						pGenMW, qGenMvar, ApparentPowerUnitType.MVA);				
			}
			break;
		case 1: // Q angle bus
			throw new ODMException("Node type = 1, not support currently. Please contact support@interpss.org");
		case 2: // PV bus
			AclfDataSetter.setGenData(aclfBus,
					LFGenCodeEnumType.PV, 
					voltage, VoltageUnitType.KV, 0.0, AngleUnitType.DEG,
					pGenMW, qGenMvar, ApparentPowerUnitType.MVA);
			if (((maxGenMVar != 0.0) || (minGenMVar != 0.0))
					&& maxGenMVar > minGenMVar) {
				// PV Bus limit control
				ODMLogger.getLogger().fine("Bus is a PVLimitBus, id: " + id);
				aclfBus.getGenData().getEquivGen().getValue().setQLimit(BaseDataSetter.createReactivePowerLimit(  
						maxGenMVar, minGenMVar, ReactivePowerUnitType.MVAR));
			}
			break;
		case 3: // swing bus
			AclfDataSetter.setGenData(aclfBus,
					LFGenCodeEnumType.SWING,
					voltage, VoltageUnitType.KV, 0.0, AngleUnitType.DEG,
					pGenMW, qGenMvar, ApparentPowerUnitType.MVA);
			break;
		default:
			// error bus nodeType code
			throw new ODMException("Wrong node type code, " + nodeType);
		}

		if (status != 0)
			BaseJaxbHelper.addNVPair(aclfBus, "Status", new Integer(status).toString());
		if (minGenMW != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "MinGenMW", new Double(minGenMW).toString());
		if (maxGenMW != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "MaxGenMW", new Double(maxGenMW).toString());
		if (staticPrimaryControl != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "SPControl", new Double(staticPrimaryControl).toString());
		if (normalPowerPrimaryControl != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "NPPControl", new Double(normalPowerPrimaryControl).toString());
		if (scMVA3P != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "SCMva3P", new Double(scMVA3P).toString());
		if (x_rRatio != 0.0)
			BaseJaxbHelper.addNVPair(aclfBus, "XRRatio", new Double(x_rRatio).toString());
		if (powerPlanType != null)
			BaseJaxbHelper.addNVPair(aclfBus, "PPlanType", powerPlanType);
	}
}