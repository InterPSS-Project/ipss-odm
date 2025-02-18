
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BreakerOverrideEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BreakerOverrideEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CB"/&gt;
 *     &lt;enumeration value="DIS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BreakerOverrideEnumType")
@XmlEnum
public enum BreakerOverrideEnumType {

    CB,
    DIS;

    public String value() {
        return name();
    }

    public static BreakerOverrideEnumType fromValue(String v) {
        return valueOf(v);
    }

}
