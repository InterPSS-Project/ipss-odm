//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IpssAclfAlgorithmXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IpssAclfAlgorithmXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}AclfAlgorithmXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="customLfMethod" type="{http://www.interpss.org/Schema/odm/2008}CustomLfMethodXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpssAclfAlgorithmXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "customLfMethod"
})
public class IpssAclfAlgorithmXmlType
    extends AclfAlgorithmXmlType
{

    protected CustomLfMethodXmlType customLfMethod;

    /**
     * Gets the value of the customLfMethod property.
     * 
     * @return
     *     possible object is
     *     {@link CustomLfMethodXmlType }
     *     
     */
    public CustomLfMethodXmlType getCustomLfMethod() {
        return customLfMethod;
    }

    /**
     * Sets the value of the customLfMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomLfMethodXmlType }
     *     
     */
    public void setCustomLfMethod(CustomLfMethodXmlType value) {
        this.customLfMethod = value;
    }

}
