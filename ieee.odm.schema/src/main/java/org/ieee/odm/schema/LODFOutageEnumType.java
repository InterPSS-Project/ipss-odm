
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LODFOutageEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LODFOutageEnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SingleBranch"/&gt;
 *     &lt;enumeration value="MultiBranch"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LODFOutageEnumType", namespace = "http://www.interpss.org/Schema/odm/2008")
@XmlEnum
public enum LODFOutageEnumType {

    @XmlEnumValue("SingleBranch")
    SINGLE_BRANCH("SingleBranch"),
    @XmlEnumValue("MultiBranch")
    MULTI_BRANCH("MultiBranch");
    private final String value;

    LODFOutageEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LODFOutageEnumType fromValue(String v) {
        for (LODFOutageEnumType c: LODFOutageEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
