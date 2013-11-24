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

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFInterchangeDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.PowerInterchangeXmlType;

public class IeeeCDFInterchangeDataMapper extends AbstractIeeeCDFDataMapper {
	
	public IeeeCDFInterchangeDataMapper() {
		this.dataParser = new IeeeCDFInterchangeDataParser();
	}

	@Override public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		InterchangeXmlType interchange = parser.createInterchange();
		PowerInterchangeXmlType p = odmObjFactory.createPowerInterchangeXmlType();
		this.processInterchangeData(str, p, parser);
		interchange.setPowerEx(p);		
	}
	
	public void processInterchangeData(final String str,
			final PowerInterchangeXmlType interchange, AclfModelParser parser) throws ODMException {
		//final String[] strAry = IeeeCDFDataParser.getInterchangeDataFields(str);
		dataParser.parseFields(str);
		
		//    	Columns  1- 2   Area number [I], no zeros! *
		final int no = dataParser.getInt("AreaNum");

		//    	Columns  4- 7   Interchange slack bus number [I] *
		//      Columns  9-20   Alternate swing bus name [A]
		int slackBusNumber = dataParser.getInt("SwingBusNum");
		String slackBusId = AbstractModelParser.BusIdPreFix + slackBusNumber;
		final String alSwingBusName = dataParser.getString("AltSwingBusName");

		//      Columns 21-28   Area interchange export, MW [F] (+ = out) *
		//      Columns 30-35   Area interchange tolerance, MW [F] *
		final double mw = dataParser.getDouble("ExportMw");
		final double err = dataParser.getDouble("ExTolerance");

		//      Columns 38-43   Area code (abbreviated name) [A] *
		//      Columns 46-75   Area name [A]
		final String code = dataParser.getString("AreaCode");
		final String name = dataParser.getString("AreaName");

		interchange.setAreaNumber(no);
		
		if (slackBusNumber > 0) {
			interchange.setSwingBus(parser.createBusRef(slackBusId));
			interchange.setAlternateSwingBusName(alSwingBusName);
		}
		
		interchange.setDesiredExPower(BaseDataSetter.createActivePowerValue(mw, ActivePowerUnitType.MW));
		interchange.setExErrTolerance(BaseDataSetter.createActivePowerValue(err, ActivePowerUnitType.MW));

		interchange.setAreaCode(code);
		interchange.setAreaName(name);
	}
}