package org.ieee.odm.adapter.matpower;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatpowerAdapter extends AbstractODMAdapter {
    private static final Logger log = LoggerFactory.getLogger(MatpowerAdapter.class);

    private static final int NONE = 0;
    private static final int BUS = 1;
    private static final int GEN = 2;
    private static final int BRANCH = 3;
    private static final int AREA = 4;
    private static final int GENCOST = 5;

    private final Map<String, Integer> branchCircuitIndex = new HashMap<>();

    @Override
    protected AclfModelParser parseInputFile(final IFileReader din, String encoding) throws ODMException {
        AclfModelParser parser = new AclfModelParser(encoding);
        parser.initCaseContentInfo(OriginalDataFormatEnumType.OPF_MATPOWER);

        LoadflowNetXmlType baseCaseNet = parser.getAclfNet();

        String line = readNextMeaningfulLine(din);
        if (line == null) {
            throw new ODMException("Empty MATPOWER input");
        }
        processTitleData(line, baseCaseNet);

        int section = NONE;
        while ((line = din.readLine()) != null) {
            String trimmed = stripInlineComment(line).trim();
            if (trimmed.length() == 0) {
                continue;
            }
            if (trimmed.startsWith("]")) {
                section = NONE;
                continue;
            }
            if (trimmed.startsWith("%")) {
                continue;
            }

            try {
                if (trimmed.startsWith("mpc.baseMVA")) {
                    processBaseMvaData(trimmed, baseCaseNet);
                }
                else if (startsWithAssignment(trimmed, "mpc.bus")) {
                    section = BUS;
                }
                else if (startsWithAssignment(trimmed, "mpc.gen")) {
                    section = GEN;
                }
                else if (startsWithAssignment(trimmed, "mpc.branch")) {
                    section = BRANCH;
                }
                else if (startsWithAssignment(trimmed, "mpc.area") || startsWithAssignment(trimmed, "mpc.areas")) {
                    section = AREA;
                }
                else if (startsWithAssignment(trimmed, "mpc.gencost")) {
                    section = GENCOST;
                }
                else if (section == BUS) {
                    processBusData(trimmed, parser);
                }
                else if (section == GEN) {
                    processGenData(trimmed, parser);
                }
                else if (section == BRANCH) {
                    processBranchData(trimmed, parser);
                }
                else if (section == AREA || section == GENCOST || trimmed.startsWith("mpc.version")) {
                }
            } catch (ODMException | ODMBranchDuplicationException | RuntimeException e) {
                log.error(e.toString() + "\n" + line);
                logErr(e.toString() + "\n" + line);
            }
        }

        return parser;
    }

    @Override
    protected IODMModelParser parseInputFile(IODMAdapter.NetType type, IFileReader[] din, String encoding)
            throws ODMException {
        throw new ODMException("not implemented yet");
    }

    private String readNextMeaningfulLine(IFileReader din) throws ODMException {
        String line;
        while ((line = din.readLine()) != null) {
            String trimmed = stripInlineComment(line).trim();
            if (trimmed.length() > 0) {
                return trimmed;
            }
        }
        return null;
    }

    private void processTitleData(String line, LoadflowNetXmlType baseCaseNet) {
        String normalized = normalizeLine(line);
        StringTokenizer tokenizer = new StringTokenizer(normalized, " ");
        if (tokenizer.countTokens() >= 4 && "function".equals(tokenizer.nextToken())) {
            tokenizer.nextToken();
            tokenizer.nextToken();
            String id = tokenizer.nextToken();
            baseCaseNet.setId("Aclf_from_Matpower_" + id);
            return;
        }
        baseCaseNet.setId("Aclf_from_Matpower");
    }

    private void processBaseMvaData(String line, LoadflowNetXmlType baseCaseNet) {
        int index = line.indexOf('=');
        if (index < 0) {
            return;
        }
        String value = normalizeLine(line.substring(index + 1));
        baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(str2d(value)));
    }

    private void processBusData(String line, AclfModelParser parser) throws ODMException {
        String[] fields = parseFields(line);

        String busId = IODMModelParser.BusIdPreFix + fields[0];
        LoadflowBusXmlType bus = parser.createBus(busId);
        bus.setNumber(Long.valueOf(fields[0]));
        bus.setName(busId);

        int type = str2i(fields[1]);
        double pd = str2d(fields[2]);
        double qd = str2d(fields[3]);
        double gs = str2d(fields[4]);
        double bs = str2d(fields[5]);
        int area = str2i(fields[6]);
        double vm = str2d(fields[7]);
        double va = str2d(fields[8]);
        double baseKv = str2d(fields[9]);
        int zone = str2i(fields[10]);
        bus.setAreaNumber(area);
        bus.setZoneNumber(zone);
        bus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv, VoltageUnitType.KV));
        bus.setVoltage(BaseDataSetter.createVoltageValue(vm, VoltageUnitType.PU));
        bus.setAngle(BaseDataSetter.createAngleValue(va, AngleUnitType.DEG));

        if (pd != 0.0 || qd != 0.0) {
            AclfDataSetter.setLoadData(bus, LFLoadCodeEnumType.CONST_P, pd, qd, ApparentPowerUnitType.MVA);
        }

        LFGenCodeEnumType genCode = toGenCode(type);
        AclfDataSetter.setGenData(bus, genCode);

        double baseMva = parser.getAclfNet().getBasePower().getValue();
        if (gs != 0.0 || bs != 0.0) {
            AclfDataSetter.setBusShuntY(bus, gs / baseMva, bs / baseMva, YUnitType.PU);
        }
    }

    private void processGenData(String line, AclfModelParser parser) {
        String[] fields = parseFields(line);

        String busId = IODMModelParser.BusIdPreFix + fields[0];
        LoadflowBusXmlType bus = parser.getAclfBus(busId);
        if (bus == null) {
            logErr("Generator references undefined bus: " + busId);
            return;
        }

        LoadflowGenDataXmlType gen = AclfParserHelper.createContriGen(bus);
        gen.setPower(BaseDataSetter.createPowerValue(str2d(fields[1]), str2d(fields[2]), ApparentPowerUnitType.MVA));
        gen.setQLimit(BaseDataSetter.createReactivePowerLimit(str2d(fields[3]), str2d(fields[4]), ReactivePowerUnitType.MVAR));
        gen.setDesiredVoltage(BaseDataSetter.createVoltageValue(str2d(fields[5]), VoltageUnitType.PU));
        gen.setMvaBase(BaseDataSetter.createPowerMvaValue(str2d(fields[6])));
        gen.setOffLine(str2i(fields[7]) <= 0);
        gen.setPLimit(BaseDataSetter.createActivePowerLimit(str2d(fields[8]), str2d(fields[9]), org.ieee.odm.schema.ActivePowerUnitType.MW));
    }

        private void processBranchData(String line, AclfModelParser parser)
            throws ODMException, ODMBranchDuplicationException {
        String[] fields = parseFields(line);

        String fromBusId = IODMModelParser.BusIdPreFix + fields[0];
        String toBusId = IODMModelParser.BusIdPreFix + fields[1];
        String circuitId = nextCircuitId(fromBusId, toBusId);

        double r = str2d(fields[2]);
        double x = str2d(fields[3]);
        double b = str2d(fields[4]);
        double rateA = str2d(fields[5]);
        double rateB = str2d(fields[6]);
        double rateC = str2d(fields[7]);
        double ratio = str2d(fields[8]);
        double angle = str2d(fields[9]);
        int status = str2i(fields[10]);

        boolean hasTap = ratio != 0.0;
        boolean hasPhaseShift = angle != 0.0;

        if (hasPhaseShift) {
            PSXfrBranchXmlType branch = parser.createPSXfrBranch(fromBusId, toBusId, circuitId);
            AclfDataSetter.createPhaseShiftXfrData(branch, r, x, ZUnitType.PU,
                    hasTap ? ratio : 1.0, 1.0, angle, 0.0, AngleUnitType.DEG);
            AclfDataSetter.setBranchRatingLimitData(branch.getRatingLimit(), rateA, rateB, rateC, ApparentPowerUnitType.MVA);
            branch.setOffLine(status <= 0);
        }
        else if (hasTap) {
            XfrBranchXmlType branch = parser.createXfrBranch(fromBusId, toBusId, circuitId);
            AclfDataSetter.createXformerData(branch, r, x, ZUnitType.PU, ratio, 1.0);
            AclfDataSetter.setBranchRatingLimitData(branch.getRatingLimit(), rateA, rateB, rateC, ApparentPowerUnitType.MVA);
            branch.setOffLine(status <= 0);
        }
        else {
            LineBranchXmlType branch = parser.createLineBranch(fromBusId, toBusId, circuitId);
            AclfDataSetter.setLineData(branch, r, x, ZUnitType.PU, 0.0, b, YUnitType.PU);
            AclfDataSetter.setBranchRatingLimitData(branch.getRatingLimit(), rateA, rateB, rateC, ApparentPowerUnitType.MVA);
            branch.setOffLine(status <= 0);
        }
    }

    private LFGenCodeEnumType toGenCode(int type) {
        if (type == 3) {
            return LFGenCodeEnumType.SWING;
        }
        if (type == 2) {
            return LFGenCodeEnumType.PV;
        }
        if (type == 4) {
            return LFGenCodeEnumType.OFF;
        }
        return LFGenCodeEnumType.PQ;
    }

    private String nextCircuitId(String fromBusId, String toBusId) {
        String key = fromBusId + "->" + toBusId;
        Integer count = this.branchCircuitIndex.get(key);
        int next = count == null ? 1 : count + 1;
        this.branchCircuitIndex.put(key, next);
        return String.valueOf(next);
    }

    private boolean startsWithAssignment(String line, String token) {
        int index = line.indexOf('=');
        if (index < 0) {
            return line.startsWith(token);
        }
        return line.substring(0, index).trim().equals(token);
    }

    private String[] parseFields(String line) {
        String normalized = normalizeLine(stripInlineComment(line));
        if (normalized.length() == 0) {
            return new String[0];
        }
        return normalized.split("\\s+");
    }

    private String stripInlineComment(String line) {
        int commentIndex = line.indexOf('%');
        return commentIndex >= 0 ? line.substring(0, commentIndex) : line;
    }

    private String normalizeLine(String line) {
        return line.replace('[', ' ')
                .replace(']', ' ')
                .replace(';', ' ')
                .replace(',', ' ')
                .trim();
    }

    private double str2d(String value) {
        if ("Inf".equalsIgnoreCase(value) || "+Inf".equalsIgnoreCase(value)
                || "Infinity".equalsIgnoreCase(value) || "+Infinity".equalsIgnoreCase(value)) {
            return Double.POSITIVE_INFINITY;
        }
        if ("-Inf".equalsIgnoreCase(value) || "-Infinity".equalsIgnoreCase(value)) {
            return Double.NEGATIVE_INFINITY;
        }
        return Double.parseDouble(value);
    }

    private int str2i(String value) {
        return Integer.parseInt(value);
    }
}