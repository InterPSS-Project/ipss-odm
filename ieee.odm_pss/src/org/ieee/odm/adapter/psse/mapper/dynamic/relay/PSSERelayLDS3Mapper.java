package org.ieee.odm.adapter.psse.mapper.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.relay.PSSERelayLDS3DataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.LDS3RelayXmlType;

public class PSSERelayLDS3Mapper extends BasePSSEDataMapper{
    
	public PSSERelayLDS3Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSERelayLDS3DataParser(ver);
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String loadId = dataParser.getString("LID");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("LDS3BL")){
	    	throw new ODMException(" load at bus  : Id"+
		             loadId+" @ Bus"+i+"is not a LDS3XX type");
	    }

	    /*
	     * // Header		
		 "IBUS", "Type", "LID",
		 
		 // M parameters
		 "GBus",      "GID",     "SC", 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		    "F1",     "T1",      "Tb1",   "Frac1",  "F2",     "T2",     "Tb2",  "Frac2",    "F3",       "T3", 
		 // 10---------11----------12---------13---------14-------15-------16---------17-----------18---------19
		    "Tb3",   "Frac3",    "F4",     "T4",      "Tb4",   "Frac4",     "F5",    "T5",      "Tb5",      "Frac5",  
		//  20----------
		    "Ttb"
	     */
	   DStabBusXmlType busXml = parser.getBus(busId);

	   if(busXml!=null){ 
	        LDS3RelayXmlType lds3 = DStabParserHelper.createRelayLDS3XmlType(busXml, loadId);
	        
	        if(lds3!= null){
	        	
    			int GBusNum   = dataParser.getInt("GBus");
    			String GID   = dataParser.getString("GID");
    			int SC   = dataParser.getInt("SC");
    			
    			double F1      = dataParser.getDouble("F1");   
    			double T1      = dataParser.getDouble("T1");   
    			double Tb1     = dataParser.getDouble("Tb1");   
    			double Frac1   = dataParser.getDouble("Frac1");   
    			double F2      = dataParser.getDouble("F2");   
    			double T2      = dataParser.getDouble("T2");   
    			double Tb2     = dataParser.getDouble("Tb2");   
    			double Frac2   = dataParser.getDouble("Frac2"); 
       			double F3      = dataParser.getDouble("F3");   
    			double T3      = dataParser.getDouble("T3");   
    			double Tb3     = dataParser.getDouble("Tb3");   
    			double Frac3   = dataParser.getDouble("Frac3");    			
       			double F4      = dataParser.getDouble("F4");   
    			double T4      = dataParser.getDouble("T4");   
    			double Tb4     = dataParser.getDouble("Tb4");   
    			double Frac4   = dataParser.getDouble("Frac4");  
       			double F5      = dataParser.getDouble("F5");   
    			double T5      = dataParser.getDouble("T5");   
    			double Tb5     = dataParser.getDouble("Tb5");   
    			double Frac5   = dataParser.getDouble("Frac5");
    			double Ttb   = dataParser.getDouble("Ttb");  
    			
    			lds3.setGBus(GBusNum);
    			lds3.setGID(GID);
    			lds3.setSC(SC);
    			
    			lds3.setF1(F1);
    			lds3.setT1(T1);
    			lds3.setTb1(Tb1);
    			lds3.setFrac1(Frac1);
    			
    			lds3.setF2(F2);
    			lds3.setT2(T2);
    			lds3.setTb2(Tb2);
    			lds3.setFrac2(Frac2);
    			
    			lds3.setF3(F3);
    			lds3.setT3(T3);
    			lds3.setTb3(Tb3);
    			lds3.setFrac3(Frac3);
    			
    			lds3.setF4(F4);
    			lds3.setT4(T4);
    			lds3.setTb4(Tb4);
    			lds3.setFrac4(Frac4);
    			
    			lds3.setF5(F5);
    			lds3.setT5(T5);
    			lds3.setTb5(Tb5);
    			lds3.setFrac5(Frac5);
    			
    			lds3.setTtb(Ttb);
	        	

	        }
	   }
		
	}

}
