package org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEExcIEEE1981ST1Parser extends BasePSSEDataRawParser {
	public PSSEExcIEEE1981ST1Parser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * PSS/E EXST1 type -> IEEE 1981 Type ST1
	 * IBUS, 'EXST1', TR, VIMAX,VIMIN, TC,TB, KA, TA, VRMAX, VRMIN, KC, KF, TF/
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS",  "Type",   "MachId",   "TR",       "VIMAX",
			//  5----------6----------7----------8----------9
				"VIMIN",     "TC",    "TB",      "KA",    "TA",     	 
			//  10----------11----------12--------13--------14	
				"VRMAX",   "VRMIN",    "KC",      "KF",    "TF"	
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){//Model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}
}
