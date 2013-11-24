 /*
  * @(#)OdmXml_Test.java   
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
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.model.opf.OpfModelParser;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.OpfDclfGenBusXmlType;
import org.junit.Test;

public class ProcessOPFData_ODMTest { 
	@Test
	public void testCase() throws Exception {
/*
<!-- 
    from a aclf network, change to a opf network
    
      1) change <aclfNet -> <opfNet
      2) change analysisCategory to OPF
      3) add <anglePenaltyFactor>1</anglePenaltyFactor> at the end
      4) change some aclfBus to opfGenBus, using sample code here
 -->
 */
		
		File file = new File("testdata/ieee_odm/Ieee14Bus_opf.xml");
		OpfModelParser parser = new OpfModelParser(OpfModelParser.OpfNetType.DclfOpf);
		parser.parse(new FileInputStream(file));
		//System.out.println(parser.toXmlDoc(false));
		assertTrue(parser.getNet().getBasePower().getValue() == 100.0);
		assertTrue(parser.getNet().getBusList().getBus().size() == 14);
		
		BusXmlType aclfBus = parser.getBus("Bus2");
		assertTrue(aclfBus instanceof LoadflowBusXmlType);
		
		// a file could be used to store all Opf related info and
		// load here
		OpfDclfGenBusXmlType opfGenBus = (OpfDclfGenBusXmlType)ODMModelStringUtil.casting(
				aclfBus, "aclfBus", "dclfOpfGenBus", parser.getEncoding());
		opfGenBus.setCoeffA(1.0);
		opfGenBus.setCoeffB(0.5);
		parser.replaceBus("Bus2", opfGenBus);

		aclfBus = parser.getBus("Bus2");
		assertTrue(aclfBus instanceof OpfDclfGenBusXmlType);
		//System.out.println(opfParser.toXmlDoc(false));
	}
}

