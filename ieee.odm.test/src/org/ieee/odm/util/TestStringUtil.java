package org.ieee.odm.util;

import java.util.Arrays;

import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGENROUDataParser;
import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGenTypeJFDataParser;
import org.ieee.odm.common.ODMException;

public class TestStringUtil {
	
	public static void main(String[] args) {
	    //String s = "Sachin,,M,\"Maths,Science,English\",Need to improve in these subjects.";
	    String s = "Sachin M  \"Maths,Science,English\" Need to improve in these subjects.";
	    s ="genrou   32494 \"YUBA CTY\"  13.80 \"1 \" : #9 mva=55.4480   \"tpdo\" 8.0000 \"tppdo\" 0.0240 \"tpqo\" 1.0000 \"tppqo\" 0.2000 \"h\" 2.0000 \"d\" 0.0000 \"ld\" 2.5200  \"lq\" 2.0000 \"lpd\" 0.1620 \"lpq\" 0.5000 \"lppd\" 0.1550 \"ll\" 0.1000 \"s1\" 0.0936 \"s12\" 0.4860 \"ra\" 0.0013 \"rcomp\" 0.0000 \"xcomp\" 0.0000 \"accel\" 0.0000";
	    String[] splitted = s.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	    for(int i=0;i<splitted.length; i++){
	    	System.out.println(splitted[i]);
	    }
	    System.out.println(Arrays.toString(splitted));
	    
	    
	   // PSLFDynGENROUDataParser parser = new PSLFDynGENROUDataParser();
	    PSLFDynGenTypeJFDataParser parser = new PSLFDynGenTypeJFDataParser();
	    try {
			parser.parseFields(s);
		} catch (ODMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(parser.getFieldTable().toString());
	}
}
