/*
 * @(#)AbstractDataFieldParser.java   
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

package org.ieee.odm.adapter;

import org.ieee.odm.common.ODMException;

/**
 * abstract class for parsing input data file string line implementation
 * 
 * @author mzhou
 *
 */
public abstract class AbstractDataFieldParser extends BaseInputLineStringParser {
	/**
	 * constructor
	 */
	public AbstractDataFieldParser() {
		super();
		this.setMetadata(getMetadata());
	}
	
	/**
	 * get input line metadata field definition
	 * 
	 * @return metadata definition
	 */
	abstract public String[] getMetadata();
	
	/**
	 * parse a line in the input file
	 * 
	 * @param str line string
	 */
	public void parseFields(final String str) throws ODMException {
		throw new ODMException("Function not implemented");
	}

	/**
	 * parse a set of lines in the input file
	 * 
	 * @param strAry line string array
	 */
	public void parseFields(final String[] strAry) throws ODMException {
		throw new ODMException("Function not implemented");
	}
}