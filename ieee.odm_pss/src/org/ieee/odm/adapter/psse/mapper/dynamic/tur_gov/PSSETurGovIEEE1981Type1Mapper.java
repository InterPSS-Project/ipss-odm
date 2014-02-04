package org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.tur_gov.PSSETurGovIEEE1981Type1Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovIEEE1981Type1XmlType;

public class PSSETurGovIEEE1981Type1Mapper extends BasePSSEDataMapper{
    
	public PSSETurGovIEEE1981Type1Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSETurGovIEEE1981Type1Parser(ver);
	}
	
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
	/*
	        //  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "JBUS",       "M",
			//  5----------6----------7----------8----------9
				"K",      "T1",      "T2",      "T3",      "Uo",   	 
			//  10----------11--------12---------13---------14	
				"Uc",     "PMAX",    "PMIN",     "T4",      "K1",
			//  15----------16--------17---------18---------19		
				"K2",      "T5",      "K3",      "K4",       "T6",
			//  20----------21--------22---------23---------24	
				"K5",      "K6",      "T7",      "K7",       "K8",
	 */			
		
		/*
		 * PSSE IEEEG1, corresponding to IEEE 1981 type 1 model
		 * 
		 * 
		*/
		int i = dataParser.getInt("IBUS");
		int j = dataParser.getInt("JBUS");
		if(j!=0) 
		   ODMLogger.getLogger().severe("IEEE G1 model currently does NOT support more than one buses, IEEG1 @ Bus" +i );
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("IEEEG1")){
	    	throw new ODMException(" Governor of machine  : Id"+
		             genId+" @ Bus"+i+"is not a IEEEG1 type");
	    }
     
	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
	   
	   GovIEEE1981Type1XmlType gov = DStabParserHelper.createGovIEEE1981Type1XmlType(dstabGenData);
	   
	   gov.setK(dataParser.getDouble("K"));
	   gov.setT1(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T1")));
	   gov.setT2(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T2")));
	   gov.setT3(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T3")));
	   gov.setT4(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T4")));
	   gov.setT5(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T5")));
	   gov.setT6(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T6")));
	   gov.setT7(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T7")));
	   
	   gov.setVOpen(dataParser.getDouble("Uo"));
	   gov.setVClose(dataParser.getDouble("Uc"));
	   
	   gov.setPMAX(dataParser.getDouble("PMAX"));
	   gov.setPMIN(dataParser.getDouble("PMIN"));
	   
	   gov.setK1(dataParser.getDouble("K1"));
	   gov.setK2(dataParser.getDouble("K2"));
	   gov.setK3(dataParser.getDouble("K3"));
	   gov.setK4(dataParser.getDouble("K4"));
	   gov.setK5(dataParser.getDouble("K5"));
	   gov.setK6(dataParser.getDouble("K6"));
	   gov.setK7(dataParser.getDouble("K7"));
	   gov.setK8(dataParser.getDouble("K8"));
	   
	   
	   
	}
}
