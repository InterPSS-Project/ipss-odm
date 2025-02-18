
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StabilizerInputSignalEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StabilizerInputSignalEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="rotor_speed_deviation"/&gt;
 *     &lt;enumeration value="bus_frequency_deviation"/&gt;
 *     &lt;enumeration value="generator_electrical_power"/&gt;
 *     &lt;enumeration value="generator_accelerating_power"/&gt;
 *     &lt;enumeration value="bus_voltage"/&gt;
 *     &lt;enumeration value="deriavative_of_bus_voltage"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StabilizerInputSignalEnumType")
@XmlEnum
public enum StabilizerInputSignalEnumType {

    @XmlEnumValue("rotor_speed_deviation")
    ROTOR_SPEED_DEVIATION("rotor_speed_deviation"),
    @XmlEnumValue("bus_frequency_deviation")
    BUS_FREQUENCY_DEVIATION("bus_frequency_deviation"),
    @XmlEnumValue("generator_electrical_power")
    GENERATOR_ELECTRICAL_POWER("generator_electrical_power"),
    @XmlEnumValue("generator_accelerating_power")
    GENERATOR_ACCELERATING_POWER("generator_accelerating_power"),
    @XmlEnumValue("bus_voltage")
    BUS_VOLTAGE("bus_voltage"),
    @XmlEnumValue("deriavative_of_bus_voltage")
    DERIAVATIVE_OF_BUS_VOLTAGE("deriavative_of_bus_voltage");
    private final String value;

    StabilizerInputSignalEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StabilizerInputSignalEnumType fromValue(String v) {
        for (StabilizerInputSignalEnumType c: StabilizerInputSignalEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
