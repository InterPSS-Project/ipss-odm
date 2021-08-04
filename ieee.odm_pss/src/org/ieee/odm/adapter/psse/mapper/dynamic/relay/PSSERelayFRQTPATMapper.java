package org.ieee.odm.adapter.psse.mapper.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.relay.PSSERelayFRQTPATDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.GenRelayFRQTPATXmlType;
import org.ieee.odm.schema.LVS3RelayXmlType;

public class PSSERelayFRQTPATMapper extends BasePSSEDataMapper{
    
	public PSSERelayFRQTPATMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSERelayFRQTPATDataParser(ver);
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("GenBus");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("GenId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("FRQTPAT")){
	    	throw new ODMException(" Gen under/over frequency relay at bus  : Id"+
		             genId+" @ Bus"+i+"is not a FRQTPAT type");
	    }

	    /*
 		// Header		
		 "MINS", "Type", 
		 
		 // parameters
		
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		 "FreqBus",    "GenBus",  "GenId",    "FL",     "FU",  "TP",   "TB", 
	     */
	   DStabBusXmlType busXml = parser.getBus(busId);

	   if(busXml!=null){ 
		   GenRelayFRQTPATXmlType ufls = DStabParserHelper.createRelayFRQTPATXmlType(busXml, genId);
	        
	        if(ufls!= null){
	        	
    			int FreqMonitorBusNum   = dataParser.getInt("FreqBus");
    			int GenBusNum   = dataParser.getInt("GenBus");
    			String GenId   = dataParser.getString("GenId");
    			
    			double FL      = dataParser.getDouble("FL");   
    			double FU      = dataParser.getDouble("FU");   
    			double TP     = dataParser.getDouble("TP");   
    			double TB   = dataParser.getDouble("TB");   
    			
    			
    			ufls.setMonitorBusId(IODMModelParser.BusIdPreFix+FreqMonitorBusNum);
    			ufls.setGenBusNumber(GenBusNum);
    			ufls.setGenID(GenId);
    			
    			
    			ufls.setFL(FL);
    			ufls.setFU(FU);
    			ufls.setTp(TP);
    			ufls.setTb(TB);
    			
	        	

	        }
	   }
		
	}

}
