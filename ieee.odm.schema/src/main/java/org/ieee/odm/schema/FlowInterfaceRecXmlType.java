
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         	Interface record is for power flow interface limit checking purpose.
 *         
 * 
 * <p>Java class for FlowInterfaceRecXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FlowInterfaceRecXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}IDRecordXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="branchList" type="{http://www.ieee.org/odm/Schema/2008}FlowInterfaceBranchXmlType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="onPeakLimit" type="{http://www.ieee.org/odm/Schema/2008}FlowInterfaceLimitXmlType"/&gt;
 *         &lt;element name="offPeakLimit" type="{http://www.ieee.org/odm/Schema/2008}FlowInterfaceLimitXmlType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="season" type="{http://www.ieee.org/odm/Schema/2008}SeasonEnumType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowInterfaceRecXmlType", propOrder = {
    "branchList",
    "onPeakLimit",
    "offPeakLimit"
})
public class FlowInterfaceRecXmlType
    extends IDRecordXmlType
{

    @XmlElement(required = true)
    protected List<FlowInterfaceBranchXmlType> branchList;
    @XmlElement(required = true)
    protected FlowInterfaceLimitXmlType onPeakLimit;
    @XmlElement(required = true)
    protected FlowInterfaceLimitXmlType offPeakLimit;
    @XmlAttribute(name = "season")
    protected SeasonEnumType season;

    /**
     * Gets the value of the branchList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the branchList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBranchList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowInterfaceBranchXmlType }
     * 
     * 
     */
    public List<FlowInterfaceBranchXmlType> getBranchList() {
        if (branchList == null) {
            branchList = new ArrayList<FlowInterfaceBranchXmlType>();
        }
        return this.branchList;
    }

    /**
     * Gets the value of the onPeakLimit property.
     * 
     * @return
     *     possible object is
     *     {@link FlowInterfaceLimitXmlType }
     *     
     */
    public FlowInterfaceLimitXmlType getOnPeakLimit() {
        return onPeakLimit;
    }

    /**
     * Sets the value of the onPeakLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlowInterfaceLimitXmlType }
     *     
     */
    public void setOnPeakLimit(FlowInterfaceLimitXmlType value) {
        this.onPeakLimit = value;
    }

    /**
     * Gets the value of the offPeakLimit property.
     * 
     * @return
     *     possible object is
     *     {@link FlowInterfaceLimitXmlType }
     *     
     */
    public FlowInterfaceLimitXmlType getOffPeakLimit() {
        return offPeakLimit;
    }

    /**
     * Sets the value of the offPeakLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlowInterfaceLimitXmlType }
     *     
     */
    public void setOffPeakLimit(FlowInterfaceLimitXmlType value) {
        this.offPeakLimit = value;
    }

    /**
     * Gets the value of the season property.
     * 
     * @return
     *     possible object is
     *     {@link SeasonEnumType }
     *     
     */
    public SeasonEnumType getSeason() {
        return season;
    }

    /**
     * Sets the value of the season property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeasonEnumType }
     *     
     */
    public void setSeason(SeasonEnumType value) {
        this.season = value;
    }

}
