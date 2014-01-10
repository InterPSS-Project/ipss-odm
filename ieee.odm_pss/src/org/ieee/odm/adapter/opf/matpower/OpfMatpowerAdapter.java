package org.ieee.odm.adapter.opf.matpower;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.base.BaseDataSetter;
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
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class OpfMatpowerAdapter extends AbstractODMAdapter {

	private static final int BusData = 1;
	private static final int BranchData = 2;
	private static final int GencostData = 3;
	private static final int AreaData = 4;
	private static final int GenData = 5;

	private ArrayList<OpfGenBusXmlType> opfGenContainer = null;

	private ObjectFactory factory = null;

	public OpfMatpowerAdapter() {
		super();
		this.factory = new ObjectFactory();
		this.opfGenContainer = new ArrayList<OpfGenBusXmlType>();
	}

	@Override
	protected OpfModelParser parseInputFile(final IFileReader din,
			String encoding) throws ODMException {
		OpfModelParser parser = new OpfModelParser(encoding);
		parser.getStudyCase().setId("ODM_OPF_Studycase");
		parser.setOPFTransInfo(OriginalDataFormatEnumType.OPF_MATPOWER);
		OpfNetworkXmlType baseCaseNet = parser.getOpfNetwork();

		String str = din.readLine();
		processTitleData(str, baseCaseNet);

		int dataType = 0;
		int gencnt = 0;
		// Matpower does not specify cirId for parallel lines
		// Here cirId is used for this purpose
		//int cirId = 1;
		do {
			str = din.readLine(); // kvaBase
			if (str != null) {
				try {
					// process the data
					if (str.startsWith("]")) {
						dataType = 0;
					} else if (str.startsWith("%")) {
						// comment line, do nothing but go to the next line;
					} else if (str.startsWith("mpc.baseMVA")) {
						processBaseMVAData(str, baseCaseNet);
					} else if (str.startsWith("mpc.version")) {
						// processBusData(str, parser);
					} else if (dataType == BusData) {
						processBusData(str, parser);
					} else if (dataType == GenData) {
						processGenData(str, parser);
					} else if (dataType == BranchData) {
						processBranchData(str, /*cirId++,*/ parser);
					} else if (dataType == AreaData) {
						// TODO: Not implemented yet.
						// processAreaData(str, parser);
					} else if (dataType == GencostData) {
						int sizeGen = opfGenContainer.size();
						if (gencnt < sizeGen) {
							processGencostData(str, gencnt);
							gencnt++;
						} else
							dataType = 0;
					} else if (str.startsWith("mpc.bus")) {
						dataType = BusData;
						ODMLogger.getLogger().fine("load bus data");
					} else if (str.trim().split("=")[0].trim().equals(
							("mpc.gen"))) {
						dataType = GenData;
						ODMLogger.getLogger().fine("load branch data");
					} else if (str.startsWith("mpc.branch")) {
						dataType = BranchData;
						// baseCaseNet.addNewLossZoneList();
						ODMLogger.getLogger().fine("load loss zone data");
					} else if (str.startsWith("mpc.area")) {
						dataType = AreaData;
						// baseCaseNet.addNewInterchangeList();
						ODMLogger.getLogger().fine("load interchange data");

					} else if (str.trim().split("=")[0].trim().equals(
							("mpc.gencost"))) {
						dataType = GencostData;
						// baseCaseNet.addNewTieLineList();
						ODMLogger.getLogger().fine("load tieline data");
					}
				} catch (final Exception e) {
					ODMLogger.getLogger().severe(e.toString() + "\n" + str);
					e.printStackTrace();
				}
			}
		} while (str != null);

		return parser;
	}

	protected IODMModelParser parseInputFile(IODMAdapter.NetType type,
			final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}

	private void processTitleData(String str,
			final LoadflowNetXmlType baseCaseNet) {
		str = str.replace(';', ' ');
		final StringTokenizer st = new StringTokenizer(str, " ");
		st.nextToken(); // function
		st.nextToken(); // mpc
		st.nextToken(); // =
		final String id = st.nextToken().toString();
		ODMLogger.getLogger().fine("fileName: " + id);
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
		ODMLogger.getLogger().fine("baseMva: " + baseMva);
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(baseMva));
	}

	private void processBusData(final String str, OpfModelParser parser) {
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
			if (type == 2 || type == 3) {
				OpfGenBusXmlType opfGenBus = parser.createOpfGenBus(busId);
				aclfBus = (LoadflowBusXmlType) opfGenBus;
				opfGenBus
						.setOperatingMode(OpfGenOperatingModeEnumType.PV_GENERATOR);
			} else {
				aclfBus = parser.createBus(busId);
			}
		} catch (Exception e) {
			this.logErr(e.toString());
			return;
		}

		ODMLogger.getLogger().fine("Bus data loaded, id: " + busId);

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

		if (type == 2 || type == 3) {
			AclfDataSetter.setGenData(aclfBus, genType, vm, VoltageUnitType.PU,
					va, AngleUnitType.DEG, 0, 0, ApparentPowerUnitType.MVA);
		}

		double baseKva = parser.getBaseOpfNet().getBasePower().getValue() / 1000;
		if (pshunt != 0.0 || qshunt != 0.0) {
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
		opfGenContainer.add(bus);
		double pg = str2d(s[1]);
		double qg = str2d(s[2]);

		LoadflowGenDataXmlType gen = bus.getGenData().getEquivGen().getValue();
		gen.setPower(BaseDataSetter.createPowerValue(pg, qg,
				ApparentPowerUnitType.MVA));

		int status = str2i(s[7]);
		if (status > 0) {
			gen.setOffLine(false);
		} else {
			gen.setOffLine(true);
		}

		double pgmax = str2d(s[8]);
		double pgmin = str2d(s[9]);

		ActivePowerLimitXmlType plimit = BaseDataSetter.createActivePowerLimit(
				pgmax, pgmin, ActivePowerUnitType.MW);		

		ConstraintsXmlType constraint = new ConstraintsXmlType();
		constraint.setActivePowerLimit(plimit);
		bus.setConstraints(constraint);

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
		}

		double r = str2d(s[2]);
		double z = str2d(s[3]);
		AclfDataSetter.setLineData(opfBra, r, z, ZUnitType.PU, 0.0, 0,
				YUnitType.PU);

		double rating1Mvar = 0.0, rating2Mvar = 0.0, rating3Mvar = 0.0;
		if (!s[5].trim().equals(""))
			rating1Mvar = new Integer(s[5]).intValue();
		if (!s[6].trim().equals(""))
			rating2Mvar = new Integer(s[6]).intValue();
		if (!s[7].trim().equals(""))
			rating3Mvar = new Integer(s[7]).intValue();

		opfBra.setRatingLimit(this.factory.createBranchRatingLimitXmlType());
		ActivePowerRatingXmlType rating = new ActivePowerRatingXmlType();
		rating.setRating1(rating1Mvar);
		rating.setRating2(rating2Mvar);
		rating.setRating3(rating3Mvar);

		rating.setUnit(ActivePowerUnitType.MW);
		opfBra.getRatingLimit().setMw(rating);

	}

	private void processGencostData(final String str, int gencnt) {
		final String[] s = getGencostDataFields(str);
		int type = str2i(s[0]);
		OpfGenBusXmlType bus = opfGenContainer.get(gencnt);
		IncCostXmlType incCost = new IncCostXmlType();
		int np = str2i(s[3]);
		if (type == 2) {
			if (np > 3) {
				ODMLogger
						.getLogger()
						.severe("Polynomial gen cost function with order higher than 2 is not supported!");
				return;
			}
			incCost.setCostModel(CostModelEnumType.QUADRATIC_MODEL);
			double[] point = new double[np];
			int startIdx = 4;
			for (int i = 0; i < np; i++) {
				point[i] = str2d(s[startIdx++]);
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

	private double str2d(String str) {
		double d = new Double(str).doubleValue();
		return d;
	}

	private int str2i(String str) {
		int d = new Integer(str).intValue();
		return d;
	}
	
}
