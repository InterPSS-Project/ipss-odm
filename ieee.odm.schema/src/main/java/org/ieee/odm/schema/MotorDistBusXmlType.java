
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		bus record for loadflow and short circuit study
 * 		
 * 
 * <p>Java class for MotorDistBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MotorDistBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}RotatingMachineDistBusXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ratedPower" type="{http://www.ieee.org/odm/Schema/2008}ActivePowerXmlType"/&gt;
 *         &lt;element name="efficiency" type="{http://www.ieee.org/odm/Schema/2008}FactorXmlType"/&gt;
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
@XmlType(name = "MotorDistBusXmlType", propOrder = {
    "ratedPower",
    "efficiency",
    "loading"
})
@XmlSeeAlso({
    SynchronousMotorDistBusXmlType.class,
    InductionMotorDistBusXmlType.class
})
public abstract class MotorDistBusXmlType
    extends RotatingMachineDistBusXmlType
{

    @XmlElement(required = true)
    protected ActivePowerXmlType ratedPower;
    @XmlElement(required = true)
    protected FactorXmlType efficiency;
    @XmlElement(required = true)
    protected FactorXmlType loading;

    /**
     * Gets the value of the ratedPower property.
     * 
     * @return
     *     possible object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public ActivePowerXmlType getRatedPower() {
        return ratedPower;
    }

    /**
     * Sets the value of the ratedPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivePowerXmlType }
     *     
     */
    public void setRatedPower(ActivePowerXmlType value) {
        this.ratedPower = value;
    }

    /**
     * Gets the value of the efficiency property.
     * 
     * @return
     *     possible object is
     *     {@link FactorXmlType }
     *     
     */
    public FactorXmlType getEfficiency() {
        return efficiency;
    }

    /**
     * Sets the value of the efficiency property.
     * 
     * @param value
     *     allowed object is
     *     {@link FactorXmlType }
     *     
     */
    public void setEfficiency(FactorXmlType value) {
        this.efficiency = value;
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
