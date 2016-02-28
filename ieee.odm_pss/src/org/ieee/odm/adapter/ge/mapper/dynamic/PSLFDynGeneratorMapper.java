package org.ieee.odm.adapter.ge.mapper.dynamic;

import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGENCLSDataParser;
import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGENROUDataParser;
import org.ieee.odm.adapter.ge.parser.dynamic.generator.PSLFDynGENSALDataParser;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenclsMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGenrouMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.generator.PSSEGensalMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSLFDynGeneratorMapper {
	
	protected PSSEGenclsMapper genclsMapper = new PSSEGenclsMapper(new PSLFDynGENCLSDataParser());
	
	protected PSSEGenrouMapper genrouMapper = new PSSEGenrouMapper(new PSLFDynGENROUDataParser());
	
	protected PSSEGensalMapper gensalMapper = new PSSEGensalMapper(new PSLFDynGENSALDataParser());
	
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("GENCLS")){
			genclsMapper.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("GENROU"))
			genrouMapper.procLineString(lineStr, parser);
		else if(type.equalsIgnoreCase("GENSAL")){
			gensalMapper.procLineString(lineStr, parser);
		}
		//GENSAE data is same as the GENSAL
		else if(type.equalsIgnoreCase("GENSAE")){
			gensalMapper.procLineString(lineStr, parser);
		}
		else{
			throw new ODMException("The input Generator dynamic model type #"+ type+" is not supporged yet!");
		}
	}
	

}
