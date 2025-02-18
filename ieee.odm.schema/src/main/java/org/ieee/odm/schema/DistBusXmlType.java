
package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		bus record for loadflow and short circuit study
 * 		
 * 
 * <p>Java class for DistBusXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistBusXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}BusXmlType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistBusXmlType")
@XmlSeeAlso({
    UtilityDistBusXmlType.class,
    NonContributingDistBusXmlType.class,
    RotatingMachineDistBusXmlType.class
})
public abstract class DistBusXmlType
    extends BusXmlType
{


}
