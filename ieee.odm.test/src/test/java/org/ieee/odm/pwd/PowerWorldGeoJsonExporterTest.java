package org.ieee.odm.pwd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;

import org.ieee.odm.adapter.pwd.util.PowerWorldGeoJsonExporter;
import org.ieee.odm.adapter.pwd.util.PowerWorldGeoJsonExporter.BusGeo;
import org.ieee.odm.adapter.pwd.util.PowerWorldGeoJsonExporter.GeoDictionary;
import org.ieee.odm.adapter.pwd.util.PowerWorldGeoJsonExporter.SubstationGeo;
import org.junit.Test;

public class PowerWorldGeoJsonExporterTest {
	@Test
	public void exportsSubstationAndBusGeoJson() throws Exception {
		GeoDictionary dictionary = PowerWorldGeoJsonExporter.parse(Path.of("testdata/pwd/titlecase_substation.AUX"));

		assertEquals(2, dictionary.getSubstations().size());
		assertEquals(2, dictionary.getBuses().size());

		SubstationGeo substation = dictionary.getSubstations().get(1);
		assertEquals("EDNA 1", substation.getName());
		assertEquals(29.198, substation.getLatitude(), 1.0e-6);
		assertEquals(-96.6616, substation.getLongitude(), 1.0e-6);

		BusGeo bus = dictionary.getBuses().get(110001L);
		assertEquals(1, bus.getSubstationNumber());
		assertEquals("EDNA 1", bus.getSubstationName());
		assertEquals(29.198, bus.getLatitude(), 1.0e-6);
		assertEquals(-96.6616, bus.getLongitude(), 1.0e-6);

		String json = dictionary.toJson();
		assertTrue(json.contains("\"Substation\""));
		assertTrue(json.contains("\"Bus\""));
		assertTrue(json.contains("\"Double.Latitude\": 29.198"));
		assertTrue(json.contains("\"Double.Longitude\": -96.6616"));
		assertTrue(json.contains("\"String.Substation Name\": \"EDNA 1\""));
	}
}
