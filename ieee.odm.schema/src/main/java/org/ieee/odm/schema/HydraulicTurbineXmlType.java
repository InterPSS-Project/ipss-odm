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
 * <p>Java class for HydraulicTurbineXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HydraulicTurbineXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}TurbineModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TW" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HydraulicTurbineXmlType", propOrder = {
    "tw"
})
public class HydraulicTurbineXmlType
    extends TurbineModelXmlType
{

    @XmlElement(name = "TW")
    protected Double tw;

    /**
     * Gets the value of the tw property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTW() {
        return tw;
    }

    /**
     * Sets the value of the tw property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTW(Double value) {
        this.tw = value;
    }

}
