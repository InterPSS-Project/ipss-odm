package org.ieee.odm.psse.raw.v36;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NameTagXmlType;
import org.ieee.odm.schema.NameValuePairXmlType;
import org.ieee.odm.schema.NetAreaXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.SwitchedShuntXmlType;
import org.junit.Test;

public class PSSEV36_RawLabel_Test {

    @Test
    public void preservesRawLabelCommentsInIeee9Case() throws Exception {
        AclfModelParser parser = parse("testdata/psse/ieee9_v36_labeled.raw");

        assertEquals(9, parser.getNet().getBusList().getBus().size());
        assertEquals(9, parser.getNet().getBranchList().getBranch().size());
        assertLabelCounts(parser, 9, 3, 3, 9, 0, 0, 0);

        LoadflowBusXmlType bus1 = parser.getBus("Bus1");
        assertLabels(bus1, "[ bus_1_16.5kv]", "bus_1_16.5kv");

        LoadflowBusXmlType bus5 = parser.getBus("Bus5");
        LoadflowLoadDataXmlType load5 = bus5.getLoadData().getContributeLoad().get(0).getValue();
        assertLabels(load5, "[ load_5_1_230kv, load_BUS-5_1_230kv ]", "load_5_1_230kv", "load_BUS-5_1_230kv");

        LoadflowGenDataXmlType gen1 = bus1.getGenData().getContributeGen().get(0).getValue();
        assertLabels(gen1, "[ generator_1_1_16.5kv, gen_BUS-1_1_16.5kv ]", "generator_1_1_16.5kv", "gen_BUS-1_1_16.5kv");

        BaseBranchXmlType line45 = parser.getBranch("Bus4", "Bus5", "0");
        assertLabels(line45, "[ line_4_5_0_230kv, branch_BUS-4_BUS-5_0_230kv ]", "line_4_5_0_230kv", "branch_BUS-4_BUS-5_0_230kv");

        BaseBranchXmlType xfr14 = parser.getBranch("Bus1", "Bus4", "1");
        assertLabels(xfr14, "[ transformer_1_4_1_16.5kv, xfmr_BUS-1_BUS-4_1_16.5kv ]", "transformer_1_4_1_16.5kv", "xfmr_BUS-1_BUS-4_1_16.5kv");
    }

    @Test
    public void preservesRawLabelCommentsInTexas2kCase() throws Exception {
        AclfModelParser parser = parse("testdata/psse/Texas2k_series24_case1_2016summerPeak_v36_labeled.RAW");

        assertEquals(2000, parser.getNet().getBusList().getBus().size());
        assertEquals(3220, parser.getNet().getBranchList().getBranch().size());
        assertLabelCounts(parser, 2000, 1350, 544, 3220, 153, 8, 28);

        LoadflowBusXmlType bus1001 = parser.getBus("Bus1001");
        assertLabels(bus1001, "[ bus_1001_115kv]", "bus_1001_115kv");

        LoadflowLoadDataXmlType load1001 = bus1001.getLoadData().getContributeLoad().get(0).getValue();
        assertLabels(load1001, "[ load_1001_1_115kv, load_ODESSA_2_0_1001_1_115kv ]", "load_1001_1_115kv", "load_ODESSA_2_0_1001_1_115kv");

        LoadflowBusXmlType bus1004 = parser.getBus("Bus1004");
        LoadflowGenDataXmlType gen1004 = bus1004.getGenData().getContributeGen().get(0).getValue();
        assertLabels(gen1004, "[ generator_1004_1_230kv, gen_O_DONNELL__2_1004_1_230kv ]", "generator_1004_1_230kv", "gen_O_DONNELL__2_1004_1_230kv");

        BaseBranchXmlType line1001To1064 = parser.getBranch("Bus1001", "Bus1064", "1");
        assertLabels(line1001To1064, "[ line_1001_1064_1_115kv, branch_ODESSA_2_0_ODESSA_3_0_1001_1064_1_115kv ]", "line_1001_1064_1_115kv", "branch_ODESSA_2_0_ODESSA_3_0_1001_1064_1_115kv");

        BaseBranchXmlType xfr1004To1003 = parser.getBranch("Bus1004", "Bus1003", "1");
        assertLabels(xfr1004To1003, "[ transformer_1004_1003_1_230kv, xfmr_O_DONNELL__2_O_DONNELL__1_1004_1003_1_230kv ]", "transformer_1004_1003_1_230kv", "xfmr_O_DONNELL__2_O_DONNELL__1_1004_1003_1_230kv");

        LoadflowBusXmlType bus1007 = (LoadflowBusXmlType) parser.getBus("Bus1007");
        SwitchedShuntXmlType switchedShunt1007 = bus1007.getSwitchedShuntData().getContributeSwitchedShunt().get(0);
        assertLabels(switchedShunt1007, "[ switched_shunt_1007_1_115kv, swsh_VAN_HORN_0_1007_1_115kv ]", "switched_shunt_1007_1_115kv", "swsh_VAN_HORN_0_1007_1_115kv");

        NetAreaXmlType area1 = parser.getAclfNet().getAreaList().getArea().get(0);
        assertLabels(area1, "[ area_1, area_FAR_WEST_1 ]", "area_1", "area_FAR_WEST_1");

        NetZoneXmlType zone1 = parser.getAclfNet().getLossZoneList().getLossZone().get(0);
        assertLabels(zone1, "[ zone_1, zone_BAY_CITY_1 ]", "zone_1", "zone_BAY_CITY_1");
    }

