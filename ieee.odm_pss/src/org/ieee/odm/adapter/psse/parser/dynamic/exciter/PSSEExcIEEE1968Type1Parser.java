package org.ieee.odm.adapter.psse.parser.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ModelStringUtil;

public class PSSEExcIEEE1968Type1Parser extends BasePSSEDataParser {
	public PSSEExcIEEE1968Type1Parser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * PSSE IEEET1 -> IEEE 1968 Type 1
	 * IBUS, ’IEEET1’, I, TR, KA, TA, VRMAX, VRMIN, KE, TE, KF, TF, 0., E1, SE(E1), E2, SE(E2)/
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "TR",       "KA",
			//  5----------6----------7----------8----------9
				"TA",    "VRMAX",   "VRMIN",   "KE",      "TE",  	 
			//  10----------11----------12--------13--------14	
				"KF",    "TF",      "Switch",   "E1",   "SE(E1)",		   
			//  15----------16----
				"E2",    "SE(E2)"			
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){//GenId, need to trim the quote
				setValue(i,ModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}
	
	

}
