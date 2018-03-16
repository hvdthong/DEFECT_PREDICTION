package org.apache.camel.model;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessorRef extends OutputType {
    @XmlAttribute(required = true)
    private String ref;
    @XmlTransient
    private Processor processor;

    public ProcessorRef() {
    }

    public ProcessorRef(Processor processor) {
        this.processor = processor;
    }

    @Override
    public String toString() {
        return "Processor[ref:  " + ref + "]";
    }

    @Override
    public String getLabel() {
        if (ref != null) {
            return "ref:  " + ref;
        }
        else if (processor != null) {
            return processor.toString();
        }
        else {
            return "";
        }
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) {
        if (processor == null) {
            processor = routeContext.lookup(getRef(), Processor.class);
        }
        return processor;
    }
}
