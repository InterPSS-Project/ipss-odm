//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OpfNetworkEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OpfNetworkEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SimpleDclf"/>
 *     &lt;enumeration value="OpfNetwork"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OpfNetworkEnumType")
@XmlEnum
public enum OpfNetworkEnumType {

    @XmlEnumValue("SimpleDclf")
    SIMPLE_DCLF("SimpleDclf"),
    @XmlEnumValue("OpfNetwork")
    OPF_NETWORK("OpfNetwork");
    private final String value;

    OpfNetworkEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OpfNetworkEnumType fromValue(String v) {
        for (OpfNetworkEnumType c: OpfNetworkEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
