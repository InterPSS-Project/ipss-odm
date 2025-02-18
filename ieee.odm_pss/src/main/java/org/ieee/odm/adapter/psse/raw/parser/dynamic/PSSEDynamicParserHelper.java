package org.ieee.odm.adapter.psse.raw.parser.dynamic;

public class PSSEDynamicParserHelper {
	
	public static boolean isModelDataCompleted(String lineStr){
		
		
		return lineStr.trim().lastIndexOf("/")>0;
		
	}
	
	public static void parseDynModel(String lineStr){
		
	}
	
	public static boolean skipCommentLine(String lineStr){
		//Check if the first line of multiInteger(String s) {
		if(lineStr.trim().startsWith("//"))
			return true;
		// skip a line if it is not started with a bus Number, which is an integer, 
	    try { 
	        Integer.parseInt(lineStr.split("\\s+")[0]); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	

}
