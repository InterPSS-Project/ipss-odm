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
 * <p>Java class for LfMethodEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LfMethodEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NR"/>
 *     &lt;enumeration value="PQ"/>
 *     &lt;enumeration value="GS"/>
 *     &lt;enumeration value="Custom"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LfMethodEnumType")
@XmlEnum
public enum LfMethodEnumType {

    NR("NR"),
    PQ("PQ"),
    GS("GS"),
    @XmlEnumValue("Custom")
    CUSTOM("Custom");
    private final String value;

    LfMethodEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LfMethodEnumType fromValue(String v) {
        for (LfMethodEnumType c: LfMethodEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
