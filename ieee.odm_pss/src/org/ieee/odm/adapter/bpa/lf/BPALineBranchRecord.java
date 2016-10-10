/*
 * @(#)BPALineBranchRecord.java   
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
package org.ieee.odm.adapter.bpa.lf;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.LengthUnitType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.VoltageXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.interpss.numeric.datatype.Unit.UnitType;

public class BPALineBranchRecord {
	public static int LCardNO;
	public static int LPCardNO;
	public void processBranchData(final String str,	BaseAclfModelParser<? extends NetworkXmlType> parser)  throws ODMException {	
		final double baseMVA = parser.getNet().getBasePower().getValue();
		// symmetry line data
		if(str.startsWith("L ")){
			LCardNO++;
			// parse the branch input line str
			final String[] strAry = getBranchDataFields(str);
			
			// symetry  branch
			final String branchType=strAry[0];

			final String modCode =strAry[1];
			final String owner=strAry[2];
			
			final String fname =  strAry[3];
			final String tname =  strAry[6];
			double fVol=0.0;
			double tVol=0.0;
			if(!strAry[4].equals("")){
				fVol= new Double(strAry[4]).doubleValue();
			}
			if(!strAry[7].equals("")){
				tVol= new Double(strAry[4]).doubleValue();
			}
			final String fid =  BPABusRecord.getBusId(fname+fVol);
			final String tid =  BPABusRecord.getBusId(tname+tVol);
			ODMLogger.getLogger().fine("Branch data loaded, from-Bus, to-Bus: " + fid + ", " + tid);
			
			// set cirId, if not specified, set to 1
			//TODO change 1->0, since one uses "1" while CirId for the other is missing for some parallel branches in BPA
			String cirId="1";
			if(!strAry[8].equals("")){
				cirId = strAry[8];
			}
			LineBranchXmlType branchRec = null;
			try {
				branchRec = (LineBranchXmlType) parser.createLineBranch(fid, tid, cirId);
			} catch (Exception e) {
				ODMLogger.getLogger().severe("branch data error, " + e.toString() + 
						"  " + fname + "->" + tname + "_" + cirId);
				return;
			}
			
			// TODO owner code
			
			
						
			branchRec.setId(ODMModelStringUtil.formBranchId(fid, tid, cirId));			
			branchRec.setName(fname+fVol+" to "+tname+tVol);
			BPALoadflowRecord.n++; 
			branchRec.setNumber(BPALoadflowRecord.n);
			String multiSectionId="";
			if(!strAry[9].equals("")){
				multiSectionId = strAry[9];
				//set multiSection data if necessary
				// TODO
			}			
			
			//if currentRating!=0.0,set rated current
			double currentRating=0.0;
			if(!strAry[10].equals("")){
				currentRating = new Double(strAry[10]).doubleValue();
				AclfDataSetter.setBranchRatingLimitData(branchRec.getRatingLimit(),
						currentRating, CurrentUnitType.AMP);
				
			}	
			
			/*
			 * Set branch r,x,b
			 * ================
			 */
			double rpu=0.0, xpu=0.0001, halfGpu=0.0, halfBpu=0.0;
			if(!strAry[12].equals("")){
				rpu = new Double(strAry[12]).doubleValue();
				/* it is due to  the data storage fomat, for example , when it stores 000123, it is ,in fact, 1.23E-3
					fix digital points by default five				 */
				if(Math.abs(rpu)>=1.0&&!strAry[12].contains(".")){
					rpu=rpu/100000;
				}
				rpu=ODMModelStringUtil.getNumberFormat(rpu);
				if(Math.abs(rpu)>1)
					ODMLogger.getLogger().warning("Line#"+fname+"-to-"+tname+",the resistance now is"
							+rpu+" ,seems to be out of normal range[0~1.0]pu, please check!");
			}
			
			if(!strAry[13].equals("")){
				xpu = new Double(strAry[13]).doubleValue();
				if(Math.abs(xpu)>=1.0&&!strAry[13].contains(".")){
					xpu=xpu/100000;
				}
				xpu=ODMModelStringUtil.getNumberFormat(xpu);
				if(Math.abs(xpu)>1||Math.abs(xpu)<1E-5)
					ODMLogger.getLogger().warning("Line#"+fname+"-to-"+tname+",the reactance now is"
							+xpu+" ,seems to be out of normal range[1E-5~1]pu, please check!");	
			}
			
			if(!strAry[14].equals("")){
				halfGpu = new Double(strAry[14]).doubleValue();
				if(Math.abs(halfGpu)>=1.0&&!strAry[14].contains(".")){
					halfGpu=halfGpu/100000;
				}
				if(Math.abs(halfGpu)>1)
					ODMLogger.getLogger().warning("Line#"+fname+"-to-"+tname+",the line charging G/2 now is"
							+halfGpu+" ,seems to be out of normal range[0,1]pu, please check!");
			}
			
			if(!strAry[15].equals("")){
				halfBpu = new Double(strAry[15]).doubleValue();
				if(Math.abs(halfBpu)>=1.0&&!strAry[15].contains(".")){
					halfBpu=halfBpu/100000;
					
				}
				if(Math.abs(halfBpu)>5){
					ODMLogger.getLogger().warning("Line#"+fname+"-to-"+tname+",the line charging B/2 now is"
							+halfBpu+" ,seems to be out of normal range[-5,+5](pu), please check!");
				}
			}
			if(rpu!=0.0||xpu!=0.0||halfGpu!=0.0||halfBpu!=0.0){
				AclfDataSetter.setLineData(branchRec, rpu, xpu,
						ZUnitType.PU, 2*halfGpu, 2*halfBpu, YUnitType.PU);;
			}
   		    
			//branch length
			double length=0.0;
			if(!strAry[16].equals("")){
				AclfDataSetter.setLineLength(branchRec.getLineInfo(), 
						length, LengthUnitType.MILE);
			}			

			// if there is a description, set
			String desc= "";
			if(!strAry[17].equals("")){
				desc= strAry[17];
				BaseJaxbHelper.addNVPair(branchRec, "branch description", desc);
				if(desc.equals("ÁãÖ§Â·")){
					
				}
			}			
		}
		/**
		 * transform the shunt reactor at the end(s) of a branch to a shuntY, 
		 * and add it to the corresponding bus of the branch.
		 */
		else if(str.startsWith("L+")){
			LPCardNO++;
			final String[] strAry = getBranchDataFields(str);
			final String fname =  strAry[3];
			final String tname =  strAry[6]; 
			double fVol=0.0;
			double tVol=0.0;
			if(!strAry[4].equals("")){
				fVol= new Double(strAry[4]).doubleValue();
			}
			if(!strAry[7].equals("")){
				tVol= new Double(strAry[4]).doubleValue();
			}
			final String fid =  BPABusRecord.getBusId(fname+fVol);
			final String tid =  BPABusRecord.getBusId(tname+tVol);
			if(!strAry[9].equals("")){
				final double fromShuntMVar=new Double(strAry[9]).doubleValue();
				
				if(fromShuntMVar!=0.0){
					LoadflowBusXmlType fromBus= (LoadflowBusXmlType)parser.getBus(fid);
					/*
					 * It should be negative considering that positive sign means capacitive shunt var
					 * 
					 */
					AclfDataSetter.addBusShuntVar(fromBus, -fromShuntMVar, YUnitType.MVAR);   
				}
			}
			if(!strAry[10].equals("")){
				final double toShuntMVar=new Double(strAry[10]).doubleValue();
				if(toShuntMVar!=0.0){
					LoadflowBusXmlType toBus= (LoadflowBusXmlType)parser.getBus(tid);
					AclfDataSetter.addBusShuntVar(toBus, -toShuntMVar, YUnitType.MVAR);
				}
			}
		}
		else{
			throw new ODMException("Only type L or L+ branch is allowed");
		}
	}	
	


	private static String[] getBranchDataFields(final String str) {
		final String[] strAry = new String[20];
		// line type
		strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
		// change
        strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
        //ower
		strAry[2] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
		
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
		
		//to bus baseKV
		strAry[7] = ODMModelStringUtil.getStringReturnEmptyString(str2,28, 31).trim();
		// circuit ID
		strAry[8] = ODMModelStringUtil.getStringReturnEmptyString(str2,32, 32).trim();
		
		if(strAry[0].equals("L")){ 
		  // no use
		  strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,33, 33).trim();
		  // current rating(ampacity)
		  strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,34, 37).trim();
		  // no use
		  strAry[11] = ODMModelStringUtil.getStringReturnEmptyString(str2,38, 38).trim();
		  // R
		  strAry[12] = ODMModelStringUtil.getStringReturnEmptyString(str2,39, 44).trim();
		  // X
		  strAry[13] = ODMModelStringUtil.getStringReturnEmptyString(str2,45, 50).trim();
		  //G/2
		  strAry[14] = ODMModelStringUtil.getStringReturnEmptyString(str2,51, 56).trim();
		  //B/2
		  strAry[15] = ODMModelStringUtil.getStringReturnEmptyString(str2,57, 62).trim();
	    }
		else if(strAry[0].equals("L+")){ //processing Branch Shunt Var Data Fields
			
			if(str2.length()>38){// get the shunt Var at the fromBus side
			     strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,34, 38).trim();
			} else
				 strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,34, str2.length()).trim();
			// there is shunt Var at the toBus side
			if(str2.length()>48)
			   strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,44, 48).trim();
			else strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,44, str2.length()).trim();
		}
		if(strAry[0].equals("L")){
			//line length
			strAry[16] = ODMModelStringUtil.getStringReturnEmptyString(str2,63, 66).trim();
			strAry[17] = ODMModelStringUtil.getStringReturnEmptyString(str2,67, 74).trim();
		}else{
			strAry[16] = ODMModelStringUtil.getStringReturnEmptyString(str2,63, 67).trim();
			strAry[17] = ODMModelStringUtil.getStringReturnEmptyString(str,69-chnCharNum1, 74-chnCharNum1).trim();
		}
		// date of first put into use
		strAry[18] = ODMModelStringUtil.getStringReturnEmptyString(str2,75, 77).trim();	
		// date of out of service
		strAry[19] = ODMModelStringUtil.getStringReturnEmptyString(str2,78, 80).trim();		
