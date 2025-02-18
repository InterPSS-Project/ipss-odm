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

package org.ieee.odm.opf;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.ieee.odm.model.ODMModelParser;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.BaseOpfNetworkXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.CostModelEnumType;
import org.ieee.odm.schema.OpfBranchXmlType;
import org.ieee.odm.schema.OpfGenBusXmlType;
import org.ieee.odm.schema.OpfNetworkEnumType;
import org.ieee.odm.schema.OpfNetworkXmlType;
import org.junit.Test;

public class OpfSample_3Bus_ODMTest_stephen { 
	@Test
	public void testCase() throws Exception {
		File file = new File("testdata/ieee_odm/opf_3bus_stephen.xml");
		ODMModelParser parser = new ODMModelParser();
		parser.parse(new FileInputStream(file));
		//System.out.println(parser.toXmlDoc(false));
		
		OpfModelParser opfParser = parser.toOpfModelParser();
		assertTrue(opfParser.getNet().getBasePower().getValue() == 100.0);
		assertTrue(opfParser.getNet().getBusList().getBus().size() == 3);
		
		OpfNetworkXmlType opfNet = opfParser.getOpfNetwork();
		/*BaseOpfNetworkXmlType bopfnet = opfParser.getBaseOpfNetwork();
		opfNet = (OpfNetworkXmlType) bopfnet;*/
		
		
		assertTrue(opfNet.getAnglePenaltyFactor() == 1);
		assertTrue(opfNet.getOpfNetType().equals(OpfNetworkEnumType.OPF_NETWORK));
		
		// bus1
		OpfGenBusXmlType bus1 = (OpfGenBusXmlType) opfParser.getBus("bus1");
		assertTrue(bus1.getIncCost().getCostModel().equals(CostModelEnumType.QUADRATIC_MODEL));
		//assertTrue(bus1.getIncCost().getQuadraticModel().getSqrCoeff()==1);
		assertTrue(bus1.getConstraints().getActivePowerLimit().getMax()==4);
		
		// bus2
		OpfGenBusXmlType bus2 = (OpfGenBusXmlType) opfParser.getBus("bus2");
		assertTrue(bus2.getIncCost().getCostModel().equals(CostModelEnumType.PIECE_WISE_LINEAR_MODEL));
		System.out.println(bus2.getIncCost().getPieceWiseLinearModel().getStairStep().get(0).getAmount().getValue());
		assertTrue(bus2.getIncCost().getPieceWiseLinearModel().getStairStep().get(0).getAmount().getValue()==2);
		
		// branch1
		OpfBranchXmlType bra1 = (OpfBranchXmlType) parser.getBranch("branch1");
		assertTrue(bra1.getRatingLimit().getMw().getRating1()==1.1);
		
	}
}

