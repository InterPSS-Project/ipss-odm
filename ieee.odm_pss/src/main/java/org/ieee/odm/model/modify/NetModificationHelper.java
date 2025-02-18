/*
 * @(#)NetModificationHelper.java   
 *
 * Copyright (C) 2006-2012 www.interpss.org
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
 * @Date 08/30/2012
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.modify;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.schema.BranchChangeRecSetXmlType;
import org.ieee.odm.schema.BranchChangeRecXmlType;
import org.ieee.odm.schema.BranchLossAreaAllocationXmlType;
import org.ieee.odm.schema.BusChangeRecSetXmlType;
import org.ieee.odm.schema.BusChangeRecXmlType;
import org.ieee.odm.schema.DailyDispatchXmlType;
import org.ieee.odm.schema.OverrideOutageScheduleXmlType;
import org.ieee.odm.schema.HourlyDispatchXmlType;
import org.ieee.odm.schema.ModifyRecordXmlType;
import org.ieee.odm.schema.NetModificationXmlType;

/**
 * Network modification helper
 * 
 */
public class NetModificationHelper {
	private IODMModelParser parser = null;
	
	/**
	 * constructor
	 * 
	 * @param parser
	 */
	public NetModificationHelper (IODMModelParser parser) {
		this.parser = parser;
	}
	
	/**
	 * create an NetModificationXmlType for net modification records
	 * 
	 * @return
	 */
	public NetModificationXmlType createNetModificationList(String id, String desc) {
		NetModificationXmlType rec = OdmObjFactory.createNetModificationXmlType();
		rec.setId(id);
		rec.setDesc(desc);
		addModifyRecord(rec);
		return rec;
	}	
	
	/**
	 * create an BranchChangeRecSetXmlType record and added to the netModifyList
	 * 
	 * @return
	 */	
	public BranchChangeRecSetXmlType createBranchChangeRecSetXmlType(NetModificationXmlType netModifyList) {
		BranchChangeRecSetXmlType branchChangeSet = OdmObjFactory.createBranchChangeRecSetXmlType();
		netModifyList.getBranchChangeRecSet().add(branchChangeSet);
		return branchChangeSet;
	}
	
	/**
	 * create an BranchChangeRecXmlType record and added to the branchSet
	 * 
	 * @return
	 */	
	public BranchChangeRecXmlType createBranchChangeRecXmlType(BranchChangeRecSetXmlType branchSet) {
		BranchChangeRecXmlType branchChange = OdmObjFactory.createBranchChangeRecXmlType();
		branchSet.getBranchChangeRec().add(branchChange);
		return branchChange;
	}
	
	/**
	 * create an BusChangeRecSetXmlType record and added to the netModifyList
	 * 
	 * @return
	 */	
	public BusChangeRecSetXmlType createBusChangeRecSetXmlType(NetModificationXmlType netModifyList) {
		BusChangeRecSetXmlType busChangeSet = OdmObjFactory.createBusChangeRecSetXmlType();
		netModifyList.getBusChangeRecSet().add(busChangeSet);
		return busChangeSet;
	}
	
	/**
	 * create an BusChangeRecXmlType record and added to the busSet
	 * 
	 * @return
	 */	
	public BusChangeRecXmlType createBusChangeRecXmlType(BusChangeRecSetXmlType busSet) {
		BusChangeRecXmlType busChange = OdmObjFactory.createBusChangeRecXmlType();
		busSet.getBusChangeRec().add(busChange);
		return busChange;
	}	

	/*
	 * Contingency analysis help method
	 * ================================
	 * 
	 * NetModificationXmlType can be used to represent contingency. The following
	 * are function for contingency processing
	 */
	
	/**
	 * When NetModificationXmlType is used for contingency analysis, return the
	 * contingency list
	 * 
	 * @return
	 */
	public NetModificationXmlType getContingencyList() {
		return (NetModificationXmlType)parser.getModification();
	}	

