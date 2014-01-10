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
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfrShortCircuitXmlType;
import org.ieee.odm.schema.ShortCircuitBusEnumType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.Xfr3WShortCircuitXmlType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;

/**
 * An Acsc ODM Xml parser for the IEEE DOM schema. 
 */
public class BaseAcscModelParser<
					TNetXml extends NetworkXmlType, 
					TBusXml extends BusXmlType,
					TLineXml extends BranchXmlType,
					TXfrXml extends BranchXmlType,
					TPsXfrXml extends BranchXmlType
				> extends BaseAclfModelParser<TNetXml, TBusXml, TLineXml, TXfrXml, TPsXfrXml> {	

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
	
	@SuppressWarnings("unchecked") @Override public TNetXml createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			ShortCircuitNetXmlType baseCase = OdmObjFactory.createShortCircuitNetXmlType();
			baseCase.setBusList(OdmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(OdmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (TNetXml)getStudyCase().getBaseCase().getValue();
	}
	
	@SuppressWarnings("unchecked") @Override public TBusXml createBus() {
		ShortCircuitBusXmlType busRec = OdmObjFactory.createShortCircuitBusXmlType();
		initAcscBus(busRec);
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
		return (TBusXml)busRec;
	}	
	
	protected void initAcscBus(ShortCircuitBusXmlType busRec) {
		initAclfBus(busRec);
		// for those non-Gen or non-Load data, equivGen or equivLoad is not required
		
   		//ShortCircuitGenDataXmlType equivGen = OdmObjFactory.createShortCircuitGenDataXmlType();
   		//busRec.getGenData().setEquivGen(OdmObjFactory.createAcscEquivGen(equivGen));		

   		//ShortCircuitLoadDataXmlType equivLoad = OdmObjFactory.createShortCircuitLoadDataXmlType();
   		//busRec.getLoadData().setEquivLoad(OdmObjFactory.createAcscEquivLoad(equivLoad));
   		
   		//SC code, use to indicate contribution to SC analysis
   		busRec.setScCode(ShortCircuitBusEnumType.NON_CONTRIBUTING);
   	
	}
	
	@SuppressWarnings("unchecked") @Override public TLineXml createLineBranch() {
		LineShortCircuitXmlType line = OdmObjFactory.createLineShortCircuitXmlType();
		initAcscLineBranch(line);
		return (TLineXml) line;
		
	}

	protected void initAcscLineBranch(LineShortCircuitXmlType line) {
		initAclfLineBranch(line);
	}
	
	@SuppressWarnings("unchecked") @Override public TXfrXml createXfrBranch() {
		XfrShortCircuitXmlType  xfr  =  OdmObjFactory.createXfrShortCircuitXmlType();
		return (TXfrXml) xfr;
	}

	protected void initAcscXfrBranch(XfrShortCircuitXmlType  xfr) {
		initAclfXfrBranch(xfr);
	}
	
	@SuppressWarnings("unchecked") @Override public TXfrXml createXfr3WBranch() {
		Xfr3WShortCircuitXmlType w3xfr = OdmObjFactory.createXfr3WShortCircuitXmlType();
		return (TXfrXml) w3xfr;
	}
    
	@SuppressWarnings("unchecked") @Override public TPsXfrXml createPSXfrBranch() {
		PSXfrShortCircuitXmlType psXfr = OdmObjFactory.createPSXfrShortCircuitXmlType();
		initAcscPsXfrBranch(psXfr);
		return (TPsXfrXml) psXfr;
	}

	protected void initAcscPsXfrBranch(PSXfrShortCircuitXmlType psXfr) {
		initAclfPsXfrBranch(psXfr);
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
