package org.apache.camel.converter.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version $Revision: 640731 $
 */
@XmlRootElement(name = "exchange")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ExchangeType {
    @XmlElement(name = "property", required = false)
    List<PropertyType> properties = new ArrayList<PropertyType>();
    @XmlAnyElement(lax = true)
    private Object body;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public List<PropertyType> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyType> properties) {
        this.properties = properties;
    }
}
