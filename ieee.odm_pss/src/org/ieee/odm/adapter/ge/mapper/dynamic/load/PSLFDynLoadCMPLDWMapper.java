package org.ieee.odm.adapter.ge.mapper.dynamic.load;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.adapter.BaseDataMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.DynamicLoadCMPLDWXmlType;
import org.ieee.odm.schema.DynamicLoadModelSelectionXmlType;
import org.ieee.odm.schema.LoadCharacteristicLocationEnumType;
import org.ieee.odm.schema.LoadCharacteristicTypeEnumType;

public class PSLFDynLoadCMPLDWMapper  extends BaseDataMapper{
	
	public PSLFDynLoadCMPLDWMapper(AbstractDataFieldParser parser){
		this.dataParser = parser;
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
	
	/*
	 *                 //  0----------1----------2----------3----------4
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
							 "Frst" ,  "Vrst"  ,  "Trst"  ,   "fuvr"  ,  "vtr1"  ,
							 "ttr1" , "vtr2"  , "ttr2",    "Vc1off" ,  "Vc2off", 
							 "Vc1on", "Vc2on"  ,  "Tth"  , "Th1t"  ,  "Th2t"  ,
							  "Tv"
	 */
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String loadId = dataParser.getString("Id");
	    
	    //check model type
	   
	    if(!(dataParser.getString("Type").equals("CMPLDW"))){
	    	throw new ODMException("dyn load  : Id"+
		             loadId+" @ Bus"+i+"is not a CMPLDW load model");
	    }
	    
	   
	    
	   DStabBusXmlType busXml = parser.getBus(busId);
	   if(busXml!=null){ 
	        DStabLoadDataXmlType dstabloadData = DStabParserHelper.getDStabLoad(busXml, loadId);
	        
	        if(dstabloadData!= null){
	        	
	        	dstabloadData.setLocation(LoadCharacteristicLocationEnumType.AT_BUS);
	        	dstabloadData.setLoadXmlType(LoadCharacteristicTypeEnumType.WECC_COMPOSITE_LOAD);
	        	
//	        	DynamicLoadModelSelectionXmlType loadModel = dstabloadData.getLoadModel();
//	        	if(loadModel ==null){
//	        		loadModel = new DynamicLoadModelSelectionXmlType();
//	        		dstabloadData.setLoadModel(loadModel);
//	        	}
	        	
	        	DynamicLoadCMPLDWXmlType  cmpldw = DStabParserHelper.createDStabLoadCMPLDW(dstabloadData);
	        	//loadModel.setCMPLDWLoad(cmpldw);
	        	
	        	
	        	
	        	double mva = dataParser.getDouble("MVA");
	        	
	        	double bss = dataParser.getDouble("Bss");   
	        	double Rfdr = dataParser.getDouble("Rfdr");
	        	double Xfdr = dataParser.getDouble("Xfdr");
	        	double Fb = dataParser.getDouble("Fb");
	        	
	        	double Xxf =  dataParser.getDouble("Xxf");
	        	int LTC =  dataParser.getInt("LTC");
	        	double Tmin =  dataParser.getDouble("Tmin");
	        	double Tmax =  dataParser.getDouble("Tmax");
	        	double step =  dataParser.getDouble("step");
	        	double Tfixls =  dataParser.getDouble("Tfixls");
	        	double Tfixhs =  dataParser.getDouble("Tfixhs");
	        	double Vmin =  dataParser.getDouble("Vmin");
	        	double Vmax =  dataParser.getDouble("Vmax");
	        	double Tdel =  dataParser.getDouble("Tdel");
	        	double Ttap =  dataParser.getDouble("Ttap");
	        	
	        	double FmA = dataParser.getDouble("FmA");
	        	double FmB = dataParser.getDouble("FmB");
	        	double FmC = dataParser.getDouble("FmC");
	        	double FmD = dataParser.getDouble("FmD");
	        	double Fel = dataParser.getDouble("Fel");
	        	
	        	
	        	int MtypA = dataParser.getInt("MtypA");
	        	int MtypB = dataParser.getInt("MtypB");
	        	int MtypC = dataParser.getInt("MtypC");
	        	int MtypD = dataParser.getInt("MtypD");
					
	        	double LfmA = dataParser.getDouble("LfmA");
	        	double RsA = dataParser.getDouble("RsA");
	        	double LsA = dataParser.getDouble("LsA");
	        	double LpA = dataParser.getDouble("LpA");
	        	double LppA = dataParser.getDouble("LppA");
	        	double TpoA = dataParser.getDouble("TpoA");
	        	double TppoA = dataParser.getDouble("TppoA");
	        	double HA = dataParser.getDouble("HA");
	        	double EtrqA = dataParser.getDouble("EtrqA");
	        	double Vtr1A = dataParser.getDouble("Vtr1A");
	        	double Ttr1A = dataParser.getDouble("Ttr1A");
	        	double Ftr1A = dataParser.getDouble("Ftr1A");
	        	double Vrc1A = dataParser.getDouble("Vrc1A");
	        	double Trc1A = dataParser.getDouble("Trc1A");
	        	double Vtr2A = dataParser.getDouble("Vtr2A");
	        	double Ttr2A = dataParser.getDouble("Ttr2A");
	        	double Ftr2A = dataParser.getDouble("Ftr2A");
	        	double Vrc2A = dataParser.getDouble("Vrc2A");
	        	double Trc2A = dataParser.getDouble("Trc2A");
	        	
				
	        	double LfmB = dataParser.getDouble("LfmB");
	        	double RsB = dataParser.getDouble("RsA");
	        	double LsB = dataParser.getDouble("LsA");
	        	double LpB = dataParser.getDouble("LpB");
	        	double LppB = dataParser.getDouble("LppB");
	        	double TpoB = dataParser.getDouble("TpoB");
	        	double TppoB = dataParser.getDouble("TppoB");
	        	double HB = dataParser.getDouble("HB");
	        	double EtrqB = dataParser.getDouble("EtrqB");
	        	double Vtr1B = dataParser.getDouble("Vtr1B");
	        	double Ttr1B = dataParser.getDouble("Ttr1B");
	        	double Ftr1B = dataParser.getDouble("Ftr1B");
	        	double Vrc1B = dataParser.getDouble("Vrc1B");
	        	double Trc1B = dataParser.getDouble("Trc1A");
	        	double Vtr2B = dataParser.getDouble("Vtr2B");
	        	double Ttr2B = dataParser.getDouble("Ttr2B");
	        	double Ftr2B = dataParser.getDouble("Ftr2B");
	        	double Vrc2B = dataParser.getDouble("Vrc2B");
	        	double Trc2B = dataParser.getDouble("Trc2B");
	        	
	        	
	        	double LfmC = dataParser.getDouble("LfmC");
	        	double RsC = dataParser.getDouble("RsC");
	        	double LsC = dataParser.getDouble("LsC");
	        	double LpC = dataParser.getDouble("LpC");
	        	double LppC = dataParser.getDouble("LppC");
	        	double TpoC = dataParser.getDouble("TpoC");
	        	double TppoC = dataParser.getDouble("TppoC");
	        	double HC = dataParser.getDouble("HC");
	        	double EtrqC = dataParser.getDouble("EtrqC");
	        	double Vtr1C = dataParser.getDouble("Vtr1C");
	        	double Ttr1C = dataParser.getDouble("Ttr1C");
	        	double Ftr1C = dataParser.getDouble("Ftr1C");
	        	double Vrc1C = dataParser.getDouble("Vrc1C");
	        	double Trc1C = dataParser.getDouble("Trc1C");
	        	double Vtr2C = dataParser.getDouble("Vtr2C");
	        	double Ttr2C = dataParser.getDouble("Ttr2C");
	        	double Ftr2C = dataParser.getDouble("Ftr2C");
	        	double Vrc2C = dataParser.getDouble("Vrc2C");
	        	double Trc2C = dataParser.getDouble("Trc2C");
			
	        	
	        	double LfmD = dataParser.getDouble("LfmD");
	        	double comPf = dataParser.getDouble("CompPF");
	        	double vstall = dataParser.getDouble("Vstall");
	        	double rstall = dataParser.getDouble("Rstall");
	        	double xstall = dataParser.getDouble("Xstall"); 
	        	double tstall = dataParser.getDouble("Tstall");
	        	double Frst = dataParser.getDouble("Frst");
	        	
	        	double Vrst = dataParser.getDouble("Vrst");
	        	double Trst = dataParser.getDouble("Trst");
	        	double Fuvr = dataParser.getDouble("Fuvr");
	        	double Vtr1 = dataParser.getDouble("Vtr1");
	        	double Ttr1 = dataParser.getDouble("Ttr1");
	        	double Vtr2 = dataParser.getDouble("Vtr2");
	        	double Ttr2 = dataParser.getDouble("Ttr2");
	        	double Vc1off = dataParser.getDouble("Vc1off");
	        	double Vc2off = dataParser.getDouble("Vc2off");
	        	double Vc1on = dataParser.getDouble("Vc1on");
	        	double Vc2on = dataParser.getDouble("Vc2on");
	        	double Tth = dataParser.getDouble("Tth");
	        	double Th1t = dataParser.getDouble("Th1t");
	        	double Th2t = dataParser.getDouble("Th2t");
	        	double Tv = dataParser.getDouble("Tv");
	        	
	        	
	        	cmpldw.setMva(mva);
	        	cmpldw.setBss(bss);
	        	cmpldw.setRfdr(Rfdr);
	        	cmpldw.setXfdr(Xfdr);
	        	cmpldw.setFb(Fb);
	        	cmpldw.setXxf(Xxf);
	        	
	        	/*
	        	 * int LTC =  dataParser.getInt("LTC");
	        	double Tmin =  dataParser.getDouble("Tmin");
	        	double Tmax =  dataParser.getDouble("Tmax");
	        	double step =  dataParser.getDouble("step");
	        	double Tfixls =  dataParser.getDouble("Tfixls");
	        	double Tfixhs =  dataParser.getDouble("Tfixhs");
	        	double Vmin =  dataParser.getDouble("Vmin");
	        	double Vmax =  dataParser.getDouble("Vmax");
	        	 */
	        	cmpldw.setLTC(LTC);
	        	cmpldw.setTmin(Tmin);
	        	cmpldw.setTmax(Tmax);
	        	cmpldw.setStep(step);
	        	cmpldw.setTfixLS(Tfixls);
	        	cmpldw.setTfixHS(Tfixhs);
	        	cmpldw.setVmin(Vmin);
	        	cmpldw.setVmax(Vmax);
	        	cmpldw.setTdel(Tdel);
	        	cmpldw.setTtap(Ttap);
	        	cmpldw.setRcomp(0.0);
	        	cmpldw.setXcomp(0.0);
	        	
	        	cmpldw.setFma(FmA);
	        	cmpldw.setFmb(FmB);
	        	cmpldw.setFmc(FmC);
	        	cmpldw.setFmd(FmD);
	        	cmpldw.setFel(Fel);
	        	
	        	
	        	cmpldw.setMtpA(MtypA);
	        	cmpldw.setMtpB(MtypB);
	        	cmpldw.setMtpC(MtypC);
	        	cmpldw.setMtpD(MtypD);
	        	
	        	
	        	// THREE-PHASE MOTOR A
	        	cmpldw.setLfmA(LfmA);
	        	cmpldw.setRsA(RsA);
	        	cmpldw.setLsA(LsA);
	        	cmpldw.setLpA(LpA);
	        	cmpldw.setLppA(LppA);
	        	cmpldw.setTpoA(TpoA);
	        	cmpldw.setTppoA(TppoA);
	        	cmpldw.setHA(HA);
	        	cmpldw.setEtrqA(EtrqA);
	        	cmpldw.setVtr1A(Vtr1A);
	        	cmpldw.setTtr1A(Ttr1A);
	        	cmpldw.setFtr1A(Ftr1A);
	        	cmpldw.setVrc1A(Vrc1A);
	        	cmpldw.setTrc1A(Trc1A);
	        	cmpldw.setVtr2A(Vtr2A);
	        	cmpldw.setTtr2A(Ttr2A);
	        	cmpldw.setFtr2A(Ftr2A);
	        	cmpldw.setVrc2A(Vrc2A);
	        	cmpldw.setTrc2A(Trc2A);
	        	
	        	
	        	// THREE-PHASE MOTOR B
	        	
	        	cmpldw.setLfmB(LfmB);
	        	cmpldw.setRsB(RsB);
	        	cmpldw.setLsB(LsB);
	        	cmpldw.setLpB(LpB);
	        	cmpldw.setLppB(LppB);
	        	cmpldw.setTpoB(TpoB);
	        	cmpldw.setTppoB(TppoB);
	        	cmpldw.setHB(HB);
	        	cmpldw.setEtrqB(EtrqB);
	        	cmpldw.setVtr1B(Vtr1B);
	        	cmpldw.setTtr1B(Ttr1B);
	        	cmpldw.setFtr1B(Ftr1B);
	        	cmpldw.setVrc1B(Vrc1B);
	        	cmpldw.setTrc1B(Trc1B);
	        	cmpldw.setVtr2B(Vtr2B);
	        	cmpldw.setTtr2B(Ttr2B);
	        	cmpldw.setFtr2B(Ftr2B);
	        	cmpldw.setVrc2B(Vrc2B);
	        	cmpldw.setTrc2B(Trc2B);
	        	
	        	
	        	
                // THREE-PHASE MOTOR C
	        	
	        	cmpldw.setLfmC(LfmC);
	        	cmpldw.setRsC(RsC);
	        	cmpldw.setLsC(LsC);
	        	cmpldw.setLpC(LpC);
	        	cmpldw.setLppC(LppC);
	        	cmpldw.setTpoC(TpoC);
	        	cmpldw.setTppoC(TppoC);
	        	cmpldw.setHC(HC);
	        	cmpldw.setEtrqC(EtrqC);
	        	cmpldw.setVtr1C(Vtr1C);
	        	cmpldw.setTtr1C(Ttr1C);
	        	cmpldw.setFtr1C(Ftr1C);
	        	cmpldw.setVrc1C(Vrc1C);
	        	cmpldw.setTrc1C(Trc1C);
	        	cmpldw.setVtr2C(Vtr2C);
	        	cmpldw.setTtr2C(Ttr2C);
	        	cmpldw.setFtr2C(Ftr2C);
	        	cmpldw.setVrc2C(Vrc2C);
	        	cmpldw.setTrc2C(Trc2C);
	        	
	        	
	        	
	        	cmpldw.setLfmD(LfmD);
	        	cmpldw.setCompPF( comPf);
	        	cmpldw.setVstall( vstall);
	        	cmpldw.setRstall( rstall);
	    
	        	cmpldw.setXstall( xstall);
	        	cmpldw.setTstall( rstall);

	        	cmpldw.setFrst( Frst);
	        	cmpldw.setVrst( Vrst);
	        	cmpldw.setTrst( Trst);
	        	
	        
	        	cmpldw.setVtr1B( Vrst);
	        	cmpldw.setTrst( Trst);
	        	
	        	cmpldw.setFuvr(Fuvr);
	        	cmpldw.setVtr1(Vtr1);
	        	cmpldw.setTtr1(Ttr1);
	        	
	        	cmpldw.setVtr2(Vtr2);
	        	cmpldw.setTtr2(Ttr2);
	        	
	        	cmpldw.setVc1Off(Vc1off);
	        	cmpldw.setVc2Off(Vc2off);
	        	
	        	cmpldw.setVc1On(Vc1on);
	        	cmpldw.setVc2On(Vc2on);
	        	
	        	cmpldw.setTth(Tth);
	        	cmpldw.setTh1T(Th1t);
	        	cmpldw.setTh2T(Th2t);
	        	cmpldw.setTv(Tv);
	       

	        	
	        	
	        }
	        
	   }
		
		
	}
}
