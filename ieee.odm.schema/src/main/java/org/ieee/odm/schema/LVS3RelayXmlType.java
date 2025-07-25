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
 * 
 * 		Under voltage load shedding relay with branch transfer trip
 * 		
 * 
 * <p>Java class for LVS3RelayXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LVS3RelayXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}LoadRelayXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="f1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="t1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="tb1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="frac1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="f2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="t2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="tb2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="frac2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="f3" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="t3" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="tb3" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="frac3" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="f4" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="t4" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="tb4" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="frac4" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="f5" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="t5" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="tb5" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="frac5" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="ttb1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="ttb2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="FBus1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="TBus1" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ID1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FBus2" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="TBus2" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ID2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SC" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LVS3RelayXmlType", propOrder = {
    "f1",
    "t1",
    "tb1",
    "frac1",
    "f2",
    "t2",
    "tb2",
    "frac2",
    "f3",
    "t3",
    "tb3",
    "frac3",
    "f4",
    "t4",
    "tb4",
    "frac4",
    "f5",
    "t5",
    "tb5",
    "frac5",
    "ttb1",
    "ttb2",
    "fBus1",
    "tBus1",
    "id1",
    "fBus2",
    "tBus2",
    "id2",
    "sc"
})
public class LVS3RelayXmlType
    extends LoadRelayXmlType
{

    @XmlElement(defaultValue = "0.0")
    protected Double f1;
    @XmlElement(defaultValue = "0.0")
    protected Double t1;
    @XmlElement(defaultValue = "0.0")
    protected Double tb1;
    @XmlElement(defaultValue = "0.0")
    protected Double frac1;
    @XmlElement(defaultValue = "0.0")
    protected Double f2;
    @XmlElement(defaultValue = "0.0")
    protected Double t2;
    @XmlElement(defaultValue = "0.0")
    protected Double tb2;
    @XmlElement(defaultValue = "0.0")
    protected Double frac2;
    @XmlElement(defaultValue = "0.0")
    protected Double f3;
    @XmlElement(defaultValue = "0.0")
    protected Double t3;
    @XmlElement(defaultValue = "0.0")
    protected Double tb3;
    @XmlElement(defaultValue = "0.0")
    protected Double frac3;
    @XmlElement(defaultValue = "0.0")
    protected Double f4;
    @XmlElement(defaultValue = "0.0")
    protected Double t4;
    @XmlElement(defaultValue = "0.0")
    protected Double tb4;
    @XmlElement(defaultValue = "0.0")
    protected Double frac4;
    @XmlElement(defaultValue = "0.0")
    protected Double f5;
    @XmlElement(defaultValue = "0.0")
    protected Double t5;
    @XmlElement(defaultValue = "0.0")
    protected Double tb5;
    @XmlElement(defaultValue = "0.0")
    protected Double frac5;
    @XmlElement(defaultValue = "0.0")
    protected Double ttb1;
    @XmlElement(defaultValue = "0.0")
    protected Double ttb2;
    @XmlElement(name = "FBus1", defaultValue = "0")
    protected Integer fBus1;
    @XmlElement(name = "TBus1", defaultValue = "0")
    protected Integer tBus1;
    @XmlElement(name = "ID1", defaultValue = "0")
    protected String id1;
    @XmlElement(name = "FBus2", defaultValue = "0")
    protected Integer fBus2;
    @XmlElement(name = "TBus2", defaultValue = "0")
    protected Integer tBus2;
    @XmlElement(name = "ID2", defaultValue = "0")
    protected String id2;
    @XmlElement(name = "SC", defaultValue = "0")
    protected Integer sc;

    /**
     * Gets the value of the f1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getF1() {
        return f1;
    }

    /**
     * Sets the value of the f1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setF1(Double value) {
        this.f1 = value;
    }

    /**
     * Gets the value of the t1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getT1() {
        return t1;
    }

    /**
     * Sets the value of the t1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setT1(Double value) {
        this.t1 = value;
    }

    /**
     * Gets the value of the tb1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb1() {
        return tb1;
    }

    /**
     * Sets the value of the tb1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb1(Double value) {
        this.tb1 = value;
    }

    /**
     * Gets the value of the frac1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFrac1() {
        return frac1;
    }

    /**
     * Sets the value of the frac1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFrac1(Double value) {
        this.frac1 = value;
    }

    /**
     * Gets the value of the f2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getF2() {
        return f2;
    }

    /**
     * Sets the value of the f2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setF2(Double value) {
        this.f2 = value;
    }

    /**
     * Gets the value of the t2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getT2() {
        return t2;
    }

    /**
     * Sets the value of the t2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setT2(Double value) {
        this.t2 = value;
    }

    /**
     * Gets the value of the tb2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb2() {
        return tb2;
    }

    /**
     * Sets the value of the tb2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb2(Double value) {
        this.tb2 = value;
    }

    /**
     * Gets the value of the frac2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFrac2() {
        return frac2;
    }

    /**
     * Sets the value of the frac2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFrac2(Double value) {
        this.frac2 = value;
    }

    /**
     * Gets the value of the f3 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getF3() {
        return f3;
    }

    /**
     * Sets the value of the f3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setF3(Double value) {
        this.f3 = value;
    }

    /**
     * Gets the value of the t3 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getT3() {
        return t3;
    }

    /**
     * Sets the value of the t3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setT3(Double value) {
        this.t3 = value;
    }

    /**
     * Gets the value of the tb3 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb3() {
        return tb3;
    }

    /**
     * Sets the value of the tb3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb3(Double value) {
        this.tb3 = value;
    }

    /**
     * Gets the value of the frac3 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFrac3() {
        return frac3;
    }

    /**
     * Sets the value of the frac3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFrac3(Double value) {
        this.frac3 = value;
    }

    /**
     * Gets the value of the f4 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getF4() {
        return f4;
    }

    /**
     * Sets the value of the f4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setF4(Double value) {
        this.f4 = value;
    }

    /**
     * Gets the value of the t4 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getT4() {
        return t4;
    }

    /**
     * Sets the value of the t4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setT4(Double value) {
        this.t4 = value;
    }

    /**
     * Gets the value of the tb4 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb4() {
        return tb4;
    }

    /**
     * Sets the value of the tb4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb4(Double value) {
        this.tb4 = value;
    }

    /**
     * Gets the value of the frac4 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFrac4() {
        return frac4;
    }

    /**
     * Sets the value of the frac4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFrac4(Double value) {
        this.frac4 = value;
    }

    /**
     * Gets the value of the f5 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getF5() {
        return f5;
    }

    /**
     * Sets the value of the f5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setF5(Double value) {
        this.f5 = value;
    }

    /**
     * Gets the value of the t5 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getT5() {
        return t5;
    }

    /**
     * Sets the value of the t5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setT5(Double value) {
        this.t5 = value;
    }

    /**
     * Gets the value of the tb5 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTb5() {
        return tb5;
    }

    /**
     * Sets the value of the tb5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTb5(Double value) {
        this.tb5 = value;
    }

    /**
     * Gets the value of the frac5 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFrac5() {
        return frac5;
    }

    /**
     * Sets the value of the frac5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFrac5(Double value) {
        this.frac5 = value;
    }

    /**
     * Gets the value of the ttb1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTtb1() {
        return ttb1;
    }

    /**
     * Sets the value of the ttb1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTtb1(Double value) {
        this.ttb1 = value;
    }

    /**
     * Gets the value of the ttb2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTtb2() {
        return ttb2;
    }

    /**
     * Sets the value of the ttb2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTtb2(Double value) {
        this.ttb2 = value;
    }

    /**
     * Gets the value of the fBus1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFBus1() {
        return fBus1;
    }

    /**
     * Sets the value of the fBus1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFBus1(Integer value) {
        this.fBus1 = value;
    }

    /**
     * Gets the value of the tBus1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTBus1() {
        return tBus1;
    }

    /**
     * Sets the value of the tBus1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTBus1(Integer value) {
        this.tBus1 = value;
    }

    /**
     * Gets the value of the id1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID1() {
        return id1;
    }

    /**
     * Sets the value of the id1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID1(String value) {
        this.id1 = value;
    }

    /**
     * Gets the value of the fBus2 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFBus2() {
        return fBus2;
    }

    /**
     * Sets the value of the fBus2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFBus2(Integer value) {
        this.fBus2 = value;
    }

    /**
     * Gets the value of the tBus2 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTBus2() {
        return tBus2;
    }

    /**
     * Sets the value of the tBus2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTBus2(Integer value) {
        this.tBus2 = value;
    }

    /**
     * Gets the value of the id2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID2() {
        return id2;
    }

    /**
     * Sets the value of the id2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID2(String value) {
        this.id2 = value;
    }

    /**
     * Gets the value of the sc property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSC() {
        return sc;
    }

    /**
     * Sets the value of the sc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSC(Integer value) {
        this.sc = value;
    }

}
