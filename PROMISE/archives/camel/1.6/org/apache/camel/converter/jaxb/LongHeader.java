package org.apache.camel.converter.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version $Revision: 640731 $
 */
@XmlRootElement(name = "longHeader")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class LongHeader extends HeaderType {
    @XmlAttribute(name = "value")
    private Long number;

    public LongHeader() {
    }

    public LongHeader(String name, Long number) {
        super(name);
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Object getValue() {
        return getNumber();
    }

    public void setValue(Object value) {
        if (value instanceof Number) {
            Number n = (Number)value;
            setNumber(n.longValue());
        } else {
            throw new IllegalArgumentException("Value must be a Long");
        }
    }
}
