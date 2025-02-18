
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			Data structure for defining a sensitivity analysis. It has a type and 1-* injection bus(es), 0-* withdraw bus(es).
 * 		
 * 
 * <p>Java class for DclfSensitivityXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DclfSensitivityXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseRecordXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="senType" type="{http://www.interpss.org/Schema/odm/2008}SenAnalysisEnumType"/&gt;
 *         &lt;element name="injectBusType" type="{http://www.interpss.org/Schema/odm/2008}SenAnalysisBusEnumType"/&gt;
 *         &lt;element name="injectBus" type="{http://www.interpss.org/Schema/odm/2008}SenAnalysisBusXmlType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="withdrawBusType" type="{http://www.interpss.org/Schema/odm/2008}SenAnalysisBusEnumType"/&gt;
 *         &lt;element name="withdrawBus" type="{http://www.interpss.org/Schema/odm/2008}SenAnalysisBusXmlType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="apNodeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userFilename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DclfSensitivityXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "senType",
    "injectBusType",
    "injectBus",
    "withdrawBusType",
    "withdrawBus",
    "apNodeId",
    "userFilename"
})
@XmlSeeAlso({
    GenLossFactorXmlType.class,
    DclfBranchSensitivityXmlType.class
})
public class DclfSensitivityXmlType
    extends BaseRecordXmlType
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SenAnalysisEnumType senType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SenAnalysisBusEnumType injectBusType;
    @XmlElement(required = true)
    protected List<SenAnalysisBusXmlType> injectBus;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SenAnalysisBusEnumType withdrawBusType;
    protected List<SenAnalysisBusXmlType> withdrawBus;
    protected String apNodeId;
    protected String userFilename;

    /**
     * Gets the value of the senType property.
     * 
     * @return
     *     possible object is
     *     {@link SenAnalysisEnumType }
     *     
     */
    public SenAnalysisEnumType getSenType() {
        return senType;
    }

    /**
     * Sets the value of the senType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SenAnalysisEnumType }
     *     
     */
    public void setSenType(SenAnalysisEnumType value) {
        this.senType = value;
    }

    /**
     * Gets the value of the injectBusType property.
     * 
     * @return
     *     possible object is
     *     {@link SenAnalysisBusEnumType }
     *     
     */
    public SenAnalysisBusEnumType getInjectBusType() {
        return injectBusType;
    }

    /**
     * Sets the value of the injectBusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SenAnalysisBusEnumType }
     *     
     */
    public void setInjectBusType(SenAnalysisBusEnumType value) {
        this.injectBusType = value;
    }

    /**
     * Gets the value of the injectBus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the injectBus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInjectBus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SenAnalysisBusXmlType }
     * 
     * 
     */
    public List<SenAnalysisBusXmlType> getInjectBus() {
        if (injectBus == null) {
            injectBus = new ArrayList<SenAnalysisBusXmlType>();
        }
        return this.injectBus;
    }

    /**
     * Gets the value of the withdrawBusType property.
     * 
     * @return
     *     possible object is
     *     {@link SenAnalysisBusEnumType }
     *     
     */
    public SenAnalysisBusEnumType getWithdrawBusType() {
        return withdrawBusType;
    }

    /**
     * Sets the value of the withdrawBusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SenAnalysisBusEnumType }
     *     
     */
    public void setWithdrawBusType(SenAnalysisBusEnumType value) {
        this.withdrawBusType = value;
    }

    /**
     * Gets the value of the withdrawBus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the withdrawBus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWithdrawBus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SenAnalysisBusXmlType }
     * 
     * 
     */
    public List<SenAnalysisBusXmlType> getWithdrawBus() {
        if (withdrawBus == null) {
            withdrawBus = new ArrayList<SenAnalysisBusXmlType>();
        }
        return this.withdrawBus;
    }

    /**
     * Gets the value of the apNodeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApNodeId() {
        return apNodeId;
    }

    /**
     * Sets the value of the apNodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApNodeId(String value) {
        this.apNodeId = value;
    }

    /**
     * Gets the value of the userFilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserFilename() {
        return userFilename;
    }

    /**
     * Sets the value of the userFilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserFilename(String value) {
        this.userFilename = value;
    }

}
