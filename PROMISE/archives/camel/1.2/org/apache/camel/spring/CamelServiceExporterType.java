package org.apache.camel.spring;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.model.IdentifiedType;

/**
 * @version $Revision: $
*/
@XmlRootElement(name = "export")
public class CamelServiceExporterType extends IdentifiedType {
    @XmlAttribute
    private String uri;
    @XmlAttribute
    private String serviceRef;
    @XmlAttribute
    private Class serviceInterface;
}
