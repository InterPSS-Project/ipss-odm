package org.ieee.odm.adapter.psse.parser.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSETurGovIEEE1981Type1Parser extends BasePSSEDataParser {
	public PSSETurGovIEEE1981Type1Parser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * //PSSE IEEEG1 -> IEEE 1981 Type 1  Tur-Gov model
	 * IBUS, 'IEEEG1', JBUS, M, K, T1, T2, T3, Uo, Uc, PMAX, PMIN, T4, K1, K2, T5, K3, K4, T6, K5, K6, T7, K7, K8/
	 *
	 *Note: JBUS and JM are set to zero for noncross compound
	 *
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "JBUS",       "M",
			//  5----------6----------7----------8----------9
				"K",      "T1",      "T2",      "T3",      "Uo",   	 
			//  10----------11--------12---------13---------14	
				"Uc",     "PMAX",    "PMIN",     "T4",      "K1",
			//  15----------16--------17---------18---------19		
				"K2",      "T5",      "K3",      "K4",       "T6",
			//  20----------21--------22---------23---------24	
				"K5",      "K6",      "T7",      "K7",       "K8",
			
					
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
