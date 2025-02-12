package org.ieee.odm.adapter.psse.raw.parser.aclf;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E  VSC HVDC Data
 * 
 * @author mzhou
 *
 */
public class PSSEVSCHVDC2TDataRawParser extends BasePSSEDataRawParser {
	public PSSEVSCHVDC2TDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/*
		 * Line-1
		 * 'NAME', MDC, RDC, O1, F1, ... O4, F4
		 * Line-2
            IBUS,TYPE,MODE,DCSET,ACSET,ALOSS,BLOSS,MINLOSS,SMAX,IMAX,PWF,MAXQ,MINQ,REMOT,RMPCT
           Line-3
            JBUS,TYPE,MODE,DCSET,ACSET,ALOSS,BLOSS,MINLOSS,SMAX,IMAX,PWF,MAXQ,MINQ,REMOT,RMPCT
		 */
		
		return new String[] {
				   //  0-----------1-----------2-----------3-----------4
				      "NAME",     "MDC",      "RDC",       "O1",       "F1",            //LINE-1
				      
				   //  5           6           7           8           9
				      "O2",       "F2",        "O3",       "F3",      "O4",    
				  //  10           11           12          13         14   
				      "F4", 
				                   "IBUS",     "TYPE1",    "MODE1",    "DCSET1",        //LINE-2
				  //  15          16            17           18         19  
				   "ACSET1",       "ALOSS1",   "BLOSS1",  "MINLOSS1",  "SMAX1", 
				   
				 //  20          21            22           23           24  
				   "IMAX1",        "PWF1",    "MAXQ1",    "MINQ1",     "REMOT1",
				 //  25          26            27           28           29  
				   "RMPCT1",           
				                "JBUS",     "TYPE2",    "MODE2",    "DCSET2",            //LINE-3
				                
                //  30          31            32           33         34  
				   "ACSET2",       "ALOSS2",   "BLOSS2",  "MINLOSS2",  "SMAX2", 
				   
				 //  35          36            37           38          39  
				   "IMAX2",        "PWF2",    "MAXQ2",    "MINQ2",     "REMOT2",
				 //  40          
				   "RMPCT2"
				                
	   
	     };
	}
	
	@Override public void parseFields(final String[] lineStrAry) throws ODMException {
		StringTokenizer st = new StringTokenizer(lineStrAry[0], ",");
		int cnt = 0;
		while (st.hasMoreTokens())
			this.setValue(cnt++, st.nextToken().trim());

		st = new StringTokenizer(lineStrAry[1], ",");
		cnt = 11;
		while (st.hasMoreTokens())
			this.setValue(cnt++, st.nextToken().trim());
		
		st = new StringTokenizer(lineStrAry[2], ",");
		cnt = 26;
		while (st.hasMoreTokens())
			this.setValue(cnt++, st.nextToken().trim());
  	}
		

}
