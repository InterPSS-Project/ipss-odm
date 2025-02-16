package org.ieee.odm.adapter.pwd.impl;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.FactorUnitType;
import org.ieee.odm.schema.IDRefRecordXmlType;
import org.ieee.odm.schema.LimitXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.MagnitizingZSideEnumType;
import org.ieee.odm.schema.MvarFlowAdjustmentDataXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.TapAdjustBusLocationEnumType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.TransformerInfoXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XformerZTableXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.ieee.odm.schema.XformerZTableXmlType.XformerZTableItem;
import org.ieee.odm.schema.XformerZTableXmlType.XformerZTableItem.Lookup;

/**
 * Transformer control data processor. It assumes that the basic load flow 
 * data for the transformers have been defined in the BRANCH part, thus, this
 * processor only deals with the tap or phase shifting control definition data.  
 * @author 
 *
 */
public class TransformerDataProcessor extends InputLineStringParser  {
	private enum XfrCtrlTargetType{Midddle_Of_Range,MaxMin};
	private enum XfrType{Fixed, LTC, Mvar,Phase};
	
	private AclfModelParser parser = null;
	private XformerZTableXmlType xfrCorrectTableList = null;
	
	public TransformerDataProcessor(AclfModelParser parser) {
		this.parser = parser;
	}
	
