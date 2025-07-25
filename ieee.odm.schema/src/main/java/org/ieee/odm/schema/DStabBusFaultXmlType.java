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
 * <p>Java class for DStabBusFaultXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DStabBusFaultXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseDStabFaultXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="faultedBus" type="{http://www.ieee.org/odm/Schema/2008}IDRefRecordXmlType"/&gt;
 *         &lt;element name="faultedBusRatedV" type="{http://www.ieee.org/odm/Schema/2008}VoltageXmlType" minOccurs="0"/&gt;
 *         &lt;element name="remoteEndBus" type="{http://www.ieee.org/odm/Schema/2008}IDRefRecordXmlType" minOccurs="0"/&gt;
 *         &lt;element name="remoteEndBusRatedV" type="{http://www.ieee.org/odm/Schema/2008}VoltageXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DStabBusFaultXmlType", propOrder = {
    "faultedBus",
    "faultedBusRatedV",
    "remoteEndBus",
    "remoteEndBusRatedV"
})
public class DStabBusFaultXmlType
    extends BaseDStabFaultXmlType
{

    @XmlElement(required = true)
    protected IDRefRecordXmlType faultedBus;
    protected VoltageXmlType faultedBusRatedV;
    protected IDRefRecordXmlType remoteEndBus;
    protected VoltageXmlType remoteEndBusRatedV;

    /**
     * Gets the value of the faultedBus property.
     * 
     * @return
     *     possible object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public IDRefRecordXmlType getFaultedBus() {
        return faultedBus;
    }

    /**
     * Sets the value of the faultedBus property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public void setFaultedBus(IDRefRecordXmlType value) {
        this.faultedBus = value;
    }

    /**
     * Gets the value of the faultedBusRatedV property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageXmlType }
     *     
     */
    public VoltageXmlType getFaultedBusRatedV() {
        return faultedBusRatedV;
    }

    /**
     * Sets the value of the faultedBusRatedV property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageXmlType }
     *     
     */
    public void setFaultedBusRatedV(VoltageXmlType value) {
        this.faultedBusRatedV = value;
    }

    /**
     * Gets the value of the remoteEndBus property.
     * 
     * @return
     *     possible object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public IDRefRecordXmlType getRemoteEndBus() {
        return remoteEndBus;
    }

    /**
     * Sets the value of the remoteEndBus property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDRefRecordXmlType }
     *     
     */
    public void setRemoteEndBus(IDRefRecordXmlType value) {
        this.remoteEndBus = value;
    }

    /**
     * Gets the value of the remoteEndBusRatedV property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageXmlType }
     *     
     */
    public VoltageXmlType getRemoteEndBusRatedV() {
        return remoteEndBusRatedV;
    }

    /**
     * Sets the value of the remoteEndBusRatedV property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageXmlType }
     *     
     */
    public void setRemoteEndBusRatedV(VoltageXmlType value) {
        this.remoteEndBusRatedV = value;
    }

}
