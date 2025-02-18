package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class PwdAdapterTest {
	/*
	 * 25/03/2012 now test to Xformer, since the following two parts seems to be 
	 * only supplement data, we can skip xformer and 3wxformer and continue
	 */
	double zError=1E-5;
	double vError=1E-3;
	@Test
	public void B5Rtest(){
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/B5R.aux"));
		AclfModelParser parser=(AclfModelParser) adapter.getModel();
		//parser.stdout();

		//check network data
		assertTrue(parser.getNet().getBasePower().getValue()==100.0);
		
		assertTrue(parser.getNet().getBusList().getBus().size()==6);
		assertTrue(parser.getNet().getBranchList().getBranch().size()==8);
		
//		 
		//ZONE
		/*
		 * DATA (ZONE, [ZoneNum,ZoneName,SchedName])
                         1 "1       " ""
		 */
		assertTrue(parser.getNet().getLossZoneList().getLossZone().size()==1);
		assertTrue(parser.getNet().getLossZoneList().getLossZone().get(0).getNumber()==1);
		
		//AREA
		/*
		 * DATA (AREA, [AreaNum,AreaName,BGAGC,BGAutoSS,BGAutoXF,EnforceGenMWLimits,SchedName,SAName,
         ConvergenceTol,AreaEDIncludeLossPF,BusSlack,AreaUnSpecifiedStudyMW])
        {
         1 "Home    " "   ED     " "YES" "YES" "YES" "" ""   1.0000 "YES" ""        0.000
         2 "2       " "   ED     " "YES" "YES" "YES" "" ""   1.0000 "YES" ""        0.000
        }
		 */
		assertTrue(parser.getNet().getAreaList().getArea().size()==2);
		assertTrue(parser.getNet().getAreaList().getArea().get(0).getNumber()==1);
		assertTrue(parser.getNet().getAreaList().getArea().get(0).getName().equals("Home"));//already trim
	
		
		
		//check bus data
		LoadflowBusXmlType bus1=(LoadflowBusXmlType) parser.getBus("Bus1");
		LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus1.getGenData());
		assertTrue(bus1.getGenData().getCode()==LFGenCodeEnumType.SWING);
		assertTrue(defaultGen.getDesiredVoltage().getValue()==1.0);
		
		assertTrue(Math.abs(defaultGen.getPower().getRe()-100.0056)<1E-3);
		assertTrue(Math.abs(defaultGen.getQLimit().getMax()-99998.999)<1E-3);
		assertTrue(Math.abs(defaultGen.getQLimit().getMin()-(-99998.999))<1E-3);
		assertTrue(Math.abs(defaultGen.getPLimit().getMax()-800.0)<1E-4);
		assertTrue(defaultGen.getPLimit().getMin()==0.0);
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus1.getLoadData());
		assertTrue(defaultLoad.getConstPLoad().getRe()==100.0);
		assertTrue(defaultLoad.getConstPLoad().getIm()==0.0);
		
		//check line data
		LineBranchXmlType line=(LineBranchXmlType) parser.getNet().getBranchList().getBranch().get(0).getValue();
		assertTrue(Math.abs(line.getZ().getRe()-0.03)<zError);
		assertTrue(Math.abs(line.getZ().getIm()-0.16)<zError);
		//check transformer data
		/*
		 * [BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG,LineAMVA,LineBMVA,
             LineCMVA,LineShuntMW,LineShuntMW:1,LineShuntMVR,LineShuntMVR:1,LineXfmr,LineTap,
             LinePhase,SeriesCapStatus]
		 *   4     5 " 1" "Closed"  0.000000  0.100000  0.000000  0.000000  1000.000  1000.000  1000.000     0.000     0.000     0.000     0.000  "YES"    0.993750   0.000000 "Not Bypassed"
		 */
		XfrBranchXmlType xfr=(XfrBranchXmlType) parser.getBranch("Bus4","Bus5","1");
		//TODO Both b and g are zero, so MagnitizingY are not set.
