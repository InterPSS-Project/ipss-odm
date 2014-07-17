package org.ieee.odm.adapter.psse.mapper.acsc;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.mapper.aclf.BasePSSEDataMapper;
import org.ieee.odm.adapter.psse.parser.acsc.PSSEXfrZeroSeqDataParser;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.common.ODMLogger;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.acsc.AcscParserHelper;
import org.ieee.odm.model.acsc.BaseAcscModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.XformerConnectionXmlType;
import org.ieee.odm.schema.XformrtConnectionEnumType;
import org.ieee.odm.schema.XfrShortCircuitXmlType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEXfrZeroSeqDataMapper extends BasePSSEDataMapper{
	
	public PSSEXfrZeroSeqDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEXfrZeroSeqDataParser(ver);
	}
	
	/*
	 * for two-winding transformers:
       I, J, K, ICKT, CC, RG, XG, R1, X1, R2, X2
       
       for three-winding transformers:
       I, J, K, ICKT, CC, RG, XG, R1, X1, R2, X2, R3, X3
	 * 
	 * 
	 * For a two-winding transformer, valid values are 1 through 9. They define the 
       following zero sequence connections that are shown in Figure 5-18.
        1 series path (e.g., grounded wye-grounded wye)
        2 no series path, ground path on winding one side (e.g., grounded wye-delta)
        3 no series path, ground path on winding two side (e.g., delta-grounded wye)
        4 no series or ground path (e.g., delta-delta)
        5 series path, ground path on winding two side (normally only used as part of 
          a three-winding transformer) 
        6 no series path, ground path on winding one side, earthing transformer on 
          winding two side (e.g., grounded wye-delta with an earthing transformer)
        7 no series path, earthing transformer on winding one side, ground path on 
          winding two side (e.g., delta with an earthing transformer-grounded wye)
        8 series path, ground path on each side (e.g., grounded wye-grounded wye 
          core-type autotransformer with a grounding resistance
          
         ****************************************************************************
         * 3-WINDING XFR **
         * 
          The connection codes of the three 
          two-winding transformers comprising the three-winding transformer are shown in 
          parenthesis in winding number order:
             1 wye-wye-wye; (5-1-1).
             2 wye-wye-delta; (1-1-3).
             3 delta-wye-delta (non-autotransformer); (3-1-3).
             4 delta-delta-delta; (3-3-3).
             5 delta-wye-delta (autotransformer); (1-2-1).
             
          ***********************************************************************
          
          RG, XG  Zero sequence grounding impedance for an impedance grounded transformer ZG
                  For a three-winding transformer, ZG is modeled in the lowest numbered 
                  winding whose corresponding connection code is 2, 3, 5, 6 or 7
          R1, X1  Zero sequence impedance of a two-winding transformer, or the winding one zero 
                  sequence impedance of a three-winding transformer,
          R2, X2  For 2-winding transformer, they are only valid when CC = 8, for specifying the grounding impedance
                  at the winding 2.
                  For 3-winding xfr, stands for the winding 2 zero sequence impedance
          R3, X3  Winding 3 zero sequence impedance 
	 */
	public void procLineString(String lineStr, BaseAcscModelParser<? extends NetworkXmlType> parser) throws ODMException {
		dataParser.parseFields(lineStr);
		
		int i = dataParser.getInt("I");
      	int j = dataParser.getInt("J");                
      	int k = dataParser.getInt("K");
      	
      	final String fbusId = IODMModelParser.BusIdPreFix+i;
	    final String tbusId = IODMModelParser.BusIdPreFix+j;
	    final String terbusId = IODMModelParser.BusIdPreFix+k;

                
        boolean is3W = (k != 0); 
        
        String cirId = dataParser.getString("ICKT");
        
        int cc = dataParser.getInt("CC");
        
        double rg = dataParser.getDouble("RG");
        double xg = dataParser.getDouble("XG");
        
        double r1 = dataParser.getDouble("R1");
        double x1 = dataParser.getDouble("X1");
        
        double r2 = dataParser.getDouble("R2",0);
        double x2 = dataParser.getDouble("X2",0);
        
        if(!is3W){
        	XfrShortCircuitXmlType scXfr = (XfrShortCircuitXmlType) parser.getXfrBranch(fbusId, tbusId, cirId);
        	if(scXfr == null){ 
        		ODMLogger.getLogger().severe("One sc xfr is NULL or cannot be found, fBusId,tBusId,cirId  #"+fbusId+"_"+tbusId+"_"+cirId);
        		//As there may be some inconsistency between load flow data and sequence data, we can check the inverse direction.
        		scXfr = (XfrShortCircuitXmlType) parser.getXfrBranch(tbusId, fbusId, cirId);
        		if(scXfr == null){
        			throw new ODMException("The xfr branch cannot be found in both direction, f->t or t->f . " +
        					"fBusId,tBusId,cirId  # "+fbusId+"_"+tbusId+"_"+cirId);
        		}
        	}
        	scXfr.setZ0(BaseDataSetter.createZValue(r1, x1, ZUnitType.PU));
        	
        	switch(cc){
        	case 1:  //grounded wye-grounded wye
        		XformerConnectionXmlType xfrConnect1 = AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		XformerConnectionXmlType xfrConnect2 = AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        		
        	case 2: //grounded wye-delta ( ground path on winding one side)
        		xfrConnect1 = (rg!=0||xg!=0)?AcscParserHelper.createZGroundingConnection(rg,xg):
        			AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		xfrConnect2 = AcscParserHelper.createUnGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        		
        	case 3: //delta-grounded wye ( ground path on winding two side)
        		xfrConnect1 = AcscParserHelper.createUnGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		
        		xfrConnect2 = (rg!=0||xg!=0)?AcscParserHelper.createZGroundingConnection(rg,xg):
        			AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        	case 4:
        		xfrConnect1 = AcscParserHelper.createUnGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		xfrConnect2 = AcscParserHelper.createUnGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        		
        	case 5: //series path, ground path on winding two side (normally only used as part of 
    		    //a three-winding transformer) 
    		  throw new UnsupportedOperationException("Zero sequence for two winding transformer of cc = 5 is not supported yet!");
        		
        	case 6://grounded wye-delta with an earthing transformer
        		xfrConnect1 = AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		
        		xfrConnect2 = (rg!=0||xg!=0)?AcscParserHelper.createZGroundingConnection(rg,xg):
        			AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        	case 7://delta with an earthing transformer-grounded wye
        		
        		/*no series path, earthing transformer on winding one side, ground path on 
        		  winding two side
        		*/
        		xfrConnect1 = (rg!=0||xg!=0)?AcscParserHelper.createZGroundingConnection(rg,xg):
        			AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.DELTA);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		xfrConnect2 = AcscParserHelper.createDirectedGroundingConnection();
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        	case 8 :
        		//ZG2 (i.e., RG2 + jXG2) is applied if the connection code CC is 8
        		 xfrConnect1 = AcscParserHelper.createZGroundingConnection(rg,xg);
        		xfrConnect1.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setFromSideConnection(xfrConnect1);
        		
        		xfrConnect2 = AcscParserHelper.createZGroundingConnection(r2,x2);
        		xfrConnect2.setXfrConnection(XformrtConnectionEnumType.WYE);
        		scXfr.setToSideConnection(xfrConnect2);
        		break;
        		
        	default:
        		
        		
        		
        		
        	}
        }
        else{// three winding transformer
        	throw new UnsupportedOperationException("Zero sequence for three winding transformer is not supported yet!");
        	
        }
        
        
	}
}
