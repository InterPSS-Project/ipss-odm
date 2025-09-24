package org.ieee.odm.adapter.pwd.impl;

import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.AngleXmlType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SwitchedShuntModeEnumType;
import org.ieee.odm.schema.SwitchedShuntXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.YUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 /**
  * Bus data processor for PowerWorld-TO-ODM Adapter based on power world v16 data definition
  * This processor processes the bus basic data, e.g. busNum, baseVolt, as well as bus load and
  * generation data. These data are defined in PWD separately, hence, processed separately in this processor.
  * 
  * @version 0.2  01/08/2013
  * @author 
  * 
  */
public class BusDataProcessor extends InputLineStringParser {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(BusDataProcessor.class);
	
	public static long swingBusNum=-1;
	private boolean isSubDataSection=false;
	private String STATION_TOKEN ="SubStation";
	private AclfModelParser parser = null;
	
	public BusDataProcessor(AclfModelParser parser) {
		this.parser = parser;
	}
	
	
	public void processBusBasicData(String busDataStr){
		/*
		 * DATA (BUS, [BusNum,BusName,BusNomVolt,BusPUVolt,BusAngle,BusG:1,BusB:1,AreaNum,ZoneNum,
            SubNum,BusSlack])
		 */
		
		
		long busNum=-1;
		int areaNum=-1,zoneNum=-1,ownerNum=-1;// purposely set to -1, a not real number;
		String busName="",busId="",substation="";
		double basekV=0, puVolt=0,kvVolt=0,angle=-360,busG=0,busB=0;
		boolean isSlackBus=false,busConnected=true;
		//first, parse the data and set it to the internal Hashtable
		parseData(busDataStr);
		try {

			busNum=getLong("BusNum");
	
			busName=exist("BusName")?getValue("BusName"):"";

			
			if(exist("BusStatus")){
				busConnected=(getValue("BusStatus").equalsIgnoreCase("Connected")||getValue("BusStatus").equalsIgnoreCase("Closed"))?true:false;
			}
			if(exist("BusNomVolt")){
				basekV=getDouble("BusNomVolt");
			}
			if(exist("BusPUVolt")){
				puVolt=getDouble("BusPUVolt");
			}
			if(exist("BusKVVolt")){
				kvVolt=getDouble("BusKVVolt");
			}
			if(exist("BusAngle")){
				angle=getDouble("BusAngle");
			}
			
			busG=exist("BusG:1")?getDouble("BusG:1"):0;
			
			busB=exist("BusB:1")?getDouble("BusB:1"):0;
			
			if(exist("AreaNum")){
				areaNum=getInt("AreaNum");
			}
			if(exist("ZoneNum")){
				zoneNum=getInt("ZoneNum");
			}
			if(exist("OwnerNum")){
				ownerNum=getInt("OwnerNum");
			}
			if(exist("BusSlack")){
				isSlackBus=getValue("BusSlack").equalsIgnoreCase("YES")
						?isSlackBus=true:false;
			}
			else
				throw new ODMException("No slack bus information is provided " +
						"in the input data!");
	
		
		if(busNum==-1) 
			log.error("bus Num is not defined yet!");
		busId=IODMModelParser.BusIdPreFix+busNum;
		
		LoadflowBusXmlType bus=parser.createBus(busId);
		bus.setId(busId);
		bus.setNumber(busNum);
		bus.setOffLine(!busConnected);
		
		if(!busName.equals("")) bus.setName(busName);
		if(areaNum!=-1)bus.setAreaNumber(areaNum);
		if(zoneNum!=-1)bus.setZoneNumber(zoneNum);
		
		//if(ownerNum!=-1)bus.getOwnerList().add(new OwnerXmlType());//setOwnerNumber(ownerNum);
		bus.setBaseVoltage(BaseDataSetter.createVoltageValue(basekV, VoltageUnitType.KV));
		if(puVolt==0&&kvVolt!=0){
			puVolt=kvVolt/basekV;
		}
		bus.setVoltage(BaseDataSetter.createVoltageValue(puVolt, VoltageUnitType.PU));
		
		if(angle!=-360)bus.setAngle(BaseDataSetter.createAngleValue(angle,AngleUnitType.DEG));
		
		if (busG != 0.0 || busB != 0.0) {
			bus.getShuntYData().setEquivY(BaseDataSetter.createYValue(busG, busB,YUnitType.MVAR));
		}
		
		if(isSlackBus){
			swingBusNum=busNum;
			//System.out.println ("==>Swing Bus number:"+swingBusNum);
			
		}
		
		//bus substation name
		//assume busName is created by following the convention: substationName_baseKV_busId
		substation = getBusSubstationName(busName);
		if(!substation.equals("")){
			BaseJaxbHelper.addNVPair(bus, STATION_TOKEN, substation);
		}
		
		} catch (ODMException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void processBusLoadData(String busLoadDataStr){
		/*
		 * DATA (LOAD, [BusNum,LoadID,LoadStatus,LoadSMW,LoadSMVR,LoadIMW,LoadIMVR,LoadZMW,LoadZMVR,
   AreaNum,ZoneNum])
		 */
		
		long busNum=-1;
		String busId="",loadId="";
		double loadSMVR=0,loadSMW=0,loadIMVR=0,loadIMW=0,loadZMVR=0,loadZMW=0;
		int areaNum=-1,zoneNum=-1;
		boolean loadOnLine=false;
		String CustomStrToken="CustomString";
		String customString ="";
		
		parseData(busLoadDataStr);
	
		try {
		busNum=getLong("BusNum"); //mandatory filed
		
		if(exist("LoadID")) loadId=getValue("LoadID");
		if(exist("LoadStatus")) 
		   loadOnLine=getValue("LoadStatus").equalsIgnoreCase("Closed")?true:false;
       
		loadSMW  = exist("LoadSMW")?getDouble("LoadSMW"):0;
		loadSMVR = exist("LoadSMVR")?getDouble("LoadSMVR"):0;
		loadIMW  = exist("LoadIMW")?getDouble("LoadIMW"):0;
		loadIMVR = exist("LoadIMVR")?getDouble("LoadIMVR"):0;
		loadZMW  = exist("LoadZMW")?getDouble("LoadZMW"):0;
		loadZMVR = exist("LoadZMVR")?getDouble("LoadZMVR"):0;
		areaNum  = exist("AreaNum")?getInt("AreaNum"):0;
		zoneNum  = exist("ZoneNum")?getInt("ZoneNum"):0;
		
		//process custom string, this is for specifically customized data 
		  if(exist(CustomStrToken)) {
				customString = getValue(CustomStrToken); 
		  }
		} catch (ODMException e) {
			e.printStackTrace();
		}
		busId=IODMModelParser.BusIdPreFix+busNum;
		
		LoadflowBusXmlType bus=parser.getBus(busId);
		
		// END OF DATA PROCESSING ,BEGIN DATA SETTING
        //TODO 04/03/2013
		//Some of original load is actually ZERO, we can set it as it is in the data
		//if(loadSMW!=0||loadSMVR!=0){
			if(loadIMW!=0||loadIMVR!=0||loadZMW!=0||loadZMVR!=0){
			  AclfDataSetter.setZIPLoadData(bus, loadSMW, loadSMVR, loadIMW, loadIMVR,
					  loadZMW, loadZMVR, ApparentPowerUnitType.MVA);
			  
			}else{ 
				AclfDataSetter.setLoadData(bus, LFLoadCodeEnumType.CONST_P, 
				loadSMW, loadSMVR, ApparentPowerUnitType.MVA);
			  }
		//}
		
		if(!customString.equals(""))
				BaseJaxbHelper.addNVPair(bus, "Load_"+CustomStrToken, customString);
      
		//TODO constI, constZ part of load is not processed. 
		
	}
	
	public void processBusGenData(String busGenDataStr){
		/*
		 * DATA (GEN, [BusNum,GenID,GenStatus,GenMW,GenMVR,GenAGCAble,GenEnforceMWLimits,GenMWMin,
                      GenMWMax,GenParFac,GenAVRAble,GenVoltSet,GenRegNum,GenMVRMin,GenMVRMax,
                      GenRMPCT,GenUseCapCurve,GenUseLDCRCC,GenXLDCRCC,GenMVABase,GenZR,GenZX,
                      GenStepR,GenStepX,GenStepTap,AreaNum,ZoneNum])
		 */
		
		long busNum=-1,regBusNum=-1;
		int areaNum=-1,zoneNum=-1;
		String genId="";
		double genVoltSet=0;
		double genMW=0,genMVR=0,genMWMin=0,genMWMax=0,genMVRMin=-9999,genMVRMax=9999,genMVABase=100;
		boolean genOnLine=false;
		boolean pLimitForced=true;
		double partFactor = 0.0;
		boolean genAGCAble = false;
		
		String customString="",     //extended name, e.g., "Sub1_14.9_G1" 
			   customString_1 ="",  //unique equipment name, e.g., "G1" 
		       customString_2 ="";
		String substation ="";      //substring before the underscore of customString
		
		
		if(busGenDataStr.trim().startsWith("<SUBDATA")){
			isSubDataSection=true;
			return;
		}
		else if(busGenDataStr.trim().startsWith("</SUBDATA>")){
			isSubDataSection=false;
			return;
		}
	
		/*
		 * skips SubData, since they are not used in load flow analysis;
		 */
	
		if (!isSubDataSection) {// if there is no subData or subData ends
		
		   parseData(busGenDataStr);
         
			try {
		
						
				 busNum=getLong("BusNum");// mandatory filed
				
				 genId =exist("GenID")?getValue("GenID"):"";
				   
				   
				  if (exist("GenStatus")) 
					  genOnLine =getValue("GenStatus").equalsIgnoreCase("Closed")?true:false;
		
				  if (exist("GenMW"))
							genMW =getDouble("GenMW");
				  if(exist("GenEnforceMWLimits")) 
						 pLimitForced= getValue("GenEnforceMWLimits").equalsIgnoreCase("YES")?
								true:false;
				  if(exist("GenRegNum"))
						regBusNum =getInt("GenRegNum");
				
				  genMVR =getDouble("GenMVR");
				  
				  if(exist("GenMWMin"))
						genMWMin = getDouble("GenMWMin");
				  
				  if (exist("GenMWMax"))
						genMWMax =getDouble("GenMWMax");
				  
				  if (exist("GenMVRMin"))
							genMVRMin = getDouble("GenMVRMin");
				  if (exist("GenMVRMax"))
							genMVRMax = getDouble("GenMVRMax");
				  if (exist("GenMVABase"))
							genMVABase =getDouble("GenMVABase");
				  if (exist("AreaNum"))
							areaNum = getInt("AreaNum");
				  if (exist("ZoneNum"))
				            zoneNum = getInt("ZoneNum");
				  
				  if(exist("GenVoltSet"))
							genVoltSet=getDouble("GenVoltSet");
				  if(exist("GenAGCAble"))
					  genAGCAble=getValue("GenAGCAble").toLowerCase().equals("yes");
				  if(exist("GenParFac"))		
					  partFactor= getDouble("GenParFac");
				  
				  //process custom string, this is for specifically customized data 
				  if(exist("CustomString")) {
						customString = getValue("CustomString"); 
						
				  }
				  
				  if(exist("CustomString:1")) customString_1 = getValue("CustomString:1");
				  
				  if(exist("CustomString:2")) customString_2 = getValue("CustomString:2");
					
				  int underScoreIdx = customString.indexOf("_");
				  if(underScoreIdx>0 && !customString_1.isEmpty() ) substation =getGenSubstationName(customString,customString_1);
				  
			   } catch (ODMException e) {
				e.printStackTrace();
		   }

			String busId = IODMModelParser.BusIdPreFix + busNum;
			LoadflowBusXmlType bus = parser.getBus(busId);
			
			//save custom string as NV pairs
			if(!substation.equals(""))BaseJaxbHelper.addNVPair(bus, STATION_TOKEN, substation);
			if(!customString.equals(""))BaseJaxbHelper.addNVPair(bus, "Gen_CustomString", customString);
			if(!customString_1.equals(""))BaseJaxbHelper.addNVPair(bus, "Gen_CustomString:1", customString_1);
			if(!customString_2.equals(""))BaseJaxbHelper.addNVPair(bus, "Gen_CustomString:2", customString_2);

			if (regBusNum != -1) {
				// this generator control the bus it connects to
				
				//generator regulates itself by default when regBusNum==0
				if (regBusNum == busNum||regBusNum==0) { 

					if (busNum != swingBusNum) {// This bus is a PV bus
						
						AclfDataSetter.setGenData(bus, LFGenCodeEnumType.PV, genVoltSet, VoltageUnitType.PU, 
								genMW, genMVR, ApparentPowerUnitType.MVA);

						LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
						
						defaultGen.setId(genId);
						defaultGen.setOffLine(!genOnLine);
						// equivGen.setRatedPower(BaseDataSetter.createApparentPower(genMVABase,
						// ApparentPowerUnitType.MVA));
						defaultGen.setPLimit(BaseDataSetter
								.createActivePowerLimit(genMWMax, genMWMin,
										ActivePowerUnitType.MW));

						defaultGen.getPLimit().setActive(pLimitForced);

						defaultGen.setQLimit(BaseDataSetter
								.createReactivePowerLimit(genMVRMax, genMVRMin,
										ReactivePowerUnitType.MVAR));

						if (areaNum != -1)
							defaultGen.setAreaNumber(areaNum);
						if (zoneNum != -1)
							defaultGen.setZoneNumber(zoneNum);

					} else { // swing bus
						//VoltageXmlType v = bus.getVoltage();
						AngleXmlType angle = bus.getAngle();
						AclfDataSetter.setGenData(bus, LFGenCodeEnumType.SWING,
								genVoltSet, VoltageUnitType.PU, genMW, genMVR,
								ApparentPowerUnitType.MVA);

						LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
						defaultGen.setId(genId);
						defaultGen.setMvaBase(BaseDataSetter.createPowerMvaValue(genMVABase));
						defaultGen.setOffLine(!genOnLine);

						// p limit
						defaultGen.setPLimit(BaseDataSetter
								.createActivePowerLimit(genMWMax, genMWMin,
										ActivePowerUnitType.MW));
					
						defaultGen.getPLimit().setActive(pLimitForced);
						// q limit
						defaultGen.setQLimit(BaseDataSetter
								.createReactivePowerLimit(genMVRMax, genMVRMin,
										ReactivePowerUnitType.MVAR));

						if (areaNum != -1)
							defaultGen.setAreaNumber(areaNum);
						if (zoneNum != -1)
							defaultGen.setZoneNumber(zoneNum);

					}

				} else {// the regulated bus is a remote bus

					//Define a remote bus that a generator controls/regulates, as a PV
					// And this gen bus is a PV bus itself

					// set remote bus data
					
					String regBusId = IODMModelParser.BusIdPreFix + regBusNum;
		
					// set this gen bus data
					AclfDataSetter.setGenData(bus, LFGenCodeEnumType.PV, genVoltSet,
							VoltageUnitType.PU, genMW,
							genMVR, ApparentPowerUnitType.MVA);

					LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
							
					defaultGen.setId(genId);
					defaultGen.setMvaBase(BaseDataSetter.createPowerMvaValue(genMVABase));
					
					defaultGen.setPLimit(BaseDataSetter.createActivePowerLimit(
							genMWMax, genMWMin, ActivePowerUnitType.MW));

					defaultGen.getPLimit().setActive(pLimitForced);

					defaultGen.setQLimit(BaseDataSetter.createReactivePowerLimit(
							genMVRMax, genMVRMin, ReactivePowerUnitType.MVAR));

					if (areaNum != -1)
						defaultGen.setAreaNumber(areaNum);
					if (zoneNum != -1)
						defaultGen.setZoneNumber(zoneNum);
					// define the remote control bus
					defaultGen.setRemoteVoltageControlBus(parser
							.createBusRef(regBusId));
				}

				// process generator participation factor
				LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
				if (genAGCAble)
					defaultGen.setMwControlParticipateFactor(partFactor);
			}
		}//end of if-subData
	}
	
	public void processBusShuntData(String shuntDataStr){
		/*
		 * DATA (SHUNT, [BusNum,ShuntID,AreaNum,ZoneNum,SSRegNum,SSStatus,SSCMode,SSVHigh,SSVLow,SSNMVR,
            SSBlockNumSteps,SSBlockMVarPerStep,SSBlockNumSteps:1,SSBlockMVarPerStep:1,
            SSBlockNumSteps:2,SSBlockMVarPerStep:2,SSBlockNumSteps:3,SSBlockMVarPerStep:3,
            SSBlockNumSteps:4,SSBlockMVarPerStep:4,SSBlockNumSteps:5,SSBlockMVarPerStep:5,
            SSBlockNumSteps:6,SSBlockMVarPerStep:6,SSBlockNumSteps:7,SSBlockMVarPerStep:7,
            SSBlockNumSteps:8,SSBlockMVarPerStep:8,SSBlockNumSteps:9,SSBlockMVarPerStep:9])

            sample data:
            
 DATA (SHUNT, [CustomString, BusNum, ShuntID, SSRegNum, SSStatus, SSCMode, DesiredVoltage, SSVHigh, SSVLow, SSNMVR, SSBlockNumSteps, SSBlockMVarPerStep])
{
"C62" 7296 1 7288 "Closed" "Discrete" 1.016 1.026 1.006 0.0 1 25.0
"C64" 7294 1 7287 "Closed" "Discrete" 1.016 1.026 1.006 0.0 1 25.0
}           
		 */
		
		long busNum=-1,regBusNum=-1;
		int areaNum=-1,zoneNum=-1,steps1=0,steps2=0;
		String shuntId="";
		boolean closed=false;
		double vHigh=1.0,vLow=1.0,normalMVR=0,MVarPerStep1=0,MVarPerStep2=0;
		SwitchedShuntModeEnumType mode=null; //Control Mode: Fixed, Discrete, Continuous, or Bus Shunt;
		
		String CustomStrToken="CustomString";
		String customString ="";
		parseData(shuntDataStr);
		
		try {
			busNum=getLong("BusNum");
		
		if(exist("SSRegNum"))
				regBusNum=getLong("SSRegNum");
	
		shuntId=exist("ShuntID")?getValue("ShuntID"):"";
		
		if(exist("SSStatus")) 
		    	closed=getValue("SSStatus").equalsIgnoreCase("Closed")?true:false;
		if (exist("AreaNum")) 
		    	areaNum=getInt("AreaNum");
		
		if (exist("ZoneNum")) 
		    	zoneNum=getInt("ZoneNum");
			
		if (exist("SSCMode")){
			String modeStr=getValue("SSCMode");
		    	mode=modeStr.equalsIgnoreCase("Discrete")?SwitchedShuntModeEnumType.DISCRETE_LOCAL_VOLTAGE:
		    				(modeStr.equalsIgnoreCase("Continuous")?SwitchedShuntModeEnumType.CONTINUOUS:
		    					SwitchedShuntModeEnumType.FIXED);
		}
		if (exist("SSVHigh"))
			vHigh=getDouble("SSVHigh");
			
		if (exist("SSVLow"))
			vLow=getDouble("SSVLow");
			
		if (exist("SSNMVR"))
				normalMVR=getDouble("SSNMVR");
			
		//TODO How to determine the number of blocks
		if (exist("SSBlockNumSteps"))
				steps1=new Double(getValue("SSBlockNumSteps")).intValue();
			
		if (exist("SSBlockMVarPerStep"))
				MVarPerStep1=getDouble("SSBlockMVarPerStep");
			
			
		if (exist("SSBlockNumSteps:1"))
				steps2=new Double(getValue("SSBlockNumSteps:1")).intValue();
			
		if (exist("SSBlockMVarPerStep:1"))
				MVarPerStep2=getDouble("SSBlockMVarPerStep:1");
		
		 if(exist(CustomStrToken)) {
				customString = getValue(CustomStrToken); 
				int underScoreIdx = customString.indexOf("_");
				if(underScoreIdx>0)
					customString.substring(0, underScoreIdx);
		  }
		
		} catch (ODMException e) {
			e.printStackTrace();
		} 
		

		//TODO no shunt status defined in ODM
		
		String busId=IODMModelParser.BusIdPreFix+busNum;
		LoadflowBusXmlType bus=parser.getBus(busId);
		
		AclfDataSetter.setShuntCompensatorData(bus, mode, normalMVR, vHigh, vLow);
		SwitchedShuntXmlType shunt=bus.getSwitchedShuntData().getContributeSwitchedShunt().get(0);
		
	
		// store custom string
		if(!customString.equals(""))
				BaseJaxbHelper.addNVPair(bus, "Shunt_"+CustomStrToken, customString);
		         
		
		// regulate a remote bus
		if(busNum!=regBusNum)
			shunt.setRemoteControlledBus(parser.createBusRef(IODMModelParser.BusIdPreFix+regBusNum));
		
		if(steps1>0&&MVarPerStep1!=0)
		AclfDataSetter.addShuntCompensatorBlock(bus, shunt,steps1, MVarPerStep1, ReactivePowerUnitType.MVAR);
        
		if(steps2>0&&MVarPerStep2!=0)
			AclfDataSetter.addShuntCompensatorBlock(bus, shunt,steps2, MVarPerStep2, ReactivePowerUnitType.MVAR);
        //TODO now only two blocks are considered. 
		//This should be enough for almost all real cases, one for capacitive, one for reactive
		
	}
	/**
	 * Get the substaion name out of the busName string, it is required that busName is created
	 * by following the convention: substationName_baseKV_busId
	 * 
	 * busName: "GE_CO_14_115_1"
	 * substationName: GE_CO_14
	 * 
	 * @param customStr
	 * @return
	 */
	private String getBusSubstationName(String customStr){
		
		String substr="";
		int last_underscore = customStr.lastIndexOf("_");
		
		if(last_underscore>0){
			String str2 = customStr.substring(0, last_underscore);
			int second_last_underscore=str2.lastIndexOf("_");
			if(second_last_underscore>0){
				substr = str2.substring(0, second_last_underscore);
			}
		}

		return substr;
		
	}
	
	private  String getGenSubstationName(String customStr,String customStr_1){
		String subName = "";
		int idx= customStr.length()-customStr_1.length()-1;
		
		if(idx<=0){
			subName = null;
			/*
			ODMLogger.getLogger().info("Equipment Name is not contained in the branch extented name." +
					" # Extented Name: "+customStr+", # equipment name:"+customStr_1);
			*/
		}
		else if(!customStr.substring(idx).equals("_"+customStr_1)){
			subName = null;
			/*
			ODMLogger.getLogger().info("Equipment Name is not contained in the branch extented name." +
					" # Extented Name: "+customStr+", # equipment name:"+customStr_1);
					*/
		}
		else{
		    String s3=customStr.substring(0, idx);
		    int last_underscore = s3.lastIndexOf("_");
		    if(last_underscore<0){
		    	/*
		    	ODMLogger.getLogger().info("No underscore within " + s3
						+", # Extented Name: "+customStr+", # equipment name:"+customStr_1);
		    	*/
		    	subName = null;
		    }
		    else{
		    	subName =s3.substring(0, last_underscore);
		    }
		    
		}
		return subName;
	}
	
}
