
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AclfLoadChangeXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AclfLoadChangeXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeChange" type="{http://www.ieee.org/odm/Schema/2008}AclfLoadCodeChangeXmlType" minOccurs="0"/&gt;
 *         &lt;element name="valueChange" type="{http://www.ieee.org/odm/Schema/2008}ComplexValueChangeXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AclfLoadChangeXmlType", propOrder = {
    "codeChange",
    "valueChange"
})
public class AclfLoadChangeXmlType {

    protected AclfLoadCodeChangeXmlType codeChange;
    protected ComplexValueChangeXmlType valueChange;

    /**
     * Gets the value of the codeChange property.
     * 
     * @return
     *     possible object is
     *     {@link AclfLoadCodeChangeXmlType }
     *     
     */
    public AclfLoadCodeChangeXmlType getCodeChange() {
        return codeChange;
    }

    /**
     * Sets the value of the codeChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link AclfLoadCodeChangeXmlType }
     *     
     */
    public void setCodeChange(AclfLoadCodeChangeXmlType value) {
        this.codeChange = value;
    }

    /**
     * Gets the value of the valueChange property.
     * 
     * @return
     *     possible object is
     *     {@link ComplexValueChangeXmlType }
     *     
     */
    public ComplexValueChangeXmlType getValueChange() {
        return valueChange;
    }

    /**
     * Sets the value of the valueChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexValueChangeXmlType }
     *     
     */
    public void setValueChange(ComplexValueChangeXmlType value) {
        this.valueChange = value;
    }

}
