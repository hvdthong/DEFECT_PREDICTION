package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.Node;

import org.apache.camel.spi.DataFormat;


/**
 * Represents a wellformed HTML document (XML well Formed) {@link DataFormat}
 *
 */
@XmlRootElement(name = "tidyMarkup")
@XmlAccessorType(XmlAccessType.FIELD)
public class TidyMarkupDataFormat extends DataFormatType {

    @XmlAttribute(required = false)
    private Class<?> dataObjectType;

    public TidyMarkupDataFormat() {
        super("org.apache.camel.dataformat.tagsoup.TidyMarkupDataFormat");
        this.setDataObjectType(Node.class);
    }

    public TidyMarkupDataFormat(Class<?> dataObjectType) {
        this();
        if (!dataObjectType.isAssignableFrom(String.class) && !dataObjectType.isAssignableFrom(Node.class)) {
            throw new IllegalArgumentException("TidyMarkupDataFormat only supports returning a String or a org.w3c.dom.Node object");
        }
        this.setDataObjectType(dataObjectType);
    }

    public void setDataObjectType(Class<?> dataObjectType) {
        this.dataObjectType = dataObjectType;
    }

    public Class<?> getDataObjectType() {
        return dataObjectType;
    }


    @Override
    protected void configureDataFormat(DataFormat dataFormat) {
        Class<?> type = getDataObjectType();
        if (type != null) {
            setProperty(dataFormat, "dataObjectType", type);
        }
    }

}
