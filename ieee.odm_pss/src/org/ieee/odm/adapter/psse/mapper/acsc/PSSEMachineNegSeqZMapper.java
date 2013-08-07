package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEMachineNegSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEMachineNegSeqZMapper <
       TNetXml extends NetworkXmlType, 
       TBusXml extends BusXmlType,
       TLineXml extends BranchXmlType,
       TXfrXml extends BranchXmlType,
       TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
    
	public PSSEMachineNegSeqZMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachineNegSeqZParser(ver);
	}
	
	/*
	 * Format 
	 * I, ID, ZRNEG, ZXNEG

	 */
	public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		String machId = dataParser.getString("ID");
		
		double ZRNEG = dataParser.getDouble("ZRNEG");
		
		double ZXNEG = dataParser.getDouble("ZXNEG");
		
		int i = dataParser.getInt("I");
	    final String busId = AbstractModelParser.BusIdPreFix+i;
	    
	    ShortCircuitBusXmlType acscBus=(ShortCircuitBusXmlType) parser.getBus(busId);
	    ShortCircuitGenDataXmlType scGenData= AcscParserHelper.getAcscContritueGen(acscBus, machId);
	    
	    scGenData.setNegativeZ(BaseDataSetter.createZValue(ZRNEG,ZXNEG, ZUnitType.PU));
	    
	    
	}
}
