/*
 * @(#)PSSELFAdapter.java   
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.psse.json.impl;

import java.util.List;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.bean.PSSESchema;
import org.ieee.odm.adapter.psse.json.mapper.PSSEAreaDataJSonMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEAreaDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEBusDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEDcLine2TDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEFixedShuntDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEGenDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEHeaderDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEInterAreaTransferDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSELineDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSELoadDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEOwnerDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSESwitchedSShuntDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEVSCHVDC2TDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEXfrDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEXfrZTableDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEZoneDataRawMapper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkCategoryEnumType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

public class PSSELFJSonAdapter extends BasePSSEJSonAdapter{

	private PSSEHeaderDataRawMapper headerDataMapper = null;	
	private PSSEAreaDataRawMapper areaDataMapper = null;
	private PSSEZoneDataRawMapper zoneDataMapper = null;
	private PSSEOwnerDataRawMapper ownerDataMapper = null;
	private PSSEInterAreaTransferDataRawMapper interAreaDataMapper = null;
	private PSSEXfrZTableDataRawMapper zTableDataMapper = null;
	
	private PSSEBusDataRawMapper busDataMapper = null;
	private PSSEGenDataRawMapper genDataMapper = null;
	private PSSEFixedShuntDataRawMapper fixedShuntDataMapper = null;
	private PSSELoadDataRawMapper loadDataMapper = null;
	private PSSESwitchedSShuntDataRawMapper switchedShuntDataMapper = null;
	
	private PSSELineDataRawMapper lineDataMapper = null;
	private PSSEXfrDataRawMapper xfrDataMapper = null;
	private PSSEDcLine2TDataRawMapper dcLine2TDataMapper = null;
	private PSSEVSCHVDC2TDataRawMapper vschvdc2TDataMapper = null;
	
	public PSSELFJSonAdapter(PsseVersion ver) {
		super(ver);
		
		this.headerDataMapper = new PSSEHeaderDataRawMapper(ver);
		this.areaDataMapper = new PSSEAreaDataRawMapper(ver);
		this.zoneDataMapper = new PSSEZoneDataRawMapper(ver);
		this.ownerDataMapper = new PSSEOwnerDataRawMapper(ver);
		this.interAreaDataMapper = new PSSEInterAreaTransferDataRawMapper(ver);
		this.zTableDataMapper = new PSSEXfrZTableDataRawMapper(ver);
		this.busDataMapper = new PSSEBusDataRawMapper(ver);
		this.genDataMapper = new PSSEGenDataRawMapper(ver);
		this.loadDataMapper = new PSSELoadDataRawMapper(ver);
		this.fixedShuntDataMapper = new PSSEFixedShuntDataRawMapper(ver);
		this.switchedShuntDataMapper = new PSSESwitchedSShuntDataRawMapper(ver);
		this.lineDataMapper = new PSSELineDataRawMapper(ver);
		this.xfrDataMapper = new PSSEXfrDataRawMapper(ver);
		this.dcLine2TDataMapper = new PSSEDcLine2TDataRawMapper(ver);
		this.vschvdc2TDataMapper = new PSSEVSCHVDC2TDataRawMapper(ver);
	}
	
    /**
     * Parse PSS/E load flow input file into ODM/XML 
     * 
     * @param din
     * @param encoding
     * @return
     * @throws Exception
     */
	public AclfModelParser parseLoadflowFile(final IFileReader din, String encoding) throws ODMException {
		this.parser = new AclfModelParser();
		
		this.parser.getStudyCase().setAnalysisCategory(AnalysisCategoryEnumType.LOADFLOW);
		
		//parser the input load flow data
		parseInputFile(din, encoding);
	
		return (AclfModelParser) this.parser;
	}
	
	private BaseAclfModelParser<? extends NetworkXmlType> getParser() {
		return (BaseAclfModelParser<? extends NetworkXmlType>) this.parser;
	}
	
	@Override
	protected IODMModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
	    //set case base info
		this.parser.setCaseContentInfo(OriginalDataFormatEnumType.PSS_E);
		this.parser.getStudyCase().setNetworkCategory(NetworkCategoryEnumType.TRANSMISSION);
		this.parser.getStudyCase().getContentInfo().setOriginalFormatVersion(this.adptrtVersion.toString());

		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) this.parser.getNet();
		// no space is allowed for ID field
		baseCaseNet.setId("Base_Case_from_PSS_E_format");

		PSSESchema jsonObj = din.getJSon(PSSESchema.class);
		
		jsonObj.getGeneral();
		
		jsonObj.getNetwork().getCaseid();
		jsonObj.getNetwork().getGeneral();
		jsonObj.getNetwork().getGauss();
		jsonObj.getNetwork().getNewton();
		jsonObj.getNetwork().getAdjust();
		jsonObj.getNetwork().getTysl();
		jsonObj.getNetwork().getSolver();
		jsonObj.getNetwork().getRating();
		jsonObj.getNetwork().getSwdratnam();
		jsonObj.getNetwork().getBus();
		jsonObj.getNetwork().getLoad();
		jsonObj.getNetwork().getFixshunt();
		jsonObj.getNetwork().getVoltagedroop();
		jsonObj.getNetwork().getGenerator();
		jsonObj.getNetwork().getSwdratset();
		jsonObj.getNetwork().getAcline();
		jsonObj.getNetwork().getSysswd();
		jsonObj.getNetwork().getTransformer();
		
		PSSESchema.Field_Data area = jsonObj.getNetwork().getArea();
		PSSEAreaDataJSonMapper mapper = new PSSEAreaDataJSonMapper(this.adptrtVersion, area.getFields());
		area.getData().forEach(row -> mapper.proc((List<Object>)row, getParser()));
		
		jsonObj.getNetwork().getTwotermdc();
		jsonObj.getNetwork().getVscdc();
		jsonObj.getNetwork().getImpcor();
		jsonObj.getNetwork().getNtermdc();
		jsonObj.getNetwork().getNtermdcconv();
		jsonObj.getNetwork().getNtermdcbus();
		jsonObj.getNetwork().getNtermdclink();
		jsonObj.getNetwork().getMsline();
		jsonObj.getNetwork().getZone();
		jsonObj.getNetwork().getIatrans();
		jsonObj.getNetwork().getOwner();
		jsonObj.getNetwork().getFacts();
		jsonObj.getNetwork().getSwshunt();
		jsonObj.getNetwork().getGne();
		jsonObj.getNetwork().getIndmach();
		jsonObj.getNetwork().getLoadtype();
		jsonObj.getNetwork().getInterface_();
		jsonObj.getNetwork().getItfelmt();
		jsonObj.getNetwork().getSub();
		jsonObj.getNetwork().getSubnode();
		jsonObj.getNetwork().getSubswd();
		jsonObj.getNetwork().getSubterm();
		 
   	   	return parser;
	}
}
