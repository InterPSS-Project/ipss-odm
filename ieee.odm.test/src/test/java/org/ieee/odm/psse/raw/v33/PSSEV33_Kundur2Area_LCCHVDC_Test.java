package org.ieee.odm.psse.raw.v33;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.DCLineData2TXmlType;
import org.ieee.odm.schema.DcLineMeteredEndEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.ThyristorConverterXmlType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PSSEV33_Kundur2Area_LCCHVDC_Test {
    
    @Test
    public void testCase1() throws Exception {
        final LogManager logMgr = LogManager.getLogManager();
        Logger logger = Logger.getLogger("IEEE ODM Logger");
        logger.setLevel(Level.INFO);
        logMgr.addLogger(logger);
        
        IODMAdapter adapter = new PSSERawAdapter(PsseVersion.PSSE_33);
        assertTrue(adapter.parseInputFile("testdata/psse/v33/Kundur_2area_LCC_HVDC.raw"));
    
        AclfModelParser parser = (AclfModelParser)adapter.getModel();
        
        // Test Bus1 data
		/*
		 *                         <power unit="MVA" re="719.0" im="164.773"/>
                        <desiredVoltage unit="PU" value="1.03"/>
                        <qLimit unit="MVAR" max="500.0" min="-150.0"/>
                        <pLimit unit="MW" max="9999.0" min="-9999.0"/>
                        <mvaBase unit="MVA" value="900.0"/>
                        <sourceZ unit="PU" re="0.0025" im="0.3"/>
                        <mvarVControlParticipateFactor>1.0</mvarVControlParticipateFactor>
                    </contributeGen>
                </genData>
                <loadData/>
                <shuntYData/>
            </aclfBus>
		 */
        LoadflowBusXmlType bus1 = parser.getBus("Bus1");
        assertNotNull("Bus1 should exist", bus1);
        assertEquals("Bus1", bus1.getId());
        assertEquals(Integer.valueOf(1), bus1.getAreaNumber());
        assertEquals(Integer.valueOf(1), bus1.getZoneNumber());
        assertFalse(bus1.isOffLine());
        assertEquals("BUS1   AR1", bus1.getName());
        assertEquals(20.0, bus1.getBaseVoltage().getValue(), 0.0001);
        assertEquals(1.03, bus1.getVoltage().getValue(), 0.0001);
        assertEquals(69.4277, bus1.getAngle().getValue(), 0.0001);
        
        // Test generator data
        assertTrue(bus1.getGenData().getContributeGen().size() > 0);
        LoadflowGenDataXmlType gen1 = bus1.getGenData().getContributeGen().get(0).getValue();
        assertEquals("Gen:1(1)", gen1.getName());
        assertEquals("PSSE Generator 1 at Bus 1", gen1.getDesc());
        assertEquals(719.0, gen1.getPower().getRe(), 0.0001);
        assertEquals(164.773, gen1.getPower().getIm(), 0.0001);
        assertEquals(1.03, gen1.getDesiredVoltage().getValue(), 0.0001);
        assertEquals(500.0, gen1.getQLimit().getMax(), 0.0001);
        assertEquals(-150.0, gen1.getQLimit().getMin(), 0.0001);
        assertEquals(9999.0, gen1.getPLimit().getMax(), 0.0001);
        assertEquals(-9999.0, gen1.getPLimit().getMin(), 0.0001);
        assertEquals(900.0, gen1.getMvaBase().getValue(), 0.0001);
        assertEquals(0.0025, gen1.getSourceZ().getRe(), 0.0001);
        assertEquals(0.3, gen1.getSourceZ().getIm(), 0.0001);
        assertEquals(1.0, gen1.getMvarVControlParticipateFactor(), 0.0001);
        
        // Test HVDC lines
        /*
         * <aclf2THvdc circuitId="1" id="Bus7_to_Bus9_cirId_1" areaNumber="1" zoneNumber="1" offLine="false">
                <fromBus idRef="Bus7"/>
                <toBus idRef="Bus9"/>
                <controlMode>power</controlMode>
                <lineR r="5.0" unit="OHM"/>
                <powerDemand unit="MW" value="500.0"/>
                <controlOnRectifierSide>true</controlOnRectifierSide>
                <scheduledDCVoltage unit="KV" value="500.0"/>
                <meteredEnd>rectifier</meteredEnd>
                <modeSwitchDCVoltage unit="KV" value="0.0"/>
                <compoundingR r="0.0" unit="OHM"/>
                <powerOrCurrentMarginPU>0.0</powerOrCurrentMarginPU>
                <minDCVoltage unit="KV" value="0.0"/>
                <rectifier>
                    <busId xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="BusIDRefXmlType" idRef="Bus7"/>
                    <numberofBridges>2</numberofBridges>
                    <minFiringAngle unit="DEG" value="15.0"/>
                    <maxFiringAngle unit="DEG" value="90.0"/>
                    <acSideRatedVoltage unit="KV" value="230.0"/>
                    <commutatingZ unit="OHM" re="0.0" im="13.5939"/>
                    <commutatingCapacitor>0.0</commutatingCapacitor>
                    <xformerTurnRatio>0.9263</xformerTurnRatio>
                    <xformerTapSetting unit="PU" value="0.9375"/>
                    <xformerTapLimit unit="PU" max="1.2" min="0.8"/>
                    <xformerTapStepSize>0.00625</xformerTapStepSize>
                </rectifier>
                <inverter>
                    <busId xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="BusIDRefXmlType" idRef="Bus9"/>
                    <numberofBridges>2</numberofBridges>
                    <minFiringAngle unit="DEG" value="15.0"/>
                    <maxFiringAngle unit="DEG" value="90.0"/>
                    <acSideRatedVoltage unit="KV" value="230.0"/>
                    <commutatingZ unit="OHM" re="0.0" im="13.3174"/>
                    <commutatingCapacitor>0.0</commutatingCapacitor>
                    <xformerTurnRatio>0.9097</xformerTurnRatio>
                    <xformerTapSetting unit="PU" value="0.94375"/>
                    <xformerTapLimit unit="PU" max="1.2" min="0.8"/>
                    <xformerTapStepSize>0.00625</xformerTapStepSize>
                </inverter>
            </aclf2THvdc>
         */
        boolean foundHvdcLine = false;
       DCLineData2TXmlType hvdc = parser.getDcLine2TRecord("Bus7", "Bus9", "1");
	   
            if (hvdc !=null) {
                foundHvdcLine = true;
     
                // Test converter data - focus on the inverter data
                ThyristorConverterXmlType inverter = hvdc.getInverter();
                
                // Test the inverter attributes
                assertEquals(90.0, inverter.getMaxFiringAngle().getValue(), 0.0001);
                assertEquals(230.0, inverter.getAcSideRatedVoltage().getValue(), 0.0001);
                assertEquals(0.0, inverter.getCommutatingZ().getRe(), 0.0001);
                assertEquals(13.3174, inverter.getCommutatingZ().getIm(), 0.0001);
                assertEquals(0.0, inverter.getCommutatingCapacitor(), 0.0001);
                assertEquals(0.9097, inverter.getXformerTurnRatio(), 0.0001);
                assertEquals(0.94375, inverter.getXformerTapSetting().getValue(), 0.0001);
                assertEquals(1.2, inverter.getXformerTapLimit().getMax(), 0.0001);
                assertEquals(0.8, inverter.getXformerTapLimit().getMin(), 0.0001);
                assertEquals(0.00625, inverter.getXformerTapStepSize(), 0.0001);
                assertEquals(15.0, inverter.getMinFiringAngle().getValue(), 0.0001);
                
                // Test rectifier data
                ThyristorConverterXmlType rectifier = hvdc.getRectifier();
                assertNotNull("Rectifier should exist", rectifier);
                assertEquals(15.0, rectifier.getMinFiringAngle().getValue(), 0.0001);
                assertEquals(90.0, rectifier.getMaxFiringAngle().getValue(), 0.0001);
                
                // Test line data
                assertEquals(5.0, hvdc.getLineR().getR(), 0.0001);
                assertEquals(500.0, hvdc.getPowerDemand().getValue(), 0.0001);
                assertEquals(500.0, hvdc.getScheduledDCVoltage().getValue(), 0.0001);
                assertEquals(DcLineMeteredEndEnumType.RECTIFIER, hvdc.getMeteredEnd());
            
        }

        parser.stdout();
    }
}
