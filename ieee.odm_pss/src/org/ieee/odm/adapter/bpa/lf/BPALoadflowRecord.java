/*
 * @(#)BPALoadflowRecord.java   
 *
 * Copyright (C) 2006 www.interpss.org
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
 * @Author Stephen Hou
 * @Version 1.0
 * @Date 08/11/2008
 * 
 *   Revision History
 *   ================
 *   
 *   modified for Jaxb Mike Zhou 02/28/2011
 *
 */

package org.ieee.odm.adapter.bpa.lf;

import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;

/**
 * BPA adapter is design to handle Loadflow data file and Loadflow+TransienStability data files
 * 
 * @author mzhou
 *
 */
public class BPALoadflowRecord<
					TNetXml extends NetworkXmlType, 
					TBusXml extends BusXmlType,
					TLineXml extends BranchXmlType,
					TXfrXml extends BranchXmlType,
					TPsXfrXml extends BranchXmlType> {
	public final static String Token_CaseType = "Type";
	public final static String Token_ProjectName = "Original Project Name";
	public final static String Token_CaseId = "Case Identification";
	public final static String Token_BN="Bus Name";
	
	public void processLfData(BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> parser, final IFileReader din) throws Exception {
		TNetXml baseCaseNet = parser.getNet();
		baseCaseNet.setId("Base_Case_from_BPA_loadflow_format");			

		// we set default base MVA here, since MVA line is optional
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(100.0));
    	
		//NameValuePairListXmlType nvList = parser.getFactory().createNameValuePairListXmlType();
		//baseCaseNet.setNvPairList(nvList);
		
		int areaId=1;// used to arrange a number to each area 

		// BPA Loadflow file does not guarantee that all bus records are put
		// in front of branch records. Therefore the branch line are cached first
		List<String> branchInputList = new ArrayList<String>(100);
		
		// BPA put AREA and AREA Interchange data at the beginning of the network data ,
		// need to be cached for processing later
		List<String> areaList = new ArrayList<String>(10);
		String str;
		do{
			str = din.readLine();					
			if(!str.trim().equals("(END)")&&!str.trim().equals("(STOP)")){
				try{
					if(str.startsWith(".")||str.startsWith("C")){
						// comment line
						ODMLogger.getLogger().fine("load comment");
					}
					else if(str.startsWith("(POWERFLOW")||str.startsWith("/")
							||str.startsWith(">")){
						ODMLogger.getLogger().fine("load header data");
						new BPANetRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processNetData(str, baseCaseNet);
					}
					else if(str.startsWith("A")||str.trim().startsWith("I")){
						areaList.add(str);
						//BPANetRecord.processAreaData(str, parser,	baseCaseNet, areaId++);
					}
					else if(str.trim().startsWith("B")||str.trim().startsWith("+")
							||str.trim().startsWith("X")){
						ODMLogger.getLogger().fine("load AC bus data");						
						new BPABusRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processBusData(str, parser);
//						System.out.println(str); //for test
					}
					else if( str.trim().startsWith("L") || str.trim().startsWith("E") ||
							 str.trim().startsWith("T") || str.trim().startsWith("R") ||
							 str.trim().startsWith("LD")||str.trim().startsWith("LM") ||
							 str.trim().startsWith("BD")||str.trim().startsWith("BM")){
						ODMLogger.getLogger().fine("load AC line data");
						// since bus info could be defined at the branch info, we
						// cache branch info for late processing
						branchInputList.add(str);
					}
					// the gen and load data modification is usually defined  at the end of all network data
					else if( str.trim().startsWith("PA")||str.trim().startsWith("PZ")||str.trim().startsWith("PO")
							||str.trim().startsWith("PC")||str.trim().startsWith("PB")){
						ODMLogger.getLogger().fine("load Gen AND Load modification data");
						new BPAGenLoadDataModifyRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processGenLoadModificationData(str,parser);
					}
					else{
						new BPANetRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processReadComment(str, baseCaseNet);
					}						
				}
				catch (final Exception e) {
					ODMLogger.getLogger().severe("Error, input : " + str + "\n" + e.toString());
					e.printStackTrace();
				}					
			}
		} while(!str.trim().equals("(END)")&&!str.trim().equals("(STOP)"));
		
		// processing branch info after all bus info are processed 
		processBranchInfo(branchInputList, parser);
		
		//process inter-area exchange data
		processInterAreaExchangeData(areaList,parser);
		
		//TODO set the area info after getting the area data
	}
	
	/**
	 * process branch info
	 * 
	 * @param strList
	 * @param parser
	 * @throws ODMException
	 */
	private void processBranchInfo(List<String> strList, BaseAclfModelParser<TNetXml,TBusXml, TLineXml, TXfrXml, TPsXfrXml> parser) throws ODMException {
		for (String str : strList) {
			if( str.trim().startsWith("L")||str.trim().startsWith("E")){
				ODMLogger.getLogger().fine("load AC line data");
				new BPALineBranchRecord<TNetXml,TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processBranchData(str, parser);
			}
			else if( str.trim().startsWith("T")){
				ODMLogger.getLogger().fine("load transformer data");
				new BPAXfrBranchRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processXfrData(str, parser);
			}
			else if(str.trim().startsWith("R")){
				ODMLogger.getLogger().fine("load transformer adjustment data");
				new BPAXfrBranchRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processXfrAdjustData(str, parser);
			}
			else if( str.trim().startsWith("LD")||str.trim().startsWith("LM") ||
					 str.trim().startsWith("BD")||str.trim().startsWith("BM")){
				ODMLogger.getLogger().fine("load DC Line data");
				// *** BPABranchRecord.processDCLineBranchData(str, parser.addNewBaseCaseDCLineBranch(),
				// ***		parser,baseCaseNet, this);
			}
		}
	}
	
	private void processInterAreaExchangeData(List<String> strList, BaseAclfModelParser<TNetXml,TBusXml, TLineXml, TXfrXml, TPsXfrXml> parser) throws ODMException{
		TNetXml baseCaseNet = parser.getNet();
		int areaNumber=0;
		for (String str : strList) {
			if(str.startsWith("AC ")||str.startsWith("A ")) areaNumber++; //only AC,NOT AC+
			new BPANetRecord<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>().processAreaData(str, parser, baseCaseNet, areaNumber );
		}
	}
}
