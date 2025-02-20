package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSEFactsDeviceDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEFactsDeviceDataRawMapper extends BasePSSEDataRawMapper {

    public PSSEFactsDeviceDataRawMapper(PsseVersion ver) {
        super(ver);
        this.dataParser = new PSSEFactsDeviceDataRawParser(ver);
    }

   
    public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
        this.dataParser.parseFields(lineStr);

        String name = dataParser.getValue("NAME");
        int i = dataParser.getInt("I");
        int j = dataParser.getInt("J");
        int mode = dataParser.getInt("MODE");
        double pdes = dataParser.getDouble("PDES", 0.0);
        double qdes = dataParser.getDouble("QDES", 0.0);
        double vset = dataParser.getDouble("VSET", 0.0);
        double shmx = dataParser.getDouble("SHMX", 0.0);
        double trmx = dataParser.getDouble("TRMX", 0.0);
        double vtmn = dataParser.getDouble("VTMN", 0.0);
        double vtmx = dataParser.getDouble("VTMX", 0.0);
        double vsmx = dataParser.getDouble("VSMX", 0.0);
        double imx = dataParser.getDouble("IMX", 0.0);
        double linx = dataParser.getDouble("LINX", 0.0);
        double rmpct = dataParser.getDouble("RMPCT", 0.0);
        int owner = dataParser.getInt("OWNER");
        double set1 = dataParser.getDouble("SET1", 0.0);
        double set2 = dataParser.getDouble("SET2", 0.0);
        int vsref = dataParser.getInt("VSREF");
        int fcreg = dataParser.getInt("FCREG");
        String mname = dataParser.getValue("MNAME");
        int nreg = dataParser.getInt("NREG");

//        FactsDeviceXmlType factsDevice = parser.createFactsDevice();
//        factsDevice.setName(name);
//        factsDevice.setI(i);
//        factsDevice.setJ(j);
//        factsDevice.setMode(mode);
//        factsDevice.setPdes(pdes);
//        factsDevice.setQdes(qdes);
//        factsDevice.setVset(vset);
//        factsDevice.setShmx(shmx);
//        factsDevice.setTrmx(trmx);
//        factsDevice.setVtmn(vtmn);
//        factsDevice.setVtmx(vtmx);
//        factsDevice.setVsmx(vsmx);
//        factsDevice.setImx(imx);
//        factsDevice.setLinx(linx);
//        factsDevice.setRmpct(rmpct);
//        factsDevice.setOwner(owner);
//        factsDevice.setSet1(set1);
//        factsDevice.setSet2(set2);
//        factsDevice.setVsref(vsref);
//        factsDevice.setFcreg(fcreg);
//        factsDevice.setMname(mname);
//        factsDevice.setNreg(nreg);
//        
//		final String fid = IODMModelParser.BusIdPreFix+i;
//		final String tid = IODMModelParser.BusIdPreFix+j;
//
//        LoadflowBusXmlType fromBus = parser.getBus(fid);
//        LoadflowBusXmlType toBus = parser.getBus(tid);

    }
}