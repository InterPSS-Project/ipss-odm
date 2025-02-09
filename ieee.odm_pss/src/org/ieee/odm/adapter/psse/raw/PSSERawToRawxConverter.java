package org.ieee.odm.adapter.psse.raw;



import java.io.*;
import java.util.*;
import com.google.gson.*;

public class PSSERawToRawxConverter {
    /**
     * 
     * @param rawFilePath input raw file
     * @param rawxFilePath output rawx file
     */
    public static void convert(String rawFilePath,String rawxFilePath) {
    	try (BufferedReader rawReader = new BufferedReader(new FileReader(rawFilePath));
                BufferedWriter rawxWriter = new BufferedWriter(new FileWriter(rawxFilePath))) {

    		 // Create main JSON structure
            Map<String, Object> rawxData = new LinkedHashMap<>();
            rawxData.put("general", Map.of("version", "36.1"));
            Map<String, Object> networkData = new LinkedHashMap<>();
            rawxData.put("network", networkData);

            // Section processing variables
            String currentSection = "caseid";
            List<String> fieldNames = new ArrayList<>();
            List<List<Object>> sectionData = new ArrayList<>();
            boolean expectingFields = false;
            boolean processingTransformer = false;
            boolean processingCaseId = true;
            int caseIdLineIdx = 0;
            
            int transformerLinesRead = 0;
            StringBuilder caseIdRecord = new StringBuilder();
            StringBuilder transformerRecord = new StringBuilder();
            

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
                    processingCaseId = false;
                    processingTransformer = currentSection.equals("transformer");
                    transformerLinesRead = 0;
                } else if (currentSection != null) {
                    if (processingCaseId && currentSection.equals("caseid")) {
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
                        continue;
                    }
                    if (processingTransformer) {
                        transformerRecord.append(line+" , ");
                        transformerLinesRead++;
                        if (transformerLinesRead == 4) {  // Each transformer record consists of three lines
                            sectionData.add(parseData(transformerRecord.toString(), fieldNames.size(),currentSection));
                            transformerRecord.setLength(0);
                            transformerLinesRead = 0;
                        }
                        
                    } else {
                        sectionData.add(parseData(line, fieldNames.size(),currentSection));
                    }
                }
            }

            // Convert Java map to JSON using Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //Gson gson = new GsonBuilder().create();
            String jsonOutput = gson.toJson(rawxData);

            // Write JSON to output file
            rawxWriter.write(jsonOutput);
            System.out.println("✅ Conversion from RAW to RAWX completed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            	   String name = field.toString().trim().replace("'", "").replace(" ", "").toLowerCase();
            	   if (name.equals("i") || name.equals("j") || name.equals("k")) {
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
                   fields.add(name);
                   field.setLength(0);
               } else {
                   field.append(c);
               }
           }
           fields.add(field.toString().trim().replace("'", "").toLowerCase()); // Add last field
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
   }
