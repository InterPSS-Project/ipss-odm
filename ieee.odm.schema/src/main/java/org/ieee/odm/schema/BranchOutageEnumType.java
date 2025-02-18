
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BranchOutageEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BranchOutageEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Open"/&gt;
 *     &lt;enumeration value="Close"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BranchOutageEnumType")
@XmlEnum
public enum BranchOutageEnumType {

    @XmlEnumValue("Open")
    OPEN("Open"),
    @XmlEnumValue("Close")
    CLOSE("Close");
    private final String value;

    BranchOutageEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BranchOutageEnumType fromValue(String v) {
        for (BranchOutageEnumType c: BranchOutageEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
