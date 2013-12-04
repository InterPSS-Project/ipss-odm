/*
 * @(#)IpssScenarioHelper.java   
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
 * @Date 09/01/2011
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.model.ext.ipss;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;
import static org.ieee.odm.model.ext.ipss.IpssAnalysisCaseFunc.initPTradingEDHourlyAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.ODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.AclfAnalysisXmlType;
import org.ieee.odm.schema.AcscBranchFaultXmlType;
import org.ieee.odm.schema.AcscBusFaultXmlType;
import org.ieee.odm.schema.AcscFaultAnalysisXmlType;
import org.ieee.odm.schema.AggregateGenXmlType;
import org.ieee.odm.schema.BranchRefXmlType;
import org.ieee.odm.schema.BranchShiftFactorXmlType;
import org.ieee.odm.schema.ContingencyAnalysisXmlType;
import org.ieee.odm.schema.DStabSimulationXmlType;
import org.ieee.odm.schema.DclfBranchSensitivityXmlType;
import org.ieee.odm.schema.DclfSenAnalysisXmlType;
import org.ieee.odm.schema.FlowInterfaceRefXmlType;
import org.ieee.odm.schema.GridComputingXmlType;
import org.ieee.odm.schema.InterfaceShiftFactorXmlType;
import org.ieee.odm.schema.IpssAnalysisCaseXmlType;
import org.ieee.odm.schema.IpssStudyScenarioXmlType;
import org.ieee.odm.schema.LODFMonitorBranchXmlType;
import org.ieee.odm.schema.LODFMonitorInterfaceXmlType;
import org.ieee.odm.schema.LODFOutageEnumType;
import org.ieee.odm.schema.LineOutageDFactorXmlType;
import org.ieee.odm.schema.PTradingEDHourlyAnalysisXmlType;
import org.ieee.odm.schema.PowerTradingInfoXmlType;
import org.ieee.odm.schema.SenAnalysisBusXmlType;
import org.ieee.odm.schema.SenAnalysisOutOptionXmlType;

/**
 * InterPSS extension study scenario helper class
 * 
 * @author mzhou
 *
 */
public class IpssScenarioHelper {
	private IODMModelParser parser = null;
	
	/**
	 * constructor
	 * 
	 * @param parser
	 */
	public IpssScenarioHelper () {
		this.parser = new AclfModelParser();
	}

	/**
	 * constructor
	 * 
	 * @param parser
	 */
	public IpssScenarioHelper (IODMModelParser parser) {
		this.parser = parser;
	}

	/**
	 * constructor
	 * 
	 * @param sce an existing study scenario xml doc
	 */
	public IpssScenarioHelper (IpssStudyScenarioXmlType sce) {
		// parser is a place holder for scenario object, therefore it does not matter its type
		this.parser = new AclfModelParser();
		this.parser.getStudyCase().setStudyScenario(OdmObjFactory.createIpssStudyScenario(sce));
	}
	
	/**
	 * constructor
	 * 
	 * @param sce an existing study scenario xml doc
	 */
	public IpssScenarioHelper (String filename) throws FileNotFoundException {
		File file = new File(filename);
		this.parser = new ODMModelParser();
		parser.parse(new FileInputStream(file));	
	}
	
	
	/*
	 *             Analysis case level functions
	 *             =============================
	 */
	
	/**
	 * get the current analysis case id
	 * 
	 * @return
	 */
	public String getCurAnalysisCaseId() {
		if (this.getIpssScenario().getAnalysisCaseList().getCurAnalysisCaseId() != null)
			return this.getIpssScenario().getAnalysisCaseList().getCurAnalysisCaseId();
		else
			return null;
	}
	
	/**
	 * set the current analysis case id
	 * 
	 * @param id
	 */
	public void setCurAnalysisCaseId(String id) {
		this.getIpssScenario().getAnalysisCaseList().setCurAnalysisCaseId(id);
	}

	/**
	 * get the current analysis case. If the current analysis case id is not
	 * defined, return the first analysis case
	 * 
	 * @return
	 */
	public IpssAnalysisCaseXmlType getCurrentAnalysisCase() {
		return this.getAnalysisCase(getCurAnalysisCaseId());
	}

