package org.apache.camel.spi;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.apache.camel.Service;
import org.apache.camel.model.RouteType;

public interface LifecycleStrategy {

	/**
     * Notification on adding a {@see CamelContext}.
     */
	void onContextCreate(CamelContext context);
	
	/**
     * Notification on adding an {@see Endpoint}.
     */
	void onEndpointAdd(Endpoint endpoint);

	/**
     * Notification on adding a {@see Service}.
     */
	void onServiceAdd(CamelContext context, Service service);
	
	/**
     * Notification on adding {@see Route}(s).
     */
	void onRoutesAdd(Collection<Route> routes);
	
	/**
     * Notification on adding {@see Route}(s).
	 * @param context TODO
     */
	void beforeStartRouteType(CamelContext context, RouteType routeType);
}
