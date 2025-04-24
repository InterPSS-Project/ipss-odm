package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSEVSCHVDC2TDataRawParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.VSCACControlModeEnumType;
import org.ieee.odm.schema.VSCConverterXmlType;
import org.ieee.odm.schema.VSCDCControlModeEnumType;
import org.ieee.odm.schema.VSCHVDC2TXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEVSCHVDC2TDataRawMapper extends BasePSSEDataRawMapper{
	
	public PSSEVSCHVDC2TDataRawMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEVSCHVDC2TDataRawParser(ver);
	}
	
	public void procLineString(String[] lineStrAry, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException, ODMBranchDuplicationException {
		dataParser.parseFields(lineStrAry);
		
		String name = this.dataParser.getValue("NAME");
		
		//status , 0 = out-of-service, 1 = in-service
		int MDC = this.dataParser.getInt("MDC"); 
		    
		double RDC = this.dataParser.getDouble("RDC"); 
		
		
		int vsc1BusNum = this.dataParser.getInt("IBUS"); 
		int vsc1DCControlType = this.dataParser.getInt("TYPE1"); 
		int vsc1ACControlMode = this.dataParser.getInt("MODE1"); 
		
		double  vsc1DCSet = this.dataParser.getDouble("DCSET1"); 
		double  vsc1ACSet = this.dataParser.getDouble("ACSET1"); 
		
		double  vsc1ALoss = this.dataParser.getDouble("ALOSS1"); 
		double  vsc1BLoss = this.dataParser.getDouble("BLOSS1"); 
		double  vsc1MinLoss = this.dataParser.getDouble("MINLOSS1"); 
		double  vsc1MVARating = this.dataParser.getDouble("SMAX1"); 
		double  vsc1CurRating = this.dataParser.getDouble("IMAX1"); 
		double  vsc1PWF = this.dataParser.getDouble("PWF1");
		double  vsc1QMAX = this.dataParser.getDouble("MAXQ1");
		double  vsc1QMIN = this.dataParser.getDouble("MINQ1");
		int    vsc1RmtBusNum = 0;
		double  vsc1Rmpct = this.dataParser.getDouble("RMPCT1");
		if (PSSERawAdapter.getVersionNo(this.version) <34){
			vsc1RmtBusNum = this.dataParser.getInt("REMOT1");
		}
		else if (PSSERawAdapter.getVersionNo(this.version)>=34 && PSSERawAdapter.getVersionNo(this.version)<37){
			vsc1RmtBusNum = this.dataParser.getInt("VSREG1");
		}
		else{
			throw new UnsupportedOperationException("The version "+this.version+" is not supported yet for VSC HVDC converter data parsing");
		}

		
		
		
		int vsc2BusNum = this.dataParser.getInt("JBUS"); 
		int vsc2DCControlType = this.dataParser.getInt("TYPE2"); 
		int vsc2ACControlMode = this.dataParser.getInt("MODE2"); 
		
		double  vsc2DCSet = this.dataParser.getDouble("DCSET2"); 
		double  vsc2ACSet = this.dataParser.getDouble("ACSET2"); 
		
		double  vsc2ALoss = this.dataParser.getDouble("ALOSS2"); 
		double  vsc2BLoss = this.dataParser.getDouble("BLOSS2"); 
		double  vsc2MinLoss = this.dataParser.getDouble("MINLOSS2"); 
		double  vsc2MVARating = this.dataParser.getDouble("SMAX2"); 
		double  vsc2CurRating = this.dataParser.getDouble("IMAX2"); 
		double  vsc2PWF = this.dataParser.getDouble("PWF2");
		double  vsc2QMAX = this.dataParser.getDouble("MAXQ2");
		double  vsc2QMIN = this.dataParser.getDouble("MINQ2");
		int    vsc2RmtBusNum =  0;
		if (PSSERawAdapter.getVersionNo(this.version) < 34) {
			vsc2RmtBusNum = this.dataParser.getInt("REMOT2");
		}
		else if (PSSERawAdapter.getVersionNo(this.version) >= 34 && PSSERawAdapter.getVersionNo(this.version) < 37) {
			vsc2RmtBusNum = this.dataParser.getInt("VSREG2");
		}
		else {
			throw new UnsupportedOperationException("The version "+this.version+" is not supported yet for VSC HVDC converter data parsing");
		}
		double  vsc2Rmpct = this.dataParser.getDouble("RMPCT2");
		
		
		
		
		final String vsc1BusId = IODMModelParser.BusIdPreFix+vsc1BusNum;
		final String vsc2BusId = IODMModelParser.BusIdPreFix+vsc2BusNum;
		
		//check the  rectifer and inverter
		boolean isVSC1Rec = false;
		
		// constant MW control and the direction of power into the network  
		// (positive if power flowing from vsc into the AC network; otherwise, negative)
		
		//  the following checks if vsc1 is rectifier  or vsc2 is inverter
		if((vsc1DCControlType ==2 && vsc1DCSet <0) || (vsc2DCControlType ==2 && vsc2DCSet >0))
			isVSC1Rec =true;
		
		String recId = isVSC1Rec? vsc1BusId:vsc2BusId;
		String invId = isVSC1Rec? vsc2BusId:vsc1BusId;
		
		VSCHVDC2TXmlType vscHVDC2T = parser.createVSCHVDC2TRecord(recId, invId, name);
		vscHVDC2T.setName(name);
		
		// set status
		vscHVDC2T.setOffLine(MDC==0?true:false);
		
		//Rdc
		vscHVDC2T.setRdc(BaseDataSetter.createRValue(RDC, ZUnitType.OHM));
		
		
		/*
		 * ====================   set the VSC 1-=====================================
		 */
		VSCConverterXmlType vsc1 = isVSC1Rec?vscHVDC2T.getRectifier():vscHVDC2T.getInverter();
		
		
		// DC Control
		
		if(vsc1DCControlType == 0)
		    vsc1.setDcControlMode(VSCDCControlModeEnumType.BLOCKED);
		else if(vsc1DCControlType == 1){
			vsc1.setDcControlMode(VSCDCControlModeEnumType.DC_VOLTAGE);
			
		}
		else{
			vsc1.setDcControlMode(VSCDCControlModeEnumType.REAL_POWER);
			
		}
		
		vsc1.setDcSetPoint(vsc1DCSet);
		
		
		// AC Control
		if(vsc1ACControlMode == 1){
			   vsc1.setAcControlMode(VSCACControlModeEnumType.VOLTAGE);
		}
		else if(vsc1ACControlMode == 2){
			vsc1.setAcControlMode(VSCACControlModeEnumType.POWER_FACTOR);
			
			// check the power factor
			  if(Math.abs(vsc1ACSet)>1.0)
				  throw new Error (" The power factor setting for VSC1 of # "+name+" # is not a valid number");
		}
		 
		vsc1.setAcSetPoint(vsc1ACSet);
		
		
		// Loss setting
		vsc1.setALoss(vsc1ALoss);
		vsc1.setBLoss(vsc1BLoss);
		vsc1.setMinLoss(vsc1MinLoss);
		
		// MVA and Current rating
		vsc1.setMVARating(BaseDataSetter.createApparentPower(vsc1MVARating,ApparentPowerUnitType.MVA));
		
		vsc1.setACCurrentRating(BaseDataSetter.createCurrentValue(vsc1CurRating, CurrentUnitType.AMP));
		
		
		vsc1.setPowerWeightFactor(vsc1PWF);
		vsc1.setQMax(BaseDataSetter.createReactivePowerValue(vsc1QMAX, ReactivePowerUnitType.MVAR));
		vsc1.setQMin(BaseDataSetter.createReactivePowerValue(vsc1QMIN, ReactivePowerUnitType.MVAR));
		
		
		if (vsc1RmtBusNum > 0 && vsc1RmtBusNum!=vsc1BusNum) {
	    	final String reBusId = IODMModelParser.BusIdPreFix+vsc1RmtBusNum;
	    	vsc1.setRemoteCtrlBusId(parser.createBusRef(reBusId));
	    }
		
		vsc1.setRemoteCtrlPercent(vsc1Rmpct);
		
		
		/*
		 * ====================   set the VSC 2-=====================================
		 */
         VSCConverterXmlType vsc2 = isVSC1Rec? vscHVDC2T.getInverter():vscHVDC2T.getRectifier();
		
		
		// DC Control
		
		if(vsc2DCControlType == 0)
		    vsc2.setDcControlMode(VSCDCControlModeEnumType.BLOCKED);
		else if(vsc2DCControlType == 1){
			vsc2.setDcControlMode(VSCDCControlModeEnumType.DC_VOLTAGE);
			
		}
		else{
			vsc2.setDcControlMode(VSCDCControlModeEnumType.REAL_POWER);
			
		}
		
		vsc2.setDcSetPoint(vsc2DCSet);
		
		
		// AC Control
		if(vsc2ACControlMode == 1){
			   vsc2.setAcControlMode(VSCACControlModeEnumType.VOLTAGE);
		}
		else if(vsc2ACControlMode == 2){
			vsc2.setAcControlMode(VSCACControlModeEnumType.POWER_FACTOR);
			if(Math.abs(vsc2ACSet)>1.0)
				  throw new Error (" The power factor setting for VSC2 of # "+name+" # is not a valid number");
		}
		
		vsc2.setAcSetPoint(vsc2ACSet);
		
		
		// Loss setting
		vsc2.setALoss(vsc2ALoss);
		vsc2.setBLoss(vsc2BLoss);
		vsc2.setMinLoss(vsc2MinLoss);
		
		// MVA and Current rating
		vsc2.setMVARating(BaseDataSetter.createApparentPower(vsc2MVARating,ApparentPowerUnitType.MVA));
		
		vsc2.setACCurrentRating(BaseDataSetter.createCurrentValue(vsc2CurRating, CurrentUnitType.AMP));
		
		
		vsc2.setPowerWeightFactor(vsc2PWF);
		vsc2.setQMax(BaseDataSetter.createReactivePowerValue(vsc2QMAX, ReactivePowerUnitType.MVAR));
		vsc2.setQMin(BaseDataSetter.createReactivePowerValue(vsc2QMIN, ReactivePowerUnitType.MVAR));
		
		
		if (vsc2RmtBusNum > 0 && vsc2RmtBusNum!=vsc2BusNum) {
	    	final String reBusId = IODMModelParser.BusIdPreFix+vsc2RmtBusNum;
	    	vsc2.setRemoteCtrlBusId(parser.createBusRef(reBusId));
	    }
		
		vsc2.setRemoteCtrlPercent(vsc2Rmpct);
		
		
		
	
	}

}
