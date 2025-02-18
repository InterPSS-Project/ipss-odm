
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GovIEEE1981Type3XmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GovIEEE1981Type3XmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}GovernorModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TG" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TP" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="VOpen" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VClose" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="PMAX" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="PMIN" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="SIGMA" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="DELTA" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="TR" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TW" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="a11" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="a13" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="a21" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="a23" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GovIEEE1981Type3XmlType", propOrder = {
    "tg",
    "tp",
    "vOpen",
    "vClose",
    "pmax",
    "pmin",
    "sigma",
    "delta",
    "tr",
    "tw",
    "a11",
    "a13",
    "a21",
    "a23"
})
public class GovIEEE1981Type3XmlType
    extends GovernorModelXmlType
{

    @XmlElement(name = "TG", required = true)
    protected TimePeriodXmlType tg;
    @XmlElement(name = "TP", required = true)
    protected TimePeriodXmlType tp;
    @XmlElement(name = "VOpen")
    protected double vOpen;
    @XmlElement(name = "VClose")
    protected double vClose;
    @XmlElement(name = "PMAX")
    protected double pmax;
    @XmlElement(name = "PMIN")
    protected double pmin;
    @XmlElement(name = "SIGMA")
    protected double sigma;
    @XmlElement(name = "DELTA")
    protected double delta;
    @XmlElement(name = "TR", required = true)
    protected TimePeriodXmlType tr;
    @XmlElement(name = "TW", required = true)
    protected TimePeriodXmlType tw;
    protected double a11;
    protected double a13;
    protected double a21;
    protected double a23;

    /**
     * Gets the value of the tg property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTG() {
        return tg;
    }

    /**
     * Sets the value of the tg property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTG(TimePeriodXmlType value) {
        this.tg = value;
    }

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTP() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTP(TimePeriodXmlType value) {
        this.tp = value;
    }

    /**
     * Gets the value of the vOpen property.
     * 
     */
    public double getVOpen() {
        return vOpen;
    }

    /**
     * Sets the value of the vOpen property.
     * 
     */
    public void setVOpen(double value) {
        this.vOpen = value;
    }

    /**
     * Gets the value of the vClose property.
     * 
     */
    public double getVClose() {
        return vClose;
    }

    /**
     * Sets the value of the vClose property.
     * 
     */
    public void setVClose(double value) {
        this.vClose = value;
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

    /**
     * Gets the value of the sigma property.
     * 
     */
    public double getSIGMA() {
        return sigma;
    }

    /**
     * Sets the value of the sigma property.
     * 
     */
    public void setSIGMA(double value) {
        this.sigma = value;
    }

    /**
     * Gets the value of the delta property.
     * 
     */
    public double getDELTA() {
        return delta;
    }

    /**
     * Sets the value of the delta property.
     * 
     */
    public void setDELTA(double value) {
        this.delta = value;
    }

    /**
     * Gets the value of the tr property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTR() {
        return tr;
    }

    /**
     * Sets the value of the tr property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTR(TimePeriodXmlType value) {
        this.tr = value;
    }

    /**
     * Gets the value of the tw property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTW() {
        return tw;
    }

    /**
     * Sets the value of the tw property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTW(TimePeriodXmlType value) {
        this.tw = value;
    }

    /**
     * Gets the value of the a11 property.
     * 
     */
    public double getA11() {
        return a11;
    }

    /**
     * Sets the value of the a11 property.
     * 
     */
    public void setA11(double value) {
        this.a11 = value;
    }

    /**
     * Gets the value of the a13 property.
     * 
     */
    public double getA13() {
        return a13;
    }

    /**
     * Sets the value of the a13 property.
     * 
     */
    public void setA13(double value) {
        this.a13 = value;
    }

    /**
     * Gets the value of the a21 property.
     * 
     */
    public double getA21() {
        return a21;
    }

    /**
     * Sets the value of the a21 property.
     * 
     */
    public void setA21(double value) {
        this.a21 = value;
    }

    /**
     * Gets the value of the a23 property.
     * 
     */
    public double getA23() {
        return a23;
    }

    /**
     * Sets the value of the a23 property.
     * 
     */
    public void setA23(double value) {
        this.a23 = value;
    }

}
