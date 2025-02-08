/*
 * @(#)PSSEHeaderDataParser.java   
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
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public class PSSEHeaderDataRawParser extends BasePSSEDataRawParser {
	public PSSEHeaderDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V26
		 * 
0,100.0
  20090212163844,CASE:110408-EMSDB:DB38,CASE:081908-EMSDB:DB36, 10
VER 26   PARAMETERS INITIALIZED ON 28-Jan-2009 09:56:32 PST
		 * 
		 * 
		 * Format V30
		 * 
		 * 	 String[0] indicator
	     *   String[1] baseKav
	     *   String[2] comments
	     *   String[3] comments
		 * 	  
		 *  format V32, V33
		 *  Case identification data consists of three data records. The first record contains six Items of data
		 *  
              IC , SBASE , REV, XFRRAT, NXFRAT , BASFRQ
              
              IC,  Indicator
              SBASE, 
              REV,    PSS/E version
              XFRRAT, 
              NXFRAT, 
              BASFRQ, base freq
              
		 * 	 String[0] ...
	     *   String[2] comments
	     *   String[3] comments         
	     *   
	     *   Sample line-1 : 0,   100.00, 32, 0, 1, 60.00     / PSS®E-32.0    THU, FEB 28 2013   7:24     
		 */
		/*
		 */
		return new String[] {
		   //  0----------  1----------2---------- 3----------4
	 	  "Indicator",  "BaseKva",  "version",  "XFRRAT", "NXFRAT",
		   //  5----------  6----------7---------- 3----------4
	 	  "BASFRQ",      "Comment1", "Comment2"
		};
	}
	
	@Override public void parseFields(final String[] lineAry) throws ODMException {
		this.clearNVPairTableData();
		
		String lineStr = lineAry[0];
		String lineStr2 = lineAry[1];
		String lineStr3 = lineAry[2];
		
		 // Remove anything after "/" if it exists
        int slashIndex = lineStr.indexOf('/');
        if (slashIndex != -1) {
        	lineStr = lineStr.substring(0, slashIndex).trim();
        }

		
		if (this.version == PsseVersion.PSSE_26) {
			StringTokenizer st = new StringTokenizer(lineStr, ",");
			setValue(0, st.nextToken());
			setValue(1, st.nextToken().trim()); 
			setValue(2, "VER 26");
			setValue(6, lineStr2);
			setValue(7, lineStr3);			
		}
		else {
			StringTokenizer st = new StringTokenizer(lineStr, ",");
			
			String ind = st.nextToken().trim();  			   
			int indicator = new Integer(ind).intValue();
			if (indicator !=0){
				throw new ODMException("Error: Only base case can be process");
			}
			setValue(0, ind);

			setValue(1, st.nextToken().trim());  			   
			setValue(2, st.nextToken().trim());   // version
			if(st.hasMoreTokens())
			   setValue(3, st.nextToken().trim());
			if(st.hasMoreTokens())
			   setValue(4, st.nextToken().trim()); 
			if(st.hasMoreTokens())
			   setValue(5, st.nextToken().trim());  //"BASFRQ"
			
			if (lineStr2!= null){
				setValue(6, lineStr2);
			}
			
			if (lineStr3!= null){
				setValue(7, lineStr3);
			}			
		}
  	}
}