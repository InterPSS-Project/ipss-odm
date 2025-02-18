package org.ieee.odm.adapter.psse.raw.parser.dynamic.generator;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEGensalDataParser extends BasePSSEDataRawParser {
	public PSSEGensalDataParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * format
	 * 
	 * IBUS, 扜ENSAL�, I, T抎o, T"do, T"qo, H, D, Xd, Xq, X抎, X"d, Xl, S(1.0), S(1.2)/
	 * 
	 */
	@Override
	public String[] getMetadata() {
		
		
		/*
		 * GENSEL
		 * 
		 * IBUS, 扜ENSAL�, I, T抎o, T"do, T"qo, H, D, 
		 * Xd, Xq, X抎, X"d, Xl, S(1.0), S(1.2)/
		 */
		return new String[] {
				// 0----------1----------2----------3----------4
				 "IBUS",    "Type",   "MachId",   "T'do",   "T''do",
				// 5----------6----------7----------8----------9
				  "T''qo",     "H",       "D",       "Xd",    "Xq",
				// 10----------11----------12--------13--------14
				   "X'd",      "X''d",   "Xl",     "S(1.0)", "S(1.2)" 
				
				
				};
	}
	
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){//gen model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}
	

}
