package org.ieee.odm.adapter.psse.raw.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.acsc.PSSEMachinePosSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusEnumType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEMachinePosSeqZMapper extends BasePSSEDataRawMapper{
	
	public PSSEMachinePosSeqZMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachinePosSeqZParser(ver);
	}
	
	/*
	 * Format 
	 * I, ID, ZRPOS, ZXPOS

	 */
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		String machId = dataParser.getString("ID");
		
		double ZRPOS = dataParser.getDouble("ZRPOS");
		
		double ZXPOS = dataParser.getDouble("ZXPOS");
		
		int i = dataParser.getInt("I");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    
	    ShortCircuitBusXmlType acscBus=(ShortCircuitBusXmlType) parser.getBus(busId);
	    //add SC code
	    acscBus.setScCode(ShortCircuitBusEnumType.CONTRIBUTING);
	    
	    ShortCircuitGenDataXmlType scGenData= AcscParserHelper.getAcscContritueGen(acscBus, machId);
        if(scGenData==null){
        	scGenData=AcscParserHelper.createAcscContributeGen(acscBus);
        	scGenData.setId(machId);
        }
	    scGenData.setPotiveZ(BaseDataSetter.createZValue(ZRPOS,ZXPOS, ZUnitType.PU));
	    
	    
	}

}
