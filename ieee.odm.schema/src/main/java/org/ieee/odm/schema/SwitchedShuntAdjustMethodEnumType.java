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
 * <p>Java class for SwitchedShuntAdjustMethodEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SwitchedShuntAdjustMethodEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DataInputOrder"/&gt;
 *     &lt;enumeration value="HighestB"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SwitchedShuntAdjustMethodEnumType")
@XmlEnum
public enum SwitchedShuntAdjustMethodEnumType {


    /**
     * 
     * 				B steps and blocks are switched on in input order, and off in reverse  input order
     * 				
     * 
     */
    @XmlEnumValue("DataInputOrder")
    DATA_INPUT_ORDER("DataInputOrder"),

    /**
     * 
     * 				B steps and blocks are switched on and off such that the next highest  (or lowest, as appropriate) total admittance is achieved.
     * 				
     * 
     */
    @XmlEnumValue("HighestB")
    HIGHEST_B("HighestB");
    private final String value;

    SwitchedShuntAdjustMethodEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SwitchedShuntAdjustMethodEnumType fromValue(String v) {
        for (SwitchedShuntAdjustMethodEnumType c: SwitchedShuntAdjustMethodEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
