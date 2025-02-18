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

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq1Ed1MachineXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.MutualZeroZXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.TransformerZeroSeqXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrDStabXmlType;
import org.ieee.odm.schema.XfrZeroSeqConnectLocationEnumType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;


public class BPADynamicSequenceRecord {

	public static void processSequenceData(String str, DStabModelParser parser) throws ODMException {				
		
		final String strAry[]=getSequenceDataFields(str);		
		
		if(strAry[0].equals("XO")){
			final String fromId = BPABusRecord.getBusId(strAry[1]);
			final String toId = BPABusRecord.getBusId(strAry[3]);
			//TODO Change 1->0;
			String cirId="1";
			if(!strAry[6].equals("")){
				cirId=strAry[6];				
			}
			XfrDStabXmlType xfr=null;
//			BusRefRecordXmlType fromBusRef=null;
//	    	BusRefRecordXmlType toBusRef=null;
	    	/*
	    	 * BPA Branch data does NOT have a strict requirement of the sequence fromId and toId, 
	    	 * it seems that an exchange of them is also supported, namely,"fromId-to-toId" and "toId-to-fromId" are the same in BPA.
	    	 * The following processing is to consider this kind of special case.
	    	 */
	    	if(parser.getBranch(fromId, toId, cirId)!=null){
			     xfr =parser.getDStabXfr(fromId, toId, cirId);
//			     fromBusRef=parser.createBusRef(fromId);
//			     toBusRef=parser.createBusRef(toId);
			}
	    	
			else if (parser.getBranch(toId, fromId, cirId)!=null){
				xfr =parser.getDStabXfr(toId, fromId, cirId);
//			     fromBusRef=parser.createBusRef(toId);
//			     toBusRef=parser.createBusRef(fromId);
			}
			else throw new ODMException("Branch not found in the DStabNet, id: " + fromId + "-" + toId + "(" + cirId + ")");
	    	
	    	
//	    	xfr.setFromBus(fromBusRef);
//	    	xfr.setToBus(toBusRef);
	    	
			TransformerZeroSeqXmlType xfrZeroSeq =new TransformerZeroSeqXmlType();
			int location= new Integer(strAry[5]).intValue();
			if(location==1){
				xfrZeroSeq.setConectionLocation(XfrZeroSeqConnectLocationEnumType.AT_BUS_1);
			}else if(location==2){
				xfrZeroSeq.setConectionLocation(XfrZeroSeqConnectLocationEnumType.AT_BUS_2);
			}else {
				xfrZeroSeq.setConectionLocation(XfrZeroSeqConnectLocationEnumType.BETWEEN_BUS_1_N_BUS_2);
			}
			xfr.setXfrZeroSeq(xfrZeroSeq);
			
			//Z0
			double x0=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			double r0=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			if(!strAry[7].contains(".")){
				x0=x0/10000;
			}
			if(!strAry[8].contains(".")){
				r0=r0/10000;
			}
			xfr.setZ0(DStabDataSetter.createZValue(r0, x0, ZUnitType.PU));
		}
	    else if(strAry[0].equals("XR")){
	    	final String busId = BPABusRecord.getBusId(strAry[1]);
        	DStabBusXmlType bus = parser.getDStabBus(busId);
        	double busBase = ODMModelStringUtil.getDouble(strAry[2], 0.0);
        	bus.setBaseVoltage(DStabDataSetter.createVoltageValue(busBase, VoltageUnitType.KV));
        	double r0 = ODMModelStringUtil.getDouble(strAry[3], 0.0);
        	double x0 = ODMModelStringUtil.getDouble(strAry[4], 0.0);
        	if(!strAry[3].contains(".")){
				r0=r0/10000;
			}
        	if(!strAry[4].contains(".")){
				x0=x0/10000;
			}
        	double factor = r0*r0+x0*x0;
        	//ScSimpleBusXmlType.ScShuntLoadData scsld =odmObjFactory.createScSimpleBusXmlTypeScShuntLoadData();

    		DStabLoadDataXmlType load = DStabParserHelper.getDefaultLoad(bus.getLoadData());
        	load.setShuntLoadZeroY(DStabDataSetter.createYValue(r0/factor, -x0/factor, YUnitType.PU));

        	//bus.setScShuntLoadData(scsld);
	    }
	    else if(strAry[0].equals("LO")){
	    	final String fromId = BPABusRecord.getBusId(strAry[1]);
			final String toId = BPABusRecord.getBusId(strAry[3]);
			//TODO Change 1->0, since id=1 already exists in one of some parallel branches,while info for the other one is missing
			// also such change is consistent with Load Flow positive sequence processing
			String cirId="1";
			if(!strAry[6].equals("")){
				cirId=strAry[6];				
			}
	    	LineDStabXmlType line =null;
//	    	BusRefRecordXmlType fromBusRef=null;
//	    	BusRefRecordXmlType toBusRef=null;
	    	/*
	    	 * BPA Branch data does NOT have a strict requirement of sequence of  fromId and toId, 
	    	 * it seems that an exchange of them is also supported.
	    	 * The following processing is to consider this kind of special case.
	    	 */
	    	if(parser.getBranch(fromId, toId, cirId)!=null){
			     line =parser.getDStabLine(fromId, toId, cirId);
//			     fromBusRef=parser.createBusRef(fromId);
//			     toBusRef=parser.createBusRef(toId);
			     }
	    	
			else if (parser.getBranch(toId, fromId, cirId)!=null){
				 line =parser.getDStabLine(toId, fromId, cirId);
//			     fromBusRef=parser.createBusRef(toId);
//			     toBusRef=parser.createBusRef(fromId);
			     }
			else throw new ODMException("Branch not found in the DStabNet, id: " + fromId + "-" + toId + "(" + cirId + ")");
	    	//parser.getDStabLine(fromId, toId, cirId);
			//TODO can't set the rated voltage of frombus and tobus .When we get the branch,these info have been included?
	    	
//	    	line.setFromBus(fromBusRef);
//	    	line.setToBus(toBusRef);

	    	
			//Z0			
			double r0=ODMModelStringUtil.getDouble(strAry[7], 0.0);
			double x0=ODMModelStringUtil.getDouble(strAry[8], 0.0);
			if(!strAry[7].contains(".")){
				r0=r0/10000;
			}
			if(!strAry[8].contains(".")){
				x0=x0/10000;
			}
			line.setZ0(DStabDataSetter.createZValue(r0, x0, ZUnitType.PU));
			     		
    		//Y1
			double g1=ODMModelStringUtil.getDouble(strAry[9], 0.0);
			double b1=ODMModelStringUtil.getDouble(strAry[10], 0.0);
			if(!strAry[9].contains(".")){
				g1=g1/10000;
			}
			if(!strAry[10].contains(".")){
				b1=b1/10000;
			}
			line.setY0ShuntFromSide(DStabDataSetter.createYValue(g1, b1, YUnitType.PU));
			     		
    		//Y2
			double g2=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			double b2=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[11].contains(".")){
				g2=g2/10000;
			}
			if(!strAry[12].contains(".")){
				b2=b2/10000;
			}
    		line.setY0ShuntToSide(DStabDataSetter.createYValue(g2, b2, YUnitType.PU));
	    }
	    else if(strAry[0].equals("LM")){
	    	final String line1fId = BPABusRecord.getBusId(strAry[1]);
			final String line1tId = BPABusRecord.getBusId(strAry[3]);
			String line1cirId="1";
			if(!strAry[5].equals("")){
				line1cirId=strAry[5];				
			}
	    	LineDStabXmlType line1 = parser.getDStabLine(line1fId, line1tId, line1cirId);
			
			final String line2fId =  BPABusRecord.getBusId(strAry[6]);
			final String line2tId =  BPABusRecord.getBusId(strAry[8]);
			String line2cirId="1";
			if(!strAry[10].equals("")){
				line2cirId=strAry[10];				
			}
			LineDStabXmlType line2 = parser.getDStabLine(line2fId, line2tId, line2cirId);
			
			double rm=ODMModelStringUtil.getDouble(strAry[11], 0.0);
			double xm=ODMModelStringUtil.getDouble(strAry[12], 0.0);
			if(!strAry[11].contains(".")){
				rm=rm/10000;
			}
			if(!strAry[12].contains(".")){
				xm=xm/10000;
			}
			MutualZeroZXmlType mutualZ0 =OdmObjFactory.createMutualZeroZXmlType();
			mutualZ0.setZM(DStabDataSetter.createZValue(rm, xm, ZUnitType.PU));
			line1.getLineMutualZeroZ().add(mutualZ0);
			line2.getLineMutualZeroZ().add(mutualZ0);			
	    }
	}

	public static void processNegativeData( DStabModelParser parser) throws ODMException{
		NetworkXmlType.BusList busList=parser.getDStabNet().getBusList();
		int size=busList.getBus().size();
		for(int i=0;i<size;i++){
			BusXmlType Bus = busList.getBus().get(i).getValue();
			if(!(Bus instanceof DStabBusXmlType)){
				i--;
				size--;
			}
			DStabBusXmlType bus=parser.getDStabBus(Bus.getId());
//		for( JAXBElement<? extends BusXmlType> busXml:parser.getDStabNet().getBusList().getBus()){
//			DStabBusXmlType bus=parser.getDStabBus(busXml.getValue().getId());
			
			// negative sequence generator data
			if(bus.getGenData().getContributeGen()!=null){
				for(JAXBElement<? extends LoadflowGenDataXmlType> gen : bus.getGenData().getContributeGen()){
					DStabGenDataXmlType dynGen = (DStabGenDataXmlType)gen.getValue();
					//for(DynamicGeneratorXmlType dynGen:odmObjFactory.createDStabBusXmlTypeDynamicGenList().getDynamicGen()){
					double xd1=0.0;
					double x2=0.0;
					double tq01=0.0;
					if(dynGen.getMachineModel().getValue().getClass().toString().equals("class org.ieee.odm.schema.Eq11Ed11MachineXmlType")){
						Eq11Ed11MachineXmlType subGen=(Eq11Ed11MachineXmlType)dynGen.getMachineModel().getValue();
						xd1=subGen.getXd1();
						tq01=subGen.getTq01().getValue();
					}else if(dynGen.getMachineModel().getValue().getClass().toString().equals("class org.ieee.odm.schema.Eq1Ed1MachineXmlType")){
						Eq1Ed1MachineXmlType tranGen=(Eq1Ed1MachineXmlType)dynGen.getMachineModel().getValue();
						xd1=tranGen.getXd1();
						tq01=tranGen.getTq01().getValue();
					}else if(dynGen.getMachineModel().getValue().getClass().toString().equals("class org.ieee.odm.schema.ClassicMachineXmlType")){
						ClassicMachineXmlType claGen=(ClassicMachineXmlType)dynGen.getMachineModel().getValue();
						xd1=claGen.getXd1();
						tq01=0.0;  //TODO Why the tq01 in classic model is equal to 0.0?		
					}
					//TODO b= X��d/X��d , b can be set in the CASE card,the default value is 0.65 .
					//so how can we get the b value from the CASE card?
					//non-salient pole machine
					if(tq01!=0.0){
						x2=1.22*0.65*xd1;
					}
					//salient pole generator 
					else{
						x2=0.65*xd1;
					}
					//4 decimal places specified
					NumberFormat ddf1= NumberFormat.getInstance();
					ddf1.setMaximumFractionDigits(4);
					x2= new Double(ddf1.format(x2)).doubleValue();
					//TODO How to set the negative sequence impedance to associate to generator?
					//How about the case that several generators is in parallel on the bus?
					//ScGenDataXmlType scgd = odmObjFactory.createScGenDataXmlType();
					//scgd.setNegativeZ(DStabDataSetter.createZValue(0.0, x2, ZUnitType.PU));
					// TODO bus.getScGenData().add(scgd);
					
					//odmObjFactory.createSequenceBusDataXmlTypeGenData().setNegativeZ(DStabDataSetter.createZValue(0.0, x2, ZUnitType.PU));	
				}
			}
			//negative sequence load data 
			if(bus.getLoadData()!=null){
				DStabLoadDataXmlType load = DStabParserHelper.getDefaultLoad(bus.getLoadData());
				if(load!=null){
					//TODO ���ｫ���ɸ����ɵ�Ч�ɶԵ�֧·�����迹�����ڵ㱾��Ĳ����ӵ�֧·�ĸ�������أ�
					//hard coded values
					//ScSimpleBusXmlType.ScShuntLoadData scsld = odmObjFactory.createScSimpleBusXmlTypeScShuntLoadData();

					load.setShuntLoadNegativeY(DStabDataSetter.createYValue(0.19, 0.36, YUnitType.PU));
			        //bus.setScShuntLoadData(scsld);
				}
			}
		}
	}
