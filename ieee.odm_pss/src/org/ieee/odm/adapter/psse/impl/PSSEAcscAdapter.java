/*
 * @(#)PSSEAcscAdapter.java   
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

import java.util.StringTokenizer;

import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEBranchZeroSeqMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEFixedShuntZeroSeqMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEMachineNegSeqZMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEMachinePosSeqZMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEMachineZeroSeqZMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEShuntLoadNegSeqMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEShuntLoadZeroSeqMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSESwitchShuntZeroSeqMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEXfrZeroSeqDataMapper;
import org.ieee.odm.adapter.psse.mapper.acsc.PSSEZeroSeqMutualZMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;

public class PSSEAcscAdapter <
				TNetXml extends NetworkXmlType, 
				TBusXml extends BusXmlType,
				TLineXml extends BranchXmlType,
				TXfrXml extends BranchXmlType,
				TPsXfrXml extends BranchXmlType> extends PSSELFAdapter<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>{
	
	/*
	 * PSS/E V30 Sequence Data sections and their sequence:
	 *  1 Change code
	 *  2 Positive sequence Generator Impedance data
	 *  3 Negative sequence Generator Impedance data
	 *  4 Zero sequence Generator Impedance data
	 *  5 Negative sequence shunt load
	 *  6 Zero sequence shunt load
	 *  7 Zero sequence non-transformer branch data
	 *  8 Zero sequence Mutual Impedance Data
	 *  9 Zero sequence Transformer Data
	 *  10 Zero sequence Switched Shunt Data
	 * 
	 */
	
	// Data Mappers
	
	private PSSEMachineNegSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> machNegSeqZMapper =null;
	private PSSEMachinePosSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> machPosSeqZMapper =null;
	private PSSEMachineZeroSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> machZeroSeqZMapper =null;
	private PSSEShuntLoadNegSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> shuntLoadNegSeqMapper =null;
	private PSSEShuntLoadZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> shuntLoadZeroSeqMapper =null;
	private PSSEBranchZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>  branchZeroSeqMapper= null;
	private PSSEZeroSeqMutualZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>  zeroSeqMutualZMapper= null;
	private PSSEXfrZeroSeqDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> xfrZeroSeqMapper =null;
	private PSSESwitchShuntZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> switchShuntZeroSeqMapper =null;
	
	// Fixed shunt NOT Included in V30
	private PSSEFixedShuntZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> fixedShuntZeroSeqMapper = null;
	
	public PSSEAcscAdapter(PsseVersion ver) {
		super(ver);
		
		machPosSeqZMapper = new PSSEMachinePosSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		machNegSeqZMapper = new PSSEMachineNegSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		machZeroSeqZMapper = new PSSEMachineZeroSeqZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		shuntLoadNegSeqMapper = new PSSEShuntLoadNegSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> (ver);
		shuntLoadZeroSeqMapper = new PSSEShuntLoadZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		branchZeroSeqMapper= new PSSEBranchZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		zeroSeqMutualZMapper = new PSSEZeroSeqMutualZMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		xfrZeroSeqMapper = new PSSEXfrZeroSeqDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		switchShuntZeroSeqMapper = new PSSESwitchShuntZeroSeqMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
	}

	private BaseAcscModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> getParser() {
		return (BaseAcscModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) this.parser;
	}
	
	/*
	 *First record in the first valid line: Change code -- IC
	 *
	 *IC = 0 indicates initialization setting is enabled, those with
	 *no data record entered are set to be their default values.
	 *
	 *IC = 1 indicates modification/update setting is enabled, the records defined
	 *in the input sequence data will be changed to the new value, while the rest are unchanged
	 *
	 *
	 * In the first phase, case of IC =0 is supported, that is  every time sequence data is input for parsing, it
	 * is to initialize the sequence network.
	 */
	
	
	
	@Override 
	public IODMModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
		 
		// check parser
		if(parser ==null){
			throw new ODMException("Parser is not initialized before parsing Acsc data");
		}
		
		//check net
		//ShortCircuitNetXmlType scNet = (ShortCircuitNetXmlType) parser.getNet();
		
		
		String lineStr = null;
  		int lineNo = 0;
  		try {
      		boolean headerProcessed = false;
      		boolean machPosZProcessed = false;
      		boolean machNegZProcessed = false;
      		boolean machZeroZProcessed = false;
      		boolean shuntLoadNegProcessed = false;
      		boolean shuntLoadZeroProcessed = false;
      		boolean lineZeroProcessed = false;
      		boolean ZeroMutualProcessed = false;
      		boolean xfrZeroProcessed = false;
      		boolean switchZeroProcessed = false;
      	
      		
      		int machPosZCnt = 0, machNegZCnt = 0, machZeroZCnt = 0, shuntLoadNegCnt = 0, shuntLoadZeroCnt = 0, 
      				lineZeroCnt = 0, ZeroMutualCnt = 0, xfrZeroCnt = 0, switchShuntZeroCnt = 0;
      		 
      		
      		do {
      			lineStr = din.readLine();
      			if (lineStr != null) {
      				lineNo++;
      				if (!headerProcessed) {
      					StringTokenizer st = new StringTokenizer(lineStr, ",");
  						String IC= st.nextToken().trim();
  						
  						if(IC.equals("1")){
  							throw new ODMException("Sequence data Header info check failed, only IC=0 is supported now!");
  						}
  						
  						int version = Integer.parseInt(st.nextToken().trim().substring(0, 2));
  						if(version>32){
  							throw new ODMException("Sequence data Header info check failed, only version 30-32 is supported now! Input Verson is #"+version);
  						}
  						headerProcessed = true;
      				}
      				else if (!machPosZProcessed) {
      					if (isEndRecLine(lineStr)) {
      						machPosZProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Positive sequence Generator data record processed");
							 this.elemCntStr += "Positive sequence Generator record " + machPosZCnt +"\n";
						}	 
						else {
							machPosSeqZMapper.procLineString(lineStr, getParser());
							machPosZCnt++;
						}
      				}
      				else if (!machNegZProcessed) {
      					if (isEndRecLine(lineStr)) {
      						machNegZProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Negative sequence Generator data record processed");
							 this.elemCntStr += "Negative sequence Generator record " + machNegZCnt +"\n";
						}	 
						else {
							machNegSeqZMapper.procLineString(lineStr, getParser());
							machNegZCnt++;
						}
      				}
      				else if (!machZeroZProcessed) {
      					if (isEndRecLine(lineStr)) {
      						machZeroZProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence Generator data record processed");
							 this.elemCntStr += "Zero sequence Generator record " + machZeroZCnt +"\n";
						}	 
						else {
							machZeroSeqZMapper.procLineString(lineStr, getParser());
							machZeroZCnt++;
						}
      				}
      				else if (!shuntLoadNegProcessed) {
      					if (isEndRecLine(lineStr)) {
      						shuntLoadNegProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Negative sequence shunt load data record processed");
							 this.elemCntStr += "Negative sequence shunt load record " + shuntLoadNegCnt +"\n";
						}	 
						else {
							shuntLoadNegSeqMapper.procLineString(lineStr, getParser());
							shuntLoadNegCnt++;
						}
      				}
      				else if (!shuntLoadZeroProcessed) {
      					if (isEndRecLine(lineStr)) {
      						shuntLoadZeroProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence shunt load data record processed");
							 this.elemCntStr += "Zero sequence shunt load record " + shuntLoadZeroCnt +"\n";
						}	 
						else {
							shuntLoadZeroSeqMapper.procLineString(lineStr, getParser());
							shuntLoadZeroCnt++;
						}
      				}
      				else if (!lineZeroProcessed) {
      					if (isEndRecLine(lineStr)) {
      						lineZeroProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence non-transformer data record processed");
							 this.elemCntStr += "Zero sequence non-transformer record " + lineZeroCnt +"\n";
						}	 
						else {
							branchZeroSeqMapper.procLineString(lineStr, getParser());
							lineZeroCnt++;
						}
      				}
      				else if (!ZeroMutualProcessed) {
      					if (isEndRecLine(lineStr)) {
      						ZeroMutualProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence multual impedance data record processed");
							 this.elemCntStr += "Zero sequence multual impedance record " + ZeroMutualCnt +"\n";
						}	 
						else {
							zeroSeqMutualZMapper.procLineString(lineStr, getParser());
							ZeroMutualCnt++;
						}
      				}
      				else if (!xfrZeroProcessed) {
      					if (isEndRecLine(lineStr)) {
      						xfrZeroProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence transformer data record processed");
							 this.elemCntStr += "Zero sequence transformer  record " + xfrZeroCnt +"\n";
						}	 
						else {
							xfrZeroSeqMapper.procLineString(lineStr, getParser());
							xfrZeroCnt++;
						}
      				}
      				else if (!switchZeroProcessed) {
      					if (isEndRecLine(lineStr)) {
      						switchZeroProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Zero sequence switch shunt data record processed");
							 this.elemCntStr += "Zero sequence switch shunt  record " + switchShuntZeroCnt +"\n";
						}	 
						else {
							switchShuntZeroSeqMapper.procLineString(lineStr, getParser());
							switchShuntZeroCnt ++;
						}
      				}
      				else{
      					//Not supported yet
      				}
      					
      			}//END IF lineStr!= null
      			
      		} while (lineStr != null);//END OF DO-LOOP
  		} catch (Exception e) {
  			e.printStackTrace();
    		throw new ODMException("PSSE data input error, line no " + lineNo + ", " + e.toString());
  		}
  		
		AcscParserHelper.createBusScEquivData(parser);		
		
		return parser;
		
	}
	
	
	
	/*
	 * post processing
	 * 
	 * 1. Gen zero sequence
	 * 
	 * During the initial input of sequence data (i.e., IC = 0 on the first data record), any machine for which
       no data record of this category is entered has its zero sequence generator impedance, ZZERO, set
       equal to ZPOS, the positive sequence generator impedance
       
       For those machines at which the step-up transformer is represented as part of the generator data
      (i.e., XTRAN is non-zero), ZZERO (i.e., RZERO + j XZERO) is not used and, in the fault analysis
       activities, the step-up transformer is assumed to be a delta wye transformer, that means 
       Gen is open from the zero sequence network.
       Refer to Modeling of Generator Step-Up Transformers (GSU).
       
	 * 
	 * 2. bus negative sequence shunt load
	 * 
	 * For any bus where no such data record is specified, or GNEG and BNEG are both specified as zero,
       the load elements are assumed to be equal in the positive and negative sequence networks.
	 * 
	 */

	
	
	/**
	 * 
	 */
	@Override
	public IODMModelParser parseInputFile(NetType type, IFileReader[] din,
			String encoding) throws ODMException {
		if(type ==NetType.AcscNet){
			// initialize the parser first
			if(parser == null){
				parser = new AcscModelParser();
				parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.SHORT_CIRCUIT);
				
			}
			
		}
		else if(type==NetType.DStabNet){
			// the parser is supposed to be set at the PSSEDstabParser class, which calls this method
			if(parser == null){
				throw new ODMException("Parser is not initialized before parsing Acsc data for Dstab NetType ");
			}
		}
		
		if(parser != null){
		//the first file is supposed to be the Load flow data file, reuse the PSSELFAdapter class method 
	    //to parse the load flow file
		super.parseInputFile(din[0], encoding);
		
		//Aclf data is included
		if(din[0]!=null){
			ShortCircuitNetXmlType baseCaseNet = (ShortCircuitNetXmlType) parser.getNet();
			baseCaseNet.setHasLoadflowData(true);
		}
		
		if(din[1]==null){
			ODMLogger.getLogger().warning("PSSEAcscAdater: Sequence network data is not provided or invalid, please check!");
		}
		     //the second one is the sequence data file;
		else parseInputFile(din[1], encoding);
		}
		
		return parser;
	}
	 
}
