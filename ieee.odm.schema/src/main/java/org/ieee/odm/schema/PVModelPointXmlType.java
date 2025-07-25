//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			Data structure for defining PV model data point
 * 		
 * 
 * <p>Java class for PVModelPointXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PVModelPointXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="volt" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="amp" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PVModelPointXmlType")
public class PVModelPointXmlType {

    @XmlAttribute(name = "volt", required = true)
    protected double volt;
    @XmlAttribute(name = "amp", required = true)
    protected double amp;

    /**
     * Gets the value of the volt property.
     * 
     */
    public double getVolt() {
        return volt;
    }

    /**
     * Sets the value of the volt property.
     * 
     */
    public void setVolt(double value) {
        this.volt = value;
    }

    /**
     * Gets the value of the amp property.
     * 
     */
    public double getAmp() {
        return amp;
    }

    /**
     * Sets the value of the amp property.
     * 
     */
    public void setAmp(double value) {
        this.amp = value;
    }

}
