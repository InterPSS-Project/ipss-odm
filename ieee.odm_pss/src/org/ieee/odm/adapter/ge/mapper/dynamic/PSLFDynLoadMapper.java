package org.ieee.odm.adapter.ge.mapper.dynamic;

import org.ieee.odm.adapter.ge.mapper.dynamic.load.PSLFDynLoadCMPLDWMapper;
import org.ieee.odm.adapter.ge.parser.dynamic.load.PSLFDynLoadCMPLDWDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;

public class PSLFDynLoadMapper {
	
	protected PSLFDynLoadCMPLDWMapper cmpldwMapper = new PSLFDynLoadCMPLDWMapper(new PSLFDynLoadCMPLDWDataParser());

	
    public PSLFDynLoadMapper(){
		
	}
 
    
    public void procLineString(String type,String lineStr, DStabModelParser parser) throws ODMException {
		if(type.equalsIgnoreCase("CMPLDW")){
			cmpldwMapper.procLineString(lineStr, parser);
		}
    }


}
