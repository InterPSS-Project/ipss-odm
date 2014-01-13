/*
 * @(#)BPABusRecord.java   
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

import java.util.Hashtable;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;

public class BPABusRecord<
					TNetXml extends NetworkXmlType, 
					TBusXml extends BusXmlType,
					TLineXml extends BranchXmlType,
					TXfrXml extends BranchXmlType,
					TPsXfrXml extends BranchXmlType> {
	private static final int swingBus=1;
	private static final int pqBus=2;
	private static final int pvBus=3;		
	private static final int pvBusNoQLimit=4;
	private static final int supplementaryBusInfo=5;
	
	/*
	 *  BPA data format does not have bus number, only has bus name. 
	 *  Bus number is generated and a looupTable for busName -> BusId
	 */
	private static long busCnt = 0;
	private static Hashtable<String,String> busIdLookupTable = null;
	
	/**
	 * reset the bus count and lookup table
	 */
	public static void resetBusCnt() { 
		busCnt = 0; 
		busIdLookupTable = new Hashtable<String,String>();
	}
	/**
	 * get bus Id and add an item to the lookup table for busName -> busId
	 * 
	 * @param busName
	 * @return
	 */
	private static String createBusId(String busName) { 
		String id = IODMModelParser.BusIdPreFix + ++busCnt;
		busIdLookupTable.put(busName.trim(), id);
		return id;
	}
	/**
	 * get busId from busName using the lookup table
	 * 
	 * @param busName
	 * @return
	 */
	public static String getBusId(String busName) throws ODMException { 
		String id =  busIdLookupTable.get(busName.trim()); 
		if (id == null) {
			throw new ODMException("Bus id not found, bus name: " + busName);
		}
		return id; 
	}
	
	public void processBusData(final String str, BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> parser) throws Exception {		
		final double baseMVA = parser.getNet().getBasePower().getValue();

		// parse the input data line
		final String[] strAry = getBusDataFields(str);
		
        //busType	
		int busType=0;
		String stemp = strAry[0];
		if(stemp.equals("B")||stemp.equals("BC")||stemp.equals("BT")||stemp.equals("BV")){
			busType=pqBus;
		}
		else if(stemp.equals("BQ")||stemp.equals("BG")||stemp.equals("BF")){
			busType=pvBus;
		}
		else if(stemp.equals("BE")){
			busType=pvBusNoQLimit;
		}
		else if(stemp.equals("BS")){
			busType=swingBus;
		}
		else if (stemp.equals("+")){
			busType=supplementaryBusInfo;
		}
		
		/*
		 * set bus record attributes
		 * =========================
		 */
		
		// modification code
		final String modCode=strAry[1];
		//owner code
		final String ownerName=strAry[2];
		//Name
		final String busName = strAry[3];


		LoadflowBusXmlType busRec = null;
		
	    if(busType==pqBus||busType==pvBus||busType==pvBusNoQLimit||busType==swingBus){
		    final String busId =  createBusId(busName);
			ODMLogger.getLogger().fine("Bus data loaded, busName: " + busId);	
		try {
			busRec = (LoadflowBusXmlType)parser.createBus(busId);
			busRec.setName(busName);
		} catch (ODMException e) {
			ODMLogger.getLogger().severe(e.toString());
			return;
		}		
		
		busRec.setId(busId);		
		busRec.setName(busName);
		//Add bus number, according to input sequence
		busRec.setNumber(busCnt);
		
		// TODO set bus owner
        //busRec.getOwnerList().getOwner().get(0).setName(ownerName);
		//basekv
		double baseKv=ODMModelStringUtil.getDouble(strAry[4], 100.0);
		busRec.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv, VoltageUnitType.KV));

		// TODO area name??
		//busRec.setAreaName(value);
		//zone name
		final String zoneName= strAry[5];
		busRec.setZoneName(zoneName);		
		
		/*
		 * Parse Loadflow data
		 * ===================
		 */
		
		//load mw and mvar
		double loadMw=ODMModelStringUtil.getDouble(strAry[6], 0.0);
		double loadMvar=ODMModelStringUtil.getDouble(strAry[7], 0.0);
		//Shunt mw--> G 
		//Shunt var B -->B
		double shuntMw=ODMModelStringUtil.getDouble(strAry[8], 0.0);
		double shuntVar=ODMModelStringUtil.getDouble(strAry[9], 0.0);	       
		final double g=ODMModelStringUtil.getNumberFormat(shuntMw/baseMVA);
		final double b=ODMModelStringUtil.getNumberFormat(shuntVar/baseMVA);

		// set pGenMax
		double pGenMax=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		double pGen=ODMModelStringUtil.getDouble(strAry[11], 0.0);	
		
		// qGen for PQ bus, qGenMax for PV bus
		double qGenOrQGenMax=ODMModelStringUtil.getDouble(strAry[12], 0.0);
		double qGenMin=ODMModelStringUtil.getDouble(strAry[13], 0.0);
		
		//for swing bus, this value is vpu, for others it is vpuorvmax.
		double vpu=ODMModelStringUtil.getDouble(strAry[14], 0.0);
		if(!strAry[14].contains(".")){
			vpu/=1000;
		}
		//for swing bus, this value is angle(degrees), for others it is vmin.
		double vMinOrAngDeg=ODMModelStringUtil.getDouble(strAry[15], 0.0);
		if(!strAry[15].contains(".")){
			vMinOrAngDeg/=1000;
		}	
		
		double varSupplied=ODMModelStringUtil.getDouble(strAry[18], 0.0);
		
		/*
		 * process data and map to the ODM bus record
		 * ==========================================
		 */
		//if(loadMw != 0.0 || loadMvar != 0.0 || 
			//	pGen!=0.0|| qGenOrQGenMax!=0.0 ||vpu!=0.0||
			//	vMinOrAngDeg!=0.0||pGenMax!=0.0
			//	||g!=0||b!=0) {
			// set G B
			if (g != 0.0 || b != 0.0) {
				busRec.getShuntYData().setEquivY(BaseDataSetter.createYValue(g, b,YUnitType.PU));
			}	
			
			// set load
			if (loadMw != 0.0 || loadMvar != 0.0) {
				AclfDataSetter.setLoadData(busRec,
						LFLoadCodeEnumType.CONST_P, loadMw,
						loadMvar, ApparentPowerUnitType.MVA);
			}
			
			LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(busRec.getGenData());
			if(busType==swingBus){
				// set bus voltage
					busRec.setVoltage(BaseDataSetter.createVoltageValue(vpu, VoltageUnitType.PU));
				// set bus angle
				busRec.setAngle(BaseDataSetter.createAngleValue(vMinOrAngDeg, AngleUnitType.DEG));
				
				//set gen data
				AclfDataSetter.setGenData(busRec,
							LFGenCodeEnumType.SWING,
							vpu, VoltageUnitType.PU,
							vMinOrAngDeg, AngleUnitType.DEG,pGen,0, ApparentPowerUnitType.MVA);
				// set Q limit
				if(qGenOrQGenMax!=0.0||qGenMin!=0.0){
					defaultGen.setQLimit(BaseDataSetter.createReactivePowerLimit( 
							qGenOrQGenMax, qGenMin, ReactivePowerUnitType.MVAR));				
				}
				
				// set P limit
				if(pGenMax!=0.0){
					defaultGen.setPLimit(BaseDataSetter.createActivePowerLimit(
							pGenMax, 0, ActivePowerUnitType.MW));
				}	
			}
			else if(busType==pqBus){			
				AclfDataSetter.setGenData(busRec, LFGenCodeEnumType.NONE_GEN);
				if(pGen!=0.0||qGenOrQGenMax!=0.0){
					busRec.getGenData().setCode(LFGenCodeEnumType.PQ);
					defaultGen.setPower(BaseDataSetter.createPowerValue(
							pGen, qGenOrQGenMax, ApparentPowerUnitType.MVA));
			    // for a PQ Bus, it is not proper to set the Vlimit;
//				// set V limit    
//				if(vpu!=0 ||vMinOrAngDeg!=0){
//					    busRec.getGenData().getEquivGen()
//					    	.setVoltageLimit(BaseDataSetter.createVoltageLimit(
//					    			vpu, vMinOrAngDeg, VoltageUnitType.PU));
//					}
				if(pGen!=0.0&&vpu!=0){
						ODMLogger.getLogger().info("This bus seems to be a GenPV bus: "+ busId+","+busName
								+" ,please check! ");
					
				}
					
				}
			}
			else if(busType==pvBus || busType==pvBusNoQLimit){
				// set bus voltage
					busRec.setVoltage(BaseDataSetter.createVoltageValue(vpu, VoltageUnitType.PU));
				// set gen data
				AclfDataSetter.setGenData(busRec,
							LFGenCodeEnumType.PV, 
							vpu, VoltageUnitType.PU, 0.0, AngleUnitType.DEG,
							pGen, 0.0, ApparentPowerUnitType.MVA);
				// set Q limit
				if(qGenOrQGenMax!=0.0||qGenMin!=0.0){
					defaultGen.setQLimit(BaseDataSetter.createReactivePowerLimit( 
							qGenOrQGenMax, qGenMin, ReactivePowerUnitType.MVAR));	
				// for "BE" type the limit if disabled
					if (busType==pvBusNoQLimit)
						defaultGen.getQLimit().setActive(false);
					   //TODO BPA automatically balance the shuntVar at BE Type Bus, 
					   // considering Ipss does not support such function, set it  to zero here.
					  // AclfDataSetter.setBusShuntVar(busRec, 0, YUnitType.PU);
				}
				// set P limit
				if(pGenMax!=0.0){
					defaultGen.setPLimit(BaseDataSetter.createActivePowerLimit(
							pGenMax, 0, ActivePowerUnitType.MW));
				}	
				
			 }//end of PV bus data parsing
			
		    }//end of load flow data mapping
		
