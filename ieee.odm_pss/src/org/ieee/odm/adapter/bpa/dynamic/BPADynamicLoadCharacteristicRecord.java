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
 * v1.1
 * Data 06/05/2011
 *
 */

package org.ieee.odm.adapter.bpa.dynamic;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.DynamicLoadIEEEStaticLoadXmlType;
import org.ieee.odm.schema.DynamicLoadModelSelectionXmlType;
import org.ieee.odm.schema.LoadCharacteristicLocationEnumType;

public class BPADynamicLoadCharacteristicRecord {
	
public static void processLoadCharacteristicData(String str, DStabModelParser parser) throws ODMException {
	final String[] strAry= getLoadDataFields(str); 	

	DStabBusXmlType bus = null;

	if(strAry[0].equals("LA")||strAry[0].equals("LB")){
		
		DynamicLoadIEEEStaticLoadXmlType ieeeStaLoad=odmObjFactory.createDynamicLoadIEEEStaticLoadXmlType();
		
		double a1=0.0;
		if(!strAry[5].equals("")){
			a1= new Double(strAry[5]).doubleValue();
			ieeeStaLoad.setA1(a1);
			ieeeStaLoad.setN1(2);
		}

		//qz
		double a4=0.0;
		if(!strAry[6].equals("")){
			a4= new Double(strAry[6]).doubleValue();
			ieeeStaLoad.setA4(a4);
			ieeeStaLoad.setN4(2);
		}			
		
		//pi
		double a2=0.0;
		if(!strAry[7].equals("")){
			a2= new Double(strAry[7]).doubleValue();
			ieeeStaLoad.setA2(a2);
			ieeeStaLoad.setN2(1);
		}
		//qi
		double a5=0.0;
		if(!strAry[8].equals("")){
			a5= new Double(strAry[8]).doubleValue();
			ieeeStaLoad.setA5(a5);
			ieeeStaLoad.setN5(1);
		}
		// pp
		double a3=0.0;
		if(!strAry[9].equals("")){
			a3= new Double(strAry[9]).doubleValue();
			ieeeStaLoad.setA3(a3);
			ieeeStaLoad.setN3(0);
		}
		//qp
		double a6=0.0;
		if(!strAry[10].equals("")){
			a6= new Double(strAry[10]).doubleValue();
			ieeeStaLoad.setA6(a6);
			ieeeStaLoad.setN6(0);
		}			
		//pf
		double a9=0.0;
		if(!strAry[11].equals("")){
			a9= new Double(strAry[11]).doubleValue();
			ieeeStaLoad.setA9(a9);				
		}
		// qf
		double a10=0.0;
		if(!strAry[12].equals("")){
			a10= new Double(strAry[12]).doubleValue();
			ieeeStaLoad.setA10(a10);				
		}
		//Ldp
		double a7=0.0;
		if(!strAry[13].equals("")){
			a7= new Double(strAry[13]).doubleValue();
			ieeeStaLoad.setA7(a7);				
		}		
		//Ldq
		double a8=0.0;
		if(!strAry[14].equals("")){
			a8= new Double(strAry[14]).doubleValue();
			ieeeStaLoad.setA8(a8);

		}
		DynamicLoadModelSelectionXmlType loadModel = odmObjFactory.createDynamicLoadModelSelectionXmlType();
				
		loadModel.setIEEEStaticLoad(ieeeStaLoad); 
		
		// The following processing sequence is arranged according to their priorities.
		
		//area name
		String areaName="";
		if(!strAry[4].equals("")){
			areaName=strAry[4];
			for(JAXBElement<? extends BusXmlType> busElem:parser.getDStabNet().getBusList().getBus()){
				bus=(DStabBusXmlType)busElem.getValue();
				// assume the zone id is the same as bus.zoneName
				if(areaName.equals(bus.getAreaName())){
					if(bus.getLoadData().getEquivLoad()!=null){
						DStabLoadDataXmlType load = (DStabLoadDataXmlType)bus.getLoadData().getEquivLoad().getValue();
						load.setLocation(LoadCharacteristicLocationEnumType.AT_AREA);
						load.setLoadModel(loadModel);
					}
				}
			}
		}
		
	    //zone name
		String zoneName="";
		if(!strAry[3].equals("")){
			zoneName=strAry[3];
			for(JAXBElement<? extends BusXmlType> busElem:parser.getDStabNet().getBusList().getBus()){
				bus=(DStabBusXmlType)busElem.getValue();
				// assume the zone id is the same as bus.zoneName
				if(zoneName.equals(bus.getZoneName())){
					if(bus.getLoadData().getEquivLoad()!=null){
						DStabLoadDataXmlType load = (DStabLoadDataXmlType)bus.getLoadData().getEquivLoad().getValue();
					   load.setLocation(LoadCharacteristicLocationEnumType.AT_ZONE);
					   load.setLoadModel(loadModel);
					}
					
				}
			}
	    }
		
		//busId
		String busName="";
		if(!strAry[1].equals("")){
			busName=strAry[1];
			String BusId="";
			try {
				BusId = BPABusRecord.getBusId(busName);
			} catch (ODMException e) {
				e.printStackTrace();
			}
			bus = parser.getDStabBus(BusId);
			if (bus !=null) {
			 DStabLoadDataXmlType load = (DStabLoadDataXmlType)bus.getLoadData().getEquivLoad().getValue();
		     load.setLocation(LoadCharacteristicLocationEnumType.AT_BUS);
		     load.setLoadModel(loadModel);
		}
			else throw new ODMException("Bus not found for Load Char rec: " + str);
		
		}
	}
}
		

private static String[] getLoadDataFields(String str){
	final String[] strAry= new String[19];
	
	try{
		if(str.substring(0, 2).trim().equals("LA")||str.substring(0, 2).trim().equals("LB")){
			strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
			//to process the Chinese characters first, if any.
			int chineseCharNum=ModelStringUtil.getChineseCharNum(str.substring(3,10).trim());
			//Columns 6-13 busName  
			strAry[1] = ModelStringUtil.getStringReturnEmptyString(str,4, 11-chineseCharNum).trim();			
			str=chineseCharNum==0?str:ModelStringUtil.replaceChineseChar(str);
			
			//bus Voltage
			strAry[2]=ModelStringUtil.getStringReturnEmptyString(str,12, 15).trim();
			//zone name
			strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,16, 17).trim();
			//area name
			strAry[4]=ModelStringUtil.getStringReturnEmptyString(str,18, 27).trim();
			//pz
			strAry[5]=ModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();
			//qz
			strAry[6]=ModelStringUtil.getStringReturnEmptyString(str,33, 37).trim();
			//pi
			strAry[7]=ModelStringUtil.getStringReturnEmptyString(str,38, 42).trim();
			//qi
			strAry[8]=ModelStringUtil.getStringReturnEmptyString(str,43, 47).trim();
			// pp
			strAry[9]=ModelStringUtil.getStringReturnEmptyString(str,48, 52).trim();
			//qp
			strAry[10]=ModelStringUtil.getStringReturnEmptyString(str,53, 57).trim();
			//pf
			strAry[11]=ModelStringUtil.getStringReturnEmptyString(str,58, 62).trim();
			// qf
			strAry[12]=ModelStringUtil.getStringReturnEmptyString(str,63, 67).trim();
			//Ldp
			strAry[13]=ModelStringUtil.getStringReturnEmptyString(str,68, 72).trim();
			//Ldq
			strAry[14]=ModelStringUtil.getStringReturnEmptyString(str,73, 77).trim();
			
				
	   }else if(str.substring(0, 2).trim().equals("MI")){
			
		 }
	
	}catch( Exception e){
		ODMLogger.getLogger().severe(e.toString());
	}
	return strAry;
}
	
}
