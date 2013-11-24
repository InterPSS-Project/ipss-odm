 /*
  * @(#)AcscParserHelper.java   
  *
  * Copyright (C) 2008 www.interpss.org
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
  * @Date 08/11/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.acsc;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.AbstractModelParser;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ModelDataUtil;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BusGenDataXmlType;
import org.ieee.odm.schema.BusLoadDataXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.GroundingEnumType;
import org.ieee.odm.schema.GroundingXmlType;
import org.ieee.odm.schema.LFGenCodeEnumType;
import org.ieee.odm.schema.LFLoadCodeEnumType;
import org.ieee.odm.schema.LineShortCircuitXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.LoadflowNetXmlType;
import org.ieee.odm.schema.PSXfrShortCircuitXmlType;
import org.ieee.odm.schema.PowerXmlType;
import org.ieee.odm.schema.ShortCircuitBusEnumType;
import org.ieee.odm.schema.ShortCircuitBusXmlType;
import org.ieee.odm.schema.ShortCircuitGenDataXmlType;
import org.ieee.odm.schema.ShortCircuitLoadDataXmlType;
import org.ieee.odm.schema.ShortCircuitNetXmlType;
import org.ieee.odm.schema.XformerConnectionXmlType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;
import org.ieee.odm.schema.YUnitType;
import org.ieee.odm.schema.YXmlType;
import org.ieee.odm.schema.ZUnitType;
import org.ieee.odm.schema.ZXmlType;

/**
 * Acsc ODM model parser helper utility functions
 * 
 * @author mzhou
 *
 */
public class AcscParserHelper extends AclfParserHelper {
	/**
	 * create Acsc equiv gen
	 * 
	 * @return
	 */
	public static JAXBElement<ShortCircuitGenDataXmlType> createAcscEquivGen() {
		ShortCircuitGenDataXmlType equivGen = OdmObjFactory.createShortCircuitGenDataXmlType();
		return OdmObjFactory.createAcscEquivGen(equivGen);
	}
	
	/**
	 * get Acsc Gen Data object on the acscBus with id = genId
	 * 
	 * @param acscBus
	 * @param genId
	 * @return null if acscGenData not found
	 */
	public static ShortCircuitGenDataXmlType getAcscContritueGen(ShortCircuitBusXmlType acscBus, String genId) throws ODMException {
		for (JAXBElement<? extends LoadflowGenDataXmlType> elem : acscBus.getGenData().getContributeGen()) {
			ShortCircuitGenDataXmlType scGenData = (ShortCircuitGenDataXmlType)elem.getValue();
			if (scGenData.getId().equals(genId))
				return scGenData;
		}
    	throw new ODMException("Generator not found, ID: " + genId + "@Bus:" + acscBus.getId());
	}
	
	/**
	 * create a Acsc Contributing Generator object
	 * 
	 */
	public static ShortCircuitGenDataXmlType createAcscContributeGen(ShortCircuitBusXmlType busRec) {
		BusGenDataXmlType genData = busRec.getGenData();
		if (genData == null) {
			genData = OdmObjFactory.createBusGenDataXmlType();
			busRec.setGenData(genData);
			genData.setEquivGen(createAcscEquivGen());
		}
		// some model does not need ContributeGenList
		ShortCircuitGenDataXmlType contribGen = OdmObjFactory.createShortCircuitGenDataXmlType();
		genData.getContributeGen().add(OdmObjFactory.createAcscContributeGen(contribGen));
		return contribGen;
	}
	
	/**
	 * create Acsc equiv load
	 * 
	 * @return
	 */
	public static JAXBElement<ShortCircuitLoadDataXmlType> createAcscEquivLoad() {
		ShortCircuitLoadDataXmlType equivLoad = OdmObjFactory.createShortCircuitLoadDataXmlType();
		return OdmObjFactory.createAcscEquivLoad(equivLoad);
	}

