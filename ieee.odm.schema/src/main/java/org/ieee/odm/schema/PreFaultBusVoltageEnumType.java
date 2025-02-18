
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreFaultBusVoltageEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PreFaultBusVoltageEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Loadflow"/&gt;
 *     &lt;enumeration value="UnitVolt"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreFaultBusVoltageEnumType")
@XmlEnum
public enum PreFaultBusVoltageEnumType {

    @XmlEnumValue("Loadflow")
    LOADFLOW("Loadflow"),
    @XmlEnumValue("UnitVolt")
    UNIT_VOLT("UnitVolt");
    private final String value;

    PreFaultBusVoltageEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PreFaultBusVoltageEnumType fromValue(String v) {
        for (PreFaultBusVoltageEnumType c: PreFaultBusVoltageEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
