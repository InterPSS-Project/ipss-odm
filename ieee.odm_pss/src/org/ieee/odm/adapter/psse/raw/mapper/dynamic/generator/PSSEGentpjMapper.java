package org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator;

import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.generator.PSSEGentpjDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq11MachineXmlType;
import org.ieee.odm.schema.Eq1MachineXmlType.SeFmt1;

public class PSSEGentpjMapper extends BasePSSEDataRawMapper{

	public PSSEGentpjMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEGentpjDataParser(ver);
	}
	
	public PSSEGentpjMapper(AbstractStringDataFieldParser parser){
		this.dataParser = parser;
	}
	
	/*
	 * Xd, Xq, X'd, X'q, X"d, X"q, Xl, H, and D are in pu, 
       machine MVA base. 
       X"qmust be equal to X"d.
       
       IBUS, modelType, machId, T'do, T"do, T"qo, T"qo, H, D, Xd, Xq, X'd, X'q, X"d, X"q, Xl, S(1.0), S(1.2), Kis/
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
		//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "T'do",     "T''do",
			//  5----------6----------7----------8----------9
				"T'qo",  "T''qo",    "H",       "D",      "Xd", 
			//  10----------11----------12--------13--------14	
			   "Xq",       "X'd",      "X'q",   "X''d",   "X''q",
			//  15----------16----------17-----18
			   "Xl",      "S(1.0)", "S(1.2)", "Kis"
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String genId = dataParser.getValue("MachId");
	    
	    //check model type
	    //GENROU and GENROE data format are the same
	    if(!(dataParser.getValue("Type").equals("GENTPJU1")||
	    		dataParser.getValue("Type").equals("GENTPJ1"))){
	    	throw new ODMException("machine  : Id"+
		             genId+" @ Bus"+i+"is not a GENTPJ generator model");
	    }
	    
	   
	    
	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){ 
		   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
		   
		   if(dstabGenData!=null){
			   
			       
				   
				   
				   double Td1 = dataParser.getDouble("T'do");
				   double Td11 = dataParser.getDouble("T''do");
				   double Tq1 = dataParser.getDouble("T'qo");
				   double Tq11 = dataParser.getDouble("T''qo");
				   double H = dataParser.getDouble("H");
				   double D = dataParser.getDouble("D");
				   
				   double Xl = dataParser.getDouble("Xl");
				   double Xd = dataParser.getDouble("Xd");
				   double Xq = dataParser.getDouble("Xq");
				   double Xd1 = dataParser.getDouble("X'd");
				   double Xq1 = dataParser.getDouble("X'q");
				   double Xd11 = dataParser.getDouble("X''d");
				   double Xq11 = dataParser.getDouble("X''q");
				   double s100 = dataParser.getDouble("S(1.0)",0.0)*100; // in percentage
				   double s120 = dataParser.getDouble("S(1.2)",0.0)*100;
				   double Kis = dataParser.getDouble("Kis");
				   
				   if (Tq1>1.0E-6) {
						   Eq11Ed11MachineXmlType mach = DStabParserHelper.createEq11Ed11Machine(dstabGenData);
						   
						   //set the type info
						   mach.setDesc(dataParser.getValue("Type"));
						   mach.setD(D);
						   mach.setH(H);
						   //TODO Ra = RSource?
						   mach.setRa(dstabGenData.getSourceZ().getRe());
						   
						   //TODO it there a need to use time Unit?
						   mach.setTd01(DStabDataSetter.createTimeConstSec(Td1));
						   mach.setTd011(DStabDataSetter.createTimeConstSec(Td11));
						   mach.setTq01(DStabDataSetter.createTimeConstSec(Tq1));
						   mach.setTq011(DStabDataSetter.createTimeConstSec(Tq11));
						   
						   mach.setXl(Xl);
						   mach.setXd(Xd);
						   mach.setXq(Xq);
						   mach.setXd1(Xd1);
						   mach.setXq1(Xq1);
						   mach.setXq11(Xq11); // x''q = x''d
						   mach.setXd11(Xd11);
						   
						   //saturation 
						   //A and B are such that the points (1.0, S1.0) and (1.2, S1.2)
						   
						   SeFmt1 s1= DStabParserHelper.createMachineSeFmt1();
						   //s1.setSliner(1.0); // by default
						   s1.setSe100(s100); 
						   s1.setSe120(s120);
						   mach.setSeFmt1(s1);
				   }
				   else {
					   Eq11MachineXmlType mach = DStabParserHelper.createEq11Machine(dstabGenData);
					   
					   //set the type info
					   mach.setDesc(dataParser.getValue("Type"));
					   mach.setD(D);
					   mach.setH(H);
					   //TODO Ra = RSource?
					   mach.setRa(dstabGenData.getSourceZ().getRe());
					   
					   //TODO it there a need to use time Unit?
					   mach.setTd01(DStabDataSetter.createTimeConstSec(Td1));
					   mach.setTd011(DStabDataSetter.createTimeConstSec(Td11));
					   mach.setTq011(DStabDataSetter.createTimeConstSec(Tq11));
					   
					   mach.setXl(Xl);
					   mach.setXd(Xd);
					   mach.setXq(Xq);
					   mach.setXd1(Xd1);
					
					   mach.setXq11(Xq11); // x''q = x''d
					   mach.setXd11(Xd11);
					   
					   //saturation 
					   //A and B are such that the points (1.0, S1.0) and (1.2, S1.2)
					   
//					   SeFmt1 s1= DStabParserHelper.createMachineSeFmt1();
//					   //s1.setSliner(1.0); // by default
//					   s1.setSe100(s100); 
//					   s1.setSe120(s120);
//					   mach.setSeFmt1(s1);
				   }
				   
		   }
		   else{
			   ODMLogger.getLogger().severe("Machine # "+genId +" is not found in Bus #"+busId);
		   }
	   }
	   else{
		   ODMLogger.getLogger().severe("Bus # "+busId +" is not available in load flow data");
	   }
	}

}
