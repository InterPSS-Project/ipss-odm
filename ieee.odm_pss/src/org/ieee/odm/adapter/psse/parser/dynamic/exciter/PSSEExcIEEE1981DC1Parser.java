package org.ieee.odm.adapter.psse.parser.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEExcIEEE1981DC1Parser extends BasePSSEDataParser {
		public PSSEExcIEEE1981DC1Parser(PsseVersion ver) {
			super(ver);
		}
		
		/*
		 * PSS/E IEEEX1 type -> IEEE 1981 Type DC1
		 * IBUS, 'IEEEX1', TR, KA, TA, TB,TC, VRMAX, VRMIN, KE, TE, KF, TF1, 0., E1, SE(E1), E2, SE(E2)/
		 */
		
		@Override
		public String[] getMetadata() {
			return new String[]{
				//  0----------1----------2----------3----------4
					"IBUS", "Type",   "MachId",   "TR",       "KA",
				//  5----------6----------7----------8----------9
					"TA",    "TB",      "TC",    "VRMAX",   "VRMIN",     	 
				//  10----------11----------12--------13--------14	
					"KE",      "TE",      "KF",    "TF1",      "Switch",   		   
				//  15----------16----
					"E1",   "SE(E1)",     "E2",    "SE(E2)"			
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
