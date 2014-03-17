 /*
  * @(#)AclfParserHelper.java   
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

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusGenDataXmlType;
import org.ieee.odm.schema.BusLoadDataXmlType;
import org.ieee.odm.schema.BusShuntYDataXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.LoadflowShuntYDataXmlType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.ReactivePowerUnitType;
import org.ieee.odm.schema.SwitchedShuntXmlType;
import org.ieee.odm.schema.StaticVarCompensatorXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.XformerZTableXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;
import org.ieee.odm.schema.YUnitType;

/**
 * Aclf model parser help functions
 * 
 * @author mzhou
 *
 */
public class AclfParserHelper extends BaseJaxbHelper {
	/**
	 * create a Contribution shuntY object under the busRec
	 * 
	 * @param busRec
	 */
	public static LoadflowShuntYDataXmlType createContriShuntY(LoadflowBusXmlType busRec) {
		BusShuntYDataXmlType shuntYData = busRec.getShuntYData();
		if (shuntYData == null) { 
			shuntYData = OdmObjFactory.createBusShuntYDataXmlType();
			busRec.setShuntYData(shuntYData);
			shuntYData.setEquivY(OdmObjFactory.createYXmlType());
		}
		LoadflowShuntYDataXmlType contribShuntY = OdmObjFactory.createLoadflowShuntYDataXmlType();
		shuntYData.getContributeShuntY().add(contribShuntY); 
	    return contribShuntY;
	}

	public static LoadflowLoadDataXmlType getDefaultLoad(BusLoadDataXmlType loadData) {
		if (loadData.getContributeLoad().size() == 0)
			loadData.getContributeLoad().add(OdmObjFactory.createContributeLoad(
					OdmObjFactory.createLoadflowLoadDataXmlType()));
		return loadData.getContributeLoad().get(0).getValue();
	}
	
	/**
	 * create a Contribution Load object under the busRec
	 * 
	 * @param busRec
	 */
	public static LoadflowLoadDataXmlType createContriLoad(LoadflowBusXmlType busRec) {
		BusLoadDataXmlType loadData = busRec.getLoadData();
		/* this should never happen
		if (loadData == null) { 
			loadData = OdmObjFactory.createBusLoadDataXmlType();
			busRec.setLoadData(loadData);
			LoadflowLoadDataXmlType load = OdmObjFactory.createLoadflowLoadDataXmlType();
			loadData.getContributeLoad().add(OdmObjFactory.createContributeLoad(load));
		}
		*/
		LoadflowLoadDataXmlType contribLoad = OdmObjFactory.createLoadflowLoadDataXmlType();
	    loadData.getContributeLoad().add(OdmObjFactory.createContributeLoad(contribLoad)); 
	    return contribLoad;
	}
	
	public static LoadflowGenDataXmlType getDefaultGen(BusGenDataXmlType genData) {
		if (genData.getContributeGen().size() == 0)
			genData.getContributeGen().add(OdmObjFactory.createContributeGen(
					OdmObjFactory.createLoadflowGenDataXmlType()));		
		return genData.getContributeGen().get(0).getValue();
	}
	
	
	/**
	 * create a Contribution Generator object under the busRec
	 * 
	 * @param busRec
	 */
	public static LoadflowGenDataXmlType createContriGen(LoadflowBusXmlType busRec) {
		BusGenDataXmlType genData = busRec.getGenData();
		/* this should never happen
		if (genData == null) {
			genData = OdmObjFactory.createBusGenDataXmlType();
			busRec.setGenData(genData);
			LoadflowGenDataXmlType gen = OdmObjFactory.createLoadflowGenDataXmlType();
			genData.getContributeGen().add(OdmObjFactory.createContributeGen(gen));
		}
		*/
		// some model does not need ContributeGenList
		LoadflowGenDataXmlType contribGen = OdmObjFactory.createLoadflowGenDataXmlType();
		genData.getContributeGen().add(OdmObjFactory.createContributeGen(contribGen));
		return contribGen;
	}

