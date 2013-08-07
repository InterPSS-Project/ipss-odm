 /*
  * @(#)GENetDatMapper.java   
  *
  * Copyright (C) 2006-2008 www.interpss.org
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
  * @Date 06/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.ge.mapper;

import java.util.StringTokenizer;

import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.LoadflowNetXmlType;

public class GENetDatMapper {
	static public class TitleRec {
		public void processLineStr(String lineStr, GePslfAdapter.Version version, AclfModelParser parser) {
			LoadflowNetXmlType baseCaseNet = parser.getNet();
			BaseJaxbHelper.addNVPair(baseCaseNet, "Title", lineStr);
		}
	}
	
	static public class CommentsRec {
		public String comments = "";

		public void processLineStr(String lineStr, GePslfAdapter.Version version, AclfModelParser parser) {
			LoadflowNetXmlType baseCaseNet = parser.getNet();
			BaseJaxbHelper.addNVPair(baseCaseNet, "Comments", lineStr);
		}
	}

	/*
	 */	
	static public class SolutionParamRec {

		public void processLineStr(String lineStr, GePslfAdapter.Version version, AclfModelParser parser) {
			LoadflowNetXmlType baseCaseNet = parser.getNet();
			int tap, phas, area, svd, dctap, gcd;
			double jump, toler;

			//System.out.println("solutionParam->" + lineStr);
			StringTokenizer st = new StringTokenizer(lineStr);
			st.nextElement();
			String str = st.nextToken();
			if (lineStr.startsWith("tap"))
				tap = new Integer(str).intValue();
			else if (lineStr.startsWith("phas"))
				phas = new Integer(str).intValue();
			else if (lineStr.startsWith("area"))
				area = new Integer(str).intValue();
			else if (lineStr.startsWith("svd"))
				svd = new Integer(str).intValue();
			else if (lineStr.startsWith("dctap"))
				dctap = new Integer(str).intValue();
			else if (lineStr.startsWith("gcd"))
				gcd = new Integer(str).intValue();
			else if (lineStr.startsWith("jump"))
				jump = new Double(str).doubleValue();
			else if (lineStr.startsWith("toler"))
				toler = new Double(str).doubleValue();
			else if (lineStr.startsWith("sbase")) {
				double sbase = new Double(str).doubleValue();
				baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(sbase));
			}
		}
	}

	public static void setDates(StringTokenizer st, String d_in, String d_out, String projId) {
		if (st.hasMoreElements())
			d_in = st.nextToken();
		if (st.hasMoreElements())
			d_out = st.nextToken();
		if (st.hasMoreElements())
			projId = st.nextToken();
	}		
}
