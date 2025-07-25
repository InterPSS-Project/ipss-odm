//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AngleAdjustmentXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AngleAdjustmentXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}AdjustmentDataXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="angleLimit" type="{http://www.ieee.org/odm/Schema/2008}AngleLimitXmlType"/&gt;
 *         &lt;element name="angleAdjOnFromSide" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="flowDirection" type="{http://www.ieee.org/odm/Schema/2008}BranchFlowDirectionEnumType"/&gt;
 *         &lt;element name="desiredActivePowerUnit" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerUnitType"/&gt;
 *         &lt;element name="desiredMeasuredOnFromSide" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="offLine" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AngleAdjustmentXmlType", propOrder = {
    "angleLimit",
    "angleAdjOnFromSide",
    "flowDirection",
    "desiredActivePowerUnit",
    "desiredMeasuredOnFromSide"
})
public class AngleAdjustmentXmlType
    extends AdjustmentDataXmlType
{

    @XmlElement(required = true)
    protected AngleLimitXmlType angleLimit;
    protected boolean angleAdjOnFromSide;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BranchFlowDirectionEnumType flowDirection;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ActivePowerUnitType desiredActivePowerUnit;
    protected boolean desiredMeasuredOnFromSide;
    @XmlAttribute(name = "offLine", required = true)
    protected boolean offLine;

    /**
     * Gets the value of the angleLimit property.
     * 
     * @return
     *     possible object is
     *     {@link AngleLimitXmlType }
     *     
     */
    public AngleLimitXmlType getAngleLimit() {
        return angleLimit;
    }

    /**
     * Sets the value of the angleLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link AngleLimitXmlType }
     *     
     */
    public void setAngleLimit(AngleLimitXmlType value) {
        this.angleLimit = value;
    }

    /**
     * Gets the value of the angleAdjOnFromSide property.
     * 
     */
    public boolean isAngleAdjOnFromSide() {
        return angleAdjOnFromSide;
    }

    /**
     * Sets the value of the angleAdjOnFromSide property.
     * 
     */
    public void setAngleAdjOnFromSide(boolean value) {
        this.angleAdjOnFromSide = value;
    }

    /**
     * Gets the value of the flowDirection property.
     * 
     * @return
     *     possible object is
     *     {@link BranchFlowDirectionEnumType }
     *     
     */
    public BranchFlowDirectionEnumType getFlowDirection() {
        return flowDirection;
    }

    /**
     * Sets the value of the flowDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchFlowDirectionEnumType }
     *     
     */
    public void setFlowDirection(BranchFlowDirectionEnumType value) {
        this.flowDirection = value;
    }

    /**
     * Gets the value of the desiredActivePowerUnit property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerUnitType }
     *     
     */
    public ActivePowerUnitType getDesiredActivePowerUnit() {
        return desiredActivePowerUnit;
    }

    /**
     * Sets the value of the desiredActivePowerUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerUnitType }
     *     
     */
    public void setDesiredActivePowerUnit(ActivePowerUnitType value) {
        this.desiredActivePowerUnit = value;
    }

    /**
     * Gets the value of the desiredMeasuredOnFromSide property.
     * 
     */
    public boolean isDesiredMeasuredOnFromSide() {
        return desiredMeasuredOnFromSide;
    }

    /**
     * Sets the value of the desiredMeasuredOnFromSide property.
     * 
     */
    public void setDesiredMeasuredOnFromSide(boolean value) {
        this.desiredMeasuredOnFromSide = value;
    }

    /**
     * Gets the value of the offLine property.
     * 
     */
    public boolean isOffLine() {
        return offLine;
    }

    /**
     * Sets the value of the offLine property.
     * 
     */
    public void setOffLine(boolean value) {
        this.offLine = value;
    }

}
