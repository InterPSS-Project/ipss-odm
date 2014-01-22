package org.ieee.odm.adapter.psse.mapper.dynamic;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ieee.odm.common.ODMLogger;



public class DynamicModelLibHelper {
	
	//only supported model data
	StringBuffer supportedDynModelData = new StringBuffer();
	
	//unsupported model data
	StringBuffer unsupportedGen = new StringBuffer();
	StringBuffer unsupportedExc = new StringBuffer();
	StringBuffer unsupportedGov = new StringBuffer();
	StringBuffer unsupportedLoad = new StringBuffer();
	StringBuffer unsupportedSVC = new StringBuffer();
	StringBuffer unsupportedElse = new StringBuffer();
	
	public enum DynModelType{GENERATOR,EXCITER,TUR_GOV,STABILIZER,FACTS,HVDC,VoltComp}
	
	//TODO This model list should be extended or updated along with the progress of implementation 
	private List<String> GeneratorList =new ArrayList<>(
			Arrays.asList("GENROU","GENSAL","GENCLS","GENSAE"));
	private List<String> UnSupportedGeneratorList =new ArrayList<>(
			Arrays.asList("GENROE","GENDCO","GENTRA","CGEN1"));
	
	private List<String> ExciterList =new ArrayList<>(
			//Arrays.asList("IEEET1")
			);
	private List<String> UnSupportedExciterList =new ArrayList<>(
			Arrays.asList("IEEET1","IEEET2","IEEET3","IEEET4", //IEEE 1968
					"ESAC3A","ESAC8B","ESST2A","ESST3A","ESST4B",
					"EXAC1","EXAC1A","EXAC2","EXAC41A","EXDC2","EXELI","EXPIC1","EXST1","EXST2","EXST3",
					"IEEEX1","IEEEX2","IEEEX3","IEEEX4", //IEEE 1979 Type1->4
					"ST5B","ST6B","ST7B", //IEEE2005
					"REXSY1","REXSYS","SCRX"));
	
	private List<String> TurGovList =new ArrayList<>();
	
	private List<String> UnsupportedTurGovList =new ArrayList<>(
			Arrays.asList("IEESGO","IEEEG1","IEEEG3","TGOV1","GGOV1","HYGOV","GAST","HYGOV2","IEEEG2","PIDGOV","WSHYDD","WSHYGP","WSIEG1"));
	
	private List<String> LoadModelList =new ArrayList<>(
			Arrays.asList("IEELBL","IEELAR","CIMTR4","CIMWBL"));
	private List<String> PssList =new ArrayList<>();
	private List<String> FactsList =new ArrayList<>();
	private List<String> HvdcList =new ArrayList<>();
	private List<String> shuntCompensatorList =new ArrayList<>(
			Arrays.asList("CSVGN1","CSVGN2","CSVGN3","CSVGN4","CSVGN5","CSVGN6"));
	private List<String> VoltCompensatorList =new ArrayList<>(
			Arrays.asList("COMP","COMPCC","IEEEVC","REMCMP"));
	
	public DynamicModelLibHelper() {
		
	}
	
	public DynModelType getModelType(String typeStr) {
		if(GeneratorList.contains(typeStr))
			return DynModelType.GENERATOR;
		else if(ExciterList.contains(typeStr))
			return  DynModelType.EXCITER;
		else if(TurGovList.contains(typeStr))
			return DynModelType.TUR_GOV;
		else if(PssList.contains(typeStr))
			return DynModelType.STABILIZER;
		else if(FactsList.contains(typeStr))
			return DynModelType.FACTS;
		else if(HvdcList.contains(typeStr))
			return DynModelType.HVDC;
		else {
			ODMLogger.getLogger().severe("The input model data type is not found in the dynamic lib collection, Type #"+typeStr);
		    return null;
		}
		
	}
	
	public void procUnsupportedModel(String typeStr, String lineStr){
		/*
		 * //  0----------1----------2----
				"IBUS", "Type",   "MachId",
		 */
		String[] strAry=lineStr.split("\\s+");
		String busId=strAry[0];
		String modelId=strAry[2];
		
		if(UnSupportedGeneratorList.contains(typeStr)){
			unsupportedGen.append("GEN,"+typeStr+","+busId+","+modelId+"\n");
		}
		else if(UnSupportedExciterList.contains(typeStr)){
			unsupportedExc.append("Exc,"+typeStr+","+busId+","+modelId+"\n");
		}
		else if(UnsupportedTurGovList.contains(typeStr)){
			unsupportedGov.append("GOV,"+typeStr+","+busId+","+modelId+"\n");
		}
		else if(LoadModelList.contains(typeStr)){
			unsupportedLoad.append("LOAD,"+typeStr+","+busId+","+modelId+"\n");
		}
		else if(shuntCompensatorList.contains(typeStr)){
			unsupportedSVC.append("SVC,"+typeStr+","+busId+","+modelId+"\n");
		}
		
		else 
			unsupportedElse.append(typeStr+","+busId+","+modelId+"\n");
		
	}
	
	public String getUnsupportdGenRecs(){
		return unsupportedGen.toString();
	}
	public String getUnsupportdExciterRecs(){
		return unsupportedExc.toString();
	}
	public String getUnsupportdGovRecs(){
		return unsupportedGov.toString();
	}
	public String getUnsupportdLoadRecs(){
		return unsupportedLoad.toString();
	}
	public String getUnsupportdSVCRecs(){
		return unsupportedSVC.toString();
	}
	
	public void saveSupportedModelData(String lineStr){
		supportedDynModelData.append(lineStr+"/ \n");
		
	}
	
	public String getSupportedModelData(){
		return supportedDynModelData.toString();
	}
	
	public boolean saveSupportedModelDataToFile(String fileName){
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
			out.write(supportedDynModelData.toString().getBytes());
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			ODMLogger.getLogger().severe("Cannot save to file: " + fileName + ", " + e.toString());
		}
		return false;
	}
	

}
