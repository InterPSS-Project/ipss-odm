
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LfResultFormatEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LfResultFormatEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Summary"/&gt;
 *     &lt;enumeration value="BusStyle"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LfResultFormatEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum LfResultFormatEnumType {

    @XmlEnumValue("Summary")
    SUMMARY("Summary"),
    @XmlEnumValue("BusStyle")
    BUS_STYLE("BusStyle");
    private final String value;

    LfResultFormatEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LfResultFormatEnumType fromValue(String v) {
        for (LfResultFormatEnumType c: LfResultFormatEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
