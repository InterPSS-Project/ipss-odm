
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShortCircuitBusEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ShortCircuitBusEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Contributing"/&gt;
 *     &lt;enumeration value="Non-contributing"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ShortCircuitBusEnumType")
@XmlEnum
public enum ShortCircuitBusEnumType {

    @XmlEnumValue("Contributing")
    CONTRIBUTING("Contributing"),
    @XmlEnumValue("Non-contributing")
    NON_CONTRIBUTING("Non-contributing");
    private final String value;

    ShortCircuitBusEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ShortCircuitBusEnumType fromValue(String v) {
        for (ShortCircuitBusEnumType c: ShortCircuitBusEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
