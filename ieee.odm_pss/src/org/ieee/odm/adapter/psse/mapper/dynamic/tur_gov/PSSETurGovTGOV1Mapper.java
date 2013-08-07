package org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.tur_gov.PSSETurGovIEESGOParser;
import org.ieee.odm.adapter.psse.parser.dynamic.tur_gov.PSSETurGovTGOV1Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovPSSEIEESGOModelXmlType;
import org.ieee.odm.schema.GovPSSETGOV1ModelXmlType;

public class PSSETurGovTGOV1Mapper extends BasePSSEDataMapper{
    
	public PSSETurGovTGOV1Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSETurGovTGOV1Parser(ver);
	}
	
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
	/*
	 * PSSE TGOV1
	 * TGOV1 is a simple model representing governor action and the reheater time 
	 * constant effect for a steam turbine. The ratio, T2/T3, equals the fraction 
	 * of turbine power that is developed by the highpressure turbine. T3is the 
	 * reheater time constant, and T1 is the governor time constant
	 * 
	        //  0----------1----------2----------3----------4
				"IBUS",  "Type",   "MachId",     "R",      "T1",
			//  5----------6----------7----------8----------9
				"VMAX",  "VMIN",     "T2",     "T3",      "Dt"  
	 */			
		
	
		int i = dataParser.getInt("IBUS");
	    final String busId = AbstractModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("TGOV1")){
	    	throw new ODMException(" Governor of machine  : Id"+
		             genId+" @ Bus"+i+"is not a TGOV1 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
	   
	   GovPSSETGOV1ModelXmlType gov = DStabParserHelper.createGovPSSETGOV1XmlType(dstabGenData);
	   
	   gov.setR(dataParser.getDouble("R"));
       gov.setT1(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T1")));
	   
	   gov.setVMAX(dataParser.getDouble("VMAX"));
	   gov.setVMIN(dataParser.getDouble("VMIN"));
	   
	   gov.setT2(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T2")));
	   gov.setT3(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T3")));
	   
	   gov.setDt(dataParser.getDouble("Dt"));
	}
}
