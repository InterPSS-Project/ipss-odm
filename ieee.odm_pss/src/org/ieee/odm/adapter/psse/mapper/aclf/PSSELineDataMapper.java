 /*
  * @(#)PSSELineDataMapper.java   
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
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.psse.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSELineDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSELineDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{

	public PSSELineDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSELineDataParser(ver);
	}
	
	/*
	 * BranchData
	 * I,J,CKT,R,X,B,RATEA,RATEB,RATEC,GI,BI,GJ,BJ,ST,LEN,O1,F1,...,O4,F4
	 */
	public void procLineString(String lineStr, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		//procLineFields(lineStr, version);
		dataParser.parseFields(lineStr);
		//System.out.println(lineStr + "\n" + dataParser.toString());
/*
		I,J,CKT,R,X,B,RATEA,RATEB,RATEC,GI,BI,GJ,BJ,ST,LEN,O1,F1,...,O4,F4
		
		ST Initial branch status where 1 designates in-service and 0 designates out-of-service. ST = 1 by default.
*/
		int i = dataParser.getInt("I");
		int j = dataParser.getInt("J");
		
		/* starting from V32
		 * MET	Metered end flag;
			<=1 to designate bus I as the metered end 
			=>2 to designate bus J as the metered end.
				MET = 1 by default.
		 */
		boolean fromMetered = true;
		if (PSSEAdapter.getVersionNo(this.version) >= 32) {
			int met = dataParser.getInt("MET", 1);
			if (met >= 2)
				fromMetered = false;
		}
		else {
			if (j < 0) {
				fromMetered = false;
				j = -j;
			}
		}
      	
		final String fid = IODMModelParser.BusIdPreFix+i;
		final String tid = IODMModelParser.BusIdPreFix+j;

		LineBranchXmlType braRecXml;
		try {
			braRecXml = (LineBranchXmlType) parser.createLineBranch(fid, tid, dataParser.getString("CKT"));
		} catch (ODMBranchDuplicationException e) {
			ODMLogger.getLogger().severe(e.toString());
			return;
		}		
		
		int status = dataParser.getInt("ST", 1);
		braRecXml.setOffLine(status != 1);
		
		braRecXml.setMeterLocation( fromMetered ? BranchBusSideEnumType.FROM_SIDE :
										BranchBusSideEnumType.TO_SIDE);
      	
		double r = dataParser.getDouble("R", 0.0);
		double x = dataParser.getDouble("X", 0.0);
		double b = dataParser.getDouble("B", 0.0);
		AclfDataSetter.setLineData(braRecXml, r, x, ZUnitType.PU, 0.0, b, YUnitType.PU);

		double ratea = dataParser.getDouble("RATEA", 0.0);
		double rateb = dataParser.getDouble("RATEB", 0.0);
		double ratec = dataParser.getDouble("RATEC", 0.0);
		braRecXml.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		AclfDataSetter.setBranchRatingLimitData(braRecXml.getRatingLimit(),
    				ratea, rateb, ratec, ApparentPowerUnitType.MVA);
        
		double gi = dataParser.getDouble("GI", 0.0);
		double bi = dataParser.getDouble("BI", 0.0);
       if ( gi != 0.0 || bi != 0.0)
    	   braRecXml.setFromShuntY(BaseDataSetter.createYValue(gi, bi, YUnitType.PU));
       
		double gj = dataParser.getDouble("GJ", 0.0);
		double bj = dataParser.getDouble("BJ", 0.0);
       if ( gj != 0.0 || bj != 0.0)
    	   braRecXml.setToShuntY(BaseDataSetter.createYValue(gj, bj, YUnitType.PU));
       
       mapOwnerInfo(braRecXml);
	}
}
