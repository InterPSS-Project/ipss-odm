
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Xfr3WBranchXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Xfr3WBranchXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}XfrBranchXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="z23" type="{http://www.ieee.org/odm/Schema/2008}ZXmlType" minOccurs="0"/&gt;
 *         &lt;element name="z31" type="{http://www.ieee.org/odm/Schema/2008}ZXmlType" minOccurs="0"/&gt;
 *         &lt;element name="tertTurnRatio" type="{http://www.ieee.org/odm/Schema/2008}TurnRatioXmlType" minOccurs="0"/&gt;
 *         &lt;element name="ratingLimit23" type="{http://www.ieee.org/odm/Schema/2008}BranchRatingLimitXmlType" minOccurs="0"/&gt;
 *         &lt;element name="ratingLimit13" type="{http://www.ieee.org/odm/Schema/2008}BranchRatingLimitXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="wind1OffLine" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="wind2OffLine" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="wind3OffLine" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Xfr3WBranchXmlType", propOrder = {
    "z23",
    "z31",
    "tertTurnRatio",
    "ratingLimit23",
    "ratingLimit13"
})
@XmlSeeAlso({
    Xfr3WShortCircuitXmlType.class,
    PSXfr3WBranchXmlType.class
})
public class Xfr3WBranchXmlType
    extends XfrBranchXmlType
{

    protected ZXmlType z23;
    protected ZXmlType z31;
    protected TurnRatioXmlType tertTurnRatio;
    protected BranchRatingLimitXmlType ratingLimit23;
    protected BranchRatingLimitXmlType ratingLimit13;
    @XmlAttribute(name = "wind1OffLine")
    protected Boolean wind1OffLine;
    @XmlAttribute(name = "wind2OffLine")
    protected Boolean wind2OffLine;
    @XmlAttribute(name = "wind3OffLine")
    protected Boolean wind3OffLine;

    /**
     * Gets the value of the z23 property.
     * 
     * @return
     *     possible object is
     *     {@link ZXmlType }
     *     
     */
    public ZXmlType getZ23() {
        return z23;
    }

    /**
     * Sets the value of the z23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZXmlType }
     *     
     */
    public void setZ23(ZXmlType value) {
        this.z23 = value;
    }

    /**
     * Gets the value of the z31 property.
     * 
     * @return
     *     possible object is
     *     {@link ZXmlType }
     *     
     */
    public ZXmlType getZ31() {
        return z31;
    }

    /**
     * Sets the value of the z31 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZXmlType }
     *     
     */
    public void setZ31(ZXmlType value) {
        this.z31 = value;
    }

    /**
     * Gets the value of the tertTurnRatio property.
     * 
     * @return
     *     possible object is
     *     {@link TurnRatioXmlType }
     *     
     */
    public TurnRatioXmlType getTertTurnRatio() {
        return tertTurnRatio;
    }

    /**
     * Sets the value of the tertTurnRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link TurnRatioXmlType }
     *     
     */
    public void setTertTurnRatio(TurnRatioXmlType value) {
        this.tertTurnRatio = value;
    }

    /**
     * Gets the value of the ratingLimit23 property.
     * 
     * @return
     *     possible object is
     *     {@link BranchRatingLimitXmlType }
     *     
     */
    public BranchRatingLimitXmlType getRatingLimit23() {
        return ratingLimit23;
    }

    /**
     * Sets the value of the ratingLimit23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchRatingLimitXmlType }
     *     
     */
    public void setRatingLimit23(BranchRatingLimitXmlType value) {
        this.ratingLimit23 = value;
    }

    /**
     * Gets the value of the ratingLimit13 property.
     * 
     * @return
     *     possible object is
     *     {@link BranchRatingLimitXmlType }
     *     
     */
    public BranchRatingLimitXmlType getRatingLimit13() {
        return ratingLimit13;
    }

    /**
     * Sets the value of the ratingLimit13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchRatingLimitXmlType }
     *     
     */
    public void setRatingLimit13(BranchRatingLimitXmlType value) {
        this.ratingLimit13 = value;
    }

    /**
     * Gets the value of the wind1OffLine property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isWind1OffLine() {
        if (wind1OffLine == null) {
            return false;
        } else {
            return wind1OffLine;
        }
    }

    /**
     * Sets the value of the wind1OffLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWind1OffLine(Boolean value) {
        this.wind1OffLine = value;
    }

    /**
     * Gets the value of the wind2OffLine property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isWind2OffLine() {
        if (wind2OffLine == null) {
            return false;
        } else {
            return wind2OffLine;
        }
    }

    /**
     * Sets the value of the wind2OffLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWind2OffLine(Boolean value) {
        this.wind2OffLine = value;
    }

    /**
     * Gets the value of the wind3OffLine property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isWind3OffLine() {
        if (wind3OffLine == null) {
            return false;
        } else {
            return wind3OffLine;
        }
    }

    /**
     * Sets the value of the wind3OffLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWind3OffLine(Boolean value) {
        this.wind3OffLine = value;
    }

}
