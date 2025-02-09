package org.ieee.odm.psse.json;

import org.ieee.odm.adapter.psse.raw.PSSERawToRawxConverter;
import org.junit.Test;

public class PSSERawToRawxConverter_IEEE9_Test {
	
	@Test
	public void testCase1() throws Exception {
		
		PSSERawToRawxConverter conv = new PSSERawToRawxConverter();
		conv.convert("testdata/psse/json/raw/ieee9_v36.raw", "testdata/psse/json/ieee9_output.rawx");
		
	}
	
	//@Test
	public void testCase2() throws Exception {
		
		PSSERawToRawxConverter conv = new PSSERawToRawxConverter();
		conv.convert("testdata/psse/json/raw/sample.raw", "testdata/psse/json/sample_output.rawx");
		
	}

}
