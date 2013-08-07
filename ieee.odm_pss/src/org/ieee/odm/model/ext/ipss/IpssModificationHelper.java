/*
 * @(#)IpssModificationHelper.java   
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
 * @Date 09/30/2011
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.ext.ipss;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.schema.DclfContingencySetXmlType;
import org.ieee.odm.schema.GenLoadModifyXmlType;
import org.ieee.odm.schema.ModifyRecordXmlType;
import org.ieee.odm.schema.OutageScheduleXmlType;

/**
 * All modification type is a child of the ModifyRecordXmlType.
 * 
 *      ModifyRecordXmlType (abstract)
 *            ^
 *            |
 *     MyModificationXmlType      
 *  
 * @author mzhou
 *
 */
public class IpssModificationHelper {
	private IODMModelParser parser = null;
	
	/**
	 * constructor
	 * 
	 * @param parser
	 */
	public IpssModificationHelper (IODMModelParser parser) {
		this.parser = parser;
	}
	
	/**
	 * create an outage schedule modifyRecord
	 * 
	 * @return
	 */
	public OutageScheduleXmlType createOutageSchedule() {
		OutageScheduleXmlType rec = odmObjFactory.createOutageScheduleXmlType();
		addModifyRecord(rec);
		return rec;
	}

	/**
	 * get outage schedule by id
	 * 
	 * @param id
	 * @return
	 */
	public OutageScheduleXmlType getOutageSchedule(String id) {
		return (OutageScheduleXmlType)getModifyRecord(id);
	}

	/**
	 * create an ContingencySet modifyRecord
	 * 
	 * @return
	 */
	public DclfContingencySetXmlType createContingencySet() {
		DclfContingencySetXmlType rec = odmObjFactory.createDclfContingencySetXmlType();
		addModifyRecord(rec);
		return rec;
	}

	/**
	 * get contingency set by id
	 * 
	 * @param id
	 * @return
	 */
	public DclfContingencySetXmlType getContingencySet(String id) {
		return (DclfContingencySetXmlType)getModifyRecord(id);
	}

	/**
	 * create a GenLoadModify modification record
	 * 
	 * @return
	 */
	public GenLoadModifyXmlType createGenLoadModify() {
		GenLoadModifyXmlType rec = odmObjFactory.createGenLoadModifyXmlType();
		addModifyRecord(rec);
		return rec;
	}

	/**
	 * get gen/load modification by id
	 * 
	 * @param id
	 * @return
	 */
	public GenLoadModifyXmlType getGenLoadModify(String id) {
		return (GenLoadModifyXmlType)getModifyRecord(id);
	}
	
	/**
	 * retrieve modifyRecord by id
	 * 
	 * @param id
	 * @return
	 */
	private ModifyRecordXmlType getModifyRecord(String id) {
		for ( ModifyRecordXmlType rec : this.parser.getStudyCase().getModificationList().getModification()) {
			if (rec.getId().equals(id))
				return rec;
		}
		return null;
	}

	private void addModifyRecord(ModifyRecordXmlType rec) {
		if (this.parser.getStudyCase().getModificationList() == null) {
			this.parser.getStudyCase().setModificationList(odmObjFactory.createStudyCaseXmlTypeModificationList());
		}
		this.parser.getStudyCase().getModificationList().getModification().add(rec);
	}
}
