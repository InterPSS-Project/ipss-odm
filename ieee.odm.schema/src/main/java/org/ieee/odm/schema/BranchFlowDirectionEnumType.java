
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BranchFlowDirectionEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BranchFlowDirectionEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="From_To"/&gt;
 *     &lt;enumeration value="To_From"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BranchFlowDirectionEnumType")
@XmlEnum
public enum BranchFlowDirectionEnumType {

    @XmlEnumValue("From_To")
    FROM_TO("From_To"),
    @XmlEnumValue("To_From")
    TO_FROM("To_From");
    private final String value;

    BranchFlowDirectionEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BranchFlowDirectionEnumType fromValue(String v) {
        for (BranchFlowDirectionEnumType c: BranchFlowDirectionEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
