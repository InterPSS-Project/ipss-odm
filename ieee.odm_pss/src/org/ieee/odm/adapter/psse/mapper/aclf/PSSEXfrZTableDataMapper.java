 /*
  * @(#)PSSEXfrZTableDataMapper.java   
  *
  * Copyright (C) 2006 www.interpss.org
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
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.psse.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEXfrZTableDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.XformerZTableXmlType;

public class PSSEXfrZTableDataMapper extends BasePSSEDataMapper{
	
	public PSSEXfrZTableDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEXfrZTableDataParser(ver);
	}
	

	public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		if (baseCaseNet.getXfrZTable() == null) {
			baseCaseNet.setXfrZTable(OdmObjFactory.createXformerZTableXmlType());
			baseCaseNet.getXfrZTable().setAdjustSide(BranchBusSideEnumType.FROM_SIDE);
		}
		XformerZTableXmlType.XformerZTableItem item = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItem(); 
		baseCaseNet.getXfrZTable().getXformerZTableItem().add(item);
		
		/*
		 * format V30: I, T1, F1, T2, F2, T3, F3, ... T11, F11
		 */
		int i = this.dataParser.getInt("I");
		item.setNumber(i);
		for (int n = 1; n < 12; n++) {
			if (this.dataParser.exist("T"+n) && this.dataParser.exist("F"+n)) {
				XformerZTableXmlType.XformerZTableItem.Lookup lookup = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup(); 
				item.getLookup().add(lookup);
				lookup.setTurnRatioShiftAngle(this.dataParser.getDouble("T"+n));
				lookup.setScaleFactor(this.dataParser.getDouble("F"+n));
			}
		}		
	}
}
