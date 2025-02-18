
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroundingXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroundingXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="groundingZ" type="{http://www.ieee.org/odm/Schema/2008}ZXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="groundingConnection" use="required" type="{http://www.ieee.org/odm/Schema/2008}GroundingEnumType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroundingXmlType", propOrder = {
    "groundingZ"
})
public class GroundingXmlType {

    protected ZXmlType groundingZ;
    @XmlAttribute(name = "groundingConnection", required = true)
    protected GroundingEnumType groundingConnection;

    /**
     * Gets the value of the groundingZ property.
     * 
     * @return
     *     possible object is
     *     {@link ZXmlType }
     *     
     */
    public ZXmlType getGroundingZ() {
        return groundingZ;
    }

    /**
     * Sets the value of the groundingZ property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZXmlType }
     *     
     */
    public void setGroundingZ(ZXmlType value) {
        this.groundingZ = value;
    }

    /**
     * Gets the value of the groundingConnection property.
     * 
     * @return
     *     possible object is
     *     {@link GroundingEnumType }
     *     
     */
    public GroundingEnumType getGroundingConnection() {
        return groundingConnection;
    }

    /**
     * Sets the value of the groundingConnection property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroundingEnumType }
     *     
     */
    public void setGroundingConnection(GroundingEnumType value) {
        this.groundingConnection = value;
    }

}
