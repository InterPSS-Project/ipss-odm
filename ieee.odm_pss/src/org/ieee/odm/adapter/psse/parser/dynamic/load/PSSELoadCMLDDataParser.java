package org.ieee.odm.adapter.psse.parser.dynamic.load;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.BasePSSEDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSELoadCMLDDataParser extends BasePSSEDataParser {
	public PSSELoadCMLDDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
	
		/*
		 * I, 'USRLOD', LID, 'CMLDxxU2', 12, IT, 2, 133, 27, 146, 48, 0, 0,
           CON(J) to CON(J+132) /
		 */
		return new String[]{
		 // Header
		 //  0----------1----------2----------3----------4
				
		 "IBUS", "LID", "ModelName", "IT",
		 
		 // model parameter
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
			"Vd2",     "PFs",     "P1e",       "P1c",    
		
		//  35--------36--------37---------38-----------39
			"P2e",    "P2c",  "Pfrq",    "Q1e",       "Q1c",
		//  40--------41--------42--------43-----------44	
			"Q2e",    "Q2c",   "Qfrq",       
		
			
			//  45--------46--------47--------48-----------49 	
			"MtypA",  "LfmA",    "RsA"  , "LsA"  , 
			"LpA"  ,  "LppA"  , "TpoA",  "TppoA",  "HA"  ,  
			 "EtrqA" , "Vtr1A", "Ttr1A" , "Ftr1A" ,  "Vrc1A" , 
			 "Trc1A" ,  "Vtr2A",  "Ttr2A",  "Ftr2A",  "Vrc2A" , 
			 "Trc2A" ,  
			 
			 "MtypB", "LfmB" , "RsB",     "LsB",    "LpB", 
			 "LppB",    "TpoB" , "TppoB" ,  "HB"  ,  "EtrqB", 
			 "Vtr1B" , "Ttr1B" , "Ftr1B" ,  "Vrc1B"  ,  "Trc1B"  ,
			 "Vtr2B" , "Ttr2B" , "Ftr2B"  ,  "Vrc2B" ,  "Trc2B" ,
			 
			 "MtypC",   "LfmC"  ,  "RsC"  ,  "LsC"  ,   "LpC"  ,    "LppC",
			 "TpoC"  ,  "TppoC"  ,  "HC"  ,  "EtrqC" , "Vtr1C" ,  
			 "Ttr1C"  ,  "Ftr1C"  ,  "Vrc1C"  ,  "Trc1C"  , "Vtr2C" ,
			 "Ttr2C"  ,  "Ftr2C"  ,  "Vrc2C"  ,  "Trc2C"  , 
			 
			 "Tstall", "Trst", "Tv","Tf",
			 "LfmD", "CompPF" , "Vstall" , "Rstall" , "Xstall" ,  
			 "LFadj",  "Kp1",      "Np1",      "Kq1",      "Nq1",     "Kp2",   "Np2",   "Kq2",   "Nq2",         "Vbrk",
			 "Frst" ,  "Vrst"  , "CmpKpf",   "CmpKqf",   
			 "Vc1off" ,  "Vc2off", "Vc1on", "Vc2on"  ,  
			 "Tth"  , "Th1t" ,  "Th2t", "Fuvr"  ,  "Vtr1"  , "Ttr1" ,
			  "Vtr2"  , "Ttr2", 
			  // Power electronic load
			  "Frcel"
			  
		};
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
		
		/*
		 * I, 'USRLOD', LID, 'CMLDxxU2', 12, IT, 2, 133, 27, 146, 48, 0, 0,
           CON(J) to CON(J+132) /
		 */
		
		this.clearNVPairTableData();
		// split the line string by multi-blanks while treating contents within quotes as a single entities
		String[] strAry= null;
		if(lineStr.contains(","))
			strAry=lineStr.split("\\s*(\\s|,)\\s*");
		else
		  strAry=lineStr.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		
		int cnt =strAry.length;
		int idx = 0;
		for (int i = 0; i <cnt ; i++){
			if(i ==1 || i ==4|| (i>5 && i<13)){
				//skip these info only data
			}
			else{
				if(i==3){//model type, need to trim the quote
					setValue(idx,ODMModelStringUtil.trimQuote(strAry[i].trim()));
				}
				else 
					setValue(idx, strAry[i].trim());
				
				idx++;
			}
			
		}
		
	}

}
