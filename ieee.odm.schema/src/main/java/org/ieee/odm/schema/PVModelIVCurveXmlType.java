
package org.ieee.odm.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			I/V curve based PV model
 * 		
 * 
 * <p>Java class for PVModelIVCurveXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PVModelIVCurveXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BasePVModelXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ivPoint" type="{http://www.ieee.org/odm/Schema/2008}PVModelPointXmlType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PVModelIVCurveXmlType", propOrder = {
    "ivPoint"
})
public class PVModelIVCurveXmlType
    extends BasePVModelXmlType
{

    @XmlElement(required = true)
    protected List<PVModelPointXmlType> ivPoint;

    /**
     * Gets the value of the ivPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ivPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIvPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PVModelPointXmlType }
     * 
     * 
     */
    public List<PVModelPointXmlType> getIvPoint() {
        if (ivPoint == null) {
            ivPoint = new ArrayList<PVModelPointXmlType>();
        }
        return this.ivPoint;
    }

}
