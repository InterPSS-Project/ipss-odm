
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XformerZTableXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XformerZTableXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="XformerZTableItem" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="lookup" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="turnRatioShiftAngle" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="scaleFactor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="adjustSide" type="{http://www.ieee.org/odm/Schema/2008}BranchBusSideEnumType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XformerZTableXmlType", propOrder = {
    "xformerZTableItem",
    "adjustSide"
})
public class XformerZTableXmlType
    extends NameTagXmlType
{

    @XmlElement(name = "XformerZTableItem")
    protected List<XformerZTableXmlType.XformerZTableItem> xformerZTableItem;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BranchBusSideEnumType adjustSide;

    /**
     * Gets the value of the xformerZTableItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xformerZTableItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXformerZTableItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XformerZTableXmlType.XformerZTableItem }
     * 
     * 
     */
    public List<XformerZTableXmlType.XformerZTableItem> getXformerZTableItem() {
        if (xformerZTableItem == null) {
            xformerZTableItem = new ArrayList<XformerZTableXmlType.XformerZTableItem>();
        }
        return this.xformerZTableItem;
    }

    /**
     * Gets the value of the adjustSide property.
     * 
     * @return
     *     possible object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public BranchBusSideEnumType getAdjustSide() {
        return adjustSide;
    }

    /**
     * Sets the value of the adjustSide property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchBusSideEnumType }
     *     
     */
    public void setAdjustSide(BranchBusSideEnumType value) {
        this.adjustSide = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}NameTagXmlType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="lookup" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="turnRatioShiftAngle" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="scaleFactor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "number",
        "lookup"
    })
    public static class XformerZTableItem
        extends NameTagXmlType
    {

        protected int number;
        protected List<XformerZTableXmlType.XformerZTableItem.Lookup> lookup;

        /**
         * Gets the value of the number property.
         * 
         */
        public int getNumber() {
            return number;
        }

        /**
         * Sets the value of the number property.
         * 
         */
        public void setNumber(int value) {
            this.number = value;
        }

        /**
         * Gets the value of the lookup property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the lookup property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLookup().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link XformerZTableXmlType.XformerZTableItem.Lookup }
         * 
         * 
         */
        public List<XformerZTableXmlType.XformerZTableItem.Lookup> getLookup() {
            if (lookup == null) {
                lookup = new ArrayList<XformerZTableXmlType.XformerZTableItem.Lookup>();
            }
            return this.lookup;
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
         *         &lt;element name="turnRatioShiftAngle" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="scaleFactor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
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
            "turnRatioShiftAngle",
            "scaleFactor"
        })
        public static class Lookup {

            protected double turnRatioShiftAngle;
            protected double scaleFactor;

            /**
             * Gets the value of the turnRatioShiftAngle property.
             * 
             */
            public double getTurnRatioShiftAngle() {
                return turnRatioShiftAngle;
            }

            /**
             * Sets the value of the turnRatioShiftAngle property.
             * 
             */
            public void setTurnRatioShiftAngle(double value) {
                this.turnRatioShiftAngle = value;
            }

            /**
             * Gets the value of the scaleFactor property.
             * 
             */
            public double getScaleFactor() {
                return scaleFactor;
            }

            /**
             * Sets the value of the scaleFactor property.
             * 
             */
            public void setScaleFactor(double value) {
                this.scaleFactor = value;
            }

        }

    }

}
