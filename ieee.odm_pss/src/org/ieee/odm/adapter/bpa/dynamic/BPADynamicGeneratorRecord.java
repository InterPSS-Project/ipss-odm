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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq11MachineXmlType;
import org.ieee.odm.schema.Eq1Ed1MachineXmlType;
import org.ieee.odm.schema.Eq1MachineXmlType;
import org.ieee.odm.schema.EquiMachineXmlType;
import org.ieee.odm.schema.VoltageUnitType;



public class BPADynamicGeneratorRecord {
	
	private static List<String> subTransBusIdList=new ArrayList<String>();
	private static Hashtable<String,Double>SubTransientData =new Hashtable<String,Double>();
	
	public static void processGeneratorData(String str, DStabModelParser parser) throws ODMException {
    	final String strAry[]=getGeneratorDataFields(str);
    	
    	DStabNetXmlType net = parser.getDStabNet();
    	
    	if (str.substring(0,2).trim().equals("MC")){
    		String busId = BPABusRecord.getBusId(strAry[1]);
        	DStabBusXmlType bus = parser.getDStabBus(busId);
        	DStabGenDataXmlType dynGen = (DStabGenDataXmlType)bus.getGenData().getContributeGen().get(0).getValue();
    		ClassicMachineXmlType mach = DStabParserHelper.createClassicMachine(dynGen);
    		
    		double ratedVoltage=ODMModelStringUtil.getDouble(strAry[2], 0.0);
	   		dynGen.setRatedMachVoltage(DStabDataSetter.createVoltageValue(ratedVoltage, VoltageUnitType.KV));
	   		
	   		String dynGenId="1";
    		if(!strAry[3].equals("")){
    			dynGenId=strAry[3];    			
    		}
	   		dynGen.setId(dynGenId);
	   		
	   		double Emws=ODMModelStringUtil.getDouble(strAry[4], 0.0);
	   		double MvaBase=ODMModelStringUtil.getDouble(strAry[7], net.getBasePower().getValue());
			// infinite bus
			if(Emws==999999){
				mach.setH(999999);				
			}else{
				double h=0.0;
				if(Emws!=0.0){
					h=Emws/MvaBase;
					NumberFormat ddf1= NumberFormat.getInstance();
					ddf1.setMaximumFractionDigits(4);
					h= new Double(ddf1.format(h)).doubleValue();
					mach.setH(h);
				}
			}
		    double pContri=ODMModelStringUtil.getDouble(strAry[5], 100.0);//% in InterPSS
			double qContri=ODMModelStringUtil.getDouble(strAry[6], 100.0);
			dynGen.setPContributionPercent(pContri);
			dynGen.setQContributionPercent(qContri);
			dynGen.setMvaBase(DStabDataSetter.createApparentPower(MvaBase, ApparentPowerUnitType.MVA));
			
			double xd1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				xd1=xd1/10000;
			}
    		mach.setXd1(xd1);
    			
    		double D=ODMModelStringUtil.getDouble(strAry[18], 0.0);// Recommended value is 2
    		if(!strAry[18].contains(".")){
    			D=D/10000;
    		}
			mach.setD(D*2);//DIPSS=DBPA*2
    	}
    	/*
    	 * M record is only to store the sub-transient info, 
    	 * this, together with MF/MG record,represents a full machine model considering damper.
    	 */
    	
