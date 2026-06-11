package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSEFactsDeviceDataRawParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.BusRefXmlType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.FACTSDeviceXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OwnerXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SVCControlModeEnumType;
import org.ieee.odm.schema.StaticVarCompensatorXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;
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
       
       

        final String fid = IODMModelParser.BusIdPreFix+i;
        final String tid = j!=0? IODMModelParser.BusIdPreFix+j: "0"; 
        
        
        if(j!=0){
            FACTSDeviceXmlType facts;
            try {
                facts = parser.createFACTSDevice(numOrName, fid, tid);
            } catch (ODMBranchDuplicationException e) {
                log.error(e.toString());
                return;
            }
            facts.setOffLine(mode == 0);
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMode(mode));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeDesiredActivePower(
                    BaseDataSetter.createActivePowerValue(pdes, ActivePowerUnitType.MW)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeDesiredReactivePower(
                    BaseDataSetter.createReactivePowerValue(qdes, ReactivePowerUnitType.MVAR)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeVoltageSetPoint(
                    BaseDataSetter.createVoltageValue(vset, VoltageUnitType.PU)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMaxShunt(
                    BaseDataSetter.createYValue(0.0, shmx, YUnitType.MVAR)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMaxBridgeActivePower(
                    BaseDataSetter.createActivePowerValue(trmx, ActivePowerUnitType.MW)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMinTerminalBusVoltage(
                    BaseDataSetter.createVoltageValue(vtmn, VoltageUnitType.PU)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMaxSeriesVoltage(
                    BaseDataSetter.createVoltageValue(vsmx, VoltageUnitType.PU)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeMaxSeriesCurrent(
                    BaseDataSetter.createCurrentValue(imx, CurrentUnitType.PU)));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeRemoteControlPercent(rmpct));
            OwnerXmlType ownerRec = OdmObjFactory.createOwnerXmlType();
            ownerRec.setId(Integer.toString(owner));
            ownerRec.setNumber(owner);
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeOwner(ownerRec));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeSet1(set1));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeSet2(set2));
            facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeSeriesVoltageRefCode(vsref));

            BaseJaxbHelper.addNVPair(facts, "LINX", Double.toString(linx));
            if(PSSERawAdapter.getVersionNo(this.version) >= 31) {
                int fcreg = dataParser.getInt("FCREG");
                String regBusId = IODMModelParser.BusIdPreFix+fcreg;
                if(fcreg > 0 && !regBusId.equals(fid)) {
                    BusRefXmlType regulatedBus = OdmObjFactory.createBusRefXmlType();
                    regulatedBus.setBusId(regBusId);
                    facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeRegulatedBus(
                            regulatedBus));
                }
                String mname = dataParser.getValue("MNAME");
                if(mname != null && mname.trim().length() > 0) {
                    BaseJaxbHelper.addNVPair(facts, "MNAME", mname);
                }
            }

            if(PSSERawAdapter.getVersionNo(this.version) >33){
                 int nreg = dataParser.getInt("NREG");
                 facts.getRest().add(OdmObjFactory.createFACTSDeviceXmlTypeRegulatedBusNodeNum(nreg));
            }
        }
        else{ // It is a static var compensator (SVC) or similar device without a toBus.
            mapShuntCompensator(numOrName, fid, mode, shmx, vset, rmpct, owner, parser);
        }


    }

    private void mapShuntCompensator(String numOrName, String fid, int mode, double shmx, double vset,
            double rmpct, int owner, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
        LoadflowBusXmlType aclfBus = (LoadflowBusXmlType) parser.getBus(fid);

        if (aclfBus == null) {
            throw new ODMException("Error: Bus not found in the network, bus number: " + fid);
        }

        StaticVarCompensatorXmlType svc = OdmObjFactory.createStaticVarCompensatorXmlType();
        svc.setName(numOrName);
        svc.setOffLine(mode == 0);
        // Note: shmx is the SVC rating in MVAR, it can be either inductive or capacitive.
        svc.setCapacitiveRating(BaseDataSetter.createReactivePowerValue(shmx, ReactivePowerUnitType.MVAR));
        svc.setInductiveRating(BaseDataSetter.createReactivePowerValue(shmx, ReactivePowerUnitType.MVAR));

        svc.setControlMode(SVCControlModeEnumType.VOLTAGE);
        svc.setVoltageSetPoint(BaseDataSetter.createVoltageValue(vset, VoltageUnitType.PU));

        svc.setRemoteControlledPercentage(rmpct);
        svc.setOwner(owner);

        if(PSSERawAdapter.getVersionNo(this.version) >= 31) {
            int fcreg = dataParser.getInt("FCREG");
            String regBusId = IODMModelParser.BusIdPreFix+fcreg;
            if(fcreg > 0 && !regBusId.equals(fid)) {
                svc.setRemoteControlledBus(parser.createBusRef(regBusId));
            }
        }

        if(PSSERawAdapter.getVersionNo(this.version) >33){
             int nreg = dataParser.getInt("NREG");
             svc.setRemoteControlledNodeNum(nreg);
        }
        aclfBus.setSvc(svc);
    }
}
