//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.01 at 09:47:29 PM PDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GridComputingXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GridComputingXmlType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enableGridRun" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="remoteJobCreation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="timeout" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="remoteNodeDebug" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="remoteNodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aclfOption" type="{http://www.interpss.org/Schema/odm/2008}GridAclfOptionXmlType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GridComputingXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "enableGridRun",
    "remoteJobCreation",
    "timeout",
    "remoteNodeDebug",
    "remoteNodeName",
    "aclfOption"
})
public class GridComputingXmlType {

    protected boolean enableGridRun;
    protected Boolean remoteJobCreation;
    protected Long timeout;
    protected Boolean remoteNodeDebug;
    protected String remoteNodeName;
    protected GridAclfOptionXmlType aclfOption;

    /**
     * Gets the value of the enableGridRun property.
     * 
     */
    public boolean isEnableGridRun() {
        return enableGridRun;
    }

    /**
     * Sets the value of the enableGridRun property.
     * 
     */
    public void setEnableGridRun(boolean value) {
        this.enableGridRun = value;
    }

    /**
     * Gets the value of the remoteJobCreation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRemoteJobCreation() {
        return remoteJobCreation;
    }

    /**
     * Sets the value of the remoteJobCreation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoteJobCreation(Boolean value) {
        this.remoteJobCreation = value;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTimeout(Long value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the remoteNodeDebug property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRemoteNodeDebug() {
        return remoteNodeDebug;
    }

    /**
     * Sets the value of the remoteNodeDebug property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoteNodeDebug(Boolean value) {
        this.remoteNodeDebug = value;
    }

    /**
     * Gets the value of the remoteNodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteNodeName() {
        return remoteNodeName;
    }

    /**
     * Sets the value of the remoteNodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteNodeName(String value) {
        this.remoteNodeName = value;
    }

    /**
     * Gets the value of the aclfOption property.
     * 
     * @return
     *     possible object is
     *     {@link GridAclfOptionXmlType }
     *     
     */
    public GridAclfOptionXmlType getAclfOption() {
        return aclfOption;
    }

    /**
     * Sets the value of the aclfOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link GridAclfOptionXmlType }
     *     
     */
    public void setAclfOption(GridAclfOptionXmlType value) {
        this.aclfOption = value;
    }

}
