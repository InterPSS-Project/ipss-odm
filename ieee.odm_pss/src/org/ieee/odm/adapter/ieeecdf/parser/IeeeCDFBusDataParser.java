/*
 * @(#)IeeeCDFBusDataParser.java   
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

package org.ieee.odm.adapter.ieeecdf.parser;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.AbstractDataFieldParser;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter.IEEECDFVersion;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

/**
 * Class for parsing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class IeeeCDFBusDataParser extends BaseIeeeCDFDataParser {
	public IeeeCDFBusDataParser() {
		super();
	}	
	
	public IeeeCDFBusDataParser(IEEECDFVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0---------------1---------------2---------------3---------------4
		     "BusNumber",  "BusName",       "Area",          "Zone",        "Type", 
		   //  5               6               7               8               9
		     "VMag",        "VAng",         "LoadP",         "LoadQ",       "GenP", 
		   //  10              11              12              13              14
		     "GenQ",       "BaseKV",        "DesiredV",     "MaxVarVolt",   "MinVarVolt", 
		   //  15              16              17              18              19
		     "ShuntG",    "ShuntB",         "RemoteBusNumber", 
		   //  for version = Ext1  
		                                                    "MaxVolt",      "MinVolt",
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		if (str.indexOf(',') >= 0) {
			final StringTokenizer st = new StringTokenizer(str, ",");
			int cnt = 0;
			while (st.hasMoreTokens()) {
				this.setValue(cnt++, st.nextToken().trim());
			}
		} else {
			//Columns  1- 4   Bus number [I] *
			this.setValue(0, str.substring(0, 4).trim());

			//Columns  6-17   Name [A] (left justify) *
			this.setValue(1, str.substring(5, 17).trim());

			//Columns 19-20   Load flow area number [I].  Don't use zero! *
			//Columns 21-23   Loss zone number [I]
			this.setValue(2, str.substring(18, 20).trim());
			this.setValue(3, str.substring(20, 23).trim());

			//Columns 77-83   Base kV [F]
			this.setValue(11, str.substring(76, 83));

			//Columns 25-26   Type [I] *
			//		0 - Unregulated (load, PQ)
			//		1 - Hold MVAR generation within voltage limits, (gen, PQ)
			//		2 - Hold voltage within VAR limits (gen, PV)
			//		3 - Hold voltage and angle (swing, V-Theta; must always have one)
			this.setValue(4, str.substring(24, 26).trim());

			//Columns 28-33   Final voltage, p.u. [F] *
			//Columns 34-40   Final angle, degrees [F] *
			this.setValue(5, str.substring(27, 33));
			this.setValue(6, str.substring(33, 40));

			//Columns 41-49   Load MW [F] *
			//Columns 50-59   Load MVAR [F] *
			this.setValue(7, str.substring(40, 49));
			this.setValue(8, str.substring(49, 59));

			//Columns 60-67   Generation MW [F] *
			//Columns 68-75   Generation MVAR [F] *
			this.setValue(9, str.substring(59, 67));
			this.setValue(10, str.substring(67, 75));

			//Columns 107-114 Shunt conductance G (per unit) [F] *
			//Columns 115-122 Shunt susceptance B (per unit) [F] *
			this.setValue(15, ODMModelStringUtil.getString(str,107, 114));
			this.setValue(16, ODMModelStringUtil.getString(str,115, 122));

			//Columns 85-90   Desired volts (pu) [F] (This is desired remote voltage if this bus is controlling another bus.)
			this.setValue(12, ODMModelStringUtil.getString(str,85, 90));

			//Columns 91-98   Minimum MVAR or voltage limit [F]
			//Columns 99-106  Maximum MVAR or voltage limit [F]
			this.setValue(13, ODMModelStringUtil.getString(str,91, 98));
			this.setValue(14, ODMModelStringUtil.getString(str,99, 106));

			//Columns 124-127 Remote controlled bus number
			this.setValue(17, ODMModelStringUtil.getString(str,123, 127).trim());
		}
	}
}