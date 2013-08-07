package org.ieee.odm.adapter.psse.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ModelStringUtil;

public class PSSEMachineZeroSeqZParser extends BasePSSEDataParser {
	public PSSEMachineZeroSeqZParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override public String[] getMetadata() {
		
	/*
	 * PSS/E ver. 30-32 machine zero sequence data
	 * 
	 * I, ID, ZRZERO, ZXZERO
	 * 
	 */
		return new String[] {
				   //  0----------1----------2----------3
					 "I",        "ID",  "ZRZERO", "ZXZERO"            
				};	
		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		StringTokenizer st = new StringTokenizer(lineStr,",");
		for (int i = 0; i < 4; i++){
			if(i==1){//genId, need to trim the quote
				setValue(i,ModelStringUtil.trimQuote(st.nextToken()).trim());
			}
			else setValue(i, st.nextToken().trim());
		}
	}

}
