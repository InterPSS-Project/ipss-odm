 /*
  * @(#)BaseAcscModelParser.java   
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

package org.ieee.odm.model.acsc;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfr3WShortCircuitXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.PSXfrShortCircuitXmlType;
import org.ieee.odm.schema.ShortCircuitBusEnumType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.Xfr3WShortCircuitXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;

/**
 * An Acsc ODM Xml parser for the IEEE DOM schema. 
 */
public class BaseAcscModelParser<TNetXml extends NetworkXmlType> extends BaseAclfModelParser<TNetXml> {	

	/**
	 * Default Constructor 
	 * 
	 */
	public BaseAcscModelParser() {
		super();
	}
	
	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public BaseAcscModelParser(String encoding) {
		super(encoding);
	}	
	
	/**
	 * get the base case object of type ShortCircuitNetXmlType
	 * 
	 * @return
	 */
	public ShortCircuitNetXmlType getAcscNet() {
		return (ShortCircuitNetXmlType)getBaseCase();
	}
	
	@SuppressWarnings("unchecked") @Override protected TNetXml createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			ShortCircuitNetXmlType baseCase = OdmObjFactory.createShortCircuitNetXmlType();
			baseCase.setBusList(OdmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(OdmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (TNetXml)getStudyCase().getBaseCase().getValue();
	}
	
	/**
	 * Get the cashed bus object by id
	 * 
	 * @param id
	 * @return
	 */
	public ShortCircuitBusXmlType getAcscBus(String id) {
		return (ShortCircuitBusXmlType)this.getBus(id);
	}	
	
	@SuppressWarnings("unchecked") @Override protected <T extends BusXmlType> T createBus() {
		ShortCircuitBusXmlType busRec = OdmObjFactory.createShortCircuitBusXmlType();
		initAcscBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (T)busRec;
	}	
	
	protected void initAcscBus(ShortCircuitBusXmlType busRec) {
		initAclfBus(busRec);
		// for those non-Gen or non-Load data, equivGen or equivLoad is not required
		//TODO no defaultGen for AcscBus 
   		//ShortCircuitGenDataXmlType defaultGen = OdmObjFactory.createShortCircuitGenDataXmlType();
   		//busRec.getGenData().getContributeGen().add(OdmObjFactory.createAcscContributeGen(defaultGen));		

   		//ShortCircuitLoadDataXmlType defaulLoad = OdmObjFactory.createShortCircuitLoadDataXmlType();
   		//busRec.getLoadData().getContributeLoad().add(OdmObjFactory.createAcscContributeLoad(defaulLoad));
   		
   		//SC code, use to indicate contribution to SC analysis
   		busRec.setScCode(ShortCircuitBusEnumType.NON_CONTRIBUTING);
   	
	}
	
	@SuppressWarnings("unchecked") @Override protected <T extends LineBranchXmlType> T createLineBranch() {
		LineShortCircuitXmlType line = OdmObjFactory.createLineShortCircuitXmlType();
		initAcscLineBranch(line);
		return (T) line;
		
	}

	protected void initAcscLineBranch(LineShortCircuitXmlType line) {
		initAclfLineBranch(line);
	}
	
	@SuppressWarnings("unchecked") @Override protected <T extends XfrBranchXmlType> T createXfrBranch() {
		XfrShortCircuitXmlType  xfr  =  OdmObjFactory.createXfrShortCircuitXmlType();
		initAcscXfrBranch(xfr);
		return (T) xfr;
	}

	protected void initAcscXfrBranch(XfrShortCircuitXmlType  xfr) {
		initAclfXfrBranch(xfr);
	}
	
	@SuppressWarnings("unchecked") @Override protected <T extends PSXfrBranchXmlType> T createPSXfrBranch() {
		PSXfrShortCircuitXmlType psXfr = OdmObjFactory.createPSXfrShortCircuitXmlType();
		initAcscPsXfrBranch(psXfr);
		return (T) psXfr;
	}

	protected void initAcscPsXfrBranch(PSXfrShortCircuitXmlType psXfr) {
		initAclfPsXfrBranch(psXfr);
	}

	@SuppressWarnings("unchecked") @Override protected <T extends BaseBranchXmlType> T createXfr3WBranch() {
		Xfr3WShortCircuitXmlType w3xfr = OdmObjFactory.createXfr3WShortCircuitXmlType();
		return (T) w3xfr;
	}

	@SuppressWarnings("unchecked") @Override protected <T extends BaseBranchXmlType> T createPSXfr3WBranch() {
		PSXfr3WShortCircuitXmlType branch = OdmObjFactory.createPSXfr3WShortCircuitXmlType();
		return (T)branch;
	}	
	
	/**
	 * get the LineShortCircuitXmlType type AcscLine from the network; if the corresponding branch is 
	 * Aclf LineBranchXmlType or LineDStabXmlType type ,it is casted to LineShortCircuitXmlType
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws ODMException
	 */
    public LineShortCircuitXmlType getAcscLine(String fromId, String toId, String cirId) throws ODMException {
		return (LineShortCircuitXmlType)this.getBranch(fromId, toId, cirId);
    }
  
	 /**
	  * get the Acsc Xfr object using the id. If the branch object is of type aclfXfr or DstabXfr,
	  * cast it to the acscXfr type

	  * @param fromId
	  * @param toId
	  * @param cirId
	  * @return
	 * @throws ODMException 
	  */
    public XfrShortCircuitXmlType getAcscXfr(String fromId, String toId, String cirId) throws ODMException{
		return (XfrShortCircuitXmlType)this.getBranch(fromId, toId, cirId);
   	}

    /**
	  * get the Acsc PsXfr object using the id. If the branch object is of type aclfXfr or DstabXfr,
	  * cast it to the acscXfr type
	  * @param fromId
	  * @param toId
	  * @param cirId
	  * @return
	 * @throws ODMException 
	  */
   public PSXfrShortCircuitXmlType getAcscPsXfr(String fromId, String toId, String cirId) throws ODMException{
		return (PSXfrShortCircuitXmlType)this.getBranch(fromId, toId, cirId);
  	}
}
