package org.ieee.odm.adapter.pwd.impl;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.common.ODMException;
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
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.FactorUnitType;
import org.ieee.odm.schema.IDRefRecordXmlType;
import org.ieee.odm.schema.LimitXmlType;
import org.ieee.odm.schema.LineBranchEnumType;
import org.ieee.odm.schema.LineBranchInfoXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
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
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 /**
  * PWD Adapter branch data processor, extends from the inputLineStringParser
  * 
  * @version 0.3  
  * @author 
  * 
  * ====revision history===
  * 09/08/2012 add Phase Xfr
  * change the storage scheme from NVPairs to hashtable
  * 10/22/2012 fix a phase xfr bug when phase angle =0 
  * 
  */
public class BranchDataProcessor extends InputLineStringParser  {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(BranchDataProcessor.class);
	
	private enum XfrCtrlTargetType{Midddle_Of_Range,MaxMin};
	private enum XfrType{Fixed, LTC, Mvar,Phase};
	
	private String STATION_TOKEN ="SubStation";
	private String EQUIMENT_NAME_TOKEN ="EquimentName";
	
	
	private AclfModelParser parser = null;
	
	public BranchDataProcessor( AclfModelParser parser) {
		this.parser = parser;
	}
	
	public void processBranchData() throws ODMException{
		/*
		 * DATA (BRANCH, [BusNum,BusNum:1,LineCircuit,LineStatus,LineR,LineX,LineC,LineG,LineAMVA,LineBMVA,
              LineCMVA,LineShuntMW,LineShuntMW:1,LineShuntMVR,LineShuntMVR:1,LineTap,
              LinePhase,SeriesCapStatus])
		 * 
		 * BusNum-># of fromBus
		 * BusNum:1-># of toBus
		 * LineC->shuntB(Per unit susceptance (B) of branch on the system base), 50% at each end; 
		 * LineG->Per unit conductance (G) of branch on the system base
		 * LineXfmr: the flag indicating whether the branch is a Line or transformer;
		 */
		
		long fromBusNum=-1,toBusNum=-1;
		String fromBusId, toBusId,circuitId="1";

		boolean closed=true, isXfmr=false;
		double DEFAULT_MVA_RATING =9999;
		double DEFAULT_LineX_MINIMUM =1.0E-5;
		double r=0,x=DEFAULT_LineX_MINIMUM,b=0,g=0, // all per unit value on system base;
		       fBusShuntMW=0,fBusShuntMvar=0,tBusShuntMW=0,tBusShuntMvar=0, //shunt Mw and Mvar at two ends;
		       mvaRating1=DEFAULT_MVA_RATING ,mvaRating2=DEFAULT_MVA_RATING, 
		       mvaRating3=DEFAULT_MVA_RATING ;//mvar rating
   
		/*
		 * ONLY for specific application
		 */
		String typeToken="CustomString"; //type
		String extendedNameToken="CustomString:1"; //extended name: subStationName_baseKV_equipmentName
		String equipmentNameToken="CustomString:2"; //equipment name

		String type="",             //extended name, e.g., "Line" 
			   extBranchName ="",   //unique equipment name, e.g., "Sub2_230_L25" 
		       equipmentName ="";   //"L25"
		String substation ="";      //substring before the underscore of customString
		
		String isMonitorEle="";
		String  lsName="";
		
		
	   if (exist("LineXfmr"))
				isXfmr=getValue("LineXfmr").equalsIgnoreCase("YES")?true:false;
			//both LineXfmr or BranchDeviceType could be used to define branch type
	   if (exist("BranchDeviceType"))
				isXfmr=getValue("BranchDeviceType").equalsIgnoreCase("Transformer")?true:false;
	   
	   //transformer data is processed differently from line data
	   if(isXfmr==true){
			process2WXfrData();
		}
        else {
			try {
				fromBusNum=getLong("BusNum"); //mandatory field
				
				toBusNum=getLong("BusNum:1"); //mandatory field
					
				circuitId=exist("LineCircuit")?getValue("LineCircuit"):"1";
					
			   if (exist("LineStatus"))
					closed=getValue("LineStatus").equalsIgnoreCase("Closed")?true:false;
				    
				// LineR:1, LineX:1, LineG:1, LineC:1, XFStep:1, XFTapMax:1, XFTapMin:1, LineTap:1
				// The suffix of ":1" is used for Transformer definition,means those data values are based on transformer MVA base 
				   
				r=exist("LineR")? getDouble("LineR"):exist("LineR:1")?getDouble("LineR:1"):0;
				    
			    x=exist("LineX")?getDouble("LineX"):exist("LineX:1")?getDouble("LineX:1"):DEFAULT_LineX_MINIMUM;
				    
				b=exist("LineC")?getDouble("LineC"):exist("LineC:1")?getDouble("LineC:1"):0;
				g=exist("LineG")?getDouble("LineG"):exist("LineG:1")?getDouble("LineG:1"):0;
				
				mvaRating1=exist("LineAMVA")?getDouble("LineAMVA"):DEFAULT_MVA_RATING; // line limit rating
				   
				mvaRating2=exist("LineAMVA:1")?getDouble("LineAMVA:1"):
					exist("LineBMVA")?getDouble("LineBMVA"):DEFAULT_MVA_RATING;

				mvaRating3=exist("LineAMVA:2")?getDouble("LineAMVA:2"):
					exist("LineCMVA")?getDouble("LineCMVA"):DEFAULT_MVA_RATING;
				
				fBusShuntMW=exist("LineShuntMW")?getDouble("LineShuntMW"):0;
				    
				tBusShuntMW=exist("LineShuntMW:1")?getDouble("LineShuntMW:1"):0;
				    
				fBusShuntMvar=exist("LineShuntMVR")?getDouble("LineShuntMVR"):0;
					     
				tBusShuntMvar=exist("LineShuntMVR:1")?getDouble("LineShuntMVR:1"):0;
				
				//process custom string, for record type
				if(exist(typeToken))
					   type=getValue(typeToken);
					    
				if(exist(equipmentNameToken)) //CustomString:2, equipment name
					   equipmentName=getValue(equipmentNameToken);
				
				if(exist(extendedNameToken)){//CustomString:1, extended branch name, including substation id.
					extBranchName =getValue(extendedNameToken);
					
					substation = getSubstationName(extBranchName,equipmentName);
				}
			   //	
			   //***END OF DATA PROCESSING, BEGIN DATA SETTING *********************************
               //
				fromBusId =IODMModelParser.BusIdPreFix + fromBusNum;
				toBusId   =IODMModelParser.BusIdPreFix + toBusNum;

				// create a branch record
				BranchXmlType branch = parser.createLineBranch(fromBusId,
						toBusId, circuitId);
				
				//save custom string info
                if(!type.equals("")){
				LineBranchInfoXmlType LineInfo = new LineBranchInfoXmlType();
				LineInfo.setType(type.equalsIgnoreCase("line") ? LineBranchEnumType.OVERHEAD_LINE
						: (type.equalsIgnoreCase("breaker") ? LineBranchEnumType.BREAKER
								: (type.equalsIgnoreCase("zbr") ? LineBranchEnumType.ZBR
										: LineBranchEnumType.OTHER)));
				((LineBranchXmlType) branch).setLineInfo(LineInfo);
                }
                
    		    if(substation!=null){
    		    	if(!substation.equals(""))
    		    	BaseDataSetter.addNVPair(branch, STATION_TOKEN, substation);
    		    }
    			if(!equipmentName.equals(""))
    				BaseDataSetter.addNVPair(branch, EQUIMENT_NAME_TOKEN, equipmentName);
    			 if(!type.equals(""))
                 	BaseDataSetter.addNVPair(branch,typeToken,type);
                if(!extBranchName.equals(""))
                	BaseDataSetter.addNVPair(branch,extendedNameToken,extBranchName);
                
    			//check if the branch is a Line monitor element and the corresponding LSName
                if(exist("LineMonEle")) 
                	isMonitorEle=getValue("LineMonEle");
                if(exist("LSName"))
                	lsName=getValue("LSName");
                
                if(!lsName.equals("")){
                	BaseDataSetter.addNVPair(branch,"LineMonEle",isMonitorEle);
                	BaseDataSetter.addNVPair(branch,"LSName",lsName);
                }
                
    			
				branch.setOffLine(!closed);
				branch.setZ(BaseDataSetter.createZValue(r, x, ZUnitType.PU));
				// processing line shunt at from bus
				if (fBusShuntMW != 0 || fBusShuntMvar != 0) {
					LoadflowBusXmlType fromBus = parser.getBus(fromBusId);
					AclfDataSetter.addBusShuntY(fromBus, fBusShuntMW,
							fBusShuntMvar, YUnitType.MVAR);
				}
				// processing line shunt at to bus
				if (tBusShuntMW != 0 || tBusShuntMvar != 0) {
					LoadflowBusXmlType toBus = parser.getBus(toBusId);
					AclfDataSetter.addBusShuntY(toBus, tBusShuntMW,
							tBusShuntMvar, YUnitType.MVAR);
				}
                // processing line total charging shunt
				LineBranchXmlType line = (LineBranchXmlType) branch;
				if (g != 0 || b != 0)
					line.setTotalShuntY(BaseDataSetter.createYValue(g, b,
							YUnitType.PU));

				// set rating limit
				branch.setRatingLimit(OdmObjFactory
						.createBranchRatingLimitXmlType());

				AclfDataSetter.setBranchRatingLimitData(
						branch.getRatingLimit(), mvaRating1, mvaRating2,
						mvaRating3, ApparentPowerUnitType.MVA);

			} catch (Exception e) {
				throw new ODMException(e.toString());
			}
		}// END OF PROCESSING BRANCH
	}

