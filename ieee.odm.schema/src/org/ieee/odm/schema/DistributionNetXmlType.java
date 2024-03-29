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
 * 
 * 		
 * 
 * <p>Java class for DistributionNetXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributionNetXmlType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NetworkXmlType">
 *       &lt;sequence>
 *         &lt;element name="positiveSeqDataOnly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="scStd" type="{http://www.ieee.org/odm/Schema/2008}ScAnalysisStdEnumType"/>
 *         &lt;element name="scPoint" type="{http://www.ieee.org/odm/Schema/2008}DistScPointXmlType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionNetXmlType", propOrder = {
    "positiveSeqDataOnly",
    "scStd",
    "scPoint"
})
public class DistributionNetXmlType
    extends NetworkXmlType
{

    protected boolean positiveSeqDataOnly;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ScAnalysisStdEnumType scStd;
    protected List<DistScPointXmlType> scPoint;

    /**
     * Gets the value of the positiveSeqDataOnly property.
     * 
     */
    public boolean isPositiveSeqDataOnly() {
        return positiveSeqDataOnly;
    }

    /**
     * Sets the value of the positiveSeqDataOnly property.
     * 
     */
    public void setPositiveSeqDataOnly(boolean value) {
        this.positiveSeqDataOnly = value;
    }

    /**
     * Gets the value of the scStd property.
     * 
     * @return
     *     possible object is
     *     {@link ScAnalysisStdEnumType }
     *     
     */
    public ScAnalysisStdEnumType getScStd() {
        return scStd;
    }

    /**
     * Sets the value of the scStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScAnalysisStdEnumType }
     *     
     */
    public void setScStd(ScAnalysisStdEnumType value) {
        this.scStd = value;
    }

    /**
     * Gets the value of the scPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DistScPointXmlType }
     * 
     * 
     */
    public List<DistScPointXmlType> getScPoint() {
        if (scPoint == null) {
            scPoint = new ArrayList<DistScPointXmlType>();
        }
        return this.scPoint;
    }

}
