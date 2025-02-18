/*
 * @(#)DcLineBranchRecord.java   
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

public class BPADcLineBranchRecord {
/*	
	public static void processDCLineBranchData(final String str, 
			final DCLineBranchRecordXmlType dcBranch, 
			ODMModelParser parser,
			final PSSNetworkXmlType baseCaseNet,BPAAdapter adapter){
		final String strAry[] = getDCLineBranchDataFields(str,adapter);	
		
		final String dataType= strAry[0];
		final String modCode= strAry[1];
		final String owner = strAry[2];
		final String rectifierBus = strAry[3];
		double rectifierRatedVoltage = 0.0;
		if(!strAry[4].equals("")){
			rectifierRatedVoltage= new Double(strAry[4]).doubleValue();			
		}
		dcBranch.setRectifierBus(rectifierBus);
		
		
		final String inverterBus = strAry[6];
		double inverterRatedVoltage = 0.0;
		if(!strAry[7].equals("")){
			inverterRatedVoltage= new Double(strAry[7]).doubleValue();			
		}
		dcBranch.setInverterBus(inverterBus);
		
		int measureLocation=0;
		if(!strAry[5].equals("")){
			measureLocation = new Integer(strAry[5]).intValue();
			// add to tieline data
			try{
				if(measureLocation==1){
					// set tieline data
					PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();

					tieLine.addNewMeteredBus().setName(rectifierBus);
					tieLine.addNewNonMeteredBus().setName(inverterBus);	
					
					// **** DCLineBusRecordXmlType busRecFrom=ParserHelper.getDCLineBusRecord(rectifierBus, baseCaseNet);					
					/*NetAreaXmlType areaFrom=ContainerHelper.
						  getAreaRecordByZone(busRecFrom.getConverter().getData().getZoneNumber(), baseCaseNet);
					tieLine.setMeteredArea(areaFrom.getName());
					DCLineBusRecordXmlType busRecTo=ContainerHelper.getDCLineBusRecord(inverterBus, baseCaseNet);
										
					NetAreaXmlType areaTo=ContainerHelper.
						  getAreaRecordByZone(busRecTo.getConverter().getData().getZoneNumber(), baseCaseNet);
					tieLine.setNonMeteredArea(areaTo.getName());*/
					
					
					// to do: set area number
