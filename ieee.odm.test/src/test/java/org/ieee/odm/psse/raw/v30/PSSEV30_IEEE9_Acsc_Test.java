package org.ieee.odm.psse.raw.v30;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.schema.GroundingEnumType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.XformrtConnectionEnumType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV30_IEEE9_Acsc_Test {
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile(NetType.AcscNet, new String[]{
				"testdata/psse/v30/IEEE9Bus/ieee9.raw",
				"testdata/psse/v30/IEEE9Bus/ieee9.seq"
		}));
		AcscModelParser acscParser =(AcscModelParser) adapter.getModel();
		//acscParser.stdout();
		
		/*
		 *  <acscBus scCode="Contributing" id="Bus1" areaNumber="1" zoneNumber="1" number="1" offLine="false" name="BUS-1       ">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <baseVoltage unit="KV" value="16.5"/>
                <voltage unit="PU" value="1.04"/>
                <angle unit="DEG" value="0.0"/>
                <genData>
                    <acscEquivGen code="Swing">
                        <power unit="MVA" re="0.0" im="0.0"/>
                        <desiredVoltage unit="PU" value="1.04"/>
                        <desiredAngle unit="DEG" value="0.0"/>
                        <potiveZ unit="PU" re="0.0" im="0.04"/>
                        <negativeZ unit="PU" re="0.0" im="0.04"/>
                        <zeroZ unit="PU" re="0.0" im="0.04"/>
                    </acscEquivGen>
                    <acscContributeGen id="1" offLine="false" name="Gen:1(1)">
                        <desc>PSSE Generator 1 at Bus 1</desc>
                        <power unit="MVA" re="71.64" im="27.05"/>
                        <desiredVoltage unit="PU" value="1.04"/>
                        <qLimit unit="MVAR" max="9999.0" min="-9999.0"/>
                        <pLimit unit="MW" max="9999.0" min="-9999.0"/>
                        <mvaBase unit="MVA" value="100.0"/>
                        <sourceZ unit="PU" re="0.0" im="0.04"/>
                        <mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
                        <potiveZ unit="PU" re="0.0" im="0.04"/>
                        <negativeZ unit="PU" re="0.0" im="0.04"/>
                        <zeroZ unit="PU" re="0.0" im="0.04"/>
                    </acscContributeGen>
                </genData>
                <loadData>
                    <acscEquivLoad/>
                </loadData>
                <shuntYData/>
            </acscBus>
            
		 */
		
		/*
		 * -------------------------------
		 *    Zero sequence generator data
		 * -------------------------------
		 */
		ShortCircuitBusXmlType scBus1 = acscParser.getBus("Bus1");
		ShortCircuitGenDataXmlType scGen1= (ShortCircuitGenDataXmlType) scBus1.getGenData().getContributeGen().get(0).getValue();
		assertTrue(scGen1.getPotiveZ().getRe()==0.0);
		assertTrue(scGen1.getPotiveZ().getIm()==0.04);
		
		assertTrue(scGen1.getNegativeZ().getRe()==0.0);
		assertTrue(scGen1.getNegativeZ().getIm()==0.04);
		
		assertTrue(scGen1.getZeroZ().getRe()==0.0);
		assertTrue(scGen1.getZeroZ().getIm()==0.04);
		
		/*
		 * -------------------------------
		 *    Zero sequence line data
		 * -------------------------------
		 */
		
		/*
		 *     <acscLine circuitId="0" id="Bus7_to_Bus8_cirId_0" areaNumber="1" zoneNumber="1" offLine="false">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <fromBus idRef="Bus7"/>
                <toBus idRef="Bus8"/>
                <z unit="PU" re="0.0085" im="0.072"/>
                <ratingLimit/>
                <meterLocation>FromSide</meterLocation>
                <totalShuntY unit="PU" re="0.0" im="0.149"/>
                <lineInfo/>
                <z0 unit="PU" re="0.02125" im="0.18"/>
                <y0Shunt unit="PU" re="0.0" im="0.149"/>
            </acscLine>
		 */
		LineShortCircuitXmlType scLine78 = acscParser.getAcscLine("Bus7", "Bus8", "0");
		assertTrue(scLine78.getZ0().getRe()==0.02125);
		assertTrue(scLine78.getZ0().getIm()==0.18);
		assertTrue(scLine78.getY0Shunt().getIm()==0.149);
		
		/*
		 * -------------------------------
		 *    Zero sequence tranformer data
		 * -------------------------------
		
           1,  4,  0,  '1',  3,   0,  0, 0.0000,  0.0567,  0, 0
           -------------------------------------------------------
           
           1) z0 = 0+j*0.0567
           2) connection code = 3, no series path, ground path on winding 2 side
           
           which means
           <fromSideConnection xfrConnection="Delta">
                    <grounding groundingConnection="Ungrounded"/>
                </fromSideConnection>
                <toSideConnection xfrConnection="Wye">
                    <grounding groundingConnection="SolidGrounded"/>
            </toSideConnection>
		 */
		
		/*
		 * <acscXfr circuitId="1" id="Bus1_to_Bus4_cirId_1" areaNumber="1" zoneNumber="1" offLine="false" name="Gen1_to_Bus1_cirId_1">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <fromBus idRef="Bus1"/>
                <toBus idRef="Bus4"/>
                <z unit="PU" re="0.0" im="0.0567"/>
                <ratingLimit/>
                <meterLocation>ToSide</meterLocation>
                <fromTurnRatio unit="PU" value="1.0"/>
                <toTurnRatio unit="PU" value="1.0"/>
                <xfrInfo>
                    <dataOnSystemBase>true</dataOnSystemBase>
                    <ratedPower unit="MVA" value="100.0"/>
                </xfrInfo>
                <z0 unit="PU" re="0.0" im="0.0567"/>
                <fromSideConnection xfrConnection="Delta">
                    <grounding groundingConnection="Ungrounded"/>
                </fromSideConnection>
                <toSideConnection xfrConnection="Wye">
                    <grounding groundingConnection="SolidGrounded"/>
                </toSideConnection>
            </acscXfr>
		 */
		
		XfrShortCircuitXmlType scXfr_14= acscParser.getAcscXfr("Bus1", "Bus4", "1");
		assertTrue(scXfr_14.getZ0().getRe()==0.0);
		assertTrue(scXfr_14.getZ0().getIm()==0.0567);
		assertTrue(scXfr_14.getFromSideConnection().getXfrConnection()== XformrtConnectionEnumType.DELTA);
		assertTrue(scXfr_14.getFromSideConnection().getGrounding().getGroundingConnection()==GroundingEnumType.UNGROUNDED);
		assertTrue(scXfr_14.getToSideConnection().getXfrConnection()== XformrtConnectionEnumType.WYE);
		assertTrue(scXfr_14.getToSideConnection().getGrounding().getGroundingConnection()==GroundingEnumType.SOLID_GROUNDED);
	}

}

