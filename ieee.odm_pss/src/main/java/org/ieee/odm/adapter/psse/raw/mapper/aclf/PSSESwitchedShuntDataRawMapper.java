 /*
  * @(#)PSSESwitchedSShuntDataMapper.java   
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

package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSESwitchedShuntDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SwitchedShuntBlockXmlType;
import org.ieee.odm.schema.SwitchedShuntModeEnumType;
import org.ieee.odm.schema.SwitchedShuntXmlType;
import org.ieee.odm.schema.VoltageUnitType;

public class PSSESwitchedShuntDataRawMapper extends BasePSSEDataRawMapper{

	public PSSESwitchedShuntDataRawMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSESwitchedShuntDataRawParser(ver);
	}
	
	public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
/*
		   //  0----------1----------2----------3----------4
			  "I",     "MODSW",   "VSWHI",   "VSWLO",   "SWREM",      
		   //  5          6          7          8          9
			  "RMPCT",  "RMIDNT", "BINIT",    "N1",      "B1", 
		   //  10         11         12         13         14
			  "N2",      "B2",      "N3",      "B3",      "N4",
		   //  15         16         17         18         19
			  "B4",      "N5",      "B5",      "N6",      "B6", 
		   //  20         21         22         23         24
			  "N7",      "B7",      "N8",      "B8"  */
		
			  /*
			   * Format V32, V33
		===============
		I,    MODSW, ADJM, STAT, VSWHI, VSWLO, SWREM,  RMPCT, 'RMIDNT', N1, B1, N2, B2, ... N8, B8
		                                                                nbPosition(10)  
		ADJM Adjustment method:
  			0 steps and blocks are switched on in input order, and off in reverse input order; this adjustment 
  			  method was the only method available prior to PSS/E-32.0.
  			1 steps and blocks are switched on and off such that the next highest (or lowest, as appropriate) total admittance is achieved.
  			ADJM = O by default.
		STAT Initial switched shunt status of one for in-service and zero for out-of-service; STAT = 1 by default.		
			   */
		
		final String busId = IODMModelParser.BusIdPreFix+this.dataParser.getValue("I");
		// get the responding-bus data with busId
		LoadflowBusXmlType aclfBus = (LoadflowBusXmlType) parser.getBus(busId);
		if (aclfBus==null){
			throw new ODMException("Error: Bus not found in the network, bus number: " + busId);
        }
				
	    SwitchedShuntXmlType shunt = OdmObjFactory.createSwitchedShuntXmlType();
	    aclfBus.setSwitchedShunt(shunt);
		
		// genId is used to distinguish multiple generations at one bus		
		int mode = this.dataParser.getInt("MODSW",  0);
		shunt.setMode(mode ==0? SwitchedShuntModeEnumType.FIXED :
						mode ==1? SwitchedShuntModeEnumType.DISCRETE : 
							SwitchedShuntModeEnumType.CONTINUOUS);
		
		if(PSSERawAdapter.getVersionNo(this.version) >31) {
			//ADJM - Adjustment method
			//STAT - Initial switched shunt status of one for in-service and zero for out-of-service
			int status = this.dataParser.getInt("STAT", 1);
			shunt.setOffLine(status == 0);
		}
		
		//VSWHI - Desired voltage upper limit, per unit
		//VSWLO - Desired voltage lower limit, per unit
		final double vmax = this.dataParser.getDouble("VSWHI", 1.0);
		final double vmin = this.dataParser.getDouble("VSWLO", 1.0);
		shunt.setDesiredVoltageRange(BaseDataSetter.createVoltageLimit(vmax, vmin, VoltageUnitType.PU));
		
		//SWREM - Number of remote bus to control. 0 to control own bus.
		int busNo = this.dataParser.getInt("SWREM", 0);
		if(PSSERawAdapter.getVersionNo(this.version) >33) {
			busNo = this.dataParser.getInt("SWREG", 0); // the naming was changed after v34 and newer
		}
		if (busNo != 0) {
			shunt.setRemoteControlledBus(parser.createBusRef(IODMModelParser.BusIdPreFix+busNo));
		}

		/* V30
			RMPCT Percent of the total Mvar required to hold the voltage at the bus controlled by bus
					I that are to be contributed by this switched shunt; RMPCT must be positive.
					RMPCT is needed only if SWREM specifies a valid remote bus and there is more
					than one local or remote voltage controlling device (plant, switched shunt, FACTS
					device shunt element, or VSC dc line converter) controlling the voltage at bus
					SWREM to a setpoint, or SWREM is zero but bus I is the controlled bus, local or
					remote, of one or more other setpoint mode voltage controlling devices. Only used
					if MODSW = 1 or 2. RMPCT = 100.0 by default.
			RMIDNT When MODSW is 4, the name of the VSC dc line whose converter bus is specified
					in SWREM. RMIDNT is not used for other values of MODSW. RMIDNT is a
					blank name by default.
		 */
		if (version == PsseVersion.PSSE_30) {
			shunt.setVarPercent(this.dataParser.getDouble("RMPCT", 100.0));
			if(mode==4)
			shunt.setVscDcLine(this.dataParser.getValue("RMIDNT"));
		}
		
		//BINIT - Initial switched shunt admittance, MVAR at 1.0 per unit volts
		final double binit = this.dataParser.getDouble("BINIT", 0.0);
		shunt.setBInit(BaseDataSetter.createReactivePowerValue(binit, ReactivePowerUnitType.MVAR));
		
		//N1 - Number of steps for block 1, first 0 is end of blocks
		//B1 - Admittance increment of block 1 in MVAR at 1.0 per unit volts. N2, B2, etc, as N1, B1
		for (int i = 0; i < 8; i++) {
	  		int n = this.dataParser.getInt("N"+(i+1), 0);
			if(n>0){
			  		double b = this.dataParser.getDouble("B"+(i+1), 0.0);
			  		SwitchedShuntBlockXmlType block = OdmObjFactory.createSwitchedShuntBlockXmlType(); 
			  		shunt.getBlock().add(block);
			  		block.setSteps(n);
			  		block.setIncrementB(BaseDataSetter.createReactivePowerValue(b, ReactivePowerUnitType.MVAR));
			  }
	  		}
	}
}
