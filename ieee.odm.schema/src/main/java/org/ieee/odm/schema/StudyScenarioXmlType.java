
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         Base Study scenario type for extension
 *         
 * 
 * <p>Java class for StudyScenarioXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudyScenarioXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}IDRecordXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="modifcation" type="{http://www.ieee.org/odm/Schema/2008}ModifyRecordXmlType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudyScenarioXmlType", propOrder = {
    "modifcation"
})
@XmlSeeAlso({
    IpssStudyScenarioXmlType.class
})
public abstract class StudyScenarioXmlType
    extends IDRecordXmlType
{

    protected ModifyRecordXmlType modifcation;

    /**
     * Gets the value of the modifcation property.
     * 
     * @return
     *     possible object is
     *     {@link ModifyRecordXmlType }
     *     
     */
    public ModifyRecordXmlType getModifcation() {
        return modifcation;
    }

    /**
     * Sets the value of the modifcation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyRecordXmlType }
     *     
     */
    public void setModifcation(ModifyRecordXmlType value) {
        this.modifcation = value;
    }

}
