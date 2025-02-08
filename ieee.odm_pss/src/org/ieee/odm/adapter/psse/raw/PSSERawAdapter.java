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
package org.ieee.odm.adapter.psse.raw;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.impl.PSSEAcscRawAdapter;
import org.ieee.odm.adapter.psse.raw.impl.PSSEDynRawAdapter;
import org.ieee.odm.adapter.psse.raw.impl.PSSELFRawAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;

/**
 * ODM adapter for PSS/E input format, including Aclf, Acsc and DStab files. This is a facet class. 
 * The actual adapter implementation is located in the ~/impl/ dir.
 * 
 * @author mzhou
 *
 */
public class PSSERawAdapter extends PSSEAdapter {
	public PSSERawAdapter(PsseVersion ver) {
		super(ver);	
	}
	
	
	/**
	 * retrieve PSS/E version number for performing check, for example, ver > 32
	 * 
	 * @param adptrVersion
	 * @return
	 */
	public static int getVersionNo(PsseVersion adptrVersion) {
		return new Integer(adptrVersion.toString().substring(5));
	}
	
    /**
     * Parse the ac load flow file
     * @param din
     * @param encoding
     * @return
     * @throws ODMException
     */
	public AclfModelParser parseAclfFile(final IFileReader din, String encoding) throws ODMException {
	    return new PSSELFRawAdapter(this.adptrVersion).parseLoadflowFile(din, encoding);
	}
	
	/**
	 * parse the aclf and sequence network files. The first file in the input "din" array is for aclf
	 * and the second one stores the sequence data
	 * 
	 * @param din
	 * @param encoding
	 * @return
	 * @throws ODMException
	 */
	public IODMModelParser parseAcscFiles(final IFileReader[] din, String encoding) throws ODMException {
		return new PSSEAcscRawAdapter(this.adptrVersion).parseInputFile(NetType.AcscNet, din, encoding);
	}
	
	/**
	 * parse the files for Dstab study. The first file in the input "din" array is for aclf,
	 * the second one stores the sequence data, if it is available, and the third one is for Dynamic model data
	 * 
	 * @param din
	 * @param encoding
	 * @return
	 * @throws ODMException
	 */
	public DStabModelParser parseDstabFiles(final IFileReader[] din, String encoding) throws ODMException {
		return (DStabModelParser) new PSSEDynRawAdapter(this.adptrVersion).parseInputFile(NetType.DStabNet, din, encoding);
	}
	

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding)	throws ODMException {
		return parseAclfFile(din,encoding);
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) throws ODMException {
		IODMModelParser tempParser =null;
		if(type==NetType.AcscNet)
			 tempParser=parseAcscFiles(din,encoding);
		else if(type==NetType.DStabNet){
			 tempParser=parseDstabFiles(din,encoding);
		}
		else{
			throw new ODMException("Only Acsc and Dstab types are supported now, " +
					"please check or contact support@interpss.com !");
		}
		return tempParser;
	}
}
