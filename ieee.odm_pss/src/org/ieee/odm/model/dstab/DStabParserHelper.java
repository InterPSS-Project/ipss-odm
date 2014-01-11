/*
 * @(#)DStabParserHelper.java   
 *
 * Copyright (C) 2006-2010 www.interpss.org
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
 * @Date 02/01/2010
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.dstab;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.ODMObjectFactory;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.schema.BusGenDataXmlType;
import org.ieee.odm.schema.BusLoadDataXmlType;
import org.ieee.odm.schema.ClassicMachineXmlType;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabGenDataXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.Eq11Ed11MachineXmlType;
import org.ieee.odm.schema.Eq11MachineXmlType;
import org.ieee.odm.schema.Eq1Ed1MachineXmlType;
import org.ieee.odm.schema.Eq1MachineXmlType;
import org.ieee.odm.schema.EquiMachineXmlType;
import org.ieee.odm.schema.ExcBPAECXmlType;
import org.ieee.odm.schema.ExcBPAEKXmlType;
import org.ieee.odm.schema.ExcBPAFJXmlType;
import org.ieee.odm.schema.ExcBPAFKXmlType;
import org.ieee.odm.schema.ExcBPAFQXmlType;
import org.ieee.odm.schema.ExcBPAFRXmlType;
import org.ieee.odm.schema.ExcBPAFSXmlType;
import org.ieee.odm.schema.ExcBPAFUXmlType;
import org.ieee.odm.schema.ExcBPAFVXmlType;
import org.ieee.odm.schema.ExcIEEE1968Type1SXmlType;
import org.ieee.odm.schema.ExcIEEE1968Type1XmlType;
import org.ieee.odm.schema.ExcIEEE1968Type2XmlType;
import org.ieee.odm.schema.ExcIEEE1968Type3XmlType;
import org.ieee.odm.schema.ExcIEEE1968Type4XmlType;
import org.ieee.odm.schema.ExcIEEE1981ST1XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeAC2XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeDC1XmlType;
import org.ieee.odm.schema.ExcIEEE1981TypeDC2XmlType;
import org.ieee.odm.schema.ExcIEEE1992TypeAC1AXmlType;
import org.ieee.odm.schema.ExcIEEEModified1968Type1XmlType;
import org.ieee.odm.schema.ExcSimpleTypeXmlType;
import org.ieee.odm.schema.ExcTSATTypeEXC34XmlType;
import org.ieee.odm.schema.GovBPAGiGaTbCombinedModelXmlType;
import org.ieee.odm.schema.GovBPAGsTbCombinedModelXmlType;
import org.ieee.odm.schema.GovBPAHydroTurbineGHXmlType;
import org.ieee.odm.schema.GovHydroSteamGeneralModelXmlType;
import org.ieee.odm.schema.GovHydroTurbineXmlType;
import org.ieee.odm.schema.GovHydroXmlType;
import org.ieee.odm.schema.GovIEEE1981Type1XmlType;
import org.ieee.odm.schema.GovIEEE1981Type2XmlType;
import org.ieee.odm.schema.GovIEEE1981Type3XmlType;
import org.ieee.odm.schema.GovPSSEIEESGOModelXmlType;
import org.ieee.odm.schema.GovPSSETGOV1ModelXmlType;
import org.ieee.odm.schema.GovSimpleTypeXmlType;
import org.ieee.odm.schema.GovSteamNRXmlType;
import org.ieee.odm.schema.GovSteamTCDRXmlType;
import org.ieee.odm.schema.GovSteamTCSRXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.PssBPADualInputXmlType;
import org.ieee.odm.schema.PssBpaSgTypeXmlType;
import org.ieee.odm.schema.PssBpaSpTypeXmlType;
import org.ieee.odm.schema.PssBpaSsTypeXmlType;
import org.ieee.odm.schema.PssIEE2STXmlType;
import org.ieee.odm.schema.PssIEEE1981TypeXmlType;
import org.ieee.odm.schema.PssIEEE1992Type2AXmlType;
import org.ieee.odm.schema.PssIEEE1AXmlType;
import org.ieee.odm.schema.PssIEEEDualInputXmlType;
import org.ieee.odm.schema.PssSimpleTypeXmlType;
import org.ieee.odm.schema.SpeedGovBPAGSModelXmlType;
import org.ieee.odm.schema.SpeedGovBPAGiGaCombinedXmlType;
import org.ieee.odm.schema.SpeedGovBPARegGIModelXmlType;
import org.ieee.odm.schema.SpeedGovBPAServoGAModelXmlType;
import org.ieee.odm.schema.SpeedGovModelXmlType;
import org.ieee.odm.schema.SteamTurbineBPATBModelXmlType;
import org.ieee.odm.schema.SteamTurbineNRXmlType;
import org.ieee.odm.schema.SteamTurbineTCDRXmlType;
import org.ieee.odm.schema.SteamTurbineTCSRXmlType;

/**
 * DStab ODM model parser helper functions
 * 
 * @author mzhou
 *
 */
