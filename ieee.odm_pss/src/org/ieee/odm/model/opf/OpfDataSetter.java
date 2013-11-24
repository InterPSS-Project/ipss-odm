 /*
  * @(#)OpfDataSetter.java   
  *
  * Copyright (C) 2008 www.interpss.org
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
  * @Date 04/11/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.model.opf;

import static org.ieee.odm.ODMObjectFactory.OdmObjFactory;

import java.util.List;



import org.ieee.odm.model.aclf.AclfDataSetter;
import org.ieee.odm.schema.ActivePowerPriceEnumType;
import org.ieee.odm.schema.CostAmountXmlType;
import org.ieee.odm.schema.CostPriceXmlType;
import org.ieee.odm.schema.IncCostXmlType;
import org.ieee.odm.schema.LinCoeffXmlType;
import org.ieee.odm.schema.MWUnitXmlType;
import org.ieee.odm.schema.OPFUnitCostXmlType;
import org.ieee.odm.schema.OpfGenBusXmlType;
import org.ieee.odm.schema.PieceWiseLinearModelXmlType;
import org.ieee.odm.schema.QuadraticModelXmlType;
import org.ieee.odm.schema.SqrCoeffXmlType;
import org.ieee.odm.schema.StairStepXmlType;

/**
 * OPF ODM model parser data setter functions
 * 
 * @author mzhou
 *
 */
public class OpfDataSetter extends AclfDataSetter {
	/**
	 * set QuadraticModel data
	 * 
	 * @param incCost
	 * @param sq
	 * @param sqUnit
	 * @param ln
	 * @param lnUnit
	 * @param cnst
	 */
	public static void setQuadraticModel(IncCostXmlType incCost,double sq, ActivePowerPriceEnumType sqUnit,
			double ln,ActivePowerPriceEnumType lnUnit,	double cnst ){
		QuadraticModelXmlType quaModel = OdmObjFactory.createQuadraticModelXmlType();		
		
		// set sqr term coeff
		SqrCoeffXmlType sqr = new SqrCoeffXmlType();
		sqr.setValue(sq);
		sqr.setUnit(sqUnit);
		quaModel.setSqrCoeff(sqr);
		//set linear term coeff
		LinCoeffXmlType lnc = new LinCoeffXmlType();
		lnc.setValue(ln);
		lnc.setUnit(lnUnit);
		quaModel.setLinCoeff(lnc);
		// set const term
		quaModel.setConstCoeff(cnst);		
		
		incCost.setQuadraticModel(quaModel);
		
	}
	
	/**
	 * set PWModel data
	 * 
	 * @param incCost
	 * @param point
	 */
	public static void setPWModel(IncCostXmlType incCost, double[] point){
		PieceWiseLinearModelXmlType pwModel = OdmObjFactory.createPieceWiseLinearModelXmlType();
		int size = point.length;
		List<StairStepXmlType> stepList = pwModel.getStairStep();
		for(int i =0 ; i<size/2-1; i++){
			double p0 = point[2*i];
			double f0 = point[2*i+1];
			double p1 = point[2*i+2];
			double f1 = point[2*i+3];
			double price_val = (f1-f0)/(p1-p0);
			
			setStep(stepList,price_val,OPFUnitCostXmlType.DOLLAR_PER_M_WH,
					p0,MWUnitXmlType.MW);
			setStep(stepList,price_val,OPFUnitCostXmlType.DOLLAR_PER_M_WH,
					p1,MWUnitXmlType.MW);			
			incCost.setPieceWiseLinearModel(pwModel);
		}
		
	}
	
	/**
	 * set Step data
	 * 
	 * @param stepList
	 * @param price_val
	 * @param priceUnit
	 * @param amount_val
	 * @param mwUnit
	 */
	private static void setStep(List<StairStepXmlType> stepList,double price_val,
			OPFUnitCostXmlType priceUnit, double amount_val, MWUnitXmlType mwUnit){
		StairStepXmlType step = OdmObjFactory.createStairStepXmlType();
		CostPriceXmlType cost = new CostPriceXmlType();				
		cost.setValue(price_val);
		cost.setUnit(priceUnit);
		CostAmountXmlType amount = new CostAmountXmlType();
		amount.setValue(amount_val);
		amount.setUnit(mwUnit);
		step.setAmount(amount);
		step.setPrice(cost);
		stepList.add(step);
	}
}
