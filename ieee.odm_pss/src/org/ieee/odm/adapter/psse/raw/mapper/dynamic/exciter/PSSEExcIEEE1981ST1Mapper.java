package org.ieee.odm.adapter.psse.raw.mapper.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter.PSSEExcIEEE1981ST1Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE1981ST1XmlType;

public class PSSEExcIEEE1981ST1Mapper extends BasePSSEDataRawMapper{
    
	public PSSEExcIEEE1981ST1Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEExcIEEE1981ST1Parser(ver);
	}

	/*
	 * 
	 * PSSE EXST1 -> IEEE 1981 Type EXST1
	 * 
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
			//  0----------1----------2----------3----------4
				"IBUS",  "Type",   "MachId",   "TR",       "VIMAX",
			//  5----------6----------7----------8----------9
				"VIMIN",     "TC",    "TB",      "KA",    "TA",     	 
			//  10----------11----------12--------13--------14	
				"VRMAX",   "VRMIN",    "KC",      "KF",    "TF"					
		
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("EXST1")){
	    	throw new ODMException(" Exciter of machine  : Id"+
		             genId+" @ Bus"+i+"is not a EXST1 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){
	       DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
			   ExcIEEE1981ST1XmlType exc = DStabParserHelper.createExcIEEE1981ST1XmlType(dstabGenData);
			   
			   exc.setDesc(dataParser.getString("Type"));
			   exc.setTR(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
			   
			   exc.setVIMAX(dataParser.getDouble("VIMAX"));
			   exc.setVIMIN(dataParser.getDouble("VIMIN"));
			   
			   exc.setTB(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TB")));
			   exc.setTC(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TC")));
			   
			   exc.setKa(dataParser.getDouble("KA"));
			   exc.setTa(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TA")));
			   
			   exc.setVrmax(dataParser.getDouble("VRMAX"));
			   exc.setVrmin(dataParser.getDouble("VRMIN"));
			   
			   exc.setKC(dataParser.getDouble("KC"));
			   
			   exc.setKF(dataParser.getDouble("KF"));
			   exc.setTF(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TF")));
			   

		   }else{
			   ODMLogger.getLogger().severe("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
		   }
		}
		else{
		   ODMLogger.getLogger().severe("Bus is not found in Bus #"+busId);
		}
	}

}
