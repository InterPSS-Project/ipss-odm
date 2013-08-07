/*
 * @(#)NumericUtil.java   
 *
 * Copyright (C) 2006 www.interpss.com
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
 * @Date 04/15/2011
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.common;

import org.ieee.odm.schema.ComplexXmlType;
import org.ieee.odm.schema.VoltageXmlType;

/**
 * numeric utility functions for testing purpose
 * 
 * @author mzhou
 *
 */
public class ODMNumericUtil {
	static double ERR = 0.00001;
	
	/**
	 * test equality
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean equals(ComplexXmlType x, ComplexXmlType y) {
		return (x == null && y == null) || x != null && y != null &&
		       Math.abs(x.getRe() - y.getRe()) < ERR && Math.abs(x.getIm() - y.getIm()) < ERR;
	}

	/**
	 * test equality
	 * 
	 * @param x
	 * @param y
	 * @param error tolerance
	 * @return
	 */
	public static boolean equals(ComplexXmlType x, ComplexXmlType y, double err) {
		return (x == null && y == null) || x != null && y != null &&
		       Math.abs(x.getRe() - y.getRe()) < err && Math.abs(x.getIm() - y.getIm()) < err;
	}
	
	/**
	 * test equality
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean equals(VoltageXmlType x, VoltageXmlType y) {
		return (x == null && y == null) || x != null && y != null &&
		       Math.abs(x.getValue() - y.getValue()) < ERR;
	}

	/**
	 * test equality
	 * 
	 * @param x
	 * @param y
	 * @param error tolerance
	 * @return
	 */
	public static boolean equals(VoltageXmlType x, VoltageXmlType y, double err) {
		return (x == null && y == null) || x != null && y != null &&
		       Math.abs(x.getValue() - y.getValue()) < err;
	}
}
