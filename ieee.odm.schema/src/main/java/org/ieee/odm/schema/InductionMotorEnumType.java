
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InductionMotorEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InductionMotorEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PSSECIM5"/&gt;
 *     &lt;enumeration value="PSSECIM6"/&gt;
 *     &lt;enumeration value="MOTORW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InductionMotorEnumType")
@XmlEnum
public enum InductionMotorEnumType {

    @XmlEnumValue("PSSECIM5")
    PSSECIM_5("PSSECIM5"),
    @XmlEnumValue("PSSECIM6")
    PSSECIM_6("PSSECIM6"),
    MOTORW("MOTORW");
    private final String value;

    InductionMotorEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InductionMotorEnumType fromValue(String v) {
        for (InductionMotorEnumType c: InductionMotorEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
