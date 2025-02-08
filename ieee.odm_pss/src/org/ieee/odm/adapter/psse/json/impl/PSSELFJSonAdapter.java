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
import org.ieee.odm.adapter.psse.json.mapper.PSSEAreaDataJSonMapper;
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

public class PSSELFJSonAdapter extends BasePSSEJSonAdapter {
	
	public PSSELFJSonAdapter() {
		super();
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
		PSSEAreaDataJSonMapper mapper = new PSSEAreaDataJSonMapper(area.getFields());
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
