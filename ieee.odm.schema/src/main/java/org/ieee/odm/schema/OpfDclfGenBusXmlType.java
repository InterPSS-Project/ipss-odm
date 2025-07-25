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
 * 
 * 		
 * 
 * <p>Java class for OpfDclfGenBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OpfDclfGenBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadflowBusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="coeffA" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="coeffB" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="capacityLimit" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerLimitXmlType"/&gt;
 *         &lt;element name="fixedCost" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpfDclfGenBusXmlType", propOrder = {
    "coeffA",
    "coeffB",
    "capacityLimit",
    "fixedCost"
})
public class OpfDclfGenBusXmlType
    extends LoadflowBusXmlType
{

    protected double coeffA;
    protected double coeffB;
    @XmlElement(required = true)
    protected ActivePowerLimitXmlType capacityLimit;
    protected double fixedCost;

    /**
     * Gets the value of the coeffA property.
     * 
     */
    public double getCoeffA() {
        return coeffA;
    }

    /**
     * Sets the value of the coeffA property.
     * 
     */
    public void setCoeffA(double value) {
        this.coeffA = value;
    }

    /**
     * Gets the value of the coeffB property.
     * 
     */
    public double getCoeffB() {
        return coeffB;
    }

    /**
     * Sets the value of the coeffB property.
     * 
     */
    public void setCoeffB(double value) {
        this.coeffB = value;
    }

    /**
     * Gets the value of the capacityLimit property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerLimitXmlType }
     *     
     */
    public ActivePowerLimitXmlType getCapacityLimit() {
        return capacityLimit;
    }

    /**
     * Sets the value of the capacityLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerLimitXmlType }
     *     
     */
    public void setCapacityLimit(ActivePowerLimitXmlType value) {
        this.capacityLimit = value;
    }

    /**
     * Gets the value of the fixedCost property.
     * 
     */
    public double getFixedCost() {
        return fixedCost;
    }

    /**
     * Sets the value of the fixedCost property.
     * 
     */
    public void setFixedCost(double value) {
        this.fixedCost = value;
    }

}
