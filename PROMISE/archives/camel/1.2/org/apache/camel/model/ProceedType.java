package org.apache.camel.model;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import java.util.Collections;
import java.util.List;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "proceed")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProceedType extends ProcessorType {

    public List<ProcessorType> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    public List<InterceptorType> getInterceptors() {
        return Collections.EMPTY_LIST;
    }

    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return routeContext.createProceedProcessor();
    }
}
