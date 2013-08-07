package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.pwd.PowerWorldAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.junit.Test;

public class PWD_XfrControl_Test {

	
/*
 * DATA (TRANSFORMER, [BusNum,BusNum:1,LineCircuit,LineXFType,XFAuto,XFRegMin,XFRegMax,XFRegTargetType,
   XFTapMin,XFTapMax,XFStep,XFTableNum,XFRegBus,OPFAreaXFPS,LineXfmr,
   TimeDomainSelectedXF,XFOPFRegEnforceLimits,XFConfiguration,XFFixedTap,
   XFFixedTap:1,XFNominalKV,XFNominalKV:1,XFMVABase,UseDelay,UseSecRegRange,
   MoveDelay,MoveDelay:1,MoveDelay:2,MoveDelay:3,MoveDelay:4,MoveDelay:5,
   MoveDelay:6,MoveDelay:7,StepsToMove,StepsToMove:1,XFRegMin:1,XFRegMax:1])
{
    1     3 "1 " "LTC" "Yes"   0.93000   1.16000 "Middle of Reg Range"   
    0.87619   1.04781   0.00536      0     3 "NO " "YES" 
    "NO " "NO " "Wye - Wye"   1.00000   
    1.05000  19.40000 230.00000 1000.00000 "NO " 
    "NO "       30       30       15       15       15       15       
    10       10      1      1   0.51000   1.50000
    
                <tapAdjustment offLine="false">
                    <adjustmentType>Voltage</adjustmentType>
                    <tapLimit unit="PU" min="0.87619" max="1.04781"/>
                    <tapAdjStepSize>0.00536</tapAdjStepSize>
                    <tapAdjOnFromSide>true</tapAdjOnFromSide>
                    <voltageAdjData desiredValue="1.045" mode="ValueAdjustment">
                        <desiredVoltageUnit>PU</desiredVoltageUnit>
                        <adjVoltageBus xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="BusIDRefXmlType" idRef="Bus3"/>
                        <adjBusLocation>ToBus</adjBusLocation>
                    </voltageAdjData>
                </tapAdjustment>
                

    5     6 "T9" "Mvar" "Yes" -250.00000 -175.00000 "Middle of Reg Range"   
    -50   50   0.00682      0     0 "NO " "YES" 
    "NO " "NO " "Wye - Wye"   1.00000   
    1.00000 230.00000  19.40000 900.00000 "NO " 
    "NO "       30       30       15       15       15       15       
    10       10      1      1  51.00000 150.00000
    
                <angleAdjustment offLine="false" desiredValue="-212.5" mode="ValueAdjustment">
                    <angleLimit unit="DEG" min="-50.0" max="50.0"/>
                    <angleAdjOnFromSide>true</angleAdjOnFromSide>
                    <desiredActivePowerUnit>MW</desiredActivePowerUnit>
                    <desiredMeasuredOnFromSide>true</desiredMeasuredOnFromSide>
                </angleAdjustment>   
}
 */
	
	@Test
	public void SixBus_2WPsXfrtest(){
		IODMAdapter adapter = new PowerWorldAdapter();
		assertTrue(adapter.parseInputFile("testdata/pwd/SixBus_2WPsXfr.aux"));
		AclfModelParser parser=(AclfModelParser) adapter.getModel();
		parser.stdout();

		//check network data
		assertTrue(parser.getNet().getBusList().getBus().size()==5);
		
		//Xfr Bus1_to_Bus3_cirId_1
		XfrBranchXmlType xfr13=parser.getXfrBranch("Bus1", "Bus3", "1");
		TapAdjustmentXmlType tapAdj13=xfr13.getTapAdjustment();
		double err=0.001;
		//Tapmin=0.87619   tapMax=1.04781
		assertTrue(Math.abs(tapAdj13.getTapLimit().getMax()-1.04781)<err);
		assertTrue(Math.abs(tapAdj13.getTapLimit().getMin()-0.87619)<err);
		
		//step SIZE 0.00536
		assertTrue(Math.abs(tapAdj13.getTapAdjStepSize()-0.00536)<1.0E-5);
		
		assertTrue(tapAdj13.getAdjustmentType().equals(TapAdjustmentEnumType.VOLTAGE));
		VoltageAdjustmentDataXmlType vAdjData=tapAdj13.getVoltageAdjData();
		assertTrue(vAdjData.getMode()==AdjustmentModeEnumType.VALUE_ADJUSTMENT);
		
		assertTrue(Math.abs(vAdjData.getDesiredValue()-(0.93000+1.16000)/2)<err);
		//assertTrue(Math.abs(vAdjData.getRange().getMax()-1.16000)<err);
	}
}
