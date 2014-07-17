 /*
  * @(#)PSSEOwnerDataMapper.java   
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
import org.ieee.odm.adapter.psse.parser.aclf.PSSEOwnerDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OwnerXmlType;

public class PSSEOwnerDataMapper extends BasePSSEDataMapper{
	
	public PSSEOwnerDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEOwnerDataParser(ver);
	}

	public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		/*
		 * format : I, ’OWNAME’
		 */
		
		int i = this.dataParser.getInt("I");
		if (i > 0) {
			LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
			OwnerXmlType owner = OdmObjFactory.createOwnerXmlType();
			baseCaseNet.getOwnerList().add(owner);

			String name = this.dataParser.getString("OWNAME");
			owner.setId(new Integer(i).toString());
			owner.setNumber(i);
			owner.setName(name);
		}
	}
}
