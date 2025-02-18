
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApparentPowerXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApparentPowerXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseDoubleXmlType"&gt;
 *       &lt;attribute name="unit" use="required" type="{http://www.ieee.org/odm/Schema/2008}ApparentPowerUnitType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApparentPowerXmlType")
public class ApparentPowerXmlType
    extends BaseDoubleXmlType
{

    @XmlAttribute(name = "unit", required = true)
    protected ApparentPowerUnitType unit;

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link ApparentPowerUnitType }
     *     
     */
    public ApparentPowerUnitType getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApparentPowerUnitType }
     *     
     */
    public void setUnit(ApparentPowerUnitType value) {
        this.unit = value;
    }

}