	/**
	 * create a ShortCircuit Contribution Load object
	 * 
	 */
	public static ShortCircuitLoadDataXmlType createAcscContributeLoad(ShortCircuitBusXmlType busRec) {
		BusLoadDataXmlType loadData = busRec.getLoadData();
		if (loadData == null) { 
			loadData = OdmObjFactory.createBusLoadDataXmlType();
			busRec.setLoadData(loadData);
			ShortCircuitLoadDataXmlType equivLoad = OdmObjFactory.createShortCircuitLoadDataXmlType();
			loadData.setEquivLoad(OdmObjFactory.createAcscEquivLoad(equivLoad));
		}
		ShortCircuitLoadDataXmlType contribLoad = OdmObjFactory.createShortCircuitLoadDataXmlType();
	    loadData.getContributeLoad().add(OdmObjFactory.createAcscContributeLoad(contribLoad)); 
	    return contribLoad;
	}	
	
	/**
	 * create directed or solid grounding xfr connection xml Type
	 * @return
	 */
	public static XformerConnectionXmlType createDirectedGroundingConnection(){
		XformerConnectionXmlType  xfrConnect = OdmObjFactory.createXformerConnectionXmlType();
		GroundingXmlType ground = OdmObjFactory.createGroundingXmlType();
		ground.setGroundingConnection(GroundingEnumType.SOLID_GROUNDED);
		xfrConnect.setGrounding(ground);
		return xfrConnect;
	}
	
	/**
	 * create impdeance grounding xfr connection xml Type
	 * ZG = rg + j* xg
	 * @param rg
	 * @param xg
	 * @return
	 */
	public static XformerConnectionXmlType createZGroundingConnection(double rg, double xg){
		XformerConnectionXmlType  xfrConnect = OdmObjFactory.createXformerConnectionXmlType();
		GroundingXmlType ground = OdmObjFactory.createGroundingXmlType();
		ground.setGroundingConnection(GroundingEnumType.Z_GROUNDED);
		ground.setGroundingZ(BaseDataSetter.createZValue(rg, xg, ZUnitType.PU));
		xfrConnect.setGrounding(ground);
		return xfrConnect;
	}
	
	/**
	 * create ungrounding xfr connection xml Type
	 * @return
	 */
	public static XformerConnectionXmlType createUnGroundingConnection(){
		XformerConnectionXmlType  xfrConnect = OdmObjFactory.createXformerConnectionXmlType();
		GroundingXmlType ground = OdmObjFactory.createGroundingXmlType();
		ground.setGroundingConnection(GroundingEnumType.UNGROUNDED);
		xfrConnect.setGrounding(ground);
		return xfrConnect;
	}
	
	/**
	 * create bus EquivData info
	 * 
	 * @param parser
	 * @return
	 */
	public static boolean createBusScEquivData(IODMModelParser parser) throws ODMException {
		createBusScEquivGenData(parser);
		
		createBusScEquivLoadData(parser);

		return true;
	}	
	
