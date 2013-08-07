 /*
  * @(#)BaseDstabModelParser.java   
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
  * @Date 08/11/2010
  * 
  *   Revision History
  *   ================
  *
  */
package org.ieee.odm.model.dstab;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

public class BaseDstabModelParser <
				TNetXml extends NetworkXmlType, 
				TBusXml extends BusXmlType,
				TLineXml extends BranchXmlType,
				TXfrXml extends BranchXmlType,
				TPsXfrXml extends BranchXmlType
				> extends BaseAcscModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> {

	/**
	 * default constructor
	 */
	public BaseDstabModelParser() {
		super();
	}
	
	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public BaseDstabModelParser(String encoding) {
		super(encoding);
	}	
	
	/**
	 * get the base case object of type ShortCircuitNetXmlType
	 * 
	 * @return
	 */
	public DStabNetXmlType getDStabNet() {
		return (DStabNetXmlType)getBaseCase();
	}
	
	/**
	 * create the base case object of type ShortCircuitNetXmlType
	 */
	@SuppressWarnings("unchecked")
	@Override public TNetXml createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			DStabNetXmlType baseCase = odmObjFactory.createDStabNetXmlType();
			baseCase.setBusList(odmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(odmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (TNetXml)getStudyCase().getBaseCase().getValue();
	}
	
	/*
	 * 		Bus functions
	 * 		=============
	 */
	
	/**
	 * add a new Bus record to the base case
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TBusXml createBus() {
		DStabBusXmlType busRec = odmObjFactory.createDStabBusXmlType();
		initDStabBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (TBusXml)busRec;
	}
	
	protected void initDStabBus(DStabBusXmlType busRec) {
		initAcscBus(busRec);
		
   		DStabGenDataXmlType equivGen = odmObjFactory.createDStabGenDataXmlType();
   		busRec.getGenData().setEquivGen(odmObjFactory.createDstabEquivGen(equivGen));		

   		DStabLoadDataXmlType equivLoad = odmObjFactory.createDStabLoadDataXmlType();
   		busRec.getLoadData().setEquivLoad(odmObjFactory.createDstabEquivLoad(equivLoad));		
	}		

	/**
	 * get the DStab bus object using the id. If the bus object is of type aclfBus or acscBus,
	 * cast it to the dstabBus type
	 * 
	 * @param id
	 * @return
	 */
	public DStabBusXmlType getDStabBus(String id) throws ODMException {
		return (DStabBusXmlType)getBus(id);
	}
	
	/*
	 * 		Branch functions
	 * 		================
	 */

	/**
	 * create a LineBranchXmlType object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override public TLineXml createLineBranch() {
		LineDStabXmlType line = odmObjFactory.createLineDStabXmlType();
		initDStabLineBranch(line);
		return (TLineXml)line;
	}
	
	protected void initDStabLineBranch(LineDStabXmlType line) {
		initAcscLineBranch(line);
	}	
	
	/**
	 * create a XfrBranchXmlType object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override public TXfrXml createXfrBranch() {
		XfrDStabXmlType xfr = odmObjFactory.createXfrDStabXmlType();
		initDStabXfrBranch(xfr);
		return (TXfrXml)xfr;
	}

	protected void initDStabXfrBranch(XfrDStabXmlType xfr) {
		initAcscXfrBranch(xfr);
	}	
	
	/**
	 * create a PSXfrBranchXmlType object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override public TPsXfrXml createPSXfrBranch() {
		PSXfrDStabXmlType psXfr = odmObjFactory.createPSXfrDStabXmlType();
		initDStabPsXfrBranch(psXfr);
		return (TPsXfrXml)psXfr;
	}

	protected void initDStabPsXfrBranch(PSXfrDStabXmlType psXfr) {
		initAcscPsXfrBranch(psXfr);
	}	
	
	/**
	 * get the DStab Line object using the id. If the branch object is of type aclfLine or acscLine,
	 * cast it to the dstabLine type
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public LineDStabXmlType getDStabLine(String fromId, String toId, String cirId) throws ODMException {
		return (LineDStabXmlType)this.getBranch(fromId, toId, cirId);
	}

	/**
	 * get the DStab Xfr object using the id. If the branch object is of type aclfXfr or acscXfr,
	 * cast it to the dstabXfr type
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public XfrDStabXmlType getDStabXfr(String fromId, String toId, String cirId) throws ODMException {
		return (XfrDStabXmlType)this.getBranch(fromId, toId, cirId);
	}
	
	/**
	 * get the DStab PSXfr object using the id. If the branch object is of type aclfPSXfr or acscPSXfr,
	 * cast it to the dstabPSXfr type
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public PSXfrDStabXmlType getDStabPSXfr(String fromId, String toId, String cirId) throws ODMException {
		return (PSXfrDStabXmlType)this.getBranch(fromId, toId, cirId);
	}	
}
