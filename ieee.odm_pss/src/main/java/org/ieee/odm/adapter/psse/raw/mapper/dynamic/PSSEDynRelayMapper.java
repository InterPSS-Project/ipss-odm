package org.ieee.odm.adapter.psse.raw.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGenclsMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGenrouMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.generator.PSSEGensalMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.load.PSSELOADCMLDMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.load.PSSELoadACMotorMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.relay.PSSERelayFRQTPATMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.relay.PSSERelayLDS3Mapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.relay.PSSERelayLVS3Mapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.relay.PSSERelayVTGTPATMapper;
import org.ieee.odm.adapter.psse.raw.mapper.dynamic.tur_gov.PSSETurGovIEEE1981Type1Mapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynRelayMapper extends BasePSSEDataRawMapper{
	
	private PSSERelayLDS3Mapper psseLDS3 = null;
	private PSSERelayLVS3Mapper psseLVS3 = null;
	private PSSERelayFRQTPATMapper psseGFTP = null;
	private PSSERelayVTGTPATMapper psseGVTP = null;
	
	public PSSEDynRelayMapper(PsseVersion ver) {
		super(ver);
		psseLDS3 = new PSSERelayLDS3Mapper(ver);
		psseLVS3 = new PSSERelayLVS3Mapper(ver);
		psseGFTP = new PSSERelayFRQTPATMapper(ver);
		psseGVTP = new PSSERelayVTGTPATMapper(ver);

	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("LDS3BL")){
			psseLDS3.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("LVS3BL")){
			psseLVS3.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("FRQTPAT")){
			psseGFTP.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("VTGTPAT")){
			psseGVTP.procLineString(lineStr, parser);
		}
	}

}
