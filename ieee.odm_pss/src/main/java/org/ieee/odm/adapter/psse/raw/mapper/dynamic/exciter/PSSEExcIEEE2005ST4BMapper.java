package org.ieee.odm.adapter.psse.raw.mapper.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter.PSSEExcIEEE2005ST4BParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE2005TypeST4BXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSEExcIEEE2005ST4BMapper extends BasePSSEDataRawMapper{
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSEExcIEEE2005ST4BMapper.class.getName());
	
	public PSSEExcIEEE2005ST4BMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEExcIEEE2005ST4BParser(ver);
	}

	/*
	 * 
	 * PSSE ESST4B -> IEEE 2005 Type ST4B
	 * 
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
	         //  0----------1----------2----------3----------4
				"IBUS",  "Type",    "MachId",   "TR",       "KPR",
			//  5----------6----------7----------8----------9
				"KIR",   "VRMAX", "VRMIN",   "TA",      "KPM",       		     	 
			//  10----------11-------12--------13---------14
				"KIM",   "VMMAX", "VMMIN",    "KG",      "KP",     
			//  15----------16-------17--------18---------19	
				"KI",    "VBMAX",  "KC",     "XL",    "THETAP"
		
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("ESST4B")){
	    	throw new ODMException(" Exciter of machine  : Id"+
		             genId+" @ Bus"+i+"is not a ESST4B type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){
	       DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
			   ExcIEEE2005TypeST4BXmlType exc = DStabParserHelper.createExcIEEE2005TypeST4BXmlType(dstabGenData);
			   
			   exc.setDesc(dataParser.getValue("Type"));
			   exc.setTR(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
			   
			   exc.setKPR(dataParser.getDouble("KPR"));
			   exc.setKIR(dataParser.getDouble("KIR"));
			   
		
			   exc.setTa(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TA")));
			   exc.setVrmax(dataParser.getDouble("VRMAX"));
			   exc.setVrmin(dataParser.getDouble("VRMIN"));
			   
			   exc.setKPM(dataParser.getDouble("KPM"));
			   exc.setKIM(dataParser.getDouble("KIM"));
			   
			   exc.setVMMAX(dataParser.getDouble("VMMAX"));
			   exc.setVMMIN(dataParser.getDouble("VMMIN"));
			   
			   exc.setKG(dataParser.getDouble("KG"));
			   exc.setKP(dataParser.getDouble("KP"));
			   exc.setKI(dataParser.getDouble("KI"));
			   
			   exc.setVBMAX(dataParser.getDouble("VBMAX"));
			   
			   exc.setKC(dataParser.getDouble("KC"));
			   exc.setXL(dataParser.getDouble("XL"));
			  
			   exc.setTHETAP(dataParser.getDouble("THETAP"));
			   
		   }else{
			   log.error("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
		   }
		}
		else{
		  log.error("Bus is not found in Bus #"+busId);
		}
	}

}
