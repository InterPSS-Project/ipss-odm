
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnalysisCategoryEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AnalysisCategoryEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Loadflow"/&gt;
 *     &lt;enumeration value="Sensitivity"/&gt;
 *     &lt;enumeration value="ShortCircuit"/&gt;
 *     &lt;enumeration value="TransientStability"/&gt;
 *     &lt;enumeration value="OPF"/&gt;
 *     &lt;enumeration value="DistributionAnalysis"/&gt;
 *     &lt;enumeration value="DcSystemAnalysis"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AnalysisCategoryEnumType")
@XmlEnum
public enum AnalysisCategoryEnumType {

    @XmlEnumValue("Loadflow")
    LOADFLOW("Loadflow"),
    @XmlEnumValue("Sensitivity")
    SENSITIVITY("Sensitivity"),
    @XmlEnumValue("ShortCircuit")
    SHORT_CIRCUIT("ShortCircuit"),
    @XmlEnumValue("TransientStability")
    TRANSIENT_STABILITY("TransientStability"),
    OPF("OPF"),
    @XmlEnumValue("DistributionAnalysis")
    DISTRIBUTION_ANALYSIS("DistributionAnalysis"),
    @XmlEnumValue("DcSystemAnalysis")
    DC_SYSTEM_ANALYSIS("DcSystemAnalysis");
    private final String value;

    AnalysisCategoryEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AnalysisCategoryEnumType fromValue(String v) {
        for (AnalysisCategoryEnumType c: AnalysisCategoryEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
