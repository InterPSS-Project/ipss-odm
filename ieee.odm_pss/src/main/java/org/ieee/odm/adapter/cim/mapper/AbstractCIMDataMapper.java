/*
 * AbstractCIMDataMapper.java
 *
 * Base mapper for converting CIM elements to ODM schema objects.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for CIM → ODM data mappers.
 */
public abstract class AbstractCIMDataMapper {
    protected static final Logger log = LoggerFactory.getLogger(AbstractCIMDataMapper.class);

    protected CIMModel cimModel;

    public void setCimModel(CIMModel model) {
        this.cimModel = model;
    }

    /**
     * Map a CIM property bag to ODM objects via the parser.
     */
    public abstract void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception;

    /**
     * Resolve the ODM bus ID for a conducting equipment by finding its
     * connected topological node and looking up the mapped bus ID.
     * Returns the bus ID or null if not found.
     * Only returns a bus ID if the bus was actually created in the parser.
     */
    public String resolveBusId(String equipmentId) {
        if (cimModel == null) return null;
        java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(equipmentId);
        if (!topoNodes.isEmpty()) {
            String busId = cimModel.getBusId(topoNodes.get(0));
            return busId; // null if bus was not created (boundary node)
        }
        return null;
    }

    /**
     * Resolve the two bus IDs for a branch (line or transformer).
     * Returns null for any bus that was not actually created in the parser.
     */
    protected String[] resolveBranchBusIds(String equipmentId) {
        if (cimModel == null) return new String[]{null, null};
        java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(equipmentId);
        String bus1 = null, bus2 = null;
        if (topoNodes.size() >= 2) {
            bus1 = cimModel.getBusId(topoNodes.get(0));
            bus2 = cimModel.getBusId(topoNodes.get(1));
        } else if (topoNodes.size() == 1) {
            bus1 = cimModel.getBusId(topoNodes.get(0));
        }
        return new String[]{bus1, bus2};
    }
}
