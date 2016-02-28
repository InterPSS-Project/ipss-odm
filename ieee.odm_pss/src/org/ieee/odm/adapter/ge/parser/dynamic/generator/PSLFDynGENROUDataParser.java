package org.ieee.odm.adapter.ge.parser.dynamic.generator;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSLFDynGENROUDataParser extends AbstractDataFieldParser {

	@Override
	public String[] getMetadata() {
	
		return new String[]{
				//  0----------1----------2----------3----------4
					"Type",  "IBUS",   "NAME",    "BASEKV", "MachId",
				//  5----------6----------7----------8----------9
					"MVA",  "T'do",   "T''do",    "T'qo",  "T''qo", 
				//  10----------11----------12--------13--------14	
					 "H",       "D",      "Xd",    "Xq",       "X'd",
				//  15----------16-------17---------18---------19----
					"X'q",   "X''d",   "Xl",     "S(1.0)",   "S(1.2)", 
				//  20--------21--------22---------23--------------
					"Ra",    "Rcomp",  "Xcomp",   "accel"
				   
			};
		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		// split the line string by multi-blanks while treating contents within quotes as a single entities
		String[] strAry=lineStr.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		int cnt =strAry.length;
		int k = 0;
		// for the ID, there could be blank within quotes
		for (int i = 0; i <cnt ; i++){
			if(i==0)
				setValue(k++, strAry[i].trim().toUpperCase());
			else if(i<5){
				if(strAry[i].contains("\"")){
					setValue(k++,ODMModelStringUtil.trimQuote(strAry[i]).trim());
				}
				else setValue(k++, strAry[i].trim());
			}
			else if(i==5 || i==6){
				// skip the " : #9"
			}
			else if(i==7){
				if(strAry[i].contains("mva=")){
					String mvaString = strAry[i].substring(4, strAry[i].length()); 
					setValue(k++,mvaString);
				}
			}
			else if(i>7){
				if(strAry[i].contains("\"")){
					// just skip items like "tpdo"
				}
				
			    else setValue(k++, strAry[i].trim());
				}
		}
		
	}

}
