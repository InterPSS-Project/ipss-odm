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

package org.ieee.odm.psse;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.junit.Test;

public class PSSEV30_GuideSampleTest { 
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new PSSEAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile("testdata/psse/PSSEV30_GuideSample.raw"));
		
		//System.out.println(adapter.getModel().toString());
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		LoadflowNetXmlType net = parser.getNet();		
		/*
      <bus id="Bus151" number="151" areaNumber="1" name="NUCPANT     " offLine="false">
        <ownerList>
          <owner id="1"/>
        </ownerList>
        <baseVoltage value="500.0" unit="KV"/>
        <loadflowData>
          <voltage value="1.0119" unit="PU"/>
          <angle value="10.8887" unit="DEG"/>
          <genData>
            <equivGen code="NoneGen"/>
          </genData>
          <shuntY re="0.0" im="-6.0" unit="PU"/>
        </loadflowData>
      </bus>
		 */
		LoadflowBusXmlType bus = parser.getBus("Bus151");
		assertTrue(bus.getShuntY().getIm() == -6.0);
		
	/*
    <ownerList>
      <owner id="1" number="1" name="'TRAN 1      '"/>
      <owner id="2" number="2" name="'TRAN 2      '"/>
      <owner id="5" number="5" name="'TRAN 5      '"/>
      <owner id="11" number="11" name="'GEN 1       '"/>
      <owner id="22" number="22" name="'GEN 2       '"/>
      <owner id="55" number="55" name="'GEN 5       '"/>
      <owner id="100" number="100" name="'NO BUSES    '"/>
    </ownerList>
    */
	assertTrue(net.getOwnerList().size() == 7);
		
	/*
    <lossZoneList>
      <lossZone id="1" number="1" name="'FIRST       '"/>
      <lossZone id="2" number="2" name="'SECOND      '"/>
      <lossZone id="5" number="5" name="'FIFTH       '"/>
      <lossZone id="77" number="77" name="'PLANT       '"/>
    </lossZoneList>
    */
	assertTrue(net.getLossZoneList().getLossZone().size() == 4);

	/*
	 <interchangeList>
      <interchange>
        <areaTransfer fromArea="1" toArea="2" id="'A'" amountMW="70.0"/>
      </interchange>
      <interchange>
        <areaTransfer fromArea="1" toArea="2" id="'B'" amountMW="30.0"/>
      </interchange>
      <interchange>
        <areaTransfer fromArea="1" toArea="5" id="'A'" amountMW="100.0"/>
      </interchange>
      <interchange>
        <areaTransfer fromArea="1" toArea="5" id="'B'" amountMW="50.0"/>
      </interchange>
    </interchangeList>
	 */
	assertTrue(net.getInterchangeList().getInterchange().size() == 4);
	
	}	
}


