//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BreakerDistBranchXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BreakerDistBranchXmlType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}DistBranchXmlType">
 *       &lt;sequence>
 *         &lt;element name="r" type="{http://www.ieee.org/odm/Schema/2008}RXmlType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BreakerDistBranchXmlType", propOrder = {
    "r"
})
public class BreakerDistBranchXmlType
    extends DistBranchXmlType
{

    @XmlElement(required = true)
    protected RXmlType r;

    /**
     * Gets the value of the r property.
     * 
     * @return
     *     possible object is
     *     {@link RXmlType }
     *     
     */
    public RXmlType getR() {
        return r;
    }

    /**
     * Sets the value of the r property.
     * 
     * @param value
     *     allowed object is
     *     {@link RXmlType }
     *     
     */
    public void setR(RXmlType value) {
        this.r = value;
    }

}