	/**
	 * When NetModificationXmlType is used for contingency analysis, get contingency
	 * by id
	 * 
	 * @param id
	 * @return
	 */
	public BranchChangeRecSetXmlType getContingency(String id) {
		for ( BranchChangeRecSetXmlType contingency : getContingencyList().getBranchChangeRecSet()) {
			if (id.equals(contingency.getId()))
				return contingency;
		}
		return null;
	}	
	
	/**
	 * get contingency branch id list
	 * 
	 * @return
	 */
	public List<String> getContingencyBranchIds() {
		Hashtable<String, Object> table = new Hashtable<String, Object>(); 
		for ( BranchChangeRecSetXmlType contingency : getContingencyList().getBranchChangeRecSet()) {
			for (BranchChangeRecXmlType branch : contingency.getBranchChangeRec()) {
				String branchId =  branch.getFromBusId() + "->" + branch.getToBusId() + "(" + branch.getCircuitId() + ")";
				if (table.get(branchId) == null)
					table.put(branchId, branch);
			}
		}
		System.out.println("Total contingency branches " + table.size());
		
		return Collections.list(table.keys());
	}
	
	/*
	 * Daily gen/load, psxfr angle dispatch help functions
	 * ===================================================
	 * 
	 */
	
	/**
	 * Get the daily dispatch. A GenDailyDispatchXmlType record will be
	 * created if genDailyDispatch is null.
	 *   
	 * @return
	 */
	public DailyDispatchXmlType getDailyDispatch() {
		if (parser.getModification() == null)
			addModifyRecord(OdmObjFactory.createDailyDispatchXmlType());
		return (DailyDispatchXmlType)parser.getModification();
	}	
	
	/**
	 * Get the hourly dispatch. 
	 *   
	 * @param hr hour  
	 * @return
	 */
	public HourlyDispatchXmlType getHourlyDispatch(int hr) {
		DailyDispatchXmlType dispXml = getDailyDispatch();
		for (HourlyDispatchXmlType hrDisp : dispXml.getHourlyDispatches()) {
			if (hrDisp.getHour() == hr)
				return hrDisp;
		}
		return null;
	}

	/*
	 *  branch loss area Allocation factor help functions
	 * ==================================================
	 */
	
	/**
	 * Get the branch loss area Allocation factor object. A BranchLossAreaAllocationXmlType record will be
	 * created if getBranchLossAreaAllocation is null.
	 *   
	 * @return
	 */
	public BranchLossAreaAllocationXmlType getBranchLossAreaAllocation() {
		if (parser.getModification() == null)
			addModifyRecord(OdmObjFactory.createBranchLossAreaAllocationXmlType());
		return (BranchLossAreaAllocationXmlType)parser.getModification();
	}	

	/*
	 * Daily outage/overrride help functions
	 * =====================================
	 * 
	 */
	
	/**
	 * Get the daily outage/override object. A DailyOverrideOutageScheduleXmlType record will be
	 * created if getDailyOverrideOutageSchedule is null.
	 *   
	 * @return
	 */
	public OverrideOutageScheduleXmlType getOverrideOutageSchedule() {
		if (parser.getModification() == null)
			addModifyRecord(OdmObjFactory.createOverrideOutageScheduleXmlType());
		return (OverrideOutageScheduleXmlType)parser.getModification();
	}
	
	//=====================================
	//=====================================
	
	private void addModifyRecord(ModifyRecordXmlType rec) {
		if (this.parser.getStudyCase().getModificationList() == null) {
			this.parser.getStudyCase().setModificationList(OdmObjFactory.createStudyCaseXmlTypeModificationList());
		}
		this.parser.getStudyCase().getModificationList().getModification().add(rec);
	}	
	
	/**
	 * output the ODM parser object as a String
	 */
	public String toString() {
		return this.parser.toXmlDoc();
	}
}
