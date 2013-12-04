/*
 * @(#)UCTE_DEFAdapter.java   
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.adapter.ucte;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.IODMAdapter;
import org.ieee.odm.adapter.ucte.mapper.UCTEExPowerDataMapper;
import org.ieee.odm.adapter.ucte.mapper.UCTELineDataMapper;
import org.ieee.odm.adapter.ucte.mapper.UCTENodeDataMapper;
import org.ieee.odm.adapter.ucte.mapper.UCTEXfrAdjustDataMapper;
import org.ieee.odm.adapter.ucte.mapper.UCTEXfrDataMapper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;

/*
	UCTE data exchange format for load flow and three phase short circuit studies (UCTE-DEF)
	Version 02 (coming into force: 2007.05.01)
*/

public class UCTE_DEFAdapter extends AbstractODMAdapter {
	public final static String PsXfrType_ASYM = "ASYM"; 

	private enum RecType {Comment, BaseVoltage, Node, Line, Xfr2W, Xfr2WReg, Xfr2WLookup, ExPower, NotDefined};

	private UCTEExPowerDataMapper exPowerDataMapper = new UCTEExPowerDataMapper();
	
	private UCTENodeDataMapper nodeDataMapper = new UCTENodeDataMapper();

	private UCTELineDataMapper lineDataMapper = new UCTELineDataMapper();
	private UCTEXfrDataMapper xfrDataMapper = new UCTEXfrDataMapper();
	private UCTEXfrAdjustDataMapper xfrAdjDataMapper = new UCTEXfrAdjustDataMapper();
	
	/**
	 * default constructor
	 * 
	 * @param logger Logger object
	 */
	public UCTE_DEFAdapter() {
		super();
	}

	@Override
	protected AclfModelParser parseInputFile(
			final IFileReader din, String encoding) throws ODMException {
		AclfModelParser parser = new AclfModelParser(encoding);
		parser.initCaseContentInfo(OriginalDataFormatEnumType.UCTE_DEF);

		// BaseCase object, plus busRecList and BranchRecList are created 
		LoadflowNetXmlType baseCaseNet = parser.getNet();
		baseCaseNet.setId("Base_Case_from_UCTE_format");

    	// no base kva definition in UCTE format, so use 100 MVA
    	// UCTE data are in actual units, mw, mva ...
		baseCaseNet.setBasePower(BaseDataSetter.createPowerMvaValue(100.0));   

    	// scan all lines and process the data
    	//customBaseVoltage = false;
      	String str;   
      	RecType recType = RecType.NotDefined;
      	int busCnt = 0;
    	do {
          	str = din.readLine();   
        	if (str != null && !str.trim().equals("")) {
        		String isoId = "";
    			try {
    				if (str.startsWith("##C"))
    					recType = RecType.Comment;
    				else if (str.startsWith("##BaseVoltage")) {
    					// this is an extension, not defined in the UCTE spec
    					recType = RecType.BaseVoltage;
    			    	this.nodeDataMapper.getDataParser().setCustomBaseVoltage(true);
    					this.nodeDataMapper.getDataParser().getCustomBaseVoltageList().clear();
    				}
    				else if (str.startsWith("##N"))
    					recType = RecType.Node;
    				else if (str.startsWith("##Z") && recType == RecType.Node) {  
    					isoId = str.substring(3);
		    			this.nodeDataMapper.setIsoId(isoId);
    				}
		    		else if (str.startsWith("##L"))
    					recType = RecType.Line;
    				else if (str.startsWith("##T"))
    					recType = RecType.Xfr2W;
    				else if (str.startsWith("##R"))
    					recType = RecType.Xfr2WReg;
    				else if (str.startsWith("##TT"))
    					recType = RecType.Xfr2WLookup;
    				else if (str.startsWith("##E")) {
    					recType = RecType.ExPower;
    					baseCaseNet.setInterchangeList(OdmObjFactory.createLoadflowNetXmlTypeInterchangeList());
    				}
    				else {
    					// process data lines
    					if (recType == RecType.Comment) {
    			    	    processCommentRecord(str, baseCaseNet);
    			    	}
    					else if (recType == RecType.BaseVoltage) {
    			    	    this.nodeDataMapper.getDataParser().processBaseVoltageRecord(str);
    			    	}
    			    	else if (recType == RecType.Node) {
    			    		this.nodeDataMapper.setBusCnt(++busCnt);
    			    	    this.nodeDataMapper.mapInputLine(str, parser);
    			    	}
    			    	else if (recType == RecType.Line) {
    			    	    this.lineDataMapper.mapInputLine(str, parser);
    			    	}
    			    	else if (recType == RecType.Xfr2W) {
    			    	    this.xfrDataMapper.mapInputLine(str, parser);
    			    	}
    			    	else if (recType == RecType.Xfr2WReg) {
    			    	    this.xfrAdjDataMapper.mapInputLine(str, parser);
    			    	}
    			    	else if (recType == RecType.Xfr2WLookup) {
    			    	    processXfr2LookupRecord(str, baseCaseNet);
    			    	}
    			    	else if (recType == RecType.ExPower) {
    			    	    this.exPowerDataMapper.mapInputLine(str, parser);
    			    	}
    				}
    			} catch (final Exception e) {
    				e.printStackTrace();
    				logErr(e.toString());
    			}
        	}
       	} while (str != null);

    	return parser;
    }

	protected IODMModelParser parseInputFile(IODMAdapter.NetType type, final IFileReader[] din, String encoding) throws ODMException {
		throw new ODMException("not implemented yet");
	}
	
    /*
     * ##C section
     */
    private boolean processCommentRecord(String str, LoadflowNetXmlType xmlBaseNet) {
    	ODMLogger.getLogger().fine("Comment: " + str);
		// there is no need to do anything to the comment lines
    	return true;
    }
    
    /*
     * ##TT section
     */
    private void processXfr2LookupRecord(String str, LoadflowNetXmlType xmlBaseNet) {
    	ODMLogger.getLogger().fine("Xfr 2W Desc Record: " + str);
    	ODMLogger.getLogger().severe("##TT not implemented yet. Contact support@interpss.org for more info");
		return;
    }
}