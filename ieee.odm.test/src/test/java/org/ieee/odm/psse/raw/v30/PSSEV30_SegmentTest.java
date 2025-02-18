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
import org.ieee.odm.schema.Xfr3WBranchXmlType;
import org.junit.Test;

public class PSSEV30_SegmentTest { 
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile("testData/psse/v30/PSSE30_SegTest.raw"));
		//System.out.println(adapter.getModel());
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//parser.stdout();
		/*
		 *  36309, 36703, 36106,'1 ',2,3,1,   0.00000,   0.00000,2,'E FRA 83    ',1,   1,1.0000
            249550,   0.06340,   300.00,    138110,   0.06190,   108.00,    132475,   0.08895,   108.00,1.01533, -36.0962
            338.250,   0.000,   0.000,   420.00,   480.00,   519.00, 0,      0, 1.10000, 0.90000, 1.10000, 0.90000,  33, 0, 0.00000, 0.00000
            138.000,   0.000,   0.000,   420.00,   480.00,   519.00, 0,      0, 1.10000, 0.90000, 1.10000, 0.90000,  33, 0, 0.00000, 0.00000
            33.0000,   0.000,   0.000,     0.00,     0.00,     0.00, 0,      0, 1.10000, 0.90000, 1.10000, 0.90000,  33, 0, 0.00000, 0.00000
		 */
		Xfr3WBranchXmlType branch = (Xfr3WBranchXmlType) parser.getXfr3WBranch("Bus36309", "Bus36703", "Bus36106", "1");
		assertTrue(Math.abs(branch.getZ().getRe()-0.0024955)<1.0E-6);
		assertTrue(Math.abs(branch.getZ().getIm()-0.02098547)<1.0E-6);
		assertTrue(branch.getFromTurnRatio().getValue() == 0.9804347826086957);
		assertTrue(branch.getToTurnRatio().getValue() == 1.0);
		
		assertTrue(Math.abs(branch.getZ23().getRe()- 0.0013811)<1.0E-6);
		assertTrue(Math.abs(branch.getZ31().getRe()-0.00132475)<1.0E-6);
				
		/*
      <dcLint2T id="Bus615600_to_Bus615353_cirId_1" number="1">
        <controlMode>power</controlMode>
        <lineR r="13.75" unit="OHM"/>
        <powerDemand value="552.0" unit="MW"/>
        <controlOnRectifierSide>true</controlOnRectifierSide>
        <scheduledDCVoltage value="410.0" unit="KV"/>
        <meterdEnd>inverter</meterdEnd>
        <modeSwitchDCVoltage value="-1.0" unit="KV"/>
        <compoundingR r="13.75" unit="OHM"/>
        <powerOrCurrentMarginPU>0.1</powerOrCurrentMarginPU>
        <minDCVoltage value="0.0" unit="KV"/>
        <rectifier>
          <busId idRef="Bus615600"/>
          <numberofBridges>2</numberofBridges>
          <minFiringAngle value="15.0" unit="DEG"/>
          <maxFiringAngle value="19.0" unit="DEG"/>
          <acSideRatedVoltage value="230.0" unit="KV"/>
          <commutatingZ re="0.0" im="21.6" unit="OHM"/>
          <commutatingCapacitor>0.0</commutatingCapacitor>
          <xformerTurnRatio>0.7426</xformerTurnRatio>
          <xformerTapSetting value="0.975" unit="PU"/>
          <xformerTapLimit max="1.2" min="0.9" unit="PU"/>
          <xformerTapStepSize>0.0125</xformerTapStepSize>
        </rectifier>
        <inverter>
          <busId idRef="Bus615353"/>
          <numberofBridges>2</numberofBridges>
          <minFiringAngle value="18.0" unit="DEG"/>
          <maxFiringAngle value="18.0" unit="DEG"/>
          <acSideRatedVoltage value="345.0" unit="KV"/>
          <commutatingZ re="0.0" im="19.8" unit="OHM"/>
          <commutatingCapacitor>0.0</commutatingCapacitor>
          <xformerTurnRatio>0.4714</xformerTurnRatio>
          <xformerTapSetting value="0.95221" unit="PU"/>
          <xformerTapLimit max="1.2125" min="0.9125" unit="PU"/>
          <xformerTapStepSize>1.0E-5</xformerTapStepSize>
        </inverter>
      </dcLint2T>
		 */
	}
}


