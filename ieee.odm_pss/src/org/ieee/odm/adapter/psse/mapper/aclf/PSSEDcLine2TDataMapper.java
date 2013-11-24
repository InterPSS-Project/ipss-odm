 /*
  * @(#)PSSEDcLine2TDataMapper.java   
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

package org.ieee.odm.adapter.psse.mapper.aclf;

import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.ieee.odm.adapter.psse.parser.aclf.PSSEDcLine2TDataParser;
import org.ieee.odm.common.ODMBranchDuplicationException;
import org.ieee.odm.common.ODMException;
import org.ieee.odm.model.IODMModelParser;
import org.ieee.odm.model.aclf.BaseAclfModelParser;
import org.ieee.odm.model.base.BaseDataSetter;
import org.ieee.odm.schema.ActivePowerUnitType;
import org.ieee.odm.schema.AngleUnitType;
import org.ieee.odm.schema.BranchXmlType;
import org.ieee.odm.schema.BusXmlType;
import org.ieee.odm.schema.ConverterXmlType;
import org.ieee.odm.schema.CurrentUnitType;
import org.ieee.odm.schema.DCLineData2TXmlType;
import org.ieee.odm.schema.DcLineControlModeEnumType;
import org.ieee.odm.schema.DcLineMeteredEndEnumType;
import org.ieee.odm.schema.NetworkXmlType;
import org.ieee.odm.schema.VoltageUnitType;
import org.ieee.odm.schema.ZUnitType;

public class PSSEDcLine2TDataMapper <
TNetXml extends NetworkXmlType, 
TBusXml extends BusXmlType,
TLineXml extends BranchXmlType,
TXfrXml extends BranchXmlType,
TPsXfrXml extends BranchXmlType> extends BasePSSEDataMapper{
	
	public PSSEDcLine2TDataMapper(PsseVersion ver) {
		super(ver);
		this.dataParser = new PSSEDcLine2TDataParser(ver);
	}
	
	public void procLineString(String[] lineStrAry, BaseAclfModelParser<TNetXml, TBusXml,TLineXml,TXfrXml,TPsXfrXml> parser) throws ODMException, ODMBranchDuplicationException {
		dataParser.parseFields(lineStrAry);
		
		int MDC = this.dataParser.getInt("MDC"), 
		    CCCITMX = this.dataParser.getInt("CCCITMX");
		
		//NOTE: DC Line Num is necessarily  an Integer, as illustrated in the "sample.raw" by PSS/E, 
		//it can be string too, Therefore, it is better to save it as DCLine Id in String
		String DCLineID = this.dataParser.getString("I"), 
		       METER = this.dataParser.getString("METER"), 
		       IDR = this.dataParser.getString("IDR"), 
		       IDI = this.dataParser.getString("IDI");
		double RDC = this.dataParser.getDouble("RDC"),
		       SETVL = this.dataParser.getDouble("SETVL"),
		       VSCHD = this.dataParser.getDouble("VSCHD"),
		       VCMOD = this.dataParser.getDouble("VCMOD"),
		       RCOMP = this.dataParser.getDouble("RCOMP"),
		       DELTI = this.dataParser.getDouble("DELTI"),
		       DCVMIN = this.dataParser.getDouble("DCVMIN"),
		       CCCACC = this.dataParser.getDouble("CCCACC"); 
		int IPR = this.dataParser.getInt("IPR"), 
		    NBR = this.dataParser.getInt("NBR"), 
		    ICR = this.dataParser.getInt("ICR"), 
		    IFR = this.dataParser.getInt("IFR"), 
		    ITR = this.dataParser.getInt("ITR");
		double ALFMX = this.dataParser.getDouble("ALFMX"),
		       ALFMN = this.dataParser.getDouble("ALFMN"),
		       RCR = this.dataParser.getDouble("RCR"),
		       XCR = this.dataParser.getDouble("XCR"),
		       EBASR = this.dataParser.getDouble("EBASR"),
		       TRR = this.dataParser.getDouble("TRR"),
		       TAPR = this.dataParser.getDouble("TAPR"),
		       TMXR = this.dataParser.getDouble("TMXR"),
		       TMNR = this.dataParser.getDouble("TMNR"),
		       STPR = this.dataParser.getDouble("STPR"),
		       XCAPR = this.dataParser.getDouble("XCAPR");	
		int IPI = this.dataParser.getInt("IPI"), 
		    NBI = this.dataParser.getInt("NBI"), 
		    ICI = this.dataParser.getInt("ICI"), 
		    IFI = this.dataParser.getInt("IFI"), 
		    ITI = this.dataParser.getInt("ITI");
		double GAMMX = this.dataParser.getDouble("GAMMX"),
		       GAMMN = this.dataParser.getDouble("GAMMN"),
		       RCI = this.dataParser.getDouble("RCI"),
		       XCI = this.dataParser.getDouble("XCI"),
		       EBASI = this.dataParser.getDouble("EBASI"),
		       TRI = this.dataParser.getDouble("TRI"),
		       TAPI = this.dataParser.getDouble("TAPI"),
		       TMXI = this.dataParser.getDouble("TMXI"),
		       TMNI = this.dataParser.getDouble("TMNI"),
		       STPI = this.dataParser.getDouble("STPI"),
		       XCAPI = this.dataParser.getDouble("XCAPI");	
		
		final String fid = IODMModelParser.BusIdPreFix+IPR;
		final String tid = IODMModelParser.BusIdPreFix+IPI;
		DCLineData2TXmlType dcLine2T;
		dcLine2T = parser.createDCLine2TRecord(fid, tid, DCLineID);

		/*
		Line-1: 
			I,MDC,RDC,      SETVL,    VSCHD,   VCMOD,  RCOMP,    DELTI,  METER,  DCVMIN,CCCITMX,CCCACC
            1,1,  13.7500,  552.00,   410.00,  -1.00,  13.7500,  0.10000,'I',    0.00,  20,   1.00000
			
			MDC Control mode: 0 for blocked, 1 for power, 2 for current. MDC = 0 by default.
			SETVL Current (amps) or power (MW) demand. When MDC is one, a positive value of
				SETVL specifies desired power at the rectifier and a negative value specifies
				desired inverter power. No default allowed.					
			RDC The dc line resistance; entered in ohms. No default allowed.
		 */
		if (MDC == 1) {
			dcLine2T.setControlMode(DcLineControlModeEnumType.POWER);
			dcLine2T.setControlOnRectifierSide(SETVL > 0.0);
			dcLine2T.setPowerDemand(BaseDataSetter.createActivePowerValue(SETVL, ActivePowerUnitType.MW));
		}
		else if (MDC == 2) {
			dcLine2T.setControlMode(DcLineControlModeEnumType.CURRENT);
			dcLine2T.setCurrentDemand(BaseDataSetter.createCurrentValue(SETVL, CurrentUnitType.AMP));
		}
		else
			dcLine2T.setControlMode(DcLineControlModeEnumType.BLOCKED);
			
		dcLine2T.setLineR(BaseDataSetter.createRValue(RDC, ZUnitType.OHM));
		
		/*
			VSCHD Scheduled compounded dc voltage; entered in kV. No default allowed.
			METER Metered end code of either ’R’ (for rectifier) or ’I’ (for inverter). METER = ’I’ by default.
		*/
		dcLine2T.setScheduledDCVoltage(BaseDataSetter.createVoltageValue(VSCHD, VoltageUnitType.KV));
		dcLine2T.setMeteredEnd(METER.equals("R")? DcLineMeteredEndEnumType.RECTIFIER :
								DcLineMeteredEndEnumType.INVERTER);
		/*
			VCMOD Mode switch dc voltage; entered in kV. When the inverter dc voltage falls below
				this value and the line is in power control mode (i.e., MDC = 1), the line switches
				to current control mode with a desired current corresponding to the desired power
				at scheduled dc voltage. VCMOD = 0.0 by default.
			RCOMP Compounding resistance; entered in ohms. Gamma and/or TAPI is used to attempt
				to hold the compounded voltage (VDCI + DCCUR*RCOMP) at VSCHD. To control
				the inverter end dc voltage VDCI, set RCOMP to zero; to control the rectifier
				end dc voltage VDCR, set RCOMP to the dc line resistance, RDC; otherwise, set
				RCOMP to the appropriate fraction of RDC. RCOMP = 0.0 by default.
		*/
		dcLine2T.setModeSwitchDCVoltage(BaseDataSetter.createVoltageValue(VCMOD, VoltageUnitType.KV));
		dcLine2T.setCompoundingR(BaseDataSetter.createRValue(RCOMP, ZUnitType.OHM));

		/*
			DELTI Margin entered in per unit of desired dc power or current. This is the fraction by
				which the order is reduced when ALPHA is at its minimum and the inverter is controlling
				the line current. DELTI = 0.0 by default.
			DCVMIN Minimum compounded dc voltage; entered in kV. Only used in constant gamma
				operation (i.e., when GAMMX = GAMMN) when TAPI is held constant and an ac
				transformer tap is adjusted to control dc voltage (i.e., when IFI, ITI, and IDI specify
				a two-winding transformer). DCVMIN = 0.0 by default.
		 */
		dcLine2T.setPowerOrCurrentMarginPU(DELTI);
		dcLine2T.setMinDCVoltage(BaseDataSetter.createVoltageValue(DCVMIN, VoltageUnitType.KV));
		
		/*
		Line-2: IPR,NBR,ALFMX,ALFMN,RCR,XCR,EBASR,TRR,TAPR,TMXR,TMNR,STPR,ICR,IFR,ITR,IDR,XCAPR
		Line-3: IPI,NBI,GAMMX,GAMMN,RCI,XCI,EBASI,TRI,TAPI,TMXI,TMNI,STPI,ICI,IFI,ITI,IDI,XCAPI		
		 */
		/*
			IPR Rectifier converter bus number, or extended bus name enclosed in single quotes
				(see Section 4.1.2). No default allowed.
			NBR Number of bridges in series (rectifier). No default allowed.
			ALFMX Nominal maximum rectifier firing angle; entered in degrees. No default allowed.
			ALFMN Minimum steady-state rectifier firing angle; entered in degrees. No default allowed.
			EBASR Rectifier primary base ac voltage; entered in kV. No default allowed.
			ICR Rectifier firing angle measuring bus number, or extended bus name enclosed in
				single quotes (see Section 4.1.2). The firing angle and angle limits used inside the
				dc model are adjusted by the difference between the phase angles at this bus and
				the ac/dc interface (i.e., the converter bus, IPR). ICR = 0 by default.
			*/
		ConverterXmlType rectifier = dcLine2T.getRectifier();
		ConverterXmlType inverter = dcLine2T.getInverter();
		
		rectifier.setNumberofBridges(NBR);
		rectifier.setMaxFiringAngle(BaseDataSetter.createAngleValue(ALFMX, AngleUnitType.DEG));
		rectifier.setMinFiringAngle(BaseDataSetter.createAngleValue(ALFMN, AngleUnitType.DEG));
		rectifier.setAcSideRatedVoltage(BaseDataSetter.createVoltageValue(EBASR, VoltageUnitType.KV));
		if (ICR != 0)
			rectifier.setFiringAngleMeasuringBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+ICR));
		
		inverter.setNumberofBridges(NBI);
		inverter.setMaxFiringAngle(BaseDataSetter.createAngleValue(GAMMX, AngleUnitType.DEG));
		inverter.setMinFiringAngle(BaseDataSetter.createAngleValue(GAMMN, AngleUnitType.DEG));
		inverter.setAcSideRatedVoltage(BaseDataSetter.createVoltageValue(EBASI, VoltageUnitType.KV));
		if (ICI != 0)
			inverter.setFiringAngleMeasuringBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+ICI));
		
		/*
			RCR Rectifier commutating transformer resistance per bridge; entered in ohms. No default allowed.
			XCR Rectifier commutating transformer reactance per bridge; entered in ohms. No default allowed.
			XCAPR Commutating capacitor reactance magnitude per bridge; entered in ohms. XCAPR = 0.0 by default.			
		*/
		rectifier.setCommutatingZ(BaseDataSetter.createZValue(RCR, XCR, ZUnitType.OHM));
		rectifier.setCommutatingCapacitor(XCAPR);
		
		inverter.setCommutatingZ(BaseDataSetter.createZValue(RCI, XCI, ZUnitType.OHM));
		inverter.setCommutatingCapacitor(XCAPI);	
		
		/*
			TRR Rectifier transformer ratio. TRR = 1.0 by default.
			TAPR Rectifier tap setting. TAPR = 1.0 by default.
			TMXR Maximum rectifier tap setting. TMXR = 1.5 by default.
			TMNR Minimum rectifier tap setting. TMNR = 0.51 by default.
			STPR Rectifier tap step; must be positive. STPR = 0.00625 by default.
			*/
		rectifier.setXformerTurnRatio(TRR);
		rectifier.setXformerTapSetting(BaseDataSetter.createTapPU(TAPR));
		rectifier.setXformerTapLimit(BaseDataSetter.createTapLimit(TMXR, TMNR));
       	rectifier.setXformerTapStepSize(STPR);

       	inverter.setXformerTurnRatio(TRI);
       	inverter.setXformerTapSetting(BaseDataSetter.createTapPU(TAPI));
       	inverter.setXformerTapLimit(BaseDataSetter.createTapLimit(TMXI, TMNI));
       	inverter.setXformerTapStepSize(STPI);
		/*
			IFR Winding one side "from bus" number, or extended bus name enclosed in single
				quotes (see Section 4.1.2), of a two-winding transformer. IFR = 0 by default.
			ITR Winding two side "to bus" number, or extended bus name enclosed in single quotes
				(see Section 4.1.2), of a two-winding transformer. ITR = 0 by default.
			IDR Circuit identifier; the branch described by IFR, ITR, and IDR must have been
				entered as a two-winding transformer; an ac transformer may control at most only
				one dc converter. IDR = '1' by default.
		*/
		if (IFR != 0 && ITR != 0) {
			rectifier.setRefXfrFromBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+IFR));
			rectifier.setRefXfrToBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+ITR));
			rectifier.setRefXfrCirId(IDR);
		}

		if (IFI != 0 && ITI != 0) {
			inverter.setRefXfrFromBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+IFI));
			inverter.setRefXfrToBusId(parser.createBusRef(IODMModelParser.BusIdPreFix+ITI));
			inverter.setRefXfrCirId(IDI);
		}
	}
}
