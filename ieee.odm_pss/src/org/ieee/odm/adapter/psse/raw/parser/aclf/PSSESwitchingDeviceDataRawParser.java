

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

    @Override
    public String[] getMetadata() {
        return new String[] {
            "I", "J", "CKT", "X", "RATE1", "RATE2", "RATE3", "RATE4", "RATE5", "RATE6", 
            "RATE7", "RATE8", "RATE9", "RATE10", "RATE11", "RATE12", "STAT", "NSTAT", 
            "MET", "STYPE", "NAME"
        };
    }

    @Override
    public void parseFields(final String str) throws ODMException {
        super.parseFields(str);
    }
}