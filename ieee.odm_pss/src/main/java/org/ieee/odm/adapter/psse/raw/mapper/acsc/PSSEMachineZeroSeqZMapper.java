package org.ieee.odm.adapter.psse.raw.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.acsc.PSSEMachineZeroSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ZUnitType;


public class PSSEMachineZeroSeqZMapper extends BasePSSEDataRawMapper{
	public PSSEMachineZeroSeqZMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachineZeroSeqZParser(ver);
	}
	
	/*
	 * Format 
	 * I, ID, ZRZERO, ZXZERO


     For those machines at which the step-up transformer is represented as part of the generator data
    (i.e., XTRAN is non-zero), ZZERO (i.e., RZERO + j XZERO) is not used and, in the fault analysis
     activities, the step-up transformer is assumed to be a delta wye transformer
	 */
	
	
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		String machId = dataParser.getValue("ID");
		
		double ZRZERO = dataParser.getDouble("ZRZERO");
		
		double ZXZERO = dataParser.getDouble("ZXZERO");
		
		int i = dataParser.getInt("I");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    
	    ShortCircuitBusXmlType acscBus=(ShortCircuitBusXmlType) parser.getBus(busId);
	    ShortCircuitGenDataXmlType scGenData= AcscParserHelper.getAcscContritueGen(acscBus, machId);

	    scGenData.setZeroZ(BaseDataSetter.createZValue(ZRZERO,ZXZERO, ZUnitType.PU));
	    
	    
	}
}
