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

package org.ieee.odm.ieeecdf;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ieeecdf.IeeeCDFAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.junit.Test;

public class IEEECDF_ODMTest { 
	@Test
	public void testCaseInputLines() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new IeeeCDFAdapter();
		assertTrue(adapter.parseInput(this.ieee14asLines));
		AclfModelParser parser = (AclfModelParser)adapter.getModel();

		//System.out.println(parser.toXmlDoc());
		
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		assertTrue(baseCaseNet.getBusList().getBus().size() == 14);
		assertTrue(baseCaseNet.getBranchList().getBranch().size() == 20);

		assertTrue(baseCaseNet.getBasePower().getValue() == 100.0);
		assertTrue(baseCaseNet.getBasePower().getUnit() == ApparentPowerUnitType.MVA);
	}
	
	@Test
	public void testCaseInputFile() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		IODMAdapter adapter = new IeeeCDFAdapter();
		assertTrue(adapter.parseInputFile("testdata/ieee_format/Ieee14Bus.ieee"));
		
		AclfModelParser parser = (AclfModelParser)adapter.getModel();
		System.out.println(parser.toXmlDoc());
		
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		assertTrue(baseCaseNet.getBusList().getBus().size() == 14);
		assertTrue(baseCaseNet.getBranchList().getBranch().size() == 20);

		assertTrue(baseCaseNet.getBasePower().getValue() == 100.0);
		assertTrue(baseCaseNet.getBasePower().getUnit() == ApparentPowerUnitType.MVA);

		// Check Bus Data
		// ==============
		
		// Bus 1 is a swing bus
		//    1 Bus 1     HV  1  1  3 1.060    0.0      0.0      0.0    232.4   -16.9   132.0  1.060     0.0     0.0   0.0    0.0        0
		LoadflowBusXmlType busRec = parser.getBus("Bus1");
		//System.out.println(busRec);
		assertTrue(busRec.getBaseVoltage().getValue() == 132.0);
		assertTrue(busRec.getVoltage().getValue() == 1.060);
		assertTrue(busRec.getAngle().getValue() == 0.0);
		assertTrue(busRec.getGenCode() == LFGenCodeEnumType.SWING);
		assertTrue(busRec.getLoadData().getContributeLoad().size()==0);
		//TODO, mike, please check the following ShuntYData()
		/* my local ODM output is  
		 *     <shuntYData>
                    <equivY im="0.0"/>
               </shuntYData>
            
           
		    @XmlAttribute
		    protected Double re;
		    @XmlAttribute(required = true)
		    protected double im;
		 ---------------------------------------------------
		     im is required, why? 
		 */
		assertTrue(busRec.getShuntYData().getEquivY().getIm()== 0.0);

		// Bus 2 is a PV bus with load
		//   2 Bus 2     HV  1  1  2 1.045  -4.98     21.7     12.7     40.0    42.4   132.0  1.045    50.0   -40.0   0.0    0.0        0
		busRec = parser.getBus("Bus2");
		//System.out.println(busRec);
		assertTrue(busRec.getGenCode() == LFGenCodeEnumType.PV);
		assertTrue(busRec.getGenData().getContributeGen().size()==1);
		assertTrue(busRec.getGenData().getContributeGen().get(0).getValue().getPower().getRe() == 40.0);
		assertTrue(busRec.getGenData().getContributeGen().get(0).getValue().getPower().getUnit() == ApparentPowerUnitType.MVA);
		assertTrue(busRec.getGenData().getContributeGen().get(0).getValue().getQLimit().getMax() == 50.0);
		assertTrue(busRec.getGenData().getContributeGen().get(0).getValue().getQLimit().getMin() == -40.0);
		assertTrue(busRec.getGenData().getContributeGen().get(0).getValue().getQLimit().getUnit() == ReactivePowerUnitType.MVAR);
		
		assertTrue(busRec.getLoadData().getContributeLoad().size()==1);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getCode() == LFLoadCodeEnumType.CONST_P);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getRe() == 21.7);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getIm() == 12.7);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);

		// Bus 9 is a load bus, also there is a capacitor of 0.19 pu
		//    9 Bus 9     LV  1  1  0 1.056 -14.94     29.5     16.6      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.19       0
		busRec = parser.getBus("Bus9");
		assertTrue(busRec.getLoadData().getContributeLoad().size()==1);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getCode() == LFLoadCodeEnumType.CONST_P);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getRe() == 29.5);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getIm() == 16.6);
		assertTrue(busRec.getLoadData().getContributeLoad().get(0).getValue().getConstPLoad().getUnit() == ApparentPowerUnitType.MVA);
		
		assertTrue(busRec.getShuntYData().getEquivY().getRe() == 0.0);
		assertTrue(busRec.getShuntYData().getEquivY().getIm() == 0.19);
		assertTrue(busRec.getShuntYData().getEquivY().getUnit() == YUnitType.PU);
		
		// Bus 7 is non-gen and non-load bus
		//    7 Bus 7     ZV  1  1  0 1.062 -13.37      0.0      0.0      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0
		busRec = parser.getBus("Bus7");
		//assertTrue(busRec.getLoadflowData().getGenData() == null);
		assertTrue(busRec.getLoadData().getContributeLoad().size()==0);
		assertTrue(busRec.getShuntYData().getEquivY().getIm() == 0.0);

		// Check Branch Data
		// =================
		
		// Branch 1->2 is a LIne
		//    1    2  1  1 1 0  0.01938   0.05917     0.0528     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0
		LineBranchXmlType braRec = parser.getLineBranch("Bus1", "Bus2", "1");
		assertTrue(braRec != null);
		
		assertTrue(braRec.getZ().getRe() == 0.01938); 
		assertTrue(braRec.getZ().getIm() == 0.05917); 
		assertTrue(braRec.getZ().getUnit() == ZUnitType.PU); 
		assertTrue(braRec.getTotalShuntY().getRe() == 0.0); 
		assertTrue(braRec.getTotalShuntY().getIm() == 0.0528); 
		assertTrue(braRec.getTotalShuntY().getUnit() == YUnitType.PU); 
		
		// Branch 4->7 is a Xfr
		//   4    7  1  1 1 1  0.0       0.20912     0.0        0     0     0    0 0  0.978     0.0 0.0    0.0     0.0    0.0   0.0
		XfrBranchXmlType xfrBraRec = parser.getXfrBranch("Bus4", "Bus7", "1");
		assertTrue(braRec != null);
		assertTrue(xfrBraRec.getZ().getRe() == 0.0); 
		assertTrue(xfrBraRec.getZ().getIm() == 0.20912); 
		assertTrue(xfrBraRec.getZ().getUnit() == ZUnitType.PU); 
		//System.out.println(braRec.getLoadflowData().getXformerData().getFromTap());
		//System.out.println(braRec.getLoadflowData().getXformerData().getToTap());
		assertTrue(xfrBraRec.getFromTurnRatio().getValue() == 0.978); 
		assertTrue(xfrBraRec.getToTurnRatio().getValue() == 1.0);

		//parser.stdout();
	}
	
	String[] ieee14asLines = {
			" 08/19/93 UW ARCHIVE           100.0  1962 W IEEE 14 Bus Test Case",
			"BUS DATA FOLLOWS                            14 ITEMS",
			"   1 Bus 1     HV  1  1  3 1.060    0.0      0.0      0.0    232.4   -16.9   132.0  1.060     0.0     0.0   0.0    0.0        0",
			"   2 Bus 2     HV  1  1  2 1.045  -4.98     21.7     12.7     40.0    42.4   132.0  1.045    50.0   -40.0   0.0    0.0        0",
			"   3 Bus 3     HV  1  1  2 1.010 -12.72     94.2     19.0      0.0    23.4   132.0  1.010    40.0     0.0   0.0    0.0        0",
			"   4 Bus 4     HV  1  1  0 1.019 -10.33     47.8     -3.9      0.0     0.0   132.0  0.0       0.0     0.0   0.0    0.0        0",
			"   5 Bus 5     HV  1  1  0 1.020  -8.78      7.6      1.6      0.0     0.0   132.0  0.0       0.0     0.0   0.0    0.0        0",
			"   6 Bus 6     LV  1  1  2 1.070 -14.22     11.2      7.5      0.0    12.2    35.0  1.070    24.0    -6.0   0.0    0.0        0",
			"   7 Bus 7     ZV  1  1  0 1.062 -13.37      0.0      0.0      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"   8 Bus 8     TV  1  1  2 1.090 -13.36      0.0      0.0      0.0    17.4    10.0  1.090    24.0    -6.0   0.0    0.0        0",
			"   9 Bus 9     LV  1  1  0 1.056 -14.94     29.5     16.6      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.19       0",
			"  10 Bus 10    LV  1  1  0 1.051 -15.10      9.0      5.8      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"  11 Bus 11    LV  1  1  0 1.057 -14.79      3.5      1.8      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"  12 Bus 12    LV  1  1  0 1.055 -15.07      6.1      1.6      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"  13 Bus 13    LV  1  1  0 1.050 -15.16     13.5      5.8      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"  14 Bus 14    LV  1  1  0 1.036 -16.04     14.9      5.0      0.0     0.0    35.0  0.0       0.0     0.0   0.0    0.0        0",
			"-999 ",
			"BRANCH DATA FOLLOWS                         20 ITEMS",
			"   1    2  1  1 1 0  0.01938   0.05917     0.0528     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   1    5  1  1 1 0  0.05403   0.22304     0.0492     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   2    3  1  1 1 0  0.04699   0.19797     0.0438     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   2    4  1  1 1 0  0.05811   0.17632     0.0340     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   2    5  1  1 1 0  0.05695   0.17388     0.0346     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   3    4  1  1 1 0  0.06701   0.17103     0.0128     0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   4    5  1  1 1 0  0.01335   0.04211     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   4    7  1  1 1 1  0.0       0.20912     0.0        0     0     0    0 0  0.978     0.0 0.0    0.0     0.0    0.0   0.0",
			"   4    9  1  1 1 1  0.0       0.55618     0.0        0     0     0    0 0  0.969     0.0 0.0    0.0     0.0    0.0   0.0",
			"   5    6  1  1 1 1  0.0       0.25202     0.0        0     0     0    0 0  0.932     0.0 0.0    0.0     0.0    0.0   0.0",
			"   6   11  1  1 1 0  0.09498   0.19890     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   6   12  1  1 1 0  0.12291   0.25581     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   6   13  1  1 1 0  0.06615   0.13027     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   7    8  1  1 1 1  0.0       0.17615     0.0        0     0     0    0 0  1.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   7    9  1  1 1 1  0.0       0.11001     0.0        0     0     0    0 0  1.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   9   10  1  1 1 0  0.03181   0.08450     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"   9   14  1  1 1 0  0.12711   0.27038     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"  10   11  1  1 1 0  0.08205   0.19207     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"  12   13  1  1 1 0  0.22092   0.19988     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"  13   14  1  1 1 0  0.17093   0.34802     0.0        0     0     0    0 0  0.0       0.0 0.0    0.0     0.0    0.0   0.0",
			"-999",
			"LOSS ZONES FOLLOWS                     1 ITEMS",
			"  1 IEEE 14 BUS",
			"-99",
			"INTERCHANGE DATA FOLLOWS                 1 ITEMS",
			" 1    2 Bus 2     HV    0.0  999.99  IEEE14  IEEE 14 Bus Test Case",
			"-9",
			"TIE LINES FOLLOWS                     0 ITEMS",
			"-999",
			"END OF DATA"			
	};
}

