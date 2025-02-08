package org.ieee.odm.adapter.common.json;

public class BaseJSonDataMapper {
	
	protected BaseInputRowJSonParser dataParser = null;
	
	public BaseJSonDataMapper(){
		
	}
	
	public BaseJSonDataMapper(BaseInputRowJSonParser parser){
		this.dataParser = parser;
	}

}
