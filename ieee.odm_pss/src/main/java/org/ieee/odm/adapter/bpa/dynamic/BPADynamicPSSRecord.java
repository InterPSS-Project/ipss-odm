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
import org.ieee.odm.schema.PssBPADualInputXmlType;
import org.ieee.odm.schema.PssBpaSgTypeXmlType;
import org.ieee.odm.schema.PssBpaSpTypeXmlType;
import org.ieee.odm.schema.PssBpaSsTypeXmlType;
import org.ieee.odm.schema.StabilizerInputSignalEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BPADynamicPSSRecord {
    private static final Logger log = LoggerFactory.getLogger(BPADynamicPSSRecord.class.getName());
	
	public static void processPSSData(String str, DStabModelParser parser) throws ODMException {
    	final String[] strAry= getPSSDataFields(str);
    	
    	String busId = BPABusRecord.getBusId(strAry[1]);
    	DStabBusXmlType bus = parser.getDStabBus(busId);
    	
    	//DStabGenDataXmlType dynGen = (DStabGenDataXmlType)bus.getGenData().getEquivGen().getValue();   
		DStabGenDataXmlType dynGen = DStabParserHelper.getDefaultGen(bus.getGenData());

    	//machine Id
    	String macId="1";
    	if(!strAry[3].equals("")){
    			macId=strAry[3];
    	}
    	if(str.substring(0, 3).trim().equals("SS")){
    		
    		PssBpaSsTypeXmlType pss = DStabParserHelper.createPssBPASsXmlType(dynGen);
    		pss.setDesc("BPA SS type PSS, machine Id-" + macId);

    		// QV block is rarely used.
    		//KQV
    		double KQV=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")) KQV/=1000;
    		pss.setKQV(KQV);// since the sign of QV block is opposite to that of IEE2ST
    		    		
    		//TQV
    		double TQV=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) TQV/=1000;
    		pss.setTQV(BaseDataSetter.createTimeConstSec(TQV));
    		
    		//KQS
    		double KQS= ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) KQS/=1000;
    		pss.setKQS(KQS);
    		
    		//TQS
    		double TQS= ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) TQS/=1000;
    		pss.setTQS(BaseDataSetter.createTimeConstSec(TQS));
    		
    		//TQ
    		double TQ= ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) TQ/=100;
    		pss.setTQ(BaseDataSetter.createTimeConstSec(TQ));

    		// TQ1
    		double TQ1= ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) TQ1/=1000;
    		pss.setTQ1(BaseDataSetter.createTimeConstSec(TQ1));    		
    		    		
    		//TQ11
    		double TQ11= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) TQ11/=1000;
    		pss.setT1Q1(BaseDataSetter.createTimeConstSec(TQ11));
    		
    		//TQ2
    		double TQ2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) TQ2/=1000;
    		pss.setTQ2(BaseDataSetter.createTimeConstSec(TQ2));
    		
    		// TQ21
    		double TQ21= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) TQ21/=1000;
    		pss.setT1Q2(BaseDataSetter.createTimeConstSec(TQ21));
    		
    		//TQ3
    		double TQ3=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) TQ3/=1000;
    		pss.setTQ3(BaseDataSetter.createTimeConstSec(TQ3));
    		   		    		
    		//TQ31
    		double TQ31=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) TQ31/=1000;
    		pss.setT1Q3(BaseDataSetter.createTimeConstSec(TQ31));
    		    		
    		//VSMAX
    		double vsmax=ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")) vsmax/=1000;
    		pss.setVSMAX(vsmax);
    			
    		//VCUTOFF
    		double vcut=ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")) vcut/=1000;
    		pss.setVCUTOFF(vcut);
    		    		
    		//VSLOW
    		double vsmin=0.0;
    		double Vslow=ODMModelStringUtil.getDouble(strAry[17], 0.0);
    		if(!strAry[17].contains(".")) Vslow/=100;
    		   		
    		if(Vslow<=0){
    			vsmin=-vsmax;
    		}else {
    			vsmin=-Vslow;
    		}
    		pss.setVSMIN(vsmin);
    		
            //TODO there is no corresponding element in IEE2ST model		    		
