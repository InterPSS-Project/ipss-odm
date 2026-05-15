/*
 * CIMShuntCompensatorMapper.java
 *
 * Maps CIM LinearShuntCompensator → ODM ShuntY on bus.
 */

package org.ieee.odm.adapter.cim.mapper;

import org.ieee.odm.adapter.cim.CIMModel;
import org.ieee.odm.adapter.cim.CIMPropertyBag;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.YUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps CIM ShuntCompensator to ODM shunt admittance on bus.
 * CIM LinearShuntCompensator stores bPerSection, gPerSection, maximumSections.
 * Total shunt = bPerSection * normalSections (or maximumSections).
 */
public class CIMShuntCompensatorMapper extends AbstractCIMDataMapper {
    private static final Logger log = LoggerFactory.getLogger(CIMShuntCompensatorMapper.class);

    private double baseMVA;

    public CIMShuntCompensatorMapper(double baseMVA) {
        this.baseMVA = baseMVA;
    }

    @Override
    public void map(CIMPropertyBag bag, AclfModelParser parser) throws Exception {
        String shuntId = bag.getLocalId();
        String name = bag.getName();
        if (name == null) name = shuntId;

        // Get shunt parameters — handle both Linear and Nonlinear shunts
        double totalB;
        double totalG;

        // Try LinearShuntCompensator properties first
        double bPerSection = bag.getDouble("LinearShuntCompensator.bPerSection", 0.0);
        double gPerSection = bag.getDouble("LinearShuntCompensator.gPerSection", 0.0);

        if (bPerSection != 0.0 || gPerSection != 0.0) {
            // Linear shunt: b = bPerSection * sections
            int sections = bag.getInt("ShuntCompensator.normalSections",
                         bag.getInt("ShuntCompensator.maximumSections", 1));
            totalB = bPerSection * sections;
            totalG = gPerSection * sections;
        } else {
            // Nonlinear shunt: find the point matching normalSections
            totalB = 0.0;
            totalG = 0.0;
            int normalSections = bag.getInt("ShuntCompensator.normalSections",
                            bag.getInt("ShuntCompensator.maximumSections", 1));
            if (cimModel != null) {
                // Look up NonlinearShuntCompensatorPoint entries
                // Try exact match first, then fall back to minimum section
                java.util.List<org.apache.jena.query.QuerySolution> points = cimModel.sparqlSelect(
                    "PREFIX cim: <" + cimModel.getCimNamespace() + "> " +
                    "SELECT ?b ?g ?section WHERE { " +
                    "  ?point cim:NonlinearShuntCompensatorPoint.NonlinearShuntCompensator <" + bag.getResource().getURI() + "> . " +
                    "  ?point cim:NonlinearShuntCompensatorPoint.sectionNumber ?section . " +
                    "  ?point cim:NonlinearShuntCompensatorPoint.b ?b . " +
                    "  ?point cim:NonlinearShuntCompensatorPoint.g ?g . " +
                    "  FILTER(?section = " + normalSections + ") " +
                    "}");
                if (!points.isEmpty()) {
                    totalB = points.get(0).getLiteral("b").getDouble();
                    totalG = points.get(0).getLiteral("g").getDouble();
                } else {
                    // No exact section match — sum all sections <= normalSections (PowSyBl behavior)
                    java.util.List<org.apache.jena.query.QuerySolution> allPoints = cimModel.sparqlSelect(
                        "PREFIX cim: <" + cimModel.getCimNamespace() + "> " +
                        "SELECT ?b ?g ?section WHERE { " +
                        "  ?point cim:NonlinearShuntCompensatorPoint.NonlinearShuntCompensator <" + bag.getResource().getURI() + "> . " +
                        "  ?point cim:NonlinearShuntCompensatorPoint.sectionNumber ?section . " +
                        "  ?point cim:NonlinearShuntCompensatorPoint.b ?b . " +
                        "  ?point cim:NonlinearShuntCompensatorPoint.g ?g . " +
                        "  FILTER(?section <= " + normalSections + ") " +
                        "}");
                    for (var pt : allPoints) {
                        totalB += pt.getLiteral("b").getDouble();
                        totalG += pt.getLiteral("g").getDouble();
                    }
                    // If still zero, try using the minimum section point
                    if (totalB == 0.0 && totalG == 0.0) {
                        java.util.List<org.apache.jena.query.QuerySolution> minPoint = cimModel.sparqlSelect(
                            "PREFIX cim: <" + cimModel.getCimNamespace() + "> " +
                            "SELECT ?b ?g WHERE { " +
                            "  ?point cim:NonlinearShuntCompensatorPoint.NonlinearShuntCompensator <" + bag.getResource().getURI() + "> . " +
                            "  ?point cim:NonlinearShuntCompensatorPoint.sectionNumber ?section . " +
                            "  ?point cim:NonlinearShuntCompensatorPoint.b ?b . " +
                            "  ?point cim:NonlinearShuntCompensatorPoint.g ?g . " +
                            "} ORDER BY ?section LIMIT 1");
                        if (!minPoint.isEmpty()) {
                            totalB = minPoint.get(0).getLiteral("b").getDouble();
                            totalG = minPoint.get(0).getLiteral("g").getDouble();
                        }
                    }
                }
            }
        }

        if (totalB == 0.0 && totalG == 0.0) {
            log.debug("Skipping zero shunt: {}", name);
            return;
        }

        // Resolve bus
        String busId = resolveBusId(bag.getId());
        if (busId == null) {
            log.warn("Skipping shunt {} - cannot resolve bus", name);
            return;
        }

        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus == null) {
            log.warn("Skipping shunt {} - bus {} not found", name, busId);
            return;
        }

        // Convert to PU
        Double baseKV = null;
        if (cimModel != null) {
            java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(bag.getId());
            if (!topoNodes.isEmpty()) {
                baseKV = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
            }
        }
        if (baseKV == null) baseKV = 100.0;

        double baseY = baseMVA / (baseKV * baseKV);
        double bPU = totalB / baseY;
        double gPU = totalG / baseY;

        // Add to bus shunt Y (accumulates with existing)
        AclfDataSetter.addBusShuntY(bus, gPU, bPU, YUnitType.PU);

        log.info(String.format("Created shunt: %s on bus %s, B=%.6f S (%.4f PU)",
            name, busId, totalB, bPU));
    }
}
