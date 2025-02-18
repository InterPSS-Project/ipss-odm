/*
 * @(#)PSSEFactsDataParser.java   
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

package org.ieee.odm.adapter.psse.raw.parser.aclf;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E FACTS Data
 * 
 * @author mzhou
 *
 */
public class PSSEFactsDataRawParser extends BasePSSEDataRawParser {
	public PSSEFactsDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
	/*
	 * Format V30
	 * 
		N,I,J,MODE,PDES,QDES,VSET,SHMX,TRMX,VTMN,VTMX,VSMX,IMX,LINX,RMPCT,OWNER,SET1,SET2,VSREF

	N FACTS device number.
	I Sending end bus number, or extended bus name enclosed in single quotes . No default allowed.
	J Terminal end bus number, or extended bus name enclosed in single quotes ; 
	 	0 for a STATCON. J = 0 by default.
	MODE Control mode:
		0 - out-of-service (i.e., series and shunt links open)
		1 - series and shunt links operating.
		2 - series link bypassed (i.e., like a zero impedance line) and shunt link operating
			as a STATCON.
		3 - series and shunt links operating with series link at constant series impedance.
		4 - series and shunt links operating with series link at constant series voltage.
		5 - "master" device of an IPFC with P and Q setpoints specified; FACTS device
			"N+1" must be the "slave" device (i.e., its MODE is 6 or 8) of this IPFC.
		6 - "slave" device of an IPFC with P and Q setpoints specified; FACTS device
			"N-1" must be the "master" device (i.e., its MODE is 5 or 7) of this IPFC. The
			Q setpoint is ignored as the "master" device dictates the active power
			exchanged between the two devices.
		7 - "master" device of an IPFC with constant series voltage setpoints specified;
			FACTS device "N+1 must be the "slave" device (i.e., its MODE is 6 or 8) of
			this IPFC.
		8 - "slave" device of an IPFC with constant series voltage setpoints specified;
			FACTS device "N-1" must be the "master" device (i.e., its MODE is 5 or 7) of
			this IPFC. The complex Vd + jVq setpoint is modified during power flow
			solutions to reflect the active power exchange determined by the "master"
			device.
		If J is specified as 0, MODE must be either 0 or 1. MODE = 1 by default.
	PDES Desired active power flow arriving at the terminal end bus; entered in MW.
		PDES = 0.0 by default.
	QDES Desired reactive power flow arriving at the terminal end bus; entered in MVAR.
		QDES = 0.0 by default.
	VSET Voltage setpoint at the sending end bus; entered in pu. VSET = 1.0 by default.
	SHMX Maximum shunt current at the sending end bus; entered in MVA at unity voltage.
		SHMX = 9999.0 by default.
	TRMX Maximum bridge active power transfer; entered in MW. TRMX = 9999.0 by
		default.
	VTMN Minimum voltage at the terminal end bus; entered in pu. VTMN = 0.9 by default.
	VTMX Maximum voltage at the terminal end bus; entered in pu. VTMX = 1.1 by default.
	VSMX Maximum series voltage; entered in pu. VSMX = 1.0 by default.
	IMX Maximum series current, or zero for no series current limit; entered in MVA at
		unity voltage. IMX = 0.0 by default.
	LINX Reactance of the dummy series element used during model solution; entered in pu.
		LINX = 0.05 by default.
	RMPCT Percent of the total Mvar required to hold the voltage at bus I that are to be contributed
		by the shunt element of this FACTS device; RMPCT must be positive.
		RMPCT is needed only if there is more than one local or remote voltage controlling
		device (plant, switched shunt, FACTS device shunt element, or VSC dc line
		converter) controlling the voltage at bus I to a setpoint. RMPCT = 100.0 by default.
	OWNER Owner number (1 through the maximum number of owners at the current size level;
		see Table P-1). OWNER = 1 by default.
	SET1,SET2 If MODE is 3, resistance and reactance respectively of the constant impedance,
		entered in pu; if MODE is 4, the magnitude (in pu) and angle (in degrees) of the
		constant series voltage with respect to the quantity indicated by VSREF; if MODE
		is 7 or 8, the real (Vd) and imaginary (Vq) components (in pu) of the constant series
		voltage with respect to the quantity indicated by VSREF; for other values of
		MODE, SET1 and SET2 are read, but not saved or used during power flow solutions.
		SET1 = 0.0 and SET2 = 0.0 by default.
	VSREF Series voltage reference code to indicate the series voltage reference of SET1 and
		SET2 when MODE is 4, 7 or 8: 0 for sending end voltage, 1 for series current.
		VSREF = 0 by default.
	 */
		/*
		 *  "fields":["name", "ibus", "jbus", "mode", "pdes", "qdes", "vset", "shmx", "trmx", "vtmn", 
		 *            "vtmx", "vsmx", "imx", "linx", "rmpct", "owner", "set1", "set2", "vsref", "fcreg", 
		 *            "nreg", "mname"], 
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "N",       "I",       "J",      "MODE",    "PDES",
			  "QDES",    "VSET",    "SHMX",   "TRMX",    "VTMN",      
			  "VTMX",    "VSMX",    "IMX",    "LINX",    "RMPCT",
			  "OWNER",   "SET1",    "SET2",   "VSREF"             
		};
	}
	
//	@Override public void parseFields(final String lineStr) throws ODMException {
//		StringTokenizer st = new StringTokenizer(lineStr, ",");
//		
//		int cnt = 0;
//		while (st.hasMoreTokens())
//			this.setValue(cnt++, st.nextToken().trim());
//  	}
}