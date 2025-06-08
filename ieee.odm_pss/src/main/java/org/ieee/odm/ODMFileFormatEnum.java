 /*
  * @(#)ODMFileFormatEnum.java   
  *
  * Copyright (C) 2008-2010 www.interpss.org
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
  * @Date 12/04/2010
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm;

/**
 *  file format supported by ODM
 * 
 * @author mzhou
 *
 */
public enum ODMFileFormatEnum {
	IeeeCDF, IeeeCDFExt1, 
	UCTE, 
	GePSLF, 
	PsseV26, PsseV29, PsseV30, PsseV31, PsseV32, PsseV33, PsseV34, PsseV35, PsseV36,
	BPA,
	PWD, PWD_Contingency,
	MatPower
}
