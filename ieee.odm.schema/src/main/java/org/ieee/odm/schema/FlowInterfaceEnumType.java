
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FlowInterfaceEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FlowInterfaceEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BG"/&gt;
 *     &lt;enumeration value="NG"/&gt;
 *     &lt;enumeration value="TOR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FlowInterfaceEnumType")
@XmlEnum
public enum FlowInterfaceEnumType {

    BG,
    NG,
    TOR;

    public String value() {
        return name();
    }

    public static FlowInterfaceEnumType fromValue(String v) {
        return valueOf(v);
    }

}
