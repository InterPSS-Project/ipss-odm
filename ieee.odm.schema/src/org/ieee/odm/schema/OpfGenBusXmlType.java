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
 * 
 * 		
 * 
 * <p>Java class for OpfGenBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OpfGenBusXmlType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadflowBusXmlType">
 *       &lt;sequence>
 *         &lt;element name="operatingMode" type="{http://www.ieee.org/odm/Schema/2008}OpfGenOperatingModeEnumType"/>
 *         &lt;element name="incCost" type="{http://www.ieee.org/odm/Schema/2008}IncCostXmlType"/>
 *         &lt;element name="constraints" type="{http://www.ieee.org/odm/Schema/2008}ConstraintsXmlType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpfGenBusXmlType", propOrder = {
    "operatingMode",
    "incCost",
    "constraints"
})
public class OpfGenBusXmlType
    extends LoadflowBusXmlType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected OpfGenOperatingModeEnumType operatingMode;
    @XmlElement(required = true)
    protected IncCostXmlType incCost;
    @XmlElement(required = true)
    protected ConstraintsXmlType constraints;

    /**
     * Gets the value of the operatingMode property.
     * 
     * @return
     *     possible object is
     *     {@link OpfGenOperatingModeEnumType }
     *     
     */
    public OpfGenOperatingModeEnumType getOperatingMode() {
        return operatingMode;
    }

    /**
     * Sets the value of the operatingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpfGenOperatingModeEnumType }
     *     
     */
    public void setOperatingMode(OpfGenOperatingModeEnumType value) {
        this.operatingMode = value;
    }

    /**
     * Gets the value of the incCost property.
     * 
     * @return
     *     possible object is
     *     {@link IncCostXmlType }
     *     
     */
    public IncCostXmlType getIncCost() {
        return incCost;
    }

    /**
     * Sets the value of the incCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncCostXmlType }
     *     
     */
    public void setIncCost(IncCostXmlType value) {
        this.incCost = value;
    }

    /**
     * Gets the value of the constraints property.
     * 
     * @return
     *     possible object is
     *     {@link ConstraintsXmlType }
     *     
     */
    public ConstraintsXmlType getConstraints() {
        return constraints;
    }

    /**
     * Sets the value of the constraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConstraintsXmlType }
     *     
     */
    public void setConstraints(ConstraintsXmlType value) {
        this.constraints = value;
    }

}