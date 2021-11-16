package org.ieee.odm.adapter.psse.parser.dynamic.relay;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSERelayVTGTPATDataParser extends BasePSSEDataParser {
	public PSSERelayVTGTPATDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
       
		/*
        *                                                      
     9   'VTGTPAT'     111    111  '1 '
         0.75000       5.0000       10.000       0.0000      /
    10   'VTGTPAT'     111    111  '1 '
         0.70000       5.0000       1.0000       0.0000      /
        */
		
		return new String[]{
		 // Header		
		 "MINS", "Type", 
		 
		 // parameters
		
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		 "VoltBus",    "GenBus",  "GenId",    "VL",     "VU",  "TP",   "TB", 

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
