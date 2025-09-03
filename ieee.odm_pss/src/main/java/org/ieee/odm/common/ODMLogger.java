/*
 * @(#)ODMLogger.java   
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
 * @Date 09/15/2010
 * 
 *   Revision History
 *   ================
 *
 */

package org.ieee.odm.common;

import java.util.logging.Logger;

/**
 * ODM logger
 * 
 * @author mzhou
 *
 */
@Deprecated
public class ODMLogger {
	private static Logger logger = null;

	/**
	 * set logger level
	 * 
	 * @param l
	 */
	public static void setLogger(Logger l) {
		logger = l;
	}

	/**
	 * get logger object
	 * 
	 * @return
	 */
	public static Logger getLogger() { 
		if (logger == null) 
			logger = Logger.getLogger("org.ieee.odm");
		return logger; 
	}
}
