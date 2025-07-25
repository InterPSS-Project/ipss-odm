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
 * <p>Java class for TransimissionEquiptmentEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransimissionEquiptmentEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Capacitor"/&gt;
 *     &lt;enumeration value="Breaker"/&gt;
 *     &lt;enumeration value="Shunt"/&gt;
 *     &lt;enumeration value="Gen"/&gt;
 *     &lt;enumeration value="Load"/&gt;
 *     &lt;enumeration value="Line"/&gt;
 *     &lt;enumeration value="Transformer"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TransimissionEquiptmentEnumType")
@XmlEnum
public enum TransimissionEquiptmentEnumType {

    @XmlEnumValue("Capacitor")
    CAPACITOR("Capacitor"),
    @XmlEnumValue("Breaker")
    BREAKER("Breaker"),
    @XmlEnumValue("Shunt")
    SHUNT("Shunt"),
    @XmlEnumValue("Gen")
    GEN("Gen"),
    @XmlEnumValue("Load")
    LOAD("Load"),
    @XmlEnumValue("Line")
    LINE("Line"),
    @XmlEnumValue("Transformer")
    TRANSFORMER("Transformer");
    private final String value;

    TransimissionEquiptmentEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransimissionEquiptmentEnumType fromValue(String v) {
        for (TransimissionEquiptmentEnumType c: TransimissionEquiptmentEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
