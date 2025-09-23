package org.ieee.odm.adapter.pwd;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.adapter.pwd.impl.ContingencyDataProcessor;
import org.ieee.odm.adapter.pwd.impl.PWDHelper;
import org.ieee.odm.common.IFileReader;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.AclfModelParser;
import org.ieee.odm.schema.OriginalDataFormatEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
  * PWD contingency data adapter
  * 
  * @version 0.2  01/08/2012
  * @author
  * 
  */
public class PWDAdapterForContingency extends AbstractODMAdapter{
    private static final Logger log = LoggerFactory.getLogger(PWDAdapterForContingency.class);
	//Define the contingency types
	public enum ContingencyType{BRANCH,SERIES_CAPACITOR,DC_LINE_CHANGE,DC_LINE_SETPOINT,PHSXFR_SETPOINT,THREEW_XFR};
	//Define the data record types
	private enum RecType{CONTINGENCY,CTG_OPTIONS,GLOBALCONTINGENCYACTIONS,UNSUPPORTED};
	
	
	public PWDAdapterForContingency(){
		super();
	}
	/** 
	 * Entry point of the PWD contingency data processor. It reads in the contingency definition data
	 * and parses it into ODM, saved in ODMModelParser object.
	 */
	@Override
	protected IODMModelParser parseInputFile(IFileReader din, String encoding) {
		String str=""; 
		final String CONTINGENCY_Token="CONTINGENCY";
		final String CTG_OPTIONS_Token="CTG_OPTIONS";
		final String GLOBAL_CTG_ACTION_Token="GLOBALCONTINGENCYACTIONS";
		
		RecType recType=null;
		
		AclfModelParser parser=new AclfModelParser(encoding);
		parser.initCaseContentInfo(OriginalDataFormatEnumType.POWER_WORLD);
		
		//create contingency data processor
		
		//In fact you don't know what kind of contingency the data defined until you get the CTGElement Part
		
		/*Now we know the contingency data we got is branch status change(most changed to OPEN, some CLOSE)
		 * so we can defined the exact type  contingency processor before the processing;
		 */
		ContingencyDataProcessor ctgProc=new ContingencyDataProcessor(parser);
		

		try {
			do{
				str=din.readLine();
				if (str != null) {
					if (str.startsWith("DATA")) {
						if (getDataType(str).equals(CONTINGENCY_Token)) {
							recType = RecType.CONTINGENCY;
						} 
						else if (getDataType(str).equals(CTG_OPTIONS_Token))
							recType = RecType.CTG_OPTIONS;
						else if (getDataType(str).equals(GLOBAL_CTG_ACTION_Token))
							recType = RecType.GLOBALCONTINGENCYACTIONS;
						else
							recType = RecType.UNSUPPORTED;

						// parse Field Names
						while (!PWDHelper.isArgumentFieldsCompleted(str)) {
							str += din.readLine();
						}
						ctgProc.parseMetadata(str);

					} else if (str.trim().startsWith("//"))
						log.debug("comments:" + str);
					else if (str.trim().startsWith("{"))
						log.info(recType + " type data begins");
					else if (str.trim().startsWith("}"))
						log.info(recType + " type data ends");

					// start processing record data
					else if (!str.trim().isEmpty()
							&& recType == RecType.CONTINGENCY) {
						ctgProc.processContingencyData(str);

					}
				}// end of if str!=null
			} while(str!=null);
		} catch (Exception e) {
			log.error("Contingency file processing error: " + e.toString());
			//e.printStackTrace();
		}
	
		return parser;
	}

	@Override
	protected IODMModelParser parseInputFile(NetType type, IFileReader[] din, String encoding) {
		log.error("Method not implemented");
		return null;
	}
	/**
	 * Get the contingency definition data type
	 * @param str
	 * @return record data type string
	 */
	private String getDataType(String str){
		int indexOfLeftParenthesis=str.indexOf("(");
		int indexOfFirstComma=str.indexOf(",");
		String dataType=str.substring(indexOfLeftParenthesis+1, indexOfFirstComma).trim();
		
		return dataType;
		
	}
}