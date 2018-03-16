package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;

/**
 * Represents the XMLBeans XML {@link DataFormat}
 *
 * @version $Revision: 640438 $
 */
@XmlRootElement(name = "xmlBeans")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLBeansDataFormat extends DataFormatType {
    @XmlAttribute(required = false)
    private Boolean prettyPrint;

    public XMLBeansDataFormat() {
        super("org.apache.camel.converter.xmlbeans.XmlBeansDataFormat");
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
}