//    		//KQS MVAbase for SP SG
//    		double kqsMvaBase=ModelStringUtil.getDouble(strAry[19], 0.0);
    	}
    	else if(str.substring(0, 3).trim().equals("SP")){
    		
    		PssBpaSpTypeXmlType pss = DStabParserHelper.createPssBPASpXmlType(dynGen);
    		pss.setDesc("BPA SP type PSS, machine Id-" + macId);

    		// QV block is rarely used.
    		//KQV
    		double KQV=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[5].contains(".")) KQV/=1000;
    		pss.setKQV(KQV);// since the sign of QV block is opposite to that of IEE2ST
    		    		
    		//TQV
    		double TQV=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) TQV/=1000;
    		pss.setTQV(BaseDataSetter.createTimeConstSec(TQV));
    		
    		//KQS
    		double KQS= ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) KQS/=1000;
    		pss.setKQS(KQS);
    		
    		//TQS
    		double TQS= ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) TQS/=1000;
    		pss.setTQS(BaseDataSetter.createTimeConstSec(TQS));
    		
    		//TQ
    		double TQ= ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) TQ/=100;
    		pss.setTQ(BaseDataSetter.createTimeConstSec(TQ));

    		// TQ1
    		double TQ1= ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) TQ1/=1000;
    		pss.setTQ1(BaseDataSetter.createTimeConstSec(TQ1));    		
    		    		
    		//TQ11
    		double TQ11= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) TQ11/=1000;
    		pss.setT1Q1(BaseDataSetter.createTimeConstSec(TQ11));
    		
    		//TQ2
    		double TQ2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) TQ2/=1000;
    		pss.setTQ2(BaseDataSetter.createTimeConstSec(TQ2));
    		
    		// TQ21
    		double TQ21= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) TQ21/=1000;
    		pss.setT1Q2(BaseDataSetter.createTimeConstSec(TQ21));
    		
    		//TQ3
    		double TQ3=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) TQ3/=1000;
    		pss.setTQ3(BaseDataSetter.createTimeConstSec(TQ3));
    		   		    		
    		//TQ31
    		double TQ31=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) TQ31/=1000;
    		pss.setT1Q3(BaseDataSetter.createTimeConstSec(TQ31));
    		    		
    		//VSMAX
    		double vsmax=ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")) vsmax/=1000;
    		pss.setVSMAX(vsmax);
    			
    		//VCUTOFF
    		double vcut=ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")) vcut/=1000;
    		pss.setVCUTOFF(vcut);
    		    		
    		//VSLOW
    		double vsmin=0.0;
    		double Vslow=ODMModelStringUtil.getDouble(strAry[17], 0.0);
    		if(!strAry[17].contains(".")) Vslow/=100;
    		   		
    		if(Vslow<=0){
    			vsmin=-vsmax;
    		}else {
    			vsmin=-Vslow;
    		}
    		pss.setVSMIN(vsmin);    		
