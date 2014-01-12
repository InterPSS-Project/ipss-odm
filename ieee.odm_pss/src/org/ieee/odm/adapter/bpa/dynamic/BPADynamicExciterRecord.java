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
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcBPAECXmlType;
import org.ieee.odm.schema.ExcBPAEKXmlType;
import org.ieee.odm.schema.ExcBPAFJXmlType;
import org.ieee.odm.schema.ExcBPAFKXmlType;
import org.ieee.odm.schema.ExcBPAFQXmlType;
import org.ieee.odm.schema.ExcBPAFRXmlType;
import org.ieee.odm.schema.ExcBPAFSXmlType;
import org.ieee.odm.schema.ExcBPAFUXmlType;
import org.ieee.odm.schema.ExcBPAFVXmlType;
import org.ieee.odm.schema.ExcIEEE1968Type1XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeAC2XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeDC1XmlType;
import org.ieee.odm.schema.ExciterModelXmlType;

public class BPADynamicExciterRecord {
	private final static int EA=1;
	private final static int EC=2;
	private final static int EK=3;
	private final static int FA=4;
	private final static int FF=5;
	private final static int FJ=6;
	private final static int FK=7;
	private final static int FQ=8;
	private final static int FR=9;
	private final static int FS=10;
	private final static int FU=11;
	private final static int FV=12;
	
	
	public static void processExciterData(String str, DStabModelParser parser ) throws ODMException {
    	final String strAry[]=getExciterDataFields(str);
    	int type= getExcType(strAry[0]);
    	
    	String busId = BPABusRecord.getBusId(strAry[1]);
    	DStabBusXmlType bus = parser.getDStabBus(busId);
    	
    	DStabGenDataXmlType dynGen = DStabParserHelper.getDefaultGen(bus.getGenData());
    	
    	if(type==EA){
    		ExcIEEE1968Type1XmlType exc = DStabParserHelper.createExcIEEE1968Type1XmlType(dynGen);

    		//machine Id
    		String id="1";
    		if(!strAry[3].equals("")){
    			id=strAry[3];
    		} 
    		exc.setDesc("BPA EA type, machId#" + id);
    		
    		//TR
    		double Tr=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")){
    			Tr=Tr/1000;
			}
    		exc.setTR(BaseDataSetter.createTimeConstSec(Tr));
    		    		
    		//KA for all, KV for EE
    		double Ka=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")){
    			Ka=Ka/100;
			}
    		exc.setKA(Ka);
    		   		