//		assertTrue(xfr.getMagnitizingY().getIm()==0.0);
//		assertTrue(xfr.getMagnitizingY().getRe()==0.0);
		assertTrue(Math.abs(xfr.getZ().getIm()-0.100000)<zError);
		assertTrue(Math.abs(xfr.getZ().getRe()-0.000000)<zError);
		assertTrue(Math.abs(xfr.getFromTurnRatio().getValue()-.993750)<zError);
		assertTrue(Math.abs(xfr.getToTurnRatio().getValue()-1.0)<zError);
		
	}
	
	@Test
	public void IEEE14Bustest(){
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/ieee14.aux"));
		AclfModelParser parser=(AclfModelParser) adapter.getModel();
		//parser.stdout();
		
		//check network data
		assertTrue(parser.getNet().getBasePower().getValue()==100.0);
		
		assertTrue(parser.getNet().getBusList().getBus().size()==18);
		assertTrue(parser.getNet().getBranchList().getBranch().size()==24);
		
		//check bus data
		LoadflowBusXmlType bus2=(LoadflowBusXmlType) parser.getBus("Bus2");
		assertTrue(bus2.getBaseVoltage().getValue()==132.00);
		assertTrue(bus2.isOffLine()==false);
		
		LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus2.getGenData());
		assertTrue(defaultGen.getDesiredVoltage().getValue()==1.045000);
		assertTrue(defaultGen.getPower().getRe() == 40.0);
		assertTrue(defaultGen.getQLimit().getMax() == 50.0);
		assertTrue(defaultGen.getQLimit().getMin() == -40.0);
		assertTrue(defaultGen.getPLimit().getMax() == 10000.0);
		assertTrue(defaultGen.getPLimit().getMin()==-10000.0);
		
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus2.getLoadData());
		assertTrue(defaultLoad.getConstPLoad().getRe()==21.700);
		assertTrue(defaultLoad.getConstPLoad().getIm()==12.700);
		
		//check line data
		/*BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG
		 * 15    2 " 1" "Closed"  0.019380  0.059170  0.052800  0.000000
		 */
		LineBranchXmlType line=(LineBranchXmlType) parser.getBranch("Bus15","Bus2","1");
		assertTrue(Math.abs(line.getZ().getRe()-0.019380)<zError);
		assertTrue(Math.abs(line.getZ().getIm()-0.059170)<zError);
		assertTrue(Math.abs(line.getTotalShuntY().getIm()-0.052800)<zError);
		assertTrue(Math.abs(line.getTotalShuntY().getRe()-0.0)<zError);
		//check transformer data
		/*
		 * [BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG]
		
            4     7 " 1" "Closed"  0.000000  0.209120  0.000000  0.000000     0.000     0.000     0.000     0.000     0.000     0.000     0.000     0.000    0.0000    0.0000    0.0000    0.0000 0.978000   0.000000 "Not Bypassed" "Closed" "Not Bypassed" "NO " "From" "" "Default" "YES" "No" "NO " "NO "   0.0000      1 100.0000   0.0000   0.0000 "Transformer" "" "NO" "NO" "NO" "NO" "NO " "NO " "NO " "NO " "NO " "NO "
            4     9 " 1" "Closed"  0.000000  0.556180  0.000000  0.000000     0.000     0.000     0.000     0.000     0.000     0.000     0.000     0.000    0.0000    0.0000    0.0000    0.0000 0.969000   0.000000 "Not Bypassed" "Closed" "Not Bypassed" "NO " "From" "" "Default" "YES" "No" "NO " "NO "   0.0000      1 100.0000   0.0000   0.0000 "Transformer" "" "NO" "NO" "NO" "NO" "NO " "NO " "NO " "NO " "NO " "NO "
            5     6 " 1" "Closed"  0.000000  0.252020  0.000000  0.000000     0.000     0.000     0.000     0.000     0.000     0.000     0.000     0.000    0.0000    0.0000    0.0000    0.0000 0.932000   0.000000 "Not Bypassed" "Closed" "Not Bypassed" "NO " "From" "" "Default" "YES" "No" "NO " "NO "   0.0000      1 100.0000   0.0000   0.0000 "Transformer" "" "NO" "NO" "NO" "NO" "NO " "NO " "NO " "NO " "NO " "NO "
		 */
		XfrBranchXmlType xfr=(XfrBranchXmlType) parser.getBranch("Bus4","Bus7","1");
		//TODO Both b and g are zero, so MagnitizingY are not set.
//		assertTrue(xfr.getMagnitizingY().getIm()==0.0);
//		assertTrue(xfr.getMagnitizingY().getRe()==0.0);
		assertTrue(xfr.getZ().getIm()==0.209120);
		assertTrue(xfr.getZ().getRe()==0.000000);
		assertTrue(xfr.getFromTurnRatio().getValue()==0.978000);
		assertTrue(xfr.getToTurnRatio().getValue()==1.0);
		
	}
}
