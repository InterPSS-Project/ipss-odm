
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SteamTurbineNRXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SteamTurbineNRXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}TurbineModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="K" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="TCH" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SteamTurbineNRXmlType", propOrder = {
    "k",
    "tch"
})
@XmlSeeAlso({
    SteamTurbineTCDRXmlType.class,
    SteamTurbineTCSRXmlType.class
})
public class SteamTurbineNRXmlType
    extends TurbineModelXmlType
{

    @XmlElement(name = "K")
    protected Double k;
    @XmlElement(name = "TCH")
    protected TimePeriodXmlType tch;

    /**
     * Gets the value of the k property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getK() {
        return k;
    }

    /**
     * Sets the value of the k property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setK(Double value) {
        this.k = value;
    }

    /**
     * Gets the value of the tch property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getTCH() {
        return tch;
    }

    /**
     * Sets the value of the tch property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setTCH(TimePeriodXmlType value) {
        this.tch = value;
    }

}
