/*
 * @(#)PSSEAdapter.java   
 *
 * Copyright (C) 2006-2009 www.interpss.org
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
 * @Date 02/11/2008
 * 
 *   Revision History
 *   ================
 *
 */
package org.ieee.odm.adapter.psse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.ieee.odm.adapter.AbstractODMAdapter;
import org.ieee.odm.common.ODMException;

/**
 * ODM adapter for PSS/E input format, including Aclf, Acsc and DStab files. This is a facet class. 
 * The actual adapter implementation is located in the ~/impl/ dir.
 * 
 * @author mzhou
 *
 */
public abstract class PSSEAdapter extends AbstractODMAdapter {
	/**
	 *  ODM PSS/E adapter version  
	 */
	public static enum PsseVersion {
		PSSE_29, PSSE_30, PSSE_31, PSSE_32, PSSE_33, PSSE_34, PSSE_35, PSSE_36, PSSE_JSON
	}

	/**
	 * Parses the PSS/E RAW header revision field.
	 * <p>
	 * Some exported RAW files include descriptive text after the REV value, for
	 * example {@code 30 / PSS(tm)E-30 RAW created ...}. PSS/E treats the leading
	 * integer as the revision; this parser does the same.
	 *
	 * @param filename the path to the PSS/E RAW file
	 * @return the corresponding adapter version
	 * @throws ODMException if the header cannot be read or the REV field is invalid
	 */
	public static PsseVersion parsePsseVersion(String filename) throws ODMException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.startsWith("@") && !line.isEmpty()) {
					break;
				}
			}
			if (line == null) {
				throw new ODMException("Unable to read header information from PSSE file");
			}
			String[] parts = line.split(",");
			if (parts.length < 3) {
				throw new ODMException("Invalid PSSE file format: insufficient fields in header line");
			}
			String revStr = parts[2].trim();
			StringBuilder digits = new StringBuilder();
			for (int i = 0; i < revStr.length(); i++) {
				char ch = revStr.charAt(i);
				if (Character.isDigit(ch)) {
					digits.append(ch);
				}
				else if (!digits.isEmpty()) {
					break;
				}
			}
			if (digits.isEmpty()) {
				throw new ODMException("Invalid REV format in PSSE file: " + revStr);
			}
			return psseVersion(Integer.parseInt(digits.toString()));
		} catch (IOException e) {
			throw new ODMException("Error reading PSSE file: " + filename + " - " + e.getMessage());
		}
	}

	private static PsseVersion psseVersion(int version) {
		switch (version) {
			case 29: return PsseVersion.PSSE_29;
			case 30: return PsseVersion.PSSE_30;
			case 31: return PsseVersion.PSSE_31;
			case 32: return PsseVersion.PSSE_32;
			case 33: return PsseVersion.PSSE_33;
			case 34: return PsseVersion.PSSE_34;
			case 35: return PsseVersion.PSSE_35;
			case 36: return PsseVersion.PSSE_36;
			default: return PsseVersion.PSSE_36;
		}
	}

	protected PsseVersion adptrVersion;
	
	public PSSEAdapter(PsseVersion ver) {
		super();
		this.adptrVersion = ver;
	
	}
}
