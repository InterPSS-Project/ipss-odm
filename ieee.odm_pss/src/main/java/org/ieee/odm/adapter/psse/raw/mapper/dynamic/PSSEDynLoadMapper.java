package org.ieee.odm.adapter.psse.raw.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGenclsMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGenrouMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGensalMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.load.PSSELOADCMLDMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.load.PSSELoadACMotorMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.tur_gov.PSSETurGovIEEE1981Type1Mapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynLoadMapper extends BasePSSEDataRawMapper{
	
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
