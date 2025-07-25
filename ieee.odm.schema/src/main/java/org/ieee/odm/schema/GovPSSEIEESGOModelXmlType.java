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
 * <p>Java class for GovPSSEIEESGOModelXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GovPSSEIEESGOModelXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}GovernorModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="T1" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T2" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T3" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T4" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T5" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T6" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="K1" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="K2" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="K3" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="PMAX" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="PMIN" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GovPSSEIEESGOModelXmlType", propOrder = {
    "t1",
    "t2",
    "t3",
    "t4",
    "t5",
    "t6",
    "k1",
    "k2",
    "k3",
    "pmax",
    "pmin"
})
public class GovPSSEIEESGOModelXmlType
    extends GovernorModelXmlType
{

    @XmlElement(name = "T1", required = true)
    protected TimePeriodXmlType t1;
    @XmlElement(name = "T2", required = true)
    protected TimePeriodXmlType t2;
    @XmlElement(name = "T3", required = true)
    protected TimePeriodXmlType t3;
    @XmlElement(name = "T4", required = true)
    protected TimePeriodXmlType t4;
    @XmlElement(name = "T5", required = true)
    protected TimePeriodXmlType t5;
    @XmlElement(name = "T6", required = true)
    protected TimePeriodXmlType t6;
    @XmlElement(name = "K1")
    protected double k1;
    @XmlElement(name = "K2")
    protected double k2;
    @XmlElement(name = "K3")
    protected double k3;
    @XmlElement(name = "PMAX")
    protected double pmax;
    @XmlElement(name = "PMIN")
    protected double pmin;

    /**
     * Gets the value of the t1 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT1() {
        return t1;
    }

    /**
     * Sets the value of the t1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT1(TimePeriodXmlType value) {
        this.t1 = value;
    }

    /**
     * Gets the value of the t2 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT2() {
        return t2;
    }

    /**
     * Sets the value of the t2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT2(TimePeriodXmlType value) {
        this.t2 = value;
    }

    /**
     * Gets the value of the t3 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT3() {
        return t3;
    }

    /**
     * Sets the value of the t3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT3(TimePeriodXmlType value) {
        this.t3 = value;
    }

    /**
     * Gets the value of the t4 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT4() {
        return t4;
    }

    /**
     * Sets the value of the t4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT4(TimePeriodXmlType value) {
        this.t4 = value;
    }

    /**
     * Gets the value of the t5 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT5() {
        return t5;
    }

    /**
     * Sets the value of the t5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT5(TimePeriodXmlType value) {
        this.t5 = value;
    }

    /**
     * Gets the value of the t6 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT6() {
        return t6;
    }

    /**
     * Sets the value of the t6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT6(TimePeriodXmlType value) {
        this.t6 = value;
    }

    /**
     * Gets the value of the k1 property.
     * 
     */
    public double getK1() {
        return k1;
    }

    /**
     * Sets the value of the k1 property.
     * 
     */
    public void setK1(double value) {
        this.k1 = value;
    }

    /**
     * Gets the value of the k2 property.
     * 
     */
    public double getK2() {
        return k2;
    }

    /**
     * Sets the value of the k2 property.
     * 
     */
    public void setK2(double value) {
        this.k2 = value;
    }

    /**
     * Gets the value of the k3 property.
     * 
     */
    public double getK3() {
        return k3;
    }

    /**
     * Sets the value of the k3 property.
     * 
     */
    public void setK3(double value) {
        this.k3 = value;
    }

    /**
     * Gets the value of the pmax property.
     * 
     */
    public double getPMAX() {
        return pmax;
    }

    /**
     * Sets the value of the pmax property.
     * 
     */
    public void setPMAX(double value) {
        this.pmax = value;
    }

    /**
     * Gets the value of the pmin property.
     * 
     */
    public double getPMIN() {
        return pmin;
    }

    /**
     * Sets the value of the pmin property.
     * 
     */
    public void setPMIN(double value) {
        this.pmin = value;
    }

}
