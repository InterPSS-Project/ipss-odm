package org.ieee.odm.ge_pslf;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.adapter.GenericODMAdapter;
import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.junit.Test;

public class PSLF_CMPLDW_Mapper_Test {
	
	
	
	@Test
	public void testCMPLDWMapper(){
		
          GenericODMAdapter adapter = new GenericODMAdapter(ODMFileFormatEnum.PsseV30,ODMFileFormatEnum.GePSLF);
		  
		  adapter.parseInputFile(NetType.DStabNet, new String[]{
			"testData/psse/IEEE9Bus/ieee9.raw",
			"testData/ge/ieee9_Dyn_CMPLDW.dyd"
			//"testData/ge/ieee9_onlyGen_GE.dyd"
	        });
		  
		    DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
			dstabParser.stdout();
		    
			
	
		
	}
	

}
