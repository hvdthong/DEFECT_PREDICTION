package org.apache.camel.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.CamelException;
import org.apache.camel.Processor;
import org.apache.camel.processor.ThrowFaultProcessor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;throwFault/&gt; element
 */
@XmlRootElement(name = "throwFault")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrowFaultType extends ProcessorType<ThrowFaultType> {
    @XmlTransient
    private Throwable fault;
    @XmlTransient
    private Processor processor;
    @XmlAttribute (required = true)
    private String faultRef;

    public ThrowFaultType() {
    }

    @Override
    public String getShortName() {
        return "throwFault";
    }

    public void setFault(Throwable fault) {
        this.fault = fault;
    }

    public Throwable getFault() {
        return fault;
    }

    public void setFaultRef(String ref) {
        this.faultRef = ref;
    }

    public String getFaultRef() {
        return faultRef;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) {

        if (processor == null) {
            if (fault == null) {
                fault = routeContext.lookup(faultRef, Throwable.class);
                if (fault == null) {
                    fault = new CamelException(faultRef);
                }
            }
            processor = new ThrowFaultProcessor(fault);
        }
        return processor;
    }

    @Override
    public List<ProcessorType<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }
}
