package org.apache.camel.impl;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.model.RouteType;
import org.apache.camel.spi.LifecycleStrategy;
import org.apache.camel.Route;
import org.apache.camel.Service;

public class DefaultLifecycleStrategy implements LifecycleStrategy {

	public void onContextCreate(CamelContext context) {
	}
	
	public void onEndpointAdd(Endpoint endpoint) {
	}

	public void onServiceAdd(CamelContext context, Service service) {
	}

	public void onRoutesAdd(Collection<Route> routes) {
	}

	public void beforeStartRouteType(CamelContext context, RouteType routeType) {
	}
}
