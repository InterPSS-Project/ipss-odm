/*
 * @(#)IeeeCDFNetDataMapper.java   
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

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFNetDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.LoadflowNetXmlType;

public class IeeeCDFNetDataMapper extends AbstractIeeeCDFDataMapper {
	public final static String Token_Date = "Date";
	public final static String Token_OrgName = "Originator Name";
	public final static String Token_Year = "Year";
	public final static String Token_Season = "Season";
	public final static String Token_CaseId = "Case Identification";
	
	public IeeeCDFNetDataMapper() {
		this.dataParser = new IeeeCDFNetDataParser();
	}
	
	@Override public void mapInputLine(final String str, final AclfModelParser parser) throws ODMException {
		// parse the input data line {"Date", "Originator", "MVA", "Year", "Season", "CaseId"}
		dataParser.parseFields(str);

		LoadflowNetXmlType baseCaseNet = parser.getNet();
		
		//NameValuePairListXmlType nvList = this.factory.createNameValuePairListXmlType();
		//baseCaseNet.setNvPairList(nvList);

		//[0] Columns  2- 9   Date, in format DD/MM/YY with leading zeros.  If no date provided, use 0b/0b/0b where b is blank.
		if (dataParser.exist("Date")) {
			final String date = dataParser.getString("Date");
			BaseJaxbHelper.addNVPair(baseCaseNet, Token_Date, date);
		}
		
		//[1] Columns 11-30   Originator's name [A]
		if (dataParser.exist("Originator")) {
			final String orgName = dataParser.getString("Originator");
			BaseJaxbHelper.addNVPair(baseCaseNet, Token_OrgName, orgName);
		}

		//[3] Columns 39-42   Year [I]
		if (dataParser.equals("Year")) {
			final String year = dataParser.getString("Year");
			BaseJaxbHelper.addNVPair(baseCaseNet, Token_Year, year);
		}
		
		//[4] Column  44      Season (S - Summer, W - Winter)
		if (dataParser.exist("Season")) {
			final String season = dataParser.getString("Season");
			BaseJaxbHelper.addNVPair(baseCaseNet, Token_Season, season);
		}
		
		//[5] Column  46-73   Case identification [A]
		if (dataParser.exist("CaseId")) {
			final String caseId = dataParser.getString("CaseId");
			if (caseId != null)
				BaseJaxbHelper.addNVPair(baseCaseNet, Token_CaseId, caseId);
			//ODMLogger.getLogger().fine("date, orgName, year, season, caseId: " + date + ", "
			//		+ orgName + ", " + year + ", " + season + ", " + caseId);
		}

		//[2] Columns 32-37   MVA Base [F] *
		double baseMva = 100.0;
		if (dataParser.exist("MVA")) {
			baseMva = dataParser.getDouble("MVA"); // in MVA
			ODMLogger.getLogger().fine("BaseKva: " + baseMva);
		}
		else{
			throw new ODMException("Network Mva base  is required, but not defined in the header of input file, please make sure the file is of IEEE-CDF format and the MVABase is correctly defined");
		}
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
	}
}