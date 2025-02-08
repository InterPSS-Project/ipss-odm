package org.ieee.odm.adapter.psse.raw.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.acsc.PSSEMachineNegSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEMachineNegSeqZMapper extends BasePSSEDataRawMapper{
    
	public PSSEMachineNegSeqZMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachineNegSeqZParser(ver);
	}
	
	/*
	 * Format 
	 * I, ID, ZRNEG, ZXNEG

	 */
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		String machId = dataParser.getString("ID");
		
		double ZRNEG = dataParser.getDouble("ZRNEG");
		
		double ZXNEG = dataParser.getDouble("ZXNEG");
		
		int i = dataParser.getInt("I");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    
	    ShortCircuitBusXmlType acscBus=(ShortCircuitBusXmlType) parser.getBus(busId);
	    ShortCircuitGenDataXmlType scGenData= AcscParserHelper.getAcscContritueGen(acscBus, machId);
	    
	    scGenData.setNegativeZ(BaseDataSetter.createZValue(ZRNEG,ZXNEG, ZUnitType.PU));
	    
	    
	}
}