//    		//KQS MVAbase for SP SG
//    		double kqsMvaBase=ModelStringUtil.getDouble(strAry[19], 0.0);
    		
    		
    	}
    	else if(str.substring(0, 3).trim().equals("SG")){
    		
    		PssBpaSgTypeXmlType pss = DStabParserHelper.createPssBPASgXmlType(dynGen);
    		pss.setDesc("BPA SP type PSS, machine Id-" + macId);

    		// QV block is rarely used.
    		//KQV
    		double KQV=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[5].contains(".")) KQV/=1000;
    		pss.setKQV(KQV);// since the sign of QV block is opposite to that of IEE2ST
    		    		
    		//TQV
    		double TQV=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) TQV/=1000;
    		pss.setTQV(BaseDataSetter.createTimeConstSec(TQV));
    		
    		//KQS
    		double KQS= ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) KQS/=1000;
    		pss.setKQS(KQS);
    		
    		//TQS
    		double TQS= ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) TQS/=1000;
    		pss.setTQS(BaseDataSetter.createTimeConstSec(TQS));
    		
    		//TQ
    		double TQ= ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) TQ/=100;
    		pss.setTQ(BaseDataSetter.createTimeConstSec(TQ));

    		// TQ1
    		double TQ1= ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) TQ1/=1000;
    		pss.setTQ1(BaseDataSetter.createTimeConstSec(TQ1));    		
    		    		
    		//TQ11
    		double TQ11= ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) TQ11/=1000;
    		pss.setT1Q1(BaseDataSetter.createTimeConstSec(TQ11));
    		
    		//TQ2
    		double TQ2= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) TQ2/=1000;
    		pss.setTQ2(BaseDataSetter.createTimeConstSec(TQ2));
    		
    		// TQ21
    		double TQ21= ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) TQ21/=1000;
    		pss.setT1Q2(BaseDataSetter.createTimeConstSec(TQ21));
    		
    		//TQ3
    		double TQ3=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) TQ3/=1000;
    		pss.setTQ3(BaseDataSetter.createTimeConstSec(TQ3));
    		   		    		
    		//TQ31
    		double TQ31=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) TQ31/=1000;
    		pss.setT1Q3(BaseDataSetter.createTimeConstSec(TQ31));
    		    		
    		//VSMAX
    		double vsmax=ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")) vsmax/=1000;
    		pss.setVSMAX(vsmax);
    			
    		//VCUTOFF
    		double vcut=ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")) vcut/=1000;
    		pss.setVCUTOFF(vcut);
    		    		
    		//VSLOW
    		double vsmin=0.0;
    		double Vslow=ODMModelStringUtil.getDouble(strAry[17], 0.0);
    		if(!strAry[17].contains(".")) Vslow/=100;
    		   		
    		if(Vslow<=0){
    			vsmin=-vsmax;
    		}else {
    			vsmin=-Vslow;
    		}
    		pss.setVSMIN(vsmin);		    		
