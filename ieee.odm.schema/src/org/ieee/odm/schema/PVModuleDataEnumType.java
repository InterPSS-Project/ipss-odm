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
 * <p>Java class for PVModuleDataEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PVModuleDataEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Points"/>
 *     &lt;enumeration value="Function"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PVModuleDataEnumType")
@XmlEnum
public enum PVModuleDataEnumType {

    @XmlEnumValue("Points")
    POINTS("Points"),
    @XmlEnumValue("Function")
    FUNCTION("Function");
    private final String value;

    PVModuleDataEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PVModuleDataEnumType fromValue(String v) {
        for (PVModuleDataEnumType c: PVModuleDataEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
