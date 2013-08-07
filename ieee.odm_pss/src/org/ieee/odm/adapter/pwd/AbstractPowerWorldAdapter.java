package org.ieee.odm.adapter.pwd;


import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.IFileReader;
import org.ieee.odm.adapter.pwd.impl.PWDHelper;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;

 /**
  * Abstract PowerWorld Adapter implementation
  * 
  * @version 0.2  01/08/2012
  * @author  
  * 
  */
public abstract class AbstractPowerWorldAdapter extends AbstractODMAdapter {
	public  static final String Token_Data="DATA";
	public  static final String Token_Bus="BUS";
	public  static final String Token_Load="LOAD";
	public  static final String Token_Gen="GEN";
	public  static final String Token_Shunt="SHUNT";
	public  static final String Token_Branch="BRANCH";
	public  static final String Token_XFormer="TRANSFORMER";
	public  static final String Token_3WXFormer="3WXFORMER";
	public  static final String Token_Area="AREA";
	public  static final String Token_Zone="ZONE";
	public  static final String Token_CaseInfo="PWCASEINFORMATION";//PWCASEINFORMATION
	public  static final String Token_XFCORRECTION="XFCORRECTION";
	public  static final String Token_LIMITSET="LIMITSET";
	//Define the record data type
	public static enum RecType{BUS,LOAD,GEN,SHUNT,BRANCH,XFORMER,TRI_W_XFORMER,AREA,ZONE,CASE_INFO,XFCORRECTION,LIMITSET,Undefined};
	
	/**
	 * process the input file line-by-line
	 * 
	 * @param din
	 * @throws ODMException
	 */
	protected void processInputFile(IFileReader din) throws ODMException {
		String str;
		RecType recordType=RecType.Undefined;
		do{
			str=din.readLine();
			
			if(str!=null) {
				str = str.trim();
				if(str.startsWith(Token_Data)) {
					recordType=PWDHelper.getDataType(str);
					processMetadataLine(din, str, recordType);
				} //end of processing data type
			
				else if(str.startsWith("//"))
					processOtherTypeLine(str);
				else if(str.startsWith("{"))
					processOtherTypeLine(str);
				else if(str.startsWith("}")){
					processOtherTypeLine(str);
				}
				// start processing record data
				else if(!str.isEmpty()){
					processDataLine(din, str, recordType);
				}
			}//end of if str is not null  
		}while (str!=null);
	}
	
	/**
	 * process meta data line(s) 
	 * 
	 * @param din
	 * @param str
	 * @param recordType
	 * @throws ODMException
	 */
	abstract protected void processMetadataLine(IFileReader din, String str, RecType recordType) throws ODMException ;
	
	/**
	 * process data line(s)
	 * 
	 * @param din
	 * @param str
	 * @param recordType
	 * @throws ODMException
	 */
	abstract protected void processDataLine(IFileReader din, String str, RecType recordType) throws ODMException ;
	
	/**
	 * process other type line
	 * 
	 * @param str
	 */
	abstract protected void processOtherTypeLine(String str);
	
	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) {
		ODMLogger.getLogger().severe("Method not implemented");
		return null;
	}	
}
