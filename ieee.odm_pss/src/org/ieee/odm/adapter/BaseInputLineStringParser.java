/*
 * @(#)BaseInputLineStringParser.java   
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

package org.ieee.odm.adapter;

import java.util.LinkedHashMap;

import org.ieee.odm.common.ODMException;

/**
 *  A generic class for holding {position, name, value} date structure for implementing
 *  ODM parser. The key is to decouple input data field position in input data file
 *  and input data field parsing/mapping logic.
 *   
 * @author mzhou
 */
public class BaseInputLineStringParser {
	/**
	 * store position to key lookup info { (1, BusNum), (2, BusNum:1) ... }
	 */
	protected LinkedHashMap<Integer, String> positionTable;  // 1, .... n

	/**
	 * store key to value lookup info { (BusNum, 4), (BusNum:1, 5), (BusName, Name) ... }
	 */
	protected LinkedHashMap<String, String> fieldTable;
	
	/**
	 * constructor
	 */
	public BaseInputLineStringParser() {
		this.positionTable = new LinkedHashMap<Integer, String>();
		this.fieldTable = new LinkedHashMap<String, String>();
	}
	
	/**
	 * set parser meta data. For example,
		
		String[] {
		   //  0---------------1---------------2---------------3---------------4
		     "BusNumber",  "BusName",       "Area",          "Zone",        "Type", 
		   //  5               6               7               8               9
		     "VMag",        "VAng",         "LoadP",         "LoadQ",       "GenP", 
		   //  10              11              12              13              14
		     "GenQ",       "BaseKV",        "DesiredV",     "MaxVarVolt",   "MinVarVolt", 
		   //  15              16              17              
		     "ShuntG",    "ShuntB",         "RemoteBusNumber" 
		};	 
	 * 
	 * @param dataAry meta data string array
	 */
	public void setMetadata(String[] dataAry) {
		//renew the position table for each data section
		this.positionTable.clear();
		int cnt =0;
		for (String s : dataAry) {
			this.positionTable.put(cnt++, s.trim());
		}
	}

	/**
	 * set value at the position
	 * 
	 * @param position
	 * @param value
	 */
	public void setValue(int position, String value) {
		this.fieldTable.put(this.positionTable.get(position), value);
	}

	/**
	 * set values according to its array position
	 * 
	 * @param values
	 */
	public void setValue(String[] values) {
		int pos = 0;
		for (String s : values)
			this.fieldTable.put(this.positionTable.get(pos++), s);
	}
	
	/**
	 * set value to the key
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		this.fieldTable.put(key, value);
	}
	
	/**
	 * clear the name-value pair table 
	 */
	public void clearNVPairTableData(){
		this.fieldTable.clear();
	}
	
	/**
	 * check if all data fields are parsed
	 * 
	 * @return
	 */
	public boolean isDataCompleted(){
		 return this.positionTable.size() == this.fieldTable.size();
	}

	/**
	 * check if the data field identified by the key exists
	 * 
	 * @param key
	 * @return
	 */
	public boolean exist(String key) {
		return this.fieldTable.get(key) != null;
	}
	
	/**
	 * Get field of type String by key
	 * 
	 * @param key data field key
	 * @return the data field
	 * @throws ODMException throw exception if the field does not exist
	 */
	public String getString(String key) throws ODMException {
		String field = this.fieldTable.get(key);
		if (field == null)
			throw new ODMException("Field " + key + " not found");
		return field;
	}
	
	/**
	 * Get field of type String by key 
	 * 
	 * @param key data field key
	 * @param defValue default value for field
	 * @return the data field
	 * @throws ODMException throw exception if the field does not exist
	 */
	public String getString(String key, String defValue) throws ODMException {
		String field = this.fieldTable.get(key);
		if (field == null)
			return defValue;
		return field;
	}

	/**
	 * Get field of type double by the key
	 * 
	 * @param key data field key
	 * @return field of type double
	 * @throws ODMException throw exception if the field does not exist
	 */
	public double getDouble(String key) throws ODMException {
		return Double.valueOf(this.getString(key));
	}

	/**
	 * Get field of type double, if the field does not exist, return default value
	 * 
	 * @param key
	 * @param defaultValue default value
	 * @return field of type double
	 * @throws ODMException throw exception if the field does not exist
	 */
	public double getDouble(String key, double defaultValue) throws ODMException {
		if (exist(key) && !this.getString(key).equals(""))
			return Double.valueOf(this.getString(key));
		else
			return defaultValue;
	}
	
	/**
	 * Get field of type int
	 * 
	 * @param key
	 * @return
	 * @throws ODMException throw exception if the field does not exist
	 */
	public int getInt(String key) throws ODMException {
		return Integer.valueOf(this.getString(key));
	}
	
	/**
	 * Get field of type int
	 * 
	 * @param key
	 * @return the field of type int
	 * @throws ODMException throw exception if the field does not exist
	 */
	public int getInt(String key, int defaultValue) throws ODMException {
		if (exist(key)  && !this.getString(key).equals(""))
			return Integer.valueOf(this.getString(key));
		else
			return defaultValue; 
	}

	/**
	 * Get field of type long
	 * 
	 * @param key
	 * @return the field of type long
	 * @throws ODMException throw exception if the field does not exist
	 */
	public long getLong(String key) throws ODMException {
		return Long.valueOf(this.getString(key));
	}
	
	/**
	 * Get field of type long, if the field does not exit, return the default value
	 * 
	 * @param key
	 * @param defaultValue default value
	 * @return the field of type long
	 * @throws ODMException throw exception if the field does not exist
	 */
	public long getLong(String key, long defaultValue) throws ODMException {
		if (exist(key)  && !this.getString(key).equals(""))
			return Long.valueOf(this.getString(key));
		else
			return defaultValue;
	}
	
	@Override public String toString() {
		return this.positionTable.toString() + "\n" + this.fieldTable.toString();
	}
	
	/**
	 * get the field lookup table
	 * 
	 * @return the field lookup table
	 */
	public LinkedHashMap<String, String> getFieldTable() {
		return fieldTable;
	}

	/**
	 * set the field lookup table
	 * 
	 * @param fieldTable the field lookup table object
	 */
	public void setFieldTable(LinkedHashMap<String, String> fieldTable) {
		this.fieldTable = fieldTable;
	}
} 
