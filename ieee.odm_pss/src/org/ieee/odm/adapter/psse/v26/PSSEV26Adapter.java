/*
 * @(#)PSSEAdapter.java   
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
 * @Author Stephen Hau, Mike Zhou
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.adapter.psse.v26;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEOwnerDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSESwitchedSShuntDataMapper;
import org.ieee.odm.adapter.psse.mapper.aclf.PSSEZoneDataMapper;
import org.ieee.odm.adapter.psse.v26.impl.PSSEV26BranchRecord;
import org.ieee.odm.adapter.psse.v26.impl.PSSEV26BusRecord;
import org.ieee.odm.adapter.psse.v26.impl.PSSEV26NetRecord;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

public class PSSEV26Adapter extends AbstractODMAdapter{
	private final int 
		BusData = 1,       // constants in indicating data block positions
		LoadData = 2,
		GenData = 3,
		BranchData = 4,
		XfrAdjData = 5,
		InterchangeData = 6,
		//DcLine2TData = 7,
		SwitchedShuntData = 8,
		//XfrDataTableData = 9,
		//DcLineMTData = 10,
		//MultiSecLineData = 11,
		ZoneData = 12,
		InterAreaTransferData = 13,
		OwnerData = 14;
		//FactsData = 15;
	
	PSSEZoneDataMapper zoneDataMapper = new PSSEZoneDataMapper(PsseVersion.PSSE_26);
	PSSEOwnerDataMapper ownerDataMapper = new PSSEOwnerDataMapper(PsseVersion.PSSE_26);
	
	PSSEV26NetRecord netRecProcessor = new PSSEV26NetRecord();
	PSSEV26BusRecord busRecProcessor = new PSSEV26BusRecord(PsseVersion.PSSE_26);
	PSSESwitchedSShuntDataMapper switchedShuntDataMapper = new PSSESwitchedSShuntDataMapper(PsseVersion.PSSE_26);
	PSSEV26BranchRecord branchRecProcessor = new PSSEV26BranchRecord();
		
	public PSSEV26Adapter() {
		super();
		//this.factory = new ObjectFactory();
	}
	
	@Override
	protected AclfModelParser parseInputFile(
			final IFileReader din, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser();
		parser.initCaseContentInfo(OriginalDataFormatEnumType.PSS_E);
		parser.getStudyCase().getContentInfo().setOriginalFormatVersion("PSSEV26");

		LoadflowNetXmlType baseCaseNet = parser.getNet();
		// no space is allowed for ID field
		baseCaseNet.setId("Base_Case_from_PSS_E_format_Ver26");

		//read header info
		String sAry[]= new String[5];
		for (int i = 0; i < 3; i++ ) {			
			String str = din.readLine();
			sAry[i]= str;				
		} 
		netRecProcessor.processHeaderData(sAry[0],sAry[1],sAry[2],baseCaseNet);	

        String str ;         
        int type=BusData;
        do{
        	str = din.readLine();

        	if (str != null){
            	//  a line string in version 26 has the following format
            	// 31212,'ADLIN  1', 115.00,1,    0.00,    0.00,  1,  1,1.01273, -10.5533,1,    /* [31212_MPE     _115_B1] */
            	// we need to get rid of the tailing comment part
            	int pos = str.indexOf("/*");
            	if (pos > 0)
            		str = str.substring(0, str.indexOf("/*"));
            	// some time, one might see 9999.0,9999.0,, .... The ,, will cause problem. Therefore, add a space in
            	// between to avoid the issue.
            	str = str.replaceAll(",,", ", ,");
            	
        		try {	         	 
        			if (str.startsWith("0 /")){
        				type++;
        			}
        			else {
        				if (type==BusData){
        					//System.out.println("BusData: " + str);
        					busRecProcessor.processBusData(str, parser);
        				}
        				else if(type==LoadData){
        					//System.out.println("LoadData: " + str);
        					busRecProcessor.processLoadData(str, parser);
        				}
        				else if(type==GenData){
        					//System.out.println("GenData: " + str);
        					busRecProcessor.processGenData(str, parser);        			 
        				}
        				else if(type==BranchData){
        					//System.out.println("LineData: " + str);
        					branchRecProcessor.processBranchData(str, parser); 
        				}
        				else if(type==XfrAdjData){        			   
        					//System.out.println("XfrData: " + str);
        					branchRecProcessor.processXformerAdjData(str, parser);
        			    	//	 parser.addNewBaseCaseBranch(),baseCaseNet, this);
        				} 
        				else if(type==SwitchedShuntData){        			   
        					//System.out.println("ShuntData: " + str);
        					switchedShuntDataMapper.procLineString(str, parser);
        			    	//	 parser.addNewBaseCaseBranch(),baseCaseNet, this);
        				} 
        				else if(type==InterchangeData){
        					//System.out.println("InterData: " + str);
        					netRecProcessor.processAreaInterchangeData(str, parser); 
        				}
        				else if(type==ZoneData){
        					//System.out.println("ZoneData: " + str);
        					zoneDataMapper.procLineString(str, parser); 
        				}
        				else if(type==InterAreaTransferData){
        					//processInterAreaTransferData(str,baseCaseNet); 
        				}
        				else if(type==OwnerData){
        					//System.out.println("OwnerData: " + str);
        					ownerDataMapper.procLineString(str, parser); 
        				}
        			}
        		}catch (final Exception e){
					this.logErr(e.toString());
        		}
             }
        }	while (str != null);
                 
   	   return parser;
	}
	
	protected IODMModelParser parseInputFile(IODMAdapter.NetType type, final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}
	
}
