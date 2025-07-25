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
 * <p>Java class for InputFileVersionEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InputFileVersionEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="V26"/&gt;
 *     &lt;enumeration value="V30"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InputFileVersionEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum InputFileVersionEnumType {

    @XmlEnumValue("V26")
    V_26("V26"),
    @XmlEnumValue("V30")
    V_30("V30");
    private final String value;

    InputFileVersionEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InputFileVersionEnumType fromValue(String v) {
        for (InputFileVersionEnumType c: InputFileVersionEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
