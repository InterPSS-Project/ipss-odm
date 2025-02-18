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

import org.apache.commons.math3.complex.Complex;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEAclineDataJSonMapper extends BasePSSEDataJSonMapper{
	/**
	 * Constructor
	 * 
	 * @param fieldDef field name definitions
	 */
	public PSSEAclineDataJSonMapper(List<String> fieldDef) {
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
		
		/*
 			"fields":["ibus", "jbus", "ckt", "rpu", "xpu", "bpu", "name", "rate1", 
 					"rate2", "rate3", "rate4", "rate5", "rate6", "rate7", "rate8", "rate9", 
 					"rate10", "rate11", "rate12", "gi", "bi", "gj", "bj", "stat", "bp", "met", 
 					"len", "o1", "f1", "o2", "f2", "o3", "f3", "o4", "f4"], 
            "data":  [4, 5, "0", 0.1000000E-01, 0.8500000E-01, 0.1760000, "", 0.000000, 0.000000, 
            		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 
            		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 1, 0, 2, 0.000000, 
            		1, 1.000000, null, null, null, null, null, null], 
		 */		
		try {
			int i = dataParser.getInt("ibus");
			int j = dataParser.getInt("jbus");
			
			/* starting from V32
			 * MET	Metered end flag;
				<=1 to designate bus I as the metered end 
				=>2 to designate bus J as the metered end.
					MET = 1 by default.
			 */
			boolean fromMetered = true;
			int met = dataParser.getInt("met", 1);
			if (met >= 2)
				fromMetered = false;
	      	
			final String fid = IODMModelParser.BusIdPreFix+i;
			final String tid = IODMModelParser.BusIdPreFix+j;

			LineBranchXmlType braRecXml;
			try {
				braRecXml = (LineBranchXmlType) odmParser.createLineBranch(fid, tid, dataParser.getString("ckt"));
			} catch (ODMBranchDuplicationException e) {
				ODMLogger.getLogger().severe(e.toString());
				return;
			}		
			
			int status = dataParser.getInt("stat", 1);
			braRecXml.setOffLine(status != 1);
			
			braRecXml.setMeterLocation(fromMetered ? BranchBusSideEnumType.FROM_SIDE :
											BranchBusSideEnumType.TO_SIDE);
	      	
			double r = dataParser.getDouble("rpu", 0.0);
			double x = dataParser.getDouble("xpu", 0.0);
			double b = dataParser.getDouble("bpu", 0.0);
			
			//TODO Temporally bad data fix
			if(new Complex(r,x).abs()< 0.00001){
				x = 0.00001;
				ODMLogger.getLogger().severe("Line # "+braRecXml.getId()+", has zero impedance input, change X = 0.00001");
			}
			
			AclfDataSetter.setLineData(braRecXml, r, x, ZUnitType.PU, 0.0, b, YUnitType.PU);

			double rate1 = dataParser.getDouble("rate1", 0.0);
			double rate2 = dataParser.getDouble("rate2", 0.0);
			double rate3 = dataParser.getDouble("rate3", 0.0);
			braRecXml.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
			AclfDataSetter.setBranchRatingLimitData(braRecXml.getRatingLimit(),
	    				rate1, rate2, rate3, ApparentPowerUnitType.MVA);
	        
			double gi = dataParser.getDouble("gi", 0.0);
			double bi = dataParser.getDouble("bi", 0.0);
	       if ( gi != 0.0 || bi != 0.0)
	    	   braRecXml.setFromShuntY(BaseDataSetter.createYValue(gi, bi, YUnitType.PU));
	       
			double gj = dataParser.getDouble("gj", 0.0);
			double bj = dataParser.getDouble("bj", 0.0);
	       if ( gj != 0.0 || bj != 0.0)
	    	   braRecXml.setToShuntY(BaseDataSetter.createYValue(gj, bj, YUnitType.PU));
	       
	       mapMultiOwnerInfo(braRecXml);
		} catch (ODMException e) {
			ODMLogger.getLogger().severe(e.toString() + "\n" + this.dataParser.getFieldTable());
		}
	}
}