    		//TA for all, TRH for EE
    		double Ta=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")){
    			Ta=Ta/100;
			}
    		exc.setTA(BaseDataSetter.createTimeConstSec(Ta));
    		
    		//VRminMult, VRmax*multi=Vrmin. VRmin for ED EJ
    		double multi=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")){
    			multi=multi/100;
			}
    		// KE
    		double Ke=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")){
    			Ke=Ke/1000;
			}
    		exc.setKE(Ke);
    		    		
    		//TE
    		double Te= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")){
    			Te=Te/1000;
			}
    		exc.setTE(BaseDataSetter.createTimeConstSec(Te));
    		
    		//rule: E1 > E2, Se(E1) > Se(E2) 
    		//SE0.75MAX for all, KI for DD
    		//e1=EfdMax, e2=0.75*EfdMax,so it is set after processing EfdMax
    		double SE2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")){
    			SE2=SE2/1000;
			}
    		exc.setSE2(SE2);    		
    		
    		//EFDMin
    		double Efdmin=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")){
    			Efdmin=Efdmin/1000;
			}
    		exc.setEFDMIN(Efdmin);
    		
    		//EFDMax for all, VNmax for ED
    		double Efdmax=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")){
    			Efdmax=Efdmax/1000;
			}
    		// SEmax for all, Kp for DD
    		exc.setE1(Efdmax);
    		exc.setE2(0.75*Efdmax);
    		double SE1= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")){
    			SE1=SE1/1000;
			}
    		exc.setSE1(SE1);    		
    		//KF
    		double Kf= ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")){
    			Kf=Kf/1000;
			}
    		exc.setKF(Kf);
    		    		
    		//TF    		
    		double Tf= ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")){
    			Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
    		exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
    		
    		//VRmax=(SE2+Ke)*EFDmax,Vrmin
    		
    		double VRmax=(SE2+Ke)*Efdmax;
    		double VRmin=VRmax*multi;
    		exc.setVRMAX(VRmax);
			if(VRmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-VRmin) );
				VRmin*=-1;
			}
    		exc.setVRMIN(VRmin);
    	}
    	//BPA EC type
    	else if(type==EC){
    		ExcBPAECXmlType exc = DStabParserHelper.createExcBPAECXmlType(dynGen);

    		//machine Id
    		String id="1";
    		if(!strAry[3].equals("")){
    			id=strAry[3];
    		} 
    		exc.setDesc("BPA EK Type Exciter , machId#" + id);
    		
    		//TR
    		double Tr=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")){
    			Tr=Tr/1000;
			}
    		exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));
    		    		
    		//KA for all, KV for EE
    		double Ka=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")){
    			Ka=Ka/100;
			}
    		exc.setKa(Ka);
    		   		
    		//TA for all, TRH for EE
    		double Ta=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")){
    			Ta=Ta/100;
			}
    		exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
    		
    		//VRminMult, VRmax*multi=Vrmin. VRmin for ED EJ
    		double multi=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")){
    			multi=multi/100;
			}
    		// KE
    		double Ke=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")){
    			Ke=Ke/1000;
			}
    		exc.setKE(Ke);
    		    		
    		//TE
    		double Te= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")){
    			Te=Te/1000;
			}
    		exc.setTE(BaseDataSetter.createTimeConstSec(Te));
    		
    		//rule: E1 > E2, Se(E1) > Se(E2) 
    		//SE0.75MAX for all, KI for DD
    		//e1=EfdMax, e2=0.75*EfdMax,so it is set after processing EfdMax
    		double SE2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")){
    			SE2=SE2/1000;
			}
    		exc.setSE2(SE2);    		
    		
    		//EFDMin
    		double Efdmin=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")){
    			Efdmin=Efdmin/1000;
			}
    		exc.setEFDMIN(Efdmin);
    		
    		//EFDMax for all, VNmax for ED
    		double Efdmax=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")){
    			Efdmax=Efdmax/1000;
			}
    		// SEmax for all, Kp for DD
    		exc.setE1(Efdmax);
    		exc.setE2(0.75*Efdmax);
    		double SE1= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")){
    			SE1=SE1/1000;
			}
    		exc.setSE1(SE1);    		
    		//KF
    		double Kf= ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")){
    			Kf=Kf/1000;
			}
    		exc.setKF(Kf);
    		    		
    		//TF    		
    		double Tf= ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")){
    			Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
    		exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
    		
    		//VRmax=(SE2+Ke)*EFDmax,Vrmin
    		
    		double VRmax=(SE2+Ke)*Efdmax;
    		double VRmin=VRmax*multi;
    		exc.setVrmax(VRmax);
			if(VRmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-VRmin) );
				VRmin*=-1;
			}
    		exc.setVrmin(VRmin);
    	}
    	else if(type==EK){
    		ExcBPAEKXmlType exc = DStabParserHelper.createExcBPAEKXmlType(dynGen);

    		//machine Id
    		String id="1";
    		if(!strAry[3].equals("")){
    			id=strAry[3];
    		} 
    		exc.setDesc("BPA EK Type Exciter , machId#" + id);
    		
    		//TR
    		double Tr=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")){
    			Tr=Tr/1000;
			}
    		exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));
    		    		
    		//KA for all, KV for EE
    		double Ka=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")){
    			Ka=Ka/100;
			}
    		exc.setKa(Ka);
    		   		
    		//TA for all, TRH for EE
    		double Ta=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")){
    			Ta=Ta/100;
			}
    		exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
    		
    		//VRminMult, VRmax*multi=Vrmin. VRmin for ED EJ
    		double multi=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")){
    			multi=multi/100;
			}
    		// KE
    		double Ke=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")){
    			Ke=Ke/1000;
			}
    		exc.setKE(Ke);
    		    		
    		//TE
    		double Te= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")){
    			Te=Te/1000;
			}
    		exc.setTE(BaseDataSetter.createTimeConstSec(Te));
    		
    		//rule: E1 > E2, Se(E1) > Se(E2) 
    		//SE0.75MAX for all, KI for DD
    		//e1=EfdMax, e2=0.75*EfdMax,so it is set after processing EfdMax
    		double SE2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")){
    			SE2=SE2/1000;
			}
    		exc.setSE2(SE2);    		
    		
    		//EFDMin
    		double Efdmin=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")){
    			Efdmin=Efdmin/1000;
			}
    		exc.setEFDMIN(Efdmin);
    		
    		//EFDMax for all, VNmax for ED
    		double Efdmax=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")){
    			Efdmax=Efdmax/1000;
			}
    		// SEmax for all, Kp for DD
    		exc.setE1(Efdmax);
    		exc.setE2(0.75*Efdmax);
    		double SE1= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")){
    			SE1=SE1/1000;
			}
    		exc.setSE1(SE1);    		
    		//KF
    		double Kf= ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")){
    			Kf=Kf/1000;
			}
    		exc.setKF(Kf);
    		    		
    		//TF    		
    		double Tf= ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")){
    			Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
    		exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
    		
    		//VRmax=(SE2+Ke)*EFDmax,Vrmin
    		
    		double VRmax=(SE2+Ke)*Efdmax;
    		double VRmin=VRmax*multi;
    		exc.setVrmax(VRmax);
			if(VRmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-VRmin) );
				VRmin*=-1;
			}
    		exc.setVrmin(VRmin);
    	}
    	else if(type==FA){
    		
            //BPA exciter FA type is the same as IEEE 1981 DC1 type or IEEE 2005 DC1A type
    		ExcIEEE1981TypeDC1XmlType exc = DStabParserHelper.createExcIEEE1981TypeDC1XmlType(dynGen);
    		
			//excId
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}	
    		exc.setDesc("IEEE Type DC1 excId-" + excId);
			
    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/10000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/10000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/10000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));			
			
			// TB
			double Tb= ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				Tb=Tb/1000;
			}
			exc.setTB(BaseDataSetter.createTimeConstSec(Tb));
			
			//TC
			double Tc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				Tc=Tc/1000;
			}
			exc.setTC(BaseDataSetter.createTimeConstSec(Tc));
			
			//KA, KV for FE
			double Ka= ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				Ka=Ka/100;
			}
			exc.setKa(Ka);			
			
			// TA, TRH for FE
			double Ta= ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
			
			//VRmax, Vamax for FH
			double Vrmax= ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Vrmax=Vrmax/1000;
			}
			exc.setVrmax(Vrmax);
			
			//VRmin, Vamin			
			double Vrmin= ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Vrmin=Vrmin/1000;
			}
			if(Vrmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-Vrmin) );
				Vrmin*=-1;
			}
			exc.setVrmin(Vrmin);
			
			//Ke
			double Ke=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Ke=Ke/1000;
			}
			exc.setKE(Ke);
			
			//Te
			double Te=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Te=Te/1000;
			}
			exc.setTE(BaseDataSetter.createTimeConstSec(Te));
    	}
    	
    	else if(type==FJ){
    		ExcBPAFJXmlType exc = DStabParserHelper.createExcBPAFJXmlType(dynGen);
    			    		
			//excId
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}	
    		exc.setDesc("BPA FJ Type Exciter, excId-" + excId);
    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/10000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/10000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/10000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
			// TB
			double Tb= ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				Tb=Tb/1000;
			}
			exc.setTB(BaseDataSetter.createTimeConstSec(Tb));
			
			//TC
			double Tc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				Tc=Tc/1000;
			}
			exc.setTC(BaseDataSetter.createTimeConstSec(Tc));
			
			//KA, KV for FE
			double Ka= ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				Ka=Ka/100;
			}
			exc.setKa(Ka);			
			
			// TA, TRH for FE
			double Ta= ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
			
			//VRmax, Vamax for FH
			double Vrmax= ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Vrmax=Vrmax/1000;
			}
			exc.setVrmax(Vrmax);
			
			//VRmin, Vamin			
			double Vrmin= ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Vrmin=Vrmin/1000;
			}
			if(Vrmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-Vrmin) );
				Vrmin*=-1;
			}
			exc.setVrmin(Vrmin);
    	}