public class DStabParserHelper extends AcscParserHelper {
	/**
	 * create DStab equiv gen
	 * 
	 * @return
	 */
	/*
	public static JAXBElement<DStabGenDataXmlType> createDStabEquivGen() {
		DStabGenDataXmlType equivGen = OdmObjFactory.createDStabGenDataXmlType();
		return OdmObjFactory.createDstabEquivGen(equivGen);
	}
	*/
	
	/**
	 * get DStab Gen Data object on the acscBus with id = genId
	 * 
	 * @param dstabBus
	 * @param genId
	 * @return null if dstabGenData not found
	 */
	public static DStabGenDataXmlType getDStabContritueGen(DStabBusXmlType dstabBus, String genId) throws ODMException {
		for (JAXBElement<? extends LoadflowGenDataXmlType> elem : dstabBus.getGenData().getContributeGen()) {
			DStabGenDataXmlType dstabGenData = (DStabGenDataXmlType)elem.getValue();
			if (dstabGenData.getId().equals(genId))
				return dstabGenData;
		}
    	throw new ODMException("Generator not found, ID: " + genId + "@Bus:" + dstabBus.getId());
	}
	
	/**
	 * create a DStab Contributing Generator object
	 * 
	 */
	public static DStabGenDataXmlType createDStabContributeGen(DStabBusXmlType busRec) {
		BusGenDataXmlType genData = busRec.getGenData();
		/* this should never happen
		if (genData == null) {
			genData = OdmObjFactory.createBusGenDataXmlType();
			busRec.setGenData(genData);
			genData.setEquivGen(createDStabEquivGen());
		}
		*/
		// some model does not need ContributeGenList
		DStabGenDataXmlType contribGen = OdmObjFactory.createDStabGenDataXmlType();
		genData.getContributeGen().add(OdmObjFactory.createDstabContributeGen(contribGen));
		return contribGen;
	}
	
	/**
	 * create DStab equiv load
	 * 
	 * @return
	 */
	/*
	public static JAXBElement<DStabLoadDataXmlType> createDStabEquivLoad() {
		DStabLoadDataXmlType equivLoad = OdmObjFactory.createDStabLoadDataXmlType();
		return OdmObjFactory.createDstabEquivLoad(equivLoad);
	}
	*/

	/**
	 * create a DStab Contribution Load object
	 * 
	 */
	public static DStabLoadDataXmlType createDStabContriLoad(DStabBusXmlType busRec) {
		BusLoadDataXmlType loadData = busRec.getLoadData();
		/* this should never happen
		if (loadData == null) { 
			loadData = OdmObjFactory.createBusLoadDataXmlType();
			busRec.setLoadData(loadData);
			DStabLoadDataXmlType equivLoad = OdmObjFactory.createDStabLoadDataXmlType();
			loadData.setEquivLoad(OdmObjFactory.createDstabEquivLoad(equivLoad));
		}
		*/
		DStabLoadDataXmlType contribLoad = OdmObjFactory.createDStabLoadDataXmlType();
	    loadData.getContributeLoad().add(OdmObjFactory.createDstabContributeLoad(contribLoad)); 
	    return contribLoad;
	}	

	/*
	 * Machine model creation functions
	 * ================================
	 */

	/**
	 * create a machine model of type Classic
	 * 
	 * @param gen
	 * @return
	 */
	public static ClassicMachineXmlType createClassicMachine(DStabGenDataXmlType gen) {
		ClassicMachineXmlType mach = OdmObjFactory.createClassicMachineXmlType();
		gen.setMachineModel(OdmObjFactory.createClassicMachModel(mach));
		return mach;
	}
	
