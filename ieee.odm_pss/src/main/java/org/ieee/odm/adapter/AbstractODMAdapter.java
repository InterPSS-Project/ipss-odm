/*
 * @(#)AbstractODMAdapter.java   
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMTextFileReader;
import org.ieee.odm.model.IODMModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * abstract base class for implementing ODM adapter 
 * 
 * @author mzhou
 *
 */
public abstract class AbstractODMAdapter implements IODMAdapter {
	/** input file parsing status */
	private boolean status;
	
	/** holds parsing error msg */
	private List<String> errMsgList;
	
	/** ODM parser object*/
	protected IODMModelParser odmParser;
	
	private static final Logger log = LoggerFactory.getLogger(AbstractODMAdapter.class);
	
	/**
	 * constructor
	 */
	public AbstractODMAdapter() {
		this.status = true;                        
		this.errMsgList = new ArrayList<String>();
	}

	@Override public String errMessage() {
		return errMsgList.toString();
	}

	@Override public IODMModelParser getModel() {
		return this.odmParser;
	}
	
	@Override public void setModelParser(IODMModelParser parser){
		this.odmParser = parser;
	}
	

	@Override public void logErr(String msg) {
		this.status = false;
		log.error(msg);
		this.errMsgList.add(msg);
	}
	
	/*
	 *  single file parsing method 
	 */
	
	@Override public boolean parseInputStream(InputStream input) {
		return parseInputStream(input, IODMModelParser.DefaultEncoding);
	}
	
	@Override public boolean parseInputStream(InputStream stream, String encoding) {
		try {
			final BufferedReader din = new BufferedReader(new InputStreamReader(stream));
			log.info("Parse input stream and create the parser object");
			try {
				this.odmParser = parseInputFile(din, encoding);
			} catch (IOException e) {
				String errorMsg = "Error parsing input file: " + e.toString();
				log.error(errorMsg);
				this.errMsgList.add(errorMsg);
				e.printStackTrace();
				return false;
			} finally { 
				din.close();
			}
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		} 
		return status;
	}
	
	@Override public boolean parseInput(String[] lines) {
		try {
			StringArrayReader reader = new StringArrayReader(lines);
			this.odmParser = parseInputFile(reader, IODMModelParser.DefaultEncoding);		
			return true;
		} catch (Exception e) {
			log.error(e.toString());
			return false;
		} 
	}

	@Override public boolean parseInputFile(String filename) {
		try {
			final File file = new File(filename);
			final InputStream stream = new FileInputStream(file);
			log.info("Parse input file and create the parser object, " + filename);
			boolean b = parseInputStream(stream);
			stream.close();
			return b;
		} catch (Exception e) {
			log.error(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	@Override public boolean parseFileContent(String fileContent) {
		return parseFileContent(fileContent, IODMModelParser.DefaultEncoding);
	}
	
	@Override public boolean parseFileContent(String fileContent, String encoding) {
		try {
			final String[] strList = fileContent.split("\n");
			log.info("Parse input fileContent and create the parser object, first line: " + strList[0]);
			this.odmParser = parseInputFile( new IFileReader() {
				private int cnt = 0;
				public String readLine() throws ODMException {
					if (cnt < strList.length)
						return strList[cnt++];
					else
						return null;
				}
			}, encoding);
		} catch (Exception e) {
			log.error(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
		return status;
	}
	
	protected IODMModelParser parseInputFile(
			final java.io.BufferedReader din, String encoding) throws Exception {
		IFileReader reader = new ODMTextFileReader(din);
		return parseInputFile(reader, encoding);
	}	

	/*
	 *  multiple files 
	 */
	
	/**
	 * parse the input file streams into a ODM parser object for multi-file scenario

	 * @param streamAry
	 * @return
	 */
	private boolean parseInputStream(IODMAdapter.NetType type, InputStream[] streamAry, String encoding) {
		try {
			final BufferedReader[] dinAry = new BufferedReader[streamAry.length];
			int cnt = 0;
			for (InputStream stream : streamAry) {
				dinAry[cnt++] = new BufferedReader(new InputStreamReader(stream));
			}
			this.odmParser = parseInputFile(type, dinAry, encoding);
		} catch (Exception e) {
			log.error(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
		return status;
	}

	@Override public boolean parseInputFile(NetType type, String[] filenames) {
		return parseInputFile(type, filenames, IODMModelParser.DefaultEncoding);
	}
	
	@Override public boolean parseInputFile(NetType type, String[] filenameAry, String encoding) {
		try {
			final InputStream[] streamAry = new InputStream[filenameAry.length];
			int cnt = 0;
			for (String filename : filenameAry) {
				final File file = new File(filename);
				final InputStream stream = new FileInputStream(file);
				log.info("Parse input file and create the parser object, " + filename);
				streamAry[cnt++] = stream;
			}
			boolean rtn = parseInputStream(type, streamAry, encoding);
			for (InputStream s : streamAry) 
				s.close();
			return rtn;
		} catch (Exception e) {
			log.error(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	protected IODMModelParser parseInputFile(IODMAdapter.NetType type,
			final java.io.BufferedReader[] dinAry, String encoding) throws Exception {
		IFileReader[] fAry = new ODMTextFileReader[dinAry.length];
		int cnt = 0;
		for (java.io.BufferedReader din: dinAry) {
			fAry[cnt++] = new ODMTextFileReader(din);
		}
		return parseInputFile(type, fAry, encoding);
	}	

	/*
	 * abstract methods to be implemented
	 */
	/**
	 * parse an input data file, wrapped as an IFileReader object
	 * 
	 * @param din input data file 
	 * @param encoding text file encoding
	 * @return ODM model parser object
	 * @throws ODMException
	 */
	abstract protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws ODMException;
	
	/**
	 * parse a set of input data files, wrapped as a set of IFileReader objects
	 * 
	 * @param type ODM model parser type
	 * @param dins input date file set
	 * @param encoding en coding
	 * @return ODM model parser object
	 * @throws ODMException
	 */
	abstract protected IODMModelParser parseInputFile(IODMAdapter.NetType type, IFileReader[] dins, String encoding) throws ODMException;
	
	/**
	 * Class for processing a set of input date lines String[] line-by-line
	 * 
	 * @author mzhou
	 *
	 */
	private class StringArrayReader implements IFileReader {
		private String[] lines = null;
		private int cnt;
		
		/**
		 * Constructor
		 * 
		 * @param lines input date lines
		 */
		public StringArrayReader(String[] lines) {
			this.lines = lines;
			this.cnt = 0;
		}
		
		/**
		 * return a string line for processing
		 */
		public String readLine() throws ODMException {
			if (this.cnt >= this.lines.length)
				return null;
			String str = this.lines[this.cnt++];
			//System.out.println(str);
			return str;
		}
	}
}