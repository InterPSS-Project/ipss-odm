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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StaticVarCompensatorXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StaticVarCompensatorXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="offLine" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ratedVoltage" type="{http://www.ieee.org/odm/Schema/2008}VoltageXmlType"/&gt;
 *         &lt;element name="voltageSetPoint" type="{http://www.ieee.org/odm/Schema/2008}VoltageXmlType"/&gt;
 *         &lt;element name="capacitiveRating" type="{http://www.ieee.org/odm/Schema/2008}ReactivePowerXmlType"/&gt;
 *         &lt;element name="inductiveRating" type="{http://www.ieee.org/odm/Schema/2008}ReactivePowerXmlType"/&gt;
 *         &lt;element name="controlMode" type="{http://www.ieee.org/odm/Schema/2008}SVCControlModeEnumType"/&gt;
 *         &lt;element name="slope" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="remoteControlledBus" type="{http://www.ieee.org/odm/Schema/2008}IDRefRecordXmlType" minOccurs="0"/&gt;
 *         &lt;element name="remoteControlledNodeNum" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="remoteControlledPercentage" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="owner" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StaticVarCompensatorXmlType", propOrder = {
    "offLine",
    "ratedVoltage",
    "voltageSetPoint",
    "capacitiveRating",
    "inductiveRating",
    "controlMode",
    "slope",
    "remoteControlledBus",
    "remoteControlledNodeNum",
    "remoteControlledPercentage",
    "owner",
    "name"
})
public class StaticVarCompensatorXmlType {

    protected boolean offLine;
    @XmlElement(required = true)
    protected VoltageXmlType ratedVoltage;
    @XmlElement(required = true)
    protected VoltageXmlType voltageSetPoint;
    @XmlElement(required = true)
    protected ReactivePowerXmlType capacitiveRating;
    @XmlElement(required = true)
    protected ReactivePowerXmlType inductiveRating;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SVCControlModeEnumType controlMode;
    protected double slope;
    protected IDRefRecordXmlType remoteControlledBus;
    protected Integer remoteControlledNodeNum;
    protected Double remoteControlledPercentage;
    protected Integer owner;
    protected String name;

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

    /**
     * Gets the value of the ratedVoltage property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageXmlType }
     *     
     */
    public VoltageXmlType getRatedVoltage() {
        return ratedVoltage;
    }

    /**
     * Sets the value of the ratedVoltage property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageXmlType }
     *     
     */
    public void setRatedVoltage(VoltageXmlType value) {
        this.ratedVoltage = value;
    }

    /**
     * Gets the value of the voltageSetPoint property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageXmlType }
     *     
     */
    public VoltageXmlType getVoltageSetPoint() {
        return voltageSetPoint;
    }

    /**
     * Sets the value of the voltageSetPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageXmlType }
     *     
     */
    public void setVoltageSetPoint(VoltageXmlType value) {
        this.voltageSetPoint = value;
    }

    /**
     * Gets the value of the capacitiveRating property.
     * 
     * @return
     *     possible object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public ReactivePowerXmlType getCapacitiveRating() {
        return capacitiveRating;
    }

    /**
     * Sets the value of the capacitiveRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public void setCapacitiveRating(ReactivePowerXmlType value) {
        this.capacitiveRating = value;
    }

    /**
     * Gets the value of the inductiveRating property.
     * 
     * @return
     *     possible object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public ReactivePowerXmlType getInductiveRating() {
        return inductiveRating;
    }

    /**
     * Sets the value of the inductiveRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public void setInductiveRating(ReactivePowerXmlType value) {
        this.inductiveRating = value;
    }

    /**
     * Gets the value of the controlMode property.
     * 
     * @return
     *     possible object is
     *     {@link SVCControlModeEnumType }
     *     
     */
    public SVCControlModeEnumType getControlMode() {
        return controlMode;
    }

    /**
     * Sets the value of the controlMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link SVCControlModeEnumType }
     *     
     */
    public void setControlMode(SVCControlModeEnumType value) {
        this.controlMode = value;
    }

    /**
     * Gets the value of the slope property.
     * 
     */
    public double getSlope() {
        return slope;
    }

    /**
     * Sets the value of the slope property.
     * 
     */
    public void setSlope(double value) {
        this.slope = value;
    }

    /**
     * Gets the value of the remoteControlledBus property.
     * 
     * @return
     *     possible object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public IDRefRecordXmlType getRemoteControlledBus() {
        return remoteControlledBus;
    }

    /**
     * Sets the value of the remoteControlledBus property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public void setRemoteControlledBus(IDRefRecordXmlType value) {
        this.remoteControlledBus = value;
    }

    /**
     * Gets the value of the remoteControlledNodeNum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRemoteControlledNodeNum() {
        return remoteControlledNodeNum;
    }

    /**
     * Sets the value of the remoteControlledNodeNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRemoteControlledNodeNum(Integer value) {
        this.remoteControlledNodeNum = value;
    }

    /**
     * Gets the value of the remoteControlledPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRemoteControlledPercentage() {
        return remoteControlledPercentage;
    }

    /**
     * Sets the value of the remoteControlledPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRemoteControlledPercentage(Double value) {
        this.remoteControlledPercentage = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwner(Integer value) {
        this.owner = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
