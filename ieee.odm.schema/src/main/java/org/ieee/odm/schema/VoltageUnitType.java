
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VoltageUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VoltageUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PU"/&gt;
 *     &lt;enumeration value="VOLT"/&gt;
 *     &lt;enumeration value="KV"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VoltageUnitType")
@XmlEnum
public enum VoltageUnitType {

    PU,
    VOLT,
    KV;

    public String value() {
        return name();
    }

    public static VoltageUnitType fromValue(String v) {
        return valueOf(v);
    }

}
