/*
 * @(#)AbstractModelParser.java   
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

package org.ieee.odm.model;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;
import static org.ieee.odm.common.ODMModelContansts.ODM_Schema_NS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.base.BaseJaxbHelper;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.AnalysisCategoryEnumType;
import org.ieee.odm.schema.BaseBranchXmlType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusIDRefXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.ContentInfoXmlType;
import org.ieee.odm.schema.IDRecordXmlType;
import org.ieee.odm.schema.LineBranchXmlType;
import org.ieee.odm.schema.ModifyRecordXmlType;
import org.ieee.odm.schema.NetAreaXmlType;
import org.ieee.odm.schema.NetZoneXmlType;
import org.ieee.odm.schema.NetworkCategoryEnumType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.ieee.odm.schema.PSXfrBranchXmlType;
import org.ieee.odm.schema.StudyCaseXmlType;
import org.ieee.odm.schema.StudyScenarioXmlType;
import org.ieee.odm.schema.XfrBranchXmlType;

/**
 * Abstract Xml parser implementation as the base for all the IEEE DOM schema parsers. 
 * 
 * 
 */
public abstract class AbstractModelParser<TNetXml extends NetworkXmlType> implements IODMModelParser {
	/** input text file encoding */
	protected String encoding = IODMModelParser.DefaultEncoding;
	
	/** bus and branch object cache for fast lookup. */ 
	protected HashMap<String,IDRecordXmlType> objectCache = null;
	
	/** bus No to Id lookup table. */ 
	protected HashMap<Long, String> busNo2IdLookup = null;

	/** the root StudyCase element */
	protected StudyCaseXmlType pssStudyCase = null;
	
	private static Unmarshaller unmarshaller = null;
	private static Marshaller marshaller = null;

	/**
	 * Default Constructor 
	 * 
	 */
	public AbstractModelParser() {
		this.objectCache = new HashMap<>();
		this.busNo2IdLookup = new HashMap<>();
		if (!(this instanceof ODMModelParser)) {
			this.getStudyCase().setId("ODM_StudyCase");
		}
	}

	/**
	 * constructor
	 * 
	 * @param encoding input text encoding
	 */
	public AbstractModelParser(String encoding) {
		this();
		this.encoding = encoding;
	}
	
	/**
	 * get encoding
	 * 
	 * @return
	 */
	public String getEncoding() { return this.encoding; }
	
	/**
	 * set encoding
	 * 
	 * @param e encoding string
	 */
	public void setEncoding(String e) { this.encoding = e; }

	/**
	 * get object cache lookup table
	 * 
	 * @return
	 */
	public HashMap<String,IDRecordXmlType> getObjectCache() { return this.objectCache; }

	/**
	 * parse the xml input file to create a model parser object
	 * 
	 * @param xmlFile input xml file
	 */
	@SuppressWarnings("unchecked") public boolean parse(File xmlFile) {
		try {
			JAXBElement<StudyCaseXmlType> elem = (JAXBElement<StudyCaseXmlType>)createUnmarshaller().unmarshal(xmlFile);
			this.pssStudyCase = elem.getValue();
			return true;
		} catch (JAXBException e) { 
			e.printStackTrace();
			ODMLogger.getLogger().severe(e.toString());
			return false;
		}
	}

	/**
	 * parse the xml string, representing an input xml file, to create a model parser object
	 * 
	 * @param xmlFile
	 */
	@SuppressWarnings("unchecked") public boolean parse(String xmlString) {
		try {
			ByteArrayInputStream bStr = new ByteArrayInputStream(xmlString.getBytes());
			JAXBElement<StudyCaseXmlType> elem = (JAXBElement<StudyCaseXmlType>)createUnmarshaller().unmarshal(bStr);
			this.pssStudyCase = elem.getValue();
			return true;
		} catch (JAXBException e) { 			
			e.printStackTrace();
			ODMLogger.getLogger().severe(e.toString());
			return false;
		}
	}
	
