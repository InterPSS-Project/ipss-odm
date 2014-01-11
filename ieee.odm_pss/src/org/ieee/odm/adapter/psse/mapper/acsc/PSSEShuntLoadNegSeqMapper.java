package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEMachineZeroSeqZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.YUnitType;

public class PSSEShuntLoadNegSeqMapper <
  TNetXml extends NetworkXmlType, 
  TBusXml extends BusXmlType,
  TLineXml extends BranchXmlType,
  TXfrXml extends BranchXmlType,
  TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{

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
	
	public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
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
