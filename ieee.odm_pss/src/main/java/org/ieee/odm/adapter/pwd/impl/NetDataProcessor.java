package org.ieee.odm.adapter.pwd.impl;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.LimitSetXmlType;
import org.ieee.odm.schema.NetAreaXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.PWDNetworkExtXmlType;
 /**
  * Network data processor for PowerWorld-TO-ODM Adapter based on power world v16 data definition
  * Now it supports both Area data and Zone data, Owner and Interface data is not yet implemented.
  * @version 0.2  09/30/2012
  * @author  
  * 
  */
public class NetDataProcessor extends InputLineStringParser  {
	private AclfModelParser parser = null;
	private boolean initLimitSet =false;
	public NetDataProcessor(AclfModelParser parser) {
		this.parser = parser;
	}
	
	/**
	 *  Process area data. It creates area XmlType for each area and adds it to the network area list
	 *  Now only areaNum and areaName are considered.
	 * @param areaDataStr
	 */
	public void processAreaData(String areaDataStr){
		/*
		 * DATA (AREA, [AreaNum,AreaName,BGAGC,BGAutoSS,BGAutoXF,EnforceGenMWLimits,SchedName,SAName,
           ConvergenceTol,AreaEDIncludeLossPF,BusSlack,AreaUnSpecifiedStudyMW])
		 */
		//now only consider areaNum, areaName
		
		int areaNum=-1;
		String areaName="";
		parseData(areaDataStr);
		
		try {
			areaNum=getInt("AreaNum");
			areaName=getValue("AreaName");
		} catch (ODMException e) {
			e.printStackTrace();
		}
		
		NetAreaXmlType area=OdmObjFactory.createNetAreaXmlType();
		area.setNumber(areaNum);
		area.setName(areaName);
		
		if(parser.getNet().getAreaList()==null)
			parser.getNet().setAreaList(OdmObjFactory.createNetworkXmlTypeAreaList());
		
		parser.getNet().getAreaList().getArea().add(area);
	}
	
	/**
	 * Process zone data.
	 * @param zoneDataStr
	 */
	public void processZoneData(String zoneDataStr){
		/*
		 * DATA (ZONE, [ZoneNum,ZoneName,SchedName])
		 */
		int zoneNum=-1;
		String zoneName="";
		parseData(zoneDataStr);
	
		try {
			zoneNum=getInt("ZoneNum");
			zoneName=getValue("ZoneName");
		} catch (ODMException e) {
			e.printStackTrace();
		}
		
				
		NetZoneXmlType zone=OdmObjFactory.createNetZoneXmlType();
		zone.setNumber(zoneNum);
		zone.setName(zoneName);
		
		if(parser.getNet().getLossZoneList()==null)
			parser.getNet().setLossZoneList(OdmObjFactory.createNetworkXmlTypeLossZoneList());
		
		parser.getNet().getLossZoneList().getLossZone().add(zone);
		
	}
    /**
     * 
     * @param limitSetStr
     * @throws ODMException 
     */
	public void processLimitSet(String limitSetStr) throws ODMException{
		PWDNetworkExtXmlType pwdNetExt =(PWDNetworkExtXmlType)parser.getNet().getExtension();
		if(!initLimitSet){
			if (pwdNetExt.getLimitSets()==null) 
			    pwdNetExt.setLimitSets(OdmObjFactory.createPWDNetworkExtXmlTypeLimitSets());
			initLimitSet=true;
		}
		
		
		String limitSetName="";
		boolean isDisable =false;
		int lsNum=0;
		
		parseData(limitSetStr);
		if(exist("LSDisabled")){
		   isDisable = getValue("LSDisabled").trim().equalsIgnoreCase("No")?false:true;
		}
		lsNum = getInt("LSNum");
		limitSetName = getValue("LSName");
		
		LimitSetXmlType limitSet = OdmObjFactory.createLimitSetXmlType();
		limitSet.setNumber(lsNum);
		limitSet.setName(limitSetName);
		limitSet.setLsDiabled(isDisable);
		 pwdNetExt.getLimitSets().getLimitSet().add(limitSet);
		
	}
	
	/**
	 * process owner data
	 * @param ownerDataStr
	 */
	public void processOwnerData(String ownerDataStr){
		//TODO
		/*
		 * 1. owner data
		 * 2. subData, judge if ownerDataStr.trim().startWith("<SubData")
		 * 
		 * add the owner data to a HashTable(ownerNum, OwnerXmlType)
		 * then for each bus/branch/gen(defined in the subdata), set the corresponding Owner
		 *
		 */
	}
	/**
	 * process interface data
	 * @param interfaceDataStr
	 */
	public void processInterfaceData(String interfaceDataStr){
		//TODO
	}
}
