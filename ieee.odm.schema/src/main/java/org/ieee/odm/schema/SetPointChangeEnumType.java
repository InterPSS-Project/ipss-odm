
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SetPointChangeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SetPointChangeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Absolute"/&gt;
 *     &lt;enumeration value="Delta"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SetPointChangeEnumType")
@XmlEnum
public enum SetPointChangeEnumType {

    @XmlEnumValue("Absolute")
    ABSOLUTE("Absolute"),
    @XmlEnumValue("Delta")
    DELTA("Delta");
    private final String value;

    SetPointChangeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SetPointChangeEnumType fromValue(String v) {
        for (SetPointChangeEnumType c: SetPointChangeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
