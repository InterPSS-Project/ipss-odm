

package org.ieee.odm.adapter.psse.raw.parser.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E System Switching Device data line string
 * 
 */
public class PSSESwitchingDeviceDataRawParser extends BasePSSEDataRawParser {

    public PSSESwitchingDeviceDataRawParser(PsseVersion ver) {
        super(ver);
    }
    
    private static String[] META_DATA_v34_35 = new String[]{
        "I", "J", "CKT", "X", "RATE1", "RATE2", "RATE3", "RATE4", "RATE5", "RATE6", 
        "RATE7", "RATE8", "RATE9", "RATE10", "RATE11", "RATE12", "STAT", "NSTAT", 
        "MET", "STYPE", "NAME"
    };
    
    private static String[] META_DATA_v36 = new String[]{
    	"I", "J", "CKT", "X", "RSETNAM", "STAT", "NSTAT", "MET", "STYPE", "NAME"
    };
    

    @Override
    public String[] getMetadata() {
    	switch(this.version){
			case PSSE_34:
			case PSSE_35:
				return META_DATA_v34_35;
			case PSSE_36:
				return META_DATA_v36;
			default:
				return META_DATA_v36;

     }
        
        
    }

    @Override
    public void parseFields(final String str) throws ODMException {
        super.parseFields(str);
    }
}