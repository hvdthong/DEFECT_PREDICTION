package org.apache.camel.management;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Managed Endpoint", currencyTimeLimit = 15)
public class ManagedEndpoint {

    private Endpoint<? extends Exchange> endpoint;

    public ManagedEndpoint(Endpoint<? extends Exchange> endpoint) {
        this.endpoint = endpoint;
    }

    public Endpoint<? extends Exchange> getEndpoint() {
        return endpoint;
    }

    @ManagedAttribute(description = "Endpoint Uri")
    public String getUri() throws Exception {
        return endpoint.getEndpointUri();
    }

    @ManagedAttribute(description = "Singleton")
    public boolean getSingleton() throws Exception {
        return endpoint.isSingleton();
    }
}
