/*
 * @(#)BPADcLineBusRecord.java   
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

public class BPADcLineBusRecord {
	/*	
	public static void processDCLineBusData(final String str,final DCLineBusRecordXmlType dcBus,  
			BPAAdapter adapter) {
		final String[] strAry= getDCLineBusDataFields(str,adapter);
		
		final String dataType= strAry[0];
		final String modCode=  strAry[1];
		final String owner = strAry[2];
		final String converterBus =strAry[3];
		double converterACSideVoltage =0.0;
		if(!strAry[4].equals("")){
			converterACSideVoltage = new Double(strAry[4]).doubleValue();
		}
		String zone="";
		if(!strAry[5].equals("")){
			zone = strAry[5];
		}
		int brdgsPerBrckt=0;
		if(!strAry[6].equals("")){
			brdgsPerBrckt = new Integer(strAry[6]).intValue();
		}
		double smoothInductance=0.0;
		if(!strAry[7].equals("")){
			smoothInductance =new Double(strAry[7]).doubleValue();
		}
		// suppose the frequence is 50 Hz;
		double smoothReactance=2*3.14*0.02*smoothInductance*0.001;
		double converterMinFiringAngle=0.0;
		if(!strAry[8].equals("")){
			converterMinFiringAngle= new Double(strAry[8]).doubleValue();
		}
		double inverterMaxFiringAngle=0.0;
		if(!strAry[9].equals("")){
			inverterMaxFiringAngle= new Double(strAry[9]).doubleValue();
		}
		double valveDropVoltage=0.0;   // in v
		if(!strAry[10].equals("")){
			valveDropVoltage= new Double(strAry[10]).doubleValue();
		}
		double brdgesCurrentRating=0.0;   // in amps
		if(!strAry[11].equals("")){
			brdgesCurrentRating= new Double(strAry[11]).doubleValue();
		}
		String commutatingBus="";
		double commutatingBusDCSideVol =0.0;
		if(!strAry[12].equals("")){
			commutatingBus =strAry[12];
		}
		if(!strAry[13].equals("")){
			commutatingBusDCSideVol= new Double(strAry[13]).doubleValue();
		}
		
		ConverterXmlType converter= dcBus.addNewConverter();
		// set converter bus id
		converter.addNewBusId().setName(converterBus);
		
		// set converter ac side voltage
		DataSetter.setVoltageData(converter.addNewAcSideRatedVoltage(), 
				converterACSideVoltage, VoltageUnitType.KV);
		// bridges
		converter.setNumberofBridges(brdgsPerBrckt);
			
		//set min firing angle as a converter
		DataSetter.setAngleData(converter.addNewMinFiringAngle(),
				converterMinFiringAngle, AngleUnitType.DEG);
		//set max firing angle as a inverter
		DataSetter.setAngleData(converter.addNewMaxFiringAngle(),
				inverterMaxFiringAngle, AngleUnitType.DEG);
		
	}
*/	
//	private static String[] getDCLineBusDataFields(final String str) {
//		final String[] strAry = new String[14];
//		
//		try{
//			//Columns  1- 2   Bus type
//		    strAry[0] = ModelStringUtil.getStringReturnEmptyString(str,1, 2);			
//			//Columns  3 code for modification			
//			strAry[1] = ModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
//			//Columns 3-5   owner code
//			strAry[2] = ModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
//			
//			//Columns 6-13 busName  14-17 rated voltage			
//			strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
//			strAry[4] = ModelStringUtil.getStringReturnEmptyString(str,15, 18).trim();
//			//Columns 18-19   zone name
//			strAry[5] = ModelStringUtil.getStringReturnEmptyString(str,19, 20).trim();
//
//			//bridge per brckt
//			strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,24, 25).trim();
//			//smooth reactor
//			strAry[7] = ModelStringUtil.getStringReturnEmptyString(str,26, 30).trim();			
//			
//			strAry[8] =ModelStringUtil.getStringReturnEmptyString(str,31, 35).trim();
//			
//			strAry[9] = ModelStringUtil.getStringReturnEmptyString(str,36, 40).trim();			
//			// Columns 38-41 pmax
//			// Columns 42-46 pmax
//			strAry[10] = ModelStringUtil.getStringReturnEmptyString(str,41, 45).trim();
//			strAry[11] = ModelStringUtil.getStringReturnEmptyString(str,46, 50).trim();		
//			//Qmax Qmin
//			strAry[12]= ModelStringUtil.getStringReturnEmptyString(str,51, 58).trim();
//			strAry[13]= ModelStringUtil.getStringReturnEmptyString(str,59, 62).trim();
//			
//		}catch (Exception e){
//			ODMLogger.getLogger().severe("This DCLine bus data is not filled completely:   "+str);
//		}		
//		return strAry;
//	}	
}
	
	
