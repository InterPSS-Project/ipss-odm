package org.ieee.odm.adapter.psse.mapper.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.exciter.PSSEExcIEEE1981AC1Parser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeAC1XmlType;

public class PSSEExcIEEE1981AC1Mapper extends BasePSSEDataMapper{
    
	public PSSEExcIEEE1981AC1Mapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEExcIEEE1981AC1Parser(ver);
	}

	/*
	 * 
	 * PSSE EXAC1 -> IEEE 1981 Type AC1
	 * 
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
	       //  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "TR",       "TB",
			//  5----------6----------7----------8----------9
				"TC",    "KA",      "TA",    "VRMAX",   "VRMIN",    	 
			//  10----------11----------12--------13--------14	
				"TE",      "KF",      "TF",      "KC",     "KD", 
			//  15----------16----------17--------18--------19	
				"KE",      "E1",      "SE(E1)",	 "E2",    "SE(E2)"						
		
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    if(!dataParser.getString("Type").equals("EXAC1")){
	    	throw new ODMException(" Exciter of machine  : Id"+
		             genId+" @ Bus"+i+"is not a EXAC1 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){
	       DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   if(dstabGenData!=null){
			   ExcIEEE1981TypeAC1XmlType exc = DStabParserHelper.createExcIEEE1981TypeAC1XmlType(dstabGenData);
			   
			   exc.setDesc(dataParser.getString("Type"));
			   exc.setTR(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TR")));
			   exc.setTB(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TB")));
			   exc.setTC(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TC")));
			   
			   exc.setKa(dataParser.getDouble("KA"));
			   exc.setTa(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TA")));
			   exc.setVrmax(dataParser.getDouble("VRMAX"));
			   exc.setVrmin(dataParser.getDouble("VRMIN"));
			   
			   exc.setKE(dataParser.getDouble("KE"));
			   exc.setTE(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TE")));
			   
			   exc.setKF(dataParser.getDouble("KF"));
			   exc.setTF(DStabDataSetter.createTimeConstSec(dataParser.getDouble("TF")));
			   
			   exc.setKC(dataParser.getDouble("KC"));
			   exc.setKD(dataParser.getDouble("KD"));
			   
			   exc.setE1(dataParser.getDouble("E1"));
			   exc.setSE1(dataParser.getDouble("SE(E1)"));
			   exc.setE2(dataParser.getDouble("E2"));
			   exc.setSE2(dataParser.getDouble("SE(E2)"));
		   }else{
			   ODMLogger.getLogger().severe("Dynamic model for generator # "+genId +" is not found in Bus #"+busId);
		   }
		}
		else{
		   ODMLogger.getLogger().severe("Bus is not found in Bus #"+busId);
		}
		
	}
	
	

}
