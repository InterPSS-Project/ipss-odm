
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TapAdjustmentEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TapAdjustmentEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Voltage"/&gt;
 *     &lt;enumeration value="MVarFlow"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TapAdjustmentEnumType")
@XmlEnum
public enum TapAdjustmentEnumType {

    @XmlEnumValue("Voltage")
    VOLTAGE("Voltage"),
    @XmlEnumValue("MVarFlow")
    M_VAR_FLOW("MVarFlow");
    private final String value;

    TapAdjustmentEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TapAdjustmentEnumType fromValue(String v) {
        for (TapAdjustmentEnumType c: TapAdjustmentEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
