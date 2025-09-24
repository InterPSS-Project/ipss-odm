package org.ieee.odm.adapter.psse.raw.mapper.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov.PSSETurGovIEEE1981Type3Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.GovIEEE1981Type3XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSETurGovIEEE1981Type3Mapper extends BasePSSEDataRawMapper{
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSETurGovIEEE1981Type3Mapper.class);
	
	public PSSETurGovIEEE1981Type3Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSETurGovIEEE1981Type3Parser(ver);
	}
	
	/*
	 * PSSE IEEEG3 -> IEEE 1981 Type 3  Tur-Gov model for Hydro 
	 * ----------------------------------------------------------
	 * IBUS, 'IEEEG3', ID, TG, TP, Uo, Uc, PMAX, PMIN, Sigma, Delta, TR,TW,a11,a13,a21,a23/
	 *
	 *
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
		 * 	   0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "TG",       "TP",       
			//  5----------6----------7----------8----------9
				"Uo",    "Uc",     "PMAX",    "PMIN",    "Sigma",  	 
			//  10----------11--------12---------13---------14	
				"Delta",  "TR",      "TW",      "a11",     "a13",
			//  15----------16	
				"a21",   "a23"
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("IEEEG3")){
	    	throw new ODMException(" Governor of machine  : Id"+
		             genId+" @ Bus"+i+"is not a IEEEG3 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   if(busXml!=null) {
		   
			   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
			   
			   if(dstabGenData!=null) {
				   GovIEEE1981Type3XmlType gov = DStabParserHelper.createGovIEEE1981Type3XmlType(dstabGenData);
				   
				   gov.setTG(BaseDataSetter.createTimeConstSec(dataParser.getDouble("TG")));
				   gov.setTP(BaseDataSetter.createTimeConstSec(dataParser.getDouble("TP")));
				   
				   gov.setVOpen(dataParser.getDouble("Uo"));
				   gov.setVClose(dataParser.getDouble("Uc"));
				   
				   gov.setPMAX(dataParser.getDouble("PMAX"));
				   gov.setPMIN(dataParser.getDouble("PMIN"));
				   
				   gov.setSIGMA(dataParser.getDouble("Sigma"));
				   gov.setDELTA(dataParser.getDouble("Delta"));
				   
				   gov.setTR(BaseDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
				   gov.setTW(BaseDataSetter.createTimeConstSec(dataParser.getDouble("TW")));
				   
				   gov.setA11(dataParser.getDouble("a11"));
				   gov.setA13(dataParser.getDouble("a13"));
				   gov.setA21(dataParser.getDouble("a21"));
				   gov.setA23(dataParser.getDouble("a23"));
			   }else{
				   log.error("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
			   }
	   }
	   else{
		   log.error("Bus is not found in Bus #"+busId);
	   }
	   
	}		
}
