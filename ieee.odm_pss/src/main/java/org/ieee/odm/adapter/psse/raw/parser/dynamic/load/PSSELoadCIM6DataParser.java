package org.ieee.odm.adapter.psse.raw.parser.dynamic.load;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSELoadCIM6DataParser extends BasePSSEDataRawParser {
	
	public PSSELoadCIM6DataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
       /*
        *                                                      
        I, CIMWxx, LID, IT,  RA,  XA,  Xm,    R1,   X1,   R2,   X2,   E1, S(E1), E2, S(E2), MBASE,PMULT, H,   VI,    TI,         TB,    A, B,   D, E, C0, Tnom
        */
		return new String[]{
		 // Header		
		 "IBUS",  "Type", "LID", "IT",
		 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		 "RA",       "XA",       "Xm",     "R1",       "X1",   "R2",     "X2",    "E1",     "S(E1)",     "E2",       
		 // 10---------11----------12---------13---------14-------15-------16---------17--------18---------19
		 "S(E2)",  "MBASE",     "PMULT",     "H",       "VI",    "TI",     "TB",    "A",        "B",        "D", 
		//  20---------21----------22---------23---------24-------25-------26---------27--------28---------29
		 "E", "C0", "Tnom"
		
		};

		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		
		String[] strAry= null;
		if(lineStr.contains(","))
			// split the line by space and/or comma
			strAry=lineStr.split("\\s*(\\s|,)\\s*");
		else
			// split the line string by multi-blanks while treating contents within quotes as a single entities
		  strAry=lineStr.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		
		int cnt =strAry.length;
		
		for (int i = 0; i <cnt ; i++){
			
			if(i==1){// model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else 
				setValue(i, strAry[i].trim());
			
			
			
		}
		
	}


}
