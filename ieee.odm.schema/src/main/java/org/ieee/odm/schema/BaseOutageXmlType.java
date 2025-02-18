
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseOutageXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseOutageXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseOutageOverrideXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ticketNum" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="station" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="voltage" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="equipName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseOutageXmlType", propOrder = {
    "ticketNum",
    "name",
    "station",
    "voltage",
    "equipName"
})
@XmlSeeAlso({
    GenOutageXmlType.class,
    TransmissionOutageXmlType.class
})
public abstract class BaseOutageXmlType
    extends BaseOutageOverrideXmlType
{

    @XmlElement(required = true)
    protected String ticketNum;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String station;
    protected double voltage;
    @XmlElement(required = true)
    protected String equipName;

    /**
     * Gets the value of the ticketNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketNum() {
        return ticketNum;
    }

    /**
     * Sets the value of the ticketNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketNum(String value) {
        this.ticketNum = value;
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

    /**
     * Gets the value of the station property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStation() {
        return station;
    }

    /**
     * Sets the value of the station property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStation(String value) {
        this.station = value;
    }

    /**
     * Gets the value of the voltage property.
     * 
     */
    public double getVoltage() {
        return voltage;
    }

    /**
     * Sets the value of the voltage property.
     * 
     */
    public void setVoltage(double value) {
        this.voltage = value;
    }

    /**
     * Gets the value of the equipName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEquipName() {
        return equipName;
    }

    /**
     * Sets the value of the equipName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEquipName(String value) {
        this.equipName = value;
    }

}
