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

import org.ieee.odm.common.ODMException;

/**
 *  A generic class for holding {position, name, value} date structure for implementing
 *  ODM parser. The key is to decouple input data field position in input data file
 *  and input data field of type String parsing/mapping logic.
 *   
 * @author mzhou
 */
public class BaseInputLineStringParser extends BaseInputRowParser<String> {

	/**
	 * Get field of type double by the key
	 * 
	 * @param key data field key
	 * @return field of type double
	 * @throws ODMException throw exception if the field does not exist
	 */
	public double getDouble(String key) throws ODMException {
		return Double.valueOf(this.getValue(key));
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
		if (exist(key) && !this.getValue(key).equals(""))
			return Double.valueOf(this.getValue(key));
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
		return Double.valueOf(this.getValue(key)).intValue();
		
	}
	
	/**
	 * Get field of type int
	 * 
	 * @param key
	 * @return the field of type int
	 * @throws ODMException throw exception if the field does not exist
	 */
	public int getInt(String key, int defaultValue) throws ODMException {
		if (exist(key)  && !this.getValue(key).equals(""))
			return Double.valueOf(this.getValue(key)).intValue();
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
		return Long.valueOf(this.getValue(key));
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
		if (exist(key)  && !this.getValue(key).equals(""))
			return Long.valueOf(this.getValue(key));
		else
			return defaultValue;
	}
	
	/**
	 * Get field of type float
	 * 
	 * @param key
	 * @return the field of type float
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public float getFloat(String key)throws ODMException{
		 return Float.parseFloat(getValue(key));
	}

	/**
	 * Get field of type float, if the field does not exit, return the default value
	 * 
	 * @param key
	 * @param defaultValue default value
	 * @return the field of type float
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public float getFloat(String key, float defaultValue) throws ODMException{
		 if ((exist(key)) && (!getValue(key).equals(""))) {
			 return Float.parseFloat(getValue(key));
		 }
		 return defaultValue;
	}

	/**
	 * Get field of type char
	 * 
	 * @param key
	 * @return the field of type char
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public char getChar(String key)throws ODMException{
		 return getValue(key).toCharArray()[0];
	}
	
	/**
	 * Get field of type char, if the field does not exit, return the default value
	 * 
	 * @param key
	 * @param defaultValue default value
	 * @return the field of type char
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public char getChar(String key, char defaultValue) throws ODMException{
		 if ((exist(key)) && (!getValue(key).equals(""))) {
			 return getValue(key).toCharArray()[0];
		 }
		 return defaultValue;
	}
	
	/**
	 * Get field of type char
	 * 
	 * @param key
	 * @return the field of type char
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public char[] getChars(String key)throws ODMException{
		 return getValue(key).toCharArray();
	}
	
	/**
	 * Get field of type char, if the field does not exit, return the default value
	 * 
	 * @param key
	 * @param defaultValue default value
	 * @return the field of type char
	 * @throws ODMException throw exception if the field does not exist
	 */	
	public char[] getChars(String key, char[] defaultValue) throws ODMException{
		 if ((exist(key)) && (!getValue(key).equals(""))) {
			 return getValue(key).toCharArray();
		 }
		 return defaultValue;
	}
} 