	/**
	 * get analysis case by analysis case id
	 * 
	 * @param id
	 * @return
	 */
	private IpssAnalysisCaseXmlType getAnalysisCase(String id) {
		if (id != null) {
			for ( IpssAnalysisCaseXmlType scase : this.getIpssScenario().getAnalysisCaseList().getAnalysisCase()) {
				if (scase.getId().equals(id))
					return scase;
			}
			ODMLogger.getLogger().info("Analysis case not found, id: " + id);
		}
		return this.getIpssScenario().getAnalysisCaseList().getAnalysisCase().get(0);
	}	
	
	/**
	 * add a new analysis case
	 * 
	 * @return
	 */
	public IpssAnalysisCaseXmlType addNewAnalysisCase() {
		IpssAnalysisCaseXmlType scase = OdmObjFactory.createIpssAnalysisCaseXmlType();
		List<IpssAnalysisCaseXmlType> list = this.getIpssScenario().getAnalysisCaseList().getAnalysisCase();
		list.add(scase);
		scase.setId("StudyCaseId-" + list.size());
		return scase;
	}
	
	/**
	 * delete the Analysis case identified by the studyCase id.
	 * 
	 *   - If the scenario only contains one Analysis case, it cannot be deleted
	 *   - The method returns the next Analysis case id. If not next, it returns the first
	 * 
	 * @param studyCaseId
	 * @return
	 */
	public String deleteAnalysisCase(String studyCaseId) {
		List<IpssAnalysisCaseXmlType> list = this.getIpssScenario().getAnalysisCaseList().getAnalysisCase();
		// if there is only one analysis case, do nothing
		if (list.size() == 1) {
			ODMLogger.getLogger().info("number of case = 1, cannot delete the case");
			return studyCaseId;
		}
		
		int cnt = 0;
		for ( IpssAnalysisCaseXmlType scase : list) {
			if (scase.getId().equals(studyCaseId)) {
				//list.remove(cnt);
				break;
			}
			else
				cnt++;
		}
		
		list.remove(cnt);
		if (cnt >= list.size())
			cnt = 0;
		String id = list.get(cnt).getId();
		ODMLogger.getLogger().info("Delete analysis case id: " + studyCaseId + " returning next  analysis case id: " + id);
		return id;
	}
	
	/**
	 * get Analysis case id array
	 * 
	 * @return
	 */
	public String[] getAnalysisCaseIdAry() {
		String[] sAry = new String[this.getIpssScenario().getAnalysisCaseList().getAnalysisCase().size()];
		int cnt = 0;
		for ( IpssAnalysisCaseXmlType scase : this.getIpssScenario().getAnalysisCaseList().getAnalysisCase()) {
			sAry[cnt++] = scase.getId();
		}
		return sAry;
	}

	/*
	 *             Grid functions
	 *             ==============
	 */

	/**
	 * get the grid run option
	 */
	public GridComputingXmlType getGridRunOption() {
		if (this.getIpssScenario().getGridRunOption() == null) {
			this.getIpssScenario().setGridRunOption(OdmObjFactory.createGridComputingXmlType());
		}
		return this.getIpssScenario().getGridRunOption();
	}

	/*
	 *             PowerTradingInfo functions
	 *             ==========================
	 */

	/**
	 * get the power trading into object
	 */
	public PowerTradingInfoXmlType getPowerTradingInfo() {
		if (this.getIpssScenario().getPowerTradingInfo() == null) {
			this.getIpssScenario().setPowerTradingInfo(
					IpssAnalysisCaseFunc.createDefaultPtInfo());
		}
		return this.getIpssScenario().getPowerTradingInfo();
	}

	/**
	 * get the AggregatePricing object
	 * 
	 * @return
	 */
	public AggregateGenXmlType getAggregateGroup() {
		if (this.getPowerTradingInfo().getLoadDist().getAggregateGen() == null) {
			this.getPowerTradingInfo().getLoadDist().setAggregateGen(OdmObjFactory.createAggregateGenXmlType());
		}
		return this.getPowerTradingInfo().getLoadDist().getAggregateGen();
	}
	
