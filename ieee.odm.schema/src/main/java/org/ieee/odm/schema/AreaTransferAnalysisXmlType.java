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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AreaTransferAnalysisXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AreaTransferAnalysisXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.interpss.org/Schema/odm/2008}NodeTransferAnalysisXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fromArea" type="{http://www.ieee.org/odm/Schema/2008}NetAreaXmlType"/&gt;
 *         &lt;element name="toArea" type="{http://www.ieee.org/odm/Schema/2008}NetAreaXmlType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AreaTransferAnalysisXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "fromArea",
    "toArea"
})
public class AreaTransferAnalysisXmlType
    extends NodeTransferAnalysisXmlType
{

    @XmlElement(required = true)
    protected NetAreaXmlType fromArea;
    @XmlElement(required = true)
    protected NetAreaXmlType toArea;

    /**
     * Gets the value of the fromArea property.
     * 
     * @return
     *     possible object is
     *     {@link NetAreaXmlType }
     *     
     */
    public NetAreaXmlType getFromArea() {
        return fromArea;
    }

    /**
     * Sets the value of the fromArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetAreaXmlType }
     *     
     */
    public void setFromArea(NetAreaXmlType value) {
        this.fromArea = value;
    }

    /**
     * Gets the value of the toArea property.
     * 
     * @return
     *     possible object is
     *     {@link NetAreaXmlType }
     *     
     */
    public NetAreaXmlType getToArea() {
        return toArea;
    }

    /**
     * Sets the value of the toArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetAreaXmlType }
     *     
     */
    public void setToArea(NetAreaXmlType value) {
        this.toArea = value;
    }

}
