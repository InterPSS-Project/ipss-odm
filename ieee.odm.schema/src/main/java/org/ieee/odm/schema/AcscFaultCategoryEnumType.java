
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AcscFaultCategoryEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcscFaultCategoryEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Fault3Phase"/&gt;
 *     &lt;enumeration value="LineToLine"/&gt;
 *     &lt;enumeration value="LineToGround"/&gt;
 *     &lt;enumeration value="LineLineToGround"/&gt;
 *     &lt;enumeration value="Outage_3Phase"/&gt;
 *     &lt;enumeration value="Outage_1Phase"/&gt;
 *     &lt;enumeration value="Outage_2Phase"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AcscFaultCategoryEnumType")
@XmlEnum
public enum AcscFaultCategoryEnumType {

    @XmlEnumValue("Fault3Phase")
    FAULT_3_PHASE("Fault3Phase"),
    @XmlEnumValue("LineToLine")
    LINE_TO_LINE("LineToLine"),
    @XmlEnumValue("LineToGround")
    LINE_TO_GROUND("LineToGround"),
    @XmlEnumValue("LineLineToGround")
    LINE_LINE_TO_GROUND("LineLineToGround"),
    @XmlEnumValue("Outage_3Phase")
    OUTAGE_3_PHASE("Outage_3Phase"),
    @XmlEnumValue("Outage_1Phase")
    OUTAGE_1_PHASE("Outage_1Phase"),
    @XmlEnumValue("Outage_2Phase")
    OUTAGE_2_PHASE("Outage_2Phase");
    private final String value;

    AcscFaultCategoryEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AcscFaultCategoryEnumType fromValue(String v) {
        for (AcscFaultCategoryEnumType c: AcscFaultCategoryEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
