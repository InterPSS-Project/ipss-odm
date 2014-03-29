 /*
  * @(#)BaseAclfModelParser.java   
  *
  * Copyright (C) 2009 www.interpss.org
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
  * @Date 04/11/2009
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.util.Hashtable;
import java.util.List;

import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.ConverterXmlType;
import org.ieee.odm.schema.DCLineData2TXmlType;
import org.ieee.odm.schema.FlowInterfaceRecXmlType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfr3WBranchXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TielineXmlType;
import org.ieee.odm.schema.Xfr3WBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;

/**
 * A base Aclf Xml parser implementation for the IEEE DOM schema. 
 */
public class BaseAclfModelParser<
					TNetXml extends NetworkXmlType, 
					TBusXml extends BusXmlType,
					TLineXml extends BaseBranchXmlType,
					TXfrXml extends BaseBranchXmlType,
					TPsXfrXml extends BaseBranchXmlType
				> extends AbstractModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> {
	/**
	 * Default Constructor 
	 * 
	 */
	public BaseAclfModelParser() {
		super();
	}	
	
	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public BaseAclfModelParser(String encoding) {
		super(encoding);
	}	
	
	/**
	 * get the base case object of type ShortCircuitNetXmlType
	 * 
	 * @return
	 */
	public LoadflowNetXmlType getAclfNet() {
		return (LoadflowNetXmlType)getBaseCase();
	}
	
	@SuppressWarnings("unchecked") @Override protected TNetXml createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			LoadflowNetXmlType baseCase = OdmObjFactory.createLoadflowNetXmlType();
			baseCase.setBusList(OdmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(OdmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (TNetXml)getStudyCase().getBaseCase().getValue();
	}
	
	/*
	 * 		Bus functions
	 * 		=============
	 */

	@SuppressWarnings("unchecked") @Override protected TBusXml createBus() {
		LoadflowBusXmlType busRec = OdmObjFactory.createLoadflowBusXmlType();
		initAclfBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (TBusXml)busRec;
	}	
	
	protected void initAclfBus(LoadflowBusXmlType busRec) {
		busRec.setOffLine(false);
		busRec.setAreaNumber(1);
		busRec.setZoneNumber(1);	
		
		busRec.setGenData(OdmObjFactory.createBusGenDataXmlType());
   		//LoadflowGenDataXmlType equivGen = OdmObjFactory.createLoadflowGenDataXmlType();
   		//busRec.getGenData().setEquivGen(OdmObjFactory.createEquivGen(equivGen));	
   		
		busRec.setLoadData(OdmObjFactory.createBusLoadDataXmlType());
   		//LoadflowLoadDataXmlType equivLoad = OdmObjFactory.createLoadflowLoadDataXmlType();
   		//busRec.getLoadData().setEquivLoad(OdmObjFactory.createEquivLoad(equivLoad));   		

   		busRec.setShuntYData(OdmObjFactory.createBusShuntYDataXmlType());
   		//busRec.getShuntYData().setEquivY(OdmObjFactory.createYXmlType());   		
	}
	
	/*
	 * 		Branch functions
	 * 		================
	 */
	
	/**
	 * get the Line branch object
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TLineXml getLineBranch(String fromId, String toId, String cirId) {
		return (TLineXml)getBranch(fromId, toId, cirId);
	}
	
	/**
	 * get the xfr branch object
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	@SuppressWarnings("unchecked") public TXfrXml getXfrBranch(String fromId, String toId, String cirId) {
		return (TXfrXml)getBranch(fromId, toId, cirId);
	}
	
	/**
	 * get the 3W xfr branch object
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	@SuppressWarnings("unchecked") public TXfrXml getXfr3WBranch(String fromId, String toId, String tertId, String cirId) {
		return (TXfrXml)getBranch(fromId, toId, tertId, cirId);
	}

	/**
	 * get the ps xfr branch object
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public PSXfrBranchXmlType getPSXfrBranch(String fromId, String toId, String cirId) {
		return (PSXfrBranchXmlType)getBranch(fromId, toId, cirId);
	}
	
	/**
	 * get the ps xfr branch object
	 * 
	 * @param fromId
	 * @param toId
	 * @param tertId
	 * @param cirId
	 * @return
	 */
	public PSXfr3WBranchXmlType getPSXfr3WBranch(String fromId, String toId, String tertId, String cirId) {
		return (PSXfr3WBranchXmlType)getBranch(fromId, toId, tertId, cirId);
	}
	
	/**
	 * add a new 3W PS Xfr branch record to the base case and to the cache table
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public PSXfr3WBranchXmlType createPSXfr3WBranch(String fromId, String toId, String tertId, String cirId) throws ODMBranchDuplicationException {
		PSXfr3WBranchXmlType branch = (PSXfr3WBranchXmlType) createPSXfr3WBranch();
		addBranch2BaseCase(branch, fromId, toId, tertId, cirId);
		return branch;
	}
	

	@SuppressWarnings("unchecked") @Override protected  TLineXml createLineBranch() {
		LineBranchXmlType line = OdmObjFactory.createLineBranchXmlType();
		initAclfLineBranch(line);
		return (TLineXml) line;
	}
    
	protected void initAclfLineBranch(LineBranchXmlType line) {
		line.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		line.setLineInfo(OdmObjFactory.createLineBranchInfoXmlType());
	}
	
	@SuppressWarnings("unchecked") @Override protected  TXfrXml createXfrBranch() {
		XfrBranchXmlType  xfr  =  OdmObjFactory.createXfrBranchXmlType();
		initAclfXfrBranch(xfr);
		return (TXfrXml) xfr;
	}

	protected void initAclfXfrBranch(XfrBranchXmlType  xfr) {
		xfr.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		xfr.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
	}
	
	
	@SuppressWarnings("unchecked") @Override protected  TPsXfrXml createPSXfrBranch() {
		PSXfrBranchXmlType psXfr = OdmObjFactory.createPSXfrBranchXmlType();
		initAclfPsXfrBranch(psXfr);
		return (TPsXfrXml) psXfr;
	}
	
	protected void initAclfPsXfrBranch(PSXfrBranchXmlType psXfr) {
		psXfr.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		psXfr.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
	}

	@SuppressWarnings("unchecked") @Override protected  TXfrXml createXfr3WBranch() {
		Xfr3WBranchXmlType w3xfr = OdmObjFactory.createXfr3WBranchXmlType();
		return (TXfrXml) w3xfr;
	}

	@SuppressWarnings("unchecked") @Override protected TPsXfrXml createPSXfr3WBranch() {
		PSXfr3WBranchXmlType branch = OdmObjFactory.createPSXfr3WBranchXmlType();
		return (TPsXfrXml)branch;
	}
	
	/*
	 * HVDC related functions
	 */

	/**
	 * Get the cashed dcLine2T object by id
	 * 
	 * @param recId
	 * @param invId
	 * @param dcLineId
	 * @return
	 */
	public DCLineData2TXmlType getDcLine2TRecord(String recId, String invId, String dcLineId) {
		String id = ODMModelStringUtil.formBranchId(recId, invId, dcLineId);
		return (DCLineData2TXmlType)this.getCachedObject(id);
	}
	
	/**
	 * add a new 2T DcLine record to the base case and to the cache table
	 * 
	 * @param recId
	 * @param invId
	 * @param dcLineId
	 * @return
	 */
	public DCLineData2TXmlType createDCLine2TRecord(String recId, String invId, String dcLineId) throws ODMBranchDuplicationException {
		DCLineData2TXmlType dcLine = OdmObjFactory.createDCLineData2TXmlType();
		addBranch2BaseCase(dcLine, recId, invId, null, dcLineId);
		intiBranchData(dcLine);
		
		ConverterXmlType rectifier = OdmObjFactory.createConverterXmlType();
		dcLine.setRectifier(rectifier);
		dcLine.getRectifier().setBusId(createBusRef(recId));
	
		ConverterXmlType inverter = OdmObjFactory.createConverterXmlType();
		dcLine.setInverter(inverter);
		dcLine.getInverter().setBusId(createBusRef(invId));
		return dcLine;
	}	
}
