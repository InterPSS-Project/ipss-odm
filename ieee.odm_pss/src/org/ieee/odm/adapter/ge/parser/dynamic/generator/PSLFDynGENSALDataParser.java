package org.ieee.odm.adapter.ge.parser.dynamic.generator;

import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSLFDynGENSALDataParser extends AbstractStringDataFieldParser {
	
	public PSLFDynGENSALDataParser() {
	    super();
		initializeMetadata();
	}

	@Override
	public String[] getMetadata() {
	
		return new String[]{
				//  0----------1----------2----------3----------4
					"Type",  "IBUS",   "NAME",    "BASEKV", "MachId",
				//  5----------6----------7----------8----------9
					"MVA",  "T'do",   "T''do",    "T''qo",    "H", 
				//  10----------11----------12--------13--------14	
					 "D",      "Xd",    "Xq",       "X'd",  "X''d",
				//  15----------16-------17---------18---------19----
                   "Xl",     "S(1.0)",   "S(1.2)",  "Ra",    "Rcomp",
				//  20--------21--------22---------23--------------
					"Xcomp"
				   
			};
		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		
		boolean hasMVAData = lineStr.contains("mva=");
		
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

			else if(i>=7){
				
				if(strAry[i].contains("\"")){
					// just skip items like "tpdo"
				
				}
				
			    else{
			    	
			    	if(i==7){
					    	if(hasMVAData){
								  if(strAry[i].contains("mva=")){
									String mvaString = strAry[i].substring(4, strAry[i].length()); 
									setValue(k++,mvaString);
								 }
							  }
					    	 else{
					    		 // since MVA is in the meta data part, even it is not provided, k index need to be updated
					    		 //setValue(k++,"-999"); //use -999 to denote that MVA is not provided;
					    		 k++;
					    		 // the data corresponding to i=7 becomes the parameter next to MVA.
					    		 setValue(k++, strAry[i].trim());
					    	 }
					    	
					}
			    	
			    	else setValue(k++, strAry[i].trim());
			    }
				
				
			} //end of i>=7
		}
		
	}
         
}
