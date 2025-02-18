
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReactivePowerUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReactivePowerUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PU"/&gt;
 *     &lt;enumeration value="VAR"/&gt;
 *     &lt;enumeration value="KVAR"/&gt;
 *     &lt;enumeration value="MVAR"/&gt;
 *     &lt;enumeration value="Ohm"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ReactivePowerUnitType")
@XmlEnum
public enum ReactivePowerUnitType {

    PU("PU"),
    VAR("VAR"),
    KVAR("KVAR"),
    MVAR("MVAR"),
    @XmlEnumValue("Ohm")
    OHM("Ohm");
    private final String value;

    ReactivePowerUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReactivePowerUnitType fromValue(String v) {
        for (ReactivePowerUnitType c: ReactivePowerUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
