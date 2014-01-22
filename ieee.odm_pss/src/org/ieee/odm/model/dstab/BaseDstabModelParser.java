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
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfr3WDStabXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.Xfr3WDStabXmlType;
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
	 * add a new Bus record to the base case
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected TBusXml createBus() {
		DStabBusXmlType busRec = OdmObjFactory.createDStabBusXmlType();
		initDStabBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (TBusXml)busRec;
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

	@SuppressWarnings("unchecked") @Override protected TLineXml createLineBranch() {
		LineDStabXmlType line = OdmObjFactory.createLineDStabXmlType();
		initDStabLineBranch(line);
		return (TLineXml)line;
	}
	
	protected void initDStabLineBranch(LineDStabXmlType line) {
		initAcscLineBranch(line);
	}	
	
	@SuppressWarnings("unchecked") @Override protected TXfrXml createXfrBranch() {
		XfrDStabXmlType xfr = OdmObjFactory.createXfrDStabXmlType();
		initDStabXfrBranch(xfr);
		return (TXfrXml)xfr;
	}

	protected void initDStabXfrBranch(XfrDStabXmlType xfr) {
		initAcscXfrBranch(xfr);
	}	
	
	@SuppressWarnings("unchecked") @Override protected TPsXfrXml createPSXfrBranch() {
		PSXfrDStabXmlType psXfr = OdmObjFactory.createPSXfrDStabXmlType();
		initDStabPsXfrBranch(psXfr);
		return (TPsXfrXml)psXfr;
	}

	protected void initDStabPsXfrBranch(PSXfrDStabXmlType psXfr) {
		initAcscPsXfrBranch(psXfr);
	}	
	
	@SuppressWarnings("unchecked") @Override protected TXfrXml createXfr3WBranch() {
		Xfr3WDStabXmlType w3xfr = OdmObjFactory.createXfr3WDStabXmlType();
		return (TXfrXml) w3xfr;
	}

	@SuppressWarnings("unchecked") @Override protected TPsXfrXml createPSXfr3WBranch() {
		PSXfr3WDStabXmlType branch = OdmObjFactory.createPSXfr3WDStabXmlType();
		return (TPsXfrXml)branch;
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
