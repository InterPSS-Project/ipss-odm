
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BreakerOverrideXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BreakerOverrideXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseBranchOverrideXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.ieee.org/odm/Schema/2008}BreakerOverrideEnumType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BreakerOverrideXmlType", propOrder = {
    "type"
})
public class BreakerOverrideXmlType
    extends BaseBranchOverrideXmlType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BreakerOverrideEnumType type;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link BreakerOverrideEnumType }
     *     
     */
    public BreakerOverrideEnumType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link BreakerOverrideEnumType }
     *     
     */
    public void setType(BreakerOverrideEnumType value) {
        this.type = value;
    }

}
