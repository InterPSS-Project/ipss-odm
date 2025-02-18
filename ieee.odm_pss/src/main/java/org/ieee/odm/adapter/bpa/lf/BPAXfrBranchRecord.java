/*
 * @(#)BPAXfrBranchRecord.java   
 *
 * Copyright (C) 2006-2011 www.interpss.org
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
package org.ieee.odm.adapter.bpa.lf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.text.NumberFormat;

import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.MvarFlowAdjustmentDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TapAdjustBusLocationEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class BPAXfrBranchRecord {
	static final int transformer=1;
	static final int phaseShiftXfr=2;
	static final int transformerAndPhaseShiftXfr=3;

	public void processXfrData(final String str, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		
		int dataType=0;	    	
		
		final String[] strAry = getXformerDataFields(str);			
			
		if(strAry[0].startsWith("T")){
			dataType=transformer;
		}
		else if(strAry[0].startsWith("TP")){
			dataType=phaseShiftXfr;
		}		
		
		final String modCode =strAry[1];
		final String owner=strAry[2];
			
		final String fname =  strAry[3];
		final String tname =  strAry[6];
		final String fid =  BPABusRecord.getBusId(fname);
		final String tid =  BPABusRecord.getBusId(tname);
		ODMLogger.getLogger().fine("Branch data loaded, from-bus, to-bus: " + fid + ", " + tid);
		//TODO change 1->0, since one uses "1" while CirId for the other is missing for some parallel branches in BPA
		String cirId="1";
		if(!strAry[8].equals("")){
			cirId = strAry[8];
		}
			
		XfrBranchXmlType branchRec = null;
		try {
			branchRec = (XfrBranchXmlType) (dataType == transformer ?
								parser.createXfrBranch(fid, tid, cirId) : parser.createPSXfrBranch(fid, tid, cirId));
		} catch (ODMBranchDuplicationException e) {
			ODMLogger.getLogger().severe("branch data error, " + e.toString()+ 
					"  " + fname + "->" + tname + "_" + cirId);
			return;
		}
		
		branchRec.setId(ODMModelStringUtil.formBranchId(fid, tid, cirId));
		
		final double fVbase= new Double(strAry[4]).doubleValue();
		final double tVbase= new Double(strAry[7]).doubleValue();	
			
			
		//  set tieline data, measure location for power interchange, 1--from side, 2- to side
//		int measureLocation=0;
//		if(!strAry[5].equals("")){				
//			measureLocation= new Integer(strAry[5]).intValue();
//			try{
//				if(measureLocation==1){	
//					PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();
//
//					tieLine.addNewMeteredBus().setName(fid);
//					tieLine.addNewNonMeteredBus().setName(tid);	
//						
//					BusRecordXmlType busRecFrom=XBeanParserHelper.findBusRecord(fid, baseCaseNet);						
//					NetAreaXmlType areaFrom=XBeanParserHelper.
//						 getAreaRecordByZone(busRecFrom.getZoneNumber(), baseCaseNet);
//					tieLine.setMeteredArea(areaFrom.getName());
//						
//					BusRecordXmlType busRecTo=XBeanParserHelper.findBusRecord(tid, baseCaseNet);						
//					NetAreaXmlType areaTo=XBeanParserHelper.
//						 getAreaRecordByZone(busRecTo.getZoneNumber(), baseCaseNet);
//					tieLine.setNonMeteredArea(areaTo.getName());					
//				}else{
//					PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();
//
//					tieLine.addNewMeteredBus().setName(tid);
//					tieLine.addNewNonMeteredBus().setName(fid);					
//					XBeanParserHelper.findBusRecord(fid, baseCaseNet).getZoneNumber();
//						
//					BusRecordXmlType busRecFrom=XBeanParserHelper.findBusRecord(tid, baseCaseNet);
//					busRecFrom.getZoneNumber();
//					NetAreaXmlType areaFrom=XBeanParserHelper.
//						 getAreaRecordByZone(busRecFrom.getZoneNumber(), baseCaseNet);
//					tieLine.setMeteredArea(areaFrom.getName());
//						
//					BusRecordXmlType busRecTo=XBeanParserHelper.findBusRecord(fid, baseCaseNet);
//					busRecTo.getZoneNumber();
//					NetAreaXmlType areaTo=XBeanParserHelper.
//						 getAreaRecordByZone(busRecTo.getZoneNumber(), baseCaseNet);
//					tieLine.setNonMeteredArea(areaTo.getName());					
//				}					
//			}catch (final Exception e) {
//					e.printStackTrace();
//			}				
//		}
			
		final String multiSectionId = strAry[9];
		//set rated current
		double MwRating=0.0;
		if(!strAry[11].equals("")){
			//MwRating = new Integer(strAry[11]).intValue();
		}
			
		// set xfr rating data
		AclfDataSetter.setXfrRatingData(branchRec, 
				fVbase, tVbase,VoltageUnitType.KV, MwRating, ApparentPowerUnitType.MVA);
			

		double rpu=0.0, xpu=0.0001, Gpu=0.0, Bpu=0.0;
		if(!strAry[12].equals("")){
			rpu = new Double(strAry[12]).doubleValue();
			if(Math.abs(rpu)>=1.0&&!strAry[12].contains(".")){
				rpu=rpu*0.00001;   //F6.5
			}
			rpu=ODMModelStringUtil.getNumberFormat(rpu);
			if(Math.abs(rpu)>0.1)
				ODMLogger.getLogger().warning("Tranformer#"+fname+"-to-"+tname +
						", the Resistance(R) now is"+rpu+" ,seems to be out of normal range[0~0.1]pu, please check!");
		}
		if(!strAry[13].equals("")){
			xpu = new Double(strAry[13]).doubleValue();
			if(Math.abs(xpu)>=1&&!strAry[13].contains(".")){
				xpu=xpu*0.00001;  //F6.5
			}
			xpu=ODMModelStringUtil.getNumberFormat(xpu);
			if(Math.abs(xpu)>0.5)
				ODMLogger.getLogger().warning("Tranformer#"+fname+"-to-"+tname+",the Reactance(X) now is"
						+xpu+" ,seems to be out of normal range[0~0.5]pu, please check!");
		}
		if(!strAry[14].equals("")){
			Gpu = new Double(strAry[14]).doubleValue();
			if(Math.abs(Gpu)>=1.0&&!strAry[14].contains(".")){
				Gpu=Gpu*1e-5;  //F6.5
			}
		}
		if(!strAry[15].equals("")){
			Bpu = new Double(strAry[15]).doubleValue();
			if(Math.abs(Bpu)>=1.0&&!strAry[15].contains(".")){
				Bpu=Bpu*1e-5;  //F6.5
			}
		}
		
		// set r x
		if(rpu!=0.0||xpu!=0.0){
			branchRec.setZ(BaseDataSetter.createZValue(rpu, xpu, ZUnitType.PU));
		}
		
		//set g b, g, b---> from side
		if(Gpu!=0.0||Bpu!=0.0){
			branchRec.setMagnitizingY(BaseDataSetter.createYValue(Gpu, Bpu, YUnitType.PU));
		}
		
		// tap1  or angle for phase shift
		double fromTurnRatedVolOrAngDeg=0.0, toTurnRatedVolOrZero=0.0;
		if(!strAry[16].equals("")){
			fromTurnRatedVolOrAngDeg = new Double(strAry[16]).doubleValue();				
		}
		//tap2
		if(strAry[17]!=null&&!strAry[17].equals("")){
			toTurnRatedVolOrZero = new Double(strAry[17]).doubleValue();
		}
		double fRatio=1.0, tRatio=1.0;			
        
		// set transformer ratio and phaseshiftxfr angle
		if (dataType==transformer){					
			//to see what is the input data format, specified or not.
			if(fromTurnRatedVolOrAngDeg>=2*fVbase){
				fromTurnRatedVolOrAngDeg=fromTurnRatedVolOrAngDeg/100.0;		//F5.2		
			}	
			fRatio=fromTurnRatedVolOrAngDeg/fVbase;
			branchRec.setFromTurnRatio(BaseDataSetter.createTurnRatioPU(fRatio));
			
			if(toTurnRatedVolOrZero>=2*tVbase){
				toTurnRatedVolOrZero=toTurnRatedVolOrZero/100.0;		//F5.2		
			}
			tRatio = toTurnRatedVolOrZero/tVbase;
			//System.out.println("toTurnRatedVol: "+toTurnRatedVolOrZero+", tratio="+tRatio);
			NumberFormat ddf1 = NumberFormat.getNumberInstance();
			ddf1.setMaximumFractionDigits(4);
			tRatio = new Double(ddf1.format(tRatio)).doubleValue();		
			branchRec.setToTurnRatio(BaseDataSetter.createTurnRatioPU(tRatio));
		}
		else {			
			PSXfrBranchXmlType psXfrRec = (PSXfrBranchXmlType)branchRec;
			psXfrRec.setFromAngle(BaseDataSetter.createAngleValue(fromTurnRatedVolOrAngDeg, AngleUnitType.DEG));
			psXfrRec.setToAngle(BaseDataSetter.createAngleValue(0, AngleUnitType.DEG));						
		}			
	}			
	
	
	public void processXfrAdjustData(final String str, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		
		final String[] strAry = getXfrAdjustDataFields(str);
		
		int dataType=0;
		int angleAdjustment=1;
		int tapAdjustment=2;
		
		int adjustType=3;
		int tapVoltageAdjustment=4;
		int tapVarAdjustment=5;
		int pAngleAdjustment=6;
		int mAngleAdjustment=7;
		
		if(strAry[0].equals("RP")||strAry[0].equals("RM")){			
			dataType=angleAdjustment;
			if(strAry[0].equals("RP")){
				adjustType=pAngleAdjustment;
			}else{
				adjustType=mAngleAdjustment;
			}
		}
		else{
			dataType=tapAdjustment;
			if(strAry[0].equals("R")||strAry[0].equals("RV")){
				adjustType=tapVoltageAdjustment;
			}else {
				adjustType=tapVarAdjustment;
			}
		}			
		
		//adjustType: R or RV---remote bus control			
		final String modCode = strAry[1];
		final String owner = strAry[2];
		final String fromBus =strAry[3];
		final double fromTurnRatedV = new Double(strAry[4]).doubleValue();
		final String toBus = strAry[6];
		final double toTurnRatedV = new Double(strAry[7]).doubleValue();	
		String cirId = "unknow";
		
		XfrBranchXmlType branchRec = (XfrBranchXmlType)parser.getBranch(fromBus, toBus, cirId);	
		
		String controlBusId = "";		
		
		final String adjBus =strAry[8];
		final String adjVol =strAry[9];	
		// set tapAdjSide
		int tapAdjSide = 0;
		if(!strAry[5].equals("")){
			tapAdjSide=new Integer(strAry[5]).intValue();				
		}
		
		double   stepSize=0.0,maxVoltPQ = 0.0, minVoltPQ = 0.0, totalTap=0.0;
		double max=0.0, min=0.0;
		// Minimum  MVAR or MW limit [F]
		// Maximum  MVAR or MW limit [F]			
		
		
		if(!strAry[10].equals("")){
			max = new Double(strAry[10]).doubleValue();				
		}
		if(!strAry[11].equals("")){
			min = new Double(strAry[11]).doubleValue();				
		}
		if(!strAry[12].equals("")){
			totalTap = new Double(strAry[12]).doubleValue();
		}
		if(!strAry[13].equals("")){
			maxVoltPQ = new Double(strAry[13]).doubleValue();
		}
		if(!strAry[14].equals("")){
			minVoltPQ = new Double(strAry[14]).doubleValue();
		}			
		// calculate stepsize
		if (totalTap!=0.0){
			stepSize =(max-min)/totalTap;
		}
		
		// scheduled Q for RQ 			
		double scheduleQ=0.0;
		if(!strAry[13].equals("")){
			scheduleQ = new Double(strAry[13]).doubleValue();
		}	
		
		if(dataType==tapAdjustment){	
			TapAdjustmentXmlType tapAdj = OdmObjFactory.createTapAdjustmentXmlType();
			branchRec.setTapAdjustment(tapAdj);
			
            if(tapAdjSide==1){
				tapAdj.setTapAdjOnFromSide(true);
				if(max>=2*fromTurnRatedV){
					max=max/100;
				}
				max=max/fromTurnRatedV;
				if(min>=2*fromTurnRatedV){
					min=min/100;
				}
				min=min/fromTurnRatedV;
			}
            else{
				tapAdj.setTapAdjOnFromSide(false);
				if(max>=2*toTurnRatedV){
					max=max/100;
				}
				max=max/toTurnRatedV;
				if(min>=2*toTurnRatedV){
					min=min/100;
				}
				min=min/toTurnRatedV;
			}
            // save result to two digits after .
            NumberFormat ddf1=NumberFormat.getNumberInstance() ;
            ddf1.setMaximumFractionDigits(2);
            max= new Double(ddf1.format(max)).doubleValue() ; 
            min= new Double(ddf1.format(min)).doubleValue() ; 
			
			if(tapAdjSide==2){
				tapAdj.setTapAdjOnFromSide(false);
				controlBusId=toBus;
			}else{
				tapAdj.setTapAdjOnFromSide(true);
				controlBusId=fromBus;
			}
			
			tapAdj.setTapLimit(BaseDataSetter.createTapLimit(max, min));
			tapAdj.setTapAdjStepSize(stepSize);
			if (adjustType==tapVoltageAdjustment ){// voltage control					
				VoltageAdjustmentDataXmlType voltTapAdj = OdmObjFactory.createVoltageAdjustmentDataXmlType();
				tapAdj.setVoltageAdjData(voltTapAdj);
				try {
					voltTapAdj.setAdjVoltageBus(parser.createBusRef(controlBusId));
				} catch (Exception e) {
					ODMLogger.getLogger().severe("Xfr control bus not defined properly, " + e.toString());
				}
					
				voltTapAdj.setAdjBusLocation(adjBus == toBus ? TapAdjustBusLocationEnumType.NEAR_TO_BUS
												: TapAdjustBusLocationEnumType.NEAR_FROM_BUS);
				voltTapAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
				BaseDataSetter.setLimit(voltTapAdj.getRange(), maxVoltPQ, minVoltPQ);				
			} 
			else if (adjustType==tapVarAdjustment) {// var control						
				MvarFlowAdjustmentDataXmlType mvarTapAdj = OdmObjFactory.createMvarFlowAdjustmentDataXmlType();
				tapAdj.setMvarFlowAdjData(mvarTapAdj);
				BaseDataSetter.setLimit(mvarTapAdj.getRange(), maxVoltPQ, minVoltPQ);
				mvarTapAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
				mvarTapAdj.setMvarMeasuredOnFormSide(true);				
			}
		} 
		else if(dataType==angleAdjustment){
			PSXfrBranchXmlType psXfrBranch = (PSXfrBranchXmlType)branchRec;
			AngleAdjustmentXmlType angAdj = OdmObjFactory.createAngleAdjustmentXmlType();
			psXfrBranch.setAngleAdjustment(angAdj);
			angAdj.setAngleLimit(OdmObjFactory.createAngleLimitXmlType());
			BaseDataSetter.setLimit(angAdj.getAngleLimit(), maxVoltPQ, minVoltPQ);
			BaseDataSetter.setLimit(angAdj.getRange(), maxVoltPQ, minVoltPQ);
			angAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
			angAdj.setDesiredMeasuredOnFromSide(true);			
		}
		
	}
/*
T  yn DD1G    22.0 DD50    525.   720..000270.0202            22.0 536.
*/
	private static String[] getXformerDataFields(final String str) {
		
		final String[] strAry = new String[20];
		try{
			strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2);
            strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
			strAry[2] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
			
//			strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
//			strAry[4] = ModelStringUtil.getStringReturnEmptyString(str,15, 18).trim();
//			strAry[5] = ModelStringUtil.getStringReturnEmptyString(str,19, 19).trim();
//			strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,20, 27).trim();			
			
			//----to process the Chinese characters in the fromBus name, if any.
			String tem=ODMModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
			int chnCharNum1=ODMModelStringUtil.getChineseCharNum(tem);
			
//			System.out.println("chnCharNum1:"+chnCharNum1);
			//from bus name
			strAry[3] = ODMModelStringUtil.getStringReturnEmptyString(str,7, 14-chnCharNum1).trim();
			//from bus basekV
			strAry[4] = ODMModelStringUtil.getStringReturnEmptyString(str,15-chnCharNum1, 18-chnCharNum1).trim();
			//meter
			strAry[5] = ODMModelStringUtil.getStringReturnEmptyString(str,19-chnCharNum1, 19-chnCharNum1).trim();
			
			//---to process the Chinese characters in the toBus name, if any.
			tem=ODMModelStringUtil.getStringReturnEmptyString(str,20-chnCharNum1, 27-chnCharNum1).trim();
			int chnCharNum2=ODMModelStringUtil.getChineseCharNum(tem);
//			System.out.println("chnCharNum2:"+chnCharNum2);
			//to bus name
			strAry[6] = ODMModelStringUtil.getStringReturnEmptyString(str,20-chnCharNum1, 27-chnCharNum1-chnCharNum2).trim();
			
			//--- replace all the Chinese Characters, since they are not used in the following processing.
			String str2=ODMModelStringUtil.replaceChineseChar(str);


			strAry[7] = ODMModelStringUtil.getStringReturnEmptyString(str2,28, 31).trim();
			strAry[8] = ODMModelStringUtil.getStringReturnEmptyString(str2,32, 32).trim();
			strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,33, 33).trim();
			strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,34, 37).trim();
			strAry[11] = ODMModelStringUtil.getStringReturnEmptyString(str2,38, 38).trim();
			strAry[12] = ODMModelStringUtil.getStringReturnEmptyString(str2,39, 44).trim();
			strAry[13] = ODMModelStringUtil.getStringReturnEmptyString(str2,45, 50).trim();
			strAry[14] = ODMModelStringUtil.getStringReturnEmptyString(str2,51, 56).trim();
			strAry[15] = ODMModelStringUtil.getStringReturnEmptyString(str2,57, 62).trim();
			strAry[16] = ODMModelStringUtil.getStringReturnEmptyString(str2,63, 67).trim();//// tap of the fromBus
           /*
            T  yn DD1G    22.0 DD50    525.   720..000270.0202            22.0 536. 			
           */
			// tap of the toBus
			if (str2.length() >= 68){
				if(str2.length() <= 72)
				     strAry[17] = ODMModelStringUtil.getStringReturnEmptyString(str2,68, str2.length()).trim();	
				else {
					strAry[17]= ODMModelStringUtil.getStringReturnEmptyString(str2,68, 72).trim();
				}
			}
			
			if (str2.length() > 78)
				strAry[18] = ODMModelStringUtil.getStringReturnEmptyString(str2,74, 77).trim();// str2.substring(74, 77).trim();
			
			if (str2.length() > 81)
				strAry[19] =ODMModelStringUtil.getStringReturnEmptyString(str2,77, 80).trim();// str2.substring(77, 80).trim();
		}catch(Exception e){
			ODMLogger.getLogger().severe(e.toString() + "\n" + str);
		}
		return strAry;
    }
	
	private static String[] getXfrAdjustDataFields(final String str) {
		final String[] strAry = new String[15];
		
		try{
			// type 		
            strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
            strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
			strAry[2] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
//			//from bus name
//			strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
//			// rated v
//			strAry[4] = ModelStringUtil.getStringReturnEmptyString(str,15, 18).trim();
//			// point of metering interface exchange power 
//			strAry[5] = ModelStringUtil.getStringReturnEmptyString(str,19, 19).trim();
//			//to bus name
//			strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,20, 27).trim();
			
			//----to process the Chinese characters in the fromBus name, if any.
			String tem=ODMModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
			int chnCharNum1=ODMModelStringUtil.getChineseCharNum(tem);
			
			//from bus name
			strAry[3] = ODMModelStringUtil.getStringReturnEmptyString(str,7, 14-chnCharNum1).trim();
			//from bus basekV
			strAry[4] = ODMModelStringUtil.getStringReturnEmptyString(str,15-chnCharNum1, 18-chnCharNum1).trim();
			//meter
			strAry[5] = ODMModelStringUtil.getStringReturnEmptyString(str,19-chnCharNum1, 19-chnCharNum1).trim();
			
			//---to process the Chinese characters in the toBus name, if any.
			tem=ODMModelStringUtil.getStringReturnEmptyString(str,20-chnCharNum1, 27-chnCharNum1).trim();
			int chnCharNum2=ODMModelStringUtil.getChineseCharNum(tem);
			
			//to bus name
			strAry[6] = ODMModelStringUtil.getStringReturnEmptyString(str,20-chnCharNum1, 27-chnCharNum1-chnCharNum2).trim();
			
			//--- replace all the Chinese Characters, since they are not used in the following processing.
			String str2=ODMModelStringUtil.replaceChineseChar(str);
			
			
			// to rated v
			strAry[7] = ODMModelStringUtil.getStringReturnEmptyString(str2,28, 31).trim();
			// controlled bus name and rated v
			strAry[8] = ODMModelStringUtil.getStringReturnEmptyString(str2,34, 41).trim();
			strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,42, 45).trim();
			
			//for R RV RQ RN
			//max tap
			strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,46, 50).trim();
			// min tap
			strAry[11] = ODMModelStringUtil.getStringReturnEmptyString(str2,51, 55).trim();
			// total tap
			strAry[12] = ODMModelStringUtil.getStringReturnEmptyString(str2,56, 57).trim();
			
			strAry[13] = ODMModelStringUtil.getStringReturnEmptyString(str2,58, 62).trim();
			strAry[14] = ODMModelStringUtil.getStringReturnEmptyString(str2,63, 67).trim();
		}catch(Exception e){
			ODMLogger.getLogger().severe(e.toString());
		}
		return strAry;
    }
}