	/**
	 * parse the xml file input stream to create a model parser object
	 * 
	 * @param xmlFile
	 */
	@SuppressWarnings("unchecked") public boolean parse(InputStream in) {
		try {
			Object obj = createUnmarshaller().unmarshal(in);
			JAXBElement<StudyCaseXmlType> elem = (JAXBElement<StudyCaseXmlType>)obj;
			this.pssStudyCase = elem.getValue();
		} catch (JAXBException e) { 
			//e.printStackTrace();
			ODMLogger.getLogger().severe("ODM xml doc parsing error, " + e.toString());
			return false;
		}
		// cache the loaded bus and branch objects
		for (JAXBElement<? extends BusXmlType> bus : this.getBaseCase().getBusList().getBus()) {
			BusXmlType b = bus.getValue();
			this.objectCache.put(b.getId(), b);
		}
		for (JAXBElement<? extends BaseBranchXmlType> branch : this.getBaseCase().getBranchList().getBranch()) {
			BaseBranchXmlType b = branch.getValue();
			this.objectCache.put(b.getId(), b);
		}
		return true;
	}
	
	/*
	 * 	Abstract functions
	 * 	================== 
	 */

	/**
	 * create BaseCase object, which should be a child of NetworkXmlType, for
	 * example LoadflowXmlType
	 */
	protected abstract TNetXml createBaseCase();
	
	/**
	 * get the base case object
	 * 
	 * @return
	 */
	public TNetXml getNet() {
		return getBaseCase();
	}
	
	/**
	 * check if the network info stored in the model parser object is for 
	 * transmission network type and loadflow analysis type
	 */
	public boolean isTransmissionLoadflow() {
		return this.getStudyCase().getNetworkCategory() == NetworkCategoryEnumType.TRANSMISSION &&
			   this.getStudyCase().getAnalysisCategory() == AnalysisCategoryEnumType.LOADFLOW;
	}
	
	/*
	 * 	Case/Network level functions
	 * 	=========================== 
	 */

	/**
	 * initial case content info, set AnalysisCategory = Loadflow, NetworkCategory = Transmission 
	 * 
	 * @param originalFormat
	 */
	public void initCaseContentInfo(OriginalDataFormatEnumType originalDataFormat) {
		ContentInfoXmlType info = OdmObjFactory.createContentInfoXmlType();
		getStudyCase().setContentInfo(info);
		info.setOriginalDataFormat(originalDataFormat);
		info.setAdapterProviderName("www.interpss.org");
		info.setAdapterProviderVersion("1.00");
		
		getStudyCase().setAnalysisCategory(
				AnalysisCategoryEnumType.LOADFLOW);
		getStudyCase().setNetworkCategory(
				NetworkCategoryEnumType.TRANSMISSION);		
	}
	
	/**
	 * Set BaseCase content info
	 * 
	 * @param originalDataFormat
	 */
	public void setCaseContentInfo(OriginalDataFormatEnumType originalDataFormat) {
		ContentInfoXmlType info = OdmObjFactory.createContentInfoXmlType();
		getStudyCase().setContentInfo(info);
		info.setOriginalDataFormat(originalDataFormat);
		info.setAdapterProviderName("www.interpss.org");
		info.setAdapterProviderVersion("1.00");
	}
	
	/**
	 * Get the root schema element StudyCase
	 * 
	 * @return the StudyCase element
	 */
	public StudyCaseXmlType getStudyCase() {
		if (this.pssStudyCase == null) {
			this.pssStudyCase = new StudyCaseXmlType();
			this.pssStudyCase.setBaseCase(BaseJaxbHelper.network(createBaseCase()));
		}	
		return this.pssStudyCase;
	}

	/**
	 * get the base case network object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked") protected TNetXml getBaseCase() {
		return (TNetXml)this.pssStudyCase.getBaseCase().getValue();
	}

	/**
	 * get child network list
	 * 
	 * @return the list
	 */
	public List<JAXBElement<? extends NetworkXmlType>> getChildNetList() {
		return this.pssStudyCase.getChildNet();
	}
	
