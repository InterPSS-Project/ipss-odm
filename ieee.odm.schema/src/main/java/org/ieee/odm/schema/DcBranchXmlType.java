
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		
 * 
 * <p>Java class for DcBranchXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DcBranchXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseBranchXmlType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="feeder" type="{http://www.ieee.org/odm/Schema/2008}DcFeederXmlType" minOccurs="0"/&gt;
 *           &lt;element name="homeRun" type="{http://www.ieee.org/odm/Schema/2008}DcFeederXmlType" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DcBranchXmlType", propOrder = {
    "feeder",
    "homeRun"
})
public class DcBranchXmlType
    extends BaseBranchXmlType
{

    protected DcFeederXmlType feeder;
    protected DcFeederXmlType homeRun;

    /**
     * Gets the value of the feeder property.
     * 
     * @return
     *     possible object is
     *     {@link DcFeederXmlType }
     *     
     */
    public DcFeederXmlType getFeeder() {
        return feeder;
    }

    /**
     * Sets the value of the feeder property.
     * 
     * @param value
     *     allowed object is
     *     {@link DcFeederXmlType }
     *     
     */
    public void setFeeder(DcFeederXmlType value) {
        this.feeder = value;
    }

    /**
     * Gets the value of the homeRun property.
     * 
     * @return
     *     possible object is
     *     {@link DcFeederXmlType }
     *     
     */
    public DcFeederXmlType getHomeRun() {
        return homeRun;
    }

    /**
     * Sets the value of the homeRun property.
     * 
     * @param value
     *     allowed object is
     *     {@link DcFeederXmlType }
     *     
     */
    public void setHomeRun(DcFeederXmlType value) {
        this.homeRun = value;
    }

}
