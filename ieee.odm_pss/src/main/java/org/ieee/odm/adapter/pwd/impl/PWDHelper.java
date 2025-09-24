package org.ieee.odm.adapter.pwd.impl;

import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_3WXFormer;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Area;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Branch;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Bus;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_CaseInfo;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Gen;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_LIMITSET;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Load;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Shunt;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_XFCORRECTION;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_XFormer;
import static org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.Token_Zone;

import org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.RecType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PWDHelper is defined to hold some common method used in the data processing.
 * For example, data completeness checking and get record data type
 * @author 
 *
 */

public class PWDHelper {
	// Add a logger instance
	private static final Logger log = LoggerFactory.getLogger(PWDHelper.class);
	
	/**
	 * Check whether the Argument Fields is completed yet, since it is
	 * possible that the Argument Fields are defined in multiple lines. 
	 * @param str
	 * @return
	 */
    public static boolean isArgumentFieldsCompleted(String str){
		
		boolean leftParenthesis=false;
		boolean rightParenthesis=false;

		if(str.indexOf("(")>-1)leftParenthesis=true;
		
        rightParenthesis=endsWithRightParenthesis(str);
        
		return leftParenthesis&&rightParenthesis;
		
	}
	
	private static boolean endsWithRightParenthesis(String str){
		return str.trim().endsWith(")");
	}
	/**
	 * Get the record data type
	 * @param str input record string
	 * @return record data type string
	 */
	public static RecType getDataType(String str){
		int indexOfLeftParenthesis=str.indexOf("(");
		int indexOfFirstComma=str.indexOf(",");
		String dataType=str.substring(indexOfLeftParenthesis+1, indexOfFirstComma).trim();
		
		RecType recordType=null;

		if(dataType.equals(Token_Bus)){
	    	recordType=RecType.BUS;		
		}
	    else if(dataType.equals(Token_Load)){
	    	recordType=RecType.LOAD;		
		} 
	    else if(dataType.equals(Token_Gen)){
	  		recordType=RecType.GEN;		
	    }
	    else if(dataType.equals(Token_Shunt)){
	  		recordType=RecType.SHUNT;		
	    }
	    else if(dataType.equals(Token_Branch)){
	  		recordType=RecType.BRANCH;		
	    }
	    else if(dataType.equals(Token_XFormer)){
	  		recordType=RecType.XFORMER;		
	    }
	    else if(dataType.equals(Token_3WXFormer)){
	  		recordType=RecType.TRI_W_XFORMER;		
	    }
	    else if(dataType.equals(Token_Area)){
	  		recordType=RecType.AREA;		
	    }
	    else if(dataType.equals(Token_Zone)){
	  		recordType=RecType.ZONE;
	    }
	    else if(dataType.equals(Token_CaseInfo)){
	  		recordType=RecType.CASE_INFO;
	    }
	    else if(dataType.equals(Token_XFCORRECTION)){
	  		recordType=RecType.XFCORRECTION;
	    }
	    else if(dataType.equals(Token_LIMITSET)){
	    	recordType=RecType.LIMITSET;
	    }
	    else {
	    	recordType=RecType.Undefined;
	    	log.info("Undifined data type:"+dataType);
	    }		
		
		return recordType;
	}
}
