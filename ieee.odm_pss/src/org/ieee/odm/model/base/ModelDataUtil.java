package org.ieee.odm.model.base;

import org.apache.commons.math3.complex.Complex;
import org.ieee.odm.schema.PowerXmlType;
import org.ieee.odm.schema.YXmlType;
import org.ieee.odm.schema.ZXmlType;

import com.sun.istack.internal.FinalArrayList;

public class ModelDataUtil {
	
	/**
	 * get z = z1//z2;
	 * @param z1
	 * @param z2
	 * @return
	 */
	public static ZXmlType addParallelZ(final ZXmlType z1, final ZXmlType z2){
		if(z1.getUnit()!=z2.getUnit()){
			try {
				throw new Exception("Unit type of both are not the same, z1 unit #"+z1.getUnit()+", z2 unit #"+z2.getUnit());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Complex z1cplx = ZXmlType2Cmplx(z1);
		Complex z2cplx = ZXmlType2Cmplx(z2);
		Complex parallelZ =z1cplx.multiply(z2cplx).divide(z1cplx.add(z2cplx));
		
		return BaseDataSetter.createZValue(parallelZ.getReal(), parallelZ.getImaginary(), z1.getUnit());
		
	}
	
	/**
	 * y1 = y1 + y2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static YXmlType addParallelY(YXmlType y1, YXmlType y2){
		if(y1.getUnit()!=y2.getUnit()){
			try {
				throw new Exception("Unit type of both are not the same, y1 unit #"+y1.getUnit()+", y2 unit #"+y2.getUnit());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		y1.setRe(y1.getRe()+y2.getRe());
		y1.setIm(y1.getIm()+y2.getIm());
		
		return y1;
		
	}
	
	public static Complex ZXmlType2Cmplx(final ZXmlType z){
		return new Complex(z.getRe(),z.getIm());
	}
	
	public static Complex YXmlType2Cmplx(final YXmlType Y){
		return new Complex(Y.getRe(),Y.getIm());
	}
	
	/**
	 * return z =z*m
	 * @param z
	 * @param m
	 * @return
	 */
	public static ZXmlType ZXmlMultiplyDouble(final ZXmlType z, double m){
		ZXmlType newZ = BaseDataSetter.createZValue(0, 0, z.getUnit());
		newZ.setIm(z.getIm()*m);
		newZ.setRe(z.getRe()*m);
		return newZ;
		
	}
	
	/**
	 * PowerXmlType p1 = p1 + p2;
	 * @param p1
	 * @param p2
	 * @return p1
	 */
	public static PowerXmlType addPower(PowerXmlType p1,PowerXmlType p2){
		if(p1.getUnit()!=p2.getUnit()){
			try {
				throw new Exception("Unit type of both are not the same, p1 unit #"+p1.getUnit()+", p2 unit #"+p2.getUnit());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		p1.setRe(p1.getRe()+p2.getRe());
		p1.setIm(p1.getRe()+p2.getRe());
		
		return p1;
		
	}
	

}
