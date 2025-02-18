/*
 * @(#) IpssModifyUtil.java   
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

import org.ieee.odm.schema.DclfContingencySetXmlType;
import org.ieee.odm.schema.DclfContingencyXmlType;

/**
 * InterPSS extension modification utility functions
 * 
 * @author mzhou
 *
 */
public class IpssModifyUtil {
	/**
	 * get contingency from the contingency set
	 * 
	 * @param id
	 * @param conSet
	 * @return
	 */
	public static DclfContingencyXmlType getContingency(String id, DclfContingencySetXmlType conSet) {
		for (DclfContingencyXmlType c : conSet.getContingency())
			if (id.equals(c.getId()))
				return c;
		return null;
	}
}
