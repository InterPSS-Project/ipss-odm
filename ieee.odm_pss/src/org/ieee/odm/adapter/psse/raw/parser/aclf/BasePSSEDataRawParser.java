/*
 * @(#)BasePSSEDataParser.java   
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

import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing IEEE CDF bus data line string
 * 
 * @author mzhou
 *
 */
public abstract class BasePSSEDataRawParser extends AbstractStringDataFieldParser {
	   //  0----------1----------2----------3----------4
	   //  5          6          7          8          9
	   //  10         11         12         13         14
	   //  15         16         17         18         19
	   //  20         21         22         23         24
	   //  25         26         27         28         29
	   //  30         31         32         33         34
	   //  35         36         37         38         39
	   //  40         41         42         43         44
	   //  45         46         47         48         49
		  
	protected PsseVersion version = PsseVersion.PSSE_30;
	
	public BasePSSEDataRawParser() {
		super();
		initializeMetadata(); 
	}

	public BasePSSEDataRawParser(PsseVersion ver) {
		super();
		this.version = ver;
		initializeMetadata(); 
	}
	
	
	@Override public void parseFields(final String str) throws ODMException {
		
		this.clearNVPairTableData();
		
		
		 List<Object> dataList = new ArrayList<>();
         boolean insideQuotes = false;
         StringBuilder field = new StringBuilder();
         List<String> splitData = new ArrayList<>();
         
         int expectedFieldCount = getMetadata().length;
         String tempStr = str;
         if (str.contains("/")) { // remove comments after "/"
        	 tempStr = str.replaceAll("(?<!\\d)/\\s.*", "");
	       }

         for (char c : tempStr.toCharArray()) {
             if (c == '\'') {
                 insideQuotes = !insideQuotes;
                 field.append(c);
             } else if (c == ',' && !insideQuotes) {
                 splitData.add(field.toString());
                 field.setLength(0);
             } else {
                 field.append(c);
             }
         }
         splitData.add(field.toString().trim()); // the last field
         

           int idx = 0;
           for (String data : splitData) {
               data = data.trim();
               if (data.contains("\'")) {
            	   setValue(idx, (data.replace("'", "").trim()));
               }
               else {
            	   setValue(idx, data);
               }
               idx++;
           }
           // 
           while (idx < expectedFieldCount) {
               //dataList.add(null);
               setValue(idx, "");
               idx++;
           }
	}
}