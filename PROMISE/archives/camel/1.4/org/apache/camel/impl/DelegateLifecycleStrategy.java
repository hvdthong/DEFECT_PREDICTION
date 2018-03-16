package org.apache.camel.impl;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.Service;
import org.apache.camel.spi.LifecycleStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * A helper class for folks writing delegate listener strategies
 *
 * @version $Revision: 669756 $
 */
public class DelegateLifecycleStrategy implements LifecycleStrategy {
    private final LifecycleStrategy delegate;

    public DelegateLifecycleStrategy(LifecycleStrategy delegate) {
        this.delegate = delegate;
    }

    public void onContextStart(CamelContext context) {
        delegate.onContextStart(context);
    }

    public void onEndpointAdd(Endpoint<? extends Exchange> endpoint) {
        delegate.onEndpointAdd(endpoint);
    }

    public void onRouteContextCreate(RouteContext routeContext) {
        delegate.onRouteContextCreate(routeContext);
    }

    public void onRoutesAdd(Collection<Route> routes) {
        delegate.onRoutesAdd(routes);
    }

    public void onServiceAdd(CamelContext context, Service service) {
        delegate.onServiceAdd(context, service);
    }
}
