//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShortCircuitLoadDataXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShortCircuitLoadDataXmlType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadflowLoadDataXmlType">
 *       &lt;sequence>
 *         &lt;element name="shuntLoadNegativeY" type="{http://www.ieee.org/odm/Schema/2008}YXmlType" minOccurs="0"/>
 *         &lt;element name="shuntLoadZeroY" type="{http://www.ieee.org/odm/Schema/2008}YXmlType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShortCircuitLoadDataXmlType", propOrder = {
    "shuntLoadNegativeY",
    "shuntLoadZeroY"
})
@XmlSeeAlso({
    DStabLoadDataXmlType.class
})
public class ShortCircuitLoadDataXmlType
    extends LoadflowLoadDataXmlType
{

    protected YXmlType shuntLoadNegativeY;
    protected YXmlType shuntLoadZeroY;

    /**
     * Gets the value of the shuntLoadNegativeY property.
     * 
     * @return
     *     possible object is
     *     {@link YXmlType }
     *     
     */
    public YXmlType getShuntLoadNegativeY() {
        return shuntLoadNegativeY;
    }

    /**
     * Sets the value of the shuntLoadNegativeY property.
     * 
     * @param value
     *     allowed object is
     *     {@link YXmlType }
     *     
     */
    public void setShuntLoadNegativeY(YXmlType value) {
        this.shuntLoadNegativeY = value;
    }

    /**
     * Gets the value of the shuntLoadZeroY property.
     * 
     * @return
     *     possible object is
     *     {@link YXmlType }
     *     
     */
    public YXmlType getShuntLoadZeroY() {
        return shuntLoadZeroY;
    }

    /**
     * Sets the value of the shuntLoadZeroY property.
     * 
     * @param value
     *     allowed object is
     *     {@link YXmlType }
     *     
     */
    public void setShuntLoadZeroY(YXmlType value) {
        this.shuntLoadZeroY = value;
    }

}
