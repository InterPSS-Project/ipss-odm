/*
 * @(#)IeeeCDFTielineDataMapper.java   
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

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFTieLineDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.TielineXmlType;

/**
 * IEEE CDF tie-line record ODM mapper
 * 
 * @author mzhou
 *
 */
public class IeeeCDFTielineDataMapper extends AbstractIeeeCDFDataMapper {
	/**
	 * constructor
	 */
	public IeeeCDFTielineDataMapper() {
		this.dataParser = new IeeeCDFTieLineDataParser();
	}

	@Override public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		dataParser.parseFields(str);

		final TielineXmlType tieLine = parser.createTieline();
				
		//    	Columns  1- 4   Metered bus number [I] *
		//    	Columns  7-8    Metered area number [I] *
		final String meteredBusId = IODMModelParser.BusIdPreFix + dataParser.getString("MeteredBusNum");
		final String meteredAreaNo = dataParser.getString("MeteredAreaNum");

		//      Columns  11-14  Non-metered bus number [I] *
		//      Columns  17-18  Non-metered area number [I] *
		final String nonMeteredBusId = IODMModelParser.BusIdPreFix + dataParser.getString("NotMeteredBusNum");
		final String nonMeteredAreaNo = dataParser.getString("NotMeteredAreaNum");

		//      Column   21     Circuit number
		int cirNo = dataParser.getInt("CirNum", 0);

		tieLine.setMeteredBus(parser.createBusRef(meteredBusId));
		tieLine.setNonMeteredBus(parser.createBusRef(nonMeteredBusId));

		tieLine.setMeteredArea(meteredAreaNo);
		tieLine.setNonMeteredArea(nonMeteredAreaNo);
		
		tieLine.setCirId(new Integer(cirNo).toString());
	}	
}