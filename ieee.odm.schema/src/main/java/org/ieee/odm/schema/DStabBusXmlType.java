
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		
 * 
 * <p>Java class for DStabBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DStabBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}ShortCircuitBusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="busRelayList" type="{http://www.ieee.org/odm/Schema/2008}RelayModelXmlType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DStabBusXmlType", propOrder = {
    "busRelayList"
})
public class DStabBusXmlType
    extends ShortCircuitBusXmlType
{

    protected List<RelayModelXmlType> busRelayList;

    /**
     * Gets the value of the busRelayList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the busRelayList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusRelayList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelayModelXmlType }
     * 
     * 
     */
    public List<RelayModelXmlType> getBusRelayList() {
        if (busRelayList == null) {
            busRelayList = new ArrayList<RelayModelXmlType>();
        }
        return this.busRelayList;
    }

}
