 /*
  * @(#)AclfDataSetter.java   
  *
  * Copyright (C) 2009 www.interpss.org
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
  * @Date 04/11/2009
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import javax.activation.UnsupportedDataTypeException;

import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.ApparentPowerXmlType;
import org.ieee.odm.schema.BranchRatingLimitXmlType;
import org.ieee.odm.schema.BusLoadDataXmlType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.CurrentXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LengthUnitType;
import org.ieee.odm.schema.LineBranchInfoXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.MagnitizingZSideEnumType;
import org.ieee.odm.schema.MvaRatingXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SwitchedShuntBlockXmlType;
import org.ieee.odm.schema.SwitchedShuntDataXmlType;
import org.ieee.odm.schema.SwitchedShuntModeEnumType;
import org.ieee.odm.schema.SwitchedShuntXmlType;
import org.ieee.odm.schema.TransformerInfoXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.VoltageXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.ieee.odm.schema.ZXmlType;

/**
 * Aclf model parser data setter functions
 * 
 * @author mzhou
 *
 */
public class AclfDataSetter extends BaseDataSetter {
	/**
	 * set load data, first create an equivLoad object, then set value(code, p, q, unit) to the created EquivLoadData object
	 * 
	 * @param busData
	 * @param code
	 * @param p
	 * @param q
	 * @param unit
	 */
	public static void setLoadData(LoadflowBusXmlType bus, 
			LFLoadCodeEnumType code, 
			double p, double q, ApparentPowerUnitType unit) {
		BusLoadDataXmlType loadData = OdmObjFactory.createBusLoadDataXmlType();
		bus.setLoadData(loadData);
		
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus.getLoadData());
		
		// TODO debug code
		if (bus.getId().equals("Bus14167")) {
			int i = 0;
		}
		
