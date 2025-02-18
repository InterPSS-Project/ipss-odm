
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusIDRefXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BusIDRefXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}IDRefRecordXmlType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="windingType" type="{http://www.ieee.org/odm/Schema/2008}BusWindingEnumType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusIDRefXmlType")
public class BusIDRefXmlType
    extends IDRefRecordXmlType
{

    @XmlAttribute(name = "windingType")
    protected BusWindingEnumType windingType;

    /**
     * Gets the value of the windingType property.
     * 
     * @return
     *     possible object is
     *     {@link BusWindingEnumType }
     *     
     */
    public BusWindingEnumType getWindingType() {
        return windingType;
    }

    /**
     * Sets the value of the windingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BusWindingEnumType }
     *     
     */
    public void setWindingType(BusWindingEnumType value) {
        this.windingType = value;
    }

}