	@Override public ModifyRecordXmlType getModification() {
		return this.pssStudyCase.getModificationList() == null? null :
			this.pssStudyCase.getModificationList().getModification().get(0);
	}

	@Override public ModifyRecordXmlType getModification(String id) {
		if (this.pssStudyCase.getModificationList() != null)
			for (ModifyRecordXmlType mod : this.pssStudyCase.getModificationList().getModification()) {
				if (mod.getId().equals(id))
						return mod;
			}
		ODMLogger.getLogger().warning("Modification record not found, id: " + id);
		return null;
	}
	
	@Override public StudyScenarioXmlType getStudyScenario() {
		return this.pssStudyCase.getStudyScenario() == null? null :
					this.pssStudyCase.getStudyScenario().getValue();
	}

	/**
	 * create an area xml record
	 * 
	 * @return the area xml record
	 */
	public NetAreaXmlType createNetworkArea() {
		if(getBaseCase().getAreaList() == null){
			getBaseCase().setAreaList(OdmObjFactory.createNetworkXmlTypeAreaList());
		}
		NetAreaXmlType area = OdmObjFactory.createNetAreaXmlType();
		getBaseCase().getAreaList().getArea().add(area);
		return area;
	}

	/**
	 * create a LossZone object
	 * 
	 * @return the loss zone object
	 */
	public NetZoneXmlType createNetworkLossZone() {
		if(getBaseCase().getLossZoneList() == null){
			getBaseCase().setLossZoneList(OdmObjFactory.createNetworkXmlTypeLossZoneList());
		}
		NetZoneXmlType zone = OdmObjFactory.createNetZoneXmlType();
		getBaseCase().getLossZoneList().getLossZone().add(zone);
		return zone;
	}
	
	/*
	 * 	Bus/Branch object, reference functions
	 * 	======================================
	 */
	
	/**
	 * Get the cashed object by id
	 * 
	 * @param id
	 * @return
	 */
	protected IDRecordXmlType getCachedObject(String id) {
		return this.objectCache.get(id);
	}	
	
	/**
	 * Get the cashed object by id
	 * 
	 * @param id
	 */
	protected void removeCachedObject(String id) {
		this.objectCache.remove(id);
	}
	
	/*
	 *    Bus functions
	 *    =============
	 */

	/**
	 * Get the cashed bus object by id
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked") public <T extends BusXmlType> T getBus(String id) {
		return (T)this.getCachedObject(id);
	}	

	/**
	 * Get the bus id by number
	 * 
	 * @param n bus number
	 * @return
	 */
	public String getBusId(long n) {
		return this.busNo2IdLookup.get(n);
	}	
	
	/**
	 * add a bus object into the BaseCase bus list and to the cashe table
	 * 
	 * @param bus bus object to be added
	 */
	public <T extends BusXmlType> void addBus(T bus) {
		getBaseCase().getBusList().getBus().add(BaseJaxbHelper.bus(bus));
		this.objectCache.put(bus.getId(), bus);
	}
	