//    		//TODO KQS MVAbase for SP SG
            double kqsMvaBase=ODMModelStringUtil.getDouble(strAry[19], 0.0);
            
            if(kqsMvaBase>0.0){
            	double newKqs=pss.getKQS()*kqsMvaBase/dynGen.getMvaBase().getValue();
            	pss.setKQS(newKqs);
            }
    	}
    	else if(str.substring(0, 3).trim().equals("SI")){
    		PssBPADualInputXmlType dualPss= DStabParserHelper.createPssBPADualInputXmlType(dynGen);
    		
    		dualPss.setDesc("BPA SI Type Dual Input Pss model,machine Id-" + macId);

    		//TRW
    		double  Trw=ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")) Trw/=10000;
    		dualPss.setTrw(BaseDataSetter.createTimeConstSec(Trw));    		
    		
    		//T5
    		double  T5=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) T5/=1000;
    		dualPss.setT5(BaseDataSetter.createTimeConstSec(T5));
    		//T6
    		double  T6=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) T6/=1000;
    		dualPss.setT6(BaseDataSetter.createTimeConstSec(T6));
    		
    		//T7
    		double  T7=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) T7/=1000;
    		dualPss.setT7(BaseDataSetter.createTimeConstSec(T7));
    		
    		//KR
    		double Kr= ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) Kr/=10000;
    		dualPss.setKr(Kr);  		
    		// TRP
    		double  Trp=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) Trp/=10000;
    		dualPss.setTrp(BaseDataSetter.createTimeConstSec(Trp));
    		
    		//TW
    		double  Tw=ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) Tw/=1000;
    		dualPss.setTW(BaseDataSetter.createTimeConstSec(Tw));
    		
    		//TW1
    		double  Tw1=ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) Tw1/=1000;
    		dualPss.setTW1(BaseDataSetter.createTimeConstSec(Tw1));
    		
    		// TW2
    		double  Tw2=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) Tw2/=1000;
    		dualPss.setTW2(BaseDataSetter.createTimeConstSec(Tw2));
    		
    		//KS
    		double Ks= ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(!strAry[13].contains(".")) Ks/=100;
    		dualPss.setKS(Ks);    	
    		//T9
    		double  T9=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(!strAry[14].contains(".")) T9/=1000;
    		dualPss.setT9(BaseDataSetter.createTimeConstSec(T9));
    		
    		//T10
    		double T10=ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")) T10/=1000;
    		dualPss.setT10(BaseDataSetter.createTimeConstSec(T10));
    		
    		//T12
    		double T12=ODMModelStringUtil.getDouble(strAry[16], 0.0);
    		if(!strAry[16].contains(".")) T12/=1000;
    		dualPss.setT12(BaseDataSetter.createTimeConstSec(T12));
    	    
    		//INP input signal:0 for speed deviation(delta_w) and generator accelerating power(delta_Pg), 
    		//1 for only delta_w, 2 for only delta_pg
    		int INP=ODMModelStringUtil.getInt(strAry[17], 0);
    		    		
    		if(INP==0){
    			dualPss.setFirstInputSignal(StabilizerInputSignalEnumType.ROTOR_SPEED_DEVIATION );
    			dualPss.setSecondInputSignal(StabilizerInputSignalEnumType.GENERATOR_ACCELERATING_POWER);
    		}else if(INP==1){
    			dualPss.setFirstInputSignal(StabilizerInputSignalEnumType.ROTOR_SPEED_DEVIATION );
    		}else if(INP==2){
    			dualPss.setSecondInputSignal(StabilizerInputSignalEnumType.GENERATOR_ACCELERATING_POWER);
    		}    	
    	}
    	// SI+ is to store the rest data of DualInputPss Model
    	else if(str.substring(0, 3).trim().equals("SI+")){ 
    		PssBPADualInputXmlType dualPss=(PssBPADualInputXmlType) dynGen.getStabilizer().getValue();

    		//KP
    		double Kp= ODMModelStringUtil.getDouble(strAry[4], 0.0);
    		if(!strAry[4].contains(".")) Kp/=1000;
    		dualPss.setKp(Kp);
    		//T1
    		double T1=ODMModelStringUtil.getDouble(strAry[5], 0.0);
    		if(!strAry[5].contains(".")) T1/=1000;
    		dualPss.setT1(BaseDataSetter.createTimeConstSec(T1));
   		
    		//T2
    		double T2=ODMModelStringUtil.getDouble(strAry[6], 0.0);
    		if(!strAry[6].contains(".")) T2/=1000;
    		dualPss.setT2(BaseDataSetter.createTimeConstSec(T2));
    		
    		//T13
    		double T13=ODMModelStringUtil.getDouble(strAry[7], 0.0);
    		if(!strAry[7].contains(".")) T13/=1000;
    		dualPss.setT13(BaseDataSetter.createTimeConstSec(T13));
    	
    		//T14
    		double T14=ODMModelStringUtil.getDouble(strAry[8], 0.0);
    		if(!strAry[8].contains(".")) T14/=1000;
    		dualPss.setT14(BaseDataSetter.createTimeConstSec(T14));
    		
    		// T3
    		double T3=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")) T3/=1000;
    		dualPss.setT3(BaseDataSetter.createTimeConstSec(T3));
    		
    		//T4
    		double T4=ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")) T4/=1000;
    		dualPss.setT4(BaseDataSetter.createTimeConstSec(T4));
    		
    		//VSMAX
    		double vsmax= ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")) vsmax/=10000;
    		dualPss.setVSMAX(vsmax);
    		
    		// VSMIN
    		double vsmin=ODMModelStringUtil.getDouble(strAry[12], 0.0);
    		if(!strAry[12].contains(".")) vsmin/=10000;
    		dualPss.setVSMIN(vsmin);
    		
    		//krBaseMVA-- the base MVA for the Kr parameter
    		//TODO still don't know how this parameter is used in the dynamic analysis
    		double krBaseMVA=ODMModelStringUtil.getDouble(strAry[13], 0.0);
    		if(krBaseMVA==0.0){
    			krBaseMVA=dynGen.getMvaBase().getValue();
    		}
    		dualPss.setKrBaseMVA(krBaseMVA);
   		}
    }
	
	private static String[] getPSSDataFields(String str){
        final String[] strAry= new String[20];
        strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
        //to process the Chinese characters first, if any.
        int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str.substring(3,10).trim());
        //Columns 6-13 busName  
        strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
        
        str=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
        try{
            if(str.substring(0, 3).trim().equals("SS")||str.substring(0, 3).trim().equals("SP")
        			||str.substring(0, 3).trim().equals("SG")){
        		//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
        		//busId
        		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
        		//bus Voltage
        		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
        		//excId
        		strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
        		//KQV 
        		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 20).trim();
        		//TQV
        		strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,21, 23).trim();
        		//KQS
        		strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,24, 27).trim();
        		//TQS
        		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,28, 30).trim();
        		//TQ
        		strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,31, 34).trim();
        		// TQ1
        		strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,35, 38).trim();
        		//TQ11
        		strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,39, 42).trim();
        		//TQ2
        		strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 46).trim();
        		// TQ21
        		strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 50).trim();
        		//TQ3
        		strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,51, 54).trim();
        		//TQ31
        		strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,55, 58).trim();
        		//VSMAX
        		strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,59, 62).trim();	
        		//VCUTOFF
        		strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,63, 66).trim();
        		//VSLOW
        		strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 68).trim();
        		//REMOTE BUS
        		strAry[18]=ODMModelStringUtil.getStringReturnEmptyString(str,69, 76).trim();
        		//REMOTE VOLTAGE,  KQS MVAbase for SP SG
        		strAry[19]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
        		
        	}else if(str.substring(0, 3).trim().equals("SI")){
        		
        		//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
        		//busId
        		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
        		//bus Voltage
        		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
        		//excId
        		strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
        		//TRW
        		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 20).trim();
        		//T5
        		strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,21, 25).trim();
        		//T6
        		strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,26, 30).trim();
        		//T7
        		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,31, 35).trim();
        		//KR
        		strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,36, 41).trim();
        		// TRP
        		strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 45).trim();
        		//TW
        		strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,46, 50).trim();
        		//TW1
        		strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,51, 55).trim();
        		// TW2
        		strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,56, 60).trim();
        		//KS
        		strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,61, 64).trim();
        		//T9
        		strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,65, 69).trim();
        		//T10
        		strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str,70, 74).trim();	
        		//T12
        		strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str,75, 79).trim();
        		//INP input signal:0for w and Pg, 1 for w, 2for pg
        		strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str,80, 80).trim();
        		
        		
        	}else if(str.substring(0, 3).trim().equals("SI+")){
        		//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 3).trim();
        		//busId
        		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
        		//bus Voltage
        		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
        		//excId
        		strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 16).trim();
        		//KP
        		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,17, 21).trim();
        		//T1
        		strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
        		//T2
        		strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 31).trim();
        		//T13
        		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,32, 36).trim();
        		//T14
        		strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,37, 41).trim();
        		// T3
        		strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 46).trim();
        		//T4
        		strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,47, 51).trim();
        		//VSMAX
        		strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,52, 57).trim();
        		// VSMIN
        		strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,58, 63).trim();
        		//KMVA, MVAbase for kr in SI 
        		strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,77, 80).trim();
        		
        	}
    	}catch (Exception e){
            log.error(e.toString());
    	}
    	
    	    	
    	return strAry;
    }

}