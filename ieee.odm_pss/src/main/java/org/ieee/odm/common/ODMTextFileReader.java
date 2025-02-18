package org.ieee.odm.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.ieee.odm.adapter.psse.bean.PSSESchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	@Override
	public <T> T getJSon(Class<T> klass) throws ODMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(din, klass);
	}
}
