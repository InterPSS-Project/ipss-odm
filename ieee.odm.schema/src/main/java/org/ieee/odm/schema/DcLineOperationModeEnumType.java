
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DcLineOperationModeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DcLineOperationModeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="double"/&gt;
 *     &lt;enumeration value="single"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DcLineOperationModeEnumType")
@XmlEnum
public enum DcLineOperationModeEnumType {

    @XmlEnumValue("double")
    DOUBLE("double"),
    @XmlEnumValue("single")
    SINGLE("single");
    private final String value;

    DcLineOperationModeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DcLineOperationModeEnumType fromValue(String v) {
        for (DcLineOperationModeEnumType c: DcLineOperationModeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