	/**
	 * consolidate bus scGenContributionList to the equiv gen 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static boolean createBusScEquivGenData(IODMModelParser parser ) throws ODMException {
		LoadflowNetXmlType baseCaseNet = ((AbstractModelParser<ShortCircuitNetXmlType, ShortCircuitBusXmlType, LineShortCircuitXmlType, XfrShortCircuitXmlType, PSXfrShortCircuitXmlType>) parser).getNet();
		double sysMVABase = baseCaseNet.getBasePower().getValue();
		if(baseCaseNet.getBasePower().getUnit()==ApparentPowerUnitType.MVA){
			//target unit, do nothing
		}
		else if(baseCaseNet.getBasePower().getUnit()==ApparentPowerUnitType.KVA)
			sysMVABase *=0.001;
		else if (baseCaseNet.getBasePower().getUnit() == ApparentPowerUnitType.VA)
			sysMVABase *= 0.000001;
		else 
			throw new ODMException("Wrong system baseMVA.unit, " + baseCaseNet.getBasePower().getUnit());
			
			
		for ( JAXBElement<? extends BusXmlType> busXml : baseCaseNet.getBusList().getBus()) {
			ShortCircuitBusXmlType scBusXml = (ShortCircuitBusXmlType)busXml.getValue();
			
			if(scBusXml.getScCode()==ShortCircuitBusEnumType.CONTRIBUTING &&
					scBusXml.getGenData().getEquivGen()!=null &&
				    scBusXml.getGenData().getContributeGen()!=null){
					
					//Consolidate the positive and negative sequence to scEquivLoadData
					ShortCircuitGenDataXmlType scEquivData = (ShortCircuitGenDataXmlType)scBusXml.getGenData().getEquivGen().getValue();
					
					// gen z is init as a large Z, or open circuit
					ZXmlType equivPosZ  = null;
					ZXmlType equivNegZ  = null;
					ZXmlType equivZeroZ = null;
					
	
					
					for( JAXBElement<? extends LoadflowGenDataXmlType> scContriGenEle : scBusXml.getGenData().getContributeGen()) {
						ShortCircuitGenDataXmlType contriGenData = (ShortCircuitGenDataXmlType) scContriGenEle.getValue();
						/*
						 * ZSource is based on GEN MVABASE, therefore, it requires to convert it to system MVABASE
						 *  before setting it to BusScZ
						 */
						double machRatedMva = contriGenData.getMvaBase().getValue();
						if (contriGenData.getMvaBase().getUnit() == ApparentPowerUnitType.MVA)   // possible unit PU, W, KW, MW, HP;
						   ;  // do nothing
						else if (contriGenData.getMvaBase().getUnit() == ApparentPowerUnitType.KVA)
							machRatedMva *= 0.001;
						else if (contriGenData.getMvaBase().getUnit() == ApparentPowerUnitType.VA)
							machRatedMva *= 0.000001;
						else {
							throw new ODMException("Wrong acscGen.retedMachPower.unit, " + contriGenData.getMvaBase().getUnit());
						}
						
						
						double factor =sysMVABase/ machRatedMva;
						
						ZXmlType z1=contriGenData.getPotiveZ();
						ZXmlType zSource = contriGenData.getSourceZ();
						if(z1 == null ){
							//check ZSOURCE
							
							
						}
						
						ZXmlType z11 = ModelDataUtil.ZXmlMultiplyDouble(z1, factor);
						
						if(equivPosZ ==null) equivPosZ = z11;
						else{
							equivPosZ =ModelDataUtil.addParallelZ(equivPosZ, z11); 
						}
						if(contriGenData.getNegativeZ()!=null){
							ZXmlType z2=contriGenData.getNegativeZ();
							ZXmlType z21 = ModelDataUtil.ZXmlMultiplyDouble(z2, factor);
                            
							if(equivNegZ==null) equivNegZ = z21;
							else{
								equivNegZ =ModelDataUtil.addParallelZ(equivNegZ, z21); 
							}
							
						}
						
						//TODO It is very hard to consolidate the grounding Zg of multi-generators , since they are in series of ZZERO of gens
						
						
						if(contriGenData.getZeroZ()!=null){
							ZXmlType z0=contriGenData.getZeroZ();
							ZXmlType z01 = ModelDataUtil.ZXmlMultiplyDouble(z0, factor);
                            
							if(equivZeroZ==null) equivZeroZ = z01;
							else{
								equivZeroZ =ModelDataUtil.addParallelZ(equivZeroZ, z01); 
							}
						}
						
