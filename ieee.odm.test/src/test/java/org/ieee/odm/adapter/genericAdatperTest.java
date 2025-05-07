package org.ieee.odm.adapter;

import org.ieee.odm.ODMFileFormatEnum;
import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq11MachineXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import static org.junit.Assert.assertTrue;

public class genericAdatperTest {
	
//	@Test
//	public void testGenericAdapterSameFormat(){
//		  
//		  GenericODMAdapter adapter = new GenericODMAdapter(ODMFileFormatEnum.PsseV30,ODMFileFormatEnum.GePSLF);
//		  
//		  adapter.parseInputFile(NetType.DStabNet, new String[]{
//			"testData/psse/IEEE9Bus/ieee9.raw",
//			"testData/psse/IEEE9Bus/ieee9_dyn_Model_2005Exc.dyr"
//	         });
//	}
	
	
	//@Test
	public void testGenericAdapterDifferentFormats() throws ODMException{
		  
		  GenericODMAdapter adapter = new GenericODMAdapter(ODMFileFormatEnum.PsseV30,ODMFileFormatEnum.GePSLF);
		  
		  adapter.parseInputFile(NetType.DStabNet, new String[]{
			"testData/psse/IEEE9Bus/ieee9.raw",
			"testData/ge/ieee9_dyn_Gentpf_GE.dyd"
			//"testData/ge/ieee9_onlyGen_GE.dyd"
	        });
		  
		    DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
			//dstabParser.stdout();
			//caseContentInfo
			assertTrue(dstabParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.TRANSIENT_STABILITY);
			
			DStabNetXmlType dynNet =  dstabParser.getDStabNet();
			
			
			 DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
			 DStabGenDataXmlType defaultGen = DStabParserHelper.getDefaultGen(bus1.getGenData());
			 //DStabLoadDataXmlType defaultLoad = DStabParserHelper.getDefaultLoad(bus1.getLoadData());
			 
			 assertTrue(bus1.getGenData().getCode()==LFGenCodeEnumType.SWING);
			 //assertTrue(defaultLoad.getCode()==LFLoadCodeEnumType.NONE_LOAD);
			 
			assertTrue(defaultGen.getDesiredVoltage().getValue()==1.04);
			assertTrue(defaultGen.getSourceZ().getIm()==0.04);
			
			DStabGenDataXmlType contriGen = (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
			assertTrue(contriGen.getMvaBase().getValue()==100.0);
			assertTrue(contriGen.getMvarVControlParticipateFactor()==1.0);
			
			Eq11MachineXmlType machine = (Eq11MachineXmlType) contriGen.getMachineModel().getValue();
			assertTrue(machine.getD()==0.0);
			assertTrue(machine.getH()==23.64);
			
			assertTrue(machine.getXl()< machine.getXd1());
			
			assertTrue(machine.getXd()==0.146);
			assertTrue(machine.getXl()==0.0336);
			assertTrue(machine.getXd1()==0.0608);
			assertTrue(machine.getXd11()==0.04);
			
			assertTrue(machine.getXd11()==machine.getXq11());
			
			assertTrue(machine.getTd011().getValue()==0.04);
			assertTrue(machine.getTq011().getValue()==0.06);
			
			
			/*
			 * BUS3
			 * 
			 * Machine GENROU
			 * 
			 *             <eq11Ed11MachModel>
		                           <desc>GENROU</desc>
		                            <H>3.01</H>
		                            <D>0.0</D>
		                            <xl>0.0742</xl>
		                            <ra>0.0</ra>
		                            <xd>1.313</xd>
		                            <xq>1.258</xq>
		                            <xd1>0.1813</xd1>
		                            <Td01 unit="Sec" value="5.89"/>
		                            <seFmt1>
		                                <se100>0.0</se100>
		                                <se120>0.0</se120>
		                                <sliner>1.0</sliner>
		                            </seFmt1>
		                            <xq1>0.25</xq1>
		                            <Tq01 unit="Sec" value="0.6"/>
		                            <xd11>0.107</xd11>
		                            <Td011 unit="Sec" value="0.033"/>
		                            <xq11>0.107</xq11>
		                            <Tq011 unit="Sec" value="0.07"/>
		                    </eq11Ed11MachModel>
		            
			 * 
			 */
			 DStabBusXmlType bus3 = dstabParser.getDStabBus("Bus3");
			 defaultGen = DStabParserHelper.getDefaultGen(bus3.getGenData());
			 assertTrue(bus3.getGenData().getCode()==LFGenCodeEnumType.PV);
			 
			 DStabGenDataXmlType contriGen3 = (DStabGenDataXmlType) bus3.getGenData().getContributeGen().get(0).getValue();
			
			 Eq11Ed11MachineXmlType gen3Mach = (Eq11Ed11MachineXmlType) contriGen3.getMachineModel().getValue();
				assertTrue(gen3Mach.getD()==0.0);
				assertTrue(gen3Mach.getH()==3.01);
				
				assertTrue(gen3Mach.getXl()< gen3Mach.getXd1());
				
				assertTrue(gen3Mach.getXd()==1.313);
				assertTrue(gen3Mach.getXq()==1.258);
				assertTrue(gen3Mach.getXl()==0.0742);
				assertTrue(gen3Mach.getXd1()==0.1813);
				assertTrue(gen3Mach.getXq1()==0.25);
				assertTrue(gen3Mach.getXd11()==0.107);
				assertTrue(gen3Mach.getXd11()==gen3Mach.getXq11());
				
				assertTrue(gen3Mach.getTd01().getValue()==5.89);
				assertTrue(gen3Mach.getTd011().getValue()==0.033);
				assertTrue(gen3Mach.getTq011().getValue()==0.07);
		  
		  
	}

}
