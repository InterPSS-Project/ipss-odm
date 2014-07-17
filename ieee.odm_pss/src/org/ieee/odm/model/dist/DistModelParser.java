 /*
  * @(#)DistModelParser.java   
  *
  * Copyright (C) 2010 www.interpss.org
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
  * @Date 11/11/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.dist;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BreakerDistBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.DistBusXmlType;
import org.ieee.odm.schema.DistributionNetXmlType;
import org.ieee.odm.schema.FeederDistBranchXmlType;
import org.ieee.odm.schema.GeneratorDistBusXmlType;
import org.ieee.odm.schema.InductionMotorDistBusXmlType;
import org.ieee.odm.schema.MixedLoadDistBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.NonContributingDistBusXmlType;
import org.ieee.odm.schema.ReactorDistBranchXmlType;
import org.ieee.odm.schema.SynchronousMotorDistBusXmlType;
import org.ieee.odm.schema.UtilityDistBusXmlType;
import org.ieee.odm.schema.XFormerDistBranchXmlType;

/**
 * A Dist ODM Xml model parser for the IEEE DOM schema. 
 */
public class DistModelParser extends BaseAclfModelParser<DistributionNetXmlType> {
	/**
	 * Default Constructor 
	 * 
	 */
	public DistModelParser() {
		super();
	}	
	
	/**
	 * get the base case object of type DcNetworkXmlType
	 * 
	 * @return
	 */
	public DistributionNetXmlType getDistNet() {
		return (DistributionNetXmlType)getBaseCase();
	}
	
	@Override public DistributionNetXmlType createBaseCase() {
		if (getStudyCase().getBaseCase() == null) {
			DistributionNetXmlType baseCase = OdmObjFactory.createDistributionNetXmlType();
			
			baseCase.setBusList(OdmObjFactory.createNetworkXmlTypeBusList());
			baseCase.setBranchList(OdmObjFactory.createNetworkXmlTypeBranchList());
			getStudyCase().setBaseCase(BaseJaxbHelper.network(baseCase));
		}
		return (DistributionNetXmlType)getStudyCase().getBaseCase().getValue();
	}
	
	/*
	 * 		Bus functions
	 * 		=============
	 */
	
	/**
	 * add the bus record into the network record, and set some default values
	 * 
	 * @param busRec
	 * @param net
	 * 
	 */
	private void addBus2Net(BusXmlType busRec, NetworkXmlType net) {
		busRec.setOffLine(false);
		busRec.setAreaNumber(1);
		busRec.setZoneNumber(1);
		net.getBusList().getBus().add(BaseJaxbHelper.bus(busRec));
	}	
	
