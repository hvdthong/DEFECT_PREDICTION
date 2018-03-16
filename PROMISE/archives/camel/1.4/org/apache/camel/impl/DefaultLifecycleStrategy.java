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
 * Default implementation of the lifecycle strategy.
 */
public class DefaultLifecycleStrategy implements LifecycleStrategy {

    public void onContextStart(CamelContext context) {
    }

    public void onEndpointAdd(Endpoint<? extends Exchange> endpoint) {
    }

    public void onServiceAdd(CamelContext context, Service service) {
    }

    public void onRoutesAdd(Collection<Route> routes) {
    }

    public void onRouteContextCreate(RouteContext routeContext) {
    }
}
