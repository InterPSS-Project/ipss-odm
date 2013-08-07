package org.ieee.odm.adapter.psse.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenclsMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenrouMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGensalMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynGeneratorMapper extends BasePSSEDataMapper{

	private PSSEGenclsMapper  machGenclsMapper= null;
	private PSSEGenrouMapper  machGenrouMapper= null;
	private PSSEGensalMapper  machGensalMapper= null;
	
	
	public PSSEDynGeneratorMapper(PsseVersion ver) {
		super(ver);
		machGenclsMapper = new PSSEGenclsMapper(ver);
		machGenrouMapper = new PSSEGenrouMapper(ver);
		machGensalMapper = new PSSEGensalMapper(ver);
	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("GENCLS")){
			machGenclsMapper.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("GENROU"))
			machGenrouMapper.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("GENSAL")){
			machGensalMapper.procLineString(lineStr, parser);
		}
		else{
			throw new ODMException("The input Generator dynamic model type #"+ type+" is not supporged yet!");
		}
	}


	


}
