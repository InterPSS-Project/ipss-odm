 /*
  * @(#)IEEECDF_ODMTest.java   
  *
  * Copyright (C) 2008 www.interpss.org
  *
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
  * as published by the Free Software Foundation; either version 2.1
  * of the License, or (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * @Author Stephen Hou
  * @Version 1.0
  * @Date 02/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.psse.raw.v30;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class PSSEV30_ODMTest { 
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile("testdata/psse/v30/PSSE_5Bus_Test.raw"));
		
//		System.out.println(adapter.getModel().toString());
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();		
	
		LoadflowNetXmlType net = parser.getNet();
		assertTrue(net.getBasePower().getValue() == 100.0);

/*
            <aclfBus id="Bus1" areaNumber="1" zoneNumber="1" number="1" offLine="false" name="UNO-U1      ">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <baseVoltage unit="KV" value="13.8"/>
                <voltage unit="PU" value="1.0"/>
                <angle unit="DEG" value="0.0"/>
                <genData code="Swing">
                    <contributeGen id="1" offLine="false" name="Gen:1(1)">
                        <desc>PSSE Generator 1 at Bus 1</desc>
                        <ownerList id="1">
                            <ownership unit="PU" value="1.0"/>
                        </ownerList>
                        <power unit="MVA" re="22.546" im="15.854"/>
                        <desiredVoltage unit="PU" value="1.0"/>
                        <desiredAngle unit="RAD" value="0.0"/>
                        <qLimit unit="MVAR" max="35.0" min="-35.0"/>
                        <pLimit unit="MW" max="45.0" min="15.0"/>
                        <mvaBase unit="MVA" value="68.24"/>
                        <sourceZ unit="PU" re="0.0" im="0.229"/>
                        <mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
                    </contributeGen>
                </genData>
                <loadData/>
                <shuntYData>
                    <equivY im="0.0"/>
                </shuntYData>
            </aclfBus>
 */
		LoadflowBusXmlType bus = parser.getBus("Bus1");
		assertTrue(bus.getBaseVoltage().getValue() == 13.8);
		LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
		assertTrue(bus.getGenData().getCode() == LFGenCodeEnumType.SWING);
		assertTrue(defaultGen.getDesiredVoltage().getValue() == 1.0);
		LoadflowGenDataXmlType Gen1= bus.getGenData().getContributeGen().get(0).getValue();
		assertTrue(Gen1.getPLimit().getMax() == 45.0);
		assertTrue(Gen1.getPLimit().getMin() == 15.0);
		assertTrue(Gen1.getQLimit().getMax() == 35.0);
		assertTrue(Gen1.getQLimit().getMin() == -35.0);
		
/*		
            <aclfBus id="Bus2" areaNumber="1" zoneNumber="1" number="2" offLine="false" name="UNO-230     ">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <baseVoltage unit="KV" value="230.0"/>
                <voltage unit="PU" value="0.97352"/>
                <angle unit="DEG" value="-2.2818"/>
                <genData code="NoneGen"/>
                <loadData/>
                <shuntYData>
                    <equivY im="0.0"/>
                </shuntYData>
            </aclfBus>
*/
		bus = parser.getBus("Bus2");
		assertTrue(bus.getLoadData().getContributeLoad().size() == 0);
		assertTrue(bus.getGenData().getCode() == LFGenCodeEnumType.NONE_GEN);
		
