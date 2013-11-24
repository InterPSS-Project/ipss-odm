/*
 * @(#)IODMModelParser.java   
 *
 * Copyright (C) 2006-2008 www.interpss.org
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
 * @Date 02/01/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model;

import java.io.File;
import java.io.InputStream;

import org.ieee.odm.schema.ModifyRecordXmlType;
import org.ieee.odm.schema.StudyCaseXmlType;
import org.ieee.odm.schema.StudyScenarioXmlType;

/**
 * Xml parser interface for representing the IEEE DOM schema. 
 */
public interface IODMModelParser {
	/** default text file encoding */
	final static String DefaultEncoding = "UTF-8";
	/** Chinese text file encoding */
	final static String ChineseEncoding = "GB18030";
	
	/**
	 * Bus pre-fix, default value "Bus", pre-fix added to the bus number to create Bus Id
	 */
	public static final String BusIdPreFix = "Bus";
	
	/**
	 * parse the file and create an ODM model object
	 * 
	 * @param xmlFile
	 * @return false if there is parsing error
	 */
	boolean parse(File xmlFile);
	
	/**
	 * parse the string representing an input file and create an ODM model object
	 * 
	 * @param xmlString string representing an input xml file
	 * @return false if there is parsing error
	 */
	boolean parse(String xmlString);
	
	/**
	 * parse the InputStream representing an input file and create an ODM model object
	 * 
	 * @param in input stream
	 * @return false if there is parsing error
	 */
	boolean parse(InputStream in);
	
	/**
	 * check if the network is of type Transmission and Loadflow analysis type
	 * 
	 * @return
	 */
	boolean isTransmissionLoadflow();
	
	/**
	 * get the ODM xml document root StudyCase element
	 * 
	 * @return
	 */
	StudyCaseXmlType getStudyCase();
	
	/**
	 * get the first modification record in the ODM xml document
	 * 
	 * @return
	 */
	ModifyRecordXmlType getModification();

	/**
	 * get modification record in the ODM xml document by Id
	 * 
	 * @param id modification record id
	 * @return
	 */
	ModifyRecordXmlType getModification(String id);

	/**
	 * get Study scenario element
	 * 
	 * @return
	 */
	StudyScenarioXmlType getStudyScenario();	

	/**
	 * output the content of the ODM object to an xml document
	 * 
	 * @return
	 */
	String toXmlDoc();
	
	/**
	 * If outfile = null, return the Xml doc, otherwise, write the xml doc
	 * to the file and return a file writing msg
	 * 
	 * @param outfile
	 * @return
	 */
	String toXmlDoc(String outfile);
}
