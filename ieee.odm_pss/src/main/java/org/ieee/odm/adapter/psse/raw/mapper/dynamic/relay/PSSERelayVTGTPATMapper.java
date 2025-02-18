package org.ieee.odm.adapter.psse.raw.mapper.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.relay.PSSERelayFRQTPATDataParser;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.relay.PSSERelayVTGTPATDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.GenRelayFRQTPATXmlType;
import org.ieee.odm.schema.GenRelayVTGTPATXmlType;
import org.ieee.odm.schema.LVS3RelayXmlType;

public class PSSERelayVTGTPATMapper extends BasePSSEDataRawMapper{
    
	public PSSERelayVTGTPATMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSERelayVTGTPATDataParser(ver);
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("GenBus");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("GenId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("VTGTPAT")){
	    	throw new ODMException(" Gen under/over voltage relay at bus  : Id"+
		             genId+" @ Bus"+i+"is not a VTGTPAT type");
	    }

	    /*
        // Header		
		 "MINS", "Type", 
		 
		 // parameters
		
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		 "VoltBus",    "GenBus",  "GenId",    "VL",     "VU",  "TP",   "TB", 
	     */
	   DStabBusXmlType busXml = parser.getBus(busId);

	   if(busXml!=null){ 
		   GenRelayVTGTPATXmlType uvls = DStabParserHelper.createRelayVTGTPATXmlType(busXml, genId);
	        
	        if(uvls!= null){
	        	
    			int VoltMonitorBusNum   = dataParser.getInt("VoltBus");
    			int GenBusNum   = dataParser.getInt("GenBus");
    			String GenId   = dataParser.getValue("GenId");
    			
    			double VL      = dataParser.getDouble("VL");   
    			double VU      = dataParser.getDouble("VU");   
    			double TP     = dataParser.getDouble("TP");   
    			double TB   = dataParser.getDouble("TB");   
    			
    			
    			uvls.setMonitorBusId(IODMModelParser.BusIdPreFix+VoltMonitorBusNum);
    			uvls.setGenBusNumber(GenBusNum);
    			uvls.setGenID(GenId);
    			
    			
    			uvls.setVL(VL);
    			uvls.setVU(VU);
    			uvls.setTp(TP);
    			uvls.setTb(TB);
    			
	        	

	        }
	   }
		
	}

}