	/**
	 * create a Utility bus record
	 * 
	 * <element name="distUtilityBus" type="pss:UtilityDistBusXmlType" substitutionGroup="pss:bus"/>
	 */
	public UtilityDistBusXmlType createUtilityDistBus() {
		UtilityDistBusXmlType busRec = OdmObjFactory.createUtilityDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	
	
	/**
	 * create a Utility bus record
	 * 
	 * <element name="distUtilityBus" type="pss:UtilityDistBusXmlType" substitutionGroup="pss:bus"/>
	 * 
	 * @param id
	 */
	public UtilityDistBusXmlType createUtilityDistBus(String id) throws Exception {
		UtilityDistBusXmlType busRec = createUtilityDistBus();
		setBusId(busRec, id);
		return busRec;
	}		
	
	/**
	 * create a Utility bus record
	 * 
	 * <element name="distUtilityBus" type="pss:UtilityDistBusXmlType" substitutionGroup="pss:bus"/>
	 * 
	 * @param id
	 * @param number
	 */
	public UtilityDistBusXmlType createUtilityDistBus(String id, long number) throws Exception {
		UtilityDistBusXmlType busRec = createUtilityDistBus(id);
		busRec.setNumber(number);
		return busRec;
	}	
	
	/**
	 * get Utilility bus by id
	 * 
	 * @param id
	 * @return
	 */
	public UtilityDistBusXmlType getUtilityDistBus(String id) {
		return (UtilityDistBusXmlType)getBus(id);
	}

	/**
	 * create DistGenerator bus record
	 * 
	 * <element name="distGeneratorBus" type="pss:GeneratorDistBusXmlType" substitutionGroup="pss:bus"/>
	
	 */
	public GeneratorDistBusXmlType createDistGeneratorBus() {
		GeneratorDistBusXmlType busRec = OdmObjFactory.createGeneratorDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	

	/**
	 *  create DistGenerator bus record

	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public GeneratorDistBusXmlType createDistGeneratorBus(String id) throws Exception {
		GeneratorDistBusXmlType busRec = createDistGeneratorBus();
		setBusId(busRec, id);
		return busRec;
	}		
	
	/**
	 * create DistGenerator bus record
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public GeneratorDistBusXmlType createDistGeneratorBus(String id, long number) throws Exception {
		GeneratorDistBusXmlType busRec = createDistGeneratorBus(id);
		busRec.setNumber(number);
		return busRec;
	}	

	/**
	 * get DistGenerator bus record by Id
	 * 
	 * @param id
	 * @return
	 */
	public GeneratorDistBusXmlType getDistGeneratorBus(String id) {
		return (GeneratorDistBusXmlType)getBus(id);
	}

	/**
	 * create Synchronous motor bus record

	 * <element name="distSynMotorBus" type="pss:SynchronousMotorDistBusXmlType" substitutionGroup="pss:bus"/>
	 */
	public SynchronousMotorDistBusXmlType createDistSynMotorBus() {
		SynchronousMotorDistBusXmlType busRec = OdmObjFactory.createSynchronousMotorDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	

	/**
	 * create Synchronous motor bus record
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SynchronousMotorDistBusXmlType createDistSynMotorBus(String id) throws Exception {
		SynchronousMotorDistBusXmlType busRec = createDistSynMotorBus();
		setBusId(busRec, id);
		return busRec;
	}		
	
	/**
	 * create Synchronous motor bus record
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public SynchronousMotorDistBusXmlType createDistSynMotorBus(String id, long number) throws Exception {
		SynchronousMotorDistBusXmlType busRec = createDistSynMotorBus(id);
		busRec.setNumber(number);
		return busRec;
	}	

	/**
	 * get Synchronous motor bus record by Id
	 * 
	 * @param id
	 * @return
	 */
	public SynchronousMotorDistBusXmlType getDistSynMotorBus(String id) {
		return (SynchronousMotorDistBusXmlType)getBus(id);
	}

	/**
	 * create induction motor bus record
	 * 
	 * <element name="distIndMotorBus" type="pss:InductionMotorDistBusXmlType" substitutionGroup="pss:bus"/>
	 */
	public InductionMotorDistBusXmlType createDistIndMotorBus() {
		InductionMotorDistBusXmlType busRec = OdmObjFactory.createInductionMotorDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	
	
	/**
	 * create induction motor bus record
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public InductionMotorDistBusXmlType createDistIndMotorBus(String id) throws Exception {
		InductionMotorDistBusXmlType busRec = createDistIndMotorBus();
		setBusId(busRec, id);
		return busRec;
	}		
	
	/**
	 * create induction motor bus record
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public InductionMotorDistBusXmlType createDistIndMotorBus(String id, long number) throws Exception {
		InductionMotorDistBusXmlType busRec = createDistIndMotorBus(id);
		busRec.setNumber(number);
		return busRec;
	}	

	/**
	 * get induction motor bus record
	 * 
	 * @param id
	 * @return
	 */
	public InductionMotorDistBusXmlType getDistIndMotorBus(String id) {
		return (InductionMotorDistBusXmlType)getBus(id);
	}

	/**
	 * create mixed load bus record
	 * 
	 * <element name="distMixedLoadBus" type="pss:MixedLoadDistBusXmlType" substitutionGroup="pss:bus"/>
	 */
	public MixedLoadDistBusXmlType createDistMixedLoadBus() {
		MixedLoadDistBusXmlType busRec = OdmObjFactory.createMixedLoadDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	

	/**
	 * create mixed load bus record
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MixedLoadDistBusXmlType createDistMixedLoadBus(String id) throws Exception {
		MixedLoadDistBusXmlType busRec = createDistMixedLoadBus();
		setBusId(busRec, id);
		return busRec;
	}		

	/**
	 * create mixed load bus record
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public MixedLoadDistBusXmlType createDistMixedLoadBus(String id, long number) throws Exception {
		MixedLoadDistBusXmlType busRec = createDistMixedLoadBus(id);
		busRec.setNumber(number);
		return busRec;
	}	

	/**
	 * get mixed load bus record
	 * 
	 * @param id
	 * @return
	 */
	public MixedLoadDistBusXmlType getDistMixedLoadBus(String id) {
		return (MixedLoadDistBusXmlType)getBus(id);
	}

	/**
	 * create non-contribute bus record
	 * 
	 * <element name="distNonContributeBus" type="pss:NonContributingDistBusXmlType" substitutionGroup="pss:bus"/>
	 */
	public NonContributingDistBusXmlType createDistNonContributeBus() {
		NonContributingDistBusXmlType busRec = OdmObjFactory.createNonContributingDistBusXmlType();
		this.addBus2Net(busRec, getBaseCase());
		return busRec;
	}	

	/**
	 * create non-contribute bus record
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public NonContributingDistBusXmlType createDistNonContributeBus(String id) throws Exception {
		NonContributingDistBusXmlType busRec = createDistNonContributeBus();
		setBusId(busRec, id);
		return busRec;
	}		

	/**
	 * create non-contribute bus record
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public NonContributingDistBusXmlType createDistNonContributeBus(String id, long number) throws Exception {
		NonContributingDistBusXmlType busRec = createDistNonContributeBus(id);
		busRec.setNumber(number);
		return busRec;
	}	

	/**
	 * get non-contribute bus record
	 * 
	 * @param id
	 * @return
	 */
	public NonContributingDistBusXmlType getDistNonContributeBus(String id) {
		return (NonContributingDistBusXmlType)getBus(id);
	}
	
	/*
	 * 		Branch functions
	 * 		================
	 */
	
	/**
	 * add feeder branch record
	 * 
	 * <element name="distFeederBranch" type="pss:FeederDistBranchXmlType" substitutionGroup="pss:branch"/>
	 */
	public void addDistFeederBranch(FeederDistBranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		this.objectCache.put(branch.getId(), branch);
	}

	/**
	 * get feeder branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public FeederDistBranchXmlType getDistFeederBranch(String fromId, String toId, String cirId) {
		return (FeederDistBranchXmlType)getBranch(fromId, toId, cirId);
	}

	/**
	 * create feeder branch record
	 * 
	 * @return
	 */
	public FeederDistBranchXmlType createDistFeederBranch() {
		FeederDistBranchXmlType branch = OdmObjFactory.createFeederDistBranchXmlType();
		intiBranchData(branch);
		return branch;
	}

	/**
	 * create feeder branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws Exception
	 */
	public FeederDistBranchXmlType createDistFeederBranch(String fromId, String toId, String cirId) throws Exception {
		FeederDistBranchXmlType branch = createDistFeederBranch();
		addBranch2BaseCase(branch, fromId, toId, null, cirId);
		return branch;
	}
	
	/**
	 * add xformer branch record
	 * 
	 * <element name="distXfrBranch" type="pss:XFormerDistBranchXmlType" substitutionGroup="pss:branch"/>
	 */
	public void addDistXfrBranch(XFormerDistBranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		this.objectCache.put(branch.getId(), branch);
	}

	/**
	 * get xformer branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public XFormerDistBranchXmlType getDistXfrBranch(String fromId, String toId, String cirId) {
		return (XFormerDistBranchXmlType)getBranch(fromId, toId, cirId);
	}

	/**
	 * create xformer branch record
	 * 
	 * @return
	 */
	public XFormerDistBranchXmlType createDistXfrBranch() {
		XFormerDistBranchXmlType branch = OdmObjFactory.createXFormerDistBranchXmlType();
		intiBranchData(branch);
		return branch;
	}

	/**
	 * create xformer branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws Exception
	 */
	public XFormerDistBranchXmlType createDistXfrBranch(String fromId, String toId, String cirId) throws Exception {
		XFormerDistBranchXmlType branch = createDistXfrBranch();
		addBranch2BaseCase(branch, fromId, toId, null, cirId);
		return branch;
	}
	
	/**
	 * add reactor branch record
	 * 
	<element name="distReactorBranch" type="pss:ReactorDistBranchXmlType" substitutionGroup="pss:branch"/>
	 */
	public void addDistReactorBranch(ReactorDistBranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		this.objectCache.put(branch.getId(), branch);
	}

	/**
	 * get reactor branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public ReactorDistBranchXmlType getDistReactorBranch(String fromId, String toId, String cirId) {
		return (ReactorDistBranchXmlType)getBranch(fromId, toId, cirId);
	}

	/**
	 * create reactor branch record
	 * 
	 * @return
	 */
	public ReactorDistBranchXmlType createDistReactorBranch() {
		ReactorDistBranchXmlType branch = OdmObjFactory.createReactorDistBranchXmlType();
		intiBranchData(branch);
		return branch;
	}

	/**
	 * create reactor branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws Exception
	 */
	public ReactorDistBranchXmlType createDistReactorBranch(String fromId, String toId, String cirId) throws Exception {
		ReactorDistBranchXmlType branch = createDistReactorBranch();
		addBranch2BaseCase(branch, fromId, toId, null, cirId);
		return branch;
	}
	
	/**
	 * add breaker branch record
	 * 
	 * <element name="distBreakerBranch" type="pss:BreakerDistBranchXmlType" substitutionGroup="pss:branch"/>
	 */
	public void addDistBreakerBranch(BreakerDistBranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		this.objectCache.put(branch.getId(), branch);
	}

	/**
	 * get breaker branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public BreakerDistBranchXmlType getDistBreakerBranch(String fromId, String toId, String cirId) {
		return (BreakerDistBranchXmlType)getBranch(fromId, toId, cirId);
	}

	/**
	 * create breaker branch record
	 * 
	 * @return
	 */
	public BreakerDistBranchXmlType createDistBreakerBranch() {
		BreakerDistBranchXmlType branch = OdmObjFactory.createBreakerDistBranchXmlType();
		intiBranchData(branch);
		return branch;
	}

	/**
	 * create breaker branch record
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 * @throws Exception
	 */
	public BreakerDistBranchXmlType createDistBreakerBranch(String fromId, String toId, String cirId) throws Exception {
		BreakerDistBranchXmlType branch = createDistBreakerBranch();
		addBranch2BaseCase(branch, fromId, toId, null, cirId);
		return branch;
	}
}
