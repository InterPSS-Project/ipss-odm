
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SteamTurbineBPATBModelXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SteamTurbineBPATBModelXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}SteamTurbineTCSRXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Lambda" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SteamTurbineBPATBModelXmlType", propOrder = {
    "lambda"
})
public class SteamTurbineBPATBModelXmlType
    extends SteamTurbineTCSRXmlType
{

    @XmlElement(name = "Lambda")
    protected double lambda;

    /**
     * Gets the value of the lambda property.
     * 
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * Sets the value of the lambda property.
     * 
     */
    public void setLambda(double value) {
        this.lambda = value;
    }

}
