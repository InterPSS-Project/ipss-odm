package org.ieee.odm.adapter.ge.parser.dynamic.load;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSLFDynLoadCMPLDWDataParser extends AbstractDataFieldParser {

			@Override
			public String[] getMetadata() {
			
				return new String[]{
						//  0----------1----------2----------3----------4
							"Type",  "IBUS",   "NAME",    "BASEKV", "Id",
						//  5----------6----------7----------8----------9
							"MVA",  "Bss",   "Rfdr",    "Xfdr",  "Fb", 
						//  10----------11----------12--------13--------14	
							 "Xxf",   "Tfixhs",   "Tfixls",  "LTC",   "Tmin",  
						//  15----------16-------17---------18---------19----
							"Tmax",   "step",   "Vmin",     "Vmax", "Tdel",  
						//  20--------21--------22---------23-----------24-
							"Ttap",  "Rcomp",  "Xcomp",   "FmA",     "FmB",
						//  25--------26--------27---------28-----------29-
							"FmC",   "FmD",	  "Fel",      "PFel",      "Vd1",
						//  30--------31--------32---------33-----------34
							"Vd2",    "frcel", "PFs",     "P1e",       "P1c",    
 						
						//  35--------36--------37---------38-----------39
							"P2e",    "P2c",  "Pfrq",    "Q1e",       "Q1c",
						//  40--------41--------42--------43-----------44	
							"Q2e",    "Q2c",   "Qfrq",    "MtypA",    "MtypB",
						//  45--------46--------47--------48-----------49 	
							"MtypC",  "MtypD",  "LfmA",    "RsA"  , "LsA"  , 
						//
							"LpA"  ,  "LppA"  , "TpoA",  "TppoA",  "HA"  ,  
							 "EtrqA" , "Vtr1A", "Ttr1A" , "Ftr1A" ,  "Vrc1A" , 
							 "Trc1A" ,  "Vtr2A",  "Ttr2A",  "Ftr2A",  "Vrc2A" , 
							 "Trc2A" ,  "LfmB" , "RsB",     "LsB",    "LpB", 
							 "LppB",    "TpoB" , "TppoB" ,  "HB"  ,  "EtrqB", 
							 "Vtr1B" , "Ttr1B" , "Ftr1B" ,  "Vrc1B"  ,  "Trc1B"  ,
							 "Vtr2B" , "Ttr2B" , "Ftr2B"  ,  "Vrc2B" ,  "Trc2B" ,
							 "LfmC"  ,  "RsC"  ,  "LsC"  ,   "LpC"  ,    "LppC",
							 "TpoC"  ,  "TppoC"  ,  "HC"  ,  "EtrqC" , "Vtr1C" ,  
							 "Ttr1C"  ,  "Ftr1C"  ,  "Vrc1C"  ,  "Trc1C"  , "Vtr2C" ,
							 "Ttr2C"  ,  "Ftr2C"  ,  "Vrc2C"  ,  "Trc2C"  , "LfmD"  , 
							 "CompPF" , "Vstall" , "Rstall" , "Xstall"  ,  "Tstall", 
							 "Frst" ,  "Vrst"  ,  "Trst"  ,   "Fuvr"  ,  "Vtr1"  ,
							 "Ttr1" , "Vtr2"  , "Ttr2",    "Vc1off" ,  "Vc2off", 
							 "Vc1on", "Vc2on"  ,  "Tth"  , "Th1t"  ,  "Th2t"  ,
							  "Tv"
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
			     }
		    }
		}

}
