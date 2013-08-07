package org.ieee.odm.adapter.psse.impl;

import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper.DynModelType;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynGeneratorMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

public class PSSEDynAdapter extends PSSEAcscAdapter<DStabNetXmlType, DStabBusXmlType, LineDStabXmlType, XfrDStabXmlType, PSXfrDStabXmlType>{

    DynamicModelLibHelper dynLibHelper = new DynamicModelLibHelper();
	PSSEDynGeneratorMapper generatorMapper =null; 
    
	public PSSEDynAdapter(PsseVersion ver) {
		super(ver);
		generatorMapper = new PSSEDynGeneratorMapper(ver);
		
	}
	
	/*
	 * parse the input dynamic model data
	 */
	
	public IODMModelParser parseDStabFile(final IFileReader din, String encoding) throws Exception {
		String lineStr = null;
		String modelType = "";
  		int lineNo = 0;
  		try {

      		do {
      			lineStr = din.readLine();
      			if (lineStr != null) {
      				lineNo++;
      				if(lineStr.trim().length()>0){//only process when it is not a blank line
      					if(!isModelDataCompleted(lineStr)){
      						lineStr += din.readLine();
      					}
      					modelType = getModelType(lineStr);
      					if(dynLibHelper.getModelType(modelType)==DynModelType.GENERATOR){
      						generatorMapper.procLineString(modelType, lineStr, (DStabModelParser) parser);
      					}
      					else{
      						throw new Exception("The input dynamic model is not supported yet, Type #"+modelType);
      					}
      				}
      				
      			}
      		}while(lineStr!=null);
		
  		} catch (Exception e) {
  			e.printStackTrace();
    		throw new ODMException("PSSE dynamic data input error, line no " + lineNo + ", " + e.toString());
  		}
		return parser;
	}

	@Override
	public IODMModelParser parseInputFile(NetType type, IFileReader[] din,
			String encoding) throws Exception {
		if(type !=NetType.DStabNet){
			// initialize the parser first
			throw new ODMException("input type is not Dstab NetType, please check ");
			
		}
		
			// the parser is supposed to be set at the PSSEDstabParser class, which call this method
		if(parser == null){
				parser = new DStabModelParser();
				parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.TRANSIENT_STABILITY);
		}
		
		//Use the Acsc Parser to parse the first two files, namely, Aclf and Sequence data.
		
		if(din[1]!=null){ // the second data file stores the sequence data
			DStabNetXmlType baseCaseNet = (DStabNetXmlType) parser.getNet();
			baseCaseNet.setHasShortCircuitData(true);
		}
		super.parseInputFile(type, din, encoding);
		
	    //It is supposed that the third file defines the Dstab data.
		this.parseDStabFile(din[2], encoding);
		
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
    	return(ModelStringUtil.trimQuote(strAry[1]));
    }

}