	private void process2WXfrData() throws ODMException {
		long fromBusNum=-1,toBusNum=-1;
		long regBusNum=-1;//ONLY used for LTC type Transformer
		
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
		
		int xfTableNum =0;
		
		String fromBusId, toBusId,circuitId="1";
		String type="";
		String regBusId="";
		XfrCtrlTargetType regTargetType=null;
		XfrType xfrType=null;
		
		//The following is ONLY for specific user-defined format.
		String typeToken="CustomString"; //type
		String extNameToken="CustomString:1"; //extended branch name
		String equimentNameToken="CustomString:2";
		String extBranchName ="",   //unique equipment name, e.g., "Sub2_230_L25" 
			   equipmentName ="";   //"L25"
		String substation ="";      //substring before the underscore of customString

		String isMonitorEle="";
		String  lsName="";
		
		
		try{
		
		    fromBusNum=getLong("BusNum"); //mandatory field
				
			toBusNum=getLong("BusNum:1"); //mandatory field
				
			circuitId=exist("LineCircuit")?getValue("LineCircuit"):"1";
				
		   if (exist("LineStatus"))
				closed=getValue("LineStatus").equalsIgnoreCase("Closed")?true:false;
			    
			// LineR:1, LineX:1, LineG:1, LineC:1, XFStep:1, XFTapMax:1, XFTapMin:1, LineTap:1
			//TODO The suffix is used for Transformer definition,means those data values are based on transformer MVA base 
			   
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
			    
			
			if(exist(equimentNameToken))
				equipmentName=getValue(equimentNameToken);
			
			if(exist(extNameToken)){
				extBranchName=getValue(extNameToken);
				substation = getSubstationName(extBranchName,equipmentName);
			}
			   
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
				
			if(exist("XFTableNum")){
				xfTableNum = getInt("XFTableNum");
			}
			
			if(gMag==0&&g!=0)gMag=g;
			if(bMag==0&&b!=0)bMag=b;
			
		    fromBusId=IODMModelParser.BusIdPreFix+fromBusNum;
		    toBusId=IODMModelParser.BusIdPreFix+toBusNum;
		    
		    if(regBusNum>0)regBusId=IODMModelParser.BusIdPreFix+regBusNum;
		    
		    // create a branch record
		  //phase shifting transformer or traditional transformer
		    XfrBranchXmlType xfr= (XfrBranchXmlType)(phaseAngle!=0||xfrType==XfrType.Phase?parser.createPSXfrBranch(fromBusId, toBusId, circuitId)
		    			               :parser.createXfrBranch(fromBusId, toBusId, circuitId));
		    
		    //custom string
		    if(!type.equals(""))  //CustomString
		    	BaseDataSetter.addNVPair(xfr, "CustomString",type);
		    if(!extBranchName.equals(""))
		    	 BaseDataSetter.addNVPair(xfr, "CustomString:1",extBranchName);
		    if(substation!=null){
		    	if(!substation.equals(""))
		    	BaseDataSetter.addNVPair(xfr, STATION_TOKEN, substation);
		    }
		    if(!equipmentName.equals(""))
		       BaseDataSetter.addNVPair(xfr, EQUIMENT_NAME_TOKEN, equipmentName);
		    
		  //check if the branch is a Line monitor element and the corresponding LSName
            if(exist("LineMonEle")) 
            	isMonitorEle=getValue("LineMonEle");
            if(exist("LSName"))
            	lsName=getValue("LSName");
            
            if(!lsName.equals("")){
            	BaseDataSetter.addNVPair(xfr,"LineMonEle",isMonitorEle);
            	BaseDataSetter.addNVPair(xfr,"LSName",lsName);
            }
		    
		    			               
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

			if (xfr instanceof PSXfrBranchXmlType) {// Phase shifting transformer
				PSXfrBranchXmlType psXfr = (PSXfrBranchXmlType) xfr;
				AclfDataSetter.createPhaseShiftXfrData(psXfr, r, x,
						ZUnitType.PU, fromTurnRatio, toTurnRatio, phaseAngle,
						0, AngleUnitType.DEG, gMag, bMag, YUnitType.PU, MagnitizingZSideEnumType.FROM_SIDE);
				if (xfrRegMin != 0 || xfrRegMax != 0)
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
					log.error(
							"Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: "
									+ fromBusId + ", " + toBusId);
				}
				
				
					
			} else { //Non-phase shifting transformer
					
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
						log.error(
								"Error: fromBusRecord and/or toBusRecord cannot be found, fromId, toId: "
										+ fromBusId + ", " + toBusId);
					}
					
					
					
					
				} 
				
