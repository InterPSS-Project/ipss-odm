//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterfaceShiftFactorXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterfaceShiftFactorXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="shiftFactor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="interface" type="{http://www.ieee.org/odm/Schema/2008}FlowInterfaceRefXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterfaceShiftFactorXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "shiftFactor",
    "_interface"
})
public class InterfaceShiftFactorXmlType {

    protected Double shiftFactor;
    @XmlElement(name = "interface")
    protected FlowInterfaceRefXmlType _interface;

    /**
     * Gets the value of the shiftFactor property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getShiftFactor() {
        return shiftFactor;
    }

    /**
     * Sets the value of the shiftFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setShiftFactor(Double value) {
        this.shiftFactor = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link FlowInterfaceRefXmlType }
     *     
     */
    public FlowInterfaceRefXmlType getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlowInterfaceRefXmlType }
     *     
     */
    public void setInterface(FlowInterfaceRefXmlType value) {
        this._interface = value;
    }

}
