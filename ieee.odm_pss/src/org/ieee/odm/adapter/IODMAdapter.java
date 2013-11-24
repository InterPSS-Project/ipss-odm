/*
 * @(#)IODMAdapter.java   
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

package org.ieee.odm.adapter;

import java.io.InputStream;

import org.ieee.odm.model.IODMModelParser;

/**
 * interface for implementing ODM adapter, which, in general, parse a input file(s),
 * in for example PSS/E format, into a ODM model parser object. 
 * 
 * @author mzhou
 *
 */
public interface IODMAdapter {
	/**
	 * Network type for implementing ODM adapter
	 * 
	 * @author mzhou
	 *
	 */
	public static enum NetType {
				AclfNet,         	// Aclf Network -> AclfModelParser
				AcscNet,         	// Acsc Network -> AcscModelParser
				DStabNet,        	// DStability Network -> DStabModelParser
				OPFNet,				// OPF Network -> OpfModelParser
				DcSystemNet, 		// DC System/Solor Network -> DcSystemModelParser
				DistributionNet, 	// Distribution Network -> DistModelParser
			};
	
	/**
	 * parse the input file into a ODM model according the the ODM schema, in
	 * general this method is for AclfNet implementation
	 * 
	 * @param filename file name
	 * @return
	 */
	boolean parseInputFile(String filename);

	/**
	 * parse the input file into a ODM model according the the ODM schema, the 
	 * base case AclfNet file should put first in the filenames array
	 * 
	 * @param filename file name
	 * @return
	 */
	boolean parseInputFile(NetType type, String[] filenames);

	/**
	 * parse the input file into a ODM model according the the ODM schema, the 
	 * base case AclfNet file should put first in the filenames array
	 * 
	 * @param filename file name
	 * @param encoding
	 * @return
	 */
	boolean parseInputFile(NetType type, String[] filenames, String encoding);

	/**
	 * parse the input file into a ODM model according the the ODM schema
	 * 
	 * @param filename file name
	 * @return
	 */
	boolean parseInputStream(InputStream input);

	/**
	 * parse the input stream into a ODM parser object
	 * 
	 * @param stream input stream
	 * @param encoding input text file encoding
	 * @return false if there is parsing error
	 */
	boolean parseInputStream(InputStream stream, String encoding);
	
	/**
	 * Parse the input file, which is stored as String[], into a ODM model according the the ODM schema, in
	 * general this method is for AclfNet implementation
	 * 
	 * @param lines input file stored as String[]
	 * @return
	 */
	boolean parseInput(String[] lines);
	
	/**
	 * parse the input file, which is stored as a String delimited by "\n", into a ODM model 
	 * according the the ODM schema
	 * 
	 * @param fileContent file content
	 * @return
	 */
	boolean parseFileContent(String fileContent);
	
	/**
	 * parse the input fileContent into a ODM parser object
	 * 
	 * @param fileContent
	 * @param encoding
	 * @return
	 */
	boolean parseFileContent(String fileContent, String encoding);
	
	/**
	 * If parsing status = false, get error massages generated during the model parsing.
	 * 
	 * @return
	 */
	String errMessage();
	
	/**
	 * log error message 
	 * 
	 * @param msg
	 */
	void logErr(String msg);	
	
	/**
	 * get the parsed ODM model
	 * 
	 * @return
	 */
	IODMModelParser getModel();
}