	/**
	 * create a machine model of type EquivMachine
	 * 
	 * @param gen
	 * @return
	 */
	public static EquiMachineXmlType createEquiMachine(DStabGenDataXmlType gen) {
		EquiMachineXmlType mach = OdmObjFactory.createEquiMachineXmlType();
		gen.setMachineModel(OdmObjFactory.createEquiMachModel(mach));
		return mach;
	}
	
	/**
	 * create a machine model of type Eq1
	 * 
	 * @param gen
	 * @return
	 */
	public static Eq1MachineXmlType createEq1Machine(DStabGenDataXmlType gen) {
		Eq1MachineXmlType mach = OdmObjFactory.createEq1MachineXmlType();
		gen.setMachineModel(OdmObjFactory.createEq1MachModel(mach));
		return mach;
	}
	
	/**
	 * create a machine model of type Eq1Ed1
	 * 
	 * @param gen
	 * @return
	 */
	public static Eq1Ed1MachineXmlType createEq1Ed1Machine(DStabGenDataXmlType gen) {
		Eq1Ed1MachineXmlType mach = OdmObjFactory.createEq1Ed1MachineXmlType();
		gen.setMachineModel(OdmObjFactory.createEq1Ed1MachModel(mach));
		return mach;
	}
	
	/**
	 * create a machine model of type Eq11
	 * 
	 * @param gen
	 * @return
	 */
	public static Eq11MachineXmlType createEq11Machine(DStabGenDataXmlType gen) {
		Eq11MachineXmlType mach = OdmObjFactory.createEq11MachineXmlType();
		gen.setMachineModel(OdmObjFactory.createEq11MachModel(mach));
		return mach;
	}
	
	/**
	 * create a machine model of type Eq11Ed11
	 * 
	 * @param gen
	 * @return
	 */
	public static Eq11Ed11MachineXmlType createEq11Ed11Machine(DStabGenDataXmlType gen) {
		Eq11Ed11MachineXmlType mach = OdmObjFactory.createEq11Ed11MachineXmlType();
		gen.setMachineModel(OdmObjFactory.createEq11Ed11MachModel(mach));
		return mach;
	}
	
	/**
	 * create machine Se parameter record, format 1
	 * 
	 * @return
	 */
	public static Eq1MachineXmlType.SeFmt1 createMachineSeFmt1() {
		Eq1MachineXmlType.SeFmt1 seFmt1 = OdmObjFactory.createEq1MachineXmlTypeSeFmt1();
		seFmt1.setSe100(0.0);			
		seFmt1.setSe120(0.0);
		seFmt1.setSliner(10.0);	// this means the machine will never get into saturation	
		return seFmt1;
	}
	
	/*
	 * Exciter model creation functions
	 * ================================
	 */

