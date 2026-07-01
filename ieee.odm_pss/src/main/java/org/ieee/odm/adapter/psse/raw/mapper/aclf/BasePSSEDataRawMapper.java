 /*
  * @(#)BasePSSEDataMapper.java   
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

package org.ieee.odm.adapter.psse.raw.mapper.aclf;

import org.ieee.odm.adapter.common.str.BaseStringDataMapper;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.base.BaseJaxbHelper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ieee.odm.schema.BaseRecordXmlType;
import org.ieee.odm.schema.NameTagXmlType;

public class BasePSSEDataRawMapper extends  BaseStringDataMapper{
	protected PsseVersion version = null;

	private static final double DEFAULT_ZERO_IMPEDANCE_THRESHOLD = 0.00001;
	private static final String EXTERNAL_UID_KEY = "externalUID";
	private static final Pattern RAW_LABEL_COMMENT = Pattern.compile("/\\*\\s*(\\[[^\\]]+\\])\\s*\\*/");
	protected double zbr_threshold = DEFAULT_ZERO_IMPEDANCE_THRESHOLD;
	
	public BasePSSEDataRawMapper(){
		
	}
	
	public BasePSSEDataRawMapper(PsseVersion ver) {
		this.version = ver;
	}
	
	protected void mapOwnerInfo(BaseRecordXmlType recXml) throws ODMException {
		String o1 = dataParser.getValue("O1");
		double f1 = dataParser.getDouble("F1", 0.0);
		

		String o2 = dataParser.getValue("O2", null);
		if (o2 != null && o2.trim().isEmpty())
			o2 = null;
		
		double f2 = dataParser.getDouble("F2", 0.0);
		String o3 = dataParser.getValue("O3", null);
		if (o3 != null &&  o3.trim().isEmpty())
			o3 = null;
		
		double f3 = dataParser.getDouble("F3", 0.0);
		String o4 = dataParser.getValue("O4", null);
		if (o4 != null && o4.trim().isEmpty())
			o4 = null;
		double f4 = dataParser.getDouble("F4", 0.0);
    	
		BaseJaxbHelper.addOwner(recXml, 
				o1, f1, 
				o2, o2==null?0.0:f2, 
				o3, o3==null?0.0:f3, 
				o4, o4==null?0.0:f4);  		
	}



	protected void mapRawLabelDescription(NameTagXmlType recXml, String lineStr) {
		String labelDesc = extractRawLabelDescription(lineStr);
		if (labelDesc != null) {
			recXml.setDesc(labelDesc);
		}
	}

	protected void mapRawLabelMetadata(NameTagXmlType recXml, String lineStr) {
		String labelDesc = extractRawLabelDescription(lineStr);
		if (labelDesc == null)
			return;
		recXml.setDesc(labelDesc);

		String labels = labelDesc.substring(1, labelDesc.length() - 1);
		String[] labelAry = labels.split(",");
		for (int i = 0; i < labelAry.length; i++) {
			String label = labelAry[i].trim();
			if (!label.isEmpty()) {
				BaseJaxbHelper.addNVPair(recXml, i == 0 ? EXTERNAL_UID_KEY : EXTERNAL_UID_KEY + ":" + (i + 1), label);
			}
		}
	}

	protected void mapRawLabelMetadata(NameTagXmlType recXml, String[] lineStrAry) {
		if (lineStrAry == null)
			return;
		for (int i = lineStrAry.length - 1; i >= 0; i--) {
			String labelDesc = extractRawLabelDescription(lineStrAry[i]);
			if (labelDesc != null) {
				mapRawLabelMetadata(recXml, lineStrAry[i]);
				return;
			}
		}
	}

	private String extractRawLabelDescription(String lineStr) {
		if (lineStr == null)
			return null;
		Matcher matcher = RAW_LABEL_COMMENT.matcher(lineStr);
		if (!matcher.find())
			return null;
		return matcher.group(1).trim();
	}

	// get the default zero impedance threshold value
	public double getZeroImpedanceThreshold() {
		return zbr_threshold;
	}
	

	public void setZeroImpedanceThreshold(double threshold) {
		zbr_threshold = threshold;
	}

}
