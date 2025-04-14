 /*
  * @(#)PSSEXfrZTableDataMapper.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSEXfrZTableDataRawParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.BranchBusSideEnumType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.XformerZTableXmlType;

public class PSSEXfrZTableDataRawMapper extends BasePSSEDataRawMapper{
	
	public PSSEXfrZTableDataRawMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEXfrZTableDataRawParser(ver);
	}
	

	public void procLineString(String[] lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		if (baseCaseNet.getXfrZTable() == null) {
			baseCaseNet.setXfrZTable(OdmObjFactory.createXformerZTableXmlType());
			baseCaseNet.getXfrZTable().setAdjustSide(BranchBusSideEnumType.FROM_SIDE);
		}
		XformerZTableXmlType.XformerZTableItem item = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItem(); 
		baseCaseNet.getXfrZTable().getXformerZTableItem().add(item);
		
	
		int i = this.dataParser.getInt("I");
		item.setNumber(i);

		/*
		 * format V30-V33: I, T1, F1, T2, F2, T3, F3, ... T11, F11
		 */
		if (PSSERawAdapter.getVersionNo(this.version) <34) {
			for (int n = 1; n < 12; n++) {
				if (this.dataParser.exist("T"+n) && this.dataParser.exist("F"+n) &&  this.dataParser.getValue("F"+n).trim().length() > 0) {
					if(this.dataParser.getDouble("F"+n,0.0) == 0.0) break;
					XformerZTableXmlType.XformerZTableItem.Lookup lookup = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup(); 
					item.getLookup().add(lookup);
					lookup.setTurnRatioShiftAngle(this.dataParser.getDouble("T"+n));
					lookup.setScaleFactor(this.dataParser.getDouble("F"+n));
				}
			}
		}
		
		//TODO: Need to update the XformerZTableXmlType.XformerZTableItem.Lookup class to support complex number
		/* 
		 * format V34-36
		 * 
		 *       I, T1, Re(F1), Im(F1), T2, Re(F2), Im(F2), ..., T6, Re(F6), Im(F6)
				T7, Re(F7), Im(F7), T8, Re(F8), Im(F8), ..., T12, Re(F12), Im(F12)
				...
				...
				Tn, Re(Fn), Im(Fn), 0.0, 0.0, 0.0
		 * 
		*/
		else {
			// the number of correction factors is based on the meta data
			int maxFactors = 12; // Default maximum number of correction factors
			if ((this.dataParser.getMetadata().length-1)/3 > maxFactors) {
				maxFactors = (this.dataParser.getMetadata().length-1)/3;
			}
			for (int n = 1; n <= maxFactors; n++) {
				if (this.dataParser.exist("T"+n) && this.dataParser.exist("RE(F"+n+")")
				&& this.dataParser.exist("IM(F"+n+")") && this.dataParser.getValue("RE(F"+n+")").trim().length() > 0) {
					if(this.dataParser.getDouble("RE(F"+n+")",0.0) == 0.0) break;
					XformerZTableXmlType.XformerZTableItem.Lookup lookup = OdmObjFactory.createXformerZTableXmlTypeXformerZTableItemLookup(); 
					item.getLookup().add(lookup);
					lookup.setTurnRatioShiftAngle(this.dataParser.getDouble("T"+n));
					lookup.setScaleFactor(this.dataParser.getDouble("RE(F"+n+")"));
					//TODO: the imaginary part is not used in the current implementation
					/*
					 * if (this.dataParser.exist("IM(F"+n+")")) {
						lookup.setImaginaryPart(this.dataParser.getDouble("IM(F"+n+")"));
					} else {
						lookup.setImaginaryPart(0.0);
					}
					 */
					
				}
			}
		}
	}
}
