
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SenAnalysisEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SenAnalysisEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PAngle"/&gt;
 *     &lt;enumeration value="QVoltage"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SenAnalysisEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum SenAnalysisEnumType {

    @XmlEnumValue("PAngle")
    P_ANGLE("PAngle"),
    @XmlEnumValue("QVoltage")
    Q_VOLTAGE("QVoltage");
    private final String value;

    SenAnalysisEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SenAnalysisEnumType fromValue(String v) {
        for (SenAnalysisEnumType c: SenAnalysisEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
