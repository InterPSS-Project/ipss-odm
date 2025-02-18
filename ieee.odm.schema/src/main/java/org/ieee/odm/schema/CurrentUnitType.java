
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrentUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurrentUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PU"/&gt;
 *     &lt;enumeration value="AMP"/&gt;
 *     &lt;enumeration value="KA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CurrentUnitType")
@XmlEnum
public enum CurrentUnitType {

    PU,
    AMP,
    KA;

    public String value() {
        return name();
    }

    public static CurrentUnitType fromValue(String v) {
        return valueOf(v);
    }

}