				if (xfrMvaBase != 0.0) {
					xfr.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
					TransformerInfoXmlType xfrInfo = xfr.getXfrInfo();
					xfrInfo.setDataOnSystemBase(false);
					xfrInfo.setRatedPower(BaseDataSetter.createApparentPower(xfrMvaBase, ApparentPowerUnitType.MVA));
					if (xfrFromSideNominalKV!=0.0)xfrInfo.setFromRatedVoltage(BaseDataSetter.createVoltageValue(xfrFromSideNominalKV, VoltageUnitType.KV));
					if (xfrToSideNominalKV!=0.0)xfrInfo.setToRatedVoltage(BaseDataSetter.createVoltageValue(xfrToSideNominalKV, VoltageUnitType.KV));
				}

			//set rating limit
			xfr.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
			
			AclfDataSetter.setBranchRatingLimitData(xfr.getRatingLimit(),
					mvaRating1, mvaRating2, mvaRating3, ApparentPowerUnitType.MVA);
			
			//set XFCorrection table number
			if(xfTableNum != 0){
			    if(xfr.getXfrInfo()==null)
			    	xfr.setXfrInfo(OdmObjFactory.createTransformerInfoXmlType());
			    TransformerInfoXmlType xfrInfo = xfr.getXfrInfo();
			    xfrInfo.setZTableNumber(xfTableNum);

			}
		}catch(Exception e){
			throw new ODMException(e.toString());
		}
	}
    /**
     * Process phase shifting transformer control data
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
     * Process LTC type transformer control data
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
		  				log.info("Cannot decide xfr tap control bus location: " + xfr.getId());
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
	
	public static String getSubstationName(String customStr_1,String customStr_2){
		String subName = "";
		int idx= customStr_1.length()-customStr_2.length()-1;
		if(idx<=0){
			subName = null;
			log.warn("Equipment Name is not contained in the branch extented name." +
					" # Extented Name: "+customStr_1+", # equipment name:"+customStr_2);
		}
		else if(!customStr_1.substring(idx).equals("_"+customStr_2)){
			subName = null;
			log.warn("Equipment Name is not contained in the branch extented name." +
					" # Extented Name: "+customStr_1+", # equipment name:"+customStr_2);
		}
		else{
		    String s3=customStr_1.substring(0, idx);
		    int last_underscore = s3.lastIndexOf("_");
		    if(last_underscore<0){
		    	log.warn("No underscore within " + s3
						+", # Extented Name: "+customStr_1+", # equipment name:"+customStr_2);
		    	subName = null;
		    }
		    else{
		    	subName =s3.substring(0, last_underscore);
		    }
		    
		}
		return subName;
	}
	
}
