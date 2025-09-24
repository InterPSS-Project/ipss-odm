/*
 * @(#)BPADynamicRecord.java   
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
 * @Author Stephen Hau
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.bpa.dynamic;
import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovBPAHydroTurbineGHXmlType;
import org.ieee.odm.schema.GovHydroSteamGeneralModelXmlType;
import org.ieee.odm.schema.SpeedGovBPAGSModelXmlType;
import org.ieee.odm.schema.SpeedGovBPAGiGaCombinedXmlType;
import org.ieee.odm.schema.SpeedGovBPARegGIModelXmlType;
import org.ieee.odm.schema.SpeedGovBPAServoGAModelXmlType;
import org.ieee.odm.schema.SteamTurbineBPATBModelXmlType;
import org.ieee.odm.schema.SteamTurbineNRXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BPADynamicTurbineGovernorRecord {
    private static final Logger log = LoggerFactory.getLogger(BPADynamicTurbineGovernorRecord.class);
	
	public static void processTurbineGovernorData(String str, DStabModelParser parser) throws ODMException {
    	final String strAry[]=getTGDataFields(str);
    	
    	String busId = BPABusRecord.getBusId(strAry[1]);
    	DStabBusXmlType bus = parser.getDStabBus(busId);
    	
    	//DStabGenDataXmlType dynGen = (DStabGenDataXmlType)bus.getGenData().getEquivGen().getValue();
    	DStabGenDataXmlType dynGen = DStabParserHelper.getDefaultGen(bus.getGenData());
    	double ratedPower=dynGen.getMvaBase().getValue();
    	
    	if(strAry[0].equals("GG")){ 
    		GovHydroSteamGeneralModelXmlType gov = DStabParserHelper.createGovHydroSteamGeneralModelXmlType(dynGen);
    					
			//machine Id
    		String tgId = "";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}	
    		gov.setDesc("Gov Hydro Steam General Model, MachId#" + tgId);
    		
			//PMAX 
    		double Pmax=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")) Pmax/=10;
    		gov.setPMAX(Pmax/ratedPower);
    		
			//R
    		double R=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) R/=1000;
    		gov.setR(R);
			
			//T1
    		double T1=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) T1/=1000;
    		gov.setT1(BaseDataSetter.createTimeConstSec(T1));

    		//T2
    	    double T2=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    	    if(!strAry[7].contains(".")) T2/=1000;
    		gov.setT2(BaseDataSetter.createTimeConstSec(T2));

    		//T3
		    double T3=ODMModelStringUtil.getDouble(strAry[8], 0.0);
		    if(!strAry[8].contains(".")) T3/=1000;
    		gov.setT3(BaseDataSetter.createTimeConstSec(T3));

    		// T4
		    double T4=ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) T4/=1000;
    		gov.setT4(BaseDataSetter.createTimeConstSec(T4));

    		//T5
		    double T5=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		    if(!strAry[10].contains(".")) T5/=1000;
    		gov.setT5(BaseDataSetter.createTimeConstSec(T5));
			
			//F
		    double F=ODMModelStringUtil.getDouble(strAry[11], 0.0);
		    if(!strAry[11].contains(".")) F/=1000;
    		gov.setF(F);
    	}
    	else if(strAry[0].equals("GH")){
    		GovBPAHydroTurbineGHXmlType gov = DStabParserHelper.createGovBPAHydroTurbineGHXmlType(dynGen);
		
			//machine Id
    		String id="1";
    		if(!strAry[3].equals("")){
    			id=strAry[3];
    		}
    		gov.setDesc("GOV Hydro Turbine GH type, machId#"+id);
    		
			//PMAX 
    		double Pmax=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")) Pmax/=10;
    		gov.setPMAX(Pmax/ratedPower);//in machine based pu unit;
    		//R
    		double R=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) R/=1000;
    		gov.setR(R);
    		
			//TG
    		double Tg=ODMModelStringUtil.getDouble(strAry[6], 0.0);    		
    		if(!strAry[6].contains(".")) Tg/=1000;
		    gov.setTG(BaseDataSetter.createTimeConstSec(Tg));		
			//TP
		    double Tp=ODMModelStringUtil.getDouble(strAry[7], 0.0);
		    if(!strAry[7].contains(".")) Tp/=1000;
		    gov.setTP(BaseDataSetter.createTimeConstSec(Tp));		
			//TD is corresponding to the TR in the ieee model
		    double Td= ODMModelStringUtil.getDouble(strAry[8], 0.0);
		    if(!strAry[8].contains(".")) Td/=1000;
		    gov.setTd(BaseDataSetter.createTimeConstSec(Td));
		    
			// TW/2
		    double Twhalf= ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) Twhalf/=1000;
		    gov.setTwHalf(BaseDataSetter.createTimeConstSec(Twhalf));	
		    // NOTE: Both VELCLOSE and VELOPEN is in PU based on PMAX.
			//VELCLOSE
		    double Uc=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		    if(!strAry[10].contains(".")) Uc/=1000;
    		gov.setVClose(Uc);
			
			//FVELOPEN
    		double Uo=ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) Uo/=1000;
    		gov.setVOpen(Uo);
			
			//Dd
    		double Dd=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) Dd/=1000;
    		gov.setDd(Dd);
    		//Epsilon
    		double Epsilon=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) Epsilon/=100000;
    		gov.setEpsilon(Epsilon);
    	}
    	else if(strAry[0].equals("GS")){
    		SpeedGovBPAGSModelXmlType gov = DStabParserHelper.createSpeedGovBPAGSModelXmlType(dynGen);
			
			//machine Id
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}
    		gov.setDesc("GOV Hydro Turbine GS type, machId#"+tgId);			
			//PMAX 
    		double Pmax=new Double(strAry[4]).doubleValue();
    		if(!strAry[4].contains(".")) Pmax/=10;
    		Pmax=Pmax/ratedPower;
    		gov.setPmax(Pmax);
    		//PMIN
    		double Pmin=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) Pmin/=10;
    		Pmin=Pmin/ratedPower;
    		gov.setPmin(Pmin);	
    			
    		//R
    		double R=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) R/=1000;
    		gov.setR(R);
			//T1
    		double T1=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) T1/=1000;
    		gov.setT1(BaseDataSetter.createTimeConstSec(T1));
			//T2
    		double T2=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) T2/=1000;
    		gov.setT2(BaseDataSetter.createTimeConstSec(T2));
			//T3
		    double T3= ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) T3/=1000;
		    gov.setT3(BaseDataSetter.createTimeConstSec(T3));
		    
			//VELOPEN
		    double Vopen=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		    if(!strAry[10].contains(".")) Vopen/=10;
		    gov.setVELOPEN(Vopen);			
			//FVELCLOSE
    		double Vclose=ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) Vclose/=10;
    		gov.setVELCLOSE(Vclose);
    		//Epsilon
    		double Epsilon=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) Epsilon/=100000;
    		gov.setEpsilon(Epsilon);
    		
    	}
    	else if(strAry[0].equals("GI")){
    		//make sure the governor and GiGa model are already there.
    		SpeedGovBPARegGIModelXmlType regGi=null;
    		if(dynGen.getGovernor()==null) 
    			DStabParserHelper.createGovBPAGiGaTbCombinedModelXmlType(dynGen);
    		if(dynGen.getGovernor().getValue().getSpeedGov()==null)
    			DStabParserHelper.createSpeedGovBPAGiGaCombinedXmlType(dynGen);
    		regGi = DStabParserHelper.createSpeedGovBPARegGIModelXmlType(dynGen);
			
			//machine Id
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}
    		regGi.setDesc("GOV Speed Governing GI type, machId#"+tgId);
			//T1 
    		double T1=new Double(strAry[4]).doubleValue();
    		if(!strAry[4].contains(".")) T1/=1000;
    		regGi.setT1(BaseDataSetter.createTimeConstSec(T1));
    		
    		//Epsilon
    		double Epsilon=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) Epsilon/=10000;
    		regGi.setEpsilon(Epsilon);
    			
    		//K
    		double K=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) K/=100;
    		regGi.setK(K);
    		
			//LoadSwich
    		double LoadSwich=ODMModelStringUtil.getDouble(strAry[7], 2.0);
    		regGi.setLoadSwichOff(LoadSwich==1.0?false:true);
    		    					
			//Kp1
    		double Kp1=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) Kp1/=1000;
    		regGi.setKp1(Kp1);    				    		
			//Kd1
		    double Kd1= ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) Kd1/=1000;
		    regGi.setKd1(Kd1);			
			//Ki1
		    double Ki1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		    if(!strAry[10].contains(".")) Ki1/=1000;
		    regGi.setKi1(Ki1);
		    
			//INTGMAX1
		    double INTGMAX1=ODMModelStringUtil.getDouble(strAry[11], 0.0);
		    if(!strAry[11].contains(".")) INTGMAX1/=1000;
    		INTGMAX1=INTGMAX1==0.0?9999.0:INTGMAX1;
    		regGi.setINTGMAX1(INTGMAX1);
    		//INTGMIN1
    		double INTGMIN1=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) INTGMIN1/=1000;
    		INTGMIN1=INTGMIN1==0.0?-9999.0:INTGMIN1;
    		regGi.setINTGMIN1(INTGMIN1);
    		
    		//PIDMAX1
		    double PIDMAX1=ODMModelStringUtil.getDouble(strAry[13], 0.0);
		    if(!strAry[13].contains(".")) PIDMAX1/=1000;
		    PIDMAX1=(PIDMAX1==0.0)?9999.0:PIDMAX1;
    		regGi.setPIDMAX1(PIDMAX1);
    		//PIDMIN1
    		double PIDMIN1=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) PIDMIN1/=1000;
		    PIDMIN1=(PIDMIN1==0.0)?-9999.0:PIDMIN1;
    		regGi.setPIDMIN1(PIDMIN1);
    		
    		//LoadForwardSwitch
    		double LoadForwardSwitch=ODMModelStringUtil.getDouble(strAry[15], 2.0);
    		regGi.setLoadForwardSwitchOff(LoadForwardSwitch==1.0?false:true);
    		
    	}
    	else if(strAry[0].equals("GI+")){
    		SpeedGovBPARegGIModelXmlType regGi = ((SpeedGovBPAGiGaCombinedXmlType)dynGen.getGovernor().getValue().getSpeedGov().getValue()).getRegulator();
			
			//machine Id
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}    		
    		regGi.setDesc("GOV Speed Governing GI/GI+ type, machId#"+tgId);
			//PresserSwitch 
    		double PresserSwitch=ODMModelStringUtil.getDouble(strAry[4], 2.0);
    		regGi.setPresserSwitchOff(PresserSwitch==1.0?false:true);
    		
    		//Kp2
    		double Kp2=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) Kp2/=1000;
    		regGi.setKp2(Kp2);
    		//Kd2
    		double Kd2=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) Kd2/=1000;
    		regGi.setKd2(Kd2);
    		//Ki2
    		double Ki2=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) Ki2/=1000;
    		regGi.setKi2(Ki2);
    				    
			//INTGMAX2
		    double INTGMAX2=ODMModelStringUtil.getDouble(strAry[8], 0.0);
		    if(!strAry[13].contains(".")) INTGMAX2/=1000;
    		INTGMAX2=INTGMAX2==0.0?9999.0:INTGMAX2;
    		regGi.setINTGMAX2(INTGMAX2);
    		//INTGMIN2
    		double INTGMIN2=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) INTGMIN2/=1000;
    		INTGMIN2=INTGMIN2==0.0?-9999.0:INTGMIN2;
    		regGi.setINTGMIN2(INTGMIN2);
    		
    		//PIDMAX2
    		double PIDMAX2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) PIDMAX2/=1000;
		    PIDMAX2=(PIDMAX2==0.0)?9999.0:PIDMAX2;
    		regGi.setPIDMAX2(PIDMAX2);
    		//PIDMIN2
    		double PIDMIN2=ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) PIDMIN2/=1000;
		    PIDMIN2=(PIDMIN2==0.0)?-9999.0:PIDMIN2;
    		regGi.setPIDMIN2(PIDMIN2);
    		
    		//ConMax
    		double ConMax=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) ConMax/=1000;
		    ConMax=(ConMax==0.0)?9999.0:ConMax;
    		regGi.setConMax(ConMax);
    		//ConMin
    		double ConMin=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) ConMin/=1000;
		    ConMin=(ConMin==0.0)?-9999.0:ConMin;
    		regGi.setConMin(ConMin);
    	}
    	else if(strAry[0].equals("GA")){
    		//make sure the governor and GiGa model are already there.
    		SpeedGovBPAServoGAModelXmlType serGa=null;
    		if(dynGen.getGovernor()==null) 
    			DStabParserHelper.createGovBPAGiGaTbCombinedModelXmlType(dynGen);
    		if(dynGen.getGovernor().getValue().getSpeedGov()==null)
    			DStabParserHelper.createSpeedGovBPAGiGaCombinedXmlType(dynGen);
    		serGa = DStabParserHelper.createSpeedGovBPAServoGAModelXmlType(dynGen);
			
			//machine Id
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}
    		serGa.setDesc("GOV Speed Governing GA type, machId#"+tgId);
			//Pe 
    		double Pe=new Double(strAry[4]).doubleValue();
    		if(!strAry[4].contains(".")) Pe/=100;
    		serGa.setPe(Pe);
    		
    		//Tc
    		double Tc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) Tc/=100;
    		serGa.setTc(BaseDataSetter.createTimeConstSec(Tc));    			
    		//To
    		double To=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) To/=100;
    		serGa.setTo(BaseDataSetter.createTimeConstSec(To));
    		
			//VELCLOSE
    		double VELCLOSE=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) VELCLOSE/=100;
    		serGa.setVELCLOSE(VELCLOSE);    		    					
			//VELOPEN
    		double VELOPEN=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) VELOPEN/=100;
    		serGa.setVELOPEN(VELOPEN);
    		
			//PMAX
		    double Pmax= ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) Pmax/=100;
		    serGa.setPmax(Pmax);			
			//PMIN
		    double Pmin=ODMModelStringUtil.getDouble(strAry[10], 0.0);
		    if(!strAry[10].contains(".")) Pmin/=100;
		    serGa.setPmin(Pmin);
		    
			//T1
		    double T1=ODMModelStringUtil.getDouble(strAry[11], 0.0);
		    if(!strAry[11].contains(".")) T1/=100;
		    serGa.setT1(BaseDataSetter.createTimeConstSec(T1));
		    
		    //Kp
    		double Kp=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) Kp/=100;
    		serGa.setKp(Kp);
    		//Kd
    		double Kd=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) Kd/=100;
    		serGa.setKd(Kd);
    		//Ki
    		double Ki=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) Ki/=100;
    		serGa.setKi(Ki);
    		
    		//INTGMAX
		    double INTGMAX=ODMModelStringUtil.getDouble(strAry[15], 0.0);
		    if(!strAry[15].contains(".")) INTGMAX/=100;
    		INTGMAX=INTGMAX==0.0?9999.0:INTGMAX;
    		serGa.setINTGMAX(INTGMAX);
    		//INTGMIN
    		double INTGMIN=ODMModelStringUtil.getDouble(strAry[16], 0.0);
		    if(!strAry[16].contains(".")) INTGMIN/=100;
    		INTGMIN=INTGMIN==0.0?-9999.0:INTGMIN;
    		serGa.setINTGMIN(INTGMIN);
    		
    		//PIDMAX
    		double PIDMAX=ODMModelStringUtil.getDouble(strAry[17], 0.0);
		    if(!strAry[17].contains(".")) PIDMAX/=100;
		    PIDMAX=(PIDMAX==0.0)?9999.0:PIDMAX;
    		serGa.setPIDMAX(PIDMAX);
    		//PIDMIN
    		double PIDMIN=ODMModelStringUtil.getDouble(strAry[18], 0.0);
		    if(!strAry[18].contains(".")) PIDMIN/=100;
		    PIDMIN=(PIDMIN==0.0)?-9999.0:PIDMIN;
    		serGa.setPIDMIN(PIDMIN);
    	}
    	else if(strAry[0].equals("TA")){
    		//TODO now we use a general stream turbine to represent 
    		
    		SteamTurbineNRXmlType st=DStabParserHelper.createSteamTurbineNRXmlType(dynGen);
    		//Machine Id   		
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}
    		st.setDesc("GOV Steam Turbine BPA TA type(non reheat), machId#"+tgId);	
    		//TCH
    		double Tch= new Double(strAry[4]).doubleValue();
    		if(!strAry[4].contains(".")) Tch/=1000;
    		st.setTCH(BaseDataSetter.createTimeConstSec(Tch));    			
    		st.setK(1.0);
    	}
    	else if(strAry[0].equals("TB")){
    		// since tur is part of the parent governor, it is assume that the parent genernor has been
    		// created
    		//TODO How to create one if there is no governor defined yet.
    		//assert(dynGen.getGovernor() != null);
    		SteamTurbineBPATBModelXmlType st=DStabParserHelper.createSteamTurbineBPATBModelXmlType(dynGen);
    		//busId   		
    		String tgId="1";
    		if(!strAry[3].equals("")){
    			tgId=strAry[3];
    		}
    		st.setDesc("GOV Steam Turbine BPA TB type, machId#"+tgId);
               
    		//TCH
			double Tch= ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")) Tch/=1000;
			st.setTCH(BaseDataSetter.createTimeConstSec(Tch));	  
			//FHP
		    double Fhp= ODMModelStringUtil.getDouble(strAry[5], 0.0);
		    if(!strAry[5].contains(".")) Fhp/=1000;
			st.setFHP(Fhp);
			//TRH
		    double Trh= ODMModelStringUtil.getDouble(strAry[6], 0.0);
		    if(!strAry[6].contains(".")) Trh/=1000;
		    st.setTRH(BaseDataSetter.createTimeConstSec(Trh));	    			
			//FIP
		    double Fip= ODMModelStringUtil.getDouble(strAry[7], 0.0);
		    if(!strAry[7].contains(".")) Fip/=1000;
		    st.setFIP(Fip);   			
			//TCO
		    double Tco=ODMModelStringUtil.getDouble(strAry[8], 0.0);
		    if(!strAry[8].contains(".")) Tco/=1000;
		    st.setTCO(BaseDataSetter.createTimeConstSec(Tco));
			// FLP
		    double Flp=ODMModelStringUtil.getDouble(strAry[9], 0.0);
		    if(!strAry[9].contains(".")) Flp/=1000;
   		    st.setFLP(Flp);
   		    //Lambda
   		    double Lambda=ODMModelStringUtil.getDouble(strAry[10], 0.0);
   		    if(!strAry[10].contains(".")) Lambda/=100;
   		    st.setLambda(Lambda);    		   
    		       		        			
    	}
    }
	
	private static String[] getTGDataFields ( String str) {
        final String[] strAry = new String[19];
    	strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 3).trim();
    	//to process the Chinese characters first, if any.
		int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str.substring(3,11).trim());
		//Columns 6-13 busName  
		strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
		str=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
		//bus Voltage
		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
		//Id
		strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
    	try{
    		if(str.substring(0, 2).trim().equals("GG")){

				//PMAX 
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 22).trim();
				//R
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 27).trim();
				//T1
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();
				//T2
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 37).trim();
				//T3
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 42).trim();
				// T4
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
				//T5
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
				//F
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,53, 57).trim();
				
	    		
	    	}else if(str.substring(0, 2).trim().equals("GH")){
	    	
				//PMAX 
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 22).trim();
				//R
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 27).trim();
				//TG
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();					
				//TP
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 37).trim();
				//TD
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 42).trim();
				// TW/2
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
				//VELCLOSE
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
				//FVELOPEN
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,53, 57).trim();
				//Dd
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,58, 62).trim();
				//Epsilon
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,63, 68).trim();
				
	    		
	    	}
	    	else if(str.substring(0, 2).trim().equals("GS")){
	    		
				//PMAX 
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 22).trim();
				//PMIN
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 28).trim();
				//R
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,29, 33).trim();
				//T1
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,34, 38).trim();
				//T2
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,39, 43).trim();
				// T3
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,44, 48).trim();
				//VELOPEN
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,49, 54).trim();
				//VELCLOSE
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,55, 60).trim();
				//Epsilon
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,61, 66).trim();
	    		
	    	}
	    	else if(str.substring(0, 3).trim().equals("GI")){
	    		
				
				//T1
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//Epsilon
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 27).trim();
				//K
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();
				//LOAD AUTO SWITCH
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 33).trim();
				//Kp1
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,34, 38).trim();
				//Kd1
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,39, 43).trim();
				//Ki1
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,44, 48).trim();
				//INTG_MAX1
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,49, 53).trim();
				//INTG_MIN1
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,54, 58).trim();
				
				//PID_MAX1
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,59, 63).trim();
				//PID_MIN1
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,64, 68).trim();
				
				//LOAD Forward Back SWITCH
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,69, 69).trim();

				//W_MAX
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,70, 74).trim();
				//W_MIN
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,75, 79).trim();
				
				
	    	}else if(str.substring(0, 3).trim().equals("GI+")){
	    		
				
				//Pressure automatic switch
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 17).trim();
				//Kp2
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,18, 22).trim();
				//Kd2
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 27).trim();
				//Ki2
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();
				//INTG_MAX2
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 37).trim();
				//INTG_MIN2
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 42).trim();
				//PID_MAX2
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
				//PID_MIN2
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
				//CON_MAX
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,53, 57).trim();
				//CON_MIN
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,58, 62).trim();
					
	    	}else if(str.substring(0, 2).trim().equals("GA")){
	    		
	    		//Pe
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 22).trim();
				//Tc
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 26).trim();
				//To
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
		
				//VELclose
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,31, 34).trim();
				//VELopen
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,35, 38).trim();
				
				//PMAX
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,39, 42).trim();
				//PMIN
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 46).trim();
								
				//T1
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 50).trim();
				
				//Kp
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,51, 54).trim();
				//Kd
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,55, 58).trim();
				//Ki
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,59, 62).trim();
				
				//INTG_MAX
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,63, 66).trim();
				//INTG_MIN
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 70).trim();
				
				//PID_MAX
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,71, 74).trim();
				//PID_MIN
				strAry[18]=ODMModelStringUtil.getStringReturnEmptyString(str,75, 78).trim();
				
	    	}
	    	
	    	else if(str.substring(0, 2).trim().equals("TA")){
	    		
				//TCH
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//k1
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 26).trim();
	    	}else if(str.substring(0, 2).trim().equals("TB")){
	    		
				
				//tch
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//FHP
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
				//TRH
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,32, 36).trim();
				//FIP
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,37, 41).trim();
				//TCO
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 51).trim();
				// FLP
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,52, 56).trim();
				//Lambda
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
				
	    	}
    	}catch(Exception e){
    		log.error(e.toString());
    	}
    	return strAry;
    }
}
