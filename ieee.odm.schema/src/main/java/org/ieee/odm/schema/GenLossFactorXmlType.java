
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			Data structure for defining generator loss factor calculation.
 * 		
 * 
 * <p>Java class for GenLossFactorXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenLossFactorXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.interpss.org/Schema/odm/2008}DclfSensitivityXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lossFactor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenLossFactorXmlType", namespace = "http://www.interpss.org/Schema/odm/2008", propOrder = {
    "lossFactor"
})
public class GenLossFactorXmlType
    extends DclfSensitivityXmlType
{

    protected Double lossFactor;

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
