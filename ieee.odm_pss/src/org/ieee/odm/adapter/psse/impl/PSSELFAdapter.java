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
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @Author Mike Zhou
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.psse.impl;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEAreaDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEBusDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEDcLine2TDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEFixedShuntDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEGenDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEHeaderDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEInterAreaTransferDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSELineDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSELoadDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEOwnerDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSESwitchedSShuntDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEXfrDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEXfrZTableDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEZoneDataMapper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkCategoryEnumType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

public class PSSELFAdapter extends BasePSSEAdapter{

	private PSSEHeaderDataMapper headerDataMapper = null;	
	private PSSEAreaDataMapper areaDataMapper = null;
	private PSSEZoneDataMapper zoneDataMapper = null;
	private PSSEOwnerDataMapper ownerDataMapper = null;
	private PSSEInterAreaTransferDataMapper interAreaDataMapper = null;
	private PSSEXfrZTableDataMapper zTableDataMapper = null;
	
	private PSSEBusDataMapper busDataMapper = null;
	private PSSEGenDataMapper genDataMapper = null;
	private PSSEFixedShuntDataMapper fixedShuntDataMapper = null;
	private PSSELoadDataMapper loadDataMapper = null;
	private PSSESwitchedSShuntDataMapper switchedShuntDataMapper = null;
	
	private PSSELineDataMapper lineDataMapper = null;
	private PSSEXfrDataMapper xfrDataMapper = null;
	private PSSEDcLine2TDataMapper dcLine2TDataMapper = null;
	
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
	private 	boolean gneDeviceProcessed = false;     // introduced in V33
	private 	boolean indMotorProcessed = false;      // introduced in V33
		
	private 	int busCnt = 0, loadCnt = 0, fxiedShuntCnt = 0, genCnt = 0, lineCnt = 0, xfrCnt = 0, xfr3WCnt = 0, xfrZTableCnt = 0,
		    areaInterCnt = 0, dcLineCnt = 0, vscDcLineCnt = 0, mtDcLineCnt = 0, factsCnt = 0,
		    switchedShuntCnt = 0, ownerCnt = 0, interTransCnt = 0, zoneCnt = 0, multiSecCnt = 0,
		    gneDeviceCnt = 0, indMotorCnt = 0;	
	
