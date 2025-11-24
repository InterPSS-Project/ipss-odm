/*
 * @(#)GEOwnerDataParser.java   
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

package org.ieee.odm.adapter.ge.parser;

import java.util.StringTokenizer;

import org.ieee.odm.common.ODMException;

/**
 * Class for processing GE PSLF owner data line string
 * 
 * @author mzhou
 *
 */
public class GEOwnerDataParser extends BaseGEDataParser {
	@Override public String[] getMetadata() {
		/*
		 * V15
		 * 
		 * 		<owner no> <"oname"> <"s name"> <net_mw> <net_mvar> <sch_mw> <sch_mvar> <ar>
		 * 
	   Sample Data
       1 "                                " "O1  " :       0.00       0.00       0.00       0.00   0
		 
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
		     "owner_no", "oname",  "s_name",  "net_mw",  "net_mvar",    
		   //  5            6          7
		     "sch_mw",   "sch_mvar",  "ar" 
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		String str1 = lineStr.substring(0, lineStr.indexOf(':')),
		       str2 = lineStr.substring(lineStr.indexOf(':')+1);

		int cnt = 0;
		//System.out.println("owner->" + lineStr);
		StringTokenizer st = new StringTokenizer(str1, "\"");
		String ownerNo = st.nextToken().trim();
		setValue(cnt++, ownerNo);		
		String oname = st.nextToken();
		setValue(cnt++, oname);
		st.nextToken();
		String sname = st.nextToken();
		setValue(cnt++, sname);
		
		int m = 3;

		//        0.00       0.00       0.00       0.00   0
		st = new StringTokenizer(str2);
		cnt = m;
		while(st.hasMoreElements())
			setValue(cnt++, st.nextToken());
	}
}