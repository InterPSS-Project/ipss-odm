package org.ieee.odm.adapter.psse.raw.parser.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSERelayLVS3DataParser extends BasePSSEDataRawParser {
	public PSSERelayLVS3DataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
       
		/*
        *                                                      
        11 'LVS3BL' 1          0            0         '0 '            0
               0         '0 '            0      0.91300       6.0000
         0.66000E-01  0.24500      0.91300       10.000      0.66000E-01
         0.54500       0.0000       0.0000       0.0000       0.0000
          0.0000       0.0000       0.0000       0.0000       0.0000
          0.0000       0.0000       0.0000       0.0000       0.0000      /
        */
		
		return new String[]{
		 // Header		
		 "IBUS", "Type", "LID",
		 
		 // M parameters
		 "FBus1",      "TBus1",  "Id1",    "FBus2",      "TBus2",  "Id2",  "SC", 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		    "F1",     "T1",      "Tb1",   "Frac1",  "F2",     "T2",     "Tb2",  "Frac2",    "F3",       "T3", 
		 // 10---------11----------12---------13---------14-------15-------16---------17-----------18---------19
		    "Tb3",   "Frac3",    "F4",     "T4",      "Tb4",   "Frac4",     "F5",    "T5",      "Tb5",      "Frac5",  
		//  20----------21
		    "Ttb1",      "Ttb2"
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
