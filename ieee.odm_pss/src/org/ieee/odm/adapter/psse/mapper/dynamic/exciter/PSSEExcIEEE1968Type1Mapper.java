package org.ieee.odm.adapter.psse.mapper.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.exciter.PSSEExcIEEE1968Type1Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE1968Type1XmlType;

public class PSSEExcIEEE1968Type1Mapper extends BasePSSEDataMapper{
    
	public PSSEExcIEEE1968Type1Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEExcIEEE1968Type1Parser(ver);
	}

	/*
	 * 
	 * PSSE IEEET1 -> IEEE 1968 Type 1
	 * IBUS, 'IEEET1', TR, KA, TA, VRMAX, VRMIN, KE, TE, KF, TF, 0., E1, SE(E1), E2, SE(E2)/
	 
		Note: Switch is always zero;
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
		 * 	//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "TR",       "KA",
			//  5----------6----------7----------8----------9
				"TA",    "VRMAX",   "VRMIN",   "KE",      "TE",  	 
			//  10----------11----------12--------13--------14	
				"KF",    "TF",      "Switch",   "E1",   "SE(E1)",		   
			//  15----------16----
				"E2",    "SE(E2)"			
		
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("IEEET1")){
	    	throw new ODMException(" Exciter of machine  : Id"+
		             genId+" @ Bus"+i+"is not a IEEET1 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml !=null){ 
		   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
		   ExcIEEE1968Type1XmlType exc = DStabParserHelper.createExcIEEE1968Type1XmlType(dstabGenData);
		   
		   exc.setDesc(dataParser.getString("Type"));
		   exc.setTR(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
		   exc.setKA(dataParser.getDouble("KA"));
		   
		   exc.setTA(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TA")));
		   exc.setVRMAX(dataParser.getDouble("VRMAX"));
		   exc.setVRMIN(dataParser.getDouble("VRMIN"));
		   
		   exc.setKE(dataParser.getDouble("KE"));
		   exc.setTE(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TE")));
		   
		   exc.setKF(dataParser.getDouble("KF"));
		   exc.setTF(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TF")));
		   
		   exc.setE1(dataParser.getDouble("E1"));
		   exc.setSE1(dataParser.getDouble("SE(E1)"));
		   exc.setE2(dataParser.getDouble("E2"));
		   exc.setSE2(dataParser.getDouble("SE(E2)"));
		   }
	   
	   }
		
	}

}
