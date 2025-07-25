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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValueChangeXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValueChangeXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="changeAction" type="{http://www.ieee.org/odm/Schema/2008}ValueChangeActionEnumType"/&gt;
 *         &lt;element name="percent" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueChangeXmlType", propOrder = {
    "changeAction",
    "percent"
})
@XmlSeeAlso({
    ComplexValueChangeXmlType.class,
    DoubleValueChangeXmlType.class
})
public class ValueChangeXmlType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ValueChangeActionEnumType changeAction;
    protected Double percent;

    /**
     * Gets the value of the changeAction property.
     * 
     * @return
     *     possible object is
     *     {@link ValueChangeActionEnumType }
     *     
     */
    public ValueChangeActionEnumType getChangeAction() {
        return changeAction;
    }

    /**
     * Sets the value of the changeAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueChangeActionEnumType }
     *     
     */
    public void setChangeAction(ValueChangeActionEnumType value) {
        this.changeAction = value;
    }

    /**
     * Gets the value of the percent property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPercent() {
        return percent;
    }

    /**
     * Sets the value of the percent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPercent(Double value) {
        this.percent = value;
    }

}
