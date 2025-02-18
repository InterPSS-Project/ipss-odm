
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivePowerLimitXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActivePowerLimitXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LimitXmlType"&gt;
 *       &lt;attribute name="unit" use="required" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerUnitType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivePowerLimitXmlType")
public class ActivePowerLimitXmlType
    extends LimitXmlType
{

    @XmlAttribute(name = "unit", required = true)
    protected ActivePowerUnitType unit;

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerUnitType }
     *     
     */
    public ActivePowerUnitType getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerUnitType }
     *     
     */
    public void setUnit(ActivePowerUnitType value) {
        this.unit = value;
    }

}
