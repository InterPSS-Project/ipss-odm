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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfr3WDStabXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.Xfr3WDStabXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

public class BaseDstabModelParser <TNetXml extends NetworkXmlType> extends BaseAcscModelParser<TNetXml> {

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
	@Override protected TNetXml createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			DStabNetXmlType baseCase = OdmObjFactory.createDStabNetXmlType();
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
	/**
	 * Get the cashed bus object by id
	 * 
	 * @param id
	 * @return
	 */
	public DStabBusXmlType getDstabBus(String id) {
		return (DStabBusXmlType)this.getBus(id);
	}	
	
	/**
	 * add a new Bus record to the base case
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BusXmlType> T createBus() {
		DStabBusXmlType busRec = OdmObjFactory.createDStabBusXmlType();
		initDStabBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (T)busRec;
	}
	
	protected void initDStabBus(DStabBusXmlType busRec) {
		initAcscBus(busRec);
		//TODO no defaultGen
   		//DStabGenDataXmlType defaulGen = OdmObjFactory.createDStabGenDataXmlType();
   		//busRec.getGenData().getContributeGen().add(OdmObjFactory.createDstabContributeGen(defaulGen));		

   		//DStabLoadDataXmlType defaultLoad = OdmObjFactory.createDStabLoadDataXmlType();
   		//busRec.getLoadData().getContributeLoad().add(OdmObjFactory.createDstabContributeLoad(defaultLoad));		
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

	@SuppressWarnings("unchecked") @Override protected <T extends LineBranchXmlType> T createLineBranch() {
		LineDStabXmlType line = OdmObjFactory.createLineDStabXmlType();
		initDStabLineBranch(line);
		return (T)line;
	}
	
	protected void initDStabLineBranch(LineDStabXmlType line) {
		initAcscLineBranch(line);
	}	
	
	@SuppressWarnings("unchecked") @Override protected <T extends XfrBranchXmlType> T createXfrBranch() {
		XfrDStabXmlType xfr = OdmObjFactory.createXfrDStabXmlType();
		initDStabXfrBranch(xfr);
		return (T)xfr;
	}

	protected void initDStabXfrBranch(XfrDStabXmlType xfr) {
		initAcscXfrBranch(xfr);
	}	
	
	@SuppressWarnings("unchecked") @Override protected <T extends PSXfrBranchXmlType> T createPSXfrBranch() {
		PSXfrDStabXmlType psXfr = OdmObjFactory.createPSXfrDStabXmlType();
		initDStabPsXfrBranch(psXfr);
		return (T)psXfr;
	}

	protected void initDStabPsXfrBranch(PSXfrDStabXmlType psXfr) {
		initAcscPsXfrBranch(psXfr);
	}	
	
	@SuppressWarnings("unchecked") @Override protected <T extends BaseBranchXmlType> T createXfr3WBranch() {
		Xfr3WDStabXmlType w3xfr = OdmObjFactory.createXfr3WDStabXmlType();
		return (T) w3xfr;
	}

	@SuppressWarnings("unchecked") @Override protected <T extends BaseBranchXmlType> T createPSXfr3WBranch() {
		PSXfr3WDStabXmlType branch = OdmObjFactory.createPSXfr3WDStabXmlType();
		return (T)branch;
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
