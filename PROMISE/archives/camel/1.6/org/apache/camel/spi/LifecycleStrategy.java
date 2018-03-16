package org.apache.camel.spi;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.Service;

/**
 * Strategy for lifecycle notifications.
 */
public interface LifecycleStrategy {

    /**
     * Notification on starting a {@link CamelContext}.
     *
     * @param context the camel context
     */
    void onContextStart(CamelContext context);

    /**
     * Notification on adding an {@link Endpoint}.
     *
     * @param endpoint the added endpoint
     */
    void onEndpointAdd(Endpoint<? extends Exchange> endpoint);

    /**
     * Notification on adding a {@link Service}.
     *
     * @param context the camel context
     * @param service the added service
     */
    void onServiceAdd(CamelContext context, Service service);

    /**
     * Notification on adding {@link Route}(s).
     *
     * @param routes the added routes
     */
    void onRoutesAdd(Collection<Route> routes);

    /**
     * Notification on adding {@link RouteContext}(s).
     *
     * @param routeContext the added route context
     */
    void onRouteContextCreate(RouteContext routeContext);
}
