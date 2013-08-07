/*
 * @(#)BPADynamicFaultOperationRecord.java   
 *
 * Copyright (C) 2006-2008 www.interpss.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @Author Stephen Hau
 * @Version 1.0
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.bpa.dynamic;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.bpa.lf.BPABusRecord;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.ModelStringUtil;
import org.ieee.odm.model.dstab.DStabDataSetter;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.schema.AcscFaultCategoryEnumType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusIDRefXmlType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.DStabBranchFaultXmlType;
import org.ieee.odm.schema.DStabBusFaultXmlType;
import org.ieee.odm.schema.DcLineFaultEnumType;
import org.ieee.odm.schema.DcLineFaultXmlType;
import org.ieee.odm.schema.GenChangeDynamicEventXmlType;
import org.ieee.odm.schema.LoadChangeDynamicEventXmlType;
import org.ieee.odm.schema.TimePeriodUnitType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.ZUnitType;

public class BPADynamicFaultOperationRecord {
	
	public static void processFaultOperationData(String str, DStabModelParser parser ) throws ODMException { 
		// TODO: Get: what is the default value for mode, in case mode field is not defined?
    	int mode = 0;
    	if (!str.substring(36, 37).trim().equals(""))
    		mode = new Integer(str.substring(36, 37).trim()).intValue();

    	final String strAry[] = getFaultOperationDataFields(str, mode);    	
    	
        if(mode==1||mode==2||mode==3||mode==-1||mode==-2||mode==-3){    		
    		final String bus1Id = BPABusRecord.getBusId(strAry[2]);
    		BusIDRefXmlType bus1=parser.createBusRef(bus1Id);
    		final String bus2Id = BPABusRecord.getBusId(strAry[5]);
    		BusIDRefXmlType bus2=parser.createBusRef(bus2Id);
    		
    		boolean breaker1Opened=true;    		
    		if(strAry[1].equals("")){
    			breaker1Opened=false;
    		}    		
    		boolean breaker2Opened=true;
    		if(strAry[4].equals("")){
    			breaker2Opened=false;
    		}
    		//TODO the parallel branch ID can't be set here
    		String parId="";
    		if(!strAry[7].equals("")){
    			parId=strAry[7];   			
    		}
    		
    		double bus1RatedV=ModelStringUtil.getDouble(strAry[3],0.0);
    		double bus2RatedV=ModelStringUtil.getDouble(strAry[6],0.0);
    		double operationTime=ModelStringUtil.getDouble(strAry[9],0.0);
    		double r=ModelStringUtil.getDouble(strAry[10],0.0);
    		double x=ModelStringUtil.getDouble(strAry[11],0.0);
    		double faultLocation=ModelStringUtil.getDouble(strAry[12],0.0);
    		
    		// bus fault
    		if(mode==1||mode==-1||mode==2||mode==-2){ 
    			DStabBusFaultXmlType busFault=odmObjFactory.createDStabBusFaultXmlType();
    			busFault.setFaultCategory(AcscFaultCategoryEnumType.FAULT_3_PHASE);
    			
    			// not permanent bus fault;
                if(mode==1||mode==-1){ 	    			
 	    			if(breaker1Opened==false&&breaker2Opened==false){	    				
 	    				busFault.setFaultedBus(bus1);
 	    				busFault.setFaultedBusRatedV(DStabDataSetter.createVoltageValue(bus1RatedV, VoltageUnitType.KV));
 	    				busFault.setRemoteEndBus(bus2);
 	    				busFault.setRemoteEndBusRatedV(DStabDataSetter.createVoltageValue(bus2RatedV, VoltageUnitType.KV)); 
 	    				busFault.setFaultStartTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));	    				 	        			
 	    			}else if(breaker1Opened==true&&breaker2Opened==false){	    				
 	    				if(busFault!=null){
 	    					busFault.setFirstOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
 	    					busFault.setPermanentFault(true);
 	            			if(mode==-1){
 	            				double duration= busFault.getFirstOperationTime().getValue()-busFault.getFaultStartTime().getValue();
 	            				busFault.setFaultDurationTime(DStabDataSetter.createTimePeriodValue(duration, TimePeriodUnitType.CYCLE));
 	            				busFault.setPermanentFault(false);
 	            			} 
 	    				} 	    				
 	    			}else if(breaker1Opened==true&&breaker2Opened==true){
	    				if(busFault!=null){
	    					busFault.setSecondOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
	    					busFault.setPermanentFault(true);
	        				if(mode==-1){	        					
	            				double duration= busFault.getSecondOperationTime().getValue()-busFault.getFaultStartTime().getValue();
	            				busFault.setFaultDurationTime(DStabDataSetter.createTimePeriodValue(duration, TimePeriodUnitType.CYCLE));
	            				busFault.setPermanentFault(false);
	            			}	            			
	    				}	    				
 	    			}
                }
            	// permanent bus fault
    			else if(mode==2||mode==-2){
    				if(mode==2){
    					busFault.setFaultedBus(bus1);
 	    				busFault.setFaultedBusRatedV(DStabDataSetter.createVoltageValue(bus1RatedV, VoltageUnitType.KV));
 	    				busFault.setRemoteEndBus(bus2);
 	    				busFault.setRemoteEndBusRatedV(DStabDataSetter.createVoltageValue(bus2RatedV, VoltageUnitType.KV)); 
    					busFault.setFaultCategory(AcscFaultCategoryEnumType.FAULT_3_PHASE);  
    					busFault.setFaultStartTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
    					busFault.setPermanentFault(true);        			
    				}else{	
    					if(busFault!=null){
    						busFault.setFirstOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
    						double duration= busFault.getFirstOperationTime().getValue()-busFault.getFaultStartTime().getValue();
    						busFault.setFaultDurationTime(DStabDataSetter.createTimePeriodValue(duration, TimePeriodUnitType.CYCLE));
    						busFault.setPermanentFault(false);
            			}
    				}
    			}
                
				if(r!=0.0||x!=0.0){
    				busFault.setFaultZ(DStabDataSetter.createZValue(r, x, ZUnitType.PU));
				}
    		}
			// branch fault
    		else if(mode==3||mode==-3){
    			DStabBranchFaultXmlType braFault=odmObjFactory.createDStabBranchFaultXmlType();	
				braFault.setFaultCategory(AcscFaultCategoryEnumType.FAULT_3_PHASE);
				
				if(breaker1Opened==false&&breaker2Opened==false){    				
        			braFault.setFromBus(bus1);
        			braFault.setFromBusRatedV(DStabDataSetter.createVoltageValue(bus1RatedV, VoltageUnitType.KV));
        			braFault.setToBus(bus2);
        			braFault.setToBusRatedV(DStabDataSetter.createVoltageValue(bus2RatedV, VoltageUnitType.KV));        			
        			braFault.setFaultLocationFromFromSide(faultLocation);
        			braFault.setFaultStartTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
        			braFault.setPermanentFault(true);
    			}else if((breaker1Opened==true&&breaker2Opened==false)||(breaker1Opened==false&&breaker2Opened==true)){    				
    				if(braFault!=null){
    					braFault.setFirstOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
    					braFault.setPermanentFault(true);
            			if(mode==-3){
            				double duration= braFault.getFirstOperationTime().getValue()-braFault.getFaultStartTime().getValue();
            				braFault.setFaultDurationTime(DStabDataSetter.createTimePeriodValue(duration, TimePeriodUnitType.CYCLE)); 
            				braFault.setPermanentFault(false);
            			}
    				}    				
    			}else if(breaker1Opened==true&&breaker2Opened==true){    				
    				if(braFault!=null){
    					braFault.setSecondOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
    					braFault.setPermanentFault(true);
        				if(mode==-3){
            				double duration= braFault.getSecondOperationTime().getValue()-braFault.getFaultStartTime().getValue();
            				braFault.setFaultDurationTime(DStabDataSetter.createTimePeriodValue(duration, TimePeriodUnitType.CYCLE)); 
        					braFault.setPermanentFault(false);
            			}            			
            		}
    			}
				
				if(r!=0.0||x!=0.0){
					braFault.setFaultZ(DStabDataSetter.createZValue(r, x, ZUnitType.PU));
    			} 
    		}
        }
        else if(mode==4||mode==-4){
    		// load change
    		if(strAry[12].equals("")){
    			String busId = BPABusRecord.getBusId(strAry[1]);
        		BusIDRefXmlType bus=parser.createBusRef(busId);
    			LoadChangeDynamicEventXmlType loadChange=odmObjFactory.createLoadChangeDynamicEventXmlType();
     		    loadChange.setBus(bus);
     		    
     		    double busRatedV=ModelStringUtil.getDouble(strAry[2], 0.0);
     		    loadChange.setBusRatedVoltage(DStabDataSetter.createVoltageValue(busRatedV, VoltageUnitType.KV));
     			double operationTime=ModelStringUtil.getDouble(strAry[5], 0.0);
     			loadChange.setOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
     		    
     			double pp=ModelStringUtil.getDouble(strAry[6], 0.0);
     		    double qp=ModelStringUtil.getDouble(strAry[7], 0.0);
     		    double pc=ModelStringUtil.getDouble(strAry[8], 0.0);
     		    double qc=ModelStringUtil.getDouble(strAry[9], 0.0);
    		    double pz=ModelStringUtil.getDouble(strAry[10], 0.0);
    		    double qz=ModelStringUtil.getDouble(strAry[11], 0.0);
       		    if(pp!=0.0||qp!=0.0){
       			    loadChange.setConstantPChange(DStabDataSetter.createPowerValue(pp, qp, ApparentPowerUnitType.MVA));
       		    }
       		    if(pc!=0.0||qc!=0.0){
       		    	loadChange.setConstantIChange(DStabDataSetter.createPowerValue(pc, qc, ApparentPowerUnitType.MVA));
       		    }
       		    if(pz!=0.0||qz!=0.0){
       		    	loadChange.setConstantZChange(DStabDataSetter.createPowerValue(pz, qz, ApparentPowerUnitType.MVA));
       		    }
    		}
    		//power change
    		else{
    			String busId = BPABusRecord.getBusId(strAry[1]);
        		BusIDRefXmlType bus=parser.createBusRef(busId);
    			GenChangeDynamicEventXmlType genChange= odmObjFactory.createGenChangeDynamicEventXmlType();
     		    genChange.setBus(bus);
     		    
     		    double busRatedV=ModelStringUtil.getDouble(strAry[2], 0.0);
    		    genChange.setBusRatedVoltage(DStabDataSetter.createVoltageValue(busRatedV, VoltageUnitType.KV));
         		double operationTime=ModelStringUtil.getDouble(strAry[5], 0.0);
     			genChange.setOperationTime(DStabDataSetter.createTimePeriodValue(operationTime, TimePeriodUnitType.CYCLE));
     			
    		    String genId="";
         		if(!strAry[3].equals("")){
         			genId=strAry[3];
         			genChange.setGeneratorId(genId);
         		}
         		
     			double pg=ModelStringUtil.getDouble(strAry[12], 0.0);
         		if(pg<90000){
     				genChange.setGenChange(DStabDataSetter.createPowerValue(pg, 0.0, ApparentPowerUnitType.MVA));
     			}else{
     				genChange.setGenOutage(true);
     			}
    		}
     	}
        else if(mode==5){
        	int sign=new Integer(str.substring(32, 33).trim()).intValue();
      	    if(sign>0){
      		    DcLineFaultXmlType dcFault=odmObjFactory.createDcLineFaultXmlType();
      		    dcFault.setPermanentFault(true);
      		  
      		    String bus1Id = BPABusRecord.getBusId(strAry[1]);
    		    BusIDRefXmlType bus1=parser.createBusRef(bus1Id);
    		    String bus2Id = BPABusRecord.getBusId(strAry[3]);
    		    BusIDRefXmlType bus2=parser.createBusRef(bus2Id);
 		        dcFault.setFromACBus(bus1);
 		        dcFault.setToACBus(bus2);
 		      
 		        double bus1RatedV=ModelStringUtil.getDouble(strAry[2], 0.0);
 		        double bus2RatedV=ModelStringUtil.getDouble(strAry[4], 0.0);
 		        dcFault.setFromACRatedVol(DStabDataSetter.createVoltageValue(bus1RatedV, VoltageUnitType.KV));
 		        dcFault.setToACRatedVol(DStabDataSetter.createVoltageValue(bus2RatedV, VoltageUnitType.KV)); 		      
  	          
 		        int faultType= new Integer(strAry[5]).intValue();
 		        if(faultType==1){
 		        	dcFault.setFaultType(DcLineFaultEnumType.FROM_BUS_BIPOLE_SHORT_CIRCUIT);
 		        }
 		        if(faultType==2){
 		        	dcFault.setFaultType(DcLineFaultEnumType.TO_BUS_BIPOLE_SHORT_CIRCUIT);
 		        }
 		        double faultLocation=ModelStringUtil.getDouble(strAry[10], 0.0);
 		        if(faultType==3){
 		        	dcFault.setFaultType(DcLineFaultEnumType.FAULT_ON_LINE);
 		        	dcFault.setFaultLocationFromFromSide(faultLocation);
 		        }
 		        if(faultType==4){
 		        	dcFault.setFaultType(DcLineFaultEnumType.POWER_BLOCKED);
 		        }
 		        if(faultType==5){
 		        	dcFault.setFaultType(DcLineFaultEnumType.POWER_REVERSED);
 		        }
 		        double changedScale=ModelStringUtil.getDouble(strAry[9], 0.0);
 		        if(faultType==7){
 		        	dcFault.setFaultType(DcLineFaultEnumType.POWER_CHANGE_BY_SPECIFICATION);
 		        	dcFault.setChangedPower(DStabDataSetter.createPowerValue(changedScale, 0.0, ApparentPowerUnitType.MVA));		  
 		        }
 		        if(faultType==8){
 		        	dcFault.setFaultType(DcLineFaultEnumType.CURRENT_CHANGE_BY_SPECIFICATION);
 		        	dcFault.setChangedCurrent(DStabDataSetter.createCurrentValue(changedScale, CurrentUnitType.KA));
 		        }
  	    	  
 		        double startTime=ModelStringUtil.getDouble(strAry[7], 0.0);
 		        dcFault.setStartTime(DStabDataSetter.createTimePeriodValue(startTime, TimePeriodUnitType.CYCLE));
 		        double faultR=ModelStringUtil.getDouble(strAry[8], 0.0);
 		        dcFault.setFaultZ(DStabDataSetter.createZValue(faultR, 0.0, ZUnitType.PU));  	    	  
      	    }else{
      	    	// DC line currently is not considered. We will deal with it late
      	    	String bus1Id = BPABusRecord.getBusId(strAry[1]);
      	    	BusIDRefXmlType bus1=parser.createBusRef(bus1Id);
      	    	String bus2Id = BPABusRecord.getBusId(strAry[3]);
      	    	BusIDRefXmlType bus2=parser.createBusRef(bus2Id);
      		  
      	    	//TODO I can't find the method to get the existed dcFault ,as the previous method:
      	    	//"DcLineFaultXmlType dcFault=XBeanParserHelper.getDCFaultRecord(tranSimu, bus1, bus2);"
      	    	//DcLineFaultXmlType dcFault=;		
      		  
      	    	//double clearedTime= new Double(strAry[7]).doubleValue();
      	    	//double durationTime=clearedTime-dcFault.getStartTime().getValue();
      	    	//dcFault.setDurationTime(DStabDataSetter.createTimePeriodValue(durationTime, TimePeriodUnitType.CYCLE));
      	    	//dcFault.setPermanentFault(false);  		  
      	    }
        }  
	}

	private static String[] getFaultOperationDataFields ( final String str, int mode) {
	final String[] strAry = new String[13];		
	try{
		if(mode==1||mode==2||mode==3||mode==-1||mode==-2||mode==-3){
			strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
			strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,4, 4).trim();
			strAry[2]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
			strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
			strAry[4]=ModelStringUtil.getStringReturnEmptyString(str,18, 18).trim();
			strAry[5]=ModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
			strAry[6]=ModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
			strAry[7]=ModelStringUtil.getStringReturnEmptyString(str,32, 32).trim();
			strAry[8]=ModelStringUtil.getStringReturnEmptyString(str,36, 37).trim();			
			strAry[9]=ModelStringUtil.getStringReturnEmptyString(str,40, 45).trim();
			strAry[10]=ModelStringUtil.getStringReturnEmptyString(str,46, 51).trim();
			strAry[11]=ModelStringUtil.getStringReturnEmptyString(str,52, 57).trim();
			strAry[12]=ModelStringUtil.getStringReturnEmptyString(str,58, 63).trim();
		}else if(mode==4){
			strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
			strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
			strAry[2]=ModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
			strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,17, 17).trim();
			//omit these two BPA data：
			//32 MOTOR，填写字符M，表示切除马达；否则，表示切除发电机 
			//34 ILE，如果要切除配电网支路的静态负荷，填写LE卡对应的支路号 
			strAry[4]=ModelStringUtil.getStringReturnEmptyString(str,37, 37).trim();
			strAry[5]=ModelStringUtil.getStringReturnEmptyString(str,40, 45).trim();
			strAry[6]=ModelStringUtil.getStringReturnEmptyString(str,46, 50).trim();
			strAry[7]=ModelStringUtil.getStringReturnEmptyString(str,51, 55).trim();
			strAry[8]=ModelStringUtil.getStringReturnEmptyString(str,56, 60).trim();
			strAry[9]=ModelStringUtil.getStringReturnEmptyString(str,61, 65).trim();
			strAry[10]=ModelStringUtil.getStringReturnEmptyString(str,66, 70).trim();
			strAry[11]=ModelStringUtil.getStringReturnEmptyString(str,71, 75).trim();
			strAry[12]=ModelStringUtil.getStringReturnEmptyString(str,76, 80).trim();
		}else if(mode==5){
			strAry[0]=ModelStringUtil.getStringReturnEmptyString(str,1, 2).trim();
			strAry[1]=ModelStringUtil.getStringReturnEmptyString(str,5, 12).trim();
			strAry[2]=ModelStringUtil.getStringReturnEmptyString(str,13, 16).trim();
			strAry[3]=ModelStringUtil.getStringReturnEmptyString(str,19, 26).trim();
			strAry[4]=ModelStringUtil.getStringReturnEmptyString(str,27, 30).trim();
			strAry[5]=ModelStringUtil.getStringReturnEmptyString(str,32, 33).trim();
			strAry[6]=ModelStringUtil.getStringReturnEmptyString(str,37, 37).trim();
			strAry[7]=ModelStringUtil.getStringReturnEmptyString(str,40, 45).trim();
			strAry[8]=ModelStringUtil.getStringReturnEmptyString(str,46, 51).trim();
			strAry[9]=ModelStringUtil.getStringReturnEmptyString(str,52, 57).trim();
			strAry[10]=ModelStringUtil.getStringReturnEmptyString(str,58, 63).trim();			
		}
	}catch(Exception e){
		ODMLogger.getLogger().severe(e.toString());
	}	
	return strAry;
}
}