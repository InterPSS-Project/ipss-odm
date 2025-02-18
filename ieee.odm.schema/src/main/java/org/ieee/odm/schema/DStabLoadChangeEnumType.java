
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DStabLoadChangeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DStabLoadChangeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LowFrequency"/&gt;
 *     &lt;enumeration value="LowVoltage"/&gt;
 *     &lt;enumeration value="FixedTime"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DStabLoadChangeEnumType")
@XmlEnum
public enum DStabLoadChangeEnumType {

    @XmlEnumValue("LowFrequency")
    LOW_FREQUENCY("LowFrequency"),
    @XmlEnumValue("LowVoltage")
    LOW_VOLTAGE("LowVoltage"),
    @XmlEnumValue("FixedTime")
    FIXED_TIME("FixedTime");
    private final String value;

    DStabLoadChangeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DStabLoadChangeEnumType fromValue(String v) {
        for (DStabLoadChangeEnumType c: DStabLoadChangeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
