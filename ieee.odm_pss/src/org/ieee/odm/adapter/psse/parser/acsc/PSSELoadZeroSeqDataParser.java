package org.ieee.odm.adapter.psse.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;

public class PSSELoadZeroSeqDataParser extends BasePSSEDataParser {
		public PSSELoadZeroSeqDataParser(PsseVersion ver) {
			super(ver);
		}
		
		@Override public String[] getMetadata() {
			
		/*
		 * PSS/E ver. 30-32 zero sequence SHUNT Load data
		 * 
		 * 
		 * I, GNEG,BNEG
		 * 
		 */
			return new String[] {
					   //  0----------1----------2
						 "I",        "GZERO", "BZERO"            
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
