package org.ieee.odm.adapter.psse.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;

public class PSSELoadNegSeqDataParser extends BasePSSEDataParser {
	public PSSELoadNegSeqDataParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override public String[] getMetadata() {
		
	/*
	 * PSS/E ver. 30-32 negative sequence SHUNT Load data
	 * 
	 * 
	 * I, GNEG,BNEG
	 * 
	 */
		return new String[] {
				   //  0----------1----------2
					 "I",        "GNEG", "BNEG"            
				};	
		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		StringTokenizer st = new StringTokenizer(lineStr,",");
		for (int i = 0; i < 3; i++)
			setValue(i, st.nextToken().trim());
	}

}
