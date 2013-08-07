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
package org.ieee.odm.adapter.psse.impl;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;

/**
 * ODM adapter for PSS/E input format
 * 
 * @author mzhou
 *
 */
public class BasePSSEAdapter extends AbstractODMAdapter{
	public final static String Token_CaseDesc = "Case Description";     
	public final static String Token_CaseId = "Case ID";		

	protected PsseVersion adptrtVersion;
	protected BaseAclfModelParser<? extends LoadflowNetXmlType, 
			        ? extends LoadflowBusXmlType, ? extends LineBranchXmlType, 
					? extends XfrBranchXmlType, ? extends PSXfrBranchXmlType> parser =null;
	
	protected String  elemCntStr = "";
       
	
	public BasePSSEAdapter(PsseVersion ver) {
		super();
		this.adptrtVersion = ver;
		
	}


	public String countElements(String filename) {
		try {
			parseInputFile(filename);
		} catch ( Exception e) {
			this.elemCntStr += e.toString();
		}
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
		return s.startsWith("0") || s.startsWith("/") || s.startsWith("Q");
	}

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding)
			throws Exception {
		
		throw new UnsupportedOperationException();
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din,
			String encoding) throws Exception {
		
		throw new UnsupportedOperationException();
	}


	public BaseAclfModelParser<? extends LoadflowNetXmlType, ? extends LoadflowBusXmlType, 
			? extends LineBranchXmlType, ? extends XfrBranchXmlType, ? extends PSXfrBranchXmlType> getParser() {
		return parser;
	}


	public void setParser(BaseAclfModelParser<? extends LoadflowNetXmlType, ? extends LoadflowBusXmlType,
			? extends LineBranchXmlType, ? extends XfrBranchXmlType, ? extends PSXfrBranchXmlType> parser) {
		this.parser = parser;
	}	
	
}
