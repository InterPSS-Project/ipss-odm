package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEMachinePosSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusEnumType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEMachinePosSeqZMapper <
    TNetXml extends NetworkXmlType, 
    TBusXml extends BusXmlType,
    TLineXml extends BranchXmlType,
    TXfrXml extends BranchXmlType,
    TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEMachinePosSeqZMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachinePosSeqZParser(ver);
	}
	
	/*
	 * Format 
	 * I, ID, ZRPOS, ZXPOS

	 */
	public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		String machId = dataParser.getString("ID");
		
		double ZRPOS = dataParser.getDouble("ZRPOS");
		
		double ZXPOS = dataParser.getDouble("ZXPOS");
		
		int i = dataParser.getInt("I");
	    final String busId = AbstractModelParser.BusIdPreFix+i;
	    
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
