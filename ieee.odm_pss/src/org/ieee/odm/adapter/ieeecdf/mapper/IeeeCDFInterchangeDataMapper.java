/*
 * @(#)IeeeCDFInterchangeDataMapper.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFInterchangeDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.PowerInterchangeXmlType;

/**
 * IEEE CDF inter-change record ODM mapper
 * 
 * @author mzhou
 *
 */
public class IeeeCDFInterchangeDataMapper extends AbstractIeeeCDFDataMapper {
	/**
	 * constructor
	 */
	public IeeeCDFInterchangeDataMapper() {
		this.dataParser = new IeeeCDFInterchangeDataParser();
	}

	@Override public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		InterchangeXmlType interchange = parser.createInterchange();
		PowerInterchangeXmlType pxchange = OdmObjFactory.createPowerInterchangeXmlType();
		interchange.setPowerEx(pxchange);		

		dataParser.parseFields(str);
		
		//    	Columns  1- 2   Area number [I], no zeros! *
		final int no = dataParser.getInt("AreaNum");

		//    	Columns  4- 7   Interchange slack bus number [I] *
		//      Columns  9-20   Alternate swing bus name [A]
		int slackBusNumber = dataParser.getInt("SwingBusNum");
		String slackBusId = IODMModelParser.BusIdPreFix + slackBusNumber;
		final String alSwingBusName = dataParser.getString("AltSwingBusName");

		//      Columns 21-28   Area interchange export, MW [F] (+ = out) *
		//      Columns 30-35   Area interchange tolerance, MW [F] *
		final double mw = dataParser.getDouble("ExportMw");
		final double err = dataParser.getDouble("ExTolerance");

		//      Columns 38-43   Area code (abbreviated name) [A] *
		//      Columns 46-75   Area name [A]
		final String code = dataParser.getString("AreaCode");
		final String name = dataParser.getString("AreaName");

		pxchange.setAreaNumber(no);
		
		if (slackBusNumber > 0) {
			pxchange.setSwingBus(parser.createBusRef(slackBusId));
			pxchange.setAlternateSwingBusName(alSwingBusName);
		}
		
		pxchange.setDesiredExPower(BaseDataSetter.createActivePowerValue(mw, ActivePowerUnitType.MW));
		pxchange.setExErrTolerance(BaseDataSetter.createActivePowerValue(err, ActivePowerUnitType.MW));

		pxchange.setAreaCode(code);
		pxchange.setAreaName(name);
	}
}