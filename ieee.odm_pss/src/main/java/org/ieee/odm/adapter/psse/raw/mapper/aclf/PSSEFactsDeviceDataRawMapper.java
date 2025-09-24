package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSEFactsDeviceDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.FACTSDeviceXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SVCControlModeEnumType;
import org.ieee.odm.schema.StaticVarCompensatorXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSEFactsDeviceDataRawMapper extends BasePSSEDataRawMapper {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSEFactsDeviceDataRawMapper.class);

    public PSSEFactsDeviceDataRawMapper(PsseVersion ver) {
        super(ver);
        this.dataParser = new PSSEFactsDeviceDataRawParser(ver);
    }

   
    public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
        this.dataParser.parseFields(lineStr);

        String numOrName = (PSSERawAdapter.getVersionNo(this.version) < 31) ? String.valueOf(dataParser.getInt("N")) : dataParser.getValue("NAME");
        int i = dataParser.getInt("I");
        int j = dataParser.getInt("J",0);
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
       
       

        FACTSDeviceXmlType facts = null;

        final String fid = IODMModelParser.BusIdPreFix+i;
        final String tid = j!=0? IODMModelParser.BusIdPreFix+j: "0"; 
        
        
        if(j!=0){
            log.error("FACTS device with a toBus (series component) is not supported yet: " + lineStr);
        }
        else{ // It is a static var compensator (SVC) or similar device without a toBus.

            //TODO: Handle the case when j is 0, which means no to bus is specified.
            // since FACTS device is a branch, it should have a toBus. But we can also process it as a Static Var Compensator (SVC) or similar device when tid is not specified.
            LoadflowBusXmlType aclfBus = (LoadflowBusXmlType) parser.getBus(fid);

            if (aclfBus == null) {
                throw new ODMException("Error: Bus not found in the network, bus number: " + fid);
            }
            
            StaticVarCompensatorXmlType svc = OdmObjFactory.createStaticVarCompensatorXmlType();
            svc.setName(numOrName);
            svc.setOffLine(mode == 0); // mode 0 means out of service
            //Note: shmx is the SVC rating in MVAR, it can be either inductive or capacitive
            svc.setCapacitiveRating(BaseDataSetter.createReactivePowerValue(shmx, ReactivePowerUnitType.MVAR));
            svc.setInductiveRating(BaseDataSetter.createReactivePowerValue(shmx, ReactivePowerUnitType.MVAR));

            svc.setControlMode(SVCControlModeEnumType.VOLTAGE);
            svc.setVoltageSetPoint(BaseDataSetter.createVoltageValue(vset, VoltageUnitType.PU));
        
            svc.setRemoteControlledPercentage(rmpct);
            svc.setOwner(owner);

            if(PSSERawAdapter.getVersionNo(this.version) >= 31) {
                int fcreg = dataParser.getInt("FCREG");
                if(fcreg > 0) {
                    // the remote bus is specified, we can set the remote bus id
                    svc.setRemoteControlledBus(parser.createBusRef(IODMModelParser.BusIdPreFix+fcreg));
                    
                }
            }

            if(PSSERawAdapter.getVersionNo(this.version) >33){
                 int nreg = dataParser.getInt("NREG");
                 svc.setRemoteControlledNodeNum(nreg);
            }
            aclfBus.setSvc(svc);

        }


    }
}