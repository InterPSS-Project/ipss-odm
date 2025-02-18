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

package org.ieee.odm.psse.raw;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.PSSEHeaderDataRawMapper;
import org.junit.Test;

public class PSSE_HearderVer_ODMTest { 
	@Test
	public void testCase1() throws Exception {
		PSSEHeaderDataRawMapper mapper = new PSSEHeaderDataRawMapper(PsseVersion.PSSE_30);
		assertTrue(mapper.getVersion("testData/psse/ver26Header.txt") == ODMFileFormatEnum.PsseV26);
		assertTrue(mapper.getVersion("testData/psse/ver29Header.txt") == ODMFileFormatEnum.PsseV30);
		assertTrue(mapper.getVersion("testData/psse/ver30Header.txt") == ODMFileFormatEnum.PsseV30);
	}
}


