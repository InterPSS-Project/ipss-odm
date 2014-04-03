package org.ieee.odm.adapter.psse.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovGASTMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovIEEE1981Type1Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovIEEE1981Type3Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovIEESGOMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovTGOV1Mapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynTurGovMapper extends BasePSSEDataMapper{

	private PSSETurGovIEEE1981Type1Mapper psseTurGovIEEEG1 = null;
	private PSSETurGovIEEE1981Type3Mapper psseTurGovIEEEG3 = null;
	private PSSETurGovIEESGOMapper        psseTurGovIEESGO = null;
	private PSSETurGovTGOV1Mapper         psseTurGovTGOV1  = null;
	private PSSETurGovGASTMapper          psseTurGovGAST   = null;
	
	public PSSEDynTurGovMapper(PsseVersion ver) {
		super(ver);
		psseTurGovIEEEG1 = new PSSETurGovIEEE1981Type1Mapper(ver);
		psseTurGovIEEEG3 = new PSSETurGovIEEE1981Type3Mapper(ver);
		psseTurGovIEESGO = new PSSETurGovIEESGOMapper(ver);
		psseTurGovTGOV1  = new PSSETurGovTGOV1Mapper(ver);
		psseTurGovGAST   = new PSSETurGovGASTMapper(ver);
	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("IEEEG1"))
			psseTurGovIEEEG1.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("IEEEG3"))
			psseTurGovIEEEG3.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("IEESGO"))
			psseTurGovIEESGO.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("TGOV1"))
			psseTurGovTGOV1.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("GAST"))
			psseTurGovGAST.procLineString(lineStr, parser);
		else{
			throw new ODMException("The input Turbine-Governor  model type #"+ type+" is not supporged yet!");
		}
	}
	

}
