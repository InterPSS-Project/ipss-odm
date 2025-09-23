package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSESwitchingDeviceDataRawParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSSESwitchingDeviceDataRawMapper extends BasePSSEDataRawMapper {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PSSESwitchingDeviceDataRawMapper.class.getName());
	
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
        int stat = dataParser.getInt("STAT",1);
        int nstat = dataParser.getInt("NSTAT",1);
        int met = dataParser.getInt("MET");
        int stype = dataParser.getInt("STYPE");
        String name = dataParser.getValue("NAME");
        
        if(PSSERawAdapter.getVersionNo(this.version) <36) {
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
        }
        else {
        	/*
        	 * @!   I,     J,'CKT',          X, 'RATING SET NAME'                       , STAT,NSTAT,  MET,STYPE,'NAME'
			   151,   201,'*1', 1.00000E-04, 'SWD_SF6_DOUBLEE_PUFFER                  ',    1,    1,    2,    3, 'SWD-DEVICE-REGION-X-SF6                 '
			   153,  3006,'@1', 1.00000E-04, 'SWD_OIL_BULK_PRESSURE                   ',    1,    1,    2,    2, 'SWD-DEVICE-REGION-Y-BULK OIL PRESSURE   '
        	 */
        	String ratingSetName = dataParser.getValue("RSETNAM");
        }

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

        // LoadflowBusXmlType fromBus = parser.getBus(fid);
        // LoadflowBusXmlType toBus = parser.getBus(tid);

         LineBranchXmlType braRecXml;
		  try {
			  braRecXml = (LineBranchXmlType) parser.createLineBranch(fid, tid, dataParser.getValue("CKT"));
		  } catch (ODMBranchDuplicationException e) {
			  log.error(e.toString());
			  return;
		  }		
		  
		  int status = dataParser.getInt("STAT", 1);
		  braRecXml.setOffLine(status != 1);
          // Set the X value for the branch
          AclfDataSetter.setLineData(braRecXml, 0, x, ZUnitType.PU, 0.0, 0, YUnitType.PU);
          

          //TODO: set rating limits if applicable

    }
}