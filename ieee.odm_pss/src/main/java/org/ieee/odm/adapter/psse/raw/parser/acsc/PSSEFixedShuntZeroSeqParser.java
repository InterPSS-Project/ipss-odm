package org.ieee.odm.adapter.psse.raw.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;

public class PSSEFixedShuntZeroSeqParser extends BasePSSEDataRawParser {
	public PSSEFixedShuntZeroSeqParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
         
	
	/*
	 * For V32 and later: Zero Sequence Fixed Shunt Data
	 * I, ID, GSZERO, BSZERO
	 * 
	 */
		
		return new String[]{
				"I", "ID", "GSZERO", "BSZERO"
		};
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		StringTokenizer st = new StringTokenizer(lineStr,",");
		int cnt =st.countTokens();
		for (int i = 0; i <cnt ; i++)
			setValue(i, st.nextToken().trim());
	}
	

}
