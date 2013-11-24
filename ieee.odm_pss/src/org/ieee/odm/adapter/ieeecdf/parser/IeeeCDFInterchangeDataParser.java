/*
 * @(#)IeeeCDFInterchangeDataParser.java   
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
 * Class for processing IEEE CDF Network data line string
 * 
 * @author mzhou
 *
 */
public class IeeeCDFInterchangeDataParser extends AbstractDataFieldParser {
	@Override public String[] getMetadata() {
		return new String[] {
		   //  0               1              2                   3            4
		     "AreaNum",   "SwingBusNum", "AltSwingBusName",  "ExportMw", "ExTolerance", 
		   //  5               6           
		     "AreaCode",  "AreaName" 
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
			//        	Columns  1- 2   Area number [I], no zeros! *
			setValue(0, str.substring(0, 2).trim());

			//        	Columns  4- 7   Interchange slack bus number [I] *
			//          Columns  9-20   Alternate swing bus name [A]
			setValue(1, str.substring(3, 7).trim());
			setValue(2, str.substring(8, 20).trim());

			//          Columns 21-28   Area interchange export, MW [F] (+ = out) *
			//          Columns 30-35   Area interchange tolerance, MW [F] *
			setValue(3, str.substring(20, 28));
			setValue(4, str.substring(29, 35));

			//          Columns 38-43   Area code (abbreviated name) [A] *
			//          Columns 46-75   Area name [A]
			setValue(5, str.substring(37, 43));
			if(str.length()>=75)
			    setValue(6, str.substring(45,75).trim());
			else 
				setValue(6, str.substring(45).trim());
		}
	}
}