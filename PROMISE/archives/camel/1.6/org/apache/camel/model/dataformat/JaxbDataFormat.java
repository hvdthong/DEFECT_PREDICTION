package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents the JAXB2 XML {@link DataFormat}
 *
 * @version $Revision: 720208 $
 */
@XmlRootElement(name = "jaxb")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbDataFormat extends DataFormatType {
    @XmlAttribute(required = false)
    private String contextPath;
    @XmlAttribute(required = false)
    private Boolean prettyPrint;
    @XmlAttribute(required = false)
    private Boolean ignoreJAXBElement;

    public JaxbDataFormat() {
        super("org.apache.camel.converter.jaxb.JaxbDataFormat");
    }

    public JaxbDataFormat(boolean prettyPrint) {
        this();
        setPrettyPrint(prettyPrint);
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
    
    public Boolean getIgnoreJAXBElement() {
        return ignoreJAXBElement;
    }
    
    public void setIgnoreJAXBElement(Boolean ignoreJAXBElement) {
        this.ignoreJAXBElement = ignoreJAXBElement;
    }
    
    @Override
    protected void configureDataFormat(DataFormat dataFormat) {
        Boolean answer = ObjectHelper.toBoolean(getPrettyPrint());
        if (answer != null && !answer.booleanValue()) {
            setProperty(dataFormat, "prettyPrint", Boolean.FALSE);
            setProperty(dataFormat, "prettyPrint", Boolean.TRUE);
        }
        answer = ObjectHelper.toBoolean(getIgnoreJAXBElement());
        if (answer != null && !answer.booleanValue()) {
            setProperty(dataFormat, "ignoreJAXBElement", Boolean.FALSE);
            setProperty(dataFormat, "ignoreJAXBElement", Boolean.TRUE);
        } 
        setProperty(dataFormat, "contextPath", contextPath);
    }
}
