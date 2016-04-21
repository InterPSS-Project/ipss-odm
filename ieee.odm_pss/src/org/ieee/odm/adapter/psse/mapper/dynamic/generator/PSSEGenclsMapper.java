package org.ieee.odm.adapter.psse.mapper.dynamic.generator;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.generator.PSSEGenclsDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;

public class PSSEGenclsMapper extends BasePSSEDataMapper{
    
	public PSSEGenclsMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEGenclsDataParser(ver);
	}
	
	public PSSEGenclsMapper(AbstractDataFieldParser parser){
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
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("GENCLS")){
	    	throw new ODMException("machine  : Id"+
		             genId+" @ Bus"+i+"is not a classical generator model (GENCLS)");
	    }
	    
	    
	    
	    DStabBusXmlType busXml = parser.getBus(busId);
	
	  if(busXml!=null){ 
	   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
	   if(dstabGenData!=null){
	   ClassicMachineXmlType mach = DStabParserHelper.createClassicMachine(dstabGenData);
	   
	 //set the type info
	   mach.setDesc(dataParser.getString("Type"));
	   
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
		   ODMLogger.getLogger().severe("Machine # "+genId +" is not found in Bus #"+busId);
	   }
	   
	 }
	  else{
		   ODMLogger.getLogger().severe("Bus # "+busId +" is not available in load flow data");
	   }
	}
}
