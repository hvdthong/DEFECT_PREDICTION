package org.apache.camel.spring;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.model.IdentifiedType;

/**
 * The &lt;proxy&gt; tag element.
 *
 * @version $Revision: 673477 $
@XmlRootElement(name = "proxy")
public class CamelProxyFactoryType extends IdentifiedType {
    @XmlAttribute
    private String serviceUrl;
    @XmlAttribute
    private Class serviceInterface;

}