//         
		return strAry;
    }
//	private static String[] getBranchShuntVarDataFields(String str) {
//		final String[] strAry = new String[8];
//		// Branch record type
//		strAry[0] = ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
//		// from bus name and basekV
//		strAry[1] = ModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
//		strAry[2] = ModelStringUtil.getStringReturnEmptyString(str,15, 18).trim();
//		// to bus name and basekV
//		strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,20, 27).trim();
//		strAry[4] = ModelStringUtil.getStringReturnEmptyString(str,28, 31).trim();
//		strAry[5] = ModelStringUtil.getStringReturnEmptyString(str,32, 32).trim();
//		// shunt Var at the fromBus side
//		if(str.length()>38)
//		     strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,34, 38).trim();
//		else
//			 strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,34, str.length()).trim();
//		// shunt Var at the toBus side
//		if(str.length()>44){
//		   strAry[7] = ModelStringUtil.getStringReturnEmptyString(str,44, str.length()).trim();
//		}
//		return strAry;
//	}
}

//else if(str.startsWith("E")){
//final String[] strAry = getBranchDataFields(str,adapter);
//// symetry  branch
//final String branchType=strAry[0];
//
//final String modCode =strAry[1];
//final String owner=strAry[2];
//
//final String fid = strAry[3];
//final String tid = strAry[6];
//ODMLogger.getLogger().fine("Branch data loaded, from-Bus, to-Bus: " + fid + ", " + tid);
//
//if(!fid.equals("")){
//	branchRec.addNewFromBus().setIdRef(fid);
//}
//if(!tid.equals("")){
//	branchRec.addNewToBus().setIdRef(tid);
//}
//
//double fVol=0.0;
//double tVol=0.0;
//if(!strAry[4].equals("")){
//	fVol= new Double(strAry[4]).doubleValue();
//}
//if(!strAry[7].equals("")){
//	tVol= new Double(strAry[4]).doubleValue();
//}			
// measure location for power interchange, 1--from side, 2- to side
//set transfer power measured location in tie line data 
//int measureLocation=0;
//if(!strAry[5].equals("")){
//	measureLocation= new Integer(strAry[5]).intValue();
//	try{
//		if(measureLocation==1){
//			PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();
//
//			// set tieline data
//			tieLine.addNewMeteredBus().setName(fid);
//			tieLine.addNewNonMeteredBus().setName(tid);	
//			
//			BusRecordXmlType busRecFrom=XBeanParserHelper.findBusRecord(fid, baseCaseNet);
//			busRecFrom.getZoneNumber();
//			NetAreaXmlType areaFrom=XBeanParserHelper.
//			  getAreaRecordByZone(busRecFrom.getZoneNumber(), baseCaseNet);
//			tieLine.setMeteredArea(areaFrom.getName());
//			
//			BusRecordXmlType busRecTo=XBeanParserHelper.findBusRecord(tid, baseCaseNet);
//			busRecTo.getZoneNumber();
//			NetAreaXmlType areaTo=XBeanParserHelper.
//			  getAreaRecordByZone(busRecTo.getZoneNumber(), baseCaseNet);
//			tieLine.setNonMeteredArea(areaTo.getName());
//			// to do: set area number
//			
//		}else{
//			PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();
//
//			tieLine.addNewMeteredBus().setName(tid);
//			tieLine.addNewNonMeteredBus().setName(fid);					
//			XBeanParserHelper.findBusRecord(fid, baseCaseNet).getZoneNumber();
//			
//			BusRecordXmlType busRecFrom=XBeanParserHelper.findBusRecord(tid, baseCaseNet);
//			busRecFrom.getZoneNumber();
//			NetAreaXmlType areaFrom=XBeanParserHelper.
//			  getAreaRecordByZone(busRecFrom.getZoneNumber(), baseCaseNet);
//			tieLine.setMeteredArea(areaFrom.getName());
//			
//			BusRecordXmlType busRecTo=XBeanParserHelper.findBusRecord(fid, baseCaseNet);
//			busRecTo.getZoneNumber();
//			NetAreaXmlType areaTo=XBeanParserHelper.
//			  getAreaRecordByZone(busRecTo.getZoneNumber(), baseCaseNet);
//			tieLine.setNonMeteredArea(areaTo.getName());						
//		}
//	}catch (final Exception e){
//		e.printStackTrace();
//	}
//	
//}

