package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSESwitchingDeviceDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSESwitchingDeviceDataRawMapper extends BasePSSEDataRawMapper {

    public PSSESwitchingDeviceDataRawMapper(PsseVersion ver) {
        super(ver);
        this.dataParser = new PSSESwitchingDeviceDataRawParser(ver);
    }

  
    public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
        this.dataParser.parseFields(lineStr);

        int i = dataParser.getInt("I");
        int j = dataParser.getInt("J");
        String ckt = dataParser.getValue("CKT");
        double x = dataParser.getDouble("X", 0.0);
        double rate1 = dataParser.getDouble("RATE1", 0.0);
        double rate2 = dataParser.getDouble("RATE2", 0.0);
        double rate3 = dataParser.getDouble("RATE3", 0.0);
        double rate4 = dataParser.getDouble("RATE4", 0.0);
        double rate5 = dataParser.getDouble("RATE5", 0.0);
        double rate6 = dataParser.getDouble("RATE6", 0.0);
        double rate7 = dataParser.getDouble("RATE7", 0.0);
        double rate8 = dataParser.getDouble("RATE8", 0.0);
        double rate9 = dataParser.getDouble("RATE9", 0.0);
        double rate10 = dataParser.getDouble("RATE10", 0.0);
        double rate11 = dataParser.getDouble("RATE11", 0.0);
        double rate12 = dataParser.getDouble("RATE12", 0.0);
        int stat = dataParser.getInt("STAT");
        int nstat = dataParser.getInt("NSTAT");
        int met = dataParser.getInt("MET");
        int stype = dataParser.getInt("STYPE");
        String name = dataParser.getValue("NAME");

//        SwitchingDeviceXmlType switchingDevice = parser.createSwitchingDevice();
//        switchingDevice.setI(i);
//        switchingDevice.setJ(j);
//        switchingDevice.setCkt(ckt);
//        switchingDevice.setX(x);
//        switchingDevice.setRate1(rate1);
//        switchingDevice.setRate2(rate2);
//        switchingDevice.setRate3(rate3);
//        switchingDevice.setRate4(rate4);
//        switchingDevice.setRate5(rate5);
//        switchingDevice.setRate6(rate6);
//        switchingDevice.setRate7(rate7);
//        switchingDevice.setRate8(rate8);
//        switchingDevice.setRate9(rate9);
//        switchingDevice.setRate10(rate10);
//        switchingDevice.setRate11(rate11);
//        switchingDevice.setRate12(rate12);
//        switchingDevice.setStat(stat);
//        switchingDevice.setNstat(nstat);
//        switchingDevice.setMet(met);
//        switchingDevice.setStype(stype);
//        switchingDevice.setName(name);
        
		final String fid = IODMModelParser.BusIdPreFix+i;
		final String tid = IODMModelParser.BusIdPreFix+j;

        LoadflowBusXmlType fromBus = parser.getBus(fid);
        LoadflowBusXmlType toBus = parser.getBus(tid);
   
    }
}