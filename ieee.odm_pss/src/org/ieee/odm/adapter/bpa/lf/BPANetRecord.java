/*
 * @(#)BPANetRecord.java   
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

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import java.util.StringTokenizer;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerXmlType;
import org.ieee.odm.schema.AreaTransferXmlType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.ExchangeAreaXmlType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetAreaXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.VoltageUnitType;

public class BPANetRecord<
				TNetXml extends NetworkXmlType, 
				TBusXml extends BusXmlType,
				TLineXml extends BranchXmlType,
				TXfrXml extends BranchXmlType,
				TPsXfrXml extends BranchXmlType> {
	/*
	 *   Network data
	 *   ============ 
	 */
	public void processReadComment(final String str, final TNetXml baseCaseNet){
		
	//	adapter.logErr("This line is for comment only:  "+str);
		// to do in future
		
	}

	public void processNetData(final String str, final TNetXml baseCaseNet) {
			// parse the input data line
			final String[] strAry = getNetDataFields(str);			
	        //read powerflow, caseID,projectName, 			
			if (strAry[0]!= null ){
				BaseJaxbHelper.addNVPair(baseCaseNet, strAry[0], strAry[1]);
				ODMLogger.getLogger().fine(strAry[0] +": " + strAry[1]);
			}
			
			if (strAry[2]!= null ){
				BaseJaxbHelper.addNVPair(baseCaseNet, strAry[2], strAry[4]);
				ODMLogger.getLogger().fine(strAry[2]+": " + strAry[4] );
			}			
			// more name-vale could be added in future 
			
			if(str.startsWith("/MVA_BASE")){
				if(strAry[5]!= null) {
					double baseMva = new Double(strAry[5]).doubleValue(); // in MVA
					ApparentPowerXmlType baseKva = baseCaseNet.getBasePower();
			    	baseKva.setValue(baseMva);   
				}
			}
	}

	/*
	 *   area data
	 *   ================ 
	 */

	public void processAreaData(final String str,final BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> parser ,
			final TNetXml baseCaseNet, int areaNumber	) throws ODMException {
		LoadflowNetXmlType net = (LoadflowNetXmlType)baseCaseNet;
		
		final String[] strAry = getAreaDataFields(str);
		int zoneId=0;
	
		if(str.trim().startsWith("A")||str.trim().startsWith("AC")){
			if (baseCaseNet.getAreaList() == null)
				baseCaseNet.setAreaList(odmObjFactory.createNetworkXmlTypeAreaList());
			
			String areaName="";
			if(!strAry[2].equals("")){
				areaName=strAry[2];
			}
			
			ExchangeAreaXmlType  area =null;
			if(!str.startsWith("AC+")) {
				area= odmObjFactory.createExchangeAreaXmlType();
				baseCaseNet.getAreaList().getArea().add(area);
				area.setName(areaName);
			    area.setNumber(areaNumber);
			}
			else area=(ExchangeAreaXmlType) getAreaByName(baseCaseNet, areaName);
			
			String slackBusName="";
			double ratedVoltage=0;
			if(!strAry[3].equals("")){
				slackBusName=strAry[3];
				String slackBusId=BPABusRecord.getBusId(slackBusName);
				area.setSwingBusId(parser.createBusRef(slackBusId));
				
			}
            if(!strAry[4].equals("")){
				ratedVoltage =new Double(strAry[4]).doubleValue();
				area.setRatedVoltage(BaseDataSetter.createVoltageValue(ratedVoltage, VoltageUnitType.KV));
			}
            double exchangeMW=0.0;
            if(!strAry[5].equals("")){            	
            	area.setDesiredExchangePower(BaseDataSetter.createActivePowerValue(exchangeMW, ActivePowerUnitType.MW));            	
            }        
            
            if(!strAry[6].trim().equals("")){
            	if (baseCaseNet.getLossZoneList() == null)
        			baseCaseNet.setLossZoneList(odmObjFactory.createNetworkXmlTypeLossZoneList());
            	
            	StringTokenizer st = new StringTokenizer(strAry[6]);
            	String zoneName="";        	
            	while(st.hasMoreTokens()){            		
            		zoneName=st.nextToken().trim();          		
            		NetZoneXmlType zone= parser.createNetworkLossZone();
            		area.getZone().add(zone);
            		//TODO what is loss zone?
            		//baseCaseNet.getLossZoneList().getLossZone().add(zone);
            		
            		zone.setName(zoneName);
            		
            		int zoneNumber =baseCaseNet.getLossZoneList().getLossZone().size()+1;           		
            		zone.setNumber(zoneNumber);
            		zone.setId("zone-"+zoneNumber);         		
            	}            	
            }
		}
		/*
		else if(str.trim().startsWith("AO")){
			final String dataType=strAry[0];
			
			if(!strAry[1].equals("")){
				final String modCode= strAry[1];
			}
			if(!strAry[2].trim().equals("")){
				String areaName=strAry[2];
				NetAreaXmlType area=BaseJaxbHelper.getAreaRecordByAreaName(areaName, baseCaseNet);
				if(area==null){
					area.setName(areaName);
				}
			}
			if(!strAry[3].trim().equals("")){
				// suppose there is a maximum of 40 areas
            	final String s[]= new String[40];
            	int cnt=0, i=0;            	
            }
		}
		*/
		else if(str.trim().startsWith("I")){// I Record defines the inter-area power exchange
			if (net.getInterchangeList() == null)
				net.setInterchangeList(odmObjFactory.createLoadflowNetXmlTypeInterchangeList());

			String fAreaName="";
			if(!strAry[2].equals("")){
			    fAreaName= strAry[2];			    
			}
			String tAreaName="";
			if(!strAry[3].equals("")){
			    tAreaName= strAry[3];
			}
			double exchangePower=0.0;
			if(!strAry[4].equals("")){
				exchangePower= new Double(strAry[4]).doubleValue();				
			}			
			if(!fAreaName.equals("")&&!tAreaName.equals("")){//&& exchangePower!=0
				InterchangeXmlType interchange = odmObjFactory.createInterchangeXmlType();
				net.getInterchangeList().getInterchange().add(interchange);
				
				AreaTransferXmlType transfer = odmObjFactory.createAreaTransferXmlType(); 
				interchange.setAreaTransfer(transfer);
				
				//get area data
				NetAreaXmlType fArea=getAreaByName(baseCaseNet, fAreaName);
				NetAreaXmlType tArea=getAreaByName(baseCaseNet, tAreaName);
				// define an transfer id
				String trId="Trans_"+fAreaName+"_to_"+tAreaName;
				
				transfer.setFromArea(fArea.getNumber());
				transfer.setToArea(tArea.getNumber());
				transfer.setId(trId);
				transfer.setAmountMW(exchangePower);			
			}			
		}	
		
	}
	
	private static String[] getNetDataFields(final String str) {
		final String[] strAry = new String[7];
		//the first line data
		if(str.startsWith("(POWERFLOW")){
		     String s1[]= new String[3];		     
		     final StringTokenizer st= new StringTokenizer(str, ",");
		     s1[0]=st.nextToken();
		     s1[1]=st.nextToken();
		     s1[2]=st.nextToken();		     
		     final StringTokenizer st1= new StringTokenizer(s1[1], "=");	 
		     strAry[0]=st1.nextToken();
		     strAry[1]=st1.nextToken();
		     final StringTokenizer st2= new StringTokenizer(s1[2], "=");
		     strAry[2]=st2.nextToken();
		     strAry[3]=st2.nextToken();
		     int length= strAry[3].length();
		     strAry[4]=strAry[3].substring(0, length-1);
			}
		// select certain concerned data to added
		//strAry[4]= baseMVA
		if(str.startsWith("/MVA_BASE")){			
			strAry[5]=str.substring(10, str.length()-1);			
		}		
	   return strAry;
	}
	
	private static String[] getAreaDataFields(final String str) {
		final String[] strAry = new String[7];
		
		try{
			if (str.trim().startsWith("A")||str.trim().startsWith("AC")||str.trim().startsWith("AC+")){			
				strAry[0] = ModelStringUtil.getStringReturnEmptyString(str, 1, 2);
				strAry[1] = ModelStringUtil.getStringReturnEmptyString(str, 3, 3);
				strAry[2] = ModelStringUtil.getStringReturnEmptyString(str, 4, 13);
				strAry[3] = ModelStringUtil.getStringReturnEmptyString(str, 14, 21);
				strAry[4] = ModelStringUtil.getStringReturnEmptyString(str, 22, 25);
				strAry[5] = ModelStringUtil.getStringReturnEmptyString(str, 26, 34);
				// zones within area
				int strlength=str.trim().length();
				if(strlength<=36) strAry[6]="";
				else strAry[6] = ModelStringUtil.getStringReturnEmptyString(str,36, strlength);
			}
			else if(str.trim().startsWith("AO")){ 
			    strAry[0] = ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
				strAry[1] = ModelStringUtil.getStringReturnEmptyString(str,3, 3).trim();
				// area name
				strAry[2] = ModelStringUtil.getStringReturnEmptyString(str,4, 13).trim();
				// zones within the area
				int strlength=str.length();
				strAry[3] = ModelStringUtil.getStringReturnEmptyString(str,15, strlength).trim();				
			}
			else if(str.trim().startsWith("I")){
			    strAry[0] = ModelStringUtil.getStringReturnEmptyString(str, 1, 1);
				strAry[1] = ModelStringUtil.getStringReturnEmptyString(str, 3, 3);
				strAry[2] = ModelStringUtil.getStringReturnEmptyString(str, 4, 13);
				strAry[3] = ModelStringUtil.getStringReturnEmptyString(str, 15, 24);
				strAry[4] = ModelStringUtil.getStringReturnEmptyString(str, 27, 34);		
			}	
		}	catch (Exception e){
			ODMLogger.getLogger().severe(e.toString());
		}
		return strAry;
	}
	
	private NetAreaXmlType getAreaByName(final TNetXml baseNet, final String areaName) throws ODMException{
		NetAreaXmlType targetArea=null;
		LoadflowNetXmlType net = (LoadflowNetXmlType)baseNet;
		for(NetAreaXmlType area : net.getAreaList().getArea()){
			if(area.getName().equals(areaName))
			{targetArea=area;break;}
		}
		if(targetArea!=null)return targetArea;
		else throw new ODMException("the target area with NAME #"+areaName+"# is not found!,please check the input name");
		
	}
}
	
