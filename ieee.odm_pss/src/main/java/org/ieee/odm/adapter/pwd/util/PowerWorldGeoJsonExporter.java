package org.ieee.odm.adapter.pwd.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ieee.odm.adapter.pwd.AbstractPowerWorldAdapter.RecType;
import org.ieee.odm.adapter.pwd.InputLineStringParser;
import org.ieee.odm.adapter.pwd.impl.PWDHelper;
import org.ieee.odm.common.ODMException;

/**
 * Extracts PowerWorld AUX bus/substation geographic data into the dictionary JSON
 * structure used by the InterPSS desktop examples.
 */
public final class PowerWorldGeoJsonExporter {
	private PowerWorldGeoJsonExporter() {
	}

	public static GeoDictionary parse(Path auxFile) throws IOException, ODMException {
		return parse(auxFile, StandardCharsets.UTF_8);
	}

	public static GeoDictionary parse(Path auxFile, Charset charset) throws IOException, ODMException {
		GeoDictionary dictionary = new GeoDictionary();
		InputLineStringParser parser = new InputLineStringParser();
		RecType recordType = RecType.Undefined;

		try (BufferedReader reader = Files.newBufferedReader(auxFile, charset)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String str = line.trim();
				if (str.isEmpty() || str.startsWith("//")) {
					continue;
				}

				if (str.toUpperCase().startsWith("DATA")) {
					String metadata = str;
					while (!PWDHelper.isArgumentFieldsCompleted(metadata)) {
						String next = reader.readLine();
						if (next == null) {
							throw new ODMException("Incomplete PowerWorld AUX metadata: " + metadata);
						}
						metadata += next.trim();
					}
					recordType = PWDHelper.getDataType(metadata);
					if (recordType == RecType.SUBSTATION || recordType == RecType.BUS) {
						parser.parseMetadata(metadata);
					}
					continue;
				}

				if (str.startsWith("{") || str.startsWith("}")) {
					continue;
				}

				if (recordType == RecType.SUBSTATION) {
					if (!parser.parseData(str)) {
						throw new ODMException("Incomplete PowerWorld Substation record: " + str);
					}
					dictionary.addSubstation(toSubstation(parser));
				}
				else if (recordType == RecType.BUS) {
					if (!parser.parseData(str)) {
						throw new ODMException("Incomplete PowerWorld Bus record: " + str);
					}
					dictionary.addBus(toBus(parser));
				}
			}
		}

