package org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSETurGovGASTParser extends BasePSSEDataRawParser {
	public PSSETurGovGASTParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * //PSSE GAST -> Gas turbine-governor model
	 * IBUS, 'GAST', ID, R,T1,T2,T3,AT,KT,VMAX,VMIN,Dturb/
	 *
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS", "Type",   "MachId",     "R",     "T1",
			//  5----------6----------7----------8----------9
				"T2",     "T3",      "AT",      "KT",    "VMAX",   	 
			//  10----------11--------12---------13---------14	
				"VMIN",     "Dturb",    
			
					
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){// model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}

}
