package org.ieee.odm.adapter.psse.raw.parser.dynamic.load;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.BasePSSEDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

public class PSSELoadACMotorDataParser extends BasePSSEDataRawParser {
	public PSSELoadACMotorDataParser(PsseVersion ver) {
		super(ver);
	}

	@Override
	public String[] getMetadata() {
       
		/*
        *                                                      Tstall, Trestart,   Tv, Tf, CompLF, CompPF, Vstall, Rstall, Xstall,LFadj, Kp1, Np1, Kq1, Nq1, Kp2,  Np2, Kq2,   Nq2, Vbrk, Frst, Vrst, CmpKpf, CmpKqf, Vc1off, Vc2off, Vc1on,Vc2on, Tth, Th1t, Th2t,Fuvr, UVtr1, Ttr1, UVtr2, Ttr2
        504, 'USRLOD', 2, 'ACMTBLU1', 12, 1, 0, 35, 9, 29, 16, 0.033,   0.40,     0.02,0.05,   0,   0.98,   0.65,   0.124,   0.114, 0.3,  0.0, 1.0, 6.0, 2.0, 12.0, 3.2, 11.0,  2.5, 0.86, 0.50, 0.80, 1.0,    -3.3,    0.45,   0.35,   0.5, 0.4,   10,  1.3,  4.30, 0.0,  0.5,  0.2,  0.90,  5.0 /
        */
		
		return new String[]{
		 // Header		
		 "IBUS", "LID", "Type",
		 
		 // model parameter
		//  0----------1----------2----------3----------4--------5--------6---------7-----------8---------9
		 "Tstall", "Trestart",  "Tv",     "Tf",     "CompLF",  "CompPF", "Vstall", "Rstall",   "Xstall", "LFadj",
		 // 10---------11----------12---------13---------14-------15-------16---------17-----------18---------19
		 "Kp1",      "Np1",      "Kq1",      "Nq1",     "Kp2",   "Np2",   "Kq2",   "Nq2",         "Vbrk",   "Frst", 
		//  20---------21----------22---------23---------24-------25-------26---------27-----------28---------29
		 "Vrst",     "CmpKpf",   "CmpKqf",   "Vc1off",  "Vc2off", "Vc1on", "Vc2on", "Tth",      "Th1t" ,    "Th2t",
		//  30---------31----------32---------33---------34-------35------
		 "Fuvr",      "UVtr1",    "Ttr1",    "UVtr2",   "Ttr2"
		};

		
	}
	
	@Override 
	public void parseFields(final String lineStr) throws ODMException {
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
			if(i ==1 || (i>3 && i<11)){
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
