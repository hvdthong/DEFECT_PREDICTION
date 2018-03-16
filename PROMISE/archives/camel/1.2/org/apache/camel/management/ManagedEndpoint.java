package org.apache.camel.management;

import org.apache.camel.Endpoint;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(
        description="Managed Endpoint", 
        currencyTimeLimit=15)
public class ManagedEndpoint {
	
	private Endpoint endpoint;

	public ManagedEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	@ManagedAttribute(description = "Endpoint Uri")
	public String getUri() throws Exception {
		return endpoint.getEndpointUri();
	}
}
