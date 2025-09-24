/*
  * @(#)GEXformerDataMapper.java   
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

import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.adapter.ge.parser.GEXfrDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TransformerInfoXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GEXformerDataMapper extends BaseGEDataMapper {
    // Add a logger instance following BPADynamicExciterRecord style
    private static final Logger log = LoggerFactory.getLogger(GEXformerDataMapper.class);
	public GEXformerDataMapper(GePslfAdapter.Version ver)  {
		super(ver);
		this.dataParser = new GEXfrDataParser();
	}	

	public void mapLineStr(String lineStr, final AclfModelParser parser) throws ODMException, ODMBranchDuplicationException {
		dataParser.parseFields(lineStr);
		
/*
*/
		double anglp = this.dataParser.getDouble("anglp", 0.0);
		double angls = this.dataParser.getDouble("angls", 0.0);
		boolean isPsXfr = anglp != 0.0 || angls != 0.0;

		final String fid = IODMModelParser.BusIdPreFix + this.dataParser.getValue("f_bus");
		final String tid = IODMModelParser.BusIdPreFix + this.dataParser.getValue("t_bus");
		final String cId = this.dataParser.getValue("ck").replace(' ', '_');
		XfrBranchXmlType branchRec = null;
		try {
			branchRec = (XfrBranchXmlType)(isPsXfr?
				parser.createPSXfrBranch(fid, tid, cId) : parser.createXfrBranch(fid, tid, cId));
		} catch (Exception e) {
			log.error("branch data error, " + e.toString());
		}				
		
		int type = this.dataParser.getInt("type");
		BaseJaxbHelper.addNVPair(branchRec, GePslfAdapter.Token_XfrType, this.dataParser.getValue("type"));
		
		double tbase = this.dataParser.getDouble("tbase");
		double vnomp = this.dataParser.getDouble("vnomp");
		double vnoms = this.dataParser.getDouble("vnoms");
		TransformerInfoXmlType xfrInfo = branchRec.getXfrInfo();
		xfrInfo.setRatedPower(BaseDataSetter.createPowerMvaValue(tbase));
		xfrInfo.setFromRatedVoltage(BaseDataSetter.createVoltageValue(vnomp, VoltageUnitType.KV));
		xfrInfo.setToRatedVoltage(BaseDataSetter.createVoltageValue(vnoms, VoltageUnitType.KV));
		
		double zpsr = this.dataParser.getDouble("zpsr");
		double zpsx = this.dataParser.getDouble("zpsx");
		branchRec.setZ(BaseDataSetter.createZValue(zpsr, zpsx, ZUnitType.PU));
		branchRec.getXfrInfo().setDataOnSystemBase(false);
		
		double tapfp = this.dataParser.getDouble("tapfp");
		double tapfs = this.dataParser.getDouble("tapfs");
		branchRec.setFromTurnRatio(BaseDataSetter.createTurnRatioPU(tapfp));
		branchRec.setToTurnRatio(BaseDataSetter.createTurnRatioPU(tapfs));
		
		if (isPsXfr) {
			PSXfrBranchXmlType branch = (PSXfrBranchXmlType)branchRec;
			branch.setFromAngle(BaseDataSetter.createAngleValue(anglp, AngleUnitType.DEG));
			branch.setToAngle(BaseDataSetter.createAngleValue(angls, AngleUnitType.DEG));
		}

		double gmag = this.dataParser.getDouble("gmag");
		double bmag = this.dataParser.getDouble("bmag");
		if (gmag != 0.0 || bmag != 0.0)
			branchRec.setMagnitizingY(BaseDataSetter.createYValue(gmag, bmag, YUnitType.PU));
		
		double aloss = this.dataParser.getDouble("aloss", 0.0);
		double alosss = this.dataParser.getDouble("alosss", 0.0);
		xfrInfo.setFromLossFactor(aloss);
		xfrInfo.setToLossFactor(alosss);

		/*		
		 */

		if (type == 2 || type == 12) {
			/* TODO
			xfr.setAdjBusNumber(this.kregBus);
			xfr.setTapAngMax(this.tmax);
			xfr.setTapAngMin(this.tmin);
			xfr.setVmax(this.vtmax);
			xfr.setVmin(this.vtmin);
			xfr.setAdjTapAngStep(this.stepp);
			xfr.setAdjTapPrim(this.tapp);
			xfr.setZTableNumber(this.zt);
			*/
		}
		else if (type == 4 || type == 14) {
			/* TODO
			xfr.setAdjBusNumber(this.kregBus);
			xfr.setTapAngMax(this.tmax);
			xfr.setTapAngMin(this.tmin);
			xfr.setVmax(this.vtmax);
			xfr.setVmin(this.vtmin);
			xfr.setAdjTapAngStep(this.stepp);
			xfr.setAdjTapPrim(this.tapp);
			xfr.setZTableNumber(this.zt);
			*/
		}
	}
}