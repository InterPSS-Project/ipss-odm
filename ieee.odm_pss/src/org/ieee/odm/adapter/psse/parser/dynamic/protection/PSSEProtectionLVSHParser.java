package org.ieee.odm.adapter.psse.parser.dynamic.protection;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEProtectionLVSHParser extends BasePSSEDataParser {
	public PSSEProtectionLVSHParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * //PSSE LVSHBL, LVSHOW, LVSHZN, LVSHAR, LVSHAL -> Undervoltage Load Shedding Model
	 * I, ’LVSHxx’, LID, ICON(M), CON(J) to CON(J+9) /
	 *
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "LID",     "V1",     "T1",
			//  5----------6----------7----------8----------9
				"F1"  , "V2",     "T2",      "F2",      "V3",   	 
			//  10----------11--------12---------13---------14	
				"T3",     "F3",     "TB",    
			
					
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
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
			else setValue(i, strAry[i].trim());
		}
		
	}


}