	/**
	 * create bus EquivData info after the contributing records are defined
	 * 
	 * @param parser
	 * @return
	 */
	@Deprecated
	public static boolean createBusEquivData(IODMModelParser parser) {
		createBusEquivGenData(parser);
		
		createBusEquivLoadData(parser);
		
		createBusEquivShuntYData(parser);

		return true;
	}	
	
	/**
	 * consolidate bus genContributionList to the equiv gen 
	 * 
	 */
	@SuppressWarnings("unchecked") public static boolean createBusEquivGenData(IODMModelParser parser ) {
		LoadflowNetXmlType baseCaseNet = ((AbstractModelParser<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>) parser).getNet(); 
		for (JAXBElement<? extends BusXmlType> bus : baseCaseNet.getBusList().getBus()) {
			LoadflowBusXmlType busRec = (LoadflowBusXmlType)bus.getValue();
			BusGenDataXmlType genData = busRec.getGenData();
			LoadflowGenDataXmlType defaultGen = getDefaultGen(genData);
			if (genData != null) {
				if ( genData.getContributeGen().size() > 0) {
					LoadflowGenDataXmlType equivGen = getDefaultGen(genData);
					double pgen = 0.0, qgen = 0.0, qmax = 0.0, qmin = 0.0, pmax = 0.0, pmin = 0.0, vSpec = 0.0;
					VoltageUnitType vSpecUnit = VoltageUnitType.PU;
					String remoteBusId = null;
					boolean offLine = true;
					for ( JAXBElement<? extends LoadflowGenDataXmlType> elem : genData.getContributeGen()) {
						LoadflowGenDataXmlType gen = elem.getValue();
						if (!gen.isOffLine()) {
							offLine = false;
							if (remoteBusId == null) {
								if (gen.getRemoteVoltageControlBus() != null) {
									remoteBusId = BaseJaxbHelper.getRecId(gen.getRemoteVoltageControlBus());
								}
							}
							else if (!remoteBusId.equals(BaseJaxbHelper.getRecId(gen.getRemoteVoltageControlBus()))) {
								ODMLogger.getLogger().severe("Inconsistant remote control bus id, " + remoteBusId +
										", " + BaseJaxbHelper.getRecId(gen.getRemoteVoltageControlBus()));
								return false; 
							}
							
							pgen += gen.getPower().getRe();
							qgen += gen.getPower().getIm();
							if(gen.getQLimit() != null) {
								qmax += gen.getQLimit().getMax();
								qmin += gen.getQLimit().getMin();
							}
							if(gen.getPLimit() != null) {
								pmax += gen.getPLimit().getMax();
								pmin += gen.getPLimit().getMin();
							}
							
							if (gen.getDesiredVoltage() != null) {
								if (vSpec == 0.0) {
									vSpec = gen.getDesiredVoltage().getValue();
									vSpecUnit = gen.getDesiredVoltage().getUnit();
								}
								else if (vSpec != gen.getDesiredVoltage().getValue()) {
									ODMLogger.getLogger().severe("Inconsistant gen desired voltage, " + 
											BaseJaxbHelper.getRecId(gen.getRemoteVoltageControlBus()));
									return false; 
								}
							}
						}
					}
					
					if (offLine && genData.getCode() != LFGenCodeEnumType.SWING)
						// generator on a swing bus might turned off
						genData.setCode(LFGenCodeEnumType.OFF);
					else if (genData.getCode() == LFGenCodeEnumType.PV) {
						equivGen.setPower(BaseDataSetter.createPowerValue(pgen, qgen, ApparentPowerUnitType.MVA));
						if (qmax != 0.0 || qmin != 0.0) {
							equivGen.setQLimit(BaseDataSetter.createReactivePowerLimit(qmax, qmin, ReactivePowerUnitType.MVAR));
							if (vSpec != 0.0) {
								equivGen.setDesiredVoltage(BaseDataSetter.createVoltageValue(vSpec, vSpecUnit));
							}
						}
						else {
							// this is the case when the generator is turn-off
							genData.setCode(LFGenCodeEnumType.PQ);
						}
					}	
					else {  // PQ bus	
						equivGen.setPower(BaseDataSetter.createPowerValue(pgen, qgen, ApparentPowerUnitType.MVA));
						if (pmax != 0.0 || pmin != 0.0) {
							equivGen.setPLimit(BaseDataSetter.createActivePowerLimit(pmax, pmin, ActivePowerUnitType.MW));
						}
					}
					
					if (remoteBusId != null && !remoteBusId.equals(busRec.getId()) && 
							genData.getCode() == LFGenCodeEnumType.PV){
						// Remote Q  Bus control, we need to change this bus to a GPQ bus so that its Q could be adjusted
						defaultGen.setRemoteVoltageControlBus(
								((AbstractModelParser<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>) parser).createBusRef(remoteBusId));
					}
				}
				else {
					genData.setCode(LFGenCodeEnumType.NONE_GEN);
					if (defaultGen.getPower() != null)
						defaultGen.setPower(null);
					if (defaultGen.getVoltageLimit() != null)
						defaultGen.setVoltageLimit(null);
				}
			}
		}

		return true;
	}

