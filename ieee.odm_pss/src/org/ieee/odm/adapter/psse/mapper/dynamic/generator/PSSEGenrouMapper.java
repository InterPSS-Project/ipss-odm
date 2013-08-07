package org.ieee.odm.adapter.psse.mapper.dynamic.generator;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.dynamic.generator.PSSEGenrouDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq1MachineXmlType.SeFmt1;

public class PSSEGenrouMapper extends BasePSSEDataMapper{

	public PSSEGenrouMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEGenrouDataParser(ver);
	}
	
	/*
	 * Xd, Xq, X�d, X�q, X"d, X"q, Xl, H, and D are in pu, 
       machine MVA base. 
       X"qmust be equal to X"d.
       
       IBUS, �GENROU�, I, T�do, T"do, T"qo, T"qo, H, D, Xd, Xq, X�d, X�q, X"d, Xl, S(1.0), S(1.2)/
	 */
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		/*
		 * //  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "T'do",     "T''do",
			//  5----------6----------7----------8----------9
				"T'qo",  "T''qo",    "H",       "D",      "Xd", 
			//  10----------11----------12--------13--------14	
			   "Xq",       "X'd",      "X'q",   "X''d",   "Xl", 
			//  15----------16----
			   "S(1.0)", "S(1.2)"
		 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = AbstractModelParser.BusIdPreFix+i;
	    String genId = dataParser.getString("MachId");
	    
	    //check model type
	    //GENROU and GENROE data format are the same
	    if(!(dataParser.getString("Type").equals("GENROU")||
	    		dataParser.getString("Type").equals("GENROE"))){
	    	throw new ODMException("machine  : Id"+
		             genId+" @ Bus"+i+"is not a GENROU or GENROE generator model");
	    }
	    
	   
	    
	   DStabBusXmlType busXml = parser.getBus(busId);
	    
	   DStabGenDataXmlType dstabGenData = DStabParserHelper.getDStabContritueGen(busXml, genId);
	   
	   Eq11Ed11MachineXmlType mach = DStabParserHelper.createEq11Ed11Machine(dstabGenData);
	   
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
	   double s100 = dataParser.getDouble("S(1.0)");
	   double s120 = dataParser.getDouble("S(1.2)");
	   
	   //set the type info
	   mach.setDesc(dataParser.getString("Type"));
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
	   mach.setXq11(Xd11); // x''q = x''d
	   mach.setXd11(Xd11);
	   
	   //saturation 
	   //A and B are such that the points (1.0, S1.0) and (1.2, S1.2)
	   
	   SeFmt1 s1= DStabParserHelper.createMachineSeFmt1();
	   s1.setSliner(1.0); // by default
	   s1.setSe100(s100); 
	   s1.setSe120(s120);
	   mach.setSeFmt1(s1);
	   
	   
	}

}
