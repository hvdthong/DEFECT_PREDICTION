package org.apache.camel.spi;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.Service;

/**
 * Strategy for notifications
 */
public interface LifecycleStrategy {
    /**
     * Notification on starting a {@link CamelContext}.
     */
    void onContextStart(CamelContext context);

    /**
     * Notification on adding an {@link Endpoint}.
     */
    void onEndpointAdd(Endpoint<? extends Exchange> endpoint);

    /**
     * Notification on adding a {@link Service}.
     */
    void onServiceAdd(CamelContext context, Service service);

    /**
     * Notification on adding {@link Route}(s).
     */
    void onRoutesAdd(Collection<Route> routes);

    /**
     * Notification on adding {@link RouteContext}(s).
     *
     * @param routeContext
     */
    void onRouteContextCreate(RouteContext routeContext);
}