	/**
	 * create Exc model record of type SimpleType
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcSimpleTypeXmlType createExcSimpleTypeXmlType(DStabGenDataXmlType gen) {
		ExcSimpleTypeXmlType exc = OdmObjFactory.createExcSimpleTypeXmlType();
		gen.setExciter(OdmObjFactory.createExcSimpleType(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1992TypeAC1A
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1992TypeAC1AXmlType createExcIEEE1992TypeAC1AXmlType(DStabGenDataXmlType gen) {
		ExcIEEE1992TypeAC1AXmlType exc = OdmObjFactory.createExcIEEE1992TypeAC1AXmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1992TypeAC1A(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1981TypeAC2
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1981TypeAC2XmlType createExcIEEE1981TypeAC2XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1981TypeAC2XmlType exc = OdmObjFactory.createExcIEEE1981TypeAC2XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1981TypeAC2(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type IEEE1981TypeDC1
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1981TypeDC1XmlType createExcIEEE1981TypeDC1XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1981TypeDC1XmlType exc = OdmObjFactory.createExcIEEE1981TypeDC1XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1981TypeDC1(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type IEEE1981TypeDC2
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1981TypeDC2XmlType createExcIEEE1981TypeDC2XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1981TypeDC2XmlType exc = OdmObjFactory.createExcIEEE1981TypeDC2XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1981TypeDC2(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type IEEE1981ST1
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1981ST1XmlType createExcIEEE1981ST1XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1981ST1XmlType exc = OdmObjFactory.createExcIEEE1981ST1XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1981ST1(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1968Type1
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1968Type1XmlType createExcIEEE1968Type1XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1968Type1XmlType exc = OdmObjFactory.createExcIEEE1968Type1XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1968Type1(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEEModified1968Type1
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEEModified1968Type1XmlType createExcIEEEModified1968Type1XmlType(DStabGenDataXmlType gen) {
		ExcIEEEModified1968Type1XmlType exc = OdmObjFactory.createExcIEEEModified1968Type1XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEEModified1968Type1(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1968Type1S
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1968Type1SXmlType createExcIEEE1968Type1SXmlType(DStabGenDataXmlType gen) {
		ExcIEEE1968Type1SXmlType exc = OdmObjFactory.createExcIEEE1968Type1SXmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1968Type1S(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1968Type2
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1968Type2XmlType createExcIEEE1968Type2XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1968Type2XmlType exc = OdmObjFactory.createExcIEEE1968Type2XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1968Type2(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1968Type4
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1968Type4XmlType createExcIEEE1968Type4XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1968Type4XmlType exc = OdmObjFactory.createExcIEEE1968Type4XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1968Type4(exc));
		return exc;
	}

	/**
	 * create Exc model record of type IEEE1968Type3
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcIEEE1968Type3XmlType createExcIEEE1968Type3XmlType(DStabGenDataXmlType gen) {
		ExcIEEE1968Type3XmlType exc = OdmObjFactory.createExcIEEE1968Type3XmlType();
		gen.setExciter(OdmObjFactory.createExcIEEE1968Type3(exc));
		return exc;
	}
	


	/**
	 * create Exc model record of type TSATTypeEXC34
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcTSATTypeEXC34XmlType createExcTSATTypeEXC34XmlType(DStabGenDataXmlType gen) {
		ExcTSATTypeEXC34XmlType exc = OdmObjFactory.createExcTSATTypeEXC34XmlType();
		gen.setExciter(OdmObjFactory.createExcTSATTypeEXC34(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAEC
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAECXmlType createExcBPAECXmlType(DStabGenDataXmlType gen) {
		ExcBPAECXmlType exc = OdmObjFactory.createExcBPAECXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeEC(exc));
		return exc;
	}

	/**
	 * create Exc model record of type BPAEk
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAEKXmlType createExcBPAEKXmlType(DStabGenDataXmlType gen) {
		ExcBPAEKXmlType exc = OdmObjFactory.createExcBPAEKXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeEK(exc));
		return exc;
	}

	/**
	 * create Exc model record of type BPAFJ
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFJXmlType createExcBPAFJXmlType(DStabGenDataXmlType gen) {
		ExcBPAFJXmlType exc = OdmObjFactory.createExcBPAFJXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFJ(exc));
		return exc;
	}

	/**
	 * create Exc model record of type BPAFk
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFKXmlType createExcBPAFKXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFKXmlType exc = OdmObjFactory.createExcBPAFKXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFK(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAFQ
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFQXmlType createExcBPAFQXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFQXmlType exc = OdmObjFactory.createExcBPAFQXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFQ(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAFR
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFRXmlType createExcBPAFRXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFRXmlType exc = OdmObjFactory.createExcBPAFRXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFR(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAFS
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFSXmlType createExcBPAFSXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFSXmlType exc = OdmObjFactory.createExcBPAFSXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFS(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAFU
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFUXmlType createExcBPAFUXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFUXmlType exc = OdmObjFactory.createExcBPAFUXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFU(exc));
		return exc;
	}
	
	/**
	 * create Exc model record of type BPAFV
	 * 
	 * @param gen
	 * @return
	 */
	public static ExcBPAFVXmlType createExcBPAFVXmlType(
			DStabGenDataXmlType gen) {
		ExcBPAFVXmlType exc = OdmObjFactory.createExcBPAFVXmlType();
		gen.setExciter(OdmObjFactory.createExcBPATypeFV(exc));
		return exc;
	}
	
	/*
	 * Governor model creation functions
	 * =================================
	 */

	/**
	 * create Gov model record of type SimpleType
	 * 
	 * @param gen
	 * @return
	 */
	public static GovSimpleTypeXmlType createGovSimpleTypeXmlType(DStabGenDataXmlType gen) {
		GovSimpleTypeXmlType gov = OdmObjFactory.createGovSimpleTypeXmlType();
		gen.setGovernor(OdmObjFactory.createGovSimpleType(gov));
		return gov;
	}

	/**
	 * create Gov model record of type IEEE1981Type1
	 * 
	 * @param gen
	 * @return
	 */
	public static GovIEEE1981Type1XmlType createGovIEEE1981Type1XmlType(DStabGenDataXmlType gen) {
		GovIEEE1981Type1XmlType gov = OdmObjFactory.createGovIEEE1981Type1XmlType();
		gen.setGovernor(OdmObjFactory.createGovIEEE1981Type1(gov));
		return gov;
	}