/*
            <aclfBus id="Bus5" areaNumber="1" zoneNumber="1" number="5" offLine="false" name="UNO-U2      ">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <baseVoltage unit="KV" value="13.8"/>
                <voltage unit="PU" value="1.0"/>
                <angle unit="DEG" value="-0.0047"/>
                <genData code="PV">
                    <contributeGen id="1" offLine="false" name="Gen:1(5)">
                        <desc>PSSE Generator 1 at Bus 5</desc>
                        <ownerList id="1">
                            <ownership unit="PU" value="1.0"/>
                        </ownerList>
                        <power unit="MVA" re="22.5" im="15.852"/>
                        <desiredVoltage unit="PU" value="1.0"/>
                        <desiredAngle unit="RAD" value="0.0"/>
                        <qLimit unit="MVAR" max="35.0" min="-35.0"/>
                        <pLimit unit="MW" max="45.0" min="15.0"/>
                        <mvaBase unit="MVA" value="68.24"/>
                        <sourceZ unit="PU" re="0.0" im="0.229"/>
                        <mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
                    </contributeGen>
                </genData>
                <loadData/>
                <shuntYData>
                    <equivY im="0.0"/>
                </shuntYData>
            </aclfBus>
*/
		bus = parser.getBus("Bus5");
		assertTrue(bus.getGenData().getCode() == LFGenCodeEnumType.PV);
		
		Gen1= bus.getGenData().getContributeGen().get(0).getValue();
		assertTrue(Gen1.getPower().getRe() == 22.5);
		assertTrue(Gen1.getPower().getIm() == 15.852);
		assertTrue(Gen1.getDesiredVoltage().getValue() == 1.0);
		assertTrue(Gen1.getPLimit().getMax() == 45.0);
		assertTrue(Gen1.getPLimit().getMin() == 15.0);
		assertTrue(Gen1.getQLimit().getMax() == 35.0);
		assertTrue(Gen1.getQLimit().getMin() == -35.0);
		
/*
    <branchList>
      <branch id="Bus2_to_Bus3_cirId_1" circuitId="1" offLine="false">
        <ownerList>
          <owner id="1" ownership="1.0"/>
        </ownerList>
        <fromBus idRef="Bus2"/>
        <toBus idRef="Bus3"/>
        <loadflowData code="Line">
          <z re="0.0015" im="0.0085" unit="PU"/>
          <totalShuntY re="0.0" im="0.0164" unit="PU"/>
          <meterLocation>FromSide</meterLocation>
          <branchRatingLimit>
            <mva rating1="300.0" rating2="330.0" rating3="0.0" unit="MVA"/>
          </branchRatingLimit>
        </loadflowData>
      </branch>
*/
		LineBranchXmlType line = parser.getLineBranch("Bus2", "Bus3", "1");
		
		assertTrue(line.getZ().getRe() == 0.0015);
		assertTrue(line.getZ().getIm() == 0.0085);
		assertTrue(line.getTotalShuntY().getRe() == 0.0);
		assertTrue(line.getTotalShuntY().getIm() == 0.0164);
		assertTrue(line.getMeterLocation() == BranchBusSideEnumType.FROM_SIDE);
		assertTrue(line.getRatingLimit().getMva().getRating1() == 300.0);
		assertTrue(line.getRatingLimit().getMva().getRating2() == 330.0);
		assertTrue(line.getRatingLimit().getMva().getRating3() == 0.0);
		
/*
      <branch id="Bus2_to_Bus1_cirId_1" circuitId="1" name="T1          " offLine="false">
        <ownerList>
          <owner id="1" ownership="1.0"/>
        </ownerList>
        <fromBus idRef="Bus2"/>
        <toBus idRef="Bus1"/>
        <loadflowData code="Transformer">
          <z re="0.0" im="0.17191" unit="PU"/>
          <fromTap value="1.0" unit="PU"/>
          <toTap value="1.0" unit="PU"/>
          <meterLocation>ToSide</meterLocation>
          <xfrInfo>
            <ratedPower value="100.0" unit="MVA"/>
            <dataOnSystemBase>true</dataOnSystemBase>
          </xfrInfo>
          <branchRatingLimit>
            <mva rating1="79.0" rating2="118.5" rating3="0.0" unit="MVA"/>
          </branchRatingLimit>
        </loadflowData>
      </branch>
 */
		XfrBranchXmlType xfr = parser.getXfrBranch("Bus2", "Bus1", "1");

		assertTrue(xfr.getZ().getRe() == 0.0);
		assertTrue(xfr.getZ().getIm() == 0.17191);
		assertTrue(xfr.getFromTurnRatio().getValue() == 1.0);
		assertTrue(xfr.getToTurnRatio().getValue() == 1.0);
		assertTrue(xfr.getMeterLocation() == BranchBusSideEnumType.TO_SIDE);
		assertTrue(xfr.getMeterLocation() == BranchBusSideEnumType.TO_SIDE);
		assertTrue(xfr.getXfrInfo().getRatedPower().getValue() == 100.0 );
		assertTrue(xfr.getXfrInfo().isDataOnSystemBase());
		assertTrue(xfr.getRatingLimit().getMva().getRating2() == 118.5);
		assertTrue(xfr.getRatingLimit().getMva().getRating3() == 0.0);
	}

}