else if(type==FK){
			
    		ExcBPAFKXmlType exc = DStabParserHelper.createExcBPAFKXmlType(dynGen);
    		
			//excId
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];
			}	
    		exc.setDesc("BPA FK Type Exciter,excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/10000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/10000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/10000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
			
			//VIMax for G K L,VAmax for FF
			double Vimax= ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				Vimax=Vimax/1000;
			}
			exc.setVIMAX(Vimax);
			
			//VIMin for G K L,VAmin for FF
			//VIMax for G K L,VAmax for FF
			double Vimin= ODMModelStringUtil.getDouble(strAry[8], 0.0);
			if(!strAry[7].contains(".")){
				Vimin=Vimin/1000;
			}
			exc.setVIMIN(Vimin);

			// TB
			double Tb= ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				Tb=Tb/1000;
			}
			exc.setTB(BaseDataSetter.createTimeConstSec(Tb));
			
			//TC
			double Tc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				Tc=Tc/1000;
			}
			exc.setTC(BaseDataSetter.createTimeConstSec(Tc));
			
			//KA, KV for FE
			double Ka= ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				Ka=Ka/100;
			}
			exc.setKa(Ka);			
			
			// TA, TRH for FE
			double Ta= ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
			
			//VRmax, Vamax for FH
			double Vrmax= ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Vrmax=Vrmax/1000;
			}
			exc.setVrmax(Vrmax);
			
			//VRmin, Vamin			
			double Vrmin= ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Vrmin=Vrmin/1000;
			}
			if(Vrmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-Vrmin) );
				Vrmin*=-1;
			}
			exc.setVrmin(Vrmin);
    	}
    	else if(type==FQ){
    		ExcBPAFQXmlType exc = DStabParserHelper.createExcBPAFQXmlType(dynGen);
    
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("BPA Eeciter FQ type, excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/1000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/1000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/1000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
						
			//K
			double K=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				K=K/1000;
			}
			exc.setK(K);
						
			//Kv(F3.0)
			double Kv=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			exc.setKV(Kv);
						
			// T1
			double T1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				T1=T1/1000;
			}
			exc.setT1(BaseDataSetter.createTimeConstSec(T1));
			
			//T2
			double T2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				T2=T2/1000;
			}
			exc.setT2(BaseDataSetter.createTimeConstSec(T2));
						
			//T3			
			double T3=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				T3=T3/1000;
			}
			exc.setT3(BaseDataSetter.createTimeConstSec(T3));
						
			// T4			
			double T4=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				T4=T4/1000;
			}
			exc.setT4(BaseDataSetter.createTimeConstSec(T4));
						
			//KA
			double Ka=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Ka=Ka/1000;
			}
			exc.setKa(Ka);
					
			//TA
			double Ta=ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
						
			//KF
			double Kf=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Kf=Kf/1000;
			}
			exc.setKF(Kf);
			
			//TF
			double Tf=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
			exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
					
			//KH
			double Kh=ODMModelStringUtil.getDouble(strAry[17], 0.0);
			if(!strAry[17].contains(".")){
				Kh=Kh/100;
			}
			exc.setKH(Kh);
    	}
    	  //type==FR
    	else if(type==FR){
    		ExcBPAFRXmlType exc = DStabParserHelper.createExcBPAFRXmlType(dynGen);
    
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("BPA Eeciter FR type, excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/1000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/1000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/1000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
						
			//K
			double K=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				K=K/1000;
			}
			exc.setK(K);
						
			//Kv(F3.0)
			double Kv=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			exc.setKV(Kv);
						
			// T1
			double T1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				T1=T1/1000;
			}
			exc.setT1(BaseDataSetter.createTimeConstSec(T1));
			
			//T2
			double T2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				T2=T2/1000;
			}
			exc.setT2(BaseDataSetter.createTimeConstSec(T2));
						
			//T3			
			double T3=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				T3=T3/1000;
			}
			exc.setT3(BaseDataSetter.createTimeConstSec(T3));
						
			// T4			
			double T4=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				T4=T4/1000;
			}
			exc.setT4(BaseDataSetter.createTimeConstSec(T4));
						
			//KA
			double Ka=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Ka=Ka/1000;
			}
			exc.setKa(Ka);
					
			//TA
			double Ta=ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
						
			//KF
			double Kf=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Kf=Kf/1000;
			}
			exc.setKF(Kf);
			
			//TF
			double Tf=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
			exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
					
			//KH
			double Kh=ODMModelStringUtil.getDouble(strAry[17], 0.0);
			if(!strAry[17].contains(".")){
				Kh=Kh/100;
			}
			exc.setKH(Kh);
    	}
    	//type==FS
    	else if(type==FS){
    		ExcBPAFSXmlType exc = DStabParserHelper.createExcBPAFSXmlType(dynGen);
    
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("BPA Eeciter FS type, excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/1000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/1000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/1000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
						
			//K
			double K=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				K=K/1000;
			}
			exc.setK(K);
						
			//Kv(F3.0)
			double Kv=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			exc.setKV(Kv);
						
			// T1
			double T1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				T1=T1/1000;
			}
			exc.setT1(BaseDataSetter.createTimeConstSec(T1));
			
			//T2
			double T2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				T2=T2/1000;
			}
			exc.setT2(BaseDataSetter.createTimeConstSec(T2));
						
			//T3			
			double T3=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				T3=T3/1000;
			}
			exc.setT3(BaseDataSetter.createTimeConstSec(T3));
						
			// T4			
			double T4=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				T4=T4/1000;
			}
			exc.setT4(BaseDataSetter.createTimeConstSec(T4));
						
			//KA
			double Ka=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Ka=Ka/1000;
			}
			exc.setKa(Ka);
					
			//TA
			double Ta=ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
						
			//KF
			double Kf=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Kf=Kf/1000;
			}
			exc.setKF(Kf);
			
			//TF
			double Tf=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
			exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
					
			//KH
			double Kh=ODMModelStringUtil.getDouble(strAry[17], 0.0);
			if(!strAry[17].contains(".")){
				Kh=Kh/100;
			}
			exc.setKH(Kh);
    	}
    	//type==FU
    	else if(type==FU){
    		ExcBPAFUXmlType exc = DStabParserHelper.createExcBPAFUXmlType(dynGen);
    
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("BPA Eeciter FU type, excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/1000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/1000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/1000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
						
			//K
			double K=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				K=K/1000;
			}
			exc.setK(K);
						
			//Kv(F3.0)
			double Kv=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			exc.setKV(Kv);
						
			// T1
			double T1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				T1=T1/1000;
			}
			exc.setT1(BaseDataSetter.createTimeConstSec(T1));
			
			//T2
			double T2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				T2=T2/1000;
			}
			exc.setT2(BaseDataSetter.createTimeConstSec(T2));
						
			//T3			
			double T3=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				T3=T3/1000;
			}
			exc.setT3(BaseDataSetter.createTimeConstSec(T3));
						
			// T4			
			double T4=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				T4=T4/1000;
			}
			exc.setT4(BaseDataSetter.createTimeConstSec(T4));
						
			//KA
			double Ka=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Ka=Ka/1000;
			}
			exc.setKa(Ka);
					
			//TA
			double Ta=ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
						
			//KF
			double Kf=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Kf=Kf/1000;
			}
			exc.setKF(Kf);
			
			//TF
			double Tf=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
			exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
    	}
    	//type==FV
    	else if(type==FV){
    		ExcBPAFVXmlType exc = DStabParserHelper.createExcBPAFVXmlType(dynGen);
    
			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("BPA Eeciter FV type, excId-" + excId);

    		//Rc
			double Rc=ODMModelStringUtil.getDouble(strAry[4], 0.0);
			if(!strAry[4].contains(".")){
				Rc=Rc/1000;
			}
			exc.setLoadRc(Rc);
						
			//Xc
			double Xc=ODMModelStringUtil.getDouble(strAry[5], 0.0);
			if(!strAry[5].contains(".")){
				Xc=Xc/1000;
			}
			exc.setLoadXc(Xc);
						
			//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/1000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));	
						
			//K
			double K=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			if(!strAry[7].contains(".")){
				K=K/1000;
			}
			exc.setK(K);
						
			//Kv(F3.0)
			double Kv=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			exc.setKV(Kv);
						
			// T1
			double T1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				T1=T1/1000;
			}
			exc.setT1(BaseDataSetter.createTimeConstSec(T1));
			
			//T2
			double T2=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				T2=T2/1000;
			}
			exc.setT2(BaseDataSetter.createTimeConstSec(T2));
						
			//T3			
			double T3=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				T3=T3/1000;
			}
			exc.setT3(BaseDataSetter.createTimeConstSec(T3));
						
			// T4			
			double T4=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				T4=T4/1000;
			}
			exc.setT4(BaseDataSetter.createTimeConstSec(T4));
						
			//KA
			double Ka=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Ka=Ka/1000;
			}
			exc.setKa(Ka);
					
			//TA
			double Ta=ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
						
			//KF
			double Kf=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Kf=Kf/1000;
			}
			exc.setKF(Kf);
			
			//TF
			double Tf=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Tf=Tf/1000;
			}
    		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
    		if(Kf==0.0&&Tf==0.0)Tf=1.0;
			exc.setTF(BaseDataSetter.createTimeConstSec(Tf));
    	}
    	else if(type==FF){
    		ExcIEEE1981TypeAC2XmlType exc = DStabParserHelper.createExcIEEE1981TypeAC2XmlType(dynGen);
    		

			String excId="1";
			if(!strAry[3].equals("")){
				excId=strAry[3];				
			}			
    		exc.setDesc("IEEE1981 AC2 Type excId-" + excId);

    		//TR
			double Tr=ODMModelStringUtil.getDouble(strAry[6], 0.0);
			if(!strAry[6].contains(".")){
				Tr=Tr/10000;
			}
			exc.setTransTr(BaseDataSetter.createTimeConstSec(Tr));			
			
			// TB
			double Tb= ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				Tb=Tb/1000;
			}
			exc.setTB(BaseDataSetter.createTimeConstSec(Tb));
			
			//TC
			double Tc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				Tc=Tc/1000;
			}
			exc.setTC(BaseDataSetter.createTimeConstSec(Tc));
			
			//KA, KV for FE
			double Ka= ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				Ka=Ka/100;
			}
			exc.setKa(Ka);			
			
			// TA, TRH for FE
			double Ta= ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				Ta=Ta/1000;
			}
			exc.setTa(BaseDataSetter.createTimeConstSec(Ta));
			
			//VRmax, Vamax for FH
			double Vrmax= ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				Vrmax=Vrmax/1000;
			}
			exc.setVrmax(Vrmax);
			
			//VRmin, Vamin			
			double Vrmin= ODMModelStringUtil.getDouble(strAry[14], 0.0);
			if(!strAry[14].contains(".")){
				Vrmin=Vrmin/1000;
			}
			if(Vrmin>0){
				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
						", automatically change it to: "+(-Vrmin) );
				Vrmin*=-1;
			}
			exc.setVrmin(Vrmin);
			
			//Ke
			double Ke=ODMModelStringUtil.getDouble(strAry[15], 0.0);
			if(!strAry[15].contains(".")){
				Ke=Ke/1000;
			}
			exc.setKE(Ke);
			
			//Te
			double Te=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				Te=Te/1000;
			}
			exc.setTE(BaseDataSetter.createTimeConstSec(Te));
    	}
    	else if(str.substring(0, 2).trim().equals("FZ")||
    			str.substring(0, 2).trim().equals("F+")){// continued record for BPA Exciter models.
    		DStabGenDataXmlType genData = DStabParserHelper.getDefaultGen(bus.getGenData());
    		ExciterModelXmlType exc = genData.getExciter().getValue();   	
        	
        	if(str.substring(0, 2).trim().equals("FZ")){
        		
            	if(exc instanceof ExcBPAFJXmlType){//ExciterType.BPAFJ   		
            		ExcBPAFJXmlType excFJ=(ExcBPAFJXmlType)exc;
            		//EFDmax
            		double EFDmax= ODMModelStringUtil.getDouble(strAry[7], 0.0);
            		if(!strAry[7].contains(".")){
            			EFDmax=EFDmax/1000;
        			}
            		excFJ.setEFDMAX(EFDmax);
        			
        			//EFDmin
        			double EFDmin= ODMModelStringUtil.getDouble(strAry[6], 0.0);
        			if(!strAry[6].contains(".")){
        				EFDmin=EFDmin/1000;
        			}
        			if(EFDmin>0)ODMLogger.getLogger().warning("the input EFDmin >0, exc info:"+exc.getDesc());
        			excFJ.setEFDMIN(EFDmin);    		
        			//KF
            		double Kf= ODMModelStringUtil.getDouble(strAry[8], 0.0);
            		if(!strAry[8].contains(".")){
        				Kf=Kf/1000;
        			}
            		excFJ.setKF(Kf);
            					
        			// TF
            		double Tf= ODMModelStringUtil.getDouble(strAry[9], 0.0);
            		if(!strAry[9].contains(".")){
            			Tf=Tf/1000;
        			}
            		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
            		if(Kf==0.0&&Tf==0.0)Tf=1.0;
            		excFJ.setTF(BaseDataSetter.createTimeConstSec(Tf));
        			
        			//KC
        			double Kc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
        			if(!strAry[10].contains(".")){
        				Kc=Kc/10000;
        			}
        			excFJ.setKC(Kc);
            	}
            	else if(exc instanceof ExcBPAFKXmlType){ // the same as BPA FK.       		
            		ExcBPAFKXmlType excFK=(ExcBPAFKXmlType)exc;
            		//KF
            		double Kf= ODMModelStringUtil.getDouble(strAry[8], 0.0);
            		if(!strAry[8].contains(".")){
        				Kf=Kf/1000;
        			}
            		excFK.setKF(Kf);
            					
        			// TF
            		double Tf= ODMModelStringUtil.getDouble(strAry[9], 0.0);
            		if(!strAry[9].contains(".")){
            			Tf=Tf/1000;
        			}
            		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
            		if(Kf==0.0&&Tf==0.0)Tf=1.0;
            		excFK.setTF(BaseDataSetter.createTimeConstSec(Tf));
        			//KC
        			double Kc= ODMModelStringUtil.getDouble(strAry[10], 0.0);
        			if(!strAry[10].contains(".")){
        				Kc=Kc/10000;
        			}
        			excFK.setKC(Kc); 
            	}
            	//BPA FA
            	else if(exc instanceof ExcIEEE1981TypeDC1XmlType){ 
            		ExcIEEE1981TypeDC1XmlType excFA=(ExcIEEE1981TypeDC1XmlType)exc;
            		//Se1            		
            		double SE1=ODMModelStringUtil.getDouble(strAry[4], 0.0);
            		if(!strAry[4].contains(".")){
            			SE1=SE1/1000;
        			}
            		excFA.setSE1(SE1);
            		//Se2            		
            		double SE2=ODMModelStringUtil.getDouble(strAry[5], 0.0);
            		if(!strAry[5].contains(".")){
            			SE2=SE2/1000;
        			}
            		excFA.setSE2(SE2);
            		// e1
            		double E1=ODMModelStringUtil.getDouble(strAry[7], 0.0); 
            		if(!strAry[7].contains(".")){
            			E1=E1/1000;
        			}
            		excFA.setE1(E1);
            		// e2
            		double E2=0.75*ODMModelStringUtil.getDouble(strAry[7], 0.0);
            		if(!strAry[7].contains(".")){
            			E2=E2/1000;
        			}
            		excFA.setE2(E2);
            		//Kf
        			double Kf= ODMModelStringUtil.getDouble(strAry[8], 0.0);
        			if(!strAry[8].contains(".")){
        				Kf=Kf/1000;
        			}
        			excFA.setKF(Kf);
            		// TF
            		double Tf= ODMModelStringUtil.getDouble(strAry[9], 0.0);
            		if(!strAry[9].contains(".")){
            			Tf=Tf/1000;
        			}
            		if(Tf==0.0)ODMLogger.getLogger().warning("the input TF=0, machine:"+busId);
            		if(Kf==0.0&&Tf==0.0)Tf=1.0;
            		excFA.setTF(BaseDataSetter.createTimeConstSec(Tf));
        			
            	}
            	else ODMLogger.getLogger().severe("processor for this type excitor is not implmented yet!");
//            	else if(exc.getExciterType().equals(ExciterXmlType.ExciterType.IEEE_1981_TYPE_AC_2)){//BPA FF
//            		           		
//            		//Se1            		
//            		double SE1=ModelStringUtil.getDouble(strAry[4], 0.0);  
//            		exc.getExciterModel().getIEEE1981TypeAC2().setSE1(SE1);
//            		//Se2            		
//            		double SE2=ModelStringUtil.getDouble(strAry[5], 0.0);  
//            		exc.getExciterModel().getIEEE1981TypeAC2().setSE1(SE2);
//            		// e1
//            		double E1=ModelStringUtil.getDouble(strAry[7], 0.0);  
//            		exc.getExciterModel().getIEEE1981TypeAC2().setE1(E1);
//            		// e2
//            		double E2=0.75*ModelStringUtil.getDouble(strAry[7], 0.0);  
//            		exc.getExciterModel().getIEEE1981TypeAC2().setE1(E2);
//            		//Kf
//        			double Kf= ModelStringUtil.getDouble(strAry[8], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKF(Kf);
//            		// TF
//            		double TF= ModelStringUtil.getDouble(strAry[9], 0.0);
//        			XBeanDataSetter.setTimePeriodData(exc.getExciterModel().getIEEE1981TypeAC2().addNewTF(), 
//        					TF, TimePeriodUnitType.SEC);
//        			//Kc
//        			double Kc= ModelStringUtil.getDouble(strAry[10], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKC(Kc);
//            		//Kd
//        			double Kd= ModelStringUtil.getDouble(strAry[11], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKD(Kd);
//            		//Kb
//        			double Kb= ModelStringUtil.getDouble(strAry[12], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKB(Kb);
//            		//Kl
//        			double Kl= ModelStringUtil.getDouble(strAry[13], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKL(Kl);
//            		//Kh
//        			double Kh= ModelStringUtil.getDouble(strAry[14], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setKH(Kh);
//            		//vlr
//        			double vlr= ModelStringUtil.getDouble(strAry[15], 0.0);
//            		exc.getExciterModel().getIEEE1981TypeAC2().setVLR(vlr);
//            	}
        		
        	}
    	else if(str.substring(0, 2).trim().equals("F+")){
    		if(exc instanceof ExcBPAFQXmlType){
    			ExcBPAFQXmlType excFQ=(ExcBPAFQXmlType)exc;
    			//VAMAX 
        		double Vamax= ODMModelStringUtil.getDouble(strAry[4], 0.0);
        		if(!strAry[4].contains(".")){
        			Vamax=Vamax/1000;
    			}
        		excFQ.setVAMAX(Vamax);    		
    			
    			//VAMIN 
        		double Vamin= ODMModelStringUtil.getDouble(strAry[5], 0.0);
        		if(!strAry[5].contains(".")){
        			Vamin=Vamin/1000;
    			}
        		excFQ.setVAMIN(Vamin);
        		
    			//KB
    			double Kb=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    			if(!strAry[6].contains(".")){
    				Kb=Kb/100;
    			}
    			excFQ.setKB(Kb);
        		    			
    			//T5
        		double T5=ODMModelStringUtil.getDouble(strAry[7], 0.0);
        		if(!strAry[7].contains(".")){
        			T5=T5/100;
    			}
        		excFQ.setT5(BaseDataSetter.createTimeConstSec(T5));
        					
    			//KE
        		double Ke=ODMModelStringUtil.getDouble(strAry[8], 0.0);
        		if(!strAry[8].contains(".")){
        			Ke=Ke/100;
    			}
        		excFQ.setKE(Ke);
        			
    			// TE
        		double Te=ODMModelStringUtil.getDouble(strAry[9], 0.0);
        		if(!strAry[9].contains(".")){
        			Te=Te/100;
    			}
        		excFQ.setTE(BaseDataSetter.createTimeConstSec(Te));
        				
    			//SE1-->EfdMax
        		// e1 for SE1 is set latter. 
        		double SE1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
        		if(!strAry[10].contains(".")){
        			SE1=SE1/10000;
    			}
        		excFQ.setSE1(SE1);
        		//SE2-->75%EFDMAX
        		double SE2=ODMModelStringUtil.getDouble(strAry[11], 0.0);
        		if(!strAry[11].contains(".")){
        			SE2=SE2/10000;
    			}
        		excFQ.setSE2(SE2);	
        		
    			// VRMAX
    			double Vrmax= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    			if(!strAry[12].contains(".")){
    				Vrmax=Vrmax/100;
    			}
    			excFQ.setVrmax(Vrmax); 			
    			
    			//VRMIN
    			double Vrmin= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    			if(!strAry[13].contains(".")){
    				Vrmin=Vrmin/100;
    			}
    			if(Vrmin>0){
    				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
    						", automatically change it to: "+(-Vrmin) );
    				Vrmin*=-1;
    			}
    			excFQ.setVrmin(Vrmin);			
    			
    			//KC
        		double Kc=ODMModelStringUtil.getDouble(strAry[14], 0.0);
        		if(!strAry[14].contains(".")){
        			Kc=Kc/100;
    			}
        		excFQ.setKC(Kc);
        		    					
    			//KD
        		double Kd=ODMModelStringUtil.getDouble(strAry[15], 0.0);
        		if(!strAry[15].contains(".")){
        			Kd=Kd/100;
    			}
        		excFQ.setKD(Kd);
        					
    			//KL1
        		double KL1=ODMModelStringUtil.getDouble(strAry[16], 0.0);
        		if(!strAry[16].contains(".")){
        			KL1=KL1/100;
    			}
        		excFQ.setKL1(KL1);
        				
    			//VL1R
        		double VL1R=ODMModelStringUtil.getDouble(strAry[17], 0.0);
        		if(!strAry[17].contains(".")){
        			VL1R=VL1R/100;
    			}
        		excFQ.setVL1R(VL1R);
        					
    			//EFDMAX
        		double EFDMAX=ODMModelStringUtil.getDouble(strAry[18], 0.0);
        		if(!strAry[18].contains(".")){
        			EFDMAX=EFDMAX/100;
    			}
        		excFQ.setEFDmax(EFDMAX);
        		//set e1,e2 for saturation calculation
        		excFQ.setE1(EFDMAX);
        		excFQ.setE2(EFDMAX*0.75);
        	}
    		else if(exc instanceof ExcBPAFRXmlType){
    			ExcBPAFRXmlType excFR=(ExcBPAFRXmlType)exc;
    			//VAMAX 
        		double Vamax= ODMModelStringUtil.getDouble(strAry[4], 0.0);
        		if(!strAry[4].contains(".")){
        			Vamax=Vamax/1000;
    			}
        		excFR.setVAMAX(Vamax);    		
    			
    			//VAMIN 
        		double Vamin= ODMModelStringUtil.getDouble(strAry[5], 0.0);
        		if(!strAry[5].contains(".")){
        			Vamin=Vamin/1000;
    			}
        		excFR.setVAMIN(Vamin);
        		
    			//KB
    			double Kb=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    			if(!strAry[6].contains(".")){
    				Kb=Kb/100;
    			}
    			excFR.setKB(Kb);
        		    			
    			//T5
        		double T5=ODMModelStringUtil.getDouble(strAry[7], 0.0);
        		if(!strAry[7].contains(".")){
        			T5=T5/100;
    			}
        		excFR.setT5(BaseDataSetter.createTimeConstSec(T5));
        					
    			//KE
        		double Ke=ODMModelStringUtil.getDouble(strAry[8], 0.0);
        		if(!strAry[8].contains(".")){
        			Ke=Ke/100;
    			}
        		excFR.setKE(Ke);
        			
    			// TE
        		double Te=ODMModelStringUtil.getDouble(strAry[9], 0.0);
        		if(!strAry[9].contains(".")){
        			Te=Te/100;
    			}
        		excFR.setTE(BaseDataSetter.createTimeConstSec(Te));
        				
    			//SE1-->EfdMax
        		// e1 for SE1 is set latter. 
        		double SE1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
        		if(!strAry[10].contains(".")){
        			SE1=SE1/10000;
    			}
        		excFR.setSE1(SE1);
        		//SE2-->75%EFDMAX
        		double SE2=ODMModelStringUtil.getDouble(strAry[11], 0.0);
        		if(!strAry[11].contains(".")){
        			SE2=SE2/10000;
    			}
        		excFR.setSE2(SE2);	
        		
    			// VRMAX
    			double Vrmax= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    			if(!strAry[12].contains(".")){
    				Vrmax=Vrmax/100;
    			}
    			excFR.setVrmax(Vrmax); 			
    			
    			//VRMIN
    			double Vrmin= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    			if(!strAry[13].contains(".")){
    				Vrmin=Vrmin/100;
    			}
    			if(Vrmin>0){
    				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
    						", automatically change it to: "+(-Vrmin) );
    				Vrmin*=-1;
    			}
    			excFR.setVrmin(Vrmin);			
    			
    			//KC
        		double Kc=ODMModelStringUtil.getDouble(strAry[14], 0.0);
        		if(!strAry[14].contains(".")){
        			Kc=Kc/100;
    			}
        		excFR.setKC(Kc);
        		    					
    			//KD
        		double Kd=ODMModelStringUtil.getDouble(strAry[15], 0.0);
        		if(!strAry[15].contains(".")){
        			Kd=Kd/100;
    			}
        		excFR.setKD(Kd);
        					
    			//KL1
        		double KL1=ODMModelStringUtil.getDouble(strAry[16], 0.0);
        		if(!strAry[16].contains(".")){
        			KL1=KL1/100;
    			}
        		excFR.setKL1(KL1);
        				
    			//VL1R
        		double VL1R=ODMModelStringUtil.getDouble(strAry[17], 0.0);
        		if(!strAry[17].contains(".")){
        			VL1R=VL1R/100;
    			}
        		excFR.setVL1R(VL1R);
        					
    			//EFDMAX
        		double EFDMAX=ODMModelStringUtil.getDouble(strAry[18], 0.0);
        		if(!strAry[18].contains(".")){
        			EFDMAX=EFDMAX/100;
    			}
        		excFR.setEFDmax(EFDMAX);
        		//set e1,e2 for saturation calculation
        		excFR.setE1(EFDMAX);
        		excFR.setE2(EFDMAX*0.75);
    	  } //end of FR
    		else if(exc instanceof ExcBPAFSXmlType){
    			ExcBPAFSXmlType excFS=(ExcBPAFSXmlType)exc;
    			//VAMAX 
        		double Vamax= ODMModelStringUtil.getDouble(strAry[4], 0.0);
        		if(!strAry[4].contains(".")){
        			Vamax=Vamax/1000;
    			}
        		excFS.setVAMAX(Vamax);    		
    			
    			//VAMIN 
        		double Vamin= ODMModelStringUtil.getDouble(strAry[5], 0.0);
        		if(!strAry[5].contains(".")){
        			Vamin=Vamin/1000;
    			}
        		excFS.setVAMIN(Vamin);
        		
    			//KB
    			double Kb=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    			if(!strAry[6].contains(".")){
    				Kb=Kb/100;
    			}
    			excFS.setKB(Kb);
        		    			
    			//T5
        		double T5=ODMModelStringUtil.getDouble(strAry[7], 0.0);
        		if(!strAry[7].contains(".")){
        			T5=T5/100;
    			}
        		excFS.setT5(BaseDataSetter.createTimeConstSec(T5));
        					
    			//KE
        		double Ke=ODMModelStringUtil.getDouble(strAry[8], 0.0);
        		if(!strAry[8].contains(".")){
        			Ke=Ke/100;
    			}
        		excFS.setKE(Ke);
        			
    			// TE
        		double Te=ODMModelStringUtil.getDouble(strAry[9], 0.0);
        		if(!strAry[9].contains(".")){
        			Te=Te/100;
    			}
        		excFS.setTE(BaseDataSetter.createTimeConstSec(Te));
        				
    			//SE1-->EfdMax
        		// e1 for SE1 is set latter. 
        		double SE1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
        		if(!strAry[10].contains(".")){
        			SE1=SE1/10000;
    			}
        		excFS.setSE1(SE1);
        		//SE2-->75%EFDMAX
        		double SE2=ODMModelStringUtil.getDouble(strAry[11], 0.0);
        		if(!strAry[11].contains(".")){
        			SE2=SE2/10000;
    			}
        		excFS.setSE2(SE2);	
        		
    			// VRMAX
    			double Vrmax= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    			if(!strAry[12].contains(".")){
    				Vrmax=Vrmax/100;
    			}
    			excFS.setVrmax(Vrmax); 			
    			
    			//VRMIN
    			double Vrmin= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    			if(!strAry[13].contains(".")){
    				Vrmin=Vrmin/100;
    			}
    			if(Vrmin>0){
    				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
    						", automatically change it to: "+(-Vrmin) );
    				Vrmin*=-1;
    			}
    			excFS.setVrmin(Vrmin);			
    			
    			//KC
        		double Kc=ODMModelStringUtil.getDouble(strAry[14], 0.0);
        		if(!strAry[14].contains(".")){
        			Kc=Kc/100;
    			}
        		excFS.setKC(Kc);
        		    					
    			//KD
        		double Kd=ODMModelStringUtil.getDouble(strAry[15], 0.0);
        		if(!strAry[15].contains(".")){
        			Kd=Kd/100;
    			}
        		excFS.setKD(Kd);
        					
    			//KL1
        		double KL1=ODMModelStringUtil.getDouble(strAry[16], 0.0);
        		if(!strAry[16].contains(".")){
        			KL1=KL1/100;
    			}
        		excFS.setKL1(KL1);
        				
    			//VL1R
        		double VL1R=ODMModelStringUtil.getDouble(strAry[17], 0.0);
        		if(!strAry[17].contains(".")){
        			VL1R=VL1R/100;
    			}
        		excFS.setVL1R(VL1R);
        					
    			//EFDMAX
        		double EFDMAX=ODMModelStringUtil.getDouble(strAry[18], 0.0);
        		if(!strAry[18].contains(".")){
        			EFDMAX=EFDMAX/100;
    			}
        		excFS.setEFDmax(EFDMAX);
        		//set e1,e2 for saturation calculation
        		excFS.setE1(EFDMAX);
        		excFS.setE2(EFDMAX*0.75);
    	  } //end of FS
    		else if(exc instanceof ExcBPAFUXmlType){
    			ExcBPAFUXmlType excFU=(ExcBPAFUXmlType)exc;
    			//VAMAX 
        		double Vamax= ODMModelStringUtil.getDouble(strAry[4], 0.0);
        		if(!strAry[4].contains(".")){
        			Vamax=Vamax/1000;
    			}
        		excFU.setVAMAX(Vamax);    		
    			
    			//VAMIN 
        		double Vamin= ODMModelStringUtil.getDouble(strAry[5], 0.0);
        		if(!strAry[5].contains(".")){
        			Vamin=Vamin/1000;
    			}
        		excFU.setVAMIN(Vamin);
        		        				
    			// VRMAX
    			double Vrmax= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    			if(!strAry[12].contains(".")){
        			Vrmax=Vrmax/100;
    			}
    			excFU.setVrmax(Vrmax); 			
    			
    			//VRMIN
    			double Vrmin= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    			if(!strAry[13].contains(".")){
    				Vrmin=Vrmin/100;
    			}
    			if(Vrmin>0){
    				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
    						", automatically change it to: "+(-Vrmin) );
    				Vrmin*=-1;
    			}
    			excFU.setVrmin(Vrmin);			
    			
    			//KC
        		double KC=ODMModelStringUtil.getDouble(strAry[14], 0.0);
        		if(!strAry[14].contains(".")){
        			KC=KC/100;
    			}
        		excFU.setKC(KC);
    	  } //end of FU
    		else if(exc instanceof ExcBPAFVXmlType){
    			ExcBPAFVXmlType excFV=(ExcBPAFVXmlType)exc;
    			//VAMAX 
        		double Vamax= ODMModelStringUtil.getDouble(strAry[4], 0.0);
        		if(!strAry[4].contains(".")){
        			Vamax=Vamax/1000;
    			}
        		excFV.setVAMAX(Vamax);    		
    			
    			//VAMIN 
        		double Vamin= ODMModelStringUtil.getDouble(strAry[5], 0.0);
        		if(!strAry[5].contains(".")){
        			Vamin=Vamin/1000;
    			}
        		excFV.setVAMIN(Vamin);
        		        				
    			// VRMAX
    			double Vrmax= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    			if(!strAry[12].contains(".")){
        			Vrmax=Vrmax/100;
    			}
    			excFV.setVrmax(Vrmax); 			
    			
    			//VRMIN
    			double Vrmin= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    			if(!strAry[13].contains(".")){
    				Vrmin=Vrmin/100;
    			}
    			if(Vrmin>0){
    				ODMLogger.getLogger().warning("the input Vrmin >0, exc info:"+exc.getDesc()+
    						", automatically change it to: "+(-Vrmin) );
    				Vrmin*=-1;
    			}
    			excFV.setVrmin(Vrmin);			
    			
    			//KC
        		double KC=ODMModelStringUtil.getDouble(strAry[14], 0.0);
        		if(!strAry[14].contains(".")){
        			KC=KC/100;
    			}
        		excFV.setKC(KC);
    	  } //end of FV
    	}//END OF F+
      }
	}
	
	private static int getExcType(String str) {
		/*
		 * 	
    private final static int EA=1;
	private final static int EC=2;
	private final static int EK=3;
	private final static int FA=4;
	private final static int FF=5;
	private final static int FJ=6;
	private final static int FK=7;
	private final static int FQ=8;
	private final static int FR=9;
	private final static int FS=10;
	private final static int FU=11;
	private final static int FV=12;
		 */
		int type = 0;
    	if(str.equals("EA")){
    		type=EA;
    	}else if(str.equals("EC")){
    		type=EC;
    	}else if(str.equals("EK")){
    		type=EK;
    	}else if(str.equals("FJ")){
    		type=FJ;
    	}else if(str.equals("FK")){
    		type=FK;
    	}else if(str.equals("FQ")){
    		type=FQ;
    	}else if(str.equals("FV")){
    		type=FV;
    	}else if(str.equals("FF")){
    		type=FF;
    	}else if(str.equals("FA")){
    		type=FA;
		}else if(str.equals("FR")){
		 type=FR;
	    }else if(str.equals("FS")){
		 type=FS;
	    }else if(str.equals("FU")){
		   type=FU;
	    }
    	return type;
	}
	
	private static String[] getExciterDataFields (String str ) {
    	final String[] strAry = new String[19];	
    	strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
    	//to process the Chinese characters first, if any.
		int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str.substring(3,10).trim());
		//Columns 6-13 busName  
		strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
		
		str=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
    	try{
    		if(str.substring(0,1).trim().equals("E")){
				
				
				//busId
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				//bus Voltage
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				//excId
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
				//TR
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 20).trim();
				//KA for all, KV for EE
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,21, 25).trim();
				//TA for all, TRH for EE
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,26, 29).trim();
				//TA1
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,30, 33).trim();
				//VRminMult, VRmax, VRmin for ED EJ
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,34, 37).trim();
				// KE
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 41).trim();
				//TE
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 45).trim();
				//SE0.75MAX for all, KI for DD
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,46, 49).trim();
				// SEmax for all, Kp for DD
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,50, 53).trim();
				//EFDMin
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,54, 58).trim();
				//EFDMax for all, VNmax for ED
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,59, 62).trim();
				//KF
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,63, 66).trim();
				//TF
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 70).trim();
				// XL for ED
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,71, 75).trim();
				//TF1 for ED
				strAry[18]=ODMModelStringUtil.getStringReturnEmptyString(str,76, 80).trim();
				
			}else if(str.substring(0, 2).trim().equals("FA")||
					str.substring(0, 2).trim().equals("FB")||str.substring(0, 2).trim().equals("FC")
					||str.substring(0, 2).trim().equals("FD")||str.substring(0, 2).trim().equals("FE")||
					str.substring(0, 2).trim().equals("FF")||str.substring(0, 2).trim().equals("FG")
					||str.substring(0, 2).trim().equals("FH")||str.substring(0, 2).trim().equals("FJ")
					||str.substring(0, 2).trim().equals("FK")||str.substring(0, 2).trim().equals("FL")
					){
				
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//busId
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				//bus Voltage
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				//excId
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
				//Rc
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//Xc
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
				//TR
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 31).trim();
				//VIMax for G K L,VAmax for FF
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,32, 36).trim();
				//VIMin for G K L,VAmin for FF
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,37, 41).trim();
				// TB
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 46).trim();
				//TC
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 51).trim();
				//KA, KV for FE
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,52, 56).trim();
				// TA, TRH for FE
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,57, 61).trim();
				//VRmax, Vamax for FH
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,62, 66).trim();					
				//VRmin, Vamin
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 71).trim();
				//KE, KJ for FL
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,72, 76).trim();
				//TE
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
			}else if(str.substring(0, 2).trim().equals("FM")||str.substring(0, 2).trim().equals("FN")
					||str.substring(0, 2).trim().equals("FO")||str.substring(0, 2).trim().equals("FP")
					||str.substring(0, 2).trim().equals("FQ")||str.substring(0, 2).trim().equals("FR")
					||str.substring(0, 2).trim().equals("FS")||str.substring(0, 2).trim().equals("FU")
					||str.substring(0, 2).trim().equals("FV")){
				
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//busId
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				//bus Voltage
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				//excId
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
				//Rc
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 20).trim();
				//Xc
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,21, 24).trim();
				//TR
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,25, 29).trim();
				//K
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,30, 34).trim();
				//Kv
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,35, 37).trim();
				
				// T1
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 42).trim();
				//T2
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
				//K3
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
				// T4
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,53, 57).trim();
				//KA
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,58, 62).trim();
				//TA
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,63, 67).trim();
				//KF
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,68, 72).trim();
				//TF
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,73, 76).trim();
				//KH
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
			}
			else if(str.substring(0,2).trim().equals("FZ")){
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//busId
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				//bus Voltage
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				//excId
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
				//SE1, 
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//SE2, 
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
				//EFDmin
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 31).trim();
				//
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,32, 36).trim();
				//KF
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,37, 41).trim();
				// TF
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 46).trim();
				//KC
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 51).trim();
				//KD
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,52, 56).trim();
				// 
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,57, 61).trim();
				//
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,62, 66).trim();
				//
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 71).trim();
				//
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,72, 76).trim();			
				
			}else if(str.substring(0,2).trim().equals("F+")){
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//busId
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				//bus Voltage
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				//excId
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
				//VAMAX 
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
				//VAMIN 
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
				//KB
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
				//T5
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,31, 34).trim();
				//KE
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,35, 38).trim();
				// TE
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,39, 42).trim();
				//SE1--@EFDMAX
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
				//SE2--@75%EFDMAX
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
				// VRMAX
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,53, 56).trim();
				//VRMIN
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,57, 60).trim();
				//KC
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,61, 64).trim();
				//KD
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,65, 68).trim();	
				//KL1
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,69, 72).trim();
				//VLIR
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,73, 76).trim();
				//EFDMAX
				strAry[18]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
				
			}
    	}catch (Exception e){
    		ODMLogger.getLogger().severe(e.toString());
    	}
        return strAry;
	}
}
