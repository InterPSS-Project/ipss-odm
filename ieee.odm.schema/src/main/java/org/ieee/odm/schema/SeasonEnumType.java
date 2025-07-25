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
 * <p>Java class for SeasonEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SeasonEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Winter"/&gt;
 *     &lt;enumeration value="Summer"/&gt;
 *     &lt;enumeration value="Spring"/&gt;
 *     &lt;enumeration value="Fall"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SeasonEnumType")
@XmlEnum
public enum SeasonEnumType {

    @XmlEnumValue("Winter")
    WINTER("Winter"),
    @XmlEnumValue("Summer")
    SUMMER("Summer"),
    @XmlEnumValue("Spring")
    SPRING("Spring"),
    @XmlEnumValue("Fall")
    FALL("Fall");
    private final String value;

    SeasonEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SeasonEnumType fromValue(String v) {
        for (SeasonEnumType c: SeasonEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
