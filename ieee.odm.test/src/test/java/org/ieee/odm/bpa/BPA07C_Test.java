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

package org.ieee.odm.bpa;

import java.io.File;
import java.io.FileOutputStream;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.bpa.BPAAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class BPA07C_Test { 
	@Test
	public void bpaTestCase() throws Exception {
		IODMAdapter adapter = new BPAAdapter();
//		IODMAdapter adapter =new PSSEV30Adapter();
		assertTrue(adapter.parseInputFile("testdata/bpa/07c-dc2load.dat"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
//		parser.stdout();
		// String xml=parser.toXmlDoc();
		// FileOutputStream out=new FileOutputStream(new File("out/bpa/07c_BPA_ODM_0607.xml"));
		// out.write(xml.getBytes());
		// out.flush();
		// out.close();
		
	}



}

