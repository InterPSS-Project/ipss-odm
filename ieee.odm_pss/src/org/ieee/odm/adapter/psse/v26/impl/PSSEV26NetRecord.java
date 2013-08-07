/*
 * @(#)PSSEAdapter.java   
 *
 * Copyright (C) 2006-2008 www.interpss.org
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
 * @Author Stephen Hau, Mike Zhou
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.adapter.psse.v26.impl;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEAreaInterchangeDataParser;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEHeaderDataParser;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEInterAreaTransferDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PowerInterchangeXmlType;

public class PSSEV26NetRecord {
	public final static String Token_CaseDesc = "Case Description";     
	public final static String Token_CaseId = "Case ID";	
	
	PSSEHeaderDataParser headerDataParser = new PSSEHeaderDataParser(PsseVersion.PSSE_26);
	PSSEAreaInterchangeDataParser areaInterDataParser = new PSSEAreaInterchangeDataParser(PsseVersion.PSSE_26);
	PSSEInterAreaTransferDataParser interAreaDataParser = new PSSEInterAreaTransferDataParser(PsseVersion.PSSE_26);
	
	public boolean processHeaderData(final String str1,final String str2,final String str3,
			final LoadflowNetXmlType baseCaseNet) throws Exception {
		//line 1 at here we have "0, 100.00 " or some times "0 100.00 "		
		//final String[] strAry = getHeaderDataFields(str1,str2,str3);
		headerDataParser.parseFields(new String[] {str1,str2,str3});
		
		//if (strAry == null)
		//	return false;
		
		final double baseMva = headerDataParser.getDouble("BaseKva", 100.0);
		ODMLogger.getLogger().fine("BaseKva: "  + baseMva);
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));	    
	    
		//NameValuePairListXmlType nvList = factory.createNameValuePairListXmlType();
		//baseCaseNet.setNvPairList(nvList);
		
		final String desc = headerDataParser.getString("Comment1");// The 2nd line is treated as description
		BaseJaxbHelper.addNVPair(baseCaseNet, Token_CaseDesc, desc);     
	   
	    // the 3rd line is treated as the network id and network name		
		final String caseId= headerDataParser.getString("Comment2");
		BaseJaxbHelper.addNVPair(baseCaseNet, Token_CaseId, caseId);				
		ODMLogger.getLogger().fine("Case Description, caseId: " + desc + ", "+ caseId);		
		
        return true;
	}
        
	public void processAreaInterchangeData(final String str, AclfModelParser parser) throws ODMException {
		final LoadflowNetXmlType baseCaseNet = parser.getNet();

		//final String[] strAry = getAreaInterchangeDataFields(str);
		areaInterDataParser.parseFields(str);
		
		// "AreaNum", "SwingBusName", "ExpoertMw", "ExTolerance", "Notefined"             
		
		//     Area number , no zeros! *
		final int no = areaInterDataParser.getInt("AreaNum", 0);
		
		//       swing bus name [A]
		final String swingBusName = areaInterDataParser.getString("SwingBusName");

		//        Area interchange export, MW [F] (+ = out) *
		//        Area interchange tolerance, MW [F] *
		final double mw = areaInterDataParser.getDouble("ExpoertMw", 0.0);
		final double err = areaInterDataParser.getDouble("ExTolerance", 0.0);
    
		PowerInterchangeXmlType interchange = odmObjFactory.createPowerInterchangeXmlType();
		baseCaseNet.setInterchangeList(odmObjFactory.createLoadflowNetXmlTypeInterchangeList());
		InterchangeXmlType ex = odmObjFactory.createInterchangeXmlType();
		baseCaseNet.getInterchangeList().getInterchange().add(ex);
		ex.setPowerEx(interchange);
	
		interchange.setAreaNumber(no);

		interchange.setSwingBus(parser.createBusRef(swingBusName));
		
		interchange.setDesiredExPower(BaseDataSetter.createActivePowerValue(mw, ActivePowerUnitType.MW));
		interchange.setExErrTolerance(BaseDataSetter.createActivePowerValue(err, ActivePowerUnitType.MW));			
	}
	
	public void processInterAreaTransferData(final String str,
			final LoadflowNetXmlType baseCaseNet) throws ODMException {
		//final String[] strAry = getInterAreaTransferDataFields(str);
		interAreaDataParser.parseFields(str);
	}
}