	/*
	 *             Aclf functions
	 *             ==============
	 */
	
	/**
	 * get AclfAnalysis of the first Aclf analysis case. If it does not
	 * exist, create a new Aclf analysis case. 
	 */
	public AclfAnalysisXmlType getAclfAnalysis() {
		return getAclfAnalysis(null);
	}

	/**
	 * get AclfAnalysis of the analysis case of analysisCaseId 
	 */
	public AclfAnalysisXmlType getAclfAnalysis(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getAclfAnalysis() == null) {
			scase.setAclfAnalysis(OdmObjFactory.createAclfAnalysisXmlType());
		}
		return scase.getAclfAnalysis();
	}

 	/*
	 *             Contingency analysis functions
	 *             ==============================
	 */
	
	/**
	 * get ContingencyAnalysis of the current analysis case 
	 */
	public ContingencyAnalysisXmlType getContingencyAnalysis() {
		return getContingencyAnalysis(null);
	}

	/**
	 * get ContingencyAnalysis of the analysis case of analysisCaseId 
	 */
	public ContingencyAnalysisXmlType getContingencyAnalysis(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getContingencyAnalysis() == null) {
			scase.setContingencyAnalysis(OdmObjFactory.createContingencyAnalysisXmlType());
		}
		return scase.getContingencyAnalysis();
	}	

	/*
	 *             Acsc functions
	 *             ==============
	 */

	/**
	 * get AcscFaultAnalysis of the current analysis case 
	 */
	public AcscFaultAnalysisXmlType getAcscFaultAnalysis() {
		return getAcscFaultAnalysis(null);
	}
	
	/**
	 * get AcscFaultAnalysis of the analysis case of analysisCaseId 
	 */
	public AcscFaultAnalysisXmlType getAcscFaultAnalysis(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getAcscAnalysis() == null) {
			scase.setAcscAnalysis(OdmObjFactory.createAcscFaultAnalysisXmlType());
		}
		return scase.getAcscAnalysis();
	}

	/**
	 * create a AcscBusFaultXmlType object
	 * 
	 * @return
	 */
	public AcscBusFaultXmlType createAcscBusFault() {
		AcscBusFaultXmlType busFault = OdmObjFactory.createAcscBusFaultXmlType();
		busFault.setRefBus(OdmObjFactory.createBusRefXmlType());
		busFault.setZLG(OdmObjFactory.createZXmlType());
		busFault.setZLL(OdmObjFactory.createZXmlType());
		return busFault;
	}
	
	/**
	 * create a AcscBranchFaultXmlType object
	 * 
	 * @return
	 */
	public AcscBranchFaultXmlType createAcscBranchFault() {
		AcscBranchFaultXmlType braFault = OdmObjFactory.createAcscBranchFaultXmlType();
		braFault.setRefBranch(OdmObjFactory.createBranchRefXmlType());
		braFault.setZLG(OdmObjFactory.createZXmlType());
		braFault.setZLL(OdmObjFactory.createZXmlType());
		braFault.setReclosureTime(OdmObjFactory.createTimePeriodXmlType());
		return braFault;
	}

	/*
	 *             DStab functions
	 *             ===============
	 */

	/**
	 * get DStabSimulation of the current analysis case 
	 */
	public DStabSimulationXmlType getDStabSimulation() {
		return getDStabSimulation(null);
	}
	
	/**
	 * get DStabSimulation of the analysis case of analysisCaseId 
	 */
	public DStabSimulationXmlType getDStabSimulation(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getDStabAnalysis() == null) {
			scase.setDStabAnalysis(OdmObjFactory.createDStabSimulationXmlType());
		}
		return scase.getDStabAnalysis();
	}
	
	/*
	 *             Sen Analysis functions
	 *             ======================
	 */
	
	/**
	 * get the SenAnalysisList of the current analysis case
	 * 
	 */
	public List<DclfSenAnalysisXmlType> getSenAnalysisList() {
		return getSenAnalysisList(null);
	}

	/**
	 * get the SenAnalysisList of the analysis case of analysisCaseId
	 * 
	 */
	public List<DclfSenAnalysisXmlType> getSenAnalysisList(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getSenAnalysis() == null) {
			ODMLogger.getLogger().severe("contact support@interpss.org");
		}
		return scase.getSenAnalysis();
	}
	
	/**
	 * create a SenAnalysis object for the current analysis case
	 * 
	 * @return
	 */
	public DclfSenAnalysisXmlType createSenAnalysis() {
		return createSenAnalysis(null);
	}
	
	/**
	 * create a SenAnalysis object for the analysis case of analysisCaseId
	 * 
	 * @return
	 */
	public DclfSenAnalysisXmlType createSenAnalysis(String anaCaseId) {
		DclfSenAnalysisXmlType dclfCase = OdmObjFactory.createDclfSenAnalysisXmlType();
		getSenAnalysisList(anaCaseId).add(dclfCase);
		return dclfCase;
	}

	/**
	 * create a GSF object and add to the dclfCase
	 * 
	 * @param dclfCase
	 * @return
	 */
	public static DclfBranchSensitivityXmlType createGSF(DclfSenAnalysisXmlType dclfCase) {
		DclfBranchSensitivityXmlType gsf = OdmObjFactory.createDclfBranchSensitivityXmlType();
		dclfCase.getGenShiftFactor().add(gsf);
		return gsf;
	}

	/**
	 * create a LODF object and add to the dclfCase
	 * 
	 * @param dclfCase
	 * @return
	 */
	public static LineOutageDFactorXmlType createLODF(DclfSenAnalysisXmlType dclfCase) {
		LineOutageDFactorXmlType lodf = OdmObjFactory.createLineOutageDFactorXmlType();
		lodf.setOutageType(LODFOutageEnumType.SINGLE_BRANCH);
		dclfCase.getLineOutageDFactor().add(lodf);
		return lodf;
	}
	
	/**
	 * create SenAnalysisOutOption object and add to the dclfCase
	 * 
	 * @param dclfCase
	 * @return
	 */
	public static SenAnalysisOutOptionXmlType createSenAnalysisOutConfig(DclfSenAnalysisXmlType dclfCase) {
		SenAnalysisOutOptionXmlType out = OdmObjFactory.createSenAnalysisOutOptionXmlType();
		dclfCase.setOutOption(out);
		return out;
	}	

	/**
	 * create MonitorBranch object and add to the branch list
	 * 
	 * @param braList
	 * @return
	 */	
	public static BranchRefXmlType createMonitorBranch(List<LODFMonitorBranchXmlType> braList) {
		LODFMonitorBranchXmlType bra = OdmObjFactory.createLODFMonitorBranchXmlType();
		braList.add(bra);
		bra.setBranch(OdmObjFactory.createBranchRefXmlType());
		return bra.getBranch();
	}

	/**
	 * create MonitorInterface object and add to the interface list
	 * 
	 * @param intfList
	 * @return
	 */	
	public static FlowInterfaceRefXmlType createMonitorInterface(List<LODFMonitorInterfaceXmlType> intfList) {
		LODFMonitorInterfaceXmlType intf = OdmObjFactory.createLODFMonitorInterfaceXmlType();
		intfList.add(intf);
		intf.setInterface(OdmObjFactory.createFlowInterfaceRefXmlType());
		return intf.getInterface();
	}

	/**
	 * create SenAnalysisBus object and add to the bus list
	 * 
	 * @param busList
	 * @return
	 */
	public static SenAnalysisBusXmlType createSenAnalysisBus(List<SenAnalysisBusXmlType> busList) {
		SenAnalysisBusXmlType bus = OdmObjFactory.createSenAnalysisBusXmlType();
		bus.setAllocFactor(OdmObjFactory.createFactorXmlType());
		busList.add(bus);
		return bus;
	}

	/**
	 * create BranchSFactor object and add to the branch list
	 * 
	 * @param braList
	 * @return
	 */	
	public static BranchShiftFactorXmlType createBranchSFactor(List<BranchShiftFactorXmlType> braList) {
		BranchShiftFactorXmlType sf = OdmObjFactory.createBranchShiftFactorXmlType();
		braList.add(sf);
		return sf;
	}
	
	/**
	 * create InterfaceShiftFactor object and add to the interface list
	 * 
	 * @param braList
	 * @return
	 */	
	public static InterfaceShiftFactorXmlType createInterfaceSFactor(List<InterfaceShiftFactorXmlType> infList) {
		InterfaceShiftFactorXmlType sf = OdmObjFactory.createInterfaceShiftFactorXmlType();
		infList.add(sf);
		return sf;
	}
	
