/*
 * @(#)DStabModelParser.java   
 *
 * Copyright (C) 2006-2010 www.interpss.org
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
 * @Date 02/01/2010
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.dstab;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.DStabSimulationXmlType;
import org.ieee.odm.schema.LineDStabXmlType;
import org.ieee.odm.schema.PSXfrDStabXmlType;
import org.ieee.odm.schema.XfrDStabXmlType;

/**
 * A DStab ODM Xml model parser for the IEEE DOM schema. 
 */
public class DStabModelParser extends BaseDstabModelParser<DStabNetXmlType> {
	// some input file might carry DStab Simu data;
	private DStabSimulationXmlType tranSimu = null;
	
	/**
	 * Default Constructor 
	 * 
	 */
	public DStabModelParser() {
		super();
		this.tranSimu = OdmObjFactory.createDStabSimulationXmlType();
	}	
	
	/**
	 * constructor
	 * 
	 * @param encoding
	 */
	public DStabModelParser(String encoding) {
		super(encoding);
		this.tranSimu = OdmObjFactory.createDStabSimulationXmlType();
	}	
	
	/**
	 * get DStab Simulation record
	 * 
	 * @return
	 */
	public DStabSimulationXmlType getDStabSimu() {
		return this.tranSimu;
	}
}
