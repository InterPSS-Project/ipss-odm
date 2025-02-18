
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DynamicLoadEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DynamicLoadEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IEEEStaticLoad"/&gt;
 *     &lt;enumeration value="InductionMotor"/&gt;
 *     &lt;enumeration value="SinglePhaseACMotor"/&gt;
 *     &lt;enumeration value="ComplexLoad"/&gt;
 *     &lt;enumeration value="CompositeLoadModel"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DynamicLoadEnumType")
@XmlEnum
public enum DynamicLoadEnumType {

    @XmlEnumValue("IEEEStaticLoad")
    IEEE_STATIC_LOAD("IEEEStaticLoad"),
    @XmlEnumValue("InductionMotor")
    INDUCTION_MOTOR("InductionMotor"),
    @XmlEnumValue("SinglePhaseACMotor")
    SINGLE_PHASE_AC_MOTOR("SinglePhaseACMotor"),
    @XmlEnumValue("ComplexLoad")
    COMPLEX_LOAD("ComplexLoad"),
    @XmlEnumValue("CompositeLoadModel")
    COMPOSITE_LOAD_MODEL("CompositeLoadModel");
    private final String value;

    DynamicLoadEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DynamicLoadEnumType fromValue(String v) {
        for (DynamicLoadEnumType c: DynamicLoadEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
