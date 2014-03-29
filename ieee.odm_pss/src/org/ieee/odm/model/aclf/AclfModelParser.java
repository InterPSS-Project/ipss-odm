 /*
  * @(#)AclfModelParser.java   
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
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.ConverterXmlType;
import org.ieee.odm.schema.DCLineData2TXmlType;
import org.ieee.odm.schema.FlowInterfaceRecXmlType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PSXfr3WBranchXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TielineXmlType;
import org.ieee.odm.schema.Xfr3WBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;

/**
 * An Aclf Xml parser implementation for the IEEE DOM schema. 
 */
public class AclfModelParser extends BaseAclfModelParser<
				LoadflowNetXmlType, 
				LoadflowBusXmlType, 
				LineBranchXmlType, 
				XfrBranchXmlType, 
				PSXfrBranchXmlType> {

	private Hashtable<String, FlowInterfaceRecXmlType> interfaceLookupTable = null;
	
	/**
	 * Default Constructor 
	 * 
	 */
	public AclfModelParser() {
		super();
	}	
	
	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public AclfModelParser(String encoding) {
		super(encoding);
	}	
	
	/*
	 * 		Network object functions
	 * 		========================
	 */
	
	/**
	 * create a tieLine object
	 * 
	 * @return
	 */
	public TielineXmlType createTieline() {
		LoadflowNetXmlType net = getAclfNet();
		if (net.getTieLineList() == null)
			net.setTieLineList(OdmObjFactory.createLoadflowNetXmlTypeTieLineList());
		TielineXmlType tieLine = OdmObjFactory.createTielineXmlType();
		net.getTieLineList().getTieline().add(tieLine);
		return tieLine;
	}	
	
	/**
	 * create a Interchange object
	 * 
	 * @return
	 */
	public InterchangeXmlType createInterchange() {
		LoadflowNetXmlType net = getAclfNet();
		if (net.getInterchangeList() == null)
			net.setInterchangeList(OdmObjFactory.createLoadflowNetXmlTypeInterchangeList());
		InterchangeXmlType interchange = OdmObjFactory.createInterchangeXmlType();
		net.getInterchangeList().getInterchange().add(interchange);
		return interchange;
	}	

	/**
	 * get interface object list
	 * 
	 * @return
	 */
	public List<FlowInterfaceRecXmlType> getInterfaceList() {
		LoadflowNetXmlType net = getAclfNet();
		return net.getFlowInterfaceList().getFlowInterface();
	}	
	
	/**
	 * create a Interface object
	 * 
	 * @return
	 */
	public FlowInterfaceRecXmlType createInterface() {
		LoadflowNetXmlType net = getAclfNet();
		if (net.getFlowInterfaceList() == null)
			net.setFlowInterfaceList(OdmObjFactory.createLoadflowNetXmlTypeFlowInterfaceList());
		FlowInterfaceRecXmlType inter = OdmObjFactory.createFlowInterfaceRecXmlType();
		net.getFlowInterfaceList().getFlowInterface().add(inter);
		return inter;
	}	

	/**
	 * get Interface record by id
	 * 
	 * @param id
	 * @return
	 */
	public FlowInterfaceRecXmlType getInterface(String id) {
		LoadflowNetXmlType net = getAclfNet();
		if (net.getFlowInterfaceList() != null)
			for (FlowInterfaceRecXmlType inter : net.getFlowInterfaceList().getFlowInterface()) {
				if (id.equals(inter.getId()))
					return inter;
			}
		return null;
	}	
	
	/**
	 * get Interface record by id
	 * 
	 * @param id
	 * @return
	 */
	public FlowInterfaceRecXmlType getInterfaceCached(String id) {
		LoadflowNetXmlType net = getAclfNet();
		if (this.interfaceLookupTable == null) {
			this.interfaceLookupTable = new Hashtable<String, FlowInterfaceRecXmlType>();
			for (FlowInterfaceRecXmlType inter : net.getFlowInterfaceList().getFlowInterface()) {
				this.interfaceLookupTable.put(inter.getId(), inter);
			}
		}
		return this.interfaceLookupTable.get(id);
	}
	
}
