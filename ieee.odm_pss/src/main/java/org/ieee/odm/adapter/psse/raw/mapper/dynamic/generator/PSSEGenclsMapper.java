package org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator;

import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.generator.PSSEGenclsDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSEGenclsMapper extends BasePSSEDataRawMapper{
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSEGenclsMapper.class);
	
	public PSSEGenclsMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEGenclsDataParser(ver);
	}
	
	public PSSEGenclsMapper(AbstractStringDataFieldParser parser){
		this.dataParser = parser;
	}
	
	
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
		 * GENCLS is the classical constant voltage behind transient reactance generator model
		 *   // 0----------1----------2----------3----------4
				 "IBUS",    "Type",   "MachId",   "H",   "D",
		 */
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("GENCLS")){
	    	throw new ODMException("machine  : Id"+
		             genId+" @ Bus"+i+"is not a classical generator model (GENCLS)");
	    }
	    
	    
	    
	    DStabBusXmlType busXml = parser.getBus(busId);
	
	  if(busXml!=null){ 
		   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
		   ClassicMachineXmlType mach = DStabParserHelper.createClassicMachine(dstabGenData);
		   
		 //set the type info
		   mach.setDesc(dataParser.getValue("Type"));
		   
		   mach.setH(dataParser.getDouble("H"));
		   mach.setD(dataParser.getDouble("D"));
		   //x'd = XSOURCE
		   if(dstabGenData.getSourceZ()!=null){
			   if(dstabGenData.getSourceZ().getIm()>0){
				   mach.setRa(dstabGenData.getSourceZ().getRe());
				   mach.setXd1(dstabGenData.getSourceZ().getIm());
			   }else
				   throw new ODMException("Source Z of of classic type(GENCLS) machine  : Id"+
			             genId+" @ Bus"+i+"is not defined");
		   }
	   
	   }
	   else{
		   log.error("Machine # "+genId +" is not found in Bus #"+busId);
	   }
	   
	 }
	  else{
		   log.error("Bus # "+busId +" is not available in load flow data");
	   }
	}
}
