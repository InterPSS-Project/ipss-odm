package org.ieee.odm.adapter.psse.raw.parser.dynamic.tur_gov;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSETurGovTGOV1Parser extends BasePSSEDataRawParser {
	
	public PSSETurGovTGOV1Parser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * //PSSE TGOV1 -> IEEE 1981 Type 1  Tur-Gov model
	 * Note: VMAX, VMIN, Dtare in per unit on generator base.
            T2/T3= high-pressure fraction.
            T3= reheater time constant.; T3>0
            
       Format: 
            
        IBUS, 扵GOV1�, I, R, T1, VMAX, VMIN, T2, T3, Dt/
	 *
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS",  "Type",   "MachId",     "R",      "T1",
			//  5----------6----------7----------8----------9
				"VMAX",  "VMIN",     "T2",     "T3",      "Dt"   	 	
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