	/**
	 * create Gov model record of type IEEE1981Type2
	 * 
	 * @param gen
	 * @return
	 */
	public static GovIEEE1981Type2XmlType createGovIEEE1981Type2XmlType(DStabGenDataXmlType gen) {
		GovIEEE1981Type2XmlType gov = OdmObjFactory.createGovIEEE1981Type2XmlType();
		gen.setGovernor(OdmObjFactory.createGovIEEE1981Type2(gov));
		return gov;
	}

	/**
	 * create Gov model record of type IEEE1981Type3
	 * 
	 * @param gen
	 * @return
	 */
	public static GovIEEE1981Type3XmlType createGovIEEE1981Type3XmlType(DStabGenDataXmlType gen) {
		GovIEEE1981Type3XmlType gov = OdmObjFactory.createGovIEEE1981Type3XmlType();
		gen.setGovernor(OdmObjFactory.createGovIEEE1981Type3(gov));
		return gov;
	}

	/**
	 * create Gov model record of type Hydro
	 * 
	 * @param gen
	 * @return
	 */
	public static GovHydroXmlType createGovHydroXmlType(DStabGenDataXmlType gen) {
		GovHydroXmlType gov = OdmObjFactory.createGovHydroXmlType();
		gen.setGovernor(OdmObjFactory.createGovHydro(gov));
		return gov;
	}

	/**
	 * create Gov model record of type HydroTurbine
	 * 
	 * @param gen
	 * @return
	 */
	public static GovHydroTurbineXmlType createGovHydroTurbineXmlType(DStabGenDataXmlType gen) {
		GovHydroTurbineXmlType gov = OdmObjFactory.createGovHydroTurbineXmlType();
		gen.setGovernor(OdmObjFactory.createGovHydroTurbine(gov));
		return gov;
	}

	/**
	 * create Gov model record of type HydroStreamGeneral
	 * 
	 * @param gen
	 * @return
	 */
	public static GovHydroSteamGeneralModelXmlType createGovHydroSteamGeneralModelXmlType(DStabGenDataXmlType gen) {
		GovHydroSteamGeneralModelXmlType gov = OdmObjFactory.createGovHydroSteamGeneralModelXmlType();
		gen.setGovernor(OdmObjFactory.createGovHydroSteamGeneralModel(gov));
		return gov;
	}
	
	/**
	 * create Gov model record of Stream speed Gov and Tandem compound,non-reheat(TCNR) turbine combo model
	 * 
	 * @param gen
	 * @return
	 */
	public static GovSteamNRXmlType createGovSteamNRXmlType(DStabGenDataXmlType gen) {
		GovSteamNRXmlType gov = OdmObjFactory.createGovSteamNRXmlType();
		gen.setGovernor(OdmObjFactory.createGovSteamNR(gov));
		return gov;
	}

	/**
	 * create Gov model record of of Stream speed Gov and Tandem compound,single-reheat (TCSR) turbine combo model
	 * 
	 * @param gen
	 * @return
	 */
	public static GovSteamTCSRXmlType createGovSteamTCSRXmlType(DStabGenDataXmlType gen) {
		GovSteamTCSRXmlType gov = OdmObjFactory.createGovSteamTCSRXmlType();
		gen.setGovernor(OdmObjFactory.createGovSteamTCSR(gov));
		return gov;
	}

	/**
	 * create Gov model record of of Stream speed Gov and Tandem compound,double-reheat(TCDR) turbine combo model
	 * 
	 * @param gen
	 * @return
	 */
	public static GovSteamTCDRXmlType createGovSteamTCDRXmlType(DStabGenDataXmlType gen) {
		GovSteamTCDRXmlType gov = OdmObjFactory.createGovSteamTCDRXmlType();
		gen.setGovernor(OdmObjFactory.createGovSteamTCDR(gov));
		return gov;
	}
	
	/*  Stream turbine  models: NR, TCSR,TCDR type
	 * ====================
	 */
	
	/**
	 * create Gov model record of type StreamTurbine
	 * 
	 * @param gen
	 * @return
	 */
	public static SteamTurbineNRXmlType createSteamTurbineNRXmlType(DStabGenDataXmlType gen){
		SteamTurbineNRXmlType stNR=OdmObjFactory.createSteamTurbineNRXmlType();
		gen.getGovernor().getValue().setTurbine(OdmObjFactory.createTurbineSteamNR(stNR));
		return stNR;
		
	}

