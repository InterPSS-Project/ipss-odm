package org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.tur_gov.PSSETurGovIEESGOParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovPSSEIEESGOModelXmlType;

public class PSSETurGovIEESGOMapper extends BasePSSEDataMapper{
    
	public PSSETurGovIEESGOMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSETurGovIEESGOParser(ver);
	}
	
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
	/*
	  //  0----------1----------2----------3----------4
		 "IBUS", "Type",   "MachId",   "T1",       "T2",
	  //  5----------6----------7----------8----------9
	     "T3",     "T4",      "T5",      "T6",     "K1",  	 
	  //  10----------11--------12---------13---------14	
		 "K2",      "K2",       "K3",   "PMAX",   "PMIN"	
	 */			
		
		/*
		 * PSSE IEESGO, similar to IEEE 1973 Tandem Compound, Single Reheat.
		 * 
		 * 
		*/
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("IEESGO")){
	    	throw new ODMException(" Governor of machine  : Id"+
		             genId+" @ Bus"+i+"is not a IEESGO type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
	   
	   GovPSSEIEESGOModelXmlType gov = DStabParserHelper.createGovPSSEIEESGOXmlType(dstabGenData);
	   gov.setT1(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T1")));
	   gov.setT2(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T2")));
	   gov.setT3(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T3")));
	   
	   gov.setT4(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T4")));
	   
	   gov.setT4(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T5")));
	   
	   gov.setT6(DStabDataSetter.createTimeConstSec(dataParser.getDouble("T6")));
	   
	   gov.setK1(dataParser.getDouble("K1"));
	   gov.setK2(dataParser.getDouble("K2"));
	   gov.setK3(dataParser.getDouble("K3"));
	   
	   gov.setPMAX(dataParser.getDouble("PMAX"));
	   gov.setPMIN(dataParser.getDouble("PMIN"));
	 
	
	}
}
