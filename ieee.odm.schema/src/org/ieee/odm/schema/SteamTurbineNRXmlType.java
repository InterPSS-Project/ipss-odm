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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SteamTurbineNRXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SteamTurbineNRXmlType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}TurbineModelXmlType">
 *       &lt;sequence>
 *         &lt;element name="K" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="TCH" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SteamTurbineNRXmlType", propOrder = {
    "k",
    "tch"
})
@XmlSeeAlso({
    SteamTurbineTCDRXmlType.class,
    SteamTurbineTCSRXmlType.class
})
public class SteamTurbineNRXmlType
    extends TurbineModelXmlType
{

    @XmlElement(name = "K")
    protected Double k;
    @XmlElement(name = "TCH")
    protected TimePeriodXmlType tch;

    /**
     * Gets the value of the k property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getK() {
        return k;
    }

    /**
     * Sets the value of the k property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setK(Double value) {
        this.k = value;
    }

    /**
     * Gets the value of the tch property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTCH() {
        return tch;
    }

    /**
     * Sets the value of the tch property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTCH(TimePeriodXmlType value) {
        this.tch = value;
    }

}
