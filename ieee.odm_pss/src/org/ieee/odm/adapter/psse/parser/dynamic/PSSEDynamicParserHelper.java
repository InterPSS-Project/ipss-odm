package org.ieee.odm.adapter.psse.parser.dynamic;

public class PSSEDynamicParserHelper {
	
	public static boolean isModelDataCompleted(String lineStr){
		
		
		return lineStr.trim().lastIndexOf("/")>0;
		
	}
	
	public static void parseDynModel(String lineStr){
		
	}

}
