//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ZUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PU"/&gt;
 *     &lt;enumeration value="VA"/&gt;
 *     &lt;enumeration value="KVA"/&gt;
 *     &lt;enumeration value="MVA"/&gt;
 *     &lt;enumeration value="OHM"/&gt;
 *     &lt;enumeration value="Percent"/&gt;
 *     &lt;enumeration value="OHMPerMile"/&gt;
 *     &lt;enumeration value="OHMPerFt"/&gt;
 *     &lt;enumeration value="OHMPerKM"/&gt;
 *     &lt;enumeration value="OHMPerM"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ZUnitType")
@XmlEnum
public enum ZUnitType {

    PU("PU"),
    VA("VA"),
    KVA("KVA"),
    MVA("MVA"),
    OHM("OHM"),
    @XmlEnumValue("Percent")
    PERCENT("Percent"),
    @XmlEnumValue("OHMPerMile")
    OHM_PER_MILE("OHMPerMile"),
    @XmlEnumValue("OHMPerFt")
    OHM_PER_FT("OHMPerFt"),
    @XmlEnumValue("OHMPerKM")
    OHM_PER_KM("OHMPerKM"),
    @XmlEnumValue("OHMPerM")
    OHM_PER_M("OHMPerM");
    private final String value;

    ZUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ZUnitType fromValue(String v) {
        for (ZUnitType c: ZUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
