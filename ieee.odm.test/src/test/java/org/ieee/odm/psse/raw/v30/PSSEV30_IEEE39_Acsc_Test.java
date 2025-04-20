package org.ieee.odm.psse.raw.v30;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.acsc.AcscModelParser;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.GroundingEnumType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.XformrtConnectionEnumType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV30_IEEE39_Acsc_Test {
	@Test
	public void testCase1() throws Exception {
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile(NetType.AcscNet, new String[]{
				"testdata/psse/v30/IEEE39Bus/IEEE39bus_v30.raw",
				"testdata/psse/v30/IEEE39Bus/IEEE39bus_v30.seq"
		}));
		AcscModelParser acscParser =(AcscModelParser) adapter.getModel();
		//acscParser.stdout();
		//caseContentInfo
		assertTrue(acscParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.SHORT_CIRCUIT);
		
		//generator sequence data @bus 30
		//ZPOS = ZNeg = ZZero =2.24000E-01
		
		
		ShortCircuitBusXmlType bus30 = acscParser.getBus("Bus30");
		ShortCircuitGenDataXmlType scGen = (ShortCircuitGenDataXmlType) bus30.getGenData().getContributeGen().get(0).getValue();
		assertTrue( scGen.getPotiveZ().getIm()== 2.24000E-01);
		assertTrue( scGen.getNegativeZ().getIm()== 2.24000E-01);
		assertTrue( scGen.getZeroZ().getIm()== 2.24000E-01);
		
		
		//Zero sequence Branch data: Bus1->bus2(1)
		// 1,     2,'1 ', 8.75000E-3, 1.02750E-1,    0.69870, 0.00000E+0, 0.00000E+0, 0.00000E+0, 0.00000E+0
		
		LineShortCircuitXmlType scLine  = acscParser.getAcscLine("Bus1", "Bus2", "1");
		assertTrue(scLine.getZ0().getRe()==8.75000E-3);
		assertTrue(scLine.getZ0().getIm()==1.02750E-1);
		assertTrue(scLine.getY0Shunt().getIm()==0.69870); // total zero seq charing susceptance
		
		
		//Zero sequence Transformer data: Bus6->bus31(1)
		//6,    31,     0,'1 ',  2, 0.00000E+0, 0.00000E+0, 0.00000E+0, 2.50000E-2
		
		//connection code =2, that is grounded wye-delta
	    XfrShortCircuitXmlType xfr6_31=	acscParser.getAcscXfr("Bus6", "Bus31", "1");
	    assertTrue(xfr6_31.getFromSideConnection().getXfrConnection() ==XformrtConnectionEnumType.WYE);
	    assertTrue(xfr6_31.getFromSideConnection().getGrounding().getGroundingConnection() ==GroundingEnumType.SOLID_GROUNDED);
	    assertTrue(xfr6_31.getToSideConnection().getXfrConnection()==XformrtConnectionEnumType.DELTA);
	   
	    //zero sequence impedance R1+j*x1 =0.00000E+0+ j* 2.50000E-2
	    assertTrue(xfr6_31.getZ0().getIm()==2.50000E-2);
	    
	    
	
	}
}
