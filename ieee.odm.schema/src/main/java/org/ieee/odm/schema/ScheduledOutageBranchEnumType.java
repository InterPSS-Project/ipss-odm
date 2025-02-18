
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScheduledOutageBranchEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ScheduledOutageBranchEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Single"/&gt;
 *     &lt;enumeration value="Multiple"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ScheduledOutageBranchEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum ScheduledOutageBranchEnumType {

    @XmlEnumValue("Single")
    SINGLE("Single"),
    @XmlEnumValue("Multiple")
    MULTIPLE("Multiple");
    private final String value;

    ScheduledOutageBranchEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScheduledOutageBranchEnumType fromValue(String v) {
        for (ScheduledOutageBranchEnumType c: ScheduledOutageBranchEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
