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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AclfLoadCodeChangeXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AclfLoadCodeChangeXmlType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loadCode" type="{http://www.ieee.org/odm/Schema/2008}AclfLoadCodeEnumType"/>
 *         &lt;element name="expLoadP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="expLoadQ" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AclfLoadCodeChangeXmlType", propOrder = {
    "loadCode",
    "expLoadP",
    "expLoadQ"
})
public class AclfLoadCodeChangeXmlType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AclfLoadCodeEnumType loadCode;
    protected Double expLoadP;
    protected Double expLoadQ;

    /**
     * Gets the value of the loadCode property.
     * 
     * @return
     *     possible object is
     *     {@link AclfLoadCodeEnumType }
     *     
     */
    public AclfLoadCodeEnumType getLoadCode() {
        return loadCode;
    }

    /**
     * Sets the value of the loadCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link AclfLoadCodeEnumType }
     *     
     */
    public void setLoadCode(AclfLoadCodeEnumType value) {
        this.loadCode = value;
    }

    /**
     * Gets the value of the expLoadP property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getExpLoadP() {
        return expLoadP;
    }

    /**
     * Sets the value of the expLoadP property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setExpLoadP(Double value) {
        this.expLoadP = value;
    }

    /**
     * Gets the value of the expLoadQ property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getExpLoadQ() {
        return expLoadQ;
    }

    /**
     * Sets the value of the expLoadQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setExpLoadQ(Double value) {
        this.expLoadQ = value;
    }

}