package org.ieee.odm.adapter.psse.raw.parser.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;

/**
 * Class for processing PSS/E FACTS Device data line string
 * 
 */
public class PSSEFactsDeviceDataRawParser extends BasePSSEDataRawParser {

    public PSSEFactsDeviceDataRawParser(PsseVersion ver) {
        super(ver);
    }

    @Override
    public String[] getMetadata() {
        return new String[] {
            "NAME", "I", "J", "MODE", "PDES", "QDES", "VSET", "SHMX", "TRMX", "VTMN", 
            "VTMX", "VSMX", "IMX", "LINX", "RMPCT", "OWNER", "SET1", "SET2", "VSREF", 
            "FCREG", "MNAME", "NREG"
        };
    }

    @Override
    public void parseFields(final String str) throws ODMException {
        super.parseFields(str);
    }
}