package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "otherwise")
@XmlAccessorType(XmlAccessType.FIELD)
public class OtherwiseType extends OutputType {

    @Override
    public String toString() {
        return "Otherwise[" + getOutputs() + "]";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return routeContext.createProcessor(this);
    }

    @Override
    public String getLabel() {
        return "otherwise";
    }
}