	/**
	 * consolidate bus loadContributionList to the load 
	 * 
	 */
	@SuppressWarnings("unchecked") public static boolean createBusEquivLoadData(IODMModelParser parser) {
		LoadflowNetXmlType baseCaseNet = ((AbstractModelParser<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>) parser).getNet(); 
		for (JAXBElement<? extends BusXmlType> bus : baseCaseNet.getBusList().getBus()) {
			LoadflowBusXmlType busRec = (LoadflowBusXmlType)bus.getValue();
			BusLoadDataXmlType loadData = busRec.getLoadData();
			LoadflowLoadDataXmlType defaultLoad = getDefaultLoad(loadData);
			if (loadData != null) {
				if ( loadData.getContributeLoad().size() > 0) {
					double cp_p=0.0, cp_q=0.0, ci_p=0.0, ci_q=0.0, cz_p=0.0, cz_q=0.0; 
					for ( JAXBElement<? extends LoadflowLoadDataXmlType> loadXml : loadData.getContributeLoad()) {
						LoadflowLoadDataXmlType load = loadXml.getValue();
						if (!load.isOffLine()) {
							if (load.getConstPLoad() != null) {
								cp_p += load.getConstPLoad().getRe();
								cp_q += load.getConstPLoad().getIm();
							}
							if (load.getConstILoad() != null) {
								ci_p += load.getConstILoad().getRe();
								ci_q += load.getConstILoad().getIm();
							}
							if (load.getConstZLoad() != null) {
								cz_p += load.getConstZLoad().getRe();
								cz_q += load.getConstZLoad().getIm();
							}
						}					
					}
					
					if ((cp_p != 0.0 || cp_q != 0.0) && (ci_p==0.0 && ci_q ==0.0 && cz_p==0.0 && cz_q ==0.0) ) {
						defaultLoad.setCode(LFLoadCodeEnumType.CONST_P);
						defaultLoad.setConstPLoad(BaseDataSetter.createPowerValue(cp_p, cp_q, ApparentPowerUnitType.MVA));
			  		}
					else if ((ci_p != 0.0 || ci_q != 0.0) && (cp_p==0.0 && cp_q ==0.0 && cz_p==0.0 && cz_q ==0.0) ) {
						defaultLoad.setCode(LFLoadCodeEnumType.CONST_I);
						defaultLoad.setConstILoad(BaseDataSetter.createPowerValue(ci_p, ci_q, ApparentPowerUnitType.MVA));
			  		}
					else if ((cz_p != 0.0 || cz_q != 0.0) && (ci_p==0.0 && ci_q ==0.0 && cp_p==0.0 && cp_q ==0.0) ) {
						defaultLoad.setCode(LFLoadCodeEnumType.CONST_Z);
						defaultLoad.setConstZLoad(BaseDataSetter.createPowerValue(cz_p, cz_q, ApparentPowerUnitType.MVA));
			  		}
					else if ((cp_p != 0.0 || cp_q != 0.0 || ci_p!= 0.0 || ci_q != 0.0 || cz_p != 0.0 || cz_q !=0.0)) {
						defaultLoad.setCode(LFLoadCodeEnumType.FUNCTION_LOAD);
						defaultLoad.setConstPLoad(BaseDataSetter.createPowerValue(cp_p, cp_q, ApparentPowerUnitType.MVA));
						defaultLoad.setConstILoad(BaseDataSetter.createPowerValue(ci_p, ci_q, ApparentPowerUnitType.MVA));
						defaultLoad.setConstZLoad(BaseDataSetter.createPowerValue(cz_p, cz_q, ApparentPowerUnitType.MVA));
					}
					else {
						defaultLoad.setCode(LFLoadCodeEnumType.NONE_LOAD);
					}
				}
				else
					defaultLoad.setCode(LFLoadCodeEnumType.NONE_LOAD);
			}
		}

		return true;
	}
	
