/*
  * @(#)PSSEAreaDataMapper.java   
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

package org.ieee.odm.adapter.psse.json.mapper;

import java.util.List;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEGenDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEGenDataJSonMapper(List<String> fieldDef) {
		super(fieldDef);
	}
	
	/**
	 * map the data list into the DOM model parser 
	 * 
	 * @param data
	 * @param odmParser
	 */
	public void map(List<Object> data, BaseAclfModelParser<? extends NetworkXmlType> odmParser) {
		dataParser.loadFields(data.toArray());
		
		/*
         "fields":["ibus", "machid", "pg", "qg", "qt", "qb", "vs", "ireg", "nreg", "mbase", 
         			"zr", "zx", "rt", "xt", "gtap", "stat", "rmpct", "pt", "pb", "baslod", 
         			"o1", "f1", "o2", "f2", "o3", "f3", "o4", "f4", "wmod", "wpf", "droopname", "name"], 
         "data":  [1, "1", 71.64000, 27.05000, 9999.000, -9999.000, 1.040000, 1, 0, 100.0000,
          			0.000000, 0.4000000E-01, 0.000000, 0.000000, 1.000000, 1, 100.0000, 9999.000, 
          			-9999.000, 0, 1, 1.000000, null, null, null, null, null, null, null, null, null, null], 
		 */		
		try {
			/*
			I,ID,PG,QG,QT,QB,VS,IREG,MBASE,ZR,ZX,RT,XT,GTAP,STAT,RMPCT,PT,PB,O1,F1,...,O4,F4

			The standard generator boundary condition is a specification of real power output at the
			high-voltage bus, bus k, and of voltage magnitude at some designated bus, not necessarily bus k.
			
			STAT - Initial machine status of one for in-service and zero for out-of-service; STAT = 1 by default.
	*/		
			int ibus = dataParser.getInt("ibus");
		    final String busId = IODMModelParser.BusIdPreFix+ibus;
		    BusXmlType busRecXml = odmParser.getBus(busId);
		    if (busRecXml == null){
		    	throw new RuntimeException("Bus "+ busId+ " not found in the network");
		    }
		    
		    /*
		     * At this point, we need to check the type of the bus object to add appropriate contribute gen
		     * data type
		     */
		    LoadflowGenDataXmlType contriGen;
		    if(busRecXml instanceof DStabBusXmlType){
		    	contriGen = DStabParserHelper.createDStabContributeGen((DStabBusXmlType)busRecXml);
		    }
		    else if (busRecXml instanceof ShortCircuitBusXmlType) {
			    contriGen = AcscParserHelper.createAcscContributeGen((ShortCircuitBusXmlType)busRecXml);
		    } 
		    else {
			    contriGen = AclfParserHelper.createContriGen((LoadflowBusXmlType)busRecXml);
		    }
		    
		    String id = dataParser.getString("machid");
		    contriGen.setId(id);
		    contriGen.setName("Gen:" + id + "(" + ibus + ")");
		    contriGen.setDesc("PSSE Generator " + id + " at Bus " + ibus);
		    
		    int stat = dataParser.getInt("stat");
		    contriGen.setOffLine(stat!=1);

		    double pg = dataParser.getDouble("pg", 0.0);
		    double qg = dataParser.getDouble("qg", 0.0);
		    contriGen.setPower(BaseDataSetter.createPowerValue(pg, qg, ApparentPowerUnitType.MVA));

		    double vs = dataParser.getDouble("vs");
		    contriGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vs, VoltageUnitType.PU));
			
		    double pt = dataParser.getDouble("pt", 0.0);
		    double pb = dataParser.getDouble("pb", 0.0);
		    contriGen.setPLimit(BaseDataSetter.createActivePowerLimit(pt, pb, ActivePowerUnitType.MW));
			
		    double qt = dataParser.getDouble("qt", 0.0);
		    double qb = dataParser.getDouble("qb", 0.0);
		    contriGen.setQLimit(BaseDataSetter.createReactivePowerLimit(qt, qb, ReactivePowerUnitType.MVAR));
		    
		    /*
	         * for bus type 2, it is set as PV bus by default. If Qmax = QMin, it is set to PQ bus
	         */
	        LoadflowBusXmlType aclfBusXml = (LoadflowBusXmlType)busRecXml;
	        if (aclfBusXml.getGenData().getCode() == LFGenCodeEnumType.PV && !contriGen.isOffLine()) {
	                if (qt == qb) {
	                        aclfBusXml.getGenData().setCode(LFGenCodeEnumType.PQ);
	                }
	        }
		    //Gen at bus of type 1 and 4 is offLine
		    if (aclfBusXml.getGenData().getCode() == LFGenCodeEnumType.NONE_GEN ||
		    		aclfBusXml.getGenData().getCode() == LFGenCodeEnumType.OFF)
		    contriGen.setOffLine(true);
		    
		    /*
			 * Bus number, or extended bus name enclosed in single quotes, of a remote Type 1 or 2 bus for which voltage 
			 * is to be regulated by this plant to the value specified by VS. If bus IREG is other than a Type 1 or 2 bus, 
			 * bus I regulates its own voltage to the value specified by VS. IREG is entered as zero if the plant is to 
			 * regulate its own voltage and must be zero for a Type 3 (swing) bus. IREG=0 by default.
			 */
		    int ireg = dataParser.getInt("ireg");
		    if (ireg > 0) {
		    	final String reBusId = IODMModelParser.BusIdPreFix+ireg;
		    	contriGen.setRemoteVoltageControlBus(odmParser.createBusRef(reBusId));
		    }
		    
		    double mbase = dataParser.getDouble("mbase");
		    contriGen.setMvaBase(BaseDataSetter.createPowerMvaValue(mbase));

		    double zr = dataParser.getDouble("zr", 0.0);
		    double zx = dataParser.getDouble("zx", 0.0);
			if ( zr != 0.0 || zx != 0.0 )
				contriGen.setSourceZ(BaseDataSetter.createZValue(zr, zx, ZUnitType.PU));

			double rt = dataParser.getDouble("rt", 0.0);
			double xt = dataParser.getDouble("xt", 0.0);
			double gtap = dataParser.getDouble("gtap");
			if ( rt != 0.0 || xt != 0.0 ) {
				contriGen.setXfrZ(BaseDataSetter.createZValue(rt, xt, ZUnitType.PU));
				contriGen.setXfrTap(gtap);
			}
			
			/*
			 * Percent of the total Mvar required to hold the voltage at the bus controlled by bus I that are to be 
			 * contributed by the generation at bus I; RMPCT must be positive. RMPCT is needed only if IREG specifies 
			 * a valid remote bus and there is more than one local or remote voltage controlling device (plant, switched shunt, 
			 * FACTS device shunt element, or VSC dc line converter) controlling the voltage at bus IREG to a setpoint, or 
			 * IREG is zero but bus I is the controlled bus, local or remote, of one or more other setpoint mode voltage 
			 * controlling devices. RMPCT=100.0 by default.
			 */
			double rmpct = dataParser.getDouble("rmpct");
			contriGen.setMvarVControlParticipateFactor(rmpct*0.01);

			mapMultiOwnerInfo(contriGen);
		} catch (ODMException e) {
			throw new RuntimeException(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}