package org.ieee.odm.adapter;

public class BaseStringDataMapper {
	
	protected AbstractStringDataFieldParser dataParser = null;
	
	public BaseStringDataMapper(){
		
	}
	
	public BaseStringDataMapper(AbstractStringDataFieldParser parser){
		this.dataParser = parser;
	}

}
