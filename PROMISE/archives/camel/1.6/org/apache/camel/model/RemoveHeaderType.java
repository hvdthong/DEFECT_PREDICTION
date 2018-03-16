package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.builder.ProcessorBuilder;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;removeHeader/&gt; element
 */
@XmlRootElement(name = "removeHeader")
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveHeaderType extends OutputType<ProcessorType> {
    @XmlAttribute(required = true)
    private String headerName;   
    
    public RemoveHeaderType() {
    }
    
    public RemoveHeaderType(String headerName) {
        setHeaderName(headerName);
    }
    
    @Override
    public String toString() {
        return "RemoveHeader[" + getHeaderName() + "]";
    }

    @Override
    public String getShortName() {
        return "removeHeader";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {        
        ObjectHelper.notNull(getHeaderName(), "headerName");
        return ProcessorBuilder.removeHeader(getHeaderName());
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