	/**
	 * create Gov model record of type StreamTurbineTCSRR
	 * 
	 * @param gen
	 * @return
	 */
	public static SteamTurbineTCSRXmlType createSteamTurbineTCSRRXmlType(DStabGenDataXmlType gen){
		SteamTurbineTCSRXmlType stTCSR=OdmObjFactory.createSteamTurbineTCSRXmlType();
		gen.getGovernor().getValue().setTurbine(OdmObjFactory.createTurbineSteamTCSR(stTCSR));
		return stTCSR;
		
	}
	
	/**
	 * create Gov model record of type StreamTurbineTCDR
	 * 
	 * @param gen
	 * @return
	 */
	public static SteamTurbineTCDRXmlType createSteamTurbineTCDRXmlType(DStabGenDataXmlType gen){
		SteamTurbineTCDRXmlType stTCDR=OdmObjFactory.createSteamTurbineTCDRXmlType();
		gen.getGovernor().getValue().setTurbine(OdmObjFactory.createTurbineSteamNR(stTCDR));
		return stTCDR;
		
	}
	
	//****************************************************************************
	// The following are PSSE Governor models, including speedGov and turbine 
	//*****************************************************************************
	
	/**
	 * create the general-purpose turbine-governor model IEESGO, based on IEEE 1973 gov model
	 * @param gen
	 * @return
	 */
	public static GovPSSEIEESGOModelXmlType createGovPSSEIEESGOXmlType(DStabGenDataXmlType gen){
		GovPSSEIEESGOModelXmlType ieeeSGO=OdmObjFactory.createGovPSSEIEESGOModelXmlType();
		gen.setGovernor(OdmObjFactory.createGovPSSEIEESGO(ieeeSGO));
		return ieeeSGO;
		
	}
	
	
	public static GovPSSETGOV1ModelXmlType createGovPSSETGOV1XmlType(DStabGenDataXmlType gen){
		GovPSSETGOV1ModelXmlType tgov1=OdmObjFactory.createGovPSSETGOV1ModelXmlType();
		gen.setGovernor(OdmObjFactory.createGovPSSETGOV1(tgov1));
		return tgov1;
		
	}
	
	
	
	//****************************************************************************
	// The following are BPA Governor models, including speedGov and turbine 
	//*****************************************************************************
	
	//GH 
	/**
	 * create Gov model record of type BPAHydroTurbineGH
	 * 
	 * @param gen
	 * @return
	 */
	public static GovBPAHydroTurbineGHXmlType createGovBPAHydroTurbineGHXmlType(
			DStabGenDataXmlType gen) {
		GovBPAHydroTurbineGHXmlType gov=OdmObjFactory.createGovBPAHydroTurbineGHXmlType();
		gen.setGovernor(OdmObjFactory.createGovBPAHydroTurbineGHModel(gov));
		
		return gov;
	}

	/**
	 * create Gov model record of type BPAGSModel
	 * 
	 * @param gen
	 * @return
	 */
	public static SpeedGovBPAGSModelXmlType createSpeedGovBPAGSModelXmlType(
			DStabGenDataXmlType gen) throws ODMException {
		SpeedGovBPAGSModelXmlType spdgov=OdmObjFactory.createSpeedGovBPAGSModelXmlType();
		if(gen.getGovernor()==null){
			gen.setGovernor(OdmObjFactory.createGovBPAGsTbCombinedModel(
					createGovBPAGsTbCombinedModelXmlType(gen)));
		}
		if(gen.getGovernor()!=null){
		gen.getGovernor().getValue().setSpeedGov(OdmObjFactory.createSpeedGov(spdgov));
		}
		return spdgov;
       
		
	}
	
