package org.ieee.odm.adapter.psse.raw;



import java.io.*;
import java.util.*;
import com.google.gson.*;

public class PSSERawToRawxConverter {
	
	private  String jsonOutput = "";
	 // Create main JSON structure
    private  Map<String, Object> rawxData = new LinkedHashMap<>();
    
    private  Gson gson = null;
    
    private  final String DEFAULT_VERSION = "36.1";
    
	
    /**
     * 
     * @param rawFilePath input raw file
     * @return String in rawx json format
     */
    public String parseInputFile(String rawFilePath) {
    	try (BufferedReader rawReader = new BufferedReader(new FileReader(rawFilePath));
                ) {
    		//create gson object
    		gson = new GsonBuilder().setPrettyPrinting().create();
    		
    		TransformerProcessor transProc = new TransformerProcessor();
    		TwoTerminalDCProcessor twoTermDCProc = new TwoTerminalDCProcessor();

    		
            rawxData.put("general", Map.of("version", DEFAULT_VERSION));
            Map<String, Object> networkData = new LinkedHashMap<>();
            rawxData.put("network", networkData);

            // Section processing variables
            String currentSection = "caseid";
            List<String> fieldNames = new ArrayList<>();
            List<List<Object>> sectionData = new ArrayList<>();
            boolean expectingFields = false;
            boolean isProcessingTransformer = false;
            boolean isProcessingTwoTermDC = false;
            boolean isProcessingCaseId = true;
            int caseIdLineIdx = 0;
            
            //int transformerLinesRead = 0;
            StringBuilder caseIdRecord = new StringBuilder();
            //StringBuilder transformerRecord = new StringBuilder();
            

            String line;
            while ((line = rawReader.readLine()) != null) {
                line = line.trim();

                // Ignore empty lines and comments
                if (line.isEmpty() || line.startsWith("!")) {
                    continue;
                }
                if (line.startsWith("Q")) {
                	//end of the file
                	break;
                }

                if (line.startsWith("@!")) {
                    if (!expectingFields) {
                        fieldNames = new ArrayList<>();
                        sectionData = new ArrayList<>();
                        expectingFields = true;
                    }
               
                     fieldNames.addAll(parseFields(line.substring(2).trim(),currentSection));
                    
                } else if (line.startsWith("0 /")) {
                    addSectionToNetwork(networkData, currentSection, fieldNames, sectionData);
                    currentSection = getSectionName(line);
                    expectingFields = false;
                    isProcessingCaseId = false;
                    isProcessingTransformer = currentSection.equals("transformer");
                    isProcessingTwoTermDC = currentSection.equals("twotermdc");
                    if(isProcessingTransformer)
                    	transProc.reset();
                    
                    
              
                } else {
                    if (isProcessingCaseId && currentSection.equals("caseid")) {
                    		caseIdLineIdx = processCaseId(currentSection, fieldNames, sectionData, caseIdLineIdx,
									caseIdRecord, line);
                        
                    }
                    else if (isProcessingTransformer) {
                        transProc.processLine(line, fieldNames, sectionData);
                         
                    } 
                    else if(isProcessingTwoTermDC) {
                    	twoTermDCProc.processLine(line, fieldNames, sectionData);
                    }
                    else {
                        sectionData.add(parseData(line, fieldNames.size(),currentSection));
                    }
                }
            }

           
        } catch (IOException e) {
            e.printStackTrace();
        }
    	 
    	
        jsonOutput = gson.toJson(rawxData);
        
        return  jsonOutput;
    }

	private int processCaseId(String currentSection, List<String> fieldNames, List<List<Object>> sectionData,
			int caseIdLineIdx, StringBuilder caseIdRecord, String line) {
		if (caseIdLineIdx==0) {
			 if (line.contains("/")) {
				 line = line.replaceAll("(?<!\\d)/\\s.*", "");
		       }
			caseIdRecord.append(line+" , ");
		}
		if (caseIdLineIdx>0 && caseIdLineIdx<3) {
			if (caseIdLineIdx == 1) caseIdRecord.append("\'"+line+"\'"+" ,");
			if (caseIdLineIdx == 2) caseIdRecord.append("\'"+line+"\'");
			fieldNames.add("title"+caseIdLineIdx);
			if (caseIdLineIdx==2) {
				 sectionData.add(parseData(caseIdRecord.toString(), fieldNames.size(),currentSection));

		    }
              	   }
              	caseIdLineIdx++;
		return caseIdLineIdx;
	}
    
