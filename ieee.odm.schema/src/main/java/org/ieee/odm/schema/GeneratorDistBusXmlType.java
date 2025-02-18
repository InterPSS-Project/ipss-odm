
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		bus record for loadflow and short circuit study
 * 		
 * 
 * <p>Java class for GeneratorDistBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneratorDistBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}RotatingMachineDistBusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="retedMva" type="{http://www.ieee.org/odm/Schema/2008}ApparentPowerXmlType"/&gt;
 *         &lt;element name="loading" type="{http://www.ieee.org/odm/Schema/2008}FactorXmlType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneratorDistBusXmlType", propOrder = {
    "retedMva",
    "loading"
})
public class GeneratorDistBusXmlType
    extends RotatingMachineDistBusXmlType
{

    @XmlElement(required = true)
    protected ApparentPowerXmlType retedMva;
    @XmlElement(required = true)
    protected FactorXmlType loading;

    /**
     * Gets the value of the retedMva property.
     * 
     * @return
     *     possible object is
     *     {@link ApparentPowerXmlType }
     *     
     */
    public ApparentPowerXmlType getRetedMva() {
        return retedMva;
    }

    /**
     * Sets the value of the retedMva property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApparentPowerXmlType }
     *     
     */
    public void setRetedMva(ApparentPowerXmlType value) {
        this.retedMva = value;
    }

    /**
     * Gets the value of the loading property.
     * 
     * @return
     *     possible object is
     *     {@link FactorXmlType }
     *     
     */
    public FactorXmlType getLoading() {
        return loading;
    }

    /**
     * Sets the value of the loading property.
     * 
     * @param value
     *     allowed object is
     *     {@link FactorXmlType }
     *     
     */
    public void setLoading(FactorXmlType value) {
        this.loading = value;
    }

}
