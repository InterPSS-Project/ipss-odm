
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SwitchedShuntBlockXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SwitchedShuntBlockXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="incrementB" type="{http://www.ieee.org/odm/Schema/2008}ReactivePowerXmlType"/&gt;
 *         &lt;element name="zeroSeqIncrementB" type="{http://www.ieee.org/odm/Schema/2008}ReactivePowerXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="steps" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwitchedShuntBlockXmlType", propOrder = {
    "incrementB",
    "zeroSeqIncrementB"
})
public class SwitchedShuntBlockXmlType {

    @XmlElement(required = true)
    protected ReactivePowerXmlType incrementB;
    protected ReactivePowerXmlType zeroSeqIncrementB;
    @XmlAttribute(name = "steps", required = true)
    protected int steps;

    /**
     * Gets the value of the incrementB property.
     * 
     * @return
     *     possible object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public ReactivePowerXmlType getIncrementB() {
        return incrementB;
    }

    /**
     * Sets the value of the incrementB property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public void setIncrementB(ReactivePowerXmlType value) {
        this.incrementB = value;
    }

    /**
     * Gets the value of the zeroSeqIncrementB property.
     * 
     * @return
     *     possible object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public ReactivePowerXmlType getZeroSeqIncrementB() {
        return zeroSeqIncrementB;
    }

    /**
     * Sets the value of the zeroSeqIncrementB property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReactivePowerXmlType }
     *     
     */
    public void setZeroSeqIncrementB(ReactivePowerXmlType value) {
        this.zeroSeqIncrementB = value;
    }

    /**
     * Gets the value of the steps property.
     * 
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets the value of the steps property.
     * 
     */
    public void setSteps(int value) {
        this.steps = value;
    }

}
