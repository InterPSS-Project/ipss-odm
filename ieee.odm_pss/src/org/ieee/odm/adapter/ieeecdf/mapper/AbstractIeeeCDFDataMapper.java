/*
 * @(#)AbstractIeeeCDFDataMapper.java   
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

package org.ieee.odm.adapter.ieeecdf.mapper;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter.IEEECDFVersion;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;

/**
 * Abstract base class for implementing data mapper to map an input data
 * line to the ODM parser object.
 * 
 * @author mzhou
 *
 */
public abstract class AbstractIeeeCDFDataMapper {
	/** input date line parser*/
	protected AbstractDataFieldParser dataParser = null;
	
	protected IEEECDFVersion version = null;
	
	public AbstractIeeeCDFDataMapper() {
	}
	
	public AbstractIeeeCDFDataMapper(IEEECDFVersion ver) {
		this.version = ver;
	}
	
	/**
	 * First the dataParser is used to parse the inout date line. Then, map the info stored 
	 * in the parser object into the model parser object
	 * 
	 * @param strLine an input data line
	 * @param parser ODM parser object
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	abstract void mapInputLine(final String strLine, AclfModelParser parser) throws ODMException, ODMBranchDuplicationException;
}