
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivePowerUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActivePowerUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PU"/&gt;
 *     &lt;enumeration value="W"/&gt;
 *     &lt;enumeration value="KW"/&gt;
 *     &lt;enumeration value="MW"/&gt;
 *     &lt;enumeration value="HP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ActivePowerUnitType")
@XmlEnum
public enum ActivePowerUnitType {

    PU,
    W,
    KW,
    MW,
    HP;

    public String value() {
        return name();
    }

    public static ActivePowerUnitType fromValue(String v) {
        return valueOf(v);
    }

}
