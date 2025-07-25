//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		
 * 
 * <p>Java class for DStabNetXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DStabNetXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}ShortCircuitNetXmlType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="hasShortCircuitData" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="saturatedMachineParameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DStabNetXmlType", propOrder = {
    "hasShortCircuitData",
    "saturatedMachineParameter"
})
public class DStabNetXmlType
    extends ShortCircuitNetXmlType
{

    protected boolean hasShortCircuitData;
    protected boolean saturatedMachineParameter;

    /**
     * Gets the value of the hasShortCircuitData property.
     * 
     */
    public boolean isHasShortCircuitData() {
        return hasShortCircuitData;
    }

    /**
     * Sets the value of the hasShortCircuitData property.
     * 
     */
    public void setHasShortCircuitData(boolean value) {
        this.hasShortCircuitData = value;
    }

    /**
     * Gets the value of the saturatedMachineParameter property.
     * 
     */
    public boolean isSaturatedMachineParameter() {
        return saturatedMachineParameter;
    }

    /**
     * Sets the value of the saturatedMachineParameter property.
     * 
     */
    public void setSaturatedMachineParameter(boolean value) {
        this.saturatedMachineParameter = value;
    }

}
