package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;otherwise/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "otherwise")
@XmlAccessorType(XmlAccessType.FIELD)
public class OtherwiseType extends OutputType<ProcessorType> implements Block {

    @Override
    public String toString() {
        return "Otherwise[" + getOutputs() + "]";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return routeContext.createProcessor(this);
    }

    @Override
    public String getShortName() {
        return "otherwise";
    }

    @Override
    public String getLabel() {
        return "otherwise";
    }
}
