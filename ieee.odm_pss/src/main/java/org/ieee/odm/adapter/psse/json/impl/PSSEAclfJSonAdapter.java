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

import org.ieee.odm.adapter.psse.bean.PSSESchema;
import org.ieee.odm.adapter.psse.json.mapper.PSSEAclineDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEAreaDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEBusDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSECaseDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEGenDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSELoadDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEOwnerDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEXformerDataJSonMapper;
import org.ieee.odm.adapter.psse.json.mapper.PSSEZoneDataJSonMapper;
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

/**
 * ODM adapter for PSS/E rawx file in the JSon input format
 * 
 * @author mzhou
 *
 */
public class PSSEAclfJSonAdapter extends BasePSSEJSonAdapter {
	// PSSE rawx json object
	//private PSSESchema jsonObj;
	
	// PSSE rawx file version number
	//private double fileVerNo;
	
	/**
	 * Constructor
	 */
	public PSSEAclfJSonAdapter() {
		super();
	}
	
	/**
	 * get the fileVerNo field
	 * 
	 * @return
	 */
    public double getFileVerNo() {
    	PSSESchema jsonObj = this.parser.getJsonObject();
		return new Double(jsonObj.getGeneral().getVersion());
	}

	/**
     * Parse PSS/E load flow input file into ODM/XML 
     * 
     * @param din
     * @param encoding
     * @return
     * @throws ODMException
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
	
	@SuppressWarnings("unchecked")
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
		this.parser.setJsonObject(jsonObj);
		
		//this.fileVerNo = new Double(jsonObj.getGeneral().getVersion());
		
		PSSESchema.Field_Data caseid = jsonObj.getNetwork().getCaseid();
		PSSECaseDataJSonMapper caseMapper = new PSSECaseDataJSonMapper(caseid.getFields());
		caseMapper.map(caseid.getData(), getParser());
		
		// TODO mapping
		jsonObj.getNetwork().getGeneral();
		jsonObj.getNetwork().getGauss();
		jsonObj.getNetwork().getNewton();
		jsonObj.getNetwork().getAdjust();
		jsonObj.getNetwork().getTysl();
		jsonObj.getNetwork().getSolver();
		jsonObj.getNetwork().getRating();
		jsonObj.getNetwork().getSwdratnam();
		
		PSSESchema.Field_Data bus = jsonObj.getNetwork().getBus();
		PSSEBusDataJSonMapper busMapper = new PSSEBusDataJSonMapper(bus.getFields());
		bus.getData().forEach(row -> busMapper.map((List<Object>)row, getParser()));
		
		PSSESchema.Field_Data load = jsonObj.getNetwork().getLoad();
		PSSELoadDataJSonMapper loadMapper = new PSSELoadDataJSonMapper(load.getFields());
		load.getData().forEach(row -> loadMapper.map((List<Object>)row, getParser()));
		
		// TODO mapping
		jsonObj.getNetwork().getFixshunt();
		jsonObj.getNetwork().getVoltagedroop();
		
		PSSESchema.Field_Data gen = jsonObj.getNetwork().getGenerator();
		PSSEGenDataJSonMapper genMapper = new PSSEGenDataJSonMapper(gen.getFields());
		gen.getData().forEach(row -> genMapper.map((List<Object>)row, getParser()));
		
		// TODO mapping
		jsonObj.getNetwork().getSwdratset();
		
		PSSESchema.Field_Data acline = jsonObj.getNetwork().getAcline();
		PSSEAclineDataJSonMapper aclineMapper = new PSSEAclineDataJSonMapper(acline.getFields());
		acline.getData().forEach(row -> aclineMapper.map((List<Object>)row, getParser()));
		
		// TODO mapping
		jsonObj.getNetwork().getSysswd();
		
		PSSESchema.Field_Data xfr = jsonObj.getNetwork().getTransformer();
		PSSEXformerDataJSonMapper xfrMapper = new PSSEXformerDataJSonMapper(xfr.getFields());
		xfr.getData().forEach(row -> xfrMapper.map((List<Object>)row, getParser()));
		
		PSSESchema.Field_Data area = jsonObj.getNetwork().getArea();
		PSSEAreaDataJSonMapper araeMapper = new PSSEAreaDataJSonMapper(area.getFields());
		area.getData().forEach(row -> araeMapper.map((List<Object>)row, getParser()));
		
		// TODO mapping
		jsonObj.getNetwork().getTwotermdc();
		jsonObj.getNetwork().getVscdc();
		jsonObj.getNetwork().getImpcor();
		jsonObj.getNetwork().getNtermdc();
		jsonObj.getNetwork().getNtermdcconv();
		jsonObj.getNetwork().getNtermdcbus();
		jsonObj.getNetwork().getNtermdclink();
		jsonObj.getNetwork().getMsline();
		
		PSSESchema.Field_Data zone = jsonObj.getNetwork().getZone();
		PSSEZoneDataJSonMapper zoneMapper = new PSSEZoneDataJSonMapper(zone.getFields());
		zone.getData().forEach(row -> zoneMapper.map((List<Object>)row, getParser()));
		
		// TODO mapping
		jsonObj.getNetwork().getIatrans();
		
		PSSESchema.Field_Data owner = jsonObj.getNetwork().getOwner();
		PSSEOwnerDataJSonMapper ownerMapper = new PSSEOwnerDataJSonMapper(owner.getFields());
		area.getData().forEach(row -> ownerMapper.proc((List<Object>)row, getParser()));
		
		// TODO mapping
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
