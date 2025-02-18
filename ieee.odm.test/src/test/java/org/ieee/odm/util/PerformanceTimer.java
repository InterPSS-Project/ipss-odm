 /*
  * @(#)PerformanceTimer.java   
  *
  * Copyright (C) 2007 www.interpss.com
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
  * @Date 06/04/2008
  * 
  *   Revision History
  *   ================
  *
  */

package org.ieee.odm.util;

import java.util.logging.Logger;


public class PerformanceTimer {
    private long starttime = 0;
    private long endtime = 0;
    private Logger logger = null;

    public PerformanceTimer(Logger log) {
    	this.logger = log;
    	this.start();
    }

    public void start() {
        this.starttime = System.currentTimeMillis() ;
    }
    
    public long end() {
        this.endtime = System.currentTimeMillis() ;
        return endtime - starttime;
    }
    
    public long getDuration() {
        return endtime - starttime;
    }
    
    /**
     * Log the duration to log file and print on the Console
     * 
     * @param str log message
     */
    public String log(String str) {
    	end();
        String s = str + " (sec) = " + getDuration()/1000.0;
        this.logger.info(s);
        return s;
    }

    public void logStd(String str) {
    	end();
        System.out.println(str + " (sec) = " + getDuration()/1000.0 );
    }
}
