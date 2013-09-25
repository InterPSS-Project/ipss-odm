 /*
  * @(#)PSSEHeaderDataMapper.java   
  *
  * Copyright (C) 2006 www.interpss.org
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
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.psse.mapper.aclf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEHeaderDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEHeaderDataMapper <
	TNetXml extends NetworkXmlType, 
	TBusXml extends BusXmlType,
	TLineXml extends BranchXmlType,
	TXfrXml extends BranchXmlType,
	TPsXfrXml extends BranchXmlType>  extends BasePSSEDataMapper {
	
	public PSSEHeaderDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEHeaderDataParser(ver);
	}
	
	public void procLineString(String[] lineStrAry, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		
		if (PSSEAdapter.getVersionNo(this.version) >= 31) {
			dataParser.parseFields(lineStrAry);
			
			/*
		   //  0----------  1----------2---------- 3----------4
	 	  "Indicator",  "BaseKva",  "version",  "XFRRAT", "NXFRAT",
		   //  5----------  6----------7---------- 3----------4
	 	  "BASFRQ",      "Comment1", "Comment2"
	 	   */
			
			double baseMva = dataParser.getDouble("BaseKva");
			baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
			BaseJaxbHelper.addNVPair(baseCaseNet, "CaseIndicator", dataParser.getString("Indicator"));
			String ver = dataParser.getString("version");
			
			baseCaseNet.setDesc(lineStrAry[0]);
			
			baseCaseNet.setName("AclfNet-PSSE-V" + ver);
			
			String str = new Integer(PSSEAdapter.getVersionNo(this.version)).toString();
			if (!ver.contains(str))
				throw new ODMException("PSSE Adapter version and input file has different version, adapter: " + this.version + "  file: " + ver);
		}
		else {
			String lineStr = lineStrAry[0];
			StringTokenizer st = new StringTokenizer(lineStr, ",");
			int indicator = new Integer(st.nextToken().trim()).intValue();
			// at here we have "100.00 / PSS/E-29.0 THU, JUN 20 2002 14:19"
			st = new StringTokenizer(st.nextToken(), "/");

			double baseMva = new Double(st.nextToken().trim()).doubleValue();
			baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
			BaseJaxbHelper.addNVPair(baseCaseNet, "CaseIndicator", new Integer(indicator).toString());
			
			// The 2nd line is treated as description
			baseCaseNet.setDesc(lineStrAry[1]);
			
			// the 3rd line is treated as the network id and network name
			baseCaseNet.setName(lineStrAry[2]);
			
			for (String s : lineStrAry)
				filterVersion(s);
		}
	}
	
	public ODMFileFormatEnum getVersion(String filename) throws ODMException {
/* 
*   Sample header lines
* 
==========
0    100.00     / RAW29 (REV 30)  THU, JUL 31 2008  13:42
                           100.0                        
==========                                                            
                                                        
==========                                                            
0,   100.00, 30, 0, 1,  60.00     / PSS(R)E 30 RAW created by rawd30  SAT, FEB 05 2011  18:16
PSS(R)E SAMPLE CASE
ALL DATA CATEGORIES WITH SEQUENCE DATA
==========

==========
0,100.0
20110729120000,CASE_D:033111-EMSDB:DB53,CASE:012411-EMSDB:DB52, 10
VER 26   PARAMETERS INITIALIZED ON 22-Jun-2011 16:45:56 PDT
==========                                                            
*/
		
		final File file = new File(filename);
		try (BufferedReader din = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String lineStr = null;
			int lineNo = 0;
	      	do {
	      		lineStr = din.readLine();
	      		if (lineStr.contains("VER 26"))
	      			return ODMFileFormatEnum.PsseV26;
	      		else if (lineStr.contains("RAW29"))
	      			return ODMFileFormatEnum.PsseV30;
	      		lineNo++;
	    	} while (lineNo < 3);
		} catch (IOException e) {
			throw new ODMException(e.toString());
		}			
		return ODMFileFormatEnum.PsseV30;
	}
	
	private void filterVersion(String lineStr) {
  		if (lineStr.contains("VER 26"))
  			this.version = PsseVersion.PSSE_26;
  		else if (lineStr.contains("RAW29"))
  			this.version = PsseVersion.PSSE_29;
	}
}
