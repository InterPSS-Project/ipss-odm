package org.ieee.odm.adapter.common.json;

/**
 * 
 * 
 */
public class BaseJSonDataMapper {
	// a JSon file row parser
	protected BaseInputRowJSonParser dataParser = null;
	
	/**
	 * Constructor
	 */
	public BaseJSonDataMapper(){
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param parser a JSon file data row parser 
	 */
	public BaseJSonDataMapper(BaseInputRowJSonParser parser){
		this.dataParser = parser;
	}
}
