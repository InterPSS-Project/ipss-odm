package org.ieee.odm.adapter.psse.parser.dynamic.generator;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEGenclsDataParser extends BasePSSEDataParser {
	public PSSEGenclsDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
		
		
		/*
		 * IBUS, ’GENCLS’, I, H, D/
		 */
		return new String[] {
				// 0----------1----------2----------3----------4
				 "IBUS",    "Type",   "MachId",   "H",   "D",
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
