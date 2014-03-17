/*
 * @(#)PSSEDynAdapter.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
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
 * @Author Tony Huang
 * @Version 1.0
 * @Date 02/11/2013
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.psse.impl;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper.DynModelType;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynExciterMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynGeneratorMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynTurGovMapper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

public class PSSEDynAdapter extends PSSEAcscAdapter<DStabNetXmlType, DStabBusXmlType, LineDStabXmlType, XfrDStabXmlType, PSXfrDStabXmlType>{

    DynamicModelLibHelper dynLibHelper = new DynamicModelLibHelper();
	PSSEDynGeneratorMapper generatorMapper =null; 
	PSSEDynExciterMapper   exciterMapper =null;
	PSSEDynTurGovMapper    turGovMapper = null;
    
	public PSSEDynAdapter(PsseVersion ver) {
		super(ver);
		generatorMapper = new PSSEDynGeneratorMapper(ver);
		exciterMapper   = new PSSEDynExciterMapper(ver);
		turGovMapper    = new PSSEDynTurGovMapper(ver);
	}
	
	/*
	 * parse the input dynamic model data
	 */
	
	public IODMModelParser parseDStabFile(final IFileReader din, String encoding) throws ODMException {
		String lineStr = null;
		String modelType = "";
  		int lineNo = 0;
  		try {

      		do {
      			lineStr = din.readLine();
      			if (lineStr != null) {
      				lineNo++;
      				if(skipInvalidLine(lineStr)){
      					System.out.println("Invalid line, line# "+lineNo+",:"+lineStr);
      					continue;
      				}
      				lineStr = lineStr.trim();
      				if(lineStr.length()>0){//only process when it is not a blank line
      					while(!isModelDataCompleted(lineStr)){
      						lineStr += din.readLine();
      						lineNo++;
      					}
      					//remove the "/" at the end of data definition
      					
      					lineStr =lineStr.substring(0, lineStr.lastIndexOf("/"));
      					
      					modelType = getModelType(lineStr);
      					DynModelType type = dynLibHelper.getModelType(modelType);
      					if(type!=null){
	      					if(type==DynModelType.GENERATOR){
	      						generatorMapper.procLineString(modelType, lineStr, (DStabModelParser) parser);
	      					}
	      					else if(dynLibHelper.getModelType(modelType)==DynModelType.EXCITER){
	      						exciterMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      					}
	      					else if(dynLibHelper.getModelType(modelType)==DynModelType.TUR_GOV){
	      						turGovMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      					}
	      					
	      					//save supported model data
	      					dynLibHelper.saveSupportedModelData(lineStr);
      					}
      					else{
      						//System.out.println("model unsupported :"+lineStr);
      						//throw new Exception("The input dynamic model is not supported yet, Type #"+modelType);
      						dynLibHelper.procUnsupportedModel(modelType, lineStr);
      					}
      				}
      				
      			}
      		}while(lineStr!=null);
		
  		} catch (Exception e) {
  			e.printStackTrace();
    		throw new ODMException("PSSE dynamic data input error, line no " + lineNo + ", " + e.toString());
  		}

  		System.out.println(dynLibHelper.getUnsupportdSVCRecs());
		return parser;
	}

	@Override
	public IODMModelParser parseInputFile(NetType type, IFileReader[] din,
			String encoding) throws ODMException {
		if(type !=NetType.DStabNet){
			// initialize the parser first
			throw new ODMException("input type is not Dstab NetType, please check ");
			
		}
		
			// the parser is supposed to be set at the PSSEDstabParser class, which call this method
		if(parser == null){
				parser = new DStabModelParser();
				parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.TRANSIENT_STABILITY);
		}
		
		DStabNetXmlType baseCaseNet = (DStabNetXmlType) parser.getNet();
		
		/*
		 three files in the input array, i.e., power flow + sequence data + dynamic data file
		 Use the Acsc Parser to parse the first two files, namely, Aclf and Sequence data file.
		*/
		if(din.length==3){ 
			// the second data file stores the sequence data
		   if(din[1]!=null){ 
			   
			   baseCaseNet.setHasShortCircuitData(true);
		   }
		   super.parseInputFile(type, din, encoding);
		
	       //It is supposed that the third file defines the Dstab data.
		   this.parseDStabFile(din[2], encoding);
		}
		
		// only power flow and dynamic data included
		else if(din.length==2){ 
			super.parseInputFile(din[0], encoding);
			this.parseDStabFile(din[1], encoding);
	
			baseCaseNet.setHasLoadflowData(true);
			baseCaseNet.setPositiveSeqDataOnly(true);
		}
		
		return parser;
	}
	/**
	 * Dynamic model Format:
	 * IBUS  'TYPE'  ID   DATALIST /
	 * 
	 * @param lineStr
	 * @return
	 */
    private boolean isModelDataCompleted(String lineStr){
		return lineStr.trim().lastIndexOf("/")>0;
		
	}
    
    /**
     * Model type string is stored in the second entry.
     * Exmaple: 
     * 1   'IEEET1'   1   0.06   20.0   0.2   1.172 
     * 
     * @param lineStr
     * @return
     */
    private String getModelType(String lineStr){
    	String[] strAry =lineStr.split("\\s+");
    	if(strAry.length>2)
    	    return(ODMModelStringUtil.trimQuote(strAry[1]));
		else
			try {
				throw new Exception("The input data is not correct model data"+lineStr);
			} catch (Exception e) {
				e.printStackTrace();
			} 
    	return null;
    		
    }
    private boolean skipInvalidLine(String lineStr){
    	if(lineStr.trim().length()==0.0){
    		return true;
    	}
		//Check if the first line of multiInteger(String s) {
    	int busNum;
		if(lineStr.trim().startsWith("//") || lineStr.trim().startsWith("/"))
			return true;
		// skip a line if it is not started with a bus Number, which is an integer, 
	    try { 
	    	String[] strAry =lineStr.trim().split("\\s+");
	        busNum =Integer.parseInt(strAry[0].trim()); 
	    } catch(NumberFormatException e) {
	    	//System.out.println("Error when processing line: \n"+ lineStr);
	        return true; 
	    }
	    // only got here if we didn't return false
	    return false;
	}
}
