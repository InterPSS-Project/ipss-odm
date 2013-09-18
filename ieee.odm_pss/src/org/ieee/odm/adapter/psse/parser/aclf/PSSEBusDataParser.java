/*
 * @(#)PSSEBusDataParser.java   
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
public class PSSEBusDataParser extends BasePSSEDataParser {
	public PSSEBusDataParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V30
		 * 
		 *  I,   ’NAME’,      BASKV, IDE,  GL,      BL, AREA, ZONE, VM,      VA,     OWNER
		 * 
		 *  Format V32
		 * 
		 *  I,   ’NAME’,      BASKV, IDE,  AREA, ZONE, OWNER, VM,      VA
		 * 
		 *  
		 */
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",      "NAME",    "BASKV",    "IDE",     "GL",      
		   //  5          6          7          8          9
			  "BL",     "AREA",    "ZONE",     "VM",      "VA",
		   //  10       
			  "OWNER"
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		this.clearNVPairTableData();
		
		if (this.verion == PsseVersion.PSSE_26) {
			StringTokenizer st = new StringTokenizer(lineStr,",");
			for (int i = 0; i < 11; i++)
				setValue(i, st.nextToken().trim());
		}
		else if (this.verion == PsseVersion.PSSE_30 || 
				this.verion == PsseVersion.PSSE_32) {
			StringTokenizer st;

			// V30
			//    10001,'ALB_T4*     ',   1.0000,1,     0.000,     0.000,   1,   1,1.03259, -13.5044,   1
			// V32
			//    10001,'ALB_T4*     ',   1.0000,1,                         1,   1, 1, 1.03259, -13.5044
			
			// -- str1-- ----str2----- -----------str3---------------	
			String str1 = lineStr.substring(0, lineStr.indexOf('\'')),
			           strbuf = lineStr.substring(lineStr.indexOf('\'')+1),
			           str2 = strbuf.substring(0, strbuf.indexOf('\'')),
			           str3 = strbuf.substring(strbuf.indexOf('\'')+1);
				
			st = new StringTokenizer(str1, ",");
			setValue(0, st.nextToken().trim());
			setValue(1, str2);

			st = new StringTokenizer(str3, ",");
		    String s = st.nextToken().trim();
		    if (s.equals(""))   // in case there are spaces between '  ,
		        s = st.nextToken().trim();		
		    setValue(2,s);
		    
		    int cnt = 3;
	    	if (this.verion == PsseVersion.PSSE_32) {
	    		setValue(3, st.nextToken().trim());
	    		setValue(6, st.nextToken().trim());
	    		setValue(7, st.nextToken().trim());
	    		setValue(10, st.nextToken().trim());
	    		setValue(8, st.nextToken().trim());
	    		setValue(9, st.nextToken().trim());
	    	}
	    	else {
			    while(st.hasMoreTokens()) {
			    	setValue(cnt++, st.nextToken().trim());
			    }
	    	}
		}
		else
			throw new ODMException("PSSEBusDataParser, wrong PSSE Version " + this.verion);
	}
}