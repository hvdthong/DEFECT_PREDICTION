package org.apache.camel.converter.jaxb;

import org.apache.camel.Message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a JAXB2 representation of a Camel {@link Message} header
 *
 * @version $Revision: 1.1 $
 */
@XmlType(name = "headerType")
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class HeaderType {
    @XmlAttribute
    private String name;

    public HeaderType() {
    }

    protected HeaderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Object getValue();

    public abstract void setValue(Object value);
}
