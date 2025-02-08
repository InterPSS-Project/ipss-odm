package org.ieee.odm.adapter.common.obj;

public class BaseObjDataMapper {
	
	protected BaseInputRowObjParser dataParser = null;
	
	public BaseObjDataMapper(){
		
	}
	
	public BaseObjDataMapper(BaseInputRowObjParser parser){
		this.dataParser = parser;
	}

}
