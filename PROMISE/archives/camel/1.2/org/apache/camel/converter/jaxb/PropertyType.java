package org.apache.camel.converter.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "property")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PropertyType {
    @XmlAttribute
    private String key;
    @XmlAnyElement(lax = true)
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
