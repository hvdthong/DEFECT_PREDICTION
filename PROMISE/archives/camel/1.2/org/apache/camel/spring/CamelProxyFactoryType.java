package org.apache.camel.spring;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.model.IdentifiedType;

/**
 * @version $Revision: $
@XmlRootElement(name = "proxy")
public class CamelProxyFactoryType extends IdentifiedType {
    @XmlAttribute
    private String serviceUrl;
    @XmlAttribute
    private Class serviceInterface;

}