	//  GI/I+ model is  a regulator, part of a speed Governing model,
	/**
	 * create Gov model record of type BPARegGIModel
	 * 
	 * @param gen
	 * @return
	 */
	public static SpeedGovBPARegGIModelXmlType createSpeedGovBPARegGIModelXmlType(
			DStabGenDataXmlType gen) throws ODMException {
		SpeedGovBPARegGIModelXmlType regGi =OdmObjFactory.createSpeedGovBPARegGIModelXmlType();
		// before linking a regulator to a gen,  it need to be first connected to SpeedGov.
		SpeedGovModelXmlType spdGov=null;
		if(gen.getGovernor().getValue().getSpeedGov().getValue()!=null){
			spdGov=gen.getGovernor().getValue().getSpeedGov().getValue();
		
		   if (spdGov instanceof SpeedGovBPAGiGaCombinedXmlType) {
				SpeedGovBPAGiGaCombinedXmlType giGa = (SpeedGovBPAGiGaCombinedXmlType) spdGov;
				giGa.setRegulator(regGi);
			}
		  return regGi;
	    }
		throw new ODMException("Error:SpeedGov not created yet!");

	}

	/**
	 * create Gov model record of type BPAGiGaCombined
	 * 
	 * @param gen
	 * @return
	 */
	public static SpeedGovBPAGiGaCombinedXmlType createSpeedGovBPAGiGaCombinedXmlType(
			DStabGenDataXmlType gen) {
		SpeedGovBPAGiGaCombinedXmlType spdgov=OdmObjFactory.createSpeedGovBPAGiGaCombinedXmlType();
		
        //create a governor when it is none in a generator;
		gen.getGovernor().getValue().setSpeedGov(OdmObjFactory.createSpeedGov(spdgov));
		return spdgov;
	}

	
	//GA model is a servo motor model, part of a speed Governing model.
	/**
	 * create Gov model record of type BPAServoGAModel
	 * 
	 * @param gen
	 * @return
	 */
	public static SpeedGovBPAServoGAModelXmlType createSpeedGovBPAServoGAModelXmlType(
			DStabGenDataXmlType dynGen) throws ODMException {
		SpeedGovBPAServoGAModelXmlType servoGa =OdmObjFactory.createSpeedGovBPAServoGAModelXmlType();
		// before linking a servo to a gen,  it need to be first connected to SpeedGov.
		
		SpeedGovModelXmlType spdGov=null;
		if(dynGen.getGovernor().getValue().getSpeedGov().getValue()!=null){
			spdGov=dynGen.getGovernor().getValue().getSpeedGov().getValue();
		
		   if (spdGov instanceof SpeedGovBPAGiGaCombinedXmlType) {
				SpeedGovBPAGiGaCombinedXmlType giGa = (SpeedGovBPAGiGaCombinedXmlType) spdGov;
				giGa.setServo(servoGa);
			}
		  return servoGa;
	    }
		throw new ODMException("SpeedGov not created yet!");

	}
	
	/**
	 * create Gov model record of type BPATBModel
	 * 
	 * @param gen
	 * @return
	 */
	public static SteamTurbineBPATBModelXmlType createSteamTurbineBPATBModelXmlType(
			DStabGenDataXmlType gen) {
		SteamTurbineBPATBModelXmlType tur=OdmObjFactory.createSteamTurbineBPATBModelXmlType();
        //create a governor when it is none in a generator;
		//if(gen.getGovernor()==null)gen.setGovernor(odmObjFactory.createGovernorModelXmlType());
		gen.getGovernor().getValue().setTurbine(OdmObjFactory.createTurbine(tur));
		return tur;
	}

	//GS+TB
	/**
	 * create Gov model record of type BPAGsTbCombinedModel
	 * 
	 * @param gen
	 * @return
	 */
	public static GovBPAGsTbCombinedModelXmlType createGovBPAGsTbCombinedModelXmlType(
			DStabGenDataXmlType gen) {
		GovBPAGsTbCombinedModelXmlType gov=OdmObjFactory.createGovBPAGsTbCombinedModelXmlType();
		gen.setGovernor(OdmObjFactory.createGovBPAGsTbCombinedModel(gov));
		return gov;
	}

	//GI+GA+TB
	/**
	 * create Gov model record of type BPAGiGaCombinedModel
	 * 
	 * @param gen
	 * @return
	 */
	
	public static GovBPAGiGaTbCombinedModelXmlType createGovBPAGiGaTbCombinedModelXmlType(
			DStabGenDataXmlType gen) {
		GovBPAGiGaTbCombinedModelXmlType gov=OdmObjFactory.createGovBPAGiGaTbCombinedModelXmlType();
		gen.setGovernor(OdmObjFactory.createGovBPAGiGaTbCombinedModel(gov));		
		return gov;
		
	}

	/*
	 * PSS model creation functions
	 * ============================
	 */
	
