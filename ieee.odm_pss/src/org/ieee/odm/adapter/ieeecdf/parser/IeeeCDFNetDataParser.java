/*
 * @(#)IeeeCDFNetDataParser.java   
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
import org.ieee.odm.model.base.ModelStringUtil;

/**
 * Class for processing IEEE CDF Network data line string
 * 
 * @author mzhou
 *
 */
public class IeeeCDFNetDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0          1          2       3        4
		     "Date", "Originator", "MVA", "Year", "Season", 
		   //  5
		     "CaseId"
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
			try {
				//Columns  2- 9   Date, in format DD/MM/YY with leading zeros.  If no date provided, use 0b/0b/0b where b is blank.
				setValue(0, str.substring(1, 9));
				//Columns 11-30   Originator's name [A]
				if (str.length() >= 30)
					setValue(1, str.substring(10, 30));
				//Columns 32-37   MVA Base [F] *
				if(str.length()<37){
					if (str.length() > 30)
						setValue(2, str.substring(31, str.length())); // in MVA
				}
				else{
					if (str.length() > 30)
						setValue(2, str.substring(31, 37)); // in MVA
					//Columns 39-42   Year [I]
					if (str.length() > 37)
						setValue(3, ModelStringUtil.getString(str, 38, 42));
					//Column  44      Season (S - Summer, W - Winter)
					if (str.length() > 42)
						setValue(4, ModelStringUtil.getString(str, 43, 44));
					//Column  46-73   Case identification [A]
					if (str.length() > 45)
						setValue(5, ModelStringUtil.getString(str, 46, 73));
				}
				
			} catch (Exception e) {
				throw new ODMException("Error: Network data line has problem, " + str + "\n" + e.toString());
			}
		}
	}
}