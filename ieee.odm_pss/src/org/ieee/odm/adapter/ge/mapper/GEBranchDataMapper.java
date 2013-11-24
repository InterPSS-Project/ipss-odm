 /*
  * @(#)GEBranchDataMapper.java   
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
  * @Author Mike Zhou
  * @Version 1.0
  * @Date 06/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.ge.mapper;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.adapter.ge.parser.GEBranchDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LengthUnitType;
import org.ieee.odm.schema.LineBranchInfoXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class GEBranchDataMapper extends BaseGEDataMapper {
	public GEBranchDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEBranchDataParser();
	}
	
	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		dataParser.parseFields(lineStr);
		
		final String fid = AclfModelParser.BusIdPreFix + dataParser.getString("f_bus");
		final String tid = AclfModelParser.BusIdPreFix + dataParser.getString("t_bus");
		final String cId = dataParser.getString("ck").replace(' ', '_');
		LineBranchXmlType branchRec = null;
		branchRec = parser.createLineBranch(fid, tid, cId);
		
		branchRec.setOffLine(dataParser.getInt("st") == 0);
		
	    double r = dataParser.getDouble("r");
	    double x = dataParser.getDouble("x");
	    double b = dataParser.getDouble("b");
	    int ohms = dataParser.getInt("ohms", 0);
		if (ohms == 0) 
			AclfDataSetter.setLineData(branchRec, r, x,
					ZUnitType.PU, 0.0, b, YUnitType.PU);			
		else
			AclfDataSetter.setLineData(branchRec, r, x,
					ZUnitType.OHM, 0.0, b, YUnitType.MHO);
		
		AclfDataSetter.setBranchRatingLimitData(branchRec.getRatingLimit(), 
				new double[] {dataParser.getDouble("r1"), dataParser.getDouble("r2"), dataParser.getDouble("r3"),
			                  dataParser.getDouble("r4"), dataParser.getDouble("r5"), dataParser.getDouble("r6"),
			                  dataParser.getDouble("r7"), dataParser.getDouble("r8")
			}, ApparentPowerUnitType.MVA);
		
		branchRec.setAreaNumber(dataParser.getInt("ar"));
		branchRec.setZoneNumber(dataParser.getInt("z"));
		
		LineBranchInfoXmlType lineInfo = branchRec.getLineInfo();
		lineInfo.setLength(OdmObjFactory.createLengthXmlType());
		lineInfo.getLength().setValue(dataParser.getDouble("l"));
		lineInfo.getLength().setUnit(LengthUnitType.MILE);
		
		BaseJaxbHelper.setBranchOwnership(branchRec, 
				new int[] {dataParser.getInt("o1"), dataParser.getInt("o2"), dataParser.getInt("o3"),
				           dataParser.getInt("o4"), dataParser.getInt("o5"), dataParser.getInt("o6"),
				           dataParser.getInt("o7"), dataParser.getInt("o8")}, 
				new double[] {dataParser.getDouble("p1"), dataParser.getDouble("p2"), dataParser.getDouble("p3"),
				              dataParser.getDouble("p4"), dataParser.getDouble("p5"), dataParser.getDouble("p6"),
				              dataParser.getDouble("p7"), dataParser.getDouble("p8")});
	}
}
