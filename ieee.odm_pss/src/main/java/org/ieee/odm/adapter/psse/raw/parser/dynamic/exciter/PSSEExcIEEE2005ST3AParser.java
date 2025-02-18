package org.ieee.odm.adapter.psse.raw.parser.dynamic.exciter;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSEExcIEEE2005ST3AParser extends BasePSSEDataRawParser {
	public PSSEExcIEEE2005ST3AParser(PsseVersion ver) {
		super(ver);
	}
	
	/*
	 * PSS/E ESST3A type -> IEEE 1992/2005 Type ST3A
	 * IBUS, 'ESST3A', TR, VIMAX,VIMIN, KM,TC,TB, KA, TA, VRMAX, VRMIN, KG, KP, KI, VBMAX, KC, XL,VGMAX, THETAP, TM, VMMAX, VMMIN/
	 */
	
	@Override
	public String[] getMetadata() {
		return new String[]{
			//  0----------1----------2----------3----------4
				"IBUS",  "Type",    "MachId",   "TR",       "VIMAX",
			//  5----------6----------7----------8----------9
				"VIMIN",    "KM",    "TC",      "TB",      "KA",    		     	 
			//  10----------11----------12--------13--------14
				"TA",	 "VRMAX",   "VRMIN",     "KG",     "KP",
			//  15----------16----------17--------18--------19	
				"KI",    "VBMAX",    "KC",     	 "XL",    "VGMAX",
			//  20----------21----------22--------23--------24
				"THETAP",   "TM",    "VMMAX",   "VMMIN"
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
