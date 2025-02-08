 /*
  * @(#)BasePSSEDataMapper.java   
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

package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.BaseStringDataMapper;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BaseRecordXmlType;

public class BasePSSEDataRawMapper extends  BaseStringDataMapper{
	protected PsseVersion version = null;
	
	public BasePSSEDataRawMapper(){
		
	}
	
	public BasePSSEDataRawMapper(PsseVersion ver) {
		this.version = ver;
	}
	
	protected void mapOwnerInfo(BaseRecordXmlType recXml) throws ODMException {
		String o1 = dataParser.getValue("O1");
		double f1 = dataParser.getDouble("F1", 0.0);
		
		String o2 = dataParser.getValue("O2", null);
		if (o2 != null && new Integer(o2) == 0)
			o2 = null;
		
		double f2 = dataParser.getDouble("F2", 0.0);
		String o3 = dataParser.getValue("O3", null);
		if (o3 != null && new Integer(o3) == 0)
			o3 = null;
		
		double f3 = dataParser.getDouble("F3", 0.0);
		String o4 = dataParser.getValue("O4", null);
		if (o4 != null && new Integer(o4) == 0)
			o4 = null;
		double f4 = dataParser.getDouble("F4", 0.0);
    	
		BaseJaxbHelper.addOwner(recXml, 
				o1, f1, 
				o2, o2==null?0.0:f2, 
				o3, o3==null?0.0:f3, 
				o4, o4==null?0.0:f4);  		
	}
}
