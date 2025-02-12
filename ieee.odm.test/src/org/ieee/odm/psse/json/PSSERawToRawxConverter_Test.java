package org.ieee.odm.psse.json;

import org.ieee.odm.adapter.psse.raw.PSSERawToRawxConverter;
import org.junit.Test;

import com.google.gson.Gson;

public class PSSERawToRawxConverter_Test {
	
	@Test
	public void testIEEE9() throws Exception {
		
		PSSERawToRawxConverter conv = new PSSERawToRawxConverter();
		conv.convert("testdata/psse/json/raw/ieee9_v36.raw", "testdata/psse/json/ieee9_output.rawx");
		
	}
	
	@Test
	public void testConvSampleFile() throws Exception {
		
		PSSERawToRawxConverter conv = new PSSERawToRawxConverter();
		String jsonStr = conv.parseInputFile("testdata/psse/json/raw/sample.raw");
		System.out.println(jsonStr);
        
		conv.saveToRawxFile( "testdata/psse/json/sample_output.rawx");
		
	}

}