						 /*
					
						if(contriGenData.getZeroZ()!=null){
							z=contriGenData.getZeroZ();
							//Based on PSS/E convention, if Tap-up transformer is modeled as part of generator
							//then, the generator is open from the zero sequence network, which can be model by LargeBusZ , or not adding ScZ.
							boolean zeroOpen =(contriGenData.getXfrZ()==null)? false:(contriGenData.getXfrZ().getIm()==0)?false:true;
							
							if(!zeroOpen && z.getIm()!=0)acscBus.addScGenZ(new Complex(z.getRe()*factor, z.getIm()*factor),SequenceCode.ZERO);
						}
						
                       */
					}
				   // generator data is modeled at the equivalent Gen level or has been consolidated already. 
					//if no sequence data provided, gen z is init as a large Z, or open circuit
					if(equivPosZ !=null ){
						/*
						equivPosZ =BaseDataSetter.createZValue(0, 1.0e10, ZUnitType.PU);
						equivNegZ =BaseDataSetter.createZValue(0, 1.0e10, ZUnitType.PU);
						equivZeroZ =BaseDataSetter.createZValue(0, 1.0e10, ZUnitType.PU);
						
						ODMLogger.getLogger().warning("posZ of gen is not provided, Bus Id, GenId #"+scBusXml.getId());
						*/
						scEquivData.setPotiveZ(equivPosZ);
						scEquivData.setNegativeZ(equivNegZ);
						scEquivData.setZeroZ(equivZeroZ);
 					}
			}
						
		}
		return true;
	}

	/**
	 * consolidate bus scLoadContributionList to the equiv load 
	 * @throws ODMException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static boolean createBusScEquivLoadData(IODMModelParser parser ) throws ODMException {
		LoadflowNetXmlType baseCaseNet = ((AbstractModelParser<LoadflowNetXmlType, LoadflowBusXmlType, LineShortCircuitXmlType, XfrShortCircuitXmlType, PSXfrShortCircuitXmlType>) parser).getNet();

		for ( JAXBElement<? extends BusXmlType> busXml : baseCaseNet.getBusList().getBus()) {
			ShortCircuitBusXmlType scBusXml = (ShortCircuitBusXmlType)busXml.getValue();
			
			if(scBusXml.getLoadData()!=null){
				
				ShortCircuitLoadDataXmlType scEquivData = (ShortCircuitLoadDataXmlType)scBusXml.getLoadData().getEquivLoad().getValue();
				
				YXmlType equivShuntNegY  = null;
				YXmlType equivShuntZeroY = null;
				
	
				
				if(scBusXml.getLoadData().getContributeLoad()!=null){
				    for( JAXBElement<? extends LoadflowLoadDataXmlType> scContriLoadEle : scBusXml.getLoadData().getContributeLoad()) {
					     ShortCircuitLoadDataXmlType contriLoadData = (ShortCircuitLoadDataXmlType) scContriLoadEle.getValue();
					    
					     //Negative sequence Y
					     if(contriLoadData.getShuntLoadNegativeY()!=null){
					    	 if(equivShuntNegY  == null)equivShuntNegY  = contriLoadData.getShuntLoadNegativeY();
					    	 else {
					    		 ModelDataUtil.addParallelY(equivShuntNegY,contriLoadData.getShuntLoadNegativeY());
							  }
					     }
					     else{ // negative sequence load data is not provided in the input data, assume y2 = equivY1
					    	 //TODO const P and I load need to be processed and adjusted differently for load flow based
					    	 //short circuit analysis or building sequence network
					    	 
					    	 /*
								 * Use unit voltage vmag=1.0 to initialize the equivalent shuntY
								 * 
								 * equivY_0 = load.conjugate();
								 * 
								 * For load flow-based short circuit analysis, 
								 *  equivY_actual = equivY_0/ v^2  for Constant Power load
								 *                = equivY_0* v    for Constant current load
								 * 
								 */
					    	 
					    	 // However, now the sequence load modeling does not treated them separately
					    	 
					    	 
					    	 
					     }
					     
					     if(contriLoadData.getShuntLoadZeroY()!=null){
					    	 if(equivShuntZeroY  == null)equivShuntZeroY  = contriLoadData.getShuntLoadZeroY();
					    	 else {
					    		 ModelDataUtil.addParallelY(equivShuntZeroY,contriLoadData.getShuntLoadZeroY());
							  }
					     }
					     //else, zero sequence load is open, do nothing
				     }
				}
				
				// we assume Y is in pu on the system base
				if(equivShuntNegY!=null) {
					equivShuntNegY.setUnit(YUnitType.PU);
					scEquivData.setShuntLoadNegativeY(equivShuntNegY);
				}
				if(equivShuntZeroY!=null) {
					equivShuntZeroY.setUnit(YUnitType.PU);
					scEquivData.setShuntLoadZeroY(equivShuntZeroY);
				}
			}
			
						
		}
		
		return true;
	}
}
