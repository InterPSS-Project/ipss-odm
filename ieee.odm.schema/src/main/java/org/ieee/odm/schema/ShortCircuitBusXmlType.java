
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		SC Bus with Aclf info
 * 		
 * 
 * <p>Java class for ShortCircuitBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShortCircuitBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadflowBusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="swithedShuntLoadZeroY" type="{http://www.ieee.org/odm/Schema/2008}YXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="scCode" type="{http://www.ieee.org/odm/Schema/2008}ShortCircuitBusEnumType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShortCircuitBusXmlType", propOrder = {
    "swithedShuntLoadZeroY"
})
@XmlSeeAlso({
    DStabBusXmlType.class
})
public class ShortCircuitBusXmlType
    extends LoadflowBusXmlType
{

    protected YXmlType swithedShuntLoadZeroY;
    @XmlAttribute(name = "scCode")
    protected ShortCircuitBusEnumType scCode;

    /**
     * Gets the value of the swithedShuntLoadZeroY property.
     * 
     * @return
     *     possible object is
     *     {@link YXmlType }
     *     
     */
    public YXmlType getSwithedShuntLoadZeroY() {
        return swithedShuntLoadZeroY;
    }

    /**
     * Sets the value of the swithedShuntLoadZeroY property.
     * 
     * @param value
     *     allowed object is
     *     {@link YXmlType }
     *     
     */
    public void setSwithedShuntLoadZeroY(YXmlType value) {
        this.swithedShuntLoadZeroY = value;
    }

    /**
     * Gets the value of the scCode property.
     * 
     * @return
     *     possible object is
     *     {@link ShortCircuitBusEnumType }
     *     
     */
    public ShortCircuitBusEnumType getScCode() {
        return scCode;
    }

    /**
     * Sets the value of the scCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShortCircuitBusEnumType }
     *     
     */
    public void setScCode(ShortCircuitBusEnumType value) {
        this.scCode = value;
    }

}
