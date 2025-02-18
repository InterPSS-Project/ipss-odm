
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusWindingEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BusWindingEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Primary"/&gt;
 *     &lt;enumeration value="Secondary"/&gt;
 *     &lt;enumeration value="Tertiary"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BusWindingEnumType")
@XmlEnum
public enum BusWindingEnumType {

    @XmlEnumValue("Primary")
    PRIMARY("Primary"),
    @XmlEnumValue("Secondary")
    SECONDARY("Secondary"),
    @XmlEnumValue("Tertiary")
    TERTIARY("Tertiary");
    private final String value;

    BusWindingEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BusWindingEnumType fromValue(String v) {
        for (BusWindingEnumType c: BusWindingEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
