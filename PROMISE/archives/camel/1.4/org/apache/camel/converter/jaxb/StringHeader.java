package org.apache.camel.converter.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version $Revision: 640731 $
 */
@XmlRootElement(name = "header")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StringHeader extends HeaderType {
    @XmlAttribute(name = "value", required = true)
    private String text;

    public StringHeader() {
    }

    public StringHeader(String name, String text) {
        super(name);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getValue() {
        return getText();
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            setText((String) value);
        } else {
            throw new IllegalArgumentException("Value must be a String");
        }
    }
}
