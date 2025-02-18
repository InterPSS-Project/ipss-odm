/*
 * @(#)UCTENodeDataParser.java   
 *
 * Copyright (C) 2006-2013 www.interpss.org
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
 * @Date 04/11/2013
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.ucte.parser;

import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.adapter.common.str.AbstractStringDataFieldParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.ODMModelStringUtil;

/**
 * Class for processing UCTE node data line string
 * 
 * @author mzhou
 *
 */
public class UCTENodeDataParser extends AbstractStringDataFieldParser {
	
	public UCTENodeDataParser() {
	    super();
		initializeMetadata();
	}
	
    // custom base voltage is an extension to the UCTE std
	private List<Double> customBaseVoltageList = new ArrayList<Double>();
	public List<Double> getCustomBaseVoltageList() { return this.customBaseVoltageList; }
	
	private boolean customBaseVoltage = false;
	public void setCustomBaseVoltage(boolean b) { this.customBaseVoltage = b; }

	@Override public String[] getMetadata() {
		return new String[] {
		   //  0---------------1---------------2---------------3---------------4
			 "id",           "name",        "baseKv",        "status",      "nodeType",
		   //  5               6               7               8               9
			 "voltage",      "pLoadMW",     "qLoadMvar",     "pGenMW",      "qGenMvar", 
		   //  10              11              12              13              14
			 "minGenMW",     "maxGenMW",    "minGenMVar",    "maxGenMVar",  "staticPrimaryControl",
		   //  15              16              17              18           	
 "normalPowerPrimaryControl", "scMVA3P",     "x_rRatio",     "powerPlanType"
		};
	}
	
	@Override public void parseFields(final String str) throws ODMException {
		String id = ODMModelStringUtil.getString(str, 1, 8).trim().replace(' ', '_');
		this.setValue(0, id);
		String name = ODMModelStringUtil.getString(str, 10, 21).trim();
		this.setValue(1, name);

		double baseKv = 0.0;
		if (!customBaseVoltage)
			baseKv = getBaseVoltageKv(id);

		String status = ODMModelStringUtil.getString(str, 23, 23);  // 0 real, 1 equivalent
		this.setValue(3, status);
		String nodeType = ODMModelStringUtil.getString(str, 25, 25);  // 0 PQ, 1 QAng, 2 PV, 3 Swing
		this.setValue(4, nodeType);
		
		double voltage = ODMModelStringUtil.getDouble(str, 27, 32);  
		if (customBaseVoltage) {
			baseKv = findCustomBaseVoltage(voltage);
			if (baseKv == 0.0) {
				throw new ODMException("Custom base voltage lookup, cannot find proper value");
			}
		}
		else { 
			if (voltage == 0.0)
				voltage = baseKv;
		}
		this.setValue(2, new Double(baseKv).toString());
		this.setValue(5, new Double(voltage).toString());

		
		String pLoadMW = ODMModelStringUtil.getString(str, 34, 40);  
		this.setValue(6, pLoadMW);
		String qLoadMvar = ODMModelStringUtil.getString(str, 42, 48);  
		this.setValue(7, qLoadMvar);
		String pGenMW = ODMModelStringUtil.getString(str, 50, 56);    // UCTE assumes out next as the positive direction
		this.setValue(8, pGenMW);
		String qGenMvar = ODMModelStringUtil.getString(str, 58, 64);
		this.setValue(9, qGenMvar);
		
		// optional fields
		String minGenMW = ODMModelStringUtil.getString(str, 66, 72); 
		this.setValue(10, minGenMW);
		String maxGenMW = ODMModelStringUtil.getString(str, 74, 80);
		this.setValue(11, maxGenMW);
		String minGenMVar = ODMModelStringUtil.getString(str, 82, 88); 
		this.setValue(12, minGenMVar);
		String maxGenMVar = ODMModelStringUtil.getString(str, 90, 96); 
		this.setValue(13, maxGenMVar);
		String staticPrimaryControl = ODMModelStringUtil.getString(str, 98, 102); 
		this.setValue(14, staticPrimaryControl);
		String normalPowerPrimaryControl = ODMModelStringUtil.getString(str, 104, 110);
		this.setValue(15, normalPowerPrimaryControl);
		String scMVA3P = ODMModelStringUtil.getString(str, 112, 118);
		this.setValue(16, scMVA3P);
		String x_rRatio = ODMModelStringUtil.getString(str, 120, 126);
		this.setValue(17, x_rRatio);
		
		String powerPlanType = ODMModelStringUtil.getString(str, 128, 128);
		this.setValue(18, powerPlanType);
	}
	
    
    public boolean processBaseVoltageRecord(String str) {
    	customBaseVoltageList.add(new Double(str));
    	return true;
    }

    private double getBaseVoltageKv(String nodeId) throws ODMException {
        // According to the spec the node base voltage code is stored at the 7th char
    	int code = ODMModelStringUtil.getInt(nodeId, 7, 7);
    	return getBaseVoltageKv(code);
    }
    
    private double getBaseVoltageKv(int code) throws ODMException {
    	switch(code) {
    	case 0 : return 750.0;
    	case 1 : return 380.0;
    	case 2 : return 220.0;
    	case 3 : return 150.0;
    	case 4 : return 120.0;
    	case 5 : return 110.0;
    	case 6 : return 70.0;
    	case 7 : return 27.0;
    	case 8 : return 330.0;
    	case 9 : return 500.0;
    	default: 
    		throw new ODMException("Wrong base voltage code, " + code);
    	}
    }
    
	private double findCustomBaseVoltage(double voltage) {
		double baseKv = 0.0;
		double e = 1.0e10;
		for (Double bv : customBaseVoltageList) {
			if (Math.abs(voltage - bv.doubleValue()) < e) {
				baseKv = bv.doubleValue();
				e = Math.abs(voltage - bv.doubleValue());
			}
		}
		return baseKv;
	}
	
}