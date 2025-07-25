//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PtAclfAnalysisXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PtAclfAnalysisXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="hour" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tolerance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="genQAdjustment" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="genQAdjOption" type="{http://www.interpss.org/Schema/odm/2008}PtGenQAdjustXmlType" minOccurs="0"/&gt;
 *         &lt;element name="swingBusPQAlloc" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="genSwingAllocOption" type="{http://www.interpss.org/Schema/odm/2008}PtSwingAllocXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PtAclfAnalysisXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "hour",
    "tolerance",
    "genQAdjustment",
    "genQAdjOption",
    "swingBusPQAlloc",
    "genSwingAllocOption"
})
public class PtAclfAnalysisXmlType {

    @XmlElement(required = true)
    protected String hour;
    protected Double tolerance;
    protected boolean genQAdjustment;
    protected PtGenQAdjustXmlType genQAdjOption;
    protected boolean swingBusPQAlloc;
    protected PtSwingAllocXmlType genSwingAllocOption;

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
     * Gets the value of the tolerance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTolerance() {
        return tolerance;
    }

    /**
     * Sets the value of the tolerance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTolerance(Double value) {
        this.tolerance = value;
    }

    /**
     * Gets the value of the genQAdjustment property.
     * 
     */
    public boolean isGenQAdjustment() {
        return genQAdjustment;
    }

    /**
     * Sets the value of the genQAdjustment property.
     * 
     */
    public void setGenQAdjustment(boolean value) {
        this.genQAdjustment = value;
    }

    /**
     * Gets the value of the genQAdjOption property.
     * 
     * @return
     *     possible object is
     *     {@link PtGenQAdjustXmlType }
     *     
     */
    public PtGenQAdjustXmlType getGenQAdjOption() {
        return genQAdjOption;
    }

    /**
     * Sets the value of the genQAdjOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link PtGenQAdjustXmlType }
     *     
     */
    public void setGenQAdjOption(PtGenQAdjustXmlType value) {
        this.genQAdjOption = value;
    }

    /**
     * Gets the value of the swingBusPQAlloc property.
     * 
     */
    public boolean isSwingBusPQAlloc() {
        return swingBusPQAlloc;
    }

    /**
     * Sets the value of the swingBusPQAlloc property.
     * 
     */
    public void setSwingBusPQAlloc(boolean value) {
        this.swingBusPQAlloc = value;
    }

    /**
     * Gets the value of the genSwingAllocOption property.
     * 
     * @return
     *     possible object is
     *     {@link PtSwingAllocXmlType }
     *     
     */
    public PtSwingAllocXmlType getGenSwingAllocOption() {
        return genSwingAllocOption;
    }

    /**
     * Sets the value of the genSwingAllocOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link PtSwingAllocXmlType }
     *     
     */
    public void setGenSwingAllocOption(PtSwingAllocXmlType value) {
        this.genSwingAllocOption = value;
    }

}
