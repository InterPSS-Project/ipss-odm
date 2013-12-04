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

import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;

public class BPADynamicRecord {	
	private final static int header=1;
	private final static int faultOperation=2;
	private final static int generatorData=3;
	private final static int exciterData=4;
	private final static int pssData=5;
	private final static int turbine_governorData=6;
	private final static int loadData=7;
	private final static int sequenceData=8;
	private final static int simuData=9;
	
	public static void processDynamicData(final IFileReader din, DStabModelParser parser) throws ODMException{
		parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.TRANSIENT_STABILITY);

		//PerformanceTimer timer = new PerformanceTimer(ODMLogger.getLogger());

		String str;
		do{
			str= din.readLine();
			if(!str.startsWith("90")){
				int dataType = getDataType(str);
				//System.out.println("processing line--"+str);
				try{
					if(dataType==header){
						processHeaderData(str);
					}
					else if(dataType==generatorData){
						//timer.start();
						BPADynamicGeneratorRecord.processGeneratorData(str, parser);
						//timer.logStd("processGeneratorData");
					}
					 /*since ODM should only deal with dynamic network data and 
					  fault setting is usually analysis/software-dependent
					else if(dataType==faultOperation){
						BPADynamicFaultOperationRecord.processFaultOperationData(str, parser);
				    }
					 */
					else if(dataType==exciterData){
						//timer.start();
						BPADynamicExciterRecord.processExciterData(str, parser);
						//timer.logStd("processExciterData");
					}
					else if(dataType==turbine_governorData){
						//timer.start();
						BPADynamicTurbineGovernorRecord.processTurbineGovernorData(str, parser);
						//timer.logStd("processTurbineGovernorData");
					}
					else if(dataType==pssData){
						//timer.start();
						BPADynamicPSSRecord.processPSSData(str, parser);
						//timer.logStd("processPSSData");
					}
					else if(dataType==loadData){
						//timer.start();
						BPADynamicLoadCharacteristicRecord.processLoadCharacteristicData(str, parser);
						//timer.logStd("processLoadCharacteristicData");
					}
					else if(dataType==sequenceData){
						//timer.start();
						BPADynamicSequenceRecord.processSequenceData(str, parser);
						//timer.logStd("processSequenceData");
					}
					else if(dataType==simuData){
						// not used
					}
					else if(dataType==0){
						
					}
				}catch (final Exception e){				
					e.printStackTrace();
				}				
			}			
		} while (!str.startsWith("90"));
		// when all the data is converted, calculate negative sequence data
//		BPADynamicSequenceRecord.processNegativeData(parser);		
	}
	
	private static int getDataType(String str){	
		int dataType=0;
		 if (str.startsWith(".") || str.startsWith("C") || str.trim().length()<1){//filter out the comment lines and blank lines
				dataType=0;
			}else if(str.startsWith("CASE")||str.startsWith("SOL") || str.startsWith("Bus.")) {
				dataType=header;
			}else if(str.startsWith("LS")){
				dataType=faultOperation;
			}else if(str.substring(0, 1).trim().equals("M")||
					str.substring(0, 2).trim().equals("MF")||
					str.substring(0, 2).trim().equals("MC")||
					str.substring(0, 2).trim().equals("LN")){
				dataType=generatorData;
			}else if (str.substring(0, 1).trim().equals("E")||
					str.substring(0, 1).trim().equals("F")&&!str.substring(3, 4).trim().equals("")){//There are two FF records
				/*
				 * ||str.substring(0, 2).trim().equals("FC")
					||str.substring(0, 2).trim().equals("FD")||str.substring(0, 2).trim().equals("FE")||
					str.substring(0, 2).trim().equals("FG")
					||str.substring(0, 2).trim().equals("FH")||str.substring(0, 2).trim().equals("FJ")
					||str.substring(0, 2).trim().equals("FK")||str.substring(0, 2).trim().equals("FL")
					||str.substring(0, 2).trim().equals("FQ")||str.substring(0, 2).trim().equals("FV")
					||str.substring(0, 1).trim().equals("E")||
					str.substring(0, 2).trim().equals("FZ")||str.substring(0, 2).trim().equals("F+")
				 */
				dataType=exciterData;
			}else if(str.substring(0, 2).trim().equals("SS")||
					str.substring(0, 2).trim().equals("SP")||str.substring(0, 2).trim().equals("SG")
					||str.substring(0, 2).trim().equals("SI")||str.substring(0, 3).trim().equals("SI+")){
				dataType=pssData;
			}else if(str.substring(0, 2).trim().equals("GS")||str.substring(0, 2).trim().equals("GH")
					||str.substring(0, 2).trim().equals("GG")||str.substring(0, 2).trim().equals("GA")
					||str.substring(0, 2).trim().equals("GI")||str.substring(0, 3).trim().equals("GI+")
					||str.substring(0, 2).trim().equals("TA")||str.substring(0, 2).trim().equals("TB")
					){
				dataType=turbine_governorData;
			}else if(str.substring(0, 2).trim().equals("LA")||
					str.substring(0, 2).trim().equals("LB")||str.substring(0, 2).trim().equals("MI")){
				dataType=loadData;				
			}else if(str.substring(0, 2).trim().equals("LO")||str.substring(0, 2).trim().equals("XO")
					||str.substring(0, 2).trim().equals("XR")||str.substring(0, 2).trim().equals("LM")){
				dataType=sequenceData;
			}else if(str.substring(0, 2).trim().equals("FF")){
				dataType=simuData;
			}else {				
				ODMLogger.getLogger().warning("This line data is not processed"+"   "+"'"+str+"'");
			}		 
		 return dataType;
	}

	public static void processHeaderData(String str){
		final String strAry[]= getHeaderDataFields(str);
		
//		AclfAlgorithmXmlType aclfAlgo = tranSimu.getAclfInitialization();
		
		// network solution card--SOL
//		if(str.startsWith("SOL")){			
//			if(!strAry[2].equals("")){
//				if(new Integer(strAry[2]).intValue()==1){
//					aclfAlgo.setLfMethod(LfMethodEnumType.NR);
//				}
//			}
//			else{
//				aclfAlgo.setLfMethod(LfMethodEnumType.PQ);
//			}			
//		}
		// CASE card
		if(str.startsWith("CASE")){
			//TransientSimulationXmlType.SimulationSetting simuSet= tranSimu.getSimulationSetting();
			
			String pfCase= strAry[1];
			//pfInitial.setPowerFlowCase(pfCase);
		}		
	}
	
    private static String[] getHeaderDataFields ( final String str) {
		final String[] strAry = new String[16];
		
		try{// for SOL card
			if(str.startsWith("SOL")){
				strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str, 0, 3);
				strAry[1]=ODMModelStringUtil.getStringReturnEmptyString(str, 5, 6);
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,9, 9);
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,12, 12);
			}		
			// for Case card
			else if(str.startsWith("CASE")){
				strAry[0]=ODMModelStringUtil.getStringReturnEmptyString(str,1, 4);
				strAry[1]=ODMModelStringUtil.getStringReturnEmptyString(str,6, 15);
				strAry[2]=ODMModelStringUtil.getStringReturnEmptyString(str,16, 17);
				strAry[3]=ODMModelStringUtil.getStringReturnEmptyString(str,20, 20);
				strAry[4]=ODMModelStringUtil.getStringReturnEmptyString(str,22, 22);
				strAry[5]=ODMModelStringUtil.getStringReturnEmptyString(str,23, 23);
				strAry[6]=ODMModelStringUtil.getStringReturnEmptyString(str,24, 24);
				strAry[7]=ODMModelStringUtil.getStringReturnEmptyString(str,24, 34);
				strAry[8]=ODMModelStringUtil.getStringReturnEmptyString(str,45, 49);
				strAry[9]=ODMModelStringUtil.getStringReturnEmptyString(str,50, 54);
				strAry[10]=ODMModelStringUtil.getStringReturnEmptyString(str,55, 59);
				strAry[11]=ODMModelStringUtil.getStringReturnEmptyString(str,60, 64);
				strAry[12]=ODMModelStringUtil.getStringReturnEmptyString(str,65, 69);
				strAry[13]=ODMModelStringUtil.getStringReturnEmptyString(str,70, 74);
				strAry[14]=ODMModelStringUtil.getStringReturnEmptyString(str,75, 80);			
			}
			}catch(Exception e){
				ODMLogger.getLogger().severe(e.toString());
			}
		
		return strAry;
    }
}
