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

package org.ieee.odm.adapter.common.str;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.common.ODMTextFileReader;

/**
 * abstract class for parsing input data file string line implementation
 * 
 * @author mzhou
 *
 */
public abstract class AbstractStringDataFieldParser extends BaseInputLineStringParser {
	/**
	 * constructor
	 */
	public AbstractStringDataFieldParser() {
		super();
		this.setMetadata(getMetadata());
	}
	
	/**
	 * get input line metadata field definition
	 * 
	 * @return metadata definition
	 */
	abstract public String[] getMetadata();
	
	///////////////////////////////////////////////////////////////////
	///////////     Methods for parsing input file line(s)   //////////
    ///////////////////////////////////////////////////////////////////
	
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

	/**
	 * parse a set of lines in the input file
	 * 
	 * @param strAry line string array
	 */
	public void parseFields(final Object[] objAry) throws ODMException {
		throw new ODMException("Function not implemented");
	}
	
	///////////////////////////////////////////////////////////////////
	///////////     Methods for parsing an input file        //////////
    ///////////////////////////////////////////////////////////////////
	
	/**
	 * parse the whole file
	 * 
	 * @param din file reader object
	 * @throws ODMException
	 */
	public void parseFile(final IFileReader din)  throws ODMException {
		String str;
		do {
			str = din.readLine(); 
			if (str!=null)
				parseFields(str);
		} while (str!=null);	
	}	
	
	/**
	 * parse the whole file
	 * 
	 * @param filename file name
	 * @throws ODMException
	 */
	public void parseFile(final String filename)  throws ODMException {
		try {
			final File file = new File(filename);
			final InputStream stream = new FileInputStream(file);
			ODMLogger.getLogger().info("Parse input file and create the parser object, " + filename);
			BufferedReader din = new BufferedReader(new InputStreamReader(stream));
			IFileReader reader = new ODMTextFileReader(din);
			parseFile(reader);
			stream.close();
		} catch (IOException e) {
			throw new ODMException(e.toString());
		}			
	}
}