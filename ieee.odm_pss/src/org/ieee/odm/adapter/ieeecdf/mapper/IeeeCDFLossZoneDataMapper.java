/*
 * @(#)IeeeCDFLossZoneDataMapper.java   
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

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFLossZoneDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.NetZoneXmlType;

/**
 * IEEE CDF loss zone record ODM mapper
 * 
 * @author mzhou
 *
 */
public class IeeeCDFLossZoneDataMapper extends AbstractIeeeCDFDataMapper {

	/**
	 * Constructor
	 */
	public IeeeCDFLossZoneDataMapper() {
		this.dataParser = new IeeeCDFLossZoneDataParser();
	}

	@Override public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		dataParser.parseFields(str);
		
		final NetZoneXmlType lossZone = parser.createNetworkLossZone();

		//    	Columns  1- 3   Loss zone number [I] *
		//    	Columns  5-16   Loss zone name [A] 
		final int no = dataParser.getInt("ZoneNum");
		final String name = dataParser.getValue("ZoneName");
		lossZone.setNumber(no);
		lossZone.setName(name);
	}
}