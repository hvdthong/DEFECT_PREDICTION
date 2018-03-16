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
 * Represents an XML &lt;removeProperty/&gt; element
 */
@XmlRootElement(name = "removeProperty")
@XmlAccessorType(XmlAccessType.FIELD)
public class RemovePropertyType extends OutputType<ProcessorType> {
    @XmlAttribute(required = true)
    private String propertyName;   
    
    public RemovePropertyType() {
    }
    
    public RemovePropertyType(String propertyName) {
        setPropertyName(propertyName);
    }
    
    @Override
    public String toString() {
        return "RemoveProperty[" + getPropertyName() + "]";
    }

    @Override
    public String getShortName() {
        return "removeProperty";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {        
        ObjectHelper.notNull(getPropertyName(), "propertyName");
        return ProcessorBuilder.removeProperty(getPropertyName());
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
