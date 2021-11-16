package org.ieee.odm.adapter.psse.parser.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSERelayLDS3DataParser extends BasePSSEDataParser {
	public PSSERelayLDS3DataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
       
		/*
        *                                                      
         10 'LDS3BL' 1          0         '0 '            0       59.300
          15.000      0.13300       1.0000       0.0000       0.0000
          0.0000       0.0000       0.0000       0.0000       0.0000
          0.0000       0.0000       0.0000       0.0000       0.0000
          0.0000       0.0000       0.0000       0.0000       0.0000    /
        */
		
		return new String[]{
		 // Header		
		 "IBUS", "Type", "LID",
		 
		 // M parameters
		 "GBus",      "GID",     "SC", 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		    "F1",     "T1",      "Tb1",   "Frac1",  "F2",     "T2",     "Tb2",  "Frac2",    "F3",       "T3", 
		 // 10---------11----------12---------13---------14-------15-------16---------17-----------18---------19
		    "Tb3",   "Frac3",    "F4",     "T4",      "Tb4",   "Frac4",     "F5",    "T5",      "Tb5",      "Frac5",  
		//  20----------
		    "Ttb"
		};

		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks while treating contents within quotes as a single entities
		String[] strAry= null;
		
			
		if(lineStr.contains(","))
			strAry=lineStr.split("\\s*(\\s|,)\\s*");
		else {
			if(lineStr.contains("'")) strAry =lineStr.replace("'", "\"").split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			else strAry=lineStr.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		}
		
		int cnt =strAry.length;
		int idx = 0;
		for (int i = 0; i <cnt ; i++){
			
			if(strAry[i].contains("\"")) 
				setValue(idx,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			else 
				setValue(idx, strAry[i].trim());
				
			idx++;
			
			
		}
		
	}

}
