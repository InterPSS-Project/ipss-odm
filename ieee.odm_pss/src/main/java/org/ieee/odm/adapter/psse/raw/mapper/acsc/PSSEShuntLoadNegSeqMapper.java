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
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.YUnitType;

public class PSSEShuntLoadNegSeqMapper extends BasePSSEDataRawMapper{

	public PSSEShuntLoadNegSeqMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEMachineZeroSeqZParser(ver);
	}
	
	
	
	/*
	 * I      Bus number; bus I must be present in the working case.
       GNEG   Active component of negative sequence shunt admittance to ground, including all
              load to be represented at the bus; entered in pu.
       BNEG   Reactive component of negative sequence shunt admittance to ground, including all
              load to be represented at the bus; entered in pu.
	 * 
	 * Negative sequence admittances corresponding to "fixed bus shunts", which is different from bus load data
	 * 
	 * Only exceptional negative sequence shunt load (i.e. differ from postive sequence load)
	 * should be entered in the sequence file, since it is set be to same as the positive
	 * sequence data
	 * 
	 * 
	 * For any bus where no such data record is specified, or GNEG and BNEG are both specified as zero,
       the load elements are assumed to be equal in the positive and negative sequence networks.
	 * 
	 */
	
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		
		int i = dataParser.getInt("I");
		final String busId = IODMModelParser.BusIdPreFix+i;
		double gNeg = dataParser.getDouble("GNEG");
		double bNeg = dataParser.getDouble("BNEG");
		ShortCircuitBusXmlType scBusXmlType= (ShortCircuitBusXmlType) parser.getBus(busId);
		ShortCircuitLoadDataXmlType load = AcscParserHelper.getDefaultScLoad(scBusXmlType.getLoadData());
		load.setShuntLoadNegativeY(BaseDataSetter.createYValue(gNeg, bNeg, YUnitType.PU));
		//check against the positive sequence 
		//
		
	}

}
