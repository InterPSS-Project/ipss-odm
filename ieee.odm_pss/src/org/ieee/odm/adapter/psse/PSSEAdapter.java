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
import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.psse.impl.PSSEAcscAdapter;
import org.ieee.odm.adapter.psse.impl.PSSEDynAdapter;
import org.ieee.odm.adapter.psse.impl.PSSELFAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.PSXfrShortCircuitXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;

/**
 * ODM adapter for PSS/E input format
 * 
 * @author mzhou
 *
 */
public class PSSEAdapter extends AbstractODMAdapter{
	public static enum PsseVersion {
		PSSE_26, PSSE_29, PSSE_30, PSSE_32	
	}

	private PsseVersion adptrtVersion;
	
	
	public PSSEAdapter(PsseVersion ver) {
		super();
		this.adptrtVersion = ver;
	
	}
    /**
     * Parse the ac load flow file
     * @param din
     * @param encoding
     * @return
     * @throws Exception
     */
	public AclfModelParser parseAclfFile(final IFileReader din, String encoding) throws ODMException {
		PSSELFAdapter<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType> 
		lfAdapter = new PSSELFAdapter<>(this.adptrtVersion);
		//new PSSELFAdapter<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>(this.adptrtVersion);
	    return lfAdapter.parseLoadflowFile(din, encoding);
	
	}
	/**
	 * parse the aclf and sequence network files. The first file in the input "din" array is for aclf
	 * and the second one stores the sequence data
	 * @param din
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public IODMModelParser parseAcscFiles(final IFileReader[] din, String encoding) throws ODMException {
		PSSEAcscAdapter<ShortCircuitNetXmlType, ShortCircuitBusXmlType, LineShortCircuitXmlType, XfrShortCircuitXmlType, PSXfrShortCircuitXmlType> 
		acscAdapter = new PSSEAcscAdapter<>(this.adptrtVersion);
		
		return acscAdapter.parseInputFile(NetType.AcscNet, din, encoding);
		 
	}
	
	/**
	 * parse the files for Dstab study. The first file in the input "din" array is for aclf,
	 * the second one stores the sequence data, if it is available, and the third one is for Dynamic model data
	 * 
	 * @param din
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public DStabModelParser parseDstabFiles(final IFileReader[] din, String encoding) throws ODMException {
		PSSEDynAdapter dynAdapter = new PSSEDynAdapter(this.adptrtVersion);
		return (DStabModelParser) dynAdapter.parseInputFile(NetType.DStabNet, din, encoding);
		
	}
	

	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding)
			throws Exception {
		return parseAclfFile(din,encoding);
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din,
			String encoding) throws Exception {
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
