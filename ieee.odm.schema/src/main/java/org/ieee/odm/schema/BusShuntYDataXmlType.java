
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusShuntYDataXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BusShuntYDataXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.ieee.org/odm/Schema/2008}equivY" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.ieee.org/odm/Schema/2008}contributeShuntY" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusShuntYDataXmlType", propOrder = {
    "equivY",
    "contributeShuntY"
})
public class BusShuntYDataXmlType {

    protected YXmlType equivY;
    protected List<LoadflowShuntYDataXmlType> contributeShuntY;

    /**
     * Gets the value of the equivY property.
     * 
     * @return
     *     possible object is
     *     {@link YXmlType }
     *     
     */
    public YXmlType getEquivY() {
        return equivY;
    }

    /**
     * Sets the value of the equivY property.
     * 
     * @param value
     *     allowed object is
     *     {@link YXmlType }
     *     
     */
    public void setEquivY(YXmlType value) {
        this.equivY = value;
    }

    /**
     * Gets the value of the contributeShuntY property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contributeShuntY property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContributeShuntY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LoadflowShuntYDataXmlType }
     * 
     * 
     */
    public List<LoadflowShuntYDataXmlType> getContributeShuntY() {
        if (contributeShuntY == null) {
            contributeShuntY = new ArrayList<LoadflowShuntYDataXmlType>();
        }
        return this.contributeShuntY;
    }

}