	/**
	 * remove a bus object from the cache and BaseCase bus list
	 * 
	 * @param busId
	 * @return
	 */
	public boolean removeBus(String busId) {
		this.removeCachedObject(busId);
		for (JAXBElement<? extends BusXmlType> busElem : this.getBaseCase().getBusList().getBus()) {
			if (busId.equals(busElem.getValue().getId())) {
				this.getBaseCase().getBusList().getBus().remove(busElem);
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * First remove the bus with the busId and then add the bus record
	 * 
	 * @param busId id of the bus to be removed
	 * @param bus bus object to be added
	 */
	public <T extends BusXmlType> void replaceBus(String busId, T bus) {
		this.removeBus(busId);
		this.addBus(bus);
	}
	
	/**
	 * set bus record id and add the bus object into the cache
	 * 
	 * @param busRec
	 * @param id
	 * @throws Exception
	 */
	public <T extends BusXmlType> void setBusId(T busRec, String id) throws ODMException {
		busRec.setId(id);
		if (this.objectCache.get(id) != null) {
			throw new ODMException("Bus record duplication, bus id: " + id);
		}
		this.objectCache.put(id, busRec);
	}
	
	/**
	 * create a ref record with id
	 * 
	 * @param id
	 * @return
	 */
	public BusIDRefXmlType createBusRef(String id) {
		BusXmlType rec = this.getBus(id);
		if (rec != null) {
			BusIDRefXmlType refBus = OdmObjFactory.createBusIDRefXmlType();
			refBus.setIdRef(rec);
			return refBus;
		}
		return null;
	}
	
	/**
	 * add a new Bus record to the base case
	 * 
	 * @return
	 */
	protected abstract <T extends BusXmlType> T createBus();
	
	/**
	 * create a bus object with the id, make sure there is no duplication
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T extends BusXmlType> T createBus(String id) throws ODMException {
		T busRec = createBus();
		busRec.setId(id);
		if (this.objectCache.get(id) != null) {
			throw new ODMException("Bus record duplication, bus id: " + id);
		}
		this.objectCache.put(id, busRec);
		return busRec;
	}		
	
	/**
	 * add a new bus record to the base case and to the cache table
	 * 
	 * @param id
	 * @param number
	 * @return
	 */
	public <T extends BusXmlType> T createBus(String id, long number) throws ODMException {
		T busRec = createBus(id);
		busRec.setNumber(number);
		this.busNo2IdLookup.put(number, id);
		return busRec;
	}	

	/*
	 *    Branch functions
	 *    ================
	 */
	/**
	 * create Line branch object, an abstract method to be implemented by the subclass
	 * 
	 * @return
	 */
	protected abstract <T extends LineBranchXmlType> T createLineBranch();
	
	/**
	 * create Xformer branch object, an abstract method to be implemented by the subclass
	 * 
	 * @return
	 */
	protected abstract <T extends XfrBranchXmlType> T  createXfrBranch();
	
	/**
	 * create 3W Xformer branch object, an abstract method to be implemented by the subclass
	 * 
	 * @return
	 */
	protected abstract <T extends BaseBranchXmlType> T  createXfr3WBranch();
	
	/**
	 * create PsXformer branch object, an abstract method to be implemented by the subclass
	 * 
	 * @return
	 */
	protected abstract <T extends PSXfrBranchXmlType> T  createPSXfrBranch();

	/**
	 * create 3W PsXformer branch object, an abstract method to be implemented by the subclass
	 * 
	 * @return
	 */
	protected abstract <T extends BaseBranchXmlType> T  createPSXfr3WBranch();
	
	/**
	 * create Line branch object
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param cirId branch circuit number
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends LineBranchXmlType> T createLineBranch(String fBusId,String toBusId, String cirId) throws ODMException, ODMBranchDuplicationException {
		T branch = createLineBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, null, cirId);
		return branch;
	}
	
	/**
	 * create Xformer branch object
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param cirId branch circuit number
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends XfrBranchXmlType> T createXfrBranch(String fBusId, String toBusId, String cirId) throws ODMException, ODMBranchDuplicationException {
		T branch = createXfrBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, null, cirId);
		return branch;
	
	}
	
	/**
	 * for creating PSASP Xformer branch object 
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param cirId branch circuit number
	 * @param idName branch name, which is used to be an identifier
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends XfrBranchXmlType> T createXfrBranch(String fBusId, String toBusId, String cirId, String idName) throws ODMException, ODMBranchDuplicationException {
		T branch = createXfrBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, null, cirId, idName);
		return branch;
	
	}
	
	
	/**
	 * create 3W Xformer branch object
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param terBusId tertiary bus id
	 * @param cirId branch circuit number
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends BaseBranchXmlType> T createXfr3WBranch(String fBusId,String toBusId, String terBusId, String cirId) throws ODMException, ODMBranchDuplicationException {
		T branch = createXfr3WBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, terBusId, cirId);
		return branch;
	
	}
	
	/**
	 * create PsXformer branch object
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param cirId branch circuit number
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends PSXfrBranchXmlType> T createPSXfrBranch(String fBusId,String toBusId, String cirId) throws ODMException, ODMBranchDuplicationException {
		T branch = createPSXfrBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, null, cirId);
		return branch;
	}
	
	/**
	 * create PsXformer branch object
	 * 
	 * @param fBusId from bus id
	 * @param toBusId to bus id
	 * @param cirId branch circuit number
	 * @param idName branch name, which is used to be an identifier
	 * @return
	 * @throws ODMException
	 * @throws ODMBranchDuplicationException
	 */
	public <T extends PSXfrBranchXmlType> T createPSXfrBranch(String fBusId,String toBusId, String cirId, String idName) throws ODMException, ODMBranchDuplicationException {
		T branch = createPSXfrBranch();
		intiBranchData(branch);
		addBranch2BaseCase(branch, fBusId, toBusId, null, cirId, idName);
		return branch;
	}
	
	
	/**
	 * Get the cashed branch object by id
	 * 
	 * @param branchId
	 * @return
	 */
	public BaseBranchXmlType getBranch(String branchId) {
		return (BaseBranchXmlType)this.getCachedObject(branchId); 
	}

	/**
	 * remove the branch object from the cache and branch list
	 * 
	 * @param branchId
	 * @return
	 */
	public boolean removeBranch(String branchId) {
		this.removeCachedObject(branchId);
		for (JAXBElement<? extends BaseBranchXmlType> braElem : this.getBaseCase().getBranchList().getBranch()) {
			if (branchId.equals(braElem.getValue().getId())) {
				this.getBaseCase().getBranchList().getBranch().remove(braElem);
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * add a branch object into the branch list and to the cashe table
	 *
	 * @param branch
	 */
	public void addBranch(BranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		this.objectCache.put(branch.getId(), branch);
	}
	
	/**
	 * get the cashed branch record using fromId, toId and cirId
	 * 
	 * @param fromId
	 * @param toId
	 * @param cirId
	 * @return
	 */
	public BaseBranchXmlType getBranch(String fromId, String toId, String cirId) {
		String id = ODMModelStringUtil.formBranchId(fromId, toId, cirId);
		return this.getBranch(id);
	}	
	
	/**
	 * get the cashed branch record using fromId, toId and cirId
	 * 
	 * @param fromId
	 * @param toId
	 * @param tertId
	 * @param cirId
	 * @return
	 */
	public BaseBranchXmlType getBranch(String fromId, String toId, String tertId, String cirId) {
		String id = ODMModelStringUtil.formBranchId(fromId, toId, tertId, cirId);
		return this.getBranch(id);
	}	
	
	/**
	 * First remove the branch with the braId and then add the branch record
	 * 
	 * @param branchId id of the branch to be removed
	 * @param branch branch object to be added
	 */
	public void replaceBranch(String branchId, BranchXmlType branch) {
		this.removeBranch(branchId);
		this.addBranch(branch);
	}
	
	/**
	 * initialize a BaseBranchXmlType object.
	 * 
	 * @param branch
	 */
	protected void intiBranchData(BaseBranchXmlType branch) {
		getBaseCase().getBranchList().getBranch().add(BaseJaxbHelper.branch(branch));
		branch.setOffLine(false);
		branch.setAreaNumber(1);
		branch.setZoneNumber(1);
	}
	
	/**
	 * add a BaseBranchXmlType object to the BaseCase
	 * 
	 * @param branch
	 * @param fromId
	 * @param toId
	 * @param tertId
	 * @param cirId
	 * @throws ODMBranchDuplicationException
	 */
	protected void addBranch2BaseCase(BaseBranchXmlType branch, String fromId, String toId, String tertId, String cirId)  throws ODMBranchDuplicationException {
		String id = "";
		if (tertId != null && !tertId.trim().isEmpty()) {
			id = ODMModelStringUtil.formBranchId(fromId, toId, tertId, cirId);
			if (this.objectCache.get(id) != null) {
				throw new ODMBranchDuplicationException("Branch record duplication, branch id: " + id);
			}
		}
		else {
			id = ODMModelStringUtil.formBranchId(fromId, toId, cirId);
			if (this.objectCache.get(id) != null) {
				throw new ODMBranchDuplicationException("Branch record duplication, branch id: " + id);
			}
		}
		
		this.objectCache.put(id, branch);		
		branch.setCircuitId(cirId);
		branch.setId(id);
		branch.setFromBus(createBusRef(fromId));
		branch.setToBus(createBusRef(toId));		
		if (tertId != null)
			branch.setTertiaryBus(createBusRef(tertId));		
	}	
	
	/**
	 * There are cases that a Xfr/PsXfr branch needs to be retrieved by its idName. Add a BaseBranchXmlType object to the BaseCase. 
	 * The branch can be by branch id or idName
	 * 
	 * @param branch
	 * @param fromId
	 * @param toId
	 * @param tertId
	 * @param cirId
	 * @param idName branch name, which is used to be an identifier
	 * @throws ODMBranchDuplicationException
	 */
	protected void addBranch2BaseCase(BaseBranchXmlType branch, String fromId, String toId, String tertId, String cirId, String idName)  throws ODMBranchDuplicationException {
		addBranch2BaseCase(branch, fromId, toId, tertId, cirId);	
		branch.setName(idName);
		// add the following item to the hashtable so that Xfr can be retrieved using its idName
		this.objectCache.put(idName, branch);		
	}	

	/*
	 * 	marshall/unmarshall, out functions
	 * 	==================================
	 */

	/**
	 * create a Jaxb unmarshaller to unmarshall Xml document to odm object
	 *  
	 * @return an unmarshaller object
	 * @throws JAXBException
	 */
	public Unmarshaller createUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			JAXBContext jaxbContext = JAXBContext.newInstance(ODM_Schema_NS);
			unmarshaller = jaxbContext.createUnmarshaller();
			//unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "GB18030");
		}
		return unmarshaller;
	}	

	/**
	 * create a Jaxb marshaller to marshall the odm object to an Xml document
	 * 
	 * @return a marshaller object
	 * @throws JAXBException
	 */
	public Marshaller createMarshaller() throws JAXBException {
		if (marshaller == null) {
			JAXBContext jaxbContext	= JAXBContext.newInstance(ODM_Schema_NS);
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			//marshaller.setProperty(Marshaller.JAXB_ENCODING, "GB18030");
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		}
		return marshaller;
	}
	
	/**
	 * print the Xml doc to the std out
	 * 
	 */
	public void stdout() {
		try {
			JAXBElement<StudyCaseXmlType> element = OdmObjFactory.createPssStudyCase(getStudyCase());
			createMarshaller().marshal( element, System.out );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * convert the model parser to a string
	 */
	public String toXmlDoc() {
		OutputStream ostream = new ByteArrayOutputStream();
		try {
			JAXBElement<StudyCaseXmlType> element = OdmObjFactory.createPssStudyCase(getStudyCase());
			createMarshaller().marshal( element, ostream );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ostream.toString();
	}	
	
	/**
	 * convert the model parser to a string and write to the file
	 * 
	 * @param outfile
	 */
	public String toXmlDoc(String outfile) {
		if (outfile == null)
			return toXmlDoc();
		else {
			try {
				OutputStream ostream = new FileOutputStream(new File(outfile));
				JAXBElement<StudyCaseXmlType> element = OdmObjFactory.createPssStudyCase(getStudyCase());
				createMarshaller().marshal( element, ostream );
			} catch (Exception e) {
				return e.toString() + " " + outfile;
			}
			return "ODM xml doc write to "  + outfile;
		}
	}
}