//// set cirId, if not specified, set to 1
//String cirId="";
//if(!strAry[8].equals("")){
//	cirId = strAry[8];
//	branchRec.setCircuitId(cirId);
//}
//else{
//	branchRec.setCircuitId("1");
//}			
//
//LoadflowBranchDataXmlType branchData = branchRec.addNewLoadflowData();
//
//branchRec.setId(ModelStringUtil.formBranchId(fid, tid, cirId));			
//branchData.setCode(LFBranchCodeEnumType.LINE);
//
//String multiSectionId="";
//if(!strAry[9].equals("")){
//	multiSectionId = strAry[9];
//	//set multiSection data if necessary
//}			
//
////if currentRating!=0.0,set rated current
//double currentRating=0.0;
//if(!strAry[10].equals("")){
//	currentRating = new Double(strAry[10]).doubleValue();
//	XBeanDataSetter.setBranchRatingLimitData(branchData.addNewBranchRatingLimit(), 
//			currentRating, CurrentUnitType.AMP);
//}			 
//double rpu=0.0, xpu=0.0001, G1pu=0.0, B1pu=0.0, G2pu=0.0, B2pu=0.0;
//if(!strAry[12].equals("")){
//	rpu = new Double(strAry[12]).doubleValue();
//}
//if(!strAry[13].equals("")){
//	xpu = new Double(strAry[13]).doubleValue();
//}
//if(!strAry[14].equals("")){
//	G1pu = new Double(strAry[14]).doubleValue();
//}
//if(!strAry[15].equals("")){
//	B1pu = new Double(strAry[15]).doubleValue();
//}
//if(!strAry[16].equals("")){
//	G2pu = new Double(strAry[16]).doubleValue();
//}
//if(!strAry[17].equals("")){
//	B2pu = new Double(strAry[17]).doubleValue();
//}
//ZXmlType z= branchData.addNewZ();
//XBeanDataSetter.setZValue(z, rpu, xpu, ZUnitType.PU);
//YXmlType y1 = branchData.addNewFromShuntY();
//YXmlType y2 = branchData.addNewToShuntY();
//XBeanDataSetter.setYData(y1, G1pu, B1pu, YUnitType.PU);
//XBeanDataSetter.setYData(y2, G2pu, B2pu, YUnitType.PU); 			
//}