	/**
	 * create PSS model record of type SimpleType
	 * 
	 * @param gen
	 * @return
	 */
	public static PssSimpleTypeXmlType createPssSimpleTypeXmlType(DStabGenDataXmlType gen) {
		PssSimpleTypeXmlType pss = OdmObjFactory.createPssSimpleTypeXmlType();
		gen.setStabilizer(OdmObjFactory.createPssSimpleType(pss));
		return pss;
	}

	/**
	 * create PSS model record of type IEEE1981Type
	 * 
	 * @param gen
	 * @return
	 */
	public static PssIEEE1981TypeXmlType createPssIEEE1981TypeXmlType(DStabGenDataXmlType gen) {
		PssIEEE1981TypeXmlType pss = OdmObjFactory.createPssIEEE1981TypeXmlType();
		gen.setStabilizer(OdmObjFactory.createPssIEEE1981Type(pss));
		return pss;
	}

	/**
	 * create PSS model record of type IEEE1992Type
	 * 
	 * @param gen
	 * @return
	 */
	public static PssIEEE1992Type2AXmlType createPssIEEE1992Type2AXmlType(DStabGenDataXmlType gen) {
		PssIEEE1992Type2AXmlType pss = OdmObjFactory.createPssIEEE1992Type2AXmlType();
		gen.setStabilizer(OdmObjFactory.createPssIEEE1992Type2A(pss));
		return pss;
	}

	/**
	 * create PSS model record of type IEEE1A
	 * 
	 * @param gen
	 * @return
	 */
	public static PssIEEE1AXmlType createPssIEEE1AXmlType(DStabGenDataXmlType gen) {
		PssIEEE1AXmlType pss = OdmObjFactory.createPssIEEE1AXmlType();
		gen.setStabilizer(OdmObjFactory.createPssIEEEType1A(pss));
		return pss;
	}

	/**
	 * create PSS model record of type IEEE2ST
	 * 
	 * @param gen
	 * @return
	 */
	public static PssIEE2STXmlType createPssIEE2STXmlType(DStabGenDataXmlType gen) {
		PssIEE2STXmlType pss = OdmObjFactory.createPssIEE2STXmlType();
		gen.setStabilizer(OdmObjFactory.createPssIEE2ST(pss));
		return pss;
	}

	/**
	 * create PSS model record of type IEEEDualInput
	 * 
	 * @param gen
	 * @return
	 */
	public static PssIEEEDualInputXmlType createPssIEEEDualInputXmlType(DStabGenDataXmlType gen) {
		PssIEEEDualInputXmlType pss = OdmObjFactory.createPssIEEEDualInputXmlType();
		gen.setStabilizer(OdmObjFactory.createPssIEEEDualInput(pss));
		return pss;
	}
	
	/**
	 * create PSS model record of type BPADualInput
	 * 
	 * @param gen
	 * @return
	 */
	public static PssBPADualInputXmlType createPssBPADualInputXmlType(DStabGenDataXmlType gen) {
		PssBPADualInputXmlType pss = OdmObjFactory.createPssBPADualInputXmlType();
		gen.setStabilizer(OdmObjFactory.createPssBPADualInput(pss));
		return pss;
	}
	
	/**
	 * create PSS model record of type BPASs
	 * 
	 * @param gen
	 * @return
	 */
	public static PssBpaSsTypeXmlType createPssBPASsXmlType(DStabGenDataXmlType gen) {
		PssBpaSsTypeXmlType pss = OdmObjFactory.createPssBpaSsTypeXmlType();
		gen.setStabilizer(OdmObjFactory.createPssBPASs(pss));
		return pss;
	}
	
	/**
	 * create PSS model record of type BPASg
	 * 
	 * @param gen
	 * @return
	 */
	public static PssBpaSgTypeXmlType createPssBPASgXmlType(DStabGenDataXmlType gen) {
		PssBpaSgTypeXmlType pss = OdmObjFactory.createPssBpaSgTypeXmlType();
		gen.setStabilizer(OdmObjFactory.createPssBPASg(pss));
		return pss;
	}
	
	/**
	 * create PSS model record of type BPASp
	 * 
	 * @param gen
	 * @return
	 */
	public static PssBpaSpTypeXmlType createPssBPASpXmlType(DStabGenDataXmlType gen) {
		PssBpaSpTypeXmlType pss = OdmObjFactory.createPssBpaSpTypeXmlType();
		gen.setStabilizer(OdmObjFactory.createPssBPASp(pss));
		return pss;
	}
}
