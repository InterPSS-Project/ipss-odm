package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.adapter.pwd.impl.BranchDataProcessor;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NameValuePairXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class CustomStringTest {
	String STATION_TOKEN ="SubStation";
	@Test
	public void SixBusBase_CustromString_test(){
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/SixBusTestCase.aux"));
		AclfModelParser parser=(AclfModelParser) adapter.getModel();
		//parser.stdout();
		//LOAD
		/*
		 * DATA (LOAD, [CustomString,BusNum,LoadID,LoadStatus,LoadSMW,LoadSMVR,CustomString:1,
            CustomSingle,CustomSingle:1,GenAGCAble])
           {
            "Sub2_230_LOAD"     2 "1 " "Closed"   100.000000    50.000000 "LDAREA_1" "" "" "YES"
		 */
		LoadflowBusXmlType bus2=parser.getBus("Bus2");
		assertTrue(bus2.getNvPair().get(0).getName().equals("Load_CustomString"));
		assertTrue(bus2.getNvPair().get(0).getValue().equals("Sub2_230_LOAD"));
        //Gen custom String
		/*
		 * DATA (GEN, [CustomString,CustomString:1,CustomString:2,BusNum,GenID,GenStatus,GenMW,GenMVR,
            GenVoltSet,GenAGCAble,GenAVRAble,GenMWMin,GenMWMax,GenMVRMin,GenMVRMax,
            GenUseCapCurve,GenRegNum,GenParFac,GenRMPCT,CustomSingle,GenOPFFastStart,
            CustomSingle:1,CustomSingle:2,CustomSingle:3,CustomSingle:4])
            {
             "Sub1_14.9_G1" "G1" "DZONE_1"    11 "1"
		 */
		LoadflowBusXmlType g1=parser.getBus("Bus11");
		assertTrue(g1.getNvPair().get(1).getName().equals("Gen_CustomString"));
		assertTrue(g1.getNvPair().get(1).getValue().equals("Sub1_14.9_G1"));
		assertTrue(g1.getNvPair().get(0).getName().equals(STATION_TOKEN));
		assertTrue(g1.getNvPair().get(0).getValue().equals("Sub1"));
		
		assertTrue(g1.getNvPair().get(2).getName().equals("Gen_CustomString:1"));
		assertTrue(g1.getNvPair().get(2).getValue().equals("G1"));
		
		assertTrue(g1.getNvPair().get(3).getName().equals("Gen_CustomString:2"));
		assertTrue(g1.getNvPair().get(3).getValue().equals("DZONE_1"));
		/*
		for(NameValuePairXmlType nv: g1.getNvPair()){
			System.out.println ("nv pair--name, value = " +nv.getName()+"  , "+nv.getValue());
		}
		*/
		
		//Branch custom string
		/*
		 * DATA (BRANCH, [CustomString,CustomString:1,CustomString:2,BusNum,BusNum:1,LineCircuit,
            {
            "Line" "Sub2_230_L25" "L25"    25    52 "1"
		 */
		//Xfr Bus25_to_Bus52_cirId_1
		/*
		 * <nvPair>
                    <name>Station</name>ZXZ
                    <value>Sub2</value>
                </nvPair>
                <nvPair>
                    <name>EquimentName</name>
                    <value>L25</value>
                </nvPair>
		 */
		LineBranchXmlType L25=parser.getLineBranch("Bus25", "Bus52", "1");
		assertTrue(L25.getNvPair().get(0).getName().equals(STATION_TOKEN));
		assertTrue(L25.getNvPair().get(0).getValue().equals("Sub2"));
		assertTrue(L25.getNvPair().get(1).getName().equals("EquimentName"));
		assertTrue(L25.getNvPair().get(1).getValue().equals("L25"));
		assertTrue(L25.getNvPair().get(2).getName().equals("CustomString"));
		assertTrue(L25.getNvPair().get(2).getValue().equals("Line"));
		assertTrue(L25.getNvPair().get(3).getName().equals("CustomString:1"));
		assertTrue(L25.getNvPair().get(3).getValue().equals("Sub2_230_L25"));
		
		//breaker
		/*
		 * DATA (BRANCH, [CustomString,CustomString:1,CustomString:2,BusNum,BusNum:1,LineCircuit,LineXfmr,
           LineR,LineX,LineC,LineAMVA,LineAMVA:1,LineAMVA:2,LineMonEle,LSName,LineStatus,
           NormLineStatus])
           {
             "Breaker" "Sub1_19.4_1A" "1A"    11     1 "1"
		 * 
		 */
		LineBranchXmlType brk=parser.getLineBranch("Bus11", "Bus1", "1");
		assertTrue(brk.getNvPair().get(0).getName().equals(STATION_TOKEN));
		assertTrue(brk.getNvPair().get(0).getValue().equals("Sub1"));
		assertTrue(brk.getNvPair().get(1).getName().equals("EquimentName"));
		assertTrue(brk.getNvPair().get(1).getValue().equals("1A"));
		assertTrue(brk.getNvPair().get(2).getName().equals("CustomString"));
		assertTrue(brk.getNvPair().get(2).getValue().equals("Breaker"));
		assertTrue(brk.getNvPair().get(3).getName().equals("CustomString:1"));
		assertTrue(brk.getNvPair().get(3).getValue().equals("Sub1_19.4_1A"));
		
		//transformer
		/*
		 * DATA (BRANCH, [CustomString,CustomString:1,CustomString:2
            {
            "Transformer" "Sub1_19.4_T12" "T12" 12    21 "T1"
		 */
		
		XfrBranchXmlType xfr=parser.getXfrBranch("Bus12", "Bus21", "T1");
		assertTrue(xfr.getNvPair().get(0).getName().equals("CustomString"));
		assertTrue(xfr.getNvPair().get(0).getValue().equals("Transformer"));
		
		assertTrue(xfr.getNvPair().get(1).getName().equals("CustomString:1"));
		assertTrue(xfr.getNvPair().get(1).getValue().equals("Sub1_19.4_T12"));
		
		assertTrue(xfr.getNvPair().get(2).getName().equals(STATION_TOKEN));
		assertTrue(xfr.getNvPair().get(2).getValue().equals("Sub1"));
		
		assertTrue(xfr.getNvPair().get(3).getName().equals("EquimentName"));
		assertTrue(xfr.getNvPair().get(3).getValue().equals("T12"));
		

		
	}
	
	@Test
	public void testSubstationName(){
		 String extName="S_RANDOL_115_447-508-1",
				 equipmentName="447-508-1";
		 assertTrue(BranchDataProcessor.getSubstationName(extName, equipmentName).equals("S_RANDOL"));
		 
		 String extName2="KEENE_RD_115_T1L-2E",
		 equipmentName2="T1L-2E";
		 assertTrue(BranchDataProcessor.getSubstationName(extName2, equipmentName2).equals("KEENE_RD"));
	}
}
