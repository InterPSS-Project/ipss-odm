package org.ieee.odm.psse.raw.v30;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter.NetType;
import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.DStabNetXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq11MachineXmlType;
import org.ieee.odm.schema.ExcIEEE1968Type1XmlType;
import org.ieee.odm.schema.GenRelayFRQTPATXmlType;
import org.ieee.odm.schema.GenRelayVTGTPATXmlType;
import org.ieee.odm.schema.GovIEEE1981Type1XmlType;
import org.ieee.odm.schema.GovIEEE1981Type3XmlType;
import org.ieee.odm.schema.LDS3RelayXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LVS3RelayXmlType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV30_IEEE9_Dstab_Test {
	
	@Test
	public void ieee9_ODM_Dstab_test() throws ODMException{
	final LogManager logMgr = LogManager.getLogManager();
	Logger logger = Logger.getLogger("IEEE ODM Logger");
	logger.setLevel(Level.INFO);
	logMgr.addLogger(logger);
	
	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
			"testdata/psse/v30/IEEE9Bus/ieee9.raw",
			"testdata/psse/v30/IEEE9Bus/ieee9.seq",
			"testdata/psse/v30/IEEE9Bus/ieee9_dyn_onlyGen.dyr"
	}));
	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
	//dstabParser.stdout();
	//caseContentInfo
	assertTrue(dstabParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.TRANSIENT_STABILITY);
	
	DStabNetXmlType dynNet =  dstabParser.getDStabNet();
	
	/*
	 * <hasLoadflowData>true</hasLoadflowData>
        <positiveSeqDataOnly>false</positiveSeqDataOnly>
        <hasShortCircuitData>true</hasShortCircuitData>
        <saturatedMachineParameter>false</saturatedMachineParameter>
	 */
	assertTrue(dynNet.isHasShortCircuitData());
	assertTrue(dynNet.isHasLoadflowData());
	assertTrue(!dynNet.isPositiveSeqDataOnly());
	
	/*
	 *  <dstabBus scCode="Contributing" id="Bus1" areaNumber="1" zoneNumber="1" number="1" offLine="false" name="BUS-1       ">
                <ownerList id="1">
                    <ownership unit="PU" value="1.0"/>
                </ownerList>
                <baseVoltage unit="KV" value="16.5"/>
                <voltage unit="PU" value="1.04"/>
                <angle unit="DEG" value="0.0"/>
                <genData>
                    <dstabEquivGen code="Swing">
                        <power unit="MVA" re="71.64" im="27.05"/>
                        <desiredVoltage unit="PU" value="1.04"/>
                        <desiredAngle unit="DEG" value="0.0"/>
                        <pLimit unit="MW" max="9999.0" min="-9999.0"/>
                        <potiveZ unit="PU" re="0.0" im="0.04"/>
                        <negativeZ unit="PU" re="0.0" im="0.04"/>
                        <zeroZ unit="PU" re="0.0" im="0.04"/>
                    </dstabEquivGen>
                    <dstabContributeGen id="1" offLine="false" name="Gen:1(1)">
                        <desc>PSSE Generator 1 at Bus 1</desc>
                        <power unit="MVA" re="71.64" im="27.05"/>
                        <desiredVoltage unit="PU" value="1.04"/>
                        <qLimit unit="MVAR" max="9999.0" min="-9999.0"/>
                        <pLimit unit="MW" max="9999.0" min="-9999.0"/>
                        <mvaBase unit="MVA" value="100.0"/>
                        <sourceZ unit="PU" re="0.0" im="0.04"/>
                        <mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
                        <potiveZ unit="PU" re="0.0" im="0.04"/>
                        <negativeZ unit="PU" re="0.0" im="0.04"/>
                        <zeroZ unit="PU" re="0.0" im="0.04"/>
                        <eq11MachModel>
                            <desc>GENSAL</desc>
                            <H>23.64</H>
                            <D>0.0</D>
                            <ra>0.0</ra>
                            <xd>0.146</xd>
                            <xq>0.0969</xq>
                            <xd1>0.0608</xd1>
                            <Td01 unit="Sec" value="8.96"/>
                            <seFmt1>
                              <se100>0.0</se100>
                              <se120>0.0</se120>
                              <sliner>1.0</sliner>
                            </seFmt1>
                            <xq11>0.04</xq11>
                            <Tq011 unit="Sec" value="0.06"/>
                            <xd11>0.04</xd11>
                            <Td011 unit="Sec" value="0.04"/>
                        </eq11MachModel>
                    </dstabContributeGen>
                </genData>
                <loadData>
                    <dstabEquivLoad code="NoneLoad"/>
                </loadData>
            </dstabBus>
	 */

	/*
	 * Bus 1
	 * Gen GENSAL-> Eq11Machine
	 */
	 DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
	 DStabGenDataXmlType defaultGen = DStabParserHelper.getDefaultGen(bus1.getGenData());
	 //DStabLoadDataXmlType defaultLoad = DStabParserHelper.getDefaultLoad(bus1.getLoadData());
	 
	 assertTrue(bus1.getGenData().getCode()==LFGenCodeEnumType.SWING);
	 //assertTrue(defaultLoad.getCode()==LFLoadCodeEnumType.NONE_LOAD);
	 
	assertTrue(defaultGen.getDesiredVoltage().getValue()==1.04);
	assertTrue(defaultGen.getPotiveZ().getIm()==0.04);
	
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

	@Test
    public void ieee9_ODM_Dstab_fullModel_test() throws ODMException{
    	final LogManager logMgr = LogManager.getLogManager();
    	Logger logger = Logger.getLogger("IEEE ODM Logger");
    	logger.setLevel(Level.INFO);
    	logMgr.addLogger(logger);
    	
    	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
    	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
    			"testdata/psse/v30/IEEE9Bus/ieee9.raw",
    			"testdata/psse/v30/IEEE9Bus/ieee9.seq",
    			"testdata/psse/v30/IEEE9Bus/ieee9_dyn_fullModel.dyr"
    	}));
    	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
    	//dstabParser.stdout();
    	//caseContentInfo
    	assertTrue(dstabParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.TRANSIENT_STABILITY);
    	
    	DStabNetXmlType dynNet =  dstabParser.getDStabNet();
    	
    	/*
    	 * <hasLoadflowData>true</hasLoadflowData>
            <positiveSeqDataOnly>false</positiveSeqDataOnly>
            <hasShortCircuitData>true</hasShortCircuitData>
            <saturatedMachineParameter>false</saturatedMachineParameter>
    	 */
    	assertTrue(dynNet.isHasShortCircuitData());
    	assertTrue(dynNet.isHasLoadflowData());
    	assertTrue(!dynNet.isPositiveSeqDataOnly());
    	
    	/*
    	 * Bus1
    	 * Gen: GENSAL
    	 * Exc: IEEET1, 1968 type 1 
    	 * Gov: IEEEG3, 1968 type 3 for for Hydro
    	 */
    	
    	DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
    	
    	DStabGenDataXmlType bus1Gen =  (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
    	
    	//Exciter
    	/*
    	 * <excIEEE1968Type1>
                            <desc>IEEET1</desc>
                            <TR unit="Sec" value="0.06"/>
                            <KA>20.0</KA>
                            <VRMAX>1.172</VRMAX>
                            <VRMIN>-0.0</VRMIN>
                            <KE>0.0</KE>
                            <TE unit="Sec" value="0.314"/>
                            <KF>0.063</KF>
                            <TF unit="Sec" value="0.35"/>
                            <EFDMIN>0.0</EFDMIN>
                            <E1>3.0</E1>
                            <SE1>0.104</SE1>
                            <E2>4.0</E2>
                            <SE2>0.293</SE2>
                            <TA unit="Sec" value="0.2"/>
                        </excIEEE1968Type1>
    	 */
    	ExcIEEE1968Type1XmlType bus1Exc = (ExcIEEE1968Type1XmlType) bus1Gen.getExciter().getValue();
    	
    	assertTrue(bus1Exc.getTR().getValue()==0.06);
    	assertTrue(bus1Exc.getKA()==20);
    	assertTrue(bus1Exc.getVRMAX()==1.172);
    	assertTrue(bus1Exc.getVRMIN()==-0.0);
    	assertTrue(bus1Exc.getKE()==0.0);
    	assertTrue(bus1Exc.getTE().getValue()==0.314);
    	assertTrue(bus1Exc.getKF()==0.063);
    	assertTrue(bus1Exc.getTF().getValue()==0.35);
    	assertTrue(bus1Exc.getKF()==0.063);
    	assertTrue(bus1Exc.getE1()==3.0);
    	assertTrue(bus1Exc.getSE1()==0.104);
    	assertTrue(bus1Exc.getE2()==4.0);
    	assertTrue(bus1Exc.getSE2()==0.293);
    	
    	//Tur-Gov
    	/*
    	 * <govIEEE1981Type3>
                            <TG unit="Sec" value="0.04"/>
                            <TP unit="Sec" value="1.0"/>
                            <VOpen>18.0</VOpen>
                            <VClose>-18.0</VClose>
                            <PMAX>180.0</PMAX>
                            <PMIN>0.0</PMIN>
                            <SIGMA>0.05</SIGMA>
                            <DELTA>0.31</DELTA>
                            <TR unit="Sec" value="5.0"/>
                            <TW unit="Sec" value="1.0"/>
                            <a11>0.5</a11>
                            <a13>1.0</a13>
                            <a21>1.5</a21>
                            <a23>1.0</a23>
                        </govIEEE1981Type3>
    	 */
    	GovIEEE1981Type3XmlType bus1Gov = (GovIEEE1981Type3XmlType) bus1Gen.getGovernor().getValue();
    	
    	assertTrue(bus1Gov.getTG().getValue()==0.04);
    	assertTrue(bus1Gov.getTP().getValue()==1.0);
    	assertTrue(bus1Gov.getVOpen()==18.0);
    	assertTrue(bus1Gov.getVClose()==-18.0);
    	assertTrue(bus1Gov.getPMAX()==180.0);
    	assertTrue(bus1Gov.getPMIN()==0.0);
    	assertTrue(bus1Gov.getSIGMA()==0.05);
    	assertTrue(bus1Gov.getDELTA()==0.31);
    	assertTrue(bus1Gov.getTR().getValue()==5.0);
    	assertTrue(bus1Gov.getTW().getValue()==1.0);
    	assertTrue(bus1Gov.getA11()==0.5);
    	assertTrue(bus1Gov.getA13()==1.0);
    	assertTrue(bus1Gov.getA21()==1.5);
    	assertTrue(bus1Gov.getA23()==1.0);
    	
    	/*
    	 * Bus2
    	 * Gen: GENSOU
    	 * Exc: IEEET1, 1968 type 1 
    	 * Gov: IEEEG1, 1968 type 1 for Thermal
    	 * 
    	 */
        DStabBusXmlType bus2 = dstabParser.getDStabBus("Bus2");
    	
    	DStabGenDataXmlType bus2Gen =  (DStabGenDataXmlType) bus2.getGenData().getContributeGen().get(0).getValue();
    	
    	/*
    	 *           <govIEEE1981Type1>
                            <K>20.833</K>
                            <T1 unit="Sec" value="0.0"/>
                            <T2 unit="Sec" value="0.0"/>
                            <T3 unit="Sec" value="0.1"/>
                            <VOpen>0.2</VOpen>
                            <VClose>-0.625</VClose>
                            <PMAX>1.0417</PMAX>
                            <PMIN>0.0</PMIN>
                            <T4 unit="Sec" value="0.25"/>
                            <K1>0.26</K1>
                            <K2>0.0</K2>
                            <T5 unit="Sec" value="7.5"/>
                            <K3>0.27</K3>
                            <K4>0.0</K4>
                            <T6 unit="Sec" value="0.4"/>
                            <K5>0.47</K5>
                            <K6>0.0</K6>
                            <T7 unit="Sec" value="0.0"/>
                            <K7>0.0</K7>
                            <K8>0.0</K8>
                        </govIEEE1981Type1>
    	 */
      	GovIEEE1981Type1XmlType bus2Gov = (GovIEEE1981Type1XmlType) bus2Gen.getGovernor().getValue();
      	
      	assertTrue(bus2Gov.getK()==20.833);
      	assertTrue(bus2Gov.getT1().getValue()==0.0);
      	assertTrue(bus2Gov.getT2().getValue()==0.0);
      	assertTrue(bus2Gov.getT3().getValue()==0.1);
      	assertTrue(bus2Gov.getVOpen()==0.2);
      	assertTrue(bus2Gov.getVClose()==-0.625);
      	assertTrue(bus2Gov.getPMAX()==1.0417);
      	assertTrue(bus2Gov.getPMIN()==0.0);
      	assertTrue(bus2Gov.getT4().getValue()==0.25);
      	assertTrue(bus2Gov.getK1()==0.26);
      	assertTrue(bus2Gov.getK2()==0.0);
      	assertTrue(bus2Gov.getT5().getValue()==7.5);
      	assertTrue(bus2Gov.getK3()==0.27);
      	assertTrue(bus2Gov.getK4()==0.0);
    	assertTrue(bus2Gov.getT6().getValue()==0.4);
      	assertTrue(bus2Gov.getK5()==0.47);
      	assertTrue(bus2Gov.getK6()==0.0);
      	assertTrue(bus2Gov.getK7()==0.0);
      	assertTrue(bus2Gov.getK8()==0.0);
    }
	
	@Test
    public void ieee9_ODM_Dstab_fullModel_Relay_test() throws ODMException{
    	final LogManager logMgr = LogManager.getLogManager();
    	Logger logger = Logger.getLogger("IEEE ODM Logger");
    	logger.setLevel(Level.INFO);
    	logMgr.addLogger(logger);
    	
    	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
    	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
    			"testdata/psse/v30/IEEE9Bus/ieee9.raw",
    			"testdata/psse/v30/IEEE9Bus/ieee9.seq",
    			"testdata/psse/v30/IEEE9Bus/ieee9_dyn_fullModel_relay.dyr"
    	}));
    	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
    	//dstabParser.stdout();
    	//caseContentInfo
    	assertTrue(dstabParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.TRANSIENT_STABILITY);
    	
    	DStabNetXmlType dynNet =  dstabParser.getDStabNet();
    	
    	/*
    	 * <hasLoadflowData>true</hasLoadflowData>
            <positiveSeqDataOnly>false</positiveSeqDataOnly>
            <hasShortCircuitData>true</hasShortCircuitData>
            <saturatedMachineParameter>false</saturatedMachineParameter>
    	 */
    	assertTrue(dynNet.isHasShortCircuitData());
    	assertTrue(dynNet.isHasLoadflowData());
    	assertTrue(!dynNet.isPositiveSeqDataOnly());
    	
    	/*
    	 * Bus1
    	 * Gen: GENSAL
    	 * Exc: IEEET1, 1968 type 1 
    	 * Gov: IEEEG3, 1968 type 3 for for Hydro
    	 */
    	
    	DStabBusXmlType bus5 = dstabParser.getDStabBus("Bus5");
    	
    	DStabLoadDataXmlType bus5Load =  (DStabLoadDataXmlType) bus5.getLoadData().getContributeLoad().get(0).getValue();
    	
    	LDS3RelayXmlType ufls = (LDS3RelayXmlType) bus5Load.getLoadRelayList().get(0);
    	
    	System.out.println(ufls.toString());
    	
    	assertTrue(ufls.getF1()==59.3);
    	
    	LVS3RelayXmlType uvls = (LVS3RelayXmlType) bus5Load.getLoadRelayList().get(1);
    	assertTrue(uvls.getF1()==0.913);
    	assertTrue(uvls.getT1()==6.0);
    	assertTrue(uvls.getF2()==0.913);
    	assertTrue(uvls.getT2()==10.0);
    	
    	
    	DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
    	
    	DStabGenDataXmlType bus1gen =  (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
    	
    	GenRelayFRQTPATXmlType freqRelay = (GenRelayFRQTPATXmlType) bus1gen.getGenRelayList().get(0);
    	assertTrue(freqRelay.getFL()==0.95000);
    	assertTrue(freqRelay.getFU()==5.000);
    	assertTrue(freqRelay.getTp()==10.000);
    	assertTrue(freqRelay.getTb()==0.000);
    	
      	GenRelayVTGTPATXmlType voltRelay = (GenRelayVTGTPATXmlType) bus1gen.getGenRelayList().get(1);
    	assertTrue(voltRelay.getVL()==0.75000);
    	assertTrue(voltRelay.getVU()==5.000);
    	assertTrue(voltRelay.getTp()==10.000);
    	assertTrue(voltRelay.getTb()==0.000);
	}
	
	@Test
    public void ieee9_ODM_Dstab_noSeqData_test() throws ODMException{
    	final LogManager logMgr = LogManager.getLogManager();
    	Logger logger = Logger.getLogger("IEEE ODM Logger");
    	logger.setLevel(Level.INFO);
    	logMgr.addLogger(logger);
    	
    	PSSEAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_30);
    	assertTrue(adapter.parseInputFile(NetType.DStabNet, new String[]{
    			"testdata/psse/v30/IEEE9Bus/ieee9.raw",
    			"testdata/psse/v30/IEEE9Bus/ieee9_dyn_fullModel.dyr"
    	}));
    	DStabModelParser dstabParser =(DStabModelParser) adapter.getModel();
    	//dstabParser.stdout();
    	//caseContentInfo
    	assertTrue(dstabParser.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.TRANSIENT_STABILITY);
    	
    	DStabNetXmlType dynNet =  dstabParser.getDStabNet();
    	
    	/*
    	 * <hasLoadflowData>true</hasLoadflowData>
            <positiveSeqDataOnly>false</positiveSeqDataOnly>
            <hasShortCircuitData>true</hasShortCircuitData>
            <saturatedMachineParameter>false</saturatedMachineParameter>
    	 */
    	assertTrue(!dynNet.isHasShortCircuitData());
    	assertTrue(dynNet.isHasLoadflowData());
    	assertTrue(dynNet.isPositiveSeqDataOnly());
    	
    	/*
    	 * Bus1
    	 * Gen: GENSAL
    	 * Exc: IEEET1, 1968 type 1 
    	 * Gov: IEEEG3, 1968 type 3 for for Hydro
    	 */
    	
    	DStabBusXmlType bus1 = dstabParser.getDStabBus("Bus1");
    	
    	DStabGenDataXmlType bus1Gen =  (DStabGenDataXmlType) bus1.getGenData().getContributeGen().get(0).getValue();
    	
    	//Exciter
    	/*
    	 * <excIEEE1968Type1>
                            <desc>IEEET1</desc>
                            <TR unit="Sec" value="0.06"/>
                            <KA>20.0</KA>
                            <VRMAX>1.172</VRMAX>
                            <VRMIN>-0.0</VRMIN>
                            <KE>0.0</KE>
                            <TE unit="Sec" value="0.314"/>
                            <KF>0.063</KF>
                            <TF unit="Sec" value="0.35"/>
                            <EFDMIN>0.0</EFDMIN>
                            <E1>3.0</E1>
                            <SE1>0.104</SE1>
                            <E2>4.0</E2>
                            <SE2>0.293</SE2>
                            <TA unit="Sec" value="0.2"/>
                        </excIEEE1968Type1>
    	 */
    	ExcIEEE1968Type1XmlType bus1Exc = (ExcIEEE1968Type1XmlType) bus1Gen.getExciter().getValue();
    	
    	assertTrue(bus1Exc.getTR().getValue()==0.06);
    	assertTrue(bus1Exc.getKA()==20);
    	assertTrue(bus1Exc.getVRMAX()==1.172);
    	assertTrue(bus1Exc.getVRMIN()==-0.0);
    	assertTrue(bus1Exc.getKE()==0.0);
    	assertTrue(bus1Exc.getTE().getValue()==0.314);
    	assertTrue(bus1Exc.getKF()==0.063);
    	assertTrue(bus1Exc.getTF().getValue()==0.35);
    	assertTrue(bus1Exc.getKF()==0.063);
    	assertTrue(bus1Exc.getE1()==3.0);
    	assertTrue(bus1Exc.getSE1()==0.104);
    	assertTrue(bus1Exc.getE2()==4.0);
    	assertTrue(bus1Exc.getSE2()==0.293);
    	
    	//Tur-Gov
    	/*
    	 * <govIEEE1981Type3>
                            <TG unit="Sec" value="0.04"/>
                            <TP unit="Sec" value="1.0"/>
                            <VOpen>18.0</VOpen>
                            <VClose>-18.0</VClose>
                            <PMAX>180.0</PMAX>
                            <PMIN>0.0</PMIN>
                            <SIGMA>0.05</SIGMA>
                            <DELTA>0.31</DELTA>
                            <TR unit="Sec" value="5.0"/>
                            <TW unit="Sec" value="1.0"/>
                            <a11>0.5</a11>
                            <a13>1.0</a13>
                            <a21>1.5</a21>
                            <a23>1.0</a23>
                        </govIEEE1981Type3>
    	 */
    	GovIEEE1981Type3XmlType bus1Gov = (GovIEEE1981Type3XmlType) bus1Gen.getGovernor().getValue();
    	
    	assertTrue(bus1Gov.getTG().getValue()==0.04);
    	assertTrue(bus1Gov.getTP().getValue()==1.0);
    	assertTrue(bus1Gov.getVOpen()==18.0);
    	assertTrue(bus1Gov.getVClose()==-18.0);
    	assertTrue(bus1Gov.getPMAX()==180.0);
    	assertTrue(bus1Gov.getPMIN()==0.0);
    	assertTrue(bus1Gov.getSIGMA()==0.05);
    	assertTrue(bus1Gov.getDELTA()==0.31);
    	assertTrue(bus1Gov.getTR().getValue()==5.0);
    	assertTrue(bus1Gov.getTW().getValue()==1.0);
    	assertTrue(bus1Gov.getA11()==0.5);
    	assertTrue(bus1Gov.getA13()==1.0);
    	assertTrue(bus1Gov.getA21()==1.5);
    	assertTrue(bus1Gov.getA23()==1.0);
    	
    	/*
    	 * Bus2
    	 * Gen: GENSOU
    	 * Exc: IEEET1, 1968 type 1 
    	 * Gov: IEEEG1, 1968 type 1 for Thermal
    	 * 
    	 */
        DStabBusXmlType bus2 = dstabParser.getDStabBus("Bus2");
    	
    	DStabGenDataXmlType bus2Gen =  (DStabGenDataXmlType) bus2.getGenData().getContributeGen().get(0).getValue();
    	
    	/*
    	 *           <govIEEE1981Type1>
                            <K>20.833</K>
                            <T1 unit="Sec" value="0.0"/>
                            <T2 unit="Sec" value="0.0"/>
                            <T3 unit="Sec" value="0.1"/>
                            <VOpen>0.2</VOpen>
                            <VClose>-0.625</VClose>
                            <PMAX>1.0417</PMAX>
                            <PMIN>0.0</PMIN>
                            <T4 unit="Sec" value="0.25"/>
                            <K1>0.26</K1>
                            <K2>0.0</K2>
                            <T5 unit="Sec" value="7.5"/>
                            <K3>0.27</K3>
                            <K4>0.0</K4>
                            <T6 unit="Sec" value="0.4"/>
                            <K5>0.47</K5>
                            <K6>0.0</K6>
                            <T7 unit="Sec" value="0.0"/>
                            <K7>0.0</K7>
                            <K8>0.0</K8>
                        </govIEEE1981Type1>
    	 */
      	GovIEEE1981Type1XmlType bus2Gov = (GovIEEE1981Type1XmlType) bus2Gen.getGovernor().getValue();
      	
      	assertTrue(bus2Gov.getK()==20.833);
      	assertTrue(bus2Gov.getT1().getValue()==0.0);
      	assertTrue(bus2Gov.getT2().getValue()==0.0);
      	assertTrue(bus2Gov.getT3().getValue()==0.1);
      	assertTrue(bus2Gov.getVOpen()==0.2);
      	assertTrue(bus2Gov.getVClose()==-0.625);
      	assertTrue(bus2Gov.getPMAX()==1.0417);
      	assertTrue(bus2Gov.getPMIN()==0.0);
      	assertTrue(bus2Gov.getT4().getValue()==0.25);
      	assertTrue(bus2Gov.getK1()==0.26);
      	assertTrue(bus2Gov.getK2()==0.0);
      	assertTrue(bus2Gov.getT5().getValue()==7.5);
      	assertTrue(bus2Gov.getK3()==0.27);
      	assertTrue(bus2Gov.getK4()==0.0);
    	assertTrue(bus2Gov.getT6().getValue()==0.4);
      	assertTrue(bus2Gov.getK5()==0.47);
      	assertTrue(bus2Gov.getK6()==0.0);
      	assertTrue(bus2Gov.getK7()==0.0);
      	assertTrue(bus2Gov.getK8()==0.0);
    }
}
