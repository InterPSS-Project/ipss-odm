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

package org.ieee.odm.adapter.psse.json.mapper;

import java.util.List;

import org.ieee.odm.adapter.common.json.BaseJSonDataMapper;
import org.ieee.odm.adapter.psse.json.parser.PSSEDataJSonParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BaseRecordXmlType;

/**
 * Base PSSE JSon format data mapper
 * 
 */
public class BasePSSEDataJSonMapper extends BaseJSonDataMapper {
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public BasePSSEDataJSonMapper(List<String> fieldDef){
		this.dataParser = new PSSEDataJSonParser(fieldDef);
	}
	
	protected void mapOwnerInfo(BaseRecordXmlType recXml) throws ODMException {
		int o1 = dataParser.getInt("o1");
		double f1 = dataParser.getDouble("f1", 0.0);
		
		int o2 = dataParser.getInt("o2", 0);
		double f2 = dataParser.getDouble("f2", 0.0);
		
		int o3 = dataParser.getInt("o3", 0);
		double f3 = dataParser.getDouble("f3", 0.0);
		
		int o4 = dataParser.getInt("o4", 0);
		double f4 = dataParser.getDouble("f4", 0.0);
    	
		BaseJaxbHelper.addOwner(recXml, 
				new Integer(o1).toString(), f1, 
				new Integer(o2).toString(), o2==0?0.0:f2, 
				new Integer(o3).toString(), o3==0?0.0:f3, 
				new Integer(o4).toString(), o4==0?0.0:f4);  		
	}
}
