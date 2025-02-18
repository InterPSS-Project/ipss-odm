
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LoadCharacteristicModelListXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoadCharacteristicModelListXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IEEEStaticLoad" type="{http://www.ieee.org/odm/Schema/2008}DynamicLoadIEEEStaticLoadXmlType" minOccurs="0"/&gt;
 *         &lt;element name="ComplexLoad" type="{http://www.ieee.org/odm/Schema/2008}DynamicLoadComplexLoadXmlType" minOccurs="0"/&gt;
 *         &lt;element name="IndMotor5" type="{http://www.ieee.org/odm/Schema/2008}DynamicLoadInductionMotorCIM5XmlType" minOccurs="0"/&gt;
 *         &lt;element name="CMPLDW" type="{http://www.ieee.org/odm/Schema/2008}DynamicLoadCMPLDWXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoadCharacteristicModelListXmlType", propOrder = {
    "ieeeStaticLoad",
    "complexLoad",
    "indMotor5",
    "cmpldw"
})
public class LoadCharacteristicModelListXmlType {

    @XmlElement(name = "IEEEStaticLoad")
    protected DynamicLoadIEEEStaticLoadXmlType ieeeStaticLoad;
    @XmlElement(name = "ComplexLoad")
    protected DynamicLoadComplexLoadXmlType complexLoad;
    @XmlElement(name = "IndMotor5")
    protected DynamicLoadInductionMotorCIM5XmlType indMotor5;
    @XmlElement(name = "CMPLDW")
    protected DynamicLoadCMPLDWXmlType cmpldw;

    /**
     * Gets the value of the ieeeStaticLoad property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicLoadIEEEStaticLoadXmlType }
     *     
     */
    public DynamicLoadIEEEStaticLoadXmlType getIEEEStaticLoad() {
        return ieeeStaticLoad;
    }

    /**
     * Sets the value of the ieeeStaticLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicLoadIEEEStaticLoadXmlType }
     *     
     */
    public void setIEEEStaticLoad(DynamicLoadIEEEStaticLoadXmlType value) {
        this.ieeeStaticLoad = value;
    }

    /**
     * Gets the value of the complexLoad property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicLoadComplexLoadXmlType }
     *     
     */
    public DynamicLoadComplexLoadXmlType getComplexLoad() {
        return complexLoad;
    }

    /**
     * Sets the value of the complexLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicLoadComplexLoadXmlType }
     *     
     */
    public void setComplexLoad(DynamicLoadComplexLoadXmlType value) {
        this.complexLoad = value;
    }

    /**
     * Gets the value of the indMotor5 property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicLoadInductionMotorCIM5XmlType }
     *     
     */
    public DynamicLoadInductionMotorCIM5XmlType getIndMotor5() {
        return indMotor5;
    }

    /**
     * Sets the value of the indMotor5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicLoadInductionMotorCIM5XmlType }
     *     
     */
    public void setIndMotor5(DynamicLoadInductionMotorCIM5XmlType value) {
        this.indMotor5 = value;
    }

    /**
     * Gets the value of the cmpldw property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicLoadCMPLDWXmlType }
     *     
     */
    public DynamicLoadCMPLDWXmlType getCMPLDW() {
        return cmpldw;
    }

    /**
     * Sets the value of the cmpldw property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicLoadCMPLDWXmlType }
     *     
     */
    public void setCMPLDW(DynamicLoadCMPLDWXmlType value) {
        this.cmpldw = value;
    }

}
