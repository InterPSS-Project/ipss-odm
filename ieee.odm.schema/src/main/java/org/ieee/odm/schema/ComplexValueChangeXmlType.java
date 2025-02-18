
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ComplexValueChangeXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ComplexValueChangeXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}ValueChangeXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.ieee.org/odm/Schema/2008}ComplexXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexValueChangeXmlType", propOrder = {
    "value"
})
public class ComplexValueChangeXmlType
    extends ValueChangeXmlType
{

    protected ComplexXmlType value;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link ComplexXmlType }
     *     
     */
    public ComplexXmlType getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexXmlType }
     *     
     */
    public void setValue(ComplexXmlType value) {
        this.value = value;
    }

}
