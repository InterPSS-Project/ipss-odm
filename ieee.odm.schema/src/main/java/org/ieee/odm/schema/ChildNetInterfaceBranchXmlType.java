
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChildNetInterfaceBranchXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChildNetInterfaceBranchXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="branch" type="{http://www.ieee.org/odm/Schema/2008}BranchIDRefXmlType"/&gt;
 *         &lt;element name="interfaceBusSide" type="{http://www.ieee.org/odm/Schema/2008}BranchBusSideEnumType"/&gt;
 *         &lt;element name="childNetSide" type="{http://www.ieee.org/odm/Schema/2008}BranchBusSideEnumType"/&gt;
 *         &lt;element name="interfaceBusIdChildNet" type="{http://www.ieee.org/odm/Schema/2008}BranchIDRefXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChildNetInterfaceBranchXmlType", propOrder = {
    "branch",
    "interfaceBusSide",
    "childNetSide",
    "interfaceBusIdChildNet"
})
public class ChildNetInterfaceBranchXmlType {

    @XmlElement(required = true)
    protected BranchIDRefXmlType branch;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BranchBusSideEnumType interfaceBusSide;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BranchBusSideEnumType childNetSide;
    protected BranchIDRefXmlType interfaceBusIdChildNet;

    /**
     * Gets the value of the branch property.
     * 
     * @return
     *     possible object is
     *     {@link BranchIDRefXmlType }
     *     
     */
    public BranchIDRefXmlType getBranch() {
        return branch;
    }

    /**
     * Sets the value of the branch property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchIDRefXmlType }
     *     
     */
    public void setBranch(BranchIDRefXmlType value) {
        this.branch = value;
    }

    /**
     * Gets the value of the interfaceBusSide property.
     * 
     * @return
     *     possible object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public BranchBusSideEnumType getInterfaceBusSide() {
        return interfaceBusSide;
    }

    /**
     * Sets the value of the interfaceBusSide property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public void setInterfaceBusSide(BranchBusSideEnumType value) {
        this.interfaceBusSide = value;
    }

    /**
     * Gets the value of the childNetSide property.
     * 
     * @return
     *     possible object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public BranchBusSideEnumType getChildNetSide() {
        return childNetSide;
    }

    /**
     * Sets the value of the childNetSide property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public void setChildNetSide(BranchBusSideEnumType value) {
        this.childNetSide = value;
    }

    /**
     * Gets the value of the interfaceBusIdChildNet property.
     * 
     * @return
     *     possible object is
     *     {@link BranchIDRefXmlType }
     *     
     */
    public BranchIDRefXmlType getInterfaceBusIdChildNet() {
        return interfaceBusIdChildNet;
    }

    /**
     * Sets the value of the interfaceBusIdChildNet property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchIDRefXmlType }
     *     
     */
    public void setInterfaceBusIdChildNet(BranchIDRefXmlType value) {
        this.interfaceBusIdChildNet = value;
    }

}
