//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DcBusCodeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DcBusCodeEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Connection"/>
 *     &lt;enumeration value="Load"/>
 *     &lt;enumeration value="PowerSource"/>
 *     &lt;enumeration value="VoltageSource"/>
 *     &lt;enumeration value="PVModule"/>
 *     &lt;enumeration value="Inverter"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DcBusCodeEnumType")
@XmlEnum
public enum DcBusCodeEnumType {

    @XmlEnumValue("Connection")
    CONNECTION("Connection"),
    @XmlEnumValue("Load")
    LOAD("Load"),
    @XmlEnumValue("PowerSource")
    POWER_SOURCE("PowerSource"),
    @XmlEnumValue("VoltageSource")
    VOLTAGE_SOURCE("VoltageSource"),
    @XmlEnumValue("PVModule")
    PV_MODULE("PVModule"),
    @XmlEnumValue("Inverter")
    INVERTER("Inverter");
    private final String value;

    DcBusCodeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DcBusCodeEnumType fromValue(String v) {
        for (DcBusCodeEnumType c: DcBusCodeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
