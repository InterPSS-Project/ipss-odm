package org.ieee.odm.pwd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.matpower.MatpowerAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NameValuePairXmlType;
import org.ieee.odm.schema.PowerXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.BeforeClass;
import org.junit.Test;

public class ACTIVSg70kCrossValidationTest {
	private static final Path DEFAULT_DIR = Path.of("/Users/ipssdev/Downloads/ACTIVSg70k");
	private static final double MW_TOL = 2.0;
	private static final double MVAR_TOL = 1.0;

	private static Path auxPath;
	private static Path rawPath;
	private static Path matpowerPath;
	private static Summary aux;
	private static Summary raw;
	private static Summary matpower;
	private static AclfModelParser auxParser;

	@BeforeClass
	public static void parseCases() throws Exception {
		auxPath = pathProperty("activsg70k.aux", DEFAULT_DIR.resolve("ACTIVSg70k.aux"));
		rawPath = pathProperty("activsg70k.raw", DEFAULT_DIR.resolve("ACTIVSg70k.RAW"));
		matpowerPath = pathProperty("activsg70k.matpower", DEFAULT_DIR.resolve("case_ACTIVSg70k.m"));
		if (!Files.isRegularFile(auxPath) || !Files.isRegularFile(rawPath) || !Files.isRegularFile(matpowerPath)) {
			return;
		}

		auxParser = parsePowerWorldAux(auxPath);
		aux = summarize(auxParser);
		raw = summarize(parse(new PSSERawAdapter(PsseVersion.PSSE_33), rawPath));
		matpower = summarize(parse(new MatpowerAdapter(), matpowerPath));
	}

	@Test
	public void auxAndMatpowerExpandedModelsMatch() {
		assumeActivs70kFiles();

		assertEquals(70000, aux.busCount);
		assertEquals(88207, aux.branchCount);
		assertEquals(71352, aux.lineCount);
		assertEquals(16855, aux.xfrCount);
		assertEquals(35000, aux.substationCount);

		assertEquals(aux.busCount, matpower.busCount);
		assertEquals(aux.branchCount, matpower.branchCount);
		assertEquals(aux.lineCount, matpower.lineCount);
		assertEquals(aux.xfrCount, matpower.xfrCount);
		assertEquals(aux.baseKvSum, matpower.baseKvSum, 1.0);
		assertEquals(aux.vmSum, matpower.vmSum, 1.0e-3);
		assertEquals(aux.vaSum, matpower.vaSum, 1.0e-3);
		assertEquals(aux.loadP, matpower.loadP, MW_TOL);
		assertEquals(aux.loadQ, matpower.loadQ, MVAR_TOL);
		assertEquals(aux.genP, matpower.genP, MW_TOL);
		assertEquals(aux.genQ, matpower.genQ, MVAR_TOL);
		assertEquals(aux.lineR, matpower.lineR, 1.0e-3);
		assertEquals(aux.lineX, matpower.lineX, 1.0e-3);
		assertEquals(aux.xfrR, matpower.xfrR, 1.0e-2);
		assertEquals(aux.xfrX, matpower.xfrX, 0.2);
	}

	@Test
	public void rawCompactThreeWindingRepresentationMatchesExpectedDelta() {
		assumeActivs70kFiles();

		assertEquals(67900, raw.busCount);
		assertEquals(84007, raw.branchCount);
		assertEquals(aux.busCount - 2100, raw.busCount);
		assertEquals(aux.branchCount - 4200, raw.branchCount);
		assertEquals(aux.lineCount, raw.lineCount);
		assertEquals(aux.xfrCount - 4200, raw.xfrCount);
		assertEquals(aux.lineR, raw.lineR, 1.0e-3);
		assertEquals(aux.lineX, raw.lineX, 1.0e-3);
		assertEquals(aux.loadP, raw.loadP, MW_TOL);
		assertEquals(aux.loadQ, raw.loadQ, MVAR_TOL);
		assertEquals(aux.genP, raw.genP, MW_TOL);
		assertEquals(aux.genQ, raw.genQ, MVAR_TOL);
	}

	@Test
	public void auxSubstationGeoLinksAreImported() {
		assumeActivs70kFiles();

		LoadflowBusXmlType bus1 = (LoadflowBusXmlType) auxParser.getBus("Bus1");
		assertEquals("AUBURN 1 1", bus1.getName());
		assertEquals("1", nv(bus1, "SubNum"));
		assertEquals("AUBURN 1", nv(bus1, "SubStation"));

		LoadflowBusXmlType bus70000 = (LoadflowBusXmlType) auxParser.getBus("Bus70000");
		assertEquals("COLSTRIP 4 10", bus70000.getName());
		assertEquals("35000", nv(bus70000, "SubNum"));
		assertEquals("COLSTRIP 4", nv(bus70000, "SubStation"));
	}

