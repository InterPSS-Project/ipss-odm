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
package org.ieee.odm.adapter.psse.json;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.json.impl.PSSELFJSonAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;

/**
 * ODM adapter for PSS/E input format, including Aclf, Acsc and DStab files. This is a facet class. 
 * The actual adapter implementation is located in the ~/impl/ dir.
 * 
 * @author mzhou
 *
 */
public class PSSEJSonAdapter extends PSSEAdapter {
	public PSSEJSonAdapter() {
		super(PsseVersion.PSSE_JSON);	
	}
	
    /**
     * Parse the ac load flow file
     * @param din
     * @param encoding
     * @return
     * @throws ODMException
     */
	public AclfModelParser parseAclfFile(final IFileReader din, String encoding) throws ODMException {
	    return new PSSELFJSonAdapter().parseLoadflowFile(din, encoding);
	}
		
	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding)	throws ODMException {
		return parseAclfFile(din,encoding);
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("function not implemented!");
	}
}
