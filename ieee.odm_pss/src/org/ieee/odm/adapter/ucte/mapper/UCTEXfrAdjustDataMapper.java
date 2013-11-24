/*
 * @(#)UCTEXfrAdjustDataMapper.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.ucte.UCTE_DEFAdapter;
import org.ieee.odm.adapter.ucte.parser.UCTEXfrAdjustDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.base.ModelContansts;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.FactorUnitType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TapAdjustBusLocationEnumType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;

public class UCTEXfrAdjustDataMapper extends BaseUCTEDataMapper {

	public UCTEXfrAdjustDataMapper() {
		this.dataParser = new UCTEXfrAdjustDataParser();
	}

	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		this.dataParser.parseFields(str);
		
		String fromNodeId = this.dataParser.getString("fromNodeId"), 
		       toNodeId = this.dataParser.getString("toNodeId"), 
		       orderCode = this.dataParser.getString("orderCode"), 
		       type = this.dataParser.getString("type", "SYMM");

		double dUPhase = this.dataParser.getDouble("dUPhase", 0.0), 
		       uKvPhase = this.dataParser.getDouble("uKvPhase", 0.0); 
		int    nPhase = this.dataParser.getInt("nPhase", 0), 
		       n1Phase = this.dataParser.getInt("n1Phase", 0);  

		double dUAngle = this.dataParser.getDouble("dUAngle", 0.0), 
		       thetaDegAngle = this.dataParser.getDouble("thetaDegAngle", 0.0), 
		       pMwAngle = this.dataParser.getDouble("pMwAngle", 0.0);  
		int    nAngle = this.dataParser.getInt("nAngle", 0), 
		       n1Angle = this.dataParser.getInt("n1Angle", 0); 
		
		if (dUPhase > 0.0 && dUAngle > 0.0) {
			throw new ODMException("Error: both phase regulation and angle regulation data are presented");
		}

		XfrBranchXmlType xfrBranch = parser.getXfrBranch(fromNodeId, toNodeId, orderCode); 
      	if (xfrBranch == null) {
      		throw new ODMException("Error: branch cannot be found, line: " + str);
      	}

      	if (dUPhase > 0.0) {
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "dUPhase", new Double(dUPhase).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "nPhase", new Double(nPhase).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "n1Phase", new Double(n1Phase).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "uKvPhase", new Double(uKvPhase).toString());

			double ratioFactor = xfrBranch.getToTurnRatio().getValue();

			double x = 1.0 / (1.0 + n1Phase*dUPhase*0.01);
			// UCTE model at to side x : 1.0, InterPSS model 1.0:turnRatio
			xfrBranch.setToTurnRatio(BaseDataSetter.createTurnRatioPU(ratioFactor/x));
			
			if (uKvPhase > 0.0) {
				TapAdjustmentXmlType tapAdj = OdmObjFactory.createTapAdjustmentXmlType();
				xfrBranch.setTapAdjustment(tapAdj);
				tapAdj.setAdjustmentType(TapAdjustmentEnumType.VOLTAGE);
				
				// tap control of voltage at to node side
				//     2 - Variable tap for voltage control (TCUL, LTC)
          		double maxTap = ratioFactor*(nPhase*dUPhase), 
          		       minTap = ratioFactor*(-nPhase*dUPhase);

          		tapAdj.setTapLimit(BaseDataSetter.createTapLimit(maxTap, minTap));
				tapAdj.getTapLimit().setUnit(FactorUnitType.PERCENT);
          		tapAdj.setTapAdjStepSize(dUPhase);
          		tapAdj.setTapAdjOnFromSide(false);
          		
          		VoltageAdjustmentDataXmlType vAdjData = OdmObjFactory.createVoltageAdjustmentDataXmlType();
          		tapAdj.setVoltageAdjData(vAdjData);
          		
          		vAdjData.setMode(AdjustmentModeEnumType.VALUE_ADJUSTMENT);
          		vAdjData.setDesiredValue(uKvPhase);				
          		vAdjData.setDesiredVoltageUnit(VoltageUnitType.KV);

          		vAdjData.setAdjVoltageBus(parser.createBusRef(toNodeId));
       			vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.TO_BUS);
			}
		}
		else if (dUAngle > 0.0) {
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "dUAngle", new Double(dUAngle).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "thetaDegAngle", new Double(thetaDegAngle).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "nAngle", new Double(nAngle).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "n1Angle", new Double(n1Angle).toString());
			if (dUPhase != 0.0)
				BaseJaxbHelper.addNVPair(xfrBranch, "pMwAngle", new Double(pMwAngle).toString());

			double ratioFactor = xfrBranch.getToTurnRatio().getValue();

	    	double ang = 0.0, angMax = 0.0, angMin = 0.0, x = 1.0;
			double a    = n1Angle*dUAngle*0.01,
				   aMax = nAngle*dUAngle*0.01,
			       aMin = -nAngle*dUAngle*0.01;
			if (type.equals(UCTE_DEFAdapter.PsXfrType_ASYM)) {
				if (thetaDegAngle == 90.0) {
					ang = Math.atan(a);
					angMax = Math.atan(aMax);
					angMin = Math.atan(aMin);
					x = 1.0 / Math.sqrt(1.0 + a*a);
				}
				else {
					double theta = thetaDegAngle * ModelContansts.Deg2Rad,
					       asin = a*Math.sin(theta),
					       acos = 1.0 + a*Math.cos(theta),
					       asinMax = aMax*Math.sin(theta),
					       acosMax = 1.0 + aMax*Math.cos(theta),
					       asinMin = aMin*Math.sin(theta),
					       acosMin = 1.0 + aMin*Math.cos(theta);
					ang = Math.atan(asin/acos);
					angMax = Math.atan(asinMax/acosMax);
					angMin = Math.atan(asinMin/acosMin);
					x = 1.0 / Math.sqrt(asin*asin + acos*acos);
				}
			}
			else {  // default type is SYMM
				ang = 2.0 * Math.atan(a/2.0);
				angMax = 2.0 * Math.atan(aMax/2.0);
				angMin = 2.0 * Math.atan(aMin/2.0);
			}
			
			PSXfrBranchXmlType psXfrBranch = (PSXfrBranchXmlType)ModelStringUtil.casting(
					xfrBranch, "XfrBranchXmlType", "PSXfrBranchXmlType", parser.getEncoding());
			
			psXfrBranch.setToAngle(BaseDataSetter.createAngleValue(-ang*ModelContansts.Rad2Deg, AngleUnitType.DEG));
			psXfrBranch.setToTurnRatio(BaseDataSetter.createTurnRatioPU(ratioFactor/x));
			
			if (pMwAngle != 0.0) {
				AngleAdjustmentXmlType angAdj = OdmObjFactory.createAngleAdjustmentXmlType();
				psXfrBranch.setAngleAdjustment(angAdj);
          		angAdj.setMode(AdjustmentModeEnumType.VALUE_ADJUSTMENT);
          		angAdj.setDesiredValue(pMwAngle);				
				angAdj.setDesiredActivePowerUnit(ActivePowerUnitType.MW);
				angAdj.setAngleLimit(BaseDataSetter.createAngleLimit(angMax, angMin, AngleUnitType.DEG));
				angAdj.setAngleAdjOnFromSide(false);
				// this part if not specified in the UCTE spec. We assume it is measured on to side
				angAdj.setDesiredMeasuredOnFromSide(false);
			}
		}
	}
}