package org.ieee.odm.psse.raw.v35;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.XfrBranchXmlType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV35_IEEE9_Test {
	/**
	 * Test case for IEEE 9 bus system
	 * This is mainly for testing the parsing of transformer data with non-traditional winding settings, such as CW = 3 and Winding base voltage != bus base voltage
	 * @throws Exception
	 */
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_35);
		assertTrue(adapter.parseInputFile("testdata/psse/v35/ieee9_qa_v35.raw"));
	
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();	
		
		/**  
		 * // R, X data is based on CZ = 3, Ploss and Zpu are provided in the raw file
		 * 
		 * <aclfXfr circuitId="1" id="Bus4_to_Bus1_cirId_1" areaNumber="1" zoneNumber="1" offLine="false" name="">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
				<fromBus idRef="Bus4"/>
		        <toBus idRef="Bus1"/>
                <z unit="PU" re="2.550760126517702E-4" im="0.015149367894878087"/>
                <ratingLimit>
                    <mva unit="MVA" rating1="990.0" rating2="990.0" rating3="990.0" rating4="0.0" rating5="0.0" rating6="0.0" rating7="0.0" rating8="0.0" rating9="0.0" rating10="0.0" rating11="0.0" rating12="0.0"/>
                </ratingLimit>
                <meterLocation>ToSide</meterLocation>
                <bypass>false</bypass>
                <fromTurnRatio unit="PU" value="1.0233"/>
                <toTurnRatio unit="PU" value="1.0"/>
                <magnitizingY unit="PU" re="0.0027999999999999995" im="0.00396"/>
                <xfrInfo>
                    <dataOnSystemBase>true</dataOnSystemBase>
                    <ratedPower unit="MVA" value="990.0"/>
                    <zCorrectionOnWinding>true</zCorrectionOnWinding>
                </xfrInfo>
            </aclfXfr>

		 */

		 XfrBranchXmlType xfr_4_1 = parser.getXfrBranch("Bus4", "Bus1", "1");
		 assertTrue(xfr_4_1 != null);
		 assertTrue(xfr_4_1.getZ().getRe() == 2.550760126517702E-4);
		 assertTrue(xfr_4_1.getZ().getIm() == 0.015149367894878087);
		 assertTrue(xfr_4_1.getFromTurnRatio().getValue() == 1.0233);
		 assertTrue(xfr_4_1.getToTurnRatio().getValue() == 1.0);
		 assertTrue(xfr_4_1.getXfrInfo().isDataOnSystemBase());
		 assertTrue(xfr_4_1.getXfrInfo().getRatedPower().getValue() == 990.0);

		 /*
		  * <aclfXfr circuitId="1" id="Bus7_to_Bus2_cirId_1" areaNumber="1" zoneNumber="1" offLine="false" name="">
                <ownerList id="101">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <ownerList id="0">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
				<fromBus idRef="Bus7"/>
                <toBus idRef="Bus2"/>
                <z unit="PU" re="3.356E-4" im="4.9135E-4"/>
                <ratingLimit>
                    <mva unit="MVA" rating1="37.0" rating2="41.0" rating3="41.0" rating4="0.0" rating5="43.0" rating6="46.0" rating7="46.0" rating8="0.0" rating9="0.0" rating10="0.0" rating11="0.0" rating12="0.0"/>
                </ratingLimit>
		        <meterLocation>ToSide</meterLocation>
                <bypass>false</bypass>
                <fromTurnRatio unit="PU" value="1.017391304347826"/>
                <toTurnRatio unit="PU" value="1.0"/>
                <xfrInfo>
                    <dataOnSystemBase>true</dataOnSystemBase>
                    <ratedPower unit="MVA" value="100.0"/>
                    <zCorrectionOnWinding>true</zCorrectionOnWinding>
                </xfrInfo>
            </aclfXfr>

		  */

		  XfrBranchXmlType xfr_7_2 = parser.getXfrBranch("Bus7", "Bus2", "1");
		  assertTrue(xfr_7_2 != null);
		  assertTrue(xfr_7_2.getZ().getRe() == 3.356E-4);
		  assertTrue(xfr_7_2.getZ().getIm() == 4.9135E-4);
		  assertTrue(xfr_7_2.getFromTurnRatio().getValue() == 1.017391304347826);
		  assertTrue(xfr_7_2.getToTurnRatio().getValue() == 1.0);
		  assertTrue(xfr_7_2.getXfrInfo().getRatedPower().getValue() == 100.0); 
		  assertTrue(xfr_7_2.getXfrInfo().isDataOnSystemBase());
	}

}
