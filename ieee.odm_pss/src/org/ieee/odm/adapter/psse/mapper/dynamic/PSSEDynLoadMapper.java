package org.ieee.odm.adapter.psse.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenclsMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenrouMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGensalMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.load.PSSELOADCMLDMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.load.PSSELoadACMotorMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.tur_gov.PSSETurGovIEEE1981Type1Mapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynLoadMapper extends BasePSSEDataMapper{
	
	private PSSELoadACMotorMapper psseACMotor = null;
	private PSSELOADCMLDMapper psseCMPLD = null;
	
	public PSSEDynLoadMapper(PsseVersion ver) {
		super(ver);
		psseACMotor = new PSSELoadACMotorMapper(ver);
		psseCMPLD = new PSSELOADCMLDMapper(ver);

	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("ACMTBLU1")){
			psseACMotor.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("CMLDBLU2")){
			psseCMPLD.procLineString(lineStr, parser);
		}
	}

}
