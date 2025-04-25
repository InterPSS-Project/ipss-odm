 /*
  * @(#)PSSELineDataMapper.java   
  *
  * Copyright (C) 2006 www.interpss.org
  *
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE
  * as published by the Free Software Foundation; either version 2.1
  * of the License, or (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * @Author Mike Zhou
  * @Version 1.0
  * @Date 09/15/2006
  * 
  *   Revision History
  *   ================
  *
  */

  package org.ieee.odm.adapter.psse.raw.mapper.aclf;

  import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;
  
  import org.apache.commons.math3.complex.Complex;
  import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
  import org.ieee.odm.adapter.psse.raw.PSSERawAdapter;
  import org.ieee.odm.adapter.psse.raw.parser.aclf.PSSELineDataRawParser;
  import org.ieee.odm.common.ODMBranchDuplicationException;
  import org.ieee.odm.common.ODMException;
  import org.ieee.odm.common.ODMLogger;
  import org.ieee.odm.model.IODMModelParser;
  import org.ieee.odm.model.aclf.AclfDataSetter;
  import org.ieee.odm.model.aclf.BaseAclfModelParser;
  import org.ieee.odm.model.base.BaseDataSetter;
  import org.ieee.odm.schema.ApparentPowerUnitType;
  import org.ieee.odm.schema.BranchBusSideEnumType;
  import org.ieee.odm.schema.LineBranchXmlType;
  import org.ieee.odm.schema.NetworkXmlType;
  import org.ieee.odm.schema.YUnitType;
  import org.ieee.odm.schema.ZUnitType;
  
  public class PSSELineDataRawMapper extends BasePSSEDataRawMapper{

	  private static final double DEFAULT_ZERO_IMPEDANCE_THRESHOLD = 0.0001;
  
	  public PSSELineDataRawMapper(PsseVersion ver) {
		  super(ver);
		  this.dataParser = new PSSELineDataRawParser(ver);
	  }
	  
	  /*
	   * BranchData
	   * V30-V33
	   * 
	   * I,J,CKT,R,X,B,RATEA,RATEB,RATEC,GI,BI,GJ,BJ,ST,LEN,O1,F1,...,O4,F4
	   * 
	   * V34-35 (MAIN Change:  RATE (ABC) --> RATE (1-12)
	   * 
  
					"I",       "J",       "CKT",     "R",       "X",             
					"B",      "NAME",    "RATE1",    "RATE2",   "RATE3",
					"RATE4",    "RATE5",   "RATE6", "RATE7",   "RATE8",    
				 //  15         16         17         18         19
					"RATE9",    "RATE10", "RATE11", "RATE12",    "GI", 
				  //  20         21         22         23        24
					"BI",      "GJ",      "BJ",     "ST",      "MET", 
				  //  25         26         27         28        29  
					 "LEN",    "O1",      "F1",      "O2",     "F2", 
										 
				 //  30         31         32         33        34
					 "O3",     "F3",      "O4",      "F4"
					 
		  V36
		  Add "BP" (bypass) in addition to V34-35
	   * 
	   */
	  public void procLineString(String lineStr, BaseAclfModelParser<? extends NetworkXmlType> parser) throws ODMException {
		  //procLineFields(lineStr, version);
		  dataParser.parseFields(lineStr);
		  //System.out.println(lineStr + "\n" + dataParser.toString());
  /*
		  I,J,CKT,R,X,B,RATEA,RATEB,RATEC,GI,BI,GJ,BJ,ST,LEN,O1,F1,...,O4,F4
		  
		  ST Initial branch status where 1 designates in-service and 0 designates out-of-service. ST = 1 by default.
  */
		  int i = dataParser.getInt("I");
		  int j = dataParser.getInt("J");
		  
		  /* starting from V31
		   * MET	Metered end flag;
			  <=1 to designate bus I as the metered end 
			  =>2 to designate bus J as the metered end.
				  MET = 1 by default.
		   */
		  boolean fromMetered = true;
		  if (PSSERawAdapter.getVersionNo(this.version) >= 31) {
			  int met = dataParser.getInt("MET", 1);
			  if (met >= 2)
				  fromMetered = false;
		  }
		  else {
			  if (j < 0) {
				  fromMetered = false;
				  j = -j;
			  }
		  }
			
		  final String fid = IODMModelParser.BusIdPreFix+i;
		  final String tid = IODMModelParser.BusIdPreFix+j;
  
		  LineBranchXmlType braRecXml;
		  try {
			  braRecXml = (LineBranchXmlType) parser.createLineBranch(fid, tid, dataParser.getValue("CKT"));
		  } catch (ODMBranchDuplicationException e) {
			  ODMLogger.getLogger().severe(e.toString());
			  return;
		  }		
		  
		  int status = dataParser.getInt("ST", 1);
		  braRecXml.setOffLine(status != 1);
		  
		  braRecXml.setMeterLocation( fromMetered ? BranchBusSideEnumType.FROM_SIDE :
										  BranchBusSideEnumType.TO_SIDE);
			
		  double r = dataParser.getDouble("R", 0.0);
		  double x = dataParser.getDouble("X", 0.0);
		  double b = dataParser.getDouble("B", 0.0);
		  
		  //TODO Temporally bad data fix
		  if(new Complex(r,x).abs()< DEFAULT_ZERO_IMPEDANCE_THRESHOLD){
			  x = DEFAULT_ZERO_IMPEDANCE_THRESHOLD;
			  ODMLogger.getLogger().severe("Line # "+braRecXml.getId()+", has zero impedance input, change X = "+DEFAULT_ZERO_IMPEDANCE_THRESHOLD);
		  }
		  
		  AclfDataSetter.setLineData(braRecXml, r, x, ZUnitType.PU, 0.0, b, YUnitType.PU);
		  
		  braRecXml.setRatingLimit(OdmObjFactory.createBranchRatingLimitXmlType());
		  
		  if(PSSERawAdapter.getVersionNo(this.version) <34) {
  
			  double ratea = dataParser.getDouble("RATEA", 0.0);
			  double rateb = dataParser.getDouble("RATEB", 0.0);
			  double ratec = dataParser.getDouble("RATEC", 0.0);
			  
			  AclfDataSetter.setBranchRatingLimitData(braRecXml.getRatingLimit(),
						  ratea, rateb, ratec, ApparentPowerUnitType.MVA);
		  
		  }
		  else if(PSSERawAdapter.getVersionNo(this.version) <37) {
			  double[] ratings = new double[12];
			  for(int idx =0; idx<ratings.length;idx++) {
				  ratings[idx] =  dataParser.getDouble("RATE"+(idx+1), 0.0);
			  }
			  AclfDataSetter.setBranchRatingLimitData(braRecXml.getRatingLimit(), ratings, ApparentPowerUnitType.MVA);
		  }
		  else {
			  throw new ODMException("The PSSE version is not supported yet:"+this.version);
		  }
		  
		  double gi = dataParser.getDouble("GI", 0.0);
		  double bi = dataParser.getDouble("BI", 0.0);
		 if ( gi != 0.0 || bi != 0.0)
			 braRecXml.setFromShuntY(BaseDataSetter.createYValue(gi, bi, YUnitType.PU));
		 
		  double gj = dataParser.getDouble("GJ", 0.0);
		  double bj = dataParser.getDouble("BJ", 0.0);
		 if ( gj != 0.0 || bj != 0.0)
			 braRecXml.setToShuntY(BaseDataSetter.createYValue(gj, bj, YUnitType.PU));
		 
		 mapOwnerInfo(braRecXml);
		 
		 if(PSSERawAdapter.getVersionNo(this.version) >=36) {
			 int bp = dataParser.getInt("BP",0);
			 braRecXml.setBypass(bp==1);
		 }
	  }
  }
  