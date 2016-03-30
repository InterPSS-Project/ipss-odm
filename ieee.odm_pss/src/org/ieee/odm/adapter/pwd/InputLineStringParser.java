/*
 * @(#)InputLineStringParser.java   
 *
 * Copyright (C) 2006 www.interpss.com
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
 * @Date 09/15/2012
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.pwd;

import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.adapter.BaseInputLineStringParser;
import org.ieee.odm.common.ODMLogger;

/**
 *  It first parses the meta data definition string, and save the attributes in a LinkedHashMap, 
 *  named positionTable. Then it parses the data string according to the field definition as 
 *  name-value pairs. For example,  
 * 
	  [BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG,LineAMVA,LineBMVA,
            LineCMVA,LineShuntMW,LineShuntMW:1,LineShuntMVR,LineShuntMVR:1,LineXfmr,LineTap,
            LinePhase,SeriesCapStatus]
	    4     5 " 1" "Closed"  0.000000  0.100000  0.000000  0.000000  1000.000  1000.000  1000.000     0.000     0.000     0.000     0.000  "YES"    0.993750   0.000000 "Not Bypassed"
 * 
 * after the parsing, the nv pairs in the fieldTable will store
 * 
 *       <"BusNum","4">, <"BusNum:1","5">, ....
 * 
 * @author mzhou
 *
 */
public class InputLineStringParser extends BaseInputLineStringParser {
	//private List<String> dataListx;
	
	/**
	 * constructor
	 */
	public InputLineStringParser() {
		super();
		//this.dataList = new ArrayList<String>();
	}
	
	/**
	 * Parse metadata. The LinkedHashMap storing meta data will be renewed
	 * for each data section, therefore, make sure the input string for the 
	 * metaData is completed by itself.  
	 * 
	 * @param data
	 */
	public void parseMetadata(String data) {
		setMetadata(parseMetaData(data));
	}

	/**
	 * parse the data string. 
	 * 
	 * @param data
	 * @return true if all fields are parsed
	 */
	public boolean parseData(String data) {
		//renew the fieldTable before processing each model definition
		this.fieldTable.clear();
        int cnt=0;

		String[] sAry = parseDataFields(data);
		for (String s : sAry) {
			//System.out.print(s+", ");
			this.fieldTable.put(this.positionTable.get(cnt++), s.trim());
		}
		
		return this.positionTable.size() == this.fieldTable.size();
	}
	
	/**
	 * parse the string data. The append mode is an option, if it is set
	 * to be true, then the data will be appended to the fieldTable, while the fiedTable will
	 * not be renewed or cleared.
	 * 
	 * @param data
	 * @param appendMode
	 * @return
	 */
	public boolean parseData(String data, boolean appendMode){
		//System.out.println(data);
		if(!appendMode) 
			parseData(data);
		else{
			int cnt =this.fieldTable.size();
			String[] sAry = parseDataFields(data);
			for (String s : sAry) {
				this.fieldTable.put(this.positionTable.get(cnt++), s.trim());
			}
			//Add because the PWD output IEEE14 data detected meta data duplication issue
			if(sAry.length==this.positionTable.size()){
				if(this.fieldTable.size()!=this.positionTable.size()){
			    ODMLogger.getLogger().severe("Duplicated meta data definition detected! "
				    +"\n"+this.positionTable.toString());
				}
			}
		}
		//System.out.println("position table="+this.positionTable.size()+", fieldTable ="+this.fieldTable.size());
	    return this.positionTable.size() == this.fieldTable.size();
	}
	
	/**
	 * parse a complete data section metaData definition and return as an string array.
	 * 
	 *    [BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG,LineAMVA,LineBMVA]
	 *    
	 *    { "BusNum", "BusNum:1", "LineCircuit", "LineStatus" }
	 * 
	 * @param str
	 * @return A String array storing the metaData definition
	 */
	private String[] parseMetaData(String str){
		int indexOfLeftBracket=str.indexOf("[");
		int indexOfRightBracket=str.indexOf("]");
		String[] arguFields=str.substring(indexOfLeftBracket+1,
				indexOfRightBracket).split(",");
		return arguFields;
	}	
	
	/**
	 * Parse an input string to a string array
	 *   input string : 4     5 " 1" "Closed"  0.000000  0.100000  0.000000  0.000000
	 *   to a string array : { "4", "5", " 1", "Closed", "0.000000", "0.100000", "0.000000", "0.000000"}
	 *   
	 * @param Str
	 * @return
	 */
	public String[] parseDataFields(String Str){
	    List<String> dataList = new ArrayList<>();

		boolean quotBegin = false;

		int beginIdx =0, endIdx=0;
		
		String s=Str.trim();
		int length = s.length();
		int length_1 = length-1;
		
		//convert the input string to a char array
	    char[] charAry =s.toCharArray();

	    for( int i = 0;i<charAry.length;i++){
			//string within a quotation is processed separately 
			// and treated as a whole
			if (!(charAry[i] == '"' || charAry[i] == '\'')) {
			   // PWD uses the space to separate data by default, the consecutive non-space
			   // characters together form a string
				if (i > 0 && !quotBegin) {
					if (i <= length_1) {
						if (Character.isWhitespace(charAry[i-1])
								&& (!Character.isWhitespace(charAry[i]))) {
							beginIdx = i;
						} else if (Character.isWhitespace(charAry[i])&& 
								(!Character.isWhitespace(charAry[i-1]) && 
								 !(charAry[i-1] == '"' || charAry[i-1] == '\''))) {
							endIdx = i;
							dataList.add(s.substring(beginIdx, endIdx)); 
							//this.dataList.add(new String(charAry, beginIdx, endIdx-beginIdx));
						}
					}
					// if the processing data is the last one, since no
					// space after it, it needs to be treated specially.
					if (i == (length_1)) {
						endIdx = length;
						dataList.add(s.substring(beginIdx, endIdx));
						//this.dataList.add(new String(charAry, beginIdx, endIdx-beginIdx));
					}
				} // end if i>1 && !quotBegin
			}	   
			else{ // The following processes the string within quotations.
				if (!quotBegin){
					quotBegin = true;
				    beginIdx=i+1;
				}else{//this quotation completes a pair
					endIdx=i;
					dataList.add(s.substring(beginIdx, endIdx));
					//this.dataList.add(new String(charAry, beginIdx, endIdx-beginIdx));
					quotBegin = false;
				}
			   
		   }
		}
		//System.out.println(dataList.toString());
		return dataList.toArray(new String[1]);
	}	
} 
