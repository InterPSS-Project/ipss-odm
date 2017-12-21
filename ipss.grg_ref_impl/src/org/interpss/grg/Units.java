
package org.interpss.grg;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "voltage",
    "current",
    "angle",
    "active_power",
    "reactive_power",
    "impedance",
    "resistance",
    "reactance",
    "conductance",
    "susceptance",
    "time"
})
public class Units {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voltage")
    private Units.Voltage voltage;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("current")
    private Units.Current current;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("angle")
    private Units.Angle angle;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active_power")
    private Units.ActivePower activePower;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactive_power")
    private Units.ReactivePower reactivePower;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("impedance")
    private Units.Impedance impedance;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("resistance")
    private Units.Resistance resistance;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactance")
    private Units.Reactance reactance;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("conductance")
    private Units.Conductance conductance;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("susceptance")
    private Units.Susceptance susceptance;
    @JsonProperty("time")
    private Units.Time time;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voltage")
    public Units.Voltage getVoltage() {
        return voltage;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("voltage")
    public void setVoltage(Units.Voltage voltage) {
        this.voltage = voltage;
    }

    public Units withVoltage(Units.Voltage voltage) {
        this.voltage = voltage;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("current")
    public Units.Current getCurrent() {
        return current;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("current")
    public void setCurrent(Units.Current current) {
        this.current = current;
    }

    public Units withCurrent(Units.Current current) {
        this.current = current;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("angle")
    public Units.Angle getAngle() {
        return angle;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("angle")
    public void setAngle(Units.Angle angle) {
        this.angle = angle;
    }

    public Units withAngle(Units.Angle angle) {
        this.angle = angle;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active_power")
    public Units.ActivePower getActivePower() {
        return activePower;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active_power")
    public void setActivePower(Units.ActivePower activePower) {
        this.activePower = activePower;
    }

    public Units withActivePower(Units.ActivePower activePower) {
        this.activePower = activePower;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactive_power")
    public Units.ReactivePower getReactivePower() {
        return reactivePower;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactive_power")
    public void setReactivePower(Units.ReactivePower reactivePower) {
        this.reactivePower = reactivePower;
    }

    public Units withReactivePower(Units.ReactivePower reactivePower) {
        this.reactivePower = reactivePower;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("impedance")
    public Units.Impedance getImpedance() {
        return impedance;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("impedance")
    public void setImpedance(Units.Impedance impedance) {
        this.impedance = impedance;
    }

    public Units withImpedance(Units.Impedance impedance) {
        this.impedance = impedance;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("resistance")
    public Units.Resistance getResistance() {
        return resistance;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("resistance")
    public void setResistance(Units.Resistance resistance) {
        this.resistance = resistance;
    }

    public Units withResistance(Units.Resistance resistance) {
        this.resistance = resistance;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactance")
    public Units.Reactance getReactance() {
        return reactance;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("reactance")
    public void setReactance(Units.Reactance reactance) {
        this.reactance = reactance;
    }

    public Units withReactance(Units.Reactance reactance) {
        this.reactance = reactance;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("conductance")
    public Units.Conductance getConductance() {
        return conductance;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("conductance")
    public void setConductance(Units.Conductance conductance) {
        this.conductance = conductance;
    }

    public Units withConductance(Units.Conductance conductance) {
        this.conductance = conductance;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("susceptance")
    public Units.Susceptance getSusceptance() {
        return susceptance;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("susceptance")
    public void setSusceptance(Units.Susceptance susceptance) {
        this.susceptance = susceptance;
    }

    public Units withSusceptance(Units.Susceptance susceptance) {
        this.susceptance = susceptance;
        return this;
    }

    @JsonProperty("time")
    public Units.Time getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Units.Time time) {
        this.time = time;
    }

    public Units withTime(Units.Time time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Units withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(voltage).append(current).append(angle).append(activePower).append(reactivePower).append(impedance).append(resistance).append(reactance).append(conductance).append(susceptance).append(time).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Units) == false) {
            return false;
        }
        Units rhs = ((Units) other);
        return new EqualsBuilder().append(voltage, rhs.voltage).append(current, rhs.current).append(angle, rhs.angle).append(activePower, rhs.activePower).append(reactivePower, rhs.reactivePower).append(impedance, rhs.impedance).append(resistance, rhs.resistance).append(reactance, rhs.reactance).append(conductance, rhs.conductance).append(susceptance, rhs.susceptance).append(time, rhs.time).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public enum ActivePower {

        WATT("watt"),
        KILO_WATT("kilo_watt"),
        MEGA_WATT("mega_watt"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.ActivePower> CONSTANTS = new HashMap<String, Units.ActivePower>();

        static {
            for (Units.ActivePower c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ActivePower(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.ActivePower fromValue(String value) {
            Units.ActivePower constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Angle {

        DEGREE("degree"),
        RADIAN("radian");
        private final String value;
        private final static Map<String, Units.Angle> CONSTANTS = new HashMap<String, Units.Angle>();

        static {
            for (Units.Angle c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Angle(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Angle fromValue(String value) {
            Units.Angle constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Conductance {

        SIEMENS("siemens"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Conductance> CONSTANTS = new HashMap<String, Units.Conductance>();

        static {
            for (Units.Conductance c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Conductance(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Conductance fromValue(String value) {
            Units.Conductance constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Current {

        AMPERE("ampere"),
        KILO_AMPERE("kilo_ampere"),
        MEGA_AMPERE("mega_ampere"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Current> CONSTANTS = new HashMap<String, Units.Current>();

        static {
            for (Units.Current c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Current(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Current fromValue(String value) {
            Units.Current constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Impedance {

        OHM("ohm"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Impedance> CONSTANTS = new HashMap<String, Units.Impedance>();

        static {
            for (Units.Impedance c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Impedance(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Impedance fromValue(String value) {
            Units.Impedance constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Reactance {

        OHM("ohm"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Reactance> CONSTANTS = new HashMap<String, Units.Reactance>();

        static {
            for (Units.Reactance c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Reactance(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Reactance fromValue(String value) {
            Units.Reactance constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ReactivePower {

        VOLT_AMPERE_REACTIVE("volt_ampere_reactive"),
        MEGA_VOLT_AMPRE_REACTIVE("mega_volt_ampre_reactive"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.ReactivePower> CONSTANTS = new HashMap<String, Units.ReactivePower>();

        static {
            for (Units.ReactivePower c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ReactivePower(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.ReactivePower fromValue(String value) {
            Units.ReactivePower constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Resistance {

        OHM("ohm"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Resistance> CONSTANTS = new HashMap<String, Units.Resistance>();

        static {
            for (Units.Resistance c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Resistance(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Resistance fromValue(String value) {
            Units.Resistance constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Susceptance {

        SIEMENS("siemens"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Susceptance> CONSTANTS = new HashMap<String, Units.Susceptance>();

        static {
            for (Units.Susceptance c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Susceptance(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Susceptance fromValue(String value) {
            Units.Susceptance constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Time {

        SECONDS("seconds"),
        MINUTES("minutes"),
        HOURS("hours"),
        DAYS("days"),
        MONTHS("months"),
        YEARS("years");
        private final String value;
        private final static Map<String, Units.Time> CONSTANTS = new HashMap<String, Units.Time>();

        static {
            for (Units.Time c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Time(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Time fromValue(String value) {
            Units.Time constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Voltage {

        VOLT("volt"),
        KILO_VOLT("kilo_volt"),
        MEGA_VOLT("mega_volt"),
        PU("pu");
        private final String value;
        private final static Map<String, Units.Voltage> CONSTANTS = new HashMap<String, Units.Voltage>();

        static {
            for (Units.Voltage c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Voltage(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Units.Voltage fromValue(String value) {
            Units.Voltage constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
