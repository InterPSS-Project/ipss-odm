package org.ieee.odm.adapter.psse.raw.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.acsc.PSSELoadZeroSeqDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.YUnitType;

public class PSSEShuntLoadZeroSeqMapper extends BasePSSEDataRawMapper{
	
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
    
	
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("I");
		final String busId = IODMModelParser.BusIdPreFix+i;
		double gZero = dataParser.getDouble("GZERO");
		double bZero = dataParser.getDouble("BZERO");
		ShortCircuitBusXmlType scBusXmlType= (ShortCircuitBusXmlType) parser.getBus(busId);
		ShortCircuitLoadDataXmlType load = AcscParserHelper.getDefaultScLoad(scBusXmlType.getLoadData());
		load.setShuntLoadNegativeY(BaseDataSetter.createYValue(gZero, bZero, YUnitType.PU));
	}
}
