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
public class PSSEHeaderDataParser extends BasePSSEDataParser {
	public PSSEHeaderDataParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* Format V30
		 * 
		 * 	 String[0] indicator
	     *   String[1] baseKav
	     *   String[2] comments
	     *   String[3] comments
		 * 	  
		 *  format V32
		 *  Case identification data consists of three data records. The first record contains six Items of data
		 *  
              IC , SBASE , REV, XFRRAT, NXFRAT , BASFRQ
		 */
		return new String[] {
		   //  0----------  1----------2---------- 3----------4
			  "Indicator", "BaseKva", "Comment1", "Comment2"
		};
	}
	
	@Override public void parseFields(final String[] lineAry) throws ODMException {
		this.clearNVPairTableData();
		
		String lineStr = lineAry[0];
		String lineStr2 = lineAry[1];
		String lineStr3 = lineAry[2];
		
		//line 1 at here we have "0, 100.00 " or some times "0 100.00 "		
		StringTokenizer st = lineStr.contains(",") ?
				new StringTokenizer(lineStr, ",") :
				new StringTokenizer(lineStr);
		
		String ind = st.nextToken();  			   
		int indicator = new Integer(ind).intValue();
		if (indicator !=0){
			throw new ODMException("Error: Only base case can be process");
		}
		setValue(0, ind);

		setValue(1, st.nextToken().trim());  			   
		
		if (lineStr2!= null){
			setValue(2, lineStr2);
		}
		
		if (lineStr3!= null){
			setValue(3, lineStr3);
		}
  	}
}