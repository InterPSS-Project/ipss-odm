 /*
  * @(#)PSSEAreaDataMapper.java   
  *
  * Copyright (C) 2006 www.interpss.org
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
  * @Author Mike Zhou
  * @Version 1.0
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.adapter.psse.json.mapper;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.util.List;

import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AdjustmentModeEnumType;
import org.ieee.odm.schema.AngleAdjustmentXmlType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.BranchFlowDirectionEnumType;
import org.ieee.odm.schema.MvarFlowAdjustmentDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.PSXfr3WBranchXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.TapAdjustmentEnumType;
import org.ieee.odm.schema.TapAdjustmentXmlType;
import org.ieee.odm.schema.Transformer3WInfoXmlType;
import org.ieee.odm.schema.TransformerInfoXmlType;
import org.ieee.odm.schema.VoltageAdjustmentDataXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.Xfr3WBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEXformerDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEXformerDataJSonMapper(List<String> fieldDef) {
		super(fieldDef);
	}
	
	/**
	 * map the data list into the DOM model parser 
	 * 
	 * @param data
	 * @param odmParser
	 */
	public void map(List<Object> data, BaseAclfModelParser<? extends NetworkXmlType> odmParser) {
		dataParser.loadFields(data.toArray());
		
		double sysMVABase=odmParser.getNet().getBasePower().getUnit()==ApparentPowerUnitType.MVA?
            	odmParser.getNet().getBasePower().getValue() : 
            	odmParser.getNet().getBasePower().getValue()*0.001;
		
		/*
            "fields":["ibus", "jbus", "kbus", "ckt", "cw", "cz", "cm", "mag1", "mag2", "nmet", 
            		"name", "stat", "o1", "f1", "o2", "f2", "o3", "f3", "o4", "f4", "vecgrp", 
            		"zcod", "r1_2", "x1_2", "sbase1_2", "r2_3", "x2_3", "sbase2_3", "r3_1", "x3_1", 
            		"sbase3_1", "vmstar", "anstar", "windv1", "nomv1", "ang1", "wdg1rate1", "wdg1rate2", 
            		"wdg1rate3", "wdg1rate4", "wdg1rate5", "wdg1rate6", "wdg1rate7", "wdg1rate8", 
            		"wdg1rate9", "wdg1rate10", "wdg1rate11", "wdg1rate12", "cod1", "cont1", "node1", 
            		"rma1", "rmi1", "vma1", "vmi1", "ntp1", "tab1", "cr1", "cx1", "cnxa1", "windv2", 
            		"nomv2", "ang2", "wdg2rate1", "wdg2rate2", "wdg2rate3", "wdg2rate4", "wdg2rate5", 
            		"wdg2rate6", "wdg2rate7", "wdg2rate8", "wdg2rate9", "wdg2rate10", "wdg2rate11", 
            		"wdg2rate12", "cod2", "cont2", "node2", "rma2", "rmi2", "vma2", "vmi2", "ntp2", 
            		"tab2", "cr2", "cx2", "cnxa2", "windv3", "nomv3", "ang3", "wdg3rate1", "wdg3rate2", 
            		"wdg3rate3", "wdg3rate4", "wdg3rate5", "wdg3rate6", "wdg3rate7", "wdg3rate8", "wdg3rate9", 
            		"wdg3rate10", "wdg3rate11", "wdg3rate12", "cod3", "cont3", "node3", "rma3", "rmi3", "vma3", 
            		"vmi3", "ntp3", "tab3", "cr3", "cx3", "cnxa3"], 
            "data": [1, 4, 0, "1", 1, 1, 1, 0.000000, 0.000000, 2, "GEN1_TO_BUS1_CIRID_1", 1, 1, 1.000000, 
            		0, 1.000000, 0, 1.000000, 0, 1.000000, "", null, 0.000000, 0.5670000E-01, 100.0000, null, 
            		null, null, null, null, null, null, null, 1.000000, 16.50000, 0.000000, 0.000000, 0.000000, 
            		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 
            		0.000000, 0, 0, 0, 1.100000, 0.9000000, 1.100000, 0.9000000, 33, 0, 0.000000, 0.000000, 
            		0.000000, 1.000000, 230.0000, null, null, null, null, null, null, null, null, null, null, 
            		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
            		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
            		null, null, null, null, null, null, null, null, null, null, null, null], 
		 */		
		try {
	      	int i = dataParser.getInt("ibus");
	      	int j = dataParser.getInt("jbus");                
	      	int k = dataParser.getInt("kbus");
	                
	        boolean is3WXfr = k != 0; 
	        
	        int cod = dataParser.getInt("cod1", 0);   
	        double ang1 = dataParser.getDouble("ang1", 0.0);
	        double ang2 = dataParser.getDouble("ang2", 0.0);
	        double ang3 = dataParser.getDouble("ang3", 0.0);
			boolean isPsXfr = false;
	    	if ( (is3WXfr && (ang1 != 0.0 || ang2 != 0.0 || ang3 != 0.0)) ||
	       		 (!is3WXfr && ang1 != 0.0) || cod == 3 || cod == -3) {
	       		isPsXfr = true; // PhaseShifting transformer branch
	       	}		
	/*
		    Line-1 
		    For 2W and 3W Xfr: 
		    	I,     J,     K,    CKT, CW,CZ,CM, MAG1,     MAG2,    NMETR,'NAME', STAT,O1,F1,...,O4,F4
		        26,    54,    0,    '1 ',1, 1, 1,  0.00000,  0.00000, 2,    '        ',    1,   1,1.0000,   0,1.0000,   0,1.0000,   0,1.0000
	            27824, 27871, 27957,'W ',2, 2, 1,  0.00089,  -0.00448,1,    'D575121     ',1,   1,1.0000
	*/
			final String fid = IODMModelParser.BusIdPreFix+i;
			final String tid = IODMModelParser.BusIdPreFix+j;
			final String tertId = IODMModelParser.BusIdPreFix+k;

			XfrBranchXmlType branRecXml;
			TransformerInfoXmlType xfrInfoXml;
			String ckt = dataParser.getString("ckt");
			if (is3WXfr && isPsXfr) {
				branRecXml = odmParser.createPSXfr3WBranch(fid, tid, tertId, ckt);
		       	xfrInfoXml = OdmObjFactory.createTransformer3WInfoXmlType(); 
		       	branRecXml.setXfrInfo(xfrInfoXml);
			}
			else if (is3WXfr) {
				branRecXml = (XfrBranchXmlType) odmParser.createXfr3WBranch(fid, tid, tertId, ckt);
		       	xfrInfoXml = OdmObjFactory.createTransformer3WInfoXmlType(); 
		       	branRecXml.setXfrInfo(xfrInfoXml);
			}
			else if (isPsXfr) {
				branRecXml = (XfrBranchXmlType) odmParser.createPSXfrBranch(fid, tid, ckt);
		       	xfrInfoXml = OdmObjFactory.createTransformerInfoXmlType(); 
		       	branRecXml.setXfrInfo(xfrInfoXml);
			}
			else {
				branRecXml = (XfrBranchXmlType) odmParser.createXfrBranch(fid, tid, ckt);
		       	xfrInfoXml = OdmObjFactory.createTransformerInfoXmlType(); 
		       	branRecXml.setXfrInfo(xfrInfoXml);
			}
			branRecXml.setName(dataParser.getString("name"));
			
	/*
			The initial transformer status, where 1 designates in-service and 0 designates
			out-of-service. In addition, for a three-winding transformer, 2 designates that
			only winding two is out-of-service, 3 indicates that only winding three is outof-
			service, and 4 indicates that only winding one is out-of-service, with the
			remaining windings in-service. STAT = 1 by default.
	 */
			int stat = dataParser.getInt("stat", 1);
			branRecXml.setOffLine(stat == 0);
			if (is3WXfr) {
	    		Xfr3WBranchXmlType branch3WRec = (Xfr3WBranchXmlType)branRecXml;			
	    		if (stat == 1) {
	    			branch3WRec.setWind1OffLine(false);
	    			branch3WRec.setWind2OffLine(false);
	    			branch3WRec.setWind3OffLine(false);
	    		}
	    		else if (stat == 2) {
	    			branch3WRec.setWind1OffLine(false);
	    			branch3WRec.setWind2OffLine(true);
	    			branch3WRec.setWind3OffLine(false);
	    		}
	    		else if (stat == 3) {
	    			branch3WRec.setWind1OffLine(false);
	    			branch3WRec.setWind2OffLine(false);
	    			branch3WRec.setWind3OffLine(true);
	    		}
	    		else if (stat == 4) {
	    			branch3WRec.setWind1OffLine(true);
	    			branch3WRec.setWind2OffLine(false);
	    			branch3WRec.setWind3OffLine(false);
	    		}
			}
	       	
	       	// rated voltage could be entered 0.0, Bus BaseVoltage should be used in this case
	        double nomv1 = dataParser.getDouble("nomv1", 0.0);
	        double nomv2 = dataParser.getDouble("nomv2", 0.0);
	        double nomv3 = dataParser.getDouble("nomv3", 0.0);
	       	if (nomv1 == 0.0)
	       		nomv1 = odmParser.getBus(fid).getBaseVoltage().getValue();
	       	if (nomv2 == 0.0)
	       		nomv2 = odmParser.getBus(tid).getBaseVoltage().getValue();
	       	if (is3WXfr && nomv3 == 0.0)
	       		nomv3 = odmParser.getBus(tertId).getBaseVoltage().getValue();
	       	
	       	/*
	       	 * The nonmetered end code of either 1 (for the Winding 1 bus) or 2 (for the Winding 2 bus). 
	       	 * In addition, for a three-winding transformer, 3 (for the Winding 3 bus) 
	       	 * is a valid specification of NMETR. NMETR = 2 by default.
	       	 */
	       	int nmetr = dataParser.getInt("nmet", 2);
			branRecXml.setMeterLocation( nmetr==1 ? BranchBusSideEnumType.FROM_SIDE :
											(nmetr==2 ? BranchBusSideEnumType.TO_SIDE : 
												BranchBusSideEnumType.TERTIARY_SIDE));

			/*
			CM - The magnetizing admittance I/O code that defines the units in which MAG1 and MAG2 are specified: 
				1 for complex admittance in pu on system MVA base and winding one bus voltage base; 
				2 for no load loss in watts and exciting current in pu on winding one to two base MVA and winding one nominal voltage.
				CM = 1 by default.
				
			MAG1, MAG2 The magnetizing conductance and susceptance, respectively, in pu on system
				MVA base and winding one bus voltage base when CM is 1; 
				MAG1 is the no load loss in watts and MAG2 is the exciting current in pu on winding one to
				two base MVA (SBASE1-2) and winding one nominal voltage (NOMV1)when CM is 2. 
				MAG1 = 0.0 and MAG2 = 0.0 by default. 
				For three-phase transformers or three-phase banks of single phase transformers, the three-phase noload
				loss should be entered.
				When CM is 1 and a non-zero MAG2 is specified, MAG2 should be entered as a negative quantity; 
				when CM is 2 and a non-zero MAG2 is specified, MAG2 should always be entered as a positive quantity.
	    	 */
		
			// sample data : 1,   0.00089,  -0.00448
			int cm = dataParser.getInt("cm", 1);
			double mag1 = dataParser.getDouble("mag1", 0.0);
			double mag2 = dataParser.getDouble("mag2", 0.0);
			// updated to fix a bug when "SBASE1-2" is blank, use default value = SBASE
			double sbase1_2 = dataParser.getDouble("sbase1_2",sysMVABase);
	    	if (cm == 2) {
	    		if (mag1 != 0.0 || mag2 != 0.0){
	    			//p=U1^2*g
	    			//I=U1*b
	    			double g_rv=mag1/(nomv1*nomv1)*1.0E-6;//real value of conductance
	    			
	    			double vbase=odmParser.getBus(fid).getBaseVoltage().getValue();
	                double Ybase=sysMVABase/(vbase*vbase);
	                double g_pu=g_rv/Ybase; // based on system base
	                
	                double Ybase_w12=sbase1_2/(nomv1*nomv1);
	                double b_rv=mag2*Ybase_w12;
	                double b_pu=b_rv/Ybase;
	    			
	    			branRecXml.setMagnitizingY(BaseDataSetter.createYValue(g_pu, b_pu, YUnitType.PU));
	    		}
	    	}
	    	else {
	    		if (mag1 != 0.0 || mag2 != 0.0)
	    			branRecXml.setMagnitizingY(BaseDataSetter.createYValue(mag1, mag2, YUnitType.PU));
	    	}
	      	
	    	super.mapMultiOwnerInfo(branRecXml);    	
		
	    	/*
	       	Line-2 
	       		format 2w: R1-2,X1-2,SBASE1-2
	       		format 3w: R1-2,X1-2,SBASE1-2,R2-3,X2-3,SBASE2-3,R3-1,X3-1,SBASE3-1,VMSTAR,ANSTAR
	       		
				VMSTAR The voltage magnitude at the hidden "star point" bus; entered in pu. VMSTAR = 1.0 by default.
				ANSTAR The bus voltage phase angle at the hidden "star point" bus; entered in degrees. ANSTAR = 0.0 by default.       		

	    	*/
	    	xfrInfoXml.setRatedPower(BaseDataSetter.createPowerMvaValue(sbase1_2));
	       	if (is3WXfr) {
	       		double sbase2_3 = dataParser.getDouble("sbase2_3",sysMVABase);
	       		double sbase3_1 = dataParser.getDouble("sbase3_1",sysMVABase);
	       		
	       		double vmstar = dataParser.getDouble("vmstar");
	       		double anstar = dataParser.getDouble("anstar");
	    		Transformer3WInfoXmlType xfr3WInfo = (Transformer3WInfoXmlType)xfrInfoXml;
	       		xfr3WInfo.setRatedPower23(BaseDataSetter.createPowerMvaValue(sbase2_3));
	       		xfr3WInfo.setRatedPower31(BaseDataSetter.createPowerMvaValue(sbase3_1));
	       		xfr3WInfo.setStarVMag(BaseDataSetter.createVoltageValue(vmstar, VoltageUnitType.PU));
	       		xfr3WInfo.setStarVAng(BaseDataSetter.createAngleValue(anstar, AngleUnitType.DEG));
	       	}

	       	/*
			CZ The impedance data I/O code defines the units in which the winding impedances R1-2, X1-2, R2-3, X2-3, R3-1 and X3-1 are specified:
				1 for resistance and reactance in pu on system MVA base and  winding voltage base
				2 for resistance and reactance in pu on a specified MVA base and  winding voltage base
				3 for transformer load loss in watts and impedance magnitude in pu  on a specified MVA base and winding voltage base.
				In specifying transformer leakage impedances, the base voltage values are always the nominal winding voltages that are specified on 
				the third, fourth and fifth records of the transformer data block (NOMV1, NOMV2 and NOMV3). If the default NOMVn 
				is not specified, it is assumed to be identical to the winding n bus base voltage. 
				
				CZ = 1 by default.
	       	 */
	       	int cz = dataParser.getInt("cz", 1);
	       	double r1_2 = dataParser.getDouble("r1_2", 0.0);
	       	double x1_2 = dataParser.getDouble("x1_2", 0.0);
	       	if (cz == 1) {
	       		// When CZ is 1, they are the resistance and reactance, respectively, in pu on 
	       		// system base quantities; 
	       		branRecXml.setZ(BaseDataSetter.createZValue(r1_2, x1_2, ZUnitType.PU));
	        	xfrInfoXml.setDataOnSystemBase(true);
	        	// TODO This system base attribute is updated by both cz and cw, might cause some problems which are hard to identified;
	       	    //It might be better to change these basic settings to system base,to avoid potential problem.However, 
	        	// these might make it hard for direct comparison
	       	}
	       	else if (cz == 2) {
	       		// when CZ is 2, they are the resistance and reactance, respectively, in pu on 
	       		// winding one to two base MVA (SBASE1-2) and winding one bus base voltage; 
	       		r1_2=r1_2*sysMVABase/sbase1_2;
	       		x1_2=x1_2*sysMVABase/sbase1_2;
	       		branRecXml.setZ(BaseDataSetter.createZValue(r1_2, x1_2, ZUnitType.PU));
	        	xfrInfoXml.setDataOnSystemBase(true);
	       	}
	       	else if (cz == 3) {
	       		// when CZ is 3, R1-2 is the load loss in watts, and X1-2 is the impedance magnitude 
	       		// in pu on winding one to two base MVA (SBASE1-2) and winding one bus base voltage.
	       		double zpu = x1_2*sysMVABase/sbase1_2;
	       		double rpu = r1_2 * 0.001 * 0.001 /sysMVABase;  
	       		branRecXml.setZ(BaseDataSetter.createZValue(rpu, Math.sqrt(zpu*zpu - rpu*rpu), ZUnitType.PU));
	        	xfrInfoXml.setDataOnSystemBase(true);
	       	}
	       	
	       	if (is3WXfr) {
	    		double sbase2_3 = dataParser.getDouble("sbase2_3",sysMVABase);
	    		double sbase3_1 = dataParser.getDouble("sbase3_1",sysMVABase);
	           	double r2_3 = dataParser.getDouble("r2_3", 0.0);
	           	double x2_3 = dataParser.getDouble("x2_3", 0.0);
	           	double r3_1 = dataParser.getDouble("r3_1", 0.0);
	           	double x3_1 = dataParser.getDouble("x3_1", 0.0);
	    		Xfr3WBranchXmlType branch3WRec = (Xfr3WBranchXmlType)branRecXml;
	           	if (cz == 1) {
	           		branch3WRec.setZ23(BaseDataSetter.createZValue(r2_3, x2_3, ZUnitType.PU));
	           		branch3WRec.setZ31(BaseDataSetter.createZValue(r3_1, x3_1, ZUnitType.PU));
	           	}
	           	else if (cz == 2) {
	           		r2_3*=sysMVABase/sbase2_3;
	           		x2_3*=sysMVABase/sbase2_3;
	           		r3_1*=sysMVABase/sbase3_1;
	           		x3_1*=sysMVABase/sbase3_1;
	           		branch3WRec.setZ23(BaseDataSetter.createZValue(r2_3, x2_3, ZUnitType.PU));
	           		branch3WRec.setZ31(BaseDataSetter.createZValue(r3_1, x3_1, ZUnitType.PU));
	           	}
	           	else if (cz == 3) {
	           		double zpu = x2_3*sysMVABase/sbase2_3;
	           		double rpu = r2_3  * 0.001 * 0.001 / sysMVABase;  
	           		branch3WRec.setZ23(BaseDataSetter.createZValue(rpu, Math.sqrt(zpu*zpu - rpu*rpu), ZUnitType.PU));
	           		
	           		zpu = x3_1*sysMVABase/sbase3_1;
	           		rpu = r3_1  * 0.001 * 0.001 / sysMVABase;  
	           		branch3WRec.setZ31(BaseDataSetter.createZValue(rpu, Math.sqrt(zpu*zpu - rpu*rpu), ZUnitType.PU));
	           	}
	       	}
			      	
	    	/*
			 Line-3 
			 	format 2W and 3W: 
			 	 	WINDV1,  NOMV1,     ANG1,    RATA1,RATB1,RATC1,          COD,    CONT,RMA,     RMI,      VMA,     VMI,      NTP,TAB,CR,CX
	            	352.001, 360.000,   0.000,   150.00,   150.00,   150.00, 0,      0,   540.0000,183.6000, 1.50000, 0.51000,  33, 0, 0.00000, 0.00000
			 	
			CW The winding data I/O code defines the units in which the turns ratios WINDV1, WINDV2 and WINDV3 are specified 
			   (the units of RMAn and RMIn are also governed by CW when |CODn| is 1 or 2):
				1 for off-nominal turns ratio in pu of winding bus base voltage
				2 for winding voltage in kV
				3 for off-nominal turns ratio in pu of nominal winding voltage,  NOMV1, NOMV2 and NOMV3.
				CW = 1 by default.
	       	 */
	       	int cw = dataParser.getInt("cw", 1);
	       	double windv1 = dataParser.getDouble("windv1");
	  		if (cw == 1) {
	       		// The winding one off-nominal turns ratio in pu of winding one bus base voltage
	       		// when CW is 1; WINDV1 is 1.0 by default. 
	        	xfrInfoXml.setDataOnSystemBase(true);
	       	}
	       	else if (cw == 2) {
	       		// WINDV1 is the actual winding one voltage in kV when CW is 2; 
	       		// TODO It seems not proper to use system base here, since the turnRatio is, in fact, still based on system base.
	        	//xfrInfo.setDataOnSystemBase(false);
	       		windv1 /=odmParser.getBus(fid).getBaseVoltage().getValue();
	       		
	       	}
	  		/*
	  		if (!xfrInfo.isDataOnSystemBase()) {
	  			windv1 /= nomv1; //NOMV1 is used only in converting magnetizing data between per unit admittance values and physical units when CM is 2
	  			xfrInfo.setFromRatedVoltage(BaseDataSetter.createVoltageValue(nomv1, VoltageUnitType.KV));
	  		}
	  		*/
	  		branRecXml.setFromTurnRatio(BaseDataSetter.createTurnRatioPU(windv1));
		
	    	if (isPsXfr && is3WXfr) {
	    		PSXfr3WBranchXmlType branchPsXfr = (PSXfr3WBranchXmlType)branRecXml; 
				branchPsXfr.setFromAngle(BaseDataSetter.createAngleValue(ang1, AngleUnitType.DEG));
	    	}
	    	else if (isPsXfr) {
	    		PSXfrBranchXmlType branchPsXfr = (PSXfrBranchXmlType)branRecXml; 
				branchPsXfr.setFromAngle(BaseDataSetter.createAngleValue(ang1, AngleUnitType.DEG));
	    	}
	    	
	       	double rata1 = dataParser.getDouble("wdg1rate1"); 
	       	double ratb1 = dataParser.getDouble("wdg2rate1", 0.0);
	       	double ratc1 = dataParser.getDouble("wdg3rate1", 0.0);
	    	branRecXml.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
	    	AclfDataSetter.setBranchRatingLimitData(branRecXml.getRatingLimit(), rata1, ratb1, ratc1, ApparentPowerUnitType.MVA);
			
			/*
			 * The transformer control mode for automatic adjustments of the winding one
				tap or phase shift angle during power flow solutions: 0 for no control (fixed tap
				and phase shift); 
				+-1 for voltage control; 
				+-2 for reactive power flow control; 
				+-3 for active power flow control; 
				+-4 for control of a dc line quantity (+4 is valid only for two-winding transformers). 
				
				If the control mode is entered as a positive
				number, automatic adjustment of this transformer winding is enabled when the
				corresponding adjustment is activated during power flow solutions;
				 
				a negative control mode suppresses the automatic adjustment of this transformer winding.
				COD1 = 0 by default.

			If CONT is entered as a positive number, or a quoted extended bus name, the ratio is
				adjusted as if bus CONT is on the winding two or winding three side of the
				transformer; if CONT is entered as a negative number, or a quoted extended
				bus name with a minus sign preceding the first character, the ratio is adjusted
				as if bus |CONT| is on the winding one side of the transformer.
	      	 */
		
	      	boolean onFromSide = false;
	      	int cont = dataParser.getInt("cont1", 0);  
	      	if (cont < 0) {
	      		cont = -cont;
	      		onFromSide = true;
	      	}
	      	
	      	String reBusId = IODMModelParser.BusIdPreFix+cont;
	      	if(cont == 0)reBusId=fid;//by default set to the winding one bus
	      		
			// COD1,CONT1,RMA,RMI,VMA,VMI,NTP,TAB1, 
			//Sample data : 1,    31, 1.10000, 0.90000, 1.09255, 1.04255, 33, 0, 0.00000, 0.00000
	      	/*
	        RMA1, RMI1 The upper and lower limits, respectively, of either:
					Off-nominal turns ratio in pu of winding one bus base voltage when
						|COD1| is 1 or 2 and CW is 1; RMA1 = 1.1 and RMI1 = 0.9 by default.
					actual winding one voltage in kV when |COD1| is 1 or 2 and CW is 2. No
						default is allowed.
					phase shift angle in degrees when |COD1| is 3. No default is allowed.
					not used when |COD1| is 0 or 4; RMA1 = 1.1 and RMI1 = 0.9 by default.
			VMA1, VMI1 The upper and lower limits, respectively, of either:
					voltage at the controlled bus (bus |CONT1|) in pu when |COD1| is 1.
						VMA1 = 1.1 and VMI1 = 0.9 by default.
					reactive power flow into the transformer at the winding one bus end in
						Mvar when |COD1| is 2. No default is allowed.
					active power flow into the transformer at the winding one bus end in MW
						when |COD1| is 3. No default is allowed.
					not used when |COD1| is 0 or 4; VMA1 = 1.1 and VMI1 = 0.9 by default.
			NTP1 The number of tap positions available; used when COD1 is 1 or 2. NTP1 must be
					between 2 and 9999. NTP1 = 33 by default.
	      	 */
	      	if (cod > 0) {
	      		double rma = dataParser.getDouble("rma1",1.1);  
	      		double rmi = dataParser.getDouble("rmi1",0.9);
	      		double vma = dataParser.getDouble("vma1");
	      		double vmi = dataParser.getDouble("vmi1");
	      		// TODO PsXfr can also adjust the tap
	      		//In the Mod_SixBus_2WPsXfr.raw, the phase shift xfr(bus5->bus6) is used to control MVAR
	    	    
	      		//if (!isPsXfr) {
	      		 if(Math.abs(cod) != 3){
	           		TapAdjustmentXmlType tapAdj = OdmObjFactory.createTapAdjustmentXmlType();
	           		branRecXml.setTapAdjustment(tapAdj);
	           		tapAdj.setOffLine(cod < 0);
	           		// PSS/E control tap is always on the from side
	           		tapAdj.setTapAdjOnFromSide(true);
	           		// cw=1, RMA, RMI are Off-nominal turns ratio in pu of winding one bus base voltage
	           		// cw=2, RMA, RMI are Actual winding one voltage in kV 
	           		if(cw==2){
	           			rma/=odmParser.getBus(fid).getBaseVoltage().getValue();
	           			rmi/=odmParser.getBus(fid).getBaseVoltage().getValue();
	           		}
	           		tapAdj.setTapLimit(BaseDataSetter.createTapLimit(rma, rmi));
	           		int ntp = dataParser.getInt("npt1");  
	           		tapAdj.setTapAdjSteps(ntp);
	           		if (Math.abs(cod) == 1) {
	           			
	               		tapAdj.setAdjustmentType(TapAdjustmentEnumType.VOLTAGE);
	               		VoltageAdjustmentDataXmlType vAdjData = OdmObjFactory.createVoltageAdjustmentDataXmlType();
	        	    	tapAdj.setVoltageAdjData(vAdjData);
	        	    	//add adjust votlage bus
	        	    	vAdjData.setAdjVoltageBus(odmParser.createBusRef(reBusId));
	        	    	vAdjData.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
	        	    	vAdjData.setRange(OdmObjFactory.createLimitXmlType());
	        	    	vAdjData.getRange().setMax(vma);
	        	    	vAdjData.getRange().setMin(vmi);       
	        	    }
	           		else {
	                 	tapAdj.setAdjustmentType(TapAdjustmentEnumType.M_VAR_FLOW);
	                 	MvarFlowAdjustmentDataXmlType mvaAdjData = OdmObjFactory.createMvarFlowAdjustmentDataXmlType(); 
	        	    	tapAdj.setMvarFlowAdjData(mvaAdjData);
	        	    	mvaAdjData.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
	        	    	mvaAdjData.setRange(OdmObjFactory.createLimitXmlType());
	        	    	mvaAdjData.getRange().setMax(vma);
	        	    	mvaAdjData.getRange().setMin(vmi);               		
	           		}
	          	}
	    	    else {//COD =3 phase shifting adjustment
	    	    	
	    	    	PSXfrBranchXmlType branchPsXfr = (PSXfrBranchXmlType)branRecXml; 
	    	    	AngleAdjustmentXmlType angAdj = OdmObjFactory.createAngleAdjustmentXmlType();
	    	    	branchPsXfr.setAngleAdjustment(angAdj);
	    	    	angAdj.setAngleLimit(BaseDataSetter.createAngleLimit(rma, rmi, AngleUnitType.DEG));
	    	    	angAdj.setRange(OdmObjFactory.createLimitXmlType());
	    	    	/*
	    	    	 active power flow into the transformer at the winding one bus end in MW when |COD1| is 3. 
	    	    	 */
	    	    	angAdj.getRange().setMax(vma);
	    	    	angAdj.getRange().setMin(vmi);
	    	    	angAdj.setFlowDirection(BranchFlowDirectionEnumType.FROM_TO);
	    	    	angAdj.setDesiredActivePowerUnit(ActivePowerUnitType.MW);
	    	    	angAdj.setMode(AdjustmentModeEnumType.RANGE_ADJUSTMENT);
	    	    	angAdj.setDesiredMeasuredOnFromSide(onFromSide);
	    	    	// PSS/E PsXfr angle is defined at the fromside
	    	    	angAdj.setAngleAdjOnFromSide(true);
	    	    	angAdj.setDesiredMeasuredOnFromSide(branRecXml.getMeterLocation() == BranchBusSideEnumType.FROM_SIDE);
	    	    }              	
	      	}
	      	
	      	/*
	      	 * CR1, CX1 - The load drop compensation impedance for voltage controlling transformers
							entered in pu on system base quantities; used when COD1 is 1.
							CR1 + j CX1 = 0.0 by default
	      	 */
	      	double cr = dataParser.getDouble("cr1", 0.0);
	      	double cx = dataParser.getDouble("cx1", 0.0);
	      	if (cr != 0.0 || cx != 0.0) {
	      		///if (branchRec.getNvPairList() == null)
	      		//	branchRec.setNvPairList(odmObjFactory.createNameValuePairListXmlType());
	      		BaseJaxbHelper.addNVPair(branRecXml, "Xfr LoadDropCZ", new Double(cr).toString() + "," + new Double(cx).toString());
	      	}

	      	/*
	      	 	TAB1 The number of a transformer impedance correction table if this transformer
					winding impedance as a function of off-nominal turns ratio or
					phase shift angle (see Section 4.1.1.11), or 0 if no transformer impedance correction
					is to be applied to this transformer winding. TAB1 = 0 by default.					
	      	 */
	      	int tab = dataParser.getInt("tab1", 0);
	      	if (tab > 0)
	      		xfrInfoXml.setZTableNumber(tab);
	      	
	  		/*
			Line-4 
				format 2W : WINDV2,NOMV2
				format 3W : 
				    WINDV2,  NOMV2,     ANG2,    RATA2,RATB2,RATC2,          COD2,   CONT2,RMA2,RMI2,VMA2,VMI2,              NTP2,TAB2,CR2,CX2
					137.500, 137.500,   0.000,   150.00,   150.00,   150.00, 0,      0, 0.00000, 0.00000, 0.00000, 0.00000,  33, 0, 0.00000, 0.00000
				
			
				WINDV2 The winding two off-nominal turns ratio in pu of winding two bus base voltage
						when CW is 1; WINDV2 = 1.0 by default. WINDV2 is the actual winding two
						voltage in kV when CW is 2; WINDV2 is equal to the base voltage of bus J by
						default.	
				NOMV2 The nominal (rated) winding two voltage in kV, or zero to indicate that nominal
						winding two voltage is to be taken as the base voltage of bus J. NOMV2
						is present for information purposes only; it is not used in any of the calculations
						for modeling the transformer. NOMV2 = 0.0 by default.								
	  		 */
	      	/*
	  		if (!xfrInfo.isDataOnSystemBase()) {
	  			windv2 /= nomv2;
	  			xfrInfo.setToRatedVoltage(BaseDataSetter.createVoltageValue(nomv2, VoltageUnitType.KV));
	  		}
	  		*/
	      	double windv2 = dataParser.getDouble("windv2");
	      	if(cw==2) 
	      		windv2 /=odmParser.getBus(tid).getBaseVoltage().getValue();
	  		branRecXml.setToTurnRatio(BaseDataSetter.createTurnRatioPU(windv2));

	  		if (is3WXfr) {
	  	       	double rata2 = dataParser.getDouble("wdg1rate2");
	  	       	double ratb2 = dataParser.getDouble("wdg2rate2", 0.0);
	  	       	double ratc2 = dataParser.getDouble("wdg3rate2", 0.0);
	    		Xfr3WBranchXmlType branch3WXfr = (Xfr3WBranchXmlType)branRecXml; 
	    		branch3WXfr.setRatingLimit23(OdmObjFactory.createBranchRatingLimitXmlType());
	       		AclfDataSetter.setBranchRatingLimitData(branch3WXfr.getRatingLimit23(), rata2, ratb2, ratc2, ApparentPowerUnitType.MVA);
	       	}
	       	else if (isPsXfr) {
	    		PSXfrBranchXmlType branchPsXfr = (PSXfrBranchXmlType)branRecXml; 
	       		branchPsXfr.setToAngle(BaseDataSetter.createAngleValue(ang2, AngleUnitType.DEG));
	       	}


	       	/*
			Line-5 
				format 2W : N/A
				format 3W : 
					WINDV3,   NOMV3,  ANG3,       RATA3,RATB3,RATC3,         COD3,   CONT3,RMA3,RMI3,VMA3,VMI3,              NTP3,TAB3,CR3,CX3
	            	34.5000,  34.500, -30.000,    22.29,    22.29,    22.29, 0,      0, 0.00000, 0.00000, 0.00000, 0.00000,  33, 0, 0.00000, 0.00000
			*/
	       	if (is3WXfr) {
	    		Xfr3WBranchXmlType branch3WXfr = (Xfr3WBranchXmlType)branRecXml; 
	    		
	    		/*Transformer3WInfoXmlType xfr3WInfo = (Transformer3WInfoXmlType)xfrInfo;
	      		if (!xfrInfo.isDataOnSystemBase()) {
	      			windv3 /= nomv3;
	      			xfr3WInfo.setTertRatedVoltage(BaseDataSetter.createVoltageValue(nomv3, VoltageUnitType.KV));
	      		}
	      		*/
	          	double windv3 = dataParser.getDouble("windv3");
	           	double rata3 = dataParser.getDouble("wdg1rate3");
	           	double ratb3 = dataParser.getDouble("wdg2rate3", 0.0);
	           	double ratc3 = dataParser.getDouble("wdg3rate3", 0.0);
	    		if(cw==2)windv3 /=odmParser.getBus(tertId).getBaseVoltage().getValue();
	      		branch3WXfr.setTertTurnRatio(BaseDataSetter.createTurnRatioPU(windv3));
	      		branch3WXfr.setRatingLimit13(OdmObjFactory.createBranchRatingLimitXmlType());
	           	AclfDataSetter.setBranchRatingLimitData(branch3WXfr.getRatingLimit13(), rata3, ratb3, ratc3, ApparentPowerUnitType.MVA);
	           	if (isPsXfr) {
	        		PSXfr3WBranchXmlType branchPsXfr3W = (PSXfr3WBranchXmlType)branRecXml; 
	        		branchPsXfr3W.setTertShiftAngle(BaseDataSetter.createAngleValue(ang3, AngleUnitType.DEG));
	           	}
	       	}
		} catch (ODMException | ODMBranchDuplicationException e) {
			ODMLogger.getLogger().severe(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	
	}
}
