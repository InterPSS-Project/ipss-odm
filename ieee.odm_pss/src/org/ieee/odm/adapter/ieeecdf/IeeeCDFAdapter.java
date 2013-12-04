/*
 * @(#)IeeeCDFAdapter.java   
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.ieeecdf;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFBranchDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFBusDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFInterchangeDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFLossZoneDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFNetDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFTielineDataMapper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

/**
 * IEEE Common Format ODM parser implementation
 * 
 * @author mzhou
 *
 */
public class IeeeCDFAdapter  extends AbstractODMAdapter {
	/** data section indicator for the beginning */
	private static final int DataNotDefine = 0;
	/** Bus data section indicator */
	private static final int BusData = 1;
	/** Branch data section indicator */
	private static final int BranchData = 2;
	/** Loss zone data section indicator */
	private static final int LossZone = 3;
	/** inter-change data section indicator */
	private static final int InterchangeData = 4;
	/** Tie line data section indicator */
	private static final int TielineData = 5;

	/** Bus data line parser */
	private IeeeCDFBusDataMapper busDataMapper = new IeeeCDFBusDataMapper();
	/** Branch data line parser */
	private IeeeCDFBranchDataMapper branchDataMapper = new IeeeCDFBranchDataMapper();
	/** Network data (head line) line parser */
	private IeeeCDFNetDataMapper netDataMappe = new IeeeCDFNetDataMapper();
	/** Loss zone data line parser */
	private IeeeCDFLossZoneDataMapper zoneDataMapper = new IeeeCDFLossZoneDataMapper();
	/** inter-exchange data line parser */
	private IeeeCDFInterchangeDataMapper exchangeDataMapper = new IeeeCDFInterchangeDataMapper();
	/** tie line data line parser */
	private IeeeCDFTielineDataMapper tieLineDataMapper = new IeeeCDFTielineDataMapper();
	
	/**
	 * constructor
	 */
	public IeeeCDFAdapter() {
		super();
	}
	 
	@Override protected AclfModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
		// IEEE CDF file stores Aclf Network info 
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.initCaseContentInfo(OriginalDataFormatEnumType.IEEE_CDF);

		LoadflowNetXmlType baseCaseNet = parser.getNet();
		baseCaseNet.setId("Base_Case_from_IEEECDF_format");

		// read the first line - head line
		// sample :  
		//       08/19/93 UW ARCHIVE           100.0  1962 W IEEE 14 Bus Test Case
		String str = din.readLine();
		netDataMappe.mapInputLine(str, parser);

		int dataLineIndicator = DataNotDefine;
		do {
			str = din.readLine(); 
			// NOTE: Some data file misses the "END OF DATA" string at the end of the file, which may cause a problem
			if(str!=null){
			    if (str.trim().equals("END OF DATA")) 
			    	break;
				try {
					/*
					 * process section head record - for example
					 * 
					 *   BUS DATA FOLLOWS  
					 */
					if ((str.length() > 3)
							&& str.substring(0, 3).equals("BUS")) {
						dataLineIndicator = BusData;
						ODMLogger.getLogger().fine("load bus data");
					} 
					else if ((str.length() > 6)
							&& str.substring(0, 6).equals("BRANCH")) {
						dataLineIndicator = BranchData;
						ODMLogger.getLogger().fine("load branch data");
					} 
					else if ((str.length() > 4)
							&& str.substring(0, 4).equals("LOSS")) {
						dataLineIndicator = LossZone;
						ODMLogger.getLogger().fine("load loss zone data");
					} 
					else if ((str.length() > 11)
							&& str.substring(0, 11).equals("INTERCHANGE")) {
						dataLineIndicator = InterchangeData;
						ODMLogger.getLogger().fine("load interchange data");
					} 
					else if ((str.length() > 3)
							&& str.substring(0, 3).equals("TIE")) {
						dataLineIndicator = TielineData;
						ODMLogger.getLogger().fine("load tieline data");
					}

					/*
					 * data section end indicator line processing
					 */
					else if (str.startsWith("-999") || str.startsWith("-99") || str.startsWith("-9")) {
						dataLineIndicator = DataNotDefine;
					} 

					/*
					 * parse data line
					 */
					else if (dataLineIndicator == BusData) {
						busDataMapper.mapInputLine(str, parser);
					} 
					else if (dataLineIndicator == BranchData) {
						branchDataMapper.mapInputLine(str, parser);
					} 
					else if (dataLineIndicator == LossZone) {
						zoneDataMapper.mapInputLine(str, parser);
					} 
					else if (dataLineIndicator == InterchangeData) {
						exchangeDataMapper.mapInputLine(str, parser);
					} 
					else if (dataLineIndicator == TielineData) {
						tieLineDataMapper.mapInputLine(str, parser);
					} 
				} catch (final Exception e) {
					ODMLogger.getLogger().severe(e.toString() + "\n" + str);
					//e.printStackTrace();
				}
			}
		} while (str!=null);

		return parser;
	}

	/**
	 * IEEE CDF does not support multiple files
	 */
	protected IODMModelParser parseInputFile(IODMAdapter.NetType type, final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}
}