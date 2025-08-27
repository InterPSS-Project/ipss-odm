package org.ieee.odm.psse.raw.v35;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.VSCACControlModeEnumType;
import org.ieee.odm.schema.VSCConverterXmlType;
import org.ieee.odm.schema.VSCDCControlModeEnumType;
import org.ieee.odm.schema.VSCHVDC2TXmlType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV35_Kundur2Area_VSCHVDC_Test {
    
    @Test
    public void testCase1() throws Exception {
        final LogManager logMgr = LogManager.getLogManager();
        Logger logger = Logger.getLogger("IEEE ODM Logger");
        logger.setLevel(Level.INFO);
        logMgr.addLogger(logger);
        
        IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_35);
        assertTrue(adapter.parseInputFile("testdata/psse/v35/Kundur_2area_vschvdc_remotebus_v35.raw"));
    
        AclfModelParser parser = (AclfModelParser)adapter.getModel();

        parser.stdout();
        
        
        // Test HVDC lines
        /*
         <branch xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="VSCHVDC2TXmlType" circuitId="VDCLINE1" id="Bus7_to_Bus9_cirId_VDCLINE1" areaNumber="1" zoneNumber="1" offLine="false" name="VDCLINE1">
                <fromBus idRef="Bus7"/>
                <toBus idRef="Bus9"/>
                <Rdc r="0.71" unit="OHM"/>
                <rectifier>
                    <busId xsi:type="BusIDRefXmlType" idRef="Bus7"/>
                    <dcControlMode>RealPower</dcControlMode>
                    <acControlMode>PowerFactor</acControlMode>
                    <acSetPoint>0.95</acSetPoint>
                    <dcSetPoint>-209.0</dcSetPoint>
                    <ALoss>100.0</ALoss>
                    <BLoss>0.1</BLoss>
                    <MinLoss>50.0</MinLoss>
                    <MVARating unit="MVA" value="400.0"/>
                    <ACCurrentRating unit="AMP" value="1200.0"/>
                    <powerWeightFactor>0.1</powerWeightFactor>
                    <QMax unit="MVAR" value="100.0"/>
                    <QMin unit="MVAR" value="-110.0"/>
                    <remoteCtrlPercent>100.0</remoteCtrlPercent>
                </rectifier>
                <inverter>
                    <busId xsi:type="BusIDRefXmlType" idRef="Bus9"/>
                    <dcControlMode>DCVoltage</dcControlMode>
                    <acControlMode>Voltage</acControlMode>
                    <acSetPoint>0.99</acSetPoint>
                    <dcSetPoint>300.0</dcSetPoint>
                    <ALoss>90.0</ALoss>
                    <BLoss>0.15</BLoss>
                    <MinLoss>40.0</MinLoss>
                    <MVARating unit="MVA" value="350.0"/>
                    <ACCurrentRating unit="AMP" value="1200.0"/>
                    <powerWeightFactor>0.15</powerWeightFactor>
                    <QMax unit="MVAR" value="150.0"/>
                    <QMin unit="MVAR" value="-140.0"/>
                    <remoteCtrlBusId xsi:type="BusIDRefXmlType" idRef="Bus10"/>
                    <remoteCtrlPercent>100.0</remoteCtrlPercent>
                </inverter>
            </branch>

         */
        boolean foundHvdcLine = false;
        VSCHVDC2TXmlType hvdc = parser.getVSCHVDC2TRecord("Bus7", "Bus9", "VDCLINE1");
    
        assertNotNull("HVDC line should exist", hvdc);

        if (hvdc != null) {
            foundHvdcLine = true;
    
            // Test converter data - focus on the inverter data
            VSCConverterXmlType inverter = hvdc.getInverter();
            assertNotNull("Inverter data should exist", inverter);
            assertTrue("Inverter bus ID should match", "Bus9".equals(((BusXmlType)inverter.getBusId().getIdRef()).getId()));
            assertEquals("Inverter DC control mode should be DCVoltage", VSCDCControlModeEnumType.DC_VOLTAGE, inverter.getDcControlMode());
            assertEquals("Inverter AC control mode should be Voltage", VSCACControlModeEnumType.VOLTAGE, inverter.getAcControlMode());
            assertEquals("Inverter AC set point should be 0.99", 0.99, inverter.getAcSetPoint(), 0.01);
            assertEquals("Inverter DC set point should be 300.0", 300.0, inverter.getDcSetPoint(), 0.01);
            assertEquals("Inverter ALoss should be 90.0", 90.0, inverter.getALoss(), 0.01);
            assertEquals("Inverter BLoss should be 0.15", 0.15, inverter.getBLoss(), 0.01);
            assertEquals("Inverter MVARating should be 350.0", 350.0, inverter.getMVARating().getValue(), 0.01);
            assertEquals("Inverter QMax should be 150.0", 150.0, inverter.getQMax().getValue(), 0.01);
            assertEquals("Inverter QMin should be -140.0", -140.0, inverter.getQMin().getValue(), 0.01);
            assertTrue("Inverter remote control bus ID should match",  ((BusXmlType)inverter.getRemoteCtrlBusId().getIdRef()).getId().equals("Bus10"));
            assertEquals("Inverter remote control percent should be 100.0", 100.0, inverter.getRemoteCtrlPercent(), 0.01);

            // Test rectifier data
            VSCConverterXmlType rectifier = hvdc.getRectifier();
            assertNotNull("Rectifier data should exist", rectifier);
            assertTrue("Rectifier bus ID should match", "Bus7".equals(((BusXmlType)rectifier.getBusId().getIdRef()).getId()));
            assertEquals("Rectifier DC control mode should be RealPower", VSCDCControlModeEnumType.REAL_POWER, rectifier.getDcControlMode());
            assertEquals("Rectifier AC control mode should be PowerFactor", VSCACControlModeEnumType.POWER_FACTOR, rectifier.getAcControlMode());
            assertEquals("Rectifier AC set point should be 0.95", 0.95, rectifier.getAcSetPoint(), 0.01);
            assertEquals("Rectifier DC set point should be 209.0", 209.0, rectifier.getDcSetPoint(), 0.01);
            assertEquals("Rectifier ALoss should be 100.0", 100.0, rectifier.getALoss(), 0.01);
            assertEquals("Rectifier BLoss should be 0.1", 0.1, rectifier.getBLoss(), 0.01);
            assertEquals("Rectifier MVARating should be 400.0", 400.0, rectifier.getMVARating().getValue(), 0.01);
            assertEquals("Rectifier QMax should be 100.0", 100.0, rectifier.getQMax().getValue(), 0.01);
            assertEquals("Rectifier QMin should be -110.0", -110.0, rectifier.getQMin().getValue(), 0.01);
            assertEquals("Rectifier remote control percent should be 100.0", 100.0, rectifier.getRemoteCtrlPercent(), 0.01);
            //remoteCtrlBusId is not set for rectifier
            assertFalse("Rectifier remote control bus ID should not be set", rectifier.getRemoteCtrlBusId() != null);
            
        }

        
    }
}
