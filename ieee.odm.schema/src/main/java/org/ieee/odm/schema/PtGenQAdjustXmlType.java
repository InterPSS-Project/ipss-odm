
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PtGenQAdjustXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PtGenQAdjustXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lfAssistGenFilename" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="noRunsLfAssist" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="lfAssistAdjTolerance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PtGenQAdjustXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "lfAssistGenFilename",
    "noRunsLfAssist",
    "lfAssistAdjTolerance"
})
public class PtGenQAdjustXmlType {

    @XmlElement(required = true)
    protected String lfAssistGenFilename;
    protected int noRunsLfAssist;
    protected Double lfAssistAdjTolerance;

    /**
     * Gets the value of the lfAssistGenFilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLfAssistGenFilename() {
        return lfAssistGenFilename;
    }

    /**
     * Sets the value of the lfAssistGenFilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLfAssistGenFilename(String value) {
        this.lfAssistGenFilename = value;
    }

    /**
     * Gets the value of the noRunsLfAssist property.
     * 
     */
    public int getNoRunsLfAssist() {
        return noRunsLfAssist;
    }

    /**
     * Sets the value of the noRunsLfAssist property.
     * 
     */
    public void setNoRunsLfAssist(int value) {
        this.noRunsLfAssist = value;
    }

    /**
     * Gets the value of the lfAssistAdjTolerance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLfAssistAdjTolerance() {
        return lfAssistAdjTolerance;
    }

    /**
     * Sets the value of the lfAssistAdjTolerance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLfAssistAdjTolerance(Double value) {
        this.lfAssistAdjTolerance = value;
    }

}
