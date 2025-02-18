
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VSCACControlModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VSCACControlModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PowerFactor"/&gt;
 *     &lt;enumeration value="ReactivePower"/&gt;
 *     &lt;enumeration value="Voltage"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VSCACControlModeEnumType")
@XmlEnum
public enum VSCACControlModeEnumType {

    @XmlEnumValue("PowerFactor")
    POWER_FACTOR("PowerFactor"),
    @XmlEnumValue("ReactivePower")
    REACTIVE_POWER("ReactivePower"),
    @XmlEnumValue("Voltage")
    VOLTAGE("Voltage");
    private final String value;

    VSCACControlModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VSCACControlModeEnumType fromValue(String v) {
        for (VSCACControlModeEnumType c: VSCACControlModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
