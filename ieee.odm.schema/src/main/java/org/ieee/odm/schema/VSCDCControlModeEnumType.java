
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VSCDCControlModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VSCDCControlModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="blocked"/&gt;
 *     &lt;enumeration value="RealPower"/&gt;
 *     &lt;enumeration value="DCVoltage"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VSCDCControlModeEnumType")
@XmlEnum
public enum VSCDCControlModeEnumType {

    @XmlEnumValue("blocked")
    BLOCKED("blocked"),
    @XmlEnumValue("RealPower")
    REAL_POWER("RealPower"),
    @XmlEnumValue("DCVoltage")
    DC_VOLTAGE("DCVoltage");
    private final String value;

    VSCDCControlModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VSCDCControlModeEnumType fromValue(String v) {
        for (VSCDCControlModeEnumType c: VSCDCControlModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
