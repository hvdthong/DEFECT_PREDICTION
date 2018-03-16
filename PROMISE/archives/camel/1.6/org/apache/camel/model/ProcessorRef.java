package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;process/&gt; element
 *
 * @version $Revision: 699876 $
 */
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessorRef extends OutputType<ProcessorType> {
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
    public String getShortName() {
        return "process";
    }

    @Override
    public String toString() {
        return "process["
                + ((ref != null) ? "ref: " + ref : processor)
                + "]";
    }

    @Override
    public String getLabel() {
        if (ref != null) {
            return "ref: " + ref;
        } else if (processor != null) {
            return processor.toString();
        } else {
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
