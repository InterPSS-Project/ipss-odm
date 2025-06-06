package org.ieee.odm.adapter.psse.raw.mapper.dynamic.load;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.raw.mapper.aclf.BasePSSEDataRawMapper;
import org.ieee.odm.adapter.psse.raw.parser.dynamic.load.PSSELoadACMotorDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.dstab.DStabModelParser;
import org.ieee.odm.model.dstab.DStabParserHelper;
import org.ieee.odm.schema.DStabBusXmlType;
import org.ieee.odm.schema.DStabLoadDataXmlType;
import org.ieee.odm.schema.DynamicLoadSinglePhaseACMotorXmlType;
import org.ieee.odm.schema.LoadCharacteristicLocationEnumType;
import org.ieee.odm.schema.LoadCharacteristicTypeEnumType;

public class PSSELoadACMotorMapper extends BasePSSEDataRawMapper{
    
	public PSSELoadACMotorMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSELoadACMotorDataParser(ver);
	}
	
	public void procLineString(String lineStr, DStabModelParser parser) throws ODMException {
		this.dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("IBUS");
	    final String busId = IODMModelParser.BusIdPreFix+i;
	    String loadId = dataParser.getValue("LID");
	    
	    //check model type
	    if(!dataParser.getValue("Type").equals("ACMTBLU1")){
	    	throw new ODMException(" load at bus  : Id"+
		             loadId+" @ Bus"+i+"is not a ACMTBLU1 type");
	    }

	   DStabBusXmlType busXml = parser.getBus(busId);

	   if(busXml!=null){ 
	        DStabLoadDataXmlType dstabloadData = DStabParserHelper.getDStabLoad(busXml, loadId);
	        
	        if(dstabloadData!= null){
	        	
	        	dstabloadData.setLocation(LoadCharacteristicLocationEnumType.AT_BUS);
	        	dstabloadData.setLoadXmlType(LoadCharacteristicTypeEnumType.SINGLE_PHASE_AC_MOTOR);
	        	
//	        	DynamicLoadModelSelectionXmlType loadModel = dstabloadData.getLoadModel();
//	        	if(loadModel ==null){
//	        		loadModel = new DynamicLoadModelSelectionXmlType();
//	        		dstabloadData.setLoadModel(loadModel);
//	        	}
	        	
	        	DynamicLoadSinglePhaseACMotorXmlType  acMotorXml = DStabParserHelper.createDStabLoadACMotor(dstabloadData);
	        	
	        	double CompLF  = dataParser.getDouble("CompLF");
    			double CompPF  = dataParser.getDouble("CompPF");
    			double Rstall  = dataParser.getDouble("Rstall");
    			double Xstall  = dataParser.getDouble("Xstall");
    			double Vstall  = dataParser.getDouble("Vstall");
    			double Tstall  = dataParser.getDouble("Tstall");
    			double LFadj   = dataParser.getDouble("LFadj");
    			double Kp1     = dataParser.getDouble("Kp1");   
    			double Np1     = dataParser.getDouble("Np1");   
    			double Kq1     = dataParser.getDouble("Kq1");   
    			double Nq1     = dataParser.getDouble("Nq1");   
    			double Kp2     = dataParser.getDouble("Kp2");   
    			double Np2     = dataParser.getDouble("Np2");   
    			double Kq2     = dataParser.getDouble("Kq2");   
    			double Nq2     = dataParser.getDouble("Nq2");   
    			double CmpKpf  = dataParser.getDouble("CmpKpf");
    			double CmpKqf  = dataParser.getDouble("CmpKqf");
    			double Vbrk    = dataParser.getDouble("Vbrk");  
    			double Frst    = dataParser.getDouble("Frst");  
    			double Vrst    = dataParser.getDouble("Vrst");  
    			double Trst    = dataParser.getDouble("Trestart");  
    			double Fuvr    = dataParser.getDouble("Fuvr");  
    			double Vtr1    = dataParser.getDouble("UVtr1");  
    			double Ttr1    = dataParser.getDouble("Ttr1");  
    			double Vtr2    = dataParser.getDouble("UVtr2");  
    			double Ttr2    = dataParser.getDouble("Ttr2");  
    			double Vc1off  = dataParser.getDouble("Vc1off");
    			double Vc2off  = dataParser.getDouble("Vc2off");
    			double Vc1on   = dataParser.getDouble("Vc1on"); 
    			double Vc2on   = dataParser.getDouble("Vc2on"); 
    			double Tth     = dataParser.getDouble("Tth");   
    			double Th1t    = dataParser.getDouble("Th1t");  
    			double Th2t    = dataParser.getDouble("Th2t");  
    			double Tv      = dataParser.getDouble("Tv");   
    			double Tf      = dataParser.getDouble("Tf");

    			
    			
				acMotorXml.setCompPF( CompPF);
				
				if (CompLF<=0.0) CompLF = 1.0;
				acMotorXml.setCompLF( CompLF);
				
				
	        	acMotorXml.setVstall( Vstall);
	        	acMotorXml.setTstall( Tstall);
	        	
	        	acMotorXml.setRstall( Rstall);
	        	acMotorXml.setXstall( Xstall);
	        	

	        	acMotorXml.setFrst( Frst);
	        	acMotorXml.setVrst( Vrst);
	        	acMotorXml.setTrst( Trst);
	        	
	        	
	        	acMotorXml.setFuvr(Fuvr);
	        	acMotorXml.setVtr1(Vtr1);
	        	acMotorXml.setTtr1(Ttr1);
	        	
	        	acMotorXml.setVtr2(Vtr2);
	        	acMotorXml.setTtr2(Ttr2);
	        	
	        	acMotorXml.setVc1Off(Vc1off);
	        	acMotorXml.setVc2Off(Vc2off);
	        	
	        	acMotorXml.setVc1On(Vc1on);
	        	acMotorXml.setVc2On(Vc2on);
	        	
	        	acMotorXml.setTth(Tth);
	        	acMotorXml.setTh1T(Th1t);
	        	acMotorXml.setTh2T(Th2t);
	        	acMotorXml.setTv(Tv);
	        	acMotorXml.setTf(Tf);
	        	
	        	/*
	        	 * double LFadj   = dataParser.getDouble("LFadj");
    			double Kp1     = dataParser.getDouble("Kp1");   
    			double Np1     = dataParser.getDouble("Np1");   
    			double Kq1     = dataParser.getDouble("Kq1");   
    			double Nq1     = dataParser.getDouble("Nq1");   
    			double Kp2     = dataParser.getDouble("Kp2");   
    			double Np2     = dataParser.getDouble("Np2");   
    			double Kq2     = dataParser.getDouble("Kq2");   
    			double Nq2     = dataParser.getDouble("Nq2");   
    			double CmpKpf  = dataParser.getDouble("CmpKpf");
    			double CmpKqf  = dataParser.getDouble("CmpKqf");
    			double Vbrk    = dataParser.getDouble("Vbrk"); 
	        	 */
	        	acMotorXml.setLFadj(LFadj);
	        	acMotorXml.setKp1(Kp1);
	        	acMotorXml.setNp1(Np1);
	        	
	        	acMotorXml.setKq1(Kq1);
	        	acMotorXml.setNq1(Nq1);
	        	
	        	acMotorXml.setKp2(Kp2);
	        	acMotorXml.setNp2(Np2);
	        	
	        	acMotorXml.setKq2(Kq2);
	        	acMotorXml.setNq2(Nq2);
	        	
	        	acMotorXml.setCmpKpf(CmpKpf);
	        	acMotorXml.setCmpKqf(CmpKqf);
	        	acMotorXml.setVbrk(Vbrk);
	        	

	        }
	   }
		
	}

}
