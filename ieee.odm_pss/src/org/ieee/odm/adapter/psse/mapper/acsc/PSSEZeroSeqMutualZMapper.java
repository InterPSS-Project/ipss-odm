package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEZeroSeqMulualZParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEZeroSeqMutualZMapper extends BasePSSEDataMapper{

   public PSSEZeroSeqMutualZMapper(PsseVersion ver) {
       super(ver);
       this.dataParser = new PSSEZeroSeqMulualZParser(ver);
   }
   
   
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		//TODO
	}

}
