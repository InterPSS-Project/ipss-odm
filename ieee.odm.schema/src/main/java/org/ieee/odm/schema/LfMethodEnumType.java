
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LfMethodEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LfMethodEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NR"/&gt;
 *     &lt;enumeration value="PQ"/&gt;
 *     &lt;enumeration value="GS"/&gt;
 *     &lt;enumeration value="Custom"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LfMethodEnumType")
@XmlEnum
public enum LfMethodEnumType {

    NR("NR"),
    PQ("PQ"),
    GS("GS"),
    @XmlEnumValue("Custom")
    CUSTOM("Custom");
    private final String value;

    LfMethodEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LfMethodEnumType fromValue(String v) {
        for (LfMethodEnumType c: LfMethodEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
