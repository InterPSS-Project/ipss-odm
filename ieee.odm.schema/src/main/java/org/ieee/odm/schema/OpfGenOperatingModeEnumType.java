
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OpfGenOperatingModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OpfGenOperatingModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PVGenerator"/&gt;
 *     &lt;enumeration value="Pumping"/&gt;
 *     &lt;enumeration value="SychronousCompensator"/&gt;
 *     &lt;enumeration value="PQGenerator"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OpfGenOperatingModeEnumType")
@XmlEnum
public enum OpfGenOperatingModeEnumType {

    @XmlEnumValue("PVGenerator")
    PV_GENERATOR("PVGenerator"),
    @XmlEnumValue("Pumping")
    PUMPING("Pumping"),
    @XmlEnumValue("SychronousCompensator")
    SYCHRONOUS_COMPENSATOR("SychronousCompensator"),
    @XmlEnumValue("PQGenerator")
    PQ_GENERATOR("PQGenerator");
    private final String value;

    OpfGenOperatingModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OpfGenOperatingModeEnumType fromValue(String v) {
        for (OpfGenOperatingModeEnumType c: OpfGenOperatingModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
