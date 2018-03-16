package org.apache.camel.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.processor.ConvertBodyProcessor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;convertBodyTo/&gt; element
 */
@XmlRootElement(name = "convertBodyTo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConvertBodyType extends ProcessorType<ProcessorType> {
    @XmlAttribute
    private String type;
    @XmlTransient
    private Class typeClass;

    public ConvertBodyType() {
    }

    public ConvertBodyType(String type) {
        setType(type);
    }

    public ConvertBodyType(Class typeClass) {
        setTypeClass(typeClass);
        setType(typeClass.getName());
    }

    @Override
    public String toString() {        
        return "convertBodyTo[" + getType() + "]";
    }

    @Override
    public String getShortName() {
        return "convertBodyTo";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return new ConvertBodyProcessor(getTypeClass());
    }

    @Override
    public List<ProcessorType<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    protected Class createTypeClass() {
        return ObjectHelper.loadClass(getType(), getClass().getClassLoader());
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTypeClass(Class typeClass) {
        this.typeClass = typeClass;
    }

    public Class getTypeClass() {
        if (typeClass == null) {
            Class clazz = createTypeClass();
            if (clazz == null) {
                throw new RuntimeCamelException("Can't load the class with the class name: " + getType());
            }
            setTypeClass(clazz);
        }
        return typeClass;
    }
}
