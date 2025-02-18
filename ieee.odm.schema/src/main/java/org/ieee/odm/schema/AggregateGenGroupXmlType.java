
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AggregateGenGroupXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AggregateGenGroupXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}IDRecordXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="apBus" type="{http://www.interpss.org/Schema/odm/2008}AggregateGenBusXmlType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateGenGroupXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "apBus"
})
public class AggregateGenGroupXmlType
    extends IDRecordXmlType
{

    @XmlElement(required = true)
    protected List<AggregateGenBusXmlType> apBus;

    /**
     * Gets the value of the apBus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apBus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApBus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AggregateGenBusXmlType }
     * 
     * 
     */
    public List<AggregateGenBusXmlType> getApBus() {
        if (apBus == null) {
            apBus = new ArrayList<AggregateGenBusXmlType>();
        }
        return this.apBus;
    }

}
