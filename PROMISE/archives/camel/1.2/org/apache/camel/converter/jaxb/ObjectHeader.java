package org.apache.camel.converter.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "objectHeader")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ObjectHeader extends HeaderType {
    @XmlAnyElement(lax = true)
    private Object value;

    public ObjectHeader() {
    }

    public ObjectHeader(String name, Object value) {
        super(name);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
