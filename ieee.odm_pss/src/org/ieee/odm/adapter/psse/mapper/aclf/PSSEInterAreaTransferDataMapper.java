 /*
  * @(#)PSSEInterAreaTransferDataMapper.java   
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

package org.ieee.odm.adapter.psse.mapper.aclf;

import static org.ieee.odm.ODMObjectFactory.odmObjFactory;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEInterAreaTransferDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.schema.AreaTransferXmlType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.InterchangeXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class PSSEInterAreaTransferDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEInterAreaTransferDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEInterAreaTransferDataParser(ver);
	}
	
	public void procLineString(String lineStr, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int	arfrom = this.dataParser.getInt("ARFROM");
		int	arto = this.dataParser.getInt("ARTO");
		String	trid = this.dataParser.getString("TRID");
		double	ptran = this.dataParser.getDouble("PTRAN");

		/*
			 * format: ARFROM, ARTO, TRID, PTRAN

			ARFROM "From area" number (1 through the maximum number of areas at the 
					current size level; see Table P-1).
			ARTO "To area" number (1 through the maximum number of areas at the current 
					size level; see Table P-1).
			TRID Single-character (0 through 9 or A through Z) upper case interarea transfer identifier
					used to distinguish among multiple transfers between areas ARFROM and
					ARTO. TRID = ’1’ by default.
			PTRAN MW comprising this transfer. A positive PTRAN indicates that area ARFROM is
					selling to area ARTO. PTRAN = 0.0 by default.
					
				- FromAreaNo_ToAreaNo_TRID is unique					 
		*/
		LoadflowNetXmlType baseCaseNet = (LoadflowNetXmlType) parser.getNet();
		if (baseCaseNet.getInterchangeList() == null)
			baseCaseNet.setInterchangeList(odmObjFactory.createLoadflowNetXmlTypeInterchangeList());
		InterchangeXmlType interchange = odmObjFactory.createInterchangeXmlType();
		baseCaseNet.getInterchangeList().getInterchange().add(interchange);
		AreaTransferXmlType transfer = odmObjFactory.createAreaTransferXmlType(); 
		interchange.setAreaTransfer(transfer);
		
		transfer.setFromArea(arfrom);
		transfer.setToArea(arto);
		transfer.setId(trid);
		transfer.setAmountMW(ptran);
		
	}
}
