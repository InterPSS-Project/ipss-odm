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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper.DynModelType;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynExciterMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynGeneratorMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynLoadMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynRelayMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynTurGovMapper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabNetXmlType;

public class PSSEDynAdapter extends PSSEAcscAdapter {

    DynamicModelLibHelper dynLibHelper = new DynamicModelLibHelper();
	PSSEDynGeneratorMapper generatorMapper =null; 
	PSSEDynExciterMapper   exciterMapper =null;
	PSSEDynTurGovMapper    turGovMapper = null;
	PSSEDynLoadMapper      loadMapper   = null;
	PSSEDynRelayMapper     relayMapper  = null;
	
	StringBuffer sb = new StringBuffer();
	
	boolean isDebug = false;
    
	public PSSEDynAdapter(PsseVersion ver) {
		super(ver);
		generatorMapper = new PSSEDynGeneratorMapper(ver);
		exciterMapper   = new PSSEDynExciterMapper(ver);
		turGovMapper    = new PSSEDynTurGovMapper(ver);
		loadMapper      = new PSSEDynLoadMapper(ver);
		relayMapper     = new PSSEDynRelayMapper(ver);
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
      					ODMLogger.getLogger().info(("Invalid line, line# "+lineNo+",:"+lineStr));
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
	      						
	      						//sb.append(lineStr+" /"+"\n");
	      					}
	      					else if(type==DynModelType.EXCITER){
	      						exciterMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      						
	      						//sb.append(lineStr+" /"+"\n");
	      					}
	      					else if(type==DynModelType.TUR_GOV){
	      						turGovMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      						sb.append(lineStr+" /"+"\n");
	      					}
	      					else if (type==DynModelType.LOAD){
	      						loadMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      					}
	      					else if (type==DynModelType.RELAY){
	      						relayMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	      					}
	      					
	      					//save supported model data
	      					if(isDebug) dynLibHelper.saveSupportedModelData(lineStr);
      					}
      					else{
      						//System.out.println("model unsupported :"+lineStr);
      						//throw new Exception("The input dynamic model is not supported yet, Type #"+modelType);
      						if(isDebug) dynLibHelper.procUnsupportedModel(modelType, lineStr);
      					}
      				}
      				
      			}
      		}while(lineStr!=null);
		
  		} catch (Exception e) {
    		throw new ODMException("PSSE dynamic data input error, line # " + lineNo + ", " + e.toString());
  		}

  		if(isDebug) System.out.println(dynLibHelper.getUnsupportdSVCRecs());
  		/*
  		try {
			//OutputStream out = new BufferedOutputStream(new FileOutputStream("mach_data.txt"));
			OutputStream out = new BufferedOutputStream(new FileOutputStream("gov_generic_data.txt"));
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			
		}
		*/
  		
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
    	String[] strAry = null;
    	if (lineStr.contains(","))
    		strAry=lineStr.split("\\s*(\\s|,)\\s*");
    	else
    	    strAry =lineStr.split("\\s+");
    	
    	if(strAry.length>2){
    		if(ODMModelStringUtil.trimQuote(strAry[1]).equals("USRLOD") || ODMModelStringUtil.trimQuote(strAry[1]).equals("USRMDL")){
    			 return(ODMModelStringUtil.trimQuote(strAry[3]));
    		}
    		else
    	         return(ODMModelStringUtil.trimQuote(strAry[1]));
    	}

    	return null;
    		
    }
    private boolean skipInvalidLine(String lineStr){
    	if(lineStr.trim().length()==0){
    		return true;
    	}
		
    	
		if(lineStr.trim().startsWith("//") || lineStr.trim().startsWith("/"))
			return true;
		
		// skip a line if it is not started with a bus Number, which is an integer, 
	    
    	String[] strAry = null;
    	if (lineStr.contains(","))
    		strAry=lineStr.trim().split("\\s*(\\s|,)\\s*");
    	else
    	    strAry =lineStr.trim().split("\\s+");
    	
	    if(strAry[0].matches("\\d+"))
	    	return false;
	    else
	    	return true;
	        
	}
}

