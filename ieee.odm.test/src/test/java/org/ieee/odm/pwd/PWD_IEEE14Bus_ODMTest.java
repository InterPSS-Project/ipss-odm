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

package org.ieee.odm.pwd;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LFGenCodeEnumType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PWD_IEEE14Bus_ODMTest { 
	@Test
	public void testCaseNew() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/ieee14.AUX"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//System.out.println(parser.toXmlDoc());
		
		assertTrue(parser.getAclfBus("Bus1").getGenData().getCode() == LFGenCodeEnumType.SWING);
		assertTrue(parser.getAclfBus("Bus2").getGenData().getCode() == LFGenCodeEnumType.PV);
	}

	//@Test
	public void testCase_multiline() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/Ieee14_multiline.AUX"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		//System.out.println(parser.toXmlDoc());
		
		//LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(parser.getBus("Bus1").getGenData());
		assertTrue(parser.getAclfBus("Bus1").getGenData().getCode() ==LFGenCodeEnumType.SWING);
		//defaultGen = AclfParserHelper.getDefaultGen(parser.getBus("Bus2").getGenData());
		assertTrue(parser.getAclfBus("Bus2").getGenData().getCode()	== LFGenCodeEnumType.PV);
	}
}

