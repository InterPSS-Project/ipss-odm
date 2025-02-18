package org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSETurGovIEESGOParser extends BasePSSEDataRawParser {
	public PSSETurGovIEESGOParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * //PSSE IEESGO -> IEEE 1973 standard Tur-Gov model
	 * IBUS, 鎵濫ESGO锟� I, T1, T2, T3, T4, T5, T6, K1, K2, K3, PMAX, PMIN/ 
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "T1",       "T2",
			//  5----------6----------7----------8----------9
				"T3",     "T4",      "T5",      "T6",     "K1",  	 
			//  10----------11--------12---------13---------14	
				"K2",       "K3",   "PMAX",   "PMIN"		   
					
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){// model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}
	
	

}
