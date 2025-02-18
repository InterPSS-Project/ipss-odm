
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DynamicStaticLoadEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DynamicStaticLoadEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CONST_Z"/&gt;
 *     &lt;enumeration value="CONST_P"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DynamicStaticLoadEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum DynamicStaticLoadEnumType {

    CONST_Z,
    CONST_P;

    public String value() {
        return name();
    }

    public static DynamicStaticLoadEnumType fromValue(String v) {
        return valueOf(v);
    }

}
