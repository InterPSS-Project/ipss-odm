
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PssIEE2STXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PssIEE2STXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}StabilizerModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="firstInputSignal" type="{http://www.ieee.org/odm/Schema/2008}StabilizerInputSignalEnumType"/&gt;
 *         &lt;element name="firstRemoteBusId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secondInputSignal" type="{http://www.ieee.org/odm/Schema/2008}StabilizerInputSignalEnumType" minOccurs="0"/&gt;
 *         &lt;element name="secondRemoteBusId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="K1" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="K2" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="T1" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T2" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T3" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T4" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T5" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T6" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T7" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T8" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T9" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="T10" type="{http://www.ieee.org/odm/Schema/2008}TimePeriodXmlType"/&gt;
 *         &lt;element name="VSMAX" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VSMIN" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="VCU" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="VCL" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="VCUTOFF" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PssIEE2STXmlType", propOrder = {
    "firstInputSignal",
    "firstRemoteBusId",
    "secondInputSignal",
    "secondRemoteBusId",
    "k1",
    "k2",
    "t1",
    "t2",
    "t3",
    "t4",
    "t5",
    "t6",
    "t7",
    "t8",
    "t9",
    "t10",
    "vsmax",
    "vsmin",
    "vcu",
    "vcl",
    "vcutoff"
})
public class PssIEE2STXmlType
    extends StabilizerModelXmlType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StabilizerInputSignalEnumType firstInputSignal;
    @XmlElement(required = true)
    protected String firstRemoteBusId;
    @XmlSchemaType(name = "string")
    protected StabilizerInputSignalEnumType secondInputSignal;
    protected String secondRemoteBusId;
    @XmlElement(name = "K1")
    protected double k1;
    @XmlElement(name = "K2")
    protected double k2;
    @XmlElement(name = "T1", required = true)
    protected TimePeriodXmlType t1;
    @XmlElement(name = "T2", required = true)
    protected TimePeriodXmlType t2;
    @XmlElement(name = "T3", required = true)
    protected TimePeriodXmlType t3;
    @XmlElement(name = "T4", required = true)
    protected TimePeriodXmlType t4;
    @XmlElement(name = "T5", required = true)
    protected TimePeriodXmlType t5;
    @XmlElement(name = "T6", required = true)
    protected TimePeriodXmlType t6;
    @XmlElement(name = "T7", required = true)
    protected TimePeriodXmlType t7;
    @XmlElement(name = "T8", required = true)
    protected TimePeriodXmlType t8;
    @XmlElement(name = "T9", required = true)
    protected TimePeriodXmlType t9;
    @XmlElement(name = "T10", required = true)
    protected TimePeriodXmlType t10;
    @XmlElement(name = "VSMAX")
    protected double vsmax;
    @XmlElement(name = "VSMIN")
    protected double vsmin;
    @XmlElement(name = "VCU")
    protected Double vcu;
    @XmlElement(name = "VCL")
    protected Double vcl;
    @XmlElement(name = "VCUTOFF")
    protected Double vcutoff;

    /**
     * Gets the value of the firstInputSignal property.
     * 
     * @return
     *     possible object is
     *     {@link StabilizerInputSignalEnumType }
     *     
     */
    public StabilizerInputSignalEnumType getFirstInputSignal() {
        return firstInputSignal;
    }

    /**
     * Sets the value of the firstInputSignal property.
     * 
     * @param value
     *     allowed object is
     *     {@link StabilizerInputSignalEnumType }
     *     
     */
    public void setFirstInputSignal(StabilizerInputSignalEnumType value) {
        this.firstInputSignal = value;
    }

    /**
     * Gets the value of the firstRemoteBusId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstRemoteBusId() {
        return firstRemoteBusId;
    }

    /**
     * Sets the value of the firstRemoteBusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstRemoteBusId(String value) {
        this.firstRemoteBusId = value;
    }

    /**
     * Gets the value of the secondInputSignal property.
     * 
     * @return
     *     possible object is
     *     {@link StabilizerInputSignalEnumType }
     *     
     */
    public StabilizerInputSignalEnumType getSecondInputSignal() {
        return secondInputSignal;
    }

    /**
     * Sets the value of the secondInputSignal property.
     * 
     * @param value
     *     allowed object is
     *     {@link StabilizerInputSignalEnumType }
     *     
     */
    public void setSecondInputSignal(StabilizerInputSignalEnumType value) {
        this.secondInputSignal = value;
    }

    /**
     * Gets the value of the secondRemoteBusId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondRemoteBusId() {
        return secondRemoteBusId;
    }

    /**
     * Sets the value of the secondRemoteBusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondRemoteBusId(String value) {
        this.secondRemoteBusId = value;
    }

    /**
     * Gets the value of the k1 property.
     * 
     */
    public double getK1() {
        return k1;
    }

    /**
     * Sets the value of the k1 property.
     * 
     */
    public void setK1(double value) {
        this.k1 = value;
    }

    /**
     * Gets the value of the k2 property.
     * 
     */
    public double getK2() {
        return k2;
    }

    /**
     * Sets the value of the k2 property.
     * 
     */
    public void setK2(double value) {
        this.k2 = value;
    }

    /**
     * Gets the value of the t1 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT1() {
        return t1;
    }

    /**
     * Sets the value of the t1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT1(TimePeriodXmlType value) {
        this.t1 = value;
    }

    /**
     * Gets the value of the t2 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT2() {
        return t2;
    }

    /**
     * Sets the value of the t2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT2(TimePeriodXmlType value) {
        this.t2 = value;
    }

    /**
     * Gets the value of the t3 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT3() {
        return t3;
    }

    /**
     * Sets the value of the t3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT3(TimePeriodXmlType value) {
        this.t3 = value;
    }

    /**
     * Gets the value of the t4 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT4() {
        return t4;
    }

    /**
     * Sets the value of the t4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT4(TimePeriodXmlType value) {
        this.t4 = value;
    }

    /**
     * Gets the value of the t5 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT5() {
        return t5;
    }

    /**
     * Sets the value of the t5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT5(TimePeriodXmlType value) {
        this.t5 = value;
    }

    /**
     * Gets the value of the t6 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT6() {
        return t6;
    }

    /**
     * Sets the value of the t6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT6(TimePeriodXmlType value) {
        this.t6 = value;
    }

    /**
     * Gets the value of the t7 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT7() {
        return t7;
    }

    /**
     * Sets the value of the t7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT7(TimePeriodXmlType value) {
        this.t7 = value;
    }

    /**
     * Gets the value of the t8 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT8() {
        return t8;
    }

    /**
     * Sets the value of the t8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT8(TimePeriodXmlType value) {
        this.t8 = value;
    }

    /**
     * Gets the value of the t9 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT9() {
        return t9;
    }

    /**
     * Sets the value of the t9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT9(TimePeriodXmlType value) {
        this.t9 = value;
    }

    /**
     * Gets the value of the t10 property.
     * 
     * @return
     *     possible object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public TimePeriodXmlType getT10() {
        return t10;
    }

    /**
     * Sets the value of the t10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimePeriodXmlType }
     *     
     */
    public void setT10(TimePeriodXmlType value) {
        this.t10 = value;
    }

    /**
     * Gets the value of the vsmax property.
     * 
     */
    public double getVSMAX() {
        return vsmax;
    }

    /**
     * Sets the value of the vsmax property.
     * 
     */
    public void setVSMAX(double value) {
        this.vsmax = value;
    }

    /**
     * Gets the value of the vsmin property.
     * 
     */
    public double getVSMIN() {
        return vsmin;
    }

    /**
     * Sets the value of the vsmin property.
     * 
     */
    public void setVSMIN(double value) {
        this.vsmin = value;
    }

    /**
     * Gets the value of the vcu property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getVCU() {
        return vcu;
    }

    /**
     * Sets the value of the vcu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setVCU(Double value) {
        this.vcu = value;
    }

    /**
     * Gets the value of the vcl property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getVCL() {
        return vcl;
    }

    /**
     * Sets the value of the vcl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setVCL(Double value) {
        this.vcl = value;
    }

    /**
     * Gets the value of the vcutoff property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getVCUTOFF() {
        return vcutoff;
    }

    /**
     * Sets the value of the vcutoff property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setVCUTOFF(Double value) {
        this.vcutoff = value;
    }

}