    	else if(str.substring(0, 2).trim().equals("M")){
    		String busId = BPABusRecord.getBusId(strAry[1]);
    		subTransBusIdList.add(busId);
    		
    		double xd11=ODMModelStringUtil.getDouble(strAry[8], 0.0); 
    		if(!strAry[8].contains(".")){
    			xd11=xd11/10000;
    		}
    		SubTransientData.put("xd11", xd11);
    		
    		
    		double xq11=ODMModelStringUtil.getDouble(strAry[9], 0.0);
    		if(!strAry[9].contains(".")){
    			xq11=xq11/10000;
    		}
    		SubTransientData.put("xq11", xq11);
    		
    		
    		double td011=ODMModelStringUtil.getDouble(strAry[10], 0.0);
    		if(!strAry[10].contains(".")){
    			td011=td011/10000;
    		}
    		SubTransientData.put("td011", td011);
    		
    		double tq011=ODMModelStringUtil.getDouble(strAry[11], 0.0);
    		if(!strAry[11].contains(".")){
    			tq011=tq011/10000;
    		}
    		SubTransientData.put("tq011", tq011);
    	}
    	/*
    	 * only MF record(there is no M record prior to MF record) 
    	 * represents a transient type machine model(Eq1Ed1)
    	 * while M and MF records together represent sub-transient;
    	 */
    	else if(str.substring(0, 2).trim().equals("MF")){
    		String busId = BPABusRecord.getBusId(strAry[1]);
        	
    		DStabBusXmlType bus = parser.getDStabBus(busId);
        	
    		String dynGenId="1";
    		if(!strAry[3].equals("")){
    		    dynGenId=strAry[3];
    		}
    		DStabGenDataXmlType dynGen = (DStabGenDataXmlType)bus.getGenData().getContributeGen().get(0).getValue();
    		dynGen.setId(dynGenId);
			double ratedVoltage=ODMModelStringUtil.getDouble(strAry[2], 0.0);
		   	dynGen.setRatedMachVoltage(DStabDataSetter.createVoltageValue(ratedVoltage, VoltageUnitType.KV));
    		
		   	Eq1MachineXmlType mach=null; 
	        boolean isEq1Ed1=true;
	        boolean isSalient=false;
    		double tq01=ODMModelStringUtil.getDouble(strAry[14], 0.0);
    		if(tq01<1E-5){// Tq01==0.0
    			isEq1Ed1=false;
    			isSalient=true;
    			mach=DStabParserHelper.createEq1Machine(dynGen);
    		}
    		else mach=DStabParserHelper.createEq1Ed1Machine(dynGen);
    		
      
    		if(isSubTransientModel(busId)){
    			if(isSalient) mach = DStabParserHelper.createEq11Machine(dynGen);
    			else mach=DStabParserHelper.createEq11Ed11Machine(dynGen);
    			// set the subTransient data saved before in the hashtable to machine.
                setSubTransientData(mach);
			}					   		
			double pContri=ODMModelStringUtil.getDouble(strAry[5], 100.0);//% in InterPSS
			double qContri=ODMModelStringUtil.getDouble(strAry[6], 100.0);
			dynGen.setPContributionPercent(pContri);
			dynGen.setQContributionPercent(qContri);
						
			double MvaBase=ODMModelStringUtil.getDouble(strAry[7], net.getBasePower().getValue());
			dynGen.setMvaBase(DStabDataSetter.createApparentPower(MvaBase, ApparentPowerUnitType.MVA));
			//TODO Mike,  this is the baseMVA for the per unit system in BPA, I don't think this is the same as the rated power of a machine. 
			// sometimes, these two are set differently, such as the baseMVA would be chosen equal to system baseMVA .
			
			double Emws=ODMModelStringUtil.getDouble(strAry[4], 0.0);	
			double h=0.0;
			if(Emws!=0.0){
				h=Emws/MvaBase;
				NumberFormat ddf1= NumberFormat.getInstance();
				ddf1.setMaximumFractionDigits(4);
				h= new Double(ddf1.format(h)).doubleValue();
				mach.setH(h);
			}
			
			double ra=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			if(!strAry[8].contains(".")){
				ra=ra/10000;
			}
			mach.setRa(ra);	    			
    		
			double xd1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			if(!strAry[9].contains(".")){
				xd1=xd1/10000;
			}
			mach.setXd1(xd1);	    			
    		
			double xq1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[10].contains(".")){
				xq1=xq1/10000;
			}
			if(isEq1Ed1){
				((Eq1Ed1MachineXmlType)mach).setXq1(xq1);
			}
				    			
    		
			double xd=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			if(!strAry[11].contains(".")){
				xd=xd/10000;
			}
			mach.setXd(xd);	    			
    		
