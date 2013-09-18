 /*
  * @(#)AclfModelComparator.java   
  *
  * Copyright (C) 2009 www.interpss.org
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
  * @Date 04/11/2009
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.aclf;

import java.util.List;

import org.ieee.odm.common.ODMNumericUtil;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.interpss.numeric.util.NumericUtil;

/**
 * Aclf model comparator for testing and verification purpose
 * 
 * @author mzhou
 *
 */
public class AclfModelComparator {
	/**
	 * compare bus objects: b1 and b2. The error is store in the msgList
	 * 
	 * @param base
	 * @param bus
	 * @param msgList
	 */
	public static void compare(LoadflowBusXmlType base, LoadflowBusXmlType bus, List<String> msgList, 
			                   String baseStr, String format) {
		String id = base.getId() + ", " + base.getName().trim();
		
		if (base.getBaseVoltage().getValue() != bus.getBaseVoltage().getValue())
			msgList.add("\nBus base voltage not equal: " + id + ", " + base.getBaseVoltage().getValue() + baseStr);
/*
                <genData>
                    <equivGen code="PV">
                        <power unit="MVA" im="0.0" re="3570.0"/>
                        <desiredVoltage unit="PU" value="1.049"/>
                        <qLimit unit="MVAR" active="false" min="0.0" max="880.8"/>
                    </equivGen>
                </genData>
*/
		if (base.getGenData() != null && bus.getGenData() != null) {
			if (base.getGenData().getEquivGen().getValue().getCode() != bus.getGenData().getEquivGen().getValue().getCode())
				msgList.add("\nBus EquivGen code not equal: " + id + ", " + 
						base.getGenData().getEquivGen().getValue().getCode() + baseStr + 
						"  " + bus.getGenData().getEquivGen().getValue().getCode() + format);
			if (base.getGenData().getEquivGen().getValue().getCode() == LFGenCodeEnumType.PV) {
				if (!NumericUtil.equals(base.getGenData().getEquivGen().getValue().getPower().getRe(), bus.getGenData().getEquivGen().getValue().getPower().getRe()))
					msgList.add("\nBus EquivGen power not equal: " + id + " " + base.getGenData().getEquivGen().getValue().getCode() 
							+ "   " + BaseJaxbHelper.toStr(base.getGenData().getEquivGen().getValue().getPower()) + baseStr
							+ "   " + BaseJaxbHelper.toStr(bus.getGenData().getEquivGen().getValue().getPower()) + format);
				if (!ODMNumericUtil.equals(base.getGenData().getEquivGen().getValue().getDesiredVoltage(), 
						                bus.getGenData().getEquivGen().getValue().getDesiredVoltage()))
					msgList.add("\nBus EquivGen desiredVoltage not equal: " + id + " " + base.getGenData().getEquivGen().getValue().getCode() 
							+ "   " + BaseJaxbHelper.toStr(base.getGenData().getEquivGen().getValue().getDesiredVoltage()) + baseStr
							+ "   " + BaseJaxbHelper.toStr(bus.getGenData().getEquivGen().getValue().getDesiredVoltage()) + format);
			}
			else if (base.getGenData().getEquivGen().getValue().getCode() == LFGenCodeEnumType.PQ) {
				if (!ODMNumericUtil.equals(base.getGenData().getEquivGen().getValue().getPower(), bus.getGenData().getEquivGen().getValue().getPower()))
					msgList.add("\nBus EquivGen power not equal: " + id + " " + base.getGenData().getEquivGen().getValue().getCode()
							+ "   " + BaseJaxbHelper.toStr(base.getGenData().getEquivGen().getValue().getPower()) + baseStr
							+ "   " + BaseJaxbHelper.toStr(bus.getGenData().getEquivGen().getValue().getPower()) + format);
			}
			else if (base.getGenData().getEquivGen().getValue().getCode() == LFGenCodeEnumType.SWING) {
				if (!ODMNumericUtil.equals(base.getGenData().getEquivGen().getValue().getDesiredVoltage(), 
		                bus.getGenData().getEquivGen().getValue().getDesiredVoltage()))
					msgList.add("\nBus EquivGen desiredVoltage not equal: " + id + " " + base.getGenData().getEquivGen().getValue().getCode()
							+ "   " + BaseJaxbHelper.toStr(base.getGenData().getEquivGen().getValue().getDesiredVoltage()) + baseStr
							+ "   " + BaseJaxbHelper.toStr(bus.getGenData().getEquivGen().getValue().getDesiredVoltage()) + format);
			}
		}
		else if (base.getGenData() == null && bus.getGenData() != null 
						&& bus.getGenData().getEquivGen().getValue().getCode() != LFGenCodeEnumType.NONE_GEN ||
				 base.getGenData() != null && bus.getGenData() == null 
				 		&& base.getGenData().getEquivGen().getValue().getCode() != LFGenCodeEnumType.NONE_GEN) {
			msgList.add("\nBus EquivGen model not equal: " + id + ", " + 
					base.getGenData().getEquivGen().getValue().getCode() + baseStr + 
					"  " + bus.getGenData().getEquivGen().getValue().getCode() + format);
		}	
			
/*
                <loadData>
                    <equivLoad code="CONST_P">
                        <constPLoad unit="MVA" im="442.6" re="700.0"/>
                    </equivLoad>
                </loadData>
*/
		if (base.getLoadData() != null && bus.getLoadData() != null) {
			if (base.getLoadData().getEquivLoad().getValue().getCode() != bus.getLoadData().getEquivLoad().getValue().getCode())
				msgList.add("\nBus EquivLoad code not equal: " + id + ", " + 
						base.getLoadData().getEquivLoad().getValue().getCode() + baseStr + 
						"  " + bus.getLoadData().getEquivLoad().getValue().getCode() + format);
			if (!ODMNumericUtil.equals(base.getLoadData().getEquivLoad().getValue().getConstPLoad(), bus.getLoadData().getEquivLoad().getValue().getConstPLoad()))
					msgList.add("\nBus EquivLoad constP not equal: " + id 
							+ "   " + BaseJaxbHelper.toStr(base.getLoadData().getEquivLoad().getValue().getConstPLoad()) + baseStr
							+ "   " + BaseJaxbHelper.toStr(bus.getLoadData().getEquivLoad().getValue().getConstPLoad()) + format);
		}
		else if (base.getLoadData() == null && bus.getLoadData() != null 
						&& bus.getLoadData().getEquivLoad().getValue().getCode() != LFLoadCodeEnumType.NONE_LOAD ||
				 base.getLoadData() != null && bus.getLoadData() == null 
				 		&& base.getLoadData().getEquivLoad().getValue().getCode() != LFLoadCodeEnumType.NONE_LOAD) {
			msgList.add("\nBus EquivLoad model not equal: " + id + ", " + 
					base.getLoadData().getEquivLoad().getValue().getCode() + baseStr + 
					"  " + bus.getLoadData().getEquivLoad().getValue().getCode() + format);
		}
		
/*
                <shuntY unit="PU" im="-1.4" re="5.3"/>
*/
		if (base.getShuntYData().getEquivY().getY() != null && bus.getShuntYData().getEquivY().getY() != null) {
			if (!ODMNumericUtil.equals(base.getShuntYData().getEquivY().getY(), bus.getShuntYData().getEquivY().getY()))
				msgList.add("\nBus ShuntY not equal: " + id 
						+ "   " + BaseJaxbHelper.toStr(base.getShuntYData().getEquivY().getY()) + baseStr
						+ "   " + BaseJaxbHelper.toStr(bus.getShuntYData().getEquivY().getY()) + format);
		}
		else if (base.getShuntYData().getEquivY().getY() == null && bus.getShuntYData().getEquivY().getY() != null ||
				 base.getShuntYData().getEquivY().getY() != null && bus.getShuntYData().getEquivY().getY() == null) {
			msgList.add("\nBus ShuntY model not equal: " + id);
		}
		
	}

