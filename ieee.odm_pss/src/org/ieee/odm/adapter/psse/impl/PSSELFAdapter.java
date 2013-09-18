package org.ieee.odm.adapter.psse.impl;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.IFileReader;
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
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkCategoryEnumType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

public class PSSELFAdapter <
				TNetXml extends NetworkXmlType, 
				TBusXml extends BusXmlType,
				TLineXml extends BranchXmlType,
				TXfrXml extends BranchXmlType,
				TPsXfrXml extends BranchXmlType> extends BasePSSEAdapter{

	public final static String Token_CaseDesc = "Case Description";     
	public final static String Token_CaseId = "Case ID";		

	private PSSEHeaderDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> headerDataMapper = null;	
	private PSSEAreaDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> areaDataMapper = null;
	private PSSEZoneDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> zoneDataMapper = null;
	private PSSEOwnerDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> ownerDataMapper = null;
	private PSSEInterAreaTransferDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> interAreaDataMapper = null;
	private PSSEXfrZTableDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> zTableDataMapper = null;
	
	private PSSEBusDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> busDataMapper = null;
	private PSSEGenDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> genDataMapper = null;
	private PSSEFixedShuntDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> fixedShuntDataMapper = null;
	private PSSELoadDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> loadDataMapper = null;
	private PSSESwitchedSShuntDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> switchedShuntDataMapper = null;
	
	private PSSELineDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> lineDataMapper = null;
	private PSSEXfrDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> xfrDataMapper = null;
	private PSSEDcLine2TDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> dcLine2TDataMapper = null;
	
	public PSSELFAdapter(PsseVersion ver) {
		super(ver);
		
		this.headerDataMapper = new PSSEHeaderDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.areaDataMapper = new PSSEAreaDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.zoneDataMapper = new PSSEZoneDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.ownerDataMapper = new PSSEOwnerDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.interAreaDataMapper = new PSSEInterAreaTransferDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.zTableDataMapper = new PSSEXfrZTableDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.busDataMapper = new PSSEBusDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.genDataMapper = new PSSEGenDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.loadDataMapper = new PSSELoadDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.switchedShuntDataMapper = new PSSESwitchedSShuntDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.lineDataMapper = new PSSELineDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.xfrDataMapper = new PSSEXfrDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
		this.dcLine2TDataMapper = new PSSEDcLine2TDataMapper<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>(ver);
	}
    /**
     * Parse PSS/E load flow input file into ODM/XML 
     * @param din
     * @param encoding
     * @return
     * @throws Exception
     */
	public AclfModelParser parseLoadflowFile(final IFileReader din, String encoding) throws ODMException {
		parser = new AclfModelParser();
		
		parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.LOADFLOW);
		
		//parser the input load flow data
		parseInputFile(din, encoding);
	
		return (AclfModelParser) parser;
		
		
	}
	
	@Override
	protected IODMModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
	    //set case base info
		parser.setCaseContentInfo(OriginalDataFormatEnumType.PSS_E);
		parser.getStudyCase().setNetworkCategory(NetworkCategoryEnumType.TRANSMISSION);
		parser.getStudyCase().getContentInfo().setOriginalFormatVersion(this.adptrtVersion.toString());

		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		// no space is allowed for ID field
		baseCaseNet.setId("Base_Case_from_PSS_E_format");

  		String lineStr = null;
  		int lineNo = 0;
  		try {
      		boolean headerProcessed = false;
      		boolean busProcessed = false;
      		boolean loadProcessed = false;
      		boolean fxiedShuntProcessed = false;     // introduced in V32
      		boolean genProcessed = false;
      		boolean lineProcessed = false;
      		boolean xfrProcessed = false;
      		boolean areaInterProcessed = false;
      		boolean dcLine2TProcessed = false;
      		boolean vscDcLineProcessed = false;
      		boolean switchedShuntProcessed = false;
      		boolean xfrZCorrectionProcessed = false;
      		boolean dcLineMTProcessed = false;
      		boolean multiSectionLineGroupProcessed = false;
      		boolean zoneProcessed = false;
      		boolean interareaTransferProcessed = false;
      		boolean ownerProcessed = false;
      		boolean factsProcessed = false;
      		
      		int busCnt = 0, loadCnt = 0, fxiedShuntCnt = 0, genCnt = 0, lineCnt = 0, xfrCnt = 0, xfr3WCnt = 0, xfrZTableCnt = 0,
      		    areaInterCnt = 0, dcLineCnt = 0, vscDcLineCnt = 0, mtDcLineCnt = 0, factsCnt = 0,
      		    switchedShuntCnt = 0, ownerCnt = 0, interTransCnt = 0, zoneCnt = 0, multiSecCnt = 0;
      		
      		do {
      			lineStr = din.readLine();
      			if (lineStr != null) {
      				lineNo++;
      				if (!headerProcessed) {
  						String lineStr2 = din.readLine(); lineNo++;
  						String lineStr3 = din.readLine(); lineNo++;
						this.headerDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
  						headerProcessed = true;
      				}
      				else if (!busProcessed) {
						if (isEndRecLine(lineStr)) {
							 busProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Bus record processed");
							 this.elemCntStr += "Bus record " + busCnt +"\n";
						}	 
						else {
							busDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							busCnt++;
						}	 
      				}
      				else if (!loadProcessed) {
						if (isEndRecLine(lineStr)) {
							 loadProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Load record processed");
							 this.elemCntStr += "Load record " + loadCnt +"\n";
						}
						else {
							loadDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							loadCnt++;
						}	 
      				}
      				else if (this.adptrtVersion == PsseVersion.PSSE_32 && !fxiedShuntProcessed) {
						if (isEndRecLine(lineStr)) {
							fxiedShuntProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Fixed Shunt record processed");
							 this.elemCntStr += "Load record " + loadCnt +"\n";
						}
						else {
							fixedShuntDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							fxiedShuntCnt++;
						}	 
      				}      				
      				else if (!genProcessed) {
						if (isEndRecLine(lineStr)) {
							 genProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Gen record processed");
							 this.elemCntStr += "Gen record " + genCnt +"\n";
						}
						else {
							genDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							genCnt++;
						}	 
      				}
      				else if (!lineProcessed) {
						if (isEndRecLine(lineStr)) {
							 lineProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Line record processed");
							 this.elemCntStr += "Line record " + lineCnt +"\n";
						}
						else {
							lineDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							lineCnt++;
						}	 
      				}
      				else if (!xfrProcessed) {
						if (isEndRecLine(lineStr)) {
							 xfrProcessed = true;
							 ODMLogger.getLogger().info("PSS/E Xfr record processed");
							 this.elemCntStr += "2W Xfr record " + xfrCnt +"\n";
							 this.elemCntStr += "3W Xfr record " + xfr3WCnt +"\n";
						}
						else {
      						String lineStr2 = din.readLine(); lineNo++;
      						String lineStr3 = din.readLine(); lineNo++;
      						String lineStr4 = din.readLine(); lineNo++;
      						String lineStr5 = "";
      						if (is3WXfr(lineStr)) {
          						lineStr5 = din.readLine(); lineNo++;
    							xfr3WCnt++;
      						}
      						else
    							xfrCnt++;
							xfrDataMapper.procLineString( new String[] { lineStr, lineStr2, lineStr3, lineStr4, lineStr5 }, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
						}	 
      				}
      				else if (!areaInterProcessed) {
						if (isEndRecLine(lineStr)) {
							 areaInterProcessed = true;
							 ODMLogger.getLogger().info("PSS/E AreaInterchange record processed");
							 this.elemCntStr += "Area interchange record " + areaInterCnt +"\n";
						}
						else {
							this.areaDataMapper.procLineString(lineStr,  (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							areaInterCnt++;
						}	 
      				}
      				else if (!dcLine2TProcessed) {
						if (isEndRecLine(lineStr)) {
							 dcLine2TProcessed = true;
							 ODMLogger.getLogger().info("PSS/E DC line record processed");
							 this.elemCntStr += "2T DC line record " + dcLineCnt +"\n";
						}
						else {
      						String lineStr2 = din.readLine(); lineNo++;
      						String lineStr3 = din.readLine(); lineNo++;
							this.dcLine2TDataMapper.procLineString(new String[] {lineStr, lineStr2, lineStr3}, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							dcLineCnt++;
						}	 
      				}
      				else if (!vscDcLineProcessed) {
						if (isEndRecLine(lineStr)) {
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
      				else if (!switchedShuntProcessed) {
						if (isEndRecLine(lineStr)) {
							 switchedShuntProcessed = true;
							 ODMLogger.getLogger().info("PSS/E switched shunt record processed");
							 this.elemCntStr += "Switched Shunt record " + switchedShuntCnt +"\n";
						}
						else {
							switchedShuntDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							switchedShuntCnt++;
						}	 
      				}
      				else if (!xfrZCorrectionProcessed) {
						if (isEndRecLine(lineStr)) {
							xfrZCorrectionProcessed = true;
							ODMLogger.getLogger().info("PSS/E Xfr table record processed");
							 this.elemCntStr += "Xfr table record " + xfrZTableCnt +"\n";
						}
						else {
							zTableDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							xfrZTableCnt++;
						}	 
      				}
      				else if (!dcLineMTProcessed) {
						if (isEndRecLine(lineStr)) {
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
      				else if (!multiSectionLineGroupProcessed) {
						if (isEndRecLine(lineStr)) {
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
      				else if (!zoneProcessed) {
						if (isEndRecLine(lineStr)) {
							zoneProcessed = true;
							ODMLogger.getLogger().info("PSS/E Zone record processed");
							 this.elemCntStr += "Zone record " + zoneCnt +"\n";
						}
						else {
							this.zoneDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							zoneCnt++;
						}	 
      				}
      				else if (!interareaTransferProcessed) {
						if (isEndRecLine(lineStr)) {
							interareaTransferProcessed = true;
							ODMLogger.getLogger().info("PSS/E Interarea Transfer record processed");
							 this.elemCntStr += "Interarea transfer record " + interTransCnt +"\n";
						}
						else {
							interAreaDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							interTransCnt++;
						}	 
      				}
      				else if (!ownerProcessed) {
						if (isEndRecLine(lineStr)) {
							ownerProcessed = true;
							ODMLogger.getLogger().info("PSS/E Owner record processed");
							 this.elemCntStr += "Owner record " + ownerCnt +"\n";
						}
						else {
							ownerDataMapper.procLineString(lineStr, (BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml>) parser);
							ownerCnt++;
						}	 
      				}
      				else if (!factsProcessed) {
						if (isEndRecLine(lineStr)) {
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
      			}
    		} while (lineStr != null);
  		} catch (Exception e) {
  			e.printStackTrace();
    		throw new ODMException("PSSE data input error, line no " + lineNo + ", " + e.toString());
  		}
             
		AclfParserHelper.createBusEquivData(parser);
  		
   	   	return parser;
	}
	
	
	private boolean is3WXfr(String str) {
		// for 2W xfr, line1, K = 0
  		StringTokenizer st = new StringTokenizer(str, ",");
		st.nextToken();
		st.nextToken();
		int K = new Integer(st.nextToken().trim()).intValue();
		return K != 0;
	}
	

}
