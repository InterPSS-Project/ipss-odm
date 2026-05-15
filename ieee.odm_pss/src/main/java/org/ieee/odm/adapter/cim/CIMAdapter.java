/*
 * CIMAdapter.java
 *
 * CIM/CGMES adapter for InterPSS ODM.
 *
 * Phase 1: Foundation — parse RDF/XML, build CIMModel with indices
 * Phase 2: Bus-Branch Conversion — substations, VLs, buses, lines, transformers
 *
 * Copyright (C) 2025 www.interpss.org
 */
package org.ieee.odm.adapter.cim;

import java.util.List;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.cim.mapper.*;
import org.ieee.odm.adapter.cim.parser.CIMRdfParser;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.VoltageUnitType;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CIM/CGMES adapter for importing CGMES RDF/XML files into InterPSS ODM format.
 */
public class CIMAdapter extends AbstractODMAdapter {
    private static final Logger log = LoggerFactory.getLogger(CIMAdapter.class);
    private static int lastLoadCount = 0;
    
    /** Get the number of individual loads mapped in the last conversion */
    public static int getLastLoadCount() { return lastLoadCount; }

    static final double DEFAULT_BASE_MVA = 100.0;

    private CIMModel cimModel;

    @Override
    protected IODMModelParser parseInputFile(IFileReader din, String encoding) throws ODMException {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = din.readLine()) != null) {
                sb.append(line).append("\n");
            }

            CIMRdfParser rdfParser = new CIMRdfParser();
            Model jenaModel = rdfParser.parseString(sb.toString());

            this.cimModel = new CIMModel(jenaModel);
            cimModel.buildIndices();

            return buildOdmModel(cimModel, encoding);
        } catch (ODMException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error parsing CIM file", e);
            throw new ODMException("Error parsing CIM file: " + e.getMessage());
        }
    }

    @Override
    protected IODMModelParser parseInputFile(IODMAdapter.NetType type,
            IFileReader[] dins, String encoding) throws ODMException {
        try {
            CIMRdfParser rdfParser = new CIMRdfParser();
            org.apache.jena.rdf.model.Model merged =
                org.apache.jena.rdf.model.ModelFactory.createDefaultModel();

            for (IFileReader reader : dins) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                try {
                    Model part = rdfParser.parseString(sb.toString());
                    merged.add(part);
                    log.info("Merged CIM file, total: {} triples", merged.size());
                } catch (Exception e) {
                    log.warn("Skipping CIM file: {}", e.getMessage());
                }
            }

            this.cimModel = new CIMModel(merged);
            cimModel.buildIndices();

            return buildOdmModel(cimModel, encoding);
        } catch (ODMException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error parsing CIM files", e);
            throw new ODMException("Error parsing CIM files: " + e.getMessage());
        }
    }

    /**
     * Build the ODM AclfModelParser from the parsed CIMModel.
     * Phase 2: containers, buses (TopologicalNodes), lines, transformers.
     */
    private IODMModelParser buildOdmModel(CIMModel cimModel, String encoding) throws Exception {
        AclfModelParser parser = new AclfModelParser(encoding);
        parser.initCaseContentInfo(OriginalDataFormatEnumType.CIM);

        LoadflowNetXmlType net = parser.getNet();
        net.setId("CIM_Import");
        net.setBasePower(BaseDataSetter.createApparentPower(DEFAULT_BASE_MVA,
            org.ieee.odm.schema.ApparentPowerUnitType.MVA));

        // Phase 2: Convert containers
        convertContainers(cimModel, parser);

        // Phase 2: Convert buses from TopologicalNodes
        convertBuses(cimModel, parser);

        // Phase 2: Convert branches
        convertBranches(cimModel, parser);

        // Phase 3: Convert injections (loads, generators, shunts)
        convertInjections(cimModel, parser);

        log.info("CIM import: {} buses, {} branches",
            net.getBusList().getBus().size(),
            net.getBranchList().getBranch().size());

        return parser;
    }

    private void convertContainers(CIMModel cimModel, AclfModelParser parser) throws Exception {
        CIMSubstationMapper subMapper = new CIMSubstationMapper();
        subMapper.setCimModel(cimModel);
        for (CIMPropertyBag sub : cimModel.substations()) {
            subMapper.map(sub, parser);
        }

        CIMVoltageLevelMapper vlMapper = new CIMVoltageLevelMapper();
        vlMapper.setCimModel(cimModel);
        for (CIMPropertyBag vl : cimModel.voltageLevels()) {
            vlMapper.map(vl, parser);
        }
    }

    private void convertBuses(CIMModel cimModel, AclfModelParser parser) throws Exception {
        List<CIMPropertyBag> topoNodes = cimModel.topologicalNodes();
        List<CIMPropertyBag> connNodes = cimModel.connectivityNodes();
        List<CIMPropertyBag> busbars = cimModel.busbarSections();

        int busNumber = 1;

        if (!topoNodes.isEmpty()) {
            // Bus-branch model (EQ+TP loaded)
            log.info("Converting {} TopologicalNodes to buses", topoNodes.size());
            for (CIMPropertyBag tn : topoNodes) {
                String tnId = tn.getId();

                // Skip boundary TopologicalNodes — they represent external network
                if (cimModel.isBoundaryTopologicalNode(tnId)) {
                    log.debug("Skipping boundary TN: {}", tn.getName());
                    continue;
                }
                String name = tn.getName() != null ? tn.getName() : tn.getLocalId();
                Double baseKV = resolveTopoNodeVoltage(cimModel, tn);

                LoadflowBusXmlType bus = parser.createBus(tn.getLocalId());
                bus.setNumber((long) busNumber++);
                bus.setName(name);
                if (baseKV != null) {
                    bus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKV, VoltageUnitType.KV));
                }
                cimModel.mapBusId(tnId, tn.getLocalId());
            }
        } else if (!busbars.isEmpty()) {
            // EQ-only node-breaker: use BusbarSections
            log.info("Using {} BusbarSections as bus proxies", busbars.size());
            for (CIMPropertyBag bb : busbars) {
                String name = bb.getName() != null ? bb.getName() : bb.getLocalId();
                String vlUri = bb.getResourceId("Equipment.EquipmentContainer");
                Double baseKV = vlUri != null ? cimModel.getVLRatedVoltage(vlUri) : null;

                LoadflowBusXmlType bus = parser.createBus(bb.getLocalId());
                bus.setNumber((long) busNumber++);
                bus.setName(name);
                if (baseKV != null) {
                    bus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKV, VoltageUnitType.KV));
                }
                cimModel.mapBusId(bb.getId(), bb.getLocalId());
            }
        } else if (!connNodes.isEmpty()) {
            // Node-breaker without TopologicalNodes or BusbarSections
            // Use ConnectivityNodes as buses (e.g., CIMHub IEEE 118 / WECC 240)
            log.info("Using {} ConnectivityNodes as buses", connNodes.size());
            for (CIMPropertyBag cn : connNodes) {
                String cnId = cn.getId();
                String name = cn.getName() != null ? cn.getName() : cn.getLocalId();

                // Try to get base voltage from ConnectivityNodeContainer → BaseVoltage
                Double baseKV = null;
                String containerUri = cn.getResourceId("ConnectivityNode.ConnectivityNodeContainer");
                if (containerUri != null) {
                    baseKV = cimModel.getVLRatedVoltage(containerUri);
                }

                // For ConnectivityNode-as-bus, map the CN URI to bus
                LoadflowBusXmlType bus = parser.createBus(cn.getLocalId());
                bus.setNumber((long) busNumber++);
                bus.setName(name);
                if (baseKV != null) {
                    bus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKV, VoltageUnitType.KV));
                }
                cimModel.mapBusId(cnId, cn.getLocalId());
            }
        }
    }

    private Double resolveTopoNodeVoltage(CIMModel cimModel, CIMPropertyBag tn) {
        // Try TopologicalNode.BaseVoltage directly
        String bvUri = tn.getResourceId("TopologicalNode.BaseVoltage");
        if (bvUri != null) {
            Double v = cimModel.getBaseVoltageValue(bvUri);
            if (v != null) return v;
        }
        // Try via ConnectivityNodeContainer → VoltageLevel
        Double v = cimModel.getNominalVoltageForTopoNode(tn.getId());
        if (v != null) return v;

        // Fallback: try to get from connected equipment's BaseVoltage
        java.util.List<String> topoNodes = cimModel.getTopologicalNodesForEquipment(tn.getId());
        if (!topoNodes.isEmpty()) {
            v = cimModel.getNominalVoltageForTopoNode(topoNodes.get(0));
            if (v != null) return v;
        }

        // Last resort: try to parse from VL name or return a default
        String name = tn.getName();
        if (name != null) {
            try {
                return Double.parseDouble(name);
            } catch (NumberFormatException e) { /* ignore */ }
        }
        return null;
    }

    private void convertBranches(CIMModel cimModel, AclfModelParser parser) throws Exception {
        // Lines
        CIMLineMapper lineMapper = new CIMLineMapper(DEFAULT_BASE_MVA);
        lineMapper.setCimModel(cimModel);
        List<CIMPropertyBag> lineSegments = cimModel.acLineSegments();
        log.info("Processing {} ACLineSegments", lineSegments.size());
        for (CIMPropertyBag line : lineSegments) {
            lineMapper.map(line, parser);
        }

        // SeriesCompensators — treated as lines per PowSyBl
        List<CIMPropertyBag> seriesComps = cimModel.seriesCompensators();
        if (!seriesComps.isEmpty()) {
            log.info("Processing {} SeriesCompensators as lines", seriesComps.size());
            for (CIMPropertyBag sc : seriesComps) {
                lineMapper.mapSeriesCompensator(sc, parser);
            }
        }

        // Transformers — route 2W and 3W to separate mappers
        CIMTransformerMapper xfr2wMapper = new CIMTransformerMapper(DEFAULT_BASE_MVA);
        xfr2wMapper.setCimModel(cimModel);
        xfr2wMapper.indexEnds(cimModel.transformerEnds());

        CIMTransformer3WMapper xfr3wMapper = new CIMTransformer3WMapper(DEFAULT_BASE_MVA);
        xfr3wMapper.setCimModel(cimModel);

        // Pre-index ends for 3W detection
        java.util.Map<String, java.util.List<CIMPropertyBag>> endsByXfr = new java.util.HashMap<>();
        for (CIMPropertyBag end : cimModel.transformerEnds()) {
            String xfrId = end.getResourceId("PowerTransformerEnd.PowerTransformer");
            if (xfrId != null) {
                endsByXfr.computeIfAbsent(xfrId, k -> new java.util.ArrayList<>()).add(end);
            }
        }
        log.debug("Ends indexed for {} transformers, checking for 3W...", endsByXfr.size());

        for (CIMPropertyBag xfr : cimModel.powerTransformers()) {
            String xfrKey = xfr.getId();
            java.util.List<CIMPropertyBag> ends = endsByXfr.get(xfrKey);
            if (ends != null && ends.size() >= 3) {
                // Sort by end number
                ends.sort((a, b) -> {
                    int ea = a.getInt("TransformerEnd.endNumber",
                        a.getInt("PowerTransformerEnd.endNumber", 1));
                    int eb = b.getInt("TransformerEnd.endNumber",
                        b.getInt("PowerTransformerEnd.endNumber", 1));
                    return Integer.compare(ea, eb);
                });
                xfr3wMapper.map3W(xfr, ends, parser);
            } else {
                xfr2wMapper.map(xfr, parser);
            }
        }
    }

    private void convertInjections(CIMModel cimModel, AclfModelParser parser) throws Exception {
        int loadCount = 0;
        // Loads from EnergyConsumer
        CIMLoadMapper loadMapper = new CIMLoadMapper(DEFAULT_BASE_MVA);
        loadMapper.setCimModel(cimModel);
        for (CIMPropertyBag load : cimModel.energyConsumers()) {
            int before = loadMapper.getMappedCount();
            loadMapper.map(load, parser);
            if (loadMapper.getMappedCount() > before) loadCount++;
        }

        // Loads from AsynchronousMachine (motors treated as loads per PowSyBl)
        for (CIMPropertyBag asm : cimModel.asynchronousMachines()) {
            int before = loadMapper.getMappedCount();
            loadMapper.map(asm, parser);
            if (loadMapper.getMappedCount() > before) loadCount++;
        }

        // Generators from SynchronousMachine
        CIMGeneratorMapper genMapper = new CIMGeneratorMapper(DEFAULT_BASE_MVA);
        genMapper.setCimModel(cimModel);
        genMapper.indexGeneratingUnits(cimModel.generatingUnits());
        boolean hasSwing = false;
        for (CIMPropertyBag gen : cimModel.synchronousMachines()) {
            genMapper.map(gen, parser);
            // Check if this became a SWING bus
            String busId = genMapper.resolveBusId(gen.getId());
            if (busId != null) {
                org.ieee.odm.schema.LoadflowBusXmlType bus = parser.getAclfBus(busId);
                if (bus != null && bus.getGenData() != null
                        && bus.getGenData().getCode() == LFGenCodeEnumType.SWING) {
                    hasSwing = true;
                }
            }
        }

        // Generators from ExternalNetworkInjection (equivalent generators per PowSyBl)
        for (CIMPropertyBag eni : cimModel.externalNetworkInjections()) {
            genMapper.mapExternalNetworkInjection(eni, parser);
            String busId = genMapper.resolveBusId(eni.getId());
            if (busId != null) {
                org.ieee.odm.schema.LoadflowBusXmlType bus = parser.getAclfBus(busId);
                if (bus != null && bus.getGenData() != null
                        && bus.getGenData().getCode() == LFGenCodeEnumType.SWING) {
                    hasSwing = true;
                }
            }
        }

        // If no SWING bus was designated, make the first PV generator the SWING
        if (!hasSwing) {
            for (CIMPropertyBag gen : cimModel.synchronousMachines()) {
                String busId = genMapper.resolveBusId(gen.getId());
                if (busId != null) {
                    org.ieee.odm.schema.LoadflowBusXmlType bus = parser.getAclfBus(busId);
                    if (bus != null && bus.getGenData() != null
                            && bus.getGenData().getCode() == LFGenCodeEnumType.PV) {
                        bus.getGenData().setCode(LFGenCodeEnumType.SWING);
                        hasSwing = true;
                        log.info("Designated first PV generator as SWING: bus {}", busId);
                        break;
                    }
                }
            }
        }

        // Shunts
        lastLoadCount = loadCount;
        CIMShuntCompensatorMapper shuntMapper = new CIMShuntCompensatorMapper(DEFAULT_BASE_MVA);
        shuntMapper.setCimModel(cimModel);
        for (CIMPropertyBag shunt : cimModel.shuntCompensators()) {
            shuntMapper.map(shunt, parser);
        }
    }

    public CIMModel getCimModel() {
        return cimModel;
    }
}