	/**
	 * compare aclf line objects: b1 and b2. The error is store in the msgList
	 * 
	 * @param base
	 * @param bra
	 * @param msgList
	 */
	public static void compare(BranchXmlType base, BranchXmlType bra, IODMModelParser baseParser, List<String> msgList, 
								String baseFormat, String format) {
		AclfModelParser aclfParser = (AclfModelParser)baseParser;
		BusXmlType baseFromBus = aclfParser.getBus(BaseJaxbHelper.getRecId(base.getFromBus()));
		BusXmlType BASEtoBus = aclfParser.getBus(BaseJaxbHelper.getRecId(base.getToBus()));
		String braId = baseFromBus.getName().trim() + "->" + BASEtoBus.getName().trim() + "_" + base.getCircuitId();
		
		//
		// 	LineBranchXmlType
		//
		if (base instanceof LineBranchXmlType ) {
			LineBranchXmlType baseLine = (LineBranchXmlType)base;
			LineBranchXmlType line = (LineBranchXmlType)bra;
/*
	           <z unit="PU" im="0.0181" re="0.0014"/>
*/
			if (!ODMNumericUtil.equals(baseLine.getZ(), line.getZ())) {
				msgList.add("\nLine Branch Z not equal: " + braId
						+ "   " + BaseJaxbHelper.toStr(baseLine.getZ()) + baseFormat
						+ "   " + BaseJaxbHelper.toStr(line.getZ()) + format);
			}
/*
            <totalShuntY unit="PU" im="0.11444" re="0.0"/>
*/
			if (!ODMNumericUtil.equals(baseLine.getTotalShuntY(), line.getTotalShuntY())) {
				msgList.add("\nLine Branch TotalShuntY not equal: " + braId
						+ "   " + BaseJaxbHelper.toStr(baseLine.getTotalShuntY()) + baseFormat
						+ "   " + BaseJaxbHelper.toStr(line.getTotalShuntY()) + format);
			}
		}
		//
		// 	XfrBranchXmlType
		//

		if (base instanceof XfrBranchXmlType ) {
			XfrBranchXmlType baseXfr = (XfrBranchXmlType)base;
			XfrBranchXmlType xfr = (XfrBranchXmlType)bra;

/*
         	<z unit="PU" im="0.0066" re="8.0E-5"/>
*/
			if (!ODMNumericUtil.equals(baseXfr.getZ(), xfr.getZ())) {
				msgList.add("\nXfr Branch Z not equal: " + braId
						+ "   " + BaseJaxbHelper.toStr(baseXfr.getZ()) + baseFormat
						+ "   " + BaseJaxbHelper.toStr(xfr.getZ()) + format);
			}
			
/*			
            <fromTurnRatio unit="PU" value="1.0"/>
            <toTurnRatio unit="PU" value="0.9524"/>
*/
			if (!NumericUtil.equals(baseXfr.getFromTurnRatio().getValue(), xfr.getFromTurnRatio().getValue(), 0.0001)) {
				msgList.add("\nXfr Branch fromTurnRatio not equal: " + braId
								+ "   [" + baseXfr.getFromTurnRatio().getValue() + "," + baseXfr.getToTurnRatio().getValue() + "] " + baseFormat
								+ "   [" + + xfr.getFromTurnRatio().getValue() + "," + xfr.getToTurnRatio().getValue() + "]");
			}
			if (!NumericUtil.equals(baseXfr.getToTurnRatio().getValue(), xfr.getToTurnRatio().getValue(), 0.0001)) {
				msgList.add("\nXfr Branch toTurnRatio not equal: " + braId
						+ "   [" + baseXfr.getFromTurnRatio().getValue() + "," + baseXfr.getToTurnRatio().getValue() + "] " + baseFormat
						+ "   [" + + xfr.getFromTurnRatio().getValue() + "," + xfr.getToTurnRatio().getValue() + "]"  + format);
			}
			
/*			
            <xfrInfo>
                <fromRatedVoltage unit="KV" value="26.0"/>
                <toRatedVoltage unit="KV" value="525.0"/>
            </xfrInfo>
*/
			
			if (base instanceof PSXfrBranchXmlType ) {
				
			}
		}
	}
}
