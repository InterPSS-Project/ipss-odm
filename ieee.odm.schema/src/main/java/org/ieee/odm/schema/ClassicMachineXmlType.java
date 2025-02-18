
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClassicMachineXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassicMachineXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}AbstractMachineXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xd1" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ra" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassicMachineXmlType", propOrder = {
    "xd1",
    "ra"
})
public class ClassicMachineXmlType
    extends AbstractMachineXmlType
{

    protected double xd1;
    protected double ra;

    /**
     * Gets the value of the xd1 property.
     * 
     */
    public double getXd1() {
        return xd1;
    }

    /**
     * Sets the value of the xd1 property.
     * 
     */
    public void setXd1(double value) {
        this.xd1 = value;
    }

    /**
     * Gets the value of the ra property.
     * 
     */
    public double getRa() {
        return ra;
    }

    /**
     * Sets the value of the ra property.
     * 
     */
    public void setRa(double value) {
        this.ra = value;
    }

}
