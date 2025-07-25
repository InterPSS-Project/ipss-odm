//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransformerZCorrectionXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransformerZCorrectionXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="correction" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="tap" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="factor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformerZCorrectionXmlType", propOrder = {
    "correction"
})
public class TransformerZCorrectionXmlType
    extends NameTagXmlType
{

    protected List<TransformerZCorrectionXmlType.Correction> correction;
    @XmlAttribute(name = "number")
    protected Integer number;

    /**
     * Gets the value of the correction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransformerZCorrectionXmlType.Correction }
     * 
     * 
     */
    public List<TransformerZCorrectionXmlType.Correction> getCorrection() {
        if (correction == null) {
            correction = new ArrayList<TransformerZCorrectionXmlType.Correction>();
        }
        return this.correction;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumber(Integer value) {
        this.number = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="tap" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="factor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tap",
        "factor"
    })
    public static class Correction {

        protected double tap;
        protected double factor;

        /**
         * Gets the value of the tap property.
         * 
         */
        public double getTap() {
            return tap;
        }

        /**
         * Sets the value of the tap property.
         * 
         */
        public void setTap(double value) {
            this.tap = value;
        }

        /**
         * Gets the value of the factor property.
         * 
         */
        public double getFactor() {
            return factor;
        }

        /**
         * Sets the value of the factor property.
         * 
         */
        public void setFactor(double value) {
            this.factor = value;
        }

    }

}
