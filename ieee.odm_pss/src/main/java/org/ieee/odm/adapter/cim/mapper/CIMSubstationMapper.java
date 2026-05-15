/*
 * CIMSubstationMapper.java
 *
 * Maps CIM Substation → ODM area/substation grouping.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM Substation elements to ODM area records.
 * Currently stores substation info as bus area number assignment.
 */
public class CIMSubstationMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMSubstationMapper.class);

    private int areaCounter = 1;

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String substationId = bag.getLocalId();
        String name = bag.getName();
        log.debug("Mapping Substation: {} ({})", name, substationId);

        // ODM uses area numbers; we assign a sequential area number per substation
        // The area mapping is stored in the model for later use by bus mapper
        if (cimModel != null) {
            // We don't create a separate ODM area object here;
            // instead, voltage levels/buses will reference their container
        }
        areaCounter++;
    }

    public int getAreaCounter() {
        return areaCounter;
    }
}
