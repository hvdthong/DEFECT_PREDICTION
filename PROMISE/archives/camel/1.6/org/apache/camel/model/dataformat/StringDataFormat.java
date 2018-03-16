package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;

/**
 * Represents the String (text based) {@link DataFormat}
 *
 * @version $Revision: 659007 $
 */
@XmlRootElement(name = "string")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringDataFormat extends DataFormatType {

    @XmlAttribute(required = false)
    private String charset;

    @Override
    protected DataFormat createDataFormat(RouteContext routeContext) {
        return new org.apache.camel.impl.StringDataFormat(charset);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
