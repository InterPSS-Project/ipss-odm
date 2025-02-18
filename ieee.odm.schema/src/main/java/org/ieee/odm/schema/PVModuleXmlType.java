
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PVModuleXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PVModuleXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pvModuleItem" type="{http://www.ieee.org/odm/Schema/2008}PVModuleItemXmlType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dataType" use="required" type="{http://www.ieee.org/odm/Schema/2008}PVModuleDataEnumType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PVModuleXmlType", propOrder = {
    "pvModuleItem"
})
public class PVModuleXmlType {

    @XmlElement(required = true)
    protected List<PVModuleItemXmlType> pvModuleItem;
    @XmlAttribute(name = "dataType", required = true)
    protected PVModuleDataEnumType dataType;

    /**
     * Gets the value of the pvModuleItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pvModuleItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPvModuleItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PVModuleItemXmlType }
     * 
     * 
     */
    public List<PVModuleItemXmlType> getPvModuleItem() {
        if (pvModuleItem == null) {
            pvModuleItem = new ArrayList<PVModuleItemXmlType>();
        }
        return this.pvModuleItem;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link PVModuleDataEnumType }
     *     
     */
    public PVModuleDataEnumType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PVModuleDataEnumType }
     *     
     */
    public void setDataType(PVModuleDataEnumType value) {
        this.dataType = value;
    }

}