		dictionary.resolveBusCoordinatesFromSubstations();
		return dictionary;
	}

	public static void writeJson(Path auxFile, Path jsonFile) throws IOException, ODMException {
		writeJson(auxFile, jsonFile, StandardCharsets.UTF_8);
	}

	public static void writeJson(Path auxFile, Path jsonFile, Charset charset) throws IOException, ODMException {
		GeoDictionary dictionary = parse(auxFile, charset);
		try (BufferedWriter writer = Files.newBufferedWriter(jsonFile, StandardCharsets.UTF_8)) {
			writer.write(dictionary.toJson());
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2 && args.length != 3) {
			System.err.println("Usage: PowerWorldGeoJsonExporter <input.aux> <output.json> [charset]");
			System.exit(2);
		}
		Charset charset = args.length == 3 ? Charset.forName(args[2]) : StandardCharsets.UTF_8;
		writeJson(Path.of(args[0]), Path.of(args[1]), charset);
	}

	private static SubstationGeo toSubstation(InputLineStringParser parser) throws ODMException {
		SubstationGeo substation = new SubstationGeo();
		substation.number = getInt(parser, "SubNum", 0);
		substation.name = getString(parser, "SubName");
		substation.subId = getString(parser, "SubID");
		substation.latitude = getDouble(parser, "Latitude");
		substation.longitude = getDouble(parser, "Longitude");
		substation.areaNumber = getInt(parser, "AreaNum", 0);
		substation.areaName = getString(parser, "AreaName");
		return substation;
	}

	private static BusGeo toBus(InputLineStringParser parser) throws ODMException {
		BusGeo bus = new BusGeo();
		bus.number = getLong(parser, "BusNum", 0);
		bus.name = getString(parser, "BusName");
		bus.nominalKv = getDouble(parser, "BusNomVolt");
		bus.areaNumber = getInt(parser, "AreaNum", 0);
		bus.zoneNumber = getInt(parser, "ZoneNum", 0);
		bus.substationNumber = getInt(parser, "SubNum", 0);
		bus.latitude = getDouble(parser, "Latitude");
		bus.longitude = getDouble(parser, "Longitude");
		return bus;
	}

	private static String getString(InputLineStringParser parser, String fieldName) throws ODMException {
		return parser.exist(fieldName) ? parser.getValue(fieldName).trim() : "";
	}

	private static Integer getInt(InputLineStringParser parser, String fieldName, int defaultValue) throws ODMException {
		String value = getString(parser, fieldName);
		return value.isEmpty() ? defaultValue : Double.valueOf(value).intValue();
	}

	private static Long getLong(InputLineStringParser parser, String fieldName, long defaultValue) throws ODMException {
		String value = getString(parser, fieldName);
		return value.isEmpty() ? defaultValue : Long.valueOf(value);
	}

	private static Double getDouble(InputLineStringParser parser, String fieldName) throws ODMException {
		String value = getString(parser, fieldName);
		return value.isEmpty() ? null : Double.valueOf(value);
	}

	public static final class GeoDictionary {
		private final Map<Integer, SubstationGeo> substations = new LinkedHashMap<>();
		private final Map<Long, BusGeo> buses = new LinkedHashMap<>();

		public Map<Integer, SubstationGeo> getSubstations() {
			return substations;
		}

		public Map<Long, BusGeo> getBuses() {
			return buses;
		}

		private void addSubstation(SubstationGeo substation) {
			substations.put(substation.number, substation);
		}

		private void addBus(BusGeo bus) {
			buses.put(bus.number, bus);
		}

		private void resolveBusCoordinatesFromSubstations() {
			for (BusGeo bus : buses.values()) {
				SubstationGeo substation = substations.get(bus.substationNumber);
				if (substation == null) {
					continue;
				}
				bus.substationName = substation.name;
				if (bus.latitude == null) {
					bus.latitude = substation.latitude;
				}
				if (bus.longitude == null) {
					bus.longitude = substation.longitude;
				}
			}
		}

		public String toJson() {
			StringBuilder json = new StringBuilder();
			json.append("{\n");
			json.append("  \"message\": [\n");
			json.append("    {\n");
			json.append("      \"id\": 14,\n");
			json.append("      \"name\": \"tcmDictionary\",\n");
			json.append("      \"description\": \"Send by a client requesting the case dictionary, or by a server in response.\"\n");
			json.append("    }\n");
			json.append("  ],\n");
			json.append("  \"content\": {\n");
			json.append("    \"type\": \"dsmDictionary\",\n");
			json.append("    \"ObjectType Count\": 2,\n");
			appendSubstations(json);
			json.append(",\n");
			appendBuses(json);
			json.append("\n");
			json.append("  }\n");
			json.append("}\n");
			return json.toString();
		}

		private void appendSubstations(StringBuilder json) {
			json.append("    \"Substation\": {\n");
			int count = 0;
			for (SubstationGeo substation : substations.values()) {
				if (count++ > 0) {
					json.append(",\n");
				}
				json.append("      \"").append(substation.number).append("\": {\n");
				appendNumber(json, "Int.Number", substation.number, 8, true);
				appendNumber(json, "Double.Latitude", substation.latitude, 8, true);
				appendNumber(json, "Double.Longitude", substation.longitude, 8, true);
				appendString(json, "String.Name", substation.name, 8, true);
				appendString(json, "String.SubID", substation.subId, 8, false);
				json.append("\n      }");
			}
			json.append("\n    }");
		}

		private void appendBuses(StringBuilder json) {
			json.append("    \"Bus\": {\n");
			int count = 0;
			for (BusGeo bus : buses.values()) {
				if (count++ > 0) {
					json.append(",\n");
				}
				json.append("      \"").append(bus.number).append("\": {\n");
				appendNumber(json, "Int.Bus Number", bus.number, 8, true);
				appendNumber(json, "Int.Area Number", bus.areaNumber, 8, true);
				appendNumber(json, "Int.Zone Number", bus.zoneNumber, 8, true);
				appendNumber(json, "Int.Sub Number", bus.substationNumber, 8, true);
				appendNumber(json, "Single.Nominal kV", bus.nominalKv, 8, true);
				appendNumber(json, "Double.Latitude", bus.latitude, 8, true);
				appendNumber(json, "Double.Longitude", bus.longitude, 8, true);
				appendString(json, "String.Name", bus.name, 8, true);
				appendString(json, "String.Substation Name", bus.substationName, 8, false);
				json.append("\n      }");
			}
			json.append("\n    }");
		}
	}

	public static final class SubstationGeo {
		private int number;
		private String name;
		private String subId;
		private Double latitude;
		private Double longitude;
		private int areaNumber;
		private String areaName;

		public int getNumber() {
			return number;
		}

		public String getName() {
			return name;
		}

		public String getSubId() {
			return subId;
		}

		public Double getLatitude() {
			return latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public int getAreaNumber() {
			return areaNumber;
		}

		public String getAreaName() {
			return areaName;
		}
	}

	public static final class BusGeo {
		private long number;
		private String name;
		private Double nominalKv;
		private int areaNumber;
		private int zoneNumber;
		private int substationNumber;
		private Double latitude;
		private Double longitude;
		private String substationName = "";

		public long getNumber() {
			return number;
		}

		public String getName() {
			return name;
		}

		public Double getNominalKv() {
			return nominalKv;
		}

		public int getAreaNumber() {
			return areaNumber;
		}

		public int getZoneNumber() {
			return zoneNumber;
		}

		public int getSubstationNumber() {
			return substationNumber;
		}

		public Double getLatitude() {
			return latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public String getSubstationName() {
			return substationName;
		}
	}

	private static void appendString(StringBuilder json, String name, String value, int spaces, boolean comma) {
		indent(json, spaces);
		json.append("\"").append(escape(name)).append("\": ");
		if (value == null) {
			json.append("null");
		}
		else {
			json.append("\"").append(escape(value)).append("\"");
		}
		if (comma) {
			json.append(",");
		}
		json.append("\n");
	}

	private static void appendNumber(StringBuilder json, String name, Number value, int spaces, boolean comma) {
		indent(json, spaces);
		json.append("\"").append(escape(name)).append("\": ");
		json.append(value == null ? "null" : value);
		if (comma) {
			json.append(",");
		}
		json.append("\n");
	}

	private static void indent(StringBuilder json, int spaces) {
		for (int i = 0; i < spaces; i++) {
			json.append(' ');
		}
	}

	private static String escape(String value) {
		StringBuilder escaped = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			switch (ch) {
			case '\\':
				escaped.append("\\\\");
				break;
			case '"':
				escaped.append("\\\"");
				break;
			case '\b':
				escaped.append("\\b");
				break;
			case '\f':
				escaped.append("\\f");
				break;
			case '\n':
				escaped.append("\\n");
				break;
			case '\r':
				escaped.append("\\r");
				break;
			case '\t':
				escaped.append("\\t");
				break;
			default:
				if (ch < 0x20) {
					escaped.append(String.format("\\u%04x", (int) ch));
				}
				else {
					escaped.append(ch);
				}
				break;
			}
		}
		return escaped.toString();
	}
}
