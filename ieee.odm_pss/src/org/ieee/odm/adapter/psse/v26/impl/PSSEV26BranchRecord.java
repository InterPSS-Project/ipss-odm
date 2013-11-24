/*
 * @(#)PSSEBranchRecord.java   
 *
 * Copyright (C) 2006-2008 www.interpss.org
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
 * @Author Stephen Hau, Mike Zhou
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.adapter.psse.v26.impl;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSELineDataParser;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEXfrAdjustDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TapAdjustBusLocationEnumType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.YXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEV26BranchRecord {
	PSSELineDataParser branchDataParser = new PSSELineDataParser(PsseVersion.PSSE_26);
	PSSEXfrAdjustDataParser xfrAdjDataParser = new PSSEXfrAdjustDataParser(PsseVersion.PSSE_26);
	
	public void processBranchData(final String str, final AclfModelParser parser) throws ODMException {
		/*
		I,    J,    CKT, R,      X,        B,     RATEA,RATEB,RATEC,RATIO,ANGLE,GI,BI,GJ,BJ,ST  LEN,O1,F1,...,O4,F4
		
		31962,32156,' 1',0,      0.444445, 0,     30,   30,   0,    1,    0,    0, 0, 0, 0, 1,  0,  1, 1, 0,0,0,0,0,0, [Transformer_798]

		I - From bus number
		J - To bus number
		CKT - Circuit identifier (two character) not clear if integer or alpha
		R - Resistance, per unit
		X - Reactance, per unit
		B - Total line charging, per unit
		RATEA, RATEB, RATEC - Higher MVA ratings
		RATIO - Transformer off nominal turns ratio
		ANGLE - Transformer phase shift angle
		 */
		// parse the input data line	
		//final String[] strAry = getBranchDataFields(str);	
		branchDataParser.parseFields(str);
		
		final String fid = AbstractModelParser.BusIdPreFix+branchDataParser.getString("I");
		final String tid = AbstractModelParser.BusIdPreFix+branchDataParser.getString("J");
		final String cirId = ODMModelStringUtil.formatCircuitId(branchDataParser.getString("CKT"));
		ODMLogger.getLogger().fine("Branch data loaded, from-id, to-id: " + fid + ", " + tid);
		
        //      Branch resistance R, per unit  *
		//      Branch reactance X, per unit  * No zero impedance lines
		//    	Line charging B, per unit  * (total line charging, +B), Xfr B is negative
		final double rpu = branchDataParser.getDouble("R", 0.0);
		final double xpu = branchDataParser.getDouble("X", 0.0);
		final double bpu = branchDataParser.getDouble("B", 0.0);
		
		final double ratio = branchDataParser.getDouble("RATIO", 0.0);
		final double angle = branchDataParser.getDouble("ANGLE", 0.0);;

		final double fromTap = ratio, toTap = 1.0;
		final double fromAng = angle, toAng = 0.0;
		
		//From side shuntY
		final double GI= branchDataParser.getDouble("GI", 0.0);
		final double BI= branchDataParser.getDouble("BI", 0.0);

	    //To side shuntY
		final double GJ= branchDataParser.getDouble("GJ", 0.0);
		final double BJ= branchDataParser.getDouble("BJ", 0.0);
		
		BranchXmlType branchRec;
		try {
			if (ratio == 0.0) {
				branchRec = parser.createLineBranch(fid, tid, cirId);
				LineBranchXmlType branchData = (LineBranchXmlType)branchRec;
				AclfDataSetter.setLineData(branchData, rpu, xpu, ZUnitType.PU, 0.0, bpu, YUnitType.PU);
				//From side shuntY
		        if(GI!=0.0 || BI!=0.0 )  {
		        	YXmlType y = BaseDataSetter.createYValue(GI, BI, YUnitType.PU);
	        		branchData.setFromShuntY(y);
		        }

			    //To side shuntY
			    if(GJ!=0.0 || BJ!=0.0)  {
		        	YXmlType y = BaseDataSetter.createYValue(GJ, BJ, YUnitType.PU);
	        		branchData.setToShuntY(y);
			    }
			}
			else if (angle == 0.0) {
				branchRec = parser.createXfrBranch(fid, tid, cirId);
				XfrBranchXmlType branchData = (XfrBranchXmlType)branchRec;
				AclfDataSetter.createXformerData(branchData,
					       rpu, xpu, ZUnitType.PU, fromTap, toTap);		
				//From side shuntY
		        if(GI!=0.0 || BI!=0.0 )  {
		        	YXmlType y = BaseDataSetter.createYValue(GI, BI, YUnitType.PU);
	        		branchData.setMagnitizingY(y);
		        }
			}
			else {
				branchRec = parser.createPSXfrBranch(fid, tid, cirId);
				PSXfrBranchXmlType branchData = (PSXfrBranchXmlType)branchRec;
				AclfDataSetter.createPhaseShiftXfrData(branchData, rpu, xpu, 
						ZUnitType.PU, fromTap, toTap, fromAng, toAng, AngleUnitType.DEG);			
				//From side shuntY
		        if(GI!=0.0 || BI!=0.0 )  {
		        	YXmlType y = BaseDataSetter.createYValue(GI, BI, YUnitType.PU);
		        	branchData.setMagnitizingY(y);
		        }
			}
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			return;
		}		
		
		int status = branchDataParser.getInt("ST", 0);
		branchRec.setOffLine(status == 0);
		
		final double rating1Mvar = branchDataParser.getDouble("RATEA", 0.0);
		final double rating2Mvar = branchDataParser.getDouble("RATEB", 0.0);
		final double rating3Mvar = branchDataParser.getDouble("RATEC", 0.0);
		
		branchRec.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		AclfDataSetter.setBranchRatingLimitData(branchRec.getRatingLimit(),
				rating1Mvar, rating2Mvar, rating3Mvar,
				ApparentPowerUnitType.MVA, 0.0,
				null);
	}
   
	public void processXformerAdjData(final String str, final AclfModelParser parser) throws ODMException {
		/*
		I,    J,     CKT,ICONT,     RMA,       RMI,       VMA,       VMI,   STEP,   TABLE
    	31212,31435,' 1',     0,    1.5000,    0.5100,    1.5000,    0.5100,0.00625,0,0, 0.000, 0.000,   

		I - From bus number
		J - To bus number
		CKT - Circuit number
		ICONT - Number of bus to control. If different from I or J, sign of ICONT
 			determines control. Positive sign, close to impedance (untapped) bus
 			of transformer. Negative sign, opposite.
		RMA - Upper limit of turns ratio or phase shift
		RMI - Lower limit of turns ratio or phase shift
		VMA - Upper limit of controlled volts, MW or MVAR
		VMI - Lower limit of controlled volts, MW or MVAR
		STEP - Turns ratio step increment
		TABLE - Zero, or number of a transformer impedance correction table 1-5
	 */
		//final String[] strAry = getXfrAdjDataFields(str);		
		xfrAdjDataParser.parseFields(str);
		
		final String fid = AbstractModelParser.BusIdPreFix+xfrAdjDataParser.getString("I");
		final String tid = AbstractModelParser.BusIdPreFix+xfrAdjDataParser.getString("J");
		final String cirId = ODMModelStringUtil.formatCircuitId(xfrAdjDataParser.getString("CKT"));
		ODMLogger.getLogger().fine("Branch data loaded, from-id, to-id: " + fid + ", " + tid);
		
		BranchXmlType branchRec = (BranchXmlType)parser.getBranch(fid, tid, cirId);
	    if (branchRec == null){
			String branchId = ODMModelStringUtil.formBranchId(fid, tid, cirId);
			ODMLogger.getLogger().severe("Branch "+ branchId + " not found in the network");
	    	return;
	    }	

	    // only one branch section
	    
	    int icon = xfrAdjDataParser.getInt("ICONT", 0);
	    boolean isNegative = false;
	    if (icon < 0) {
	    	isNegative = true;
	    	icon = - icon;
	    }
		final String iconId = icon > 0? AbstractModelParser.BusIdPreFix+icon : null;

		if (branchRec instanceof XfrBranchXmlType) {
			XfrBranchXmlType branchData = (XfrBranchXmlType)branchRec;
	    	double tmax = xfrAdjDataParser.getDouble("RMA", 0.0);
	    	double tmin = xfrAdjDataParser.getDouble("RMI", 0.0);
	    	double tstep = xfrAdjDataParser.getDouble("STEP", 0.0);
	    	double vup = xfrAdjDataParser.getDouble("VMA", 0.0);
	    	double vlow = xfrAdjDataParser.getDouble("VMI", 0.0);
	    	
	    	TapAdjustmentXmlType tapAdj = OdmObjFactory.createTapAdjustmentXmlType(); 
	    	branchData.setTapAdjustment(tapAdj);
	    	tapAdj.setAdjustmentType(TapAdjustmentEnumType.VOLTAGE);
	    	tapAdj.setTapLimit(BaseDataSetter.createTapLimit(tmax, tmin));
	    	tapAdj.setTapAdjStepSize(tstep);
	    	tapAdj.setTapAdjOnFromSide(true);

	    	VoltageAdjustmentDataXmlType vAdjData = OdmObjFactory.createVoltageAdjustmentDataXmlType(); 
	    	tapAdj.setVoltageAdjData(vAdjData);
	    	vAdjData.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
	    	vAdjData.setRange(OdmObjFactory.createLimitXmlType());
	    	vAdjData.getRange().setMax(vup);
	    	vAdjData.getRange().setMin(vlow);
	    	
	    	if (iconId != null) {
		    	tapAdj.setOffLine(false);
	    		if (iconId.equals(fid))
	    			vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.FROM_BUS);
	    		else if (iconId.equals(tid))
	    			vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.TO_BUS);
	    		else {
		    		vAdjData.setAdjVoltageBus(parser.createBusRef(iconId));
		    		if (isNegative)
		    			vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.NEAR_TO_BUS);
		    		else
		    			vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.NEAR_FROM_BUS);
	    		}
	    	}
	    	else
		    	tapAdj.setOffLine(true);
	    }
	    else if (branchRec instanceof PSXfrBranchXmlType) {
			PSXfrBranchXmlType branchData = (PSXfrBranchXmlType)branchRec;
	    	double angmax = xfrAdjDataParser.getDouble("RMA", 0.0);
	    	double angmin = xfrAdjDataParser.getDouble("RMI", 0.0);
	    	double mwup = xfrAdjDataParser.getDouble("VMA", 0.0);
	    	double mwlow = xfrAdjDataParser.getDouble("VMI", 0.0);

	    	AngleAdjustmentXmlType angAdj = OdmObjFactory.createAngleAdjustmentXmlType(); 
	    	branchData.setAngleAdjustment(angAdj);
	    	angAdj.setAngleLimit(BaseDataSetter.createAngleLimit(angmax, angmin, AngleUnitType.DEG));
	    	angAdj.setRange(OdmObjFactory.createLimitXmlType());
	    	angAdj.getRange().setMax(mwup);
	    	angAdj.getRange().setMin(mwlow);
	    	angAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
	    	angAdj.setDesiredMeasuredOnFromSide(true);
	    }

	}
}
