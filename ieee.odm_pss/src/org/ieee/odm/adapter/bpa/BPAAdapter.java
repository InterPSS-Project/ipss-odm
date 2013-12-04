/*
 * @(#)BPAAdapter.java   
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
 * @Author Stephen Hou
 * @Version 1.0
 * @Date 08/11/2008
 * 
 *   Revision History
 *   ================
 *   
 *   modified for Jaxb Mike Zhou 02/28/2011
 *
 */

package org.ieee.odm.adapter.bpa;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.dynamic.BPADynamicRecord;
import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.adapter.bpa.lf.BPALoadflowRecord;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

/**
 * BPA adapter is design to handle Loadflow data file and Loadflow+TransienStability data files
 * 
 * @author mzhou
 *
 */
public class BPAAdapter  extends AbstractODMAdapter {
	public final static String Token_CaseType = "Type";
	public final static String Token_ProjectName = "Original Project Name";
	public final static String Token_CaseId = "Case Identification";
	public final static String Token_BN="Bus Name";
	
	public BPAAdapter() {
		super();
		BPABusRecord.resetBusCnt();
	}
	
	protected IODMModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.LOADFLOW);
		parser.initCaseContentInfo(OriginalDataFormatEnumType.BPA);

		String str="";
		// first line, as a sign to run power flow data or transient data
		// there may be comments starting with . or blank line
		do{
			str = din.readLine();
		} while (str.startsWith(".") || str.trim().equals(""));  // bypass lines starts with . or blank lines
		
		if(str.equals("loadflow") || str.contains("POWERFLOW")){
			new BPALoadflowRecord<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>().processLfData(parser, din);			
			return parser;
		}
		throw new ODMException("Only LF data file could be prcessed by this method");
	}
	
	/**
	 * For transient stability case, it is assumed that two FileReader objects are passed in, the first representing Loadflow file;
	 * the second containing DStab info 
	 * 
	 */
	protected IODMModelParser parseInputFile(IODMAdapter.NetType type, final IFileReader[] dinAry, String encoding) throws ODMException {
		if (type == IODMAdapter.NetType.DStabNet) {
			DStabModelParser parser = new DStabModelParser(encoding);
			parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.TRANSIENT_STABILITY);
			parser.initCaseContentInfo(OriginalDataFormatEnumType.BPA);
			
			//PerformanceTimer timer = new PerformanceTimer(ODMLogger.getLogger());

			// parse Loadflow file
			//timer.start();
			String str="";
			IFileReader din = dinAry[0];
			do{
				str = din.readLine();
			} while (str.startsWith(".") || str.trim().equals(""));
			
			if(str.equals("loadflow") || str.contains("POWERFLOW")){
				new BPALoadflowRecord<DStabNetXmlType, DStabBusXmlType, LineDStabXmlType, XfrDStabXmlType, PSXfrDStabXmlType>().processLfData(parser, din);			
			}
			//timer.logStd("Load LF data");
			
			//timer.start();
			// parse DStab file
			din = dinAry[1];
			BPADynamicRecord.processDynamicData(din, parser);
			//timer.logStd("Load DStab data");

			return parser;
		}
		throw new ODMException("Only IODMAdapter.NetType.DStabNet type could be processed by the method");
	}
}
