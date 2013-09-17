/*
 * @(#)PSSEXfrZTableDataParser.java   
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
public class PSSEXfrZTableDataParser extends BasePSSEDataParser {
	public PSSEXfrZTableDataParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
		/* 
		 * format V30, V32
		 * 
		 *       I, T1, F1, T2, F2, T3, F3, ... T11, F11
		 * 
		*/
		return new String[] {
		   //  0----------1----------2----------3----------4
			  "I",      "T1",      "F1",      "T2",       "F2", 
		   //  0----------1----------2----------3----------4
			  "T3",     "F3",      "T4",      "F4",       "T5",     
		   //  0----------1----------2----------3----------4
			  "F5",     "T6",      "F6",      "T7",       "F8",  
		   //  0----------1----------2----------3----------4	  
			  "T9",     "F9",      "T10",     "F10",       "T11",     
		   //  0----------1----------2----------3----------4	  
			  "F11"           
		};
	}
	
	@Override public void parseFields(final String lineStr) throws ODMException {
		StringTokenizer st = new StringTokenizer(lineStr, ",");
		
		int cnt = 0;
		while (st.hasMoreTokens())
			this.setValue(cnt++, st.nextToken().trim());			
  	}
}