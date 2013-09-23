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

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFBranchDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFBusDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFInterchangeDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFLossZoneDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFNetDataMapper;
import org.ieee.odm.adapter.ieeecdf.mapper.IeeeCDFTielineDataMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.ObjectFactory;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PowerInterchangeXmlType;

public class IeeeCDFAdapter  extends AbstractODMAdapter {
	private static final int BusData = 1;
	private static final int BranchData = 2;
	private static final int LossZone = 3;
	private static final int InterchangeData = 4;
	private static final int TielineData = 5;

	//private ObjectFactory factory = null;

	private IeeeCDFBusDataMapper busDataMapper = new IeeeCDFBusDataMapper();
	private IeeeCDFBranchDataMapper branchDataMapper = new IeeeCDFBranchDataMapper();
	private IeeeCDFNetDataMapper netDataMappe = new IeeeCDFNetDataMapper();
	private IeeeCDFLossZoneDataMapper zoneDataMapper = new IeeeCDFLossZoneDataMapper();
	private IeeeCDFInterchangeDataMapper exchangeDataMapper = new IeeeCDFInterchangeDataMapper();
	private IeeeCDFTielineDataMapper tieLineDataMapper = new IeeeCDFTielineDataMapper();
	
	public IeeeCDFAdapter() {
		super();
		//this.factory = new ObjectFactory();		
	}
	 
	@Override
	protected AclfModelParser parseInputFile(
			final IFileReader din, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.initCaseContentInfo(OriginalDataFormatEnumType.IEEE_CDF);

		LoadflowNetXmlType baseCaseNet = parser.getNet();
		baseCaseNet.setId("Base_Case_from_IEEECDF_format");

		String str = din.readLine();
		netDataMappe.mapInputLine(str, baseCaseNet);

		int dataType = 0;
		do {
			str = din.readLine(); //kvaBase
			//NOTE: Some data miss the "END OF DATA" string at the end of the file, which may cause a problem
			if(str!=null){
			    if (str.trim().equals("END OF DATA")) break;
				try {
					// process the data
					if (str.startsWith("-999") || str.startsWith("-99")
							|| str.startsWith("-9")) {
						dataType = 0;
					} else if (dataType == BusData) {
						busDataMapper.mapInputLine(str, parser);
					} else if (dataType == BranchData) {
						branchDataMapper.mapInputLine(str, parser);
					} else if (dataType == LossZone) {
						zoneDataMapper.processLossZoneData(str, parser.createNetworkLossZone());
					} else if (dataType == InterchangeData) {
						InterchangeXmlType interchange = parser.createInterchange();
						PowerInterchangeXmlType p = odmObjFactory.createPowerInterchangeXmlType();
						exchangeDataMapper.processInterchangeData(str, p, parser);
						interchange.setPowerEx(p);
					} else if (dataType == TielineData) {
						tieLineDataMapper.processTielineData(str, parser.createTieline(), parser);
					} else if ((str.length() > 3)
							&& str.substring(0, 3).equals("BUS")) {
						dataType = BusData;
						ODMLogger.getLogger().fine("load bus data");
					} else if ((str.length() > 6)
							&& str.substring(0, 6).equals("BRANCH")) {
						dataType = BranchData;
						ODMLogger.getLogger().fine("load branch data");
					} else if ((str.length() > 4)
							&& str.substring(0, 4).equals("LOSS")) {
						dataType = LossZone;
						//baseCaseNet.addNewLossZoneList();
						ODMLogger.getLogger().fine("load loss zone data");
					} else if ((str.length() > 11)
							&& str.substring(0, 11).equals("INTERCHANGE")) {
						dataType = InterchangeData;
						//baseCaseNet.addNewInterchangeList();
						ODMLogger.getLogger().fine("load interchange data");
					} else if ((str.length() > 3)
							&& str.substring(0, 3).equals("TIE")) {
						dataType = TielineData;
						//baseCaseNet.addNewTieLineList();
						ODMLogger.getLogger().fine("load tieline data");
					}
				} catch (final Exception e) {
					ODMLogger.getLogger().severe(e.toString() + "\n" + str);
					e.printStackTrace();
				}
			}
		
			  
		} while (str!=null);

		return parser;
	}

	protected IODMModelParser parseInputFile(IODMAdapter.NetType type, final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}
}