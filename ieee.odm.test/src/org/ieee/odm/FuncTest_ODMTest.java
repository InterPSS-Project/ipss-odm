package org.ieee.odm;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class FuncTest_ODMTest {
	static String PSSStudyCaseHead = "<pssStudyCase xmlns=\"http://www.ieee.org/odm/Schema/2008\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
	static String PSSStudyCaseEnd = "</pssStudyCase>";
	
	static String BusListHead = PSSStudyCaseHead + "<baseCase xsi:type=\"LoadflowNetXmlType\"><busList>";
	static String BusListEnd = "</busList></baseCase>" + PSSStudyCaseEnd; 

	static String BranchListHead = PSSStudyCaseHead + "<baseCase xsi:type=\"LoadflowNetXmlType\"><branchList>";
	static String BranchListEnd = "</branchList></baseCase>" + PSSStudyCaseEnd; 

	@Test
	public void castingTestCase() throws Exception {
		String str = BranchListHead +
		      "<aclfXfr areaNumber=\"1\" zoneNumber=\"1\" circuitId=\"1\" id=\"Bus4_to_Bus7_cirId_1\">" +
		      "  <fromBus idRef=\"Bus4\"/>" +
		      "  <toBus idRef=\"Bus7\"/>" +
		      "    <z re=\"0.0\" im=\"0.20912\" unit=\"PU\"/>" +
		      "    <fromTurnRatio value=\"0.978\" unit=\"PU\"/ssss>" +
		      "    <toTurnRatio value=\"1.0\" unit=\"PU\"/>" +
		      "    <xfrInfo>" +
		      "      <fromRatedVoltage value=\"132.0\" unit=\"KV\"/>" +
		      "      <toRatedVoltage value=\"35.0\" unit=\"KV\"/>" +
		      "    </xfrInfo>" +
		      "</aclfXfr>" + 
		      BranchListEnd;		
		
		AclfModelParser parser = ODMObjectFactory.createAclfModelParser();
		if (parser.parse(str)) {
			XfrBranchXmlType xfr = (XfrBranchXmlType)parser.getNet().getBranchList().getBranch().get(0).getValue();
			
			PSXfrBranchXmlType psXfr = (PSXfrBranchXmlType)ModelStringUtil.casting(
							xfr, "aclfXfr", "aclfPSXfr", parser.getEncoding());
			assertTrue(psXfr.getId() != null);
			assertTrue(psXfr.getFromBus() != null);
			assertTrue(psXfr.getZ().getIm() == .20912);
		}
	}
}
