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
  * @Author Mike Zhou
  * @Version 1.0
  * @Date 02/01/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.opf.matpower;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter;
import org.ieee.odm.adapter.opf.matpower.OpfMatpowerAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.CostModelEnumType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OpfGenBusXmlType;
import org.ieee.odm.schema.OpfGenOperatingModeEnumType;
import org.ieee.odm.schema.OpfNetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.junit.Test;

public class OPF_Matpower_ODMTest { 
	@Test
	public void testCase3buslp() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("OPF_Matpower Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new OpfMatpowerAdapter();
		assertTrue(adapter.parseInputFile("testdata/matpower/case3bus.m"));
		
		OpfModelParser parser = (OpfModelParser)adapter.getModel();
		//System.out.println(parser.toXmlDoc("out/matpower/case3bus.xml"));
		
		//LoadflowNetXmlType baseCaseNet = parser.getAclfNet();
		OpfNetworkXmlType net = parser.getOpfNetwork();
		
		assertTrue(net.getBusList().getBus().size() == 3);
		assertTrue(net.getBranchList().getBranch().size() == 3);

		assertTrue(net.getBasePower().getValue() == 100.0);
		assertTrue(net.getBasePower().getUnit() == ApparentPowerUnitType.MVA);

		// Check Bus Data
		// ==============
		
		// Bus 1 is a swing bus
		//    1 Bus 1     HV  1  1  3 1.060    0.0      0.0      0.0    232.4   -16.9   132.0  1.060     0.0     0.0   0.0    0.0        0
		OpfGenBusXmlType busRec = parser.getOpfGenBus("Bus1");
		//System.out.println(busRec);
		assertTrue(busRec.getBaseVoltage().getValue() == 10.0);
		assertTrue(busRec.getVoltage().getValue() == 1.0);
		assertTrue(busRec.getAngle().getValue() == 0.0);
		//LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(busRec.getGenData());
		assertTrue(busRec.getGenData().getCode() == LFGenCodeEnumType.SWING);
		//assertTrue(busRec.getLoadData().getEquivLoad() == null);
		assertTrue(busRec.getOperatingMode().equals(OpfGenOperatingModeEnumType.PV_GENERATOR));
		assertTrue(busRec.getIncCost().getCostModel().equals(CostModelEnumType.PIECE_WISE_LINEAR_MODEL));
		assertTrue(busRec.getIncCost().getPieceWiseLinearModel().getStairStep().get(0).getPrice().getValue()==10);
		assertTrue(busRec.getIncCost().getPieceWiseLinearModel().getStairStep().get(0).getAmount().getValue()==20);
		assertTrue(busRec.getConstraints().getActivePowerLimit().getMax() == 200);
		assertTrue(busRec.getConstraints().getActivePowerLimit().getMin() == 20);
		
		// Bus 2 is a PV bus with load
		//   2 Bus 2     HV  1  1  2 1.045  -4.98     21.7     12.7     40.0    42.4   132.0  1.045    50.0   -40.0   0.0    0.0        0
		LoadflowBusXmlType bus3 =  parser.getBus("Bus3");
		//System.out.println(busRec);
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus3.getLoadData());
		assertTrue(defaultLoad.getConstPLoad().getRe()==150);		
		assertTrue(defaultLoad.getCode() == LFLoadCodeEnumType.CONST_P);		
		assertTrue(defaultLoad.getConstPLoad().getIm() == 0);
		assertTrue(defaultLoad.getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);

		
		// Check Branch Data
		// =================
				
		LineBranchXmlType braRec = parser.getLineBranch("Bus1", "Bus2", "1");
		assertTrue(braRec != null);
		
		assertTrue(braRec.getZ().getRe() == 0); 
		assertTrue(braRec.getZ().getIm() == 0.2); 
		assertTrue(braRec.getZ().getUnit() == ZUnitType.PU); 
		assertTrue(braRec.getRatingLimit().getMw().getRating1() == 30);	
		

		//parser.stdout();
	}
	
	@Test
	public void testCase3busqp() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("OPF_Matpower Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new OpfMatpowerAdapter();
		assertTrue(adapter.parseInputFile("testdata/matpower/case3bus_qp.m"));
		
		OpfModelParser parser = (OpfModelParser)adapter.getModel();
		//System.out.println(parser.toXmlDoc("out/matpower/case3bus_qp.xml"));
		
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		OpfNetworkXmlType net = parser.getOpfNetwork();
		
		assertTrue(baseCaseNet.getBusList().getBus().size() == 3);
		assertTrue(baseCaseNet.getBranchList().getBranch().size() == 3);

		assertTrue(baseCaseNet.getBasePower().getValue() == 100.0);
		assertTrue(baseCaseNet.getBasePower().getUnit() == ApparentPowerUnitType.MVA);

		// Check Bus Data
		// ==============
		
		// Bus 1 is a swing bus
		//    1 Bus 1     HV  1  1  3 1.060    0.0      0.0      0.0    232.4   -16.9   132.0  1.060     0.0     0.0   0.0    0.0        0
		OpfGenBusXmlType busRec = (OpfGenBusXmlType) parser.getBus("Bus1");
		//System.out.println(busRec);
		assertTrue(busRec.getBaseVoltage().getValue() == 10.0);
		assertTrue(busRec.getVoltage().getValue() == 1.0);
		assertTrue(busRec.getAngle().getValue() == 0.0);
		
		//LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(busRec.getGenData());
		assertTrue(busRec.getGenData().getCode() == LFGenCodeEnumType.SWING);		
		assertTrue(busRec.getOperatingMode().equals(OpfGenOperatingModeEnumType.PV_GENERATOR));
		assertTrue(busRec.getIncCost().getCostModel().equals(CostModelEnumType.QUADRATIC_MODEL));
		assertTrue(busRec.getIncCost().getQuadraticModel().getSqrCoeff().getValue() == 0.00463);
		assertTrue(busRec.getIncCost().getQuadraticModel().getLinCoeff().getValue() == 10.694);
		assertTrue(busRec.getIncCost().getQuadraticModel().getConstCoeff() == 10000);
		assertTrue(busRec.getConstraints().getActivePowerLimit().getMax() == 200);
		assertTrue(busRec.getConstraints().getActivePowerLimit().getMin() == 20);
		
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(busRec.getLoadData());
		assertTrue(defaultLoad.getConstPLoad().getRe()== 132.66);
		assertTrue(defaultLoad.getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);

		
		// Check Branch Data
		// =================
				
		LineBranchXmlType braRec = parser.getLineBranch("Bus1", "Bus2", "1");
		assertTrue(braRec != null);
		
		assertTrue(braRec.getZ().getRe() == 0); 
		assertTrue(braRec.getZ().getIm() == 0.2); 
		assertTrue(braRec.getZ().getUnit() == ZUnitType.PU); 
		assertTrue(braRec.getRatingLimit().getMw().getRating1() == 55);	
		

		//parser.stdout();
	}
}

