
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XformrtConnectionEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="XformrtConnectionEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Wye"/&gt;
 *     &lt;enumeration value="Delta"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "XformrtConnectionEnumType")
@XmlEnum
public enum XformrtConnectionEnumType {

    @XmlEnumValue("Wye")
    WYE("Wye"),
    @XmlEnumValue("Delta")
    DELTA("Delta");
    private final String value;

    XformrtConnectionEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XformrtConnectionEnumType fromValue(String v) {
        for (XformrtConnectionEnumType c: XformrtConnectionEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
