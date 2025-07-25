//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.08 at 11:57:40 PM MDT 
//


package org.ieee.odm.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			Each modifyRecord may describe different modification for some purpose. Each ModifyRecord is retrieved 
 * 			using the record id. ModifyRecordXmlType is an abstract type. All modification record types are sub-type
 * 			of ModifyRecordXmlType.
 * 		
 * 
 * <p>Java class for ModifyRecordXmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyRecordXmlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ieee.org/odm/Schema/2008}IDRecordXmlType"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyRecordXmlType")
@XmlSeeAlso({
    NetModificationXmlType.class,
    OverrideOutageScheduleXmlType.class,
    BaseDailyScheduleXmlType.class,
    BranchLossAreaAllocationXmlType.class,
    OutageScheduleXmlType.class,
    DclfContingencySetXmlType.class,
    GenLoadModifyXmlType.class
})
public abstract class ModifyRecordXmlType
    extends IDRecordXmlType
{


}
