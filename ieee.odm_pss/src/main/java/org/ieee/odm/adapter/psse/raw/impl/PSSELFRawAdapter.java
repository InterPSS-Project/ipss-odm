/*
 * @(#)PSSELFAdapter.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * @Author Mike Zhou
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

 package org.ieee.odm.adapter.psse.raw.impl;

 import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
 import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEAreaDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEBusDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEDcLine2TDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEFactsDeviceDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEFixedShuntDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEGenDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEHeaderDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEInterAreaTransferDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSELineDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSELoadDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEOwnerDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSESwitchedShuntDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSESwitchingDeviceDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEVSCHVDC2TDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEXfrDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEXfrZTableDataRawMapper;
 import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEZoneDataRawMapper;
 import org.ieee.odm.common.IFileReader;
 import org.ieee.odm.common.ODMBranchDuplicationException;
 import org.ieee.odm.common.ODMException;
 import org.ieee.odm.model.IODMModelParser;
 import org.ieee.odm.model.aclf.AclfModelParser;
 import org.ieee.odm.model.aclf.BaseAclfModelParser;
 import org.ieee.odm.schema.AnalysisCategoryEnumType;
 import org.ieee.odm.schema.LoadflowNetXmlType;
 import org.ieee.odm.schema.NetworkCategoryEnumType;
 import org.ieee.odm.schema.NetworkXmlType;
 import org.ieee.odm.schema.OriginalDataFormatEnumType;

 import java.util.ArrayList;
 import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 public class PSSELFRawAdapter extends BasePSSERawAdapter{

    private static final Logger log = LoggerFactory.getLogger(PSSELFRawAdapter.class);
 
	 private PSSEHeaderDataRawMapper headerDataMapper = null;	
	 private PSSEAreaDataRawMapper areaDataMapper = null;
	 private PSSEZoneDataRawMapper zoneDataMapper = null;
	 private PSSEOwnerDataRawMapper ownerDataMapper = null;
	 private PSSEInterAreaTransferDataRawMapper interAreaDataMapper = null;
	 private PSSEXfrZTableDataRawMapper zTableDataMapper = null;
	 
	 private PSSEBusDataRawMapper busDataMapper = null;
	 private PSSEGenDataRawMapper genDataMapper = null;
	 private PSSEFixedShuntDataRawMapper fixedShuntDataMapper = null;
	 private PSSELoadDataRawMapper loadDataMapper = null;
	 private PSSESwitchedShuntDataRawMapper switchedShuntDataMapper = null;
	 
	 private PSSELineDataRawMapper lineDataMapper = null;
	 private PSSEXfrDataRawMapper xfrDataMapper = null;
	 private PSSEDcLine2TDataRawMapper dcLine2TDataMapper = null;
	 private PSSEVSCHVDC2TDataRawMapper vschvdc2TDataMapper = null;
	 
	 private PSSESwitchingDeviceDataRawMapper switchingDeviceDataMapper = null;
	 private PSSEFactsDeviceDataRawMapper  factsDataMapper= null;
	 
	 private 	boolean headerProcessed = false;
	 private 	boolean busProcessed = false;
	 private 	boolean loadProcessed = false;
	 private 	boolean fixedShuntProcessed = false;     // introduced in V32
	 private 	boolean genProcessed = false;
	 private 	boolean lineProcessed = false;
	 private 	boolean xfrProcessed = false;
	 private 	boolean areaInterProcessed = false;
	 private 	boolean dcLine2TProcessed = false;
	 private 	boolean vscDcLineProcessed = false;
	 private 	boolean switchedShuntProcessed = false;
	 private 	boolean xfrZCorrectionProcessed = false;
	 private 	boolean dcLineMTProcessed = false;
	 private 	boolean multiSectionLineGroupProcessed = false;
	 private 	boolean zoneProcessed = false;
	 private 	boolean interareaTransferProcessed = false;
	 private 	boolean ownerProcessed = false;
	 private 	boolean factsProcessed = false;
	 
	 private 	boolean switchingDeviceProcessed = false; // introduced in V34
	 private 	boolean gneDeviceProcessed = false;     // introduced in V34
	 private 	boolean indMotorProcessed = false;      // introduced in V34
	 private 	boolean substationProcessed = false;      // introduced in V34
 
	 private 	boolean voltDroopCtrlProcessed = false;      // introduced in V36
	 private 	boolean switchingDeviceRatingProcessed = false; // introduced in V36
		 
	 private 	int busCnt = 0, loadCnt = 0, fixedShuntCnt = 0, voltDroopCnt=0, genCnt = 0, 
			 switchingDeviceRatingCnt =0, lineCnt = 0, xfrCnt = 0, xfr3WCnt = 0, xfrZTableCnt = 0,
			 areaInterCnt = 0, dcLineCnt = 0, vscDcLineCnt = 0, mtDcLineCnt = 0, factsCnt = 0,
			 switchedShuntCnt = 0, ownerCnt = 0, interTransCnt = 0, zoneCnt = 0, multiSecCnt = 0,
			 gneDeviceCnt = 0, indMotorCnt = 0, switchingDeviceCnt=0;	
	 
	 public PSSELFRawAdapter(PsseVersion ver) {
		 super(ver);
		 
		 this.headerDataMapper = new PSSEHeaderDataRawMapper(ver);
		 this.areaDataMapper = new PSSEAreaDataRawMapper(ver);
		 this.zoneDataMapper = new PSSEZoneDataRawMapper(ver);
		 this.ownerDataMapper = new PSSEOwnerDataRawMapper(ver);
		 this.interAreaDataMapper = new PSSEInterAreaTransferDataRawMapper(ver);
		 this.zTableDataMapper = new PSSEXfrZTableDataRawMapper(ver);
		 this.busDataMapper = new PSSEBusDataRawMapper(ver);
		 this.genDataMapper = new PSSEGenDataRawMapper(ver);
		 this.loadDataMapper = new PSSELoadDataRawMapper(ver);
		 this.fixedShuntDataMapper = new PSSEFixedShuntDataRawMapper(ver);
		 this.switchedShuntDataMapper = new PSSESwitchedShuntDataRawMapper(ver);
		 this.lineDataMapper = new PSSELineDataRawMapper(ver);
		 this.xfrDataMapper = new PSSEXfrDataRawMapper(ver);
		 this.dcLine2TDataMapper = new PSSEDcLine2TDataRawMapper(ver);
		 this.vschvdc2TDataMapper = new PSSEVSCHVDC2TDataRawMapper(ver);
		 
		 this.switchingDeviceDataMapper = new PSSESwitchingDeviceDataRawMapper(ver);
		 this.factsDataMapper = new PSSEFactsDeviceDataRawMapper(ver);
	 }
	 
	 /**
	  * Parse PSS/E load flow input file into ODM/XML 
	  * 
	  * @param din
	  * @param encoding
	  * @return
	  * @throws Exception
	  */
	 public AclfModelParser parseLoadflowFile(final IFileReader din, String encoding) throws ODMException {
		 this.parser = new AclfModelParser();
		 
		 this.parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.LOADFLOW);
		 
		 //parser the input load flow data
		 parseInputFile(din, encoding);
	 
		 return (AclfModelParser) this.parser;
	 }
	 
	 private BaseAclfModelParser<? extends NetworkXmlType> getParser() {
		 return (BaseAclfModelParser<? extends NetworkXmlType>) this.parser;
	 }
	 
	 @Override
	 protected IODMModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
		 //set case base info
		 this.parser.setCaseContentInfo(OriginalDataFormatEnumType.PSS_E);
		 this.parser.getStudyCase().setNetworkCategory(NetworkCategoryEnumType.TRANSMISSION);
		 this.parser.getStudyCase().getContentInfo().setOriginalFormatVersion(this.adptrtVersion.toString());
 
		 LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) this.parser.getNet();
		 // no space is allowed for ID field
		 baseCaseNet.setId("Base_Case_from_PSS_E_format");
 
		   String lineStr = null;
		   int lineNo = 0;
		   try {
			   do {
				   lineStr = din.readLine();
				   //ODMLogger.getLogger().log(Level.INFO, "Reading line: " + lineStr);
				   if (lineStr != null) {
					   lineNo++;
					   if(lineStr.startsWith("//") || lineStr.startsWith("@!")) { // it is a comment line or section field definition line
						   continue;
					   }
					   if (!headerProcessed) {
						   processHeaderLineStr(lineStr, din, lineNo);
					   }
					   else if (!busProcessed) {
						   processBusLineStr(lineStr);	 
					   }
					   else if (!loadProcessed) {
						   processLoadLineStr(lineStr);	 
					   }
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 31 && !fixedShuntProcessed) {
						   processFixedShuntLineStr(lineStr);		 
					   }
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 36 && !voltDroopCtrlProcessed) {// available starting at v36
						   processVoltDroopCtrlLineStr(lineStr);		 
					   }
					   else if (!genProcessed) {
						   processGenLineStr(lineStr);		 
					   }
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion)>=36  && !switchingDeviceRatingProcessed) {// available starting at v36
						   processSwitchingDeviceRatingSetLineStr(lineStr);
					   }
					   else if (!lineProcessed) {
						   processBranchLineStr(lineStr);		 
					   }
					   else if(PSSERawAdapter.getVersionNo(this.adptrtVersion)>33  && !switchingDeviceProcessed) { // available starting at v34
						   processSwitchingDeviceLineStr(lineStr);
					   }
					   else if (!xfrProcessed) {
						   if (!isEndRecLine(lineStr)) {
							   lineNo = lineNo + 3;
							   if (is3WXfr(lineStr))
								   lineNo++;
						   }
						   processXfrLineStr(lineStr, din);
					   }
					   else if (!areaInterProcessed) {
						   processAreaLineStr(lineStr);		 
					   }
					   else if (!dcLine2TProcessed) {
						   if (!isEndRecLine(lineStr))
							   lineNo = lineNo + 2;
						   process2THvdcLineStr(lineStr, din);
					   }
					   else if (!vscDcLineProcessed) {
						   if (!isEndRecLine(lineStr))
							   lineNo = lineNo + 2;
						   processVscHvdcLineStr(lineStr, din);	 
					   }
					   
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) <= 30 && !switchedShuntProcessed) {
						   processSwitchedShuntLineStr(lineStr); 
					  }
		   
					   else if (!xfrZCorrectionProcessed) {
						   processXfrZCorrLineStr(lineStr,din);	 
					   }
					   else if (!dcLineMTProcessed) {
						   processMTHvdcLineStr(lineStr);	 
					   }
					   else if (!multiSectionLineGroupProcessed) {
						   processMultiSecLineStr(lineStr);	 
					   }
					   else if (!zoneProcessed) {
						   processZoneLineStr(lineStr);		 
					   }
					   else if (!interareaTransferProcessed) {
						   processInterAreaTransferLineStr(lineStr);	 
					   }
					   else if (!ownerProcessed) {
						   processOwnerLineStr(lineStr);	 
					   }
					   else if (!factsProcessed) {
						   processFACTSLineStr(lineStr);	 
					   }
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 31 && !switchedShuntProcessed) {
						   processSwitchedShuntLineStr(lineStr); 
					  }
					   
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 33 && !gneDeviceProcessed) {
						   processGNELineStr(lineStr);	 
					  }      
					   else if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 33 && !indMotorProcessed) {
						   processIndMotorLineStr(lineStr);	 
					  }      				
				   }
			 } while (lineStr != null);
		   } catch (Exception e) {
			 throw new ODMException("PSSE power flow data input error, line # " + lineNo + ", Line Str = "+lineStr +"error:"+ e.toString());
		   }
			  
		 //AclfParserHelper.createBusEquivData(parser);
		   
			   return parser;
	 }
	 
	 private void processHeaderLineStr(String lineStr, final IFileReader din, int lineNo) throws ODMException {
		 
			 String lineStr2 = din.readLine(); lineNo++;
			 String lineStr3 = din.readLine(); lineNo++;
			 this.headerDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, getParser());
			 
			 //for version 34 and newer, there are additional system level data
			 if(PSSERawAdapter.getVersionNo(this.adptrtVersion)>33) {
				 processSystemDataStr(lineStr, din, lineNo);
			 }
			 else
				 headerProcessed = true;
					 
	 }
	 
	 private void processSystemDataStr(String lineStr, final IFileReader din, int lineNo) throws ODMException {
		 
			 while(!headerProcessed) {
				 String line= din.readLine();
				 if (isEndRecLine(line)) {
					 assert line.toUpperCase().contains("END OF SYSTEM-WIDE DATA"); 
					 headerProcessed = true;
					 log.info("PSS/E System record processed");
				 }
				 lineNo++;
			 }
		 
 }
	 
	 
	 private void processBusLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF BUS DATA"); 
			 busProcessed = true;
			 log.info("PSS/E Bus record processed");
			 this.elemCntStr += "Bus record " + busCnt +"\n";
		 }	 
		 else {
			 busDataMapper.procLineString(lineStr, getParser());
			 busCnt++;
		 }			
	 }
 
	 private void processLoadLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF LOAD DATA"); 
			 loadProcessed = true;
			 log.info("PSS/E Load record processed");
			 this.elemCntStr += "Load record " + loadCnt +"\n";
		 }
		 else {
			 loadDataMapper.procLineString(lineStr, getParser());
			 loadCnt++;
		 }
	 }
	 
	 private void processVoltDroopCtrlLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF VOLTAGE DROOP CONTROL DATA"); 
			 voltDroopCtrlProcessed = true;
			 log.info("PSS/E voltage droop control record processed");
			 this.elemCntStr += "Voltage droop control record " + voltDroopCnt +"\n";
		 }
		 else {
			 //genDataMapper.procLineString(lineStr, getParser());
			 voltDroopCnt++;
		 }
	 }
	 
	 private void processGenLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF GENERATOR DATA"); 
			 genProcessed = true;
			 log.info("PSS/E Gen record processed");
			 this.elemCntStr += "Gen record " + genCnt +"\n";
		 }
		 else {
			 genDataMapper.procLineString(lineStr, getParser());
			 genCnt++;
		 }
	 }
	 
	 private void processSwitchingDeviceRatingSetLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF SWITCHING DEVICE RATING SET"); 
			 switchingDeviceRatingProcessed = true;
			 log.info("PSS/E switching device rating set record processed");
			 this.elemCntStr += "Switching device rating set record " + switchingDeviceRatingCnt +"\n";
		 }
		 else {
			 //genDataMapper.procLineString(lineStr, getParser());
			 switchingDeviceRatingCnt++;
		 }
	 }
	 
	 private void processFixedShuntLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF FIXED SHUNT DATA"); 
			 fixedShuntProcessed = true;
			 log.info("PSS/E Fixed Shunt record processed");
			 this.elemCntStr += "Fixed shunt record " + fixedShuntCnt +"\n";
		 }
		 else {
			 fixedShuntDataMapper.procLineString(lineStr, getParser());
			 fixedShuntCnt++;
		 }
	 }
	 
	 private void processBranchLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert (lineStr.toUpperCase().contains("END") && lineStr.toUpperCase().contains("BRANCH")); 
			 lineProcessed = true;
			 log.info("PSS/E Line record processed");
			 this.elemCntStr += "Line record " + lineCnt +"\n";
		 }
		 else {
			 lineDataMapper.procLineString(lineStr, getParser());
			 lineCnt++;
		 }
	 }
	 
	 private void processSwitchingDeviceLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF SYSTEM SWITCHING"); 
			 switchingDeviceProcessed = true;
			 log.info("PSS/E system switching device record processed");
			 this.elemCntStr += "System Switching Device record " + switchingDeviceCnt +"\n";
		 }
		 else {
			 switchingDeviceDataMapper.procLineString(lineStr, getParser());
			 switchingDeviceCnt++;
		 }
	 }
	 
 
	 private void processXfrLineStr(String lineStr, final IFileReader din) throws ODMException, ODMBranchDuplicationException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF TRANSFORMER DATA"); 
			 xfrProcessed = true;
			 log.info("PSS/E Xfr record processed");
			 this.elemCntStr += "2W Xfr record " + xfrCnt +"\n";
			 this.elemCntStr += "3W Xfr record " + xfr3WCnt +"\n";
		 }
		 else {
				 String lineStr2 = din.readLine();
				 String lineStr3 = din.readLine();
				 String lineStr4 = din.readLine();
				 String lineStr5 = "";
				 if (is3WXfr(lineStr)) {
					 lineStr5 = din.readLine();
				 xfr3WCnt++;
				 }
				 else
				 xfrCnt++;
			 xfrDataMapper.procLineString( new String[] { lineStr, lineStr2, lineStr3, lineStr4, lineStr5 }, getParser());
		 }
	 }
	 
	 private void processAreaLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF AREA"); 
			 areaInterProcessed = true;
			 log.info("PSS/E AreaInterchange record processed");
			 this.elemCntStr += "Area interchange record " + areaInterCnt +"\n";
		 }
		 else {
			 this.areaDataMapper.procLineString(lineStr,  getParser());
			 areaInterCnt++;
		 }
	 }
 
	 private void processZoneLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF ZONE DATA"); 
			 zoneProcessed = true;
			 log.info("PSS/E Zone record processed");
			 this.elemCntStr += "Zone record " + zoneCnt +"\n";
		 }
		 else {
			 this.zoneDataMapper.procLineString(lineStr, getParser());
			 zoneCnt++;
		 }
	 }
	 
	 private void processOwnerLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF OWNER DATA"); 
			 ownerProcessed = true;
			 log.info("PSS/E Owner record processed");
			 this.elemCntStr += "Owner record " + ownerCnt +"\n";
		 }
		 else {
			 ownerDataMapper.procLineString(lineStr, getParser());
			 ownerCnt++;
		 }	 		
	 }
	 
	 private void process2THvdcLineStr(String lineStr, final IFileReader din) throws ODMException, ODMBranchDuplicationException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF TWO-TERMINAL"); 
			 dcLine2TProcessed = true;
			 log.info("PSS/E DC line record processed");
			 this.elemCntStr += "2T DC line record " + dcLineCnt +"\n";
		 }
		 else {
				 String lineStr2 = din.readLine();
				 String lineStr3 = din.readLine();
			 this.dcLine2TDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, getParser());
			 dcLineCnt++;
		 }			
	 }
	 
	 private void processVscHvdcLineStr(String lineStr,final IFileReader din) throws ODMException, ODMBranchDuplicationException {
		 if (isEndRecLine(lineStr)) {
			 assert (lineStr.toUpperCase().contains("END OF VSC DC LINE DATA") || 
					 lineStr.toUpperCase().contains("END OF VOLTAGE SOURCE CONVERTER DATA")); 
			 vscDcLineProcessed = true;
			 log.info("PSS/E vscDcLine record processed");
			 this.elemCntStr += "vscDcLine record " + vscDcLineCnt +"\n";
		 }
		 else {
			 String lineStr2 = din.readLine();
			 String lineStr3 = din.readLine();
			 this.vschvdc2TDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, getParser());
			 vscDcLineCnt++;
		 }		
	 }
 
	 private void processMTHvdcLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF MULTI-TERMINAL"); 
			 dcLineMTProcessed = true;
			 log.info("PSS/E multi terminal DC Line record processed");
			 this.elemCntStr += "MT DC line record " + mtDcLineCnt +"\n";
		 }
		 else {
			 //	PSSEMultiTermDCLineDataRec rec = new PSSEMultiTermDCLineDataRec(lineStr, version);
			 //	rec.processMultiTerminalDCLine(adjNet, msg);
			 mtDcLineCnt++;
		 }			
	 }
	 
	 private void processFACTSLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF FACTS"); 
			 factsProcessed = true;
			 log.info("PSS/E FACTS record processed");
			 this.elemCntStr += "Facts record " + factsCnt +"\n";
		 }
		 else { 
			 factsDataMapper.procLineString(lineStr, getParser());
			 factsCnt++;
		 }			
	 }
 
	 private void processMultiSecLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF MULTI-SECTION LINE DATA"); 
			 multiSectionLineGroupProcessed = true;
			 log.info("PSS/E multi section Line Group record processed");
			 this.elemCntStr += "MultiSec record " + multiSecCnt +"\n";
		 }
		 else {
			 //PSSEMultiSecLineDataRec rec = new PSSEMultiSecLineDataRec(lineStr, version);
			 //rec.processMultiSecLine(adjNet, msg);
			 multiSecCnt++;
		 }			
	 }
	 private void processSwitchedShuntLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF SWITCHED SHUNT DATA"); 
			 switchedShuntProcessed = true;
			 log.info("PSS/E switched shunt record processed");
			 this.elemCntStr += "Switched Shunt record " + switchedShuntCnt +"\n";
		 }
		 else {
			 switchedShuntDataMapper.procLineString(lineStr, getParser());
			 switchedShuntCnt++;
		 }
	 }
	 
	 private void processXfrZCorrLineStr(String lineStr,final IFileReader din) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert (lineStr.toUpperCase().contains("END") && lineStr.toUpperCase().contains("CORR")); 
			 xfrZCorrectionProcessed = true;
			 log.info("PSS/E Xfr table record processed");
			 this.elemCntStr += "Xfr table record " + xfrZTableCnt +"\n";
		 }
		 else {
			 // PSS/E version 33 and older has only one line for each record
			 // if version is 34 or newer, we may need to read multiple lines for each record
			 if (PSSERawAdapter.getVersionNo(this.adptrtVersion) >= 34) {
				 // Collect all lines until the record is complete
				 List<String> recordLines = new ArrayList<>();
				 recordLines.add(lineStr);

				 while (!isZCorrRecordComplete(recordLines.get(recordLines.size() - 1))) {
					 String nextLine = din.readLine();
					 if (nextLine == null) {
						 log.error("PSS/E Xfr table record incomplete, reached end of file.");
						 break;
					 }
					 recordLines.add(nextLine);
				 }

				 // Process the collected lines
				 zTableDataMapper.procLineString(recordLines.toArray(new String[0]), getParser());
				 xfrZTableCnt++;
			 } else {
				 // For version 33 and older, process the line directly
				 zTableDataMapper.procLineString(new String[] {lineStr}, getParser());
				 xfrZTableCnt++;
			 }
		 }		
	 }

	 /**
	 * Check if a transformer impedance correction record is complete by examining 
	 * if the last two values are zeros, which indicates the end of meaningful data.
	 * 
	 * @param values the array of string values from the data line
	 * @return true if the record is complete, false if more data is expected
	 */
	private boolean isZCorrRecordComplete(String lineStr) {
		String[] values = lineStr.trim().split(",");
		if (values.length < 3) {
			return false; // Not enough data to make a determination
		}
		
		// Check if the last two values are zeros (or very close to zero)
		try {
			// Format expected: last two values should be 0.0
			double secondLastValue = Double.parseDouble(values[values.length - 2].trim());
			double lastValue = Double.parseDouble(values[values.length - 1].trim());
			
			// Consider values very close to zero (e.g., 0.00000) as zero
			boolean isZeroEnding = Math.abs(secondLastValue) < 0.00001 && Math.abs(lastValue) < 0.00001;
			
			if (isZeroEnding) {
				return true;
			}
			// Also check if we have a complete set of meaningful points
			// If we have a zero at position that could indicate record completion
		} catch (NumberFormatException e) {
			// If we can't parse the numbers, assume the record is not complete
			return false;
		}
		return false;
	}

	 
	 private void processInterAreaTransferLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 assert lineStr.toUpperCase().contains("END OF INTER-AREA") || lineStr.toUpperCase().contains("END OF INTERAREA"); 
			 interareaTransferProcessed = true;
			 log.info("PSS/E Interarea Transfer record processed");
			 this.elemCntStr += "Interarea transfer record " + interTransCnt +"\n";
		 }
		 else {
			 interAreaDataMapper.procLineString(lineStr, getParser());
			 interTransCnt++;
		 }
	 }
	 
	 private void processGNELineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 if(lineStr.toUpperCase().contains("END OF GNE")) { 
				 gneDeviceProcessed = true;
				 log.info("PSS/E GNE record processed");
			 }
			 this.elemCntStr += "Load record " + loadCnt +"\n";
		 }
		 else {
			 gneDeviceCnt++;
		 }		
	 }
	 
	 private void processIndMotorLineStr(String lineStr) throws ODMException {
		 if (isEndRecLine(lineStr)) {
			 if(lineStr.toUpperCase().contains("END OF INDUCTION MACHINE DATA")){
				indMotorProcessed = true;
				log.info("PSS/E INDUCTION MACHINE record processed");
			 }
			 this.elemCntStr += "Load record " + loadCnt +"\n";
		 }
		 else {
			 indMotorCnt++;
		 }		
	 }
	 
	 private boolean is3WXfr(String str) {
		 // for 2W xfr, line1, K = 0
		 /*
		  * Sample : 324558,324023,     0,'1 ',2,2,1,   0.00036,  -0.00197,1,'HFL- 1,2    ',1,   1,1.0000
		  */
		 String[] strAry = str.split(",");
		 /*
		   StringTokenizer st = new StringTokenizer(str, ",");
		 st.nextToken();
		 st.nextToken();
		 int K = new Integer(st.nextToken().trim()).intValue();
		 */
		 int K = new Integer(strAry[2].trim()).intValue();
		 return K != 0;
	 }
 }