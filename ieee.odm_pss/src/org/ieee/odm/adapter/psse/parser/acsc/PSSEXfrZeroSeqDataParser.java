package org.ieee.odm.adapter.psse.parser.acsc;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ModelStringUtil;

public class PSSEXfrZeroSeqDataParser extends BasePSSEDataParser {
	public PSSEXfrZeroSeqDataParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override 
	public String[] getMetadata() {

	
	/**
	 * PSS/E ver. 30-32 TRANSFORMER Zero sequence data
	 * 1) For two-winding transformers:
	 * 
	 * I,J,K, ICKT,CC, RG,XG,R1,X1,R2,X2
	 * 
	 * 2) For three-winding transformers:
	 * I,J,K, ICKT,CC, RG,XG,R1,X1,R2,X2,R3,X3
	 * 
	 * 
	 * k=0 for two-winding xfr; k=0 by default
	 * ICKT =1 by default
	 * 
	 * RG, XG--Zero sequence grounding impedance for an impedance grounded transformer
	   R2  X2--For a two-winding transformer, Z2 = R2 + jX2 is applied as shown in Figure 5-18if 
               the connection code is 8 or 9, and is ignored if the connection code is 1 through 7.
               Z2 is the zero sequence grounding impedance entered in per unit at the Winding 2 
               side of an impedance grounded transformer where the connection code is 8.
               Z2 is the series impedance, entered in per unit on system base MVA and bus 
               voltage base, connected to the Winding 2 side bus if the connection code is 9.
               R2 = 0.0 and X2 = 0.0 by default.
	 */
	//Data sample
	//101,   151,     0,'1 ',  2,    0.00000,    0.00000,    0.00030,    0.01360
	   return new String[]{
		//  0----------1----------2----------3----------4
			"I",       "J",      "K",       "ICKT",    "CC", 
		//  5          6          7          8           9  	
			"RG",      "XG",     "R1",       "X1",      "R2",
		//  10         11         12  	
			"X2",      "R3",      "X3"
	   };
	}
	
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
			this.clearNVPairTableData();
			StringTokenizer st = new StringTokenizer(lineStr,",");
			int cnt =st.countTokens();
			for (int i = 0; i <cnt ; i++){
				if(i==3){
					setValue(i,ModelStringUtil.trimQuote(st.nextToken().trim()).trim());
				}
				else setValue(i, st.nextToken().trim());
			}
			
	
	
	
	}
}
