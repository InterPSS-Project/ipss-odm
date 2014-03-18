/*
 * @(#)PSSEBusRecord.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEBusDataParser;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEGenDataParser;
import org.ieee.odm.adapter.psse.parser.aclf.PSSELoadDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusGenDataXmlType;
import org.ieee.odm.schema.BusLoadDataXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEV26BusRecord {
	private PSSEBusDataParser busDataParser = null;
	private PSSEGenDataParser genDataParser = null;
	private PSSELoadDataParser loadDataParser = null;
	
	public PSSEV26BusRecord(PsseVersion version) {
		this.busDataParser = new PSSEBusDataParser(version);
		this.genDataParser = new PSSEGenDataParser(version);
		this.loadDataParser = new PSSELoadDataParser(version);
	}

	public void processBusData(final String str, final AclfModelParser parser) throws ODMException {
		// parse the input data line
		//final String[] strAry = getBusDataFields(str);
		busDataParser.parseFields(str);

		// I,    NAME        BASKV, IDE,  GL,      BL, AREA, ZONE, VM,      VA,      OWNER
		// 31212,'ADLIN  1', 115.00,1,    0.00,    0.00,  1,  1,   1.01273, -10.5533,1 
		// 45035,'CAPJAK 1', 500.00,1,    0.00,  400.00,  8, 11,1.08554,  18.6841,1,    /* [45035_CAPTJACK_500_B2] */ 

		final String busId = IODMModelParser.BusIdPreFix+busDataParser.getString("I");
			// XML requires id start with a char
		ODMLogger.getLogger().fine("Bus data loaded, id: " + busId);
		LoadflowBusXmlType busRec;
		try {
			busRec = parser.createBus(busId, busDataParser.getInt("I"));
		} catch (Exception e) {
			ODMLogger.getLogger().severe(e.toString());
			return;
		}
		
		busRec.setNumber(busDataParser.getLong("I", 0));
		
		//final String busName = ModelStringUtil.removeSingleQuote(busDataParser.getString("NAME"));
		final String busName = busDataParser.getString("NAME");
		busRec.setName(busName);
		double baseKv = busDataParser.getDouble("BASKV", 0.0);
		if (baseKv == 0.0) {
			ODMLogger.getLogger().severe("Error: base kv = 0.0");
			baseKv = 1.0;
		}
		
		final String owner=busDataParser.getString("OWNER");
		BaseJaxbHelper.addOwner(busRec, owner);
		
		busRec.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv, VoltageUnitType.KV));
		
		// vm voltage, p.u. [F] *
		//va angle, degrees [F] *
		final double vpu = busDataParser.getDouble("VM", 1.0);
		final double angDeg = busDataParser.getDouble("VA", 0.0);
		busRec.setVoltage(BaseDataSetter.createVoltageValue(vpu, VoltageUnitType.PU));
		busRec.setAngle(BaseDataSetter.createAngleValue(angDeg, AngleUnitType.DEG));
		
		/* bus type identifier IDE
			1 - load bus (no generator boundary condition)
			2 - generator or plant bus (either voltage regulating or fixed Mvar)
			3 - swing bus
			4 - disconnected (isolated) bus
			IDE = 1 by default.
		*/			
		final int IDE = busDataParser.getInt("IDE", 1);
		if (IDE ==3){//Swing bus
			/*
			busRec.setGenData(OdmObjFactory.createBusGenDataXmlType());
			LoadflowGenDataXmlType equivGen = OdmObjFactory.createLoadflowGenDataXmlType(); 
			busRec.getGenData().setEquivGen(OdmObjFactory.createEquivGen(equivGen));
			*/
			busRec.getGenData().setCode(LFGenCodeEnumType.SWING);
			LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(busRec.getGenData());
			defaultGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vpu, VoltageUnitType.PU));
		}
		else if (IDE==2){// generator bus. At this point we do not know if it is a PQ or PV bus
			// by default, Gen is a PV bus
			/*
			busRec.setGenData(OdmObjFactory.createBusGenDataXmlType());
			busRec.getGenData().setEquivGen(OdmObjFactory.createEquivGen(OdmObjFactory.createLoadflowGenDataXmlType()));
			*/
			busRec.getGenData().setCode(LFGenCodeEnumType.PV);
		} else if (IDE==4){// Isolated bus
			// should be no gen and load defined
			busRec.setOffLine(true);
		}
		else { //Non-Gen Load Bus
			/*
			busRec.setLoadData(OdmObjFactory.createBusLoadDataXmlType());
			busRec.getLoadData().setEquivLoad(OdmObjFactory.createEquivLoad(OdmObjFactory.createLoadflowLoadDataXmlType()));
			*/
		}
		
		//GL BL in Mva
		final double gMw = busDataParser.getDouble("GL", 0.0);
		final double bMvar= busDataParser.getDouble("BL", 0.0);
		if (gMw != 0.0 || bMvar != 0.0) {
			busRec.getShuntYData().setEquivY(BaseDataSetter.createYValue(gMw, bMvar, YUnitType.MVAR));
		}
		
		//area zone	
		busRec.setAreaNumber(busDataParser.getInt("AREA", 0));
		busRec.setZoneNumber(busDataParser.getInt("ZONE", 0));		
	}
		
	public void processLoadData(final String str,final AclfModelParser parser) throws ODMException {
		// I,    ID,  STATUS, AREA, ZONE, PL,   QL,   IP,   IQ,   YP,    YQ,  OWNER
		// 33547,' 1',1,      1,    1,    3.00, 9.54, 0.00, 0.00, 0.00,  0.00,1,   /* [EnergyConsumer_1704] */
		
		//final String[] strAry = getLoadDataFields(str);
		loadDataParser.parseFields(str);

	    final String busId = IODMModelParser.BusIdPreFix+loadDataParser.getString("I");
	    //to test if there is a responding bus in the bus data record
		LoadflowBusXmlType busRec = parser.getBus(busId);
	    if (busRec == null){
	    	ODMLogger.getLogger().severe("Bus "+ busId+ " not found in the network");
	    	return;
	    }

	    // ODM allows one equiv load has many contribute loads, but here, we assume there is only one contribute load.

		BusLoadDataXmlType loadData = busRec.getLoadData();
		/* this should never happen
		if (loadData == null) { 
			loadData = OdmObjFactory.createBusLoadDataXmlType(); 
			busRec.setLoadData(loadData);
			loadData.setEquivLoad(OdmObjFactory.createEquivLoad(OdmObjFactory.createLoadflowLoadDataXmlType()));
		}
		*/
	    LoadflowLoadDataXmlType contribLoad = AclfParserHelper.createContriLoad(busRec); 
		
	    // processing contributing load data

	    //loadId is used to distinguish multiple loads at one bus
	    final String loadId = loadDataParser.getString("ID");
		contribLoad.setId(loadId);
		
		// STATUS - Initial load status of one for in-service and zero for out-of-service. STATUS = 1 by default
		int status = loadDataParser.getInt("STATUS", 1);
		int area = loadDataParser.getInt("AREA", 1);
		int zone = loadDataParser.getInt("ZONE", 1);
		contribLoad.setOffLine(status != 1);
		contribLoad.setAreaNumber(area);
		contribLoad.setZoneNumber(zone);
		
		//set owner and it's factor
		BaseJaxbHelper.addOwner(contribLoad, loadDataParser.getString("OWNER"));
		    
	    //Constant-P load
		final double CPloadMw = loadDataParser.getDouble("PL", 0.0);
		final double CQloadMvar = loadDataParser.getDouble("QL", 0.0);
		//Constant-I load
		final double CIloadMw = loadDataParser.getDouble("IP", 0.0);
		final double CIloadMvar = loadDataParser.getDouble("IQ", 0.0);
		//Constant-Y load
		final double CYloadMw = loadDataParser.getDouble("YP", 0.0);
		final double CYloadMvar = loadDataParser.getDouble("YQ", 0.0);

		if (CPloadMw!=0.0 || CQloadMvar!=0.0 )
			contribLoad.setConstPLoad(BaseDataSetter.createPowerValue(
	    			CPloadMw, CQloadMvar, ApparentPowerUnitType.MVA));

	    if (CIloadMw!=0.0 || CIloadMvar!=0.0)
	    	contribLoad.setConstILoad(BaseDataSetter.createPowerValue(
	    			CIloadMw, CIloadMvar, ApparentPowerUnitType.MVA));
	   
	    if (CYloadMw!=0.0 || CYloadMvar!=0.0)
	    	contribLoad.setConstZLoad(BaseDataSetter.createPowerValue(
	    			CYloadMw, CYloadMvar, ApparentPowerUnitType.MVA));

	    contribLoad.setCode(LFLoadCodeEnumType.CONST_P);
	    //double tp = CPloadMw + CIloadMw + CYloadMw + load.getConstPLoad().getRe();
	    //double tq = CQloadMvar + CIloadMvar + CYloadMvar  + load.getConstPLoad().getIm();;
	    //contribLoad.setConstPLoad(BaseDataSetter.createPowerValue(tp, tq, ApparentPowerUnitType.MVA));
	}
	
	public void processGenData(final String str,final AclfModelParser parser) throws ODMException {
		//I,    ID,      PG,      QG,     QT,      QB,   VS,        IREG,MBASE, ZR,    ZX,    RT,    XT,    GTAP,  STAT,RMPCT,  PT,         PB,  O1,F1,...,O4,F4
		//31435,' 1',    8.52,    2.51,   10.00,   -6.00,1.0203,    0,   100.00,0.0000,1.0000,0.0000,0.0000,1.0000,1,   100.00, 9999.00,    0.00,1,1.00,0,0.00,0,0.00,0,0.00,   /* [SynchronousMachine_78] */ 
		
		//37585,' 1',    0.00,    0.00,    0.00,    0.00,1.0331,    0,   100.00,0.0000,1.0000,0.0000,0.0000,1.0000,0,   100.00, 9999.00,-5322.00,1,1.00,0,0.00,0,0.00,0,0.00,   /* [SynchronousMachine_27209] */ 
		//37585,' 2',   -1.00,  186.48, 1774.00,-1774.00,1.0331,    0,   100.00,0.0000,1.0000,0.0000,0.0000,1.0000,1,   100.00, 9999.00,-5322.00,1,1.00,0,0.00,0,0.00,0,0.00,   /* [SynchronousMachine_20804] */ 
		
		// parse the input data line
	    //final String[] strAry = getGenDataFields(str);
	    genDataParser.parseFields(str);
	    
		final String busId = IODMModelParser.BusIdPreFix+genDataParser.getString("I");
		// get the responding-bus data with busId
		LoadflowBusXmlType busRec = parser.getBus(busId);
		if (busRec==null){
			ODMLogger.getLogger().severe("Error: Bus not found in the network, bus number: " + busId);
        	return;
        }
				
	    // ODM allows one equiv gen has many contribute generators

		BusGenDataXmlType genData = busRec.getGenData();
		/* this should never happen
		if (genData == null) {
			genData = OdmObjFactory.createBusGenDataXmlType();
			busRec.setGenData(genData);
			busRec.getGenData().setEquivGen(OdmObjFactory.createEquivGen(OdmObjFactory.createLoadflowGenDataXmlType()));
		}
		*/
		LoadflowGenDataXmlType contriGen = AclfParserHelper.createContriGen(busRec);
	    //LoadflowGenDataXmlType contriGen = AclfParserHelper.createContriGen(busRec);
		
	    // processing contributing gen data
	    
		// genId is used to distinguish multiple generations at one bus		
		final String genId = genDataParser.getString("ID");
		contriGen.setId(genId);
		
		double mbase = genDataParser.getDouble("MBASE", 0.0),
		       zr = genDataParser.getDouble("ZR", 0.0),
		       zx = genDataParser.getDouble("ZX", 0.0),
		       rt = genDataParser.getDouble("RT", 0.0),
		       xt = genDataParser.getDouble("XT", 0.0),
		       gtap = genDataParser.getDouble("GTAP", 0.0); 
		contriGen.setMvaBase(BaseDataSetter.createPowerMvaValue(mbase));
		if(zr != 0.0 || zx != 0.0)
			contriGen.setSourceZ(BaseDataSetter.createZValue(zr, zx, ZUnitType.PU));
		if(rt != 0.0 || xt != 0.0)
			contriGen.setXfrZ(BaseDataSetter.createZValue(rt, xt, ZUnitType.PU));
		contriGen.setXfrTap(gtap);
		
		// STATUS - Initial load status of one for in-service and zero for out-of-service. STATUS = 1 by default
		int status = genDataParser.getInt("STAT", 1);
		contriGen.setOffLine(status != 1);

		//I,    ID,      PG,      QG,     QT,      QB,   VS,        IREG,MBASE, ZR,    ZX,    RT,    XT,    GTAP,  STAT,RMPCT,  PT,         PB,  O1,F1,...,O4,F4

		double genMw = genDataParser.getDouble("PG", 0.0);
		double genMvar = genDataParser.getDouble("QG", 0.0);
		contriGen.setPower(BaseDataSetter.createPowerValue(genMw, genMvar, ApparentPowerUnitType.MVA));

		BaseJaxbHelper.addOwner(contriGen, 
				genDataParser.getString("O1"), genDataParser.getDouble("F1", 0.0), 
				genDataParser.getString("O2"), genDataParser.getDouble("F2", 0.0), 
				genDataParser.getString("O3"), genDataParser.getDouble("F3", 0.0), 
				genDataParser.getString("O4"), genDataParser.getDouble("F4", 0.0));


		final double vSpecPu = genDataParser.getDouble("VS", 1.0);
		if (genData.getCode() == LFGenCodeEnumType.SWING) {
			contriGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vSpecPu, VoltageUnitType.PU));
		}
		else {
			// qmax, gmin in Mvar. there may exist already
			double max = genDataParser.getDouble("QT", 0.0);
			double min = genDataParser.getDouble("QB", 0.0);
			contriGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vSpecPu, VoltageUnitType.PU));
			contriGen.setQLimit(BaseDataSetter.createReactivePowerLimit(max, min, ReactivePowerUnitType.MVAR));

				// Desired volts (pu) (This is desired remote voltage if this bus is controlling another bus.)
				/*  IREG  
		      	final int iReg = genDataParser.getInt("IREG", 0);
				if (iReg > 0) {
					final String reBusId = IODMModelParser.BusIdPreFix+genDataParser.getString("IREG");
					equivGen.setRemoteVoltageControlBus(parser.createBusRef(reBusId));
				}
				*/
		}
		
		//System.out.println(busRec.toString());
    }
}
