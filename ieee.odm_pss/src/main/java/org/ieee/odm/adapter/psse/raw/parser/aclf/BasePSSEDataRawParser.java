/*
 * @(#)BasePSSEDataParser.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @Author Mike Zhou
 * @Version 1.0
 * @Date 04/11/2013
 * 
 *   Revision History
 *   ================
 *
 */

 package org.ieee.odm.adapter.psse.raw.parser.aclf;

 import java.util.Arrays;

 import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
 import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
 import org.ieee.odm.common.ODMException;
 import org.ieee.odm.common.ODMLogger;
 
 /**
  * Class for processing IEEE CDF bus data line string
  * 
  * @author mzhou
  *
  */
 public abstract class BasePSSEDataRawParser extends AbstractStringDataFieldParser {
        //  0----------1----------2----------3----------4
        //  5          6          7          8          9
        //  10         11         12         13         14
        //  15         16         17         18         19
        //  20         21         22         23         24
        //  25         26         27         28         29
        //  30         31         32         33         34
        //  35         36         37         38         39
        //  40         41         42         43         44
        //  45         46         47         48         49
           
     protected PsseVersion version = PsseVersion.PSSE_30;
     
     public BasePSSEDataRawParser() {
         super();
         initializeMetadata(); 
     }
 
     public BasePSSEDataRawParser(PsseVersion ver) {
         super();
         this.version = ver;
         initializeMetadata(); 
     }
     
     
     @Override public void parseFields(final String str) throws ODMException {
         
         this.clearNVPairTableData();
         
          
          
          int expectedFieldCount = getMetadata().length;
          
          // replace the following code with a uniform processing method parseLineStr(final String str, int startingIdx, int expectedFieldCount)
  /*        
          String[] splitData;
          String tempStr = str;
          if (str.contains("/")) { // remove comments after "/"
              tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
            }
 
          splitData = tempStr.split(",");
          
          
            int idx = 0;
            for (String data : splitData) {
                data = data.trim();
                if (data.contains("\'")) {
                    setValue(idx, (data.replace("'", "").trim()));
                }
                else {
                    setValue(idx, data);
                }
                idx++;
            }
            // 
            while (idx < expectedFieldCount) {
                //dataList.add(null);
                setValue(idx, "");
                idx++;
            }
            */
          
          parseLineStr(str, 0, expectedFieldCount); 
     }
     
     public void parseLineStr(final String str, int startingIdx, int expectedFieldCount) {
        // First remove comments after "/", but don't affect the "/" within numbers (like 1/2)
        String tempStr = str;
        if (str.contains("/")) {
            tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
        }
        
        // Single-pass algorithm that both detects comma-in-quotes and parses in one go
        StringBuilder currentToken = new StringBuilder();
        boolean insideQuotes = false;
        char quoteChar = 0;
        int idx = startingIdx;
        boolean hasSpecialCase = false;
        
        for (int i = 0; i <= tempStr.length(); i++) {
            // Process end of string as a special case
            char c = (i == tempStr.length()) ? ',' : tempStr.charAt(i);
            
            // Handle quotes (both single and double quotes)
            if (i < tempStr.length() && (c == '\'' || c == '"')) {
                // If we're not in quotes yet, mark that we're starting quotes
                if (!insideQuotes) {
                    insideQuotes = true;
                    quoteChar = c;
                    // Don't add the quote character to the token
                    continue;
                } 
                // If we're already in quotes and this is the matching closing quote
                else if (c == quoteChar) {
                    insideQuotes = false;
                    quoteChar = 0;
                    // Don't add the quote character to the token
                    continue;
                }
                // Otherwise it's a quote character within another quote type, treat as normal char
            }
            
            // If we hit a comma outside quotes, or end of string, complete the token
            if ((c == ',' && !insideQuotes) || i == tempStr.length()) {
                String tokenValue = currentToken.toString().trim();
                setValue(idx++, tokenValue);
                currentToken.setLength(0); // Reset for next token
                
                // Don't include the delimiter in the next token
                if (i < tempStr.length()) {
                    continue;
                }
            } 
            // If we have a comma inside quotes, mark that we've seen a special case
            else if (c == ',' && insideQuotes) {
                hasSpecialCase = true;
                currentToken.append(c);
            }
            // Normal character, add to current token
            else {
                currentToken.append(c);
            }
        }
        
        // Fill in missing data with default values
        while (idx < startingIdx + expectedFieldCount) {
            setValue(idx++, "");
        }
        
        // If we encountered the special case, log it for debugging/statistics
        if (hasSpecialCase) {
            ODMLogger.getLogger().fine("Handled special case of comma within quotes in: " + str);
        }
    }

    /**
     * Parses a line string containing only numerical values, starting from the specified index.
     * @param str The line string to parse.
     * @param startingIdx The starting index for parsing.
     * @param expectedFieldCount The expected number of fields to parse.
     */
    public void parseNumericalOnlyLineStr(final String str, int startingIdx, int expectedFieldCount) {
        // First remove comments after "/", but don't affect the "/" within numbers (like 1/2)
        String tempStr = str;
        if (str.contains("/")) {
            tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
        }
       
        String[] splitData = tempStr.split(",");

        int idx = 0;
        for (String data : splitData) {
            setValue(idx, data.trim());
            idx++;
        }
        
        // Fill in missing data with default values
        while (idx < startingIdx + expectedFieldCount) {
            setValue(idx++, "");
        }
    }

    /**
     * Parses a line string containing only numerical values, starting from the specified index.
     * @return the number of fields parsed.
     * @param str The line string to parse.
     */
    public int parseNumericalOnlyLineStr(final String str, int startingIdx) {
        // First remove comments after "/", but don't affect the "/" within numbers (like 1/2)
        String tempStr = str;
        if (str.contains("/")) {
            tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
        }
       
        String[] splitData = tempStr.split(",");

        int idx = 0;
        for (String data : splitData) {
            setValue(idx + startingIdx, data.trim());
            idx++;
        }
        
        return idx;
        
    }

    //  NOTE: this method is not used in the current implementation, but it is kept for future use.
    // @Override public void parseLineStr(final String str, int startingIdx, int expectedFieldCount) {
    //  public void parseLineStr(final String str, int startingIdx, int expectedFieldCount) {
    //       String[] splitData;
 
    //       String tempStr = str;
    //       if (str.contains("/")) { // remove comments after "/"
    //           tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
    //         }
 
    //       splitData = tempStr.split(",");
         
    //         int idx = 0;
    //         for (String data : splitData) {
    //             data = data.trim();
    //             if (data.contains("\'")) {
    //                 setValue(startingIdx+idx, data.replace("'", "").trim());
    //             }
    //             else if (data.contains("\"")) {
    //                 setValue(startingIdx+idx, data.replace("\"", "").trim());
    //             }
    //             else {
    //                 setValue(startingIdx+idx, data);
    //             }
    //             idx++;
    //         }
    //         // fill in missing data with the default values 
    //         while (idx < expectedFieldCount) {
 
    //             setValue(startingIdx+idx, "");
    //             idx++;
    //         }
    //  }
     
     public String[] convertStringAry2DTo1D(String[][] twoDArray) {
         return Arrays.stream(twoDArray)
         .flatMap(Arrays::stream)
         .toArray(String[]::new);
     }
 }