/*
 *             PTradingAnalysis functions
 *             ==========================
 */
	
	/**
	 * return the default PTradingEDHourlyAnalysis of the current analysis case
	 * 
	 */
	public PTradingEDHourlyAnalysisXmlType getPtEDHourlyAnalysis() {
		return getPtEDHourlyAnalysis(null);
	}
	
	/**
	 * return PTradingEDHourlyAnalysis of the analysis case of analysisCaseId
	 *  
	 * @return
	 */
	public PTradingEDHourlyAnalysisXmlType getPtEDHourlyAnalysis(String anaCaseId) {
		IpssAnalysisCaseXmlType scase = getAnalysisCase(anaCaseId); 
		if (scase.getPtAnalysis() == null) {
			PTradingEDHourlyAnalysisXmlType pt = OdmObjFactory.createPTradingEDHourlyAnalysisXmlType();
			scase.setPtAnalysis(OdmObjFactory.createPtAnalysis(pt));
		}
		return (PTradingEDHourlyAnalysisXmlType)scase.getPtAnalysis().getValue();
	}

	/**
	 * get PTrading case name array
	 * 
	 * @return
	 */
	public List<String> getPTradingCaseNameAry() {
		List<String> list = new ArrayList<String>();
		for ( IpssAnalysisCaseXmlType scase : this.getIpssScenario().getAnalysisCaseList().getAnalysisCase()) {
			if (scase.getPtAnalysis() != null) {
				list.add(scase.getPtAnalysis().getValue().getName());
			}
		}
		return list;
	}

	/**
	 * get analysis case id by PTrading case name
	 * 
	 * @return
	 */
	public String getStudyCaseIdByPtCaseName(String ptCaseName) {
		for ( IpssAnalysisCaseXmlType scase : this.getIpssScenario().getAnalysisCaseList().getAnalysisCase()) {
			if (scase.getPtAnalysis() != null) {
				if (ptCaseName.equals(scase.getPtAnalysis().getValue().getName()))
					return scase.getId();
			}
		}
		return null;
	}

	/**
	 * create a new analysis case and init PTradingAnalysis 
	 * 
	 * @param ptName
	 * @return
	 */
	public IpssAnalysisCaseXmlType addNewPTradingStudyCase(String ptName) {
		IpssAnalysisCaseXmlType scase = this.addNewAnalysisCase();
		PTradingEDHourlyAnalysisXmlType ptCase = OdmObjFactory.createPTradingEDHourlyAnalysisXmlType();
		scase.setPtAnalysis(OdmObjFactory.createPtAnalysis(ptCase));
		ptCase.setName(ptName);
		ptCase.setDesc("Analysis Case description");
		initPTradingEDHourlyAnalysis(ptCase);
		return scase;
	}
	
	private IpssStudyScenarioXmlType getIpssScenario() {
		if (parser.getStudyScenario() == null) {
			IpssStudyScenarioXmlType studyScenario = OdmObjFactory.createIpssStudyScenarioXmlType();
			studyScenario.setAnalysisCaseList(OdmObjFactory.createIpssStudyScenarioXmlTypeAnalysisCaseList());
			parser.getStudyCase().setStudyScenario(OdmObjFactory.createIpssStudyScenario(studyScenario));
			
			IpssAnalysisCaseXmlType scenario = OdmObjFactory.createIpssAnalysisCaseXmlType();
			studyScenario.getAnalysisCaseList().getAnalysisCase().add(scenario);
		}
		return (IpssStudyScenarioXmlType)parser.getStudyScenario();
	}
}
