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

package org.ieee.odm.adapter.psse.raw.parser.aclf;

import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E Transformer impedance table data
 * 
 * @author mzhou
 *
 */
public class PSSEXfrZTableDataRawParser extends BasePSSEDataRawParser {
	protected int detectedCorrectionFactors = 0; // Initialize the detected correction factors
	public PSSEXfrZTableDataRawParser(PsseVersion ver) {
		super(ver);
	}	
	
	@Override public String[] getMetadata() {
	    /* 
		 * format V29, V30, V32, V33
		 * 
		 *       I, T1, F1, T2, F2, T3, F3, ... T11, F11
		 * 
		*/

		
	    String[] v29_33 = new  String[] {
		//  0----------1----------2----------3----------4
					"I",      "T1",      "F1",      "T2",       "F2", 
				//  0----------1----------2----------3----------4
		"T3",     "F3",      "T4",      "F4",       "T5",     
				//  0----------1----------2----------3----------4
		"F5",     "T6",      "F6",      "T7",       "F7",  
				//  0----------1----------2----------3----------4	  
		"T8",      "F8",     "T9",     "F9",      "T10",         
				//  0----------1----------2----------3----------4	  
		"F10",      "T11",  "F11"
	};

	    // Dynamically generate metadata for v34 to v36
	    if (PSSERawAdapter.getVersionNo(this.version) >= 34) {
	        int maxFactors = 12; // Default maximum number of correction factors

	        // Check if more correction factors are detected (this logic can be customized)
	        if (this.detectedCorrectionFactors > maxFactors) {
	            maxFactors = this.detectedCorrectionFactors;
	        }

	        // Dynamically generate metadata for v34 to v36
	        List<String> metadata = new ArrayList<>();
	        metadata.add("I");
	        for (int i = 1; i <= maxFactors; i++) {
	            metadata.add("T" + i);
	            metadata.add("RE(F" + i + ")");
	            metadata.add("IM(F" + i + ")");
	        }
	        return metadata.toArray(new String[0]);
	    }

	    // Return default metadata for v29 to v33
	    return v29_33;
	}
	
	@Override public void parseFields(final String[] strAry) throws ODMException {
		this.clearNVPairTableData();

		 
		 String lineStr1 = strAry[0];
		

		 if(PSSERawAdapter.getVersionNo(this.version) < 34) {
			int expectedLine1Num = 23;
			parseNumericalOnlyLineStr(lineStr1, 0, expectedLine1Num);
		 }
		 else{ // v34 and above
			// first calculate the total cocorrectionctors
			int totalValues = 0;
			for (String str : strAry) {
				totalValues += str.split(",").length;
			}
			this.detectedCorrectionFactors = (totalValues - 1) / 3; // 3 values per correction factor (T, RE(F), IM(F))

			initializeMetadata(); // Call after detected correction factors are set

			//line 1
			int startingIdx = 0;
			for (String lineStr : strAry) {
				int fieldCount = parseNumericalOnlyLineStr(lineStr, startingIdx);
				startingIdx += fieldCount;
			}
  		}
	}
}