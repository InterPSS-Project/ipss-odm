package org.ieee.odm.common;

import java.io.IOException;

public class ODMTextFileReader implements IFileReader {
	java.io.BufferedReader din = null;
	
	public ODMTextFileReader(java.io.BufferedReader din) { 
		this.din = din;
	}
	
	@Override public String readLine() throws ODMException {
		try {
			return din.readLine();
			//System.out.println(str);
		} catch (IOException e) {
			throw new ODMException(e.toString());
		}
	}
}
