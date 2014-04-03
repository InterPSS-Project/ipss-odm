package org.ieee.odm.adapter.psse.mapper.dynamic;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE1968Type1Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE1981AC1Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE1981DC1Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE1981ST1Mapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE2005ST3AMapper;
import org.ieee.odm.adapter.psse.mapper.dynamic.exciter.PSSEExcIEEE2005ST4BMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSSEDynExciterMapper extends BasePSSEDataMapper{
	
	private PSSEExcIEEE1968Type1Mapper psseExcIEEET1 = null;
	private PSSEExcIEEE1981DC1Mapper psseExcIEEEX1 = null;
	private PSSEExcIEEE1981AC1Mapper psseExcEXAC1 = null;
	private PSSEExcIEEE1981ST1Mapper psseExcEXST1 = null;
	private PSSEExcIEEE2005ST3AMapper psseExcESST3A = null;
	private PSSEExcIEEE2005ST4BMapper psseExcESST4B = null;
	
	public PSSEDynExciterMapper(PsseVersion ver) {
		super(ver);
		psseExcIEEET1  = new PSSEExcIEEE1968Type1Mapper(ver);
		psseExcIEEEX1  = new PSSEExcIEEE1981DC1Mapper(ver);
		psseExcEXAC1   = new PSSEExcIEEE1981AC1Mapper(ver);
		psseExcEXST1   = new PSSEExcIEEE1981ST1Mapper(ver);
		psseExcESST3A  = new PSSEExcIEEE2005ST3AMapper(ver);
		psseExcESST4B  = new PSSEExcIEEE2005ST4BMapper(ver);
	}
	
	public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("IEEET1")){
			psseExcIEEET1.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("IEEEX1")){
			psseExcIEEEX1.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("EXAC1")){
			psseExcEXAC1.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("EXST1")){
			psseExcEXST1.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("ESST3A")){
			psseExcESST3A.procLineString(lineStr, parser);
		}
		else if(type.equalsIgnoreCase("ESST4B")){
			psseExcESST4B.procLineString(lineStr, parser);
		}
		else{
			throw new ODMException("The input Exciter  model type #"+ type+" is not supporged yet!");
		}
	}

}
