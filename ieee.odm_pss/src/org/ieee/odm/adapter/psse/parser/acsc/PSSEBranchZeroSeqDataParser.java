package org.ieee.odm.adapter.psse.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEBranchZeroSeqDataParser extends BasePSSEDataParser {
	public PSSEBranchZeroSeqDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
		/**
		 * PSS/E ver. 30-32 NON-TRANSFORMER Branch Zero sequence data
		 * 
		 * I, J, ICKT, RLINZ, XLINZ, BCHZ, GI, BI, GJ, BJ
		 */
		
		return new String[] {
				 //  0----------1----------2----------3----------4
				   "I",        "J",     "ICKT",    "RLINZ",   "XLINZ",
				   
				 //  5          6          7          8           9  
				   "BCHZ",     "GI",      "BI",      "GJ",       "BJ"
		};
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		StringTokenizer st = new StringTokenizer(lineStr,",");
		int cnt =st.countTokens();
		for (int i = 0; i <cnt ; i++){
			if(i==2){//cirId, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(st.nextToken().trim()).trim());
			}
			else setValue(i, st.nextToken().trim());
		}
		
	}
}


	
