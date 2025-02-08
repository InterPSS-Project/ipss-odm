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
package org.ieee.odm.adapter.psse.json.impl;

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
public class BasePSSEJSonAdapter extends AbstractODMAdapter {
	protected PsseVersion adptrtVersion = PsseVersion.PSSE_JSON;
	protected BaseAclfModelParser<? extends LoadflowNetXmlType> parser =null;
	
	public BasePSSEJSonAdapter() {
		super();
	}

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws ODMException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) throws ODMException {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	@Override public void setModelParser(IODMModelParser parser){
		this.odmParser = parser;
		this.parser = (BaseAclfModelParser<? extends LoadflowNetXmlType>) parser;
	}
	
}
