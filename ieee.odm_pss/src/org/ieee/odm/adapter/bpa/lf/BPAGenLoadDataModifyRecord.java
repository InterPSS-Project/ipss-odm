package org.ieee.odm.adapter.bpa.lf;

import javax.xml.bind.JAXBElement;

import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.aclf.AclfParserHelper;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.model.base.ODMModelStringUtil;
import org.ieee.odm.schema.ApparentPowerUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.LoadflowBusXmlType;
import org.ieee.odm.schema.LoadflowGenDataXmlType;
import org.ieee.odm.schema.LoadflowLoadDataXmlType;
import org.ieee.odm.schema.NetworkXmlType;

public class BPAGenLoadDataModifyRecord {
	/*
	 * only AC Bus will be considered for generation and/or load data modification.
	 */
	private static final String Modify_Gen_n_Load_ALL="PA";        //PA
	private static final String Modify_Gen_n_Load_BY_ZONE="PZ";    //PZ
	private static final String Modify_Gen_n_Load_BY_OWNER="PO";   //PO
	// for the following, NOT implemented yet
	//TODO
	private static final String Modify_ConstZILoad_BY_ZONE="PC";   //PC
	private static final String Modify_ConstZILoad_BY_OWNER="PB";   //PB
	public static long ModifyCardNO;
	public void processGenLoadModificationData(final String str, BaseAclfModelParser<? extends NetworkXmlType> parser)
	throws Exception {
		final String[] strAry =getModificationData(str);
		double loadP_Factor=1;
		double loadQ_Factor=1;
		double genP_Factor=1;
		double genQ_Factor=1;
		if(!strAry[2].equals(""))
			loadP_Factor= new Double(strAry[2]).doubleValue();
		if(!strAry[3].equals(""))
			loadQ_Factor= new Double(strAry[3]).doubleValue();
		if(!strAry[4].equals(""))
			genP_Factor= new Double(strAry[4]).doubleValue();
		if(!strAry[5].equals(""))
			genQ_Factor= new Double(strAry[5]).doubleValue();
		
		if(strAry[0].equals(Modify_Gen_n_Load_ALL)){
			
			for(JAXBElement<? extends BusXmlType> b : parser.getNet().getBusList().getBus()){
				LoadflowBusXmlType bus=(LoadflowBusXmlType) b.getValue();
				modifyLoadData(bus,loadP_Factor,loadQ_Factor);
				modifyGenData(bus,genP_Factor,genQ_Factor);
			}
		}
		else if(strAry[0].equals(Modify_Gen_n_Load_BY_ZONE)){
			
			for(JAXBElement<? extends BusXmlType> b : parser.getNet().getBusList().getBus()){
				LoadflowBusXmlType bus=(LoadflowBusXmlType) b.getValue();
				if(bus.getZoneName().equals(strAry[1])){
					if(strAry[6].length()>1){
						if(bus.getDesc().equals(strAry[6])){
							ModifyCardNO++;
							//System.out.println(bus.getId());
							modifyLoadData(bus, loadP_Factor, loadQ_Factor);
							modifyGenData(bus, genP_Factor, genQ_Factor);
						}
					}else{
						ModifyCardNO++;
						//System.out.println(bus.getId());
						modifyLoadData(bus, loadP_Factor, loadQ_Factor);
						modifyGenData(bus, genP_Factor, genQ_Factor);
					}
				}
			}
		}
		else if (strAry[0].equals(Modify_Gen_n_Load_BY_OWNER)){
			for(JAXBElement<? extends BusXmlType> b : parser.getNet().getBusList().getBus()){
				LoadflowBusXmlType bus=(LoadflowBusXmlType) b.getValue();
				if(bus.getZoneName().equals(strAry[1])){
					modifyLoadData(bus,loadP_Factor,loadQ_Factor);
					modifyGenData(bus,genP_Factor,genQ_Factor);
				}
			}
		}
		else ODMLogger.getLogger().severe("The input modification type #"+strAry[0]+" is not supported yet.");

	}
	private static String[] getModificationData(final String str){
		final String[] strAry = new String[20];
		try{
			// type
			strAry[0] = ODMModelStringUtil.getStringReturnEmptyString(str,1, 2);
			//zone name
			if(strAry[0].equals(Modify_Gen_n_Load_BY_ZONE)||strAry[0].equals(Modify_ConstZILoad_BY_ZONE))
               strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 5).trim();
			// ower name
			else if(strAry[0].equals(Modify_Gen_n_Load_BY_OWNER)||strAry[0].equals(Modify_ConstZILoad_BY_OWNER))
			  strAry[1] = ODMModelStringUtil.getStringReturnEmptyString(str,4, 6).trim();
			// LoadP and LoadQ modification factor
			strAry[2] = ODMModelStringUtil.getStringReturnEmptyString(str,10, 14).trim();
			strAry[3] = ODMModelStringUtil.getStringReturnEmptyString(str,16, 20).trim();
			// GenP and GenQ modification factor
			strAry[4] = ODMModelStringUtil.getStringReturnEmptyString(str,22, 26).trim();
			strAry[5] = ODMModelStringUtil.getStringReturnEmptyString(str,28, 32).trim();
			// for the rest optional setting, not implementation yet
			//TODO
			strAry[6] = ODMModelStringUtil.getStringReturnEmptyString(str,35, 37).trim();
		}catch(Exception e){
			ODMLogger.getLogger().severe(e.toString() + "\n" + str);
			e.printStackTrace();
		}
		return strAry;
		
	
		
	}
	private static boolean modifyGenData(LoadflowBusXmlType bus, double genPFactor,double genQFactor){
		LoadflowGenDataXmlType defaultGen = AclfParserHelper.getDefaultGen(bus.getGenData());
		try{
			if(bus.getGenData()!=null&&defaultGen!=null
					&&defaultGen.getPower()!=null){
				double genP=defaultGen.getPower().getRe();
				double genQ=defaultGen.getPower().getIm();
				if(genP!=0.0)genP*=genPFactor;
				if(genQ!=0.0)genQ*=genQFactor;
				if(genP!=0.0||genQ!=0){
					  genP=ODMModelStringUtil.getNumberFormat(genP);
					  genQ=ODMModelStringUtil.getNumberFormat(genQ);
					  defaultGen.setPower(BaseDataSetter.createPowerValue(
						genP,genQ,ApparentPowerUnitType.MVA));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;	
		
	}
	private static boolean modifyLoadData(LoadflowBusXmlType bus, double loadPFactor,double loadQFactor){
		LoadflowLoadDataXmlType defaultLoad = AclfParserHelper.getDefaultLoad(bus.getLoadData());
		try{
			if(bus.getLoadData()!=null&&defaultLoad!=null
					&&defaultLoad.getConstPLoad()!=null){
				  double loadP=defaultLoad.getConstPLoad().getRe();
				  double loadQ=defaultLoad.getConstPLoad().getIm();
				  if(loadP!=0.0)loadP*=loadPFactor;
				  if(loadQ!=0.0)loadQ*=loadQFactor;
				  if(loadP!=0.0||loadQ!=0.0){
					  loadP=ODMModelStringUtil.getNumberFormat(loadP);
					  loadQ=ODMModelStringUtil.getNumberFormat(loadQ);
					  defaultLoad.setConstPLoad(BaseDataSetter.createPowerValue(
							loadP,loadQ,ApparentPowerUnitType.MVA));
				  }

				 }
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
