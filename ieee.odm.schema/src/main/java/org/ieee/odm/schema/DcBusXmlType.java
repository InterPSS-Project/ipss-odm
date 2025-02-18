
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		
 * 
 * <p>Java class for DcBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DcBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="voltage" type="{http://www.ieee.org/odm/Schema/2008}VoltageXmlType" minOccurs="0"/&gt;
 *         &lt;element name="power" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerXmlType" minOccurs="0"/&gt;
 *         &lt;element name="load" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerXmlType" minOccurs="0"/&gt;
 *         &lt;element name="pvModule" type="{http://www.ieee.org/odm/Schema/2008}PVModuleXmlType" minOccurs="0"/&gt;
 *         &lt;element name="inverter" type="{http://www.ieee.org/odm/Schema/2008}DcAcInverterXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" use="required" type="{http://www.ieee.org/odm/Schema/2008}DcBusCodeEnumType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcBusXmlType", propOrder = {
    "voltage",
    "power",
    "load",
    "pvModule",
    "inverter"
})
public class DcBusXmlType
    extends BusXmlType
{

    protected VoltageXmlType voltage;
    protected ActivePowerXmlType power;
    protected ActivePowerXmlType load;
    protected PVModuleXmlType pvModule;
    protected DcAcInverterXmlType inverter;
    @XmlAttribute(name = "code", required = true)
    protected DcBusCodeEnumType code;

    /**
     * Gets the value of the voltage property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageXmlType }
     *     
     */
    public VoltageXmlType getVoltage() {
        return voltage;
    }

    /**
     * Sets the value of the voltage property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageXmlType }
     *     
     */
    public void setVoltage(VoltageXmlType value) {
        this.voltage = value;
    }

    /**
     * Gets the value of the power property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public ActivePowerXmlType getPower() {
        return power;
    }

    /**
     * Sets the value of the power property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public void setPower(ActivePowerXmlType value) {
        this.power = value;
    }

    /**
     * Gets the value of the load property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public ActivePowerXmlType getLoad() {
        return load;
    }

    /**
     * Sets the value of the load property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public void setLoad(ActivePowerXmlType value) {
        this.load = value;
    }

    /**
     * Gets the value of the pvModule property.
     * 
     * @return
     *     possible object is
     *     {@link PVModuleXmlType }
     *     
     */
    public PVModuleXmlType getPvModule() {
        return pvModule;
    }

    /**
     * Sets the value of the pvModule property.
     * 
     * @param value
     *     allowed object is
     *     {@link PVModuleXmlType }
     *     
     */
    public void setPvModule(PVModuleXmlType value) {
        this.pvModule = value;
    }

    /**
     * Gets the value of the inverter property.
     * 
     * @return
     *     possible object is
     *     {@link DcAcInverterXmlType }
     *     
     */
    public DcAcInverterXmlType getInverter() {
        return inverter;
    }

    /**
     * Sets the value of the inverter property.
     * 
     * @param value
     *     allowed object is
     *     {@link DcAcInverterXmlType }
     *     
     */
    public void setInverter(DcAcInverterXmlType value) {
        this.inverter = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link DcBusCodeEnumType }
     *     
     */
    public DcBusCodeEnumType getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link DcBusCodeEnumType }
     *     
     */
    public void setCode(DcBusCodeEnumType value) {
        this.code = value;
    }

}
