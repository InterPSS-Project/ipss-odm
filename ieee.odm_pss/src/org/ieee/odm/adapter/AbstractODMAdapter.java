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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;

/**
 * abstract base class for implementing ODM adapter 
 * 
 * @author mzhou
 *
 */
public abstract class AbstractODMAdapter implements IODMAdapter {
	private boolean status;
	private List<String> errMsgList;
	private IODMModelParser parser;
	
	/**
	 * constructor
	 */
	public AbstractODMAdapter() {
		this.status = true;                        
		this.errMsgList = new ArrayList<String>();
	}

	/**
	 * return error message as a string 
	 */
	public String errMessage() {
		return errMsgList.toString();
	}

	/**
	 * get the parser object
	 */
	public IODMModelParser getModel() {
		return this.parser;
	}

	/**
	 * log error message 
	 * 
	 * @param msg
	 */
	public void logErr(String msg) {
		this.status = false;
		ODMLogger.getLogger().severe(msg);
		this.errMsgList.add(msg);
	}
	
	/*
	 *  single files 
	 */
	
	/**
	 * parse the input stream into a ODM parser object
	 * 
	 * @param input
	 */
	public boolean parseInputStream(InputStream input) {
		return parseInputStream(input, IODMModelParser.defaultEncoding);
	}
	
	/**
	 * parse the input stream into a ODM parser object
	 * 
	 * @param stream
	 * @param encoding
	 * @return
	 */
	public boolean parseInputStream(InputStream stream, String encoding) {
		try {
			final BufferedReader din = new BufferedReader(new InputStreamReader(stream));
			ODMLogger.getLogger().info("Parse input stream and create the parser object");
			try {
				this.parser = parseInputFile(din, encoding);
			} catch (IOException e) {
				ODMLogger.getLogger().severe(e.toString());
				this.errMsgList.add(e.toString());
				e.printStackTrace();
				return false;
			} finally { 
				din.close();
			}
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			return false;
		} 
		return status;
	}
	
	/**
	 * parse the input string lines into a ODM parser object
	 * 
	 * @param lines input string lines
	 */
	public boolean parseInput(String[] lines) {
		try {
			StringArrayReader reader = new StringArrayReader(lines);
			this.parser = parseInputFile(reader, IODMModelParser.defaultEncoding);		
			return true;
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			return false;
		} 
	}

	/**
	 * parse the input file into a ODM parser object
	 * 
	 * @param filename
	 */
	public boolean parseInputFile(String filename) {
		try {
			final File file = new File(filename);
			final InputStream stream = new FileInputStream(file);
			ODMLogger.getLogger().info("Parse input file and create the parser object, " + filename);
			boolean b = parseInputStream(stream);
			stream.close();
			return b;
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * parse the input fileContent into a ODM parser object
	 * 
	 * @param fileContent
	 */
	public boolean parseFileContent(String fileContent) {
		return parseFileContent(fileContent, IODMModelParser.defaultEncoding);
	}
	
	/**
	 * parse the input fileContent into a ODM parser object
	 * 
	 * @param fileContent
	 * @param encoding
	 * @return
	 */
	public boolean parseFileContent(String fileContent, String encoding) {
		try {
			final String[] strList = fileContent.split("\n");
			ODMLogger.getLogger().info("Parse input fileContent and create the parser object, first line: " + strList[0]);
			this.parser = parseInputFile( new IFileReader() {
				private int cnt = 0;
				public String readLine() throws ODMException {
					if (cnt < strList.length)
						return strList[cnt++];
					else
						return null;
				}
			}, encoding);
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
		return status;
	}
	
	protected IODMModelParser parseInputFile(
			final java.io.BufferedReader din, String encoding) throws Exception {
		FileReader reader = new FileReader(din);
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
			this.parser = parseInputFile(type, dinAry, encoding);
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
		return status;
	}

	/**
	 * parse the input files into a ODM parser object for multi-file scenario
	 * 
	 * @param type network type
	 * @param filenames file name array
	 */
	public boolean parseInputFile(NetType type, String[] filenames) {
		return parseInputFile(type, filenames, IODMModelParser.defaultEncoding);
	}
	
	/**
	 * parse the input files into a ODM parser object for multi-file scenario
	 * 
	 * @param type network type
	 * @param filenames file name array
	 * @param encoding
	 */
	public boolean parseInputFile(NetType type, String[] filenameAry, String encoding) {
		try {
			final InputStream[] streamAry = new InputStream[filenameAry.length];
			int cnt = 0;
			for (String filename : filenameAry) {
				final File file = new File(filename);
				final InputStream stream = new FileInputStream(file);
				ODMLogger.getLogger().info("Parse input file and create the parser object, " + filename);
				streamAry[cnt++] = stream;
			}
			boolean rtn = parseInputStream(type, streamAry, encoding);
			for (InputStream s : streamAry) 
				s.close();
			return rtn;
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			this.errMsgList.add(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	protected IODMModelParser parseInputFile(IODMAdapter.NetType type,
			final java.io.BufferedReader[] dinAry, String encoding) throws Exception {
		IFileReader[] fAry = new FileReader[dinAry.length];
		int cnt = 0;
		for (java.io.BufferedReader din: dinAry) {
			fAry[cnt++] = new FileReader(din);
		}
		return parseInputFile(type, fAry, encoding);
	}	

	/*
	 * abstract methods to be implemented
	 */
	abstract protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws Exception;
	abstract protected IODMModelParser parseInputFile(IODMAdapter.NetType type, IFileReader[] din, String encoding) throws Exception;
	

	private class FileReader implements IFileReader {
		java.io.BufferedReader din = null;
		public FileReader(java.io.BufferedReader din) { this.din = din;}
		public String readLine() throws ODMException {
			try {
				return din.readLine();
				//System.out.println(str);
			} catch (IOException e) {
				throw new ODMException(e.toString());
			}
		}
	}

	private class StringArrayReader implements IFileReader {
		private String[] lines = null;
		private int cnt;
		public StringArrayReader(String[] lines) {
			this.lines = lines;
			this.cnt = 0;
		}
		public String readLine() throws ODMException {
			if (this.cnt >= this.lines.length)
				return null;
			String str = this.lines[this.cnt++];
			//System.out.println(str);
			return str;
		}
	}
}