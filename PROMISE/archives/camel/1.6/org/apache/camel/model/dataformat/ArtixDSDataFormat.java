package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link DataFormat}
 *
 * @version $Revision: 640438 $
 */
@XmlRootElement(name = "artixDS")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArtixDSDataFormat extends DataFormatType {
    private static final transient Log LOG = LogFactory.getLog(ArtixDSDataFormat.class);

    @XmlAttribute(required = false)
    private String elementTypeName;
    @XmlAttribute(required = false)
    private String format;
    @XmlAttribute(required = false)
    private Class<?> elementType;
    @XmlAttribute(required = false)
    private ArtixDSContentType contentType;

    public ArtixDSDataFormat() {
        super("org.apache.camel.artix.ds.ArtixDSFormat");
    }

    public ArtixDSDataFormat(Class<?> elementType) {
        this();
        this.elementType = elementType;
    }

    public ArtixDSDataFormat(Class<?> elementType, ArtixDSContentType contentType) {
        this();
        this.elementType = elementType;
        this.contentType = contentType;
    }

    public ArtixDSDataFormat(ArtixDSContentType contentType) {
        this();
        this.contentType = contentType;
    }


    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public ArtixDSContentType getContentType() {
        return contentType;
    }

    public void setContentType(ArtixDSContentType contentType) {
        this.contentType = contentType;
    }

    public Class<?> getElementType() {
        if (elementType == null) {
            if (elementTypeName != null) {
                elementType = ObjectHelper.loadClass(elementTypeName, getClass().getClassLoader());
                if (elementType == null) {
                    throw new IllegalArgumentException("The ArtixDS Element class " + elementTypeName + " is not on the classpath! Cannot use the dataFormat " + this);
                }
            }
        }
        return elementType;
    }

    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


    @Override
    protected void configureDataFormat(DataFormat dataFormat) {
        Class<?> type = getElementType();
        if (type != null) {
            setProperty(dataFormat, "elementType", type);
        }
        ArtixDSContentType content = getContentType();
        if (content != null) {
            setProperty(dataFormat, "contentType", content);
        }
    }
}
