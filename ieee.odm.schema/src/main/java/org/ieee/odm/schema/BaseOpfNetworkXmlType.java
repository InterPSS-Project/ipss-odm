
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseOpfNetworkXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseOpfNetworkXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadflowNetXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="opfNetType" type="{http://www.ieee.org/odm/Schema/2008}OpfNetworkEnumType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseOpfNetworkXmlType", propOrder = {
    "opfNetType"
})
@XmlSeeAlso({
    OpfNetworkXmlType.class,
    OpfDclfNetworkXmlType.class
})
public abstract class BaseOpfNetworkXmlType
    extends LoadflowNetXmlType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected OpfNetworkEnumType opfNetType;

    /**
     * Gets the value of the opfNetType property.
     * 
     * @return
     *     possible object is
     *     {@link OpfNetworkEnumType }
     *     
     */
    public OpfNetworkEnumType getOpfNetType() {
        return opfNetType;
    }

    /**
     * Sets the value of the opfNetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpfNetworkEnumType }
     *     
     */
    public void setOpfNetType(OpfNetworkEnumType value) {
        this.opfNetType = value;
    }

}
