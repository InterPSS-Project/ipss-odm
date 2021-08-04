package org.ieee.odm.adapter.psse.mapper.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.relay.PSSERelayLVS3DataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.LVS3RelayXmlType;

public class PSSERelayLVS3Mapper extends BasePSSEDataMapper{
    
	public PSSERelayLVS3Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSERelayLVS3DataParser(ver);
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String loadId = dataParser.getString("LID");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("LVS3BL")){
	    	throw new ODMException(" load at bus  : Id"+
		             loadId+" @ Bus"+i+"is not a LVS3XX type");
	    }

	    /*
	     // Header		
		 "IBUS", "Type", "LID",
		 
		 // M parameters
		 "FBus1",      "TBus1",  "Id1",    "FBus2",      "TBus2",  "Id2",  "SC", 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		    "F1",     "T1",      "Tb1",   "Frac1",  "F2",     "T2",     "Tb2",  "Frac2",    "F3",       "T3", 
		 // 10---------11----------12---------13---------14-------15-------16---------17-----------18---------19
		    "Tb3",   "Frac3",    "F4",     "T4",      "Tb4",   "Frac4",     "F5",    "T5",      "Tb5",      "Frac5",  
		//  20----------21
		    "Ttb1",      "Ttb2"
	     */
	   DStabBusXmlType busXml = parser.getBus(busId);

	   if(busXml!=null){ 
	        LVS3RelayXmlType lvs3 = DStabParserHelper.createRelayLVS3XmlType(busXml, loadId);
	        
	        if(lvs3!= null){
	        	
    			int FBus1Num   = dataParser.getInt("FBus1");
    			int TBus1Num   = dataParser.getInt("TBus1");
    			String Id1   = dataParser.getString("Id1");
    			int FBus2Num   = dataParser.getInt("FBus2");
    			int TBus2Num   = dataParser.getInt("TBus2");
    			String Id2   = dataParser.getString("Id2");
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
    			double Ttb1   = dataParser.getDouble("Ttb1");
    			double Ttb2   = dataParser.getDouble("Ttb2");  
    			
    			lvs3.setFBus1(FBus1Num);
    			lvs3.setTBus1(TBus1Num);
    			lvs3.setID1(Id1);
    			
    			lvs3.setFBus2(FBus2Num);
    			lvs3.setTBus2(TBus2Num);
    			lvs3.setID2(Id2);
    			
    			lvs3.setSC(SC);
    			
    			lvs3.setF1(F1);
    			lvs3.setT1(T1);
    			lvs3.setTb1(Tb1);
    			lvs3.setFrac1(Frac1);
    			
    			lvs3.setF2(F2);
    			lvs3.setT2(T2);
    			lvs3.setTb2(Tb2);
    			lvs3.setFrac2(Frac2);
    			
    			lvs3.setF3(F3);
    			lvs3.setT3(T3);
    			lvs3.setTb3(Tb3);
    			lvs3.setFrac3(Frac3);
    			
    			lvs3.setF4(F4);
    			lvs3.setT4(T4);
    			lvs3.setTb4(Tb4);
    			lvs3.setFrac4(Frac4);
    			
    			lvs3.setF5(F5);
    			lvs3.setT5(T5);
    			lvs3.setTb5(Tb5);
    			lvs3.setFrac5(Frac5);
    			
    			lvs3.setTtb1(Ttb1);
    			lvs3.setTtb2(Ttb2);
	        	

	        }
	   }
		
	}

}
