package org.ieee.odm.adapter;

public class BaseDataMapper {
	
	protected AbstractDataFieldParser dataParser = null;
	
	public BaseDataMapper(){
		
	}
	
	public BaseDataMapper(AbstractDataFieldParser parser){
		this.dataParser = parser;
	}

}
