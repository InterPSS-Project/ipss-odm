 /*
  * @(#)UCTE_ODMTest.java   
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
  * @Author Mike Zhou
  * @Version 1.0
  * @Date 02/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.ucte;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ucte.UCTE_DEFAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.junit.Test;

public class UCTE_ODMTest {
	@Test
	public void testCaseAclf() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new UCTE_DEFAdapter();
		assertTrue(adapter.parseInputFile("testdata/ucte/AusPower_TestCase_Xfr.uct"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		assertTrue(baseCaseNet.getBusList().getBus().size() == 18);
		assertTrue(baseCaseNet.getBranchList().getBranch().size() == 30);
		
		// A1____1 is a load bus, voltage code 1 -> 380kv
		// A1    1                 0        280.000 .000000 .000000 .000000               
		LoadflowBusXmlType busRec = parser.getBus("A1____1");
		assertTrue(busRec.getBaseVoltage().getValue() == 380.0);
		assertTrue(busRec.getBaseVoltage().getUnit() == VoltageUnitType.KV);
		// if voltage not defined, it is equal to the base voltage
		assertTrue(busRec.getVoltage().getValue() == 380.0);
		assertTrue(busRec.getVoltage().getUnit() == VoltageUnitType.KV);
		assertTrue(busRec.getGenData() == null);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getRe() == 280.0);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getIm() == 0.0);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);
		assertTrue(busRec.getShuntYData().getEquivY().getY() == null);

		// A2____1 is a load bus
		// A2    1                 0        .000000 .000000 -150.00 .000000                
		busRec = parser.getBus("A2____1");
		assertTrue(busRec.getGenData().getEquivGen().getValue().getCode() == LFGenCodeEnumType.PQ);
		assertTrue(busRec.getGenData().getEquivGen().getValue().getPower().getRe() == 150.0);
		assertTrue(busRec.getGenData().getEquivGen().getValue().getPower().getIm() == 0.0);
		assertTrue(busRec.getGenData().getEquivGen().getValue().getPower().getUnit() == ApparentPowerUnitType.MVA);
		assertTrue(busRec.getLoadData() == null);
		
		// B4____1 is a swing bus
		// B4    1                 3 405.00 70.0000 .000000 .000000 .000000                
		busRec = parser.getBus("B4____1");
		assertTrue(busRec.getVoltage().getValue() == 405.0);
		assertTrue(busRec.getVoltage().getUnit() == VoltageUnitType.KV);
		assertTrue(busRec.getAngle().getValue() == 0.0);
		assertTrue(busRec.getGenData().getEquivGen().getValue().getCode() == LFGenCodeEnumType.SWING);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getRe() == 70.0);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getIm() == 0.0);
		assertTrue(busRec.getLoadData().getEquivLoad().getValue().getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);

		// A1____1->A2____1 is a line
		// A1    1  A2    1  1 0 1.3600 19.350 240.9601    480 
		LineBranchXmlType lineRec = parser.getLineBranch("A1____1", "A2____1", "1");
		
		assertTrue(lineRec.getZ().getRe() == 1.3600); 
		assertTrue(lineRec.getZ().getIm() == 19.350); 
		assertTrue(lineRec.getZ().getUnit() == ZUnitType.OHM); 
		assertTrue(lineRec.getTotalShuntY().getRe() == 0.0); 
		assertTrue(lineRec.getTotalShuntY().getIm() == 240.9601); 
		assertTrue(lineRec.getTotalShuntY().getUnit() == YUnitType.MICROMHO); 
		assertTrue(lineRec.getRatingLimit().getCurrent().getValue() == 480.0); 
		assertTrue(lineRec.getRatingLimit().getCurrent().getUnit() == CurrentUnitType.AMP); 
		
		// D1____1->D3____2 is a Xfr
		// D1    1  D3    2  1 0 400.  230.  600.0 .20000 15.000 -16.0000 5.0000   1000 
		XfrBranchXmlType xfrRec = parser.getXfrBranch("D1____1", "D3____2", "1");
		
		assertTrue(xfrRec.getZ().getRe() == 0.20); 
		assertTrue(xfrRec.getZ().getIm() == 15.0); 
		assertTrue(xfrRec.getZ().getUnit() == ZUnitType.OHM); 		
		
		assertTrue(xfrRec.getFromTurnRatio().getValue() == 1.0); // from ratio not defined, set to default 1.0
		assertTrue(xfrRec.getToTurnRatio().getValue() == 1.0); 

		assertTrue(xfrRec.getMagnitizingY().getRe() == 5.0); 
		assertTrue(xfrRec.getMagnitizingY().getIm() == -16.0); 
		assertTrue(xfrRec.getMagnitizingY().getUnit() == YUnitType.MICROMHO); 
		
		assertTrue(xfrRec.getXfrInfo().getFromRatedVoltage().getValue() == 400.0); 		
		assertTrue(xfrRec.getXfrInfo().getFromRatedVoltage().getUnit() == VoltageUnitType.KV); 		
		assertTrue(xfrRec.getXfrInfo().getToRatedVoltage().getValue() == 230.0); 		
		assertTrue(xfrRec.getXfrInfo().getToRatedVoltage().getUnit() == VoltageUnitType.KV); 		
		assertTrue(xfrRec.getXfrInfo().getRatedPower().getValue() == 600.0); 		
		assertTrue(xfrRec.getXfrInfo().getRatedPower().getUnit() == ApparentPowerUnitType.MVA); 		
		
		assertTrue(xfrRec.getRatingLimit().getCurrent().getValue() == 1000.0); 
	}
}

