//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AclfLoadCodeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AclfLoadCodeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CONST_P"/&gt;
 *     &lt;enumeration value="CONST_I"/&gt;
 *     &lt;enumeration value="CONST_Z"/&gt;
 *     &lt;enumeration value="EXPONENTIAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AclfLoadCodeEnumType")
@XmlEnum
public enum AclfLoadCodeEnumType {

    CONST_P,
    CONST_I,
    CONST_Z,
    EXPONENTIAL;

    public String value() {
        return name();
    }

    public static AclfLoadCodeEnumType fromValue(String v) {
        return valueOf(v);
    }

}