    /**
     * 
     * @return the Gson object 
     */
   public  Gson getGsonObject() {
	   return gson;
   }
    /**
     * 
     * @return String in PSS/E RAWX json format
     */
   public String getJson() {
    	
        //Gson gson = new GsonBuilder().create();
       jsonOutput = gson.toJson(rawxData);
        
       return  jsonOutput;

    }
    
   /**
    * Save the converted RAWX Json string to a file
    * @param rawxFilePath
    */
    public  void saveToRawxFile(String rawxFilePath) {
    	
		try {
			BufferedWriter rawxWriter = new BufferedWriter(new FileWriter(rawxFilePath));
			rawxWriter.write(getJson());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("✅ Conversion from RAW to RAWX completed successfully!");
    }
    
    public void convert(String rawFilePath,String rawxFilePath) {
    	parseInputFile(rawFilePath);
    	saveToRawxFile(rawxFilePath);
    	
    }

       /**
        * Adds a processed section to the network JSON structure.
        */
       private static void addSectionToNetwork(Map<String, Object> networkData, String sectionName, List<String> fieldNames, List<List<Object>> sectionData) {
           Map<String, Object> sectionJson = new LinkedHashMap<>();
           sectionJson.put("fields", fieldNames);
           if(sectionName.equals("caseid")) {
        	   sectionJson.put("data", sectionData.get(0));
           }
           else
        	   sectionJson.put("data", sectionData);
           networkData.put(sectionName, sectionJson);
       }

       /**
        * Parses field names from a given line, cleaning up spaces and quotes.
        */
       private static List<String> parseFields(String line, String sectionName) {
           List<String> fields = new ArrayList<>();
           boolean insideQuotes = false;
           StringBuilder field = new StringBuilder();
           
           for (char c : line.toCharArray()) {
               if (c == '\'') {
                   insideQuotes = !insideQuotes;
               } else if (c == ',' && !insideQuotes) {
            	   String name = field.toString().trim().replace("'", "").replace("\\s+", "").toLowerCase();
            	   if (name.equals("i") || name.equals("j") || name.equals("k")) {
            		       if(sectionName.equals("area")||sectionName.equals("zone")||sectionName.equals("owner")||sectionName.contains("sub"))
            		    	   name = name+sectionName;
            		       else
            		    	   name = name+"bus";
            	   }
            	   else if(name.equals("id")){
            		   if (sectionName.equals("load")) {
            			   name ="load"+name;
            		   }
            		   else if (sectionName.equals("generator")) {
            			   name ="mach"+name;
            		   }
            		   else if (sectionName.equals("fixshunt")) {
            			   name ="shnt"+name;
            		   }
            		   
            		   
            	   }
            	   else if (name.equals("r") || name.equals("x") || name.equals("b")) {
        			   name = name+"pu";
        	       }
            	   // rawx only use "_"
            	   else if(name.contains("-")){
            		   name = name.replace("-", "_");
            	   }
            	   
            	   //Note: voltagedroop section still use "status"
            	   
            	   else if (!sectionName.equalsIgnoreCase("voltagedroop") && (name.equals("st")|| name.equals("status")) ) {
            		   name = "stat";
            	   }
            	   /*
            	    *  convert transformer ratings
            	    *  RAW: RATE1-1, RATE1-2, RATE1-3, RATE1-4, RATE1-5, RATE1-6, RATE1-7, RATE1-8, RATE1-9,RATE1-10,RATE1-11,RATE1-12
            	    *  
            	    *  to 
            	    *  
            	    *  Rawx: "wdg1rate1", "wdg1rate2", "wdg1rate3", "wdg1rate4", "wdg1rate5", "wdg1rate6", "wdg1rate7", "wdg1rate8", "wdg1rate9", "wdg1rate10", "wdg1rate11", "wdg1rate12"
            	    *  
            	    *  
            	    */
            	   if (sectionName.equals("transformer") && name.startsWith("rate")) {
            		   String winding = name.substring(4,5);
            		   String ratenumber = name.substring(6);
            	        
            	        name = "wdg"+winding+"rate"+ratenumber;
            	        
            	            
            	   }
            	   else if(sectionName.equals("sysswd") && name.contains("rating")) {
            		   name ="rsetnam"; // rating set name
            	   }
            	   
                   fields.add(name);
                   field.setLength(0);
               } else {
                   field.append(c);
               }
           }
           fields.add(field.toString().trim().replace("'", "").replace("\\s+", "").toLowerCase()); // Add last field
           return fields;
       }


       /**
        * Parses data while ensuring it matches the expected field count.
        */
       private static List<Object> parseData(String line, int expectedFieldCount,String sectionName) {
           List<Object> dataList = new ArrayList<>();
           boolean insideQuotes = false;
           StringBuilder field = new StringBuilder();
           List<String> splitData = new ArrayList<>();
           
           if (line.contains("/")) { // remove comments after "/"
				 line = line.replaceAll("(?<!\\d)/\\s.*", "");
	       }

           for (char c : line.toCharArray()) {
               if (c == '\'') {
                   insideQuotes = !insideQuotes;
                   field.append(c);
               } else if (c == ',' && !insideQuotes) {
                   splitData.add(field.toString());
                   field.setLength(0);
               } else {
                   field.append(c);
               }
           }
           splitData.add(field.toString().trim()); // the last field
           
  
           
           if(sectionName.equals("caseid")) {
        	     int index = 0;
        	     for (String data : splitData) {
                     data = data.trim();
                     if(index < expectedFieldCount -2 ) {
	                     try {
	                         if (data.matches("-?\\d+\\.\\d+[eE]-?\\d+")) {
	                             dataList.add(Double.parseDouble(data));
	                         } else if (data.contains(".")) {
	                             dataList.add(Double.parseDouble(data));
	                         } else {
	                             dataList.add(Integer.parseInt(data));
	                         }
	                     } catch (NumberFormatException e) {
	                         dataList.add(data.isEmpty() ? "" : data);
	                     }
                     }
                     else {
                    	 dataList.add(data.isEmpty() ? "" : data);
                     }
                     index++;
                 }
           }
           else {
	           for (String data : splitData) {
	               data = data.trim();
	               if (data.contains("\'")) {
	            	   dataList.add(data.replace("'", "").trim());
	               }
	               else {
		               try {
		                   if (data.matches("-?\\d+\\.\\d+[eE]-?\\d+")) {
		                       dataList.add(Double.parseDouble(data));
		                   } else if (data.contains(".")) {
		                       dataList.add(Double.parseDouble(data));
		                   } else {
		                       dataList.add(Integer.parseInt(data));
		                   }
		               } catch (NumberFormatException e) {
		                   dataList.add(data.isEmpty() ? "" : data);
		               }
	               }
	           }
           }

           while (dataList.size() < expectedFieldCount) {
               dataList.add(null);
           }

           return dataList;
       }

       /**
        * Normalizes section names to match the required JSON format.
        */
       private static String getSectionName(String line) {
    	     // Convert to uppercase to handle case variations
           line = line.toLowerCase();
           
           String name = "";

           // Find index positions
           int beginIndex = line.indexOf("begin");
           int lastDataIndex = line.lastIndexOf("data");

           // Ensure both words exist in the string
           if (beginIndex != -1 && lastDataIndex != -1 && beginIndex < lastDataIndex) {
               // Extract substring and trim whitespace
               name = line.substring(beginIndex + 6, lastDataIndex).trim();
           }
           // fixed shunt
           if (name.contains("fixed")){
        	   name = "fixshunt";
           }
           else if (name.contains("voltage") && name.contains("droop")) {
        	   name = "voltagedroop";
           }
           else if (name.contains("switching") && name.contains("rating")) {
        	   name = "swdratset";
           }
           else if (name.contains("branch") ) {
        	   name = "acline";
           }
           //switching device
           else if (name.contains("system") && name.contains("switching")) {
        	   name = "sysswd";
           }
           //TWO-TERMINAL DC
           else if (name.contains("two-terminal") && name.contains("dc")) {
        	   name = "twotermdc";
           }
           //VSC DC LINE 
           else if (name.contains("vsc") && name.contains("dc")) {
        	   name = "vscdc";
           }
           //IMPEDANCE CORRECTION
           else if (name.contains("impedance")) {
        	   name = "impcor";
           }
           //MULTI-TERMINAL DC
           else if (name.contains("multi-terminal")) {
        	   name = "ntermdc";
           }
           
           //MULTI-SECTION LINE 
           else if (name.contains("multi-section")) {
        	   name = "msline";
           }
           //INTER-AREA TRANSFER
           else if (name.contains("inter-area")) {
        	   name = "iatrans";
           }
           //FACTS DEVICE
           else if (name.contains("facts")) {
        	   name = "facts";
           }
           
           //Switched Shunt
           else if (name.contains("switched")) {
        	   name = "swshunt";
           }
          
           //GNE -- automatic processing
           
           //INDUCTION MACHINE
           else if (name.contains("induction")) {
        	   name = "indmach";
           }
          
           //Load Type 
           else if (name.contains("load") && name.contains("type")) {
        	   name = "loadtype";
           }
           
           //interface -- automatic processing
           
           //INTERFACE ELEMENT
           else if (name.contains("interface") && name.contains("element")) {
        	   name = "itfelmt";
           }
           
           // Substation
           else if (name.contains("substation")) {
        	   name = "sub";
           }
           
           // Node
           else if (name.contains("node")) {
        	   name = "subnode";
           }
           
           //substation switching device
           else if (name.contains("substation") && name.contains("switching")) {
        	   name = "subswd";
           }
           //Equipment Terminal Data
           else if (name.contains("equipment")) {
        	   name = "subterm";
           }
           
           
           
           return name;  // Return empty string if not found
       }
       
       private class TransformerProcessor {
           private int transformerLinesRead = 0;
           private boolean isThreeWinding = false;
           private final List<String> transformerLines = new ArrayList<>();
           
           /* Line #1 fields":["ibus", "jbus", "kbus", "ckt", "cw", "cz", "cm","mag1", "mag2", "nmet", "name", 
           "stat", "o1", "f1", "o2", "f2", "o3", "f3", "o4", "f4", "vecgrp", "zcod"*/
           //private final int line1ExpactedValues = 22;
           
           //Line #2 R1-2, X1-2, SBASE1-2, R2-3, X2-3, SBASE2-3, R3-1, X3-1, SBASE3-1, VMSTAR, ANSTAR
           
           //private final int line2ExpactedValues = 11;
           /* Line # 4 and 5 are the same except the number
            *  "windv3", "nomv3", "ang3","wdg3rate1", "wdg3rate2", "wdg3rate3", "wdg3rate4",
           "wdg3rate5", "wdg3rate6", "wdg3rate7", "wdg3rate8",
           "wdg3rate9", "wdg3rate10", "wdg3rate11", "wdg3rate12",
           "cod3", "cont3", "node3", "rma3", "rmi3", "vma3", "vmi3",
           "ntp3", "tab3", "cr3", "cx3", "cnxa3"],
           */
           //private final int line345ExpactedValues = 27;
           private final int[] lineExpactedValues = {22, 11, 27, 27, 27};
        		   

           public void processLine(String line, List<String> fieldNames, List<List<Object>> sectionData) {
               String processedLine = line;
               transformerLinesRead++;

               // Check if it's a three-winding transformer from first line
               if (transformerLinesRead == 1) {
                   String[] firstLineData = line.trim().split("\\s*,\\s*");
                   isThreeWinding = !firstLineData[2].trim().equals("0");
               }
               
               //  pad incomplete lines with null values
               processedLine = padLine(processedLine, lineExpactedValues[transformerLinesRead-1]);
                 
               
               transformerLines.add(processedLine);

               // Process complete transformer record
               int expectedLines = isThreeWinding ? 5 : 4;
               if (transformerLinesRead == expectedLines) {
                   addCompleteTransformer(fieldNames, sectionData);
                   reset();
               }
           }

           private String padLine(String line, int expectedValues) {
               String[] parts = line.trim().split("\\s*,\\s*");
               StringBuilder result = new StringBuilder(line.trim());
               while (parts.length < expectedValues) {
                   result.append(", ");  // Empty value will be parsed as null
                   parts = result.toString().split(",");
               }
               return result.toString();
           }

           private void addCompleteTransformer(List<String> fieldNames, List<List<Object>> sectionData) {
               StringBuilder transformerRecord = new StringBuilder();
               for (int i = 0; i < transformerLines.size(); i++) {
                   transformerRecord.append(transformerLines.get(i));
                   if (i < transformerLines.size() - 1) {
                       transformerRecord.append(" , ");
                   }
               }
               sectionData.add(parseData(transformerRecord.toString(), fieldNames.size(), "transformer"));
           }

           private void reset() {
               transformerLines.clear();
               transformerLinesRead = 0;
               isThreeWinding = false;
           }
       }
       
       private class TwoTerminalDCProcessor {
           private int linesRead = 0;
           private final List<String> dcLines = new ArrayList<>();

           public void processLine(String line, List<String> fieldNames, List<List<Object>> sectionData) {
               dcLines.add(line.trim());
               linesRead++;

               // Process complete DC record (3 lines)
               if (linesRead == 3) {
                   addCompleteDCRecord(fieldNames, sectionData);
                   reset();
               }
           }

           private void addCompleteDCRecord(List<String> fieldNames, List<List<Object>> sectionData) {
               StringBuilder dcRecord = new StringBuilder();
               for (int i = 0; i < dcLines.size(); i++) {
                   dcRecord.append(dcLines.get(i));
                   if (i < dcLines.size() - 1) {
                       dcRecord.append(" , ");
                   }
               }
               sectionData.add(parseData(dcRecord.toString(), fieldNames.size(), "twotermdc"));
           }

           private void reset() {
               dcLines.clear();
               linesRead = 0;
           }
       }
   }
