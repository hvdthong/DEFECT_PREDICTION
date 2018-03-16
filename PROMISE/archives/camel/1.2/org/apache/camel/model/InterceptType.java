package org.apache.camel.model;

import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.processor.Interceptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "intercept")
@XmlAccessorType(XmlAccessType.FIELD)
public class InterceptType extends OutputType<ProcessorType> {

    @Override
    public String toString() {
        return "Intercept[" + getOutputs() + "]";
    }

    public void addRoutes(RouteContext routeContext, Collection<Route> routes) throws Exception {
        Interceptor interceptor = new Interceptor();
        routeContext.intercept(interceptor);

        final Processor interceptRoute = routeContext.createProcessor(this);
        interceptor.setInterceptorLogic(interceptRoute);
    }

    /**
     * Applies this interceptor only if the given predicate is true
     */
    public OtherwiseType when(Predicate predicate) {
        return choice().when(PredicateBuilder.not(predicate)).proceed().otherwise();

    }
}
