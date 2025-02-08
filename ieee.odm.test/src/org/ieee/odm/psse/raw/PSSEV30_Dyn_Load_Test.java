package org.ieee.odm.psse.raw;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.load.PSSELoadCMLDDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.junit.Test;

public class PSSEV30_Dyn_Load_Test {
	
	@Test
	public void ieee9_ODM_Dstab_ACMotor_test() throws ODMException{
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
				"testData/psse/IEEE9Bus/ieee9.raw",
				"testData/psse/IEEE9Bus/ieee9_dyn_Load_ACMotor.dyr"
		}));
		DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
		dstabParser.stdout();
		
		DStabBusXmlType bus5 = dstabParser.getDStabBus("Bus5");
		
		if(bus5.getLoadData().getContributeLoad().size() > 0){
			for(JAXBElement<? extends LoadflowLoadDataXmlType> dynLoadModel:bus5.getLoadData().getContributeLoad()){
				DStabLoadDataXmlType dynLoad =(DStabLoadDataXmlType) dynLoadModel.getValue();
				dynLoad.toString();
			}
		}
		
		
	
	
	}
	
	@Test
	public void ODM_Dstab_CMLD_Parser_test() throws ODMException{
		String s = "10020 'USRLOD'  1  'CMLDBLU2'        12    1    2  133   27  146   48 0 0 " + 
				"0 ," + 
				"1 ,2 ,3 ,4 ,5 ,6 ,7 ,8 ,9 ,10 ," + 
				"11 ,12 ,13 ,14 ,15 ,16 ,17 ,18 ,19 ,20 ," + 
				"21 ,22 ,23 ,24 ,25 ,26 ,27 ,28 ,29 ,30 ," + 
				"31 ,32 ,33 ,34 ,35 ,36 ,37 ,38 ,39 ,40 ," + 
				"41 ,42 ,43 ,44 ,45 ,46 ,47 ,48 ,49 ,50 ," + 
				"51 ,52 ,53 ,54 ,55 ,56 ,57 ,58 ,59 ,60 ," + 
				"61 ,62 ,63 ,64 ,65 ,66 ,67 ,68 ,69 ,70 ," + 
				"71 ,72 ,73 ,74 ,75 ,76 ,77 ,78 ,79 ,80 ," + 
				"81 ,82 ,83 ,84 ,85 ,86 ,87 ,88 ,89 ,90 ," + 
				"91 ,92 ,93 ,94 ,95 ,96 ,97 ,98 ,99 ,100 ," + 
				"101 ,102 ,103 ,104 ,105 ,106 ,107 ,108 ,109 ,110 ," + 
				"111 ,112 ,113 ,114 ,115 ,116 ,117 ,118 ,119 ,120 ," + 
				"121 ,122 ,123 ,124 ,125 ,126 ,127 ,128 ,129 ,130 ," + 
				"131 , 132";
				 		
		         PSSELoadCMLDDataParser parser = new PSSELoadCMLDDataParser(PsseVersion.PSSE_30);
			    try {
					parser.parseFields(s);
				} catch (ODMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    //System.out.println(parser.getFieldTable().toString());
			    String[] keys = parser.getMetadata();
			    
			    assertTrue(parser.getDouble("MVA") ==0.0);
			    assertTrue(parser.getDouble("Tdel") ==14.0);
			    assertTrue(parser.getDouble("FmD") ==21.0);
			    assertTrue(parser.getDouble("Trc2A") ==56.0);
			    assertTrue(parser.getDouble("Frcel") ==132.0);
			    
			    for(int i=4; i<keys.length;i++) { // skip the headers
			    	assertTrue(parser.getInt(keys[i]) ==i-4);
			    }
		
	}
	
	@Test
	public void ieee9_ODM_Dstab_CMLD_Mapper_test() throws ODMException{
		final LogManager logMgr = LogManager.getLogManager();
		Logger logger = Logger.getLogger("IEEE ODM Logger");
		logger.setLevel(Level.INFO);
		logMgr.addLogger(logger);
		
		PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
		assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
				"testData/psse/IEEE9Bus/ieee9.raw",
				"testData/psse/IEEE9Bus/ieee9_dyn_Load_CMLD.dyr"
		}));
		DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
		dstabParser.stdout();
		
		DStabBusXmlType bus5 = dstabParser.getDStabBus("Bus5");
		
		if(bus5.getLoadData().getContributeLoad().size() > 0){
			for(JAXBElement<? extends LoadflowLoadDataXmlType> dynLoadModel:bus5.getLoadData().getContributeLoad()){
				DStabLoadDataXmlType dynLoad =(DStabLoadDataXmlType) dynLoadModel.getValue();
				System.out.println(dynLoad.getLoadModel().getCMPLDWLoad().getBss());
				
				assertTrue(dynLoad.getLoadModel().getCMPLDWLoad().getBss()==1.0);
				assertTrue(dynLoad.getLoadModel().getCMPLDWLoad().getVtr1C()==87.0);
			}
		}
	}
}