/*						
		for(GeneratorXmlType gen:tranSimu.getDynamicDataList().getBusDynDataList()
				.getGeneratorDataList().getGeneratorArray()){
			double xd1=0.0;
			double x2=0.0;
			double tq01=0.0;
			if(gen.getGeneratorType().equals(GeneratorXmlType.GeneratorType.SUBTRANS_MODEL)){
				SubTransientMachineXmlType subGen=
					gen.getGeneratorModel().getSubTransientModel();
				xd1=subGen.getXd1();
				tq01=subGen.getTq01().getValue();				
			}else if(gen.getGeneratorType().equals(GeneratorXmlType.GeneratorType.TRANSIENT_MODEL)){
				TransientMachineXmlType tranGen=
					gen.getGeneratorModel().addNewTransModel();
					xd1=tranGen.getXd1();
					tq01=tranGen.getTq01().getValue();
			}else if(gen.getGeneratorType().equals(GeneratorXmlType.GeneratorType.CLASSICAL_MODEL)){
				ClassicMachineXmlType claGen=
					gen.getGeneratorModel().getClassicalModel();
				xd1=claGen.getXd1();
				tq01=0.0;;				
			}
			//non-salient pole machine
			if(tq01!=0.0){
				x2=1.22*0.65*xd1;
			}
			//salient pole generator 
			else{
				x2=0.65*xd1;
			}
			NegativeSequenceDataListXmlType.GeneratorNegativeList.GeneratorNegative xfrNeg=
				XBeanTranStabSimuHelper.addNewGenNeg(tranSimu);
			
			String busId=gen.getLocatedBus().getName();
			String genId="";
			if(gen.getGenId()!=null){
				genId=gen.getGenId().getName();
				xfrNeg.addNewMacId().setName(genId);
			}
			xfrNeg.addNewBusId().setName(busId);
			xfrNeg.setZXNeg(x2);			
		}
		for( LoadCharacteristicXmlType load: tranSimu.getDynamicDataList().
				getBusDynDataList().getLoadCharacteristicDataList().getLoadArray() ){
			NegativeSequenceDataListXmlType.ShuntLoadNegativeList.ShuntLoadNegative loadNeg=
				XBeanTranStabSimuHelper.addNewShuntLoadNeg(tranSimu);
			
			if(load.getLocation().equals(LoadCharacteristicXmlType.Location.AT_AREA)){
				loadNeg.setLoadLocation(NegativeSequenceDataListXmlType.ShuntLoadNegativeList.
						ShuntLoadNegative.LoadLocation.AT_AREA);
				
			}else if(load.getLocation().equals(LoadCharacteristicXmlType.Location.AT_BUS)){
				loadNeg.setLoadLocation(NegativeSequenceDataListXmlType.ShuntLoadNegativeList.
						ShuntLoadNegative.LoadLocation.AT_BUS);
			}else {
				loadNeg.setLoadLocation(NegativeSequenceDataListXmlType.ShuntLoadNegativeList.
						ShuntLoadNegative.LoadLocation.AT_ZONE);
			}			
			loadNeg.addNewLocationId().setName(load.getLocationId().getName());
			// TODO: hard coded values
			loadNeg.setRNeg(0.19);
			loadNeg.setXNeg(0.36);
		}
*/	
	private static String[] getSequenceDataFields(String str){
		final String[] strAry= new String[13];
		// line type
		strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
		
		//----to process the Chinese characters in the fromBus name, if any.
		String temp=ODMModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
		int chnCharNum1=ODMModelStringUtil.getChineseCharNum(temp);		
		//from bus name
		strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,5, 12-chnCharNum1).trim();
		
		//---to process the Chinese characters in the toBus name, if any.
		temp=ODMModelStringUtil.getStringReturnEmptyString(str,19-chnCharNum1, 26-chnCharNum1).trim();
		int chnCharNum2=ODMModelStringUtil.getChineseCharNum(temp);
		//to bus name
		strAry[3] = ODMModelStringUtil.getStringReturnEmptyString(str,19-chnCharNum1, 26-chnCharNum1-chnCharNum2).trim();
		
		//LM card have 4 buses
		if(str.substring(0, 2).startsWith("LM")){
			//----to process the Chinese characters in the fromBus name 0f line2, if any.
    		temp=ODMModelStringUtil.getStringReturnEmptyString(str,36-chnCharNum1-chnCharNum2, 43-chnCharNum1-chnCharNum2).trim();
    		int chnCharNum3=ODMModelStringUtil.getChineseCharNum(temp);
    		//from bus name
    		strAry[6] = ODMModelStringUtil.getStringReturnEmptyString(str,36-chnCharNum1-chnCharNum2, 43-chnCharNum1-chnCharNum2-chnCharNum3).trim();
    		
    		//---to process the Chinese characters in the toBus name of line2, if any.
    		temp=ODMModelStringUtil.getStringReturnEmptyString(str,50-chnCharNum1-chnCharNum2-chnCharNum3, 57-chnCharNum1-chnCharNum2-chnCharNum3).trim();
    		int chnCharNum4=ODMModelStringUtil.getChineseCharNum(temp);
    		//to bus name
    		strAry[8] = ODMModelStringUtil.getStringReturnEmptyString(str,50-chnCharNum1-chnCharNum2-chnCharNum3, 57-chnCharNum1-chnCharNum2-chnCharNum3-chnCharNum4).trim();
		}
		
		//--- replace all the Chinese Characters, since they are not used in the following processing.
		if(chnCharNum1>0||chnCharNum2>0)str=ODMModelStringUtil.replaceChineseChar(str);
		
		try{
			if(str.substring(0, 2).startsWith("XO")){
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
	    		//bus1
	    		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
	    		//bus1 Voltage
	    		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
	    		//bus2
	    		//strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
	    		//bus2 Voltage
	    		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
	    		//zrLocation
	    		strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,32, 32).trim();
	    		//par
	    		strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,34, 34).trim();
	    		//X0
	    		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,38, 44).trim();
	    		//R0
	    		strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,45, 51).trim();		
				
			}else if(str.substring(0, 2).startsWith("XR")){			
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
	    		//bus1
	    		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
	    		//bus1 Voltage
	    		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
	    		//R0
	    		strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 28).trim();
	    		//X0
	    		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,29, 35).trim();
			}else if(str.substring(0, 2).startsWith("LO")){			
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
	    		//bus1
	    		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
	    		//bus1 Voltage
	    		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
	    		//bus2
	    		//strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
	    		//bus2 Voltage
	    		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();    		
	    		//par
	    		strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 33).trim();
	    		//R0
	    		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,36, 42).trim();
	    		//X0
	    		strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,43, 49).trim();
	    		//G1
	    		strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,50, 56).trim();
	    		//B1
	    		strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,57, 63).trim();
	    		//G2
	    		strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,64, 70).trim();
	    		//B2
	    		strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,71, 77).trim();
			}else if(str.substring(0, 2).startsWith("LM")){
				//strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
	    		//busI line 1
	    		//strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
	    		//busI Voltage
	    		strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
	    		//busJ line 1
	    		//strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
	    		//busJ Voltage
	    		strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();    		
	    		//par1
	    		strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,33, 33).trim();
	    		//busK line2
	    		//strAry[6]=ModelStringUtil.getStringReturnEmptyString(str,36, 43).trim();
	    		//busK voltage
	    		strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,44, 47).trim();
	    		//busL line2
	    		//strAry[8]=ModelStringUtil.getStringReturnEmptyString(str,50, 57).trim();
	    		//busL voltage
	    		strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,58, 61).trim();
	    		//par2
	    		strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,64, 64).trim();
	    		//Rm
	    		strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,67, 73).trim();
	    		//Xm
	    		strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,74, 80).trim();
			}
		}catch(Exception e){
			ODMLogger.getLogger().severe(e.toString());
		}				
		return strAry;
	}	
}
