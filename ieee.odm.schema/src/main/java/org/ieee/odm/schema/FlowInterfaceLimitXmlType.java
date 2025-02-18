
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FlowInterfaceLimitXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FlowInterfaceLimitXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.ieee.org/odm/Schema/2008}FlowInterfaceEnumType" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="refDirExportLimit" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerXmlType"/&gt;
 *         &lt;element name="OppsiteRefDirImportLimit" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerXmlType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlowInterfaceLimitXmlType", propOrder = {
    "type",
    "status",
    "refDirExportLimit",
    "oppsiteRefDirImportLimit"
})
public class FlowInterfaceLimitXmlType {

    @XmlSchemaType(name = "string")
    protected FlowInterfaceEnumType type;
    protected boolean status;
    @XmlElement(required = true)
    protected ActivePowerXmlType refDirExportLimit;
    @XmlElement(name = "OppsiteRefDirImportLimit", required = true)
    protected ActivePowerXmlType oppsiteRefDirImportLimit;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link FlowInterfaceEnumType }
     *     
     */
    public FlowInterfaceEnumType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlowInterfaceEnumType }
     *     
     */
    public void setType(FlowInterfaceEnumType value) {
        this.type = value;
    }

    /**
     * Gets the value of the status property.
     * 
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setStatus(boolean value) {
        this.status = value;
    }

    /**
     * Gets the value of the refDirExportLimit property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public ActivePowerXmlType getRefDirExportLimit() {
        return refDirExportLimit;
    }

    /**
     * Sets the value of the refDirExportLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public void setRefDirExportLimit(ActivePowerXmlType value) {
        this.refDirExportLimit = value;
    }

    /**
     * Gets the value of the oppsiteRefDirImportLimit property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public ActivePowerXmlType getOppsiteRefDirImportLimit() {
        return oppsiteRefDirImportLimit;
    }

    /**
     * Sets the value of the oppsiteRefDirImportLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public void setOppsiteRefDirImportLimit(ActivePowerXmlType value) {
        this.oppsiteRefDirImportLimit = value;
    }

}
