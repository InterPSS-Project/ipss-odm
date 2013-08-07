/*
 * @(#)ModelStringUtil.java   
 *
 * Copyright (C) 2006-2011 www.interpss.com
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
 * @Date 02/15/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.base;

import java.text.DecimalFormat;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;

/**
 * DOM model parser string utility functions
 * 
 * @author mzhou
 *
 */
public class ModelStringUtil {
	/**
	 * form branch id based on from node id, to node id and branch circuit id 
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public static String formBranchId(String fromId, String toId, String cirId) {
		// the combination of form bus id, to bus id and cirId should be always unique
		return fromId + "_to_" + toId + "_cirId_" + cirId;
	}
	
	/**
	 * form branch id based on from node id, to node id and branch circuit id 
	 * 
	 * @param fromId
	 * @param toId
	 * @param tertId
	 * @param cirId
	 * @return
	 */
	public static String formBranchId(String fromId, String toId, String tertId, String cirId) {
		// the combination of form bus id, to bus id and cirId should be always unique
		return fromId + "_to_" + toId + "_n_" + tertId + "_cirId_" + cirId;
	}

	/**
	 * format "' 1'" to "_1" 
	 * 
	 * @param cirId
	 * @return
	 */
	public static String formatCircuitId(String cirId) {
		String s = cirId.replace(' ', '_');
		return removeSingleQuote(s);
	}
	
	/**
	 * change "'aaaa'" to "aaaa"
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSingleQuote(String str) {
		return str.substring(str.indexOf('\'')+1, str.lastIndexOf('\''));
	}
	
	/**
	 * convert charters [beginCol, endCol] of the input string to a double. Return 0.0 number if empty. 
	 * 
	 * @param str input string
	 * @param beginCol begin column, starts from 1 ...
	 * @param endCol end column
	 * @return the number
	 */
	public static double getDouble(String str, int beginCol, int endCol) {
		if (str.length() < endCol)
			return 0.0;
		String s = str.substring(beginCol-1, endCol);
		if (s.trim().equals(""))
			return 0.0;
		else
			return new Double(s.trim()).doubleValue();
	}

	/**
	 * convert str to a double. Return defaultValue number if empty. 
	 * 
	 * @param str input string
	 * @return the number
	 */
	public static double getDouble(String str, double defaultValue) {
		try {
			return new Double(str.trim()).doubleValue();
		} catch (Exception e) {	
			return defaultValue;
		}
	}

	/**
	 * convert str to an int. Return defaultValue number if empty. 
	 * 
	 * @param str input string
	 * @return the number
	 */
	public static int getInt(String str, int defaultValue) {
		try {
			return new Integer(str.trim()).intValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * convert str to a long. Return defaultValue number if empty. 
	 * 
	 * @param str input string
	 * @return the number
	 */
	public static long getLong(String str, int defaultValue) {
		try {
			return new Long(str.trim()).longValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * convert charters [beginCol, endCol] of the input string to an int. Return 0 if empty. 
	 * 
	 * @param str input string
	 * @param beginCol begin column, starts from 1 ...
	 * @param endCol end column
	 * @return the number
	 */
	public static int getInt(String str, int beginCol, int endCol) {
		if (str.length() < endCol)
			return 0;
		String s = str.substring(beginCol-1, endCol);
		if (s.trim().equals(""))
			return 0;
		else
			return new Integer(s.trim()).intValue();
	}

	/**
	 * convert charters [beginCol, endCol] of the input string to an String. Return null if empty. 
	 * 
	 * @param str input string
	 * @param beginCol begin column, starts from 1 ...
	 * @param endCol end column
	 * @return the string
	 */
	public static String getString(String str, int beginCol, int endCol) {
		if (str.length() < endCol)
			return null;
		return str.substring(beginCol-1, endCol).trim();
	}
	
	/**
	 * convert charters [beginCol, endCol] of the input string to an String. Return null if empty. 
	 * 
	 * @param str input string
	 * @param beginCol begin column, starts from 1 ...
	 * @param endCol end column
	 * @return the string
	 */
	public static String getStringReturnEmptyString(String str, int beginCol, int endCol) {
		if (str.length() <=endCol){
			if(beginCol>str.length()){
				return "";
			}else if(beginCol<=str.length()){
			    return str.substring(beginCol-1, str.length()).trim();
		     }
		}
		return str.substring(beginCol-1, endCol).trim();
	}
	
	/**
	 * format the double number
	 *  
	 * @param d
	 * @return
	 */
	public static double getNumberFormat(double d){
		DecimalFormat   format=new   DecimalFormat("###0.000000");
		String str="";
		str=format.format(d);
		double e=Double.parseDouble(str);
		return e;
	}
	
	/**
	 * remove ending /*
	 * 
	 * @param s
	 * @return
	 */
	public static String removeTailComment(String s) {
		if (s.indexOf("/*") > 0)
			return s.substring(0, s.indexOf("/*"));
		else
			return s;
	}	
	
	/**
	 * trim the blanks on both ends first, then
	 * trim "xxxx" -> xxxx
	 * 
	 * @param str
	 * @return
	 */
	public static String trimQuote(String str) {
		str=str.trim();
		return str.substring(1, str.length()-1);
	}	
	
	/**
	 * casting branch objects
	 * 
	 * @param fromBranch from branch object, it has to be an AclfBranch object
	 * @param fromType
	 * @param toType
	 * @return
	 * @throws Exception
	 */
	public static BranchXmlType casting(BranchXmlType fromBranch, String fromType, String toType, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.getNet().getBranchList().getBranch().add(BaseJaxbHelper.branch(fromBranch));
		String braStr = parser.toXmlDoc()
				.replaceAll("<"+fromType, "<"+toType)
				.replace("</"+fromType, "</"+toType);
		parser = new AclfModelParser(encoding);
		parser.parse(braStr);
		BranchXmlType toBranch = (BranchXmlType)parser.getNet().getBranchList().getBranch().get(0).getValue();
		toBranch.setFromBus(fromBranch.getFromBus());
		toBranch.setToBus(fromBranch.getToBus());
		return toBranch;
	}
		

	/**
	 * casting bus objects
	 * 
	 * @param from from bus object. It has to be AclfBus object
	 * @param fromType
	 * @param toType
	 * @return
	 * @throws Exception
	 */
	public static BusXmlType casting(BusXmlType from, String fromType, String toType, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.getNet().getBusList().getBus().add(BaseJaxbHelper.bus(from));
		String busStr = parser.toXmlDoc()
				.replaceAll("<"+fromType, "<"+toType)
				.replace("</"+fromType, "</"+toType);
		parser = new AclfModelParser(encoding);
		parser.parse(busStr);
		return (BusXmlType)parser.getNet().getBusList().getBus().get(0).getValue();
	}	


	/**
	 * a Chinese character takes up two-char-space but only counts one char, this method 
	 * replace the Chinese Character with "aa"(just arbitrary chosen here),with dealing with 
	 * operations like String.subString(int beginIdx,int endIdx);
	 * @param s
	 * @return a String with all the Chinese Character in it replaced by "aa"
	 */
	public static String replaceChineseChar(String s){
		String regEx = "[\u4e00-\u9fa5]"; 
     	String tem= s.replaceAll(regEx,"aa"); 
        return tem;
	}
	
	/**
	 * to get the Number of Chinese Characters in the input String.
	 * @param str
	 * @return the Number of Chinese Characters
	 */
	public static int getChineseCharNum(String str){
		String regEx = "[\u4e00-\u9fa5]"; 
		String tem= str.replaceAll(regEx,"aa"); 
		int n=tem.length()-str.length(); 
		return n;
	}
}
