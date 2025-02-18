package org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEExcIEEE2005ST4BParser extends BasePSSEDataRawParser {
	public PSSEExcIEEE2005ST4BParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * PSS/E ESST4B type -> IEEE 1992/2005 Type ST4B
	 * IBUS, 'ESST4B', TR, KPR,KIR, VRMAX, VRMIN,TA KPM,KIM, VMMAX, VMMIN, KG, KP, KI, VBMAX, KC, XL,THETAP/
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS",  "Type",    "MachId",   "TR",       "KPR",
			//  5----------6----------7----------8----------9
				"KIR",   "VRMAX", "VRMIN",   "TA",      "KPM",       		     	 
			//  10----------11-------12--------13---------14
				"KIM",   "VMMAX", "VMMIN",    "KG",      "KP",     
			//  15----------16-------17--------18---------19	
				"KI",    "VBMAX",  "KC",     "XL",    "THETAP"
		
		};
		
	}
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks
		String[] strAry=lineStr.split("\\s+");
		int cnt =strAry.length;
		for (int i = 0; i <cnt ; i++){
			if(i==1){//Model type, need to trim the quote
				setValue(i,ODMModelStringUtil.trimQuote(strAry[i].trim()));
			}
			else setValue(i, strAry[i].trim());
		}
		
	}

}
