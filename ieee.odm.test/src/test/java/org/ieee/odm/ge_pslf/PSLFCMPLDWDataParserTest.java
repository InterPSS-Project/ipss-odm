package org.ieee.odm.ge_pslf;

import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGenTypeJFDataParser;
import org.ieee.odm.adapter.ge.parser.dynamic.load.PSLFDynLoadCMPLDWDataParser;
import org.ieee.odm.common.ODMException;
import org.junit.Test;

public class PSLFCMPLDWDataParserTest {
	
	@Test
	public void testCase05162011() throws Exception {
		String s = "cmpldw     54003  \"COLINTO9\"  138  \"99\"  :  #1   mva=-1 "+
				 "\"Bss\"   0  \"Rfdr\"  0.04  \"Xfdr\"  0.04  \"Fb\"  0.75 " +
				 "\"Xxf\"   0.08  \"TfixHS\"  1  \"TfixLS\"  1  \"LTC\"  1  \"Tmin\"  0.9  \"Tmax\"  1.1  \"step\"  0.00625 " +
				 "\"Vmin\"   1.025  \"Vmax\"  1.04  \"Tdel\"  30  \"Ttap\"  5  \"Rcomp\"  0  \"Xcomp\"  0  "+
				 "\"Fma\"   0.167  \"Fmb\"  0.135  \"Fmc\"  0.061  \"Fmd\"  0.113  \"Fel\"  0.173  "+
				 "\"PFel\"   1  \"Vd1\"  0.7  \"Vd2\"  0.5  \"Frcel\"  1  "+
				 "\"Pfs\"   -0.998  \"P1e\"  2  \"P1c\"  0.566  \"P2e\"  1  \"P2c\"  0.434  \"Pfreq\"  0  "+
				 "\"Q1e\"  2  \"Q1c\"  -0.5  \"Q2e\"  1  \"Q2c\"  1.5  \"Qfreq\"  -1  "+
				 "\"MtpA\"   3  \"MtpB\"  3  \"MtpC\"  3  \"MtpD\"  1 "+
				 "\"LfmA\"  0.75  \"RsA\"  0.04  \"LsA\"  1.8  \"LpA\"  0.12  \"LppA\"  0.104  "+
				 "\"TpoA\"  0.095  \"TppoA\"  0.0021  \"HA\"  0.1  \"etrqA\"  0   "+
				 "\"Vtr1A\"  0.7  \"Ttr1A\"  0.02  \"Ftr1A\"  0.2  \"Vrc1A\"  1  \"Trc1A\"  99999  "+
				 "\"Vtr2A\"  0.5  \"Ttr2A\"  0.02  \"Ftr2A\"  0.7  \"Vrc2A\"  0.7  \"Trc2A\"  0.1  "+
				 "\"LfmB\"  0.75  \"RsB\"  0.03  \"LsB\"  1.8  \"LpB\"  0.19  \"LppB\"  0.14  "+
				 "\"TpoB\"  0.2  \"TppoB\"  0.0026  \"HB\"  0.5  \"etrqB\"  2   "+
				 "\"Vtr1B\"  0.6  \"Ttr1B\"  0.02  \"Ftr1B\"  0.2  \"Vrc1B\"  0.75  \"Trc1B\"  0.05  "+
				 "\"Vtr2B\"  0.5  \"Ttr2B\"  0.02  \"Ftr2B\"  0.3  \"Vrc2B\"  0.65  \"Trc2B\"  0.05  "+
				 "\"LfmC\"  0.75  \"RsC\"  0.03  \"LsC\"  1.8  \"LpC\"  0.19  \"LppC\"  0.14  "+
				 "\"TpoC\"  0.2  \"TppoC\"  0.0026  \"HC\"  0.1  \"etrqC\"  2   "+
				 "\"Vtr1C\"  0.65  \"Ttr1C\"  0.02  \"Ftr1C\"  0.2  \"Vrc1C\"  1  \"Trc1C\"  9999  "+
				 "\"Vtr2C\"  0.5  \"Ttr2C\"  0.02  \"Ftr2C\"  0.3  \"Vrc2C\"  0.65  \"Trc2C\"  0.1  "+
				 "\"LfmD\"  1  \"CompPF\"  0.98   "+
				 "\"Vstall\"  0.5  \"Rstall\"  0.1  \"Xstall\"  0.1  \"Tstall\"  9999  \"Frst\"  0.2  \"Vrst\"  0.95  \"Trst\"  0.3  "+
				 "\"fuvr\"  0.1  \"vtr1\"  0.6  \"ttr1\"  0.02  \"vtr2\"  0  \"ttr2\"  9999  "+
				 "\"Vc1off\"  0.5  \"Vc2off\"  0.4  \"Vc1on\"  0.6  \"Vc2on\"  0.5   "+
				 "\"Tth\"  15  \"Th1t\"  0.7  \"Th2t\"  1.9  \"tv\"  0.025";
				 		
		         PSLFDynLoadCMPLDWDataParser parser = new PSLFDynLoadCMPLDWDataParser();
			    try {
					parser.parseFields(s);
				} catch (ODMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    System.out.println(parser.getFieldTable().toString());
    
	}
}
