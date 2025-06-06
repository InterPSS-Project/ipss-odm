/*
 * @(#)IFileReader.java   
 *
 * Copyright (C) 2006 www.interpss.org
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.common;


/**
 * interface for implementing text/json file reader
 *  
 * @author mzhou
 *
 */
public interface IFileReader {
	/**
	 * read a line from an underlying file
	 * 
	 * @return the line, or null if end of the file
	 */
	default String readLine() throws ODMException {
		throw new ODMException("Function not implemented");
	};
	
	/**
	 * get a JSon object from an underlying file
	 * 
	 * @return the JSon object
	 */
	default <T> T getJSon(Class<T> klass) throws ODMException {
		throw new ODMException("Function not implemented");
	};
}