			double xq=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[12].contains(".")){
				xq=xq/10000;
			}
			mach.setXq(xq);	    			
    		
			double td01=ODMModelStringUtil.getDouble(strAry[13], 0.0);
			if(!strAry[13].contains(".")){
				td01=td01/100;
			}
			mach.setTd01(DStabDataSetter.createTimeConstSec(td01));
    		
    		
    		if(!strAry[14].contains(".")){
    			tq01=tq01/100;
    		}
    		if(isEq1Ed1) ((Eq1Ed1MachineXmlType)mach).setTq01(DStabDataSetter.createTimeConstSec(tq01));	 
    		
    		double xl=ODMModelStringUtil.getDouble(strAry[15], 0.0);
    		if(!strAry[15].contains(".")){
    			xl=xl/10000;
    		}
    		if(xl<1E-6) xl=0.95*xd1;// set a default value, also a BPA approach.
			mach.setXl(xl); 
    			    		
    		Eq1MachineXmlType.SeFmt1 seFmt1 = DStabParserHelper.createMachineSeFmt1();
			double SE1=ODMModelStringUtil.getDouble(strAry[16], 0.0);
			if(!strAry[16].contains(".")){
				SE1=SE1/10000;
			}
			double SE2=ODMModelStringUtil.getDouble(strAry[17], 0.0);
			if(!strAry[17].contains(".")){
				SE2=SE2/1000;
			}

			// if SE1 or SE2 not defined, use the default value
			if (SE1 > 0.0 && SE2 > 0.0) {
				seFmt1.setSe100(SE1*100);//SE1% in InterPSS.			
				seFmt1.setSe120(SE2*100);
				seFmt1.setSliner(0.8);
			}
			mach.setSeFmt1(seFmt1);	
			
				
			double D=ODMModelStringUtil.getDouble(strAry[18], 0.0);
			if(!strAry[18].contains(".")){
				D=D/100;				
			}
			mach.setD(D*2);//DIPSS=DBPA*2
			
		}
    	else if(str.substring(0, 2).trim().equals("LN")){    		
    		String busId1="";
			if(!strAry[1].equals("")){
				busId1 = BPABusRecord.getBusId(strAry[1]);
			}
			double Vol1=ODMModelStringUtil.getDouble(strAry[2], 0.0);
			
			if(!busId1.equals("")&&Vol1!=0.0){
		    	DStabBusXmlType bus1 = parser.getDStabBus(busId1);
		    	DStabGenDataXmlType dynGen = (DStabGenDataXmlType)bus1.getGenData().getContributeGen().get(0).getValue();
				dynGen.setRatedMachVoltage(DStabDataSetter.createVoltageValue(Vol1, VoltageUnitType.KV));
				EquiMachineXmlType mach = DStabParserHelper.createEquiMachine(dynGen);
				
				EquiMachineXmlType.EquivGen equGen =OdmObjFactory.createEquiMachineXmlTypeEquivGen();
				if(bus1.getGenData()!=null){
					double pGen=bus1.getGenData().getContributeGen().get(0).getValue().getPower().getRe();
					equGen.setEquiPgen(pGen);//TODO why only pGen, for equivalence, qGen should be included
					equGen.setPGenUnit(ApparentPowerUnitType.MVA);
					mach.setEquivGen(equGen);
				}else{
					equGen.setDCLineBus(true);
					mach.setEquivGen(equGen);
				}
			}
			
//			String busId2="";
//			if(!strAry[3].equals("")){
//				busId2=BusRecord.getBusId(strAry[3]);
//			}
//			double Vol2=ModelStringUtil.getDouble(strAry[4], 0.0);
//			
//			String busId3="";
//			if(!strAry[5].equals("")){
//				busId3=BusRecord.getBusId(strAry[5]);
//			}
//			double Vol3=ModelStringUtil.getDouble(strAry[6], 0.0);
//			
//			String busId4="";
//			if(!strAry[7].equals("")){
//				busId4=BusRecord.getBusId(strAry[7]);
//			}
//			double Vol4=ModelStringUtil.getDouble(strAry[8], 0.0);
//			
//			String busId5="";
//			if(!strAry[9].equals("")){
//				busId5=BusRecord.getBusId(strAry[9]);
//			}
//			double Vol5=ModelStringUtil.getDouble(strAry[10], 0.0);
    	}    	
    }
	
	private static String[] getGeneratorDataFields ( final String str) {
		final String[] strAry = new String[19];
		
		try{
			if(str.substring(0, 2).trim().equals("M")){
				strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				
				//to process the Chinese characters first, if any.
				int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str.substring(3,10).trim());
				//Columns 6-13 busName  
				strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
				
				String str2=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
				
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str2,12, 15).trim();
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str2,16, 16).trim();
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str2,17, 21).trim();
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str2,23, 25).trim();
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str2,31, 32).trim();
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str2,34, 36).trim();
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str2,38, 42).trim();
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str2,43, 47).trim();
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str2,48, 51).trim();
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str2,52, 55).trim();				
			}
			else if(str.substring(0, 2).trim().equals("MC")||str.substring(0, 2).trim().equals("MF")){
				strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//to process the Chinese characters first, if any.
				int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str.substring(3,10).trim());;
				//Columns 6-13 busName  
				strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
				
				String str2=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str2,4, 11).trim();
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str2,12, 15).trim();
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str2,16, 16).trim();
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str2,17, 22).trim();
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str2,23, 25).trim();
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str2,26, 28).trim();
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str2,29, 32).trim();
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str2,33, 36).trim();
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str2,37, 41).trim();
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str2,42, 46).trim();
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str2,47, 51).trim();
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str2,52, 56).trim();
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str2,57, 60).trim();
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str2,61, 63).trim();
				strAry[15]=ODMModelStringUtil.getStringReturnEmptyString(str2,64, 68).trim();
				strAry[16]=ODMModelStringUtil.getStringReturnEmptyString(str2,69, 73).trim();
				strAry[17]=ODMModelStringUtil.getStringReturnEmptyString(str2,74, 77).trim();
				strAry[18]=ODMModelStringUtil.getStringReturnEmptyString(str2,78, 80).trim();
			}
			else if(str.substring(0, 2).trim().equals("LN")){
				strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				//to process the Chinese characters first, if any.
				int chineseCharNum=ODMModelStringUtil.getChineseCharNum(str);
				//Columns 6-13 busName  
				strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();
				
				String str2=chineseCharNum==0?str:ODMModelStringUtil.replaceChineseChar(str);
				//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 11).trim();
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,34, 41).trim();
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,42, 45).trim();
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,49, 56).trim();
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,57, 60).trim();
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,64, 71).trim();
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,72, 75).trim();
			}
		}catch(Exception e){
			ODMLogger.getLogger().severe(e.toString());
		}
		
		return strAry;
    }
	
	private static boolean isSubTransientModel(String busId){
		return subTransBusIdList.contains(busId);
	}
	private static void setSubTransientData(Eq1MachineXmlType mach){
		if(mach instanceof Eq11MachineXmlType){
			((Eq11MachineXmlType)mach).setXd11(SubTransientData.get("xd11"));
			((Eq11MachineXmlType)mach).setTd011(DStabDataSetter.createTimeConstSec(SubTransientData.get("td011")));
			((Eq11MachineXmlType)mach).setXq11(SubTransientData.get("xq11"));
			((Eq11MachineXmlType)mach).setTq011(DStabDataSetter.createTimeConstSec(SubTransientData.get("tq011")));
		}
		else if(mach instanceof Eq11Ed11MachineXmlType){
			((Eq11Ed11MachineXmlType)mach).setXd11(SubTransientData.get("xd11"));
			((Eq11Ed11MachineXmlType)mach).setTd011(DStabDataSetter.createTimeConstSec(SubTransientData.get("td011")));
			((Eq11Ed11MachineXmlType)mach).setXq11(SubTransientData.get("xq11"));
			((Eq11Ed11MachineXmlType)mach).setTq011(DStabDataSetter.createTimeConstSec(SubTransientData.get("tq011")));
		}
	}
}