	/**
	 * consolidate bus loadContributionList to the load 
	 * 
	 */
	@SuppressWarnings("unchecked") public static boolean createBusEquivShuntYData(IODMModelParser parser) {
		LoadflowNetXmlType baseCaseNet = ((AbstractModelParser<LoadflowNetXmlType, LoadflowBusXmlType, LineBranchXmlType, XfrBranchXmlType, PSXfrBranchXmlType>) parser).getNet(); 
		for (JAXBElement<? extends BusXmlType> bus : baseCaseNet.getBusList().getBus()) {
			LoadflowBusXmlType busRec = (LoadflowBusXmlType)bus.getValue();
			BusShuntYDataXmlType shuntYData = busRec.getShuntYData();
			if (shuntYData != null) {
				if ( shuntYData.getContributeShuntY().size() > 0) {
					//LoadflowShuntYDataXmlType equivY = shuntYData.getEquivY();
					double g=0.0, b=0.0; 
					for ( LoadflowShuntYDataXmlType y : shuntYData.getContributeShuntY()) {
						if (!y.isOffLine()) {
							if (y.getY() != null) {
								g += y.getY().getRe();
								b += y.getY().getIm();
							}
						}					
					}
					
					if (g != 0.0 || b != 0.0) {
						shuntYData.setEquivY(BaseDataSetter.createYValue(g, b, YUnitType.PU));
			  		}
				}
			}
		}
		return true;
	}

	
	/**
	 * create a SVC object under the bus object
	 * 
	 * @param bus
	 */
	public static StaticVarCompensatorXmlType createSVC(LoadflowBusXmlType bus) {
		StaticVarCompensatorXmlType svc = OdmObjFactory.createStaticVarCompensatorXmlType();
		bus.setSvc(svc);
		return svc;
	}

	/**
	 * create a ShuntCompensatorXmlType object under the bus object
	 * 
	 * @param bus
	 */
	public static SwitchedShuntXmlType createShuntCompensator(LoadflowBusXmlType bus) {
		//if (bus.getShuntCompensatorData().getShuntCompensatorList() == null) {
		//	bus.getShuntCompensatorData().setShuntCompensatorList(odmObjFactory.createShuntCompensatorDataXmlTypeShuntCompensatorList());
		//}
		SwitchedShuntXmlType compensator = OdmObjFactory.createSwitchedShuntXmlType();
		bus.setSwitchedShunt(compensator);
		return compensator; 
	}
	
	/**
	 * get Xfr Z correction table item by item number
	 * 
	 * @param number
	 * @param xfrZTable
	 * @return
	 */
	public static XformerZTableXmlType.XformerZTableItem getXfrZTableItem(int number, XformerZTableXmlType xfrZTable) {
		for (XformerZTableXmlType.XformerZTableItem item : xfrZTable.getXformerZTableItem()) {
			if (item.getNumber() == number)
				return item;
		}
		return null;
	}
}
