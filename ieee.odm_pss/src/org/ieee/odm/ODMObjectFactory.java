 /*
  * @(#)ODMObjectFactory.java   
  *
  * Copyright (C) 2008-2010 www.interpss.org
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
  * @Date 12/04/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.adapter.ge.GePslfAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter.IEEECDFVersion;
import org.ieee.odm.adapter.opf.matpower.OpfMatpowerAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.v26.PSSEV26Adapter;
import org.ieee.odm.adapter.pwd.PWDAdapterForContingency;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.adapter.ucte.UCTE_DEFAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.model.dc.DcSystemModelParser;
import org.ieee.odm.model.dist.DistModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.ObjectFactory;

/**
 * ODM object factory
 * 
 * @author mzhou
 *
 */
public class ODMObjectFactory {
	/** ODM xml schema factor object*/
	public static ObjectFactory OdmObjFactory = null;	
    static    {
    	OdmObjFactory = new ObjectFactory();
    } 
	
    /**
     * create an Aclf ODM model parser
     * 
     * @return
     */
	public static AclfModelParser createAclfModelParser() {
		AclfModelParser parser = new AclfModelParser();
		return parser;
	}

    /**
     * create a DC system ODM model parser
     * 
     * @return
     */
	public static DcSystemModelParser createDcSystemModelParser() {
		DcSystemModelParser parser = new DcSystemModelParser();
		return parser;
	}

    /**
     * create a distribution system ODM model parser
     * 
     * @return
     */
	public static DistModelParser createDistModelParser() {
		DistModelParser parser = new DistModelParser();
		return parser;
	}
	
    /**
     * create an OFP ODM model parser
     * 
     * @return
     */
	public static OpfModelParser createOpfModelParser() {
		OpfModelParser parser = new OpfModelParser(OpfModelParser.OpfNetType.OPF);
		return parser;
	}

    /**
     * create a DStability ODM model parser
     * 
     * @return
     */
	public static DStabModelParser createDStabModelParser() {
		DStabModelParser parser = new DStabModelParser();
		return parser;
	}
	
    /**
     * create an Acsc ODM model parser
     * 
     * @return
     */
	public static AcscModelParser createAcscModelParser() {
		AcscModelParser parser = new AcscModelParser();
		return parser;
	}	
	
    /**
     * create an input file adapter for the file format
     * 
     * @param f file format
     * @return
     */
	public static IODMAdapter createODMAdapter(ODMFileFormatEnum f) throws ODMException {
		if ( f == ODMFileFormatEnum.IeeeCDF ) 
			return new IeeeCDFAdapter();
		else if ( f == ODMFileFormatEnum.IeeeCDFExt1 ) 
			return new IeeeCDFAdapter(IEEECDFVersion.Ext1);
		
		else if ( f == ODMFileFormatEnum.PsseV26 )
			return new PSSEV26Adapter();
		else if ( f == ODMFileFormatEnum.PsseV30 )
			return new PSSEAdapter(PsseVersion.PSSE_30);
		else if ( f == ODMFileFormatEnum.PsseV31 )
			return new PSSEAdapter(PsseVersion.PSSE_31);
		else if ( f == ODMFileFormatEnum.PsseV32 )
			return new PSSEAdapter(PsseVersion.PSSE_32);
		else if ( f == ODMFileFormatEnum.PsseV33 )
			return new PSSEAdapter(PsseVersion.PSSE_33);
		
		else if ( f == ODMFileFormatEnum.GePSLF ) 
			return new GePslfAdapter(GePslfAdapter.Version.PSLF15);
		
		else if ( f == ODMFileFormatEnum.UCTE ) 
			return new UCTE_DEFAdapter();
		
		else if ( f == ODMFileFormatEnum.BPA ) 
			return new BPAAdapter();
		
		else if ( f == ODMFileFormatEnum.PWD ) 
			return new PowerWorldAdapter();
		else if ( f == ODMFileFormatEnum.PWD_Contingency ) 
			return new PWDAdapterForContingency();
		
		else if ( f == ODMFileFormatEnum.MatPower ) 
			return new OpfMatpowerAdapter();
		
		throw new ODMException("Error - unkown ODM file type");
	}
}
