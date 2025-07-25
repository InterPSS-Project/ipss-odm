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
 * <p>Java class for AdjustmentModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdjustmentModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ValueAdjustment"/&gt;
 *     &lt;enumeration value="RangeAdjustment"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AdjustmentModeEnumType")
@XmlEnum
public enum AdjustmentModeEnumType {

    @XmlEnumValue("ValueAdjustment")
    VALUE_ADJUSTMENT("ValueAdjustment"),
    @XmlEnumValue("RangeAdjustment")
    RANGE_ADJUSTMENT("RangeAdjustment");
    private final String value;

    AdjustmentModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AdjustmentModeEnumType fromValue(String v) {
        for (AdjustmentModeEnumType c: AdjustmentModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
