package org.ieee.odm.adapter.ge;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.ge.mapper.dynamic.PSLFDynGeneratorMapper;
import org.ieee.odm.adapter.ge.mapper.dynamic.PSLFDynLoadMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynExciterMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynGeneratorMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.PSSEDynTurGovMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.DynamicModelLibHelper.DynModelType;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;

public class PSLFDynAdapter extends AbstractODMAdapter{
	    
	    protected BaseAclfModelParser<? extends LoadflowNetXmlType> parser =null;
	    
	    DynamicModelLibHelper dynLibHelper = new DynamicModelLibHelper();
		PSLFDynGeneratorMapper generatorMapper =null; 
	//	PSSEDynExciterMapper   exciterMapper =null;
	//	PSSEDynTurGovMapper    turGovMapper = null;
		
		PSLFDynLoadMapper loadMapper = null;
		
		boolean saveSupportedData = false;
		
		public PSLFDynAdapter(){
			
			generatorMapper = new PSLFDynGeneratorMapper();
			loadMapper = new PSLFDynLoadMapper();
		}
		
		public PSLFDynAdapter(DStabModelParser dynParser){
			this();
			parser = dynParser;
			
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
	      						//remove the "/" at the end of data definition
	      						//if(lineStr.lastIndexOf("/") ==lineStr.length()-1)
		      				    lineStr =lineStr.substring(0, lineStr.lastIndexOf("/"));
		      					
	      						lineStr += " "+din.readLine().trim(); // add the blank to avoid data of two lines connecting together
	      						lineNo++;
	      					}
	      		
	      					
	      					modelType = getModelType(lineStr);
	      					if(modelType !=null){
		      					DynModelType type = dynLibHelper.getModelType(modelType);
		      					
		      					if(type!=null){
			      					if(type==DynModelType.GENERATOR){
			      						generatorMapper.procLineString(modelType, lineStr, (DStabModelParser) parser);
			      					}
			      					
	//		      					else if(dynLibHelper.getModelType(modelType)==DynModelType.EXCITER){
	//		      						exciterMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	//		      					}
	//		      					else if(dynLibHelper.getModelType(modelType)==DynModelType.TUR_GOV){
	//		      						turGovMapper.procLineString(modelType, lineStr, (DStabModelParser)parser);
	//		      					}
			      					
			      					else if(type==DynModelType.LOAD){
			      						loadMapper.procLineString(modelType, lineStr, (DStabModelParser) parser);
			      					}
			      					
			      					//save supported model data
			      					if(saveSupportedData)
			      					    dynLibHelper.saveSupportedModelData(lineStr);
		      					}
		      					else{
		      						//System.out.println("model unsupported :"+lineStr);
		      						//throw new Exception("The input dynamic model is not supported yet, Type #"+modelType);
		      						dynLibHelper.procUnsupportedModel(modelType, lineStr);
		      					}
	      					}
	      				}
	      				
	      			}
	      		}while(lineStr!=null);
			
	  		} catch (Exception e) {
	    		throw new ODMException("PSSE dynamic data input error, line # " + lineNo + ", " + e.toString());
	  		}

	  		System.out.println(dynLibHelper.getUnsupportdSVCRecs());
			return parser;
		}
		
		 private boolean isModelDataCompleted(String lineStr){
			    // if the end of the line string is "/", then the data is not completed
				boolean hasContiuneFlag = lineStr.lastIndexOf("/")>0 && (lineStr.lastIndexOf("/") ==lineStr.length()-1);
				return !hasContiuneFlag;
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
	    	    return strAry[0];
			else
				System.out.println("The input data is not correct model data : "+lineStr);

	    	return null;
	    		
	    }
	    
	    /**
	     * return true if the line is not a valid line, such as a comment line
	     * @param lineStr
	     * @return
	     */
	    private boolean skipInvalidLine(String lineStr){
	    	if(lineStr.trim().length()==0.0){
	    		return true;
	    	}
			
	    	
			if(lineStr.trim().startsWith("#") || lineStr.trim().startsWith("/"))
				return true;
			
			 // only got here if we didn't return false
		    return false;
	    }

		@Override
		protected IODMModelParser parseInputFile(IFileReader din,
				String encoding) throws ODMException {

			return parseDStabFile(din, encoding);
		}

		@Override
		protected IODMModelParser parseInputFile(NetType type,
				IFileReader[] dins, String encoding) throws ODMException {
			
			throw new UnsupportedOperationException();
		}
		
		@Override public void setModelParser(IODMModelParser parser){
			this.odmParser = parser;
			this.parser = (BaseAclfModelParser<? extends LoadflowNetXmlType>) parser;
		}
}
