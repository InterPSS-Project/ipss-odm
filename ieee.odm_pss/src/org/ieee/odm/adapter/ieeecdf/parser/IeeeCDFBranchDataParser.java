/*
 * @(#)IeeeCDFBranchDataParser.java   
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
import org.ieee.odm.common.ODMException;

/**
 * Class for processing IEEE CDF branch data line string
 * 
 * @author mzhou
 *
 */
public class IeeeCDFBranchDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0               1                  2                3               4
		     "FromNum",    "ToNum",           "Area",           "Zone",         "CirId", 
		   //  5               6                  7                8               9
		     "Type",       "R",               "X",              "B",            "MvaRating1", 
		   //  10             11                  12              13               14
		     "MvaRating2", "MvaRating3",      "CntlBusNum",     "CntlBusSide",  "TurnRatio", 
		   //  15             16                 17               18               19
		     "ShiftAngle", "MaxTapShiftAng",  "MinTapShiftAng", "TapStepSize",  "MinVoltMvarMw", 
		   //  20             
		     "MaxVoltMvarMw"
		};
	}

	@Override public void parseFields(final String str) throws ODMException {
		if (str.indexOf(',') >= 0) {
			final StringTokenizer st = new StringTokenizer(str, ",");
			int cnt = 0;
			while (st.hasMoreTokens()) {
				setValue(cnt++, st.nextToken().trim());
			}
		} else {
			//        	Columns  1- 4   Tap bus number [I] *
			//      	For transformers or phase shifters, the side of the model the non-unity tap is on.
			//			Columns  6- 9   Z bus number [I] *
			//      	For transformers and phase shifters, the side of the model the device impedance is on.
			setValue(0, str.substring(0, 4).trim());
			setValue(1, str.substring(5, 9).trim());
			//    		IpssLogger.getLogger().fine("Branch data loaded, from-id, to-id: " + strAry[0] + ", " + strAry[1]);

			//    		Columns 11-12   Load flow area [I]
			//    		Columns 13-15   Loss zone [I]
			//    		Column  17      Circuit [I] * (Use 1 for single lines)
			setValue(2, str.substring(10, 12).trim());
			setValue(3, str.substring(12, 15).trim());
			setValue(4, str.substring(16, 17).trim());

			//    		Column  19      Type [I] *
			String typeStr = str.substring(18, 19).trim();
			setValue(5, typeStr);

			//    		Columns 20-29   Branch resistance R, per unit [F] *
			//    		Columns 30-40   Branch reactance X, per unit [F] * No zero impedance lines
			//    		Columns 41-50   Line charging B, per unit [F] * (total line charging, +B)
			setValue(6, str.substring(19, 29));
			setValue(7, str.substring(29, 40));
			setValue(8, str.substring(40, 50));

			//    		Columns 77-82   Transformer final turns ratio [F]
			//    		Columns 84-90   Transformer (phase shifter) final angle [F]
			setValue(14, str.substring(76, 82));
			setValue(15, str.substring(83, 90));

			//    		Columns 51-55   Line MVA rating No 1 [I] Left justify!
			//    		Columns 57-61   Line MVA rating No 2 [I] Left justify!
			//    		Columns 63-67   Line MVA rating No 3 [I] Left justify!
			setValue(9, str.substring(50, 55).trim());
			setValue(10, str.substring(56, 61).trim());
			setValue(11, str.substring(62, 67).trim());

			int type = 0;
			if (!typeStr.equals(""))
				type = new Integer(typeStr).intValue();
			if (type > 1) {
				//    			Columns 69-72   Control bus number
				setValue(12, str.substring(68, 72).trim());

				//        		Column  74      Side [I]
				setValue(13, str.substring(73, 74).trim());

				//        		Columns 106-111 Step size [F]
				setValue(18, str.substring(105, 111));

				//        		Columns 91-97   Maximum tap or phase shift [F]
				setValue(16, str.substring(90, 97));
				//        		Columns 98-104  Minimum tap or phase shift [F]
				setValue(17, str.substring(97, 104));

				//        		Columns 113-119 Minimum voltage, MVAR or MW limit [F]
				setValue(19, str.substring(112, 119));
				//        		Columns 120-126 Maximum voltage, MVAR or MW limit [F]
				setValue(20, str.substring(119, 126));
			}
		}
	}
}