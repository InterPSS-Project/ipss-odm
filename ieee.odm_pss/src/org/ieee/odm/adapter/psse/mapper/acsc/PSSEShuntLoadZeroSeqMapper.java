package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSELoadZeroSeqDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.YUnitType;

public class PSSEShuntLoadZeroSeqMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEShuntLoadZeroSeqMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSELoadZeroSeqDataParser(ver);
	}	
	
	/*
	 * I, GZERO, BZERO
	 * NOTE: 
	 * 1) For any bus where no such data record is specified, no shunt load component is represented in the
          zero sequence.
	 * 2) Zero sequence admittances corresponding to fixed bus shunts (refer to Fixed Bus Shunt Data) are
       specified in the zero sequence fixed shunt data records 
	 */
    
	
	public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("I");
		final String busId = AbstractModelParser.BusIdPreFix+i;
		double gZero = dataParser.getDouble("GZERO");
		double bZero = dataParser.getDouble("BZERO");
		ShortCircuitBusXmlType scBusXmlType= (ShortCircuitBusXmlType) parser.getBus(busId);
		((ShortCircuitLoadDataXmlType)scBusXmlType.getLoadData().getEquivLoad().getValue())
						.setShuntLoadNegativeY(BaseDataSetter.createYValue(gZero, bZero, YUnitType.PU));
	}
}
