
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SwitchedShuntModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SwitchedShuntModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Fixed"/&gt;
 *     &lt;enumeration value="Discrete"/&gt;
 *     &lt;enumeration value="Continuous"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SwitchedShuntModeEnumType")
@XmlEnum
public enum SwitchedShuntModeEnumType {

    @XmlEnumValue("Fixed")
    FIXED("Fixed"),
    @XmlEnumValue("Discrete")
    DISCRETE("Discrete"),
    @XmlEnumValue("Continuous")
    CONTINUOUS("Continuous");
    private final String value;

    SwitchedShuntModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SwitchedShuntModeEnumType fromValue(String v) {
        for (SwitchedShuntModeEnumType c: SwitchedShuntModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