    private AclfModelParser parse(String file) throws Exception {
        IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_36);
        assertTrue(adapter.parseInputFile(file));
        return (AclfModelParser) adapter.getModel();
    }

    private void assertLabelCounts(AclfModelParser parser, int buses, int loads, int generators, int branches,
            int switchedShunts, int areas, int zones) {
        int labeledBuses = 0;
        int labeledLoads = 0;
        int labeledGenerators = 0;
        int labeledSwitchedShunts = 0;

        for (JAXBElement<? extends BusXmlType> busElement : parser.getNet().getBusList().getBus()) {
            LoadflowBusXmlType bus = (LoadflowBusXmlType) busElement.getValue();
            if (hasExternalUid(bus)) {
                labeledBuses++;
                assertNull(nvPairValue(bus, "externalUID:2"));
            }
            if (bus.getLoadData() != null) {
                for (JAXBElement<? extends LoadflowLoadDataXmlType> loadElement : bus.getLoadData().getContributeLoad()) {
                    if (hasExternalUid(loadElement.getValue())) {
                        labeledLoads++;
                        assertNotNull(nvPairValue(loadElement.getValue(), "externalUID:2"));
                    }
                }
            }
            if (bus.getGenData() != null) {
                for (JAXBElement<? extends LoadflowGenDataXmlType> genElement : bus.getGenData().getContributeGen()) {
                    if (hasExternalUid(genElement.getValue())) {
                        labeledGenerators++;
                        assertNotNull(nvPairValue(genElement.getValue(), "externalUID:2"));
                    }
                }
            }
            if (bus.getSwitchedShuntData() != null) {
                for (SwitchedShuntXmlType shunt : bus.getSwitchedShuntData().getContributeSwitchedShunt()) {
                    if (hasExternalUid(shunt)) {
                        labeledSwitchedShunts++;
                        assertNotNull(nvPairValue(shunt, "externalUID:2"));
                    }
                }
            }
        }

        int labeledBranches = 0;
        for (JAXBElement<? extends BaseBranchXmlType> branchElement : parser.getNet().getBranchList().getBranch()) {
            BaseBranchXmlType branch = branchElement.getValue();
            if (hasExternalUid(branch)) {
                labeledBranches++;
                assertNotNull(nvPairValue(branch, "externalUID:2"));
            }
        }

        int labeledAreas = 0;
        if (parser.getAclfNet().getAreaList() != null) {
            for (NetAreaXmlType area : parser.getAclfNet().getAreaList().getArea()) {
                if (hasExternalUid(area)) {
                    labeledAreas++;
                    assertNotNull(nvPairValue(area, "externalUID:2"));
                }
            }
        }

        int labeledZones = 0;
        if (parser.getAclfNet().getLossZoneList() != null) {
            for (NetZoneXmlType zone : parser.getAclfNet().getLossZoneList().getLossZone()) {
                if (hasExternalUid(zone)) {
                    labeledZones++;
                    assertNotNull(nvPairValue(zone, "externalUID:2"));
                }
            }
        }

        assertEquals(buses, labeledBuses);
        assertEquals(loads, labeledLoads);
        assertEquals(generators, labeledGenerators);
        assertEquals(branches, labeledBranches);
        assertEquals(switchedShunts, labeledSwitchedShunts);
        assertEquals(areas, labeledAreas);
        assertEquals(zones, labeledZones);
    }

    private void assertLabels(NameTagXmlType record, String desc, String primaryLabel, String... secondaryLabels) {
        assertEquals(desc, record.getDesc());
        assertEquals(primaryLabel, nvPairValue(record, "externalUID"));
        for (int i = 0; i < secondaryLabels.length; i++) {
            assertEquals(secondaryLabels[i], nvPairValue(record, "externalUID:" + (i + 2)));
        }
        assertNull(nvPairValue(record, "externalUID:" + (secondaryLabels.length + 2)));
    }

    private boolean hasExternalUid(NameTagXmlType record) {
        return nvPairValue(record, "externalUID") != null;
    }

    private String nvPairValue(NameTagXmlType record, String name) {
        for (NameValuePairXmlType nvPair : record.getNvPair()) {
            if (name.equals(nvPair.getName())) {
                return nvPair.getValue();
            }
        }
        return null;
    }
}
