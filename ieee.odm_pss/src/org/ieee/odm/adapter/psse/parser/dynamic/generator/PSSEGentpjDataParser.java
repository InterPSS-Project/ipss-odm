package org.ieee.odm.adapter.psse.parser.dynamic.generator;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEGentpjDataParser extends BasePSSEDataParser {
	public PSSEGentpjDataParser(PsseVersion ver) {
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
			   "Xq",       "X'd",      "X'q",   "X''d",   "X''q",
			//  15----------16----------17-----18
			   "Xl",      "S(1.0)", "S(1.2)", "Kis"
		};
	}
	
	/*
	 * Xd, Xq, X’d, X’q, X"d, X"q, Xl, H, and D are in pu, 
       machine MVA base. 
       X"qmust be equal to X"d.
       
       version >33
       IBUS, ’GENTPJ1’, I, T’do, T"do, T"qo, T"qo, H, D, Xd, Xq, X’d, X’q, X"d, X"q, Xl, S(1.0), S(1.2),Kis/
       
       version <=33
       IBUS, 'USRMDL', ID,   'GENTPJU1',   1,   1,   0,  16,   6,   1, <Actual parameter list as above>       
	 */
	
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		int tableIdx = 0;
		for (int i = 0; i <cnt ; i++){
			
			if(PSSEAdapter.getVersionNo(this.version)<=33) {
				if(i==1 || (i>3 && i<10)) {
					//skip it
				}	
				else if(i==3){//gen model type, need to trim the quote
					setValue(1,ODMModelStringUtil.trimQuote(strAry[i].trim()));
					tableIdx++;
				}
				else if(i==2){ //machId
					setValue(2,strAry[i].trim());
					tableIdx++;
				}
				else if(i==0 || i>=10){
					setValue(tableIdx,strAry[i].trim());
					tableIdx++;
				}
			
				
				
			}
			else { //Version = 34 or newer 
				if(i==1){//gen model type, need to trim the quote
					setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
				}
				else {
					setValue(tableIdx,strAry[i].trim());
				}
				
			}
			
			
		}
		
	}
	

}