/*					
				}
			else{
					PSSNetworkXmlType.TieLineList.Tieline tieLine=parser.addNewBaseCaseTieline();

					tieLine.addNewMeteredBus().setName(inverterBus);
					tieLine.addNewNonMeteredBus().setName(rectifierBus);					
					ParserHelper.findBusRecord(rectifierBus, baseCaseNet).getZoneNumber();
					
					BusRecordXmlType busRecFrom=ParserHelper.findBusRecord(inverterBus, baseCaseNet);
					busRecFrom.getZoneNumber();
					NetAreaXmlType areaFrom=ParserHelper.
					  getAreaRecordByZone(busRecFrom.getZoneNumber(), baseCaseNet);
					tieLine.setMeteredArea(areaFrom.getName());
					
					BusRecordXmlType busRecTo=ParserHelper.findBusRecord(inverterBus, baseCaseNet);
					busRecTo.getZoneNumber();
					NetAreaXmlType areaTo=ParserHelper.
					  getAreaRecordByZone(busRecTo.getZoneNumber(), baseCaseNet);
					tieLine.setNonMeteredArea(areaTo.getName());						
				}
			}catch (final Exception e){
				e.printStackTrace();
			}
		}
		
		
		double lineRatingCurrent=0.0;
		if(!strAry[8].equals("")){
			lineRatingCurrent = new Double(strAry[8]).doubleValue();
			DataSetter.setCurrentData(dcBranch.getData().addNewMaxCurrent(),
					lineRatingCurrent, CurrentUnitType.AMP);
		}
		double r=0.0, l=0.0,c=0.0;
		double x=0;
		final double w= 2*3.14*0.02;
		if(!strAry[9].equals("")){
			r = new Double(strAry[9]).doubleValue();
		}
		if(!strAry[10].equals("")){
			l = new Double(strAry[10]).doubleValue();
		}
		if(!strAry[11].equals("")){
			c = new Double(strAry[11]).doubleValue();
		}
		// line reactance  x=XL-XC=2*pi*f*l-1/(2*Pi*f*c)
		if(x!=0.0&&c==0.0){
			x= w*l*0.001;
		}else if(c!=0.0){
			x= w*l*0.001-1/(w*c*0.000001);
		}		
		
		if(r!=0.0||x!=0.0){
			DataSetter.setZValue(dcBranch.getData().addNewLineZ(), r, x, ZUnitType.OHM);
		}
		String mwControlSide="";
		if(!strAry[12].equals("")){
			mwControlSide =strAry[12];
			if(mwControlSide.equals("R")){				
				dcBranch.getData().setControlOnRectifierSide(true);
			}else{				
				dcBranch.getData().setControlOnRectifierSide(false);
				}
		}		
		// DC power control mode, and control power
		double scheduledMw=0.0;
		if(!strAry[13].equals("")){
			scheduledMw =new Double(strAry[13]).doubleValue();
//			dcBranch.getData().setControlMode(DCLineDataXmlType.ControlMode.POWER);
			DataSetter.setPowerData(dcBranch.getData().addNewPowerDemand(), 
					scheduledMw, 0.0, ApparentPowerUnitType.MVA);
		}
		
	/*	double dcLineRatedVoltage=0.0;
		if(!strAry[14].equals("")){
			dcLineRatedVoltage = new Double(strAry[14]).doubleValue();
			DataSetter.setVoltageData(dcBranch.getData().addNewRatedDVol(),
					dcLineRatedVoltage, VoltageUnitType.KV);			
		}*/
		
		/* TODO - the following part has compiling error
		double recOperFiringAngle=0.0, invStopFiringAngle=0.0;
		if(!strAry[15].equals("")){
			recOperFiringAngle= new Double(strAry[15]).doubleValue();
			DataSetter.setAngleData(ContainerHelper.getConverterRecord(rectifierBus,
					baseCaseNet).getData().addNewRectifierMaxFiringAngle(), recOperFiringAngle, AngleUnitType.DEG);
			ContainerHelper.getConverterRecord(rectifierBus,baseCaseNet).
			        setType(ConverterXmlType.Type.RECTIFIER);
		}
		if(!strAry[16].equals("")){
			invStopFiringAngle= new Double(strAry[16]).doubleValue();
			DataSetter.setAngleData(ContainerHelper.getConverterRecord(inverterBus,
					baseCaseNet).getData().addNewInverterMinFiringAgnle(),invStopFiringAngle, AngleUnitType.DEG);
			ContainerHelper.getConverterRecord(rectifierBus,baseCaseNet).
	        setType(ConverterXmlType.Type.INVERTER);
		}
		*/
/*		
		double length=0.0;
		if(!strAry[17].equals("")){
			length= new Double(strAry[17]).doubleValue();
			dcBranch.getData().addNewLength().setValue(length);
			dcBranch.getData().getLength().setUnit(LengthUnitType.MILE);
		}
		
	}	
*/	
	
//	private static String[] getDCLineBranchDataFields(final String str) {
//		final String[] strAry = new String[20];
//		try{
//			strAry[0] = ModelStringUtil.getStringReturnEmptyString(str,1, 2);
//            strAry[1] = ModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
//			strAry[2] = ModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
//			
//			strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
//			strAry[4] = ModelStringUtil.getStringReturnEmptyString(str,15, 18).trim();
//			strAry[5] = ModelStringUtil.getStringReturnEmptyString(str,19, 19).trim();
//			strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,20, 27).trim();
//
//			strAry[7] = ModelStringUtil.getStringReturnEmptyString(str,28, 31).trim();
//			strAry[8] = ModelStringUtil.getStringReturnEmptyString(str,34, 37).trim();
//			strAry[9] = ModelStringUtil.getStringReturnEmptyString(str,38, 43).trim();
//			strAry[10] = ModelStringUtil.getStringReturnEmptyString(str,44, 49).trim();
//			strAry[11] = ModelStringUtil.getStringReturnEmptyString(str,50, 55).trim();
//			strAry[12] = ModelStringUtil.getStringReturnEmptyString(str,56, 56).trim();
//			strAry[13] = ModelStringUtil.getStringReturnEmptyString(str,57, 61).trim();
//			strAry[14] = ModelStringUtil.getStringReturnEmptyString(str,62, 66).trim();
//			strAry[15] = ModelStringUtil.getStringReturnEmptyString(str,67, 70).trim();
//			strAry[16] = ModelStringUtil.getStringReturnEmptyString(str,71, 74).trim();
//			
//			strAry[17] = ModelStringUtil.getStringReturnEmptyString(str,75, 78).trim();
//		}catch(Exception e){
//			ODMLogger.getLogger().severe(e.toString());
//		}
//		return strAry;
//    }
}

