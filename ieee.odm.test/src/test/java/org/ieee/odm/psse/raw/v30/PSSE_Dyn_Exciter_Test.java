package org.ieee.odm.psse.raw.v30;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.ExcIEEE1981ST1XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeAC1XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeDC1XmlType;
import org.ieee.odm.schema.ExcIEEE2005TypeST3AXmlType;
import org.ieee.odm.schema.ExcIEEE2005TypeST4BXmlType;
import org.junit.Test;

public class PSSE_Dyn_Exciter_Test {
	
	
	@Test
	public void ieee9_ODM_Dstab_1981Exc_test() throws ODMException{
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
			"testData/psse/IEEE9Bus/ieee9.raw",
			"testData/psse/IEEE9Bus/ieee9_dyn_Model_1981Exc.dyr"
	}));
	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
	//dstabParser.stdout();
	
	DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
	
	DStabGenDataXmlType bus1Gen =  (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
	
	ExcIEEE1981TypeDC1XmlType bus1Exc = (ExcIEEE1981TypeDC1XmlType) bus1Gen.getExciter().getValue();
	assertTrue(bus1Exc.getVrmax()==3.5);
	assertTrue(bus1Exc.getVrmin()==-3.5);
	
	
	DStabBusXmlType bus2 = dstabParser.getDStabBus("Bus2");
	
	DStabGenDataXmlType bus2Gen =  (DStabGenDataXmlType) bus2.getGenData().getContributeGen().get(0).getValue();
	
	ExcIEEE1981ST1XmlType bus2Exc = (ExcIEEE1981ST1XmlType) bus2Gen.getExciter().getValue();
	assertTrue(bus2Exc.getVrmax()==5.1);
	assertTrue(bus2Exc.getVrmin()==0.0);
	
	
    DStabBusXmlType bus3 = dstabParser.getDStabBus("Bus3");
	
	DStabGenDataXmlType bus3Gen =  (DStabGenDataXmlType) bus3.getGenData().getContributeGen().get(0).getValue();
	
	ExcIEEE1981TypeAC1XmlType bus3Exc = (ExcIEEE1981TypeAC1XmlType) bus3Gen.getExciter().getValue();
	
	
	
	}
	
	@Test
	public void ieee9_ODM_Dstab_2005Exc_test() throws ODMException{
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
			"testData/psse/IEEE9Bus/ieee9.raw",
			"testData/psse/IEEE9Bus/ieee9_dyn_Model_2005Exc.dyr"
	}));
	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
	//dstabParser.stdout();
	
	DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
	
	DStabGenDataXmlType bus1Gen =  (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
	
	ExcIEEE2005TypeST3AXmlType bus1Exc = (ExcIEEE2005TypeST3AXmlType) bus1Gen.getExciter().getValue();
	assertTrue(bus1Exc.getVrmax()==99);
	assertTrue(bus1Exc.getVrmin()==-99);
	
	
	DStabBusXmlType bus2 = dstabParser.getDStabBus("Bus2");
	
	DStabGenDataXmlType bus2Gen =  (DStabGenDataXmlType) bus2.getGenData().getContributeGen().get(0).getValue();
	
	ExcIEEE2005TypeST4BXmlType bus2Exc = (ExcIEEE2005TypeST4BXmlType) bus2Gen.getExciter().getValue();
	assertTrue(bus2Exc.getVrmax()==1.0);
	assertTrue(bus2Exc.getVrmin()==-0.87);
	
	}

}
