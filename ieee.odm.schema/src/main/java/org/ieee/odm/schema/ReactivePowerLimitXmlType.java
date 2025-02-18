
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReactivePowerLimitXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReactivePowerLimitXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LimitXmlType"&gt;
 *       &lt;attribute name="unit" use="required" type="{http://www.ieee.org/odm/Schema/2008}ReactivePowerUnitType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReactivePowerLimitXmlType")
public class ReactivePowerLimitXmlType
    extends LimitXmlType
{

    @XmlAttribute(name = "unit", required = true)
    protected ReactivePowerUnitType unit;

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link ReactivePowerUnitType }
     *     
     */
    public ReactivePowerUnitType getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReactivePowerUnitType }
     *     
     */
    public void setUnit(ReactivePowerUnitType value) {
        this.unit = value;
    }

}
