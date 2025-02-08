package org.ieee.odm.adapter.common.str;

public class BaseStringDataMapper {
	
	protected AbstractStringDataFieldParser dataParser = null;
	
	public BaseStringDataMapper(){
		
	}
	
	public BaseStringDataMapper(AbstractStringDataFieldParser parser){
		this.dataParser = parser;
	}

}
