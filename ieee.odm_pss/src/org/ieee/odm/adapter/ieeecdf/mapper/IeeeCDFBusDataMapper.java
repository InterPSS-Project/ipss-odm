/*
 * @(#)IeeeCDFBusDataMapper.java   
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

import org.ieee.odm.adapter.ieeecdf.parser.IeeeCDFBusDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;

public class IeeeCDFBusDataMapper extends BaseIeeeCDFDataMapper {
	
	public IeeeCDFBusDataMapper() {
		this.dataParser = new IeeeCDFBusDataParser();
	}
	 
	public void mapInputLine(final String str, AclfModelParser parser) throws ODMException {
		// parse the input data line
		//final String[] strAry = IeeeCDFDataParser.getBusDataFields(str);
		dataParser.parseFields(str);

		//Columns  1- 4   Bus number [I] *
		final String busId = AbstractModelParser.BusIdPreFix + dataParser.getString("BusNumber");
		ODMLogger.getLogger().fine("Bus data loaded, id: " + busId);
		LoadflowBusXmlType aclfBus = parser.createBus(busId);
		aclfBus.setNumber(dataParser.getLong("BusNumber"));

		//Columns  6-17   Name [A] (left justify) *
		final String busName = dataParser.getString("BusName");
		aclfBus.setName(busName);

		//Columns 19-20   Load flow area number [I].  Don't use zero! *
		//Columns 21-23   Loss zone number [I]
		final int areaNo = dataParser.getInt("Area");
		final int zoneNo = dataParser.getInt("Zone");
		aclfBus.setAreaNumber(areaNo);
		aclfBus.setZoneNumber(zoneNo);

		//Columns 77-83   Base kV [F]
		double baseKv =  dataParser.getDouble("BaseKV");
		if (baseKv == 0.0) {
			baseKv = 1.0;
		}
		aclfBus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv, VoltageUnitType.KV));

		//aclfBus.setLoadflowData(this.factory.createLoadflowBusDataXmlType());
		//LoadflowBusDataXmlType busData = aclfBus.getLoadflowData();
		//Columns 25-26   Type [I] *
		//		0 - Unregulated (load, PQ)
		//		1 - Hold MVAR generation within voltage limits, (gen, PQ)
		//		2 - Hold voltage within VAR limits (gen, PV)
		//		3 - Hold voltage and angle (swing, V-Theta; must always have one)
		// it might empty, if empty, type = 0;
		int type = 0;
		if (!dataParser.getString("Type").trim().equals(""))
			type = dataParser.getInt("Type");

		//Columns 28-33   Final voltage, p.u. [F] *
		//Columns 34-40   Final angle, degrees [F] *
		final double vpu = dataParser.getDouble("VMag");
		final double angDeg = dataParser.getDouble("VAng");
		aclfBus.setVoltage(BaseDataSetter.createVoltageValue(vpu, VoltageUnitType.PU));

		aclfBus.setAngle(BaseDataSetter.createAngleValue(angDeg, AngleUnitType.DEG));

		//Columns 41-49   Load MW [F] *
		//Columns 50-59   Load MVAR [F] *
		final double loadMw = dataParser.getDouble("LoadP");
		final double loadMvar = dataParser.getDouble("LoadQ");
		if (loadMw != 0.0 || loadMvar != 0.0) {
			AclfDataSetter.setLoadData(aclfBus,
					LFLoadCodeEnumType.CONST_P, loadMw,
					loadMvar, ApparentPowerUnitType.MVA);
		}

		//Columns 60-67   Generation MW [F] *
		//Columns 68-75   Generation MVAR [F] *
		final double genMw = dataParser.getDouble("GenP");
		final double genMvar = dataParser.getDouble("GenQ");

		LFGenCodeEnumType genType = type == 3? LFGenCodeEnumType.SWING :
				( type == 2? LFGenCodeEnumType.PV : LFGenCodeEnumType.PQ );
		AclfDataSetter.setGenData(
				aclfBus, genType, vpu, VoltageUnitType.PU, angDeg, AngleUnitType.DEG, 
				genMw, genMvar,	ApparentPowerUnitType.MVA);

		//Columns 107-114 Shunt conductance G (per unit) [F] *
		//Columns 115-122 Shunt susceptance B (per unit) [F] *
		final double gPU = dataParser.getDouble("ShuntG");
		final double bPU = dataParser.getDouble("ShuntB");
		if (gPU != 0.0 || bPU != 0.0) {
			aclfBus.setShuntY(BaseDataSetter.createYValue(gPU, bPU, YUnitType.PU));
		}

		//Columns 85-90   Desired volts (pu) [F] (This is desired remote voltage if this bus is controlling another bus.)
		final double vSpecPu = dataParser.getDouble("DesiredV");

		//Columns 91-98   Maximum MVAR or voltage limit [F]
		//Columns 99-106  Minimum MVAR or voltage limit [F]
		final double max = dataParser.getDouble("MaxVarVolt");
		final double min = dataParser.getDouble("MinVarVolt");

		//Columns 124-127 Remote controlled bus number
		final String reBusId = dataParser.getString("RemoteBusNumber");

		if (max != 0.0 || min != 0.0) {
			LoadflowGenDataXmlType equivGen = aclfBus.getGenData().getEquivGen().getValue();
			if (type == 1) {
				equivGen.setVoltageLimit(BaseDataSetter.createVoltageLimit(max, min, VoltageUnitType.PU));
			} else if (type == 2) {
				aclfBus.getGenData().getEquivGen().getValue().setQLimit(BaseDataSetter.createReactivePowerLimit(max, min, ReactivePowerUnitType.MVAR));
				if (reBusId != null && !reBusId.equals("0")
						&& !reBusId.equals(busId)) {
					equivGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vSpecPu, VoltageUnitType.PU));
					equivGen.setRemoteVoltageControlBus(parser.createBusRef(reBusId));
				}
			}
		}
	}
}