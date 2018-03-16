package org.apache.camel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.model.FromType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;
import org.apache.camel.processor.Interceptor;
import org.apache.camel.processor.Pipeline;
import org.apache.camel.processor.ProceedProcessor;
import org.apache.camel.processor.UnitOfWorkProcessor;

/**
 * The context used to activate new routing rules
 * 
 * @version $Revision: $
 */
public class RouteContext {
    private final RouteType route;
    private final FromType from;
    private final Collection<Route> routes;
    private Endpoint endpoint;
    private List<Processor> eventDrivenProcessors = new ArrayList<Processor>();
    private Interceptor lastInterceptor;

    public RouteContext(RouteType route, FromType from, Collection<Route> routes) {
        this.route = route;
        this.from = from;
        this.routes = routes;
    }

    public Endpoint getEndpoint() {
        if (endpoint == null) {
            endpoint = from.resolveEndpoint(this);
        }
        return endpoint;
    }

    public FromType getFrom() {
        return from;
    }

    public RouteType getRoute() {
        return route;
    }

    public CamelContext getCamelContext() {
        return getRoute().getCamelContext();
    }

    public Processor createProcessor(ProcessorType node) throws Exception {
        return node.createOutputsProcessor(this);
    }

    public Endpoint resolveEndpoint(String uri) {
        return route.resolveEndpoint(uri);
    }

    /**
     * Resolves an endpoint from either a URI or a named reference
     */
    public Endpoint resolveEndpoint(String uri, String ref) {
        Endpoint endpoint = null;
        if (uri != null) {
            endpoint = resolveEndpoint(uri);
            if (endpoint == null) {
                throw new NoSuchEndpointException(uri);
            }
        }
        if (ref != null) {
            endpoint = lookup(ref, Endpoint.class);
            if (endpoint == null) {
                throw new NoSuchEndpointException("ref:" + ref);
            }
        }
        if (endpoint == null) {
            throw new IllegalArgumentException("Either 'uri' or 'ref' must be specified on: " + this);
        } else {
            return endpoint;
        }
    }

    /**
     * lookup an object by name and type
     */
    public <T> T lookup(String name, Class<T> type) {
        return getCamelContext().getRegistry().lookup(name, type);
    }

    /**
     * Lets complete the route creation, creating a single event driven route
     * for the current from endpoint with any processors required
     */
    public void commit() {
        if (!eventDrivenProcessors.isEmpty()) {
            Processor processor = Pipeline.newInstance(eventDrivenProcessors);

            final AsyncProcessor asyncProcessor = AsyncProcessorTypeConverter.convert(processor);
            Processor unitOfWorkProcessor = new UnitOfWorkProcessor(asyncProcessor);

            routes.add(new EventDrivenConsumerRoute(getEndpoint(), unitOfWorkProcessor));
        }
    }

    public void addEventDrivenProcessor(Processor processor) {
        eventDrivenProcessors.add(processor);
    }

    public void intercept(Interceptor interceptor) {
        getRoute().intercept(interceptor);
        lastInterceptor = interceptor;
    }

    public Processor createProceedProcessor() {
        if (lastInterceptor == null) {
            throw new IllegalArgumentException("Cannot proceed() from outside of an interceptor!");
        }
        else {
            return new ProceedProcessor(lastInterceptor);
        }
    }
}
