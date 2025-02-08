package org.ieee.odm.pwd;

import static org.junit.Assert.assertTrue;

import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.common.ODMException;
import org.junit.Test;

public class LineStringParserTest {
	@Test
	public void test() throws ODMException {
		InputLineStringParser parser = new InputLineStringParser();
		
		parser.parseMetadata("[A, B, C, A:1, D]");
		
		parser.parseData("1 2 C 4 5");

		//System.out.println(parser.toString());
		
		assertTrue(parser.getDouble("A") == 1.0);
		assertTrue(parser.getValue("C").equals("C"));
		assertTrue(parser.exist("A:1"));
		assertTrue(!parser.exist("A:2"));
	}

	@Test
	public void multi_line_test() throws ODMException {
		InputLineStringParser parser = new InputLineStringParser();
		
		parser.parseMetadata("[A, B, C ,A:1, D]");
		
		// parseData() return false, since more data is expected
		assertTrue(parser.parseData("1 2") == false);

		assertTrue(parser.parseData("C 4 5",true) == true);

		//System.out.println(parser.toString());
		
		assertTrue(parser.getDouble("A") == 1.0);
		assertTrue(parser.getValue("C").equals("C"));
		assertTrue(parser.exist("A:1"));
		assertTrue(!parser.exist("A:2"));
	}
}
