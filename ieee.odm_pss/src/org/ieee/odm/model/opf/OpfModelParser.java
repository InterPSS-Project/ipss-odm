 /*
  * @(#)OpfModelParser.java   
  *
  * Copyright (C) 2008 www.interpss.org
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
  * @Date 04/11/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.opf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.BaseOpfNetworkXmlType;
import org.ieee.odm.schema.ContentInfoXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkCategoryEnumType;
import org.ieee.odm.schema.OpfBranchXmlType;
import org.ieee.odm.schema.OpfDclfNetworkXmlType;
import org.ieee.odm.schema.OpfGenBusXmlType;
import org.ieee.odm.schema.OpfNetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.PieceWiseLinearModelXmlType;
import org.ieee.odm.schema.QuadraticModelXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;

/**
 * An OPF ODM Xml parser for the IEEE DOM schema. It supports two types of Opf net
 * 
 */
public class OpfModelParser extends BaseAclfModelParser<BaseOpfNetworkXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType> {
	/**
	 * OPF network type
	 * 
	 * @author mzhou
	 *
	 */
	public static enum OpfNetType { 
			OPF,     // Full OPF implementation
			DclfOpf  // a sample DCLF OPF implementation based on ISU OPF implementation
		}
	
	private OpfNetType netType = OpfNetType.OPF;
	
	/**
	 * Default Constructor 
	 *
	 * @param type
	 */
	public OpfModelParser(OpfNetType type) {
		super();
		this.netType = type;
	}	

	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public OpfModelParser(String encoding) {
		super(encoding);
	}
	
	/**
	 * Set BaseCase to Loadflow and Transmission 
	 * 
	 * @param parser
	 * @param originalFormat
	 */
	public void setOPFTransInfo(OriginalDataFormatEnumType originalDataFormat) {
		ContentInfoXmlType info = OdmObjFactory.createContentInfoXmlType();
		getStudyCase().setContentInfo(info);
		info.setOriginalDataFormat(originalDataFormat);
		info.setAdapterProviderName("www.interpss.org");
		info.setAdapterProviderVersion("1.00");
		
		getStudyCase().setAnalysisCategory(
				AnalysisCategoryEnumType.OPF);
		getStudyCase().setNetworkCategory(
				NetworkCategoryEnumType.TRANSMISSION);		
	}	
	
	/**
	 * get base OPF network object
	 * 
	 * @return
	 */
	public BaseOpfNetworkXmlType getBaseOpfNet() {
		return (BaseOpfNetworkXmlType)getBaseCase();
	}	
	
	/**
	 * create the base case object of type LoadflowXmlType
	 */
	@Override
	public BaseOpfNetworkXmlType createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			BaseOpfNetworkXmlType baseCase;
			if (netType == OpfNetType.DclfOpf)
				baseCase = OdmObjFactory.createOpfDclfNetworkXmlType();
			else
				baseCase = OdmObjFactory.createOpfNetworkXmlType();
			baseCase.setBusList(OdmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(OdmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (BaseOpfNetworkXmlType)getStudyCase().getBaseCase().getValue();
	}	
	
	/*
	 * OpfNetwork
	 * ==========
	 */
	
	/**
	 * add a new Bus record to the base case
	 * 
	 * @return
	 */
	public OpfGenBusXmlType createOpfGenBus() {
		OpfGenBusXmlType busRec = OdmObjFactory.createOpfGenBusXmlType();
		busRec.setOffLine(false);
		busRec.setAreaNumber(1);
		busRec.setZoneNumber(1);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return busRec;
	}	
	
	/**
	 * create a bus object with the id, make sure there is no duplication
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OpfGenBusXmlType createOpfGenBus(String id) throws ODMException {
		OpfGenBusXmlType busRec = createOpfGenBus();
		busRec.setId(id);
		if (this.objectCache.get(id) != null) {
			throw new ODMException("Bus record duplication, bus id: " + id);
		}
		this.objectCache.put(id, busRec);
		return busRec;
	}		
	
	/**
	 * add a new bus record to the base case and to the cache table
	 * 
	 * @param id
	 * @return
	 */
	public OpfGenBusXmlType createOpfGenBus(String id, long number) throws ODMException {
		OpfGenBusXmlType busRec = createOpfGenBus(id);
		busRec.setNumber(number);
		return busRec;
	}	
	
	/**
	 * create a LineBranchXmlType object
	 * 
	 * @return
	 */
	public OpfBranchXmlType createOpfBranch() {
		OpfBranchXmlType branch = OdmObjFactory.createOpfBranchXmlType();
		branch.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		branch.setLineInfo(OdmObjFactory.createLineBranchInfoXmlType());
		intiBranchData(branch);
		return branch;
	}
	
	/**
	 * create PWCostModel record
	 * 
	 * @return
	 */
	public PieceWiseLinearModelXmlType createPWCostModel(){
		return OdmObjFactory.createPieceWiseLinearModelXmlType();
	}
	
	/**
	 * create QuadraticCostModel record
	 * 
	 * @return
	 */
	public QuadraticModelXmlType createQuadraticCostModel(){
		return OdmObjFactory.createQuadraticModelXmlType();
	}

	/**
	 * create an OPF branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws ODMBranchDuplicationException
	 */
	public OpfBranchXmlType createOpfBranch(String fromId, String toId, String cirId) throws ODMBranchDuplicationException {
		OpfBranchXmlType branch =  createOpfBranch();
		addBranch2BaseCase(branch, fromId, toId, null, cirId);
		return branch;
	}
	
	/**
	 * For situations, where cirNomber is not defined
	 * 
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public OpfBranchXmlType createOpfBranch(String fromId, String toId) {
		OpfBranchXmlType branch =  createOpfBranch();
		int cirNo = 1;
		boolean done = false;
		while(!done) {
			try {
				addBranch2BaseCase(branch, fromId, toId, null, new Integer(cirNo).toString());
				done = true;
			} catch (ODMBranchDuplicationException e) {
				cirNo++;
				ODMLogger.getLogger().info("Branch " + fromId + " is a parallel branch, cirId set to " + cirNo);
			}
		}
		return branch;
	}

	/**
	 * get OPF network
	 * 
	 * @return
	 */
	public OpfNetworkXmlType getOpfNetwork(){
		return (OpfNetworkXmlType) getBaseCase();
	}
	
	/**
	 * get OPF Gen Bus by id
	 * 
	 * @param id
	 * @return
	 */
	public OpfGenBusXmlType getOpfGenBus(String id) {
		return (OpfGenBusXmlType) getBus(id);
	}
	
	/*
	 * OpfDclfNetwork
	 * ==============
	 */
	
	/**
	 * get the base case object of type LoadflowXmlType
	 * 
	 * @return
	 */
	public OpfDclfNetworkXmlType getOpfDclfNet() {
		return (OpfDclfNetworkXmlType)getBaseCase();
	}
}
