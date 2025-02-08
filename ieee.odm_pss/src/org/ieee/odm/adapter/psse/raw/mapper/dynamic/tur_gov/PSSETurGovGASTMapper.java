package org.ieee.odm.adapter.psse.raw.mapper.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov.PSSETurGovGASTParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovIEEE1981Type1XmlType;
import org.ieee.odm.schema.GovPSSEGASTModelXmlType;

public class PSSETurGovGASTMapper extends BasePSSEDataRawMapper{
    
	public PSSETurGovGASTMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSETurGovGASTParser(ver);
	}
	
	
	/*
	 *    0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",     "R",     "T1",
		   5----------6----------7----------8----------9
				"T2",     "T3",      "AT",      "KT",    "VMAX",   	 
		  10----------11--------12---------13---------14	
				"VMIN",     "Dturb", 
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("GAST")){
	    	throw new ODMException(" Governor of machine  : Id"+
		             genId+" @ Bus"+i+"is not a GAST type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   if(busXml !=null) {
			   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
			   
			   if(dstabGenData!=null) {
					   GovPSSEGASTModelXmlType gov = DStabParserHelper.createGovPSSEGASTXmlType(dstabGenData);
					   
					   gov.setR(dataParser.getDouble("R"));
					   gov.setT1(BaseDataSetter.createTimeConstSec(dataParser.getDouble("T1")));
					   gov.setT2(BaseDataSetter.createTimeConstSec(dataParser.getDouble("T2")));
					   gov.setT3(BaseDataSetter.createTimeConstSec(dataParser.getDouble("T3")));
					   gov.setAT(dataParser.getDouble("AT"));
					   gov.setKT(dataParser.getDouble("KT"));
					   gov.setVMAX(dataParser.getDouble("VMAX"));
					   gov.setVMIN(dataParser.getDouble("VMIN"));
					   gov.setDturb(dataParser.getDouble("Dturb"));
			   
		     }else{
			   ODMLogger.getLogger().severe("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
		   }
		}
		else{
		ODMLogger.getLogger().severe("Bus is not found in Bus #"+busId);
		}
		
	}
	

}
