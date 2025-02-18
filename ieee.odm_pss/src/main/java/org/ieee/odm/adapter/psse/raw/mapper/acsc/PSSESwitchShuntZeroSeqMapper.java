package org.ieee.odm.adapter.psse.raw.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.acsc.PSSESwitchedShuntZeroSeqParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.SwitchedShuntBlockXmlType;
import org.ieee.odm.schema.SwitchedShuntXmlType;

public class PSSESwitchShuntZeroSeqMapper extends BasePSSEDataRawMapper{
	
	public PSSESwitchShuntZeroSeqMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSESwitchedShuntZeroSeqParser(ver);
	}
	
	//I, BZ1, BZ2, ... BZ8
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("I");
   
      	final String busId = IODMModelParser.BusIdPreFix+i;
      	
        ShortCircuitBusXmlType scBus =(ShortCircuitBusXmlType) parser.getBus(busId);
        /*
         The zero sequence admittance switched on at a bus is determined from the bus� positive sequence 
         value, with the same number of blocks and steps in each block switched on
        */
        SwitchedShuntXmlType shunt = scBus.getSwitchedShunt();
        int k = 1;
        if(shunt !=null){
           for(SwitchedShuntBlockXmlType block: shunt.getBlock()){
        	    block.setZeroSeqIncrementB(
        			BaseDataSetter.createReactivePowerValue(dataParser.getDouble("BZ"+k++), ReactivePowerUnitType.PU));
           }
        }
        
	}
}
