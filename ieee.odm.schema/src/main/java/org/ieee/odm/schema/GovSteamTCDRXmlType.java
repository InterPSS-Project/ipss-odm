
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GovSteamTCDRXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GovSteamTCDRXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}GovSteamNRXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TRH1" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TRH2" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="TCO" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="FVHP" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="FHP" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="FIP" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="FLP" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GovSteamTCDRXmlType", propOrder = {
    "trh1",
    "trh2",
    "tco",
    "fvhp",
    "fhp",
    "fip",
    "flp"
})
public class GovSteamTCDRXmlType
    extends GovSteamNRXmlType
{

    @XmlElement(name = "TRH1", required = true)
    protected TimePeriodXmlType trh1;
    @XmlElement(name = "TRH2", required = true)
    protected TimePeriodXmlType trh2;
    @XmlElement(name = "TCO", required = true)
    protected TimePeriodXmlType tco;
    @XmlElement(name = "FVHP")
    protected double fvhp;
    @XmlElement(name = "FHP")
    protected double fhp;
    @XmlElement(name = "FIP")
    protected double fip;
    @XmlElement(name = "FLP")
    protected double flp;

    /**
     * Gets the value of the trh1 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTRH1() {
        return trh1;
    }

    /**
     * Sets the value of the trh1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTRH1(TimePeriodXmlType value) {
        this.trh1 = value;
    }

    /**
     * Gets the value of the trh2 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTRH2() {
        return trh2;
    }

    /**
     * Sets the value of the trh2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTRH2(TimePeriodXmlType value) {
        this.trh2 = value;
    }

    /**
     * Gets the value of the tco property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTCO() {
        return tco;
    }

    /**
     * Sets the value of the tco property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTCO(TimePeriodXmlType value) {
        this.tco = value;
    }

    /**
     * Gets the value of the fvhp property.
     * 
     */
    public double getFVHP() {
        return fvhp;
    }

    /**
     * Sets the value of the fvhp property.
     * 
     */
    public void setFVHP(double value) {
        this.fvhp = value;
    }

    /**
     * Gets the value of the fhp property.
     * 
     */
    public double getFHP() {
        return fhp;
    }

    /**
     * Sets the value of the fhp property.
     * 
     */
    public void setFHP(double value) {
        this.fhp = value;
    }

    /**
     * Gets the value of the fip property.
     * 
     */
    public double getFIP() {
        return fip;
    }

    /**
     * Sets the value of the fip property.
     * 
     */
    public void setFIP(double value) {
        this.fip = value;
    }

    /**
     * Gets the value of the flp property.
     * 
     */
    public double getFLP() {
        return flp;
    }

    /**
     * Sets the value of the flp property.
     * 
     */
    public void setFLP(double value) {
        this.flp = value;
    }

}
