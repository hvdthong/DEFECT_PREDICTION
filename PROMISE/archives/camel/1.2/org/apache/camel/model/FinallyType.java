package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "finally")
@XmlAccessorType(XmlAccessType.FIELD)
public class FinallyType extends OutputType {
    @Override
    public String toString() {
        return "Finally[" + getOutputs() + "]";
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return routeContext.createProcessor(this);
    }
}
