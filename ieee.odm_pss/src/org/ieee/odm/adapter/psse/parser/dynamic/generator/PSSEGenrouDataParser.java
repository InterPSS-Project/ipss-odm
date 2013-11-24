package org.ieee.odm.adapter.psse.parser.dynamic.generator;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEGenrouDataParser extends BasePSSEDataParser {
	public PSSEGenrouDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
		
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",   "T'do",     "T''do",
			//  5----------6----------7----------8----------9
				"T'qo",  "T''qo",    "H",       "D",      "Xd", 
			//  10----------11----------12--------13--------14	
			   "Xq",       "X'd",      "X'q",   "X''d",   "Xl", 
			//  15----------16----
			   "S(1.0)", "S(1.2)"
		};
	}
	
	/*
	 * Xd, Xq, X’d, X’q, X"d, X"q, Xl, H, and D are in pu, 
       machine MVA base. 
       X"qmust be equal to X"d.
       
       IBUS, ’GENROU’, I, T’do, T"do, T"qo, T"qo, H, D, Xd, Xq, X’d, X’q, X"d, Xl, S(1.0), S(1.2)/
	 */
	
	
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
