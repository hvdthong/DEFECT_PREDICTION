package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.spi.DataFormat;

/**
 * Represents the JAXB2 XML {@link DataFormat}
 *
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "jaxb")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbDataFormat extends DataFormatType {
    @XmlAttribute(required = false)
    private Boolean prettyPrint;

    public JaxbDataFormat() {
        super("org.apache.camel.converter.jaxb.JaxbDataFormat");
    }

    public JaxbDataFormat(boolean prettyPrint) {
        this();
        setPrettyPrint(prettyPrint);
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    @Override
    protected void configureDataFormat(DataFormat dataFormat) {
        if (ObjectConverter.toBool(getPrettyPrint())) {
            setProperty(dataFormat, "prettyPrint", Boolean.TRUE);
        }
    }
}
