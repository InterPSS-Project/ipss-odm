
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LineBranchInfoXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LineBranchInfoXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.ieee.org/odm/Schema/2008}LineBranchEnumType" minOccurs="0"/&gt;
 *         &lt;element name="length" type="{http://www.ieee.org/odm/Schema/2008}LengthXmlType" minOccurs="0"/&gt;
 *         &lt;element name="lossFactor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LineBranchInfoXmlType", propOrder = {
    "type",
    "length",
    "lossFactor"
})
public class LineBranchInfoXmlType {

    @XmlSchemaType(name = "string")
    protected LineBranchEnumType type;
    protected LengthXmlType length;
    protected Double lossFactor;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link LineBranchEnumType }
     *     
     */
    public LineBranchEnumType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link LineBranchEnumType }
     *     
     */
    public void setType(LineBranchEnumType value) {
        this.type = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link LengthXmlType }
     *     
     */
    public LengthXmlType getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthXmlType }
     *     
     */
    public void setLength(LengthXmlType value) {
        this.length = value;
    }

    /**
     * Gets the value of the lossFactor property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLossFactor() {
        return lossFactor;
    }

    /**
     * Sets the value of the lossFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLossFactor(Double value) {
        this.lossFactor = value;
    }

}