//	      }//end of normal type bus data processing.
		
			/*a bus recored starting with"+", is to supplement
			 info(e.g.,ZIP type load and generation) to an existing bus record with the same busId
			*/
			if(busType==supplementaryBusInfo){
				
				TBusXml Bus=parser.getBus(getBusId(busName));
				final String loadType=strAry[5];
				//loadType: *I or 01 for constI,  and *P or 02 for constP
				final double p=ODMModelStringUtil.getDouble(strAry[6], 0.0);
				final double q=ODMModelStringUtil.getDouble(strAry[7], 0.0);
				//TODO how to set constI type load
				
		        if(!strAry[9].equals("")||!strAry[8].equals("")){
					final double ShuntG=ODMModelStringUtil.getDouble(strAry[8], 0.0);
					final double ShuntB=ODMModelStringUtil.getDouble(strAry[9], 0.0);
					//System.out.println("Shunt G +B="+ShuntG+","+ShuntB);
					double re=ODMModelStringUtil.getNumberFormat(ShuntG/baseMVA); // x(pu)=Var/baseMVA
					double im=ODMModelStringUtil.getNumberFormat(ShuntB/baseMVA);
					if(re!=0.0||im!=0.0){
						AclfDataSetter.addBusShuntY((LoadflowBusXmlType)Bus, re, im, YUnitType.PU);	
					}
					//System.out.println("Im="+im+",Shunt B="+Bus.getShuntY().getIm());
				}
			}
				
			/*for BG and BX, controlled bus name and voltage
			 desired bus voltage is specified in strAry[14], equals to vpu
			 */
			final String controlledBus= strAry[16];
			double controlledBusRatedVol=ODMModelStringUtil.getDouble(strAry[17], 0.0);
			
			LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(busRec.getGenData());
			if(strAry[0].equals("BG")||strAry[0].equals("BX")){
				if(!controlledBus.equals("")) {			
					defaultGen.getRemoteVoltageControlBus().setIdRef(controlledBus);
					defaultGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(
							controlledBusRatedVol, VoltageUnitType.PU));
				}
			}
	}
	
	private static String[] getBusDataFields(final String str) throws Exception {
		final String[] strAry = new String[19];
/* sample data
B     XIANLS= 500.XX305.3 -215.                                                                      
*/
		//Columns  1- 2   Bus type
	    strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2); 
		//Columns  3 code for modification			
		strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
		//Columns 3-5   owner code
		
		strAry[2] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
		
		//to process the Chinese characters first, if any.
		String tem=ODMModelStringUtil.getStringReturnEmptyString(str,7, 14).trim();
		int chineseCharNum=ODMModelStringUtil.getChineseCharNum(tem);
		
		//Columns 6-13 busName  
		strAry[3] = ODMModelStringUtil.getStringReturnEmptyString(str,7, 14-chineseCharNum).trim();
		
		String str2=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
		//14-17 rated voltage
		strAry[4] = ODMModelStringUtil.getStringReturnEmptyString(str2,15, 18).trim();

		//Columns 18-19   zone name for Bus card, load type for complementary Bus card.
		strAry[5] = ODMModelStringUtil.getStringReturnEmptyString(str2,19, 20).trim();
		//Columns 21-25   Load MW [F] *
		//Columns 26-30   Load MVAR [F] *
		strAry[6] = ODMModelStringUtil.getStringReturnEmptyString(str2,21, 25).trim();
		strAry[7] = ODMModelStringUtil.getStringReturnEmptyString(str2,26, 30).trim();			
		//Columns 31-34   shunt MW [F] *
		//Columns 35-38   shunt MVAR [F] *
		strAry[8] = ODMModelStringUtil.getStringReturnEmptyString(str2,31, 34).trim();
		strAry[9] = ODMModelStringUtil.getStringReturnEmptyString(str2,35, 38).trim();	
		// Columns 38-41 pmax
		// Columns 42-46 pgen
		strAry[10] = ODMModelStringUtil.getStringReturnEmptyString(str2,39, 42).trim();			
		strAry[11] = ODMModelStringUtil.getStringReturnEmptyString(str2,43, 47).trim();
		//Qmax Qmin
		strAry[12]= ODMModelStringUtil.getStringReturnEmptyString(str2,48, 52).trim();
		strAry[13]= ODMModelStringUtil.getStringReturnEmptyString(str2,53, 57).trim();			
		//scheduled V or Vmax, Vmin
		strAry[14]= ODMModelStringUtil.getStringReturnEmptyString(str2,58, 61).trim();			
		strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str2,62, 65).trim();
		//remoted busName, rated voltage
		strAry[16]= ODMModelStringUtil.getStringReturnEmptyString(str2,66, 73).trim();
		strAry[17]= ODMModelStringUtil.getStringReturnEmptyString(str2,74, 77).trim();
		// used in remoted bus control, var fraction
		strAry[18]= ODMModelStringUtil.getStringReturnEmptyString(str2,78, 80).trim();
		return strAry;
	}
	
	
	
}
	
	
