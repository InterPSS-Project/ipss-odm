//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LFLoadCodeEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LFLoadCodeEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CONST_P"/&gt;
 *     &lt;enumeration value="CONST_I"/&gt;
 *     &lt;enumeration value="CONST_Z"/&gt;
 *     &lt;enumeration value="FunctionLoad"/&gt;
 *     &lt;enumeration value="LoadPV"/&gt;
 *     &lt;enumeration value="NoneLoad"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LFLoadCodeEnumType")
@XmlEnum
public enum LFLoadCodeEnumType {

    CONST_P("CONST_P"),
    CONST_I("CONST_I"),
    CONST_Z("CONST_Z"),
    @XmlEnumValue("FunctionLoad")
    FUNCTION_LOAD("FunctionLoad"),
    @XmlEnumValue("LoadPV")
    LOAD_PV("LoadPV"),
    @XmlEnumValue("NoneLoad")
    NONE_LOAD("NoneLoad");
    private final String value;

    LFLoadCodeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LFLoadCodeEnumType fromValue(String v) {
        for (LFLoadCodeEnumType c: LFLoadCodeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
