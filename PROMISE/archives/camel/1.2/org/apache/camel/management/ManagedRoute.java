package org.apache.camel.management;

import java.io.IOException;

import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(
        description="Managed Route", 
        currencyTimeLimit=15)
public class ManagedRoute{
	
	public static final String VALUE_UNKNOWN = "Unknown";
	private Route route;
	private String description;
	
	ManagedRoute(Route route) {
		this.route = route;
		this.description = route.toString();
	}

	public Route getRoute() {
		return route;
	}

	@ManagedAttribute(description = "Route Endpoint Uri")
	public String getEndpointUri() {
		Endpoint ep = route.getEndpoint();
		return ep != null ? ep.getEndpointUri() : VALUE_UNKNOWN;
	}
	
	@ManagedAttribute(description = "Route description")
	public String getDescription() {
		return description;
	}

	@ManagedOperation(description = "Start Route")
    public void start() throws IOException {
		throw new IOException("Not supported");
    }

	@ManagedOperation(description = "Stop Route")
    public void stop() throws IOException {
		throw new IOException("Not supported");
    }
}
