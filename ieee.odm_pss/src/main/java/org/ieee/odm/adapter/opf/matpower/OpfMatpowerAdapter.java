package org.ieee.odm.adapter.opf.matpower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.opf.OpfDataSetter;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.ActivePowerLimitXmlType;
import org.ieee.odm.schema.ActivePowerPriceEnumType;
import org.ieee.odm.schema.ActivePowerRatingXmlType;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.ConstraintsXmlType;
import org.ieee.odm.schema.CostModelEnumType;
import org.ieee.odm.schema.DCLineData2TXmlType;
import org.ieee.odm.schema.DcLineControlModeEnumType;
import org.ieee.odm.schema.DcLineOperationModeEnumType;
import org.ieee.odm.schema.IncCostXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.ObjectFactory;
import org.ieee.odm.schema.OpfBranchXmlType;
import org.ieee.odm.schema.OpfGenBusXmlType;
import org.ieee.odm.schema.OpfGenOperatingModeEnumType;
import org.ieee.odm.schema.OpfNetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpfMatpowerAdapter extends AbstractODMAdapter {
    private static final Logger log = LoggerFactory.getLogger(OpfMatpowerAdapter.class);

	private static final int BusData = 1;
	private static final int BranchData = 2;
	private static final int GencostData = 3;
	private static final int AreaData = 4;
	private static final int GenData = 5;
	private static final int BranchNameData = 6;
	private static final int GenNameData = 7;
	private static final int GenTypeData = 8;
	private static final int GenFuelData = 9;
	private static final int DcLineData = 10;
	private static final Pattern QUOTED_TOKEN = Pattern.compile("'([^']*)'");

	private ArrayList<OpfGenBusXmlType> opfGenContainer = null;
	private ArrayList<LoadflowGenDataXmlType> opfGenDataContainer = null;
	private ArrayList<OpfBranchXmlType> opfBranchContainer = null;

	private ObjectFactory factory = null;

	public OpfMatpowerAdapter() {
		super();
		this.factory = new ObjectFactory();
		this.opfGenContainer = new ArrayList<OpfGenBusXmlType>();
		this.opfGenDataContainer = new ArrayList<LoadflowGenDataXmlType>();
		this.opfBranchContainer = new ArrayList<OpfBranchXmlType>();
	}

	@Override
	protected OpfModelParser parseInputFile(final IFileReader din,
			String encoding) throws ODMException {
		OpfModelParser parser = new OpfModelParser(encoding);
		parser.getStudyCase().setId("ODM_OPF_Studycase");
		parser.setOPFTransInfo(OriginalDataFormatEnumType.OPF_MATPOWER);
		OpfNetworkXmlType baseCaseNet = parser.getOpfNetwork();

		List<String> lines = new ArrayList<String>();
		String str = null;
		while ((str = din.readLine()) != null) {
			lines.add(str);
		}
		if (lines.isEmpty()) {
			return parser;
		}

		processTitleData(lines.get(0), baseCaseNet);
		Set<String> generatorBusIds = collectGeneratorBusIds(lines);

		int dataType = 0;
		int gencnt = 0;
		int branchNameCnt = 0;
		int genNameCnt = 0;
		int genTypeCnt = 0;
		int genFuelCnt = 0;
		int dcLineCnt = 0;
		// Matpower does not specify cirId for parallel lines
		// Here cirId is used for this purpose
		//int cirId = 1;
		for (int i = 1; i < lines.size(); i++) {
			str = lines.get(i);
			try {
				String blockName = assignmentName(str);
				if (isDataBlockEnd(str)) {
					dataType = 0;
				} else if (isCommentOrBlank(str)) {
					// comment line, do nothing but go to the next line;
				} else if ("mpc.baseMVA".equals(blockName)) {
					processBaseMVAData(str, baseCaseNet);
				} else if ("mpc.version".equals(blockName)) {
					// processBusData(str, parser);
				} else if ("mpc.bus".equals(blockName)) {
					dataType = BusData;
					log.debug("load bus data");
				} else if ("mpc.gen".equals(blockName)) {
					dataType = GenData;
					log.debug("load gen data");
				} else if ("mpc.branch".equals(blockName)) {
					dataType = BranchData;
					log.debug("load branch data");
				} else if (isAreaBlock(blockName)) {
					dataType = AreaData;
					log.debug("load interchange data");
				} else if ("mpc.gencost".equals(blockName)) {
					dataType = GencostData;
					log.debug("load gencost data");
				} else if ("mpc.branch_name".equals(blockName)) {
					dataType = BranchNameData;
					log.debug("load branch name data");
				} else if ("mpc.gen_name".equals(blockName)) {
					dataType = GenNameData;
					log.debug("load gen name data");
				} else if ("mpc.gentype".equals(blockName)) {
					dataType = GenTypeData;
					log.debug("load gen type data");
				} else if ("mpc.genfuel".equals(blockName)) {
					dataType = GenFuelData;
					log.debug("load gen fuel data");
				} else if ("mpc.dcline".equals(blockName)) {
					dataType = DcLineData;
					log.debug("load dc line data");
				} else if (blockName.startsWith("mpc.")) {
					dataType = 0;
				} else if (dataType == BusData && isDataRow(str)) {
					processBusData(str, parser, generatorBusIds);
				} else if (dataType == GenData && isDataRow(str)) {
					processGenData(str, parser);
				} else if (dataType == BranchData && isDataRow(str)) {
					processBranchData(str, /*cirId++,*/ parser);
				} else if (dataType == AreaData) {
					// TODO: Not implemented yet.
					// processAreaData(str, parser);
				} else if (dataType == GencostData && isDataRow(str)) {
					int sizeGen = opfGenContainer.size();
					if (gencnt < sizeGen) {
						processGencostData(str, gencnt);
						gencnt++;
					} else
						dataType = 0;
				} else if (dataType == BranchNameData && isDataRow(str)) {
					processBranchNameData(str, branchNameCnt);
					branchNameCnt++;
				} else if (dataType == GenNameData && isDataRow(str)) {
					processGenNameData(str, genNameCnt);
					genNameCnt++;
				} else if (dataType == GenTypeData && isDataRow(str)) {
					processGenTypeData(str, genTypeCnt);
					genTypeCnt++;
				} else if (dataType == GenFuelData && isDataRow(str)) {
					processGenFuelData(str, genFuelCnt);
					genFuelCnt++;
				} else if (dataType == DcLineData && isDataRow(str)) {
					processDcLineData(str, parser, dcLineCnt);
					dcLineCnt++;
				}
			} catch (final Exception e) {
				log.error(e.toString() + "\n" + str);
			}
		}

		return parser;
	}

	protected IODMModelParser parseInputFile(IODMAdapter.NetType type,
			final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}

	private Set<String> collectGeneratorBusIds(List<String> lines) {
		Set<String> busIds = new HashSet<String>();
		int dataType = 0;
		for (String line : lines) {
			String blockName = assignmentName(line);
			if (isDataBlockEnd(line)) {
				dataType = 0;
			} else if ("mpc.gen".equals(blockName)) {
				dataType = GenData;
			} else if (blockName.startsWith("mpc.")) {
				dataType = 0;
			} else if (dataType == GenData && isDataRow(line)) {
				String[] fields = getGenDataFields(line);
				if (fields[0] != null) {
					busIds.add(IODMModelParser.BusIdPreFix + fields[0]);
				}
			}
		}
		return busIds;
	}

	private boolean isCommentOrBlank(String str) {
		String line = str.trim();
		return line.length() == 0 || line.startsWith("%");
	}

	private boolean isDataRow(String str) {
		String line = str.trim();
		return line.length() > 0 && !line.startsWith("%")
				&& !line.startsWith("]") && !line.startsWith("};")
				&& !line.startsWith("mpc.");
	}

	private boolean isDataBlockEnd(String str) {
		String line = str.trim();
		return line.startsWith("]") || line.startsWith("};");
	}

	private boolean isAreaBlock(String blockName) {
		return "mpc.area".equals(blockName) || "mpc.areas".equals(blockName);
	}

	private String assignmentName(String str) {
		int idx = str.indexOf('=');
		return idx >= 0 ? str.substring(0, idx).trim() : "";
	}

	private void processTitleData(String str,
			final LoadflowNetXmlType baseCaseNet) {
		str = str.replace(';', ' ');
		final StringTokenizer st = new StringTokenizer(str, " ");
		st.nextToken(); // function
		st.nextToken(); // mpc
		st.nextToken(); // =
		final String id = st.nextToken().toString();
		log.debug("fileName: " + id);
		baseCaseNet.setId("Opf_from_Matpower_" + id);
	}

	private void processBaseMVAData(String str,
			final LoadflowNetXmlType baseCaseNet) {
		str = str.replace(';', ' ');
		final StringTokenizer st = new StringTokenizer(str, " ");
		st.nextToken(); // mpc.baseMVA
		st.nextToken(); // =
		final String basemva = st.nextToken().toString();
		double baseMva = str2d(basemva);
		log.debug("baseMva: " + baseMva);
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
	}

	private void processBusData(final String str, OpfModelParser parser, Set<String> generatorBusIds) {
		// parse the input data line
		final String[] s = getBusDataFields(str);

		final String busId = IODMModelParser.BusIdPreFix + s[0];
		final int type = str2i(s[1]);
		final double pl = str2d(s[2]);
		final double ql = str2d(s[3]);

		final double pshunt = str2d(s[4]);
		final double qshunt = str2d(s[5]);

		final int area = str2i(s[6]);

		final double vm = str2d(s[7]);
		final double va = str2d(s[8]);

		final double baseKv = str2d(s[9]);
		final int zone = str2i(s[10]);

		final double vmax = str2d(s[11]);
		final double vmin = str2d(s[12]);

		LoadflowBusXmlType aclfBus = null;
		// OpfGenBusXmlType opfGenBus = null;
		try {
			if (type == 2 || type == 3 || generatorBusIds.contains(busId)) {
				OpfGenBusXmlType opfGenBus = parser.createOpfGenBus(busId);
				aclfBus = (LoadflowBusXmlType) opfGenBus;
				opfGenBus.setOperatingMode(type == 1
						? OpfGenOperatingModeEnumType.PQ_GENERATOR
						: OpfGenOperatingModeEnumType.PV_GENERATOR);
			} else {
				aclfBus = parser.createBus(busId);
			}
		} catch (Exception e) {
			this.logErr(e.toString());
			return;
		}

		log.debug("Bus data loaded, id: " + busId);

		aclfBus.setNumber(new Long(s[0]));

		final String busName = busId;
		aclfBus.setName(busName);

		aclfBus.setAreaNumber(area);
		aclfBus.setZoneNumber(zone);

		aclfBus.setBaseVoltage(BaseDataSetter.createVoltageValue(baseKv,
				VoltageUnitType.KV));

		aclfBus.setVoltage(BaseDataSetter.createVoltageValue(vm,
				VoltageUnitType.PU));
		aclfBus.setAngle(BaseDataSetter.createAngleValue(va, AngleUnitType.DEG));

		if (pl != 0.0 || ql != 0.0) {
			AclfDataSetter.setLoadData(aclfBus, LFLoadCodeEnumType.CONST_P, pl,
					ql, ApparentPowerUnitType.MVA);
		}

		// 1 - PQ; 2 - pv; 3 - swing; 4 - isolated

		LFGenCodeEnumType genType = type == 3 ? LFGenCodeEnumType.SWING
				: (type == 2 ? LFGenCodeEnumType.PV
						: (type == 1 ? LFGenCodeEnumType.PQ
								: LFGenCodeEnumType.OFF));

		if (type == 2 || type == 3 || generatorBusIds.contains(busId)) {
			aclfBus.getGenData().setCode(genType);
		}

		double baseKva = parser.getBaseOpfNet().getBasePower().getValue() / 1000;
		if (pshunt != 0.0 || qshunt != 0.0) {
			if (aclfBus.getShuntYData() == null) {
				aclfBus.setShuntYData(factory.createBusShuntYDataXmlType());
			}
			aclfBus.getShuntYData().setEquivY(BaseDataSetter.createYValue(pshunt / baseKva,
					qshunt / baseKva, YUnitType.PU));
		}

	}

	private void processGenData(final String str, OpfModelParser parser) {
		// parse the input data line
		final String[] s = getGenDataFields(str);
		final String id = s[0];
		final String busId = IODMModelParser.BusIdPreFix + id;
		OpfGenBusXmlType bus = (OpfGenBusXmlType) parser.getBus(busId);
		if (bus == null) {
			log.error("Cannot find MATPOWER generator bus: " + busId);
			return;
		}
		opfGenContainer.add(bus);
		double pg = str2d(s[1]);
		double qg = str2d(s[2]);

		LoadflowGenDataXmlType gen = AclfParserHelper.createContriGen(bus);
		gen.setId(busId + "_Gen_" + opfGenDataContainer.size());
		gen.setPower(BaseDataSetter.createPowerValue(pg, qg,
				ApparentPowerUnitType.MVA));

		int status = str2i(s[7]);
		if (status > 0) {
			gen.setOffLine(false);
		} else {
			gen.setOffLine(true);
		}

		double qgmax = str2d(s[3]);
		double qgmin = str2d(s[4]);
		gen.setDesiredVoltage(BaseDataSetter.createVoltageValue(str2d(s[5]), VoltageUnitType.PU));
		gen.setMvaBase(BaseDataSetter.createPowerMvaValue(str2d(s[6])));
		gen.setQLimit(BaseDataSetter.createReactivePowerLimit(qgmax, qgmin, ReactivePowerUnitType.MVAR));

		double pgmax = str2d(s[8]);
		double pgmin = str2d(s[9]);

		ActivePowerLimitXmlType plimit = BaseDataSetter.createActivePowerLimit(
				pgmax, pgmin, ActivePowerUnitType.MW);		
		gen.setPLimit(plimit);

		ConstraintsXmlType constraint = new ConstraintsXmlType();
		constraint.setActivePowerLimit(plimit);
		bus.setConstraints(constraint);
		opfGenDataContainer.add(gen);

	}

	private void processBranchData(final String str, /*int cirId,*/ OpfModelParser parser) {
		// parse the input data line
		final String[] s = getBranchDataFields(str);
		final String fid = IODMModelParser.BusIdPreFix + s[0];
		final String tid = IODMModelParser.BusIdPreFix + s[1];

		OpfBranchXmlType opfBra = null;
		try {
			// MapPower does not have branch cirId defined. It will be auto set in the 
			// branch creation process
			opfBra = parser.createOpfBranch(fid, tid);
		} catch (Exception e) {
			this.logErr("branch data error, " + e.toString());
			return;
		}
		opfBranchContainer.add(opfBra);

		double r = str2d(s[2]);
		double z = str2d(s[3]);
		AclfDataSetter.setLineData(opfBra, r, z, ZUnitType.PU, 0.0, 0,
				YUnitType.PU);

		double rating1Mvar = 0.0, rating2Mvar = 0.0, rating3Mvar = 0.0;
		if (!s[5].trim().equals(""))
			rating1Mvar = str2d(s[5]);
		if (!s[6].trim().equals(""))
			rating2Mvar = str2d(s[6]);
		if (!s[7].trim().equals(""))
			rating3Mvar = str2d(s[7]);

		opfBra.setRatingLimit(this.factory.createBranchRatingLimitXmlType());
		ActivePowerRatingXmlType rating = new ActivePowerRatingXmlType();
		rating.setRating1(rating1Mvar);
		rating.setRating2(rating2Mvar);
		rating.setRating3(rating3Mvar);

		rating.setUnit(ActivePowerUnitType.MW);
		opfBra.getRatingLimit().setMw(rating);

	}

	private void processBranchNameData(final String str, int branchNameCnt) {
		if (branchNameCnt >= opfBranchContainer.size()) {
			return;
		}
		List<String> tokens = getStringTokens(str);
		if (tokens.isEmpty()) {
			return;
		}
		opfBranchContainer.get(branchNameCnt).setName(tokens.get(0));
	}

	private void processGenNameData(final String str, int genNameCnt) {
		if (genNameCnt >= opfGenDataContainer.size()) {
			return;
		}
		List<String> tokens = getStringTokens(str);
		if (tokens.isEmpty()) {
			return;
		}
		LoadflowGenDataXmlType gen = opfGenDataContainer.get(genNameCnt);
		gen.setName(tokens.get(0));
		BaseJaxbHelper.addNVPair(gen, "matpower.gen.name", tokens.get(0));
		if (tokens.size() > 1) {
			BaseJaxbHelper.addNVPair(gen, "matpower.gen.type", tokens.get(1));
		}
		if (tokens.size() > 2) {
			BaseJaxbHelper.addNVPair(gen, "matpower.gen.fuel", tokens.get(2));
		}
	}

	private void processGenTypeData(final String str, int genTypeCnt) {
		if (genTypeCnt >= opfGenDataContainer.size()) {
			return;
		}
		List<String> tokens = getStringTokens(str);
		if (!tokens.isEmpty()) {
			BaseJaxbHelper.addNVPair(opfGenDataContainer.get(genTypeCnt), "matpower.gen.type", tokens.get(0));
		}
	}

	private void processGenFuelData(final String str, int genFuelCnt) {
		if (genFuelCnt >= opfGenDataContainer.size()) {
			return;
		}
		List<String> tokens = getStringTokens(str);
		if (!tokens.isEmpty()) {
			BaseJaxbHelper.addNVPair(opfGenDataContainer.get(genFuelCnt), "matpower.gen.fuel", tokens.get(0));
		}
	}

	private void processDcLineData(final String str, OpfModelParser parser, int dcLineCnt)
			throws ODMBranchDuplicationException {
		String[] s = getDcLineDataFields(str);
		if (s[0] == null || s[1] == null || s[2] == null) {
			return;
		}
		int status = str2i(s[2]);
		if (status == 0) {
			return;
		}
		String fromBusId = IODMModelParser.BusIdPreFix + s[0];
		String toBusId = IODMModelParser.BusIdPreFix + s[1];
		LoadflowBusXmlType fromBus = (LoadflowBusXmlType) parser.getBus(fromBusId);
		LoadflowBusXmlType toBus = (LoadflowBusXmlType) parser.getBus(toBusId);
		if (fromBus == null || toBus == null) {
			log.error("Cannot find MATPOWER dc line endpoint: " + fromBusId + " -> " + toBusId);
			return;
		}
		double pf = str2d(s[3]);
		double pt = str2d(s[4]);
		double qf = str2d(s[5]);
		double qt = str2d(s[6]);
		DCLineData2TXmlType dcLine = parser.createDCLine2TRecord(fromBusId, toBusId,
				Integer.toString(dcLineCnt + 1));
		dcLine.setName("MATPOWER DC line " + (dcLineCnt + 1));
		dcLine.setControlMode(DcLineControlModeEnumType.POWER);
		dcLine.setOperationMode(DcLineOperationModeEnumType.SINGLE);
		dcLine.setControlOnRectifierSide(pf >= 0.0);
		dcLine.setPowerDemand(BaseDataSetter.createActivePowerValue(Math.abs(pf), ActivePowerUnitType.MW));
		dcLine.setLineR(BaseDataSetter.createRValue(0.0, ZUnitType.PU));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.index", Integer.toString(dcLineCnt + 1));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.pf", Double.toString(pf));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.pt", Double.toString(pt));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.qf", Double.toString(qf));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.qt", Double.toString(qt));
		BaseJaxbHelper.addNVPair(dcLine, "matpower.dcline.raw", str.trim());
	}

	private void processGencostData(final String str, int gencnt) {
		final String[] s = getGencostDataFields(str);
		int type = str2i(s[0]);
		LoadflowGenDataXmlType gen = opfGenDataContainer.get(gencnt);
		OpfGenBusXmlType bus = opfGenContainer.get(gencnt);
		if (gen == null || bus == null) {
			return;
		}
		IncCostXmlType incCost = new IncCostXmlType();
		int np = str2i(s[3]);
		BaseJaxbHelper.addNVPair(gen, "matpower.gencost.raw", str.trim());
		BaseJaxbHelper.addNVPair(gen, "matpower.gencost.type", s[0]);
		BaseJaxbHelper.addNVPair(gen, "matpower.gencost.startup", s[1]);
		BaseJaxbHelper.addNVPair(gen, "matpower.gencost.shutdown", s[2]);
		BaseJaxbHelper.addNVPair(gen, "matpower.gencost.n", s[3]);
		if (type == 2) {
			if (np > 3) {
				log.error("Polynomial gen cost function with order higher than 2 is not supported!");
				return;
			}
			incCost.setCostModel(CostModelEnumType.QUADRATIC_MODEL);
			double[] point = new double[np];
			int startIdx = 4;
			for (int i = 0; i < np; i++) {
				point[i] = str2d(s[startIdx++]);
				BaseJaxbHelper.addNVPair(gen, "matpower.gencost.c" + (np - i - 1), Double.toString(point[i]));
			}
			OpfDataSetter.setQuadraticModel(incCost, point[0],
					ActivePowerPriceEnumType.DOLLAR_PER_MW_SQUARE, point[1],
					ActivePowerPriceEnumType.DOLLAR_PER_MW, point[2]);

		} else {
			incCost.setCostModel(CostModelEnumType.PIECE_WISE_LINEAR_MODEL);
			double[] point = new double[np * 2];
			int startIdx = 4;
			for (int i = 0; i < 2 * np; i++) {
				point[i] = str2d(s[startIdx++]);
				BaseJaxbHelper.addNVPair(gen, "matpower.gencost.pw" + i, Double.toString(point[i]));
			}
			OpfDataSetter.setPWModel(incCost, point);
		}
		bus.setIncCost(incCost);
	}

	/*
	 * private void processVersionData( String str, final AclfModelParser
	 * parser) { str = str.replace(';', ' '); final StringTokenizer st = new
	 * StringTokenizer(str); st.nextToken(); // mpc.version st.nextToken(); // =
	 * //st.nextToken(); // = final String versionNum =
	 * st.nextToken().toString(); // '2' final StringTokenizer st1 = new
	 * StringTokenizer(versionNum, " "); String version = st1.nextToken();
	 * ODMLogger.getLogger().fine("Matpower version: " + version );
	 * 
	 * }
	 */

	private String[] getBusDataFields(String str) {
		str = str.replace(';', ' ');
		final String[] strAry = new String[17];
		final StringTokenizer st = new StringTokenizer(str);
		int cnt = 0;
		while (st.hasMoreTokens()) {
			strAry[cnt++] = st.nextToken().trim();
		}
		return strAry;
	}

	private String[] getGenDataFields(String str) {
		str = str.replace(';', ' ');
		final String[] strAry = new String[25];
		final StringTokenizer st = new StringTokenizer(str);
		int cnt = 0;
		while (st.hasMoreTokens()) {
			strAry[cnt++] = st.nextToken().trim();
		}
		return strAry;
	}

	private String[] getBranchDataFields(String str) {
		str = str.replace(';', ' ');
		final String[] strAry = new String[21];
		final StringTokenizer st = new StringTokenizer(str);
		int cnt = 0;
		while (st.hasMoreTokens()) {
			strAry[cnt++] = st.nextToken().trim();
		}
		return strAry;
	}

	private String[] getGencostDataFields(String str) {
		str = str.replace(';', ' ');
		final StringTokenizer st = new StringTokenizer(str);
		int size = st.countTokens();
		final String[] strAry = new String[size];
		int cnt = 0;
		while (st.hasMoreTokens()) {
			strAry[cnt++] = st.nextToken().trim();
		}
		return strAry;
	}

	private String[] getDcLineDataFields(String str) {
		str = str.replace(';', ' ');
		final String[] strAry = new String[23];
		final StringTokenizer st = new StringTokenizer(str);
		int cnt = 0;
		while (st.hasMoreTokens() && cnt < strAry.length) {
			strAry[cnt++] = st.nextToken().trim();
		}
		return strAry;
	}

	private List<String> getStringTokens(String str) {
		List<String> tokens = new ArrayList<String>();
		Matcher matcher = QUOTED_TOKEN.matcher(str);
		while (matcher.find()) {
			tokens.add(matcher.group(1));
		}
		if (!tokens.isEmpty()) {
			return tokens;
		}
		str = str.replace(';', ' ').replace('{', ' ').replace('}', ' ');
		StringTokenizer st = new StringTokenizer(str);
		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken().trim());
		}
		return tokens;
	}

	private double str2d(String str) {
		double d = new Double(str).doubleValue();
		return d;
	}

	private int str2i(String str) {
		int d = new Integer(str).intValue();
		return d;
	}
	
}
