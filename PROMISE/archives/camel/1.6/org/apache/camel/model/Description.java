package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Represents a description element which can be used on any element in the EIP route
 *
 * @version $Revision: 662301 $
 */
@XmlRootElement(name = "description")
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {
    @XmlAttribute(required = false)
    private String lang;

    @XmlValue
    private String text;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
