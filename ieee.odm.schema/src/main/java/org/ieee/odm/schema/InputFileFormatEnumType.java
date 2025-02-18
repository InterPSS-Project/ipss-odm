
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InputFileFormatEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InputFileFormatEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PSSE"/&gt;
 *     &lt;enumeration value="BPA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InputFileFormatEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum InputFileFormatEnumType {

    PSSE,
    BPA;

    public String value() {
        return name();
    }

    public static InputFileFormatEnumType fromValue(String v) {
        return valueOf(v);
    }

}
