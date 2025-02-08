package org.ieee.odm.adapter.psse.raw.mapper.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter.PSSEExcIEEE1981ST1Parser;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter.PSSEExcIEEE2005ST3AParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE1981ST1XmlType;
import org.ieee.odm.schema.ExcIEEE2005TypeST3AXmlType;

public class PSSEExcIEEE2005ST3AMapper extends BasePSSEDataRawMapper{
    
	public PSSEExcIEEE2005ST3AMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEExcIEEE2005ST3AParser(ver);
	}

	/*
	 * 
	 * PSSE ESST3A -> IEEE 2005 Type ST3A
	 * 
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
            //  0----------1----------2----------3----------4
				"IBUS",  "Type",    "MachId",   "TR",       "VIMAX",
			//  5----------6----------7----------8----------9
				"VIMIN",    "KM",    "TC",      "TB",      "KA",    		     	 
			//  10----------11----------12--------13--------14
				"TA",	 "VRMAX",   "VRMIN",     "KG",     "KP",
			//  15----------16----------17--------18--------19	
				"KI",    "VBMAX",    "KC",     	 "XL",    "VGMAX",
			//  20----------21----------22--------23--------24
				"THETAP",   "TM",    "VMMAX",   "VMMIN"
		
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("ESST3A")){
	    	throw new ODMException(" Exciter of machine  : Id"+
		             genId+" @ Bus"+i+"is not a ESST3A type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){
	       DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
			   ExcIEEE2005TypeST3AXmlType exc = DStabParserHelper.createExcIEEE2005TypeST3AXmlType(dstabGenData);
			   
			   exc.setDesc(dataParser.getValue("Type"));
			   exc.setTR(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
			   
			   exc.setVIMAX(dataParser.getDouble("VIMAX"));
			   exc.setVIMIN(dataParser.getDouble("VIMIN"));
			   
			   exc.setKM(dataParser.getDouble("KM"));
			   
			   exc.setTB(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TB")));
			   exc.setTC(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TC")));
			   
			   exc.setKa(dataParser.getDouble("KA"));
			   exc.setTa(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TA")));
			   exc.setVrmax(dataParser.getDouble("VRMAX"));
			   exc.setVrmin(dataParser.getDouble("VRMIN"));
			   
			   exc.setKG(dataParser.getDouble("KG"));
			   exc.setKP(dataParser.getDouble("KP"));
			   exc.setKI(dataParser.getDouble("KI"));
			   
			   exc.setVBMAX(dataParser.getDouble("VBMAX"));
			   
			   exc.setKC(dataParser.getDouble("KC"));
			   exc.setXL(dataParser.getDouble("XL"));
			   
			   exc.setVGMAX(dataParser.getDouble("VGMAX"));
			   
			   exc.setTM(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TM")));
			   
			   exc.setTHETAP(dataParser.getDouble("THETAP"));
			   
			   exc.setVMMAX(dataParser.getDouble("VMMAX"));
			   exc.setVMMIN(dataParser.getDouble("VMMIN"));
			
			   
		   }else{
			   ODMLogger.getLogger().severe("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
		   }
		}
		else{
		   ODMLogger.getLogger().severe("Bus is not found in Bus #"+busId);
		}
	}

}
