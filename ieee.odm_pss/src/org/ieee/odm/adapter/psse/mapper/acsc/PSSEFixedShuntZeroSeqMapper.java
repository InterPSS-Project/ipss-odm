package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEBranchZeroSeqDataParser;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEFixedShuntZeroSeqParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEFixedShuntZeroSeqMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{

    public PSSEFixedShuntZeroSeqMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEFixedShuntZeroSeqParser(ver);
	}
    
    
    public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
    }

	

}
