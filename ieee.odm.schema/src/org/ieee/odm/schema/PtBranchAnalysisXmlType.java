//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PtBranchAnalysisXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PtBranchAnalysisXmlType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hour" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.interpss.org/Schema/odm/2008}PtBranchAnalysisEnumType"/>
 *         &lt;element name="branch" type="{http://www.ieee.org/odm/Schema/2008}BranchRefXmlType" maxOccurs="unbounded"/>
 *         &lt;element name="outageScheduleFilename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PtBranchAnalysisXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "hour",
    "type",
    "branch",
    "outageScheduleFilename"
})
public class PtBranchAnalysisXmlType {

    @XmlElement(required = true)
    protected String hour;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PtBranchAnalysisEnumType type;
    @XmlElement(required = true)
    protected List<BranchRefXmlType> branch;
    protected String outageScheduleFilename;

    /**
     * Gets the value of the hour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHour() {
        return hour;
    }

    /**
     * Sets the value of the hour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHour(String value) {
        this.hour = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link PtBranchAnalysisEnumType }
     *     
     */
    public PtBranchAnalysisEnumType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link PtBranchAnalysisEnumType }
     *     
     */
    public void setType(PtBranchAnalysisEnumType value) {
        this.type = value;
    }

    /**
     * Gets the value of the branch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the branch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBranch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BranchRefXmlType }
     * 
     * 
     */
    public List<BranchRefXmlType> getBranch() {
        if (branch == null) {
            branch = new ArrayList<BranchRefXmlType>();
        }
        return this.branch;
    }

    /**
     * Gets the value of the outageScheduleFilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutageScheduleFilename() {
        return outageScheduleFilename;
    }

    /**
     * Sets the value of the outageScheduleFilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutageScheduleFilename(String value) {
        this.outageScheduleFilename = value;
    }

}
