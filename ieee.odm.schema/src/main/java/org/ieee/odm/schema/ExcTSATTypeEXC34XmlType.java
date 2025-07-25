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
 * <p>Java class for ExcTSATTypeEXC34XmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExcTSATTypeEXC34XmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}ExcSimpleTypeXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IVUEL" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="IVOEL" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="LVS" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="TB" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TC" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TC1" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TB1" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TF" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="KF" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VIMAX" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VIMIN" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VAMAX" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VAMIN" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="KC" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="KLR" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ILR" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExcTSATTypeEXC34XmlType", propOrder = {
    "ivuel",
    "ivoel",
    "lvs",
    "tb",
    "tc",
    "tc1",
    "tb1",
    "tf",
    "kf",
    "vimax",
    "vimin",
    "vamax",
    "vamin",
    "kc",
    "klr",
    "ilr"
})
public class ExcTSATTypeEXC34XmlType
    extends ExcSimpleTypeXmlType
{

    @XmlElement(name = "IVUEL")
    protected double ivuel;
    @XmlElement(name = "IVOEL")
    protected double ivoel;
    @XmlElement(name = "LVS")
    protected double lvs;
    @XmlElement(name = "TB", required = true)
    protected TimePeriodXmlType tb;
    @XmlElement(name = "TC", required = true)
    protected TimePeriodXmlType tc;
    @XmlElement(name = "TC1", required = true)
    protected TimePeriodXmlType tc1;
    @XmlElement(name = "TB1", required = true)
    protected TimePeriodXmlType tb1;
    @XmlElement(name = "TF", required = true)
    protected TimePeriodXmlType tf;
    @XmlElement(name = "KF")
    protected double kf;
    @XmlElement(name = "VIMAX")
    protected double vimax;
    @XmlElement(name = "VIMIN")
    protected double vimin;
    @XmlElement(name = "VAMAX")
    protected double vamax;
    @XmlElement(name = "VAMIN")
    protected double vamin;
    @XmlElement(name = "KC")
    protected double kc;
    @XmlElement(name = "KLR")
    protected double klr;
    @XmlElement(name = "ILR")
    protected double ilr;

    /**
     * Gets the value of the ivuel property.
     * 
     */
    public double getIVUEL() {
        return ivuel;
    }

    /**
     * Sets the value of the ivuel property.
     * 
     */
    public void setIVUEL(double value) {
        this.ivuel = value;
    }

    /**
     * Gets the value of the ivoel property.
     * 
     */
    public double getIVOEL() {
        return ivoel;
    }

    /**
     * Sets the value of the ivoel property.
     * 
     */
    public void setIVOEL(double value) {
        this.ivoel = value;
    }

    /**
     * Gets the value of the lvs property.
     * 
     */
    public double getLVS() {
        return lvs;
    }

    /**
     * Sets the value of the lvs property.
     * 
     */
    public void setLVS(double value) {
        this.lvs = value;
    }

    /**
     * Gets the value of the tb property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTB() {
        return tb;
    }

    /**
     * Sets the value of the tb property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTB(TimePeriodXmlType value) {
        this.tb = value;
    }

    /**
     * Gets the value of the tc property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTC() {
        return tc;
    }

    /**
     * Sets the value of the tc property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTC(TimePeriodXmlType value) {
        this.tc = value;
    }

    /**
     * Gets the value of the tc1 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTC1() {
        return tc1;
    }

    /**
     * Sets the value of the tc1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTC1(TimePeriodXmlType value) {
        this.tc1 = value;
    }

    /**
     * Gets the value of the tb1 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTB1() {
        return tb1;
    }

    /**
     * Sets the value of the tb1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTB1(TimePeriodXmlType value) {
        this.tb1 = value;
    }

    /**
     * Gets the value of the tf property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTF() {
        return tf;
    }

    /**
     * Sets the value of the tf property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTF(TimePeriodXmlType value) {
        this.tf = value;
    }

    /**
     * Gets the value of the kf property.
     * 
     */
    public double getKF() {
        return kf;
    }

    /**
     * Sets the value of the kf property.
     * 
     */
    public void setKF(double value) {
        this.kf = value;
    }

    /**
     * Gets the value of the vimax property.
     * 
     */
    public double getVIMAX() {
        return vimax;
    }

    /**
     * Sets the value of the vimax property.
     * 
     */
    public void setVIMAX(double value) {
        this.vimax = value;
    }

    /**
     * Gets the value of the vimin property.
     * 
     */
    public double getVIMIN() {
        return vimin;
    }

    /**
     * Sets the value of the vimin property.
     * 
     */
    public void setVIMIN(double value) {
        this.vimin = value;
    }

    /**
     * Gets the value of the vamax property.
     * 
     */
    public double getVAMAX() {
        return vamax;
    }

    /**
     * Sets the value of the vamax property.
     * 
     */
    public void setVAMAX(double value) {
        this.vamax = value;
    }

    /**
     * Gets the value of the vamin property.
     * 
     */
    public double getVAMIN() {
        return vamin;
    }

    /**
     * Sets the value of the vamin property.
     * 
     */
    public void setVAMIN(double value) {
        this.vamin = value;
    }

    /**
     * Gets the value of the kc property.
     * 
     */
    public double getKC() {
        return kc;
    }

    /**
     * Sets the value of the kc property.
     * 
     */
    public void setKC(double value) {
        this.kc = value;
    }

    /**
     * Gets the value of the klr property.
     * 
     */
    public double getKLR() {
        return klr;
    }

    /**
     * Sets the value of the klr property.
     * 
     */
    public void setKLR(double value) {
        this.klr = value;
    }

    /**
     * Gets the value of the ilr property.
     * 
     */
    public double getILR() {
        return ilr;
    }

    /**
     * Sets the value of the ilr property.
     * 
     */
    public void setILR(double value) {
        this.ilr = value;
    }

}
