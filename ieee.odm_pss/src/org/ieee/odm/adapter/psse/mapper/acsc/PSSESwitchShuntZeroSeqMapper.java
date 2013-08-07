package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSESwitchedShuntZeroSeqParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShuntCompensatorBlockXmlType;
import org.ieee.odm.schema.ShuntCompensatorXmlType;

public class PSSESwitchShuntZeroSeqMapper <
    TNetXml  extends NetworkXmlType, 
    TBusXml  extends BusXmlType,
    TLineXml extends BranchXmlType,
    TXfrXml  extends BranchXmlType,
    TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSESwitchShuntZeroSeqMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSESwitchedShuntZeroSeqParser(ver);
	}
	
	//I, BZ1, BZ2, ... BZ8
	public void procLineString(String lineStr, BaseAcscModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("I");
   
      	final String busId = AbstractModelParser.BusIdPreFix+i;
      	
        ShortCircuitBusXmlType scBus =(ShortCircuitBusXmlType) parser.getBus(busId);
        /*
         The zero sequence admittance switched on at a bus is determined from the bus’ positive sequence 
         value, with the same number of blocks and steps in each block switched on
        */
        ShuntCompensatorXmlType shunt = scBus.getShuntCompensator();
        int k = 1;
        if(shunt !=null){
           for(ShuntCompensatorBlockXmlType block: shunt.getBlock()){
        	    block.setZeroSeqIncrementB(
        			BaseDataSetter.createReactivePowerValue(dataParser.getDouble("BZ"+k++), ReactivePowerUnitType.PU));
           }
        }
        
	}
}
