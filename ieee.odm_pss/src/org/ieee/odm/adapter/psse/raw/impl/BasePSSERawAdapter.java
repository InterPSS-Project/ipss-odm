/*
 * @(#)PSSEAdapter.java   
 *
 * Copyright (C) 2006-2009 www.interpss.org
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
package org.ieee.odm.adapter.psse.raw.impl;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;

/**
 * Base ODM adapter for PSS/E input format
 * 
 * @author mzhou
 *
 */
public class BasePSSERawAdapter extends AbstractODMAdapter {
	public final static String Token_CaseDesc = "Case Description";     
	public final static String Token_CaseId = "Case ID";		

	protected PsseVersion adptrtVersion;
	protected BaseAclfModelParser<? extends LoadflowNetXmlType> parser =null;
	
	protected String  elemCntStr = "";
	
	public BasePSSERawAdapter(PsseVersion ver) {
		super();
		this.adptrtVersion = ver;
	}

	public String countElements(String filename) {
		parseInputFile(filename);
		return "PSS/E File elements coount\n" + this.elemCntStr;
	}
	
	/**
	 * PTI use 0 to indicate end of a data set, Bus Data for example. This function checks
	 * if the input line is the end of record line
	 *
	 * @param str a input data line string
	 */
	public static boolean isEndRecLine(String str) {
		String s = str.trim();
		return s.startsWith("0") || s.startsWith("Q"); // "/" could be used to comment out the line
	}

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws ODMException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) throws ODMException {
		throw new UnsupportedOperationException();
	}
	
	@Override public void setModelParser(IODMModelParser parser){
		this.odmParser = parser;
		this.parser = (BaseAclfModelParser<? extends LoadflowNetXmlType>) parser;
	}
	
}
