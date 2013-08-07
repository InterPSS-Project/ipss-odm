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
 * interface for implementing DOM adapter
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
				AclfNet, AcscNet, DStabNet, OPFNet,
				DcSystemNet, DistributionNet, };
	
	/**
	 * parse the input string lines into a ODM model according the the ODM schema, in
	 * general this method is for AclfNet implementation
	 * 
	 * @param lines input lines
	 * @return
	 */
	boolean parseInput(String[] lines);

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
	 * parse the input file content as a string into a ODM model according the the ODM schema
	 * 
	 * @param fileContent file content
	 * @return
	 */
	boolean parseFileContent(String fileContent);
	
	/**
	 * If parsing staus = false, get error massages
	 * 
	 * @return
	 */
	String errMessage();
	
	/**
	 * get the parsed ODM model
	 * 
	 * @return
	 */
	IODMModelParser getModel();
}