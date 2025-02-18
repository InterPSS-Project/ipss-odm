
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		Under/over frequency generator trip relay
 * 		
 * 
 * <p>Java class for GenRelayFRQTPATXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenRelayFRQTPATXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}GenRelayXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FL" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="FU" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="Tp" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="Tb" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenRelayFRQTPATXmlType", propOrder = {
    "fl",
    "fu",
    "tp",
    "tb"
})
public class GenRelayFRQTPATXmlType
    extends GenRelayXmlType
{

    @XmlElement(name = "FL", defaultValue = "0.0")
    protected Double fl;
    @XmlElement(name = "FU", defaultValue = "0.0")
    protected Double fu;
    @XmlElement(name = "Tp", defaultValue = "0.0")
    protected Double tp;
    @XmlElement(name = "Tb", defaultValue = "0.0")
    protected Double tb;

    /**
     * Gets the value of the fl property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFL() {
        return fl;
    }

    /**
     * Sets the value of the fl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFL(Double value) {
        this.fl = value;
    }

    /**
     * Gets the value of the fu property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFU() {
        return fu;
    }

    /**
     * Sets the value of the fu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFU(Double value) {
        this.fu = value;
    }

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTp(Double value) {
        this.tp = value;
    }

    /**
     * Gets the value of the tb property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb() {
        return tb;
    }

    /**
     * Sets the value of the tb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb(Double value) {
        this.tb = value;
    }

}
