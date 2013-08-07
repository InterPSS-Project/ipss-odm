/*
 * @(#)IeeeCDFBranchDataMapper.java   
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

package org.ieee.odm.adapter.ieeecdf.mapper;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFBranchDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LimitXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.MvarFlowAdjustmentDataXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TapAdjustBusLocationEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class IeeeCDFBranchDataMapper extends BaseIeeeCDFDataMapper {
	
	public IeeeCDFBranchDataMapper() {
		this.dataParser = new IeeeCDFBranchDataParser();
	}
	 
	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		// parse the input data line
		//final String[] strAry = IeeeCDFDataParser.getBranchDataFields(str);
		dataParser.parseFields(str);

		//    	Columns  1- 4   Tap bus number [I] *
		//      	For transformers or phase shifters, the side of the model the non-unity tap is on.
		//		Columns  6- 9   Z bus number [I] *
		//      	For transformers and phase shifters, the side of the model the device impedance is on.
		//    	Columns 11-12   Load flow area [I]
		//    	Columns 13-15   Loss zone [I]
		//    	Column  17      Circuit [I] * (Use 1 for single lines)
		//    	Column  19      Type [I] *
		//      0 - Transmission line
		//      1 - Fixed tap
		//      2 - Variable tap for voltage control (TCUL, LTC)
		//      3 - Variable tap (turns ratio) for MVAR control
		//      4 - Variable phase angle for MW control (phase shifter)
		final String fid = AbstractModelParser.BusIdPreFix + dataParser.getString("FromNum");
		final String tid = AbstractModelParser.BusIdPreFix + dataParser.getString("ToNum");
		String cirId = dataParser.getString("CirId");
		if(cirId.equals(""))cirId="1";//if empty,set cirId to 1 by default
		int branchType = dataParser.getInt("Type", 0);
		//String branchId = ModelStringUtil.formBranchId(fid, tid, cirId);
		BranchXmlType branch = null;

		branch = branchType == 0?
			parser.createLineBranch(fid, tid, cirId) :
						((branchType == 1 || branchType == 2 || branchType == 3)?
								parser.createXfrBranch(fid, tid, cirId) : parser.createPSXfrBranch(fid, tid, cirId));

		/*
		getLogger().fine("Branch data loaded, from-id, to-id: " + fid + ", " + tid);
		try {
			branch.setFromBus(parser.createBusRef(fid));
			branch.setToBus(parser.createBusRef(tid));
		} catch (Exception e) {
			this.logErr("branch is not connected properly, " + e.toString());
		}
		*/
		branch.setAreaNumber(dataParser.getInt("Area", 0));
		branch.setZoneNumber(dataParser.getInt("Zone", 0));

		branch.setCircuitId(cirId);

		branch.setId(ModelStringUtil.formBranchId(fid, tid, cirId));

		//LoadflowBranchDataXmlType branchData = this.factory.createLoadflowBranchDataXmlType(); 
		//branch.getLoadflowData().add(branchData);


		//    	Columns 20-29   Branch resistance R, per unit [F] *
		//    	Columns 30-40   Branch reactance X, per unit [F] * No zero impedance lines
		//    	Columns 41-50   Line charging B, per unit [F] * (total line charging, +B), Xfr B is negative
		final double rpu = dataParser.getDouble("R");
		final double xpu = dataParser.getDouble("X");
		final double bpu = dataParser.getDouble("B");
		if (branchType == 0) {
			LineBranchXmlType line = (LineBranchXmlType)branch;
			AclfDataSetter.setLineData(line, rpu, xpu,
					ZUnitType.PU, 0.0, bpu, YUnitType.PU);
		}

		// assume ratio and angle are defined at to side
		//    	Columns 77-82   Transformer final turns ratio [F]
		//    	Columns 84-90   Transformer (phase shifter) final angle [F]
		final double ratio = dataParser.getDouble("TurnRatio");
		final double angle = dataParser.getDouble("ShiftAngle");
		if (branchType > 0) {
			if (angle == 0.0) {
				XfrBranchXmlType xfrBranch = (XfrBranchXmlType)branch;
				AclfDataSetter.createXformerData(xfrBranch,
						rpu, xpu, ZUnitType.PU, ratio, 1.0, 
						0.0, bpu, YUnitType.PU);
				BusXmlType fromBusRec = parser.getBus(fid);
				BusXmlType toBusRec = parser.getBus(tid);
				if (fromBusRec != null && toBusRec != null) {
					AclfDataSetter.setXfrRatingData(xfrBranch,
							fromBusRec.getBaseVoltage().getValue(), 
							toBusRec.getBaseVoltage().getValue(), 
							fromBusRec.getBaseVoltage().getUnit());				
				}
				else {
					throw new ODMException("Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: " + fid + ", " + tid);
				}
			} else {
				PSXfrBranchXmlType psXfrBranch = (PSXfrBranchXmlType)branch;
				AclfDataSetter.createPhaseShiftXfrData(psXfrBranch, rpu, xpu, ZUnitType.PU,
						ratio, 1.0, angle, 0.0, AngleUnitType.DEG,
						0.0, bpu, YUnitType.PU);
				BusXmlType fromBusRec = parser.getBus(fid);
				BusXmlType toBusRec = parser.getBus(tid);
				if (fromBusRec != null && toBusRec != null) {
					AclfDataSetter.setXfrRatingData(psXfrBranch,
							fromBusRec.getBaseVoltage().getValue(), 
							toBusRec.getBaseVoltage().getValue(), 
							fromBusRec.getBaseVoltage().getUnit());				
				}
				else {
					throw new ODMException("Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: " + fid + ", " + tid);
				}
			}
		}

		//    	Columns 51-55   Line MVA rating No 1 [I] Left justify!
		//    	Columns 57-61   Line MVA rating No 2 [I] Left justify!
		//    	Columns 63-67   Line MVA rating No 3 [I] Left justify!
		double rating1Mvar = dataParser.getDouble("MvaRating1", 0.0), 
			   rating2Mvar = dataParser.getDouble("MvaRating2", 0.0), 
			   rating3Mvar = dataParser.getDouble("MvaRating3", 0.0);
		branch.setRatingLimit(odmObjFactory.createBranchRatingLimitXmlType());
		AclfDataSetter.setBranchRatingLimitData(branch.getRatingLimit(),
				rating1Mvar, rating2Mvar, rating3Mvar, ApparentPowerUnitType.MVA);

		String controlBusId = "";
		int controlSide = 0;
		double stepSize = 0.0, maxTapAng = 0.0, minTapAng = 0.0, maxVoltPQ = 0.0, minVoltPQ = 0.0;
		if (branchType > 1) {
			//    		Columns 69-72   Control bus number
			controlBusId = AbstractModelParser.BusIdPreFix + dataParser.getString("CntlBusNum");

			//        	Column  74      Side [I]
			//          	0 - Controlled bus is one of the terminals
			//          	1 - Controlled bus is near the tap side
			//          	2 - Controlled bus is near the impedance side (Z bus)
			controlSide = dataParser.getInt("CntlBusSide", 0);

			//        	Columns 106-111 Step size [F]
			stepSize = dataParser.getDouble("TapStepSize");

			//        	Columns 91-97   Minimum tap or phase shift [F]
			//        	Columns 98-104  Maximum tap or phase shift [F]
			minTapAng = dataParser.getDouble("MaxTapShiftAng");
			maxTapAng = dataParser.getDouble("MinTapShiftAng");

			//        	Columns 113-119 Minimum voltage, MVAR or MW limit [F]
			//        	Columns 120-126 Maximum voltage, MVAR or MW limit [F]
			maxVoltPQ = dataParser.getDouble("MinVoltMvarMw");
			minVoltPQ = dataParser.getDouble("MaxVoltMvarMw");
		}

		if (branchType == 2 || branchType == 3) {
			XfrBranchXmlType xfrBranch = (XfrBranchXmlType)branch;
			TapAdjustmentXmlType tapAdj = odmObjFactory.createTapAdjustmentXmlType();
			xfrBranch.setTapAdjustment(tapAdj);
			tapAdj.setTapLimit(BaseDataSetter.createTapLimit(maxTapAng, minTapAng));
			tapAdj.setTapAdjStepSize(stepSize);
			tapAdj.setTapAdjOnFromSide(true);
			if (branchType == 2) {
				VoltageAdjustmentDataXmlType voltTapAdj = odmObjFactory.createVoltageAdjustmentDataXmlType();
				tapAdj.setVoltageAdjData(voltTapAdj);
				
				voltTapAdj.setAdjVoltageBus(parser.createBusRef(controlBusId));
					
				voltTapAdj.setAdjBusLocation(controlSide == 0 ? TapAdjustBusLocationEnumType.TERMINAL_BUS
								: (controlSide == 1 ? TapAdjustBusLocationEnumType.NEAR_FROM_BUS
										: TapAdjustBusLocationEnumType.NEAR_TO_BUS));
				voltTapAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
				
				if(voltTapAdj.getRange()==null)voltTapAdj.setRange(new LimitXmlType());
				BaseDataSetter.setLimit(voltTapAdj.getRange(), maxVoltPQ, minVoltPQ);
				
			} else if (branchType == 3) {
				MvarFlowAdjustmentDataXmlType mvarTapAdj = odmObjFactory.createMvarFlowAdjustmentDataXmlType();
				tapAdj.setMvarFlowAdjData(mvarTapAdj);
				
				if(mvarTapAdj.getRange()==null)mvarTapAdj.setRange(new LimitXmlType());
				BaseDataSetter.setLimit(mvarTapAdj.getRange(), maxVoltPQ, minVoltPQ);
				
				mvarTapAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
				mvarTapAdj.setMvarMeasuredOnFormSide(true);
			}
		} else if (branchType == 4) {
			PSXfrBranchXmlType psXfrBranch = (PSXfrBranchXmlType)branch;
			AngleAdjustmentXmlType angAdj = odmObjFactory.createAngleAdjustmentXmlType();
			psXfrBranch.setAngleAdjustment(angAdj);
			angAdj.setAngleLimit(odmObjFactory.createAngleLimitXmlType());
			BaseDataSetter.setLimit(angAdj.getAngleLimit(), maxTapAng, minTapAng);
			BaseDataSetter.setLimit(angAdj.getRange(), maxVoltPQ, minVoltPQ);
			angAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
			angAdj.setDesiredMeasuredOnFromSide(true);
		}
	}
}