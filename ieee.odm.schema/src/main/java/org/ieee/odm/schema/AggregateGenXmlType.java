
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AggregateGenXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AggregateGenXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="aggregatePricingFilename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="apGroup" type="{http://www.interpss.org/Schema/odm/2008}AggregateGenGroupXmlType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateGenXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "aggregatePricingFilename",
    "apGroup"
})
public class AggregateGenXmlType
    extends NameTagXmlType
{

    protected String aggregatePricingFilename;
    @XmlElement(required = true)
    protected List<AggregateGenGroupXmlType> apGroup;

    /**
     * Gets the value of the aggregatePricingFilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAggregatePricingFilename() {
        return aggregatePricingFilename;
    }

    /**
     * Sets the value of the aggregatePricingFilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAggregatePricingFilename(String value) {
        this.aggregatePricingFilename = value;
    }

    /**
     * Gets the value of the apGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AggregateGenGroupXmlType }
     * 
     * 
     */
    public List<AggregateGenGroupXmlType> getApGroup() {
        if (apGroup == null) {
            apGroup = new ArrayList<AggregateGenGroupXmlType>();
        }
        return this.apGroup;
    }

}
