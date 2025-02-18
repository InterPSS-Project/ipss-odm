
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		
 * 
 * <p>Java class for OpfNetworkXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OpfNetworkXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BaseOpfNetworkXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anglePenaltyFactor" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpfNetworkXmlType", propOrder = {
    "anglePenaltyFactor"
})
public class OpfNetworkXmlType
    extends BaseOpfNetworkXmlType
{

    protected double anglePenaltyFactor;

    /**
     * Gets the value of the anglePenaltyFactor property.
     * 
     */
    public double getAnglePenaltyFactor() {
        return anglePenaltyFactor;
    }

    /**
     * Sets the value of the anglePenaltyFactor property.
     * 
     */
    public void setAnglePenaltyFactor(double value) {
        this.anglePenaltyFactor = value;
    }

}