	 /**
     * It assumed that the basic loadflow data,such as R,X,TapRatio,etc., 
     * for Transformer has been processed before the transformer control data
     * 
     */
	public void processXFormerControlData(String xfomerDataStr) {
	
		/*
		 * DATA (TRANSFORMER,
		 * [BusNum,BusNum:1,LineCircuit,LineXFType,XFAuto,XFRegMin
		 * ,XFRegMax,XFTapMin, XFTapMax,XFStep,XFTableNum,XFRegBus])
		 */
		long fromBusNum = -1, toBusNum = -1;
		String fromBusId, toBusId, circuitId = "1";
		int tableNum = 0;
		double xfrTapMin = 0, xfrTapMax = 0, xfrTapStep = 0, xfrRegMin = 0, xfrRegMax = 0;
		double xfrMvaBase = 0;
		boolean isXFAutoControl=false;
		long regBusNum=-1;
		String regBusId="";
		XfrCtrlTargetType regTargetType=null;
		XfrType xfrType=null;
		
		parseData(xfomerDataStr);
		try {
			
			fromBusNum=getLong("BusNum"); //mandatory field
			
			toBusNum=getLong("BusNum:1"); //mandatory field
				
			circuitId=exist("LineCircuit")?getValue("LineCircuit"):"1";
			
				 
			xfrRegMin=exist("XFRegMin")?getDouble("XFRegMin"):0;
			   
		    xfrRegMax=exist("XFRegMax")?getDouble("XFRegMax"):0;
			   
			   
			xfrTapMax=exist("XFTapMax")?getDouble("XFTapMax"):exist("XFTapMax:1")?
					getDouble("XFTapMax:1"):1.0;
			    
			    
			xfrTapMin=exist("XFTapMin")?getDouble("XFTapMin"):exist("XFTapMin:1")?
					getDouble("XFTapMin:1"):1.0;
			    
			xfrTapStep=exist("XFStep")?getDouble("XFStep"):exist("XFStep:1")?
					getDouble("XFStep:1"):1.0;
			
						
		   if (exist("XFRegTargetType"))
				regTargetType=getValue("XFRegTargetType").startsWith("Middle")?XfrCtrlTargetType.Midddle_Of_Range
					    			       :XfrCtrlTargetType.MaxMin;
		  if (exist("XFAuto"))
				isXFAutoControl=getValue("XFAuto").trim().equalsIgnoreCase("No")?false:true;
					    	
		  if (exist("XFRegBus"))
				regBusNum=getLong("XFRegBus"); 
		  if (exist("LineXFType"))
				xfrType=getValue("LineXFType").trim().equalsIgnoreCase("Fixed")?XfrType.Fixed:
					   getValue("LineXFType").trim().equalsIgnoreCase("LTC")?XfrType.LTC:
					   getValue("LineXFType").trim().equalsIgnoreCase("Mvar")?XfrType.Mvar:XfrType.Phase;
						
		  xfrMvaBase=exist("XFMVABase")?getDouble("XFMVABase"):100;
			/*
			*/
			fromBusId =IODMModelParser.BusIdPreFix + fromBusNum;
			toBusId   =IODMModelParser.BusIdPreFix + toBusNum;
			regBusId  =IODMModelParser.BusIdPreFix + regBusNum;

			XfrBranchXmlType xfr = parser.getXfrBranch(fromBusId, toBusId, circuitId);
			if(xfr instanceof PSXfrBranchXmlType){
				PSXfrBranchXmlType psXfr=(PSXfrBranchXmlType) xfr;
				if(xfrRegMin!=0|| xfrRegMax!=0){
					setXfrPhaseControlData(isXFAutoControl, xfrRegMin, xfrRegMax,
							xfrTapMax, xfrTapMin, regTargetType, psXfr);
				}
			}
			else{
				if(xfrRegMin!=0|| xfrRegMax!=0)
			        setTapControlData(isXFAutoControl, xfrRegMin, xfrRegMax,
					xfrTapMax, xfrTapMin, xfrTapStep, regBusId,
					regTargetType, xfrType, xfr);
			}
			// TODO set type and regulation info;
			TransformerInfoXmlType xfmrInfo = new TransformerInfoXmlType();
			xfr.setXfrInfo(xfmrInfo);
			xfmrInfo.setZTableNumber(tableNum);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void process2WXfrData(){
		long fromBusNum=-1,toBusNum=-1;
		String fromBusId, toBusId,circuitId="1";
		String type="";
		String xfrId="";
		boolean closed=true,isXFAutoControl=false;
		double r=0,x=0,b=0,g=0, bMag=0,gMag=0, // all per unit value on system base;
		       fBusShuntMW=0,fBusShuntMvar=0,tBusShuntMW=0,tBusShuntMvar=0, //shunt Mw and Mvar at two ends;
		       mvaRating1=9999,mvaRating2=9999,mvaRating3=9999,//mvar rating
		       fromTurnRatio=1.0, toTurnRatio=1.0;//tap ratio
		double phaseAngle=0.0;
		double xfrRegMin=0, xfrRegMax=0;
		double xfrTapMax=0, xfrTapMin=0;
		double xfrStep=0;
		double xfrMvaBase = 0.0, xfrFromSideNominalKV = 0.0, xfrToSideNominalKV=0.0;
		double miniLineX=1.0E-5;
		long regBusNum=-1;//ONLY use for LTC type Transformer
		String regBusId="";
		XfrCtrlTargetType regTargetType=null;
		XfrType xfrType=null;
		//The following is ONLY for specific user-defined format.
		String typeToken="CustomString"; //type
		String idToken="CustomString:1"; //branch Id

		try{
		
		    fromBusNum=getLong("BusNum"); //mandatory field
				
			toBusNum=getLong("BusNum:1"); //mandatory field
				
			circuitId=exist("LineCircuit")?getValue("LineCircuit"):"1";
				
		   if (exist("LineStatus"))
				closed=getValue("LineStatus").equalsIgnoreCase("Closed")?true:false;
			    
			// LineR:1, LineX:1, LineG:1, LineC:1, XFStep:1, XFTapMax:1, XFTapMin:1, LineTap:1
			//TODO The suffix of ����1�� is used for Transformer definition,means those data values are based on transformer MVA base 
			   
		    r=exist("LineR")?getDouble("LineR"):0;
			if(exist("LineR:1"))r=getDouble("LineR:1");
			    
		    x=exist("LineX")?getDouble("LineX"):miniLineX;
		    if(exist("LineX:1"))x=getDouble("LineX:1");
			    
			b=exist("LineC")?getDouble("LineC"):0;
			if(exist("LineC:1")) b=getDouble("LineC:1");
			
			g=exist("LineG")?getDouble("LineG"):0;
			if(exist("LineG:1")) g=getDouble("LineG:1");
			
			if (exist("XfrmerMagnetizingB"))
			  bMag=getValue("XfrmerMagnetizingB").isEmpty()?0:getDouble("XfrmerMagnetizingB");
			    
			
		    if (exist("XfrmerMagnetizingG"))
			        gMag=getValue("XfrmerMagnetizingG").isEmpty()?0:getDouble("XfrmerMagnetizingG");
			    
			mvaRating1=exist("LineAMVA")?getDouble("LineAMVA"):0; // line limit rating
			   
			mvaRating2=exist("LineAMVA:1")?getDouble("LineAMVA:1"):exist("LineBMVA")?getDouble("LineBMVA"):0;

			mvaRating3=exist("LineAMVA:2")?getDouble("LineAMVA:2"):exist("LineCMVA")?getDouble("LineCMVA"):0;
			    
			    
			fBusShuntMW=exist("LineShuntMW")?getDouble("LineShuntMW"):0;
			    
			tBusShuntMW=exist("LineShuntMW:1")?getDouble("LineShuntMW:1"):0;
			    
			fBusShuntMvar=exist("LineShuntMVR")?getDouble("LineShuntMVR"):0;
			     
			tBusShuntMvar=exist("LineShuntMVR:1")?getDouble("LineShuntMVR:1"):0;
			    
			//if (nv.name.equals("LineTap") || nv.name.equals("LineTap:1") || nv.name.equals("XFFixedTap"))
		    fromTurnRatio=exist("LineTap")?getDouble("LineTap"):exist("LineTap:1")?getDouble("LineTap:1"):1.0;
				
			// XFFixedTap is chosen as the priority setting
		    fromTurnRatio=exist("XFFixedTap")?getDouble("XFFixedTap"):fromTurnRatio; 	    
			   
			toTurnRatio=exist("XFFixedTap:1")?getDouble("XFFixedTap:1"):1.0;
				
			phaseAngle=exist("LinePhase")?getDouble("LinePhase"):0;
				
			   
			xfrRegMin=exist("XFRegMin")?getDouble("XFRegMin"):0;
			   
		    xfrRegMax=exist("XFRegMax")?getDouble("XFRegMax"):0;
			   
			   
			xfrTapMax=exist("XFTapMax")?getDouble("XFTapMax"):exist("XFTapMax:1")?
					getDouble("XFTapMax:1"):1.0;
			    
			    
			xfrTapMin=exist("XFTapMin")?getDouble("XFTapMin"):exist("XFTapMin:1")?
					getDouble("XFTapMin:1"):1.0;
			    
			xfrStep=exist("XFStep")?getDouble("XFStep"):exist("XFStep:1")?
					getDouble("XFStep:1"):1.0;
			    
			if(exist(typeToken))
			   type=getValue(typeToken);
			    
			if(exist(idToken))
			   xfrId=getValue(idToken);
				
			   
			if(exist("XFMVABase")) xfrMvaBase=getDouble("XFMVABase");
			    
			if (exist("XFNominalKV"))
			    xfrFromSideNominalKV=getDouble("XFNominalKV");
			   
			if (exist("XFNominalKV:1"))
			    xfrToSideNominalKV=getDouble("XFNominalKV:1");
				
			if (exist("XFRegTargetType"))
			    regTargetType=getValue("XFRegTargetType").startsWith("Middle")?XfrCtrlTargetType.Midddle_Of_Range
			    			       :XfrCtrlTargetType.MaxMin;
			if (exist("XFAuto"))
			    isXFAutoControl=getValue("XFAuto").trim().equalsIgnoreCase("No")?false:true;
			    	
			if (exist("XFRegBus"))
					regBusNum=getLong("XFRegBus"); 
			if (exist("LineXFType"))
			    	xfrType=getValue("LineXFType").trim().equalsIgnoreCase("Fixed")?XfrType.Fixed:
			    		getValue("LineXFType").trim().equalsIgnoreCase("LTC")?XfrType.LTC:
			    			getValue("LineXFType").trim().equalsIgnoreCase("Mvar")?XfrType.Mvar:XfrType.Phase;
				
			
			
			if(gMag==0&&g!=0)gMag=g;
			if(bMag==0&&b!=0)bMag=b;
			
		    fromBusId=IODMModelParser.BusIdPreFix+fromBusNum;
		    toBusId=IODMModelParser.BusIdPreFix+toBusNum;
		    
		    if(regBusNum>0)regBusId=IODMModelParser.BusIdPreFix+regBusNum;
		    
		    // create a branch record
		  //phase shifting transformer or traditional transformer
		    XfrBranchXmlType xfr= (XfrBranchXmlType)(phaseAngle!=0?parser.createPSXfrBranch(fromBusId, toBusId, circuitId)
		    			               :parser.createXfrBranch(fromBusId, toBusId, circuitId));
		    
			/*
			 * common setting for Transformer branch
			 */
		    
			xfr.setOffLine(!closed);
			xfr.setZ(BaseDataSetter.createZValue(r, x, ZUnitType.PU));
			//processing lint shunt at from bus 
			if(fBusShuntMW!=0||fBusShuntMvar!=0){
				LoadflowBusXmlType fromBus=parser.getBus(fromBusId);
				AclfDataSetter.addBusShuntY(fromBus, fBusShuntMW, fBusShuntMvar, YUnitType.MVAR);
			}
			//processing lint shunt at to bus 
			if(tBusShuntMW!=0||tBusShuntMvar!=0){
				LoadflowBusXmlType toBus=parser.getBus(toBusId);
				AclfDataSetter.addBusShuntY(toBus, tBusShuntMW, tBusShuntMvar, YUnitType.MVAR);
			}

			if (phaseAngle == 0) {// transformer type, since it is rare for a PSXfr to have LinePhase=0; 
					
					AclfDataSetter.createXformerData(xfr, r, x, ZUnitType.PU,
							fromTurnRatio, toTurnRatio, gMag, bMag, YUnitType.PU, MagnitizingZSideEnumType.FROM_SIDE);
					if(xfrRegMin!=0||xfrRegMax!=0)
                    setTapControlData(isXFAutoControl, xfrRegMin, xfrRegMax,
							xfrTapMax, xfrTapMin, xfrStep, regBusId,
							regTargetType, xfrType, xfr);
					
					BusXmlType fromBusRec = parser.getBus(fromBusId);
					BusXmlType toBusRec = parser.getBus(toBusId);
					if (fromBusRec != null && toBusRec != null) {
						AclfDataSetter.setXfrRatingData(xfr, fromBusRec
								.getBaseVoltage().getValue(), toBusRec
								.getBaseVoltage().getValue(), fromBusRec
								.getBaseVoltage().getUnit());
					} else {
						ODMLogger.getLogger().severe(
								"Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: "
										+ fromBusId + ", " + toBusId);
					}
				} else {// Phase shifting transformer
					PSXfrBranchXmlType psXfr = (PSXfrBranchXmlType) xfr;
					AclfDataSetter.createPhaseShiftXfrData(psXfr, r, x,
							ZUnitType.PU, fromTurnRatio, toTurnRatio, phaseAngle, 0,
							AngleUnitType.DEG, gMag, bMag, YUnitType.PU, MagnitizingZSideEnumType.FROM_SIDE);
					if(xfrRegMin!=0||xfrRegMax!=0)
					setXfrPhaseControlData(isXFAutoControl, xfrRegMin,
							xfrRegMax, xfrTapMax, xfrTapMin, regTargetType,
							psXfr);
					

					// xfr rating
					BusXmlType fromBusRec = parser.getBus(fromBusId);
					BusXmlType toBusRec = parser.getBus(toBusId);
					if (fromBusRec != null && toBusRec != null) {
						AclfDataSetter.setXfrRatingData(psXfr, fromBusRec
								.getBaseVoltage().getValue(), toBusRec
								.getBaseVoltage().getValue(), fromBusRec
								.getBaseVoltage().getUnit());
					} else {
						ODMLogger.getLogger().severe(
								"Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: "
										+ fromBusId + ", " + toBusId);
					}
				} 
				
				if (xfrMvaBase != 0.0) {
					xfr.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
					TransformerInfoXmlType xfrInfo = xfr.getXfrInfo();
					// TODO it seems PWD xfr data is alway on system base
					xfrInfo.setDataOnSystemBase(false);
					xfrInfo.setRatedPower(BaseDataSetter.createApparentPower(xfrMvaBase, ApparentPowerUnitType.MVA));
					if (xfrFromSideNominalKV!=0.0)xfrInfo.setFromRatedVoltage(BaseDataSetter.createVoltageValue(xfrFromSideNominalKV, VoltageUnitType.KV));
					if (xfrToSideNominalKV!=0.0)xfrInfo.setToRatedVoltage(BaseDataSetter.createVoltageValue(xfrToSideNominalKV, VoltageUnitType.KV));
				}

			//set rating limit
			xfr.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
			
			AclfDataSetter.setBranchRatingLimitData(xfr.getRatingLimit(),
					mvaRating1, mvaRating2, mvaRating3, ApparentPowerUnitType.MVA);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
    /**
     *  set the phase shifting control data
     * @param isXFAutoControl
     * @param xfrRegMin
     * @param xfrRegMax
     * @param xfrTapMax
     * @param xfrTapMin
     * @param regTargetType
     * @param psXfr
     */
	private void setXfrPhaseControlData(boolean isXFAutoControl,
			double xfrRegMin, double xfrRegMax, double xfrTapMax,
			double xfrTapMin, XfrCtrlTargetType regTargetType,
			PSXfrBranchXmlType psXfr) {
		// angle adjustment
		AngleAdjustmentXmlType angAdj = new AngleAdjustmentXmlType();
		psXfr.setAngleAdjustment(angAdj);
		//set control status, active if offLine=false;
		angAdj.setOffLine(!isXFAutoControl);
		angAdj.setAngleAdjOnFromSide(true);
		
		angAdj.setAngleLimit(OdmObjFactory.createAngleLimitXmlType());
		/*
		 * It is assumed here that the {xfrTapMax, xfrTapMin} are also 
		 * used to represent Angle adjustment Limit;
		 */
		BaseDataSetter.setLimit(angAdj.getAngleLimit(), xfrTapMax, xfrTapMin);
		angAdj.getAngleLimit().setUnit(AngleUnitType.DEG);
		
		/*
		 * assume the desired value is measured at the from side, since the 
		 * value is not specified in PWD file
		 */
		angAdj.setDesiredMeasuredOnFromSide(true);
		
		if(regTargetType!=null){
			if(regTargetType==XfrCtrlTargetType.Midddle_Of_Range){
				angAdj.setDesiredValue((xfrRegMax+xfrRegMin)/2);
				angAdj.setDesiredActivePowerUnit(ActivePowerUnitType.MW);
				angAdj.setMode(AdjustmentModeEnumType.VALUE_ADJUSTMENT);
			}
			else {
				angAdj.setRange(new LimitXmlType());
				angAdj.setDesiredActivePowerUnit(ActivePowerUnitType.MW);
				BaseDataSetter.setLimit(angAdj.getRange(), xfrRegMax,
						xfrRegMin);
				angAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
			}
			
		}
		
		angAdj.setDesiredMeasuredOnFromSide(true);
	}
    /**
     * set  LTC type transformer tap control data
     * @param isXFAutoControl
     * @param xfrRegMin
     * @param xfrRegMax
     * @param xfrTapMax
     * @param xfrTapMin
     * @param xfrStep
     * @param regBusId
     * @param regTargetType
     * @param xfrType
     * @param xfr
     */
	private void setTapControlData(boolean isXFAutoControl, double xfrRegMin,
			double xfrRegMax, double xfrTapMax, double xfrTapMin,
			double xfrStep, String regBusId, XfrCtrlTargetType regTargetType,
			XfrType xfrType, XfrBranchXmlType xfr) {
		//tap control
		TapAdjustmentXmlType tapAdj = OdmObjFactory.createTapAdjustmentXmlType();
		xfr.setTapAdjustment(tapAdj);
		tapAdj.setOffLine(!isXFAutoControl);
		tapAdj.setTapLimit(BaseDataSetter.createTapLimit(xfrTapMax, xfrTapMin));
		tapAdj.getTapLimit().setUnit(FactorUnitType.PU);
		tapAdj.setTapAdjStepSize(xfrStep);
		tapAdj.setTapAdjOnFromSide(true);//PWD standard transformer model tap setting: fromTap:1.0
		
		if(xfrType!=null){
			if(xfrType==XfrType.LTC){
				tapAdj.setAdjustmentType(TapAdjustmentEnumType.VOLTAGE);
		  		VoltageAdjustmentDataXmlType vAdjData = OdmObjFactory.createVoltageAdjustmentDataXmlType();
		  		tapAdj.setVoltageAdjData(vAdjData);
		  		//common setting
		  		vAdjData.setDesiredVoltageUnit(VoltageUnitType.PU);
		  		
		  		 if(regTargetType!=null){
		            	if(regTargetType==XfrCtrlTargetType.Midddle_Of_Range){
		            		vAdjData.setMode(AdjustmentModeEnumType.VALUE_ADJUSTMENT);
			          		vAdjData.setDesiredValue((xfrRegMax+xfrRegMin)/2);				
		            	}
		            	else{
		            		vAdjData.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
		    		  		vAdjData.setRange(OdmObjFactory.createLimitXmlType());
		    		  		BaseDataSetter.setLimit(vAdjData.getRange(), xfrRegMax,
		    						xfrRegMin);
		            	}
		  		 }
		  		
		  		IDRefRecordXmlType refBus = parser.createBusRef(regBusId);
		  		if (refBus != null) {
		  			vAdjData.setAdjVoltageBus(refBus);
		  			if (regBusId.equals(BaseJaxbHelper.getRecId(xfr.getFromBus())))
		  				vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.FROM_BUS);
		  			else if (regBusId.equals(BaseJaxbHelper.getRecId(xfr.getToBus())))
		  				vAdjData.setAdjBusLocation(TapAdjustBusLocationEnumType.TO_BUS);
		  			else {
		  				ODMLogger.getLogger().warning("Cannot decide xfr tap control bus location: " + xfr.getId());
		  				tapAdj.setOffLine(true);
		  			}
		  		}
		  		else
		  			// when the bus to be voltage controlled cannot be found in the
		  			// network, the control is turned off
		  			tapAdj.setOffLine(true);
			}
			else if(xfrType==XfrType.Mvar){
				tapAdj.setAdjustmentType(TapAdjustmentEnumType.M_VAR_FLOW);
			    MvarFlowAdjustmentDataXmlType mvarAdjData = OdmObjFactory.createMvarFlowAdjustmentDataXmlType();
		  		tapAdj.setMvarFlowAdjData(mvarAdjData);
		  		//common setting
		  		mvarAdjData.setRange(new LimitXmlType());
		  		BaseDataSetter.setLimit(mvarAdjData.getRange(), xfrRegMax,
						xfrRegMin);
		  		mvarAdjData.setDesiredMvarFlowUnit(ReactivePowerUnitType.MVAR);
		  		
		  		 if(regTargetType!=null){
		  			 
		            	if(regTargetType==XfrCtrlTargetType.Midddle_Of_Range){
		            		mvarAdjData.setMode(AdjustmentModeEnumType.VALUE_ADJUSTMENT);
			          		mvarAdjData.setDesiredValue((xfrRegMax+xfrRegMin)/2);				
			          		
		            	}
		            	else{
		            		mvarAdjData.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
		            	}
		            	
		  		 }
			}
		}//END OF TAP CONTROL SETTING
	}
	
	/**
	 *  process three winding transformer data
	 * @param triWXformerDataStr
	 */
	public void process3WXFomerData(String triWXformerDataStr){
		throw new UnsupportedOperationException("The 3winding transformer is not supported yet!");
		/*
		 * the 3-winding transformers are treated as 3 2-winding transformers
		 *  with an additional star bus added to the network;
		 */
		
		
	}
	
	public void processXFCorrection(String XfCorrectionStr) throws ODMException{
		if(xfrCorrectTableList == null){
			xfrCorrectTableList = OdmObjFactory.createXformerZTableXmlType();
			xfrCorrectTableList.setAdjustSide(BranchBusSideEnumType.FROM_SIDE);
		    parser.getNet().setXfrZTable(xfrCorrectTableList);
		}
		//parse the input data and save it to the fieldTable
		parseData(XfCorrectionStr);
		
		//create an xfr correction Item
		XformerZTableItem corItem = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItem();
		xfrCorrectTableList.getXformerZTableItem().add(corItem);
		
		//create tap-factor pair 
		Lookup nvPair=OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup();
		
	    int tableNum = getInt("XFCorTableNum");
		String tableName = getValue("XFCorTableName");
	    corItem.setNumber(tableNum);
	    corItem.setName(tableName);
	    
	    /*Based on definition of PSS/E:
	      On each record, at least 2 pairs of values must be specified
	      and up to 11 may be entered.
	    */
	   
		double tap0=getDouble("XFCorTap");
		double factor0=getDouble("XFCorFactor");
		
		nvPair.setTurnRatioShiftAngle(tap0);
		nvPair.setScaleFactor(factor0);
		corItem.getLookup().add(nvPair);
		
		double tap1=getDouble("XFCorTap:1");
		double factor1=getDouble("XFCorFactor:1");
		nvPair=OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup();
		nvPair.setTurnRatioShiftAngle(tap1);
		nvPair.setScaleFactor(factor1);
		corItem.getLookup().add(nvPair);

		//processing the remaining tap-factor pairs
		String corTapPrefix="XFCorTap:";
		String corFactorPrefix="XFCorFactor:";
		for(int k=2;k<=10;k++){
			
			if(!getValue(corTapPrefix+k).equals("")){
				double tapk=getDouble(corTapPrefix+k);
				double factork=getDouble(corFactorPrefix+k);
				//set tap-factor pair
				nvPair=OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup();
				nvPair.setTurnRatioShiftAngle(tapk);
				nvPair.setScaleFactor(factork);
				//add it to lookup list
				corItem.getLookup().add(nvPair);
			}
			else{// the rest is not defined, break the loop;
				break;
			}
			
		}
	}
	

}
