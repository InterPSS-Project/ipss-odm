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
import org.junit.Test;

public class OpfSample_3Bus_ODMTest { 
	@Test
	public void testCase() throws Exception {
		File file = new File("testdata/ieee_odm/opf_3bus_test.xml");
		ODMModelParser parser = new ODMModelParser();
		parser.parse(new FileInputStream(file));
		//System.out.println(parser.toXmlDoc(false));
		
		OpfModelParser opfParser = parser.toOpfModelParser();
		assertTrue(opfParser.getNet().getBasePower().getValue() == 100.0);
		assertTrue(opfParser.getNet().getBusList().getBus().size() == 3);
	}
}

