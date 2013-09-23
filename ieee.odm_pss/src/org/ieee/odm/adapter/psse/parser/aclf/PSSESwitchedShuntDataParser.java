/*
 * @(#)PSSESwitchedShuntDataParser.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
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
 * @Date 04/11/2013
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.psse.parser.aclf;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class PSSESwitchedShuntDataParser extends BasePSSEDataParser {
	private int nbPosition = 6;
	
	public PSSESwitchedShuntDataParser(PsseVersion ver) {
		super(ver);
	}
	
	@Override public String[] getMetadata() {
		/*
		Format V26
		==========
		I,    MODSW,VSWHI, VSWLO,  SWREM,                  BINIT,  N1,      B1,   N2,        B2...N8,B8
		
		                                                   nbPosition(6)

	Sample Data
		34606,0,    1.1000,0.9000,     0,-190.800,     1, -47.700,     1, -47.700,     1, -47.700,     1, -47.700,
		
		
		Format V29
		==========
		I,    MODSW, VSWHI, VSWLO, SWREM,         ’RMIDNT’, BINIT, N1, B1, N2, B2, ... N8, B8
                                                            nbPosition(7) 
                                                            
        Format V30
		==========
		I,    MODSW, VSWHI, VSWLO, SWREM,  RMPCT, ’RMIDNT’, BINIT, N1, B1, N2, B2, ... N8, B8
                                                            nbPosition(8) 
	Sample Data
		8441,1,1.03869,0.99869,    0,100.0,'        ',  334.80, 3,  50.40, 1,  37.80, 2,  36.00, 1,  37.80, 1,  36.00
    
			I - Bus number
			MODSW - Mode 0 - fixed 1 - discrete 2 - continuous
			VSWHI - Desired voltage upper limit, per unit
			VSWLO - Desired voltage lower limit, per unit
			SWREM - Number of remote bus to control. 0 to control own bus.
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
			BINIT - Initial switched shunt admittance, MVAR at 1.0 per unit volts
			N1 - Number of steps for block 1, first 0 is end of blocks
			B1 - Admittance increment of block 1 in MVAR at 1.0 per unit volts.
			N2, B2, etc, as N1, B1
			
			
		Format V32, V33
		===============
		I,    MODSW, ADJM, STAT, VSWHI, VSWLO, SWREM,  RMPCT, ’RMIDNT’, BINIT, N1, B1, N2, B2, ... N8, B8
		                                                                nbPosition(10)  
		ADJM Adjustment method:
  			0 steps and blocks are switched on in input order, and off in reverse input order; this adjustment 
  			  method was the only method available prior to PSS/E-32.0.
  			1 steps and blocks are switched on and off such that the next highest (or lowest, as appropriate) total admittance is achieved.
  			ADJM = O by default.
		STAT Initial switched shunt status of one for in-service and zero for out-of-service; STAT = 1 by default.		
			
		 */		
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",     "MODSW",                          "VSWHI",
			                       "ADJM",    "STAT",          			// V32,V33
		   //  5          6          7          8          9
			  "VSWLO",   "SWREM", "RMPCT",  "RMIDNT",                   // V30, V32,V33 
			                                             "BINIT",     
		   //  10         11         12         13         14
			  "N1",      "B1",      "N2",      "B2",      "N3",      
		   //  15         16         17         18         19
			  "B3",      "N4",      "B4",      "N5",      "B5",       
		   //  20         21         22         23         24
			  "N6",      "B6",      "N7",      "B7",      "N8",      
		   //  25	  
			  "B8" 
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
  		StringTokenizer st = new StringTokenizer(lineStr, ",");
  		
		nbPosition = 6;
  		if (this.version == PsseVersion.PSSE_29)
  			nbPosition = 7;
  		else if (this.version == PsseVersion.PSSE_30)
  			nbPosition = 8;
  		else if (this.version == PsseVersion.PSSE_32 ||
  				 this.version == PsseVersion.PSSE_33)
  			nbPosition = 10;
  		
  		for ( int i = 0; i < this.nbPosition; i++) { 
  			if (i==2 && (this.version == PsseVersion.PSSE_26 ||
  					     this.version == PsseVersion.PSSE_29 ||
  	  			         this.version == PsseVersion.PSSE_30))
  				i += 2;
  	  		this.setValue(i, st.nextToken().trim());
  		}
  		
  		// process the  N1,B1,...N8,B8 part
  		for ( int i = 0; i < 8; i++) {
  			if (st.hasMoreTokens()) {
  		  		String str = st.nextToken().trim();
  		  		if (!str.trim().equals("")) {
  		  			int pos = this.nbPosition+i*2;
  		  			this.setValue(pos, str);
  		  			this.setValue(pos+1, st.nextToken().trim());
  		  		}
  			}
  		}
		
	}
}