
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OPFUnitCostXmlType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OPFUnitCostXmlType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="dollarPerMWh"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OPFUnitCostXmlType")
@XmlEnum
public enum OPFUnitCostXmlType {

    @XmlEnumValue("dollarPerMWh")
    DOLLAR_PER_M_WH("dollarPerMWh");
    private final String value;

    OPFUnitCostXmlType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OPFUnitCostXmlType fromValue(String v) {
        for (OPFUnitCostXmlType c: OPFUnitCostXmlType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
