/*
 * @(#)PSSEAdapter.java   
 *
 * Copyright (C) 2006-2009 www.interpss.org
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.adapter.psse;

import org.ieee.odm.adapter.AbstractODMAdapter;

/**
 * ODM adapter for PSS/E input format, including Aclf, Acsc and DStab files. This is a facet class. 
 * The actual adapter implementation is located in the ~/impl/ dir.
 * 
 * @author mzhou
 *
 */
public abstract class PSSEAdapter extends AbstractODMAdapter {
	/**
	 *  ODM PSS/E adapter version  
	 */
	public static enum PsseVersion {
		PSSE_29, PSSE_30, PSSE_31, PSSE_32, PSSE_33, PSSE_34, PSSE_35, PSSE_36, PSSE_JSON	
	}

	protected PsseVersion adptrVersion;
	
	public PSSEAdapter(PsseVersion ver) {
		super();
		this.adptrVersion = ver;
	
	}
}