	public PSSELFAdapter(PsseVersion ver) {
		super(ver);
		
		this.headerDataMapper = new PSSEHeaderDataMapper(ver);
		this.areaDataMapper = new PSSEAreaDataMapper(ver);
		this.zoneDataMapper = new PSSEZoneDataMapper(ver);
		this.ownerDataMapper = new PSSEOwnerDataMapper(ver);
		this.interAreaDataMapper = new PSSEInterAreaTransferDataMapper(ver);
		this.zTableDataMapper = new PSSEXfrZTableDataMapper(ver);
		this.busDataMapper = new PSSEBusDataMapper(ver);
		this.genDataMapper = new PSSEGenDataMapper(ver);
		this.loadDataMapper = new PSSELoadDataMapper(ver);
		this.fixedShuntDataMapper = new PSSEFixedShuntDataMapper(ver);
		this.switchedShuntDataMapper = new PSSESwitchedSShuntDataMapper(ver);
		this.lineDataMapper = new PSSELineDataMapper(ver);
		this.xfrDataMapper = new PSSEXfrDataMapper(ver);
		this.dcLine2TDataMapper = new PSSEDcLine2TDataMapper(ver);
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
      			if (lineStr != null) {
      				lineNo++;
      				if (!headerProcessed) {
  						String lineStr2 = din.readLine(); lineNo++;
  						String lineStr3 = din.readLine(); lineNo++;
						this.headerDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, getParser());
  						headerProcessed = true;
      				}
      				else if (!busProcessed) {
      					processBusLineStr(lineStr);	 
      				}
      				else if (!loadProcessed) {
      					processLoadLineStr(lineStr);	 
      				}
      				else if (!fixedShuntProcessed && PSSEAdapter.getVersionNo(this.adptrtVersion) >= 31) {
      					processFixedShuntLineStr(lineStr);		 
      				}      				
      				else if (!genProcessed) {
      					processGenLineStr(lineStr);		 
      				}
      				else if (!lineProcessed) {
      					processBranchLineStr(lineStr);		 
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
      					processVscHvdcLineStr(lineStr);	 
      				}
      				else if (!switchedShuntProcessed && PSSEAdapter.getVersionNo(this.adptrtVersion) <= 30) {
      					processSwitchedShuntLineStr(lineStr);	 
      				}
      				else if (!xfrZCorrectionProcessed) {
      					processXfrZCorrLineStr(lineStr);	 
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
      				else if (!switchedShuntProcessed && PSSEAdapter.getVersionNo(this.adptrtVersion) > 30) {
      					processSwitchedShuntLineStr(lineStr); 
     				}
      				
      				else if (!gneDeviceProcessed && PSSEAdapter.getVersionNo(this.adptrtVersion) >= 33) {
      					processGNELineStr(lineStr);	 
     				}      
      				else if (!indMotorProcessed && PSSEAdapter.getVersionNo(this.adptrtVersion) >= 33) {
      					processIndMotorLineStr(lineStr);	 
     				}      				
      			}
    		} while (lineStr != null);
  		} catch (Exception e) {
  			e.printStackTrace();
    		throw new ODMException("PSSE data input error, line no " + lineNo + ", " + e.toString());
  		}
             
		//AclfParserHelper.createBusEquivData(parser);
  		
   	   	return parser;
	}
	
	private void processBusLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF BUS DATA"); 
			 busProcessed = true;
			 ODMLogger.getLogger().info("PSS/E Bus record processed");
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
			ODMLogger.getLogger().info("PSS/E Load record processed");
			this.elemCntStr += "Load record " + loadCnt +"\n";
		}
		else {
			loadDataMapper.procLineString(lineStr, getParser());
			loadCnt++;
		}
	}
	
	private void processGenLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF GENERATOR DATA"); 
			genProcessed = true;
			ODMLogger.getLogger().info("PSS/E Gen record processed");
			this.elemCntStr += "Gen record " + genCnt +"\n";
		}
		else {
			genDataMapper.procLineString(lineStr, getParser());
			genCnt++;
		}
	}
	
	private void processFixedShuntLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF FIXED SHUNT DATA"); 
			fixedShuntProcessed = true;
			 ODMLogger.getLogger().info("PSS/E Fixed Shunt record processed");
			 this.elemCntStr += "Load record " + loadCnt +"\n";
		}
		else {
			fixedShuntDataMapper.procLineString(lineStr, getParser());
			fxiedShuntCnt++;
		}
	}
	
	private void processBranchLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("BRANCH DATA"); 
			lineProcessed = true;
			ODMLogger.getLogger().info("PSS/E Line record processed");
			this.elemCntStr += "Line record " + lineCnt +"\n";
		}
		else {
			lineDataMapper.procLineString(lineStr, getParser());
			lineCnt++;
		}
	}

	private void processXfrLineStr(String lineStr, final IFileReader din) throws ODMException, ODMBranchDuplicationException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF TRANSFORMER DATA"); 
			xfrProcessed = true;
			ODMLogger.getLogger().info("PSS/E Xfr record processed");
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
			assert lineStr.toUpperCase().contains("AREA"); 
			areaInterProcessed = true;
			ODMLogger.getLogger().info("PSS/E AreaInterchange record processed");
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
			ODMLogger.getLogger().info("PSS/E Zone record processed");
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
			ODMLogger.getLogger().info("PSS/E Owner record processed");
			this.elemCntStr += "Owner record " + ownerCnt +"\n";
		}
		else {
			ownerDataMapper.procLineString(lineStr, getParser());
			ownerCnt++;
		}	 		
	}
	
	private void process2THvdcLineStr(String lineStr, final IFileReader din) throws ODMException, ODMBranchDuplicationException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("TWO-TERMINAL"); 
			dcLine2TProcessed = true;
			ODMLogger.getLogger().info("PSS/E DC line record processed");
			this.elemCntStr += "2T DC line record " + dcLineCnt +"\n";
		}
		else {
				String lineStr2 = din.readLine();
				String lineStr3 = din.readLine();
			this.dcLine2TDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, getParser());
			dcLineCnt++;
		}			
	}
	
	private void processVscHvdcLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF VSC DC LINE DATA"); 
			vscDcLineProcessed = true;
			ODMLogger.getLogger().info("PSS/E vscDcLine record processed");
			this.elemCntStr += "vscDcLine record " + vscDcLineCnt +"\n";
		}
		else {
			//	PSSEVscDCLineDataRec rec = new PSSEVscDCLineDataRec(lineStr, version);
			//	rec.processVscDCLine(adjNet, msg);
			vscDcLineCnt++;
		}		
	}

	private void processMTHvdcLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("MULTI-TERMINAL"); 
			dcLineMTProcessed = true;
			ODMLogger.getLogger().info("PSS/E multi terminal DC Line record processed");
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
			assert lineStr.toUpperCase().contains("END OF FACTS DEVICE DATA"); 
			factsProcessed = true;
			ODMLogger.getLogger().info("PSS/E FACTS record processed");
			this.elemCntStr += "Facts record " + factsCnt +"\n";
		}
		else { 
			//PSSEFACTSDataRec rec = new PSSEFACTSDataRec(lineStr, version);
			//rec.processFACTS(adjNet, msg);
			factsCnt++;
		}			
	}

	private void processMultiSecLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF MULTI-SECTION LINE DATA"); 
			multiSectionLineGroupProcessed = true;
			ODMLogger.getLogger().info("PSS/E multi section Line Group record processed");
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
			ODMLogger.getLogger().info("PSS/E switched shunt record processed");
			this.elemCntStr += "Switched Shunt record " + switchedShuntCnt +"\n";
		}
		else {
			switchedShuntDataMapper.procLineString(lineStr, getParser());
			switchedShuntCnt++;
		}
	}
	
	private void processXfrZCorrLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("CORR"); 
			xfrZCorrectionProcessed = true;
			ODMLogger.getLogger().info("PSS/E Xfr table record processed");
			this.elemCntStr += "Xfr table record " + xfrZTableCnt +"\n";
		}
		else {
			zTableDataMapper.procLineString(lineStr, getParser());
			xfrZTableCnt++;
		}		
	}
	
	private void processInterAreaTransferLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("INTER-AREA") || lineStr.toUpperCase().contains("INTERAREA"); 
			interareaTransferProcessed = true;
			ODMLogger.getLogger().info("PSS/E Interarea Transfer record processed");
			this.elemCntStr += "Interarea transfer record " + interTransCnt +"\n";
		}
		else {
			interAreaDataMapper.procLineString(lineStr, getParser());
			interTransCnt++;
		}
	}
	
	private void processGNELineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF GNE DATA"); 
			gneDeviceProcessed = true;
			ODMLogger.getLogger().info("PSS/E GNE record processed");
			this.elemCntStr += "Load record " + loadCnt +"\n";
		}
		else {
			gneDeviceCnt++;
		}		
	}
	
	private void processIndMotorLineStr(String lineStr) throws ODMException {
		if (isEndRecLine(lineStr)) {
			assert lineStr.toUpperCase().contains("END OF INDUCTION MACHINE DATA"); 
			indMotorProcessed = true;
			ODMLogger.getLogger().info("PSS/E INDUCTION MACHINE record processed");
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
