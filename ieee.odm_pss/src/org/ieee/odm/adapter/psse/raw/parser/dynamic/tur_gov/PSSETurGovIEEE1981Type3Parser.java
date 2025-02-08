package org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSETurGovIEEE1981Type3Parser extends BasePSSEDataRawParser {
	public PSSETurGovIEEE1981Type3Parser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * PSSE IEEEG3 -> IEEE 1981 Type 3  Tur-Gov model for Hydro 
	 * ----------------------------------------------------------
	 * IBUS, 'IEEEG3', ID, TG, TP, Uo, Uc, PMAX, PMIN, Sigma, Delta, TR,TW,a11,a13,a21,a23/
	 *
	 *
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "TG",       "TP",       
			//  5----------6----------7----------8----------9
				"Uo",    "Uc",     "PMAX",    "PMIN",    "Sigma",  	 
			//  10----------11--------12---------13---------14	
				"Delta",  "TR",      "TW",      "a11",     "a13",
			//  15----------16	
				"a21",   "a23"
			
					
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
