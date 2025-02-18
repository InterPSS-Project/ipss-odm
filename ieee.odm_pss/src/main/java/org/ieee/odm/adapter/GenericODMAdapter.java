package org.ieee.odm.adapter;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.adapter.ge.PSLFDynAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.impl.PSSEDynRawAdapter;
import org.ieee.odm.adapter.psse.raw.impl.PSSELFRawAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
/**
 * A generic adapter that supports the formats for the power flow, sequence and dynamic data can be different.
 * For example, the power flow is of PSS/E format, and the dynamic data is of PSLF format.
 * @author Qiuhua Huang
 *
 */
public class GenericODMAdapter extends AbstractODMAdapter{
	
	protected BaseAclfModelParser<? extends LoadflowNetXmlType> parser =null;
	
	// power flow data format
	protected ODMFileFormatEnum pfDataFormat = null;
	
	// dynamic data format
	protected ODMFileFormatEnum dynDataFormat = null;
	
	// pfAdapter
	AbstractODMAdapter pfAdapter = null;
	
	// Dynamic Adapter
	AbstractODMAdapter dynAdapter = null;
	
	public GenericODMAdapter(ODMFileFormatEnum powerflowDataFormat,ODMFileFormatEnum dynamicDataFormat){
		 pfDataFormat  = powerflowDataFormat;
		 dynDataFormat = dynamicDataFormat;
		 
		 if(dynDataFormat != null){
			  parser = new DStabModelParser();
			  parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.TRANSIENT_STABILITY);
		 }
		 else
			 parser = new AclfModelParser();
		
		 if(pfDataFormat != null){
			switch(pfDataFormat){
				
				case PsseV30:
			       pfAdapter = new  PSSELFRawAdapter(PsseVersion.PSSE_30);
			       
			       break;
			default:
				break;
			}
			
			if(pfAdapter !=null)
				  pfAdapter.setModelParser(parser);
			
		 }
		 
		 
		if(dynDataFormat != null){
			switch(dynDataFormat){
			
			case PsseV30:
				dynAdapter = new PSSEDynRawAdapter(PsseVersion.PSSE_30);
				break;
			case GePSLF:
				dynAdapter = new PSLFDynAdapter();
				break;
			
			}
			if(dynAdapter !=null)
			    dynAdapter.setModelParser(parser);
		}
	}

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding)
			throws ODMException {
		
		return null;
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] dins,
			String encoding) throws ODMException {
		
		if(type==NetType.DStabNet && dins.length>=2){
			
			DStabNetXmlType baseCaseNet = (DStabNetXmlType) parser.getNet();
			
			if(dins.length==2){
				
				baseCaseNet.setHasLoadflowData(true);
				baseCaseNet.setPositiveSeqDataOnly(true);
				
				pfAdapter.parseInputFile(dins[0], encoding);
				
				//TODO not applicable to PSS/E dyn adapter
				if(dynDataFormat == ODMFileFormatEnum.GePSLF){
				     dynAdapter.parseInputFile(dins[1], encoding);
				     
				}
				else 
					throw new UnsupportedOperationException("The input dyn data format is not supported yet!");
			}
			else{
				
			}
		}
			
		return this.parser;
	}
	
	
	

}