		defaultLoad.setCode(code);
    	if(code==LFLoadCodeEnumType.CONST_P)
    	    defaultLoad.setConstPLoad(createPowerValue(p, q, unit));
    	else if(code==LFLoadCodeEnumType.LOAD_PV)
    	    defaultLoad.setConstPLoad(createPowerValue(p, 0.0, unit));
    	else if(code==LFLoadCodeEnumType.CONST_I)
    		defaultLoad.setConstILoad(createPowerValue(p, q, unit));
    	else if (code==LFLoadCodeEnumType.CONST_Z)
    		defaultLoad.setConstZLoad(createPowerValue(p, q, unit));
		else
			try {
				throw new UnsupportedDataTypeException();
			} catch (UnsupportedDataTypeException e) {
				e.printStackTrace();
			}
    	
	}

	/**
	 * set ZIP type (or Function Type) of load data to a load flow Bus
	 * 
	 * @param bus
	 * @param loadSP
	 * @param loadSQ
	 * @param loadIP
	 * @param loadIQ
	 * @param loadZP
	 * @param loadZQ
	 * @param unit
	 */
	public static void setZIPLoadData(LoadflowBusXmlType bus,double loadSP,
			double loadSQ,double loadIP,double loadIQ, double loadZP, double loadZQ,
			ApparentPowerUnitType unit){
		
		//BusLoadDataXmlType loadData = OdmObjFactory.createBusLoadDataXmlType();
		//bus.setLoadData(loadData);
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus.getLoadData());
		//loadData.setEquivLoad(OdmObjFactory.createEquivLoad(equivLoad));
		defaultLoad.setCode(LFLoadCodeEnumType.FUNCTION_LOAD);
    	
    	defaultLoad.setConstPLoad(createPowerValue(loadSP, loadSQ, unit));
    	defaultLoad.setConstILoad(createPowerValue(loadIP, loadIQ, unit));
    	defaultLoad.setConstZLoad(createPowerValue(loadZP, loadZQ, unit));
	}
	
	/**
	 * set EquivGen object, then set value(code, p, q, unit) to the created EquivGenData object
	 * 
	 * @param bus
	 * @param code
	 * @param v
	 * @param vUnit
	 * @param ang
	 * @param angUnit
	 * @param p
	 * @param q
	 * @param pUnit
	 */
	public static void setGenData(LoadflowBusXmlType bus, LFGenCodeEnumType code, 
			double v, VoltageUnitType vUnit,
			double p, double q, ApparentPowerUnitType pUnit) {
		setGenData(bus, code);
   		LoadflowGenDataXmlType defaulfGen = AclfParserHelper.getDefaultGen(bus.getGenData());
		defaulfGen.setPower(createPowerValue(p, q, pUnit));
   		if (code == LFGenCodeEnumType.PV || code == LFGenCodeEnumType.SWING) {
   			defaulfGen.setDesiredVoltage(createVoltageValue(v, vUnit));
   		}
	}	

	/**
	 * set EquivGen object, then set value(code, p, q, unit) to the created EquivGenData object
	 * 
	 * @param bus
	 * @param code
	 * @param v
	 * @param vUnit
	 * @param ang
	 * @param angUnit
	 */
	public static void setGenData(LoadflowBusXmlType bus, LFGenCodeEnumType code) {
   		bus.getGenData().setCode(code);
	}	

	/**
	 * Set bus shunt Y 
	 * 
	 * @param bus
	 * @param re
	 * @param im
	 * @param unit
	 */
	public static void setBusShuntY(LoadflowBusXmlType bus, double re, double im, YUnitType unit) {
		bus.getShuntYData().setEquivY(OdmObjFactory.createYXmlType());
		bus.getShuntYData().getEquivY().setRe(re);
		bus.getShuntYData().getEquivY().setIm(im);
		bus.getShuntYData().getEquivY().setUnit(unit);
	}	

	/**
	 * Set bus shunt Y 
	 * 
	 * @param bus
	 * @param var
	 * @param unit
	 */
	public static void setBusShuntVar(LoadflowBusXmlType bus, double var, YUnitType unit) {
		setBusShuntY(bus, 0.0, var, unit);
	}

	/**
	 * set the shunt compensator data
	 * 
	 * @param bus
	 * @param shuntId
	 * @param mode
	 * @param normalMvar
	 * @param vHigh
	 * @param vLow
	 */
	public static void setShuntCompensatorData(LoadflowBusXmlType bus,SwitchedShuntModeEnumType mode
			, double normalMvar, double vHigh, double vLow){
		SwitchedShuntXmlType shunt = OdmObjFactory.createSwitchedShuntXmlType();
		SwitchedShuntDataXmlType shuntData = OdmObjFactory.createSwitchedShuntDataXmlType();
		bus.setSwitchedShuntData(shuntData);
		shuntData.getContributeSwitchedShunt().add(shunt);
		shunt.setDesiredVoltageRange(createVoltageLimit(vHigh, vLow, VoltageUnitType.KV));
		shunt.setNorminalQOutput(createReactivePowerValue(normalMvar, ReactivePowerUnitType.MVAR));
		shunt.setMode(mode);

	}

	/**
	 * set the shunt compensator block
	 * 
	 * @param bus
	 * @param steps
	 * @param mvarPerStep
	 * @param type
	 */
	public static void addShuntCompensatorBlock(LoadflowBusXmlType bus,SwitchedShuntXmlType shunt,int steps, double mvarPerStep,
			ReactivePowerUnitType type){
		SwitchedShuntBlockXmlType block=OdmObjFactory.createSwitchedShuntBlockXmlType();
		shunt.getBlock().add(block);
		block.setSteps(steps);
		block.setIncrementB(createReactivePowerValue(mvarPerStep, type));
		
	}
	
	/**
	 * add bus shunt Y
	 * 
	 * @param bus
	 * @param re
	 * @param im
	 * @param unit
	 */
	public static void addBusShuntY(LoadflowBusXmlType bus, double re, double im, YUnitType unit) {
		if (bus.getShuntYData() == null) {
			bus.setShuntYData(OdmObjFactory.createBusShuntYDataXmlType());
			bus.getShuntYData().setEquivY(OdmObjFactory.createYXmlType());
		}else if(bus.getShuntYData().getEquivY() == null){
			bus.getShuntYData().setEquivY(OdmObjFactory.createYXmlType());
		}
		bus.getShuntYData().getEquivY().setRe(re + bus.getShuntYData().getEquivY().getRe());
		bus.getShuntYData().getEquivY().setIm(im + bus.getShuntYData().getEquivY().getIm());
		bus.getShuntYData().getEquivY().setUnit(unit);
	}	

	/**
	 * add bus shunt Y
	 * 
	 * @param bus
	 * @param var
	 * @param unit
	 */
	public static void addBusShuntVar(LoadflowBusXmlType bus, double var, YUnitType unit) {
		addBusShuntY(bus, 0.0, var, unit);
	}	

	/**
	 * add a LineData object to the branchData object, then set value(r, x, zUnit, g, b, yUnit) 
	 * to the created LineData object
	 * 
	 * @param branchData
	 * @param r
	 * @param x
	 * @param zUnit
	 * @param g
	 * @param b
	 * @param bUnit
	 */
	public static void setLineData(LineBranchXmlType branch, 
			             double r, double x, ZUnitType zUnit, 
			             double g, double b, YUnitType yUnit) {
		ZXmlType z = createZValue(r, x, zUnit);
		branch.setZ(z);
		if (g != 0.0 || b != 0.0) {
			branch.setTotalShuntY(createYValue(g, b, yUnit));
		}
	}
	
	/**
	 * set Line lengh
	 * 
	 * @param lineInfo
	 * @param length
	 * @param unit
	 */
	public static void setLineLength(LineBranchInfoXmlType lineInfo, 
            double length, LengthUnitType unit) {
		lineInfo.setLength(OdmObjFactory.createLengthXmlType());
		lineInfo.getLength().setValue(length);
		lineInfo.getLength().setUnit(unit);
	}

	/**
	 * add a XformerData object to the branchData object, then set value(r, x, zUnit, gFrom, bFrom, gTo, bTo, yUnit) to the created XfomerData object
	 * 
	 * @param branchData
	 * @param r
	 * @param x
	 * @param zUnit
	 * @param gFrom
	 * @param bFrom
	 * @param gTo
	 * @param bTo
	 * @param bUnit
	 */
	public static void createXformerData(XfrBranchXmlType branch, 
            double r, double x, ZUnitType zUnit,
            double fromTurnRatio, double toTurnRatio) {
		branch.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
		branch.getXfrInfo().setDataOnSystemBase(true);
		setXformerData(branch,
				r, x, zUnit, fromTurnRatio, toTurnRatio,
				0.0, 0.0, YUnitType.PU, MagnitizingZSideEnumType.HIGH_VOLTAGE);
	}

	/**
	 * add a XformerData object to the branchData object, then set value(r, x, zUnit, gFrom, bFrom, gTo, bTo, yUnit) to the created XfomerData object
	 * 
	 * @param branch
	 * @param r
	 * @param x
	 * @param zUnit
	 * @param fromTurnRaio
	 * @param toTurnRatio
	 * @param gMag
	 * @param bMag
	 * @param yUnit
	 */
	public static void createXformerData(XfrBranchXmlType branch, 
			             double r, double x, ZUnitType zUnit,
			             double fromTurnRaio, double toTurnRatio,
			             double gMag, double bMag, YUnitType yUnit, MagnitizingZSideEnumType side) {
		branch.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
		branch.getXfrInfo().setDataOnSystemBase(true);
		setXformerData(branch,
				r, x, zUnit, fromTurnRaio, toTurnRatio,
				gMag, bMag, yUnit, side);
	}

	private static void setXformerData(XfrBranchXmlType xfr,
			double r, double x, ZUnitType zUnit, 
			double fromTurnRatio, double toTurnRatio,
			double gMag, double bMag, YUnitType yUnit, MagnitizingZSideEnumType side) {
		ZXmlType z = createZValue(r, x, zUnit);
		xfr.setZ(z);
		xfr.setFromTurnRatio(createTurnRatioPU(fromTurnRatio));
		xfr.setToTurnRatio(createTurnRatioPU(toTurnRatio));
		if (gMag != 0.0 || bMag != 0.0) {
			xfr.setMagnitizingY(createYValue(gMag, bMag, yUnit));
			xfr.setMagnitizingZSide(side);
		}	
	}	
	
	/**
	 * add a PhaseShiftXfrData object to the branchData object, then set value(r, x, zUnit, gFrom, bFrom, gTo, bTo, yUnit) 
	 * to the created PhaseShiftXfrData object
	 * 
	 * @param branchData
	 * @param r
	 * @param x
	 * @param zUnit
	 * @param gFrom
	 * @param bFrom
	 * @param bUnit
	 */
	public static void createPhaseShiftXfrData(PSXfrBranchXmlType branch,
			double r, double x, ZUnitType zUnit,
			double fromTap, double toTap, double fromAng, double toAng, AngleUnitType angUnit,
			double gFrom, double bFrom, YUnitType yUnit, MagnitizingZSideEnumType side) {
		setXformerData(branch,
				r, x, zUnit, fromTap, toTap, gFrom, bFrom, yUnit, side);
		//TODO if we don't set it, it will cause if we want to getFromAngle(), which
		// return null, why not just let it be zero, a PSXfr without a fromAngle attribute
		// is not completed ;
		branch.setFromAngle(createAngleValue(fromAng, angUnit));
//		if (fromAng != 0.0) {
//			branch.setFromAngle(createAngleValue(fromAng, angUnit));
//		}
		if (toAng != 0.0) {
			branch.setToAngle(createAngleValue(toAng, angUnit));
		}
	}

	/**
	 * add a PhaseShiftXfrData object to the branchData object, then set value(r, x, zUnit, gFrom, bFrom, gTo, bTo, yUnit) 
	 * to the created PhaseShiftXfrData object
	 * 
	 * @param branchData
	 * @param r
	 * @param x
	 * @param zUnit
	 * @param gFrom
	 * @param bFrom
	 * @param bUnit
	 */
	public static void createPhaseShiftXfrData(PSXfrBranchXmlType branchData,
			double r, double x, ZUnitType zUnit,
			double fromTap, double toTap,
			double fromAng, double toAng, AngleUnitType angUnit) {
		createPhaseShiftXfrData(branchData, r, x, zUnit,
				fromTap, toTap, fromAng, toAng, angUnit,
				0.0, 0.0, YUnitType.PU, MagnitizingZSideEnumType.HIGH_VOLTAGE);
	}	
	
	/**
	 * set transformer rating data
	 *  
	 * @param branchData
	 * @param fromRatedV
	 * @param toRatedV
	 * @param vUnit
	 * @param normialMva
	 * @param pUnit
	 */
	public static void setXfrRatingData(XfrBranchXmlType branch, 
			double fromRatedV, double toRatedV, VoltageUnitType vUnit,
			double normialMva, ApparentPowerUnitType pUnit) {
		if (branch.getXfrInfo() == null) {
			branch.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
		}
		TransformerInfoXmlType xfrInfo = branch.getXfrInfo();
		VoltageXmlType fromRatedVolt = OdmObjFactory.createVoltageXmlType();
		xfrInfo.setFromRatedVoltage(fromRatedVolt);
		fromRatedVolt.setValue(fromRatedV);
		fromRatedVolt.setUnit(vUnit);
		VoltageXmlType toRatedVolt = OdmObjFactory.createVoltageXmlType();
		xfrInfo.setToRatedVoltage(toRatedVolt);
		toRatedVolt.setValue(toRatedV);
		toRatedVolt.setUnit(vUnit);
   		if (normialMva != 0.0) {
   			ApparentPowerXmlType ratedMva = OdmObjFactory.createApparentPowerXmlType();
   			xfrInfo.setRatedPower(ratedMva);
   			ratedMva.setValue(normialMva);
   			ratedMva.setUnit(pUnit);		
   		}
	}

	/**
	 * set transformer rating data
	 * 
	 * @param xfrData
	 * @param fromRatedV
	 * @param toRatedV
	 * @param vUnit
	 */
	public static void setXfrRatingData(XfrBranchXmlType xfrData, 
			double fromRatedV, double toRatedV, VoltageUnitType vUnit) {
		setXfrRatingData(xfrData, fromRatedV, toRatedV, vUnit, 0.0, null);
	}	
	
	/**
	 * add a RatingLimitData object to the branchData object, then set value(mvarLimit1, mvarLimit2, mvarLimit3, mvarUnit, curLimit, curUnit) 
	 * to the created RatingLimitData object
	 * 
	 * @param branchLimit
	 * @param mvar1
	 * @param mvar2
	 * @param mvar3
	 * @param mvarUnit
	 * @param current
	 * @param curUnit
	 */
	public static void setBranchRatingLimitData(BranchRatingLimitXmlType branchLimit, 
				double mva1, double mva2, double mva3, ApparentPowerUnitType mvaUnit,
				double current, CurrentUnitType curUnit) {
    	if (mva1 != 0.0 || mva2 != 0.0 || mva3 != 0.0 || current != 0.0) {
        	if (mva1 != 0.0 || mva2 != 0.0 || mva3 != 0.0) {
        		MvaRatingXmlType mvaRating = OdmObjFactory.createMvaRatingXmlType();
        		branchLimit.setMva(mvaRating);
        		mvaRating.setRating1(mva1);
        		mvaRating.setRating2(mva2);
        		mvaRating.setRating3(mva3);
        		mvaRating.setUnit(mvaUnit);
        	}

        	if (current != 0.0) {
        		CurrentXmlType limit = OdmObjFactory.createCurrentXmlType();
        		branchLimit.setCurrent(limit);
        		limit.setValue(current);
        		limit.setUnit(curUnit);
        	}
    	}
	}

	/**
	 * add a RatingLimitData object to the branchData object, then set value(mvaLimit1, mvaLimit2, mvaLimit3, mvaUnit) 
	 * to the created RatingLimitData object
	 * 
	 * @param branchData
	 * @param mva1
	 * @param mva2
	 * @param mva3
	 * @param mvaUnit
	 */
	public static void setBranchRatingLimitData(BranchRatingLimitXmlType branchLimit, 
				double mva1, double mva2, double mva3, ApparentPowerUnitType mvaUnit) {
		setBranchRatingLimitData(branchLimit, mva1, mva2, mva3, mvaUnit, 0.0, null);
	}

	/**
	 * add a RatingLimitData object to the branchData object, then set value( mvaAry [contains up to 12 limits], mvaUnit) 
	 * to the created RatingLimitData object
	 * 
	 * @param branchData
	 * @param mvaAry
	 * @param mvaUnit
	 */
	public static void setBranchRatingLimitData(BranchRatingLimitXmlType branchLimit, 
			double[] mvaAry, ApparentPowerUnitType mvaUnit) {
		//setBranchRatingLimitData(branchLimit, mvaAry[0], mvaAry[1], mvaAry[2], mvaUnit);
		
		if(mvaAry !=null && mvaAry[0]>0) {
			MvaRatingXmlType mvaRating = OdmObjFactory.createMvaRatingXmlType();
			branchLimit.setMva(mvaRating);
			mvaRating.setUnit(mvaUnit);
			
			for (int i = 0; i < mvaAry.length; i++) {
		        switch (i) {
		            case 0:
		                mvaRating.setRating1(mvaAry[i]);
		                break;
		            case 1:
		                mvaRating.setRating2(mvaAry[i]);
		                break;
		            case 2:
		                mvaRating.setRating3(mvaAry[i]);
		                break;
		            case 3:
		                mvaRating.setRating4(mvaAry[i]);
		                break;
		            case 4:
		                mvaRating.setRating5(mvaAry[i]);
		                break;
		            case 5:
		                mvaRating.setRating6(mvaAry[i]);
		                break;
		            case 6:
		                mvaRating.setRating7(mvaAry[i]);
		                break;
		            case 7:
		                mvaRating.setRating8(mvaAry[i]);
		                break;
		            case 8:
		                mvaRating.setRating9(mvaAry[i]);
		                break;
		            case 9:
		                mvaRating.setRating10(mvaAry[i]);
		                break;
		            case 10:
		                mvaRating.setRating11(mvaAry[i]);
		                break;
		            case 11:
		                mvaRating.setRating12(mvaAry[i]);
		                break;
		        }
		    }
		}
	}

	/**
	 * add a RatingLimitData object to the branchData object, then set value(curLimit, curUnit) 
	 * to the created RatingLimitData object
	 * 
	 * @param branchData
	 * @param current
	 * @param curUnit
	 */
	public static void setBranchRatingLimitData(BranchRatingLimitXmlType branchLimit, 
				double current, CurrentUnitType curUnit) {
		setBranchRatingLimitData(branchLimit, 0.0, 0.0, 0.0, null, current, curUnit);
	}
}
