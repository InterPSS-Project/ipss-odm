 /*
  * @(#)ODMModelParser.java   
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

package org.ieee.odm.model;

import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.model.dc.DcSystemModelParser;
import org.ieee.odm.model.dist.DistModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;

/**
 * A generic Xml parser for the IEEE DOM schema, used when the network/analysis type is unknown.
 * The to<*>ModelParser() method is used to "cast" to appropriate ODM model type whenever
 * its network/analysis type is known.
 */
public class ODMModelParser extends AbstractModelParser<NetworkXmlType, BusXmlType, BaseBranchXmlType, BaseBranchXmlType, BaseBranchXmlType> {
	/**
	 * Default Constructor 
	 * 
	 */
	public ODMModelParser() {
		super();
	}
	
	/**
	 * convert to an Aclf model parser
	 * 
	 * @return
	 */
	public AclfModelParser toAclfModelParser() {
		return (AclfModelParser)copyTo(new AclfModelParser());
	}
 	
	/**
	 * convert to an Acsc model parser
	 * 
	 * @return
	 */
	public AcscModelParser toAcscModelParser() {
		return (AcscModelParser)copyTo(new AcscModelParser());
	}

	/**
	 * convert to a DC system model parser
	 * 
	 * @return
	 */
	public DcSystemModelParser toDcSystemModelParser() {
		return (DcSystemModelParser)copyTo(new DcSystemModelParser());
	}

	/**
	 * convert to a distribution system model parser
	 * 
	 * @return
	 */
	public DistModelParser toDistModelParser() {
		return (DistModelParser)copyTo(new DistModelParser());
	}

	/**
	 * convert to a dynamic stability network model parser
	 * 
	 * @return
	 */
	public DStabModelParser toDStabModelParser() {
		return (DStabModelParser)copyTo(new DStabModelParser());
	}

	/**
	 * convert to an OPF model parser
	 * 
	 * @return
	 */
	public OpfModelParser toOpfModelParser() {
		return (OpfModelParser)copyTo(new OpfModelParser(OpfModelParser.OpfNetType.OPF));
	}
	
	@Override protected NetworkXmlType createBaseCase() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BusXmlType createBus() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BaseBranchXmlType createLineBranch() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BaseBranchXmlType createXfrBranch() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BaseBranchXmlType createXfr3WBranch() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BaseBranchXmlType createPSXfrBranch() {
		throw new RuntimeException("Programming error, method not implemented");
	}

	@Override protected BaseBranchXmlType createPSXfr3WBranch() {
		throw new RuntimeException("Programming error, method not implemented");
	}
	
	private AbstractModelParser<?,?,?,?,?> copyTo(AbstractModelParser<?,?,?,?,?> parser) {
		parser.objectCache = this.objectCache;
		parser.pssStudyCase = this.pssStudyCase;
		return parser;
	}
}