	private static Path pathProperty(String name, Path defaultPath) {
		String value = System.getProperty(name);
		return value == null || value.isBlank() ? defaultPath : Path.of(value);
	}

	private static void assumeActivs70kFiles() {
		assumeTrue("ACTIVSg70k AUX file not found: " + auxPath, Files.isRegularFile(auxPath));
		assumeTrue("ACTIVSg70k RAW file not found: " + rawPath, Files.isRegularFile(rawPath));
		assumeTrue("ACTIVSg70k MATPOWER file not found: " + matpowerPath, Files.isRegularFile(matpowerPath));
	}

	private static AclfModelParser parsePowerWorldAux(Path path) throws Exception {
		PowerWorldAdapter adapter = new PowerWorldAdapter();
		try (InputStream input = Files.newInputStream(path)) {
			assertTrue("PowerWorld AUX parse failed: " + adapter.errMessage(),
					adapter.parseInputStream(input, "ISO-8859-1"));
		}
		return (AclfModelParser) adapter.getModel();
	}

	private static AclfModelParser parse(IODMAdapter adapter, Path path) {
		assertTrue("Parse failed for " + path + ": " + adapter.errMessage(),
				adapter.parseInputFile(path.toString()));
		return (AclfModelParser) adapter.getModel();
	}

	private static Summary summarize(AclfModelParser parser) {
		Summary summary = new Summary();
		summary.busCount = parser.getNet().getBusList().getBus().size();
		summary.branchCount = parser.getNet().getBranchList().getBranch().size();
		summary.substationCount = parser.getNet().getSubstationList() == null ? 0
				: parser.getNet().getSubstationList().getSubstation().size();
		for (JAXBElement<? extends BusXmlType> busElem : parser.getNet().getBusList().getBus()) {
			LoadflowBusXmlType bus = (LoadflowBusXmlType) busElem.getValue();
			if (bus.getBaseVoltage() != null) {
				summary.baseKvSum += bus.getBaseVoltage().getValue();
			}
			if (bus.getVoltage() != null) {
				summary.vmSum += bus.getVoltage().getValue();
			}
			if (bus.getAngle() != null) {
				summary.vaSum += bus.getAngle().getValue();
			}
			if (bus.getLoadData() != null) {
				for (JAXBElement<? extends LoadflowLoadDataXmlType> loadElem : bus.getLoadData().getContributeLoad()) {
					addPower(summary, loadElem.getValue().getConstPLoad(), true);
					addPower(summary, loadElem.getValue().getConstILoad(), true);
					addPower(summary, loadElem.getValue().getConstZLoad(), true);
				}
			}
			if (bus.getGenData() != null) {
				for (JAXBElement<? extends LoadflowGenDataXmlType> genElem : bus.getGenData().getContributeGen()) {
					addPower(summary, genElem.getValue().getPower(), false);
				}
			}
		}
		for (JAXBElement<? extends BaseBranchXmlType> branchElem : parser.getNet().getBranchList().getBranch()) {
			BaseBranchXmlType branch = branchElem.getValue();
			if (branch instanceof LineBranchXmlType line) {
				summary.lineCount++;
				if (line.getZ() != null) {
					summary.lineR += line.getZ().getRe();
					summary.lineX += line.getZ().getIm();
				}
			}
			else if (branch instanceof XfrBranchXmlType xfr) {
				summary.xfrCount++;
				if (xfr.getZ() != null) {
					summary.xfrR += xfr.getZ().getRe();
					summary.xfrX += xfr.getZ().getIm();
				}
			}
		}
		return summary;
	}

	private static void addPower(Summary summary, PowerXmlType power, boolean load) {
		if (power == null) {
			return;
		}
		if (load) {
			summary.loadP += power.getRe();
			summary.loadQ += power.getIm();
		}
		else {
			summary.genP += power.getRe();
			summary.genQ += power.getIm();
		}
	}

	private static String nv(LoadflowBusXmlType bus, String name) {
		for (NameValuePairXmlType pair : bus.getNvPair()) {
			if (name.equals(pair.getName())) {
				return pair.getValue();
			}
		}
		return "";
	}

	private static final class Summary {
		private int busCount;
		private int branchCount;
		private int lineCount;
		private int xfrCount;
		private int substationCount;
		private double baseKvSum;
		private double vmSum;
		private double vaSum;
		private double loadP;
		private double loadQ;
		private double genP;
		private double genQ;
		private double lineR;
		private double lineX;
		private double xfrR;
		private double xfrX;
	}
}
