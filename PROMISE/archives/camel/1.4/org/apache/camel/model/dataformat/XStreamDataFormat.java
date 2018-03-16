package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;

/**
 * Represents the XStream XML {@link DataFormat}
 *
 * @version $Revision: 640438 $
 */
@XmlRootElement(name = "xstream")
@XmlAccessorType(XmlAccessType.FIELD)
public class XStreamDataFormat extends DataFormatType {
    @XmlAttribute(required = false)
    private Boolean prettyPrint;

    public XStreamDataFormat() {
        super("org.apache.camel.dataformat.xstream.XStreamDataFormat");
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
}
