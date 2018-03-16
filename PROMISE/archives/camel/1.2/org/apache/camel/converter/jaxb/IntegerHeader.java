package org.apache.camel.converter.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "intHeader")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class IntegerHeader extends HeaderType {
    @XmlAttribute(name = "value")
    private Integer number;

    public IntegerHeader() {
    }

    public IntegerHeader(String name, Integer number) {
        super(name);
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Object getValue() {
        return getNumber();
    }

    public void setValue(Object value) {
        if (value instanceof Number) {
            Number n = (Number) value;
            setNumber(n.intValue());
        }
        else {
            throw new IllegalArgumentException("Value must be an Integer");
        }
    }
}
