 /*
  * @(#)PSSEGenDataMapper.java   
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

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEGenDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
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

public class PSSEGenDataMapper extends BasePSSEDataMapper{
	
	public PSSEGenDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEGenDataParser(ver);
	}
	
	/*
	 * GenData
	 * I,ID,PG,QG,QT,QB,VS,IREG,MBASE,ZR,ZX,RT,XT,GTAP,STAT,RMPCT,PT,PB,O1,F1,...,O4,F4
	 * 
	 * The standard generator boundary condition is a specification of real
	 * power output at the high-voltage bus, bus k, and of voltage magnitude at
	 * some designated bus, not necessarily bus k.
	 * 
		PG Generator active power output; entered in MW. PG = 0.0 by default.
		QG Generator reactive power output; entered in Mvar. QG need be entered only if the
			case, as read in, is to be treated as a solved case. QG = 0.0 by default.
		QT Maximum generator reactive power output; entered in Mvar. For fixed output generators
			(i.e., nonregulating), QT must be equal to the fixed Mvar output.
			QT = 9999.0 by default.
		QB Minimum generator reactive power output; entered in Mvar. For fixed output
			generators, QB must be equal to the fixed Mvar output. QB = -9999.0 by default.
		VS Regulated voltage setpoint; entered in pu. VS = 1.0 by default.

 This is a case where the gen is turn-off
 I,    ID,       PG,        QG,        QT,        QB,   VS,          IREG,MBASE,     ZR,        ZX,        RT,         XT,        GTAP,   STAT,RMPCT,    PT,        PB,      O1,F1,...,O4,F4
 10636,'1 ',     0.000,     0.000,     0.000,     0.000,1.05100,     0,   100.000,    0.00000,    1.00000,    0.00000,   0.00000, 1.00000,1,  100.0,     0.000,     0.000,   1,1.0000
     6,'1 ',     4.600,    -0.923,     2.700,    -1.800,1.02500,     0,     6.200, 0.00000E+0, 1.80000E-1, 0.00000E+0, 0.00000E+0,1.00000,1,  100.0,     5.600,     0.000,   7,1.0000
	 */

	public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		//procFields(lineStr, version);
		dataParser.parseFields(lineStr);

/*
		I,ID,PG,QG,QT,QB,VS,IREG,MBASE,ZR,ZX,RT,XT,GTAP,STAT,RMPCT,PT,PB,O1,F1,...,O4,F4

		The standard generator boundary condition is a specification of real power output at the
		high-voltage bus, bus k, and of voltage magnitude at some designated bus, not necessarily bus k.
		
		STAT - Initial machine status of one for in-service and zero for out-of-service; STAT = 1 by default.
*/		
		int i = dataParser.getInt("I");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    BusXmlType busRecXml = parser.getBus(busId);
	    if (busRecXml == null){
	    	ODMLogger.getLogger().severe("Bus "+ busId+ " not found in the network");
	    	return;
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
	    
	    String id = dataParser.getString("ID");
	    contriGen.setId(id);
	    contriGen.setName("Gen:" + id + "(" + i + ")");
	    contriGen.setDesc("PSSE Generator " + id + " at Bus " + i);
	    
	    int stat = dataParser.getInt("STAT");
	    contriGen.setOffLine(stat!=1);

	    double pg = dataParser.getDouble("PG", 0.0);
	    double qg = dataParser.getDouble("QG", 0.0);
	    contriGen.setPower(BaseDataSetter.createPowerValue(pg, qg, ApparentPowerUnitType.MVA));

	    double vs = dataParser.getDouble("VS");
	    contriGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vs, VoltageUnitType.PU));
		
	    double pt = dataParser.getDouble("PT", 0.0);
	    double pb = dataParser.getDouble("PB", 0.0);
	    contriGen.setPLimit(BaseDataSetter.createActivePowerLimit(pt, pb, ActivePowerUnitType.MW));
		
	    double qt = dataParser.getDouble("QT", 0.0);
	    double qb = dataParser.getDouble("QB", 0.0);
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
	    int ireg = dataParser.getInt("IREG");
	    if (ireg > 0) {
	    	final String reBusId = IODMModelParser.BusIdPreFix+ireg;
	    	contriGen.setRemoteVoltageControlBus(parser.createBusRef(reBusId));
	    }
	    
	    double mbase = dataParser.getDouble("MBASE");
	    contriGen.setMvaBase(BaseDataSetter.createPowerMvaValue(mbase));

	    double zr = dataParser.getDouble("ZR", 0.0);
	    double zx = dataParser.getDouble("ZX", 0.0);
		if ( zr != 0.0 || zx != 0.0 )
			contriGen.setSourceZ(BaseDataSetter.createZValue(zr, zx, ZUnitType.PU));

		double rt = dataParser.getDouble("RT", 0.0);
		double xt = dataParser.getDouble("XT", 0.0);
		double gtap = dataParser.getDouble("GTAP");
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
		double rmpct = dataParser.getDouble("RMPCT");
		contriGen.setMvarVControlParticipateFactor(rmpct*0.01);

		mapOwnerInfo(contriGen);
	}
}
