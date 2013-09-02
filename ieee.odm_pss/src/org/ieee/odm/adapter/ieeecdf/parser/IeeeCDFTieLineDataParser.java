/*
 * @(#)IeeeCDFTieLineDataParser.java   
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
import org.ieee.odm.common.ODMLogger;

/**
 * Class for processing IEEE CDF Network data line string
 * 
 * @author mzhou
 *
 */
public class IeeeCDFTieLineDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0                    1             2                   3                   4
		     "MeteredBusNum", "MeteredAreaNum", "NotMeteredBusNum", "NotMeteredAreaNum", "CirNum", 
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
			//        	Columns  1- 4   Metered bus number [I] *
			//        	Columns  7-8    Metered area number [I] *
			setValue(0, str.substring(0, 4).trim());
			setValue(1, str.substring(6, 8).trim());

			//          Columns  11-14  Non-metered bus number [I] *
			//          Columns  17-18  Non-metered area number [I] *
			setValue(2, str.substring(10, 14).trim());
			setValue(3, str.substring(16, 18).trim());

			//          Column   21     Circuit number
			int cirColNum =21;
			String cirIdString  =str.substring(20, cirColNum).trim();
			// length after triming ending blanks
//			int length = str.replaceAll("\\s+$", "").length();
//			if(length>cirColNum) {
//				cirColNum = length;
//			    cirIdString  =str.substring(20, length).trim();
//			    ODMLogger.getLogger().warning("TieLine CirId is not in Column 21,Tieline str = "+str+",parsing cirId = "+cirIdString);
//			}
			setValue(4, cirIdString);
		}
	}
}