package org.ieee.odm.adapter.psse.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE1968Type1Mapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynExciterMapper extends BasePSSEDataMapper{
	
	private PSSEExcIEEE1968Type1Mapper psseExcIEEET1 = null;
	
	public PSSEDynExciterMapper(PsseVersion ver) {
		super(ver);
		psseExcIEEET1  = new PSSEExcIEEE1968Type1Mapper(ver);
		
	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("IEEET1")){
			psseExcIEEET1.procLineString(lineStr, parser);
		}
		else{
			throw new ODMException("The input Exciter  model type #"+ type+" is not supporged yet!");
		}
	}

}
