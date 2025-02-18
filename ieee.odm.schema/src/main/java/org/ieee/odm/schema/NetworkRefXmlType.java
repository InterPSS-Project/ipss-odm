
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * used to reference parent containing network.
 * 
 * <p>Java class for NetworkRefXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NetworkRefXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="netId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkRefXmlType")
public class NetworkRefXmlType
    extends NameTagXmlType
{

    @XmlAttribute(name = "netId", required = true)
    protected String netId;

    /**
     * Gets the value of the netId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Sets the value of the netId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetId(String value) {
        this.netId = value;
    }

}
