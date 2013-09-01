package org.ieee.odm.adapter.psse.mapper.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;



public class DynamicModelLibHelper {
	
	public enum DynModelType{GENERATOR,EXCITER,TUR_GOV,STABILIZER,FACTS,HVDC}
	
	//TODO This model list should be extended or updated along with the progress of implementation 
	private List<String> GeneratorList =new ArrayList<>(
			Arrays.asList("GENROU","GENROE","GENSAL","GENSAE","GENCLS"));
	
	private List<String> ExciterList =new ArrayList<>(
			Arrays.asList("IEEET1","IEEET2","IEEET3","IEEET4"));
	
	private List<String> TurGovList =new ArrayList<>(
			Arrays.asList("IEESGO","IEEEG1","IEEEG3","TGOV1"));
	private List<String> PssList =new ArrayList<>();
	private List<String> FactsList =new ArrayList<>();
	private List<String> HvdcList =new ArrayList<>();
	
	public DynamicModelLibHelper() {
		
	}
	
	public DynModelType getModelType(String typeStr) throws UnsupportedDataTypeException{
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
			throw new UnsupportedDataTypeException("The input model data type is not found in the dynamic lib collection, Type #"+typeStr);
		}
		
	}

}
