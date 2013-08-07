package org.ieee.odm.adapter.pwd;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.pwd.impl.BranchDataProcessor;
import org.ieee.odm.adapter.pwd.impl.BusDataProcessor;
import org.ieee.odm.adapter.pwd.impl.NetDataProcessor;
import org.ieee.odm.adapter.pwd.impl.PWDHelper;
import org.ieee.odm.adapter.pwd.impl.TransformerDataProcessor;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LimitSetXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PWDNetworkExtXmlType;

 /**
  * PowerWorld-TO-ODM Adapter based on power world v16 data definition
  * 
  * @version 0.2  01/08/2012
  * @author  
  * 
  */
public class PowerWorldAdapter extends AbstractPowerWorldAdapter{
    //Define data specifier, two options defined in PWD, CSV or Blank
	public static enum FileTypeSpecifier{CSV,Blank};
	public static FileTypeSpecifier dataSeparator=FileTypeSpecifier.Blank;//By default

	private AclfModelParser parser = null;

	private NetDataProcessor netProc = null;
	private BusDataProcessor busProc = null;
	private BranchDataProcessor branchProc = null;
	private TransformerDataProcessor xfrProc = null;

	public PowerWorldAdapter(){
		super();
		this.parser=new AclfModelParser();
		parser.initCaseContentInfo(OriginalDataFormatEnumType.POWER_WORLD);
		
		// BaseCase object, plus busRecList and BranchRecList are created 
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		
		// add PWD network extension 
		PWDNetworkExtXmlType pwdNetExt = odmObjFactory.createPWDNetworkExtXmlType();
		
		// Since the LimitSet is not necessarily included but an optional data, 
		// LimitSets is initialized when it is processed for the first time.
		
		//pwdNetExt.setLimitSets(odmObjFactory.createPWDNetworkExtXmlTypeLimitSets());
		
		baseCaseNet.setExtension(pwdNetExt);
		
		baseCaseNet.setId("Base_Case_from_PowerWorld_format");
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(100.0));//not defined in the file

		this.netProc = new NetDataProcessor(parser);
		this.busProc = new BusDataProcessor(parser);
		this.branchProc = new BranchDataProcessor(parser);
		this.xfrProc = new TransformerDataProcessor(parser);
	}
	/**
	 * Entry point of the PWD adapter. It reads in the input PWD data file and parses 
	 * it into an ODM file in memory. The records in the PWD file are processed sequentially
	 * by the corresponding data record processors, e.g. netDataProcessor, busDataProcessor.
	 */
	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding) {
		this.parser.setEncoding(encoding);
		try {
			processInputFile(din);
		}catch(Exception e){
			//e.printStackTrace();
			ODMLogger.getLogger().severe("PWD AUX file parsing error, " + e.toString());
		}
		
		postProcessing(parser);
		
		return parser;
	}

	/**
	 * Perform data checking, or post processing, after parsing the data into ODM.
	 * Here the buses whose voltage is less than 0.1, is marked as "OPEN".
	 * @param parser
	 * @return
	 */
	public boolean postProcessing(AclfModelParser parser) {
		LoadflowNetXmlType baseCaseNet = parser.getNet(); 

		for (JAXBElement<? extends BusXmlType> bus : baseCaseNet.getBusList().getBus()) {
			LoadflowBusXmlType busRec = (LoadflowBusXmlType)bus.getValue();
			// turn-off bus if bus voltage is 0.0
			if (busRec.getVoltage().getValue() < 0.1) {
				busRec.setOffLine(Boolean.TRUE);
			}
		}
		
		return true;
	}	

	@Override protected void processMetadataLine(IFileReader din, String str, RecType recordType) throws ODMException {
	    //get all the argument fields of a record, then save them to a list.
	    while(!PWDHelper.isArgumentFieldsCompleted(str)){
			str+=din.readLine();
		}
	    //parse meta data
	    switch(recordType){
	    	case BUS     :busProc.parseMetadata(str);break;
	    	case GEN     :busProc.parseMetadata(str);break;
	    	case LOAD    :busProc.parseMetadata(str);break;
	    	case SHUNT   :busProc.parseMetadata(str);break;
	    	case BRANCH  :branchProc.parseMetadata(str);break;
	    	case XFORMER :xfrProc.parseMetadata(str);break;
	    	case ZONE    :netProc.parseMetadata(str);break;
	    	case AREA    :netProc.parseMetadata(str);break;
	    	case XFCORRECTION:xfrProc.parseMetadata(str);break;
	    	case LIMITSET:netProc.parseMetadata(str);break;
	    	default: // do nothing
	    }
	}
	
	@Override protected void processDataLine(IFileReader din, String str, RecType recordType) throws ODMException {
		   if(recordType==RecType.BUS) 
			   busProc.processBusBasicData(str);
		   else if(recordType==RecType.LOAD)
			   busProc.processBusLoadData(str);
		   else if(recordType==RecType.GEN)
			   busProc.processBusGenData(str);
		   else if(recordType==RecType.SHUNT)
			   busProc.processBusShuntData(str);
		   else if(recordType==RecType.BRANCH){
			   //NE-ISO file uses multiple lines to store some data, e.g. transformer data;
			   //clear the processed data in memory, or it will cause fieldTable size wrong
			   branchProc.clearNVPairTableData();  
			   
			   while(!branchProc.parseData(str,true))
					str=din.readLine();
			 
			   branchProc.processBranchData();
		   }
		   //Here we assumed that TRANSFOMER part data is supplementary to the BRANCH part data
		   //and is only to provide the transformer control/adjustment data
		   else if(recordType==RecType.XFORMER)
			   xfrProc.processXFormerControlData(str);
		   else if(recordType==RecType.TRI_W_XFORMER){}
			   //xfrProc.process3WXFomerData(str);
		   else if(recordType==RecType.XFCORRECTION)
			   xfrProc.processXFCorrection(str);
		   else if(recordType==RecType.AREA)
			   netProc.processAreaData(str);
		   else if(recordType==RecType.ZONE)
			   netProc.processZoneData(str);
		   else if(recordType==RecType.LIMITSET)
			   netProc.processLimitSet(str);
		   else{
			  // ODMLogger.getLogger().warning("unsupported data #"+str);
		   }
	}
	
	@Override protected void processOtherTypeLine(String str) {
		// do nothing
	}
}
