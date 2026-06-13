package org.ieee.odm.psse.raw;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.ieee.odm.adapter.psse.PSSEAdapter;
import org.ieee.odm.adapter.psse.PSSEAdapter.PsseVersion;
import org.junit.Test;

public class PSSEHeaderVersionParserTest {
	@Test
	public void parsesRevisionWithPsseCommentSuffix() throws Exception {
		Path raw = Files.createTempFile("Texas7k_testheader", ".RAW");
		try {
			Files.writeString(raw, """
					0, 100.0, 30 / PSS(tm)E-30 RAW created Fri
					Random title line
					Random comment line
					""");

			assertEquals(PsseVersion.PSSE_30, PSSEAdapter.parsePsseVersion(raw.toString()));
		}
		finally {
			Files.deleteIfExists(raw);
		}
	}
}
